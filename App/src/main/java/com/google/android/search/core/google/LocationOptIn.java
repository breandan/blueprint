package com.google.android.search.core.google;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import com.google.android.gsf.UseLocationForServices;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class LocationOptIn
  implements LocationSettings
{
  private final Executor mBackgroundExecutor;
  private final Context mContext;
  private final Runnable mGetGoogleLocationSetting = new GetGoogleLocationSetting(null);
  private final Runnable mGetSystemLocationSetting = new GetSystemLocationSetting(null);
  private final ContentObserver mGoogleLocationObserver;
  private int mGoogleLocationSetting;
  private boolean mGoogleLocationSettingValid;
  private Boolean mIsGoogleSettingSupported;
  private final LocationManager mLocationManager;
  private final BroadcastReceiver mLocationProviderReceiver;
  private final Set<LocationSettings.Observer> mObservers = Sets.newHashSet();
  private final Object mSettingsLock = new Object();
  private boolean mSystemLocationEnabled;
  private boolean mSystemLocationSettingValid;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public LocationOptIn(final Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor)
  {
    this.mContext = paramContext;
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mBackgroundExecutor = paramExecutor;
    this.mBackgroundExecutor.execute(this.mGetGoogleLocationSetting);
    this.mLocationManager = ((LocationManager)this.mContext.getSystemService("location"));
    this.mLocationProviderReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        LocationOptIn.this.mBackgroundExecutor.execute(LocationOptIn.this.mGetSystemLocationSetting);
      }
    };
    this.mContext.registerReceiver(this.mLocationProviderReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    this.mGoogleLocationObserver = new ContentObserver(this.mUiThreadExecutor.getHandler())
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        LocationOptIn.this.mGetGoogleLocationSetting.run();
      }
    };
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        UseLocationForServices.registerUseLocationForServicesObserver(paramContext, LocationOptIn.this.mGoogleLocationObserver);
        LocationOptIn.this.mGetSystemLocationSetting.run();
      }
    });
  }
  
  private int getGoogleLocationSettingLocked()
  {
    ExtraPreconditions.checkHoldsLock(this.mSettingsLock);
    while (!this.mGoogleLocationSettingValid) {
      try
      {
        this.mSettingsLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.w("QSB.LocationOptIn", "Unexpected InterrupedException", localInterruptedException);
      }
    }
    return this.mGoogleLocationSetting;
  }
  
  private boolean isGoogleLocationEnabledLocked()
  {
    return isOn(getGoogleLocationSettingLocked());
  }
  
  private static boolean isNotSet(int paramInt)
  {
    return paramInt == 2;
  }
  
  private static boolean isOn(int paramInt)
  {
    return paramInt == 1;
  }
  
  private boolean isSystemLocationEnabledLocked()
  {
    ExtraPreconditions.checkHoldsLock(this.mSettingsLock);
    while (!this.mSystemLocationSettingValid) {
      try
      {
        this.mSettingsLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.w("QSB.LocationOptIn", "Unexpected InterrupedException", localInterruptedException);
      }
    }
    return this.mSystemLocationEnabled;
  }
  
  private void notifyObserversLocked(final boolean paramBoolean)
  {
    final HashSet localHashSet = new HashSet(this.mObservers);
    this.mUiThreadExecutor.execute(new Runnable()
    {
      public void run()
      {
        Iterator localIterator = localHashSet.iterator();
        while (localIterator.hasNext()) {
          ((LocationSettings.Observer)localIterator.next()).onUseLocationChanged(paramBoolean);
        }
      }
    });
  }
  
  private void updateSettingsLocked(boolean paramBoolean, int paramInt)
  {
    ExtraPreconditions.checkHoldsLock(this.mSettingsLock);
    int i;
    if ((this.mSystemLocationEnabled) && (isOn(this.mGoogleLocationSetting)))
    {
      i = 1;
      this.mSystemLocationEnabled = paramBoolean;
      this.mGoogleLocationSetting = paramInt;
      if ((!this.mSystemLocationEnabled) || (!isOn(this.mGoogleLocationSetting))) {
        break label74;
      }
    }
    label74:
    for (boolean bool = true;; bool = false)
    {
      if (i != bool) {
        notifyObserversLocked(bool);
      }
      return;
      i = 0;
      break;
    }
  }
  
  public void addUseLocationObserver(LocationSettings.Observer paramObserver)
  {
    synchronized (this.mSettingsLock)
    {
      this.mObservers.add(paramObserver);
      return;
    }
  }
  
  public boolean canUseLocationForGoogleApps()
  {
    if (!isGoogleSettingForAllApps()) {
      return true;
    }
    return canUseLocationForSearch();
  }
  
  public boolean canUseLocationForSearch()
  {
    for (;;)
    {
      synchronized (this.mSettingsLock)
      {
        if ((isSystemLocationEnabledLocked()) && (isGoogleLocationEnabledLocked()))
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public Intent getGoogleSettingIntent(String paramString)
  {
    Intent localIntent = new Intent("com.google.android.gsf.GOOGLE_APPS_LOCATION_SETTINGS");
    if (paramString != null) {
      localIntent.putExtra("account", paramString);
    }
    return localIntent;
  }
  
  @TargetApi(19)
  public int getKlpLocationMode()
  {
    if (Build.VERSION.SDK_INT < 19) {
      return -1;
    }
    try
    {
      int i = Settings.Secure.getInt(this.mContext.getContentResolver(), "location_mode");
      return i;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
      Log.w("QSB.LocationOptIn", localSettingNotFoundException);
    }
    return -1;
  }
  
  public boolean isGmsCoreLocationSettingAvailable()
  {
    return this.mContext.getPackageManager().resolveActivity(new Intent("com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS"), 65536) != null;
  }
  
  public boolean isGoogleLocationEnabled()
  {
    synchronized (this.mSettingsLock)
    {
      boolean bool = isGoogleLocationEnabledLocked();
      return bool;
    }
  }
  
  public boolean isGoogleSettingForAllApps()
  {
    if (this.mIsGoogleSettingSupported == null) {
      if (this.mContext.getPackageManager().resolveActivity(new Intent("com.google.android.gsf.GOOGLE_APPS_LOCATION_SETTINGS"), 65536) == null) {
        break label50;
      }
    }
    label50:
    for (boolean bool = true;; bool = false)
    {
      this.mIsGoogleSettingSupported = Boolean.valueOf(bool);
      return this.mIsGoogleSettingSupported.booleanValue();
    }
  }
  
  public boolean isSystemLocationEnabled()
  {
    synchronized (this.mSettingsLock)
    {
      boolean bool = isSystemLocationEnabledLocked();
      return bool;
    }
  }
  
  public void maybeShowLegacyOptIn()
  {
    if (isGoogleSettingForAllApps()) {
      return;
    }
    synchronized (this.mSettingsLock)
    {
      if ((!isSystemLocationEnabledLocked()) || (!isNotSet(getGoogleLocationSettingLocked()))) {
        return;
      }
    }
    Intent localIntent = new Intent("com.google.android.gsf.action.SET_USE_LOCATION_FOR_SERVICES");
    localIntent.setFlags(268435456);
    try
    {
      this.mContext.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.e("QSB.LocationOptIn", "Couldn't start location opt-in: " + localActivityNotFoundException);
    }
  }
  
  public void removeUseLocationObserver(LocationSettings.Observer paramObserver)
  {
    synchronized (this.mSettingsLock)
    {
      this.mObservers.remove(paramObserver);
      return;
    }
  }
  
  private class GetGoogleLocationSetting
    implements Runnable
  {
    private GetGoogleLocationSetting() {}
    
    public void run()
    {
      int i = UseLocationForServices.getUseLocationForServices(LocationOptIn.this.mContext);
      synchronized (LocationOptIn.this.mSettingsLock)
      {
        LocationOptIn.this.updateSettingsLocked(LocationOptIn.this.mSystemLocationEnabled, i);
        LocationOptIn.access$1302(LocationOptIn.this, true);
        LocationOptIn.this.mSettingsLock.notifyAll();
        return;
      }
    }
  }
  
  private class GetSystemLocationSetting
    implements Runnable
  {
    private GetSystemLocationSetting() {}
    
    public void run()
    {
      LocationManager localLocationManager = LocationOptIn.this.mLocationManager;
      boolean bool = false;
      if (localLocationManager != null)
      {
        Iterator localIterator = LocationOptIn.this.mLocationManager.getProviders(true).iterator();
        while (localIterator.hasNext()) {
          if (!"passive".equals((String)localIterator.next())) {
            bool = true;
          }
        }
      }
      synchronized (LocationOptIn.this.mSettingsLock)
      {
        LocationOptIn.this.updateSettingsLocked(bool, LocationOptIn.this.mGoogleLocationSetting);
        LocationOptIn.access$1002(LocationOptIn.this, true);
        LocationOptIn.this.mSettingsLock.notifyAll();
        return;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.LocationOptIn
 * JD-Core Version:    0.7.0.1
 */