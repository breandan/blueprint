package com.google.android.search.core.state;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.WebSearchConnectionError;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.prefetch.SearchResult;
import com.google.android.search.core.util.LatencyTracker;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.Util;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.presenter.UiMode;
import com.google.android.voicesearch.EffectOnWebResults;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.annotation.Nullable;

public class QueryState
  extends VelvetState
{
  private boolean mAudioFocusSettling;
  private final List<BackStackEntry> mBackStack = Lists.newArrayList();
  private boolean mBrowserDimensionsAvailable;
  private boolean mCarConnected;
  private boolean mCharging;
  private final Clock mClock;
  private Query mCommittedQuery;
  private final SearchConfig mConfig;
  private boolean mCookiesAccessAllowed;
  @Nullable
  private SearchResult mCurrentSearchResult;
  private final VelvetEventBus mEventBus;
  private boolean mExternalActivityLaunching;
  private boolean mHotwordDetectionEnabled;
  private int mHotwordState;
  @Nullable
  private BackStackEntry mLastPoppedBackStackEntry;
  private final LatencyTracker mLatencyTracker;
  private boolean mLoggedAlternateAction;
  private int mMinimumHotwordQuality = -1;
  private long mMusicLastDetectedAt;
  private final LoadState<ActionData> mNetworkActionState;
  private final NetworkInformation mNetworkInformation;
  private Query mPendingFollowOnQuery;
  private final LoadState<ActionData> mPumpkinActionState;
  private int mPumpkinState;
  private Query mQuery;
  private final Random mRandom;
  private boolean mResolvingAdClickUrl;
  private final SearchBoxLogging mSearchBoxLogging;
  private final Observer mSearchResultObserver = new Observer()
  {
    public void update(Observable paramAnonymousObservable, Object paramAnonymousObject)
    {
      
      if ((paramAnonymousObservable == QueryState.this.mCurrentSearchResult) && (QueryState.this.onSearchResultChanged())) {
        QueryState.this.notifyChanged();
      }
    }
  };
  private final Settings mSettings;
  private boolean mStartupComplete;
  private boolean mStopListeningPending;
  private boolean mSuggestSessionStarted;
  private final SearchUrlHelper mUrlHelper;
  private boolean mWebCorporaAvailable;
  @Nullable
  private SearchResult mWebViewSearchResult;
  private final WebviewLoadState mWebviewState;
  
  public QueryState(VelvetEventBus paramVelvetEventBus, SearchConfig paramSearchConfig, Settings paramSettings, Clock paramClock, Random paramRandom, SearchUrlHelper paramSearchUrlHelper, SearchBoxLogging paramSearchBoxLogging, LatencyTracker paramLatencyTracker, NetworkInformation paramNetworkInformation)
  {
    super(paramVelvetEventBus, 1);
    this.mEventBus = paramVelvetEventBus;
    this.mConfig = paramSearchConfig;
    this.mSettings = paramSettings;
    this.mClock = paramClock;
    this.mLatencyTracker = paramLatencyTracker;
    this.mNetworkInformation = paramNetworkInformation;
    this.mCommittedQuery = Query.EMPTY.sentinel(UiMode.NONE, null).committed();
    this.mQuery = this.mCommittedQuery.text();
    this.mPendingFollowOnQuery = Query.EMPTY;
    this.mWebviewState = new WebviewLoadState();
    this.mPumpkinState = 0;
    this.mNetworkActionState = new LoadState("network");
    this.mPumpkinActionState = new LoadState("pumpkin");
    this.mRandom = paramRandom;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mSearchBoxLogging = paramSearchBoxLogging;
  }
  
  private boolean canPumpkinHandleQuery(Query paramQuery)
  {
    ((ActionData)this.mNetworkActionState.getLoadedData());
    return (!this.mEventBus.getActionState().hasDataForQuery(paramQuery)) && (paramQuery.isTextOrVoiceWebSearchWithQueryChars()) && (paramQuery.shouldShowCards()) && (this.mPumpkinState != 2);
  }
  
  private boolean canUseCommittedQueryToSearch()
  {
    return (this.mCommittedQuery.isValidSearch()) && ((!this.mCommittedQuery.needBrowserDimensions()) || (this.mBrowserDimensionsAvailable)) && ((!this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) || ((this.mWebCorporaAvailable) && (this.mCookiesAccessAllowed))) && (!this.mCommittedQuery.isSummonsCorpus()) && (!this.mCommittedQuery.isNotificationAnnouncement());
  }
  
  private boolean editQueryIfNothingToShow()
  {
    if (resultsLoadedAndNothingToShow()) {
      return startQueryEditInternal();
    }
    return false;
  }
  
  private static String hotwordStateString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "INVALID(" + paramInt + ")";
    case 0: 
      return "INACTIVE";
    case 1: 
      return "UNAVAILABLE";
    }
    return "ACTIVE";
  }
  
  public static boolean isExternalSentinelQuery(Query paramQuery)
  {
    return (paramQuery.isSentinel()) && (UiMode.fromSentinelQuery(paramQuery) == UiMode.EXTERNAL);
  }
  
  private boolean isHotwordWhenChargingEnabled()
  {
    return (Feature.HOTWORD_WHEN_CHARGING.isEnabled()) && (this.mCharging);
  }
  
  private boolean isShowingSummonsQuery()
  {
    return (this.mQuery.isSummonsCorpus()) && (this.mCommittedQuery.isSummonsCorpus());
  }
  
  private boolean maybeCommitPendingFollowOnQuery()
  {
    if (!Feature.FOLLOW_ON.isEnabled()) {}
    ActionState localActionState;
    do
    {
      return false;
      localActionState = this.mEventBus.getActionState();
    } while ((!Feature.FOLLOW_ON.isEnabled()) || (this.mPendingFollowOnQuery == Query.EMPTY) || (!this.mEventBus.getTtsState().isDone()) || (!localActionState.haveCards()) || (isEditingQuery()));
    Preconditions.checkState(localActionState.hasDataForQuery(this.mCommittedQuery));
    Query localQuery = this.mPendingFollowOnQuery;
    maybePushQuery();
    this.mCommittedQuery = localQuery;
    this.mQuery = this.mQuery.withCommitIdFrom(this.mCommittedQuery);
    return true;
  }
  
  private boolean maybeGoBackOnCardActionComplete()
  {
    int i = -1 + this.mBackStack.size();
    if ((i >= 0) && (((BackStackEntry)this.mBackStack.get(i)).query.isSentinel())) {
      return goBack();
    }
    return false;
  }
  
  private void maybeLogLatencyEvents()
  {
    int i;
    if ((haveCommit()) && (!this.mLatencyTracker.isLogged()))
    {
      i = this.mNetworkInformation.getConnectionId();
      if (!this.mCommittedQuery.isVoiceSearch()) {
        break label46;
      }
      this.mLatencyTracker.logLatencyEvents(85, i);
    }
    label46:
    do
    {
      return;
      if (this.mCommittedQuery.isTextSearch())
      {
        this.mLatencyTracker.logLatencyEvents(86, i);
        return;
      }
    } while (!this.mCommittedQuery.isMusicSearch());
    this.mLatencyTracker.logLatencyEvents(104, i);
  }
  
  private void maybePushQuery()
  {
    maybePushQuery(false);
  }
  
  private void maybePushQuery(boolean paramBoolean)
  {
    int i = 1;
    ActionState localActionState = this.mEventBus.getActionState();
    if (((this.mCommittedQuery.isVoiceSearch()) || (this.mCommittedQuery.isMusicSearch()) || (this.mCommittedQuery.isTvSearch())) && (!this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) && ((!localActionState.hasDataForQuery(this.mCommittedQuery)) || (localActionState.getActionData().isEmpty()))) {
      i = 0;
    }
    if ((isShowingSummonsQuery()) && (!paramBoolean)) {
      i = 0;
    }
    if (this.mCommittedQuery.isPredictiveTvSearch()) {
      i = 0;
    }
    if (i != 0)
    {
      if (!this.mBackStack.isEmpty()) {
        ((BackStackEntry)this.mBackStack.get(-1 + this.mBackStack.size())).searchResult = null;
      }
      this.mBackStack.add(new BackStackEntry(this.mCommittedQuery, localActionState.getActionData(), localActionState.getVoiceActions(), this.mWebViewSearchResult));
    }
    this.mPendingFollowOnQuery = Query.EMPTY;
  }
  
  private void onNetworkLoadErrorInternal(SearchError paramSearchError)
  {
    if ((paramSearchError.isAuthError()) && (!this.mCommittedQuery.shouldExcludeAuthTokens()))
    {
      retryFromSrpAuthError();
      return;
    }
    this.mNetworkActionState.loadError(paramSearchError);
    reportFailureEvent(paramSearchError);
    notifyChanged();
  }
  
  private boolean onSearchResultChanged()
  {
    if ((this.mCurrentSearchResult.isFailed()) && (!this.mNetworkActionState.isFinishedFor(this.mCommittedQuery))) {
      onNetworkLoadErrorInternal(new WebSearchConnectionError(this.mCurrentSearchResult.getError(), "search result error"));
    }
    label99:
    do
    {
      return true;
      Query localQuery;
      if (this.mCurrentSearchResult.getSrpMetadata() != null)
      {
        localQuery = this.mCurrentSearchResult.getSrpQuery();
        if (!localQuery.isRewritten()) {
          break label154;
        }
        if (!localQuery.getQueryStringForSearch().equals(this.mCommittedQuery.getQueryStringForSearch())) {
          this.mQuery = localQuery;
        }
      }
      for (this.mCommittedQuery = localQuery; (this.mCommittedQuery.shouldShowCards()) && (this.mCurrentSearchResult.getActionData() != null) && (!this.mNetworkActionState.isFinishedFor(this.mCommittedQuery)); this.mCommittedQuery = localQuery)
      {
        reportLatencyEvent(2);
        this.mNetworkActionState.loadComplete(this.mCurrentSearchResult.getActionData());
        return true;
        if ((!localQuery.needVoiceCorrection()) || (this.mCommittedQuery == localQuery)) {
          break label99;
        }
        this.mQuery = localQuery;
      }
    } while ((!isCurrentCommit(this.mWebviewState.getQuery())) && (this.mCurrentSearchResult.getWebPage() != null));
    label154:
    return false;
  }
  
  private boolean popQuery()
  {
    int i = -1 + this.mBackStack.size();
    if (i < 0)
    {
      reset();
      return false;
    }
    BackStackEntry localBackStackEntry = (BackStackEntry)this.mBackStack.remove(i);
    EventLogger.recordSpeechEvent(3, null);
    Query localQuery = localBackStackEntry.query.fromBackStack();
    setCommittedQuery(localQuery);
    this.mNetworkActionState.setVoiceSearchResultsDone();
    this.mLatencyTracker.reset();
    this.mNetworkActionState.reset();
    if (localBackStackEntry.actionData != null)
    {
      this.mPumpkinActionState.queryCommitted(this.mCommittedQuery);
      if (localBackStackEntry.actionData.isNetworkAction())
      {
        this.mPumpkinActionState.loadComplete(ActionData.NONE);
        if ((localQuery.isMusicSearch()) || (localQuery.isTvSearch()))
        {
          this.mNetworkActionState.queryCommitted(localQuery);
          this.mNetworkActionState.loadComplete(localBackStackEntry.actionData);
        }
      }
    }
    for (;;)
    {
      if ((localBackStackEntry.searchResult != this.mWebViewSearchResult) || ((localBackStackEntry.searchResult != null) && (!this.mWebviewState.isLoadedFor(localBackStackEntry.searchResult.getSrpQuery()))))
      {
        this.mWebviewState.reset();
        setCurrentSearchResult(null);
      }
      this.mLastPoppedBackStackEntry = localBackStackEntry;
      return true;
      this.mPumpkinActionState.loadComplete(localBackStackEntry.actionData);
      break;
      this.mPumpkinActionState.reset();
    }
  }
  
  private void reportFailureEvent(SearchError paramSearchError)
  {
    this.mLatencyTracker.reportError(paramSearchError.getErrorTypeForLogs());
    if (getError() != null) {
      maybeLogLatencyEvents();
    }
  }
  
  private boolean resetSearchClientInternal()
  {
    if (isExternalSentinelQuery(this.mCommittedQuery))
    {
      this.mQuery = this.mQuery.withQueryChars("");
      return true;
    }
    for (int i = -1 + this.mBackStack.size();; i--) {
      if ((i < 0) || (isExternalSentinelQuery(((BackStackEntry)this.mBackStack.get(i)).query)))
      {
        if (i < 0) {
          break label149;
        }
        setCommittedQuery(((BackStackEntry)this.mBackStack.get(i)).query);
        this.mQuery = this.mQuery.withQueryChars("");
        for (int j = -1 + this.mBackStack.size(); j >= i; j--) {
          this.mBackStack.remove(j);
        }
      }
    }
    this.mLastPoppedBackStackEntry = null;
    return true;
    label149:
    return false;
  }
  
  private Query restoreQuery(Bundle paramBundle, String paramString)
  {
    Query localQuery = (Query)paramBundle.getParcelable(paramString);
    if (localQuery != null) {
      localQuery = localQuery.restoredState();
    }
    return localQuery;
  }
  
  private void setCommittedQuery(Query paramQuery)
  {
    this.mCommittedQuery = paramQuery;
    if (shouldEditOnCommit(paramQuery))
    {
      this.mQuery = paramQuery.text().clearCommit();
      return;
    }
    this.mQuery = paramQuery;
  }
  
  private void setCurrentSearchResult(SearchResult paramSearchResult)
  {
    if (this.mCurrentSearchResult != null) {
      this.mCurrentSearchResult.deleteObserver(this.mSearchResultObserver);
    }
    this.mCurrentSearchResult = paramSearchResult;
    if ((!this.mCommittedQuery.isVoiceSearch()) || (paramSearchResult != null)) {
      this.mWebViewSearchResult = this.mCurrentSearchResult;
    }
    if (this.mCurrentSearchResult != null)
    {
      this.mCurrentSearchResult.addObserver(this.mSearchResultObserver);
      onSearchResultChanged();
    }
  }
  
  private boolean setQuery(Query paramQuery)
  {
    Preconditions.checkNotNull(paramQuery);
    Query localQuery = this.mQuery;
    boolean bool = false;
    if (paramQuery != localQuery)
    {
      this.mResolvingAdClickUrl = false;
      this.mQuery = paramQuery;
      bool = true;
    }
    return bool;
  }
  
  private boolean shouldEditOnCommit(Query paramQuery)
  {
    return (paramQuery.isSentinel()) && (UiMode.fromSentinelQuery(paramQuery).isSuggestMode());
  }
  
  private boolean shouldResetQueryOnStopEdit()
  {
    if (shouldEditOnCommit(this.mCommittedQuery)) {}
    while ((isShowingSummonsQuery()) || (this.mQuery.isSecondarySearchQuery()) || (isExternalSentinelQuery(this.mCommittedQuery))) {
      return false;
    }
    return true;
  }
  
  private boolean startQueryEditInternal()
  {
    if (!isEditingQuery())
    {
      if (shouldResetQueryOnStopEdit()) {}
      for (this.mQuery = this.mCommittedQuery.text().clearCommit();; this.mQuery = this.mQuery.text().clearCommit()) {
        return true;
      }
    }
    return false;
  }
  
  private static String stateString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "INVALID(" + paramInt + ")";
    case 0: 
      return "IDLE";
    case 1: 
      return "COMMITTED";
    case 2: 
      return "LOADING";
    case 3: 
      return "LOADED";
    }
    return "ERROR";
  }
  
  private boolean stopListeningInternal(Query paramQuery)
  {
    if (!isCurrentCommit(paramQuery)) {}
    while ((this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) || (this.mEventBus.getActionState().hasDataForQuery(this.mCommittedQuery))) {
      return false;
    }
    this.mStopListeningPending = true;
    return true;
  }
  
  private void stopQueryEditInternal()
  {
    if (shouldResetQueryOnStopEdit())
    {
      this.mQuery = this.mCommittedQuery;
      return;
    }
    this.mQuery = this.mQuery.withCommitIdFrom(this.mCommittedQuery);
  }
  
  public boolean adClickInProgress()
  {
    return this.mResolvingAdClickUrl;
  }
  
  boolean areNetworkResultsFinished()
  {
    return this.mNetworkActionState.isFinishedFor(this.mCommittedQuery);
  }
  
  boolean areVoiceSearchResultsDone(Query paramQuery)
  {
    return (this.mNetworkActionState.isCurrentCommit(paramQuery)) && (this.mNetworkActionState.areVoiceSearchResultsDone());
  }
  
  boolean areWebSearchResultsFinished()
  {
    return (this.mWebViewSearchResult != null) && (this.mWebviewState.isFinishedFor(this.mWebViewSearchResult.getSrpQuery()));
  }
  
  boolean canForceReload()
  {
    return (this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) && (this.mWebCorporaAvailable);
  }
  
  public boolean canPumpkinHandleCurrentCommit()
  {
    return canPumpkinHandleQuery(this.mCommittedQuery);
  }
  
  public void commit()
  {
    if (this.mQuery.canCommit())
    {
      maybePushQuery();
      if (this.mQuery.isEyesFree()) {
        this.mQuery = this.mQuery.withCarMode(this.mCarConnected);
      }
      this.mQuery = this.mSearchBoxLogging.snapshotServiceSideQueryStats(this.mQuery);
      this.mLatencyTracker.reset();
      this.mExternalActivityLaunching = false;
      setCommittedQuery(this.mQuery.committed());
      setCurrentSearchResult(null);
      notifyChanged();
    }
  }
  
  public void commit(Query paramQuery)
  {
    setQuery(paramQuery);
    commit();
  }
  
  public void commitSearchClient()
  {
    Preconditions.checkState(isExternalSentinelQuery(((BackStackEntry)this.mBackStack.get(-1 + this.mBackStack.size())).query));
    this.mBackStack.clear();
    this.mBackStack.add(new BackStackEntry(Query.EMPTY.sentinel(UiMode.NONE, null).committed(), null, null, null));
    this.mLastPoppedBackStackEntry = null;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("QueryState:");
    String str1 = paramString + "  ";
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mQuery: ");
    paramPrintWriter.println(this.mQuery);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mCommittedQuery: ");
    paramPrintWriter.println(this.mCommittedQuery);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mPendingFollowOnQuery: ");
    paramPrintWriter.println(this.mPendingFollowOnQuery);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mCurrentSearchResult: ");
    paramPrintWriter.println(this.mCurrentSearchResult);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mWebviewState: ");
    paramPrintWriter.println(this.mWebviewState);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mNetworkActionState: ");
    paramPrintWriter.println(this.mNetworkActionState);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("mPumpkinActionState: ");
    paramPrintWriter.println(this.mPumpkinActionState);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("HotwordDetectionEnabled: ");
    paramPrintWriter.println(this.mHotwordDetectionEnabled);
    paramPrintWriter.print(str1);
    paramPrintWriter.print("HotwordState: ");
    paramPrintWriter.println(hotwordStateString(this.mHotwordState));
    paramPrintWriter.print(str1);
    paramPrintWriter.print("MinHotwordQuality: ");
    paramPrintWriter.println(this.mMinimumHotwordQuality);
    paramPrintWriter.print(str1);
    paramPrintWriter.println("Backstack");
    String str2 = str1 + "  ";
    int i = -1 + this.mBackStack.size();
    if (i >= 0)
    {
      BackStackEntry localBackStackEntry = (BackStackEntry)this.mBackStack.get(i);
      paramPrintWriter.print(str2);
      paramPrintWriter.println(localBackStackEntry.query);
      paramPrintWriter.print(str2);
      if (localBackStackEntry.actionData != null) {}
      for (Object localObject = localBackStackEntry.actionData;; localObject = "[no action]")
      {
        paramPrintWriter.println(localObject);
        i--;
        break;
      }
    }
  }
  
  public void forceReloadIfPossible()
  {
    if (canForceReload())
    {
      Query localQuery = this.mCommittedQuery.forceReload();
      if (!isEditingQuery()) {
        this.mQuery = localQuery;
      }
      this.mCommittedQuery = localQuery;
      this.mPumpkinActionState.reset();
      this.mPendingFollowOnQuery = Query.EMPTY;
      setCurrentSearchResult(null);
      notifyChanged();
    }
  }
  
  public Query get()
  {
    return this.mQuery;
  }
  
  ActionData getActionData()
  {
    int i = 1;
    ActionData localActionData3;
    if (!this.mCommittedQuery.shouldShowCards()) {
      localActionData3 = ActionData.NONE;
    }
    boolean bool;
    do
    {
      int m;
      do
      {
        ActionData localActionData1;
        do
        {
          ActionData localActionData2;
          do
          {
            return localActionData3;
            if (this.mCommittedQuery.isNotificationAnnouncement()) {
              return ActionData.NONE;
            }
            if ((this.mLastPoppedBackStackEntry != null) && (isCurrentCommit(this.mLastPoppedBackStackEntry.query)) && (this.mLastPoppedBackStackEntry.actionData != null)) {
              return this.mLastPoppedBackStackEntry.actionData;
            }
            localActionData1 = (ActionData)this.mNetworkActionState.getDataFor(this.mCommittedQuery);
            localActionData2 = (ActionData)this.mPumpkinActionState.getDataFor(this.mCommittedQuery);
            if (this.mNetworkActionState.getErrorFor(this.mCommittedQuery) != null) {}
            for (int k = i; (localActionData2 != null) && (localActionData2 != ActionData.NONE); m = 0) {
              return localActionData2;
            }
            if ((!canPumpkinHandleQuery(this.mCommittedQuery)) || (this.mPumpkinState != i)) {
              break;
            }
            localActionData3 = null;
          } while (localActionData2 == null);
          if (localActionData1 != null)
          {
            if (localActionData2 == ActionData.NONE) {}
            for (;;)
            {
              Preconditions.checkState(i);
              return localActionData1;
              int j = 0;
            }
          }
          localActionData3 = null;
        } while (m == 0);
        return ActionData.NONE;
        if (localActionData1 != null) {
          return localActionData1;
        }
        localActionData3 = null;
      } while (m == 0);
      bool = canPumpkinHandleQuery(this.mCommittedQuery);
      localActionData3 = null;
    } while (bool);
    return ActionData.NONE;
  }
  
  public Query getCommittedQuery()
  {
    return this.mCommittedQuery;
  }
  
  @Nullable
  public SearchResult getCurrentSearchResultForLogging()
  {
    if ((this.mCurrentSearchResult != null) && (this.mCurrentSearchResult.getWebPage() != null) && (!this.mCurrentSearchResult.isFailedOrCancelled()) && (this.mWebviewState.isCurrentCommit(this.mCurrentSearchResult.getSrpQuery()))) {
      return this.mCurrentSearchResult;
    }
    return null;
  }
  
  @Nullable
  public SearchError getError()
  {
    ActionData localActionData = (ActionData)this.mPumpkinActionState.getDataFor(this.mCommittedQuery);
    SearchError localSearchError1 = this.mNetworkActionState.getErrorFor(this.mCommittedQuery);
    if ((localActionData != null) && (localActionData != ActionData.NONE)) {
      localSearchError1 = null;
    }
    SearchError localSearchError2;
    do
    {
      return localSearchError1;
      if ((canPumpkinHandleQuery(this.mCommittedQuery)) && (this.mPumpkinState == 1) && (localActionData == null)) {
        return null;
      }
      if ((canPumpkinHandleQuery(this.mCommittedQuery)) && (localActionData == null) && (localSearchError1 != null)) {
        return null;
      }
      localSearchError2 = this.mWebviewState.getErrorFor(this.mCommittedQuery);
    } while (localSearchError1 != null);
    if (localSearchError2 != null) {
      return localSearchError2;
    }
    return null;
  }
  
  public LatencyTracker getLatencyTracker()
  {
    return this.mLatencyTracker;
  }
  
  @Nullable
  public Uri getUrlInCommittedQuery()
  {
    if ((!isEditingQuery()) && (this.mQuery.isTextOrVoiceWebSearchWithQueryChars())) {
      return Util.smartUrlFilter(this.mQuery.getQueryString());
    }
    return null;
  }
  
  @Nullable
  List<VoiceAction> getVoiceActionsFromBackStack(ActionData paramActionData)
  {
    if ((this.mLastPoppedBackStackEntry != null) && (paramActionData.equals(this.mLastPoppedBackStackEntry.actionData))) {
      return this.mLastPoppedBackStackEntry.voiceActions;
    }
    return null;
  }
  
  public boolean goBack()
  {
    if (this.mExternalActivityLaunching) {
      return true;
    }
    if ((isEditingQuery()) && (!isShowingSummonsQuery()) && (!resultsLoadedAndNothingToShow())) {
      stopQueryEditInternal();
    }
    do
    {
      notifyChanged();
      return true;
      if (isExternalSentinelQuery(this.mCommittedQuery)) {
        break;
      }
      this.mEventBus.getActionState().maybeLogGoBack();
      reportLatencyEvent(45);
    } while (popQuery());
    return false;
  }
  
  boolean hasNetworkActionError()
  {
    return this.mNetworkActionState.hasError();
  }
  
  boolean hasWebviewStateError()
  {
    return this.mWebviewState.hasError();
  }
  
  public boolean haveCommit()
  {
    return this.mCommittedQuery.isValidSearch();
  }
  
  public boolean isCharging()
  {
    return this.mCharging;
  }
  
  public boolean isCommittedQuerySoundSearchWithResult()
  {
    ActionState localActionState = this.mEventBus.getActionState();
    return ((this.mCommittedQuery.isMusicSearch()) || (this.mCommittedQuery.isTvSearch())) && (localActionState.hasDataForQuery(this.mCommittedQuery)) && (!localActionState.getActionData().isEmpty());
  }
  
  public boolean isCurrentCommit(Query paramQuery)
  {
    return paramQuery.getCommitId() == this.mCommittedQuery.getCommitId();
  }
  
  public boolean isEditingQuery()
  {
    return !isCurrentCommit(this.mQuery);
  }
  
  public boolean isHotwordActive()
  {
    return this.mHotwordState == 2;
  }
  
  public boolean isHotwordSupported()
  {
    return this.mHotwordState != 1;
  }
  
  public boolean isLoadingWebSearchResults()
  {
    return this.mWebviewState.isLoadingResult(this.mWebViewSearchResult);
  }
  
  public boolean isMusicDetected()
  {
    return this.mClock.uptimeMillis() - this.mMusicLastDetectedAt < this.mConfig.getMusicDetectionTimeoutMs();
  }
  
  boolean isNetworkActionEmpty()
  {
    ActionData localActionData = (ActionData)this.mNetworkActionState.getDataFor(this.mCommittedQuery);
    return (localActionData != null) && (localActionData.isEmpty());
  }
  
  public boolean isPaused(Query paramQuery)
  {
    return this.mNetworkActionState.isPausedFor(paramQuery);
  }
  
  public boolean isQueryInBackStack(Query paramQuery)
  {
    if (!paramQuery.isSentinel()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Iterator localIterator = this.mBackStack.iterator();
      do
      {
        if (!localIterator.hasNext()) {
          break;
        }
      } while (((BackStackEntry)localIterator.next()).query.getCommitId() != paramQuery.getCommitId());
      return true;
    }
    return false;
  }
  
  boolean isWebViewReadyToShow()
  {
    return this.mWebviewState.mReadyToShow;
  }
  
  public boolean isZeroQuery()
  {
    return this.mQuery.getQueryString().isEmpty();
  }
  
  public void maybePopExternalActivitySentinel()
  {
    if ((this.mQuery.isExternalActivitySentinel()) && (this.mExternalActivityLaunching))
    {
      this.mExternalActivityLaunching = false;
      popQuery();
      notifyChanged();
    }
  }
  
  public void newQueryFromWebView(Query paramQuery)
  {
    if (this.mUrlHelper.equivalentForSearch(paramQuery, this.mQuery)) {
      return;
    }
    this.mLatencyTracker.reset();
    maybePushQuery();
    this.mCommittedQuery = paramQuery.committed();
    setQuery(this.mCommittedQuery);
    setCurrentSearchResult(null);
    notifyChanged();
  }
  
  public void onAdClickComplete()
  {
    this.mResolvingAdClickUrl = false;
    notifyChanged();
  }
  
  public void onAdClickStart()
  {
    this.mResolvingAdClickUrl = true;
    notifyChanged();
  }
  
  public void onCarConnectedChanged(boolean paramBoolean)
  {
    if (paramBoolean != this.mCarConnected)
    {
      this.mCarConnected = paramBoolean;
      this.mCommittedQuery = this.mCommittedQuery.withCarMode(paramBoolean);
      notifyChanged();
    }
  }
  
  public void onCharging(boolean paramBoolean)
  {
    if (paramBoolean != this.mCharging)
    {
      this.mCharging = paramBoolean;
      notifyChanged();
    }
  }
  
  public void onGreco3DataManagerInitialized()
  {
    notifyChanged();
  }
  
  public void onListeningForHotwordChanged(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1) {
      if (paramBoolean2) {
        i = 2;
      }
    }
    for (;;)
    {
      if (i != this.mHotwordState)
      {
        this.mHotwordState = i;
        notifyChanged();
      }
      return;
      i = 0;
      continue;
      i = 1;
    }
  }
  
  public void onMusicDetected()
  {
    long l = this.mClock.uptimeMillis();
    if (l - this.mMusicLastDetectedAt > this.mConfig.getMusicDetectionTimeoutMs()) {}
    for (int i = 1;; i = 0)
    {
      this.mMusicLastDetectedAt = l;
      if (i != 0) {
        notifyChanged();
      }
      return;
    }
  }
  
  public void onNetworkActionData(Query paramQuery, ActionData paramActionData)
  {
    if ((isCurrentCommit(paramQuery)) && (!this.mNetworkActionState.hasError()))
    {
      reportLatencyEvent(2);
      this.mNetworkActionState.loadComplete(paramActionData);
      notifyChanged();
    }
  }
  
  public void onNetworkLoadError(Query paramQuery, SearchError paramSearchError)
  {
    if (isCurrentCommit(paramQuery))
    {
      onNetworkLoadErrorInternal(paramSearchError);
      notifyChanged();
    }
  }
  
  public void onNetworkSearchResult(Query paramQuery, SearchResult paramSearchResult)
  {
    if (isCurrentCommit(paramQuery))
    {
      if (paramSearchResult == this.mCurrentSearchResult) {
        break label26;
      }
      setCurrentSearchResult(paramSearchResult);
      notifyChanged();
    }
    label26:
    while (!onSearchResultChanged()) {
      return;
    }
    notifyChanged();
  }
  
  public void onPumpkinActionData(Query paramQuery, ActionData paramActionData)
  {
    if (isCurrentCommit(paramQuery))
    {
      this.mLatencyTracker.reportLatencyEvent(34);
      this.mPumpkinActionState.loadComplete(paramActionData);
      notifyChanged();
    }
  }
  
  public void onPumpkinDestroyed()
  {
    if (this.mPumpkinState != 0)
    {
      this.mPumpkinState = 0;
      notifyChanged();
    }
  }
  
  public void onPumpkinInitialized(boolean paramBoolean)
  {
    if (this.mPumpkinState == 0) {
      if (!paramBoolean) {
        break label23;
      }
    }
    label23:
    for (int i = 1;; i = 2)
    {
      this.mPumpkinState = i;
      notifyChanged();
      return;
    }
  }
  
  public void onRecognitionPaused(Query paramQuery)
  {
    if ((isCurrentCommit(paramQuery)) && (this.mNetworkActionState.isCommittedOrLoadingFor(paramQuery)))
    {
      if (!paramQuery.isVoiceSearch()) {
        break label68;
      }
      UiState localUiState = this.mEventBus.getUiState();
      if ((!localUiState.shouldShowCards()) && (!localUiState.shouldShowWebView())) {
        break label58;
      }
      popQuery();
    }
    for (;;)
    {
      notifyChanged();
      return;
      label58:
      this.mNetworkActionState.paused();
      continue;
      label68:
      this.mNetworkActionState.reset();
    }
  }
  
  public void onResumeVelvet(Query paramQuery, ActionData paramActionData)
  {
    Preconditions.checkNotNull(paramQuery);
    if (isCurrentCommit(paramQuery)) {}
    while (!this.mBackStack.isEmpty()) {
      return;
    }
    maybePushQuery();
    setQuery(paramQuery);
    this.mCommittedQuery = paramQuery;
    setCurrentSearchResult(null);
    if (paramActionData != null)
    {
      this.mNetworkActionState.queryCommitted(paramQuery);
      this.mNetworkActionState.loadComplete(paramActionData);
    }
    notifyChanged();
  }
  
  public void onSpokenLocaleChanged()
  {
    notifyChanged();
  }
  
  public void onStartupComplete()
  {
    if (!this.mStartupComplete)
    {
      this.mStartupComplete = true;
      notifyChanged();
    }
  }
  
  protected void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    boolean bool1 = paramEvent.hasActionChanged();
    boolean bool2 = false;
    ActionState localActionState;
    if (bool1)
    {
      localActionState = this.mEventBus.getActionState();
      boolean bool3 = localActionState.isReady();
      bool2 = false;
      if (bool3) {
        if ((!localActionState.isCardActionComplete()) || (!maybeGoBackOnCardActionComplete())) {}
      }
    }
    do
    {
      return;
      Query localQuery = localActionState.takeModifiedCommit();
      if (localQuery != null)
      {
        switchQuery(this.mCommittedQuery, localQuery);
        return;
      }
      bool2 = false | editQueryIfNothingToShow();
      if ((paramEvent.hasTtsChanged()) && (this.mEventBus.getTtsState().isDone())) {
        bool2 |= maybeCommitPendingFollowOnQuery();
      }
    } while (!bool2);
    notifyChanged();
  }
  
  public void onTextRecognized(Query paramQuery, CharSequence paramCharSequence, @Nullable ImmutableList<CharSequence> paramImmutableList)
  {
    if (isCurrentCommit(paramQuery))
    {
      Query localQuery = this.mCommittedQuery.withRecognizedText(paramCharSequence, paramImmutableList, paramQuery.needVoiceCorrection());
      setQuery(localQuery);
      this.mCommittedQuery = localQuery;
      notifyChanged();
    }
  }
  
  public void onUserInteraction()
  {
    this.mPendingFollowOnQuery = Query.EMPTY;
    if ((this.mCommittedQuery.isFollowOn()) && (stopListeningInternal(this.mCommittedQuery))) {
      notifyChanged();
    }
  }
  
  public void onVoiceSearchResultsDone(Query paramQuery)
  {
    if ((isCurrentCommit(paramQuery)) && (!this.mNetworkActionState.hasError()))
    {
      this.mNetworkActionState.setVoiceSearchResultsDone();
      notifyChanged();
    }
  }
  
  public void onWebCorporaAvailable()
  {
    if (!this.mWebCorporaAvailable)
    {
      this.mWebCorporaAvailable = true;
      notifyChanged();
    }
  }
  
  public void popSearchClient()
  {
    if (resetSearchClientInternal())
    {
      Preconditions.checkState(isExternalSentinelQuery(this.mCommittedQuery));
      Query localQuery = this.mCommittedQuery;
      popQuery();
      if (!TextUtils.equals(this.mQuery.getQueryChars(), localQuery.getQueryChars())) {
        this.mQuery = this.mQuery.clearCommit().withQueryChars(localQuery.getQueryChars());
      }
    }
  }
  
  public void pushSearchClient()
  {
    if (!resetSearchClientInternal())
    {
      Query localQuery = Query.EMPTY.committed().withQueryChars(this.mQuery.getQueryChars()).sentinel(UiMode.EXTERNAL, null);
      maybePushQuery(true);
      setCommittedQuery(localQuery);
      this.mQuery = this.mQuery.withQueryChars("");
    }
    notifyChanged();
  }
  
  public boolean queryShouldKeepSearchServiceInForeground()
  {
    return (this.mCommittedQuery.isEyesFree()) && (shouldKeepAudioOpen());
  }
  
  public void recommit(Query paramQuery)
  {
    if (isCurrentCommit(paramQuery)) {
      switchQuery(paramQuery, paramQuery);
    }
  }
  
  public void replaceOrPushExternalQueryAndCommit(Query paramQuery1, Query paramQuery2)
  {
    Preconditions.checkArgument(paramQuery1.isExternalActivitySentinel());
    boolean bool = this.mQuery.isExternalActivitySentinel();
    if (bool)
    {
      Preconditions.checkState(this.mExternalActivityLaunching);
      this.mExternalActivityLaunching = false;
    }
    for (;;)
    {
      Query localQuery = paramQuery1.committed();
      this.mQuery = localQuery;
      this.mCommittedQuery = localQuery;
      if (!bool) {
        setCurrentSearchResult(null);
      }
      commit(paramQuery2);
      return;
      maybePushQuery();
    }
  }
  
  public void reportLatencyEvent(int paramInt)
  {
    ExtraPreconditions.checkMainThread();
    this.mLatencyTracker.reportLatencyEvent(paramInt);
    if ((paramInt == 14) || (paramInt == 45) || ((paramInt == 39) && (!this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()))) {
      maybeLogLatencyEvents();
    }
  }
  
  public void reset()
  {
    this.mResolvingAdClickUrl = false;
    this.mNetworkActionState.reset();
    this.mPumpkinActionState.reset();
    this.mWebviewState.reset();
    this.mExternalActivityLaunching = false;
    this.mBackStack.clear();
    this.mLatencyTracker.reset();
    this.mPendingFollowOnQuery = Query.EMPTY;
    this.mCommittedQuery = Query.EMPTY.sentinel(UiMode.NONE, null).committed();
    setCurrentSearchResult(null);
    this.mSuggestSessionStarted = false;
    setQuery(this.mCommittedQuery);
  }
  
  public void resetSearchClient()
  {
    Preconditions.checkState(resetSearchClientInternal());
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    reset();
    this.mQuery = restoreQuery(paramBundle, "velvet:query_state:query");
    this.mCommittedQuery = restoreQuery(paramBundle, "velvet:query_state:committed_query");
    ActionData localActionData1 = this.mEventBus.getActionState().getActionData();
    LoadState localLoadState;
    if (localActionData1 != null)
    {
      this.mPumpkinActionState.queryCommitted(this.mCommittedQuery);
      localLoadState = this.mPumpkinActionState;
      if (!localActionData1.isNetworkAction()) {
        break label163;
      }
    }
    label163:
    for (ActionData localActionData2 = ActionData.NONE;; localActionData2 = localActionData1)
    {
      localLoadState.loadComplete(localActionData2);
      if ((this.mCommittedQuery.isMusicSearch()) || (this.mCommittedQuery.isTvSearch()))
      {
        this.mNetworkActionState.queryCommitted(this.mCommittedQuery);
        this.mNetworkActionState.loadComplete(localActionData1);
      }
      Parcelable[] arrayOfParcelable = paramBundle.getParcelableArray("velvet:query_state:back_stack");
      for (int i = 0; i < arrayOfParcelable.length; i++)
      {
        BackStackEntry localBackStackEntry = (BackStackEntry)arrayOfParcelable[i];
        this.mBackStack.add(localBackStackEntry);
      }
    }
    popSearchClient();
    if (this.mQuery.isExternalActivitySentinel()) {
      popQuery();
    }
  }
  
  boolean resultsLoadedAndNothingToShow()
  {
    ActionState localActionState = this.mEventBus.getActionState();
    return (haveCommit()) && (!this.mCommittedQuery.isSummonsCorpus()) && (localActionState.hasDataForQuery(this.mCommittedQuery)) && (localActionState.isReady()) && (!localActionState.haveCards()) && (getError() == null) && ((!this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) || (this.mEventBus.getUiState().getEffectOnWebResults().shouldPreventWebResults()));
  }
  
  public void resultsPageEnd(Query paramQuery)
  {
    if (this.mWebviewState.isCurrentCommit(paramQuery))
    {
      this.mWebviewState.loadComplete(null);
      maybeCommitPendingFollowOnQuery();
      notifyChanged();
    }
  }
  
  public void resultsPageError(Query paramQuery, SearchError paramSearchError)
  {
    if (this.mWebviewState.isCurrentCommit(paramQuery))
    {
      this.mWebviewState.loadError(paramSearchError);
      reportFailureEvent(paramSearchError);
      notifyChanged();
    }
  }
  
  public void resultsPageStart(Query paramQuery)
  {
    if (this.mWebviewState.isCurrentCommit(paramQuery))
    {
      this.mWebviewState.loadStarted();
      notifyChanged();
    }
  }
  
  public void retry(Query paramQuery)
  {
    Preconditions.checkArgument(paramQuery.isValidSearch());
    if (isCurrentCommit(paramQuery))
    {
      this.mCommittedQuery = paramQuery.committed();
      this.mQuery = this.mCommittedQuery;
      setCurrentSearchResult(null);
      notifyChanged();
    }
  }
  
  protected void retryFromSrpAuthError()
  {
    if (!this.mCommittedQuery.isTextOrVoiceWebSearchWithQueryChars()) {
      VelvetStrictMode.logW("Velvet.QueryState", "SRP Auth failure for search type without SRP.");
    }
    this.mCommittedQuery = this.mCommittedQuery.fromAuthFailure();
    this.mQuery = this.mCommittedQuery;
    setCurrentSearchResult(null);
    notifyChanged();
  }
  
  public void saveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("velvet:query_state:query", this.mQuery);
    paramBundle.putParcelable("velvet:query_state:committed_query", this.mCommittedQuery.fromBackStack());
    paramBundle.putParcelableArray("velvet:query_state:back_stack", (Parcelable[])this.mBackStack.toArray(new BackStackEntry[this.mBackStack.size()]));
  }
  
  public void set(Query paramQuery)
  {
    if (setQuery(paramQuery)) {
      notifyChanged();
    }
  }
  
  public void setAudioFocusSettling(boolean paramBoolean)
  {
    if (paramBoolean != this.mAudioFocusSettling)
    {
      this.mAudioFocusSettling = paramBoolean;
      notifyChanged();
    }
  }
  
  public void setBrowserDimensionsAvailable(boolean paramBoolean)
  {
    if (paramBoolean != this.mBrowserDimensionsAvailable)
    {
      this.mBrowserDimensionsAvailable = paramBoolean;
      notifyChanged();
    }
  }
  
  public void setCookiesAccessAllowed(boolean paramBoolean)
  {
    if (this.mCookiesAccessAllowed != paramBoolean)
    {
      this.mCookiesAccessAllowed = paramBoolean;
      notifyChanged();
    }
  }
  
  public void setHotwordDetectionEnabled(boolean paramBoolean)
  {
    if (paramBoolean != this.mHotwordDetectionEnabled)
    {
      this.mHotwordDetectionEnabled = paramBoolean;
      notifyChanged();
    }
  }
  
  public void setMinimumHotwordQuality(int paramInt)
  {
    if (paramInt != this.mMinimumHotwordQuality)
    {
      this.mMinimumHotwordQuality = paramInt;
      notifyChanged();
    }
  }
  
  public boolean shouldCancel(Query paramQuery)
  {
    if (!paramQuery.isSentinel()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (isCurrentCommit(paramQuery)) {
        break;
      }
      return true;
    }
    return false;
  }
  
  public boolean shouldInitSearchController()
  {
    return (this.mStartupComplete) && ((isEditingQuery()) || (this.mCommittedQuery.isValidSearch()));
  }
  
  public boolean shouldKeepAudioOpen()
  {
    boolean bool1 = isCurrentCommit(this.mNetworkActionState.getQuery());
    boolean bool2 = this.mNetworkActionState.isCommittedOrLoadingFor(this.mCommittedQuery);
    int i;
    if (((this.mCommittedQuery.isVoiceSearch()) && (!this.mCommittedQuery.isFromBackStack())) || (this.mCommittedQuery.isMusicSearch()) || (this.mCommittedQuery.isTvSearch()))
    {
      i = 1;
      if (((bool1) && (!bool2)) || (i == 0)) {
        break label85;
      }
    }
    label85:
    while (((this.mCommittedQuery.isNotificationAnnouncement()) && (!this.mEventBus.getTtsState().isDone())) || ((bool1) && (i != 0) && (!this.mNetworkActionState.hasError()) && (!this.mEventBus.getTtsState().isDone())))
    {
      return true;
      i = 0;
      break;
    }
    return false;
  }
  
  public int shouldListenForHotword(int paramInt, boolean paramBoolean)
  {
    if (!this.mStartupComplete) {}
    do
    {
      do
      {
        do
        {
          return 1;
          if (paramBoolean) {
            return 2;
          }
        } while (((!this.mHotwordDetectionEnabled) && (!isHotwordWhenChargingEnabled())) || (this.mAudioFocusSettling));
        if (!this.mConfig.isHotwordEnabled()) {
          return 2;
        }
        if (!this.mSettings.isHotwordDetectorEnabled()) {
          return 2;
        }
        if (paramInt < this.mMinimumHotwordQuality) {
          return 2;
        }
        if (!haveCommit()) {
          break;
        }
        if ((!this.mConfig.isHotwordFromResultsEnabled()) || (paramInt < 1)) {
          return 2;
        }
      } while (((!this.mNetworkActionState.isPausedFor(this.mCommittedQuery)) && (!this.mNetworkActionState.hasErrorFor(this.mCommittedQuery)) && ((!this.mEventBus.getActionState().isReady()) || (shouldKeepAudioOpen()) || ((!this.mCommittedQuery.isTextSearch()) && (!this.mCommittedQuery.isFromBackStack()) && (!areVoiceSearchResultsDone(this.mCommittedQuery))))) || ((isEditingQuery()) && (!this.mQuery.getQueryString().isEmpty())));
      return 0;
    } while ((!this.mCommittedQuery.isSentinel()) || ((isEditingQuery()) && (!this.mQuery.getQueryString().isEmpty())));
    return 0;
  }
  
  boolean shouldOverrideEffectOnWebResults()
  {
    ActionData localActionData = this.mEventBus.getActionState().getActionData();
    return (localActionData != null) && (localActionData.equals(this.mPumpkinActionState.getDataFor(this.mCommittedQuery))) && (this.mNetworkActionState.getDataFor(this.mCommittedQuery) == ActionData.ANSWER_IN_SRP) && (!this.mCommittedQuery.isMusicSearch());
  }
  
  public void startQueryEdit()
  {
    if (startQueryEditInternal()) {
      notifyChanged();
    }
  }
  
  public void stopListening(Query paramQuery)
  {
    if (stopListeningInternal(paramQuery)) {
      notifyChanged();
    }
  }
  
  public void stopQueryEdit()
  {
    if (isEditingQuery())
    {
      stopQueryEditInternal();
      notifyChanged();
    }
  }
  
  public void switchQuery(Query paramQuery1, Query paramQuery2)
  {
    if (isCurrentCommit(paramQuery1))
    {
      this.mLatencyTracker.reportLatencyEvent(36);
      if ((Feature.FOLLOW_ON.isEnabled()) && (paramQuery2.isFollowOn()))
      {
        this.mPendingFollowOnQuery = paramQuery2.committed();
        maybeCommitPendingFollowOnQuery();
        notifyChanged();
      }
    }
    else
    {
      return;
    }
    if (paramQuery2.isSecondarySearchQuery()) {}
    for (;;)
    {
      this.mCommittedQuery = paramQuery2;
      this.mQuery = this.mCommittedQuery;
      this.mPendingFollowOnQuery = Query.EMPTY;
      setCurrentSearchResult(null);
      break;
      paramQuery2 = paramQuery2.committed();
    }
  }
  
  @Nullable
  public Query takeLaunchExternalActivity()
  {
    if ((this.mCommittedQuery.isExternalActivitySentinel()) && (!this.mExternalActivityLaunching))
    {
      Query localQuery = this.mCommittedQuery;
      if (!resetSearchClientInternal()) {
        this.mExternalActivityLaunching = true;
      }
      return localQuery;
    }
    return null;
  }
  
  @Nullable
  public Query takeNewNetworkQuery()
  {
    if ((canUseCommittedQueryToSearch()) && (!isCurrentCommit(this.mNetworkActionState.getQuery())))
    {
      if (this.mCommittedQuery.isVoiceSearch()) {
        reportLatencyEvent(0);
      }
      for (;;)
      {
        this.mNetworkActionState.queryCommitted(this.mCommittedQuery);
        return this.mCommittedQuery;
        if (this.mCommittedQuery.isTextSearch()) {
          if (this.mCommittedQuery.shouldShowCards()) {
            reportLatencyEvent(35);
          } else {
            reportLatencyEvent(1);
          }
        }
      }
    }
    return null;
  }
  
  @Nullable
  public Query takeNewlyCommittedWebQuery()
  {
    if (this.mWebviewState.takeNewlyCommitted()) {
      return this.mWebviewState.getQuery();
    }
    return null;
  }
  
  @Nullable
  public Query takeNewlyLoadedWebQuery()
  {
    if (this.mWebviewState.takeNewlyLoaded()) {
      return this.mWebviewState.getQuery();
    }
    return null;
  }
  
  @Nullable
  public Query takeQueryToCommitToPumpkin()
  {
    if ((this.mPumpkinState == 1) && (canPumpkinHandleQuery(this.mCommittedQuery)) && (!isCurrentCommit(this.mPumpkinActionState.getQuery())))
    {
      this.mLatencyTracker.reportLatencyEvent(33);
      this.mPumpkinActionState.queryCommitted(this.mCommittedQuery);
      return this.mCommittedQuery;
    }
    return null;
  }
  
  @Nullable
  public SearchResult takeReadySearchResult()
  {
    if ((this.mWebViewSearchResult != null) && (!this.mWebViewSearchResult.isFailedOrCancelled()) && (this.mWebViewSearchResult.getWebPage() != null) && (this.mCookiesAccessAllowed))
    {
      Query localQuery = this.mWebViewSearchResult.getSrpQuery();
      if (!this.mWebviewState.isCurrentCommit(localQuery))
      {
        this.mWebviewState.queryCommitted(this.mWebViewSearchResult.getSrpQuery());
        return this.mWebViewSearchResult;
      }
    }
    return null;
  }
  
  public boolean takeStopListening(Query paramQuery)
  {
    boolean bool1 = isCurrentCommit(paramQuery);
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = this.mStopListeningPending;
      bool2 = false;
      if (bool3)
      {
        this.mStopListeningPending = false;
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean takeSuggestSessionStart()
  {
    boolean bool1 = isEditingQuery();
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = this.mSuggestSessionStarted;
      bool2 = false;
      if (!bool3) {
        bool2 = true;
      }
    }
    this.mSuggestSessionStarted = isEditingQuery();
    return bool2;
  }
  
  @Nullable
  public ActionData takeUnusedNetworkActionToLog()
  {
    ActionState localActionState = this.mEventBus.getActionState();
    if ((localActionState.hasDataForQuery(this.mCommittedQuery)) && (this.mNetworkActionState.hasDataFor(this.mCommittedQuery)) && (this.mPumpkinActionState.hasDataFor(this.mCommittedQuery)) && (localActionState.getActionData() == this.mPumpkinActionState.getLoadedData()) && (!this.mLoggedAlternateAction))
    {
      this.mLoggedAlternateAction = true;
      ActionData localActionData = (ActionData)this.mNetworkActionState.getLoadedData();
      if (!localActionData.isEmpty()) {
        return localActionData;
      }
    }
    return null;
  }
  
  public String toString()
  {
    return "QS[\n\t\tQ:" + this.mQuery + "\n\t\tCQ:" + this.mCommittedQuery + "\n\t\tAS:" + this.mNetworkActionState + "\n\t\tWVS:" + this.mWebviewState + "\n\t\tBS:" + this.mBackStack + "\n\t\tSR:" + this.mCurrentSearchResult + "\n\t\tWSR:" + this.mWebViewSearchResult;
  }
  
  public void webViewReadyToShowChanged(boolean paramBoolean)
  {
    if (paramBoolean != this.mWebviewState.mReadyToShow)
    {
      WebviewLoadState.access$202(this.mWebviewState, paramBoolean);
      notifyChanged();
    }
  }
  
  private static class BackStackEntry
    implements Parcelable
  {
    public static final Parcelable.Creator<BackStackEntry> CREATOR = new Parcelable.Creator()
    {
      public QueryState.BackStackEntry createFromParcel(Parcel paramAnonymousParcel)
      {
        ClassLoader localClassLoader = getClass().getClassLoader();
        return new QueryState.BackStackEntry((Query)paramAnonymousParcel.readParcelable(localClassLoader), (ActionData)paramAnonymousParcel.readParcelable(localClassLoader), paramAnonymousParcel.readArrayList(localClassLoader), null);
      }
      
      public QueryState.BackStackEntry[] newArray(int paramAnonymousInt)
      {
        return new QueryState.BackStackEntry[paramAnonymousInt];
      }
    };
    final ActionData actionData;
    final Query query;
    @Nullable
    SearchResult searchResult;
    final List<VoiceAction> voiceActions;
    
    BackStackEntry(Query paramQuery, ActionData paramActionData, List<VoiceAction> paramList, @Nullable SearchResult paramSearchResult)
    {
      this.query = ((Query)Preconditions.checkNotNull(paramQuery));
      this.actionData = paramActionData;
      this.voiceActions = paramList;
      this.searchResult = paramSearchResult;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      return this.query + "/" + this.actionData;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeParcelable(this.query, paramInt);
      paramParcel.writeParcelable(this.actionData, paramInt);
      paramParcel.writeList(this.voiceActions);
    }
  }
  
  private static class LoadState<T>
  {
    private Query mCurrentQuery = Query.EMPTY;
    private SearchError mError;
    private T mLoadedData;
    private boolean mNewlyCommitted;
    private boolean mNewlyLoaded;
    protected int mState;
    private final String mType;
    private boolean mVoiceSearchResultsDone;
    
    LoadState(String paramString)
    {
      this.mType = paramString;
      this.mState = 0;
      this.mError = null;
    }
    
    boolean areVoiceSearchResultsDone()
    {
      return this.mVoiceSearchResultsDone;
    }
    
    T getDataFor(Query paramQuery)
    {
      if (hasDataFor(paramQuery)) {
        return this.mLoadedData;
      }
      return null;
    }
    
    public SearchError getErrorFor(Query paramQuery)
    {
      if (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId()) {
        return this.mError;
      }
      return null;
    }
    
    T getLoadedData()
    {
      return this.mLoadedData;
    }
    
    Query getQuery()
    {
      return this.mCurrentQuery;
    }
    
    boolean hasDataFor(Query paramQuery)
    {
      return (this.mLoadedData != null) && (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId());
    }
    
    boolean hasError()
    {
      return this.mState == 4;
    }
    
    boolean hasErrorFor(Query paramQuery)
    {
      return (isCurrentCommit(paramQuery)) && (this.mState == 4);
    }
    
    boolean isCommittedOrLoadingFor(Query paramQuery)
    {
      return (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId()) && ((this.mState == 2) || (this.mState == 1));
    }
    
    boolean isCurrentCommit(Query paramQuery)
    {
      return this.mCurrentQuery.getCommitId() == paramQuery.getCommitId();
    }
    
    boolean isFinishedFor(Query paramQuery)
    {
      return (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId()) && ((this.mState == 4) || (this.mState == 3) || (this.mState == 0));
    }
    
    boolean isLoadedFor(Query paramQuery)
    {
      return (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId()) && (this.mState == 3);
    }
    
    boolean isLoadingOrLoadedFor(Query paramQuery)
    {
      return (this.mCurrentQuery.getCommitId() == paramQuery.getCommitId()) && ((this.mState == 2) || (this.mState == 3));
    }
    
    boolean isPausedFor(Query paramQuery)
    {
      return (isCurrentCommit(paramQuery)) && (this.mState == 5);
    }
    
    void loadComplete(T paramT)
    {
      this.mLoadedData = paramT;
      setState(3);
    }
    
    void loadError(SearchError paramSearchError)
    {
      this.mError = paramSearchError;
      setState(4);
    }
    
    void paused()
    {
      setState(5);
    }
    
    void queryCommitted(Query paramQuery)
    {
      this.mCurrentQuery = paramQuery;
      this.mLoadedData = null;
      setState(1);
    }
    
    void reset()
    {
      setState(0);
      this.mCurrentQuery = Query.EMPTY;
      this.mLoadedData = null;
    }
    
    protected void setState(int paramInt)
    {
      int i = 1;
      if ((this.mState != i) && (paramInt == i))
      {
        this.mVoiceSearchResultsDone = false;
        this.mNewlyCommitted = i;
        if ((this.mState == 3) || (paramInt != 3)) {
          break label93;
        }
        label38:
        this.mNewlyLoaded = i;
        if (paramInt != 4) {
          break label98;
        }
      }
      label93:
      label98:
      for (SearchError localSearchError = (SearchError)Preconditions.checkNotNull(this.mError);; localSearchError = null)
      {
        this.mError = localSearchError;
        this.mState = paramInt;
        return;
        if ((paramInt == i) || (paramInt == 2) || (paramInt == 3)) {
          break;
        }
        this.mNewlyCommitted = false;
        break;
        i = 0;
        break label38;
      }
    }
    
    void setVoiceSearchResultsDone()
    {
      this.mVoiceSearchResultsDone = true;
    }
    
    boolean takeNewlyCommitted()
    {
      boolean bool = this.mNewlyCommitted;
      this.mNewlyCommitted = false;
      return bool;
    }
    
    boolean takeNewlyLoaded()
    {
      boolean bool = this.mNewlyLoaded;
      this.mNewlyLoaded = false;
      return bool;
    }
    
    public String toString()
    {
      return this.mType + ":" + QueryState.stateString(this.mState) + ":" + this.mCurrentQuery + " NC=" + this.mNewlyCommitted + " NL=" + this.mNewlyLoaded + " D=" + this.mLoadedData + " VoiceDone=" + this.mVoiceSearchResultsDone;
    }
  }
  
  private static class WebviewLoadState
    extends QueryState.LoadState<Void>
  {
    private boolean mReadyToShow;
    
    WebviewLoadState()
    {
      super();
    }
    
    boolean isLoadingResult(SearchResult paramSearchResult)
    {
      return (paramSearchResult != null) && (isLoadingOrLoadedFor(paramSearchResult.getSrpQuery()));
    }
    
    void loadStarted()
    {
      setState(2);
    }
    
    protected void setState(int paramInt)
    {
      if ((this.mState == 4) && ((paramInt == 2) || (paramInt == 3))) {
        return;
      }
      super.setState(paramInt);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append(super.toString());
      if (this.mReadyToShow) {}
      for (String str = ", ready to show";; str = "") {
        return str;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.QueryState
 * JD-Core Version:    0.7.0.1
 */