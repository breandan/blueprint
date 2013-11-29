package com.google.android.sidekick.main.inject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

public class SystemNotificationManagerInjectable
  implements NotificationManagerInjectable
{
  private final NotificationManager mNotificationManager;
  
  public SystemNotificationManagerInjectable(Context paramContext)
  {
    this.mNotificationManager = ((NotificationManager)paramContext.getSystemService("notification"));
  }
  
  public void cancel(int paramInt)
  {
    this.mNotificationManager.cancel(paramInt);
  }
  
  public void cancelAll()
  {
    this.mNotificationManager.cancelAll();
  }
  
  public void notify(int paramInt, Notification paramNotification)
  {
    this.mNotificationManager.notify(paramInt, paramNotification);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SystemNotificationManagerInjectable
 * JD-Core Version:    0.7.0.1
 */