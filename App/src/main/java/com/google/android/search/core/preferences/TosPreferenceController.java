package com.google.android.search.core.preferences;

import android.content.Intent;
import android.preference.Preference;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.SearchUrlHelper;

public class TosPreferenceController
  extends SettingsControllerBase
{
  private final SearchConfig mConfig;
  private final SearchUrlHelper mUrlHelper;
  
  public TosPreferenceController(SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, SearchUrlHelper paramSearchUrlHelper)
  {
    super(paramSearchSettings);
    this.mConfig = paramSearchConfig;
    this.mUrlHelper = paramSearchUrlHelper;
  }
  
  public void handlePreference(Preference paramPreference)
  {
    paramPreference.setIntent(new Intent("android.intent.action.VIEW", this.mUrlHelper.formatUrlForSearchDomain(this.mConfig.getTosUrlFormat())));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.TosPreferenceController
 * JD-Core Version:    0.7.0.1
 */