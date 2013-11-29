package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.sidekick.shared.util.CalendarDataUtil;

public class EmailAttendeesAction
  implements NotificationAction
{
  private final Calendar.CalendarData mCalendarData;
  
  public EmailAttendeesAction(Calendar.CalendarData paramCalendarData)
  {
    this.mCalendarData = paramCalendarData;
  }
  
  public int getActionIcon()
  {
    return 2130837794;
  }
  
  public String getActionString(Context paramContext)
  {
    return paramContext.getString(2131362704);
  }
  
  public Intent getCallbackIntent(Context paramContext)
  {
    return CalendarDataUtil.createEmailAttendeesIntent(this.mCalendarData.getEventData().getEventId());
  }
  
  public String getCallbackType()
  {
    return "broadcast";
  }
  
  public String getLogString()
  {
    return "EMAIL_GUESTS";
  }
  
  public boolean isActive()
  {
    return this.mCalendarData.getEventData().getNumberOfAttendees() >= 2;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.EmailAttendeesAction
 * JD-Core Version:    0.7.0.1
 */