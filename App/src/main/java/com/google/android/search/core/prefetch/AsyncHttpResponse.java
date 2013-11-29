package com.google.android.search.core.prefetch;

import android.util.Log;
import com.google.android.search.core.util.EagerBufferedInputStream;
import com.google.android.search.core.util.EagerBufferedInputStream.BufferTaskListener;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.JavaNetHttpHelper;
import com.google.android.search.core.util.JavaNetHttpHelper.MimeTypeAndCharSet;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AsyncHttpResponse
  implements EagerBufferedInputStream.BufferTaskListener, Closeable
{
  private boolean mChanged;
  private boolean mComplete;
  private boolean mFailed;
  private Map<String, List<String>> mHeaders;
  @Nonnull
  private final InputStream mInputStream;
  private IOException mIoException;
  private ResponseListener mListener;
  private final Object mLock = new Object();
  @Nonnull
  private final HttpChunkProducer mProducer;
  
  public AsyncHttpResponse(@Nonnull HttpChunkProducer paramHttpChunkProducer)
  {
    this.mProducer = ((HttpChunkProducer)Preconditions.checkNotNull(paramHttpChunkProducer));
    this.mInputStream = EagerBufferedInputStream.newStream(paramHttpChunkProducer, this);
  }
  
  private final void notifyListener()
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mChanged;
      ResponseListener localResponseListener1 = null;
      if (bool)
      {
        ResponseListener localResponseListener2 = this.mListener;
        localResponseListener1 = null;
        if (localResponseListener2 != null)
        {
          localResponseListener1 = this.mListener;
          this.mChanged = false;
        }
      }
      if (localResponseListener1 != null) {
        localResponseListener1.onResponseChanged();
      }
      return;
    }
  }
  
  public void close()
  {
    this.mProducer.close();
  }
  
  public JavaNetHttpHelper.MimeTypeAndCharSet getContentTypeIfAvailable()
  {
    try
    {
      if (!hasHeaders()) {
        return null;
      }
      List localList = (List)getHeaders().get("Content-Type");
      if ((localList != null) && (!localList.isEmpty()))
      {
        JavaNetHttpHelper.MimeTypeAndCharSet localMimeTypeAndCharSet = JavaNetHttpHelper.parseContentTypeHeader((String)localList.get(0));
        return localMimeTypeAndCharSet;
      }
      return null;
    }
    catch (Exception localException)
    {
      Log.w("Velvet.AsyncHttpResponse", "Failed to parse Content-Type header");
    }
    return null;
  }
  
  public Map<String, List<String>> getHeaders()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mHeaders != null)
        {
          bool = true;
          Preconditions.checkState(bool);
          Map localMap = this.mHeaders;
          return localMap;
        }
      }
      boolean bool = false;
    }
  }
  
  AsyncFetcher.HttpHeadersListener getHeadersListener()
  {
    new AsyncFetcher.HttpHeadersListener()
    {
      public void onConnected(Map<String, List<String>> paramAnonymousMap, @Nullable HttpHelper.HttpException paramAnonymousHttpException)
      {
        boolean bool = true;
        synchronized (AsyncHttpResponse.this.mLock)
        {
          if ((AsyncHttpResponse.this.mHeaders == null) && (AsyncHttpResponse.this.mIoException == null))
          {
            Preconditions.checkState(bool);
            AsyncHttpResponse.access$102(AsyncHttpResponse.this, (Map)Preconditions.checkNotNull(paramAnonymousMap));
            AsyncHttpResponse.access$202(AsyncHttpResponse.this, paramAnonymousHttpException);
            AsyncHttpResponse.access$302(AsyncHttpResponse.this, true);
            AsyncHttpResponse.this.notifyListener();
            return;
          }
          bool = false;
        }
      }
      
      public void onFailure(IOException paramAnonymousIOException)
      {
        boolean bool = true;
        synchronized (AsyncHttpResponse.this.mLock)
        {
          if ((AsyncHttpResponse.this.mHeaders == null) && (AsyncHttpResponse.this.mIoException == null))
          {
            Preconditions.checkState(bool);
            AsyncHttpResponse.access$202(AsyncHttpResponse.this, (IOException)Preconditions.checkNotNull(paramAnonymousIOException));
            AsyncHttpResponse.access$302(AsyncHttpResponse.this, true);
            AsyncHttpResponse.this.notifyListener();
            return;
          }
          bool = false;
        }
      }
    };
  }
  
  public InputStream getInputStream()
  {
    return this.mInputStream;
  }
  
  public IOException getIoException()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mIoException != null)
        {
          bool = true;
          Preconditions.checkState(bool);
          IOException localIOException = this.mIoException;
          return localIOException;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean hasHeaders()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mHeaders != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean hasIoException()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mIoException != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean isComplete()
  {
    return this.mComplete;
  }
  
  public boolean isFailed()
  {
    return this.mFailed;
  }
  
  public void onComplete()
  {
    boolean bool1 = true;
    synchronized (this.mLock)
    {
      if (!this.mFailed) {}
      for (boolean bool2 = bool1;; bool2 = false)
      {
        Preconditions.checkState(bool2);
        if (this.mComplete) {
          break;
        }
        this.mChanged = bool1;
        this.mComplete = true;
        notifyListener();
        return;
      }
      bool1 = false;
    }
  }
  
  public void onFailed()
  {
    boolean bool1 = true;
    synchronized (this.mLock)
    {
      if (!this.mComplete) {}
      for (boolean bool2 = bool1;; bool2 = false)
      {
        Preconditions.checkState(bool2);
        if (this.mFailed) {
          break;
        }
        this.mChanged = bool1;
        this.mFailed = true;
        notifyListener();
        return;
      }
      bool1 = false;
    }
  }
  
  public void setListener(@Nonnull ResponseListener paramResponseListener)
  {
    synchronized (this.mLock)
    {
      if (this.mListener == null)
      {
        bool = true;
        Preconditions.checkState(bool);
        this.mListener = ((ResponseListener)Preconditions.checkNotNull(paramResponseListener));
        notifyListener();
        return;
      }
      boolean bool = false;
    }
  }
  
  public String toString()
  {
    return "AsyncHttpResponse{" + this.mInputStream + "}";
  }
  
  public static abstract interface ResponseListener
  {
    public abstract void onResponseChanged();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.AsyncHttpResponse
 * JD-Core Version:    0.7.0.1
 */