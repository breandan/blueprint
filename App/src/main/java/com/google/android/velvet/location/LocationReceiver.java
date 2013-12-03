package com.google.android.velvet.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;

public class LocationReceiver
        extends BroadcastReceiver {
    private static final String TAG = Tag.getTag(LocationReceiver.class);
    private final VelvetServices mServices;

    public LocationReceiver() {
        this(VelvetServices.get());
    }

    LocationReceiver(VelvetServices paramVelvetServices) {
        this.mServices = paramVelvetServices;
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        if (!"com.google.android.velvet.location.GMS_CORE_LOCATION".equals(paramIntent.getAction())) {
            return;
        }
        Parcelable localParcelable = paramIntent.getParcelableExtra("com.google.android.location.LOCATION");
        if ((localParcelable == null) || (!(localParcelable instanceof Location))) {
            Log.e(TAG, "Received bad location: " + localParcelable);
            return;
        }
        Location localLocation = (Location) localParcelable;
        this.mServices.getLocationOracle().postLocation(localLocation);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.location.LocationReceiver

 * JD-Core Version:    0.7.0.1

 */