package com.google.android.sidekick.main.inject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SystemPendingIntentFactory
  implements PendingIntentFactory
{
  private final Context mAppContext;
  
  public SystemPendingIntentFactory(Context paramContext)
  {
    this.mAppContext = paramContext;
  }
  
  public PendingIntent getBroadcast(int paramInt1, Intent paramIntent, int paramInt2)
  {
    return PendingIntent.getBroadcast(this.mAppContext, paramInt1, paramIntent, paramInt2);
  }
  
  public PendingIntent getService(int paramInt1, Intent paramIntent, int paramInt2)
  {
    return PendingIntent.getService(this.mAppContext, paramInt1, paramIntent, paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SystemPendingIntentFactory
 * JD-Core Version:    0.7.0.1
 */