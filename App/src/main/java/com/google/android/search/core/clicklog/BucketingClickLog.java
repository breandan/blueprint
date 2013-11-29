package com.google.android.search.core.clicklog;

import android.text.TextUtils;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExpiringSum;
import com.google.android.shared.util.NamedTask;
import com.google.android.shared.util.NamedTaskExecutor;
import com.google.android.shared.util.NonCancellableNamedTask;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BucketingClickLog
  implements ClickLog
{
  static final long CLICK_COUNT_GRANULARITY_MILLIS = 172800000L;
  private final Map<String, ExpiringSum> mClicks;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final NamedTaskExecutor mExecutor;
  private boolean mFlushPending;
  private final NamedTask mFlushSettingsTask;
  private final SearchSettings mSettings;
  
  public BucketingClickLog(SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, Clock paramClock, NamedTaskExecutor paramNamedTaskExecutor)
  {
    this.mConfig = paramSearchConfig;
    this.mSettings = paramSearchSettings;
    this.mClock = paramClock;
    this.mExecutor = paramNamedTaskExecutor;
    this.mClicks = Maps.newHashMap();
    this.mFlushSettingsTask = new NonCancellableNamedTask()
    {
      public String getName()
      {
        return getClass().getName();
      }
      
      public void run()
      {
        BucketingClickLog.this.flushSettings();
      }
    };
    Iterator localIterator = this.mSettings.getAllSourceClickStats().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str1 = (String)localEntry.getKey();
      String str2 = (String)localEntry.getValue();
      if (!TextUtils.isEmpty((CharSequence)localEntry.getValue())) {
        this.mClicks.put(str1, new ExpiringSum(this.mClock, this.mConfig.getMaxStatAgeMillis(), 172800000L, str2));
      }
    }
  }
  
  private void flushSettings()
  {
    try
    {
      this.mFlushPending = false;
      Iterator localIterator = this.mClicks.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = ((ExpiringSum)localEntry.getValue()).getJsonIfChanged();
        if (str != null) {
          this.mSettings.setSourceClickStats((String)localEntry.getKey(), str);
        }
      }
    }
    finally {}
  }
  
  private void scheduleFlushSettings()
  {
    if (!this.mFlushPending)
    {
      this.mFlushPending = true;
      this.mExecutor.execute(this.mFlushSettingsTask);
    }
  }
  
  public Map<String, Integer> getSourceScores()
  {
    HashMap localHashMap;
    try
    {
      localHashMap = Maps.newHashMap();
      Iterator localIterator = this.mClicks.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        int i = ((ExpiringSum)localEntry.getValue()).getTotal();
        if (i >= this.mConfig.getMinClicksForSourceRanking()) {
          localHashMap.put(localEntry.getKey(), Integer.valueOf(i));
        }
      }
    }
    finally {}
    return localHashMap;
  }
  
  public void reportClick(Suggestion paramSuggestion)
  {
    if (paramSuggestion.getSourceCanonicalName() != null) {
      reportClickAtTime(paramSuggestion.getSourceCanonicalName(), this.mClock.currentTimeMillis());
    }
  }
  
  public void reportClickAtTime(String paramString, long paramLong)
  {
    try
    {
      ExpiringSum localExpiringSum = (ExpiringSum)this.mClicks.get(paramString);
      if (localExpiringSum == null)
      {
        localExpiringSum = new ExpiringSum(this.mClock, this.mConfig.getMaxStatAgeMillis(), 172800000L);
        this.mClicks.put(paramString, localExpiringSum);
      }
      localExpiringSum.incrementAtTime(paramLong);
      scheduleFlushSettings();
      return;
    }
    finally {}
  }
  
  public void upgradeToCanonicalNames(Map<String, String> paramMap)
  {
    try
    {
      Iterator localIterator = this.mSettings.takeAllLegacySourceClickStats().entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str1 = (String)paramMap.get((String)localEntry.getKey());
        if (str1 != null)
        {
          String str2 = (String)localEntry.getValue();
          ExpiringSum localExpiringSum = new ExpiringSum(this.mClock, this.mConfig.getMaxStatAgeMillis(), 172800000L, str2, true);
          this.mClicks.put(str1, localExpiringSum);
        }
      }
      scheduleFlushSettings();
    }
    finally {}
    this.mSettings.setSourceClickStatsUpgradeNoLongerNeeded();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.clicklog.BucketingClickLog
 * JD-Core Version:    0.7.0.1
 */