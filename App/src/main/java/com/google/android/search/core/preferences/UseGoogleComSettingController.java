package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.shared.util.BidiUtils;

public class UseGoogleComSettingController
  extends SettingsControllerBase
  implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  private final Activity mActivity;
  private final SearchConfig mConfig;
  private final SearchUrlHelper mSearchUrlHelper;
  private CheckBoxPreference mUseGoogleComPreference;
  
  public UseGoogleComSettingController(SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, SearchUrlHelper paramSearchUrlHelper, Activity paramActivity)
  {
    super(paramSearchSettings);
    this.mConfig = paramSearchConfig;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mActivity = paramActivity;
    paramSearchSettings.registerOnSharedPreferenceChangeListener(this);
  }
  
  private String getSummary(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 2131363183;; i = 2131363182)
    {
      Resources localResources = this.mActivity.getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrapLtr(this.mSearchUrlHelper.getLocalSearchDomain());
      return localResources.getString(i, arrayOfObject);
    }
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return (this.mSearchUrlHelper.isDotComAnyway()) || (this.mConfig.getFixedSearchDomain() != null);
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mUseGoogleComPreference = ((CheckBoxPreference)paramPreference);
    this.mUseGoogleComPreference.setSummary(getSummary(this.mUseGoogleComPreference.isChecked()));
    this.mUseGoogleComPreference.setOnPreferenceClickListener(this);
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    getSettings().unregisterOnSharedPreferenceChangeListener(this);
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if (this.mUseGoogleComPreference.isChecked()) {}
    for (;;)
    {
      return true;
      this.mUseGoogleComPreference.setSummary(this.mActivity.getResources().getString(2131363184));
    }
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (("search_domain_apply_time".equals(paramString)) && (this.mUseGoogleComPreference != null)) {
      this.mUseGoogleComPreference.setSummary(getSummary(this.mUseGoogleComPreference.isChecked()));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.UseGoogleComSettingController
 * JD-Core Version:    0.7.0.1
 */