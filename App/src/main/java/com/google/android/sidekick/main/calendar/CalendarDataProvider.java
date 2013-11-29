package com.google.android.sidekick.main.calendar;

import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.UploadCalendarData;
import java.util.Collection;

public abstract interface CalendarDataProvider
{
  public abstract void addCalendarDataToClientUserData(Sidekick.ClientUserData paramClientUserData);
  
  public abstract void clearAllEventNotifiedMarkers();
  
  public abstract void clearData();
  
  public abstract boolean didGettingEventsFail();
  
  public abstract Calendar.CalendarData getCalendarDataByServerHash(String paramString);
  
  public abstract Iterable<Sidekick.UploadCalendarData> getCalendarDataForNotify();
  
  public abstract Collection<Calendar.CalendarInfo> getCalendarsList();
  
  public abstract Long getEarliestNotificationTimeSecs();
  
  public abstract Iterable<Calendar.CalendarData> getNotifyingCalendarData();
  
  public abstract void initialize();
  
  public abstract boolean markEventAsDismissed(long paramLong);
  
  public abstract boolean markEventAsNotified(long paramLong);
  
  public abstract boolean markEventNotificationAsDismissed(long paramLong);
  
  public abstract boolean updateWithNewEntryTreeFromServer(Sidekick.EntryTree paramEntryTree);
  
  public abstract boolean updateWithNewEventData(Collection<Calendar.EventData> paramCollection, Collection<Calendar.CalendarInfo> paramCollection1, boolean paramBoolean);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarDataProvider
 * JD-Core Version:    0.7.0.1
 */