package com.google.android.search.core.preferences;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.common.base.Strings;

public class DebugSearchController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener
{
  private final DebugFeatures mDebugFeatures;
  private final SearchConfig mSearchConfig;
  private final SearchSettings mSearchSettings;
  
  public DebugSearchController(SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, DebugFeatures paramDebugFeatures)
  {
    this.mSearchSettings = paramSearchSettings;
    this.mSearchConfig = paramSearchConfig;
    this.mDebugFeatures = paramDebugFeatures;
  }
  
  private CharSequence getSummary(String paramString)
  {
    if (Strings.isNullOrEmpty(paramString)) {
      paramString = "[No override set]";
    }
    return paramString;
  }
  
  private static boolean isSandbox(String paramString)
  {
    return (paramString != null) && (paramString.endsWith(".sandbox.google.com"));
  }
  
  private void maybeEnableHostParamPref(Preference paramPreference, String paramString)
  {
    String str = this.mSearchSettings.getDebugSearchDomainOverride();
    if ((isSandbox(this.mSearchConfig.getDogfoodDomainOverride())) || (isSandbox(str)))
    {
      paramPreference.setSummary(getSummary(paramString));
      paramPreference.setEnabled(true);
      return;
    }
    paramPreference.setSummary("[Enabled for sandbox domain overrides]");
    paramPreference.setEnabled(false);
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    if ((paramPreference.getKey().equals("debug_js_injection_enabled")) || (paramPreference.getKey().equals("debug_js_server_address"))) {
      if (this.mDebugFeatures.teamDebugEnabled()) {}
    }
    while (!this.mDebugFeatures.dogfoodDebugEnabled())
    {
      return true;
      return false;
    }
    return false;
  }
  
  public void handlePreference(Preference paramPreference)
  {
    if (paramPreference.getKey().equals("debug_search_domain_override"))
    {
      paramPreference.setSummary(getSummary(this.mSearchSettings.getDebugSearchDomainOverride()));
      paramPreference.setOnPreferenceChangeListener(this);
    }
    if (paramPreference.getKey().equals("debug_search_scheme_override"))
    {
      paramPreference.setSummary(getSummary(this.mSearchSettings.getDebugSearchSchemeOverride()));
      paramPreference.setOnPreferenceChangeListener(this);
    }
    if (paramPreference.getKey().equals("debug_search_host_param"))
    {
      maybeEnableHostParamPref(paramPreference, this.mSearchSettings.getDebugSearchHostParam());
      paramPreference.setOnPreferenceChangeListener(this);
    }
    if (paramPreference.getKey().equals("debug_js_server_address"))
    {
      paramPreference.setSummary(getSummary(this.mSearchSettings.getDebugWeinreServerAddress()));
      paramPreference.setOnPreferenceChangeListener(this);
    }
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if (paramPreference.getKey().equals("debug_search_host_param"))
    {
      maybeEnableHostParamPref(paramPreference, (String)paramObject);
      return true;
    }
    if ((paramPreference.getKey().equals("debug_search_scheme_override")) || (paramPreference.getKey().equals("debug_search_domain_override")) || (paramPreference.getKey().equals("debug_js_server_address")))
    {
      paramPreference.setSummary(getSummary((String)paramObject));
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.DebugSearchController
 * JD-Core Version:    0.7.0.1
 */