package com.google.android.search.core;

import android.content.Context;
import android.preference.Preference;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.preferences.SettingsControllerBase;

public class CommuteSharingSettingController
  extends SettingsControllerBase
{
  private final NowConfigurationPreferences mNowConfigurationPrefs;
  private Preference mPref;
  
  public CommuteSharingSettingController(PredictiveCardsPreferences paramPredictiveCardsPreferences)
  {
    this.mNowConfigurationPrefs = paramPredictiveCardsPreferences.getWorkingPreferences();
  }
  
  private void updateSummary()
  {
    Context localContext;
    if (this.mPref != null)
    {
      localContext = this.mPref.getContext();
      if (!this.mNowConfigurationPrefs.getBoolean(localContext.getString(2131362163), false)) {
        break label48;
      }
    }
    label48:
    for (String str = localContext.getString(2131362751);; str = localContext.getString(2131362750))
    {
      this.mPref.setSummary(str);
      return;
    }
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPref = paramPreference;
    updateSummary();
  }
  
  public void onResume()
  {
    updateSummary();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.CommuteSharingSettingController
 * JD-Core Version:    0.7.0.1
 */