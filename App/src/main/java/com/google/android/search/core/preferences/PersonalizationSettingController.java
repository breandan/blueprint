package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import com.google.android.search.core.SearchSettings;
import com.google.android.voicesearch.personalization.PersonalizationHelper;
import com.google.android.voicesearch.settings.Settings;

public class PersonalizationSettingController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener
{
  private final Activity mActivity;
  private final PersonalizationHelper mPersonalizationHelper;
  private CheckBoxPreference mPreference;
  private final Settings mSettings;
  
  public PersonalizationSettingController(SearchSettings paramSearchSettings, Activity paramActivity, Settings paramSettings, PersonalizationHelper paramPersonalizationHelper)
  {
    super(paramSearchSettings);
    this.mActivity = paramActivity;
    this.mSettings = paramSettings;
    this.mPersonalizationHelper = paramPersonalizationHelper;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !this.mPersonalizationHelper.isPersonalizationAvailable();
  }
  
  public void handlePreference(Preference paramPreference)
  {
    this.mPreference = ((CheckBoxPreference)paramPreference);
    this.mPreference.setOnPreferenceChangeListener(this);
    this.mPreference.setChecked(this.mSettings.isPersonalizationEnabled());
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if (paramObject == null) {
      return true;
    }
    Boolean localBoolean = (Boolean)paramObject;
    Intent localIntent = new Intent("com.google.android.voicesearch.action.PERSONALIZATION_OPT_IN");
    localIntent.putExtra("PERSONALIZATION_OPT_IN_ENABLE", localBoolean);
    try
    {
      this.mActivity.startActivity(localIntent);
      return false;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      for (;;)
      {
        Log.e("PersonalizationSettingController", "Couldn't start personalization opt-in: " + localActivityNotFoundException);
      }
    }
  }
  
  public void onResume()
  {
    if (this.mPreference != null) {
      this.mPreference.setChecked(this.mSettings.isPersonalizationEnabled());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PersonalizationSettingController
 * JD-Core Version:    0.7.0.1
 */