package com.google.android.sidekick.main.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.velvet.VelvetServices;

public class AndroidLocationReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("com.google.android.apps.sidekick.android_location_broadcast".equals(paramIntent.getAction()))
    {
      LocationOracle localLocationOracle = VelvetServices.get().getSidekickInjector().getLocationOracle();
      Parcelable localParcelable = paramIntent.getParcelableExtra("location");
      if ((localParcelable != null) && ((localParcelable instanceof Location)) && ((localLocationOracle instanceof LocationOracleImpl))) {
        localLocationOracle.postLocation((Location)localParcelable);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.AndroidLocationReceiver
 * JD-Core Version:    0.7.0.1
 */