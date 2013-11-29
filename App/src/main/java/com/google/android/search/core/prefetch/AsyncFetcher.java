package com.google.android.search.core.prefetch;

import android.net.TrafficStats;
import com.google.android.search.core.google.PelletDemultiplexer.ExtrasConsumer;
import com.google.android.search.core.google.PelletHttpChunkProducer;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.HttpResponseFetcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AsyncFetcher
  extends HttpResponseFetcher<AsyncHttpResponse>
{
  private final ExecutorService mExecutor;
  private final PelletDemultiplexer.ExtrasConsumer mExtrasConsumer;
  private final int mMaxResponseBytes;
  private final SdchManager mSdchManager;
  private final String mSuggestionPelletPath;
  
  private AsyncFetcher(ExecutorService paramExecutorService, int paramInt, String paramString, PelletDemultiplexer.ExtrasConsumer paramExtrasConsumer, SdchManager paramSdchManager)
  {
    this.mExecutor = paramExecutorService;
    this.mMaxResponseBytes = paramInt;
    this.mSuggestionPelletPath = paramString;
    this.mExtrasConsumer = paramExtrasConsumer;
    this.mSdchManager = paramSdchManager;
  }
  
  public static AsyncFetcher createForNonPelletizedResponse(ExecutorService paramExecutorService, int paramInt, String paramString, SdchManager paramSdchManager)
  {
    return new AsyncFetcher(paramExecutorService, paramInt, paramString, null, paramSdchManager);
  }
  
  public static AsyncFetcher createForPelletizedResponse(ExecutorService paramExecutorService, int paramInt, String paramString, PelletDemultiplexer.ExtrasConsumer paramExtrasConsumer, SdchManager paramSdchManager)
  {
    return new AsyncFetcher(paramExecutorService, paramInt, paramString, paramExtrasConsumer, paramSdchManager);
  }
  
  private HttpChunkProducer createProducerFor(HttpURLConnection paramHttpURLConnection, InputSupplier<InputStream> paramInputSupplier)
  {
    if (this.mExtrasConsumer == null) {
      return new HttpChunkProducer(paramHttpURLConnection, this.mExecutor, this.mMaxResponseBytes, paramInputSupplier);
    }
    return new PelletHttpChunkProducer(paramHttpURLConnection, this.mExecutor, this.mMaxResponseBytes, this.mSuggestionPelletPath, paramInputSupplier, this.mExtrasConsumer);
  }
  
  private static Map<String, List<String>> makeHeadersNonNull(@Nullable Map<String, List<String>> paramMap)
  {
    if (paramMap == null) {
      paramMap = ImmutableMap.of();
    }
    return paramMap;
  }
  
  public AsyncHttpResponse fetchResponse(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
  {
    HttpConnectionInputSupplier localHttpConnectionInputSupplier = new HttpConnectionInputSupplier(paramHttpURLConnection, paramArrayOfByte, paramInt);
    AsyncHttpResponse localAsyncHttpResponse = new AsyncHttpResponse(createProducerFor(paramHttpURLConnection, localHttpConnectionInputSupplier));
    localHttpConnectionInputSupplier.setHttpHeadersListener(localAsyncHttpResponse.getHeadersListener());
    return localAsyncHttpResponse;
  }
  
  class HttpConnectionInputSupplier
    implements InputSupplier<InputStream>
  {
    private final HttpURLConnection mConnection;
    private IOException mException;
    private Map<String, List<String>> mHeaders;
    private AsyncFetcher.HttpHeadersListener mHeadersListener;
    private final AtomicBoolean mInputStreamAvailable = new AtomicBoolean(true);
    private final Object mListenerLock = new Object();
    private boolean mNotified;
    private final byte[] mRequestContent;
    private final int mTrafficTag;
    
    HttpConnectionInputSupplier(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
    {
      this.mConnection = paramHttpURLConnection;
      this.mRequestContent = paramArrayOfByte;
      this.mTrafficTag = paramInt;
    }
    
    private void notifyListener()
    {
      IOException localIOException1;
      AsyncFetcher.HttpHeadersListener localHttpHeadersListener1;
      synchronized (this.mListenerLock)
      {
        boolean bool = this.mNotified;
        localIOException1 = null;
        Map localMap = null;
        localHttpHeadersListener1 = null;
        if (!bool)
        {
          AsyncFetcher.HttpHeadersListener localHttpHeadersListener2 = this.mHeadersListener;
          localIOException1 = null;
          localMap = null;
          localHttpHeadersListener1 = null;
          if (localHttpHeadersListener2 != null) {
            if (this.mHeaders == null)
            {
              IOException localIOException2 = this.mException;
              localIOException1 = null;
              localMap = null;
              localHttpHeadersListener1 = null;
              if (localIOException2 == null) {}
            }
            else
            {
              this.mNotified = true;
              localHttpHeadersListener1 = this.mHeadersListener;
              localMap = this.mHeaders;
              localIOException1 = this.mException;
            }
          }
        }
        if (localHttpHeadersListener1 != null)
        {
          if (localMap != null) {
            localHttpHeadersListener1.onConnected(localMap, (HttpHelper.HttpException)localIOException1);
          }
        }
        else {
          return;
        }
      }
      localHttpHeadersListener1.onFailure(localIOException1);
    }
    
    private void recordFailure(IOException paramIOException)
    {
      synchronized (this.mListenerLock)
      {
        if ((this.mHeaders == null) && (this.mException == null))
        {
          bool = true;
          Preconditions.checkState(bool);
          this.mException = paramIOException;
          notifyListener();
          return;
        }
        boolean bool = false;
      }
    }
    
    private void recordHttpFailure(Map<String, List<String>> paramMap, HttpHelper.HttpException paramHttpException)
    {
      synchronized (this.mListenerLock)
      {
        if ((this.mHeaders == null) && (this.mException == null))
        {
          bool = true;
          Preconditions.checkState(bool);
          this.mHeaders = AsyncFetcher.makeHeadersNonNull(paramMap);
          this.mException = paramHttpException;
          notifyListener();
          return;
        }
        boolean bool = false;
      }
    }
    
    private void recordSuccess(Map<String, List<String>> paramMap)
    {
      synchronized (this.mListenerLock)
      {
        if ((this.mHeaders == null) && (this.mException == null))
        {
          bool = true;
          Preconditions.checkState(bool);
          this.mHeaders = AsyncFetcher.makeHeadersNonNull(paramMap);
          notifyListener();
          return;
        }
        boolean bool = false;
      }
    }
    
    private void setHttpHeadersListener(AsyncFetcher.HttpHeadersListener paramHttpHeadersListener)
    {
      synchronized (this.mListenerLock)
      {
        if (this.mHeadersListener == null)
        {
          bool = true;
          Preconditions.checkState(bool);
          this.mHeadersListener = ((AsyncFetcher.HttpHeadersListener)Preconditions.checkNotNull(paramHttpHeadersListener));
          notifyListener();
          return;
        }
        boolean bool = false;
      }
    }
    
    public InputStream getInput()
      throws IOException
    {
      Preconditions.checkState(this.mInputStreamAvailable.compareAndSet(true, false));
      try
      {
        TrafficStats.setThreadStatsTag(this.mTrafficTag);
        AsyncFetcher.this.mSdchManager.advertiseSdch(this.mConnection);
        AsyncFetcher.this.sendRequest(this.mConnection, this.mRequestContent);
        recordSuccess(this.mConnection.getHeaderFields());
        return AsyncFetcher.this.mSdchManager.maybeDecompressResponse(this.mConnection);
      }
      catch (HttpHelper.HttpException localHttpException)
      {
        for (;;)
        {
          recordHttpFailure(this.mConnection.getHeaderFields(), localHttpException);
        }
      }
      catch (IOException localIOException)
      {
        recordFailure(localIOException);
        throw localIOException;
      }
    }
  }
  
  static abstract interface HttpHeadersListener
  {
    public abstract void onConnected(Map<String, List<String>> paramMap, @Nullable HttpHelper.HttpException paramHttpException);
    
    public abstract void onFailure(IOException paramIOException);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.AsyncFetcher
 * JD-Core Version:    0.7.0.1
 */