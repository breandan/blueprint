package com.google.android.sidekick.main.inject;

import android.app.PendingIntent;
import android.content.Intent;

public abstract interface PendingIntentFactory
{
  public abstract PendingIntent getBroadcast(int paramInt1, Intent paramIntent, int paramInt2);
  
  public abstract PendingIntent getService(int paramInt1, Intent paramIntent, int paramInt2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.PendingIntentFactory
 * JD-Core Version:    0.7.0.1
 */