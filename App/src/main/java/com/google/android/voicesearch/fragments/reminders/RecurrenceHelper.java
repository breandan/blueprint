package com.google.android.voicesearch.fragments.reminders;

import android.text.format.Time;
import android.util.Log;
import com.android.recurrencepicker.EventRecurrence;
import com.google.android.speech.utils.ProtoBufUtils;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.caribou.tasks.DateTimeProtos.DateTime;
import com.google.caribou.tasks.DateTimeProtos.DateTime.Time;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.DailyPattern;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.MonthlyPattern;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.RecurrenceEnd;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.RecurrenceStart;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.WeeklyPattern;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence.YearlyPattern;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.ReminderEntry.RecurrenceInfo;
import com.google.geo.sidekick.Sidekick.ReminderEntry.RecurrenceInfo.DailyPattern;
import com.google.geo.sidekick.Sidekick.ReminderEntry.RecurrenceInfo.MonthlyPattern;
import com.google.geo.sidekick.Sidekick.ReminderEntry.RecurrenceInfo.WeeklyPattern;
import com.google.geo.sidekick.Sidekick.ReminderEntry.RecurrenceInfo.YearlyPattern;
import com.google.majel.proto.ActionV2Protos.AbsoluteTimeTrigger;
import java.util.Calendar;
import java.util.HashSet;

public class RecurrenceHelper
{
  public static int convertCalendarWeekdayToEventRecurrenceWeekday(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 2: 
      return 131072;
    case 3: 
      return 262144;
    case 4: 
      return 524288;
    case 5: 
      return 1048576;
    case 6: 
      return 2097152;
    case 7: 
      return 4194304;
    }
    return 65536;
  }
  
  public static EventRecurrence convertCaribouRecurrenceToEventRecurrence(RecurrenceProtos.Recurrence paramRecurrence)
  {
    if ((paramRecurrence == null) || (!paramRecurrence.hasFrequency()))
    {
      localEventRecurrence = null;
      return localEventRecurrence;
    }
    EventRecurrence localEventRecurrence = new EventRecurrence();
    switch (paramRecurrence.getFrequency())
    {
    default: 
      Log.e("RecurrenceHelper", "The frequency " + paramRecurrence.getFrequency() + " is not supported.");
      localEventRecurrence = null;
    }
    while (paramRecurrence.hasEvery())
    {
      localEventRecurrence.interval = paramRecurrence.getEvery();
      return localEventRecurrence;
      localEventRecurrence.freq = 4;
      continue;
      localEventRecurrence.freq = 5;
      setWeeklyPattern(paramRecurrence, localEventRecurrence);
      continue;
      localEventRecurrence.freq = 6;
      RecurrenceProtos.Recurrence.MonthlyPattern localMonthlyPattern = paramRecurrence.getMonthlyPattern();
      if (localMonthlyPattern == null)
      {
        Log.e("RecurrenceHelper", "monthlyPattern shouldn't be NULL.");
      }
      else if (localMonthlyPattern.hasWeekDay())
      {
        int i = convertCaribouWeekdayToEventRecurrenceWeekday(localMonthlyPattern.getWeekDay());
        if (i > 0)
        {
          localEventRecurrence.bydayCount = 1;
          localEventRecurrence.byday = new int[] { i };
          if ((localMonthlyPattern.getLastWeek()) || (localMonthlyPattern.getWeekDayNumber() == 5))
          {
            localEventRecurrence.bydayNum = new int[] { -1 };
          }
          else if ((localMonthlyPattern.getWeekDayNumber() > 5) || (localMonthlyPattern.getWeekDayNumber() <= 0))
          {
            localEventRecurrence.bydayCount = 0;
            Log.e("RecurrenceHelper", "Invalid weekdayNumber: " + localMonthlyPattern.getWeekDayNumber());
          }
          else
          {
            localEventRecurrence.bydayCount = 1;
            int[] arrayOfInt = new int[1];
            arrayOfInt[0] = localMonthlyPattern.getWeekDayNumber();
            localEventRecurrence.bydayNum = arrayOfInt;
            continue;
            localEventRecurrence.freq = 7;
          }
        }
      }
    }
  }
  
  private static int convertCaribouWeekdayToEventRecurrenceWeekday(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      Log.e("RecurrenceHelper", "Invalid weekday: " + paramInt);
      return -1;
    case 1: 
      return 131072;
    case 2: 
      return 262144;
    case 3: 
      return 524288;
    case 4: 
      return 1048576;
    case 5: 
      return 2097152;
    case 6: 
      return 4194304;
    }
    return 65536;
  }
  
  public static RecurrenceProtos.Recurrence convertEventRecurrenceToCaribouRecurrence(EventRecurrence paramEventRecurrence, SetReminderAction paramSetReminderAction)
  {
    RecurrenceProtos.Recurrence localRecurrence;
    if ((paramEventRecurrence == null) || (paramSetReminderAction == null)) {
      localRecurrence = null;
    }
    RecurrenceProtos.Recurrence.WeeklyPattern localWeeklyPattern;
    do
    {
      return localRecurrence;
      localRecurrence = new RecurrenceProtos.Recurrence();
      if (paramEventRecurrence.interval != 0) {
        localRecurrence.setEvery(paramEventRecurrence.interval);
      }
      RecurrenceProtos.Recurrence.RecurrenceStart localRecurrenceStart = new RecurrenceProtos.Recurrence.RecurrenceStart();
      Calendar localCalendar1 = Calendar.getInstance();
      localCalendar1.setTimeInMillis(paramSetReminderAction.getDateTimeMs());
      DateTimeProtos.DateTime localDateTime = new DateTimeProtos.DateTime();
      localDateTime.setYear(localCalendar1.get(1));
      localDateTime.setMonth(1 + localCalendar1.get(2));
      localDateTime.setDay(localCalendar1.get(5));
      localRecurrenceStart.setStartDateTime(localDateTime);
      localRecurrence.setRecurrenceStart(localRecurrenceStart);
      RecurrenceProtos.Recurrence.RecurrenceEnd localRecurrenceEnd = new RecurrenceProtos.Recurrence.RecurrenceEnd();
      int i = paramEventRecurrence.count;
      int j = 0;
      if (i != 0)
      {
        localRecurrenceEnd.setNumOccurences(paramEventRecurrence.count);
        j = 1;
      }
      if (paramEventRecurrence.until != null)
      {
        Time localTime = new Time();
        localTime.parse(paramEventRecurrence.until);
        localRecurrenceEnd.setEndMillis(localTime.toMillis(false));
        j = 1;
      }
      if (j != 0) {
        localRecurrence.setRecurrenceEnd(localRecurrenceEnd);
      }
      RecurrenceProtos.Recurrence.DailyPattern localDailyPattern = getDailyPattern(paramSetReminderAction.getDateTimeMs(), paramSetReminderAction.getSymbolicTime());
      if (localDailyPattern != null) {
        localRecurrence.setDailyPattern(localDailyPattern);
      }
      if (paramEventRecurrence.freq == 4)
      {
        localRecurrence.setFrequency(0);
        return localRecurrence;
      }
      if (paramEventRecurrence.freq != 5) {
        break;
      }
      localRecurrence.setFrequency(1);
      localWeeklyPattern = new RecurrenceProtos.Recurrence.WeeklyPattern();
      for (int n = 0; n < paramEventRecurrence.bydayCount; n++)
      {
        int i1 = paramEventRecurrence.byday[n];
        int i2 = convertEventRecurrenceWeekdayToCaribouWeekday(i1);
        if (i2 == -1)
        {
          Log.e("RecurrenceHelper", "Failed to convert byday: " + i1 + " to caribou Weekday.");
          return null;
        }
        localWeeklyPattern.addWeekDay(i2);
      }
    } while (localWeeklyPattern.getWeekDayCount() == 0);
    localRecurrence.setWeeklyPattern(localWeeklyPattern);
    return localRecurrence;
    if (paramEventRecurrence.freq == 6)
    {
      localRecurrence.setFrequency(2);
      RecurrenceProtos.Recurrence.MonthlyPattern localMonthlyPattern2 = new RecurrenceProtos.Recurrence.MonthlyPattern();
      if (paramEventRecurrence.bydayCount > 0)
      {
        if ((paramEventRecurrence.byday == null) || (paramEventRecurrence.bydayNum == null))
        {
          Log.e("RecurrenceHelper", "byday and bydayNum can't be NULL for monthly reminder, eventRecurrence: " + paramEventRecurrence.toString());
          return null;
        }
        int k = paramEventRecurrence.byday[0];
        int m = convertEventRecurrenceWeekdayToCaribouWeekday(k);
        if (m == -1)
        {
          Log.e("RecurrenceHelper", "Failed to convert byday: " + k + " to caribou Weekday.");
          return null;
        }
        if (paramEventRecurrence.bydayNum[0] == -1)
        {
          localMonthlyPattern2.setLastWeek(true);
          localMonthlyPattern2.setWeekDay(m);
        }
      }
      for (;;)
      {
        localRecurrence.setMonthlyPattern(localMonthlyPattern2);
        return localRecurrence;
        localMonthlyPattern2.setWeekDayNumber(paramEventRecurrence.bydayNum[0]);
        break;
        Calendar localCalendar3 = Calendar.getInstance();
        localCalendar3.setTimeInMillis(paramSetReminderAction.getDateTimeMs());
        localMonthlyPattern2.addMonthDay(localCalendar3.get(5));
      }
    }
    if (paramEventRecurrence.freq == 7)
    {
      localRecurrence.setFrequency(3);
      Calendar localCalendar2 = Calendar.getInstance();
      localCalendar2.setTimeInMillis(paramSetReminderAction.getDateTimeMs());
      RecurrenceProtos.Recurrence.MonthlyPattern localMonthlyPattern1 = new RecurrenceProtos.Recurrence.MonthlyPattern();
      localMonthlyPattern1.addMonthDay(localCalendar2.get(5));
      RecurrenceProtos.Recurrence.YearlyPattern localYearlyPattern = new RecurrenceProtos.Recurrence.YearlyPattern();
      localYearlyPattern.setMonthlyPattern(localMonthlyPattern1);
      localYearlyPattern.addYearMonth(1 + localCalendar2.get(2));
      localRecurrence.setYearlyPattern(localYearlyPattern);
      return localRecurrence;
    }
    Log.e("RecurrenceHelper", "The frequency " + paramEventRecurrence.freq + " is not supported.");
    return null;
  }
  
  private static int convertEventRecurrenceWeekdayToCaribouWeekday(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      Log.e("RecurrenceHelper", "The day " + paramInt + " is invalid.");
      return -1;
    case 131072: 
      return 1;
    case 262144: 
      return 2;
    case 524288: 
      return 3;
    case 1048576: 
      return 4;
    case 2097152: 
      return 5;
    case 4194304: 
      return 6;
    }
    return 7;
  }
  
  private static int convertEventRecurrenceWeekdayToTimeWeekday(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 131072: 
      return 1;
    case 262144: 
      return 2;
    case 524288: 
      return 3;
    case 1048576: 
      return 4;
    case 2097152: 
      return 5;
    case 4194304: 
      return 6;
    }
    return 0;
  }
  
  private static int convertSidekickRecurrenceInfoToEventRecurrenceWeekday(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Invalid weekday: " + paramInt);
    case 1: 
      return 131072;
    case 2: 
      return 262144;
    case 3: 
      return 524288;
    case 4: 
      return 1048576;
    case 5: 
      return 2097152;
    case 6: 
      return 4194304;
    }
    return 65536;
  }
  
  public static ActionV2Protos.AbsoluteTimeTrigger getAbsoluteTimeFromRecurrence(long paramLong, RecurrenceProtos.Recurrence paramRecurrence)
  {
    ActionV2Protos.AbsoluteTimeTrigger localAbsoluteTimeTrigger;
    if (paramRecurrence == null) {
      localAbsoluteTimeTrigger = null;
    }
    label130:
    label345:
    for (;;)
    {
      return localAbsoluteTimeTrigger;
      localAbsoluteTimeTrigger = new ActionV2Protos.AbsoluteTimeTrigger();
      RecurrenceProtos.Recurrence.RecurrenceStart localRecurrenceStart;
      if (paramRecurrence.hasRecurrenceStart())
      {
        localRecurrenceStart = paramRecurrence.getRecurrenceStart();
        if (localRecurrenceStart.hasStartMillis()) {
          localAbsoluteTimeTrigger.setTimeMs(localRecurrenceStart.getStartMillis());
        }
      }
      else
      {
        if (paramRecurrence.hasDailyPattern()) {
          break label130;
        }
        localAbsoluteTimeTrigger.setSymbolicTime(4);
      }
      for (;;)
      {
        if (localAbsoluteTimeTrigger.hasTimeMs()) {
          break label345;
        }
        localAbsoluteTimeTrigger.setTimeMs(paramLong);
        return localAbsoluteTimeTrigger;
        if (!localRecurrenceStart.hasStartDateTime()) {
          break;
        }
        DateTimeProtos.DateTime localDateTime = localRecurrenceStart.getStartDateTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.set(localDateTime.getYear(), -1 + localDateTime.getMonth(), localDateTime.getDay());
        localAbsoluteTimeTrigger.setTimeMs(localCalendar2.getTimeInMillis());
        break;
        if (paramRecurrence.getDailyPattern().hasDayPeriod())
        {
          switch (paramRecurrence.getDailyPattern().getDayPeriod())
          {
          default: 
            Log.w("RecurrenceHelper", "Invalid day_period from daily_pattern:  " + ProtoBufUtils.toString(paramRecurrence.getDailyPattern()));
            break;
          case 1: 
            localAbsoluteTimeTrigger.setSymbolicTime(0);
            break;
          case 2: 
            localAbsoluteTimeTrigger.setSymbolicTime(1);
            break;
          case 3: 
            localAbsoluteTimeTrigger.setSymbolicTime(2);
            break;
          case 4: 
            localAbsoluteTimeTrigger.setSymbolicTime(3);
            break;
          }
        }
        else
        {
          if (!paramRecurrence.getDailyPattern().hasTimeOfDay()) {
            break label347;
          }
          if (!localAbsoluteTimeTrigger.hasTimeMs())
          {
            Log.w("RecurrenceHelper", "The time_ms should have been populated.");
            return null;
          }
          Calendar localCalendar1 = Calendar.getInstance();
          localCalendar1.setTimeInMillis(localAbsoluteTimeTrigger.getTimeMs());
          DateTimeProtos.DateTime.Time localTime = paramRecurrence.getDailyPattern().getTimeOfDay();
          localCalendar1.set(11, localTime.getHour());
          localCalendar1.set(12, localTime.getMinute());
          localCalendar1.set(13, localTime.getSecond());
          localAbsoluteTimeTrigger.setTimeMs(localCalendar1.getTimeInMillis());
        }
      }
    }
    label347:
    Log.w("RecurrenceHelper", "The DailyPattern should have day_period or time_of_day field. DailyPattern: " + ProtoBufUtils.toString(paramRecurrence.getDailyPattern()));
    return null;
  }
  
  public static RecurrenceProtos.Recurrence.DailyPattern getDailyPattern(long paramLong, SymbolicTime paramSymbolicTime)
  {
    RecurrenceProtos.Recurrence.DailyPattern localDailyPattern = new RecurrenceProtos.Recurrence.DailyPattern();
    if (paramSymbolicTime != null)
    {
      switch (paramSymbolicTime.actionV2Symbol)
      {
      default: 
        Log.w("RecurrenceHelper", "Unsupported symbolicTime " + paramSymbolicTime + " for DAILY reminders.");
        return null;
      case 0: 
        localDailyPattern.setDayPeriod(1);
        return localDailyPattern;
      case 1: 
        localDailyPattern.setDayPeriod(2);
        return localDailyPattern;
      case 2: 
        localDailyPattern.setDayPeriod(3);
        return localDailyPattern;
      case 3: 
        localDailyPattern.setDayPeriod(4);
        return localDailyPattern;
      }
      return null;
    }
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    DateTimeProtos.DateTime.Time localTime = new DateTimeProtos.DateTime.Time();
    localTime.setHour(localCalendar.get(11));
    localTime.setMinute(localCalendar.get(12));
    localTime.setSecond(localCalendar.get(13));
    localDailyPattern.setTimeOfDay(localTime);
    return localDailyPattern;
  }
  
  public static Time getUpdatedDateForWeeklyReminder(EventRecurrence paramEventRecurrence, long paramLong)
  {
    Time localTime;
    if ((paramEventRecurrence == null) || (paramEventRecurrence.freq != 5))
    {
      localTime = null;
      return localTime;
    }
    HashSet localHashSet = new HashSet();
    for (int i = 0; i < paramEventRecurrence.bydayCount; i++) {
      localHashSet.add(Integer.valueOf(convertEventRecurrenceWeekdayToTimeWeekday(paramEventRecurrence.byday[i])));
    }
    if (localHashSet.isEmpty()) {
      return null;
    }
    for (int j = 0;; j++)
    {
      if (j >= 7) {
        break label124;
      }
      localTime = new Time();
      localTime.set(paramLong + 86400000L * j);
      if (localHashSet.contains(Integer.valueOf(localTime.weekDay))) {
        break;
      }
    }
    label124:
    return null;
  }
  
  public static boolean maybeUpdateNthDayOfWeekForMonthly(long paramLong, EventRecurrence paramEventRecurrence)
  {
    if ((paramEventRecurrence == null) || (paramEventRecurrence.freq != 6)) {}
    while ((paramEventRecurrence.bydayCount != 1) || (paramEventRecurrence.byday == null) || (paramEventRecurrence.bydayNum == null)) {
      return false;
    }
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    int i = convertCalendarWeekdayToEventRecurrenceWeekday(localCalendar.get(7));
    if (i < 0)
    {
      Log.e("RecurrenceHelper", "Failed to convert Calendar weekday:" + i + " to EventRecurrence.Weekday.");
      return false;
    }
    int j = localCalendar.get(8);
    if ((j <= 0) || (j > 5))
    {
      Log.e("RecurrenceHelper", "weekdayNumber should be in range [1,5] but was " + j);
      return false;
    }
    paramEventRecurrence.byday[0] = i;
    int[] arrayOfInt = paramEventRecurrence.bydayNum;
    if (j == 5) {
      j = -1;
    }
    arrayOfInt[0] = j;
    return true;
  }
  
  static void parseDailyPattern(Sidekick.ReminderEntry.RecurrenceInfo.DailyPattern paramDailyPattern, SetReminderAction paramSetReminderAction)
  {
    if (paramDailyPattern == null) {
      return;
    }
    if (paramDailyPattern.hasDayPart())
    {
      SymbolicTime localSymbolicTime;
      switch (paramDailyPattern.getDayPart())
      {
      default: 
        Log.w("RecurrenceHelper", "Unsupported day part: " + paramDailyPattern.getDayPart());
        return;
      case 1: 
        localSymbolicTime = SymbolicTime.MORNING;
      }
      for (;;)
      {
        paramSetReminderAction.setSymbolicTime(localSymbolicTime, Lists.newArrayList(DateTimePickerHelper.TIMES_OF_DAY));
        return;
        localSymbolicTime = SymbolicTime.AFTERNOON;
        continue;
        localSymbolicTime = SymbolicTime.EVENING;
        continue;
        localSymbolicTime = SymbolicTime.NIGHT;
      }
    }
    if ((paramDailyPattern.hasHour()) && (paramDailyPattern.hasMinute()))
    {
      paramSetReminderAction.setTime(paramDailyPattern.getHour(), paramDailyPattern.getMinute());
      return;
    }
    Log.w("RecurrenceHelper", "Invalid daily pattern: " + ProtoBufUtils.toString(paramDailyPattern));
  }
  
  static void parseMonthlyPattern(Sidekick.ReminderEntry.RecurrenceInfo.MonthlyPattern paramMonthlyPattern, EventRecurrence paramEventRecurrence)
  {
    if (paramEventRecurrence == null) {
      Log.w("RecurrenceHelper", "No eventRecurrence to update");
    }
    if (paramMonthlyPattern == null) {
      Log.w("RecurrenceHelper", "Missing monthly pattern");
    }
    int i;
    do
    {
      do
      {
        return;
        if (paramMonthlyPattern.hasWeekDay())
        {
          if ((paramMonthlyPattern.getLastWeek()) || (paramMonthlyPattern.getWeekDayNumber() == 5)) {}
          int[] arrayOfInt;
          for (paramEventRecurrence.bydayNum = new int[] { -1 };; paramEventRecurrence.bydayNum = arrayOfInt)
          {
            int k = convertSidekickRecurrenceInfoToEventRecurrenceWeekday(paramMonthlyPattern.getWeekDay());
            paramEventRecurrence.bydayCount = 1;
            paramEventRecurrence.byday = new int[] { k };
            return;
            if ((paramMonthlyPattern.getWeekDayNumber() > 5) || (paramMonthlyPattern.getWeekDayNumber() <= 0))
            {
              Log.w("RecurrenceHelper", "Bad weekday number: " + paramMonthlyPattern.getWeekDayNumber());
              return;
            }
            arrayOfInt = new int[1];
            arrayOfInt[0] = paramMonthlyPattern.getWeekDayNumber();
          }
        }
        i = paramMonthlyPattern.getMonthDayCount();
        if (paramMonthlyPattern.getLastDay()) {
          i++;
        }
      } while (i <= 0);
      paramEventRecurrence.bymonthdayCount = i;
      paramEventRecurrence.bymonthday = new int[i];
      if (paramMonthlyPattern.getMonthDayCount() > 0) {
        for (int j = 0; j < paramMonthlyPattern.getMonthDayCount(); j++) {
          paramEventRecurrence.bymonthday[j] = paramMonthlyPattern.getMonthDay(j);
        }
      }
    } while (!paramMonthlyPattern.getLastDay());
    paramEventRecurrence.bymonthday[(i - 1)] = -1;
  }
  
  public static void parseSidekickRecurrenceInfo(Sidekick.ReminderEntry.RecurrenceInfo paramRecurrenceInfo, SetReminderAction paramSetReminderAction)
  {
    if ((paramRecurrenceInfo == null) || (paramSetReminderAction == null)) {
      return;
    }
    if (paramRecurrenceInfo.hasRecurrenceId()) {
      paramSetReminderAction.setRecurrenceId(paramRecurrenceInfo.getRecurrenceId());
    }
    EventRecurrence localEventRecurrence = new EventRecurrence();
    if (paramRecurrenceInfo.hasEvery()) {
      localEventRecurrence.interval = paramRecurrenceInfo.getEvery();
    }
    if (paramRecurrenceInfo.hasStartMillis()) {
      paramSetReminderAction.setDateTimeMs(paramRecurrenceInfo.getStartMillis());
    }
    if (paramRecurrenceInfo.hasEndMillis())
    {
      Time localTime = new Time();
      localTime.set(paramRecurrenceInfo.getEndMillis());
      localTime.switchTimezone("UTC");
      localTime.normalize(false);
      localEventRecurrence.until = localTime.format2445();
    }
    if (paramRecurrenceInfo.hasNumOccurrences()) {
      localEventRecurrence.count = paramRecurrenceInfo.getNumOccurrences();
    }
    parseDailyPattern(paramRecurrenceInfo.getDailyPattern(), paramSetReminderAction);
    switch (paramRecurrenceInfo.getFrequency())
    {
    default: 
      Log.e("RecurrenceHelper", "Invalid ReminderEntry Frequency: " + paramRecurrenceInfo.getFrequency());
      return;
    case 0: 
      localEventRecurrence.freq = 4;
    }
    for (;;)
    {
      paramSetReminderAction.setRecurrence(localEventRecurrence);
      return;
      localEventRecurrence.freq = 5;
      parseWeeklyPattern(paramRecurrenceInfo.getWeeklyPattern(), localEventRecurrence);
      continue;
      localEventRecurrence.freq = 6;
      parseMonthlyPattern(paramRecurrenceInfo.getMonthlyPattern(), localEventRecurrence);
      continue;
      localEventRecurrence.freq = 7;
      parseYearlyPattern(paramRecurrenceInfo.getYearlyPattern(), localEventRecurrence);
    }
  }
  
  static void parseWeeklyPattern(Sidekick.ReminderEntry.RecurrenceInfo.WeeklyPattern paramWeeklyPattern, EventRecurrence paramEventRecurrence)
  {
    if (paramEventRecurrence == null) {
      Log.w("RecurrenceHelper", "No eventRecurrence to update");
    }
    if (paramWeeklyPattern == null) {
      Log.w("RecurrenceHelper", "Missing weekly pattern");
    }
    for (;;)
    {
      return;
      paramEventRecurrence.bydayCount = paramWeeklyPattern.getWeekDayCount();
      paramEventRecurrence.byday = new int[paramEventRecurrence.bydayCount];
      paramEventRecurrence.bydayNum = new int[paramEventRecurrence.bydayCount];
      for (int i = 0; i < paramEventRecurrence.bydayCount; i++)
      {
        Integer localInteger = Integer.valueOf(convertSidekickRecurrenceInfoToEventRecurrenceWeekday(paramWeeklyPattern.getWeekDay(i)));
        if (localInteger != null) {
          paramEventRecurrence.byday[i] = localInteger.intValue();
        }
      }
    }
  }
  
  static void parseYearlyPattern(Sidekick.ReminderEntry.RecurrenceInfo.YearlyPattern paramYearlyPattern, EventRecurrence paramEventRecurrence)
  {
    if (paramEventRecurrence == null) {
      Log.w("RecurrenceHelper", "No eventRecurrence to update");
    }
    if (paramYearlyPattern == null) {
      Log.w("RecurrenceHelper", "Missing yearly pattern");
    }
    for (;;)
    {
      return;
      if (paramYearlyPattern.hasMonthlyPattern()) {
        parseMonthlyPattern(paramYearlyPattern.getMonthlyPattern(), paramEventRecurrence);
      }
      if (paramYearlyPattern.getYearMonthCount() > 0)
      {
        paramEventRecurrence.bymonthCount = paramYearlyPattern.getYearMonthCount();
        paramEventRecurrence.bymonth = new int[paramEventRecurrence.bymonthCount];
        for (int i = 0; i < paramEventRecurrence.bymonthCount; i++) {
          paramEventRecurrence.bymonth[i] = paramYearlyPattern.getYearMonth(i);
        }
      }
    }
  }
  
  private static void setWeeklyPattern(RecurrenceProtos.Recurrence paramRecurrence, EventRecurrence paramEventRecurrence)
  {
    if ((paramRecurrence == null) || (paramEventRecurrence == null) || (paramRecurrence.getFrequency() != 1)) {
      Log.e("RecurrenceHelper", "The recurrence is not a valid WEEKLY recurrence.");
    }
    for (;;)
    {
      return;
      if (paramRecurrence.getWeeklyPattern().getWeekDayCount() == 0)
      {
        Log.e("RecurrenceHelper", "No weekDay in the recurrence: " + ProtoBufUtils.toString(paramRecurrence));
        return;
      }
      paramEventRecurrence.bydayCount = paramRecurrence.getWeeklyPattern().getWeekDayCount();
      paramEventRecurrence.byday = new int[paramEventRecurrence.bydayCount];
      paramEventRecurrence.bydayNum = new int[paramEventRecurrence.bydayCount];
      for (int i = 0; i < paramRecurrence.getWeeklyPattern().getWeekDayCount(); i++)
      {
        int j = convertCaribouWeekdayToEventRecurrenceWeekday(paramRecurrence.getWeeklyPattern().getWeekDay(i));
        if (j > 0) {
          paramEventRecurrence.byday[i] = j;
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.RecurrenceHelper
 * JD-Core Version:    0.7.0.1
 */