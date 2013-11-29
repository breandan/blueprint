package com.google.android.sidekick.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.velvet.VelvetServices;

public class NearbyBeaconReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    if ((localBundle != null) && (localBundle.containsKey("data")))
    {
      byte[] arrayOfByte = paramIntent.getByteArrayExtra("data");
      SidekickInjector localSidekickInjector = VelvetServices.get().getSidekickInjector();
      localSidekickInjector.getSensorSignalsOracle().setBeacondData(arrayOfByte);
      if ((localBundle.containsKey("refresh_hint")) && (localBundle.getBoolean("refresh_hint"))) {
        localSidekickInjector.getEntryProvider().invalidate();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.NearbyBeaconReceiver
 * JD-Core Version:    0.7.0.1
 */