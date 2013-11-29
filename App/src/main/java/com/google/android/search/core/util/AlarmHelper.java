package com.google.android.search.core.util;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.Tag;
import java.util.Random;

public class AlarmHelper
{
  static final int INTERVAL_TO_JITTER_DIVISOR = 15;
  static final int MIN_JITTER_MILLIS = 30000;
  private static final String TAG = Tag.getTag(AlarmHelper.class);
  private final AlarmManager mAlarmManager;
  private final Clock mClock;
  private final GsaPreferenceController mPreferenceController;
  private final Random mRandom;
  private final int mSdkVersion;
  
  AlarmHelper(AlarmManager paramAlarmManager, Clock paramClock, GsaPreferenceController paramGsaPreferenceController, Random paramRandom, int paramInt)
  {
    this.mAlarmManager = paramAlarmManager;
    this.mClock = paramClock;
    this.mPreferenceController = paramGsaPreferenceController;
    this.mRandom = paramRandom;
    this.mSdkVersion = paramInt;
  }
  
  public AlarmHelper(Context paramContext, Clock paramClock, GsaPreferenceController paramGsaPreferenceController, Random paramRandom)
  {
    this((AlarmManager)paramContext.getSystemService("alarm"), paramClock, paramGsaPreferenceController, paramRandom, Build.VERSION.SDK_INT);
  }
  
  private static int calculateMaxJitter(long paramLong)
  {
    return Math.max((int)(paramLong / 15L), 30000);
  }
  
  private int getRandomPositiveJitterMillis(long paramLong)
  {
    return this.mRandom.nextInt(calculateMaxJitter(paramLong));
  }
  
  static String getStartTimeKey(String paramString)
  {
    return "AlarmStartTimeMillis_" + paramString;
  }
  
  private void putLongSetting(String paramString, long paramLong)
  {
    this.mPreferenceController.getMainPreferences().edit().putLong(paramString, paramLong).apply();
  }
  
  public void cancel(PendingIntent paramPendingIntent)
  {
    this.mAlarmManager.cancel(paramPendingIntent);
  }
  
  public void cancel(PendingIntent paramPendingIntent, String paramString)
  {
    String str = getStartTimeKey(paramString);
    this.mPreferenceController.getMainPreferences().edit().remove(str).apply();
    cancel(paramPendingIntent);
  }
  
  public void scheduleNextAlarmOccurrence(long paramLong, PendingIntent paramPendingIntent, String paramString, boolean paramBoolean)
  {
    SharedPreferencesExt localSharedPreferencesExt = this.mPreferenceController.getMainPreferences();
    String str = getStartTimeKey(paramString);
    long l1 = 0L;
    long l2 = this.mClock.elapsedRealtime();
    long l3;
    if ((!paramBoolean) && (localSharedPreferencesExt.getLong(str, 0L) > 0L))
    {
      l3 = localSharedPreferencesExt.getLong(str, 0L);
      if (l3 >= l2) {
        break label131;
      }
      l1 = l3 + paramLong * (1L + (l2 - l3) / paramLong);
      putLongSetting(str, l1);
    }
    for (;;)
    {
      if (l1 == 0L)
      {
        l1 = l2 + paramLong + getRandomPositiveJitterMillis(paramLong);
        putLongSetting(str, l1);
      }
      setExact(2, l1, paramPendingIntent);
      return;
      label131:
      if (l3 < l2 + paramLong) {
        l1 = l3;
      }
    }
  }
  
  @TargetApi(19)
  public void setExact(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    if (this.mSdkVersion >= 19)
    {
      this.mAlarmManager.setExact(paramInt, paramLong, paramPendingIntent);
      return;
    }
    this.mAlarmManager.set(paramInt, paramLong, paramPendingIntent);
  }
  
  public void setInexact(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    this.mAlarmManager.set(paramInt, paramLong, paramPendingIntent);
  }
  
  public void setInexactRepeating(int paramInt, long paramLong1, long paramLong2, PendingIntent paramPendingIntent)
  {
    this.mAlarmManager.setRepeating(paramInt, paramLong1, paramLong2, paramPendingIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.AlarmHelper
 * JD-Core Version:    0.7.0.1
 */