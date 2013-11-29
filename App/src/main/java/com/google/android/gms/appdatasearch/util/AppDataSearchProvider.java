package com.google.android.gms.appdatasearch.util;

import android.content.ContentProvider;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.CancellationSignal;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.appdatasearch.AppDataSearchClient;
import com.google.android.gms.appdatasearch.CorpusStatus;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.gms.appdatasearch.GlobalSearchCorpusConfig;
import com.google.android.gms.appdatasearch.RegisterCorpusInfo;
import com.google.android.gms.appdatasearch.RegisterSectionInfo;
import com.google.android.gms.appdatasearch.SyncContentProviderHelper.SyncQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class AppDataSearchProvider
  extends ContentProvider
  implements AppDataSearchDataManager.TableChangeListener
{
  private AppDataSearchDataManager mDataManager;
  private final Object mDataManagerLock = new Object();
  private AppDataSearchClient mSearchClient;
  private final UriMatcher mUriMatcher = new UriMatcher(-1);
  
  private RegisterCorpusInfo createCorpusInfo(TableStorageSpec paramTableStorageSpec)
  {
    Uri localUri = new Uri.Builder().scheme("content").authority(getContentProviderAuthority()).appendPath("appdatasearch").appendPath(paramTableStorageSpec.getCorpusName()).build();
    List localList = paramTableStorageSpec.getSections();
    int[] arrayOfInt = paramTableStorageSpec.getGlobalSearchSectionTemplates();
    if (arrayOfInt != null) {}
    for (GlobalSearchCorpusConfig localGlobalSearchCorpusConfig = new GlobalSearchCorpusConfig(arrayOfInt);; localGlobalSearchCorpusConfig = null) {
      return new RegisterCorpusInfo(paramTableStorageSpec.getCorpusName(), paramTableStorageSpec.getVersion(), localUri, (RegisterSectionInfo[])localList.toArray(new RegisterSectionInfo[localList.size()]), localGlobalSearchCorpusConfig, paramTableStorageSpec.getTrimmable());
    }
  }
  
  private final <T> T executeWithConnection(Callable<T> paramCallable, String paramString, T paramT)
  {
    if (getContext() == null) {
      throw new IllegalStateException(paramString + " can't be called before onCreate");
    }
    if (getContext().getMainLooper().getThread() == Thread.currentThread()) {
      throw new IllegalStateException(paramString + " can't be called on main thread");
    }
    Object localObject3;
    synchronized (this.mSearchClient)
    {
      ConnectionResult localConnectionResult = this.mSearchClient.connectWithTimeout(30000L);
      if (!localConnectionResult.isSuccess())
      {
        Log.e(".AppDataSearchProvider", "Could not connect to AppDataSearchClient for " + paramString + ", error " + localConnectionResult.getErrorCode());
        return paramT;
      }
    }
  }
  
  private static final String getUriPathFor(String paramString)
  {
    return "appdatasearch/" + Uri.encode(paramString);
  }
  
  private void registerCorporaAsync()
  {
    new AsyncTask()
    {
      protected Integer doInBackground(Void... paramAnonymousVarArgs)
      {
        return Integer.valueOf(AppDataSearchProvider.this.registerCorpora(AppDataSearchProvider.this.getDataManager().getTableStorageSpecs()));
      }
      
      protected void onPostExecute(Integer paramAnonymousInteger)
      {
        if (paramAnonymousInteger.intValue() != 0) {
          AppDataSearchProvider.this.onError(paramAnonymousInteger.intValue());
        }
      }
    }.execute(new Void[0]);
  }
  
  private boolean requestIndexingIfRequired(TableStorageSpec paramTableStorageSpec)
  {
    CorpusStatus localCorpusStatus = this.mSearchClient.getCorpusStatus(paramTableStorageSpec.getCorpusName());
    if ((localCorpusStatus == null) || (!localCorpusStatus.found()))
    {
      if (localCorpusStatus == null) {}
      for (String str = "Couldn't fetch status for";; str = "Couldn't find")
      {
        Log.e(".AppDataSearchProvider", str + " corpus '" + paramTableStorageSpec.getCorpusName() + "'");
        return false;
      }
    }
    AppDataSearchDataManager localAppDataSearchDataManager = getDataManager();
    localAppDataSearchDataManager.cleanSequenceTable(paramTableStorageSpec, localCorpusStatus.getLastCommittedSeqno());
    long l = localAppDataSearchDataManager.getMaxSeqno(paramTableStorageSpec);
    if (l > localCorpusStatus.getLastIndexedSeqno()) {
      return this.mSearchClient.requestIndexing(paramTableStorageSpec.getCorpusName(), l);
    }
    return true;
  }
  
  protected abstract AppDataSearchDataManager createDataManager(AppDataSearchDataManager.TableChangeListener paramTableChangeListener);
  
  protected final int diagnoseTable(final TableStorageSpec paramTableStorageSpec)
  {
    if (!Arrays.asList(getDataManager().getTableStorageSpecs()).contains(paramTableStorageSpec)) {
      throw new IllegalArgumentException("The table " + paramTableStorageSpec.getCorpusName() + " does not have a registered TableStorageSpec.");
    }
    ((Integer)executeWithConnection(new Callable()
    {
      public Integer call()
      {
        CorpusStatus localCorpusStatus = AppDataSearchProvider.this.mSearchClient.getCorpusStatus(paramTableStorageSpec.getCorpusName());
        if (localCorpusStatus == null)
        {
          Log.e(".AppDataSearchProvider", "Couldn't fetch status for corpus " + paramTableStorageSpec.getCorpusName());
          return Integer.valueOf(4);
        }
        if (!localCorpusStatus.found()) {
          return Integer.valueOf(3);
        }
        long l1 = AppDataSearchProvider.this.getDataManager().getMaxSeqno(paramTableStorageSpec);
        long l2 = localCorpusStatus.getLastIndexedSeqno();
        if (l1 == 0L) {
          return Integer.valueOf(0);
        }
        if (l1 < l2)
        {
          Log.e(".AppDataSearchProvider", "Local highest seqno=" + l1 + " less than " + "lastIndexedSeqno=" + l2);
          return Integer.valueOf(4);
        }
        if (l1 == l2) {
          return Integer.valueOf(0);
        }
        if (l2 == 0L) {}
        for (int i = 2;; i = 1) {
          return Integer.valueOf(i);
        }
      }
    }, "diagnoseTable", Integer.valueOf(4))).intValue();
  }
  
  protected abstract String doGetType(Uri paramUri);
  
  protected abstract boolean doOnCreate();
  
  protected abstract Cursor doQuery(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2);
  
  protected Cursor doQuery(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    return doQuery(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
  }
  
  protected abstract String getContentProviderAuthority();
  
  protected String[] getCorpusNames()
  {
    TableStorageSpec[] arrayOfTableStorageSpec = getDataManager().getTableStorageSpecs();
    String[] arrayOfString = new String[arrayOfTableStorageSpec.length];
    for (int i = 0; i < arrayOfTableStorageSpec.length; i++) {
      arrayOfString[i] = arrayOfTableStorageSpec[i].getCorpusName();
    }
    return arrayOfString;
  }
  
  protected final AppDataSearchDataManager getDataManager()
  {
    synchronized (this.mDataManagerLock)
    {
      if (this.mDataManager == null) {
        this.mDataManager = createDataManager(this);
      }
      return this.mDataManager;
    }
  }
  
  protected abstract GlobalSearchApplicationInfo getGlobalSearchableAppInfo();
  
  public final String getType(Uri paramUri)
  {
    if (this.mUriMatcher.match(paramUri) == -1) {
      return doGetType(paramUri);
    }
    AppDataSearchClient.verifyContentProviderClient(getContext());
    return "vnd.android.cursor.dir/vnd.goodle.appdatasearch";
  }
  
  public final boolean onCreate()
  {
    this.mSearchClient = new AppDataSearchClient(getContext());
    boolean bool = doOnCreate();
    String str = getContentProviderAuthority();
    String[] arrayOfString = getCorpusNames();
    for (int i = 0; i < arrayOfString.length; i++) {
      this.mUriMatcher.addURI(str, getUriPathFor(arrayOfString[i]), i);
    }
    int j;
    if (shouldRegisterCorporaOnCreate())
    {
      j = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
      if (j == 0) {
        registerCorporaAsync();
      }
    }
    else
    {
      return bool;
    }
    onError(j);
    return bool;
  }
  
  protected void onError(int paramInt) {}
  
  public final boolean onTableChanged(final TableStorageSpec paramTableStorageSpec)
  {
    if (!Arrays.asList(getDataManager().getTableStorageSpecs()).contains(paramTableStorageSpec)) {
      throw new IllegalArgumentException("The table " + paramTableStorageSpec.getCorpusName() + " does not have a registered TableStorageSpec.");
    }
    ((Boolean)executeWithConnection(new Callable()
    {
      public Boolean call()
      {
        return Boolean.valueOf(AppDataSearchProvider.this.requestIndexingIfRequired(paramTableStorageSpec));
      }
    }, "onTableChanged", Boolean.valueOf(false))).booleanValue();
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, null);
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    int i = this.mUriMatcher.match(paramUri);
    if (i == -1) {
      return doQuery(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, paramCancellationSignal);
    }
    AppDataSearchClient.verifyContentProviderClient(getContext());
    SyncContentProviderHelper.SyncQuery localSyncQuery = SyncContentProviderHelper.SyncQuery.parse(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    AppDataSearchDataManager localAppDataSearchDataManager = getDataManager();
    TableStorageSpec localTableStorageSpec = localAppDataSearchDataManager.getTableStorageSpecs()[i];
    if (localSyncQuery.wantsFullSync()) {
      localAppDataSearchDataManager.recreateSequenceTable(localTableStorageSpec);
    }
    if (localSyncQuery.isValidDocumentsQuery()) {
      return localAppDataSearchDataManager.querySequenceTable(localTableStorageSpec, localSyncQuery.getLastSeqNo(), localSyncQuery.getLimit());
    }
    if (localSyncQuery.isValidTagsQuery()) {
      return localAppDataSearchDataManager.queryTagsTable(localTableStorageSpec, localSyncQuery.getLastSeqNo(), localSyncQuery.getLimit());
    }
    return null;
  }
  
  protected final int registerCorpora(TableStorageSpec[] paramArrayOfTableStorageSpec)
  {
    if (paramArrayOfTableStorageSpec == null) {}
    for (final List localList = Collections.emptyList(); !Arrays.asList(getDataManager().getTableStorageSpecs()).containsAll(localList); localList = Arrays.asList(paramArrayOfTableStorageSpec)) {
      throw new IllegalArgumentException("Not all tables in " + localList + " were registered " + "when the AppDataSearchProvider was created.");
    }
    final HashSet localHashSet = new HashSet();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext()) {
      localHashSet.add(createCorpusInfo((TableStorageSpec)localIterator.next()));
    }
    ((Integer)executeWithConnection(new Callable()
    {
      public Integer call()
      {
        boolean bool = AppDataSearchProvider.this.mSearchClient.setRegisteredCorpora(localHashSet);
        if (this.val$appInfo != null) {
          bool &= AppDataSearchProvider.this.mSearchClient.registerGlobalSearchApplication(this.val$appInfo);
        }
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          TableStorageSpec localTableStorageSpec = (TableStorageSpec)localIterator.next();
          AppDataSearchProvider.this.requestIndexingIfRequired(localTableStorageSpec);
        }
        if (bool) {}
        for (int i = 0;; i = 8) {
          return Integer.valueOf(i);
        }
      }
    }, "registerCorpora", Integer.valueOf(8))).intValue();
  }
  
  protected boolean shouldRegisterCorporaOnCreate()
  {
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.AppDataSearchProvider
 * JD-Core Version:    0.7.0.1
 */