package com.android.launcher3;

import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcelable;

class PendingAddWidgetInfo
  extends PendingAddItemInfo
{
  Bundle bindOptions = null;
  AppWidgetHostView boundWidget;
  Parcelable configurationData;
  int icon;
  AppWidgetProviderInfo info;
  String mimeType;
  int minHeight;
  int minResizeHeight;
  int minResizeWidth;
  int minWidth;
  int previewImage;
  
  public PendingAddWidgetInfo(AppWidgetProviderInfo paramAppWidgetProviderInfo, String paramString, Parcelable paramParcelable)
  {
    this.itemType = 4;
    this.info = paramAppWidgetProviderInfo;
    this.componentName = paramAppWidgetProviderInfo.provider;
    this.minWidth = paramAppWidgetProviderInfo.minWidth;
    this.minHeight = paramAppWidgetProviderInfo.minHeight;
    this.minResizeWidth = paramAppWidgetProviderInfo.minResizeWidth;
    this.minResizeHeight = paramAppWidgetProviderInfo.minResizeHeight;
    this.previewImage = paramAppWidgetProviderInfo.previewImage;
    this.icon = paramAppWidgetProviderInfo.icon;
    if ((paramString != null) && (paramParcelable != null))
    {
      this.mimeType = paramString;
      this.configurationData = paramParcelable;
    }
  }
  
  public PendingAddWidgetInfo(PendingAddWidgetInfo paramPendingAddWidgetInfo)
  {
    this.minWidth = paramPendingAddWidgetInfo.minWidth;
    this.minHeight = paramPendingAddWidgetInfo.minHeight;
    this.minResizeWidth = paramPendingAddWidgetInfo.minResizeWidth;
    this.minResizeHeight = paramPendingAddWidgetInfo.minResizeHeight;
    this.previewImage = paramPendingAddWidgetInfo.previewImage;
    this.icon = paramPendingAddWidgetInfo.icon;
    this.info = paramPendingAddWidgetInfo.info;
    this.boundWidget = paramPendingAddWidgetInfo.boundWidget;
    this.mimeType = paramPendingAddWidgetInfo.mimeType;
    this.configurationData = paramPendingAddWidgetInfo.configurationData;
    this.componentName = paramPendingAddWidgetInfo.componentName;
    this.itemType = paramPendingAddWidgetInfo.itemType;
    this.spanX = paramPendingAddWidgetInfo.spanX;
    this.spanY = paramPendingAddWidgetInfo.spanY;
    this.minSpanX = paramPendingAddWidgetInfo.minSpanX;
    this.minSpanY = paramPendingAddWidgetInfo.minSpanY;
    Bundle localBundle1 = paramPendingAddWidgetInfo.bindOptions;
    Bundle localBundle2 = null;
    if (localBundle1 == null) {}
    for (;;)
    {
      this.bindOptions = localBundle2;
      return;
      localBundle2 = (Bundle)paramPendingAddWidgetInfo.bindOptions.clone();
    }
  }
  
  public String toString()
  {
    return "Widget: " + this.componentName.toShortString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PendingAddWidgetInfo
 * JD-Core Version:    0.7.0.1
 */