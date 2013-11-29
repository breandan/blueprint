package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.MoonshineEventTicketEntry;

public class MoonshineEventTicketNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.MoonshineEventTicketEntry mEventTicket;
  
  public MoonshineEventTicketNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
    this.mEventTicket = paramEntry.getMoonshineEventTicketEntry();
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = DateUtils.formatDateTime(paramContext, this.mEventTicket.getStartTimeMs(), 1);
    return paramContext.getString(2131362599, arrayOfObject);
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return this.mEventTicket.getTitle();
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.EVENT_TIME_TO_LEAVE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130837733;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.MoonshineEventTicketNotification
 * JD-Core Version:    0.7.0.1
 */