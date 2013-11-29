package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.widget.RemoteViews;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.cards.FlightStatusEntryAdapter;
import com.google.android.sidekick.shared.util.FlightStatusFormatter;
import com.google.android.sidekick.shared.util.RelevantFlight;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;

public class FlightRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<FlightStatusEntryAdapter>
{
  private static final int[] DELAYED_DRAWABLES = { 2130838028, 2130838027, 2130838029 };
  private static final int[] ON_TIME_DRAWABLES = { 2130838031, 2130838030, 2130838029 };
  private static final int[] UNKNOWN_DRAWABLES = { 2130838026, 2130838025, 2130838029 };
  private final Clock mClock;
  
  public FlightRemoteViewsAdapter(FlightStatusEntryAdapter paramFlightStatusEntryAdapter, Clock paramClock)
  {
    super(paramFlightStatusEntryAdapter);
    this.mClock = paramClock;
  }
  
  private RemoteViews createRemoteViewInternal(Context paramContext, boolean paramBoolean)
  {
    Sidekick.FlightStatusEntry localFlightStatusEntry = ((FlightStatusEntryAdapter)getEntryCardViewAdapter()).getEntry().getFlightStatusEntry();
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968692);
    RelevantFlight localRelevantFlight = RelevantFlight.fromFlightStatusEntry(localFlightStatusEntry, this.mClock.currentTimeMillis());
    Sidekick.FlightStatusEntry.Flight localFlight;
    int[] arrayOfInt;
    int i;
    if (localRelevantFlight != null)
    {
      localFlight = localRelevantFlight.getFlight();
      localRemoteViews.setTextViewText(2131296619, localFlight.getDepartureAirport().getCode());
      localRemoteViews.setTextViewText(2131296623, localFlight.getArrivalAirport().getCode());
      if (paramBoolean)
      {
        int j = paramContext.getResources().getDimensionPixelSize(2131689802);
        localRemoteViews.setTextViewTextSize(2131296619, 0, j);
        localRemoteViews.setTextViewTextSize(2131296623, 0, j);
      }
      arrayOfInt = getProgressResourceIds(localFlight.getStatusCode());
      if (localRelevantFlight == null) {
        break label280;
      }
      i = localRelevantFlight.getStatus();
      label146:
      switch (i)
      {
      }
    }
    for (;;)
    {
      FlightStatusFormatter localFlightStatusFormatter = new FlightStatusFormatter(paramContext);
      localRemoteViews.setTextViewText(2131296308, localFlightStatusFormatter.getStatusSummary(localFlight.getStatusCode()));
      localRemoteViews.setTextColor(2131296308, localFlightStatusFormatter.getColorForStatus(localFlight.getStatusCode()));
      if ((!paramBoolean) && (localRelevantFlight != null))
      {
        CharSequence localCharSequence = localRelevantFlight.getFormattedTime(paramContext);
        if (localCharSequence != null) {
          localRemoteViews.setTextViewText(2131296624, localCharSequence);
        }
      }
      return localRemoteViews;
      localFlight = ((FlightStatusEntryAdapter)getEntryCardViewAdapter()).getEntry().getFlightStatusEntry().getFlight(0);
      break;
      label280:
      i = 1;
      break label146;
      setDrawable(localRemoteViews, 2131296621, arrayOfInt[1]);
      if (!paramBoolean)
      {
        setDrawable(localRemoteViews, 2131296622, arrayOfInt[2]);
        continue;
        setDrawable(localRemoteViews, 2131296621, arrayOfInt[1]);
        if (!paramBoolean)
        {
          setDrawable(localRemoteViews, 2131296620, arrayOfInt[0]);
          setDrawable(localRemoteViews, 2131296622, arrayOfInt[2]);
          continue;
          setDrawable(localRemoteViews, 2131296621, arrayOfInt[1]);
          if (!paramBoolean) {
            setDrawable(localRemoteViews, 2131296620, arrayOfInt[0]);
          }
        }
      }
    }
  }
  
  private int[] getProgressResourceIds(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return UNKNOWN_DRAWABLES;
    case 1: 
    case 2: 
    case 3: 
      return ON_TIME_DRAWABLES;
    }
    return DELAYED_DRAWABLES;
  }
  
  private void setDrawable(RemoteViews paramRemoteViews, int paramInt1, int paramInt2)
  {
    paramRemoteViews.setViewVisibility(paramInt1, 0);
    paramRemoteViews.setInt(paramInt1, "setBackgroundResource", paramInt2);
  }
  
  public boolean canCreateRemoteViews()
  {
    return ((FlightStatusEntryAdapter)getEntryCardViewAdapter()).getEntry().getFlightStatusEntry().getFlightCount() > 0;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, true);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.FlightRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */