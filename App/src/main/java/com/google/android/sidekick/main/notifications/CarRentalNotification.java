package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.CarRentalEntry;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.TimeWithZone;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class CarRentalNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.CarRentalEntry mCarRentalEntry;
  @Nullable
  private final Sidekick.Location mCurrentLocation;
  private final Sidekick.Location mDestination;
  private final DirectionsLauncher mDirectionsLauncher;
  
  public CarRentalNotification(Sidekick.Entry paramEntry, @Nullable Sidekick.Location paramLocation1, Sidekick.Location paramLocation2, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry);
    this.mCarRentalEntry = paramEntry.getCarRentalEntry();
    this.mCurrentLocation = paramLocation1;
    this.mDestination = paramLocation2;
    this.mDirectionsLauncher = paramDirectionsLauncher;
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    if (this.mCarRentalEntry.hasRoute()) {}
    for (Sidekick.CommuteSummary localCommuteSummary = this.mCarRentalEntry.getRoute();; localCommuteSummary = null) {
      return ImmutableList.of(new NavigateAction(NavigationContext.fromRenderingContext(paramCardRenderingContext), this.mDirectionsLauncher, this.mCurrentLocation, this.mDestination, localCommuteSummary));
    }
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    int i;
    if (this.mCarRentalEntry.getType() == 1)
    {
      i = 2131362836;
      if (this.mCarRentalEntry.getType() != 1) {
        break label74;
      }
    }
    label74:
    for (long l = this.mCarRentalEntry.getPickupTime().getSeconds();; l = this.mCarRentalEntry.getReturnTime().getSeconds())
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = DateUtils.formatDateTime(paramContext, TimeUnit.SECONDS.toMillis(l), 1);
      return paramContext.getString(i, arrayOfObject);
      i = 2131362837;
      break;
    }
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return this.mCarRentalEntry.getProviderName();
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.EVENT_TIME_TO_LEAVE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838069;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.CarRentalNotification
 * JD-Core Version:    0.7.0.1
 */