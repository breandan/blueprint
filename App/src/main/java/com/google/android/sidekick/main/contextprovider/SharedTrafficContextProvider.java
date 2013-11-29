package com.google.android.sidekick.main.contextprovider;

import android.content.Context;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.cards.SharedTrafficEntryAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.SharedTrafficContext;
import com.google.geo.sidekick.Sidekick.SharedTrafficCardEntry;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact;

public class SharedTrafficContextProvider
{
  private final Context mAppContext;
  private final PredictiveCardsPreferences mCardsPreferences;
  
  public SharedTrafficContextProvider(Context paramContext, PredictiveCardsPreferences paramPredictiveCardsPreferences)
  {
    this.mAppContext = paramContext;
    this.mCardsPreferences = paramPredictiveCardsPreferences;
  }
  
  public void addSharedTrafficContext(CardRenderingContext paramCardRenderingContext, Sidekick.SharedTrafficCardEntry paramSharedTrafficCardEntry)
  {
    ExtraPreconditions.checkNotMainThread();
    String str1 = this.mAppContext.getString(2131362091);
    String str2 = SharedTrafficEntryAdapter.getPrimaryKey(paramSharedTrafficCardEntry);
    Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact localLocationSharingContact = (Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact)this.mCardsPreferences.getWorkingPreferences().getMessage(str1, str2);
    SharedTrafficContext localSharedTrafficContext = (SharedTrafficContext)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(SharedTrafficContext.BUNDLE_KEY, new SharedTrafficContext());
    if ((localLocationSharingContact != null) && (localLocationSharingContact.hasHide())) {}
    for (boolean bool = true;; bool = false)
    {
      localSharedTrafficContext.setIsHidden(str2, bool);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.SharedTrafficContextProvider
 * JD-Core Version:    0.7.0.1
 */