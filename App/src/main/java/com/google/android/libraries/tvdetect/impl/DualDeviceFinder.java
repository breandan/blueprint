package com.google.android.libraries.tvdetect.impl;

import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.DeviceCache;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.DeviceFinder.Callback;
import com.google.android.libraries.tvdetect.DeviceFinderOptions;
import com.google.android.libraries.tvdetect.ProductInfoService;
import com.google.android.libraries.tvdetect.net.NetworkAccessor;
import com.google.android.libraries.tvdetect.util.Clock;
import com.google.android.libraries.tvdetect.util.HttpFetcher;
import com.google.android.libraries.tvdetect.util.L;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

class DualDeviceFinder
  implements DeviceFinder
{
  private DeviceFinder.Callback callback;
  private final ConcurrentMap<String, Boolean> foundDeviceIds;
  private final DeviceFinder httpDeviceFinder;
  private AtomicInteger numFinishedFinders;
  private final DeviceFinder.Callback proxyCallback;
  private long searchStartMillis = 0L;
  private final DeviceFinder ssdpDeviceFinder;
  
  public DualDeviceFinder(NetworkAccessor paramNetworkAccessor, DeviceCache paramDeviceCache, ProductInfoService paramProductInfoService, HttpFetcher paramHttpFetcher, Clock paramClock)
  {
    this.httpDeviceFinder = new HttpCachedDeviceFinder(paramNetworkAccessor, paramDeviceCache, paramHttpFetcher, paramClock);
    this.ssdpDeviceFinder = new SsdpDeviceFinder(paramNetworkAccessor, paramDeviceCache, paramProductInfoService, paramHttpFetcher, paramClock);
    this.proxyCallback = newProxyCallback();
    this.foundDeviceIds = new ConcurrentHashMap();
    this.numFinishedFinders = new AtomicInteger(0);
  }
  
  private DeviceFinder.Callback newProxyCallback()
  {
    new DeviceFinder.Callback()
    {
      public void onDeviceFound(Device paramAnonymousDevice)
      {
        if (DualDeviceFinder.this.foundDeviceIds.putIfAbsent(paramAnonymousDevice.uuid, Boolean.valueOf(true)) == null) {
          try
          {
            if (DualDeviceFinder.this.callback != null) {
              DualDeviceFinder.this.callback.onDeviceFound(paramAnonymousDevice);
            }
            return;
          }
          finally {}
        }
      }
      
      public void onProgressChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        if (paramAnonymousInt1 == paramAnonymousInt2)
        {
          int i = DualDeviceFinder.this.numFinishedFinders.incrementAndGet();
          Object[] arrayOfObject = new Object[3];
          arrayOfObject[0] = Long.valueOf(System.currentTimeMillis() - DualDeviceFinder.this.searchStartMillis);
          arrayOfObject[1] = Integer.valueOf(i);
          arrayOfObject[2] = Integer.valueOf(2);
          L.ifmt("Device search progress changed at %d millis (%d/%d done).", arrayOfObject);
          try
          {
            if (DualDeviceFinder.this.callback != null)
            {
              DualDeviceFinder.this.callback.onProgressChanged(i, 2);
              if (i == 2) {
                DualDeviceFinder.access$102(DualDeviceFinder.this, null);
              }
            }
            return;
          }
          finally {}
        }
      }
    };
  }
  
  private boolean setSearchStartMillisOnce(long paramLong)
  {
    try
    {
      if (this.searchStartMillis == 0L)
      {
        this.searchStartMillis = paramLong;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public boolean search(DeviceFinder.Callback paramCallback, DeviceFinderOptions paramDeviceFinderOptions)
  {
    L.i("Starting dual device search");
    if (!setSearchStartMillisOnce(System.currentTimeMillis()))
    {
      L.e("Dual search started more than once.");
      return false;
    }
    this.callback = paramCallback;
    this.httpDeviceFinder.search(this.proxyCallback, paramDeviceFinderOptions);
    this.ssdpDeviceFinder.search(this.proxyCallback, paramDeviceFinderOptions);
    return true;
  }
  
  public void stopSearch()
  {
    L.i("Stopping dual device search");
    try
    {
      this.callback = null;
      this.httpDeviceFinder.stopSearch();
      this.ssdpDeviceFinder.stopSearch();
      return;
    }
    finally {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.DualDeviceFinder
 * JD-Core Version:    0.7.0.1
 */