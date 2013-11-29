package com.google.android.search.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.google.android.velvet.VelvetStrictMode;

public class ChargingStateBroadcastReceiver
  extends BroadcastReceiver
  implements ChargingStateListener
{
  private ChargingStateListener.Observer mObserver;
  
  protected void handleChargingStateChanged(Context paramContext, boolean paramBoolean)
  {
    if (this.mObserver != null) {
      this.mObserver.onChargingStateChanged(paramBoolean);
    }
  }
  
  protected boolean isCharging(Intent paramIntent)
  {
    int i = paramIntent.getIntExtra("plugged", -1);
    return (i == 2) || (i == 1);
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramIntent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
      handleChargingStateChanged(paramContext, true);
    }
    while (!paramIntent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
      return;
    }
    handleChargingStateChanged(paramContext, false);
  }
  
  public void startListening(Context paramContext, ChargingStateListener.Observer paramObserver)
  {
    if (this.mObserver == null)
    {
      this.mObserver = paramObserver;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
      localIntentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
      paramContext.registerReceiver(this, localIntentFilter);
      handleChargingStateChanged(paramContext, isCharging(paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))));
      return;
    }
    VelvetStrictMode.logW("ChargingStateBroadcastReceiver", "Trying to start listening, but already listening for: " + this.mObserver, new Throwable());
  }
  
  public void stopListening(Context paramContext, ChargingStateListener.Observer paramObserver)
  {
    if (this.mObserver == paramObserver)
    {
      paramContext.unregisterReceiver(this);
      this.mObserver = null;
      return;
    }
    Log.w("ChargingStateBroadcastReceiver", "Trying to stop listening, but already stopped for: " + paramObserver);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ChargingStateBroadcastReceiver
 * JD-Core Version:    0.7.0.1
 */