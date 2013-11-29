package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;

public class fp
  extends dk<fn>
{
  private final fs<fn> tb = new c(null);
  private final fo th = new fo(paramContext, this.tb);
  private final String ti;
  
  public fp(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener, String paramString)
  {
    super(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener, new String[0]);
    this.ti = paramString;
  }
  
  protected fn S(IBinder paramIBinder)
  {
    return fn.a.R(paramIBinder);
  }
  
  protected void a(dp paramdp, dk.d paramd)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("client_name", this.ti);
    paramdp.e(paramd, 4023500, getContext().getPackageName(), localBundle);
  }
  
  protected String ag()
  {
    return "com.google.android.location.internal.GoogleLocationManagerService.START";
  }
  
  protected String ah()
  {
    return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
  }
  
  public void disconnect()
  {
    synchronized (this.th)
    {
      if (isConnected())
      {
        this.th.removeAllListeners();
        this.th.cq();
      }
      super.disconnect();
      return;
    }
  }
  
  public Location getLastLocation()
  {
    return this.th.getLastLocation();
  }
  
  public void removeLocationUpdates(PendingIntent paramPendingIntent)
  {
    this.th.removeLocationUpdates(paramPendingIntent);
  }
  
  public void requestLocationUpdates(LocationRequest paramLocationRequest, PendingIntent paramPendingIntent)
  {
    this.th.requestLocationUpdates(paramLocationRequest, paramPendingIntent);
  }
  
  private final class c
    implements fs<fn>
  {
    private c() {}
    
    public void bf()
    {
      fp.a(fp.this);
    }
    
    public fn cr()
    {
      return (fn)fp.b(fp.this);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fp
 * JD-Core Version:    0.7.0.1
 */