package com.google.android.search.core;

import android.content.Context;
import android.database.DataSetObserver;
import com.google.android.search.core.clicklog.BucketingClickLog;
import com.google.android.search.core.clicklog.ClickLog;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.suggest.SuggestionFilterProvider;
import com.google.android.search.core.suggest.SuggestionsProvider;
import com.google.android.search.core.summons.CompositeSources;
import com.google.android.search.core.summons.ContentProviderSource;
import com.google.android.search.core.summons.DefaultSourceRanker;
import com.google.android.search.core.summons.SearchableSources;
import com.google.android.search.core.summons.ShouldQueryStrategy;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.SourceNameHelper;
import com.google.android.search.core.summons.SourceRanker;
import com.google.android.search.core.summons.Sources;
import com.google.android.search.core.summons.icing.IcingFactory;
import com.google.android.search.core.summons.icing.IcingSources;
import com.google.android.search.shared.ui.LevenshteinSuggestionFormatter;
import com.google.android.search.shared.ui.SuggestionFormatter;
import com.google.android.search.shared.ui.TextAppearanceFactory;
import com.google.android.shared.util.NamedTaskExecutor;
import com.google.android.shared.util.PerNameExecutor;
import com.google.android.shared.util.PriorityThreadFactory;
import com.google.android.shared.util.SingleThreadNamedTaskExecutor;
import com.google.android.velvet.VelvetStrictMode;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public class GlobalSearchServicesImpl
  implements GlobalSearchServices
{
  private final AsyncServices mAsyncServices;
  private BucketingClickLog mBucketingClickLog;
  private final Context mContext;
  private final CoreSearchServices mCoreServices;
  private final Object mCreationLock;
  private SearchHistoryHelper mHistoryHelper;
  private IcingFactory mIcingFactory;
  private ThreadFactory mQueryThreadFactory;
  private ShouldQueryStrategy mShouldQueryStrategy;
  private final SourceNameHelper mSourceNameHelper;
  private SourceRanker mSourceRanker;
  private NamedTaskExecutor mSourceTaskExecutor;
  private CompositeSources mSources;
  private SuggestionFormatter mSuggestionFormatter;
  private SuggestionsProvider mSuggestionsProvider;
  
  public GlobalSearchServicesImpl(Context paramContext, CoreSearchServices paramCoreSearchServices, AsyncServices paramAsyncServices, Object paramObject)
  {
    this.mContext = paramContext;
    this.mCoreServices = paramCoreSearchServices;
    this.mAsyncServices = paramAsyncServices;
    this.mSourceNameHelper = new SourceNameHelper(paramContext.getPackageName());
    this.mCreationLock = paramObject;
  }
  
  private BucketingClickLog createBucketingClickLog()
  {
    return new BucketingClickLog(this.mCoreServices.getConfig(), this.mCoreServices.getSearchSettings(), this.mCoreServices.getClock(), this.mAsyncServices.getPooledBackgroundTaskExecutor());
  }
  
  private ThreadFactory createQueryThreadFactory()
  {
    int i = this.mCoreServices.getConfig().getQueryThreadPriority();
    return VelvetStrictMode.applyThreadPolicy(new ThreadFactoryBuilder().setNameFormat("QSB #%d").setThreadFactory(new PriorityThreadFactory(i)).build());
  }
  
  private SearchHistoryHelper createSearchHistoryHelper()
  {
    return new SearchHistoryHelper(this.mCoreServices.getHttpHelper(), this.mCoreServices.getSearchUrlHelper(), this.mCoreServices.getLoginHelper(), this.mCoreServices.getConfig(), this.mAsyncServices.getNamedUserFacingTaskExecutor("history-api"), this.mAsyncServices.getUiThreadExecutor());
  }
  
  private ShouldQueryStrategy createShouldQueryStrategy()
  {
    return new ShouldQueryStrategy(this, this.mCoreServices.getConfig());
  }
  
  private SourceRanker createSourceRanker()
  {
    VelvetStrictMode.checkStartupAtLeast(6);
    return new DefaultSourceRanker(getSources(), this.mSources.getContentProviderSources(), this.mCoreServices.getSearchSettings(), getClickLog());
  }
  
  private NamedTaskExecutor createSourceTaskExecutor()
  {
    return new PerNameExecutor(SingleThreadNamedTaskExecutor.factory(getQueryThreadFactory()));
  }
  
  private CompositeSources createSources()
  {
    VelvetStrictMode.checkStartupAtLeast(6);
    boolean bool = this.mCoreServices.getConfig().isContentProviderGlobalSearchEnabled();
    SearchableSources localSearchableSources = null;
    if (bool)
    {
      SuggestionFilterProvider localSuggestionFilterProvider = new SuggestionFilterProvider(this.mContext, this.mCoreServices.getConfig());
      localSearchableSources = new SearchableSources(this.mContext, this.mCoreServices.getConfig(), this.mCoreServices.getClock(), localSuggestionFilterProvider, this.mAsyncServices.getPooledUserFacingTaskExecutor(), this.mAsyncServices.getUiThreadExecutor(), this.mSourceNameHelper);
      if (this.mCoreServices.getSearchSettings().needLegacyClickStatsUpgrade()) {
        localSearchableSources.registerDataSetObserver(new UpgradeClickStatsOnSourcesChangedObserver(getBucketingClickLog(), localSearchableSources));
      }
      localSearchableSources.updateSources();
    }
    return new CompositeSources(localSearchableSources, getIcingFactory().createIcingSources(this.mCoreServices.getSearchSettings(), this.mSourceNameHelper));
  }
  
  private SuggestionFormatter createSuggestionFormatter()
  {
    VelvetStrictMode.checkStartupAtLeast(6);
    return new LevenshteinSuggestionFormatter(new TextAppearanceFactory(this.mContext));
  }
  
  private SuggestionsProvider createSuggestionsProvider()
  {
    VelvetStrictMode.checkStartupAtLeast(6);
    return new SuggestionsProvider(this.mContext, this.mCoreServices.getConfig(), this.mCoreServices.getGsaConfigFlags(), getShouldQueryStrategy(), getSourceTaskExecutor(), this.mAsyncServices.getUiThreadExecutor(), this.mCoreServices.getNowOptInSettings());
  }
  
  private BucketingClickLog getBucketingClickLog()
  {
    if (this.mBucketingClickLog == null) {
      this.mBucketingClickLog = createBucketingClickLog();
    }
    return this.mBucketingClickLog;
  }
  
  private ThreadFactory getQueryThreadFactory()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mQueryThreadFactory == null) {
        this.mQueryThreadFactory = createQueryThreadFactory();
      }
      ThreadFactory localThreadFactory = this.mQueryThreadFactory;
      return localThreadFactory;
    }
  }
  
  private NamedTaskExecutor getSourceTaskExecutor()
  {
    if (this.mSourceTaskExecutor == null) {
      this.mSourceTaskExecutor = createSourceTaskExecutor();
    }
    return this.mSourceTaskExecutor;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    if (this.mIcingFactory != null) {
      this.mIcingFactory.dump(paramString, paramPrintWriter);
    }
    if (this.mSources != null) {
      this.mSources.dump(paramString, paramPrintWriter);
    }
  }
  
  public ClickLog getClickLog()
  {
    return getBucketingClickLog();
  }
  
  public IcingFactory getIcingFactory()
  {
    if (this.mIcingFactory == null) {
      this.mIcingFactory = new IcingFactory(this.mContext, this.mAsyncServices.getUiThreadExecutor(), this.mCoreServices.getConfig(), this.mCoreServices.getGooglePlayServicesHelper());
    }
    return this.mIcingFactory;
  }
  
  public IcingSources getIcingSources()
  {
    if (this.mSources == null) {
      this.mSources = createSources();
    }
    return this.mSources.getIcingSources();
  }
  
  public SearchHistoryHelper getSearchHistoryHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mHistoryHelper == null) {
        this.mHistoryHelper = createSearchHistoryHelper();
      }
      SearchHistoryHelper localSearchHistoryHelper = this.mHistoryHelper;
      return localSearchHistoryHelper;
    }
  }
  
  public ShouldQueryStrategy getShouldQueryStrategy()
  {
    if (this.mShouldQueryStrategy == null) {
      this.mShouldQueryStrategy = createShouldQueryStrategy();
    }
    return this.mShouldQueryStrategy;
  }
  
  public SourceRanker getSourceRanker()
  {
    if (this.mSourceRanker == null) {
      this.mSourceRanker = createSourceRanker();
    }
    return this.mSourceRanker;
  }
  
  public Sources<Source> getSources()
  {
    if (this.mSources == null) {
      this.mSources = createSources();
    }
    return this.mSources;
  }
  
  public SuggestionFormatter getSuggestionFormatter()
  {
    if (this.mSuggestionFormatter == null) {
      this.mSuggestionFormatter = createSuggestionFormatter();
    }
    return this.mSuggestionFormatter;
  }
  
  public SuggestionsProvider getSuggestionsProvider()
  {
    if (this.mSuggestionsProvider == null) {
      this.mSuggestionsProvider = createSuggestionsProvider();
    }
    return this.mSuggestionsProvider;
  }
  
  private static final class UpgradeClickStatsOnSourcesChangedObserver
    extends DataSetObserver
  {
    private final BucketingClickLog mBucketingClickLog;
    private final SearchableSources mSearchableSources;
    
    public UpgradeClickStatsOnSourcesChangedObserver(BucketingClickLog paramBucketingClickLog, SearchableSources paramSearchableSources)
    {
      this.mBucketingClickLog = paramBucketingClickLog;
      this.mSearchableSources = paramSearchableSources;
    }
    
    public void onChanged()
    {
      this.mSearchableSources.unregisterDataSetObserver(this);
      HashMap localHashMap = Maps.newHashMap();
      Iterator localIterator = this.mSearchableSources.getSources().iterator();
      while (localIterator.hasNext())
      {
        ContentProviderSource localContentProviderSource = (ContentProviderSource)localIterator.next();
        localHashMap.put(localContentProviderSource.getName(), localContentProviderSource.getCanonicalName());
      }
      this.mBucketingClickLog.upgradeToCanonicalNames(localHashMap);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GlobalSearchServicesImpl
 * JD-Core Version:    0.7.0.1
 */