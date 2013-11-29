package com.google.android.search.shared.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.common.base.Preconditions;

public class SearchBoxStats
  implements Parcelable
{
  public static final Parcelable.Creator<SearchBoxStats> CREATOR = new Parcelable.Creator()
  {
    public SearchBoxStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SearchBoxStats(paramAnonymousParcel, null);
    }
    
    public SearchBoxStats[] newArray(int paramAnonymousInt)
    {
      return new SearchBoxStats[paramAnonymousInt];
    }
  };
  public static final int SUGGESTION_CLICKED = 1;
  public static final int SUGGESTION_NONE = 0;
  public static final int SUGGESTION_REFINEMENT = 2;
  private final String mClientId;
  private final long mCommitMs;
  private final long mFirstEditMs;
  private final boolean mIsFromPredictive;
  private final long mLastEditMs;
  private final SuggestionLogInfo mLastSuggestions;
  private final int mNumZeroPrefixSuggestionsShown;
  private final String mOriginalQuery;
  private final long mSearchBoxReadyMs;
  private final long mServiceConnectedMs;
  private final long mServiceSuggestLatency;
  private final int mSnappySuggestCount;
  private final String mSource;
  private final int mSuggestCacheHitCount;
  private final int mSuggestIndex;
  private final long mSuggestInteractionMs;
  private final int mSuggestInteractionType;
  private final int mSuggestRequestCount;
  private final int mSuggestServerResponseCount;
  private final long mUserVisibleSuggestLatency;
  private final int mUserVisibleSuggestRequests;
  private final long mZeroPrefixSuggestionsShownMs;
  
  private SearchBoxStats(Parcel paramParcel)
  {
    this.mClientId = paramParcel.readString();
    this.mSource = paramParcel.readString();
    if (paramParcel.readInt() == i) {}
    for (;;)
    {
      this.mIsFromPredictive = i;
      this.mLastSuggestions = ((SuggestionLogInfo)paramParcel.readParcelable(SuggestionLogInfo.class.getClassLoader()));
      this.mSuggestInteractionType = paramParcel.readInt();
      this.mSuggestIndex = paramParcel.readInt();
      this.mOriginalQuery = paramParcel.readString();
      this.mSearchBoxReadyMs = paramParcel.readLong();
      this.mServiceConnectedMs = paramParcel.readLong();
      this.mZeroPrefixSuggestionsShownMs = paramParcel.readLong();
      this.mFirstEditMs = paramParcel.readLong();
      this.mLastEditMs = paramParcel.readLong();
      this.mSuggestInteractionMs = paramParcel.readLong();
      this.mCommitMs = paramParcel.readLong();
      this.mNumZeroPrefixSuggestionsShown = paramParcel.readInt();
      this.mUserVisibleSuggestRequests = paramParcel.readInt();
      this.mUserVisibleSuggestLatency = paramParcel.readLong();
      this.mSuggestRequestCount = paramParcel.readInt();
      this.mSnappySuggestCount = paramParcel.readInt();
      this.mSuggestCacheHitCount = paramParcel.readInt();
      this.mSuggestServerResponseCount = paramParcel.readInt();
      this.mServiceSuggestLatency = paramParcel.readLong();
      return;
      i = 0;
    }
  }
  
  private SearchBoxStats(Builder paramBuilder)
  {
    this.mClientId = paramBuilder.mClientId;
    this.mSource = paramBuilder.mSource;
    this.mIsFromPredictive = paramBuilder.mIsFromPredictive;
    this.mLastSuggestions = paramBuilder.mLastSuggestions;
    this.mSuggestInteractionType = paramBuilder.mSuggestInteractionType;
    this.mSuggestIndex = paramBuilder.mSuggestIndex;
    this.mOriginalQuery = paramBuilder.mOriginalQuery;
    this.mSearchBoxReadyMs = paramBuilder.mSearchBoxReadyMs;
    this.mServiceConnectedMs = paramBuilder.mServiceConnectedMs;
    this.mZeroPrefixSuggestionsShownMs = paramBuilder.mZeroPrefixSuggestionsShownMs;
    this.mFirstEditMs = paramBuilder.mFirstEditMs;
    this.mLastEditMs = paramBuilder.mLastEditMs;
    this.mSuggestInteractionMs = paramBuilder.mSuggestInteractionMs;
    this.mCommitMs = paramBuilder.mCommitMs;
    this.mNumZeroPrefixSuggestionsShown = paramBuilder.mNumZeroPrefixSuggestionsShown;
    this.mUserVisibleSuggestRequests = paramBuilder.mUserVisibleSuggestRequests;
    this.mUserVisibleSuggestLatency = paramBuilder.mUserVisibleSuggestLatency;
    this.mSuggestRequestCount = paramBuilder.mSuggestRequestCount;
    this.mSnappySuggestCount = paramBuilder.mSnappySuggestCount;
    this.mSuggestCacheHitCount = paramBuilder.mSuggestCacheHitCount;
    this.mSuggestServerResponseCount = paramBuilder.mSuggestServerResponseCount;
    this.mServiceSuggestLatency = paramBuilder.mServiceSuggestLatency;
  }
  
  public static Builder newBuilder(String paramString1, String paramString2)
  {
    return new Builder(paramString1, paramString2, null);
  }
  
  public Builder buildUpon()
  {
    return new Builder(this, null);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getClientId()
  {
    return this.mClientId;
  }
  
  public long getCommitMs()
  {
    return this.mCommitMs;
  }
  
  public long getFirstEditMs()
  {
    return this.mFirstEditMs;
  }
  
  public boolean getIsFromPredictive()
  {
    return this.mIsFromPredictive;
  }
  
  public long getLastEditMs()
  {
    return this.mLastEditMs;
  }
  
  public long getLastSuggestionInteractionMs()
  {
    return this.mSuggestInteractionMs;
  }
  
  public SuggestionLogInfo getLastSuggestionsStats()
  {
    return this.mLastSuggestions;
  }
  
  public int getNumZeroPrefixSuggestionsShown()
  {
    return this.mNumZeroPrefixSuggestionsShown;
  }
  
  public String getOriginalQuery()
  {
    return this.mOriginalQuery;
  }
  
  public long getSearchBoxReadyMs()
  {
    return this.mSearchBoxReadyMs;
  }
  
  public long getSearchServiceConnectedMs()
  {
    return this.mServiceConnectedMs;
  }
  
  public long getServiceSuggestLatency()
  {
    return this.mServiceSuggestLatency;
  }
  
  public int getSnappySuggestCount()
  {
    return this.mSnappySuggestCount;
  }
  
  public String getSource()
  {
    return this.mSource;
  }
  
  public int getSuggestCacheHitCount()
  {
    return this.mSuggestCacheHitCount;
  }
  
  public int getSuggestIndex()
  {
    if (this.mSuggestInteractionType != 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return this.mSuggestIndex;
    }
  }
  
  public int getSuggestRequestCount()
  {
    return this.mSuggestRequestCount;
  }
  
  public int getSuggestServerResponseCount()
  {
    return this.mSuggestServerResponseCount;
  }
  
  public long getUserVisibleSuggestLatency()
  {
    return this.mUserVisibleSuggestLatency;
  }
  
  public int getUserVisibleSuggestRequests()
  {
    return this.mUserVisibleSuggestRequests;
  }
  
  public long getZeroPrefixSuggestionsShownMs()
  {
    return this.mZeroPrefixSuggestionsShownMs;
  }
  
  public boolean hasSuggestClick()
  {
    return this.mSuggestInteractionType == 1;
  }
  
  public boolean hasSuggestRefinement()
  {
    return this.mSuggestInteractionType == 2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mClientId);
    paramParcel.writeString(this.mSource);
    if (this.mIsFromPredictive) {}
    for (int i = 1;; i = 0)
    {
      paramParcel.writeInt(i);
      paramParcel.writeParcelable(this.mLastSuggestions, 0);
      paramParcel.writeInt(this.mSuggestInteractionType);
      paramParcel.writeInt(this.mSuggestIndex);
      paramParcel.writeString(this.mOriginalQuery);
      paramParcel.writeLong(this.mSearchBoxReadyMs);
      paramParcel.writeLong(this.mServiceConnectedMs);
      paramParcel.writeLong(this.mZeroPrefixSuggestionsShownMs);
      paramParcel.writeLong(this.mFirstEditMs);
      paramParcel.writeLong(this.mLastEditMs);
      paramParcel.writeLong(this.mSuggestInteractionMs);
      paramParcel.writeLong(this.mCommitMs);
      paramParcel.writeInt(this.mNumZeroPrefixSuggestionsShown);
      paramParcel.writeInt(this.mUserVisibleSuggestRequests);
      paramParcel.writeLong(this.mUserVisibleSuggestLatency);
      paramParcel.writeInt(this.mSuggestRequestCount);
      paramParcel.writeInt(this.mSnappySuggestCount);
      paramParcel.writeInt(this.mSuggestCacheHitCount);
      paramParcel.writeInt(this.mSuggestServerResponseCount);
      paramParcel.writeLong(this.mServiceSuggestLatency);
      return;
    }
  }
  
  public static class Builder
  {
    private final String mClientId;
    private long mCommitMs;
    private long mFirstEditMs;
    private boolean mIsFromPredictive;
    private long mLastEditMs;
    private SuggestionLogInfo mLastSuggestions;
    private int mNumZeroPrefixSuggestionsShown;
    private String mOriginalQuery;
    private long mSearchBoxReadyMs;
    private long mServiceConnectedMs;
    private long mServiceSuggestLatency;
    private int mSnappySuggestCount;
    private final String mSource;
    private int mSuggestCacheHitCount;
    private int mSuggestIndex;
    private long mSuggestInteractionMs;
    private int mSuggestInteractionType;
    private int mSuggestRequestCount;
    private int mSuggestServerResponseCount;
    private long mUserVisibleSuggestLatency;
    private int mUserVisibleSuggestRequests;
    private long mZeroPrefixSuggestionsShownMs;
    
    private Builder(SearchBoxStats paramSearchBoxStats)
    {
      this.mClientId = paramSearchBoxStats.mClientId;
      this.mSource = paramSearchBoxStats.mSource;
      this.mIsFromPredictive = paramSearchBoxStats.mIsFromPredictive;
      this.mLastSuggestions = paramSearchBoxStats.mLastSuggestions;
      this.mSuggestInteractionType = paramSearchBoxStats.mSuggestInteractionType;
      this.mSuggestIndex = paramSearchBoxStats.mSuggestIndex;
      this.mOriginalQuery = paramSearchBoxStats.mOriginalQuery;
      this.mSearchBoxReadyMs = paramSearchBoxStats.mSearchBoxReadyMs;
      this.mServiceConnectedMs = paramSearchBoxStats.mServiceConnectedMs;
      this.mZeroPrefixSuggestionsShownMs = paramSearchBoxStats.mZeroPrefixSuggestionsShownMs;
      this.mFirstEditMs = paramSearchBoxStats.mFirstEditMs;
      this.mLastEditMs = paramSearchBoxStats.mLastEditMs;
      this.mSuggestInteractionMs = paramSearchBoxStats.mSuggestInteractionMs;
      this.mCommitMs = paramSearchBoxStats.mCommitMs;
      this.mNumZeroPrefixSuggestionsShown = paramSearchBoxStats.mNumZeroPrefixSuggestionsShown;
      this.mUserVisibleSuggestRequests = paramSearchBoxStats.mUserVisibleSuggestRequests;
      this.mUserVisibleSuggestLatency = paramSearchBoxStats.mUserVisibleSuggestLatency;
      this.mSuggestRequestCount = paramSearchBoxStats.mSuggestRequestCount;
      this.mSnappySuggestCount = paramSearchBoxStats.mSnappySuggestCount;
      this.mSuggestCacheHitCount = paramSearchBoxStats.mSuggestCacheHitCount;
      this.mSuggestServerResponseCount = paramSearchBoxStats.mSuggestServerResponseCount;
      this.mServiceSuggestLatency = paramSearchBoxStats.mServiceSuggestLatency;
    }
    
    private Builder(String paramString1, String paramString2)
    {
      this.mClientId = paramString1;
      this.mSource = paramString2;
      this.mOriginalQuery = "";
      this.mLastSuggestions = SuggestionLogInfo.EMPTY;
    }
    
    public SearchBoxStats build()
    {
      return new SearchBoxStats(this, null);
    }
    
    public Builder setClientSuggestionStats(int paramInt1, long paramLong1, int paramInt2, long paramLong2)
    {
      this.mNumZeroPrefixSuggestionsShown = paramInt1;
      this.mZeroPrefixSuggestionsShownMs = paramLong1;
      this.mUserVisibleSuggestRequests = paramInt2;
      this.mUserVisibleSuggestLatency = paramLong2;
      return this;
    }
    
    public Builder setIsFromPredictive(boolean paramBoolean)
    {
      this.mIsFromPredictive = paramBoolean;
      return this;
    }
    
    public Builder setLastSuggestions(SuggestionLogInfo paramSuggestionLogInfo)
    {
      this.mLastSuggestions = paramSuggestionLogInfo;
      return this;
    }
    
    public Builder setOriginalQuery(String paramString)
    {
      this.mOriginalQuery = paramString;
      return this;
    }
    
    public Builder setSearchTimingStats(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
    {
      this.mSearchBoxReadyMs = paramLong1;
      this.mServiceConnectedMs = paramLong2;
      this.mFirstEditMs = paramLong3;
      this.mLastEditMs = paramLong4;
      this.mCommitMs = paramLong5;
      return this;
    }
    
    public Builder setServiceSuggestStats(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
    {
      this.mSuggestRequestCount = paramInt1;
      this.mSnappySuggestCount = paramInt2;
      this.mSuggestCacheHitCount = paramInt3;
      this.mSuggestServerResponseCount = paramInt4;
      this.mServiceSuggestLatency = paramLong;
      return this;
    }
    
    public Builder setSuggestInteraction(int paramInt1, int paramInt2, long paramLong)
    {
      this.mSuggestInteractionType = paramInt1;
      this.mSuggestIndex = paramInt2;
      this.mSuggestInteractionMs = paramLong;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.SearchBoxStats
 * JD-Core Version:    0.7.0.1
 */