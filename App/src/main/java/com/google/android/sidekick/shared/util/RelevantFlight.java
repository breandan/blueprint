package com.google.android.sidekick.shared.util;

import android.content.Context;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Time;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Nullable;

public class RelevantFlight
{
  private final Sidekick.FlightStatusEntry.Flight mFlight;
  private final int mStatus;
  
  public RelevantFlight(Sidekick.FlightStatusEntry.Flight paramFlight, int paramInt)
  {
    this.mFlight = paramFlight;
    this.mStatus = paramInt;
  }
  
  @Nullable
  public static RelevantFlight fromFlightStatusEntry(Sidekick.FlightStatusEntry paramFlightStatusEntry, long paramLong)
  {
    Object localObject1 = null;
    Iterator localIterator = paramFlightStatusEntry.getFlightList().iterator();
    Object localObject3;
    Object localObject4;
    for (;;)
    {
      boolean bool = localIterator.hasNext();
      Object localObject2 = null;
      localObject3 = null;
      localObject4 = null;
      Sidekick.FlightStatusEntry.Flight localFlight;
      if (bool)
      {
        localFlight = (Sidekick.FlightStatusEntry.Flight)localIterator.next();
        if (localFlight.getStatusCode() != 5) {
          break label77;
        }
        localObject2 = localFlight;
      }
      for (;;)
      {
        if (localObject2 == null) {
          break label183;
        }
        return new RelevantFlight(localObject2, 0);
        label77:
        if ((!localFlight.hasDepartureTime()) || (!localFlight.hasArrivalTime())) {
          break;
        }
        long l1 = getTimeToUseInMs(localFlight.getDepartureTime());
        long l2 = getTimeToUseInMs(localFlight.getArrivalTime());
        if (l1 > paramLong)
        {
          localObject4 = localFlight;
          localObject2 = null;
          localObject3 = null;
        }
        else
        {
          if ((l1 >= paramLong) || (l2 <= paramLong)) {
            break label160;
          }
          localObject3 = localFlight;
          localObject2 = null;
          localObject4 = null;
        }
      }
      label160:
      if ((localFlight.hasStatusCode()) && (localFlight.getStatusCode() == 3)) {
        localObject1 = localFlight;
      }
    }
    label183:
    if (localObject4 != null) {
      return new RelevantFlight(localObject4, 1);
    }
    if (localObject3 != null) {
      return new RelevantFlight(localObject3, 2);
    }
    if (localObject1 != null) {
      return new RelevantFlight(localObject1, 3);
    }
    return null;
  }
  
  private static long getTimeToUseInMs(Sidekick.FlightStatusEntry.Time paramTime)
  {
    if (paramTime.hasActualTimeSecondsSinceEpoch()) {
      return 1000L * paramTime.getActualTimeSecondsSinceEpoch();
    }
    return 1000L * paramTime.getScheduledTimeSecondsSinceEpoch();
  }
  
  String formatNotificationTime(Context paramContext, int paramInt, Sidekick.FlightStatusEntry.Time paramTime)
  {
    java.text.DateFormat localDateFormat = android.text.format.DateFormat.getTimeFormat(paramContext);
    localDateFormat.setTimeZone(TimeZone.getTimeZone(paramTime.getTimeZoneId()));
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = localDateFormat.format(new Date(getTimeToUseInMs(paramTime)));
    return paramContext.getString(paramInt, arrayOfObject);
  }
  
  public Sidekick.FlightStatusEntry.Flight getFlight()
  {
    return this.mFlight;
  }
  
  @Nullable
  public CharSequence getFormattedTime(Context paramContext)
  {
    switch (this.mStatus)
    {
    case 0: 
    default: 
      return null;
    case 1: 
      return formatNotificationTime(paramContext, 2131362312, getFlight().getDepartureTime());
    case 2: 
      return formatNotificationTime(paramContext, 2131362313, getFlight().getArrivalTime());
    }
    return formatNotificationTime(paramContext, 2131362314, getFlight().getArrivalTime());
  }
  
  public int getStatus()
  {
    return this.mStatus;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.RelevantFlight
 * JD-Core Version:    0.7.0.1
 */