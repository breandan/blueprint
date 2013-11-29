package com.google.android.voicesearch.fragments.reminders;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.search.core.Feature;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DateTimePickerHelper
{
  static final SymbolicTime[] TIMES_OF_DAY;
  private final DropDownElement<Integer> mDatePickerPromptElement;
  private final DropDownElement<Integer> mDateTodayElement;
  private final DropDownElement<Integer> mDateTomorrowElement;
  private final DropDownElement<Integer> mDateWeekendElement;
  private final Map<SymbolicTime, DropDownElement<SymbolicTime>> mTimeElements;
  private final DropDownElement<SymbolicTime> mTimePickerPromptElement;
  
  static
  {
    SymbolicTime[] arrayOfSymbolicTime = new SymbolicTime[5];
    arrayOfSymbolicTime[0] = SymbolicTime.MORNING;
    arrayOfSymbolicTime[1] = SymbolicTime.AFTERNOON;
    arrayOfSymbolicTime[2] = SymbolicTime.EVENING;
    arrayOfSymbolicTime[3] = SymbolicTime.NIGHT;
    arrayOfSymbolicTime[4] = SymbolicTime.TIME_UNSPECIFIED;
    TIMES_OF_DAY = arrayOfSymbolicTime;
  }
  
  public DateTimePickerHelper(Context paramContext)
  {
    String[] arrayOfString1 = paramContext.getResources().getStringArray(2131492939);
    this.mTimeElements = createTimeElements(arrayOfString1);
    this.mTimePickerPromptElement = new DropDownElement(arrayOfString1[5], null, true);
    String[] arrayOfString2 = paramContext.getResources().getStringArray(2131492938);
    this.mDateTodayElement = new DropDownElement(arrayOfString2[0], Integer.valueOf(0), false);
    this.mDateTomorrowElement = new DropDownElement(arrayOfString2[1], Integer.valueOf(1), false);
    this.mDateWeekendElement = new DropDownElement(arrayOfString2[2], Integer.valueOf(2), false);
    this.mDatePickerPromptElement = new DropDownElement(arrayOfString2[3], null, true);
  }
  
  private static <T> void addPickerElementToMap(Map<T, DropDownElement<T>> paramMap, T paramT, String paramString)
  {
    paramMap.put(paramT, new DropDownElement(paramString, paramT, false));
  }
  
  private static Map<SymbolicTime, DropDownElement<SymbolicTime>> createTimeElements(String[] paramArrayOfString)
  {
    HashMap localHashMap = Maps.newHashMap();
    for (int i = 0; i < TIMES_OF_DAY.length; i++) {
      addPickerElementToMap(localHashMap, TIMES_OF_DAY[i], paramArrayOfString[i]);
    }
    return localHashMap;
  }
  
  public static List<SymbolicTime> getAvailableTimes(long paramLong)
  {
    return getAvailableTimes(false, paramLong, System.currentTimeMillis());
  }
  
  public static List<SymbolicTime> getAvailableTimes(boolean paramBoolean, long paramLong1, long paramLong2)
  {
    ArrayList localArrayList;
    int i;
    SymbolicTime[] arrayOfSymbolicTime;
    int j;
    int k;
    if ((!paramBoolean) && (TimeUtilities.isSameDay(paramLong2, paramLong1)))
    {
      localArrayList = Lists.newArrayListWithCapacity(TIMES_OF_DAY.length);
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(paramLong2);
      i = localCalendar.get(11);
      arrayOfSymbolicTime = TIMES_OF_DAY;
      j = arrayOfSymbolicTime.length;
      k = 0;
    }
    while (k < j)
    {
      SymbolicTime localSymbolicTime = arrayOfSymbolicTime[k];
      if (isAvailableAtHour(i, localSymbolicTime)) {
        localArrayList.add(localSymbolicTime);
      }
      k++;
      continue;
      localArrayList = Lists.newArrayList(TIMES_OF_DAY);
    }
    return localArrayList;
  }
  
  private static boolean isAvailableAtHour(int paramInt, SymbolicTime paramSymbolicTime)
  {
    return paramSymbolicTime.defaultHour > paramInt + 1;
  }
  
  public static boolean isDateTodayAvailable(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    return localCalendar.get(11) < 23;
  }
  
  public static boolean isDateWeekendAvailable(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    int i = localCalendar.get(7);
    return (Feature.REMINDERS_WEEKEND.isEnabled()) && ((isDateTodayAvailable(paramLong)) || (i != 6)) && (i != 7) && (i != 1);
  }
  
  public static <T> DropDownElement<T>[] safeListToArray(List<DropDownElement<T>> paramList)
  {
    return (DropDownElement[])paramList.toArray(new DropDownElement[paramList.size()]);
  }
  
  public List<DropDownElement<Integer>> getAvailableDateElements(long paramLong)
  {
    ArrayList localArrayList = Lists.newArrayList();
    if (isDateTodayAvailable(paramLong)) {
      localArrayList.add(this.mDateTodayElement);
    }
    localArrayList.add(this.mDateTomorrowElement);
    if (isDateWeekendAvailable(paramLong)) {
      localArrayList.add(this.mDateWeekendElement);
    }
    localArrayList.add(this.mDatePickerPromptElement);
    return localArrayList;
  }
  
  public List<DropDownElement<SymbolicTime>> getAvailableTimeElements(List<SymbolicTime> paramList)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(2 + paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SymbolicTime localSymbolicTime = (SymbolicTime)localIterator.next();
      localArrayList.add(Preconditions.checkNotNull(this.mTimeElements.get(localSymbolicTime)));
    }
    localArrayList.add(this.mTimePickerPromptElement);
    return localArrayList;
  }
  
  public DropDownElement<Integer> getDatePickerPromptValueElement()
  {
    return this.mDatePickerPromptElement;
  }
  
  public DropDownElement<Integer> getDateTodayElement()
  {
    return this.mDateTodayElement;
  }
  
  public DropDownElement<Integer> getDateTomorrowElement()
  {
    return this.mDateTomorrowElement;
  }
  
  public DropDownElement<Integer> getDateWeekendElement()
  {
    return this.mDateWeekendElement;
  }
  
  public DropDownElement<SymbolicTime> getTimeElement(SymbolicTime paramSymbolicTime)
  {
    return (DropDownElement)Preconditions.checkNotNull(this.mTimeElements.get(paramSymbolicTime));
  }
  
  public DropDownElement<SymbolicTime> getTimePickerPromptValueElement()
  {
    return this.mTimePickerPromptElement;
  }
  
  public static class DropDownElement<T>
  {
    @Nonnull
    final String label;
    private final boolean mIsPrompt;
    @Nullable
    final T object;
    
    DropDownElement(String paramString, T paramT, boolean paramBoolean)
    {
      this.label = paramString;
      this.object = paramT;
      this.mIsPrompt = paramBoolean;
    }
    
    public boolean isPrompt()
    {
      return this.mIsPrompt;
    }
    
    public String toString()
    {
      return this.label;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.DateTimePickerHelper
 * JD-Core Version:    0.7.0.1
 */