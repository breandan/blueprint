package com.android.datetimepicker.date;

import android.content.Context;

public class SimpleMonthAdapter
  extends MonthAdapter
{
  public SimpleMonthAdapter(Context paramContext, DatePickerController paramDatePickerController)
  {
    super(paramContext, paramDatePickerController);
  }
  
  public MonthView createMonthView(Context paramContext)
  {
    return new SimpleMonthView(paramContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.SimpleMonthAdapter
 * JD-Core Version:    0.7.0.1
 */