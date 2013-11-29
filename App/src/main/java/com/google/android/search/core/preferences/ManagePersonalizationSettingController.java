package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import com.google.android.search.core.SearchSettings;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.personalization.PersonalizationHelper;
import com.google.android.voicesearch.settings.Settings;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Personalization;

public class ManagePersonalizationSettingController
  extends SettingsControllerBase
  implements Preference.OnPreferenceClickListener
{
  private final Activity mActivity;
  private final PersonalizationHelper mPersonalizationHelper;
  private Preference mPreference;
  private final Settings mVoiceSettings;
  
  public ManagePersonalizationSettingController(SearchSettings paramSearchSettings, Activity paramActivity, Settings paramSettings, PersonalizationHelper paramPersonalizationHelper)
  {
    super(paramSearchSettings);
    this.mActivity = paramActivity;
    this.mVoiceSettings = paramSettings;
    this.mPersonalizationHelper = paramPersonalizationHelper;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !this.mPersonalizationHelper.isPersonalizationAvailable();
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPreference = paramPreference;
    this.mPreference.setOnPreferenceClickListener(this);
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    EventLogger.recordClientEvent(47);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(this.mVoiceSettings.getConfiguration().getPersonalization().getDashboardUrl()));
    this.mActivity.startActivity(localIntent);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.ManagePersonalizationSettingController
 * JD-Core Version:    0.7.0.1
 */