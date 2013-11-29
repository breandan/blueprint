package com.google.android.sidekick.main.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.google.android.apps.sidekick.widget.PredictiveCardsWidgetProvider;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.inject.WidgetManager;
import java.util.concurrent.atomic.AtomicBoolean;

public class WidgetManagerImpl
  implements WidgetManager
{
  private final Context mAppContext;
  private final ScheduledSingleThreadedExecutor mExecutor;
  private DelayedWidgetUpdater mLastQueuedUpdate;
  
  public WidgetManagerImpl(Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mAppContext = paramContext;
    this.mExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  private void updateWidgetInternal()
  {
    AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this.mAppContext);
    ComponentName localComponentName = new ComponentName(this.mAppContext, PredictiveCardsWidgetProvider.class);
    Intent localIntent = new Intent(this.mAppContext, PredictiveCardsWidgetProvider.class);
    localIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    localIntent.putExtra("appWidgetIds", localAppWidgetManager.getAppWidgetIds(localComponentName));
    localIntent.putExtra("internal_request", true);
    this.mAppContext.sendBroadcast(localIntent);
  }
  
  public int getWidgetInstallCount()
  {
    int[] arrayOfInt = AppWidgetManager.getInstance(this.mAppContext).getAppWidgetIds(new ComponentName(this.mAppContext, PredictiveCardsWidgetProvider.class));
    if (arrayOfInt == null) {
      return 0;
    }
    return arrayOfInt.length;
  }
  
  public void updateWidget()
  {
    DelayedWidgetUpdater localDelayedWidgetUpdater = new DelayedWidgetUpdater(null);
    try
    {
      if (this.mLastQueuedUpdate != null) {
        this.mLastQueuedUpdate.cancel();
      }
      this.mLastQueuedUpdate = localDelayedWidgetUpdater;
      this.mExecutor.executeOnIdle(localDelayedWidgetUpdater);
      return;
    }
    finally {}
  }
  
  private class DelayedWidgetUpdater
    implements Runnable
  {
    private AtomicBoolean mCancelled = new AtomicBoolean(false);
    
    private DelayedWidgetUpdater() {}
    
    public void cancel()
    {
      this.mCancelled.set(true);
    }
    
    public void run()
    {
      if (!this.mCancelled.get()) {
        WidgetManagerImpl.this.updateWidgetInternal();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.WidgetManagerImpl
 * JD-Core Version:    0.7.0.1
 */