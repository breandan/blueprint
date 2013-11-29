package com.google.android.sidekick.shared.util;

import com.google.geo.sidekick.Sidekick.CarRentalEntry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class CarRentalEntryUtil
{
  @Nullable
  public static Sidekick.Location getCarRentalLocation(Sidekick.CarRentalEntry paramCarRentalEntry)
  {
    if ((paramCarRentalEntry.getType() == 1) && (paramCarRentalEntry.hasPickupLocation())) {
      return paramCarRentalEntry.getPickupLocation();
    }
    if ((paramCarRentalEntry.getType() == 2) && (paramCarRentalEntry.hasReturnLocation())) {
      return paramCarRentalEntry.getReturnLocation();
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.CarRentalEntryUtil
 * JD-Core Version:    0.7.0.1
 */