package com.google.android.sidekick.main.location;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.google.android.search.core.google.LocationSettings;

public class SystemLocationManagerInjectable
  implements LocationManagerInjectable
{
  private final LocationManager mLocationManager;
  private final LocationSettings mLocationSettings;
  
  public SystemLocationManagerInjectable(LocationManager paramLocationManager, LocationSettings paramLocationSettings)
  {
    this.mLocationManager = paramLocationManager;
    this.mLocationSettings = paramLocationSettings;
  }
  
  public Location getLastKnownLocation(String paramString)
  {
    if (!this.mLocationSettings.canUseLocationForGoogleApps())
    {
      Log.w("SystemLocationManagerInjectable", "Location access is not permitted");
      return null;
    }
    return this.mLocationManager.getLastKnownLocation(paramString);
  }
  
  public boolean isProviderEnabled(String paramString)
  {
    return this.mLocationManager.isProviderEnabled(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.SystemLocationManagerInjectable
 * JD-Core Version:    0.7.0.1
 */