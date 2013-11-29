package com.google.android.speech.network;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.util.NetworkUtils;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.speech.callback.Callback;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.ServerRecognizeException;
import com.google.android.speech.message.S3RequestStream;
import com.google.android.speech.message.S3ResponseStream;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.speech.s3.S3.S3Request;
import com.google.speech.s3.S3.S3Response;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.HttpServerInfo;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.PairHttpServerInfo;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

public class PairHttpConnection
  implements S3Connection
{
  private final boolean mAcceptUpstreamResponses;
  private Callback<S3.S3Response, RecognizeException> mCallback;
  private final ConnectionFactory mConnectionFactory;
  private DownloadThread mDownloadThread;
  private S3RequestStream mOutput;
  private final GstaticConfiguration.PairHttpServerInfo mPairHttpServerInfo;
  private ResponseState mResponseState;
  private final ExtraPreconditions.ThreadCheck mSameThreadCheck = ExtraPreconditions.createSameThreadCheck();
  private UpResponseThread mUpResponseThread;
  private URL mUpUrl;
  private HttpURLConnection mUploadConnection;
  
  public PairHttpConnection(GstaticConfiguration.PairHttpServerInfo paramPairHttpServerInfo, ConnectionFactory paramConnectionFactory, boolean paramBoolean)
  {
    this.mPairHttpServerInfo = paramPairHttpServerInfo;
    this.mConnectionFactory = paramConnectionFactory;
    this.mAcceptUpstreamResponses = paramBoolean;
  }
  
  private static String generateKey()
  {
    return UUID.randomUUID().toString();
  }
  
  private void handleEndOfData()
  {
    for (;;)
    {
      InputStream localInputStream;
      try
      {
        this.mOutput.close();
        int j = this.mUploadConnection.getResponseCode();
        this.mOutput = null;
        verifyResponseCodeAndUrl(j, this.mUpUrl, this.mUploadConnection);
        localInputStream = null;
        localObject = null;
      }
      catch (IOException localIOException3)
      {
        S3.S3Response localS3Response2;
        try
        {
          localInputStream = this.mUploadConnection.getInputStream();
          localS3ResponseStream = new S3ResponseStream(localInputStream);
        }
        catch (IOException localIOException1) {}
        try
        {
          localS3Response2 = localS3ResponseStream.read();
          localS3Response1 = localS3Response2;
          i = 1;
          localObject = localS3ResponseStream;
          if (i == 0) {
            break;
          }
          setResponseState(ResponseState.UP);
          Preconditions.checkNotNull(localS3Response1);
          this.mUpResponseThread = new UpResponseThread(localObject, this.mUploadConnection, this.mCallback, localS3Response1);
          this.mUploadConnection = null;
          this.mUpResponseThread.start();
          return;
        }
        catch (IOException localIOException2)
        {
          for (;;)
          {
            localObject = localS3ResponseStream;
          }
        }
        localIOException3 = localIOException3;
        this.mCallback.onError(new NetworkRecognizeException("[Upload] Failed to send request", localIOException3));
        return;
      }
      catch (RecognizeException localRecognizeException)
      {
        this.mCallback.onError(localRecognizeException);
        continue;
      }
      Closeables.closeQuietly(localInputStream);
      S3.S3Response localS3Response1 = null;
      int i = 0;
    }
    Closeables.closeQuietly(localObject);
  }
  
  private void maybeSetCompressionHeader(HttpURLConnection paramHttpURLConnection, GstaticConfiguration.HttpServerInfo paramHttpServerInfo)
  {
    if (!paramHttpServerInfo.getDisableCompression()) {
      paramHttpURLConnection.setRequestProperty("X-S3-Send-Compressible", "1");
    }
  }
  
  public void close()
  {
    this.mSameThreadCheck.check();
    Closeables.closeQuietly(this.mOutput);
    if (this.mUploadConnection != null)
    {
      this.mUploadConnection.disconnect();
      this.mUploadConnection = null;
    }
    Closeables.closeQuietly(this.mUpResponseThread);
    Closeables.closeQuietly(this.mDownloadThread);
  }
  
  public void connect(Callback<S3.S3Response, RecognizeException> paramCallback, S3.S3Request paramS3Request)
    throws IOException
  {
    this.mSameThreadCheck.check();
    setResponseState(ResponseState.UNKNOWN);
    this.mCallback = ((Callback)Preconditions.checkNotNull(paramCallback));
    String str = generateKey();
    try
    {
      this.mDownloadThread = new DownloadThread(new URL(this.mPairHttpServerInfo.getDown().getUrl() + str), null);
      this.mDownloadThread.start();
      GstaticConfiguration.HttpServerInfo localHttpServerInfo = this.mPairHttpServerInfo.getUp();
      this.mUpUrl = new URL(localHttpServerInfo.getUrl() + str);
      this.mUploadConnection = this.mConnectionFactory.openHttpConnection(localHttpServerInfo, this.mUpUrl);
      IoUtils.addHttpHeaders(this.mUploadConnection, localHttpServerInfo);
      maybeSetCompressionHeader(this.mUploadConnection, localHttpServerInfo);
      this.mUploadConnection.setDoOutput(true);
      this.mUploadConnection.setUseCaches(false);
      NetworkUtils.connect(this.mUploadConnection);
      this.mOutput = new S3RequestStream(this.mUploadConnection.getOutputStream(), localHttpServerInfo.getHeader(), false);
      this.mOutput.writeHeader(paramS3Request);
      if (!this.mDownloadThread.waitForConnection()) {
        throw new IOException("Timed out / error during connect");
      }
    }
    catch (IOException localIOException)
    {
      Log.w("PairHttpConnection", "[Upload] Connection error", localIOException);
      close();
      throw localIOException;
    }
  }
  
  public void send(S3.S3Request paramS3Request)
  {
    this.mSameThreadCheck.check();
    if (this.mOutput != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "call to send() after close() / error / end of data");
      try
      {
        this.mOutput.write(paramS3Request);
        this.mOutput.flush();
        if (paramS3Request.getEndOfData()) {
          handleEndOfData();
        }
        return;
      }
      catch (IOException localIOException)
      {
        this.mCallback.onError(new NetworkRecognizeException("Failed to send request", localIOException));
      }
    }
  }
  
  boolean setResponseState(ResponseState paramResponseState)
  {
    boolean bool1;
    for (;;)
    {
      try
      {
        int i = 1.$SwitchMap$com$google$android$speech$network$PairHttpConnection$ResponseState[paramResponseState.ordinal()];
        bool1 = false;
        switch (i)
        {
        default: 
          return bool1;
        }
      }
      finally {}
      if (this.mResponseState == ResponseState.DOWN)
      {
        Log.w("PairHttpConnection", "The response is sent in the up and down");
        bool1 = false;
      }
      else
      {
        this.mResponseState = ResponseState.UP;
        bool1 = true;
      }
    }
    if (this.mResponseState != ResponseState.DOWN_ERROR) {}
    for (boolean bool3 = true;; bool3 = false)
    {
      Preconditions.checkState(bool3);
      if (this.mResponseState == ResponseState.UP)
      {
        Log.w("PairHttpConnection", "The response is sent in the up and down");
        bool1 = false;
        break;
      }
      this.mResponseState = ResponseState.DOWN;
      bool1 = true;
      break;
      boolean bool2 = this.mAcceptUpstreamResponses;
      bool1 = false;
      if (!bool2) {
        break;
      }
      if (this.mResponseState == ResponseState.DOWN)
      {
        this.mResponseState = ResponseState.DOWN_ERROR;
        bool1 = false;
        break;
      }
      if (this.mResponseState == ResponseState.UP)
      {
        bool1 = true;
        break;
      }
      this.mResponseState = ResponseState.DOWN_ERROR;
      bool1 = true;
      break;
      this.mResponseState = ResponseState.UNKNOWN;
      bool1 = true;
      break;
    }
  }
  
  protected void verifyResponseCodeAndUrl(int paramInt, URL paramURL, HttpURLConnection paramHttpURLConnection)
    throws RecognizeException
  {
    if (!paramURL.getHost().equals(paramHttpURLConnection.getURL().getHost()))
    {
      Log.w("PairHttpConnection", "[Upload] unexpected redirect to " + paramHttpURLConnection.getURL());
      throw new NetworkRecognizeException("[Upload] unexpected redirect to " + paramHttpURLConnection.getURL());
    }
    if (paramInt != 200)
    {
      String str = paramHttpURLConnection.getHeaderField("X-Speech-S3-Res-Code");
      if (!TextUtils.isEmpty(str)) {
        throw new ServerRecognizeException(Integer.parseInt(str));
      }
      Log.w("PairHttpConnection", "[Upload] response code " + paramInt);
      throw new NetworkRecognizeException("[Upload] bad response: " + paramInt);
    }
  }
  
  protected class DownloadThread
    extends Thread
    implements Closeable
  {
    private final CountDownLatch mConnectedLatch;
    private final URL mDownUrl;
    private volatile boolean mRunning;
    
    private DownloadThread(URL paramURL)
    {
      super();
      this.mDownUrl = paramURL;
      this.mConnectedLatch = new CountDownLatch(1);
      this.mRunning = true;
    }
    
    /* Error */
    private void runDownloadLoop(@Nonnull HttpURLConnection paramHttpURLConnection)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: invokespecial 46	com/google/android/speech/network/PairHttpConnection$DownloadThread:verifyResponseCodeAndUrl	(Ljava/net/HttpURLConnection;)Z
      //   5: ifne +42 -> 47
      //   8: aload_0
      //   9: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   12: getstatic 52	com/google/android/speech/network/PairHttpConnection$ResponseState:DOWN_ERROR	Lcom/google/android/speech/network/PairHttpConnection$ResponseState;
      //   15: invokevirtual 58	com/google/android/speech/network/PairHttpConnection:setResponseState	(Lcom/google/android/speech/network/PairHttpConnection$ResponseState;)Z
      //   18: ifne +24 -> 42
      //   21: aload_0
      //   22: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   25: invokestatic 62	com/google/android/speech/network/PairHttpConnection:access$400	(Lcom/google/android/speech/network/PairHttpConnection;)Lcom/google/android/speech/callback/Callback;
      //   28: new 64	com/google/android/speech/exception/NetworkRecognizeException
      //   31: dup
      //   32: ldc 66
      //   34: invokespecial 67	com/google/android/speech/exception/NetworkRecognizeException:<init>	(Ljava/lang/String;)V
      //   37: invokeinterface 73 2 0
      //   42: aload_1
      //   43: invokevirtual 79	java/net/HttpURLConnection:disconnect	()V
      //   46: return
      //   47: aconst_null
      //   48: astore_2
      //   49: new 81	com/google/android/speech/message/S3ResponseStream
      //   52: dup
      //   53: aload_1
      //   54: invokevirtual 85	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
      //   57: invokespecial 88	com/google/android/speech/message/S3ResponseStream:<init>	(Ljava/io/InputStream;)V
      //   60: astore_3
      //   61: aload_0
      //   62: getfield 34	com/google/android/speech/network/PairHttpConnection$DownloadThread:mRunning	Z
      //   65: ifeq +43 -> 108
      //   68: aload_3
      //   69: invokevirtual 92	com/google/android/speech/message/S3ResponseStream:read	()Lcom/google/speech/s3/S3$S3Response;
      //   72: astore 7
      //   74: aload_0
      //   75: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   78: getstatic 95	com/google/android/speech/network/PairHttpConnection$ResponseState:DOWN	Lcom/google/android/speech/network/PairHttpConnection$ResponseState;
      //   81: invokevirtual 58	com/google/android/speech/network/PairHttpConnection:setResponseState	(Lcom/google/android/speech/network/PairHttpConnection$ResponseState;)Z
      //   84: ifne +33 -> 117
      //   87: aload_0
      //   88: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   91: invokestatic 62	com/google/android/speech/network/PairHttpConnection:access$400	(Lcom/google/android/speech/network/PairHttpConnection;)Lcom/google/android/speech/callback/Callback;
      //   94: new 64	com/google/android/speech/exception/NetworkRecognizeException
      //   97: dup
      //   98: ldc 97
      //   100: invokespecial 67	com/google/android/speech/exception/NetworkRecognizeException:<init>	(Ljava/lang/String;)V
      //   103: invokeinterface 73 2 0
      //   108: aload_3
      //   109: invokestatic 103	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
      //   112: aload_1
      //   113: invokevirtual 79	java/net/HttpURLConnection:disconnect	()V
      //   116: return
      //   117: aload 7
      //   119: invokevirtual 109	com/google/speech/s3/S3$S3Response:getStatus	()I
      //   122: iconst_2
      //   123: if_icmpeq +12 -> 135
      //   126: aload 7
      //   128: invokevirtual 109	com/google/speech/s3/S3$S3Response:getStatus	()I
      //   131: iconst_1
      //   132: if_icmpne +8 -> 140
      //   135: aload_0
      //   136: iconst_0
      //   137: putfield 34	com/google/android/speech/network/PairHttpConnection$DownloadThread:mRunning	Z
      //   140: aload_0
      //   141: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   144: invokestatic 62	com/google/android/speech/network/PairHttpConnection:access$400	(Lcom/google/android/speech/network/PairHttpConnection;)Lcom/google/android/speech/callback/Callback;
      //   147: aload 7
      //   149: invokeinterface 112 2 0
      //   154: goto -93 -> 61
      //   157: astore 5
      //   159: aload_3
      //   160: astore_2
      //   161: aload_0
      //   162: getfield 34	com/google/android/speech/network/PairHttpConnection$DownloadThread:mRunning	Z
      //   165: ifeq +68 -> 233
      //   168: ldc 114
      //   170: new 116	java/lang/StringBuilder
      //   173: dup
      //   174: invokespecial 118	java/lang/StringBuilder:<init>	()V
      //   177: ldc 120
      //   179: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   182: aload 5
      //   184: invokevirtual 128	java/io/IOException:getMessage	()Ljava/lang/String;
      //   187: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   190: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   193: invokestatic 137	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   196: pop
      //   197: aload_0
      //   198: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   201: getstatic 52	com/google/android/speech/network/PairHttpConnection$ResponseState:DOWN_ERROR	Lcom/google/android/speech/network/PairHttpConnection$ResponseState;
      //   204: invokevirtual 58	com/google/android/speech/network/PairHttpConnection:setResponseState	(Lcom/google/android/speech/network/PairHttpConnection$ResponseState;)Z
      //   207: ifne +26 -> 233
      //   210: aload_0
      //   211: getfield 18	com/google/android/speech/network/PairHttpConnection$DownloadThread:this$0	Lcom/google/android/speech/network/PairHttpConnection;
      //   214: invokestatic 62	com/google/android/speech/network/PairHttpConnection:access$400	(Lcom/google/android/speech/network/PairHttpConnection;)Lcom/google/android/speech/callback/Callback;
      //   217: new 64	com/google/android/speech/exception/NetworkRecognizeException
      //   220: dup
      //   221: ldc 139
      //   223: aload 5
      //   225: invokespecial 142	com/google/android/speech/exception/NetworkRecognizeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   228: invokeinterface 73 2 0
      //   233: aload_2
      //   234: invokestatic 103	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
      //   237: aload_1
      //   238: invokevirtual 79	java/net/HttpURLConnection:disconnect	()V
      //   241: return
      //   242: astore 4
      //   244: aload_2
      //   245: invokestatic 103	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
      //   248: aload_1
      //   249: invokevirtual 79	java/net/HttpURLConnection:disconnect	()V
      //   252: aload 4
      //   254: athrow
      //   255: astore 4
      //   257: aload_3
      //   258: astore_2
      //   259: goto -15 -> 244
      //   262: astore 5
      //   264: aconst_null
      //   265: astore_2
      //   266: goto -105 -> 161
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	269	0	this	DownloadThread
      //   0	269	1	paramHttpURLConnection	HttpURLConnection
      //   48	218	2	localObject1	Object
      //   60	198	3	localS3ResponseStream	S3ResponseStream
      //   242	11	4	localObject2	Object
      //   255	1	4	localObject3	Object
      //   157	67	5	localIOException1	IOException
      //   262	1	5	localIOException2	IOException
      //   72	76	7	localS3Response	S3.S3Response
      // Exception table:
      //   from	to	target	type
      //   61	108	157	java/io/IOException
      //   117	135	157	java/io/IOException
      //   135	140	157	java/io/IOException
      //   140	154	157	java/io/IOException
      //   49	61	242	finally
      //   161	233	242	finally
      //   61	108	255	finally
      //   117	135	255	finally
      //   135	140	255	finally
      //   140	154	255	finally
      //   49	61	262	java/io/IOException
    }
    
    private boolean verifyResponseCodeAndUrl(@Nonnull HttpURLConnection paramHttpURLConnection)
    {
      try
      {
        int i = paramHttpURLConnection.getResponseCode();
        if (i != 200)
        {
          Log.w("PairHttpConnection", "[Download] response code " + i);
          return false;
        }
      }
      catch (IOException localIOException)
      {
        Log.w("PairHttpConnection", "[Download] Error opening connection: " + localIOException.getMessage());
        return false;
      }
      if (!this.mDownUrl.getHost().equals(paramHttpURLConnection.getURL().getHost()))
      {
        Log.w("PairHttpConnection", "[Download] redirect to " + paramHttpURLConnection.getURL());
        return false;
      }
      return true;
    }
    
    public void close()
    {
      this.mRunning = false;
      interrupt();
    }
    
    public void run()
    {
      HttpURLConnection localHttpURLConnection = null;
      try
      {
        localHttpURLConnection = PairHttpConnection.this.mConnectionFactory.openHttpConnection(PairHttpConnection.this.mPairHttpServerInfo.getDown(), this.mDownUrl);
        IoUtils.addHttpHeaders(localHttpURLConnection, PairHttpConnection.this.mPairHttpServerInfo.getDown());
        PairHttpConnection.this.maybeSetCompressionHeader(localHttpURLConnection, PairHttpConnection.this.mPairHttpServerInfo.getDown());
        localHttpURLConnection.setDoInput(true);
        localHttpURLConnection.setUseCaches(false);
        NetworkUtils.connect(localHttpURLConnection);
        this.mConnectedLatch.countDown();
        runDownloadLoop((HttpURLConnection)Preconditions.checkNotNull(localHttpURLConnection));
        return;
      }
      catch (IOException localIOException)
      {
        if (localHttpURLConnection != null) {
          localHttpURLConnection.disconnect();
        }
        PairHttpConnection.this.setResponseState(PairHttpConnection.ResponseState.DOWN_ERROR);
        this.mRunning = false;
        return;
      }
      finally
      {
        this.mConnectedLatch.countDown();
      }
    }
    
    public boolean waitForConnection()
    {
      boolean bool;
      if (getState() != Thread.State.NEW) {
        bool = true;
      }
      for (;;)
      {
        Preconditions.checkState(bool);
        try
        {
          if (!this.mConnectedLatch.await(10L, TimeUnit.SECONDS)) {
            this.mRunning = false;
          }
          return this.mRunning;
          bool = false;
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;)
          {
            this.mRunning = false;
          }
        }
      }
    }
  }
  
  static enum ResponseState
  {
    static
    {
      DOWN = new ResponseState("DOWN", 2);
      DOWN_ERROR = new ResponseState("DOWN_ERROR", 3);
      ResponseState[] arrayOfResponseState = new ResponseState[4];
      arrayOfResponseState[0] = UNKNOWN;
      arrayOfResponseState[1] = UP;
      arrayOfResponseState[2] = DOWN;
      arrayOfResponseState[3] = DOWN_ERROR;
      $VALUES = arrayOfResponseState;
    }
    
    private ResponseState() {}
  }
  
  protected class UpResponseThread
    extends Thread
    implements Closeable
  {
    @Nonnull
    private final Callback<S3.S3Response, RecognizeException> mCallback;
    private S3.S3Response mFirstResponse;
    private volatile boolean mRunning;
    @Nonnull
    private final S3ResponseStream mS3ResponseStream;
    @Nonnull
    private final HttpURLConnection mUploadConnection;
    
    public UpResponseThread(HttpURLConnection paramHttpURLConnection, Callback<S3.S3Response, RecognizeException> paramCallback, S3.S3Response paramS3Response)
    {
      super();
      this.mS3ResponseStream = paramHttpURLConnection;
      this.mUploadConnection = paramCallback;
      this.mCallback = paramS3Response;
      Object localObject;
      this.mFirstResponse = localObject;
      this.mRunning = true;
    }
    
    public void close()
    {
      this.mRunning = false;
      interrupt();
    }
    
    public void run()
    {
      try
      {
        for (;;)
        {
          if (!this.mRunning) {
            break label148;
          }
          if (this.mFirstResponse == null) {
            break;
          }
          localObject2 = this.mFirstResponse;
          this.mFirstResponse = null;
          if ((((S3.S3Response)localObject2).getStatus() == 2) || (((S3.S3Response)localObject2).getStatus() == 1)) {
            this.mRunning = false;
          }
          this.mCallback.onResult(localObject2);
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          if (this.mRunning)
          {
            Log.w("PairHttpConnection", "[Upload] exception - exit" + localIOException.getMessage());
            this.mCallback.onError(new NetworkRecognizeException("Error while reading", localIOException));
          }
          return;
          S3.S3Response localS3Response = this.mS3ResponseStream.read();
          Object localObject2 = localS3Response;
        }
        label148:
        return;
      }
      finally
      {
        Closeables.closeQuietly(this.mS3ResponseStream);
        this.mUploadConnection.disconnect();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.PairHttpConnection
 * JD-Core Version:    0.7.0.1
 */