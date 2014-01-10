package com.embryo.android.voicesearch;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.embryo.android.search.core.GsaPreferenceController;
import com.embryo.android.shared.util.ConcurrentUtils;
import com.embryo.android.shared.util.ExtraPreconditions;
import com.embryo.android.shared.util.SpeechLevelSource;
import com.embryo.android.speech.Recognizer;
import com.embryo.android.speech.RecognizerImpl;
import com.embryo.android.speech.SpeechLibFactory;
import com.embryo.android.speech.audio.AudioController;
import com.embryo.android.speech.embedded.Greco3Container;
import com.embryo.android.speech.internal.DefaultCallbackFactory;
import com.embryo.android.speech.internal.DefaultModeSelector;
import com.embryo.android.speech.params.RecognitionEngineParams;
import com.embryo.android.voicesearch.audio.AudioRouter;
import com.embryo.android.voicesearch.audio.AudioRouterImpl;
import com.embryo.android.voicesearch.audio.AudioTrackSoundManager;
import com.embryo.android.voicesearch.settings.Settings;
import com.embryo.android.search.core.AsyncServices;

import java.util.concurrent.ScheduledExecutorService;

public class VoiceSearchServices {
    private final AsyncServices mAsyncServices;
    private final Context mContext;
    private final Object mCreationLock;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final Settings mSettings;
    private AudioController mAudioController;
    private AudioManager mAudioManager;
    private AudioRouter mAudioRouter;
    private Greco3Container mGreco3Container;
    private com.embryo.android.voicesearch.hotword.HotwordDetector mHotwordDetector;
    private Recognizer mRecognizer;
    private AudioTrackSoundManager mSoundManager;
    private SpeechLevelSource mSpeechLevelSource;
    private SpeechLibFactory mSpeechLibFactory;
    private com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater mVoiceImeSubtypeUpdater;

    public VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, GsaPreferenceController paramGsaPreferenceController, Object paramObject) {
        this(paramContext, paramAsyncServices, paramObject, new Settings(paramContext, paramGsaPreferenceController, paramAsyncServices.getPooledBackgroundExecutorService()));
    }

    VoiceSearchServices(Context paramContext, AsyncServices paramAsyncServices, Object paramObject, Settings paramSettings) {
        this.mContext = paramContext;
        this.mAsyncServices = paramAsyncServices;
        this.mScheduledExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSafeScheduledExecutorService(5, "ContainerScheduledExecutor");
        this.mSettings = paramSettings;
        this.mSettings.addConfigurationListener(new Settings.ConfigurationChangeListener() {
            public void onChange(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration paramAnonymousConfiguration) {
                VoiceSearchServices.this.getVoiceImeSubtypeUpdater().onChange(paramAnonymousConfiguration);
            }
        });
        this.mCreationLock = paramObject;
        init();
    }

    public void init() {
        mSettings.asyncLoad();
    }

    private void createAudioRouterLocked() {
        ScheduledExecutorService localScheduledExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSingleThreadedScheduledExecutorService("AudioRouter");
        ExtraPreconditions.ThreadCheck localThreadCheck1 = ExtraPreconditions.createSetThreadsCheck(new String[]{"AudioRouter"});
        ExtraPreconditions.ThreadCheck localThreadCheck2 = ExtraPreconditions.createNotSetThreadsCheck(new String[]{"AudioRouter"});
        AudioManager localAudioManager = getAudioManager();
        this.mAudioRouter = new AudioRouterImpl(this.mSettings, localAudioManager, localScheduledExecutorService, localThreadCheck1, localThreadCheck2);
    }

    private RecognitionEngineParams.EmbeddedParams createEmbeddedParams() {
        return new RecognitionEngineParams.EmbeddedParams(new DefaultCallbackFactory(), getGreco3Container().getGreco3EngineManager(), new DefaultModeSelector(), getSpeechLevelSource(), this.mSettings, 2, 8000);
    }

    private RecognitionEngineParams createRecognitionEngineParams() {
        return new RecognitionEngineParams(createEmbeddedParams());
    }

    private Recognizer createRecognizer() {
        Log.i("VS.Container", "create_speech_recognizer");
        return RecognizerImpl.create(ConcurrentUtils.newSingleThreadExecutor("GrecoExecutor"), getAudioController(), getSpeechLibFactory());
    }

    private SpeechLibFactory getSpeechLibFactory() {
        if (this.mSpeechLibFactory == null) {
            this.mSpeechLibFactory = new SpeechLibFactoryImpl(createRecognitionEngineParams(), this.mSettings, this.mScheduledExecutorService);
        }
        return this.mSpeechLibFactory;
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

    public Greco3Container getGreco3Container() {
        synchronized (this.mCreationLock) {
            if (this.mGreco3Container == null) {
                this.mGreco3Container = Greco3Container.create(this.mContext, this.mScheduledExecutorService, this.mAsyncServices.getUiThreadExecutor());
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

    public Recognizer getRecognizer() {

        if (this.mRecognizer == null) {
            this.mRecognizer = createRecognizer();
        }
        return this.mRecognizer;
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

    public com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater getVoiceImeSubtypeUpdater() {
        synchronized (this.mCreationLock) {
            if (this.mVoiceImeSubtypeUpdater == null) {
                this.mVoiceImeSubtypeUpdater = new com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater(this.mContext, this.mScheduledExecutorService);
            }
            com.embryo.android.voicesearch.ime.VoiceImeSubtypeUpdater localVoiceImeSubtypeUpdater = this.mVoiceImeSubtypeUpdater;
            return localVoiceImeSubtypeUpdater;
        }
    }

    protected boolean isLowRamDevice() {
        return com.embryo.android.shared.util.Util.isLowRamDevice(this.mContext);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceSearchServices

 * JD-Core Version:    0.7.0.1

 */