package com.android.datetimepicker.date;

public abstract interface DatePickerController
{
  public abstract int getFirstDayOfWeek();
  
  public abstract int getMaxYear();
  
  public abstract int getMinYear();
  
  public abstract MonthAdapter.CalendarDay getSelectedDay();
  
  public abstract void onDayOfMonthSelected(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void onYearSelected(int paramInt);
  
  public abstract void registerOnDateChangedListener(DatePickerDialog.OnDateChangedListener paramOnDateChangedListener);
  
  public abstract void tryVibrate();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.DatePickerController
 * JD-Core Version:    0.7.0.1
 */