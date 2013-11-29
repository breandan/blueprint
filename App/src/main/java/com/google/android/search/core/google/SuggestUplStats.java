package com.google.android.search.core.google;

import com.google.android.search.shared.api.Query;

public class SuggestUplStats
{
  private int mCacheHitCount;
  private Query mPendingRequest;
  private long mPendingRequestTime;
  private int mServerResponseCount;
  private int mSnappySuggestCount;
  private int mTotalRequestCount;
  private long mTotalRoundTripTime;
  
  public int getCacheHitCount()
  {
    return this.mCacheHitCount;
  }
  
  public int getServerResponseCount()
  {
    return this.mServerResponseCount;
  }
  
  public int getSnappyRequestCount()
  {
    return this.mSnappySuggestCount;
  }
  
  public int getTotalRequestCount()
  {
    return this.mTotalRequestCount;
  }
  
  public long getTotalSuggestLatency()
  {
    return this.mTotalRoundTripTime;
  }
  
  public void registerSnappyRequest(Query paramQuery)
  {
    if (this.mPendingRequestTime != 0L)
    {
      this.mSnappySuggestCount = (1 + this.mSnappySuggestCount);
      this.mPendingRequest = paramQuery;
    }
  }
  
  public void registerSuggestRequest(long paramLong)
  {
    this.mTotalRequestCount = (1 + this.mTotalRequestCount);
    this.mPendingRequestTime = paramLong;
  }
  
  public void registerSuggestResponse(long paramLong, boolean paramBoolean, Query paramQuery)
  {
    if ((this.mPendingRequestTime == 0L) || (this.mPendingRequest != paramQuery)) {
      return;
    }
    if (paramBoolean) {
      this.mCacheHitCount = (1 + this.mCacheHitCount);
    }
    for (;;)
    {
      this.mPendingRequestTime = 0L;
      this.mPendingRequest = null;
      return;
      this.mServerResponseCount = (1 + this.mServerResponseCount);
      this.mTotalRoundTripTime += paramLong - this.mPendingRequestTime;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.SuggestUplStats
 * JD-Core Version:    0.7.0.1
 */