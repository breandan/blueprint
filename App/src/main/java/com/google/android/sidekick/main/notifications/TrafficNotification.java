package com.google.android.sidekick.main.notifications;

import android.content.Context;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Notification;

public class TrafficNotification
  extends AbstractPlaceNotification
{
  private final Sidekick.Entry mEntry;
  
  public TrafficNotification(Sidekick.Entry paramEntry, Sidekick.Location paramLocation, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry, paramLocation, paramDirectionsLauncher);
    this.mEntry = paramEntry;
  }
  
  private int getTrafficIcon(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 2130838072;
    case 1: 
      return 2130838074;
    case 2: 
      return 2130838075;
    }
    return 2130838073;
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport != null) {
      return this.mTravelReport.buildCommuteString(paramContext);
    }
    return null;
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationTickerText(paramContext, paramCardRenderingContext);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    if (isLowPriorityNotification()) {
      return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.TRAFFIC_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    if (this.mTravelReport != null)
    {
      switch (this.mTravelReport.getTravelMode())
      {
      default: 
        return getTrafficIcon(this.mTravelReport.getTrafficStatus());
      case 1: 
        return 2130838067;
      case 3: 
        return 2130838061;
      }
      return 2130838076;
    }
    return 2130838072;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if ((this.mEntry.hasNotification()) && (this.mEntry.getNotification().hasNotificationBarText())) {
      return this.mEntry.getNotification().getNotificationBarText();
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(PlaceUtils.getPlaceName(paramContext, this.mFrequentPlaceEntry.getFrequentPlace()));
    return paramContext.getString(2131362215, arrayOfObject);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.TrafficNotification
 * JD-Core Version:    0.7.0.1
 */