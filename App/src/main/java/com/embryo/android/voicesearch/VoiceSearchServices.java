package com.embryo.android.voicesearch;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.embryo.android.shared.util.ExtraPreconditions;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.DeviceCapabilityManagerImpl;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.embryo.android.shared.util.SpeechLevelSource;
import com.embryo.android.speech.Recognizer;
import com.embryo.android.speech.RecognizerImpl;
import com.embryo.android.speech.SpeechLibFactory;
import com.embryo.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.embryo.android.speech.audio.AudioController;
import com.embryo.android.speech.audio.AudioStore;
import com.embryo.android.speech.embedded.Greco3Container;
import com.embryo.android.speech.embedded.OfflineActionsManager;
import com.embryo.android.speech.internal.DefaultCallbackFactory;
import com.embryo.android.speech.internal.DefaultModeSelector;
import com.embryo.android.speech.params.RecognitionEngineParams;
import com.embryo.android.voicesearch.audio.AudioRouter;
import com.embryo.android.voicesearch.audio.AudioRouterImpl;
import com.embryo.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.bluetooth.BluetoothController;
import com.google.android.voicesearch.greco3.languagepack.LanguagePackUpdateController;
import com.embryo.android.voicesearch.settings.Settings;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class VoiceSearchServices {
    private final AsyncServices mAsyncServices;
    private final Context mContext;
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
    private com.embryo.android.voicesearch.hotword.HotwordDetector mHotwordDetector;
    private HypothesisToSuggestionSpansConverter mHypothesisToSuggestionSpansConverter;
    private LanguagePackUpdateController mLanguagePackUpdateController;
    private OfflineActionsManager mOfflineActionsManager;
    private Recognizer mRecognizer;
    private AudioTrackSoundManager mSoundManager;
    private SpeechLevelSource mSpeechLevelSource;
    private SpeechLibFactory mSpeechLibFactory;
    private com.embryo.android.speech.logger.SuggestionLogger mSuggestionLogger;
    private com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater mVoiceImeSubtypeUpdater;

    public VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, GsaPreferenceController paramGsaPreferenceController, Object paramObject) {
        this(paramContext, paramAsyncServices, paramGsaPreferenceController, paramObject, new Settings(paramContext, paramGsaPreferenceController, paramAsyncServices.getPooledBackgroundExecutorService()));
    }

    VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, GsaPreferenceController paramGsaPreferenceController, Object paramObject, Settings paramSettings) {
        this.mContext = paramContext;
        this.mPreferenceController = paramGsaPreferenceController;
        this.mAsyncServices = paramAsyncServices;
        this.mScheduledExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSafeScheduledExecutorService(5, "ContainerScheduledExecutor");
        this.mDeviceCapabilityManager = new DeviceCapabilityManagerImpl(mContext);
        this.mSettings = paramSettings;
        this.mSettings.addConfigurationListener(new Settings.ConfigurationChangeListener() {
            public void onChange(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration paramAnonymousConfiguration) {
                VoiceSearchServices.this.getVoiceImeSubtypeUpdater().onChange(paramAnonymousConfiguration);
            }
        });
        this.mCreationLock = paramObject;
    }

    private void createAudioRouterLocked() {
        ScheduledExecutorService localScheduledExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSingleThreadedScheduledExecutorService("AudioRouter");
        ExtraPreconditions.ThreadCheck localThreadCheck1 = ExtraPreconditions.createSetThreadsCheck(new String[]{"AudioRouter"});
        ExtraPreconditions.ThreadCheck localThreadCheck2 = ExtraPreconditions.createNotSetThreadsCheck(new String[]{"AudioRouter"});
        AudioManager localAudioManager = getAudioManager();
        this.mAudioRouter = new AudioRouterImpl(this.mSettings, localAudioManager, localScheduledExecutorService, localThreadCheck1, localThreadCheck2, this.mBluetoothController);
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
        return RecognizerImpl.create(com.embryo.android.shared.util.ConcurrentUtils.newSingleThreadExecutor("GrecoExecutor"), getAudioController(), getSpeechLibFactory());
    }

    private SpeechLibFactory getSpeechLibFactory() {
        if (this.mSpeechLibFactory == null) {
            this.mSpeechLibFactory = new SpeechLibFactoryImpl(createRecognitionEngineParams(), this.mSettings, this.mScheduledExecutorService);
        }
        return this.mSpeechLibFactory;
    }

    public boolean canCreatePumpkinTagger(GsaConfigFlags paramGsaConfigFlags, String paramString) {
        if (isLowRamDevice()) {
            return false;
        }
        return paramGsaConfigFlags.hasPumpkinLocale(paramString);
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

    public com.embryo.android.voicesearch.hotword.HotwordDetector getHotwordDetector() {
        if (this.mHotwordDetector == null) {
            this.mHotwordDetector = new com.embryo.android.voicesearch.hotword.HotwordDetector(this, this.mContext, this.mSettings, this.mAsyncServices.getUiThreadExecutor());
        }
        return this.mHotwordDetector;
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

    public com.embryo.android.speech.logger.SuggestionLogger getSuggestionLogger() {

        if (this.mSuggestionLogger == null) {
            this.mSuggestionLogger = new com.embryo.android.speech.logger.SuggestionLogger();
        }
        return this.mSuggestionLogger;
    }

    public com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater getVoiceImeSubtypeUpdater() {
        synchronized (this.mCreationLock) {
            if (this.mVoiceImeSubtypeUpdater == null) {
                this.mVoiceImeSubtypeUpdater = new com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater(this.mContext, this.mScheduledExecutorService);
            }
            com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater localVoiceImeSubtypeUpdater = this.mVoiceImeSubtypeUpdater;
            return localVoiceImeSubtypeUpdater;
        }
    }

    public AudioStore getVoiceSearchAudioStore() {
        if (this.mAudioStore == null) {
            this.mAudioStore = new com.embryo.android.speech.audio.SingleRecordingAudioStore();
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
        return com.embryo.android.shared.util.Util.isLowRamDevice(this.mContext);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceSearchServices

 * JD-Core Version:    0.7.0.1

 */