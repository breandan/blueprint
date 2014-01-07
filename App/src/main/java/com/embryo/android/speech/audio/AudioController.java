package com.embryo.android.speech.audio;

import android.content.Context;

import com.google.common.base.Preconditions;

import java.util.List;

import javax.annotation.Nullable;

public class AudioController {
    private final com.embryo.android.voicesearch.audio.AudioRouter mAudioRouter;
    private final Context mContext;
    private final com.embryo.android.speech.logger.SpeechLibLogger mLogger;
    private final com.embryo.android.speech.SpeechSettings mSettings;
    private final SpeakNowSoundPlayer mSoundManager;
    private final com.embryo.android.shared.util.SpeechLevelSource mSpeechLevelSource;
    private com.embryo.android.speech.params.AudioInputParams mAudioInputParams;
    private AudioSource mAudioSource;
    private boolean mListening;
    private List<String> mNoiseSuppressors;
    @Nullable
    private AudioInputStreamFactory mRawInputStreamFactory = null;

    public AudioController(Context paramContext, com.embryo.android.speech.SpeechSettings paramSpeechSettings, com.embryo.android.shared.util.SpeechLevelSource paramSpeechLevelSource, SpeakNowSoundPlayer paramSpeakNowSoundPlayer, com.embryo.android.voicesearch.audio.AudioRouter paramAudioRouter, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger) {
        this.mContext = paramContext;
        this.mSettings = paramSpeechSettings;
        this.mSoundManager = paramSpeakNowSoundPlayer;
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mAudioRouter = paramAudioRouter;
        this.mLogger = paramSpeechLibLogger;
    }

    private AudioSource createAudioSource(AudioInputStreamFactory inputStreamFactory, com.embryo.android.speech.params.AudioInputParams params) {
        return new AudioSource(params.getSamplingRate(), com.embryo.android.speech.audio.MicrophoneInputStreamFactory.getMicrophoneReadSize(params.getSamplingRate()), 0x1f4, 0x3e8, inputStreamFactory, params.shouldReportSoundLevels() ? mSpeechLevelSource : null);
    }

    private AudioInputStreamFactory createDefaultRawInputStreamFactoryLocked(com.embryo.android.speech.params.AudioInputParams params) {
        boolean preemptible = (params.usePreemptibleAudioSource()) && (isPreemptibleAudioSourceSupported());
        com.embryo.android.speech.audio.MicrophoneInputStreamFactory microphoneInputStreamFactory = new com.embryo.android.speech.audio.MicrophoneInputStreamFactory(params.getSamplingRate(), isNoiseSuppressionEnabled(params), mSoundManager, mAudioRouter, mLogger, preemptible);
        return new VoiceAudioInputStreamFactory(microphoneInputStreamFactory, mSettings, mContext);
    }

    private AudioInputStreamFactory getRawInputStreamFactoryLocked(com.embryo.android.speech.params.AudioInputParams paramAudioInputParams) {
        if (this.mRawInputStreamFactory != null) {
            return this.mRawInputStreamFactory;
        }
        return createDefaultRawInputStreamFactoryLocked(paramAudioInputParams);
    }

    private boolean isNoiseSuppressionEnabled(com.embryo.android.speech.params.AudioInputParams params) {
        if (!params.isNoiseSuppressionEnabled()) {
            return false;
        }
        if (mNoiseSuppressors == null) {
            mNoiseSuppressors = AudioUtils.getNoiseSuppressors(mSettings.getConfiguration().getPlatform());
        }
        if (mNoiseSuppressors.size() != 0) {
            return true;
        }
        return false;
    }

    private boolean isPreemptibleAudioSourceSupported() {
        return this.mContext.getPackageManager().checkPermission("android.permission.CAPTURE_AUDIO_HOTWORD", this.mContext.getPackageName()) == 0;
    }

    public synchronized AudioInputStreamFactory createInputStreamFactory(com.embryo.android.speech.params.AudioInputParams paramAudioInputParams) {
        mAudioSource = createAudioSource(getRawInputStreamFactoryLocked(paramAudioInputParams), paramAudioInputParams);
        return mAudioSource;
    }

    public synchronized AudioInputStreamFactory rewindInputStreamFactory(long paramLong) {
        Preconditions.checkNotNull(this.mAudioSource);
        Preconditions.checkState(this.mListening);
        this.mAudioSource = new AudioSource(this.mAudioSource);
        this.mAudioSource.setStartTime(paramLong);
        AudioSource localAudioSource = this.mAudioSource;
        return localAudioSource;
    }

    public synchronized void shutdown() {
        if (this.mAudioSource != null) {
            this.mAudioSource.shutdown();
            this.mAudioSource = null;
        }
        stopListening();
    }

    public synchronized void startListening(com.embryo.android.speech.params.AudioInputParams audioInputParams, com.embryo.android.speech.listeners.RecognitionEventListener listener) {
        if(!mListening) {
            mSpeechLevelSource.reset();
            mAudioInputParams = audioInputParams;
            mAudioRouter.onStartListening(audioInputParams.shouldRequestAudioFocus());
            if(mAudioSource != null) {
                mAudioSource.start(listener);
            }
            mLogger.logAudioPathEstablished(new com.embryo.android.speech.logger.SpeechLibLogger.LogData(mAudioRouter.getInputDeviceToLog()));
            mListening = true;
        }
    }

    public synchronized void stopListening() {
        if (mListening) {
            if (mAudioSource != null) {
                mAudioSource.stopListening();
            }
            mAudioRouter.onStopListening(mAudioInputParams.shouldRequestAudioFocus());
            mSpeechLevelSource.reset();
            mListening = false;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioController

 * JD-Core Version:    0.7.0.1

 */
