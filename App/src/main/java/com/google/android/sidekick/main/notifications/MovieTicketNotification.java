package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Movie;
import com.google.geo.sidekick.Sidekick.MovieTicketEntry;

public class MovieTicketNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.MovieTicketEntry mMovieTicket;
  
  public MovieTicketNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
    this.mMovieTicket = paramEntry.getMovieTicketEntry();
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = DateUtils.formatDateTime(paramContext, 1000L * this.mMovieTicket.getShowtimeSeconds(), 1);
    return paramContext.getString(2131362585, arrayOfObject);
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return this.mMovieTicket.getMovie().getTitle();
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
 * Qualified Name:     com.google.android.sidekick.main.notifications.MovieTicketNotification
 * JD-Core Version:    0.7.0.1
 */