package com.google.android.search.core.preferences;

import android.preference.Preference;
import com.google.android.search.core.SearchConfig;
import com.google.android.voicesearch.bluetooth.BluetoothShim;

public class BluetoothHeadsetSettingController
  extends SettingsControllerBase
{
  private final boolean mBluetoothUnavailable;
  private final SearchConfig mSearchConfig;
  
  public BluetoothHeadsetSettingController(SearchConfig paramSearchConfig)
  {
    this.mSearchConfig = paramSearchConfig;
    if (BluetoothShim.getDefaultAdapter() == null) {}
    for (boolean bool = true;; bool = false)
    {
      this.mBluetoothUnavailable = bool;
      return;
    }
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return (this.mBluetoothUnavailable) || (!this.mSearchConfig.isBluetoothEnabled());
  }
  
  public void handlePreference(Preference paramPreference) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.BluetoothHeadsetSettingController
 * JD-Core Version:    0.7.0.1
 */