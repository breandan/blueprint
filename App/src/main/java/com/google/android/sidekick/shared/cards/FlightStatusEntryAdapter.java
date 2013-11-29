package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.FlightCard;
import com.google.android.sidekick.shared.ui.FlightCard.Builder;
import com.google.android.sidekick.shared.ui.FlightCard.SegmentBuilder;
import com.google.android.sidekick.shared.ui.FlightCard.StopBuilder;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.FlightStatusEntryUtil;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Time;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class FlightStatusEntryAdapter
  extends BaseEntryAdapter
{
  private final DirectionsLauncher mDirectionsLauncher;
  
  public FlightStatusEntryAdapter(Sidekick.Entry paramEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mDirectionsLauncher = paramDirectionsLauncher;
  }
  
  private void addFlightSegment(Sidekick.FlightStatusEntry.Flight paramFlight, FlightCard.SegmentBuilder paramSegmentBuilder, FlightCard.Builder paramBuilder)
  {
    paramSegmentBuilder.setStatus(paramFlight.getStatusCode());
    if ((paramFlight.hasDepartureAirport()) && (paramFlight.hasDepartureTime()))
    {
      Sidekick.FlightStatusEntry.Airport localAirport2 = paramFlight.getDepartureAirport();
      Sidekick.FlightStatusEntry.Time localTime2 = paramFlight.getDepartureTime();
      String str2 = "";
      if (localAirport2.hasLocation()) {
        str2 = localAirport2.getLocation().getName();
      }
      paramSegmentBuilder.departure().setAirportName(str2).setAirportCode(localAirport2.getCode()).setScheduled(getTime(localTime2, TimeType.SCHEDULED)).setActual(getTime(localTime2, TimeType.ACTUAL)).setTerminal(paramFlight.getDepartureTerminal()).setGate(paramFlight.getDepartureGate());
      if ((!paramFlight.hasArrivalAirport()) || (!paramFlight.hasArrivalTime())) {
        break label234;
      }
      Sidekick.FlightStatusEntry.Airport localAirport1 = paramFlight.getArrivalAirport();
      Sidekick.FlightStatusEntry.Time localTime1 = paramFlight.getArrivalTime();
      String str1 = "";
      if (localAirport1.hasLocation()) {
        str1 = localAirport1.getLocation().getName();
      }
      paramSegmentBuilder.arrival().setAirportName(str1).setAirportCode(localAirport1.getCode()).setScheduled(getTime(localTime1, TimeType.SCHEDULED)).setActual(getTime(localTime1, TimeType.ACTUAL)).setTerminal(paramFlight.getArrivalTerminal()).setGate(paramFlight.getArrivalGate());
    }
    for (;;)
    {
      paramBuilder.setGmailReferenceList(paramFlight.getGmailReferenceList());
      return;
      Log.w("FlightStatusEntryAdapter", "Missing departure info");
      break;
      label234:
      Log.w("FlightStatusEntryAdapter", "Missing arrival info");
    }
  }
  
  public static Sidekick.Entry createSampleEntry(Context paramContext, boolean paramBoolean)
  {
    Sidekick.Location localLocation1 = new Sidekick.Location().setName(paramContext.getString(2131362505));
    Sidekick.FlightStatusEntry.Airport localAirport1 = new Sidekick.FlightStatusEntry.Airport().setLocation(localLocation1).setCode(paramContext.getString(2131362506));
    Sidekick.Location localLocation2 = new Sidekick.Location().setName(paramContext.getString(2131362503));
    Sidekick.FlightStatusEntry.Airport localAirport2 = new Sidekick.FlightStatusEntry.Airport().setLocation(localLocation2).setCode(paramContext.getString(2131362504));
    Sidekick.FlightStatusEntry.Time localTime1 = new Sidekick.FlightStatusEntry.Time().setActualTimeSecondsSinceEpoch(1334091300L).setScheduledTimeSecondsSinceEpoch(1334091300L);
    Sidekick.FlightStatusEntry.Time localTime2 = new Sidekick.FlightStatusEntry.Time().setActualTimeSecondsSinceEpoch(1334162400L).setScheduledTimeSecondsSinceEpoch(1334162400L);
    Sidekick.FlightStatusEntry.Flight localFlight = new Sidekick.FlightStatusEntry.Flight().setDepartureAirport(localAirport2).setArrivalAirport(localAirport1).setStatusCode(1).setStatus(paramContext.getString(2131362502)).setAirlineCode(paramContext.getString(2131362501)).setAirlineName(paramContext.getString(2131362499)).setDepartureTime(localTime1).setArrivalTime(localTime2).setFlightNumber(paramContext.getString(2131362500)).setDepartureTerminal(paramContext.getString(2131362507)).setDepartureGate(paramContext.getString(2131362508)).setArrivalTerminal(paramContext.getString(2131362509)).setArrivalGate(paramContext.getString(2131362510));
    if (paramBoolean)
    {
      Sidekick.GmailReference localGmailReference = new Sidekick.GmailReference();
      localGmailReference.setEmailIdentifier("bogus_id");
      localGmailReference.setEmailUrl("http://gmail.com");
      localGmailReference.setSenderEmailAddress(paramContext.getString(2131362684));
      localFlight.addGmailReference(localGmailReference);
    }
    Sidekick.FlightStatusEntry localFlightStatusEntry = new Sidekick.FlightStatusEntry().addFlight(localFlight);
    return new Sidekick.Entry().setFlightStatusEntry(localFlightStatusEntry);
  }
  
  private static Calendar getTime(Sidekick.FlightStatusEntry.Time paramTime, TimeType paramTimeType)
  {
    Preconditions.checkNotNull(paramTimeType);
    if (paramTime == null) {
      return null;
    }
    String str;
    label26:
    TimeZone localTimeZone;
    long l;
    if (paramTime.hasTimeZoneId())
    {
      str = FlightCard.fixTimeZone(paramTime.getTimeZoneId());
      localTimeZone = TimeZone.getTimeZone(str);
      l = 0L;
      switch (2.$SwitchMap$com$google$android$sidekick$shared$cards$FlightStatusEntryAdapter$TimeType[paramTimeType.ordinal()])
      {
      default: 
        if (paramTime.hasActualTimeSecondsSinceEpoch()) {
          l = paramTime.getActualTimeSecondsSinceEpoch();
        }
        break;
      }
    }
    while (l > 0L)
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar(localTimeZone);
      localGregorianCalendar.setTime(new Date(1000L * l));
      return localGregorianCalendar;
      str = "UTC";
      break label26;
      if (paramTime.hasScheduledTimeSecondsSinceEpoch()) {
        l = paramTime.getScheduledTimeSecondsSinceEpoch();
      }
    }
  }
  
  public static void populateSampleCard(FlightCard paramFlightCard, LayoutInflater paramLayoutInflater, Clock paramClock, boolean paramBoolean)
  {
    Context localContext = paramFlightCard.getContext();
    new FlightStatusEntryAdapter(createSampleEntry(localContext, paramBoolean), null, null).populateView(localContext, null, paramFlightCard);
  }
  
  public String getLoggingName()
  {
    String str = super.getLoggingName();
    if (getEntry().getFlightStatusEntry().getFlightCount() > 0)
    {
      Iterator localIterator = getEntry().getFlightStatusEntry().getFlightList().iterator();
      while (localIterator.hasNext()) {
        if (((Sidekick.FlightStatusEntry.Flight)localIterator.next()).getGmailReferenceCount() > 0) {
          str = "Gmail" + str;
        }
      }
    }
    return str;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    FlightCard localFlightCard = new FlightCard(paramContext);
    populateView(paramContext, paramPredictiveCardContainer, localFlightCard);
    return localFlightCard;
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296612);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.FlightStatusEntry localFlightStatusEntry = getEntry().getFlightStatusEntry();
    if (localFlightStatusEntry.getFlight(0).hasDetailsUrl()) {
      openUrl(paramContext, localFlightStatusEntry.getFlight(0).getDetailsUrl());
    }
  }
  
  public void populateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, FlightCard paramFlightCard)
  {
    Sidekick.FlightStatusEntry localFlightStatusEntry = getEntry().getFlightStatusEntry();
    Sidekick.FlightStatusEntry.Flight localFlight = localFlightStatusEntry.getFlight(0);
    FlightCard.Builder localBuilder = new FlightCard.Builder(paramPredictiveCardContainer, localFlight.getAirlineName(), localFlight.getFlightNumber());
    localBuilder.setFlightStatusEntryAdapter(this);
    Iterator localIterator = localFlightStatusEntry.getFlightList().iterator();
    while (localIterator.hasNext()) {
      addFlightSegment((Sidekick.FlightStatusEntry.Flight)localIterator.next(), localBuilder.addSegment(), localBuilder);
    }
    Sidekick.FlightStatusEntry.Airport localAirport = FlightStatusEntryUtil.getNavigateAirport(localFlightStatusEntry);
    final Sidekick.CommuteSummary localCommuteSummary;
    final Sidekick.Location localLocation;
    final MapsLauncher.TravelMode localTravelMode;
    String str;
    if (localAirport != null)
    {
      NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
      if (localNavigationContext.shouldShowNavigation(localAirport.getLocation()))
      {
        localCommuteSummary = localAirport.getRoute();
        localLocation = localAirport.getLocation();
        localTravelMode = this.mDirectionsLauncher.getTravelMode(localNavigationContext, localCommuteSummary);
        str = TimeUtilities.getEtaString(paramContext, localCommuteSummary, true);
        if (!this.mDirectionsLauncher.modeSupportsNavigation(localTravelMode)) {
          break label245;
        }
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = BidiUtils.unicodeWrap(localAirport.getCode());
        arrayOfObject2[1] = str;
        localBuilder.setNavigateAction(paramContext.getString(2131362320, arrayOfObject2));
      }
    }
    for (;;)
    {
      paramFlightCard.setOnNavigateListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 57)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          FlightStatusEntryAdapter.this.mDirectionsLauncher.start(localLocation, null, localTravelMode, MapsLauncher.getPersonalizedRouteToken(localCommuteSummary));
        }
      });
      localBuilder.update(paramFlightCard);
      return;
      label245:
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = BidiUtils.unicodeWrap(localAirport.getCode());
      arrayOfObject1[1] = str;
      localBuilder.setGetDirectionsAction(paramContext.getString(2131362321, arrayOfObject1));
    }
  }
  
  public View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry)
  {
    return getView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  private static enum TimeType
  {
    static
    {
      ACTUAL = new TimeType("ACTUAL", 1);
      TimeType[] arrayOfTimeType = new TimeType[2];
      arrayOfTimeType[0] = SCHEDULED;
      arrayOfTimeType[1] = ACTUAL;
      $VALUES = arrayOfTimeType;
    }
    
    private TimeType() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.FlightStatusEntryAdapter
 * JD-Core Version:    0.7.0.1
 */