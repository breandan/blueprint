package com.google.android.sidekick.main.notifications;

import android.content.Context;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.RelevantFlight;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Time;
import javax.annotation.Nullable;

public class FlightStatusNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.FlightStatusEntry mFlightStatusEntry;
  private final RelevantFlight mRelevantFlight;
  
  public FlightStatusNotification(Sidekick.Entry paramEntry, RelevantFlight paramRelevantFlight)
  {
    super(paramEntry);
    this.mFlightStatusEntry = paramEntry.getFlightStatusEntry();
    this.mRelevantFlight = paramRelevantFlight;
  }
  
  private int flightDelayMinutes()
  {
    int i = this.mRelevantFlight.getStatus();
    Sidekick.FlightStatusEntry.Time localTime = null;
    switch (i)
    {
    }
    while ((localTime == null) || (!localTime.hasScheduledTimeSecondsSinceEpoch()) || (!localTime.hasActualTimeSecondsSinceEpoch()))
    {
      return 0;
      localTime = this.mRelevantFlight.getFlight().getDepartureTime();
      continue;
      localTime = this.mRelevantFlight.getFlight().getArrivalTime();
    }
    return (int)Math.max(0L, (localTime.getActualTimeSecondsSinceEpoch() - localTime.getScheduledTimeSecondsSinceEpoch()) / 60L);
  }
  
  @Nullable
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mRelevantFlight == null) {
      return null;
    }
    return this.mRelevantFlight.getFormattedTime(paramContext);
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Sidekick.FlightStatusEntry.Flight localFlight2;
    if (this.mRelevantFlight != null) {
      localFlight2 = this.mRelevantFlight.getFlight();
    }
    switch (localFlight2.getStatusCode())
    {
    default: 
      Sidekick.FlightStatusEntry.Flight localFlight1 = this.mFlightStatusEntry.getFlight(0);
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = BidiUtils.unicodeWrap(localFlight1.getAirlineName());
      arrayOfObject1[1] = BidiUtils.unicodeWrap(localFlight1.getFlightNumber());
      return paramContext.getString(2131362305, arrayOfObject1);
    case 5: 
      Object[] arrayOfObject4 = new Object[2];
      arrayOfObject4[0] = BidiUtils.unicodeWrap(localFlight2.getAirlineName());
      arrayOfObject4[1] = BidiUtils.unicodeWrap(localFlight2.getFlightNumber());
      return paramContext.getString(2131362306, arrayOfObject4);
    }
    int i = flightDelayMinutes();
    if (i > 0)
    {
      Object[] arrayOfObject3 = new Object[3];
      arrayOfObject3[0] = BidiUtils.unicodeWrap(localFlight2.getAirlineName());
      arrayOfObject3[1] = BidiUtils.unicodeWrap(localFlight2.getFlightNumber());
      arrayOfObject3[2] = Integer.valueOf(i);
      return paramContext.getString(2131362307, arrayOfObject3);
    }
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = BidiUtils.unicodeWrap(localFlight2.getAirlineName());
    arrayOfObject2[1] = BidiUtils.unicodeWrap(localFlight2.getFlightNumber());
    return paramContext.getString(2131362308, arrayOfObject2);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    if (isLowPriorityNotification()) {
      return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.FLIGHT_STATUS_WARNING_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838063;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (isLowPriorityNotification()) {
      return null;
    }
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.FlightStatusNotification
 * JD-Core Version:    0.7.0.1
 */