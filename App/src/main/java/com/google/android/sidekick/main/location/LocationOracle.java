package com.google.android.sidekick.main.location;

import android.location.Location;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract interface LocationOracle
{
  public abstract void addLightweightGeofencer(LightweightGeofencer paramLightweightGeofencer);
  
  @Nullable
  public abstract Location blockingUpdateBestLocation();
  
  @Nullable
  public abstract Location getBestLocation();
  
  @Nonnull
  public abstract List<Location> getBestLocations();
  
  public abstract boolean hasLocation();
  
  public abstract RunningLock newRunningLock(String paramString);
  
  public abstract void postLocation(Location paramLocation);
  
  public abstract void requestRecentLocation(long paramLong);
  
  public static abstract interface LightweightGeofencer
  {
    public abstract void onLocationChanged(@Nullable Location paramLocation1, @Nullable Location paramLocation2);
  }
  
  public static abstract interface RunningLock
  {
    public abstract void acquire();
    
    public abstract void release();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationOracle
 * JD-Core Version:    0.7.0.1
 */