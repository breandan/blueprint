package com.google.android.gms.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.fp;

public class LocationClient
  implements GooglePlayServicesClient
{
  private final fp sH;
  
  public LocationClient(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener)
  {
    this.sH = new fp(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener, "location");
  }
  
  public void connect()
  {
    this.sH.connect();
  }
  
  public void disconnect()
  {
    this.sH.disconnect();
  }
  
  public Location getLastLocation()
  {
    return this.sH.getLastLocation();
  }
  
  public void removeLocationUpdates(PendingIntent paramPendingIntent)
  {
    this.sH.removeLocationUpdates(paramPendingIntent);
  }
  
  public void requestLocationUpdates(LocationRequest paramLocationRequest, PendingIntent paramPendingIntent)
  {
    this.sH.requestLocationUpdates(paramLocationRequest, paramPendingIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.LocationClient
 * JD-Core Version:    0.7.0.1
 */