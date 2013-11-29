package com.android.recurrencepicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.TimeFormatException;

public class EventRecurrenceFormatter
{
  private static int[] mMonthRepeatByDayOfWeekIds;
  private static String[][] mMonthRepeatByDayOfWeekStrs;
  
  private static void cacheMonthRepeatStrings(Resources paramResources, int paramInt)
  {
    if (mMonthRepeatByDayOfWeekIds == null)
    {
      int[] arrayOfInt = new int[7];
      arrayOfInt[0] = R.array.repeat_by_nth_sun;
      arrayOfInt[1] = R.array.repeat_by_nth_mon;
      arrayOfInt[2] = R.array.repeat_by_nth_tues;
      arrayOfInt[3] = R.array.repeat_by_nth_wed;
      arrayOfInt[4] = R.array.repeat_by_nth_thurs;
      arrayOfInt[5] = R.array.repeat_by_nth_fri;
      arrayOfInt[6] = R.array.repeat_by_nth_sat;
      mMonthRepeatByDayOfWeekIds = arrayOfInt;
    }
    if (mMonthRepeatByDayOfWeekStrs == null) {
      mMonthRepeatByDayOfWeekStrs = new String[7][];
    }
    if (mMonthRepeatByDayOfWeekStrs[paramInt] == null) {
      mMonthRepeatByDayOfWeekStrs[paramInt] = paramResources.getStringArray(mMonthRepeatByDayOfWeekIds[paramInt]);
    }
  }
  
  private static String dayToString(int paramInt1, int paramInt2)
  {
    return DateUtils.getDayOfWeekString(dayToUtilDay(paramInt1), paramInt2);
  }
  
  private static int dayToUtilDay(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("bad day argument: " + paramInt);
    case 65536: 
      return 1;
    case 131072: 
      return 2;
    case 262144: 
      return 3;
    case 524288: 
      return 4;
    case 1048576: 
      return 5;
    case 2097152: 
      return 6;
    }
    return 7;
  }
  
  public static String getRepeatString(Context paramContext, Resources paramResources, EventRecurrence paramEventRecurrence, boolean paramBoolean, long paramLong)
  {
    String str1 = "";
    StringBuilder localStringBuilder1;
    if (paramBoolean)
    {
      localStringBuilder1 = new StringBuilder();
      if (paramEventRecurrence.until == null) {}
    }
    try
    {
      Time localTime2 = new Time();
      localTime2.parse(paramEventRecurrence.until);
      String str3 = DateUtils.formatDateTime(paramContext, localTime2.toMillis(false), 131072);
      localStringBuilder1.append(paramResources.getString(R.string.endByDate, new Object[] { str3 }));
      label79:
      if (paramEventRecurrence.count > 0)
      {
        int i4 = R.plurals.endByCount;
        int i5 = paramEventRecurrence.count;
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Integer.valueOf(paramEventRecurrence.count);
        localStringBuilder1.append(paramResources.getQuantityString(i4, i5, arrayOfObject3));
      }
      str1 = localStringBuilder1.toString();
      if (paramEventRecurrence.interval <= 1) {}
      for (int i = 1;; i = paramEventRecurrence.interval) {
        switch (paramEventRecurrence.freq)
        {
        default: 
          return null;
        }
      }
      StringBuilder localStringBuilder5 = new StringBuilder();
      int i3 = R.plurals.daily;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(i);
      return paramResources.getQuantityString(i3, i, arrayOfObject2) + str1;
      if (paramEventRecurrence.repeatsOnEveryWeekDay()) {
        return paramResources.getString(R.string.every_weekday) + str1;
      }
      int m = 20;
      if (paramEventRecurrence.bydayCount == 1) {
        m = 10;
      }
      StringBuilder localStringBuilder3 = new StringBuilder();
      if (paramEventRecurrence.bydayCount > 0)
      {
        int i1 = -1 + paramEventRecurrence.bydayCount;
        for (int i2 = 0; i2 < i1; i2++)
        {
          localStringBuilder3.append(dayToString(paramEventRecurrence.byday[i2], m));
          localStringBuilder3.append(", ");
        }
        localStringBuilder3.append(dayToString(paramEventRecurrence.byday[i1], m));
      }
      for (String str2 = localStringBuilder3.toString();; str2 = dayToString(EventRecurrence.timeDay2Day(paramEventRecurrence.startDate.weekDay), 10))
      {
        StringBuilder localStringBuilder4 = new StringBuilder();
        int n = R.plurals.weekly;
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Integer.valueOf(i);
        arrayOfObject1[1] = str2;
        return paramResources.getQuantityString(n, i, arrayOfObject1) + str1;
        if (paramEventRecurrence.startDate == null) {
          return null;
        }
      }
      if (paramEventRecurrence.bydayCount == 1)
      {
        Time localTime1 = paramEventRecurrence.startDate;
        if (localTime1 == null)
        {
          localTime1 = new Time();
          localTime1.set(paramLong);
        }
        int j = localTime1.weekDay;
        cacheMonthRepeatStrings(paramResources, j);
        int k = (-1 + localTime1.monthDay) / 7;
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(paramResources.getString(R.string.monthly));
        localStringBuilder2.append(" (");
        localStringBuilder2.append(mMonthRepeatByDayOfWeekStrs[j][k]);
        localStringBuilder2.append(")");
        localStringBuilder2.append(str1);
        return localStringBuilder2.toString();
      }
      return paramResources.getString(R.string.monthly) + str1;
      return paramResources.getString(R.string.yearly_plain) + str1;
    }
    catch (TimeFormatException localTimeFormatException)
    {
      break label79;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.recurrencepicker.EventRecurrenceFormatter
 * JD-Core Version:    0.7.0.1
 */