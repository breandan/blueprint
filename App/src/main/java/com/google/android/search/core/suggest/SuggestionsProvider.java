package com.google.android.search.core.suggest;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.GoogleSource;
import com.google.android.search.core.summons.ContentProviderSource;
import com.google.android.search.core.summons.ShouldQueryStrategy;
import com.google.android.search.core.summons.SourceNamedTask;
import com.google.android.search.core.summons.icing.IcingSources;
import com.google.android.search.core.summons.icing.IcingSuggestionsFactory.IcingResults;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.BatchingNamedTaskExecutor;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.NamedTask;
import com.google.android.shared.util.NamedTaskExecutor;
import com.google.android.shared.util.NoOpConsumer;
import com.google.android.shared.util.NonCancellableNamedTask;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class SuggestionsProvider
{
  private static final Consumer<SuggestionList> NO_OP_CONSUMER = new NoOpConsumer();
  private BatchingNamedTaskExecutor mBatchingExecutor;
  private final SearchConfig mConfig;
  private boolean mContentProvidersDone;
  private final Context mContext;
  private final GsaConfigFlags mGsaConfigFlags;
  private boolean mIcingQueryDone;
  private final NowOptInSettings mNowOptInSettings;
  private final ScheduledSingleThreadedExecutor mPublishThread;
  private final NamedTaskExecutor mQueryExecutor;
  private final ShouldQueryStrategy mShouldQueryStrategy;
  private boolean mZeroQueryAppSuggestQueryDone;
  
  public SuggestionsProvider(Context paramContext, SearchConfig paramSearchConfig, GsaConfigFlags paramGsaConfigFlags, ShouldQueryStrategy paramShouldQueryStrategy, NamedTaskExecutor paramNamedTaskExecutor, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, NowOptInSettings paramNowOptInSettings)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
    this.mGsaConfigFlags = paramGsaConfigFlags;
    this.mShouldQueryStrategy = paramShouldQueryStrategy;
    this.mQueryExecutor = paramNamedTaskExecutor;
    this.mPublishThread = paramScheduledSingleThreadedExecutor;
    this.mNowOptInSettings = paramNowOptInSettings;
  }
  
  private NamedTask createGoogleSourceQueryTask(final Query paramQuery, final GoogleSource paramGoogleSource, final Consumer<SuggestionList> paramConsumer)
  {
    new NonCancellableNamedTask()
    {
      public String getName()
      {
        return paramGoogleSource.getSourceName();
      }
      
      public void run()
      {
        paramGoogleSource.getSuggestions(paramQuery, Consumers.createAsyncConsumer(SuggestionsProvider.this.mPublishThread, paramConsumer));
      }
    };
  }
  
  private NamedTask createIcingQueryTask(final Query paramQuery, final IcingSources paramIcingSources, final Consumer<IcingSuggestionsFactory.IcingResults> paramConsumer)
  {
    Preconditions.checkNotNull(paramIcingSources);
    new NonCancellableNamedTask()
    {
      public String getName()
      {
        return "IcingSource";
      }
      
      public void run()
      {
        if (SuggestionsProvider.this.mShouldQueryStrategy.shouldMixIcingResults()) {}
        for (int i = SuggestionsProvider.this.mConfig.getIcingNumberRequestedResultsInMixedSuggest();; i = SuggestionsProvider.this.mConfig.getIcingNumberRequestedResultsInGroupedMode())
        {
          paramIcingSources.getSuggestions(paramQuery, i, SuggestionsProvider.this.mShouldQueryStrategy.shouldMixIcingResults(), Consumers.createAsyncConsumer(SuggestionsProvider.this.mPublishThread, paramConsumer));
          return;
        }
      }
    };
  }
  
  private NamedTask createZeroQueryAppSuggestionTask(final Query paramQuery, final Consumer<SuggestionList> paramConsumer)
  {
    new NonCancellableNamedTask()
    {
      public String getName()
      {
        return "ZeroQueryAppSuggestSource";
      }
      
      public void run()
      {
        SuggestionsProvider.this.getZeroPrefixApplicationSuggestions(paramQuery, Consumers.createAsyncConsumer(SuggestionsProvider.this.mPublishThread, paramConsumer));
      }
    };
  }
  
  private void fetchWebResults(Query paramQuery, GoogleSource paramGoogleSource, final Suggestions paramSuggestions)
  {
    SuggestionList localSuggestionList = paramGoogleSource.getCachedSuggestions(paramQuery);
    if (localSuggestionList != null) {
      paramSuggestions.addWebResult(localSuggestionList);
    }
    if (!this.mShouldQueryStrategy.shouldGetCachedWebOnly())
    {
      Object localObject = NO_OP_CONSUMER;
      if (localSuggestionList == null) {
        localObject = new Consumer()
        {
          public boolean consume(SuggestionList paramAnonymousSuggestionList)
          {
            paramSuggestions.addWebResult(paramAnonymousSuggestionList);
            return true;
          }
        };
      }
      this.mQueryExecutor.execute(createGoogleSourceQueryTask(paramQuery, paramGoogleSource, (Consumer)localObject));
    }
  }
  
  private List<ContentProviderSource> filterSource(Query paramQuery, List<ContentProviderSource> paramList)
  {
    Object localObject;
    if (paramList.size() == 0) {
      localObject = Collections.emptyList();
    }
    for (;;)
    {
      return localObject;
      if (paramList.size() == 1) {
        return Collections.singletonList(paramList.get(0));
      }
      localObject = new ArrayList(paramList.size());
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        ContentProviderSource localContentProviderSource = (ContentProviderSource)localIterator.next();
        if (this.mShouldQueryStrategy.shouldQuerySource(localContentProviderSource, paramQuery)) {
          ((List)localObject).add(localContentProviderSource);
        }
      }
    }
  }
  
  private void getZeroPrefixApplicationSuggestions(Query paramQuery, Consumer<SuggestionList> paramConsumer)
  {
    Cursor localCursor = this.mContext.getContentResolver().query(InternalIcingCorporaProvider.ZERO_PREFIX_APPLICATION_SUGGESTIONS_CONTENT_URI, null, "", null, null);
    ArrayList localArrayList = Lists.newArrayList();
    try
    {
      while (localCursor.moveToNext())
      {
        String str = localCursor.getString(1);
        localArrayList.add(SuggestionBuilder.builder().isApplication(true).packageName(localCursor.getString(3)).sourceCanonicalName("applications").text1(str).intentAction("android.intent.action.MAIN").intentData("content://applications/applications/" + localCursor.getString(3) + "/" + localCursor.getString(4)).icon1(Util.toResourceUriString(localCursor.getString(3), localCursor.getString(2))).isFromIcing(true).isHistory(true).build());
      }
      paramConsumer.consume(SuggestionListFactory.createSuggestionList("Icing", paramQuery, localArrayList, false));
    }
    finally
    {
      localCursor.close();
    }
  }
  
  private void maybeAddNowPromo(Suggestions paramSuggestions)
  {
    if ((paramSuggestions.getQuery().isEmptySuggestQuery()) && (this.mNowOptInSettings.canLoggedInAccountRunNow() == 1) && (!this.mNowOptInSettings.isUserOptedIn()) && (!this.mNowOptInSettings.userHasDismissedGetGoogleNowButton()) && (!paramSuggestions.isClosed()))
    {
      Suggestion[] arrayOfSuggestion = new Suggestion[1];
      arrayOfSuggestion[0] = SuggestionBuilder.builder().sourceCanonicalName("GoogleNow").isNowPromo(true).build();
      ArrayList localArrayList1 = Lists.newArrayList(arrayOfSuggestion);
      ArrayList localArrayList2 = Lists.newArrayList();
      localArrayList2.add(SuggestionListFactory.createSuggestionList("NowPromo", paramSuggestions.getQuery(), localArrayList1, false));
      paramSuggestions.addSummonsResults(localArrayList2);
    }
  }
  
  private void maybeSetSummonsDone(Suggestions paramSuggestions)
  {
    if ((this.mIcingQueryDone) && (this.mContentProvidersDone) && (this.mZeroQueryAppSuggestQueryDone)) {
      paramSuggestions.setSummonsDone();
    }
  }
  
  private void updateShouldQueryStrategy(SuggestionList paramSuggestionList)
  {
    if ((paramSuggestionList.getCount() == 0) && (paramSuggestionList.wasRequestMade())) {
      this.mShouldQueryStrategy.onZeroResults(paramSuggestionList.getSourceName(), paramSuggestionList.getUserQuery().getQueryStringForSuggest());
    }
  }
  
  public void cancelOngoingQuery()
  {
    if (this.mBatchingExecutor != null)
    {
      this.mBatchingExecutor.cancelPendingTasks();
      this.mBatchingExecutor.cancelRunningTasks();
      this.mBatchingExecutor = null;
    }
    this.mQueryExecutor.cancelPendingTasks();
  }
  
  public Suggestions getSuggestions(@Nonnull Query paramQuery, List<ContentProviderSource> paramList, GoogleSource paramGoogleSource, IcingSources paramIcingSources)
  {
    List localList = filterSource(paramQuery, paramList);
    boolean bool1;
    if (paramIcingSources == null)
    {
      bool1 = true;
      this.mIcingQueryDone = bool1;
      this.mContentProvidersDone = localList.isEmpty();
      this.mZeroQueryAppSuggestQueryDone = true;
      if ((this.mGsaConfigFlags.getZeroPrefixAppSuggestEnabled()) && (paramQuery.isEmptySuggestQuery())) {
        this.mZeroQueryAppSuggestQueryDone = false;
      }
      if ((this.mIcingQueryDone) && (this.mContentProvidersDone) && (this.mZeroQueryAppSuggestQueryDone)) {
        break label143;
      }
    }
    Suggestions localSuggestions;
    label143:
    for (boolean bool2 = true;; bool2 = false)
    {
      localSuggestions = new Suggestions(paramQuery, bool2, paramGoogleSource);
      if ((!localList.isEmpty()) || (paramGoogleSource != null) || (paramIcingSources != null) || (!this.mZeroQueryAppSuggestQueryDone)) {
        break label149;
      }
      maybeAddNowPromo(localSuggestions);
      localSuggestions.done();
      return localSuggestions;
      bool1 = false;
      break;
    }
    label149:
    this.mBatchingExecutor = new BatchingNamedTaskExecutor(this.mQueryExecutor);
    SuggestionListReceiver localSuggestionListReceiver = new SuggestionListReceiver(this.mBatchingExecutor, localSuggestions, this.mConfig.getPublishResultDelayMillis(), this.mShouldQueryStrategy.shouldQuerySingleContentProviderIfIcingEmpty(), localList.size());
    if (paramGoogleSource != null) {
      fetchWebResults(paramQuery, paramGoogleSource, localSuggestions);
    }
    if (!this.mZeroQueryAppSuggestQueryDone)
    {
      NamedTask localNamedTask2 = createZeroQueryAppSuggestionTask(paramQuery, localSuggestionListReceiver.getZeroQueryAppSuggestionResultConsumer());
      this.mQueryExecutor.execute(localNamedTask2);
    }
    if (!this.mIcingQueryDone)
    {
      NamedTask localNamedTask1 = createIcingQueryTask(paramQuery, paramIcingSources, localSuggestionListReceiver.getIcingResultConsumer());
      this.mQueryExecutor.execute(localNamedTask1);
    }
    if (!this.mContentProvidersDone)
    {
      int i = this.mConfig.getMaxResultsPerSource();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        ContentProviderSource localContentProviderSource = (ContentProviderSource)localIterator.next();
        this.mBatchingExecutor.execute(new SourceNamedTask(this.mContext, paramQuery, localContentProviderSource, i, localSuggestionListReceiver, this.mPublishThread));
      }
      if ((this.mShouldQueryStrategy.shouldQueryAllContentProviders()) || ((this.mShouldQueryStrategy.shouldQuerySingleContentProviderIfIcingEmpty()) && (paramIcingSources == null))) {
        localSuggestionListReceiver.startFirstBatch();
      }
    }
    maybeAddNowPromo(localSuggestions);
    maybeSetSummonsDone(localSuggestions);
    return localSuggestions;
  }
  
  private class SuggestionListReceiver
    implements Consumer<SuggestionList>
  {
    private final int mContentProvidersToQuery;
    private final BatchingNamedTaskExecutor mExecutor;
    private int mNumQueriesInProgress;
    private int mNumQueriesStarted;
    private final ArrayList<SuggestionList> mPendingResults;
    private final long mPublishResultsDelayMillis;
    private final Runnable mPublishResultsTask = new Runnable()
    {
      public void run()
      {
        SuggestionsProvider.SuggestionListReceiver.this.publishPendingResults();
      }
    };
    private final boolean mQuerySingleContentProviderIfIcingEmpty;
    private final Runnable mStartNewQueryTask = new Runnable()
    {
      public void run()
      {
        SuggestionsProvider.SuggestionListReceiver.this.executeNextBatch(1);
      }
    };
    private final Suggestions mSuggestions;
    
    public SuggestionListReceiver(BatchingNamedTaskExecutor paramBatchingNamedTaskExecutor, Suggestions paramSuggestions, long paramLong, boolean paramBoolean, int paramInt)
    {
      this.mExecutor = paramBatchingNamedTaskExecutor;
      this.mSuggestions = paramSuggestions;
      this.mPublishResultsDelayMillis = paramLong;
      this.mContentProvidersToQuery = paramInt;
      this.mQuerySingleContentProviderIfIcingEmpty = paramBoolean;
      this.mPendingResults = new ArrayList();
    }
    
    private void executeNextBatch(int paramInt)
    {
      if (!this.mSuggestions.isClosed())
      {
        int i = Math.min(paramInt, SuggestionsProvider.this.mConfig.getMaxConcurrentSourceQueries() - this.mNumQueriesInProgress);
        if (i > 0)
        {
          SuggestionsProvider.this.mPublishThread.cancelExecute(this.mStartNewQueryTask);
          int j = this.mExecutor.executeNextBatch(i);
          this.mNumQueriesInProgress = (j + this.mNumQueriesInProgress);
          this.mNumQueriesStarted = (j + this.mNumQueriesStarted);
          if ((!this.mQuerySingleContentProviderIfIcingEmpty) && (this.mNumQueriesStarted < this.mContentProvidersToQuery)) {
            SuggestionsProvider.this.mPublishThread.executeDelayed(this.mStartNewQueryTask, SuggestionsProvider.this.mConfig.getNewConcurrentSourceQueryDelay());
          }
        }
      }
    }
    
    private void handleNewSummonsAdded()
    {
      if ((this.mPublishResultsDelayMillis > 0L) && (!this.mSuggestions.isClosed()) && (shouldDelayPublish()))
      {
        SuggestionsProvider.this.mPublishThread.cancelExecute(this.mPublishResultsTask);
        SuggestionsProvider.this.mPublishThread.executeDelayed(this.mPublishResultsTask, this.mPublishResultsDelayMillis);
        return;
      }
      SuggestionsProvider.this.mPublishThread.cancelExecute(this.mPublishResultsTask);
      publishPendingResults();
    }
    
    private void publishPendingResults()
    {
      this.mSuggestions.addSummonsResults(this.mPendingResults);
      this.mPendingResults.clear();
      SuggestionsProvider.this.maybeSetSummonsDone(this.mSuggestions);
    }
    
    private boolean shouldDelayPublish()
    {
      return (!SuggestionsProvider.this.mIcingQueryDone) || (!SuggestionsProvider.this.mContentProvidersDone) || (!SuggestionsProvider.this.mZeroQueryAppSuggestQueryDone);
    }
    
    public boolean consume(SuggestionList paramSuggestionList)
    {
      this.mNumQueriesInProgress = (-1 + this.mNumQueriesInProgress);
      int i = this.mNumQueriesStarted - this.mNumQueriesInProgress;
      if ((!this.mQuerySingleContentProviderIfIcingEmpty) || (paramSuggestionList == null) || (paramSuggestionList.getCount() == 0)) {
        executeNextBatch(1);
      }
      if (paramSuggestionList == null)
      {
        Log.w("Search.SuggestionsProvider", "Source returned a null list.");
        return false;
      }
      SuggestionsProvider.this.updateShouldQueryStrategy(paramSuggestionList);
      this.mPendingResults.add(paramSuggestionList);
      if (i >= this.mContentProvidersToQuery) {
        SuggestionsProvider.access$702(SuggestionsProvider.this, true);
      }
      handleNewSummonsAdded();
      return true;
    }
    
    public Consumer<IcingSuggestionsFactory.IcingResults> getIcingResultConsumer()
    {
      new Consumer()
      {
        public boolean consume(IcingSuggestionsFactory.IcingResults paramAnonymousIcingResults)
        {
          int i = paramAnonymousIcingResults.totalNumResults;
          List localList = paramAnonymousIcingResults.suggestionLists;
          SuggestionsProvider.access$802(SuggestionsProvider.this, true);
          if (i > 0)
          {
            SuggestionsProvider.access$776(SuggestionsProvider.this, SuggestionsProvider.SuggestionListReceiver.this.mQuerySingleContentProviderIfIcingEmpty);
            SuggestionsProvider.SuggestionListReceiver.this.mPendingResults.addAll(localList);
            SuggestionsProvider.SuggestionListReceiver.this.handleNewSummonsAdded();
          }
          for (;;)
          {
            SuggestionsProvider.this.maybeSetSummonsDone(SuggestionsProvider.SuggestionListReceiver.this.mSuggestions);
            return true;
            if ((!SuggestionsProvider.this.mContentProvidersDone) && (SuggestionsProvider.SuggestionListReceiver.this.mQuerySingleContentProviderIfIcingEmpty)) {
              SuggestionsProvider.SuggestionListReceiver.this.startFirstBatch();
            }
          }
        }
      };
    }
    
    public Consumer<SuggestionList> getZeroQueryAppSuggestionResultConsumer()
    {
      new Consumer()
      {
        public boolean consume(SuggestionList paramAnonymousSuggestionList)
        {
          SuggestionsProvider.access$902(SuggestionsProvider.this, true);
          if (paramAnonymousSuggestionList.getCount() > 0)
          {
            SuggestionsProvider.SuggestionListReceiver.this.mPendingResults.add(paramAnonymousSuggestionList);
            SuggestionsProvider.SuggestionListReceiver.this.handleNewSummonsAdded();
          }
          SuggestionsProvider.this.maybeSetSummonsDone(SuggestionsProvider.SuggestionListReceiver.this.mSuggestions);
          return true;
        }
      };
    }
    
    public void startFirstBatch()
    {
      if (this.mQuerySingleContentProviderIfIcingEmpty) {}
      for (int i = 1;; i = SuggestionsProvider.this.mConfig.getConcurrentSourceQueries())
      {
        executeNextBatch(i);
        return;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionsProvider
 * JD-Core Version:    0.7.0.1
 */