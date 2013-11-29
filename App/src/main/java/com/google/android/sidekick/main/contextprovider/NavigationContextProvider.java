package com.google.android.sidekick.main.contextprovider;

import android.content.Context;
import android.net.Uri;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.util.CommuteSummaryUtil;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class NavigationContextProvider
{
  private static final Uri NAVIGATION_AVAILABILITY_CONTENT_URI = Uri.parse("content://com.google.android.maps.NavigationAvailabilityProvider");
  private final Context mContext;
  private final PredictiveCardsPreferences mPredictiveCardsPreferences;
  
  public NavigationContextProvider(Context paramContext, PredictiveCardsPreferences paramPredictiveCardsPreferences)
  {
    this.mContext = paramContext;
    this.mPredictiveCardsPreferences = paramPredictiveCardsPreferences;
  }
  
  private boolean checkNavigationAvailability(@Nullable Sidekick.Location paramLocation1, Sidekick.Location paramLocation2)
  {
    return true;
  }
  
  public void addNavigationContext(CardRenderingContext paramCardRenderingContext, @Nullable Sidekick.Location paramLocation, @Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    ExtraPreconditions.checkNotMainThread();
    NavigationContext localNavigationContext = (NavigationContext)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(NavigationContext.BUNDLE_KEY, new NavigationContext());
    if ((paramLocation == null) || (localNavigationContext.haveCheckedNavigationTo(paramLocation))) {}
    do
    {
      return;
      localNavigationContext.setShowNavigation(paramLocation, checkNavigationAvailability(LocationUtilities.androidLocationToSidekickLocation(paramCardRenderingContext.getCurrentLocation()), paramLocation));
    } while (CommuteSummaryUtil.getTravelMode(paramCommuteSummary) != null);
    int i = CommuteSummaryUtil.getTravelModeSetting(paramCommuteSummary);
    localNavigationContext.setTravelModePreference(i, this.mPredictiveCardsPreferences.getTravelMode(i));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.NavigationContextProvider
 * JD-Core Version:    0.7.0.1
 */