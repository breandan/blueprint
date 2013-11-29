package com.google.android.search.core.google;

import android.location.Location;
import android.util.Base64;
import com.google.common.base.Joiner;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import location.unified.LocationDescriptorProto.LatLng;
import location.unified.LocationDescriptorProto.LocationDescriptor;

public class XGeoEncoder
{
  public static String createHeader(boolean paramBoolean, @Nullable Location paramLocation1, @Nullable Location paramLocation2)
  {
    if ((paramLocation1 == null) && (paramLocation2 == null)) {
      return null;
    }
    if (paramBoolean)
    {
      String str1 = encodeLocation(paramLocation1, 1, 12);
      String str2 = encodeLocation(paramLocation2, 4, 1);
      return Joiner.on(" ").skipNulls().join(str1, str2, new Object[0]);
    }
    if (paramLocation2 != null) {}
    for (Location localLocation = paramLocation2;; localLocation = paramLocation1) {
      return encodeLocation(localLocation, 1, 12);
    }
  }
  
  @Nullable
  private static String encodeLocation(@Nullable Location paramLocation, int paramInt1, int paramInt2)
  {
    if (paramLocation == null) {
      return null;
    }
    LocationDescriptorProto.LocationDescriptor localLocationDescriptor = new LocationDescriptorProto.LocationDescriptor();
    LocationDescriptorProto.LatLng localLatLng = new LocationDescriptorProto.LatLng();
    localLocationDescriptor.setRole(paramInt1);
    localLocationDescriptor.setProducer(paramInt2);
    localLatLng.setLatitudeE7((int)Math.round(10000000.0D * paramLocation.getLatitude()));
    localLatLng.setLongitudeE7((int)Math.round(10000000.0D * paramLocation.getLongitude()));
    localLocationDescriptor.setLatlng(localLatLng);
    long l = paramLocation.getTime();
    localLocationDescriptor.setTimestamp(TimeUnit.MILLISECONDS.toMicros(l));
    if (paramLocation.hasAccuracy()) {
      localLocationDescriptor.setRadius((float)(1000.0D * paramLocation.getAccuracy()));
    }
    return "w " + Base64.encodeToString(localLocationDescriptor.toByteArray(), 10);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.XGeoEncoder
 * JD-Core Version:    0.7.0.1
 */