package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

public class cw
  extends dk<cx>
{
  public cw(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener)
  {
    super(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener, new String[0]);
  }
  
  protected void a(dp paramdp, dk.d paramd)
    throws RemoteException
  {
    paramdp.a(paramd, 4023500, getContext().getPackageName());
  }
  
  protected String ag()
  {
    return "com.google.android.gms.icing.INDEX_SERVICE";
  }
  
  protected String ah()
  {
    return "com.google.android.gms.appdatasearch.internal.IAppDataSearch";
  }
  
  public cx getSearchService()
    throws RemoteException
  {
    try
    {
      cx localcx = (cx)bg();
      return localcx;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      RemoteException localRemoteException = new RemoteException("Error getting service");
      localRemoteException.initCause(localIllegalStateException);
      throw localRemoteException;
    }
  }
  
  protected cx s(IBinder paramIBinder)
  {
    return cx.a.t(paramIBinder);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.cw
 * JD-Core Version:    0.7.0.1
 */