package com.google.android.search.core.preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import com.google.android.search.core.SearchSettings;
import com.google.common.base.Preconditions;

public abstract class SettingsControllerBase
  implements PreferenceController
{
  private final SearchSettings mSettings;
  
  public SettingsControllerBase()
  {
    this(null);
  }
  
  public SettingsControllerBase(SearchSettings paramSearchSettings)
  {
    this.mSettings = paramSearchSettings;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return false;
  }
  
  protected SearchSettings getSettings()
  {
    if (this.mSettings != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return this.mSettings;
    }
  }
  
  public void onCreateComplete(Bundle paramBundle) {}
  
  public void onDestroy() {}
  
  public void onPause() {}
  
  public void onResume() {}
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStop() {}
  
  public void setScreen(PreferenceScreen paramPreferenceScreen) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SettingsControllerBase
 * JD-Core Version:    0.7.0.1
 */