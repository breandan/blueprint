package com.google.android.search.core.google;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DeletedQueryRemovingSourceWrapper
  extends WebSuggestSourceWrapper
{
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Map<Integer, Long> mDeletions;
  
  public DeletedQueryRemovingSourceWrapper(WebSuggestSource paramWebSuggestSource, SearchConfig paramSearchConfig, Clock paramClock)
  {
    super(paramWebSuggestSource);
    this.mConfig = paramSearchConfig;
    this.mClock = paramClock;
    this.mDeletions = Maps.newHashMap();
  }
  
  private SuggestionList filterDeletedQueries(SuggestionList paramSuggestionList)
  {
    removeExpiredDeletions();
    if (this.mDeletions.size() == 0) {
      return paramSuggestionList;
    }
    int i = 0;
    while (i < paramSuggestionList.getCount())
    {
      Suggestion localSuggestion = paramSuggestionList.get(i);
      if ((localSuggestion.isWebSearchSuggestion()) && (localSuggestion.isHistorySuggestion()))
      {
        int j = localSuggestion.getSuggestionQuery().hashCode();
        if (this.mDeletions.containsKey(Integer.valueOf(j)))
        {
          if (!(paramSuggestionList instanceof MutableSuggestionList)) {
            paramSuggestionList = new MutableSuggestionListImpl(paramSuggestionList);
          }
          ((MutableSuggestionList)paramSuggestionList).remove(i);
          continue;
        }
      }
      i++;
    }
    return paramSuggestionList;
  }
  
  private void removeExpiredDeletions()
  {
    long l = this.mClock.uptimeMillis() - this.mConfig.getDeletedQueryPropagationDelayMs();
    Iterator localIterator = this.mDeletions.entrySet().iterator();
    while (localIterator.hasNext()) {
      if (((Long)((Map.Entry)localIterator.next()).getValue()).longValue() <= l) {
        localIterator.remove();
      }
    }
  }
  
  public void getSuggestions(Query paramQuery, final Consumer<SuggestionList> paramConsumer)
  {
    super.getSuggestions(paramQuery, new Consumer()
    {
      public boolean consume(SuggestionList paramAnonymousSuggestionList)
      {
        return paramConsumer.consume(DeletedQueryRemovingSourceWrapper.this.filterDeletedQueries(paramAnonymousSuggestionList));
      }
    });
  }
  
  public boolean removeFromHistory(String paramString)
  {
    if (super.removeFromHistory(paramString))
    {
      this.mDeletions.put(Integer.valueOf(paramString.hashCode()), Long.valueOf(this.mClock.uptimeMillis()));
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.DeletedQueryRemovingSourceWrapper
 * JD-Core Version:    0.7.0.1
 */