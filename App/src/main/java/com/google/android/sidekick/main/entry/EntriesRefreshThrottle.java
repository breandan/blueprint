package com.google.android.sidekick.main.entry;

import android.app.PendingIntent;
import javax.annotation.Nullable;

public abstract interface EntriesRefreshThrottle
{
  public abstract boolean isRefreshAllowed(int paramInt, @Nullable PendingIntent paramPendingIntent, long paramLong);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntriesRefreshThrottle
 * JD-Core Version:    0.7.0.1
 */