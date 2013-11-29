package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;

public abstract interface NotificationAction
{
  public abstract int getActionIcon();
  
  public abstract String getActionString(Context paramContext);
  
  public abstract Intent getCallbackIntent(Context paramContext);
  
  public abstract String getCallbackType();
  
  public abstract String getLogString();
  
  public abstract boolean isActive();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NotificationAction
 * JD-Core Version:    0.7.0.1
 */