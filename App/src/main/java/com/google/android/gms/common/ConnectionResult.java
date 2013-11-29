package com.google.android.gms.common;

import android.app.PendingIntent;
import com.google.android.gms.internal.dr;
import com.google.android.gms.internal.dr.a;

public final class ConnectionResult
{
  public static final ConnectionResult mv = new ConnectionResult(0, null);
  private final int kj;
  private final PendingIntent mPendingIntent;
  
  public ConnectionResult(int paramInt, PendingIntent paramPendingIntent)
  {
    this.kj = paramInt;
    this.mPendingIntent = paramPendingIntent;
  }
  
  private String aL()
  {
    switch (this.kj)
    {
    default: 
      return "unknown status code " + this.kj;
    case 0: 
      return "SUCCESS";
    case 1: 
      return "SERVICE_MISSING";
    case 2: 
      return "SERVICE_VERSION_UPDATE_REQUIRED";
    case 3: 
      return "SERVICE_DISABLED";
    case 4: 
      return "SIGN_IN_REQUIRED";
    case 5: 
      return "INVALID_ACCOUNT";
    case 6: 
      return "RESOLUTION_REQUIRED";
    case 7: 
      return "NETWORK_ERROR";
    case 8: 
      return "INTERNAL_ERROR";
    case 9: 
      return "SERVICE_INVALID";
    case 10: 
      return "DEVELOPER_ERROR";
    }
    return "LICENSE_CHECK_FAILED";
  }
  
  public int getErrorCode()
  {
    return this.kj;
  }
  
  public boolean isSuccess()
  {
    return this.kj == 0;
  }
  
  public String toString()
  {
    return dr.d(this).a("statusCode", aL()).a("resolution", this.mPendingIntent).toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.common.ConnectionResult
 * JD-Core Version:    0.7.0.1
 */