package com.google.android.speech.network;

import android.util.Log;
import com.google.android.shared.util.ConcurrentUtils;
import com.google.android.speech.callback.Callback;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.network.producers.S3RequestProducer;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.speech.s3.S3.S3Response;
import java.io.Closeable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.annotation.Nullable;

public class NetworkRecognitionRunner
  implements Closeable
{
  private final WrapperCallback mCallback;
  private final Runnable mCloseConnectionRunnable = new Runnable()
  {
    public void run()
    {
      Closeables.closeQuietly(NetworkRecognitionRunner.this.mS3Connection);
      NetworkRecognitionRunner.access$102(NetworkRecognitionRunner.this, null);
    }
  };
  private final S3ConnectionFactory mConnectionFactory;
  private Future<?> mCurrentRecognition;
  private final NetworkEventListener mNetworkEventListener;
  private final Runnable mNetworkLoopRunnable = new Runnable()
  {
    public void run()
    {
      NetworkRecognitionRunner.this.runNetworkLoop();
    }
  };
  private final S3RequestProducer mRequestProducer;
  private final ExecutorService mRunnerThread;
  @Nullable
  private S3Connection mS3Connection;
  
  public NetworkRecognitionRunner(Callback<S3.S3Response, RecognizeException> paramCallback, NetworkEventListener paramNetworkEventListener, S3ConnectionFactory paramS3ConnectionFactory, S3RequestProducer paramS3RequestProducer)
  {
    this(paramCallback, paramNetworkEventListener, paramS3ConnectionFactory, paramS3RequestProducer, ConcurrentUtils.createSafeScheduledExecutorService(1, "NetworkRunner"));
  }
  
  NetworkRecognitionRunner(Callback<S3.S3Response, RecognizeException> paramCallback, NetworkEventListener paramNetworkEventListener, S3ConnectionFactory paramS3ConnectionFactory, S3RequestProducer paramS3RequestProducer, ExecutorService paramExecutorService)
  {
    this.mCallback = new WrapperCallback(paramCallback, paramNetworkEventListener);
    this.mNetworkEventListener = paramNetworkEventListener;
    this.mConnectionFactory = paramS3ConnectionFactory;
    this.mRequestProducer = paramS3RequestProducer;
    this.mRunnerThread = paramExecutorService;
  }
  
  @Nullable
  private Future<?> cancel()
  {
    Future localFuture1 = this.mCurrentRecognition;
    Future localFuture2 = null;
    if (localFuture1 != null)
    {
      this.mCurrentRecognition.cancel(true);
      localFuture2 = this.mCurrentRecognition;
      this.mCurrentRecognition = null;
    }
    return localFuture2;
  }
  
  private static void checkInterrupted()
    throws InterruptedException
  {
    if (Thread.currentThread().isInterrupted()) {
      throw new InterruptedException();
    }
  }
  
  /* Error */
  private void runNetworkLoop()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 62	com/google/android/speech/network/NetworkRecognitionRunner:mNetworkEventListener	Lcom/google/android/speech/network/NetworkEventListener;
    //   4: invokeinterface 108 1 0
    //   9: aload_0
    //   10: aload_0
    //   11: getfield 64	com/google/android/speech/network/NetworkRecognitionRunner:mConnectionFactory	Lcom/google/android/speech/network/S3ConnectionFactory;
    //   14: invokeinterface 114 1 0
    //   19: putfield 76	com/google/android/speech/network/NetworkRecognitionRunner:mS3Connection	Lcom/google/android/speech/network/S3Connection;
    //   22: invokestatic 116	com/google/android/speech/network/NetworkRecognitionRunner:checkInterrupted	()V
    //   25: aload_0
    //   26: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   29: invokeinterface 122 1 0
    //   34: astore 4
    //   36: aload_0
    //   37: getfield 76	com/google/android/speech/network/NetworkRecognitionRunner:mS3Connection	Lcom/google/android/speech/network/S3Connection;
    //   40: aload_0
    //   41: getfield 60	com/google/android/speech/network/NetworkRecognitionRunner:mCallback	Lcom/google/android/speech/network/NetworkRecognitionRunner$WrapperCallback;
    //   44: aload 4
    //   46: invokeinterface 128 3 0
    //   51: aload_0
    //   52: getfield 62	com/google/android/speech/network/NetworkRecognitionRunner:mNetworkEventListener	Lcom/google/android/speech/network/NetworkEventListener;
    //   55: invokeinterface 131 1 0
    //   60: invokestatic 116	com/google/android/speech/network/NetworkRecognitionRunner:checkInterrupted	()V
    //   63: aload_0
    //   64: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   67: invokeinterface 122 1 0
    //   72: astore 5
    //   74: aload 5
    //   76: ifnull +76 -> 152
    //   79: invokestatic 116	com/google/android/speech/network/NetworkRecognitionRunner:checkInterrupted	()V
    //   82: aload_0
    //   83: getfield 76	com/google/android/speech/network/NetworkRecognitionRunner:mS3Connection	Lcom/google/android/speech/network/S3Connection;
    //   86: aload 5
    //   88: invokeinterface 135 2 0
    //   93: aload_0
    //   94: getfield 62	com/google/android/speech/network/NetworkRecognitionRunner:mNetworkEventListener	Lcom/google/android/speech/network/NetworkEventListener;
    //   97: invokeinterface 138 1 0
    //   102: goto -39 -> 63
    //   105: astore_3
    //   106: aload_0
    //   107: getfield 60	com/google/android/speech/network/NetworkRecognitionRunner:mCallback	Lcom/google/android/speech/network/NetworkRecognitionRunner$WrapperCallback;
    //   110: new 140	com/google/android/speech/exception/NetworkRecognizeException
    //   113: dup
    //   114: ldc 142
    //   116: aload_3
    //   117: invokespecial 145	com/google/android/speech/exception/NetworkRecognizeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   120: invokevirtual 149	com/google/android/speech/network/NetworkRecognitionRunner$WrapperCallback:onError	(Lcom/google/android/speech/exception/RecognizeException;)V
    //   123: aload_0
    //   124: getfield 62	com/google/android/speech/network/NetworkRecognitionRunner:mNetworkEventListener	Lcom/google/android/speech/network/NetworkEventListener;
    //   127: invokeinterface 151 1 0
    //   132: aload_0
    //   133: getfield 76	com/google/android/speech/network/NetworkRecognitionRunner:mS3Connection	Lcom/google/android/speech/network/S3Connection;
    //   136: invokestatic 157	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   139: aload_0
    //   140: aconst_null
    //   141: putfield 76	com/google/android/speech/network/NetworkRecognitionRunner:mS3Connection	Lcom/google/android/speech/network/S3Connection;
    //   144: aload_0
    //   145: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   148: invokestatic 157	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   151: return
    //   152: aload_0
    //   153: getfield 62	com/google/android/speech/network/NetworkRecognitionRunner:mNetworkEventListener	Lcom/google/android/speech/network/NetworkEventListener;
    //   156: invokeinterface 160 1 0
    //   161: aload_0
    //   162: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   165: invokestatic 157	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   168: return
    //   169: astore_2
    //   170: aload_0
    //   171: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   174: invokestatic 157	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   177: return
    //   178: astore_1
    //   179: aload_0
    //   180: getfield 66	com/google/android/speech/network/NetworkRecognitionRunner:mRequestProducer	Lcom/google/android/speech/network/producers/S3RequestProducer;
    //   183: invokestatic 157	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   186: aload_1
    //   187: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	188	0	this	NetworkRecognitionRunner
    //   178	9	1	localObject	Object
    //   169	1	2	localInterruptedException	InterruptedException
    //   105	12	3	localIOException	java.io.IOException
    //   34	11	4	localS3Request1	com.google.speech.s3.S3.S3Request
    //   72	15	5	localS3Request2	com.google.speech.s3.S3.S3Request
    // Exception table:
    //   from	to	target	type
    //   0	63	105	java/io/IOException
    //   63	74	105	java/io/IOException
    //   79	102	105	java/io/IOException
    //   152	161	105	java/io/IOException
    //   0	63	169	java/lang/InterruptedException
    //   63	74	169	java/lang/InterruptedException
    //   79	102	169	java/lang/InterruptedException
    //   152	161	169	java/lang/InterruptedException
    //   0	63	178	finally
    //   63	74	178	finally
    //   79	102	178	finally
    //   106	144	178	finally
    //   152	161	178	finally
  }
  
  public void close()
  {
    this.mCallback.invalidate();
    if (cancel() == null)
    {
      if (!this.mRunnerThread.isShutdown()) {
        Preconditions.checkState(this.mRunnerThread.shutdownNow().isEmpty());
      }
      return;
    }
    this.mRunnerThread.execute(this.mCloseConnectionRunnable);
    this.mRunnerThread.shutdown();
  }
  
  public void finalize()
    throws Throwable
  {
    if (this.mS3Connection != null)
    {
      Log.e("VS.NetworkRecognitionRunner", "Recognition runner not closed, connection: " + this.mS3Connection);
      Closeables.closeQuietly(this.mS3Connection);
    }
    super.finalize();
  }
  
  Callback<S3.S3Response, RecognizeException> getWrappedCallbackForTesting()
  {
    return this.mCallback;
  }
  
  public void start()
  {
    if (this.mCurrentRecognition == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Duplicate call to start.");
      this.mCurrentRecognition = this.mRunnerThread.submit(this.mNetworkLoopRunnable);
      return;
    }
  }
  
  static class WrapperCallback
    implements Callback<S3.S3Response, RecognizeException>
  {
    private final Callback<S3.S3Response, RecognizeException> mDelegate;
    private volatile boolean mInvalid;
    private final NetworkEventListener mNetworkEventListener;
    
    WrapperCallback(Callback<S3.S3Response, RecognizeException> paramCallback, NetworkEventListener paramNetworkEventListener)
    {
      this.mDelegate = paramCallback;
      this.mNetworkEventListener = paramNetworkEventListener;
    }
    
    void invalidate()
    {
      this.mInvalid = true;
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      if (!this.mInvalid) {
        this.mDelegate.onError(paramRecognizeException);
      }
      this.mNetworkEventListener.onError();
    }
    
    public void onResult(S3.S3Response paramS3Response)
    {
      if (!this.mInvalid) {
        this.mDelegate.onResult(paramS3Response);
      }
      this.mNetworkEventListener.onDataReceived();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.NetworkRecognitionRunner
 * JD-Core Version:    0.7.0.1
 */