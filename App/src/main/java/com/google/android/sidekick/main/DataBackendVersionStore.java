package com.google.android.sidekick.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.shared.util.Clock;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.MinimumDataVersion;
import java.util.Collection;
import java.util.Iterator;

public class DataBackendVersionStore
{
  static final String KANSAS_VERSION_INFO_EXPIRATION_PREFERENCE = "com.google.android.apps.sidekick.KANSAS_VERSION_INFO_EXPIRATION";
  static final String KANSAS_VERSION_INFO_PREFERENCE = "com.google.android.apps.sidekick.KANSAS_VERSION_INFO";
  static final long MAX_KANSAS_CONSISTENCY_DELAY_MILLIS = 300000L;
  private final Context mAppContext;
  private final Clock mClock;
  private final Supplier<SharedPreferences> mMainPreferences;
  
  public DataBackendVersionStore(Context paramContext, Supplier<SharedPreferences> paramSupplier, Clock paramClock)
  {
    this.mAppContext = paramContext;
    this.mMainPreferences = paramSupplier;
    this.mClock = paramClock;
  }
  
  private void sendBroadcast(String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.setAction("com.google.android.apps.sidekick.DATA_BACKEND_VERSION_STORE");
    localIntent.putExtra("com.google.android.apps.sidekick.KANSAS_VERSION_INFO", paramString);
    localIntent.putExtra("reminder_updated", true);
    LocalBroadcastManager.getInstance(this.mAppContext).sendBroadcast(localIntent);
  }
  
  public Collection<Sidekick.MinimumDataVersion> getMinimumDataVersions()
  {
    SharedPreferences localSharedPreferences = (SharedPreferences)this.mMainPreferences.get();
    String str = localSharedPreferences.getString("com.google.android.apps.sidekick.KANSAS_VERSION_INFO", null);
    if (str != null)
    {
      if (localSharedPreferences.getLong("com.google.android.apps.sidekick.KANSAS_VERSION_INFO_EXPIRATION", 0L) > this.mClock.currentTimeMillis()) {
        return ImmutableSet.of(new Sidekick.MinimumDataVersion().setStorageBackend(1).setVersion(str));
      }
      localSharedPreferences.edit().remove("com.google.android.apps.sidekick.KANSAS_VERSION_INFO").remove("com.google.android.apps.sidekick.KANSAS_VERSION_INFO_EXPIRATION").apply();
    }
    return ImmutableSet.of();
  }
  
  public void requireDataVersions(Collection<Sidekick.MinimumDataVersion> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.MinimumDataVersion localMinimumDataVersion = (Sidekick.MinimumDataVersion)localIterator.next();
      if (localMinimumDataVersion.getStorageBackend() == 1)
      {
        long l = 300000L + this.mClock.currentTimeMillis();
        String str = localMinimumDataVersion.getVersion();
        ((SharedPreferences)this.mMainPreferences.get()).edit().putString("com.google.android.apps.sidekick.KANSAS_VERSION_INFO", str).putLong("com.google.android.apps.sidekick.KANSAS_VERSION_INFO_EXPIRATION", l).apply();
        sendBroadcast(str);
      }
    }
  }
  
  public void requireKansasVersionInfo(String paramString)
  {
    ((SharedPreferences)this.mMainPreferences.get()).edit().putString("com.google.android.apps.sidekick.KANSAS_VERSION_INFO", paramString).putLong("com.google.android.apps.sidekick.KANSAS_VERSION_INFO_EXPIRATION", 300000L + this.mClock.currentTimeMillis()).apply();
    sendBroadcast(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.DataBackendVersionStore
 * JD-Core Version:    0.7.0.1
 */