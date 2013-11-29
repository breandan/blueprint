package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import com.google.android.gms.location.settings.SettingsClient;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.LocationSettings;

public class LocationSettingsController
  extends SettingsControllerBase
  implements Preference.OnPreferenceClickListener
{
  private final Activity mActivity;
  private final LocationSettings mLocationSettings;
  private final boolean mSupportsGoogleLocationSettings;
  
  public LocationSettingsController(SearchSettings paramSearchSettings, Activity paramActivity, LocationSettings paramLocationSettings)
  {
    super(paramSearchSettings);
    this.mActivity = paramActivity;
    this.mLocationSettings = paramLocationSettings;
    this.mSupportsGoogleLocationSettings = this.mLocationSettings.isGoogleSettingForAllApps();
  }
  
  public void handlePreference(Preference paramPreference)
  {
    paramPreference.setOnPreferenceClickListener(this);
    if (this.mSupportsGoogleLocationSettings)
    {
      paramPreference.setTitle(2131363161);
      return;
    }
    paramPreference.setTitle(2131362519);
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    Intent localIntent = null;
    if (this.mSupportsGoogleLocationSettings) {
      if (this.mLocationSettings.isGmsCoreLocationSettingAvailable()) {
        SettingsClient.launchGoogleLocationSettings(paramPreference.getContext());
      }
    }
    for (;;)
    {
      if (localIntent != null) {
        this.mActivity.startActivity(localIntent);
      }
      return false;
      localIntent = this.mLocationSettings.getGoogleSettingIntent(getSettings().getGoogleAccountToUse());
      continue;
      localIntent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.LocationSettingsController
 * JD-Core Version:    0.7.0.1
 */