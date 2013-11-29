package com.google.android.gms.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.reporting.OptInResult;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.gms.location.reporting.UploadRequest;
import com.google.android.gms.location.reporting.UploadRequestResult;
import java.io.IOException;

public class fv
  extends dk<ft>
{
  public fv(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener, String... paramVarArgs)
  {
    super(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener, paramVarArgs);
  }
  
  protected ft U(IBinder paramIBinder)
  {
    return ft.a.T(paramIBinder);
  }
  
  protected void a(dp paramdp, dk.d paramd)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    paramdp.c(paramd, 4023500, getContext().getPackageName(), localBundle);
  }
  
  protected String ag()
  {
    return "com.google.android.gms.location.reporting.service.START";
  }
  
  protected String ah()
  {
    return "com.google.android.gms.location.reporting.internal.IReportingService";
  }
  
  public int cancelUpload(long paramLong)
    throws IOException
  {
    bf();
    try
    {
      int i = ((ft)bg()).i(paramLong);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      IOException localIOException = new IOException();
      localIOException.initCause(localRemoteException);
      throw localIOException;
    }
  }
  
  public ReportingState getReportingState(Account paramAccount)
    throws IOException
  {
    bf();
    try
    {
      ReportingState localReportingState = ((ft)bg()).getReportingState(paramAccount);
      return localReportingState;
    }
    catch (RemoteException localRemoteException)
    {
      IOException localIOException = new IOException();
      localIOException.initCause(localRemoteException);
      throw localIOException;
    }
  }
  
  public UploadRequestResult requestUpload(UploadRequest paramUploadRequest)
    throws IOException
  {
    bf();
    if (paramUploadRequest.getAccount() == null) {
      throw new IllegalArgumentException();
    }
    try
    {
      UploadRequestResult localUploadRequestResult = ((ft)bg()).requestUpload(paramUploadRequest);
      return localUploadRequestResult;
    }
    catch (RemoteException localRemoteException)
    {
      IOException localIOException = new IOException();
      localIOException.initCause(localRemoteException);
      throw localIOException;
    }
  }
  
  public int tryOptIn(Account paramAccount)
  {
    bf();
    try
    {
      int j = ((ft)bg()).tryOptIn(paramAccount);
      i = j;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        int i = 9;
      }
    }
    return OptInResult.sanitize(i);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fv
 * JD-Core Version:    0.7.0.1
 */