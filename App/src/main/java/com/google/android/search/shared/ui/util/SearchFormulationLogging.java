package com.google.android.search.shared.ui.util;

import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.SearchBoxStats.Builder;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.shared.util.Clock;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;

public class SearchFormulationLogging
{
  private final String mClientId;
  private final Clock mClock;
  private long mCommitMs;
  private Query mCurrentQuery;
  private long mFirstEditMs;
  private final boolean mIsFromPredictive;
  private long mLastEditMs;
  private int mLastSuggestInteractionType;
  private SuggestionLogInfo mLastSuggestions;
  private List<Suggestion> mLastSuggestionsList;
  private long mLastSuggestionsShownMs;
  private int mNumZeroPrefixSuggestionsShown;
  private String mOriginalQuery;
  private long mSearchBoxReadyMs;
  private long mSearchStartMs;
  private long mServiceConnectedMs;
  private final String mSource;
  private int mSuggestIndex;
  private long mSuggestionInteractionMs;
  private long mUserVisibleSuggestLatency;
  private int mUserVisibleSuggestRequests;
  private long mZeroPrefixSuggestionsShownMs;
  
  public SearchFormulationLogging(String paramString1, String paramString2, Clock paramClock, Query paramQuery, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramString1);
    Preconditions.checkNotNull(paramQuery);
    Preconditions.checkNotNull(paramString2);
    this.mClock = paramClock;
    this.mClientId = paramString1;
    this.mSource = paramString2;
    this.mCurrentQuery = paramQuery;
    this.mIsFromPredictive = paramBoolean;
    this.mLastSuggestionsList = ImmutableList.of();
    this.mLastSuggestions = SuggestionLogInfo.EMPTY;
    this.mLastSuggestInteractionType = 0;
    this.mSuggestIndex = 0;
    this.mOriginalQuery = "";
    this.mSearchStartMs = this.mClock.elapsedRealtime();
  }
  
  private long getMsFromSearchStart()
  {
    return this.mClock.elapsedRealtime() - this.mSearchStartMs;
  }
  
  private void registerSuggestInteraction(int paramInt, Suggestion paramSuggestion)
  {
    int i = 1;
    if ((paramInt == i) || (paramInt == 2)) {}
    for (;;)
    {
      Preconditions.checkArgument(i);
      Preconditions.checkNotNull(paramSuggestion);
      this.mLastSuggestInteractionType = paramInt;
      if (this.mLastSuggestionsList.contains(paramSuggestion)) {
        this.mSuggestIndex = this.mLastSuggestionsList.indexOf(paramSuggestion);
      }
      this.mOriginalQuery = this.mCurrentQuery.getQueryStringForSearch();
      this.mSuggestionInteractionMs = getMsFromSearchStart();
      return;
      i = 0;
    }
  }
  
  public SearchBoxStats build()
  {
    this.mCommitMs = getMsFromSearchStart();
    return SearchBoxStats.newBuilder(this.mClientId, this.mSource).setIsFromPredictive(this.mIsFromPredictive).setSearchTimingStats(this.mSearchBoxReadyMs, this.mServiceConnectedMs, this.mFirstEditMs, this.mLastEditMs, this.mCommitMs).setClientSuggestionStats(this.mNumZeroPrefixSuggestionsShown, this.mZeroPrefixSuggestionsShownMs, this.mUserVisibleSuggestRequests, this.mUserVisibleSuggestLatency).setLastSuggestions(this.mLastSuggestions).setOriginalQuery(this.mOriginalQuery).setSuggestInteraction(this.mLastSuggestInteractionType, this.mSuggestIndex, this.mSuggestionInteractionMs).build();
  }
  
  public SearchFormulationLogging registerQueryEdit(Query paramQuery)
  {
    this.mCurrentQuery = paramQuery;
    this.mOriginalQuery = paramQuery.getQueryStringForSearch();
    long l = getMsFromSearchStart();
    if (this.mFirstEditMs == 0L) {
      this.mFirstEditMs = l;
    }
    this.mLastEditMs = l;
    return this;
  }
  
  public SearchFormulationLogging registerQueryRefinement(@Nonnull Suggestion paramSuggestion)
  {
    registerSuggestInteraction(2, paramSuggestion);
    return this;
  }
  
  public SearchFormulationLogging registerSearchBoxReady()
  {
    if (this.mSearchBoxReadyMs == 0L) {
      this.mSearchBoxReadyMs = getMsFromSearchStart();
    }
    return this;
  }
  
  public SearchFormulationLogging registerServiceConnected()
  {
    if (this.mServiceConnectedMs == 0L) {
      this.mServiceConnectedMs = getMsFromSearchStart();
    }
    return this;
  }
  
  public SearchFormulationLogging registerSuggestClick(Suggestion paramSuggestion)
  {
    registerSuggestInteraction(1, paramSuggestion);
    return this;
  }
  
  public SearchFormulationLogging registerSuggestionsShown(List<Suggestion> paramList, SuggestionLogInfo paramSuggestionLogInfo, boolean paramBoolean)
  {
    this.mLastSuggestionsList = paramList;
    this.mLastSuggestions = paramSuggestionLogInfo;
    long l = getMsFromSearchStart();
    if ((!TextUtils.isEmpty(paramSuggestionLogInfo.getSuggestionsEncoding())) || (paramBoolean))
    {
      if ((this.mLastSuggestionsShownMs == 0L) || (this.mLastSuggestionsShownMs < this.mLastEditMs))
      {
        this.mUserVisibleSuggestRequests = (1 + this.mUserVisibleSuggestRequests);
        this.mUserVisibleSuggestLatency += l - this.mLastEditMs;
      }
      this.mLastSuggestionsShownMs = l;
      if ((this.mCurrentQuery.isEmptySuggestQuery()) && (this.mZeroPrefixSuggestionsShownMs == 0L))
      {
        this.mZeroPrefixSuggestionsShownMs = l;
        this.mNumZeroPrefixSuggestionsShown = paramList.size();
      }
    }
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.util.SearchFormulationLogging
 * JD-Core Version:    0.7.0.1
 */