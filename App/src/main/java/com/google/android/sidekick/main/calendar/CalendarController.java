package com.google.android.sidekick.main.calendar;

import com.google.android.sidekick.main.inject.SidekickInjector;

public abstract interface CalendarController
{
  public abstract CalendarDataProvider newCalendarDataProvider();
  
  public abstract void startCalendar(SidekickInjector paramSidekickInjector);
  
  public abstract void stopCalendar(SidekickInjector paramSidekickInjector);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarController
 * JD-Core Version:    0.7.0.1
 */