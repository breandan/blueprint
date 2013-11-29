package com.google.android.sidekick.main.contextprovider;

import android.util.Log;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class LocationRenderingContextAdapter
  implements EntryRenderingContextAdapter
{
  private static final String TAG = Tag.getTag(LocationRenderingContextAdapter.class);
  private final Sidekick.Location mLocation;
  private final Sidekick.CommuteSummary mRoute;
  
  public LocationRenderingContextAdapter(Sidekick.Location paramLocation, @Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    this.mLocation = paramLocation;
    this.mRoute = paramCommuteSummary;
  }
  
  private void checkConfigurationFor(SharedPreferencesContextProvider paramSharedPreferencesContextProvider, int paramInt)
  {
    String str = paramSharedPreferencesContextProvider.keyIsBad(paramInt);
    if (str != null) {
      Log.e(TAG, "Incomplete configuration for " + str);
    }
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    paramCardRenderingContextProviders.getNavigationContextProvider().addNavigationContext(paramCardRenderingContext, this.mLocation, this.mRoute);
    SharedPreferencesContextProvider localSharedPreferencesContextProvider = paramCardRenderingContextProviders.getSharedPreferencesContextProvider();
    localSharedPreferencesContextProvider.addBoolean(paramCardRenderingContext, 2131362164, true);
    localSharedPreferencesContextProvider.addBoolean(paramCardRenderingContext, 2131362163, true);
    localSharedPreferencesContextProvider.addBoolean(paramCardRenderingContext, 2131362165, true);
    checkConfigurationFor(localSharedPreferencesContextProvider, 2131362164);
    checkConfigurationFor(localSharedPreferencesContextProvider, 2131362163);
    checkConfigurationFor(localSharedPreferencesContextProvider, 2131362165);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.LocationRenderingContextAdapter
 * JD-Core Version:    0.7.0.1
 */