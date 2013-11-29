package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.a.a;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class fo
{
  private final Context mContext;
  private final fs<fn> tb;
  private ContentProviderClient tc = null;
  private boolean td = false;
  private HashMap te = new HashMap();
  
  public fo(Context paramContext, fs<fn> paramfs)
  {
    this.mContext = paramContext;
    this.tb = paramfs;
  }
  
  public void cq()
  {
    if (this.td) {
      setMockMode(false);
    }
  }
  
  public Location getLastLocation()
  {
    this.tb.bf();
    try
    {
      Location localLocation = ((fn)this.tb.bg()).cp();
      return localLocation;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }
  
  public void removeAllListeners()
  {
    try
    {
      synchronized (this.te)
      {
        Iterator localIterator = this.te.values().iterator();
        while (localIterator.hasNext())
        {
          b localb = (b)localIterator.next();
          if (localb != null) {
            ((fn)this.tb.bg()).a(localb);
          }
        }
      }
      this.te.clear();
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }
  
  public void removeLocationUpdates(PendingIntent paramPendingIntent)
  {
    this.tb.bf();
    try
    {
      ((fn)this.tb.bg()).a(paramPendingIntent);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }
  
  public void requestLocationUpdates(LocationRequest paramLocationRequest, PendingIntent paramPendingIntent)
  {
    this.tb.bf();
    try
    {
      ((fn)this.tb.bg()).a(paramLocationRequest, paramPendingIntent);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }
  
  public void setMockMode(boolean paramBoolean)
  {
    this.tb.bf();
    try
    {
      ((fn)this.tb.bg()).setMockMode(paramBoolean);
      this.td = paramBoolean;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }
  
  private static class b
    extends a.a
  {
    private Handler tg;
    
    public void onLocationChanged(Location paramLocation)
    {
      if (this.tg == null)
      {
        Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
        return;
      }
      Message localMessage = Message.obtain();
      localMessage.what = 1;
      localMessage.obj = paramLocation;
      this.tg.sendMessage(localMessage);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fo
 * JD-Core Version:    0.7.0.1
 */