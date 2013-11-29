package com.google.android.search.core.ears;

import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.engine.RecognitionEngine;
import com.google.android.speech.exception.AudioRecognizeException;
import com.google.android.speech.params.SessionParams;
import com.google.common.io.ByteStreams;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.SoundSearch;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MusicDetectorRecognitionEngine
  implements RecognitionEngine
{
  private boolean mClosed = true;
  private final SpeechSettings mSpeechSettings;
  
  public MusicDetectorRecognitionEngine(SpeechSettings paramSpeechSettings)
  {
    this.mSpeechSettings = paramSpeechSettings;
  }
  
  private float getMusicDetectorThreshold()
  {
    if ((!this.mSpeechSettings.getConfiguration().hasSoundSearch()) || (!this.mSpeechSettings.getConfiguration().getSoundSearch().hasMusicDetectorThreshold())) {
      return 1.0F;
    }
    return this.mSpeechSettings.getConfiguration().getSoundSearch().getMusicDetectorThreshold();
  }
  
  /* Error */
  private boolean initMusicDetector(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 17	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:mClosed	Z
    //   6: istore_3
    //   7: iconst_0
    //   8: istore 4
    //   10: iload_3
    //   11: ifeq +8 -> 19
    //   14: aload_0
    //   15: monitorexit
    //   16: iload 4
    //   18: ireturn
    //   19: iload_1
    //   20: invokestatic 53	com/google/audio/ears/MusicDetector:init	(I)Z
    //   23: pop
    //   24: iconst_1
    //   25: istore 4
    //   27: goto -13 -> 14
    //   30: astore 5
    //   32: ldc 55
    //   34: new 57	java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial 58	java/lang/StringBuilder:<init>	()V
    //   41: ldc 60
    //   43: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: aload 5
    //   48: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   51: invokevirtual 71	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   54: invokestatic 77	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   57: pop
    //   58: iconst_0
    //   59: istore 4
    //   61: goto -47 -> 14
    //   64: astore_2
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_2
    //   68: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	69	0	this	MusicDetectorRecognitionEngine
    //   0	69	1	paramInt	int
    //   64	4	2	localObject	Object
    //   6	5	3	bool1	boolean
    //   8	52	4	bool2	boolean
    //   30	17	5	localUnsatisfiedLinkError	java.lang.UnsatisfiedLinkError
    // Exception table:
    //   from	to	target	type
    //   19	24	30	java/lang/UnsatisfiedLinkError
    //   2	7	64	finally
    //   19	24	64	finally
    //   32	58	64	finally
  }
  
  /* Error */
  private Float processAudio(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 17	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:mClosed	Z
    //   6: istore_3
    //   7: iload_3
    //   8: ifeq +11 -> 19
    //   11: aconst_null
    //   12: astore 5
    //   14: aload_0
    //   15: monitorexit
    //   16: aload 5
    //   18: areturn
    //   19: aload_1
    //   20: aload_1
    //   21: arraylength
    //   22: iconst_2
    //   23: idiv
    //   24: invokestatic 83	com/google/audio/ears/MusicDetector:process	([BI)F
    //   27: invokestatic 89	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   30: astore 4
    //   32: aload 4
    //   34: astore 5
    //   36: goto -22 -> 14
    //   39: astore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	44	0	this	MusicDetectorRecognitionEngine
    //   0	44	1	paramArrayOfByte	byte[]
    //   39	4	2	localObject1	Object
    //   6	2	3	bool	boolean
    //   30	3	4	localFloat	Float
    //   12	23	5	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	7	39	finally
    //   19	32	39	finally
  }
  
  private void processInputStream(InputStream paramInputStream, RecognitionEngineCallback paramRecognitionEngineCallback, SessionParams paramSessionParams)
  {
    float f = getMusicDetectorThreshold();
    int i;
    if (paramSessionParams.getMode() == 6) {
      i = 8000;
    }
    for (;;)
    {
      byte[] arrayOfByte = new byte[i];
      int j = 0;
      try
      {
        for (;;)
        {
          ByteStreams.readFully(paramInputStream, arrayOfByte);
          Float localFloat = processAudio(arrayOfByte);
          if (localFloat == null) {
            return;
          }
          if (localFloat.floatValue() < f) {
            break label134;
          }
          j++;
          if (paramSessionParams.getMode() != 6) {
            break;
          }
          if (j >= 3) {
            paramRecognitionEngineCallback.onResult(new RecognitionResponse(Boolean.TRUE));
          }
        }
        paramRecognitionEngineCallback.onResult(new RecognitionResponse(Boolean.TRUE));
        return;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          paramRecognitionEngineCallback.onError(new AudioRecognizeException("Error reading from input stream.", localIOException));
          return;
          label134:
          j = 0;
        }
        i = 4000;
      }
      catch (EOFException localEOFException) {}
    }
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 17	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:mClosed	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: iconst_1
    //   16: putfield 17	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:mClosed	Z
    //   19: invokestatic 146	com/google/android/speech/embedded/Greco3Recognizer:maybeLoadSharedLibrary	()V
    //   22: invokestatic 148	com/google/audio/ears/MusicDetector:close	()V
    //   25: goto -14 -> 11
    //   28: astore_3
    //   29: ldc 55
    //   31: new 57	java/lang/StringBuilder
    //   34: dup
    //   35: invokespecial 58	java/lang/StringBuilder:<init>	()V
    //   38: ldc 150
    //   40: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: aload_3
    //   44: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   47: invokevirtual 71	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   50: invokestatic 77	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   53: pop
    //   54: goto -43 -> 11
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	62	0	this	MusicDetectorRecognitionEngine
    //   57	4	1	localObject	Object
    //   6	2	2	bool	boolean
    //   28	16	3	localUnsatisfiedLinkError	java.lang.UnsatisfiedLinkError
    // Exception table:
    //   from	to	target	type
    //   22	25	28	java/lang/UnsatisfiedLinkError
    //   2	7	57	finally
    //   14	22	57	finally
    //   22	25	57	finally
    //   29	54	57	finally
  }
  
  /* Error */
  public void startRecognition(com.google.android.speech.audio.AudioInputStreamFactory paramAudioInputStreamFactory, RecognitionEngineCallback paramRecognitionEngineCallback, SessionParams paramSessionParams)
  {
    // Byte code:
    //   0: invokestatic 146	com/google/android/speech/embedded/Greco3Recognizer:maybeLoadSharedLibrary	()V
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: iconst_0
    //   7: putfield 17	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:mClosed	Z
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: invokeinterface 158 1 0
    //   18: astore 6
    //   20: aload_0
    //   21: aload_3
    //   22: invokevirtual 162	com/google/android/speech/params/SessionParams:getAudioInputParams	()Lcom/google/android/speech/params/AudioInputParams;
    //   25: invokevirtual 167	com/google/android/speech/params/AudioInputParams:getSamplingRate	()I
    //   28: invokespecial 169	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:initMusicDetector	(I)Z
    //   31: ifne +31 -> 62
    //   34: return
    //   35: astore 4
    //   37: aload_0
    //   38: monitorexit
    //   39: aload 4
    //   41: athrow
    //   42: astore 5
    //   44: aload_2
    //   45: new 132	com/google/android/speech/exception/AudioRecognizeException
    //   48: dup
    //   49: ldc 171
    //   51: aload 5
    //   53: invokespecial 137	com/google/android/speech/exception/AudioRecognizeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   56: invokeinterface 140 2 0
    //   61: return
    //   62: aload_0
    //   63: aload 6
    //   65: aload_2
    //   66: aload_3
    //   67: invokespecial 173	com/google/android/search/core/ears/MusicDetectorRecognitionEngine:processInputStream	(Ljava/io/InputStream;Lcom/google/android/speech/callback/RecognitionEngineCallback;Lcom/google/android/speech/params/SessionParams;)V
    //   70: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	71	0	this	MusicDetectorRecognitionEngine
    //   0	71	1	paramAudioInputStreamFactory	com.google.android.speech.audio.AudioInputStreamFactory
    //   0	71	2	paramRecognitionEngineCallback	RecognitionEngineCallback
    //   0	71	3	paramSessionParams	SessionParams
    //   35	5	4	localObject	Object
    //   42	10	5	localIOException	IOException
    //   18	46	6	localInputStream	InputStream
    // Exception table:
    //   from	to	target	type
    //   5	12	35	finally
    //   37	39	35	finally
    //   12	20	42	java/io/IOException
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ears.MusicDetectorRecognitionEngine
 * JD-Core Version:    0.7.0.1
 */