package com.google.android.speech.audio;

import android.content.Context;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.voicesearch.audio.AudioRouter;
import com.google.common.base.Preconditions;

import java.util.List;

import javax.annotation.Nullable;

public class AudioController {
    private final AudioRouter mAudioRouter;
    private final Context mContext;
    private final SpeechLibLogger mLogger;
    private final SpeechSettings mSettings;
    private final SpeakNowSoundPlayer mSoundManager;
    private final SpeechLevelSource mSpeechLevelSource;
    private AudioInputParams mAudioInputParams;
    private AudioSource mAudioSource;
    private boolean mListening;
    private List<String> mNoiseSuppressors;
    @Nullable
    private AudioInputStreamFactory mRawInputStreamFactory = null;

    public AudioController(Context paramContext, SpeechSettings paramSpeechSettings, SpeechLevelSource paramSpeechLevelSource, SpeakNowSoundPlayer paramSpeakNowSoundPlayer, AudioRouter paramAudioRouter, SpeechLibLogger paramSpeechLibLogger) {
        this.mContext = paramContext;
        this.mSettings = paramSpeechSettings;
        this.mSoundManager = paramSpeakNowSoundPlayer;
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mAudioRouter = paramAudioRouter;
        this.mLogger = paramSpeechLibLogger;
    }

    private AudioSource createAudioSource(AudioInputStreamFactory inputStreamFactory, AudioInputParams params) {
        return new AudioSource(params.getSamplingRate(), MicrophoneInputStreamFactory.getMicrophoneReadSize(params.getSamplingRate()), 0x1f4, 0x3e8, inputStreamFactory, params.shouldReportSoundLevels() ? mSpeechLevelSource : null);
    }

    private AudioInputStreamFactory createDefaultRawInputStreamFactoryLocked(AudioInputParams params) {
        boolean preemptible = (params.usePreemptibleAudioSource()) && (isPreemptibleAudioSourceSupported());
        MicrophoneInputStreamFactory microphoneInputStreamFactory = new MicrophoneInputStreamFactory(params.getSamplingRate(), isNoiseSuppressionEnabled(params), mSoundManager, mAudioRouter, mLogger, preemptible);
        return new VoiceAudioInputStreamFactory(microphoneInputStreamFactory, mSettings, mContext);
    }

    private AudioInputStreamFactory getRawInputStreamFactoryLocked(AudioInputParams paramAudioInputParams) {
        if (this.mRawInputStreamFactory != null) {
            return this.mRawInputStreamFactory;
        }
        return createDefaultRawInputStreamFactoryLocked(paramAudioInputParams);
    }

    private boolean isNoiseSuppressionEnabled(AudioInputParams params) {
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

    public synchronized AudioInputStreamFactory createInputStreamFactory(AudioInputParams paramAudioInputParams) {
        this.mAudioSource = createAudioSource(getRawInputStreamFactoryLocked(paramAudioInputParams), paramAudioInputParams);
        AudioSource localAudioSource = this.mAudioSource;
        return localAudioSource;
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

    public synchronized void startListening(AudioInputParams audioInputParams, RecognitionEventListener listener) {
        if(!mListening) {
            mSpeechLevelSource.reset();
            mAudioInputParams = audioInputParams;
            mAudioRouter.onStartListening(audioInputParams.shouldRequestAudioFocus());
            if(mAudioSource != null) {
                mAudioSource.start(listener);
            }
            mLogger.logAudioPathEstablished(new SpeechLibLogger.LogData(mAudioRouter.getInputDeviceToLog()));
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

 * Qualified Name:     com.google.android.speech.audio.AudioController

 * JD-Core Version:    0.7.0.1

 */
