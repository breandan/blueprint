package com.google.android.voicesearch.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import com.google.majel.proto.CalendarProtos.CalendarDateTime;
import java.util.Calendar;
import java.util.TimeZone;
import javax.annotation.Nullable;

public class AgendaTimeUtil
{
  public static boolean alreadyEnded(CalendarProtos.AgendaItem paramAgendaItem, Clock paramClock)
  {
    if (!paramAgendaItem.hasEndTime()) {
      return isBeforeNow(paramAgendaItem.getStartTime(), paramClock);
    }
    return isBeforeNow(paramAgendaItem.getEndTime(), paramClock);
  }
  
  public static int compareDateTime(CalendarProtos.CalendarDateTime paramCalendarDateTime1, CalendarProtos.CalendarDateTime paramCalendarDateTime2)
  {
    return Long.valueOf(toUtcMillis(paramCalendarDateTime1)).compareTo(Long.valueOf(toUtcMillis(paramCalendarDateTime2)));
  }
  
  public static String format(Context paramContext, CalendarProtos.CalendarDateTime paramCalendarDateTime, int paramInt)
  {
    return DateUtils.formatDateTime(paramContext, toUtcMillis(paramCalendarDateTime), paramInt);
  }
  
  public static String formatDateFromJulianDay(Context paramContext, int paramInt)
  {
    Time localTime = new Time();
    localTime.setJulianDay(paramInt);
    return DateUtils.formatDateTime(paramContext, localTime.toMillis(false), 18);
  }
  
  public static String formatPretty(Context paramContext, CalendarProtos.CalendarDateTime paramCalendarDateTime, int paramInt)
  {
    return TimeUtilities.formatDisplayTime(paramContext, toUtcMillis(paramCalendarDateTime), paramInt).toString();
  }
  
  public static int getJulianDay(long paramLong)
  {
    return getJulianDay(toTime(paramLong));
  }
  
  public static int getJulianDay(Time paramTime)
  {
    return Time.getJulianDay(paramTime.toMillis(false), paramTime.gmtoff);
  }
  
  public static int getJulianDay(CalendarProtos.CalendarDateTime paramCalendarDateTime)
  {
    return getJulianDay(toTime(paramCalendarDateTime));
  }
  
  private static boolean isBeforeNow(CalendarProtos.CalendarDateTime paramCalendarDateTime, Clock paramClock)
  {
    return toUtcMillis(paramCalendarDateTime) < paramClock.currentTimeMillis();
  }
  
  public static boolean isStartOfDay(Time paramTime)
  {
    return (paramTime.hour == 0) && (paramTime.minute == 0) && (paramTime.second == 0);
  }
  
  public static boolean isToday(CalendarProtos.AgendaItem paramAgendaItem)
  {
    return relativeDays(null, toTime(paramAgendaItem.getStartTime())) == 0;
  }
  
  public static boolean isTomorrow(CalendarProtos.AgendaItem paramAgendaItem)
  {
    return relativeDays(null, toTime(paramAgendaItem.getStartTime())) == 1;
  }
  
  public static int relativeDays(@Nullable Time paramTime1, Time paramTime2)
  {
    if (paramTime1 == null)
    {
      paramTime1 = new Time();
      paramTime1.setToNow();
    }
    return getJulianDay(paramTime2) - getJulianDay(paramTime1);
  }
  
  public static Calendar toCalendar(CalendarProtos.CalendarDateTime paramCalendarDateTime)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(toUtcMillis(paramCalendarDateTime));
    return localCalendar;
  }
  
  public static CalendarProtos.CalendarDateTime toCalendarDateTime(long paramLong)
  {
    TimeZone localTimeZone = TimeZone.getDefault();
    return new CalendarProtos.CalendarDateTime().setTimeMs(paramLong).setOffsetMs(localTimeZone.getOffset(paramLong));
  }
  
  public static Time toTime(long paramLong)
  {
    Time localTime = new Time();
    localTime.set(paramLong);
    return localTime;
  }
  
  public static Time toTime(CalendarProtos.CalendarDateTime paramCalendarDateTime)
  {
    return toTime(toUtcMillis(paramCalendarDateTime));
  }
  
  public static long toUtcMillis(CalendarProtos.CalendarDateTime paramCalendarDateTime)
  {
    if (paramCalendarDateTime.hasOffsetMs()) {
      return paramCalendarDateTime.getTimeMs();
    }
    TimeZone localTimeZone = TimeZone.getDefault();
    return paramCalendarDateTime.getTimeMs() - localTimeZone.getOffset(paramCalendarDateTime.getTimeMs());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.util.AgendaTimeUtil
 * JD-Core Version:    0.7.0.1
 */