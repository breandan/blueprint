package com.android.launcher3;

import android.appwidget.AppWidgetHostView;
import android.content.ComponentName;
import android.content.ContentValues;

class LauncherAppWidgetInfo
  extends ItemInfo
{
  int appWidgetId = -1;
  AppWidgetHostView hostView = null;
  private boolean mHasNotifiedInitialWidgetSizeChanged;
  int minHeight = -1;
  int minWidth = -1;
  ComponentName providerName;
  
  LauncherAppWidgetInfo(int paramInt, ComponentName paramComponentName)
  {
    this.itemType = 4;
    this.appWidgetId = paramInt;
    this.providerName = paramComponentName;
    this.spanX = -1;
    this.spanY = -1;
  }
  
  void notifyWidgetSizeChanged(Launcher paramLauncher)
  {
    AppWidgetResizeFrame.updateWidgetSizeRanges(this.hostView, paramLauncher, this.spanX, this.spanY);
    this.mHasNotifiedInitialWidgetSizeChanged = true;
  }
  
  void onAddToDatabase(ContentValues paramContentValues)
  {
    super.onAddToDatabase(paramContentValues);
    paramContentValues.put("appWidgetId", Integer.valueOf(this.appWidgetId));
    paramContentValues.put("appWidgetProvider", this.providerName.flattenToString());
  }
  
  void onBindAppWidget(Launcher paramLauncher)
  {
    if (!this.mHasNotifiedInitialWidgetSizeChanged) {
      notifyWidgetSizeChanged(paramLauncher);
    }
  }
  
  public String toString()
  {
    return "AppWidget(id=" + Integer.toString(this.appWidgetId) + ")";
  }
  
  void unbind()
  {
    super.unbind();
    this.hostView = null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAppWidgetInfo
 * JD-Core Version:    0.7.0.1
 */