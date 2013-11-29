package com.google.android.sidekick.shared.util;

import android.location.Location;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.TimestampedLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class LocationUtilities
{
  @Nullable
  public static Sidekick.Location androidLocationToSidekickLocation(@Nullable Location paramLocation)
  {
    if (paramLocation == null) {
      return null;
    }
    Sidekick.Location localLocation = new Sidekick.Location();
    localLocation.setLat(paramLocation.getLatitude());
    localLocation.setLng(paramLocation.getLongitude());
    return localLocation;
  }
  
  public static boolean areLocationsEqual(Location paramLocation1, Location paramLocation2)
  {
    return (paramLocation1.getLatitude() == paramLocation2.getLatitude()) && (paramLocation1.getLongitude() == paramLocation2.getLongitude()) && (paramLocation1.getAccuracy() == paramLocation2.getAccuracy());
  }
  
  public static float distanceBetween(Location paramLocation1, Location paramLocation2)
  {
    float[] arrayOfFloat = new float[1];
    Location.distanceBetween(paramLocation1.getLatitude(), paramLocation1.getLongitude(), paramLocation2.getLatitude(), paramLocation2.getLongitude(), arrayOfFloat);
    return arrayOfFloat[0];
  }
  
  public static DistanceUnit getLocalDistanceUnits()
  {
    Locale localLocale = Locale.getDefault();
    if ((Locale.UK.equals(localLocale)) || (Locale.US.equals(localLocale))) {
      return DistanceUnit.MILES;
    }
    return DistanceUnit.KILOMETERS;
  }
  
  public static Sidekick.TimestampedLocation locationToTimestampedLocation(Location paramLocation)
  {
    Sidekick.TimestampedLocation localTimestampedLocation = new Sidekick.TimestampedLocation().setLocation(new Sidekick.Location().setLat(paramLocation.getLatitude()).setLng(paramLocation.getLongitude())).setTimestampSeconds(paramLocation.getTime() / 1000L).setProvider(paramLocation.getProvider());
    if (paramLocation.hasAccuracy()) {
      localTimestampedLocation.setAccuracyMeters((int)paramLocation.getAccuracy());
    }
    return localTimestampedLocation;
  }
  
  public static List<Sidekick.TimestampedLocation> locationsToTimestampedLocations(List<Location> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Location localLocation = (Location)localIterator.next();
      if (localLocation == null) {}
      for (Object localObject = null;; localObject = locationToTimestampedLocation(localLocation))
      {
        localArrayList.add(localObject);
        break;
      }
    }
    return localArrayList;
  }
  
  @Nullable
  public static Location sidekickLocationToAndroidLocation(@Nullable Sidekick.Location paramLocation)
  {
    if (paramLocation == null) {
      return null;
    }
    Location localLocation = new Location("unknown");
    localLocation.setLatitude(paramLocation.getLat());
    localLocation.setLongitude(paramLocation.getLng());
    return localLocation;
  }
  
  public static double toLocalDistanceUnits(int paramInt)
  {
    if (getLocalDistanceUnits() == DistanceUnit.KILOMETERS) {}
    for (double d = 1000.0D;; d = 1609.3440000000001D) {
      return 1.0D / d * paramInt;
    }
  }
  
  public static enum DistanceUnit
  {
    static
    {
      DistanceUnit[] arrayOfDistanceUnit = new DistanceUnit[2];
      arrayOfDistanceUnit[0] = KILOMETERS;
      arrayOfDistanceUnit[1] = MILES;
      $VALUES = arrayOfDistanceUnit;
    }
    
    private DistanceUnit() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.LocationUtilities
 * JD-Core Version:    0.7.0.1
 */