package com.google.android.gms.location.reporting;

import android.accounts.Account;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.fv;
import java.io.IOException;

public class ReportingClient
  implements GooglePlayServicesClient
{
  private final fv tu;
  
  public ReportingClient(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener)
  {
    this.tu = new fv(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener, new String[0]);
  }
  
  public int cancelUpload(long paramLong)
    throws IOException
  {
    return this.tu.cancelUpload(paramLong);
  }
  
  public void connect()
  {
    this.tu.connect();
  }
  
  public void disconnect()
  {
    this.tu.disconnect();
  }
  
  public ReportingState getReportingState(Account paramAccount)
    throws IOException
  {
    return this.tu.getReportingState(paramAccount);
  }
  
  public UploadRequestResult requestUpload(UploadRequest paramUploadRequest)
    throws IOException
  {
    return this.tu.requestUpload(paramUploadRequest);
  }
  
  public int tryOptIn(Account paramAccount)
  {
    return this.tu.tryOptIn(paramAccount);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.ReportingClient
 * JD-Core Version:    0.7.0.1
 */