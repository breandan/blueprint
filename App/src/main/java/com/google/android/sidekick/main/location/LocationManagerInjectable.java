package com.google.android.sidekick.main.location;

import android.location.Location;

public abstract interface LocationManagerInjectable
{
  public abstract Location getLastKnownLocation(String paramString);
  
  public abstract boolean isProviderEnabled(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationManagerInjectable
 * JD-Core Version:    0.7.0.1
 */