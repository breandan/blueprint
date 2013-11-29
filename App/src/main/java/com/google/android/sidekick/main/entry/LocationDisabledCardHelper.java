package com.google.android.sidekick.main.entry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.sidekick.main.location.LocationManagerInjectable;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.GenericCardEntry;
import javax.annotation.Nullable;

public class LocationDisabledCardHelper
{
  static final int MODE_GOOGLE_APPS_LOCATION_DISABLED = 2;
  static final int MODE_LOCATION_OFF = 3;
  static final int MODE_NETWORK_PROVIDER_DISABLED = 1;
  static final int MODE_NONE;
  private static final String TAG = Tag.getTag(LocationDisabledCardHelper.class);
  private final Context mContext;
  private final LocationManagerInjectable mLocationManager;
  private final LocationSettings mLocationSettings;
  private final LoginHelper mLoginHelper;
  private final Supplier<SharedPreferences> mMainPreferencesSupplier;
  
  public LocationDisabledCardHelper(Context paramContext, Supplier<SharedPreferences> paramSupplier, LocationManagerInjectable paramLocationManagerInjectable, LocationSettings paramLocationSettings, LoginHelper paramLoginHelper)
  {
    this.mContext = paramContext;
    this.mMainPreferencesSupplier = paramSupplier;
    this.mLocationManager = paramLocationManagerInjectable;
    this.mLocationSettings = paramLocationSettings;
    this.mLoginHelper = paramLoginHelper;
  }
  
  private int getMode()
  {
    int i = 1;
    int j = this.mLocationSettings.getKlpLocationMode();
    if (j != -1) {
      switch (j)
      {
      }
    }
    while (!this.mLocationSettings.canUseLocationForGoogleApps())
    {
      i = 2;
      return i;
      return 3;
      if (!this.mLocationManager.isProviderEnabled("network")) {
        return i;
      }
    }
    return 0;
  }
  
  private int getModePreference()
  {
    return ((SharedPreferences)this.mMainPreferencesSupplier.get()).getInt("location_disabled_card_mode", 0);
  }
  
  private void setModePreference(int paramInt)
  {
    SharedPreferences localSharedPreferences = (SharedPreferences)this.mMainPreferencesSupplier.get();
    if (paramInt == 0)
    {
      if (localSharedPreferences.contains("location_disabled_card_mode"))
      {
        SharedPreferences.Editor localEditor2 = localSharedPreferences.edit();
        localEditor2.remove("location_disabled_card_mode");
        localEditor2.apply();
      }
      return;
    }
    SharedPreferences.Editor localEditor1 = localSharedPreferences.edit();
    localEditor1.putInt("location_disabled_card_mode", paramInt);
    localEditor1.apply();
  }
  
  @Nullable
  Sidekick.GenericCardEntry createDisabledLocationCard()
  {
    int i = 1;
    int j = getMode();
    setModePreference(j);
    if (j == 0) {
      return null;
    }
    String str1;
    String str2;
    if ((j == 3) || (j == i))
    {
      localClickAction = new Sidekick.ClickAction();
      localClickAction.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
      localClickAction.setLabel(this.mContext.getString(2131363422));
      if (j == 3)
      {
        str1 = this.mContext.getString(2131362556);
        str2 = "LocationDisabled";
        Sidekick.GenericCardEntry localGenericCardEntry = new Sidekick.GenericCardEntry();
        localGenericCardEntry.setText(str1).setTitle(this.mContext.getString(2131362552)).addViewAction(localClickAction).setCardType(str2);
        return localGenericCardEntry;
      }
      Context localContext = this.mContext;
      if (hasKlpLocationMode()) {}
      for (int k = 2131362555;; k = 2131362554)
      {
        str1 = localContext.getString(k);
        str2 = "NlpDisabled";
        break;
      }
    }
    if (j == 2)
    {
      label163:
      Preconditions.checkState(i);
      if (!hasKlpLocationMode()) {
        break label231;
      }
    }
    label231:
    Intent localIntent;
    for (Sidekick.ClickAction localClickAction = ProtoUtils.getViewActionFromIntent(this.mLocationSettings.getGoogleSettingIntent(this.mLoginHelper.getAccountName()));; localClickAction = ProtoUtils.getViewActionFromIntent(localIntent))
    {
      localClickAction.setLabel(this.mContext.getString(2131362696));
      str1 = this.mContext.getString(2131362553);
      str2 = "GlsDisabled";
      break;
      i = 0;
      break label163;
      String str3 = this.mLoginHelper.getAccountName();
      localIntent = new Intent("com.google.android.gsf.action.SET_USE_LOCATION_FOR_SERVICES");
      if (str3 != null) {
        localIntent.putExtra("account", str3);
      }
      localIntent.putExtra("disable", false);
    }
  }
  
  boolean hasKlpLocationMode()
  {
    return Build.VERSION.SDK_INT >= 19;
  }
  
  public boolean requiresRefresh()
  {
    return getModePreference() != getMode();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.LocationDisabledCardHelper
 * JD-Core Version:    0.7.0.1
 */