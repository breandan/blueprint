package com.embryo.android.speech.audio;

import android.util.Log;

import java.io.InputStream;
import java.lang.reflect.Constructor;

public class MicrophoneInputStreamFactory
        implements AudioInputStreamFactory {
    private final com.embryo.android.voicesearch.audio.AudioRouter mAudioRouter;
    private final SpeakNowSoundPlayer mBeepPlayer;
    private final boolean mNoiseSuppression;
    private final boolean mPreemptible;
    private final int mSampleRateHz;
    private final com.embryo.android.speech.logger.SpeechLibLogger mSpeechLibLogger;

    public MicrophoneInputStreamFactory(int paramInt, boolean paramBoolean1, SpeakNowSoundPlayer paramSpeakNowSoundPlayer, com.embryo.android.voicesearch.audio.AudioRouter paramAudioRouter, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger, boolean paramBoolean2) {
        this.mSampleRateHz = paramInt;
        this.mNoiseSuppression = paramBoolean1;
        this.mBeepPlayer = paramSpeakNowSoundPlayer;
        this.mAudioRouter = paramAudioRouter;
        this.mSpeechLibLogger = paramSpeechLibLogger;
        this.mPreemptible = paramBoolean2;
    }

    public static int getBytesPerMsec(int paramInt) {
        return paramInt * 2 / 1000;
    }

    public static int getMicrophoneReadSize(int paramInt) {
        return 20 * getBytesPerMsec(paramInt);
    }

    private int getAudioRecordBufferSizeBytes() {
        return 8 * (2 * this.mSampleRateHz);
    }

    public InputStream createInputStream() {
        try {
            Class localClass = FullMicrophoneInputStream.class;
            Class[] arrayOfClass = new Class[7];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = Boolean.TYPE;
            arrayOfClass[3] = SpeakNowSoundPlayer.class;
            arrayOfClass[4] = com.embryo.android.voicesearch.audio.AudioRouter.class;
            arrayOfClass[5] = com.embryo.android.speech.logger.SpeechLibLogger.class;
            arrayOfClass[6] = Boolean.TYPE;
            Constructor localConstructor = localClass.getConstructor(arrayOfClass);
            Object[] arrayOfObject = new Object[7];
            arrayOfObject[0] = Integer.valueOf(this.mSampleRateHz);
            arrayOfObject[1] = Integer.valueOf(getAudioRecordBufferSizeBytes());
            arrayOfObject[2] = Boolean.valueOf(this.mNoiseSuppression);
            arrayOfObject[3] = this.mBeepPlayer;
            arrayOfObject[4] = this.mAudioRouter;
            arrayOfObject[5] = this.mSpeechLibLogger;
            arrayOfObject[6] = Boolean.valueOf(this.mPreemptible);
            InputStream localInputStream = (InputStream) localConstructor.newInstance(arrayOfObject);
            return localInputStream;
        } catch (Exception localException) {
            Log.e("MicrophoneInputStreamFactory", "Unable to create MicrophoneInputStream", localException);
            throw new RuntimeException("Unable to create MicrophoneInputStream", localException);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     MicrophoneInputStreamFactory

 * JD-Core Version:    0.7.0.1

 */