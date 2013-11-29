package com.google.android.sidekick.shared.util;

import android.content.Context;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class ViewInMapsAction
  implements EntryAction
{
  private final ActivityHelper mActivityHelper;
  private final Context mContext;
  private final Sidekick.Location mDestination;
  @Nullable
  private final Sidekick.CommuteSummary mRoute;
  private final boolean mShowRoute;
  
  public ViewInMapsAction(Context paramContext, ActivityHelper paramActivityHelper, Sidekick.Location paramLocation)
  {
    this.mContext = paramContext;
    this.mActivityHelper = paramActivityHelper;
    this.mDestination = paramLocation;
    this.mRoute = null;
    this.mShowRoute = false;
  }
  
  public ViewInMapsAction(Context paramContext, ActivityHelper paramActivityHelper, Sidekick.Location paramLocation, Sidekick.CommuteSummary paramCommuteSummary)
  {
    this.mContext = paramContext;
    this.mActivityHelper = paramActivityHelper;
    this.mDestination = paramLocation;
    this.mRoute = paramCommuteSummary;
    this.mShowRoute = true;
  }
  
  public void run()
  {
    if (this.mShowRoute)
    {
      MapsLauncher.TravelMode localTravelMode = MapsLauncher.TravelMode.fromSidekickProtoTravelMode(this.mRoute.getTravelMode());
      MapsLauncher.start(this.mContext, this.mActivityHelper, this.mDestination, localTravelMode, MapsLauncher.getPersonalizedRouteToken(this.mRoute));
      return;
    }
    MapsLauncher.start(this.mContext, this.mActivityHelper, this.mDestination);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ViewInMapsAction
 * JD-Core Version:    0.7.0.1
 */