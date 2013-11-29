package com.google.android.libraries.tvdetect.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.Device.BuildException;
import com.google.android.libraries.tvdetect.DeviceCache;
import com.google.android.libraries.tvdetect.ProductInfo;
import com.google.android.libraries.tvdetect.ProductType;
import com.google.android.libraries.tvdetect.util.L;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class SharedPrefDeviceCache
  implements DeviceCache
{
  private final Object lock = new Object();
  private final SharedPreferences networkBssidToDeviceUuids;
  private final SharedPreferences uuidToDevice;
  
  private SharedPrefDeviceCache(Context paramContext)
  {
    this.uuidToDevice = paramContext.getSharedPreferences("TvDetectUuidToDeviceV19", 0);
    this.networkBssidToDeviceUuids = paramContext.getSharedPreferences("TvDetectNetworkBssidToDeviceUuidsV19", 0);
  }
  
  static String convertSetToString(Set<String> paramSet)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (localStringBuilder.length() != 0) {
        localStringBuilder.append("||");
      }
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
  }
  
  public static SharedPrefDeviceCache create(Context paramContext)
  {
    return new SharedPrefDeviceCache(paramContext);
  }
  
  static Set<String> stringToSet(String paramString)
  {
    Object localObject;
    if (paramString.length() == 0) {
      localObject = Collections.emptySet();
    }
    for (;;)
    {
      return localObject;
      localObject = new HashSet();
      String[] arrayOfString = paramString.split("\\|\\|");
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        ((Set)localObject).add(arrayOfString[j]);
      }
    }
  }
  
  public void addDevice(Device paramDevice)
  {
    if (paramDevice.networkBssid == null) {
      L.e("Device with no nbssid");
    }
    synchronized (this.lock)
    {
      Device localDevice = getDevice(paramDevice.uuid);
      String str;
      if (localDevice != null)
      {
        boolean bool1 = localDevice.equals(paramDevice);
        str = null;
        if (bool1) {}
      }
      else
      {
        str = null;
        if (localDevice != null)
        {
          boolean bool2 = localDevice.networkBssid.equals(paramDevice.networkBssid);
          str = null;
          if (!bool2) {
            str = localDevice.networkBssid;
          }
        }
        SharedPreferences.Editor localEditor2 = this.uuidToDevice.edit();
        localEditor2.putString(paramDevice.uuid, paramDevice.serializeToString());
        localEditor2.commit();
      }
      Set localSet1 = stringToSet(this.networkBssidToDeviceUuids.getString(paramDevice.networkBssid, ""));
      if ((str != null) || (!localSet1.contains(paramDevice.uuid)))
      {
        SharedPreferences.Editor localEditor1 = this.networkBssidToDeviceUuids.edit();
        if (str != null)
        {
          Set localSet2 = stringToSet(this.networkBssidToDeviceUuids.getString(str, ""));
          if (localSet2.contains(paramDevice.uuid))
          {
            localSet2.remove(paramDevice.uuid);
            localEditor1.putString(str, convertSetToString(localSet2));
          }
        }
        if (!localSet1.contains(paramDevice.uuid))
        {
          HashSet localHashSet = new HashSet();
          localHashSet.addAll(localSet1);
          localHashSet.add(paramDevice.uuid);
          localEditor1.putString(paramDevice.networkBssid, convertSetToString(localHashSet));
        }
        localEditor1.commit();
      }
      return;
    }
  }
  
  public Collection<Device> getAllDevicesForWifiNetwork(String paramString, Set<ProductType> paramSet, long paramLong)
  {
    Set localSet = stringToSet(this.networkBssidToDeviceUuids.getString(paramString, ""));
    Object localObject;
    if (localSet.isEmpty()) {
      localObject = Collections.emptyList();
    }
    do
    {
      return localObject;
      localObject = null;
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        Device localDevice = getDevice((String)localIterator.next());
        if ((localDevice != null) && (localDevice.productInfo != null) && (paramSet.contains(localDevice.productInfo.type)) && (localDevice.productInfoUpdateTimeMillis > paramLong))
        {
          if (localObject == null) {
            localObject = new ArrayList();
          }
          ((List)localObject).add(localDevice);
        }
      }
    } while (localObject != null);
    return Collections.emptyList();
  }
  
  public Device getDevice(String paramString)
  {
    String str = this.uuidToDevice.getString(paramString, "");
    if (str.isEmpty()) {
      return null;
    }
    try
    {
      Device localDevice = Device.deserializeFromString(str);
      return localDevice;
    }
    catch (Device.BuildException localBuildException)
    {
      L.e("Cached device was invalid");
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.SharedPrefDeviceCache
 * JD-Core Version:    0.7.0.1
 */