package com.google.android.sidekick.main.entry;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.android.sidekick.main.inject.SidekickInteractionManager;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.shared.util.Tag;

public class EntriesRefreshScheduler
{
  static final String REFRESH_ALARM_NAME = "refresh_alarm";
  private static final String TAG = Tag.getTag(EntriesRefreshScheduler.class);
  private final AlarmHelper mAlarmHelper;
  private final Context mAppContext;
  private final PendingIntentFactory mPendingIntentFactory;
  private final SearchConfig mSearchConfig;
  private final SidekickInteractionManager mSidekickInteractionManager;
  
  public EntriesRefreshScheduler(Context paramContext, AlarmHelper paramAlarmHelper, SearchConfig paramSearchConfig, SidekickInteractionManager paramSidekickInteractionManager, PendingIntentFactory paramPendingIntentFactory)
  {
    this.mAppContext = paramContext;
    this.mAlarmHelper = paramAlarmHelper;
    this.mSearchConfig = paramSearchConfig;
    this.mSidekickInteractionManager = paramSidekickInteractionManager;
    this.mPendingIntentFactory = paramPendingIntentFactory;
  }
  
  private PendingIntent getRefreshPendingIntent(Context paramContext, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, EntriesRefreshIntentService.class);
    localIntent.setAction("com.google.android.apps.sidekick.REFRESH");
    PendingIntentFactory localPendingIntentFactory = this.mPendingIntentFactory;
    if (paramBoolean) {}
    for (int i = 134217728;; i = 536870912) {
      return localPendingIntentFactory.getService(0, localIntent, i);
    }
  }
  
  public int getBackgroundRefreshIntervalMinutes()
  {
    if (this.mSidekickInteractionManager.isIdle()) {
      return this.mSearchConfig.getMarinerIdleBackgroundRefreshIntervalMinutes();
    }
    return this.mSearchConfig.getMarinerBackgroundRefreshIntervalMinutes();
  }
  
  public void setNextRefreshAlarm(boolean paramBoolean)
  {
    long l = 60000L * getBackgroundRefreshIntervalMinutes();
    if (getRefreshPendingIntent(this.mAppContext, false) == null)
    {
      Intent localIntent = new Intent(this.mAppContext, NotificationRefreshService.class);
      localIntent.setAction("com.google.android.apps.sidekick.notifications.INITIALIZE");
      this.mAppContext.startService(localIntent);
    }
    this.mAlarmHelper.scheduleNextAlarmOccurrence(l, getRefreshPendingIntent(this.mAppContext, true), "refresh_alarm", paramBoolean);
  }
  
  public void unregisterRefreshAlarm()
  {
    PendingIntent localPendingIntent = getRefreshPendingIntent(this.mAppContext, false);
    if (localPendingIntent != null)
    {
      this.mAlarmHelper.cancel(localPendingIntent, "refresh_alarm");
      localPendingIntent.cancel();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntriesRefreshScheduler
 * JD-Core Version:    0.7.0.1
 */