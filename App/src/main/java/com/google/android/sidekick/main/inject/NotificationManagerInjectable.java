package com.google.android.sidekick.main.inject;

import android.app.Notification;

public abstract interface NotificationManagerInjectable
{
  public abstract void cancel(int paramInt);
  
  public abstract void cancelAll();
  
  public abstract void notify(int paramInt, Notification paramNotification);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.NotificationManagerInjectable
 * JD-Core Version:    0.7.0.1
 */