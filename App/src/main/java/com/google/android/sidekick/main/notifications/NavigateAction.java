package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.List;
import javax.annotation.Nullable;

public class NavigateAction
  implements NotificationAction
{
  private final Sidekick.Location mDestination;
  private final DirectionsLauncher mDirectionsLauncher;
  private final NavigationContext mNavigationContext;
  private final Sidekick.CommuteSummary mRoute;
  
  public NavigateAction(NavigationContext paramNavigationContext, DirectionsLauncher paramDirectionsLauncher, @Nullable Sidekick.Location paramLocation1, @Nullable Sidekick.Location paramLocation2, @Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    this.mNavigationContext = paramNavigationContext;
    this.mDestination = paramLocation2;
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mRoute = paramCommuteSummary;
  }
  
  private boolean supportsNav()
  {
    MapsLauncher.TravelMode localTravelMode = this.mDirectionsLauncher.getTravelMode(this.mNavigationContext, this.mRoute);
    return this.mDirectionsLauncher.modeSupportsNavigation(localTravelMode);
  }
  
  public int getActionIcon()
  {
    if (supportsNav()) {
      return 2130837668;
    }
    return 2130838058;
  }
  
  public String getActionString(Context paramContext)
  {
    if (supportsNav()) {}
    for (int i = 2131362186;; i = 2131362187) {
      return paramContext.getString(i);
    }
  }
  
  public Intent getCallbackIntent(Context paramContext)
  {
    if (this.mDestination != null)
    {
      Sidekick.CommuteSummary localCommuteSummary = this.mRoute;
      List localList = null;
      MapsLauncher.TravelMode localTravelMode = null;
      if (localCommuteSummary != null) {
        if (this.mRoute.getPathfinderWaypointCount() <= 0) {
          break label92;
        }
      }
      label92:
      for (localList = this.mRoute.getPathfinderWaypointList();; localList = null)
      {
        localTravelMode = this.mDirectionsLauncher.getTravelMode(this.mNavigationContext, this.mRoute);
        Intent localIntent = this.mDirectionsLauncher.getLauncherIntent(this.mDestination, localList, localTravelMode, MapsLauncher.getPersonalizedRouteToken(this.mRoute));
        localIntent.putExtra("callback_type", "activity");
        return localIntent;
      }
    }
    return null;
  }
  
  public String getCallbackType()
  {
    return "activity";
  }
  
  public String getLogString()
  {
    if (supportsNav()) {
      return "NAVIGATE";
    }
    return "GET_DIRECTIONS";
  }
  
  public boolean isActive()
  {
    if (this.mDestination != null) {
      return this.mNavigationContext.shouldShowNavigation(this.mDestination);
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NavigateAction
 * JD-Core Version:    0.7.0.1
 */