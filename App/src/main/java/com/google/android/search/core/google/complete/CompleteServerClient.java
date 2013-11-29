package com.google.android.search.core.google.complete;

import android.content.Context;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.AbstractGoogleWebSource;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;

public class CompleteServerClient
  extends AbstractGoogleWebSource
{
  private final SuggestionCache mCache;
  private final SuggestionFetcher mFetcher;
  private final SuggestionParser mParser;
  
  public CompleteServerClient(SuggestionFetcher paramSuggestionFetcher, SuggestionParser paramSuggestionParser, Context paramContext, CoreSearchServices paramCoreSearchServices)
  {
    super(paramContext, paramCoreSearchServices);
    this.mFetcher = ((SuggestionFetcher)Preconditions.checkNotNull(paramSuggestionFetcher));
    this.mParser = ((SuggestionParser)Preconditions.checkNotNull(paramSuggestionParser));
    this.mCache = new SuggestionCache(paramCoreSearchServices);
  }
  
  private void cacheSuggestions(SuggestionList paramSuggestionList)
  {
    this.mCache.put(paramSuggestionList.getUserQuery().getQueryStringForSuggest(), paramSuggestionList);
  }
  
  public SuggestionList getCachedSuggestions(Query paramQuery)
  {
    if ((getConfig().isSuggestLookAheadEnabled()) && (!paramQuery.isEmptySuggestQuery()))
    {
      this.mCache.purgeOldData();
      SuggestionList localSuggestionList = (SuggestionList)this.mCache.get(paramQuery.getQueryStringForSuggest());
      if (localSuggestionList != null) {
        localSuggestionList.setFromCache(true);
      }
      return localSuggestionList;
    }
    return null;
  }
  
  public String getSourceName()
  {
    return "complete-server";
  }
  
  protected void query(Query paramQuery, boolean paramBoolean, MutableSuggestionList paramMutableSuggestionList)
  {
    SuggestionFetcher.SuggestionResponse localSuggestionResponse = this.mFetcher.fetch(paramQuery, paramBoolean);
    if (localSuggestionResponse != null) {
      try
      {
        if (localSuggestionResponse.mJson != null)
        {
          SuggestionParser.ParsedSuggestions localParsedSuggestions = this.mParser.parseJson(paramQuery, localSuggestionResponse.mJson, localSuggestionResponse.mAccountUsed);
          paramMutableSuggestionList.addAll(localParsedSuggestions.mMainSuggestions);
          if (localSuggestionResponse.mAccountUsed != null) {
            paramMutableSuggestionList.setAccount(localSuggestionResponse.mAccountUsed);
          }
          if (getConfig().isSuggestLookAheadEnabled())
          {
            cacheSuggestions(localParsedSuggestions.mMainSuggestions);
            Iterator localIterator = localParsedSuggestions.mLookaheadSuggestions.iterator();
            while (localIterator.hasNext())
            {
              cacheSuggestions((SuggestionList)localIterator.next());
              continue;
              paramMutableSuggestionList.setRequestFailed(true);
            }
          }
        }
      }
      catch (JSONException localJSONException)
      {
        Log.w("Search.CompleteServerClient", "Error parsing suggestions '" + localSuggestionResponse.mJson + "'", localJSONException);
      }
    }
  }
  
  public boolean removeFromHistory(String paramString)
  {
    return this.mFetcher.removeFromHistory(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.CompleteServerClient
 * JD-Core Version:    0.7.0.1
 */