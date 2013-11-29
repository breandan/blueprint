package com.google.analytics.tracking.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

class GANetworkReceiver
  extends BroadcastReceiver
{
  private final ServiceManager mManager;
  
  GANetworkReceiver(ServiceManager paramServiceManager)
  {
    this.mManager = paramServiceManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    ServiceManager localServiceManager;
    if ("android.net.conn.CONNECTIVITY_CHANGE".equals(paramIntent.getAction()))
    {
      Bundle localBundle = paramIntent.getExtras();
      Boolean localBoolean = Boolean.FALSE;
      if (localBundle != null) {
        localBoolean = Boolean.valueOf(paramIntent.getExtras().getBoolean("noConnectivity"));
      }
      localServiceManager = this.mManager;
      if (localBoolean.booleanValue()) {
        break label67;
      }
    }
    label67:
    for (boolean bool = true;; bool = false)
    {
      localServiceManager.updateConnectivityStatus(bool);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.GANetworkReceiver
 * JD-Core Version:    0.7.0.1
 */