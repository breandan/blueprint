package com.google.android.velvet.location;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.search.core.GmsClientWrapper;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.util.Tag;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nullable;

public class GmsLocationProvider
  extends GmsClientWrapper<LocationClient>
{
  private static final String TAG = Tag.getTag(GmsLocationProvider.class);
  private final Context mContext;
  private AtomicLong mLocationUpdateIntervalMs = new AtomicLong(0L);
  
  public GmsLocationProvider(Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor)
  {
    super(TAG, paramContext, paramScheduledSingleThreadedExecutor, paramExecutor, 30000L);
    this.mContext = paramContext;
  }
  
  protected LocationClient createClient(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener)
  {
    return new LocationClient(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener);
  }
  
  public GmsClientWrapper.GmsFuture<Location> getLastLocation()
  {
    invoke(new Callable()
    {
      public Location call()
        throws Exception
      {
        return ((LocationClient)GmsLocationProvider.this.getClient()).getLastLocation();
      }
    });
  }
  
  @Nullable
  PendingIntent getLocationBroadcastIntent(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 134217728;; i = 536870912)
    {
      Intent localIntent = new Intent("com.google.android.velvet.location.GMS_CORE_LOCATION");
      localIntent.setComponent(new ComponentName(this.mContext, LocationReceiver.class));
      return PendingIntent.getBroadcast(this.mContext, 0, localIntent, i);
    }
  }
  
  public GmsClientWrapper.GmsFuture<Void> startBackgroundUpdates(final long paramLong)
  {
    if (this.mLocationUpdateIntervalMs.get() == paramLong) {
      return GmsClientWrapper.GmsFuture.immediateFuture(null);
    }
    final LocationRequest localLocationRequest = LocationRequest.create().setInterval(paramLong).setFastestInterval(paramLong);
    localLocationRequest.setPriority(102);
    invoke(new Callable()
    {
      public Void call()
        throws Exception
      {
        ((LocationClient)GmsLocationProvider.this.getClient()).requestLocationUpdates(localLocationRequest, this.val$callbackIntent);
        GmsLocationProvider.this.mLocationUpdateIntervalMs.set(paramLong);
        return null;
      }
    });
  }
  
  public GmsClientWrapper.GmsFuture<Void> stopBackgroundUpdates()
  {
    invoke(new Callable()
    {
      public Void call()
        throws Exception
      {
        ((LocationClient)GmsLocationProvider.this.getClient()).removeLocationUpdates(this.val$callbackIntent);
        GmsLocationProvider.this.mLocationUpdateIntervalMs.set(0L);
        return null;
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.location.GmsLocationProvider
 * JD-Core Version:    0.7.0.1
 */