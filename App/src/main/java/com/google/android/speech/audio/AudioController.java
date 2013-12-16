package com.google.android.speech.audio;

import android.content.Context;
import android.net.Uri;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.voicesearch.LogExtras;
import com.google.android.voicesearch.audio.AudioRouter;
import com.google.common.base.Preconditions;

import java.util.List;

import javax.annotation.Nullable;

public class AudioController {
    private AudioInputParams mAudioInputParams;
    private final AudioRouter mAudioRouter;
    private AudioSource mAudioSource;
    private final Context mContext;
    private boolean mListening;
    private final LogExtras mLogExtras;
    private final SpeechLibLogger mLogger;
    private List<String> mNoiseSuppressors;
    @Nullable
    private AudioInputStreamFactory mRawInputStreamFactory = null;
    private final SpeechSettings mSettings;
    private final SpeakNowSoundPlayer mSoundManager;
    private final SpeechLevelSource mSpeechLevelSource;

    public AudioController(Context paramContext, SpeechSettings paramSpeechSettings, SpeechLevelSource paramSpeechLevelSource, SpeakNowSoundPlayer paramSpeakNowSoundPlayer, AudioRouter paramAudioRouter, SpeechLibLogger paramSpeechLibLogger, LogExtras paramLogExtras) {
        this.mContext = paramContext;
        this.mSettings = paramSpeechSettings;
        this.mSoundManager = paramSpeakNowSoundPlayer;
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mAudioRouter = paramAudioRouter;
        this.mLogger = paramSpeechLibLogger;
        this.mLogExtras = paramLogExtras;
    }

    private AudioSource createAudioSource(AudioInputStreamFactory paramAudioInputStreamFactory, AudioInputParams paramAudioInputParams) {
        int i = paramAudioInputParams.getSamplingRate();
        int j = MicrophoneInputStreamFactory.getMicrophoneReadSize(paramAudioInputParams.getSamplingRate());
        if (paramAudioInputParams.shouldReportSoundLevels()) {
        }
        for (SpeechLevelSource localSpeechLevelSource = this.mSpeechLevelSource; ; localSpeechLevelSource = null) {
            return new AudioSource(i, j, 500, 1000, paramAudioInputStreamFactory, localSpeechLevelSource);
        }
    }

    private AudioInputStreamFactory createDefaultRawInputStreamFactoryLocked(AudioInputParams paramAudioInputParams) {
        boolean bool1;
        int i;
        boolean bool2;
        if ((paramAudioInputParams.usePreemptibleAudioSource()) && (isPreemptibleAudioSourceSupported())) {
            bool1 = true;
            i = paramAudioInputParams.getSamplingRate();
            bool2 = isNoiseSuppressionEnabled(paramAudioInputParams);
            if (!paramAudioInputParams.isPlayBeepEnabled()) {
                break label83;
            }
        }
        label83:
        for (SpeakNowSoundPlayer localSpeakNowSoundPlayer = this.mSoundManager; ; localSpeakNowSoundPlayer = null) {
            return new VoiceAudioInputStreamFactory(new MicrophoneInputStreamFactory(i, bool2, localSpeakNowSoundPlayer, this.mAudioRouter, this.mLogger, bool1), this.mSettings, this.mContext);
            bool1 = false;
            break;
        }
    }

    private AudioInputStreamFactory createFactoryForRecordedUri(final Uri paramUri) {
        new AudioInputStreamFactory() {
            private int mNumStreamsCreated = 0;

            /* Error */
            public java.io.InputStream createInputStream()
                    throws java.io.IOException {
                // Byte code:
                //   0: aload_0
                //   1: monitorenter
                //   2: aload_0
                //   3: getfield 28	com/google/android/speech/audio/AudioController$1:mNumStreamsCreated	I
                //   6: istore_2
                //   7: aload_0
                //   8: iload_2
                //   9: iconst_1
                //   10: iadd
                //   11: putfield 28	com/google/android/speech/audio/AudioController$1:mNumStreamsCreated	I
                //   14: iload_2
                //   15: ifne +38 -> 53
                //   18: iconst_1
                //   19: istore_3
                //   20: iload_3
                //   21: invokestatic 38	com/google/common/base/Preconditions:checkState	(Z)V
                //   24: aload_0
                //   25: monitorexit
                //   26: new 40	android/os/ParcelFileDescriptor$AutoCloseInputStream
                //   29: dup
                //   30: aload_0
                //   31: getfield 21	com/google/android/speech/audio/AudioController$1:this$0	Lcom/google/android/speech/audio/AudioController;
                //   34: invokestatic 44	com/google/android/speech/audio/AudioController:access$000	(Lcom/google/android/speech/audio/AudioController;)Landroid/content/Context;
                //   37: invokevirtual 50	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
                //   40: aload_0
                //   41: getfield 23	com/google/android/speech/audio/AudioController$1:val$recordedAudioUri	Landroid/net/Uri;
                //   44: ldc 52
                //   46: invokevirtual 58	android/content/ContentResolver:openFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
                //   49: invokespecial 61	android/os/ParcelFileDescriptor$AutoCloseInputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
                //   52: areturn
                //   53: iconst_0
                //   54: istore_3
                //   55: goto -35 -> 20
                //   58: astore_1
                //   59: aload_0
                //   60: monitorexit
                //   61: aload_1
                //   62: athrow
                // Local variable table:
                //   start	length	slot	name	signature
                //   0	63	0	this	1
                //   58	4	1	localObject	Object
                //   6	9	2	i	int
                //   19	36	3	bool	boolean
                // Exception table:
                //   from	to	target	type
                //   2	14	58	finally
                //   20	26	58	finally
                //   59	61	58	finally
            }
        };
    }

    private AudioInputStreamFactory getRawInputStreamFactoryLocked(AudioInputParams paramAudioInputParams) {
        if (this.mRawInputStreamFactory != null) {
            return this.mRawInputStreamFactory;
        }
        if (paramAudioInputParams.getRecordedAudioUri() != null) {
            return createFactoryForRecordedUri(paramAudioInputParams.getRecordedAudioUri());
        }
        return createDefaultRawInputStreamFactoryLocked(paramAudioInputParams);
    }

    private boolean isNoiseSuppressionEnabled(AudioInputParams paramAudioInputParams) {
        if (!paramAudioInputParams.isNoiseSuppressionEnabled()) {
        }
        do {
            return false;
            if (this.mNoiseSuppressors == null) {
                this.mNoiseSuppressors = AudioUtils.getNoiseSuppressors(this.mSettings.getConfiguration().getPlatform());
            }
        } while (this.mNoiseSuppressors.size() == 0);
        return true;
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

    public synchronized void setRawInputStreamFactory(@Nullable AudioInputStreamFactory paramAudioInputStreamFactory) {
            this.mRawInputStreamFactory = paramAudioInputStreamFactory;
    }

    public synchronized void shutdown() {
            if (this.mAudioSource != null) {
                this.mAudioSource.shutdown();
                this.mAudioSource = null;
            }
            stopListening();
    }

    public void startListening(AudioInputParams paramAudioInputParams, RecognitionEventListener paramRecognitionEventListener) {
        try {
            if (!this.mListening) {
                this.mSpeechLevelSource.reset();
                this.mAudioInputParams = paramAudioInputParams;
                this.mAudioRouter.onStartListening(paramAudioInputParams.shouldRequestAudioFocus());
                if (this.mAudioSource != null) {
                    this.mAudioSource.start(paramRecognitionEventListener);
                }
                this.mLogger.logAudioPathEstablished(new SpeechLibLogger.LogData(this.mAudioRouter.getInputDeviceToLog(), this.mLogExtras.getNetworkType()));
                this.mListening = true;
            }
            return;
        } finally {
        }
    }

    public void stopListening() {
        try {
            if (this.mListening) {
                if (this.mAudioSource != null) {
                    this.mAudioSource.stopListening();
                }
                this.mAudioRouter.onStopListening(this.mAudioInputParams.shouldRequestAudioFocus());
                this.mSpeechLevelSource.reset();
                this.mListening = false;
            }
            return;
        } finally {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.AudioController

 * JD-Core Version:    0.7.0.1

 */
