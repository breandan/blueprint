package com.google.android.sidekick.main.notifications;

import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;

public abstract class AbstractPlaceNotification
  extends AbstractSingleEntryNotification
{
  protected final Sidekick.Location mCurrentLocation;
  protected final DirectionsLauncher mDirectionsLauncher;
  protected final Sidekick.FrequentPlaceEntry mFrequentPlaceEntry;
  protected final TravelReport mTravelReport;
  
  public AbstractPlaceNotification(Sidekick.Entry paramEntry, Sidekick.Location paramLocation, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry);
    this.mCurrentLocation = paramLocation;
    this.mFrequentPlaceEntry = PlaceUtils.getFrequentPlaceEntry(paramEntry);
    if (this.mFrequentPlaceEntry.getRouteCount() > 0) {}
    for (this.mTravelReport = new TravelReport(this.mFrequentPlaceEntry.getRoute(0));; this.mTravelReport = null)
    {
      this.mDirectionsLauncher = paramDirectionsLauncher;
      return;
    }
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary localCommuteSummary = this.mTravelReport.getRoute();
      Sidekick.Location localLocation = this.mFrequentPlaceEntry.getFrequentPlace().getLocation();
      return ImmutableList.of(new NavigateAction(NavigationContext.fromRenderingContext(paramCardRenderingContext), this.mDirectionsLauncher, this.mCurrentLocation, localLocation, localCommuteSummary));
    }
    return ImmutableList.of();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.AbstractPlaceNotification
 * JD-Core Version:    0.7.0.1
 */