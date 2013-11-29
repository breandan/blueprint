package com.google.android.sidekick.main.entry;

import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.sidekick.shared.client.EntriesRefreshRequestType;
import javax.annotation.Nullable;

public class EntriesRefreshThrottleImpl
  implements EntriesRefreshThrottle
{
  private final AlarmHelper mAlarmHelper;
  private final GsaPreferenceController mPrefController;
  private final GsaConfigFlags mSearchConfig;
  
  public EntriesRefreshThrottleImpl(AlarmHelper paramAlarmHelper, GsaPreferenceController paramGsaPreferenceController, GsaConfigFlags paramGsaConfigFlags)
  {
    this.mAlarmHelper = paramAlarmHelper;
    this.mPrefController = paramGsaPreferenceController;
    this.mSearchConfig = paramGsaConfigFlags;
  }
  
  private long nextTimeFor(int paramInt, long paramLong)
  {
    if (EntriesRefreshRequestType.isUserInitiated(paramInt)) {}
    while (paramInt == 0) {
      return paramLong;
    }
    SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
    long l1 = localSharedPreferencesExt.getLong("throttle_last_partial", 0L);
    long l2 = localSharedPreferencesExt.getLong("throttle_last_full", 0L);
    if (paramInt == 5) {}
    for (long l3 = 60000L * this.mSearchConfig.getMarinerBackgroundPartialRefreshRateLimitMinutes() + Math.max(l1, l2);; l3 = l2 + 60000L * this.mSearchConfig.getMarinerBackgroundRefreshRateLimitMinutes()) {
      return l3;
    }
  }
  
  public boolean isRefreshAllowed(int paramInt, @Nullable PendingIntent paramPendingIntent, long paramLong)
  {
    long l = nextTimeFor(paramInt, paramLong);
    if (l <= paramLong)
    {
      SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
      if (paramInt == 5) {
        localSharedPreferencesExt.edit().putLong("throttle_last_partial", paramLong).apply();
      }
      for (;;)
      {
        return true;
        localSharedPreferencesExt.edit().putLong("throttle_last_full", paramLong).apply();
      }
    }
    if (paramPendingIntent != null) {
      this.mAlarmHelper.setInexact(2, l, paramPendingIntent);
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntriesRefreshThrottleImpl
 * JD-Core Version:    0.7.0.1
 */