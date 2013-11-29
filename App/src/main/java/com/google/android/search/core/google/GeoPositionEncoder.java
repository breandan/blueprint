package com.google.android.search.core.google;

import android.location.Location;

public class GeoPositionEncoder
{
  public static String encodeLocation(Location paramLocation)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramLocation.getLatitude()).append(";").append(paramLocation.getLongitude());
    if (paramLocation.hasAccuracy()) {
      localStringBuilder.append(" epu=").append((int)paramLocation.getAccuracy());
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.GeoPositionEncoder
 * JD-Core Version:    0.7.0.1
 */