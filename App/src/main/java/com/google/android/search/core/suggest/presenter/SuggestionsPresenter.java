package com.google.android.search.core.suggest.presenter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.complete.LastQuerySuggestionSource;
import com.google.android.search.core.prefetch.SearchResultFetcher;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.core.suggest.SuggestionsProvider;
import com.google.android.search.core.summons.ContentProviderSource;
import com.google.android.search.core.summons.ShouldQueryStrategy;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.SourceRanker;
import com.google.android.search.core.summons.Sources;
import com.google.android.search.core.summons.icing.ConnectionToIcing;
import com.google.android.search.core.summons.icing.IcingSources;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.VelvetFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class SuggestionsPresenter
  implements VelvetEventBus.Observer
{
  private final Executor mBgExecutor;
  private SuggestionsClient mClient;
  private final Clock mClock;
  private final CoreSearchServices mCoreSearchServices;
  private Suggestions mCurrentSuggestions;
  private VelvetEventBus mEventBus;
  private final VelvetFactory mFactory;
  private boolean mForceSuggestionFetch;
  private final GlobalSearchServices mGlobalSearchServices;
  private LastQuerySuggestionSource mGoogleSource;
  private final ConnectionToIcing mIcingConnection;
  private final IcingInitialization mIcingInit;
  private boolean mInitialized;
  private Query mLastSeenCommittedQuery = Query.EMPTY;
  private Query mLastSeenFailedQuery = Query.EMPTY;
  private Query mLastSeenQuery = Query.EMPTY;
  private long mLastSuggestionFetch;
  private QueryState mQueryState;
  private final RemoveFromHistoryDoneTask mRemoveFromHistoryDoneTask = new RemoveFromHistoryDoneTask(null);
  private final SearchBoxLogging mSearchBoxLogging;
  private final ShouldQueryStrategy mShouldQueryStrategy;
  private Sources<Source> mSources;
  private final DataSetObserver mSourcesObserver;
  private final Supplier<SearchResultFetcher> mSrfSupplier;
  private boolean mStarted;
  private boolean mSuggestionFetchScheduled;
  private boolean mSuggestionRemovalFailureNotificationPending;
  private volatile int mSuggestionsBeingRemoved;
  private final Runnable mSuggestionsTimeoutTask = new Runnable()
  {
    public void run()
    {
      if ((SuggestionsPresenter.this.mCurrentSuggestions != null) && (!SuggestionsPresenter.this.mCurrentSuggestions.isDone()))
      {
        SuggestionsPresenter.this.mGlobalSearchServices.getSuggestionsProvider().cancelOngoingQuery();
        SuggestionsPresenter.this.mCurrentSuggestions.timedOut();
      }
    }
  };
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  private final Runnable mUpdateSuggestionsTask = new Runnable()
  {
    public void run()
    {
      SuggestionsPresenter.this.updateSuggestionsInternal();
    }
  };
  private Locale mUserLocale;
  
  public SuggestionsPresenter(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, CoreSearchServices paramCoreSearchServices, GlobalSearchServices paramGlobalSearchServices, VelvetFactory paramVelvetFactory, SearchBoxLogging paramSearchBoxLogging, Clock paramClock, ConnectionToIcing paramConnectionToIcing, ShouldQueryStrategy paramShouldQueryStrategy, IcingInitialization paramIcingInitialization, Supplier<SearchResultFetcher> paramSupplier)
  {
    this.mGlobalSearchServices = paramGlobalSearchServices;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mFactory = paramVelvetFactory;
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
    this.mBgExecutor = paramExecutor;
    this.mSearchBoxLogging = paramSearchBoxLogging;
    this.mClock = paramClock;
    this.mIcingConnection = paramConnectionToIcing;
    this.mShouldQueryStrategy = paramShouldQueryStrategy;
    this.mSrfSupplier = paramSupplier;
    this.mSourcesObserver = new DataSetObserver()
    {
      public void onChanged()
      {
        SuggestionsPresenter.this.updateSuggestions();
      }
    };
    paramCoreSearchServices.getSearchHistoryChangedObservable().registerObserver(new DataSetObserver()
    {
      public void onChanged()
      {
        SuggestionsPresenter.this.updateSuggestions();
      }
    });
    this.mUserLocale = Locale.getDefault();
    this.mIcingInit = paramIcingInitialization;
  }
  
  private void cancelOngoingQuery()
  {
    
    if (this.mCurrentSuggestions != null)
    {
      this.mCurrentSuggestions.close();
      this.mCurrentSuggestions = null;
      cancelSuggestionsTimeoutTask();
      this.mGlobalSearchServices.getSuggestionsProvider().cancelOngoingQuery();
    }
  }
  
  private void cancelSuggestionsTimeoutTask()
  {
    this.mUiExecutor.cancelExecute(this.mSuggestionsTimeoutTask);
  }
  
  private void doRemoveFromHistory(Suggestion paramSuggestion)
  {
    if (this.mSuggestionsBeingRemoved > 0) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkState(bool1);
      String str = paramSuggestion.getSuggestionQuery();
      boolean bool2 = getGoogleSource().removeFromHistory(str);
      this.mRemoveFromHistoryDoneTask.addSuggestionRemoved(bool2);
      this.mUiExecutor.execute(this.mRemoveFromHistoryDoneTask);
      return;
    }
  }
  
  private LastQuerySuggestionSource getGoogleSource()
  {
    if (this.mGoogleSource == null) {
      this.mGoogleSource = new LastQuerySuggestionSource(this.mFactory.createGoogleSource((SearchResultFetcher)this.mSrfSupplier.get()), this.mCoreSearchServices.getLoginHelper(), this.mCoreSearchServices.getGsaConfigFlags(), this.mClock, this.mCoreSearchServices.getSearchSettings());
    }
    return this.mGoogleSource;
  }
  
  private void getSourcesToQuery(Consumer<List<ContentProviderSource>> paramConsumer)
  {
    this.mGlobalSearchServices.getSourceRanker().getSourcesForQuerying(Consumers.createAsyncConsumer(this.mUiExecutor, paramConsumer));
  }
  
  private void initSummons()
  {
    if (this.mSources == null)
    {
      this.mSources = this.mGlobalSearchServices.getSources();
      this.mSources.registerDataSetObserver(this.mSourcesObserver);
    }
    this.mIcingInit.initialize();
  }
  
  private void maybeClearClientSuggestions()
  {
    if (this.mClient != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (!this.mClient.ignoreClearSuggestionsOnStop()) {
        this.mClient.showSuggestions(Suggestions.NONE);
      }
      return;
    }
  }
  
  private void removeFromHistoryDone(int paramInt1, int paramInt2)
  {
    
    boolean bool;
    if (this.mSuggestionsBeingRemoved >= paramInt1 + paramInt2)
    {
      bool = true;
      Preconditions.checkState(bool);
      this.mSuggestionsBeingRemoved -= paramInt1 + paramInt2;
      if (this.mClient == null) {
        break label86;
      }
      if (this.mSuggestionsBeingRemoved == 0) {
        this.mClient.setWebSuggestionsEnabled(true);
      }
      if (paramInt2 > 0) {
        this.mClient.indicateRemoveFromHistoryFailed();
      }
    }
    for (;;)
    {
      this.mCoreSearchServices.getSearchHistoryChangedObservable().notifyChanged();
      return;
      bool = false;
      break;
      label86:
      if (paramInt2 > 0) {
        this.mSuggestionRemovalFailureNotificationPending = true;
      }
    }
  }
  
  private boolean shouldRefreshSuggestions(Query paramQuery, boolean paramBoolean)
  {
    return (!Query.equivalentForSuggest(this.mLastSeenQuery, paramQuery)) || (paramBoolean);
  }
  
  private void showSuggestions(Suggestions paramSuggestions)
  {
    if (this.mCurrentSuggestions != null)
    {
      this.mCurrentSuggestions.close();
      cancelSuggestionsTimeoutTask();
    }
    this.mCurrentSuggestions = paramSuggestions;
    startSuggestionsTimeoutTask();
    if (this.mClient != null) {
      this.mClient.showSuggestions(this.mCurrentSuggestions);
    }
  }
  
  private void startSuggestionsTimeoutTask()
  {
    this.mUiExecutor.executeDelayed(this.mSuggestionsTimeoutTask, this.mCoreSearchServices.getConfig().getSourceTimeoutMillis());
  }
  
  private void updateSuggestions(long paramLong)
  {
    this.mUiExecutor.cancelExecute(this.mUpdateSuggestionsTask);
    cancelOngoingQuery();
    this.mSuggestionFetchScheduled = true;
    this.mUiExecutor.executeDelayed(this.mUpdateSuggestionsTask, paramLong);
  }
  
  private void updateSuggestionsBuffered()
  {
    if (!this.mStarted) {
      this.mForceSuggestionFetch = true;
    }
    do
    {
      do
      {
        return;
      } while (!this.mShouldQueryStrategy.shouldQueryAnySource());
      this.mSearchBoxLogging.logSuggestRequest();
    } while (this.mSuggestionFetchScheduled);
    updateSuggestions(Math.max(this.mCoreSearchServices.getConfig().getTypingUpdateSuggestionsDelayMillis() - (this.mClock.uptimeMillis() - this.mLastSuggestionFetch), 0L));
  }
  
  private void updateSuggestionsInternal()
  {
    Preconditions.checkNotNull(this.mClient);
    Preconditions.checkNotNull(this.mQueryState);
    this.mLastSuggestionFetch = this.mClock.uptimeMillis();
    this.mForceSuggestionFetch = false;
    this.mSuggestionFetchScheduled = false;
    final Query localQuery = this.mQueryState.get();
    if (!this.mGlobalSearchServices.getShouldQueryStrategy().shouldQuerySummons(localQuery))
    {
      updateSuggestionsInternal(localQuery, Collections.emptyList(), null);
      return;
    }
    getSourcesToQuery(new Consumer()
    {
      public boolean consume(List<ContentProviderSource> paramAnonymousList)
      {
        SuggestionsPresenter.this.updateSuggestionsInternal(localQuery, paramAnonymousList, SuggestionsPresenter.this.mGlobalSearchServices.getIcingSources());
        return true;
      }
    });
  }
  
  private void updateSuggestionsInternal(Query paramQuery, List<ContentProviderSource> paramList, IcingSources paramIcingSources)
  {
    this.mSearchBoxLogging.logSnappyRequest(paramQuery);
    if (this.mShouldQueryStrategy.shouldQueryWeb()) {}
    for (LastQuerySuggestionSource localLastQuerySuggestionSource = getGoogleSource();; localLastQuerySuggestionSource = null)
    {
      showSuggestions(this.mGlobalSearchServices.getSuggestionsProvider().getSuggestions(paramQuery, paramList, localLastQuerySuggestionSource, paramIcingSources));
      return;
    }
  }
  
  public void connectToIcing()
  {
    this.mIcingConnection.startWaitingForQueries();
  }
  
  public void disconnectFromIcing()
  {
    this.mIcingConnection.stopWaitingForQueries();
  }
  
  public void dispose()
  {
    this.mIcingInit.destroy();
    this.mGoogleSource.close();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    this.mIcingConnection.dump(paramString, paramPrintWriter);
  }
  
  public void initialize()
  {
    
    if (!this.mInitialized)
    {
      initSummons();
      this.mInitialized = true;
    }
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if (paramEvent.hasQueryChanged())
    {
      if (this.mQueryState.takeSuggestSessionStart()) {
        this.mSearchBoxLogging.logSuggestSessionStart();
      }
      Query localQuery1 = this.mQueryState.get();
      if (localQuery1 != this.mLastSeenQuery)
      {
        if (shouldRefreshSuggestions(localQuery1, this.mShouldQueryStrategy.updateQueryStrategy(this.mQueryState))) {
          updateSuggestionsBuffered();
        }
        this.mLastSeenQuery = localQuery1;
      }
      Query localQuery2 = this.mQueryState.getCommittedQuery();
      if (localQuery2 != this.mLastSeenCommittedQuery)
      {
        getGoogleSource().newCommittedQuery(localQuery2);
        this.mLastSeenCommittedQuery = localQuery2;
      }
      if ((this.mLastSeenFailedQuery != localQuery2) && (this.mQueryState.getError() != null))
      {
        getGoogleSource().queryFailed(localQuery2);
        this.mLastSeenFailedQuery = localQuery2;
      }
    }
  }
  
  public void removeSuggestionFromHistory(final Suggestion paramSuggestion)
  {
    ExtraPreconditions.checkMainThread();
    Preconditions.checkState(this.mInitialized);
    Preconditions.checkArgument(paramSuggestion.isHistorySuggestion());
    if (paramSuggestion.isWebSearchSuggestion())
    {
      if (this.mClient != null) {
        this.mClient.setWebSuggestionsEnabled(false);
      }
      cancelOngoingQuery();
      this.mSuggestionsBeingRemoved = (1 + this.mSuggestionsBeingRemoved);
      this.mBgExecutor.execute(new Runnable()
      {
        public void run()
        {
          SuggestionsPresenter.this.doRemoveFromHistory(paramSuggestion);
        }
      });
      return;
    }
    Log.w("Search.SuggestionsPresenterImpl", "Attempt to remove non-web suggestion?. Just refresh");
    updateSuggestions();
  }
  
  public void start(SuggestionsClient paramSuggestionsClient, VelvetEventBus paramVelvetEventBus)
  {
    
    if (this.mEventBus != paramVelvetEventBus)
    {
      if (this.mEventBus != null) {
        this.mEventBus.removeObserver(this);
      }
      this.mEventBus = paramVelvetEventBus;
      this.mQueryState = paramVelvetEventBus.getQueryState();
    }
    if (this.mQueryState.takeSuggestSessionStart()) {
      this.mSearchBoxLogging.logSuggestSessionStart();
    }
    if (this.mClient == paramSuggestionsClient) {
      return;
    }
    if (this.mClient != null) {
      maybeClearClientSuggestions();
    }
    this.mClient = paramSuggestionsClient;
    if (!this.mStarted) {
      this.mStarted = true;
    }
    if (this.mInitialized)
    {
      Locale localLocale = Locale.getDefault();
      if (!this.mUserLocale.equals(localLocale))
      {
        this.mUserLocale = localLocale;
        this.mCoreSearchServices.getSearchHistoryChangedObservable().notifyChanged();
      }
    }
    if ((!this.mForceSuggestionFetch) && (this.mCurrentSuggestions != null) && (this.mCurrentSuggestions.isDone()))
    {
      if (this.mCurrentSuggestions.isClosed()) {
        this.mCurrentSuggestions = this.mCurrentSuggestions.getOpenedCopy();
      }
      if (this.mSuggestionsBeingRemoved > 0)
      {
        this.mClient.setWebSuggestionsEnabled(false);
        this.mClient.showSuggestions(this.mCurrentSuggestions);
      }
    }
    for (;;)
    {
      this.mEventBus.addObserver(this);
      return;
      if (!this.mSuggestionRemovalFailureNotificationPending) {
        break;
      }
      this.mClient.indicateRemoveFromHistoryFailed();
      this.mSuggestionRemovalFailureNotificationPending = false;
      break;
      updateSuggestions();
    }
  }
  
  public void stop(SuggestionsClient paramSuggestionsClient, VelvetEventBus paramVelvetEventBus)
  {
    
    if ((paramSuggestionsClient == this.mClient) && (paramVelvetEventBus == this.mEventBus))
    {
      this.mUiExecutor.cancelExecute(this.mUpdateSuggestionsTask);
      this.mSuggestionFetchScheduled = false;
      if (this.mCurrentSuggestions != null)
      {
        cancelSuggestionsTimeoutTask();
        this.mGlobalSearchServices.getSuggestionsProvider().cancelOngoingQuery();
        this.mCurrentSuggestions.close();
        if (this.mClient != null) {
          maybeClearClientSuggestions();
        }
      }
      this.mClient = null;
      this.mStarted = false;
    }
  }
  
  public void updateSuggestions()
  {
    if (this.mUiExecutor.isThisThread())
    {
      updateSuggestionsBuffered();
      return;
    }
    this.mUiExecutor.execute(new Runnable()
    {
      public void run()
      {
        SuggestionsPresenter.this.updateSuggestionsBuffered();
      }
    });
  }
  
  private class RemoveFromHistoryDoneTask
    implements Runnable
  {
    private int mFailed = 0;
    private int mSuccessful = 0;
    
    private RemoveFromHistoryDoneTask() {}
    
    public void addSuggestionRemoved(boolean paramBoolean)
    {
      if (paramBoolean) {}
      for (;;)
      {
        try
        {
          this.mSuccessful = (1 + this.mSuccessful);
          return;
        }
        finally {}
        this.mFailed = (1 + this.mFailed);
      }
    }
    
    public void run()
    {
      try
      {
        SuggestionsPresenter.this.mUiExecutor.cancelExecute(this);
        SuggestionsPresenter.this.removeFromHistoryDone(this.mSuccessful, this.mFailed);
        this.mSuccessful = 0;
        this.mFailed = 0;
        return;
      }
      finally {}
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.presenter.SuggestionsPresenter
 * JD-Core Version:    0.7.0.1
 */