package com.google.android.search.core.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.velvet.Help;

public class PrivacyAndAccountFragment
  extends SettingsFragmentBase
{
  protected int getPreferencesResourceId()
  {
    return 2131099673;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getController().setScreen(getPreferenceScreen());
    setHasOptionsMenu(true);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    super.onCreateOptionsMenu(paramMenu, paramMenuInflater);
    paramMenuInflater.inflate(2131886083, paramMenu);
    new Help(getActivity().getApplicationContext()).setHelpMenuItemIntent(paramMenu, "privacy");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PrivacyAndAccountFragment
 * JD-Core Version:    0.7.0.1
 */