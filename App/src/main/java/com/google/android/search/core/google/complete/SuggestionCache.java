package com.google.android.search.core.google.complete;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.LruCache;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.shared.util.Clock;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SuggestionCache
  extends LruCache<String, SuggestionList>
{
  private final Clock mClock;
  private final int mTimeout;
  
  public SuggestionCache(CoreSearchServices paramCoreSearchServices)
  {
    super(paramCoreSearchServices.getConfig().getSuggestionCacheMaxValues());
    this.mTimeout = paramCoreSearchServices.getConfig().getSuggestionCacheTimeout();
    this.mClock = paramCoreSearchServices.getClock();
    paramCoreSearchServices.getSearchHistoryChangedObservable().registerObserver(new DataSetObserver()
    {
      public void onChanged()
      {
        super.onChanged();
        SuggestionCache.this.evictAll();
      }
      
      public void onInvalidated()
      {
        super.onInvalidated();
        SuggestionCache.this.evictAll();
      }
    });
  }
  
  public void purgeOldData()
  {
    long l = this.mClock.uptimeMillis() - this.mTimeout;
    Iterator localIterator = snapshot().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (((SuggestionList)localEntry.getValue()).getCreationTime() < l) {
        remove(localEntry.getKey());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.SuggestionCache
 * JD-Core Version:    0.7.0.1
 */