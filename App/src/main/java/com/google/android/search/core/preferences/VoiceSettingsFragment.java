package com.google.android.search.core.preferences;

import android.os.Bundle;

public class VoiceSettingsFragment
  extends SettingsFragmentBase
{
  protected int getPreferencesResourceId()
  {
    return 2131099678;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getController().setScreen(getPreferenceScreen());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.VoiceSettingsFragment
 * JD-Core Version:    0.7.0.1
 */