package com.google.android.search.core.preferences;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import com.google.android.search.core.SearchSettings;
import com.google.android.voicesearch.greco3.languagepack.InstallActivity;
import com.google.common.base.Supplier;

public class DownloadLanguagePacksSettingController
  extends SettingsControllerBase
  implements Preference.OnPreferenceClickListener
{
  private final Supplier<Integer> mDeviceClassSupplier;
  private Preference mPreference;
  
  public DownloadLanguagePacksSettingController(SearchSettings paramSearchSettings, Supplier<Integer> paramSupplier)
  {
    super(paramSearchSettings);
    this.mDeviceClassSupplier = paramSupplier;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return ((Integer)this.mDeviceClassSupplier.get()).intValue() < 100;
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPreference = paramPreference;
    this.mPreference.setOnPreferenceClickListener(this);
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    InstallActivity.start(this.mPreference.getContext());
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.DownloadLanguagePacksSettingController
 * JD-Core Version:    0.7.0.1
 */