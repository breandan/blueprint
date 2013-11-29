package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.util.Log;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Date;

public class TimeToLeaveNotification
  extends AbstractPlaceNotification
{
  private static final String TAG = Tag.getTag(TimeToLeaveNotification.class);
  private final Sidekick.FrequentPlace mPlace = this.mFrequentPlaceEntry.getFrequentPlace();
  
  public TimeToLeaveNotification(Sidekick.Entry paramEntry, Sidekick.Location paramLocation, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry, paramLocation, paramDirectionsLauncher);
  }
  
  public String getLoggingName()
  {
    switch (this.mPlace.getSourceType())
    {
    default: 
      return super.getLoggingName();
    case 6: 
      return "GmailRestaurantReservation";
    }
    return "GmailEventReservation";
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport == null) {}
    Integer localInteger;
    do
    {
      do
      {
        return null;
      } while (!this.mFrequentPlaceEntry.hasEventTimeSeconds());
      localInteger = this.mTravelReport.getTotalEtaMinutes();
    } while (localInteger == null);
    int i = 60 * localInteger.intValue();
    Date localDate = new Date(1000L * (this.mFrequentPlaceEntry.getEventTimeSeconds() - i));
    java.text.DateFormat localDateFormat = android.text.format.DateFormat.getTimeFormat(paramContext);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = localDateFormat.format(localDate);
    return paramContext.getString(2131362272, arrayOfObject);
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(PlaceUtils.getPlaceName(paramContext, this.mPlace));
    return paramContext.getString(2131362271, arrayOfObject);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    int i = this.mPlace.getSourceType();
    switch (i)
    {
    default: 
      Log.e(TAG, "Unsupported source type for time to leave notification: " + i);
      return NowNotificationManager.NotificationType.EVENT_TIME_TO_LEAVE_NOTIFICATION;
    case 6: 
      return NowNotificationManager.NotificationType.RESTAURANT_TIME_TO_LEAVE_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.EVENT_TIME_TO_LEAVE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130837907;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
  
  public int getNotificationType()
  {
    return 2;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.TimeToLeaveNotification
 * JD-Core Version:    0.7.0.1
 */