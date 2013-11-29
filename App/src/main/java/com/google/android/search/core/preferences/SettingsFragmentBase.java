package com.google.android.search.core.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.velvet.VelvetServices;

public abstract class SettingsFragmentBase
  extends PreferenceFragment
{
  private PreferenceController mController;
  
  protected PreferenceController getController()
  {
    return this.mController;
  }
  
  protected abstract int getPreferencesResourceId();
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mController = VelvetServices.get().createPreferenceController(getActivity());
    GsaPreferenceController.useMainPreferences(getPreferenceManager());
    addPreferencesFromResource(getPreferencesResourceId());
    this.mController.handlePreference(getPreferenceScreen());
    this.mController.onCreateComplete(paramBundle);
    setHasOptionsMenu(true);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    paramMenu.clear();
  }
  
  public void onDestroy()
  {
    this.mController.onDestroy();
    super.onDestroy();
  }
  
  public void onPause()
  {
    this.mController.onPause();
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mController.onResume();
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mController.onSaveInstanceState(paramBundle);
  }
  
  public void onStop()
  {
    this.mController.onStop();
    super.onStop();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SettingsFragmentBase
 * JD-Core Version:    0.7.0.1
 */