package com.google.android.search.core.summons;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.presenter.UiMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class ShouldQueryStrategy
{
  private final SearchConfig mConfig;
  private final HashMap<Source, Integer> mEmptySources = new HashMap();
  private final GlobalSearchServices mGss;
  private String mLastQuery = "";
  private volatile int mQueryStrategy;
  
  public ShouldQueryStrategy(GlobalSearchServices paramGlobalSearchServices, SearchConfig paramSearchConfig)
  {
    this.mGss = paramGlobalSearchServices;
    this.mConfig = paramSearchConfig;
    this.mQueryStrategy = 2;
  }
  
  private boolean setStrategy(int paramInt)
  {
    int i = this.mQueryStrategy;
    this.mQueryStrategy = paramInt;
    return i != paramInt;
  }
  
  private void updateQuery(String paramString)
  {
    if (paramString.startsWith(this.mLastQuery)) {}
    for (;;)
    {
      this.mLastQuery = paramString;
      return;
      if (this.mLastQuery.startsWith(paramString))
      {
        Iterator localIterator = this.mEmptySources.entrySet().iterator();
        while (localIterator.hasNext()) {
          if (((Integer)((Map.Entry)localIterator.next()).getValue()).intValue() > paramString.length()) {
            localIterator.remove();
          }
        }
      }
      else
      {
        this.mEmptySources.clear();
      }
    }
  }
  
  boolean isExternalSentinelQuery(Query paramQuery)
  {
    return QueryState.isExternalSentinelQuery(paramQuery);
  }
  
  public void onZeroResults(String paramString1, String paramString2)
  {
    Sources localSources = this.mGss.getSources();
    if (!localSources.containsSource(paramString1)) {
      Log.e("QSB.ShouldQueryStrategy", "onZeroResults for non-existant source");
    }
    ContentProviderSource localContentProviderSource;
    do
    {
      return;
      localContentProviderSource = (ContentProviderSource)localSources.getSource(paramString1);
    } while ((!this.mLastQuery.startsWith(paramString2)) || (localContentProviderSource.queryAfterZeroResults()) || (TextUtils.isEmpty(paramString2)));
    this.mEmptySources.put(localContentProviderSource, Integer.valueOf(paramString2.length()));
  }
  
  public boolean shouldGetCachedWebOnly()
  {
    return this.mQueryStrategy == 5;
  }
  
  public boolean shouldMixIcingResults()
  {
    return (this.mQueryStrategy == 4) || (this.mQueryStrategy == 2);
  }
  
  public boolean shouldQueryAllContentProviders()
  {
    return this.mQueryStrategy == 3;
  }
  
  public boolean shouldQueryAnySource()
  {
    return this.mQueryStrategy != 0;
  }
  
  public boolean shouldQuerySingleContentProviderIfIcingEmpty()
  {
    return this.mQueryStrategy == 2;
  }
  
  public boolean shouldQuerySource(ContentProviderSource paramContentProviderSource, Query paramQuery)
  {
    if ((this.mQueryStrategy != 2) && (this.mQueryStrategy != 3)) {}
    String str;
    do
    {
      do
      {
        return false;
      } while ((!shouldQuerySummons(paramQuery)) || ((this.mQueryStrategy == 2) && (!this.mConfig.isSourceEnabledInSuggestMode(paramContentProviderSource))));
      str = paramQuery.getQueryStringForSuggest();
      updateQuery(str);
    } while ((str.length() == 0) || (str.length() < paramContentProviderSource.getQueryThreshold()) || ((!paramContentProviderSource.queryAfterZeroResults()) && (this.mEmptySources.containsKey(paramContentProviderSource))));
    return true;
  }
  
  public boolean shouldQuerySummons(Query paramQuery)
  {
    return ((this.mQueryStrategy == 2) || (this.mQueryStrategy == 3) || (this.mQueryStrategy == 4)) && (!paramQuery.isEmptySuggestQuery());
  }
  
  public boolean shouldQueryWeb()
  {
    return (this.mQueryStrategy == 1) || (this.mQueryStrategy == 5) || (this.mQueryStrategy == 2) || (this.mQueryStrategy == 4);
  }
  
  public boolean updateQueryStrategy(QueryState paramQueryState)
  {
    Query localQuery = paramQueryState.get();
    if (localQuery.isSummonsCorpus()) {
      return setStrategy(3);
    }
    if ((localQuery.isSentinel()) && (UiMode.fromSentinelQuery(localQuery) == UiMode.SUGGEST)) {
      return setStrategy(2);
    }
    if (localQuery.isTextSearch())
    {
      if (isExternalSentinelQuery(paramQueryState.getCommittedQuery()))
      {
        if (paramQueryState.isEditingQuery()) {
          return setStrategy(4);
        }
        return setStrategy(5);
      }
      if (paramQueryState.isEditingQuery())
      {
        if (paramQueryState.getCommittedQuery().isSentinel()) {
          return setStrategy(2);
        }
        return setStrategy(4);
      }
    }
    return setStrategy(0);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.ShouldQueryStrategy
 * JD-Core Version:    0.7.0.1
 */