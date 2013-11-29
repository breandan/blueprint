package com.google.android.search.core.google.complete;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.google.gaia.LoginHelper.AuthToken;
import com.google.android.search.core.prefetch.SearchResult;
import com.google.android.search.core.prefetch.SearchResultFetcher;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GwsSuggestionFetcher
  extends SuggestionFetcher
{
  @Nullable
  private final SearchResultFetcher mFetcher;
  private SearchResult mSearchResult;
  @Nonnull
  private final SearchUrlHelper mSearchUrlHelper;
  
  public GwsSuggestionFetcher(SearchConfig paramSearchConfig, HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper)
  {
    super(paramSearchConfig, paramHttpHelper, null);
    this.mSearchUrlHelper = ((SearchUrlHelper)Preconditions.checkNotNull(paramSearchUrlHelper));
    this.mFetcher = null;
  }
  
  public GwsSuggestionFetcher(SearchConfig paramSearchConfig, HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper, LoginHelper paramLoginHelper, @Nullable SearchResultFetcher paramSearchResultFetcher)
  {
    super(paramSearchConfig, paramHttpHelper, (LoginHelper)Preconditions.checkNotNull(paramLoginHelper));
    this.mSearchUrlHelper = ((SearchUrlHelper)Preconditions.checkNotNull(paramSearchUrlHelper));
    this.mFetcher = paramSearchResultFetcher;
  }
  
  @Nullable
  private SuggestionFetcher.SuggestionResponse fetchSuggestionsAndPrefetch(Query paramQuery)
  {
    Preconditions.checkNotNull(this.mFetcher);
    this.mSearchResult = this.mFetcher.fetchSuggestionsAndSrp(paramQuery, getSuggestionToken());
    final CountDownLatch localCountDownLatch = new CountDownLatch(1);
    Observer local1 = new Observer()
    {
      public void update(Observable paramAnonymousObservable, Object paramAnonymousObject)
      {
        if ((GwsSuggestionFetcher.this.mSearchResult.hasSuggestions()) || (!GwsSuggestionFetcher.this.mSearchResult.isLoading())) {
          localCountDownLatch.countDown();
        }
      }
    };
    this.mSearchResult.addObserver(local1);
    local1.update(null, null);
    try
    {
      localCountDownLatch.await();
      return this.mSearchResult.getSuggestions();
    }
    catch (InterruptedException localInterruptedException)
    {
      for (;;)
      {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  @Nullable
  private SuggestionFetcher.SuggestionResponse fetchSuggestionsOnly(Query paramQuery, boolean paramBoolean)
  {
    paramQuery.getQueryStringForSuggest();
    if (paramBoolean) {}
    for (;;)
    {
      try
      {
        LoginHelper.AuthToken localAuthToken = getSuggestionToken();
        UriRequest localUriRequest;
        if (paramBoolean)
        {
          localUriRequest = this.mSearchUrlHelper.getSuggestionRequest(paramQuery, localAuthToken);
          String str1 = localUriRequest.getUri().toString();
          Map localMap = localUriRequest.getHeaders();
          int i = 1;
          if (!paramBoolean)
          {
            i |= 0x10000000;
            localMap = localUriRequest.getHeadersCopy();
            localMap.remove("Cookie");
          }
          HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(str1, localMap);
          localGetRequest.setUseCaches(false);
          localGetRequest.setUseSpdy(this.mConfig.isSpdyForSuggestionsEnabled());
          String str2 = this.mHttpHelper.get(localGetRequest, i);
          if (localAuthToken != null)
          {
            localObject = localAuthToken.getAccount();
            return new SuggestionFetcher.SuggestionResponse("", str2, (String)localObject);
          }
        }
        else
        {
          localUriRequest = this.mSearchUrlHelper.getExternalSuggestionRequest(paramQuery);
          continue;
        }
        String str3 = getAccountName();
        Object localObject = str3;
        continue;
        localAuthToken = null;
      }
      catch (IOException localIOException)
      {
        return null;
      }
      catch (HttpHelper.HttpException localHttpException)
      {
        return null;
      }
    }
  }
  
  private LoginHelper.AuthToken getSuggestionToken()
  {
    if (!TextUtils.isEmpty(this.mConfig.getSuggestionTokenType())) {
      return getAuthToken(this.mConfig.getSuggestionTokenType());
    }
    return null;
  }
  
  @Nullable
  public SuggestionFetcher.SuggestionResponse fetch(Query paramQuery, boolean paramBoolean)
  {
    if ((paramBoolean) && (!TextUtils.isEmpty(paramQuery.getQueryString())) && (this.mConfig.shouldCombineSuggestAndPrefetch())) {
      return fetchSuggestionsAndPrefetch(paramQuery);
    }
    return fetchSuggestionsOnly(paramQuery, paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.GwsSuggestionFetcher
 * JD-Core Version:    0.7.0.1
 */