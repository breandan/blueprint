package com.google.android.search.core.prefetch;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class AsyncHttpResponseFetcher
{
  private final ExecutorService mHttpExecutor;
  private final HttpHelper mHttpHelper;
  private final SdchManager mSdchManager;
  private final SearchConfig mSearchConfig;
  
  public AsyncHttpResponseFetcher(SearchConfig paramSearchConfig, ExecutorService paramExecutorService, HttpHelper paramHttpHelper, SdchManager paramSdchManager)
  {
    this.mSearchConfig = paramSearchConfig;
    this.mHttpExecutor = paramExecutorService;
    this.mHttpHelper = paramHttpHelper;
    this.mSdchManager = paramSdchManager;
  }
  
  public AsyncHttpResponse get(HttpHelper.GetRequest paramGetRequest, int paramInt)
    throws IOException
  {
    AsyncFetcher localAsyncFetcher = AsyncFetcher.createForNonPelletizedResponse(this.mHttpExecutor, this.mSearchConfig.getMaxGwsResponseSizeBytes(), this.mSearchConfig.getSuggestionPelletPath(), this.mSdchManager);
    return (AsyncHttpResponse)this.mHttpHelper.get(paramGetRequest, paramInt, localAsyncFetcher);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.AsyncHttpResponseFetcher
 * JD-Core Version:    0.7.0.1
 */