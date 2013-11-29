package com.google.android.speech.engine;

import com.google.android.shared.util.Clock;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.ServerRecognizeException;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Supplier;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.NetworkRecognizer;

public class DefaultRetryPolicy
  implements RetryPolicy
{
  private final Clock mClock;
  private int mCounter;
  private int mMaxRetryTimeoutMsec;
  private final Supplier<GstaticConfiguration.NetworkRecognizer> mNetworkRecognizer;
  private long mRecognitionStartedTimestamp;
  
  public DefaultRetryPolicy(Supplier<GstaticConfiguration.NetworkRecognizer> paramSupplier, Clock paramClock)
  {
    this.mNetworkRecognizer = paramSupplier;
    this.mClock = paramClock;
    reset();
  }
  
  public boolean canRetry(RecognizeException paramRecognizeException)
  {
    for (;;)
    {
      try
      {
        if (this.mCounter == 0)
        {
          EventLogger.recordClientEvent(27);
          bool2 = false;
          return bool2;
        }
        if (this.mCounter == -1)
        {
          this.mCounter = ((GstaticConfiguration.NetworkRecognizer)this.mNetworkRecognizer.get()).getMaxRetries();
          this.mMaxRetryTimeoutMsec = ((GstaticConfiguration.NetworkRecognizer)this.mNetworkRecognizer.get()).getMaxRetryTimeoutMsec();
        }
        if (this.mRecognitionStartedTimestamp + this.mMaxRetryTimeoutMsec < this.mClock.currentTimeMillis())
        {
          EventLogger.recordClientEvent(28);
          bool2 = false;
          continue;
        }
        boolean bool1 = isAuthException(paramRecognizeException);
        if (((paramRecognizeException instanceof NetworkRecognizeException)) || (bool1))
        {
          this.mCounter = (-1 + this.mCounter);
          if (bool1) {
            EventLogger.recordClientEvent(26);
          } else {
            EventLogger.recordClientEvent(25);
          }
        }
      }
      finally {}
      boolean bool2 = false;
      continue;
      bool2 = true;
    }
  }
  
  /* Error */
  public RecognizeException equivalentToError(com.google.speech.s3.S3.S3Response paramS3Response)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual 75	com/google/speech/s3/S3$S3Response:getStatus	()I
    //   6: iconst_2
    //   7: if_icmpne +19 -> 26
    //   10: new 77	com/google/android/speech/exception/ServerRecognizeException
    //   13: dup
    //   14: aload_1
    //   15: invokevirtual 80	com/google/speech/s3/S3$S3Response:getErrorCode	()I
    //   18: invokespecial 82	com/google/android/speech/exception/ServerRecognizeException:<init>	(I)V
    //   21: astore_3
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_3
    //   25: areturn
    //   26: aload_0
    //   27: iconst_0
    //   28: putfield 32	com/google/android/speech/engine/DefaultRetryPolicy:mCounter	I
    //   31: aconst_null
    //   32: astore_3
    //   33: goto -11 -> 22
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	41	0	this	DefaultRetryPolicy
    //   0	41	1	paramS3Response	com.google.speech.s3.S3.S3Response
    //   36	4	2	localObject	Object
    //   21	12	3	localServerRecognizeException	ServerRecognizeException
    // Exception table:
    //   from	to	target	type
    //   2	22	36	finally
    //   26	31	36	finally
  }
  
  public boolean isAuthException(RecognizeException paramRecognizeException)
  {
    if ((paramRecognizeException instanceof ServerRecognizeException)) {
      return ((ServerRecognizeException)paramRecognizeException).isAuthException();
    }
    return false;
  }
  
  public void reset()
  {
    try
    {
      this.mCounter = -1;
      this.mRecognitionStartedTimestamp = this.mClock.currentTimeMillis();
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
 * Qualified Name:     com.google.android.speech.engine.DefaultRetryPolicy
 * JD-Core Version:    0.7.0.1
 */