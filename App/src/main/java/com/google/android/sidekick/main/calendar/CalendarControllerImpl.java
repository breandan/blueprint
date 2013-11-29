package com.google.android.sidekick.main.calendar;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.Tag;

public class CalendarControllerImpl
  implements CalendarController
{
  private static final String TAG = Tag.getTag(CalendarControllerImpl.class);
  private static boolean sCalendarStarted = false;
  private final Context mAppContext;
  private CalendarObserver mCalendarObserver;
  private final Clock mClock;
  
  public CalendarControllerImpl(Context paramContext, Clock paramClock)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mClock = paramClock;
  }
  
  private void registerCalendarObserver()
  {
    this.mCalendarObserver = new CalendarObserver(this.mAppContext, this.mClock);
    new CalendarAccessor(this.mAppContext).registerEventsObserver(this.mCalendarObserver);
  }
  
  private void unregisterCalendarObserver()
  {
    if (this.mCalendarObserver != null)
    {
      new CalendarAccessor(this.mAppContext).unregisterEventsObserver(this.mCalendarObserver);
      this.mCalendarObserver = null;
    }
  }
  
  public CalendarDataProvider newCalendarDataProvider()
  {
    return new CalendarDataProviderImpl(this.mAppContext, this.mClock);
  }
  
  public void startCalendar(SidekickInjector paramSidekickInjector)
  {
    try
    {
      if (!sCalendarStarted)
      {
        paramSidekickInjector.getCalendarDataProvider().initialize();
        CalendarIntentService.registerForSignificantLocationChange(paramSidekickInjector);
        registerCalendarObserver();
        sCalendarStarted = true;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void stopCalendar(SidekickInjector paramSidekickInjector)
  {
    try
    {
      ExtraPreconditions.checkMainThread();
      CalendarIntentService.stopUpdateAlarm(this.mAppContext);
      if (sCalendarStarted)
      {
        CalendarIntentService.unregisterForSignificantLocationChange(paramSidekickInjector);
        unregisterCalendarObserver();
        sCalendarStarted = false;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private static class CalendarObserver
    extends ContentObserver
  {
    private final Context mAppContext;
    private int mChangesSwallowedLastBatch = 0;
    private final Clock mClock;
    private long mLastBatchStartTimeMillis = 0L;
    
    public CalendarObserver(Context paramContext, Clock paramClock)
    {
      super();
      this.mAppContext = paramContext;
      this.mClock = paramClock;
    }
    
    public boolean deliverSelfNotifications()
    {
      return true;
    }
    
    public void onChange(boolean paramBoolean)
    {
      long l = this.mClock.currentTimeMillis();
      if (l > 6000L + this.mLastBatchStartTimeMillis)
      {
        this.mLastBatchStartTimeMillis = l;
        this.mChangesSwallowedLastBatch = 0;
        CalendarIntentService.sendIntentWithAction(this.mAppContext, CalendarIntentService.UPDATE_CALENDAR_ACTION);
        return;
      }
      this.mChangesSwallowedLastBatch = (1 + this.mChangesSwallowedLastBatch);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarControllerImpl
 * JD-Core Version:    0.7.0.1
 */