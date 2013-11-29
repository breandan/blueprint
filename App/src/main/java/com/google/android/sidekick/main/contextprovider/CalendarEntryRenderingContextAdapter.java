package com.google.android.sidekick.main.contextprovider;

import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;

public class CalendarEntryRenderingContextAdapter
  implements EntryRenderingContextAdapter
{
  private final CalendarDataProvider mCalendarDataProvider;
  private final Sidekick.CalendarEntry mCalendarEntry;
  
  public CalendarEntryRenderingContextAdapter(Sidekick.Entry paramEntry, CalendarDataProvider paramCalendarDataProvider)
  {
    this.mCalendarEntry = paramEntry.getCalendarEntry();
    this.mCalendarDataProvider = paramCalendarDataProvider;
  }
  
  private Sidekick.Location getLocation()
  {
    Calendar.CalendarData localCalendarData = this.mCalendarDataProvider.getCalendarDataByServerHash(this.mCalendarEntry.getHash());
    return CalendarDataUtil.getCalendarLocation(this.mCalendarEntry, localCalendarData);
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    NavigationContextProvider localNavigationContextProvider = paramCardRenderingContextProviders.getNavigationContextProvider();
    Sidekick.Location localLocation = getLocation();
    if (this.mCalendarEntry.hasRoute()) {}
    for (Sidekick.CommuteSummary localCommuteSummary = this.mCalendarEntry.getRoute();; localCommuteSummary = null)
    {
      localNavigationContextProvider.addNavigationContext(paramCardRenderingContext, localLocation, localCommuteSummary);
      paramCardRenderingContextProviders.getCalendarDataContextProvider().addCalendarData(paramCardRenderingContext, this.mCalendarEntry.getHash());
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.CalendarEntryRenderingContextAdapter
 * JD-Core Version:    0.7.0.1
 */