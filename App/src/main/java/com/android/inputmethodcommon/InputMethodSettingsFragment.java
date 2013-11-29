package com.android.inputmethodcommon;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public abstract class InputMethodSettingsFragment
  extends PreferenceFragment
{
  private final InputMethodSettingsImpl mSettings = new InputMethodSettingsImpl();
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Activity localActivity = getActivity();
    setPreferenceScreen(getPreferenceManager().createPreferenceScreen(localActivity));
    this.mSettings.init(localActivity, getPreferenceScreen());
  }
  
  public void onResume()
  {
    super.onResume();
    this.mSettings.updateSubtypeEnabler();
  }
  
  public void setInputMethodSettingsCategoryTitle(int paramInt)
  {
    this.mSettings.setInputMethodSettingsCategoryTitle(paramInt);
  }
  
  public void setSubtypeEnablerTitle(int paramInt)
  {
    this.mSettings.setSubtypeEnablerTitle(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.inputmethodcommon.InputMethodSettingsFragment
 * JD-Core Version:    0.7.0.1
 */