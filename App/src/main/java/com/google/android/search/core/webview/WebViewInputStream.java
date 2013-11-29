package com.google.android.search.core.webview;

import android.util.Log;
import com.google.android.search.core.WebSearchConnectionError;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.shared.api.Query;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

class WebViewInputStream
  extends InputStream
{
  private final InputStream mDelegate;
  private final Executor mErrorReportingThread;
  private final Query mQuery;
  private final QueryState mQueryState;
  private final Runnable mReportConnectionError = new Runnable()
  {
    public void run()
    {
      WebViewInputStream.this.mQueryState.resultsPageError(WebViewInputStream.this.mQuery, new WebSearchConnectionError(444, "No Response"));
    }
  };
  
  WebViewInputStream(Query paramQuery, InputStream paramInputStream, QueryState paramQueryState, Executor paramExecutor)
  {
    this.mQuery = paramQuery;
    this.mDelegate = paramInputStream;
    this.mQueryState = paramQueryState;
    this.mErrorReportingThread = paramExecutor;
  }
  
  private void reportErrorFromInterceptedStream(IOException paramIOException)
  {
    Log.e("Velvet.WebViewInputStream", "Error reported from delegate stream: " + paramIOException.getMessage());
    this.mErrorReportingThread.execute(this.mReportConnectionError);
  }
  
  public int available()
  {
    try
    {
      int i = this.mDelegate.available();
      return i;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
    }
    return 0;
  }
  
  public void close()
  {
    try
    {
      this.mDelegate.close();
      return;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
    }
  }
  
  public int read()
  {
    try
    {
      int i = this.mDelegate.read();
      return i;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
    }
    return -1;
  }
  
  public int read(byte[] paramArrayOfByte)
  {
    try
    {
      int i = this.mDelegate.read(paramArrayOfByte);
      return i;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
    }
    return -1;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      int i = this.mDelegate.read(paramArrayOfByte, paramInt1, paramInt2);
      return i;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
    }
    return -1;
  }
  
  public void reset()
    throws IOException
  {
    try
    {
      this.mDelegate.reset();
      return;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
      throw localIOException;
    }
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    try
    {
      long l = this.mDelegate.skip(paramLong);
      return l;
    }
    catch (IOException localIOException)
    {
      reportErrorFromInterceptedStream(localIOException);
      throw localIOException;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.webview.WebViewInputStream
 * JD-Core Version:    0.7.0.1
 */