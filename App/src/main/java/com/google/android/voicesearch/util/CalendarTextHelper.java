package com.google.android.voicesearch.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.google.android.sidekick.shared.util.TimeUtilities;
import java.util.List;

public class CalendarTextHelper
{
  private final Context mContext;
  private final Resources mResources;
  
  public CalendarTextHelper(Context paramContext)
  {
    this.mResources = paramContext.getResources();
    this.mContext = paramContext;
  }
  
  String createCalendarQueryTts(CalendarHelper.CalendarEvent paramCalendarEvent, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    if ((paramBoolean4) && (!paramBoolean2) && (!paramBoolean3) && (!paramBoolean1))
    {
      String str2 = paramCalendarEvent.getLocation();
      if (TextUtils.isEmpty(str2)) {
        return this.mResources.getString(2131363548);
      }
      return this.mResources.getString(2131363542, new Object[] { str2 });
    }
    if ((paramBoolean2) && (!paramBoolean1) && (!paramBoolean3) && (!paramBoolean4))
    {
      Resources localResources6 = this.mResources;
      Object[] arrayOfObject6 = new Object[1];
      arrayOfObject6[0] = DateUtils.formatSameDayTime(paramCalendarEvent.getStartTimeMs(), paramCalendarEvent.getStartTimeMs(), 3, 3);
      return localResources6.getString(2131363544, arrayOfObject6);
    }
    if ((paramBoolean2) && (paramBoolean3) && (!paramBoolean1) && (!paramBoolean4))
    {
      Resources localResources5 = this.mResources;
      Object[] arrayOfObject5 = new Object[1];
      arrayOfObject5[0] = TimeUtilities.formatDisplayTime(this.mContext, paramCalendarEvent.getStartTimeMs(), 0);
      return localResources5.getString(2131363546, arrayOfObject5);
    }
    if ((paramBoolean1) && (!paramBoolean2) && (!paramBoolean3) && (!paramBoolean4))
    {
      Resources localResources4 = this.mResources;
      Object[] arrayOfObject4 = new Object[1];
      arrayOfObject4[0] = paramCalendarEvent.getSummary();
      return localResources4.getString(2131363541, arrayOfObject4);
    }
    if ((paramBoolean4) && (paramBoolean1) && (!paramBoolean2) && (!paramBoolean3))
    {
      String str1 = paramCalendarEvent.getLocation();
      if (TextUtils.isEmpty(str1)) {
        return this.mResources.getString(2131363549);
      }
      Resources localResources3 = this.mResources;
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = paramCalendarEvent.getSummary();
      arrayOfObject3[1] = str1;
      return localResources3.getString(2131363543, arrayOfObject3);
    }
    if ((paramBoolean2) && (paramBoolean1) && (!paramBoolean3) && (!paramBoolean4))
    {
      Resources localResources2 = this.mResources;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = paramCalendarEvent.getSummary();
      arrayOfObject2[1] = DateUtils.formatSameDayTime(paramCalendarEvent.getStartTimeMs(), paramCalendarEvent.getStartTimeMs(), 3, 3);
      return localResources2.getString(2131363545, arrayOfObject2);
    }
    if ((paramBoolean2) && (paramBoolean3) && (paramBoolean1) && (!paramBoolean4))
    {
      Resources localResources1 = this.mResources;
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramCalendarEvent.getSummary();
      arrayOfObject1[1] = TimeUtilities.formatDisplayTime(this.mContext, paramCalendarEvent.getStartTimeMs(), 0);
      return localResources1.getString(2131363547, arrayOfObject1);
    }
    throw new UnsupportedOperationException("Can't answer include location: " + paramBoolean4 + ", " + "include summary: " + paramBoolean1 + ", " + "include date: " + paramBoolean3 + ", " + "include time: " + paramBoolean2);
  }
  
  public String formatDisplayTime(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    int i = 2560;
    long l = System.currentTimeMillis();
    if ((!TimeUtilities.isSameDay(l, paramLong1)) || (!TimeUtilities.isSameDay(l, paramLong2)))
    {
      if ((!TimeUtilities.isWithinOneWeek(l, paramLong1)) || (!TimeUtilities.isWithinOneWeek(l, paramLong2))) {
        break label75;
      }
      i |= 0x2;
    }
    for (;;)
    {
      if (paramBoolean) {
        i |= 0x1;
      }
      return DateUtils.formatDateRange(this.mContext, paramLong1, paramLong2, i);
      label75:
      i |= 0x10;
    }
  }
  
  @Deprecated
  public String getDisplaySummary(String paramString, List<String> paramList)
  {
    if (TextUtils.isEmpty(paramString)) {
      paramString = this.mResources.getString(2131363536);
    }
    if ((paramList == null) || (paramList.isEmpty())) {
      return paramString;
    }
    Resources localResources = this.mResources;
    int i = paramList.size();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = TextUtils.join(", ", paramList);
    return localResources.getQuantityString(2131558449, i, arrayOfObject);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.util.CalendarTextHelper
 * JD-Core Version:    0.7.0.1
 */