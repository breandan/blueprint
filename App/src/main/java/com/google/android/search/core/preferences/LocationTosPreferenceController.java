package com.google.android.search.core.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import java.util.Locale;

public class LocationTosPreferenceController
  implements PreferenceController
{
  public LocationTosPreferenceController(Context paramContext) {}
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !Locale.getDefault().getCountry().equals("KR");
  }
  
  public void handlePreference(Preference paramPreference) {}
  
  public void onCreateComplete(Bundle paramBundle) {}
  
  public void onDestroy() {}
  
  public void onPause() {}
  
  public void onResume() {}
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStop() {}
  
  public void setScreen(PreferenceScreen paramPreferenceScreen) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.LocationTosPreferenceController
 * JD-Core Version:    0.7.0.1
 */