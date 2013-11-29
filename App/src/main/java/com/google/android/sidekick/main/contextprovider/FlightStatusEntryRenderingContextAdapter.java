package com.google.android.sidekick.main.contextprovider;

import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.FlightStatusEntryUtil;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;

public class FlightStatusEntryRenderingContextAdapter
  implements EntryRenderingContextAdapter
{
  private final Sidekick.FlightStatusEntry mFlightStatusEntry;
  
  public FlightStatusEntryRenderingContextAdapter(Sidekick.FlightStatusEntry paramFlightStatusEntry)
  {
    this.mFlightStatusEntry = paramFlightStatusEntry;
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    Sidekick.FlightStatusEntry.Airport localAirport = FlightStatusEntryUtil.getNavigateAirport(this.mFlightStatusEntry);
    if ((localAirport != null) && (localAirport.hasLocation())) {
      paramCardRenderingContextProviders.getNavigationContextProvider().addNavigationContext(paramCardRenderingContext, localAirport.getLocation(), localAirport.getRoute());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.FlightStatusEntryRenderingContextAdapter
 * JD-Core Version:    0.7.0.1
 */