package com.google.android.search.core.preferences;

import android.preference.Preference;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.gaia.LoginHelper;

public class PersonalizedSearchSettingsController
  extends SettingsControllerBase
{
  private final SearchConfig mConfig;
  private final LoginHelper mLoginHelper;
  
  public PersonalizedSearchSettingsController(SearchConfig paramSearchConfig, LoginHelper paramLoginHelper)
  {
    this.mConfig = paramSearchConfig;
    this.mLoginHelper = paramLoginHelper;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return (!this.mConfig.isPersonalizedSearchEnabled()) || (this.mLoginHelper.getAccount() == null);
  }
  
  public void handlePreference(Preference paramPreference) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PersonalizedSearchSettingsController
 * JD-Core Version:    0.7.0.1
 */