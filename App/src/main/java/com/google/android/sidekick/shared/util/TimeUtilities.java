package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.webkit.DateSorter;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtilities
{
  private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}\\/\\d{1,2}\\/\\d{4}");
  
  private static CharSequence ensureUserDateFormatUsed(Context paramContext, CharSequence paramCharSequence, long paramLong)
  {
    Matcher localMatcher = DATE_PATTERN.matcher(paramCharSequence);
    if (localMatcher.find()) {
      paramCharSequence = localMatcher.replaceFirst(android.text.format.DateFormat.getDateFormat(paramContext).format(new Date(paramLong)));
    }
    return paramCharSequence;
  }
  
  public static CharSequence formatDateTimeFromRFC3339(Context paramContext, String paramString, int paramInt)
  {
    Time localTime = new Time();
    if ((!localTime.parse3339(paramString)) && (!localTime.allDay)) {
      localTime.switchTimezone("UTC");
    }
    long l = localTime.toMillis(true);
    if (localTime.allDay)
    {
      if (DateUtils.isToday(l)) {
        return new DateSorter(paramContext).getLabel(0);
      }
      return ensureUserDateFormatUsed(paramContext, DateUtils.getRelativeTimeSpanString(l, System.currentTimeMillis(), 86400000L, paramInt | 0x10), l);
    }
    return formatDisplayTime(paramContext, l, paramInt);
  }
  
  public static CharSequence formatDateTimeRangeFromRFC3339(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    Time localTime1 = new Time();
    localTime1.parse3339(paramString1);
    long l1 = localTime1.toMillis(true);
    Time localTime2 = new Time();
    localTime2.parse3339(paramString2);
    long l2 = localTime2.toMillis(true);
    if ((localTime1.allDay) && (localTime2.allDay)) {
      l2 += 1000L;
    }
    for (;;)
    {
      if ((!DateUtils.isToday(l1)) || (!DateUtils.isToday(l2))) {
        paramInt |= 0x10;
      }
      return DateUtils.formatDateRange(paramContext, l1, l2, paramInt);
      paramInt |= 0x1;
    }
  }
  
  public static CharSequence formatDisplayTime(Context paramContext, long paramLong, int paramInt)
  {
    long l = System.currentTimeMillis();
    if (DateUtils.isToday(paramLong)) {
      return DateUtils.formatDateTime(paramContext, paramLong, showTimeOnlyFlags(paramInt));
    }
    if (isTomorrow(paramLong, l))
    {
      String str2 = DateUtils.formatDateTime(paramContext, paramLong, showTimeOnlyFlags(paramInt));
      return paramContext.getResources().getString(2131362777, new Object[] { str2 });
    }
    if (wasYesterday(paramLong, l))
    {
      String str1 = DateUtils.formatDateTime(paramContext, paramLong, showTimeOnlyFlags(paramInt));
      return paramContext.getResources().getString(2131362778, new Object[] { str1 });
    }
    if (paramLong - System.currentTimeMillis() > 172800000L)
    {
      if (Build.VERSION.SDK_INT <= 16) {
        return DateUtils.formatDateTime(paramContext, paramLong, 0x1 | paramInt | 0x10);
      }
      if ((paramInt & 0x2) != 0) {
        return DateUtils.formatDateTime(paramContext, paramLong, paramInt);
      }
    }
    return ensureUserDateFormatUsed(paramContext, DateUtils.getRelativeDateTimeString(paramContext, paramLong, 86400000L, 172800000L, paramInt | 0x10), paramLong);
  }
  
  public static String getElapsedString(Context paramContext, long paramLong, boolean paramBoolean)
  {
    if (paramLong < 3600000L)
    {
      int i4 = (int)TimeUnit.MILLISECONDS.toMinutes(paramLong);
      if (i4 < 1) {
        i4 = 1;
      }
      if (paramBoolean) {}
      for (int i5 = 2131558412;; i5 = 2131558411)
      {
        Resources localResources5 = paramContext.getResources();
        Object[] arrayOfObject5 = new Object[1];
        arrayOfObject5[0] = Integer.valueOf(i4);
        return localResources5.getQuantityString(i5, i4, arrayOfObject5);
      }
    }
    if (paramLong < 86400000L)
    {
      int i2 = (int)TimeUnit.MILLISECONDS.toHours(paramLong);
      if (paramBoolean) {}
      for (int i3 = 2131558410;; i3 = 2131558409)
      {
        Resources localResources4 = paramContext.getResources();
        Object[] arrayOfObject4 = new Object[1];
        arrayOfObject4[0] = Integer.valueOf(i2);
        return localResources4.getQuantityString(i3, i2, arrayOfObject4);
      }
    }
    if (paramLong < 2592000000L)
    {
      int n = (int)TimeUnit.MILLISECONDS.toDays(paramLong);
      if (paramBoolean) {}
      for (int i1 = 2131558414;; i1 = 2131558413)
      {
        Resources localResources3 = paramContext.getResources();
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Integer.valueOf(n);
        return localResources3.getQuantityString(i1, n, arrayOfObject3);
      }
    }
    if (paramLong < 31536000000L)
    {
      int k = (int)(paramLong / 2592000000L);
      if (paramBoolean) {}
      for (int m = 2131558416;; m = 2131558415)
      {
        Resources localResources2 = paramContext.getResources();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.valueOf(k);
        return localResources2.getQuantityString(m, k, arrayOfObject2);
      }
    }
    int i = (int)(paramLong / 31536000000L);
    if (paramBoolean) {}
    for (int j = 2131558418;; j = 2131558417)
    {
      Resources localResources1 = paramContext.getResources();
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(i);
      return localResources1.getQuantityString(j, i, arrayOfObject1);
    }
  }
  
  public static String getEtaString(Context paramContext, int paramInt, boolean paramBoolean)
  {
    int i = 2131558409;
    if (paramInt >= 10080)
    {
      Object[] arrayOfObject9 = new Object[1];
      arrayOfObject9[0] = Integer.valueOf(paramInt / 1440);
      return paramContext.getString(2131362216, arrayOfObject9);
    }
    if (paramInt >= 1440)
    {
      Object[] arrayOfObject8 = new Object[2];
      arrayOfObject8[0] = Integer.valueOf(paramInt / 1440);
      arrayOfObject8[1] = Integer.valueOf(paramInt % 1440 / 60);
      return paramContext.getString(2131362217, arrayOfObject8);
    }
    if (paramInt >= 60)
    {
      int j = paramInt / 60;
      int k = paramInt % 60;
      if (k > 0)
      {
        if (paramBoolean)
        {
          Object[] arrayOfObject7 = new Object[2];
          arrayOfObject7[0] = Integer.valueOf(j);
          arrayOfObject7[1] = Integer.valueOf(k);
          return paramContext.getString(2131362220, arrayOfObject7);
        }
        Object[] arrayOfObject4 = new Object[2];
        Resources localResources4 = paramContext.getResources();
        Object[] arrayOfObject5 = new Object[1];
        arrayOfObject5[0] = Integer.valueOf(j);
        arrayOfObject4[0] = localResources4.getQuantityString(i, j, arrayOfObject5);
        Resources localResources5 = paramContext.getResources();
        Object[] arrayOfObject6 = new Object[1];
        arrayOfObject6[0] = Integer.valueOf(k);
        arrayOfObject4[1] = localResources5.getQuantityString(2131558411, k, arrayOfObject6);
        return paramContext.getString(2131362224, arrayOfObject4);
      }
      Resources localResources3 = paramContext.getResources();
      if (paramBoolean) {
        i = 2131558410;
      }
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(j);
      return localResources3.getQuantityString(i, j, arrayOfObject3);
    }
    if (paramBoolean)
    {
      Resources localResources2 = paramContext.getResources();
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(paramInt);
      return localResources2.getQuantityString(2131558412, paramInt, arrayOfObject2);
    }
    Resources localResources1 = paramContext.getResources();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(paramInt);
    return localResources1.getQuantityString(2131558411, paramInt, arrayOfObject1);
  }
  
  public static String getEtaString(Context paramContext, Sidekick.CommuteSummary paramCommuteSummary, boolean paramBoolean)
  {
    return getEtaString(paramContext, paramCommuteSummary.getTravelTimeWithoutDelayInMinutes() + paramCommuteSummary.getTrafficDelayInMinutes(), paramBoolean);
  }
  
  public static String getRelativeElapsedString(Context paramContext, int paramInt, long paramLong, boolean paramBoolean)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = getElapsedString(paramContext, paramLong, paramBoolean);
    return paramContext.getString(paramInt, arrayOfObject);
  }
  
  public static String getRelativeElapsedString(Context paramContext, long paramLong, boolean paramBoolean)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = getElapsedString(paramContext, paramLong, paramBoolean);
    return paramContext.getString(2131362744, arrayOfObject);
  }
  
  static <T> T getTimeOfDayDependentObject(int paramInt, T paramT1, T paramT2, T paramT3, T paramT4, T paramT5)
  {
    if ((paramInt >= 2) && (paramInt < 5)) {
      return paramT5;
    }
    if ((paramInt >= 5) && (paramInt < 12)) {
      return paramT1;
    }
    if ((paramInt >= 12) && (paramInt < 17)) {
      return paramT2;
    }
    if ((paramInt >= 17) && (paramInt < 20)) {
      return paramT3;
    }
    return paramT4;
  }
  
  public static <T> T getTimeOfDayDependentObject(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5)
  {
    return getTimeOfDayDependentObject(Calendar.getInstance().get(11), paramT1, paramT2, paramT3, paramT4, paramT5);
  }
  
  public static boolean isSameDay(long paramLong1, long paramLong2)
  {
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTimeInMillis(paramLong1);
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.setTimeInMillis(paramLong2);
    return (localCalendar1.get(1) == localCalendar2.get(1)) && (localCalendar1.get(6) == localCalendar2.get(6));
  }
  
  public static boolean isToday(long paramLong)
  {
    return isSameDay(paramLong, System.currentTimeMillis());
  }
  
  public static boolean isTomorrow(long paramLong)
  {
    return isTomorrow(paramLong, System.currentTimeMillis());
  }
  
  public static boolean isTomorrow(long paramLong1, long paramLong2)
  {
    Time localTime = new Time();
    localTime.set(paramLong1);
    int i = Time.getJulianDay(paramLong1, localTime.gmtoff);
    localTime.set(paramLong2);
    return i - Time.getJulianDay(paramLong2, localTime.gmtoff) == 1;
  }
  
  public static boolean isWithinOneWeek(long paramLong1, long paramLong2)
  {
    long l = paramLong1 + 604800000L;
    return (paramLong1 <= paramLong2) && (paramLong2 <= l) && (!isSameDay(l, paramLong2));
  }
  
  private static int showTimeOnlyFlags(int paramInt)
  {
    return 0x1 | 0xFFFFFFFD & paramInt & 0xFFFFFFEF;
  }
  
  public static boolean wasYesterday(long paramLong1, long paramLong2)
  {
    return isTomorrow(paramLong2, paramLong1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.TimeUtilities
 * JD-Core Version:    0.7.0.1
 */