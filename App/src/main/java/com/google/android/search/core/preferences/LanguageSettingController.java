package com.google.android.search.core.preferences;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchSettings;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.ActionDiscoveryData;
import com.google.android.voicesearch.handsfree.HandsFreeIntentActivity;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.ui.LanguagePreference;

public class LanguageSettingController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener
{
  private final Context mContext;
  private final CoreSearchServices mCoreSearchServices;
  private final Settings mVoiceSettings;
  
  public LanguageSettingController(SearchSettings paramSearchSettings, Settings paramSettings, CoreSearchServices paramCoreSearchServices, Context paramContext)
  {
    super(paramSearchSettings);
    this.mVoiceSettings = paramSettings;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mContext = paramContext;
  }
  
  private void updateSummary(String paramString, LanguagePreference paramLanguagePreference)
  {
    String str = SpokenLanguageUtils.getDisplayName(this.mVoiceSettings.getConfiguration(), paramString);
    if (this.mVoiceSettings.isDefaultSpokenLanguage())
    {
      paramLanguagePreference.setSummary(paramLanguagePreference.getContext().getString(2131363471, new Object[] { str }));
      return;
    }
    paramLanguagePreference.setSummary(str);
  }
  
  public void handlePreference(Preference paramPreference)
  {
    LanguagePreference localLanguagePreference = (LanguagePreference)paramPreference;
    localLanguagePreference.setOnPreferenceChangeListener(this);
    updateSummary(this.mVoiceSettings.getSpokenLocaleBcp47(), localLanguagePreference);
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    String str = (String)paramObject;
    LanguagePreference localLanguagePreference = (LanguagePreference)paramPreference;
    if (SpokenLanguageUtils.updateSpokenLanguage(this.mVoiceSettings, str))
    {
      ActionDiscoveryData localActionDiscoveryData = this.mCoreSearchServices.getActionDiscoveryData();
      localActionDiscoveryData.initializeFromCached();
      localActionDiscoveryData.maybeUpdateCache();
      HandsFreeIntentActivity.updateEnabledState(this.mContext, this.mVoiceSettings);
    }
    updateSummary(str, localLanguagePreference);
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.LanguageSettingController
 * JD-Core Version:    0.7.0.1
 */