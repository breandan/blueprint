package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class FlightStatusFormatter
{
  private static final String TAG = FlightStatusFormatter.class.getSimpleName();
  private final Context mContext;
  
  public FlightStatusFormatter(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public String formatDateTime(Calendar paramCalendar, int paramInt)
  {
    Formatter localFormatter = new Formatter(new StringBuilder());
    long l = paramCalendar.getTime().getTime();
    TimeZone localTimeZone = paramCalendar.getTimeZone();
    if (localTimeZone != null) {
      l += localTimeZone.getOffset(l);
    }
    DateUtils.formatDateRange(this.mContext, localFormatter, l, l, paramInt, "UTC");
    return localFormatter.toString();
  }
  
  public String formatTime(Calendar paramCalendar)
  {
    java.text.DateFormat localDateFormat = android.text.format.DateFormat.getTimeFormat(this.mContext);
    localDateFormat.setTimeZone(paramCalendar.getTimeZone());
    return localDateFormat.format(paramCalendar.getTime());
  }
  
  public int getColorForStatus(int paramInt)
  {
    Resources localResources = this.mContext.getResources();
    switch (paramInt)
    {
    default: 
      return localResources.getColor(17170450);
    case 1: 
    case 2: 
    case 3: 
      return localResources.getColor(2131230842);
    }
    return localResources.getColor(2131230840);
  }
  
  public CharSequence getFlightStatus(int paramInt, Calendar paramCalendar)
  {
    String str1 = formatDateTime(paramCalendar, 524310);
    int i = getColorForStatus(paramInt);
    String str2 = getStatusSummary(paramInt);
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(0xFFFFFF & i);
    String str3 = String.format(localLocale, "#%1$h", arrayOfObject);
    return Html.fromHtml(this.mContext.getString(2131362309, new Object[] { str3, str2, str1 }));
  }
  
  public CharSequence getFormattedDepartureTime(Calendar paramCalendar)
  {
    String str = formatDateTime(paramCalendar, 18);
    return Html.fromHtml(this.mContext.getString(2131362312, new Object[] { str }));
  }
  
  public String getStatusSummary(int paramInt)
  {
    String[] arrayOfString = this.mContext.getResources().getStringArray(2131492877);
    if (paramInt >= arrayOfString.length)
    {
      Log.w(TAG, "Unknown status code " + paramInt);
      paramInt = 0;
    }
    return arrayOfString[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.FlightStatusFormatter
 * JD-Core Version:    0.7.0.1
 */