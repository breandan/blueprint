package com.google.android.libraries.tvdetect;

import java.util.Collection;
import java.util.Set;

public abstract interface DeviceCache
{
  public abstract void addDevice(Device paramDevice);
  
  public abstract Collection<Device> getAllDevicesForWifiNetwork(String paramString, Set<ProductType> paramSet, long paramLong);
  
  public abstract Device getDevice(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.DeviceCache
 * JD-Core Version:    0.7.0.1
 */