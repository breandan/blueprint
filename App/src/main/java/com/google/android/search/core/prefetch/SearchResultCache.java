package com.google.android.search.core.prefetch;

import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchResultCache
{
  private final Queue<SearchResult> mCache;
  private final SearchConfig mConfig;
  @Nonnull
  private Query mLastDistinctQuery = Query.EMPTY;
  private long mLastFetchTime = -1L;
  private final Object mLock = new Object();
  private final Queue<SearchResult> mPrefetchSrpDownloads;
  private final SearchUrlHelper mUrlHelper;
  private SearchResult mWaitingFetch = null;
  
  public SearchResultCache(SearchConfig paramSearchConfig, SearchUrlHelper paramSearchUrlHelper)
  {
    this.mConfig = paramSearchConfig;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mPrefetchSrpDownloads = new ConcurrentLinkedQueue();
    this.mCache = new ConcurrentLinkedQueue();
  }
  
  private void enforceMaxDownloads()
  {
    Iterator localIterator = this.mPrefetchSrpDownloads.iterator();
    while (localIterator.hasNext()) {
      if (!((SearchResult)localIterator.next()).isLoading()) {
        localIterator.remove();
      }
    }
    int i = this.mConfig.getPrefetchSimultaneousDownloads();
    while (this.mPrefetchSrpDownloads.size() > i)
    {
      SearchResult localSearchResult = (SearchResult)this.mPrefetchSrpDownloads.poll();
      if ((localSearchResult != null) && (!localSearchResult.isComplete())) {
        localSearchResult.setCancelled();
      }
    }
  }
  
  private boolean equivalentForCache(Query paramQuery1, Query paramQuery2)
  {
    if (paramQuery1.getCommitId() == paramQuery2.getCommitId()) {}
    do
    {
      return true;
      if (paramQuery1.isFromBackStack()) {
        return this.mUrlHelper.equivalentForSearch(paramQuery1, paramQuery2);
      }
    } while ((queryContextIsValid(paramQuery2)) && (this.mUrlHelper.equivalentForSearch(paramQuery1, paramQuery2)));
    return false;
  }
  
  private boolean queryContextIsValid(Query paramQuery)
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (paramQuery.getCommitId() >= this.mLastDistinctQuery.getCommitId())
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public void add(final SearchResult paramSearchResult)
  {
    while (this.mCache.size() >= this.mConfig.getPrefetchCacheEntries()) {
      this.mCache.poll();
    }
    this.mCache.add(paramSearchResult);
    Query localQuery = paramSearchResult.getSrpQuery();
    if (localQuery != Query.EMPTY) {
      synchronized (this.mLock)
      {
        if (!localQuery.isPrefetch()) {
          notifyQueryFulfilled(localQuery);
        }
        return;
      }
    }
    paramSearchResult.addObserver(new Observer()
    {
      private boolean receivedSrpQuery;
      
      public void update(Observable paramAnonymousObservable, Object paramAnonymousObject)
      {
        if ((!this.receivedSrpQuery) && (paramSearchResult.getSrpQuery() != Query.EMPTY))
        {
          this.receivedSrpQuery = true;
          if (paramSearchResult.isLoading()) {
            SearchResultCache.this.mPrefetchSrpDownloads.add(paramSearchResult);
          }
          SearchResultCache.this.enforceMaxDownloads();
        }
      }
    });
  }
  
  public void clear()
  {
    synchronized (this.mLock)
    {
      this.mWaitingFetch = null;
      Iterator localIterator = this.mCache.iterator();
      while (localIterator.hasNext())
      {
        SearchResult localSearchResult = (SearchResult)localIterator.next();
        if (!localSearchResult.isComplete()) {
          localSearchResult.setCancelled();
        }
      }
    }
    this.mCache.clear();
    this.mPrefetchSrpDownloads.clear();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    DumpUtils.println(paramPrintWriter, new Object[] { paramString, "SearchResultCache state:" });
    String str = paramString + "  ";
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = str;
    arrayOfObject1[1] = ("max cache entries: " + this.mConfig.getPrefetchCacheEntries());
    DumpUtils.println(paramPrintWriter, arrayOfObject1);
    synchronized (this.mLock)
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = str;
      arrayOfObject2[1] = ("mLastFetchTime: " + this.mLastFetchTime);
      DumpUtils.println(paramPrintWriter, arrayOfObject2);
      DumpUtils.println(paramPrintWriter, new Object[] { str, "mWaitingFetch:" });
      if (this.mWaitingFetch == null)
      {
        DumpUtils.println(paramPrintWriter, new Object[] { str, "  null" });
        Object[] arrayOfObject3 = new Object[4];
        arrayOfObject3[0] = str;
        arrayOfObject3[1] = "mPrefetchSrpDownloads, ";
        arrayOfObject3[2] = Integer.valueOf(this.mPrefetchSrpDownloads.size());
        arrayOfObject3[3] = " items, these should also be in mCache:";
        DumpUtils.println(paramPrintWriter, arrayOfObject3);
        Iterator localIterator1 = this.mPrefetchSrpDownloads.iterator();
        while (localIterator1.hasNext()) {
          ((SearchResult)localIterator1.next()).dump(str + "  ", paramPrintWriter);
        }
      }
      this.mWaitingFetch.dump(str + "  ", paramPrintWriter);
    }
    Object[] arrayOfObject4 = new Object[4];
    arrayOfObject4[0] = str;
    arrayOfObject4[1] = "mCache, ";
    arrayOfObject4[2] = Integer.valueOf(this.mCache.size());
    arrayOfObject4[3] = " items:";
    DumpUtils.println(paramPrintWriter, arrayOfObject4);
    Iterator localIterator2 = this.mCache.iterator();
    while (localIterator2.hasNext()) {
      ((SearchResult)localIterator2.next()).dump(str + "  ", paramPrintWriter);
    }
  }
  
  void fetchWaitingPage(long paramLong)
  {
    synchronized (this.mLock)
    {
      SearchResult localSearchResult1 = this.mWaitingFetch;
      SearchResult localSearchResult2 = null;
      if (localSearchResult1 != null)
      {
        localSearchResult2 = this.mWaitingFetch;
        this.mWaitingFetch = null;
        this.mLastFetchTime = paramLong;
      }
      if (localSearchResult2 != null)
      {
        add(localSearchResult2);
        localSearchResult2.startFetch();
      }
      return;
    }
  }
  
  @Nullable
  public SearchResult get(@Nonnull Query paramQuery, long paramLong, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramQuery);
    if (paramBoolean) {}
    Object localObject1;
    for (;;)
    {
      int i;
      SearchResult localSearchResult;
      long l;
      int k;
      synchronized (this.mLock)
      {
        this.mWaitingFetch = null;
        localObject1 = null;
        i = this.mConfig.getPrefetchTtlMillis();
        Iterator localIterator = this.mCache.iterator();
        int j = 1;
        if (!localIterator.hasNext()) {
          break;
        }
        localSearchResult = (SearchResult)localIterator.next();
        l = localSearchResult.getFetchTimeMillis();
        ((String)null);
        j++;
        if (localSearchResult.isFailed())
        {
          k = 1;
          if (k == 0) {
            continue;
          }
          localIterator.remove();
          if (!localSearchResult.isLoading()) {
            continue;
          }
          localSearchResult.setCancelled();
        }
      }
      if ((localSearchResult.isComplete()) && (localSearchResult.getSrpQuery() == Query.EMPTY))
      {
        k = 1;
      }
      else if (paramLong - l > i)
      {
        k = 1;
      }
      else if (equivalentForCache(paramQuery, localSearchResult.getSrpQuery()))
      {
        if (localSearchResult.isFailed())
        {
          k = 0;
          if (localObject1 != null) {}
        }
        else
        {
          if (localObject1 != null)
          {
            localObject1.setCancelled();
            Log.w("Velvet.SearchResultCache", "Found another page matching the query. Using the latest one.");
          }
          localObject1 = localSearchResult;
          k = 0;
        }
      }
      else
      {
        k = 0;
        if (paramBoolean)
        {
          boolean bool = localSearchResult.isComplete();
          k = 0;
          if (!bool) {
            k = 1;
          }
        }
      }
    }
    return localObject1;
  }
  
  public void notifyQueryFulfilled(Query paramQuery)
  {
    synchronized (this.mLock)
    {
      if ((queryContextIsValid(paramQuery)) && (!this.mUrlHelper.equivalentForContext(this.mLastDistinctQuery, paramQuery))) {
        this.mLastDistinctQuery = paramQuery;
      }
      return;
    }
  }
  
  public void onTrimMemory()
  {
    Iterator localIterator = this.mCache.iterator();
    if (localIterator.hasNext())
    {
      localIterator.next();
      while (localIterator.hasNext())
      {
        localIterator.remove();
        localIterator.next();
      }
    }
  }
  
  long setWaitingSearch(SearchResult paramSearchResult, long paramLong)
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mLastFetchTime <= 0L) {
          break label102;
        }
        if (paramLong >= 0L)
        {
          long l1 = paramLong - this.mLastFetchTime;
          l2 = this.mConfig.getPrefetchThrottlePeriodMillis() - l1;
          if (l2 <= 0L) {
            break label96;
          }
          if (this.mWaitingFetch != null) {
            break label88;
          }
          l3 = l2;
          this.mWaitingFetch = paramSearchResult;
          return l3;
        }
      }
      long l2 = 0L;
      continue;
      label88:
      long l3 = -1L;
      continue;
      label96:
      l3 = 0L;
      continue;
      label102:
      l3 = 0L;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    int i = 0;
    Iterator localIterator = this.mCache.iterator();
    if (localIterator.hasNext())
    {
      int j = i + 1;
      if (i > 0) {
        localStringBuilder.append(",");
      }
      Query localQuery = ((SearchResult)localIterator.next()).getSrpQuery();
      if (localQuery != null) {
        localStringBuilder.append(localQuery.getQueryString());
      }
      for (;;)
      {
        i = j;
        break;
        localStringBuilder.append("non-srp");
      }
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.SearchResultCache
 * JD-Core Version:    0.7.0.1
 */