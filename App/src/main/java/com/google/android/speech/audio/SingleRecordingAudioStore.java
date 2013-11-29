package com.google.android.speech.audio;

import android.text.TextUtils;
import javax.annotation.Nullable;

public class SingleRecordingAudioStore
  implements AudioStore
{
  private AudioStore.AudioRecording mLastAudioRecording;
  private String mLastRequestId;
  
  /* Error */
  @Nullable
  public AudioStore.AudioRecording getAudio(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield 19	com/google/android/speech/audio/SingleRecordingAudioStore:mLastRequestId	Ljava/lang/String;
    //   7: invokestatic 25	android/text/TextUtils:equals	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z
    //   10: ifeq +12 -> 22
    //   13: aload_0
    //   14: getfield 27	com/google/android/speech/audio/SingleRecordingAudioStore:mLastAudioRecording	Lcom/google/android/speech/audio/AudioStore$AudioRecording;
    //   17: astore_3
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_3
    //   21: areturn
    //   22: aconst_null
    //   23: astore_3
    //   24: goto -6 -> 18
    //   27: astore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_2
    //   31: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	32	0	this	SingleRecordingAudioStore
    //   0	32	1	paramString	String
    //   27	4	2	localObject	Object
    //   17	7	3	localAudioRecording	AudioStore.AudioRecording
    // Exception table:
    //   from	to	target	type
    //   2	18	27	finally
  }
  
  @Nullable
  public AudioStore.AudioRecording getLastAudio()
  {
    try
    {
      AudioStore.AudioRecording localAudioRecording = this.mLastAudioRecording;
      return localAudioRecording;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean hasAudio(String paramString)
  {
    try
    {
      boolean bool = TextUtils.equals(paramString, this.mLastRequestId);
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void put(String paramString, AudioStore.AudioRecording paramAudioRecording)
  {
    try
    {
      this.mLastRequestId = paramString;
      this.mLastAudioRecording = paramAudioRecording;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.audio.SingleRecordingAudioStore
 * JD-Core Version:    0.7.0.1
 */