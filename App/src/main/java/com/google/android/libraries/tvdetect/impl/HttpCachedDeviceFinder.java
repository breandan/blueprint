package com.google.android.libraries.tvdetect.impl;

import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.DeviceCache;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.DeviceFinder.Callback;
import com.google.android.libraries.tvdetect.DeviceFinderOptions;
import com.google.android.libraries.tvdetect.net.NetworkAccessor;
import com.google.android.libraries.tvdetect.net.WifiNetwork;
import com.google.android.libraries.tvdetect.util.Clock;
import com.google.android.libraries.tvdetect.util.DeviceUtil;
import com.google.android.libraries.tvdetect.util.HttpFetcher;
import com.google.android.libraries.tvdetect.util.L;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class HttpCachedDeviceFinder
  implements DeviceFinder
{
  private DeviceFinder.Callback callback;
  private final Clock clock;
  private final DeviceCache deviceCache;
  private ExecutorService executor;
  private final HttpFetcher httpFetcher;
  private final Object lock = new Object();
  private final NetworkAccessor networkAccessor;
  private DeviceFinderOptions options;
  private long searchStartMillis = 0L;
  
  public HttpCachedDeviceFinder(NetworkAccessor paramNetworkAccessor, DeviceCache paramDeviceCache, HttpFetcher paramHttpFetcher, Clock paramClock)
  {
    this.networkAccessor = paramNetworkAccessor;
    this.deviceCache = paramDeviceCache;
    this.httpFetcher = paramHttpFetcher;
    this.clock = paramClock;
  }
  
  private static ExecutorService createExecutor()
  {
    return new ThreadPoolExecutor(2, 5, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(10));
  }
  
  private void deviceFound(Device paramDevice)
  {
    synchronized (this.lock)
    {
      if (this.callback != null) {
        this.callback.onDeviceFound(paramDevice);
      }
      return;
    }
  }
  
  private void executeSearch()
  {
    long l1 = this.clock.getCurrentTimeMillis();
    WifiNetwork localWifiNetwork = this.networkAccessor.getActiveWifiNetwork(false);
    if (localWifiNetwork == null) {
      L.i("No active network for device discovery");
    }
    for (;;)
    {
      return;
      long l2 = l1 - DeviceUtil.MAX_VALID_PRODUCT_INFO_AGE_MILLIS;
      Collection localCollection = this.deviceCache.getAllDevicesForWifiNetwork(localWifiNetwork.getBssid(), this.options.wantedProductTypes, l2);
      if (localCollection.size() == 0)
      {
        L.i("No pre-cached devices on this network");
        return;
      }
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(localCollection.size());
      L.ifmt("%d devices to check on network", arrayOfObject);
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator1 = localCollection.iterator();
      while (localIterator1.hasNext())
      {
        final Device localDevice = (Device)localIterator1.next();
        if (localDevice.deviceDescriptionUrl == null)
        {
          L.w("Cached device has no DDD URL");
        }
        else
        {
          ExecutorService localExecutorService = this.executor;
          Runnable local2 = new Runnable()
          {
            public void run()
            {
              if (!DeviceUtil.deviceExistsAtDeviceDescriptionUrl(HttpCachedDeviceFinder.this.httpFetcher, localDevice))
              {
                L.i("Could not read DDD for cached device (probably turned off)");
                return;
              }
              Object[] arrayOfObject = new Object[1];
              arrayOfObject[0] = Long.valueOf(HttpCachedDeviceFinder.this.clock.getCurrentTimeMillis() - HttpCachedDeviceFinder.this.searchStartMillis);
              L.ifmt("HTTP found device %d millis after start of search", arrayOfObject);
              HttpCachedDeviceFinder.this.deviceFound(localDevice);
            }
          };
          localArrayList.add(localExecutorService.submit(local2));
        }
      }
      long l3 = l1 + TimeUnit.SECONDS.toMillis(5L);
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        Future localFuture = (Future)localIterator2.next();
        long l4 = Math.max(0L, l3 - this.clock.getCurrentTimeMillis());
        try
        {
          localFuture.get(l4, TimeUnit.SECONDS);
        }
        catch (InterruptedException localInterruptedException)
        {
          L.w("HTTP device response read task interrupted");
        }
        catch (ExecutionException localExecutionException)
        {
          L.w("HTTP device response read task had error", localExecutionException);
        }
        catch (TimeoutException localTimeoutException)
        {
          localFuture.cancel(true);
        }
      }
    }
  }
  
  private void searchFinished()
  {
    synchronized (this.lock)
    {
      if (this.callback != null)
      {
        this.callback.onProgressChanged(1, 1);
        this.callback = null;
      }
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(this.clock.getCurrentTimeMillis() - this.searchStartMillis);
      L.ifmt("HTTP device search finished in %d millis", arrayOfObject);
      this.executor.shutdownNow();
      return;
    }
  }
  
  private boolean setSearchStartMillisOnce(long paramLong)
  {
    synchronized (this.lock)
    {
      if (this.searchStartMillis == 0L)
      {
        this.searchStartMillis = paramLong;
        return true;
      }
      return false;
    }
  }
  
  public boolean search(DeviceFinder.Callback paramCallback, DeviceFinderOptions paramDeviceFinderOptions)
  {
    L.i("Starting HTTP device search");
    if (!setSearchStartMillisOnce(this.clock.getCurrentTimeMillis()))
    {
      L.e("HTTP search started more than once.");
      return false;
    }
    this.executor = createExecutor();
    this.callback = paramCallback;
    this.options = paramDeviceFinderOptions;
    this.executor.submit(new Runnable()
    {
      public void run()
      {
        try
        {
          HttpCachedDeviceFinder.this.executeSearch();
          return;
        }
        finally
        {
          HttpCachedDeviceFinder.this.searchFinished();
        }
      }
    });
    return true;
  }
  
  public void stopSearch()
  {
    L.i("Stopping HTTP device search");
    synchronized (this.lock)
    {
      this.callback = null;
      this.executor.shutdownNow();
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.HttpCachedDeviceFinder
 * JD-Core Version:    0.7.0.1
 */