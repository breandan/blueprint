package com.google.android.search.core.preferences;

import android.preference.Preference;
import com.google.android.search.core.GooglePlayServicesHelper;

public class IcingStorageSettingsController
  extends SettingsControllerBase
{
  private final GooglePlayServicesHelper mGmsHelper;
  
  public IcingStorageSettingsController(GooglePlayServicesHelper paramGooglePlayServicesHelper)
  {
    this.mGmsHelper = paramGooglePlayServicesHelper;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !this.mGmsHelper.isGooglePlayServicesAvailable();
  }
  
  public void handlePreference(Preference paramPreference) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.IcingStorageSettingsController
 * JD-Core Version:    0.7.0.1
 */