package com.google.android.speech.audio;

import android.os.Build.VERSION;
import android.util.Log;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.voicesearch.audio.AudioRouter;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class MicrophoneInputStreamFactory
  implements AudioInputStreamFactory
{
  private final AudioRouter mAudioRouter;
  private final SpeakNowSoundPlayer mBeepPlayer;
  private final boolean mNoiseSuppression;
  private final boolean mPreemptible;
  private final int mSampleRateHz;
  private final SpeechLibLogger mSpeechLibLogger;
  
  public MicrophoneInputStreamFactory(int paramInt, boolean paramBoolean1, SpeakNowSoundPlayer paramSpeakNowSoundPlayer, AudioRouter paramAudioRouter, SpeechLibLogger paramSpeechLibLogger, boolean paramBoolean2)
  {
    this.mSampleRateHz = paramInt;
    this.mNoiseSuppression = paramBoolean1;
    this.mBeepPlayer = paramSpeakNowSoundPlayer;
    this.mAudioRouter = paramAudioRouter;
    this.mSpeechLibLogger = paramSpeechLibLogger;
    this.mPreemptible = paramBoolean2;
  }
  
  private int getAudioRecordBufferSizeBytes()
  {
    return 8 * (2 * this.mSampleRateHz);
  }
  
  public static int getBytesPerMsec(int paramInt)
  {
    return paramInt * 2 / 1000;
  }
  
  public static int getMicrophoneReadSize(int paramInt)
  {
    return 20 * getBytesPerMsec(paramInt);
  }
  
  public InputStream createInputStream()
  {
    if (Build.VERSION.SDK_INT >= 16) {}
    for (String str = "com.google.android.speech.audio.FullMicrophoneInputStream";; str = "com.google.android.speech.audio.MicrophoneInputStream") {
      try
      {
        Class localClass = Class.forName(str);
        Class[] arrayOfClass = new Class[7];
        arrayOfClass[0] = Integer.TYPE;
        arrayOfClass[1] = Integer.TYPE;
        arrayOfClass[2] = Boolean.TYPE;
        arrayOfClass[3] = SpeakNowSoundPlayer.class;
        arrayOfClass[4] = AudioRouter.class;
        arrayOfClass[5] = SpeechLibLogger.class;
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
        InputStream localInputStream = (InputStream)localConstructor.newInstance(arrayOfObject);
        return localInputStream;
      }
      catch (Exception localException)
      {
        Log.e("MicrophoneInputStreamFactory", "Unable to create MicrophoneInputStream", localException);
        throw new RuntimeException("Unable to create MicrophoneInputStream", localException);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.audio.MicrophoneInputStreamFactory
 * JD-Core Version:    0.7.0.1
 */