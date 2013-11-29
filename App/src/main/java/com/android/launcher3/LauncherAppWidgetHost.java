package com.android.launcher3;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

public class LauncherAppWidgetHost
  extends AppWidgetHost
{
  Launcher mLauncher;
  
  public LauncherAppWidgetHost(Launcher paramLauncher, int paramInt)
  {
    super(paramLauncher, paramInt);
    this.mLauncher = paramLauncher;
  }
  
  protected AppWidgetHostView onCreateView(Context paramContext, int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    return new LauncherAppWidgetHostView(paramContext);
  }
  
  protected void onProvidersChanged()
  {
    this.mLauncher.bindPackagesUpdated(LauncherModel.getSortedWidgetsAndShortcuts(this.mLauncher));
  }
  
  public void stopListening()
  {
    super.stopListening();
    clearViews();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAppWidgetHost
 * JD-Core Version:    0.7.0.1
 */