package com.google.android.search.core.summons;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Process;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.SuggestionFilterProvider;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.NamedTaskExecutor;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchableSources
  implements Sources<ContentProviderSource>
{
  private final NamedTaskExecutor mBackgroundExecutor;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Context mContext;
  private final DataSetObservable mObservable;
  private final AtomicBoolean mReceiverRegistered;
  private final SearchManager mSearchManager;
  private ImmutableList<ContentProviderSource> mSourceList;
  private final SourceNameHelper mSourceNameHelper;
  private final Map<String, ContentProviderSource> mSources;
  private final SuggestionFilterProvider mSuggestionFilterProvider;
  private final Executor mUiThreadExecutor;
  
  public SearchableSources(Context paramContext, SearchConfig paramSearchConfig, Clock paramClock, SuggestionFilterProvider paramSuggestionFilterProvider, NamedTaskExecutor paramNamedTaskExecutor, Executor paramExecutor, SourceNameHelper paramSourceNameHelper)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
    this.mClock = paramClock;
    this.mSuggestionFilterProvider = paramSuggestionFilterProvider;
    this.mBackgroundExecutor = paramNamedTaskExecutor;
    this.mUiThreadExecutor = paramExecutor;
    this.mSourceNameHelper = paramSourceNameHelper;
    this.mSearchManager = ((SearchManager)paramContext.getSystemService("search"));
    this.mSources = Maps.newHashMap();
    this.mSourceList = ImmutableList.of();
    this.mObservable = new DataSetObservable();
    this.mReceiverRegistered = new AtomicBoolean();
  }
  
  SearchableSources(List<ContentProviderSource> paramList)
  {
    this.mContext = null;
    this.mConfig = null;
    this.mClock = null;
    this.mSearchManager = null;
    this.mSuggestionFilterProvider = null;
    this.mSources = Maps.newHashMap();
    this.mObservable = new DataSetObservable();
    this.mBackgroundExecutor = null;
    this.mUiThreadExecutor = null;
    this.mSourceNameHelper = null;
    this.mReceiverRegistered = new AtomicBoolean();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ContentProviderSource localContentProviderSource = (ContentProviderSource)localIterator.next();
      this.mSources.put(localContentProviderSource.getName(), localContentProviderSource);
    }
    this.mSourceList = ImmutableList.copyOf(this.mSources.values());
  }
  
  private boolean canRead(Uri paramUri, String paramString)
  {
    ProviderInfo localProviderInfo = this.mContext.getPackageManager().resolveContentProvider(paramUri.getAuthority(), 0);
    if (localProviderInfo == null)
    {
      Log.w("Search.SearchableSources", paramString + " has bad suggestion authority " + paramUri.getAuthority());
      return false;
    }
    String str1 = localProviderInfo.readPermission;
    if (str1 == null) {
      return true;
    }
    int i = Process.myPid();
    int j = Process.myUid();
    if (this.mContext.checkPermission(str1, i, j) == 0) {
      return true;
    }
    PathPermission[] arrayOfPathPermission = localProviderInfo.pathPermissions;
    if ((arrayOfPathPermission == null) || (arrayOfPathPermission.length == 0)) {
      return false;
    }
    String str2 = paramUri.getPath();
    int k = arrayOfPathPermission.length;
    for (int m = 0; m < k; m++)
    {
      PathPermission localPathPermission = arrayOfPathPermission[m];
      String str3 = localPathPermission.getReadPermission();
      if ((str3 != null) && (localPathPermission.match(str2)) && (this.mContext.checkPermission(str3, i, j) == 0)) {
        return true;
      }
    }
    return false;
  }
  
  private ContentProviderSource createSearchableSource(SearchableInfo paramSearchableInfo)
  {
    if (paramSearchableInfo == null) {
      return null;
    }
    try
    {
      ComponentName localComponentName = paramSearchableInfo.getSearchActivity();
      String str1 = this.mSourceNameHelper.getSourceNameForSearchableSource(localComponentName);
      Uri localUri = getSuggestUri(paramSearchableInfo);
      if ((localUri != null) && (!canRead(localUri, str1))) {
        return null;
      }
      String str2 = this.mSourceNameHelper.getCanonicalNameForSearchableSource(localUri, localComponentName.getPackageName());
      SearchableSource localSearchableSource = new SearchableSource(this.mContext.getPackageManager().getActivityInfo(localComponentName, 0), this.mConfig, this.mClock, paramSearchableInfo, str1, str2, localUri, this.mSuggestionFilterProvider);
      return localSearchableSource;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.e("Search.SearchableSources", "Source not found: " + localNameNotFoundException);
    }
    return null;
  }
  
  private Map<String, ContentProviderSource> createSources()
  {
    List localList = this.mSearchManager.getSearchablesInGlobalSearch();
    Object localObject;
    if (localList == null)
    {
      Log.e("Search.SearchableSources", "getSearchablesInGlobalSearch() returned null");
      localObject = Collections.emptyMap();
    }
    for (;;)
    {
      return localObject;
      localObject = Maps.newHashMap();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        ContentProviderSource localContentProviderSource = createSearchableSource((SearchableInfo)localIterator.next());
        if ((localContentProviderSource != null) && (!this.mConfig.isSourceIgnored(localContentProviderSource)))
        {
          if ((ContentProviderSource)((Map)localObject).put(localContentProviderSource.getName(), localContentProviderSource) == null) {}
          for (boolean bool = true;; bool = false)
          {
            Preconditions.checkState(bool);
            break;
          }
        }
        if (localContentProviderSource == null) {}
      }
    }
  }
  
  private static Uri getSuggestUri(SearchableInfo paramSearchableInfo)
  {
    if (paramSearchableInfo == null) {}
    String str1;
    do
    {
      return null;
      str1 = paramSearchableInfo.getSuggestAuthority();
    } while (str1 == null);
    Uri.Builder localBuilder = new Uri.Builder().scheme("content").authority(str1);
    String str2 = paramSearchableInfo.getSuggestPath();
    if (str2 != null) {
      localBuilder.appendEncodedPath(str2);
    }
    localBuilder.appendPath("search_suggest_query");
    return localBuilder.build();
  }
  
  public boolean containsSource(String paramString)
  {
    synchronized (this.mSources)
    {
      boolean bool = this.mSources.containsKey(paramString);
      return bool;
    }
  }
  
  public ContentProviderSource getSource(String paramString)
  {
    synchronized (this.mSources)
    {
      ContentProviderSource localContentProviderSource = (ContentProviderSource)Preconditions.checkNotNull(this.mSources.get(paramString));
      return localContentProviderSource;
    }
  }
  
  public Collection<ContentProviderSource> getSources()
  {
    synchronized (this.mSources)
    {
      ImmutableList localImmutableList = this.mSourceList;
      return localImmutableList;
    }
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.registerObserver(paramDataSetObserver);
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.unregisterObserver(paramDataSetObserver);
  }
  
  public void updateSources()
  {
    if (!this.mReceiverRegistered.getAndSet(true))
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.search.action.SEARCHABLES_CHANGED");
      localIntentFilter.addAction("android.search.action.SETTINGS_CHANGED");
      this.mContext.getApplicationContext().registerReceiver(new SourceUpdateReceiver(null), localIntentFilter);
    }
    new ExecutorAsyncTask(this.mUiThreadExecutor, "UpdateSearchableSources", this.mBackgroundExecutor)
    {
      protected Map<String, ContentProviderSource> doInBackground(Void... paramAnonymousVarArgs)
      {
        return SearchableSources.this.createSources();
      }
      
      protected void onPostExecute(Map<String, ContentProviderSource> paramAnonymousMap)
      {
        synchronized (SearchableSources.this.mSources)
        {
          SearchableSources.this.mSources.clear();
          SearchableSources.this.mSources.putAll(paramAnonymousMap);
          SearchableSources.access$302(SearchableSources.this, ImmutableList.copyOf(SearchableSources.this.mSources.values()));
          SearchableSources.this.mObservable.notifyChanged();
          return;
        }
      }
    }.execute(new Void[0]);
  }
  
  private class SourceUpdateReceiver
    extends BroadcastReceiver
  {
    private SourceUpdateReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str = paramIntent.getAction();
      if (("android.search.action.SEARCHABLES_CHANGED".equals(str)) || ("android.search.action.SETTINGS_CHANGED".equals(str))) {
        SearchableSources.this.updateSources();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SearchableSources
 * JD-Core Version:    0.7.0.1
 */