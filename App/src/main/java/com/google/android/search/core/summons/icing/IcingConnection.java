package com.google.android.search.core.summons.icing;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.google.android.gms.appdatasearch.AppDataSearchClient;
import com.google.android.gms.appdatasearch.CorpusId;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.gms.appdatasearch.GlobalSearchQuerySpecification;
import com.google.android.gms.appdatasearch.GlobalSearchQuerySpecification.Builder;
import com.google.android.gms.appdatasearch.PhraseAffinityResponse;
import com.google.android.gms.appdatasearch.PhraseAffinitySpecification;
import com.google.android.gms.appdatasearch.QuerySpecification;
import com.google.android.gms.appdatasearch.SearchResults;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.shared.util.CancellableSingleThreadedExecutor;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.HandlerExecutor;
import com.google.android.voicesearch.logger.EventLogger;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class IcingConnection
  implements ConnectionToIcing
{
  private static final long CONNECTION_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(10L);
  private final CancellableSingleThreadedExecutor mBackgroundExecutor;
  private final SearchConfig mConfig;
  private final AvailabilityAwareInternalConnection mInternalConnection;
  private final Runnable mMaybeConnectRunnable;
  private final Runnable mMaybeDisconnectRunnable;
  private final AtomicInteger mPendingConnections;
  private final AtomicBoolean mWaitingForQueries;
  
  IcingConnection(AppDataSearchClient paramAppDataSearchClient, SearchConfig paramSearchConfig, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this(new AvailabilityAwareInternalConnection(new InternalConnection(new SafeAppDataSearchClient(paramAppDataSearchClient, paramBoolean2), paramBoolean3), paramBoolean1), paramSearchConfig, createExecutor());
  }
  
  IcingConnection(AvailabilityAwareInternalConnection paramAvailabilityAwareInternalConnection, SearchConfig paramSearchConfig, CancellableSingleThreadedExecutor paramCancellableSingleThreadedExecutor)
  {
    this.mBackgroundExecutor = paramCancellableSingleThreadedExecutor;
    this.mInternalConnection = paramAvailabilityAwareInternalConnection;
    this.mConfig = paramSearchConfig;
    this.mPendingConnections = new AtomicInteger();
    this.mWaitingForQueries = new AtomicBoolean();
    this.mMaybeConnectRunnable = new Runnable()
    {
      public void run()
      {
        IcingConnection.this.mInternalConnection.connect();
        IcingConnection.this.mPendingConnections.decrementAndGet();
      }
    };
    this.mMaybeDisconnectRunnable = new Runnable()
    {
      public void run()
      {
        if ((IcingConnection.this.mPendingConnections.get() <= 0) && (!IcingConnection.this.mWaitingForQueries.get())) {
          IcingConnection.this.mInternalConnection.disconnect();
        }
      }
    };
  }
  
  private static CancellableSingleThreadedExecutor createExecutor()
  {
    HandlerThread localHandlerThread = new HandlerThread("IcingConnectionThread", 0);
    localHandlerThread.start();
    return new HandlerExecutor(new Handler(localHandlerThread.getLooper()));
  }
  
  private void postConnectionTask()
  {
    this.mPendingConnections.incrementAndGet();
    this.mBackgroundExecutor.execute(this.mMaybeConnectRunnable);
  }
  
  public PhraseAffinityResponse blockingGetPhraseAffinity(final String[] paramArrayOfString, final PhraseAffinitySpecification paramPhraseAffinitySpecification)
  {
    final ConditionVariable localConditionVariable = new ConditionVariable();
    final PhraseAffinityResponse[] arrayOfPhraseAffinityResponse = new PhraseAffinityResponse[1];
    postConnectionTask();
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        arrayOfPhraseAffinityResponse[0] = IcingConnection.this.mInternalConnection.getPhraseAffinity(paramArrayOfString, paramPhraseAffinitySpecification);
        localConditionVariable.open();
      }
    });
    this.mBackgroundExecutor.execute(this.mMaybeDisconnectRunnable);
    localConditionVariable.block();
    return arrayOfPhraseAffinityResponse[0];
  }
  
  public SearchResults blockingQuery(final String paramString, final String[] paramArrayOfString, final QuerySpecification paramQuerySpecification)
  {
    final ConditionVariable localConditionVariable = new ConditionVariable();
    final SearchResults[] arrayOfSearchResults = new SearchResults[1];
    postConnectionTask();
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        arrayOfSearchResults[0] = IcingConnection.this.mInternalConnection.query(paramString, paramArrayOfString, paramQuerySpecification);
        localConditionVariable.open();
      }
    });
    this.mBackgroundExecutor.execute(this.mMaybeDisconnectRunnable);
    localConditionVariable.block();
    return arrayOfSearchResults[0];
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = paramString;
    arrayOfObject1[1] = (getClass().getSimpleName() + " state:");
    DumpUtils.println(paramPrintWriter, arrayOfObject1);
    String str = paramString + "  ";
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = str;
    arrayOfObject2[1] = ("mPendingConnection=" + this.mPendingConnections.get());
    DumpUtils.println(paramPrintWriter, arrayOfObject2);
    Object[] arrayOfObject3 = new Object[2];
    arrayOfObject3[0] = str;
    arrayOfObject3[1] = ("mWaitingForQueries=" + this.mWaitingForQueries.get());
    DumpUtils.println(paramPrintWriter, arrayOfObject3);
    Object[] arrayOfObject4 = new Object[2];
    arrayOfObject4[0] = str;
    arrayOfObject4[1] = ("isConnected=" + this.mInternalConnection.isConnected());
    DumpUtils.println(paramPrintWriter, arrayOfObject4);
  }
  
  void getGlobalSearchRegisteredApplications(final Consumer<GlobalSearchApplicationInfo[]> paramConsumer)
  {
    postConnectionTask();
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        paramConsumer.consume(IcingConnection.this.mInternalConnection.getGlobalSearchRegisteredApplications());
      }
    });
    this.mBackgroundExecutor.execute(this.mMaybeDisconnectRunnable);
  }
  
  void queryGlobalSearch(final String paramString, final int paramInt, final Collection<IcingSource> paramCollection, final Consumer<SearchResults> paramConsumer)
  {
    final boolean bool1 = this.mWaitingForQueries.get();
    if ((paramCollection == null) || (paramCollection.isEmpty())) {}
    for (final boolean bool2 = true;; bool2 = false)
    {
      this.mBackgroundExecutor.execute(new Runnable()
      {
        public void run()
        {
          if ((bool1) && (!bool2))
          {
            GlobalSearchQuerySpecification.Builder localBuilder = new GlobalSearchQuerySpecification.Builder();
            Iterator localIterator = paramCollection.iterator();
            while (localIterator.hasNext())
            {
              IcingSource localIcingSource = (IcingSource)localIterator.next();
              String str2 = localIcingSource.getInternalCorpusName();
              int i = IcingConnection.this.mConfig.getIcingQueryWeightForCanonicalName(localIcingSource.getCanonicalName());
              if (str2 != null)
              {
                CorpusId localCorpusId = new CorpusId(localIcingSource.getPackageName(), str2);
                localBuilder.addCorpus(localCorpusId);
                localBuilder.setCorpusWeight(localCorpusId, i);
              }
              else
              {
                localBuilder.addPackage(localIcingSource.getPackageName());
                localBuilder.setPackageWeight(localIcingSource.getPackageName(), i);
              }
            }
            if (Feature.EXTENSIVE_ICING_LOGGING.isEnabled()) {
              localBuilder.setScoringVerbosityLevel(1);
            }
            GlobalSearchQuerySpecification localGlobalSearchQuerySpecification = localBuilder.build();
            SearchResults localSearchResults = IcingConnection.this.mInternalConnection.queryGlobalSearch(paramString, paramInt, localGlobalSearchQuerySpecification);
            paramConsumer.consume(localSearchResults);
            return;
          }
          if (!bool1) {}
          for (String str1 = "queryGlobalSearch when not waiting for queries.";; str1 = "queryGlobalSearch with no enabled sources")
          {
            Log.w("Search.IcingConnection", str1);
            paramConsumer.consume(null);
            return;
          }
        }
      });
      return;
    }
  }
  
  public void setServiceAvailable(final boolean paramBoolean)
  {
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        IcingConnection.this.mInternalConnection.setServiceAvailable(paramBoolean);
      }
    });
  }
  
  public void startWaitingForQueries()
  {
    this.mWaitingForQueries.set(true);
    postConnectionTask();
  }
  
  public void stopWaitingForQueries()
  {
    this.mWaitingForQueries.set(false);
    this.mBackgroundExecutor.execute(this.mMaybeDisconnectRunnable);
  }
  
  static class AvailabilityAwareInternalConnection
  {
    private boolean mConnectionWanted;
    private final IcingConnection.InternalConnection mSafeInternalConnection;
    private boolean mServiceAvailable;
    
    AvailabilityAwareInternalConnection(IcingConnection.InternalConnection paramInternalConnection, boolean paramBoolean)
    {
      this.mSafeInternalConnection = paramInternalConnection;
      this.mServiceAvailable = paramBoolean;
    }
    
    public void connect()
    {
      this.mConnectionWanted = true;
      if (this.mServiceAvailable) {
        this.mSafeInternalConnection.blockingConnect();
      }
    }
    
    public void disconnect()
    {
      this.mConnectionWanted = false;
      if (this.mServiceAvailable) {
        this.mSafeInternalConnection.disconnect();
      }
    }
    
    public GlobalSearchApplicationInfo[] getGlobalSearchRegisteredApplications()
    {
      if (this.mServiceAvailable) {
        return this.mSafeInternalConnection.getGlobalSearchRegisteredApplications();
      }
      return null;
    }
    
    public PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
    {
      if (this.mServiceAvailable) {
        return this.mSafeInternalConnection.getPhraseAffinity(paramArrayOfString, paramPhraseAffinitySpecification);
      }
      return null;
    }
    
    public boolean isConnected()
    {
      return this.mSafeInternalConnection.isConnected();
    }
    
    public SearchResults query(String paramString, String[] paramArrayOfString, QuerySpecification paramQuerySpecification)
    {
      if (this.mServiceAvailable) {
        return this.mSafeInternalConnection.query(paramString, paramArrayOfString, paramQuerySpecification);
      }
      return null;
    }
    
    public SearchResults queryGlobalSearch(String paramString, int paramInt, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
    {
      if (this.mServiceAvailable) {
        return this.mSafeInternalConnection.queryGlobalSearch(paramString, paramInt, paramGlobalSearchQuerySpecification);
      }
      return null;
    }
    
    public void setServiceAvailable(boolean paramBoolean)
    {
      if (this.mServiceAvailable != paramBoolean)
      {
        this.mServiceAvailable = paramBoolean;
        if ((paramBoolean) && (this.mConnectionWanted)) {
          this.mSafeInternalConnection.blockingConnect();
        }
      }
    }
  }
  
  static class InternalConnection
  {
    private final IcingConnection.SafeAppDataSearchClient mClient;
    private final boolean mLogLatency;
    
    public InternalConnection(IcingConnection.SafeAppDataSearchClient paramSafeAppDataSearchClient, boolean paramBoolean)
    {
      this.mClient = paramSafeAppDataSearchClient;
      this.mLogLatency = paramBoolean;
    }
    
    public void blockingConnect()
    {
      ConnectionResult localConnectionResult;
      StringBuilder localStringBuilder;
      if (!this.mClient.isConnected())
      {
        localConnectionResult = this.mClient.connectWithTimeout(IcingConnection.CONNECTION_TIMEOUT_MILLIS);
        if ((localConnectionResult == null) || (!localConnectionResult.isSuccess()))
        {
          localStringBuilder = new StringBuilder().append("Could not connect to Icing. Error code ");
          if (localConnectionResult != null) {
            break label67;
          }
        }
      }
      label67:
      for (String str = "Unknown.";; str = localConnectionResult.getErrorCode() + ".")
      {
        Log.e("Search.IcingConnection", str);
        return;
      }
    }
    
    public void disconnect()
    {
      if (this.mClient.isConnected()) {
        this.mClient.disconnect();
      }
    }
    
    public GlobalSearchApplicationInfo[] getGlobalSearchRegisteredApplications()
    {
      if (!this.mClient.isConnected())
      {
        Log.w("Search.IcingConnection", "getGlobalSearchRegisteredApplications when not connected.");
        return null;
      }
      return this.mClient.getGlobalSearchRegisteredApplications();
    }
    
    public PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
    {
      PhraseAffinityResponse localPhraseAffinityResponse;
      if (!this.mClient.isConnected())
      {
        Log.w("Search.IcingConnection", "query when not connected.");
        localPhraseAffinityResponse = null;
      }
      do
      {
        return localPhraseAffinityResponse;
        localPhraseAffinityResponse = this.mClient.getPhraseAffinity(paramArrayOfString, paramPhraseAffinitySpecification);
        if (localPhraseAffinityResponse == null)
        {
          Log.e("Search.IcingConnection", "Got null results from query.");
          return null;
        }
      } while (!localPhraseAffinityResponse.hasError());
      Log.e("Search.IcingConnection", "Got error for search: " + localPhraseAffinityResponse.getErrorMessage());
      return null;
    }
    
    public boolean isConnected()
    {
      return this.mClient.isConnected();
    }
    
    public SearchResults query(String paramString, String[] paramArrayOfString, QuerySpecification paramQuerySpecification)
    {
      SearchResults localSearchResults;
      if (!this.mClient.isConnected())
      {
        Log.w("Search.IcingConnection", "query when not connected.");
        localSearchResults = null;
      }
      do
      {
        return localSearchResults;
        localSearchResults = this.mClient.query(paramString, paramArrayOfString, 0, 10, paramQuerySpecification);
        if (localSearchResults == null)
        {
          Log.e("Search.IcingConnection", "Got null results from query.");
          return null;
        }
      } while (!localSearchResults.hasError());
      Log.e("Search.IcingConnection", "Got error for search: " + localSearchResults.getErrorMessage());
      return null;
    }
    
    public SearchResults queryGlobalSearch(String paramString, int paramInt, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
    {
      SearchResults localSearchResults;
      if (!this.mClient.isConnected())
      {
        Log.w("Search.IcingConnection", "queryGlobalSearch when not connected.");
        localSearchResults = null;
      }
      do
      {
        return localSearchResults;
        if (this.mLogLatency) {
          EventLogger.recordLatencyStart(12);
        }
        localSearchResults = this.mClient.queryGlobalSearch(paramString, 0, paramInt, paramGlobalSearchQuerySpecification);
        if (this.mLogLatency) {
          EventLogger.recordClientEvent(131);
        }
        if (localSearchResults == null)
        {
          Log.e("Search.IcingConnection", "Got null results from queryGlobalSearch.");
          return null;
        }
      } while (!localSearchResults.hasError());
      Log.e("Search.IcingConnection", "Got error for search: " + localSearchResults.getErrorMessage());
      return null;
    }
  }
  
  static class SafeAppDataSearchClient
  {
    private boolean mSafeBinderCalls;
    private final AppDataSearchClient mSearchClient;
    
    public SafeAppDataSearchClient(AppDataSearchClient paramAppDataSearchClient, boolean paramBoolean)
    {
      this.mSearchClient = paramAppDataSearchClient;
      this.mSafeBinderCalls = paramBoolean;
    }
    
    public ConnectionResult connectWithTimeout(long paramLong)
    {
      return this.mSearchClient.connectWithTimeout(paramLong);
    }
    
    public void disconnect()
    {
      this.mSearchClient.disconnect();
    }
    
    public GlobalSearchApplicationInfo[] getGlobalSearchRegisteredApplications()
    {
      try
      {
        GlobalSearchApplicationInfo[] arrayOfGlobalSearchApplicationInfo = this.mSearchClient.getGlobalSearchRegisteredApplications();
        return arrayOfGlobalSearchApplicationInfo;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Search.IcingConnection", "Exception when calling getGlobalSearchRegisteredApplications", localRuntimeException);
        if (this.mSafeBinderCalls) {
          return null;
        }
        throw localRuntimeException;
      }
    }
    
    public PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
    {
      try
      {
        PhraseAffinityResponse localPhraseAffinityResponse = this.mSearchClient.getPhraseAffinity(paramArrayOfString, paramPhraseAffinitySpecification);
        return localPhraseAffinityResponse;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Search.IcingConnection", "Exception when calling getPhraseAffinity", localRuntimeException);
        if (this.mSafeBinderCalls) {
          return null;
        }
        throw localRuntimeException;
      }
    }
    
    public boolean isConnected()
    {
      return this.mSearchClient.isConnected();
    }
    
    public SearchResults query(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, QuerySpecification paramQuerySpecification)
    {
      try
      {
        SearchResults localSearchResults = this.mSearchClient.query(paramString, paramArrayOfString, paramInt1, paramInt2, paramQuerySpecification);
        return localSearchResults;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Search.IcingConnection", "Exception when calling query", localRuntimeException);
        if (this.mSafeBinderCalls) {
          return null;
        }
        throw localRuntimeException;
      }
    }
    
    public SearchResults queryGlobalSearch(String paramString, int paramInt1, int paramInt2, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
    {
      try
      {
        SearchResults localSearchResults = this.mSearchClient.queryGlobalSearch(paramString, paramInt1, paramInt2, paramGlobalSearchQuerySpecification);
        return localSearchResults;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Search.IcingConnection", "Exception when calling queryGlobalSearch", localRuntimeException);
        if (this.mSafeBinderCalls) {
          return null;
        }
        throw localRuntimeException;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingConnection
 * JD-Core Version:    0.7.0.1
 */