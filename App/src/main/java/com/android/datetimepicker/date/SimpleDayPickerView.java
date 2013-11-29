package com.android.datetimepicker.date;

import android.content.Context;

public class SimpleDayPickerView
  extends DayPickerView
{
  public SimpleDayPickerView(Context paramContext, DatePickerController paramDatePickerController)
  {
    super(paramContext, paramDatePickerController);
  }
  
  public MonthAdapter createMonthAdapter(Context paramContext, DatePickerController paramDatePickerController)
  {
    return new SimpleMonthAdapter(paramContext, paramDatePickerController);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.SimpleDayPickerView
 * JD-Core Version:    0.7.0.1
 */