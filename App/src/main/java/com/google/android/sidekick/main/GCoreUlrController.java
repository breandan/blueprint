package com.google.android.sidekick.main;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Configuration;

public class GCoreUlrController
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  private static final String TAG = Tag.getTag(GCoreUlrController.class);
  private final PredictiveCardsPreferences mCardsPrefs;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final LoginHelper mLoginHelper;
  
  public GCoreUlrController(PredictiveCardsPreferences paramPredictiveCardsPreferences, LoginHelper paramLoginHelper, GmsLocationReportingHelper paramGmsLocationReportingHelper)
  {
    this.mCardsPrefs = paramPredictiveCardsPreferences;
    this.mLoginHelper = paramLoginHelper;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
    this.mCardsPrefs.getWorkingPreferences().registerOnSharedPreferenceChangeListener(this);
    this.mCardsPrefs.getWorkingPreferences().edit().putBoolean("location_reporting_configuration", false).apply();
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (("location_reporting_configuration".equals(paramString)) && (paramSharedPreferences.getBoolean(paramString, false)))
    {
      Sidekick.Configuration localConfiguration = this.mCardsPrefs.getMasterConfigurationFor(this.mLoginHelper.getAccount());
      if (localConfiguration.hasLocationReportingConfiguration())
      {
        this.mGmsLocationReportingHelper.handleLocationReportingConfiguration(localConfiguration.getLocationReportingConfiguration());
        this.mCardsPrefs.getWorkingPreferences().edit().putBoolean("location_reporting_configuration", false).apply();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.GCoreUlrController
 * JD-Core Version:    0.7.0.1
 */