package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.NotificationDetails;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class FlightTimeToLeaveForNotification
  extends AbstractSingleEntryNotification
{
  private static final String TAG = Tag.getTag(FlightTimeToLeaveForNotification.class);
  @Nullable
  private final Sidekick.Location mCurrentLocation;
  private final DirectionsLauncher mDirectionsLauncher;
  private final Sidekick.FlightStatusEntry.Flight mNotifiedFlight;
  private TravelReport mTravelReport;
  
  public FlightTimeToLeaveForNotification(Sidekick.Entry paramEntry, @Nullable Sidekick.Location paramLocation, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry);
    this.mCurrentLocation = paramLocation;
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mNotifiedFlight = getNotifiedFlight();
    if (this.mNotifiedFlight != null)
    {
      Sidekick.CommuteSummary localCommuteSummary = getCommuteSumary(this.mNotifiedFlight);
      if (localCommuteSummary != null) {
        this.mTravelReport = new TravelReport(localCommuteSummary);
      }
    }
  }
  
  @Nullable
  private Sidekick.CommuteSummary getCommuteSumary(Sidekick.FlightStatusEntry.Flight paramFlight)
  {
    if (paramFlight.hasDepartureAirport())
    {
      Sidekick.FlightStatusEntry.Airport localAirport = paramFlight.getDepartureAirport();
      if (localAirport.hasRoute()) {
        return localAirport.getRoute();
      }
    }
    return null;
  }
  
  @Nullable
  private Sidekick.FlightStatusEntry.Flight getNotifiedFlight()
  {
    Sidekick.Entry localEntry = getEntry();
    if ((localEntry != null) && (localEntry.hasFlightStatusEntry()))
    {
      Iterator localIterator = localEntry.getFlightStatusEntry().getFlightList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.FlightStatusEntry.Flight localFlight = (Sidekick.FlightStatusEntry.Flight)localIterator.next();
        if (localFlight.hasNotificationDetails()) {
          return localFlight;
        }
      }
    }
    return null;
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport == null) {
      return super.getActions(paramCardRenderingContext);
    }
    Sidekick.CommuteSummary localCommuteSummary = this.mTravelReport.getRoute();
    NavigateAction localNavigateAction = null;
    if (localCommuteSummary != null)
    {
      Sidekick.FlightStatusEntry.Flight localFlight = this.mNotifiedFlight;
      localNavigateAction = null;
      if (localFlight != null)
      {
        boolean bool1 = this.mNotifiedFlight.hasDepartureAirport();
        localNavigateAction = null;
        if (bool1)
        {
          Sidekick.FlightStatusEntry.Airport localAirport = this.mNotifiedFlight.getDepartureAirport();
          boolean bool2 = localAirport.hasLocation();
          localNavigateAction = null;
          if (bool2) {
            localNavigateAction = new NavigateAction(NavigationContext.fromRenderingContext(paramCardRenderingContext), this.mDirectionsLauncher, this.mCurrentLocation, localAirport.getLocation(), localCommuteSummary);
          }
        }
      }
    }
    if (localNavigateAction != null) {
      return ImmutableList.of(localNavigateAction);
    }
    return super.getActions(paramCardRenderingContext);
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mNotifiedFlight == null) {}
    while (!this.mNotifiedFlight.hasNotificationDetails()) {
      return null;
    }
    Sidekick.FlightStatusEntry.NotificationDetails localNotificationDetails = this.mNotifiedFlight.getNotificationDetails();
    return paramContext.getString(2131362273, new Object[] { DateUtils.formatDateTime(paramContext, 1000L * localNotificationDetails.getLeaveByTimeSecondsSinceEpoch(), 1), TimeUtilities.getEtaString(paramContext, localNotificationDetails.getArriveMinutesBefore(), false) });
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mNotifiedFlight == null) {
      return null;
    }
    Object[] arrayOfObject1 = new Object[1];
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = BidiUtils.unicodeWrap(this.mNotifiedFlight.getAirlineCode());
    arrayOfObject2[1] = BidiUtils.unicodeWrap(this.mNotifiedFlight.getFlightNumber());
    arrayOfObject1[0] = String.format("%s %s", arrayOfObject2);
    return paramContext.getString(2131362271, arrayOfObject1);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.FLIGHT_TIME_TO_LEAVE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130837907;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
  
  public int getNotificationType()
  {
    return 2;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.FlightTimeToLeaveForNotification
 * JD-Core Version:    0.7.0.1
 */