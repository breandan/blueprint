package com.google.android.search.core.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import com.google.android.search.core.SearchSettings;

public class TtsModeSettingController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener
{
  private ListPreference mPreference;
  
  public TtsModeSettingController(SearchSettings paramSearchSettings)
  {
    super(paramSearchSettings);
  }
  
  private String getString(int paramInt)
  {
    return this.mPreference.getContext().getString(paramInt);
  }
  
  private void updateTtsModeSummary(String paramString)
  {
    String str;
    if ("handsFreeOnly".equals(paramString)) {
      str = getString(2131363477);
    }
    for (;;)
    {
      this.mPreference.setSummary(str);
      return;
      if ("never".equals(paramString)) {
        str = getString(2131363478);
      } else {
        str = getString(2131363476);
      }
    }
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPreference = ((ListPreference)paramPreference);
    this.mPreference.setOnPreferenceChangeListener(this);
    updateTtsModeSummary(this.mPreference.getValue().toString());
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    updateTtsModeSummary(paramObject.toString());
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.TtsModeSettingController
 * JD-Core Version:    0.7.0.1
 */