package com.google.android.search.core.prefetch;

import android.accounts.Account;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.complete.SuggestionFetcher.SuggestionResponse;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.google.gaia.LoginHelper.AuthToken;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.speech.params.RequestIdGenerator;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchResultFetcher
{
  private final SearchResultCache mCache;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final NamingDelayedTaskExecutor mHttpExecutor;
  private final HttpHelper mHttpHelper;
  private final LoginHelper mLoginHelper;
  private final RequestIdGenerator mRequestIdGenerator;
  private final SdchManager mSdchManager;
  private final Executor mUiExecutor;
  private final SearchUrlHelper mUrlHelper;
  
  public SearchResultFetcher(SearchConfig paramSearchConfig, Clock paramClock, LoginHelper paramLoginHelper, SearchUrlHelper paramSearchUrlHelper, HttpHelper paramHttpHelper, SearchResultCache paramSearchResultCache, Executor paramExecutor, NamingDelayedTaskExecutor paramNamingDelayedTaskExecutor, RequestIdGenerator paramRequestIdGenerator, SdchManager paramSdchManager)
  {
    this.mConfig = ((SearchConfig)Preconditions.checkNotNull(paramSearchConfig));
    this.mClock = paramClock;
    this.mLoginHelper = paramLoginHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mHttpHelper = paramHttpHelper;
    this.mCache = ((SearchResultCache)Preconditions.checkNotNull(paramSearchResultCache));
    this.mUiExecutor = paramExecutor;
    this.mHttpExecutor = paramNamingDelayedTaskExecutor;
    this.mRequestIdGenerator = paramRequestIdGenerator;
    this.mSdchManager = paramSdchManager;
  }
  
  private SearchResult createPage(Query paramQuery, @Nullable String paramString, boolean paramBoolean, LoginHelper.AuthToken paramAuthToken)
  {
    Preconditions.checkArgument(paramQuery.isTextOrVoiceWebSearchWithQueryChars());
    long l = this.mClock.elapsedRealtime();
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null) {}
    HttpFetchTask localHttpFetchTask;
    for (String str = null;; str = localAccount.name)
    {
      localHttpFetchTask = new HttpFetchTask(paramQuery, paramString, str, paramBoolean, paramAuthToken, this.mConfig, this.mHttpHelper, this.mUrlHelper, this.mHttpExecutor, this.mSdchManager);
      if (!paramBoolean) {
        break;
      }
      return SearchResult.forSuggestionsAndSrp(paramQuery, l, localHttpFetchTask, this.mUiExecutor);
    }
    return SearchResult.forSrp(paramQuery, l, paramString, localHttpFetchTask, this.mUiExecutor);
  }
  
  @Nullable
  private SearchResult fetchSrp(Query paramQuery)
  {
    if (!paramQuery.isPrefetch()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (paramQuery.isTextOrVoiceWebSearchWithQueryChars()) {
        break;
      }
      return null;
    }
    String str = this.mRequestIdGenerator.newRequestId();
    EventLogger.logTextSearchStart(str);
    SearchResult localSearchResult = createPage(paramQuery, str, false, null);
    fetchThrottled(localSearchResult, false);
    return localSearchResult;
  }
  
  private void fetchThrottled(SearchResult paramSearchResult, boolean paramBoolean)
  {
    SearchResultCache localSearchResultCache = this.mCache;
    if (paramBoolean) {}
    long l2;
    for (long l1 = this.mClock.elapsedRealtime();; l1 = -1L)
    {
      l2 = localSearchResultCache.setWaitingSearch(paramSearchResult, l1);
      if (l2 != -1L) {
        break;
      }
      return;
    }
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        long l = SearchResultFetcher.this.mClock.elapsedRealtime();
        SearchResultFetcher.this.mCache.fetchWaitingPage(l);
      }
    };
    if (l2 == 0L)
    {
      this.mHttpExecutor.execute(local1);
      return;
    }
    Log.v("Velvet.SearchResultFetcher", "Throttling prefetch: waiting " + l2 + " ms");
    if (l2 > 1000L) {
      Log.w("Velvet.SearchResultFetcher", "Large delay (" + l2 + " ms). Is this an error?");
    }
    this.mHttpExecutor.executeDelayed(local1, l2);
  }
  
  public SearchResult fetchSuggestionsAndSrp(Query paramQuery, @Nullable LoginHelper.AuthToken paramAuthToken)
  {
    SearchResult localSearchResult = createPage(paramQuery, null, true, paramAuthToken);
    fetchThrottled(localSearchResult, false);
    return localSearchResult;
  }
  
  @Nullable
  public SearchResult obtainSearchResult(Query paramQuery)
  {
    SearchResult localSearchResult = this.mCache.get(paramQuery, this.mClock.elapsedRealtime(), true);
    if (localSearchResult == null) {
      localSearchResult = fetchSrp(paramQuery);
    }
    return localSearchResult;
  }
  
  public static abstract class FetchTask
  {
    private final List<ActionData> mActionBuffer = new ArrayList();
    private volatile SearchResultFetcher.FetchTaskConsumer mConsumer;
    
    abstract void cancel();
    
    protected final SearchResultFetcher.FetchTaskConsumer getConsumer()
    {
      if (this.mConsumer != null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        return this.mConsumer;
      }
    }
    
    protected void onCardReceived(ActionData paramActionData)
    {
      if ((paramActionData == ActionData.ANSWER_IN_SRP) || (paramActionData == ActionData.NONE))
      {
        if (this.mActionBuffer.isEmpty())
        {
          getConsumer().setActionData(paramActionData);
          return;
        }
        getConsumer().setActionData((ActionData)this.mActionBuffer.get(0));
        return;
      }
      if (!this.mActionBuffer.isEmpty()) {
        VelvetStrictMode.logWDeveloper("Velvet.SearchResultFetcher", "Multiple actions received. Using only the first.  New action=" + paramActionData + " Existing actions=" + this.mActionBuffer);
      }
      this.mActionBuffer.add(paramActionData);
    }
    
    public void startFetch(@Nonnull SearchResultFetcher.FetchTaskConsumer paramFetchTaskConsumer)
    {
      if (this.mConsumer == null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        this.mConsumer = ((SearchResultFetcher.FetchTaskConsumer)Preconditions.checkNotNull(paramFetchTaskConsumer));
        return;
      }
    }
  }
  
  public static abstract interface FetchTaskConsumer
  {
    public abstract void setActionData(ActionData paramActionData);
    
    public abstract void setComplete();
    
    public abstract void setFailed(IOException paramIOException);
    
    public abstract void setSrpMetadata(SearchResult.SrpMetadata paramSrpMetadata);
    
    public abstract void setSrpQuery(String paramString);
    
    public abstract void setSuggestions(SuggestionFetcher.SuggestionResponse paramSuggestionResponse);
    
    public abstract void setWebPage(WebPage paramWebPage);
  }
  
  public static class ResponseFetchException
    extends IOException
  {
    public ResponseFetchException(String paramString)
    {
      super();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.SearchResultFetcher
 * JD-Core Version:    0.7.0.1
 */