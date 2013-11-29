package com.google.android.search.core.service;

import android.app.Notification;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class ServiceForegroundHelper
{
  private final Context mContext;
  private final SearchServiceImpl mService;
  
  public ServiceForegroundHelper(SearchServiceImpl paramSearchServiceImpl, Context paramContext)
  {
    this.mService = paramSearchServiceImpl;
    this.mContext = paramContext;
  }
  
  public void setForeground(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Intent localIntent1 = new Intent();
      localIntent1.setClassName(this.mContext, "com.google.android.search.core.service.SearchServiceImpl");
      this.mContext.startService(localIntent1);
      Resources localResources = this.mContext.getResources();
      Notification localNotification = new Notification.Builder(this.mContext).setContentTitle(localResources.getText(2131363678)).setContentText(localResources.getText(2131363679)).setSmallIcon(2130837591).build();
      this.mService.startForeground(1, localNotification);
      return;
    }
    this.mService.stopForeground(true);
    Intent localIntent2 = new Intent();
    localIntent2.setClassName(this.mContext, "com.google.android.search.core.service.SearchServiceImpl");
    this.mContext.stopService(localIntent2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.ServiceForegroundHelper
 * JD-Core Version:    0.7.0.1
 */