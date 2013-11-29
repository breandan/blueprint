package com.google.android.sidekick.shared.util;

import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class FlightStatusEntryUtil
{
  @Nullable
  public static Sidekick.FlightStatusEntry.Airport getNavigateAirport(Sidekick.FlightStatusEntry paramFlightStatusEntry)
  {
    Iterator localIterator = paramFlightStatusEntry.getFlightList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.FlightStatusEntry.Flight localFlight = (Sidekick.FlightStatusEntry.Flight)localIterator.next();
      if ((localFlight.hasDepartureAirport()) && (localFlight.getDepartureAirport().hasRoute()) && (localFlight.getDepartureAirport().hasLocation())) {
        return localFlight.getDepartureAirport();
      }
      if ((localFlight.hasArrivalAirport()) && (localFlight.getArrivalAirport().hasRoute()) && (localFlight.getArrivalAirport().hasLocation())) {
        return localFlight.getArrivalAirport();
      }
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.FlightStatusEntryUtil
 * JD-Core Version:    0.7.0.1
 */