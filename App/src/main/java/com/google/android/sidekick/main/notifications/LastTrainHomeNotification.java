package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.cards.LastTrainHomeEntryAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Notification;
import javax.annotation.Nullable;

public class LastTrainHomeNotification
  extends AbstractSingleEntryNotification
{
  protected final Clock mClock;
  protected final Sidekick.Location mCurrentLocation;
  protected final DirectionsLauncher mDirectionsLauncher;
  protected final Sidekick.FrequentPlaceEntry mLastTrainHomeEntry;
  @Nullable
  private final TravelReport mTravelReport;
  
  public LastTrainHomeNotification(Sidekick.Entry paramEntry, Clock paramClock, Sidekick.Location paramLocation, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry);
    this.mClock = paramClock;
    this.mCurrentLocation = paramLocation;
    this.mLastTrainHomeEntry = paramEntry.getLastTrainHomeEntry();
    if (this.mLastTrainHomeEntry.getRouteCount() > 0) {
      this.mLastTrainHomeEntry.getFrequentPlace();
    }
    for (this.mTravelReport = new TravelReport(this.mLastTrainHomeEntry.getRoute(0));; this.mTravelReport = null)
    {
      this.mDirectionsLauncher = paramDirectionsLauncher;
      return;
    }
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramCardRenderingContext);
    Sidekick.Location localLocation = this.mLastTrainHomeEntry.getFrequentPlace().getLocation();
    return ImmutableList.of(new NavigateAction(localNavigationContext, this.mDirectionsLauncher, this.mCurrentLocation, localLocation, this.mLastTrainHomeEntry.getRoute(0)));
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mTravelReport.getTransitDetails();
      return paramContext.getString(2131362272, new Object[] { DateUtils.formatDateTime(paramContext, 1000L * (localTransitDetails.getDepartureTimeSeconds() - 60 * localTransitDetails.getWalkingTimeMinutes()), 1) });
    }
    return null;
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mTravelReport.getTransitDetails();
      (this.mClock.currentTimeMillis() / 1000L);
      String str = DateUtils.formatDateTime(paramContext, 1000L * localTransitDetails.getDepartureTimeSeconds(), 1);
      return paramContext.getString(LastTrainHomeEntryAdapter.selectMessageByDestination(this.mLastTrainHomeEntry, 2131362254, 2131362255), new Object[] { str, "", "" });
    }
    return null;
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.LAST_TRAIN_HOME_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838069;
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
    return getEntry().getNotification().getType();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.LastTrainHomeNotification
 * JD-Core Version:    0.7.0.1
 */