package com.google.android.voicesearch;

import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ConcurrentUtils;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.shared.util.Util;
import com.google.android.speech.Recognizer;
import com.google.android.speech.RecognizerImpl;
import com.google.android.speech.SpeechLibFactory;
import com.google.android.speech.audio.AudioController;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.audio.SingleRecordingAudioStore;
import com.google.android.speech.embedded.Greco3Container;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.internal.DefaultCallbackFactory;
import com.google.android.speech.internal.DefaultModeSelector;
import com.google.android.speech.logger.SuggestionLogger;
import com.google.android.speech.network.ConnectionFactory;
import com.google.android.speech.params.RecognitionEngineParams;
import com.google.android.voicesearch.audio.AudioRouter;
import com.google.android.voicesearch.audio.AudioRouterImpl;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.audio.TtsAudioPlayer;
import com.google.android.voicesearch.bluetooth.BluetoothController;
import com.google.android.voicesearch.fragments.VoiceSearchController;
import com.google.android.voicesearch.greco3.languagepack.LanguagePackUpdateController;
import com.google.android.voicesearch.hotword.HotwordDetector;
import com.google.android.voicesearch.ime.VoiceImeSubtypeUpdater;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class VoiceSearchServices {
    private final AsyncServices mAsyncServices;
    private final Context mContext;
    private final CoreSearchServices mCoreSearchServices;
    private final Object mCreationLock;
    private final DeviceCapabilityManager mDeviceCapabilityManager;
    private final GsaPreferenceController mPreferenceController;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final Settings mSettings;
    private AudioController mAudioController;
    private AudioManager mAudioManager;
    private AudioRouter mAudioRouter;
    private AudioStore mAudioStore;
    private BluetoothController mBluetoothController;
    private Greco3Container mGreco3Container;
    private HotwordDetector mHotwordDetector;
    private HypothesisToSuggestionSpansConverter mHypothesisToSuggestionSpansConverter;
    private LanguagePackUpdateController mLanguagePackUpdateController;
    private LocalTtsManager mLocalTtsManager;
    private OfflineActionsManager mOfflineActionsManager;
    private Recognizer mRecognizer;
    private AudioTrackSoundManager mSoundManager;
    private SpeechLevelSource mSpeechLevelSource;
    private SpeechLibFactory mSpeechLibFactory;
    private SuggestionLogger mSuggestionLogger;
    private TtsAudioPlayer mTtsAudioPlayer;
    private VoiceImeSubtypeUpdater mVoiceImeSubtypeUpdater;

    public VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, GsaPreferenceController paramGsaPreferenceController, CoreSearchServices paramCoreSearchServices, Object paramObject) {
        this(paramContext, paramAsyncServices, paramGsaPreferenceController, paramCoreSearchServices, paramObject, new Settings(paramContext, paramGsaPreferenceController, paramCoreSearchServices.getSearchSettings(), paramCoreSearchServices.getConfig(), paramCoreSearchServices.getGsaConfigFlags(), paramCoreSearchServices.getHttpHelper(), paramAsyncServices.getPooledBackgroundExecutorService()));
    }

    VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, GsaPreferenceController paramGsaPreferenceController, CoreSearchServices paramCoreSearchServices, Object paramObject, Settings paramSettings) {
        this.mContext = paramContext;
        this.mPreferenceController = paramGsaPreferenceController;
        this.mAsyncServices = paramAsyncServices;
        this.mScheduledExecutorService = ConcurrentUtils.createSafeScheduledExecutorService(5, "ContainerScheduledExecutor");
        this.mDeviceCapabilityManager = paramCoreSearchServices.getDeviceCapabilityManager();
        this.mSettings = paramSettings;
        this.mSettings.addConfigurationListener(new Settings.ConfigurationChangeListener() {
            public void onChange(GstaticConfiguration.Configuration paramAnonymousConfiguration) {
                VoiceSearchServices.this.getVoiceImeSubtypeUpdater().onChange(paramAnonymousConfiguration);
            }
        });
        this.mCoreSearchServices = paramCoreSearchServices;
        this.mCreationLock = paramObject;
    }

    private void createAudioRouterLocked() {
        ScheduledExecutorService localScheduledExecutorService = ConcurrentUtils.createSingleThreadedScheduledExecutorService("AudioRouter");
        ExtraPreconditions.ThreadCheck localThreadCheck1 = ExtraPreconditions.createSetThreadsCheck(new String[]{"AudioRouter"});
        ExtraPreconditions.ThreadCheck localThreadCheck2 = ExtraPreconditions.createNotSetThreadsCheck(new String[]{"AudioRouter"});
        AudioManager localAudioManager = getAudioManager();
        this.mBluetoothController = new BluetoothController(this.mDeviceCapabilityManager, localAudioManager, this.mContext, localScheduledExecutorService, localThreadCheck1);
        this.mAudioRouter = new AudioRouterImpl(this.mSettings, localAudioManager, localScheduledExecutorService, localThreadCheck1, localThreadCheck2, this.mBluetoothController);
        this.mBluetoothController.addListener((AudioRouterImpl) this.mAudioRouter, localScheduledExecutorService);
    }

    private RecognitionEngineParams.EmbeddedParams createEmbeddedParams() {
        return new RecognitionEngineParams.EmbeddedParams(new DefaultCallbackFactory(), getGreco3Container().getGreco3EngineManager(), new DefaultModeSelector(this.mDeviceCapabilityManager.isTelephoneCapable()), getSpeechLevelSource(), this.mSettings, 2, 8000);
    }

    private RecognitionEngineParams.MusicDetectorParams createMusicDetectorParams() {
        return new RecognitionEngineParams.MusicDetectorParams(this.mSettings);
    }

    private RecognitionEngineParams createRecognitionEngineParams() {
        return new RecognitionEngineParams(createEmbeddedParams(), createMusicDetectorParams());
    }

    private Recognizer createRecognizer() {
        Log.i("VS.Container", "create_speech_recognizer");
        return RecognizerImpl.create(ConcurrentUtils.newSingleThreadExecutor("GrecoExecutor"), getAudioController(), getSpeechLibFactory());
    }

    private SpeechLibFactory getSpeechLibFactory() {
        if (this.mSpeechLibFactory == null) {
            this.mSpeechLibFactory = new SpeechLibFactoryImpl(createRecognitionEngineParams(), this.mSettings, this.mScheduledExecutorService, this.mCoreSearchServices.getClock());
        }
        return this.mSpeechLibFactory;
    }

    public boolean canCreatePumpkinTagger(GsaConfigFlags paramGsaConfigFlags, String paramString) {
        if (isLowRamDevice()) {
            return false;
        }
        return paramGsaConfigFlags.hasPumpkinLocale(paramString);
    }

    public VoiceSearchController createVoiceSearchController(Clock paramClock, SearchUrlHelper paramSearchUrlHelper) {
        return new VoiceSearchController(this, paramClock, paramSearchUrlHelper, getGsaConfigFlags());
    }

    public void dump(String paramString, PrintWriter paramPrintWriter) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("VoiceSearchServices state:");
        synchronized (this.mCreationLock) {
            LanguagePackUpdateController localLanguagePackUpdateController = this.mLanguagePackUpdateController;
            if (localLanguagePackUpdateController != null) {
                localLanguagePackUpdateController.dumpState(paramString + "  ", paramPrintWriter);
                return;
            }
        }
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("  LanguageUpdateController not initialized");
    }

    public AudioController getAudioController() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(this.mContext, this.mSettings, getSpeechLevelSource(), getSoundManager(), getAudioRouter(), getSpeechLibFactory().buildSpeechLibLogger());
        }
        return this.mAudioController;
    }

    public AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = ((AudioManager) this.mContext.getSystemService("audio"));
        }
        return this.mAudioManager;
    }

    public AudioRouter getAudioRouter() {
        synchronized (this.mCreationLock) {
            if (this.mAudioRouter == null) {
                createAudioRouterLocked();
            }
            return this.mAudioRouter;
        }
    }

    public ConnectionFactory getConnectionFactory() {
        return this.mCoreSearchServices.getSpdyConnectionFactory();
    }

    public ExecutorService getExecutorService() {
        return this.mScheduledExecutorService;
    }

    public Greco3Container getGreco3Container() {
        synchronized (this.mCreationLock) {
            if (this.mGreco3Container == null) {
                this.mGreco3Container = Greco3Container.create(this.mContext, this.mPreferenceController.getMainPreferences(), this.mScheduledExecutorService, this.mAsyncServices.getUiThreadExecutor());
            }
            Greco3Container localGreco3Container = this.mGreco3Container;
            return localGreco3Container;
        }
    }

    public GsaConfigFlags getGsaConfigFlags() {
        return this.mCoreSearchServices.getGsaConfigFlags();
    }

    public HotwordDetector getHotwordDetector() {
        if (this.mHotwordDetector == null) {
            this.mHotwordDetector = new HotwordDetector(this, this.mContext, this.mSettings, this.mAsyncServices.getUiThreadExecutor());
        }
        return this.mHotwordDetector;
    }

    public HypothesisToSuggestionSpansConverter getHypothesisToSuggestionSpansConverter() {
        if (this.mHypothesisToSuggestionSpansConverter == null) {
            this.mHypothesisToSuggestionSpansConverter = new HypothesisToSuggestionSpansConverter(this.mContext, getSuggestionLogger());
        }
        return this.mHypothesisToSuggestionSpansConverter;
    }

    public LanguagePackUpdateController getLanguageUpdateController() {
        synchronized (this.mCreationLock) {
            if (this.mLanguagePackUpdateController == null) {
                this.mLanguagePackUpdateController = LanguagePackUpdateController.create(getGreco3Container(), getSettings(), (DownloadManager) this.mContext.getSystemService("download"), this.mContext, getGreco3Container().getGreco3Preferences());
            }
            LanguagePackUpdateController localLanguagePackUpdateController = this.mLanguagePackUpdateController;
            return localLanguagePackUpdateController;
        }
    }

    public LocalTtsManager getLocalTtsManager() {

        if (this.mLocalTtsManager == null) {
            this.mLocalTtsManager = new LocalTtsManager(this.mContext, this.mAsyncServices.getUiThreadExecutor(), this.mScheduledExecutorService, getAudioRouter(), getSettings());
        }
        return this.mLocalTtsManager;
    }

    public Executor getMainThreadExecutor() {
        return this.mAsyncServices.getUiThreadExecutor();
    }

    public OfflineActionsManager getOfflineActionsManager() {

        if (this.mOfflineActionsManager == null) {
            this.mOfflineActionsManager = new OfflineActionsManager(this.mContext, getGreco3Container().getGreco3DataManager(), this.mSettings, this.mAsyncServices.getUiThreadExecutor());
        }
        return this.mOfflineActionsManager;
    }

    public Recognizer getRecognizer() {

        if (this.mRecognizer == null) {
            this.mRecognizer = createRecognizer();
        }
        return this.mRecognizer;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return this.mScheduledExecutorService;
    }

    public SearchConfig getSearchConfig() {
        return this.mCoreSearchServices.getConfig();
    }

    public Settings getSettings() {
        return this.mSettings;
    }

    public AudioTrackSoundManager getSoundManager() {

        if (this.mSoundManager == null) {
            this.mSoundManager = new AudioTrackSoundManager(this.mContext, getAudioRouter(), this.mScheduledExecutorService);
        }
        return this.mSoundManager;
    }

    public SpeechLevelSource getSpeechLevelSource() {
        if (this.mSpeechLevelSource == null) {
            this.mSpeechLevelSource = new SpeechLevelSource();
        }
        return this.mSpeechLevelSource;
    }

    public SuggestionLogger getSuggestionLogger() {

        if (this.mSuggestionLogger == null) {
            this.mSuggestionLogger = new SuggestionLogger();
        }
        return this.mSuggestionLogger;
    }

    public TtsAudioPlayer getTtsAudioPlayer() {

        if (this.mTtsAudioPlayer == null) {
            this.mTtsAudioPlayer = new TtsAudioPlayer(getAudioRouter(), this.mAsyncServices.getUiThreadExecutor());
        }
        return this.mTtsAudioPlayer;
    }

    public VoiceImeSubtypeUpdater getVoiceImeSubtypeUpdater() {
        synchronized (this.mCreationLock) {
            if (this.mVoiceImeSubtypeUpdater == null) {
                this.mVoiceImeSubtypeUpdater = new VoiceImeSubtypeUpdater(this.mContext, this.mScheduledExecutorService);
            }
            VoiceImeSubtypeUpdater localVoiceImeSubtypeUpdater = this.mVoiceImeSubtypeUpdater;
            return localVoiceImeSubtypeUpdater;
        }
    }

    public AudioStore getVoiceSearchAudioStore() {
        if (this.mAudioStore == null) {
            this.mAudioStore = new SingleRecordingAudioStore();
        }
        return this.mAudioStore;
    }

    public void init() {
        this.mSettings.asyncLoad();
    }

    public boolean isFollowOnEnabled(GsaConfigFlags paramGsaConfigFlags, String paramString) {
        if (!canCreatePumpkinTagger(paramGsaConfigFlags, paramString)) {
            return false;
        }
        return paramGsaConfigFlags.hasFollowOnLocale(paramString);
    }

    protected boolean isLowRamDevice() {
        return Util.isLowRamDevice(this.mContext);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.VoiceSearchServices

 * JD-Core Version:    0.7.0.1

 */