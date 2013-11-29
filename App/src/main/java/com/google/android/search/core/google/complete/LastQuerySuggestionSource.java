package com.google.android.search.core.google.complete;

import android.database.DataSetObserver;
import android.text.TextUtils;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.WebSuggestSource;
import com.google.android.search.core.google.WebSuggestSourceWrapper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.web.WebSuggestions;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LastQuerySuggestionSource
  extends WebSuggestSourceWrapper
{
  private final DataSetObserver mAccountsObserver;
  private final Clock mClock;
  private final GsaConfigFlags mConfigFlags;
  private String mLastFailedQuery;
  private long mLastFailedQueryUpdatedMs;
  private String mLastQuery;
  private long mLastQueryUpdatedMs;
  private final LoginHelper mLoginHelper;
  private final SearchSettings mSettings;
  private String mUndoQuery;
  
  public LastQuerySuggestionSource(WebSuggestSource paramWebSuggestSource, LoginHelper paramLoginHelper, GsaConfigFlags paramGsaConfigFlags, Clock paramClock, SearchSettings paramSearchSettings)
  {
    super(paramWebSuggestSource);
    this.mLoginHelper = paramLoginHelper;
    this.mAccountsObserver = new DataSetObserver()
    {
      public void onChanged()
      {
        LastQuerySuggestionSource.this.updateLastQuery("");
        LastQuerySuggestionSource.this.updateLastFailedQuery("");
      }
      
      public void onInvalidated()
      {
        onChanged();
      }
    };
    this.mLoginHelper.registerDataSetObserver(this.mAccountsObserver);
    this.mConfigFlags = paramGsaConfigFlags;
    this.mClock = paramClock;
    this.mSettings = paramSearchSettings;
    this.mLastQuery = this.mSettings.getLastQuery();
    this.mLastQueryUpdatedMs = this.mSettings.getLastQueryUpdated();
    this.mLastFailedQuery = this.mSettings.getLastFailedQuery();
    this.mLastFailedQueryUpdatedMs = this.mSettings.getLastFailedQueryUpdated();
    this.mUndoQuery = "";
  }
  
  private SuggestionList maybeAddLastQueries(Query paramQuery, SuggestionList paramSuggestionList)
  {
    if (paramSuggestionList == null) {
      paramSuggestionList = null;
    }
    for (;;)
    {
      return paramSuggestionList;
      int i;
      MutableSuggestionListImpl localMutableSuggestionListImpl;
      if (paramQuery.isEmptySuggestQuery())
      {
        i = this.mConfigFlags.getShowLastQueryInZeroPrefixSuggestMin();
        int j = this.mConfigFlags.getShowLastFailedQueryInZeroPrefixSuggestMin();
        String str1 = this.mLastFailedQuery;
        long l1 = this.mLastFailedQueryUpdatedMs;
        if (shouldAdd(paramSuggestionList, str1, j, l1))
        {
          localMutableSuggestionListImpl = new MutableSuggestionListImpl(paramSuggestionList);
          localMutableSuggestionListImpl.add(WebSuggestions.createDeviceFailedQuerySuggestion(this.mLastFailedQuery));
        }
      }
      while (localMutableSuggestionListImpl != null)
      {
        return localMutableSuggestionListImpl;
        String str2 = this.mLastQuery;
        long l2 = this.mLastQueryUpdatedMs;
        boolean bool4 = shouldAdd(paramSuggestionList, str2, i, l2);
        localMutableSuggestionListImpl = null;
        if (bool4)
        {
          localMutableSuggestionListImpl = new MutableSuggestionListImpl(paramSuggestionList);
          localMutableSuggestionListImpl.add(WebSuggestions.createDeviceQuerySuggestion(this.mLastQuery));
          continue;
          boolean bool1 = TextUtils.isEmpty(this.mUndoQuery);
          localMutableSuggestionListImpl = null;
          if (!bool1)
          {
            boolean bool2 = this.mConfigFlags.getShowOriginalQueryInGenieResultSuggest();
            localMutableSuggestionListImpl = null;
            if (bool2)
            {
              boolean bool3 = Query.equivalentForSuggest(paramQuery.getQueryStringForSuggest(), this.mLastQuery);
              localMutableSuggestionListImpl = null;
              if (bool3)
              {
                localMutableSuggestionListImpl = new MutableSuggestionListImpl(paramSuggestionList);
                localMutableSuggestionListImpl.add(WebSuggestions.createUndoSuggestion(this.mUndoQuery));
              }
            }
          }
        }
      }
    }
  }
  
  private boolean shouldAdd(SuggestionList paramSuggestionList, String paramString, int paramInt, long paramLong)
  {
    if (TextUtils.isEmpty(paramString)) {}
    while (TimeUnit.MINUTES.convert(this.mClock.currentTimeMillis() - paramLong, TimeUnit.MILLISECONDS) >= paramInt) {
      return false;
    }
    Iterator localIterator = paramSuggestionList.iterator();
    while (localIterator.hasNext()) {
      if (Query.equivalentForSuggest(((Suggestion)localIterator.next()).getSuggestionQuery(), paramString)) {
        return false;
      }
    }
    return true;
  }
  
  private void updateLastFailedQuery(String paramString)
  {
    this.mLastFailedQuery = paramString;
    this.mLastFailedQueryUpdatedMs = this.mClock.currentTimeMillis();
    this.mSettings.setLastFailedQuery(this.mLastFailedQuery, this.mLastFailedQueryUpdatedMs);
  }
  
  private void updateLastQuery(String paramString)
  {
    this.mLastQuery = paramString;
    this.mLastQueryUpdatedMs = this.mClock.currentTimeMillis();
    this.mSettings.setLastQuery(this.mLastQuery, this.mLastQueryUpdatedMs);
  }
  
  public void close()
  {
    this.mLoginHelper.unregisterDataSetObserver(this.mAccountsObserver);
    super.close();
  }
  
  public SuggestionList getCachedSuggestions(Query paramQuery)
  {
    return maybeAddLastQueries(paramQuery, super.getCachedSuggestions(paramQuery));
  }
  
  public void getSuggestions(final Query paramQuery, final Consumer<SuggestionList> paramConsumer)
  {
    super.getSuggestions(paramQuery, new Consumer()
    {
      public boolean consume(SuggestionList paramAnonymousSuggestionList)
      {
        SuggestionList localSuggestionList = LastQuerySuggestionSource.this.maybeAddLastQueries(paramQuery, paramAnonymousSuggestionList);
        return paramConsumer.consume(localSuggestionList);
      }
    });
  }
  
  public void newCommittedQuery(Query paramQuery)
  {
    String str = paramQuery.getQueryStringForSuggest();
    if ((paramQuery.isTextOrVoiceWebSearchWithQueryChars()) && (!TextUtils.equals(str, this.mLastQuery))) {
      if (!paramQuery.isRewritten()) {
        break label50;
      }
    }
    label50:
    for (this.mUndoQuery = this.mLastQuery;; this.mUndoQuery = "")
    {
      updateLastQuery(str);
      updateLastFailedQuery("");
      return;
    }
  }
  
  public void queryFailed(Query paramQuery)
  {
    if (paramQuery.isTextOrVoiceWebSearchWithQueryChars()) {
      updateLastFailedQuery(paramQuery.getQueryStringForSuggest());
    }
  }
  
  public boolean removeFromHistory(String paramString)
  {
    if ((this.mLastQuery != null) && (Query.equivalentForSuggest(this.mLastQuery, paramString))) {
      updateLastQuery("");
    }
    if ((this.mLastFailedQuery != null) && (Query.equivalentForSuggest(this.mLastFailedQuery, paramString))) {
      updateLastFailedQuery("");
    }
    return super.removeFromHistory(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.LastQuerySuggestionSource
 * JD-Core Version:    0.7.0.1
 */