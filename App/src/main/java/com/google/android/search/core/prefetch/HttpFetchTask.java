package com.google.android.search.core.prefetch;

import android.net.Uri;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.PelletDemultiplexer.ExtrasConsumer;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.complete.SuggestionFetcher.SuggestionResponse;
import com.google.android.search.core.google.gaia.LoginHelper.AuthToken;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.velvet.ActionData;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import java.io.IOException;
import javax.annotation.Nullable;

class HttpFetchTask
  extends SearchResultFetcher.FetchTask
  implements PelletDemultiplexer.ExtrasConsumer
{
  private final String mAccountName;
  private boolean mComplete;
  private final SearchConfig mConfig;
  private boolean mFailed;
  private boolean mHeadersSet;
  private final NamingDelayedTaskExecutor mHttpExecutor;
  private final HttpHelper mHttpHelper;
  private AsyncHttpResponse mHttpResponse;
  private final boolean mIncludeSuggestions;
  private final Object mLock = new Object();
  private final Query mQuery;
  private final AsyncHttpResponse.ResponseListener mResponseListener = new AsyncHttpResponse.ResponseListener()
  {
    public void onResponseChanged()
    {
      boolean bool1 = true;
      boolean bool2;
      label261:
      label303:
      do
      {
        for (;;)
        {
          synchronized (HttpFetchTask.this.mLock)
          {
            int i;
            if ((!HttpFetchTask.this.mHeadersSet) && (HttpFetchTask.this.mHttpResponse.hasHeaders()))
            {
              HttpFetchTask.access$102(HttpFetchTask.this, true);
              i = 1;
              if ((HttpFetchTask.this.mFailed) || (HttpFetchTask.this.mComplete)) {
                continue;
              }
              HttpFetchTask.access$302(HttpFetchTask.this, HttpFetchTask.this.mHttpResponse.isFailed());
              HttpFetchTask.access$402(HttpFetchTask.this, HttpFetchTask.this.mHttpResponse.isComplete());
              if ((!HttpFetchTask.this.mFailed) || (!HttpFetchTask.this.mComplete))
              {
                Preconditions.checkState(bool1);
                bool3 = HttpFetchTask.this.mFailed;
                bool2 = HttpFetchTask.this.mComplete;
                if (i != 0)
                {
                  if (!HttpFetchTask.this.mHttpResponse.hasIoException()) {
                    break label261;
                  }
                  HttpFetchTask.this.getConsumer().setFailed(HttpFetchTask.this.mHttpResponse.getIoException());
                }
                if (!bool3) {
                  break;
                }
                SearchResultFetcher.FetchTaskConsumer localFetchTaskConsumer = HttpFetchTask.this.getConsumer();
                if (!HttpFetchTask.this.mHttpResponse.hasIoException()) {
                  break label303;
                }
                localIOException = HttpFetchTask.this.mHttpResponse.getIoException();
                localFetchTaskConsumer.setFailed(localIOException);
              }
            }
            else
            {
              i = 0;
              continue;
            }
            bool1 = false;
            continue;
            bool2 = false;
            boolean bool3 = false;
          }
          HttpFetchTask.this.getConsumer().setWebPage(new WebPage(HttpFetchTask.this.mHttpResponse.getHeaders(), HttpFetchTask.this.mHttpResponse.getInputStream()));
          continue;
          IOException localIOException = new IOException("Failed HttpResponse.");
        }
      } while (!bool2);
      HttpFetchTask.this.getConsumer().setComplete();
    }
  };
  private final SdchManager mSdchManager;
  private final String mSpeechRequestId;
  private final LoginHelper.AuthToken mSuggestionsAuthToken;
  private volatile String mUrlDebug;
  private final SearchUrlHelper mUrlHelper;
  
  HttpFetchTask(Query paramQuery, String paramString1, String paramString2, boolean paramBoolean, LoginHelper.AuthToken paramAuthToken, SearchConfig paramSearchConfig, HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper, NamingDelayedTaskExecutor paramNamingDelayedTaskExecutor, SdchManager paramSdchManager)
  {
    this.mQuery = paramQuery;
    this.mSpeechRequestId = paramString1;
    this.mAccountName = paramString2;
    this.mIncludeSuggestions = paramBoolean;
    this.mSuggestionsAuthToken = paramAuthToken;
    this.mConfig = paramSearchConfig;
    this.mHttpHelper = paramHttpHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mHttpExecutor = paramNamingDelayedTaskExecutor;
    this.mSdchManager = paramSdchManager;
  }
  
  private HttpHelper.GetRequest createRequest()
  {
    UriRequest localUriRequest;
    HttpHelper.GetRequest localGetRequest;
    if (this.mIncludeSuggestions)
    {
      localUriRequest = this.mUrlHelper.getSuggestionAndPrefetchRequest(this.mQuery, this.mSuggestionsAuthToken);
      localGetRequest = new HttpHelper.GetRequest(localUriRequest.getUri().toString(), localUriRequest.getHeaders());
      localGetRequest.setUseSpdy(this.mConfig.isSpdyForSearchResultFetchesEnabled());
      if ((!this.mIncludeSuggestions) && (!this.mUrlHelper.shouldRequestPelletResponse(this.mQuery))) {
        break label102;
      }
    }
    label102:
    for (boolean bool = true;; bool = false)
    {
      localGetRequest.setUsePellets(bool);
      return localGetRequest;
      localUriRequest = this.mUrlHelper.getSearchRequest(this.mQuery, this.mSpeechRequestId);
      break;
    }
  }
  
  @Nullable
  private AsyncHttpResponse fetchHttpResponse(HttpHelper.GetRequest paramGetRequest)
    throws IOException
  {
    AsyncFetcher localAsyncFetcher;
    if (paramGetRequest.getUsePellets())
    {
      localAsyncFetcher = AsyncFetcher.createForPelletizedResponse(this.mHttpExecutor, this.mConfig.getMaxGwsResponseSizeBytes(), this.mConfig.getSuggestionPelletPath(), this, this.mSdchManager);
      if (!this.mQuery.isPrefetch()) {
        break label92;
      }
    }
    label92:
    for (int i = 10;; i = 11)
    {
      return (AsyncHttpResponse)this.mHttpHelper.get(paramGetRequest, i, localAsyncFetcher);
      localAsyncFetcher = AsyncFetcher.createForNonPelletizedResponse(this.mHttpExecutor, this.mConfig.getMaxGwsResponseSizeBytes(), this.mConfig.getSuggestionPelletPath(), this.mSdchManager);
      break;
    }
  }
  
  public void cancel()
  {
    synchronized (this.mLock)
    {
      if ((!this.mComplete) && (!this.mFailed))
      {
        this.mFailed = true;
        Closeables.closeQuietly(this.mHttpResponse);
      }
      return;
    }
  }
  
  AsyncHttpResponse.ResponseListener getResponseListener()
  {
    return this.mResponseListener;
  }
  
  boolean isFailed()
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mFailed;
      return bool;
    }
  }
  
  public void onActionDataFinished(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (ActionData localActionData = ActionData.ANSWER_IN_SRP;; localActionData = ActionData.NONE)
    {
      onCardReceived(localActionData);
      return;
    }
  }
  
  public void onActionDataReceived(ActionData paramActionData)
  {
    onCardReceived(paramActionData);
  }
  
  public void onSrpMetadata(SearchResult.SrpMetadata paramSrpMetadata)
  {
    getConsumer().setSrpMetadata(paramSrpMetadata);
  }
  
  public void onSrpQuery(String paramString)
  {
    getConsumer().setSrpQuery(paramString);
  }
  
  public void onSuggestions(String paramString1, String paramString2)
  {
    getConsumer().setSuggestions(new SuggestionFetcher.SuggestionResponse(paramString1, paramString2, this.mAccountName));
  }
  
  /* Error */
  public void startFetch(@javax.annotation.Nonnull SearchResultFetcher.FetchTaskConsumer paramFetchTaskConsumer)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 237	com/google/android/search/core/prefetch/SearchResultFetcher$FetchTask:startFetch	(Lcom/google/android/search/core/prefetch/SearchResultFetcher$FetchTaskConsumer;)V
    //   5: aload_0
    //   6: invokevirtual 239	com/google/android/search/core/prefetch/HttpFetchTask:isFailed	()Z
    //   9: istore 5
    //   11: aconst_null
    //   12: astore 6
    //   14: iload 5
    //   16: ifne +30 -> 46
    //   19: aload_0
    //   20: invokespecial 241	com/google/android/search/core/prefetch/HttpFetchTask:createRequest	()Lcom/google/android/search/core/util/HttpHelper$GetRequest;
    //   23: astore 7
    //   25: aload_0
    //   26: aload 7
    //   28: invokevirtual 244	com/google/android/search/core/util/HttpHelper$GetRequest:getUrl	()Ljava/lang/String;
    //   31: putfield 246	com/google/android/search/core/prefetch/HttpFetchTask:mUrlDebug	Ljava/lang/String;
    //   34: aload_0
    //   35: aload 7
    //   37: invokespecial 248	com/google/android/search/core/prefetch/HttpFetchTask:fetchHttpResponse	(Lcom/google/android/search/core/util/HttpHelper$GetRequest;)Lcom/google/android/search/core/prefetch/AsyncHttpResponse;
    //   40: astore 11
    //   42: aload 11
    //   44: astore 6
    //   46: aload_0
    //   47: getfield 45	com/google/android/search/core/prefetch/HttpFetchTask:mLock	Ljava/lang/Object;
    //   50: astore 9
    //   52: aload 9
    //   54: monitorenter
    //   55: aload_0
    //   56: aload 6
    //   58: putfield 84	com/google/android/search/core/prefetch/HttpFetchTask:mHttpResponse	Lcom/google/android/search/core/prefetch/AsyncHttpResponse;
    //   61: aload 6
    //   63: ifnonnull +8 -> 71
    //   66: aload_0
    //   67: iconst_1
    //   68: putfield 87	com/google/android/search/core/prefetch/HttpFetchTask:mFailed	Z
    //   71: aload 9
    //   73: monitorexit
    //   74: aload 6
    //   76: ifnull +12 -> 88
    //   79: aload 6
    //   81: aload_0
    //   82: getfield 52	com/google/android/search/core/prefetch/HttpFetchTask:mResponseListener	Lcom/google/android/search/core/prefetch/AsyncHttpResponse$ResponseListener;
    //   85: invokevirtual 252	com/google/android/search/core/prefetch/AsyncHttpResponse:setListener	(Lcom/google/android/search/core/prefetch/AsyncHttpResponse$ResponseListener;)V
    //   88: return
    //   89: astore 8
    //   91: aload_1
    //   92: aload 8
    //   94: invokeinterface 256 2 0
    //   99: aconst_null
    //   100: astore 6
    //   102: goto -56 -> 46
    //   105: astore_2
    //   106: aload_0
    //   107: getfield 45	com/google/android/search/core/prefetch/HttpFetchTask:mLock	Ljava/lang/Object;
    //   110: astore_3
    //   111: aload_3
    //   112: monitorenter
    //   113: aload_0
    //   114: aconst_null
    //   115: putfield 84	com/google/android/search/core/prefetch/HttpFetchTask:mHttpResponse	Lcom/google/android/search/core/prefetch/AsyncHttpResponse;
    //   118: iconst_0
    //   119: ifne +8 -> 127
    //   122: aload_0
    //   123: iconst_1
    //   124: putfield 87	com/google/android/search/core/prefetch/HttpFetchTask:mFailed	Z
    //   127: aload_3
    //   128: monitorexit
    //   129: iconst_0
    //   130: ifeq +11 -> 141
    //   133: aconst_null
    //   134: aload_0
    //   135: getfield 52	com/google/android/search/core/prefetch/HttpFetchTask:mResponseListener	Lcom/google/android/search/core/prefetch/AsyncHttpResponse$ResponseListener;
    //   138: invokevirtual 252	com/google/android/search/core/prefetch/AsyncHttpResponse:setListener	(Lcom/google/android/search/core/prefetch/AsyncHttpResponse$ResponseListener;)V
    //   141: aload_2
    //   142: athrow
    //   143: astore 10
    //   145: aload 9
    //   147: monitorexit
    //   148: aload 10
    //   150: athrow
    //   151: astore 4
    //   153: aload_3
    //   154: monitorexit
    //   155: aload 4
    //   157: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	158	0	this	HttpFetchTask
    //   0	158	1	paramFetchTaskConsumer	SearchResultFetcher.FetchTaskConsumer
    //   105	37	2	localObject1	Object
    //   151	5	4	localObject3	Object
    //   9	6	5	bool	boolean
    //   12	89	6	localObject4	Object
    //   23	13	7	localGetRequest	HttpHelper.GetRequest
    //   89	4	8	localIOException	IOException
    //   143	6	10	localObject6	Object
    //   40	3	11	localAsyncHttpResponse	AsyncHttpResponse
    // Exception table:
    //   from	to	target	type
    //   34	42	89	java/io/IOException
    //   5	11	105	finally
    //   19	34	105	finally
    //   34	42	105	finally
    //   91	99	105	finally
    //   55	61	143	finally
    //   66	71	143	finally
    //   71	74	143	finally
    //   145	148	143	finally
    //   113	118	151	finally
    //   122	127	151	finally
    //   127	129	151	finally
    //   153	155	151	finally
  }
  
  public String toString()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mHttpResponse == null)
        {
          str1 = "not started";
          String str2 = "HttpFetchTask{" + str1 + "}";
          return str2;
        }
        if (this.mHttpResponse.isComplete()) {
          str1 = "complete";
        }
      }
      String str1 = "not complete";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.HttpFetchTask
 * JD-Core Version:    0.7.0.1
 */