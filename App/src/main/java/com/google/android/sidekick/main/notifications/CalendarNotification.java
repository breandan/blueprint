package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Notification;
import java.util.Date;
import javax.annotation.Nullable;

public class CalendarNotification
  extends AbstractSingleEntryNotification
{
  private final Calendar.CalendarData mCalendarData;
  private final Sidekick.CalendarEntry mCalendarEntry;
  @Nullable
  private final Sidekick.Location mCurrentLocation;
  private final Sidekick.Location mDestination;
  private final DirectionsLauncher mDirectionsLauncher;
  
  public CalendarNotification(Sidekick.Entry paramEntry, CalendarDataProvider paramCalendarDataProvider, @Nullable Sidekick.Location paramLocation1, Sidekick.Location paramLocation2, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry);
    this.mCalendarEntry = paramEntry.getCalendarEntry();
    this.mCalendarData = paramCalendarDataProvider.getCalendarDataByServerHash(this.mCalendarEntry.getHash());
    this.mCurrentLocation = paramLocation1;
    this.mDestination = paramLocation2;
    this.mDirectionsLauncher = paramDirectionsLauncher;
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    if (this.mCalendarEntry.hasRoute()) {}
    for (Sidekick.CommuteSummary localCommuteSummary = this.mCalendarEntry.getRoute();; localCommuteSummary = null) {
      return ImmutableList.of(new NavigateAction(NavigationContext.fromRenderingContext(paramCardRenderingContext), this.mDirectionsLauncher, this.mCurrentLocation, this.mDestination, localCommuteSummary), new EmailAttendeesAction(this.mCalendarData));
    }
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (isLowPriorityNotification()) {
      return null;
    }
    int i;
    if (this.mCalendarEntry.hasTravelTimeSeconds()) {
      i = this.mCalendarEntry.getTravelTimeSeconds();
    }
    for (;;)
    {
      Date localDate = new Date(1000L * (this.mCalendarData.getEventData().getStartTimeSeconds() - i));
      java.text.DateFormat localDateFormat = android.text.format.DateFormat.getTimeFormat(paramContext);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localDateFormat.format(localDate);
      return paramContext.getString(2131362272, arrayOfObject);
      boolean bool = this.mCalendarData.getServerData().hasTravelTimeMinutes();
      i = 0;
      if (bool) {
        i = 60 * this.mCalendarData.getServerData().getTravelTimeMinutes();
      }
    }
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (isLowPriorityNotification()) {
      return getDefaultNotificationText();
    }
    Sidekick.Entry localEntry = getEntry();
    if ((localEntry.hasNotification()) && (!TextUtils.isEmpty(localEntry.getNotification().getNotificationBarText()))) {
      return localEntry.getNotification().getNotificationBarText();
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(this.mCalendarData.getEventData().getTitle());
    return paramContext.getString(2131362271, arrayOfObject);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    if (isLowPriorityNotification()) {
      return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.CALENDAR_TIME_TO_LEAVE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    if (isLowPriorityNotification()) {
      return 2130838062;
    }
    return 2130837907;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
  
  public int getNotificationType()
  {
    if (getEntry().getNotification().getType() == 3) {
      return 2;
    }
    return super.getNotificationType();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.CalendarNotification
 * JD-Core Version:    0.7.0.1
 */