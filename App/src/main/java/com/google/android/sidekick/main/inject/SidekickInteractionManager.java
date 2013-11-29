package com.google.android.sidekick.main.inject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.search.core.SearchConfig;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Supplier;

public class SidekickInteractionManager
{
  static final String LAST_INTERACTION_TIME_KEY = "last_predictive_interaction";
  private static final String TAG = Tag.getTag(SidekickInteractionManager.class);
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Supplier<SharedPreferences> mSharedPreferences;
  private final WidgetManager mWidgetManager;
  
  public SidekickInteractionManager(Supplier<SharedPreferences> paramSupplier, SearchConfig paramSearchConfig, WidgetManager paramWidgetManager, Clock paramClock)
  {
    this.mSharedPreferences = paramSupplier;
    this.mConfig = paramSearchConfig;
    this.mWidgetManager = paramWidgetManager;
    this.mClock = paramClock;
  }
  
  private boolean hasRecentPredictiveInteraction()
  {
    int i = this.mConfig.getPredictiveIdleUserThresholdMinutes();
    if (i <= 0) {
      return true;
    }
    long l = ((SharedPreferences)this.mSharedPreferences.get()).getLong("last_predictive_interaction", 0L);
    if (l == 0L)
    {
      recordPredictiveInteraction();
      return true;
    }
    if (l > this.mClock.currentTimeMillis() - 60000L * i) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isIdle()
  {
    return (this.mWidgetManager.getWidgetInstallCount() == 0) && (!hasRecentPredictiveInteraction());
  }
  
  public void recordPredictiveInteraction()
  {
    ((SharedPreferences)this.mSharedPreferences.get()).edit().putLong("last_predictive_interaction", this.mClock.currentTimeMillis()).apply();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SidekickInteractionManager
 * JD-Core Version:    0.7.0.1
 */