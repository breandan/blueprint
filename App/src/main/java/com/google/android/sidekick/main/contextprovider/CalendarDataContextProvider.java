package com.google.android.sidekick.main.contextprovider;

import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.CalendarDataContext;

public class CalendarDataContextProvider
{
  private final CalendarDataProvider mCalendarDataProvider;
  
  public CalendarDataContextProvider(CalendarDataProvider paramCalendarDataProvider)
  {
    this.mCalendarDataProvider = paramCalendarDataProvider;
  }
  
  public void addCalendarData(CardRenderingContext paramCardRenderingContext, String paramString)
  {
    Calendar.CalendarData localCalendarData = this.mCalendarDataProvider.getCalendarDataByServerHash(paramString);
    CalendarDataContext localCalendarDataContext = (CalendarDataContext)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(CalendarDataContext.BUNDLE_KEY, new CalendarDataContext());
    if (localCalendarData != null) {
      localCalendarDataContext.setCalendarData(paramString, localCalendarData);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.CalendarDataContextProvider
 * JD-Core Version:    0.7.0.1
 */