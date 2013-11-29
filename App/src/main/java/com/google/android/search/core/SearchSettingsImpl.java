package com.google.android.search.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.search.core.summons.Source;
import com.google.android.velvet.VelvetStrictMode;
import com.google.common.collect.Maps;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.GsaExperiments;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchSettingsImpl
  implements SearchSettings
{
  private final Context mContext;
  private final GsaPreferenceController mPrefController;
  
  public SearchSettingsImpl(Context paramContext, GsaPreferenceController paramGsaPreferenceController)
  {
    this.mContext = paramContext;
    this.mPrefController = paramGsaPreferenceController;
  }
  
  private Intent getSearchSettingsIntent()
  {
    Intent localIntent = new Intent("android.search.action.SEARCH_SETTINGS");
    localIntent.addFlags(524288);
    localIntent.setPackage(getContext().getPackageName());
    return localIntent;
  }
  
  private static String getSourceClickStatsPreference(String paramString)
  {
    return "corpora_name_source_stats_" + paramString;
  }
  
  public static String getSourceEnabledPreference(Source paramSource)
  {
    return "enable_corpus_" + paramSource.getName();
  }
  
  private void setSearchDomain(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong)
  {
    getMainPreferences().edit().putString("search_domain_scheme", paramString1).putString("search_domain", paramString2).putString("search_domain_country_code", paramString3).putString("search_language", paramString4).putLong("search_domain_apply_time", paramLong).apply();
  }
  
  public void addMenuItems(Menu paramMenu, boolean paramBoolean)
  {
    new MenuInflater(getContext()).inflate(2131886086, paramMenu);
    paramMenu.findItem(2131297286).setIntent(getSearchSettingsIntent());
  }
  
  public void broadcastSettingsChanged()
  {
    Intent localIntent = new Intent("android.search.action.SETTINGS_CHANGED");
    getContext().sendBroadcast(localIntent);
  }
  
  protected String createAndSetInstallId()
  {
    try
    {
      String str = UUID.randomUUID().toString();
      storeStringInMainPrefs("install-id", str);
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public byte[] getActionDiscoveryData(String paramString)
  {
    return getMainPreferences().getBytes("action_discovery_data_" + paramString, null);
  }
  
  public String getActionDiscoveryDataUri(String paramString)
  {
    return getMainPreferences().getString("action_discovery_data_url_" + paramString, "");
  }
  
  public Map<String, String> getAllSourceClickStats()
  {
    Map localMap = getMainPreferences().getAllByKeyPrefix("corpora_name_source_stats_");
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = localMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localHashMap.put(((String)localEntry.getKey()).substring("corpora_name_source_stats_".length()), localEntry.getValue());
    }
    return localHashMap;
  }
  
  public long getBackgroundTaskEarliestNextRun(String paramString)
  {
    return getMainPreferences().getLong("background_task_earliest_next_run_" + paramString, 0L);
  }
  
  public long getBackgroundTaskForcedRun(String paramString)
  {
    return getMainPreferences().getLong("background_task_forced_run_" + paramString, 0L);
  }
  
  public String getCachedUserAgentBase()
  {
    return getMainPreferences().getString("user_agent", null);
  }
  
  public String getCachedZeroQueryWebResults()
  {
    return getMainPreferences().getString("zero_query_web_results", null);
  }
  
  protected Context getContext()
  {
    return this.mContext;
  }
  
  public int getDebugFeaturesLevel()
  {
    return getStartupPreferences().getInt("debug_features_level", 0);
  }
  
  public Set<String> getDebugGsaConfigOverridableFlags()
  {
    return getMainPreferences().getStringSet("debug_gsa_config_overridable_flags", null);
  }
  
  public String getDebugSearchDomainOverride()
  {
    return getMainPreferences().getString("debug_search_domain_override", null);
  }
  
  public String getDebugSearchHostParam()
  {
    return getMainPreferences().getString("debug_search_host_param", null);
  }
  
  public String getDebugSearchSchemeOverride()
  {
    return getMainPreferences().getString("debug_search_scheme_override", null);
  }
  
  public String getDebugWeinreServerAddress()
  {
    return getMainPreferences().getString("debug_js_server_address", null);
  }
  
  public long getFirstHotwordHintShownAtTime()
  {
    return getMainPreferences().getLong("first_hotword_hint_shown_at", 0L);
  }
  
  @Nullable
  public String getGoogleAccountToUse()
  {
    return getStartupPreferences().getString("google_account", null);
  }
  
  public String getGsaConfigChecksum()
  {
    return getStartupPreferences().getString("gsa_config_checksum", null);
  }
  
  public GsaConfigurationProto.GsaExperiments getGsaConfigOverride()
  {
    byte[] arrayOfByte = getStartupPreferences().getBytes("gsa_config_overrides", null);
    if (arrayOfByte == null) {
      return new GsaConfigurationProto.GsaExperiments();
    }
    try
    {
      GsaConfigurationProto.GsaExperiments localGsaExperiments = GsaConfigurationProto.GsaExperiments.parseFrom(arrayOfByte);
      return localGsaExperiments;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e("QSB.SearchSettingsImpl", "Couldn't load local configuration.");
    }
    return new GsaConfigurationProto.GsaExperiments();
  }
  
  public GsaConfigurationProto.GsaExperiments getGsaConfigServer()
  {
    byte[] arrayOfByte = getStartupPreferences().getBytes("gsa_config_server", null);
    if (arrayOfByte == null) {
      return new GsaConfigurationProto.GsaExperiments();
    }
    try
    {
      GsaConfigurationProto.GsaExperiments localGsaExperiments = GsaConfigurationProto.GsaExperiments.parseFrom(arrayOfByte);
      return localGsaExperiments;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e("QSB.SearchSettingsImpl", "Couldn't load default configuration.");
    }
    return new GsaConfigurationProto.GsaExperiments();
  }
  
  public String getHotwordUsageStatsJson()
  {
    return getMainPreferences().getString("hotword_usage_stats", "");
  }
  
  public long getLastApplicationLaunch()
  {
    return getMainPreferences().getLong("last_launch", 0L);
  }
  
  public String getLastFailedQuery()
  {
    return getMainPreferences().getString("last_failed_query", "");
  }
  
  public long getLastFailedQueryUpdated()
  {
    return getMainPreferences().getLong("last_failed_query_updated", 0L);
  }
  
  public String getLastQuery()
  {
    return getMainPreferences().getString("last_query", "");
  }
  
  public long getLastQueryUpdated()
  {
    return getMainPreferences().getLong("last_query_updated", 0L);
  }
  
  public String getLastRunSystemBuild()
  {
    return getStartupPreferences().getString("last_run_system_build", null);
  }
  
  public int getLastRunVersion()
  {
    return getStartupPreferences().getInt("last_run_version", -1);
  }
  
  protected SharedPreferencesExt getMainPreferences()
  {
    return this.mPrefController.getMainPreferences();
  }
  
  public boolean getPersonalizedSearchEnabled()
  {
    return getMainPreferences().getBoolean("personalized_search_bool", this.mContext.getResources().getBoolean(2131755023));
  }
  
  public long getRefreshWebViewCookiesAt()
  {
    return getMainPreferences().getLong("refresh_webview_cookies_at", 0L);
  }
  
  @Nonnull
  public RelationshipContactInfo getRelationshipContactInfo()
  {
    byte[] arrayOfByte = getMainPreferences().getBytes("gsa_relationship_contact_info", null);
    if (arrayOfByte == null) {
      return new RelationshipContactInfo();
    }
    try
    {
      RelationshipContactInfo localRelationshipContactInfo = RelationshipContactInfo.parseFrom(arrayOfByte);
      return localRelationshipContactInfo;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      VelvetStrictMode.logWDeveloper("QSB.SearchSettingsImpl", "Couldn't load relationship contact mapping.");
    }
    return new RelationshipContactInfo();
  }
  
  public String getSafeSearch()
  {
    if (getMainPreferences().getBoolean("safe_search", false)) {
      return this.mContext.getResources().getString(2131362003);
    }
    return this.mContext.getResources().getString(2131362004);
  }
  
  public String getSearchDomainCountryCode()
  {
    return getMainPreferences().getString("search_domain_country_code", null);
  }
  
  public String getSearchDomainLanguage()
  {
    return getMainPreferences().getString("search_language", null);
  }
  
  public String getSearchDomainPreference()
  {
    return getMainPreferences().getString("search_domain", null);
  }
  
  public String getSearchDomainSchemePreference()
  {
    return getMainPreferences().getString("search_domain_scheme", "http");
  }
  
  public boolean getSignedOut()
  {
    return getStartupPreferences().getBoolean("signed_out", false);
  }
  
  protected SharedPreferencesExt getStartupPreferences()
  {
    return this.mPrefController.getStartupPreferences();
  }
  
  public String getTextSearchTokenTypeRefreshed()
  {
    return getMainPreferences().getString("text_search_token_type", "");
  }
  
  public int getVoiceActionDiscoveryInstantPeekCount()
  {
    return getMainPreferences().getInt("voice_action_discovery_instant_peek_count", 0);
  }
  
  public String getVoiceSearchInstallId()
  {
    try
    {
      Object localObject2 = getMainPreferences().getString("install-id", null);
      if (localObject2 == null)
      {
        String str = createAndSetInstallId();
        localObject2 = str;
      }
      return localObject2;
    }
    finally {}
  }
  
  public byte[] getWebCorpora()
  {
    return getMainPreferences().getBytes("web_corpora_config", null);
  }
  
  public String getWebCorporaConfigUrl()
  {
    return getMainPreferences().getString("web_corpora_config_url", "");
  }
  
  public String getWebViewLoggedInAccount()
  {
    return getMainPreferences().getString("webview_logged_in_account", "");
  }
  
  public String getWebViewLoggedInDomain()
  {
    return getMainPreferences().getString("webview_logged_in_domain", "");
  }
  
  public void incrementVoiceActionDiscoveryInstantPeekCount()
  {
    storeIntInMainPrefs("voice_action_discovery_instant_peek_count", 1 + getVoiceActionDiscoveryInstantPeekCount());
  }
  
  public boolean isActionDiscoveryDataAvailable(String paramString)
  {
    return getMainPreferences().contains("action_discovery_data_" + paramString);
  }
  
  public boolean isAppHistoryReportingEnabled()
  {
    return getMainPreferences().getBoolean("app_history_reporting_enabled", true);
  }
  
  public boolean isDebugWeinreEnabled()
  {
    return getMainPreferences().getBoolean("debug_js_injection_enabled", false);
  }
  
  public boolean isSourceEnabled(Source paramSource)
  {
    boolean bool = paramSource.isEnabledByDefault();
    String str = getSourceEnabledPreference(paramSource);
    return getMainPreferences().getBoolean(str, bool);
  }
  
  public boolean needLegacyClickStatsUpgrade()
  {
    return getMainPreferences().getBoolean("need_source_stats_upgrade", false);
  }
  
  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.mPrefController.registerChangeListener(paramOnSharedPreferenceChangeListener);
  }
  
  public void setActionDiscoveryData(String paramString, byte[] paramArrayOfByte)
  {
    storeBytesInMainPrefs("action_discovery_data_" + paramString, paramArrayOfByte);
  }
  
  public void setActionDiscoveryDataUri(String paramString1, String paramString2)
  {
    storeStringInMainPrefs("action_discovery_data_url_" + paramString1, paramString2);
  }
  
  public void setBackgroundTaskEarliestNextRun(String paramString, long paramLong)
  {
    storeLongInMainPrefs("background_task_earliest_next_run_" + paramString, paramLong);
  }
  
  public void setBackgroundTaskForcedRun(String paramString, long paramLong)
  {
    storeLongInMainPrefs("background_task_forced_run_" + paramString, paramLong);
  }
  
  public void setCachedUserAgentBase(String paramString)
  {
    storeStringInMainPrefs("user_agent", paramString);
  }
  
  public void setCachedZeroQueryWebResults(String paramString)
  {
    storeStringInMainPrefs("zero_query_web_results", paramString);
  }
  
  public void setDebugFeaturesLevel(int paramInt)
  {
    SharedPreferencesExt.Editor localEditor = getStartupPreferences().edit();
    localEditor.putInt("debug_features_level", paramInt);
    localEditor.apply();
  }
  
  public void setDebugGsaConfigOverridableFlags(Set<String> paramSet)
  {
    getMainPreferences().edit().putStringSet("debug_gsa_config_overridable_flags", paramSet).commit();
  }
  
  public void setFirstHotwordHintShownAtTime(long paramLong)
  {
    storeLongInMainPrefs("first_hotword_hint_shown_at", paramLong);
  }
  
  public void setGoogleAccountToUse(@Nullable String paramString)
  {
    SharedPreferencesExt.Editor localEditor = getStartupPreferences().edit();
    if (paramString == null) {
      localEditor.remove("google_account");
    }
    for (;;)
    {
      localEditor.apply();
      return;
      localEditor.putString("google_account", paramString);
    }
  }
  
  public void setGsaConfigChecksum(String paramString)
  {
    getStartupPreferences().edit().putString("gsa_config_checksum", paramString).commit();
  }
  
  public void setGsaConfigOverride(GsaConfigurationProto.GsaExperiments paramGsaExperiments)
  {
    getStartupPreferences().edit().putBytes("gsa_config_overrides", paramGsaExperiments.toByteArray()).commit();
  }
  
  public void setGsaConfigServer(GsaConfigurationProto.GsaExperiments paramGsaExperiments)
  {
    getStartupPreferences().edit().putBytes("gsa_config_server", paramGsaExperiments.toByteArray()).commit();
  }
  
  public void setGservicesOverridesJson(String paramString)
  {
    storeStringInMainPrefs("gservices_overrides", paramString);
  }
  
  public void setHotwordUsageStatsJson(String paramString)
  {
    storeStringInMainPrefs("hotword_usage_stats", paramString);
  }
  
  public void setLastApplicationLaunch(long paramLong)
  {
    storeLongInMainPrefs("last_launch", paramLong);
  }
  
  public void setLastFailedQuery(String paramString, long paramLong)
  {
    getMainPreferences().edit().putString("last_failed_query", paramString).putLong("last_failed_query_updated", paramLong).apply();
  }
  
  public void setLastQuery(String paramString, long paramLong)
  {
    getMainPreferences().edit().putString("last_query", paramString).putLong("last_query_updated", paramLong).apply();
  }
  
  public void setLastRunSystemBuild(String paramString)
  {
    getStartupPreferences().edit().putString("last_run_system_build", paramString).apply();
  }
  
  public void setLastRunVersion(int paramInt)
  {
    getStartupPreferences().edit().putInt("last_run_version", paramInt).apply();
  }
  
  public void setRefreshWebViewCookiesAt(long paramLong)
  {
    storeLongInMainPrefs("refresh_webview_cookies_at", paramLong);
  }
  
  @Nonnull
  public void setRelationshipContactInfo(RelationshipContactInfo paramRelationshipContactInfo)
  {
    getMainPreferences().edit().putBytes("gsa_relationship_contact_info", paramRelationshipContactInfo.toByteArray()).commit();
  }
  
  public void setSearchDomain(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    setSearchDomain(paramString1, paramString2, paramString3, paramString4, System.currentTimeMillis());
  }
  
  public void setSignedOut(boolean paramBoolean)
  {
    SharedPreferencesExt.Editor localEditor = getStartupPreferences().edit();
    localEditor.putBoolean("signed_out", paramBoolean);
    localEditor.apply();
  }
  
  public void setSourceClickStats(String paramString1, String paramString2)
  {
    storeStringInMainPrefs(getSourceClickStatsPreference(paramString1), paramString2);
  }
  
  public void setSourceClickStatsUpgradeNoLongerNeeded()
  {
    getMainPreferences().edit().remove("source_stats_").apply();
  }
  
  public void setTextSearchTokenTypeRefreshed(String paramString)
  {
    getMainPreferences().edit().putString("text_search_token_type", paramString).apply();
  }
  
  public void setUseGoogleCom(boolean paramBoolean)
  {
    storeBooleanInMainPrefs("use_google_com", paramBoolean);
  }
  
  public void setWebCorpora(byte[] paramArrayOfByte)
  {
    storeBytesInMainPrefs("web_corpora_config", paramArrayOfByte);
  }
  
  public void setWebCorporaConfigUrl(String paramString)
  {
    storeStringInMainPrefs("web_corpora_config_url", paramString);
  }
  
  public void setWebViewLoggedInAccount(String paramString)
  {
    storeStringInMainPrefs("webview_logged_in_account", paramString);
  }
  
  public void setWebViewLoggedInDomain(String paramString)
  {
    storeStringInMainPrefs("webview_logged_in_domain", paramString);
  }
  
  public boolean shouldUseGoogleCom()
  {
    return getMainPreferences().getBoolean("use_google_com", false);
  }
  
  protected void storeBooleanInMainPrefs(String paramString, boolean paramBoolean)
  {
    getMainPreferences().edit().putBoolean(paramString, paramBoolean).apply();
  }
  
  protected void storeBytesInMainPrefs(String paramString, byte[] paramArrayOfByte)
  {
    getMainPreferences().edit().putBytes(paramString, paramArrayOfByte).apply();
  }
  
  protected void storeIntInMainPrefs(String paramString, int paramInt)
  {
    getMainPreferences().edit().putInt(paramString, paramInt).apply();
  }
  
  protected void storeLongInMainPrefs(String paramString, long paramLong)
  {
    getMainPreferences().edit().putLong(paramString, paramLong).apply();
  }
  
  protected void storeStringInMainPrefs(String paramString1, String paramString2)
  {
    getMainPreferences().edit().putString(paramString1, paramString2).apply();
  }
  
  public Map<String, String> takeAllLegacySourceClickStats()
  {
    Map localMap = getMainPreferences().getAllByKeyPrefix("source_stats_");
    SharedPreferencesExt.Editor localEditor = getMainPreferences().edit();
    Iterator localIterator1 = localMap.keySet().iterator();
    while (localIterator1.hasNext()) {
      localEditor.remove((String)localIterator1.next());
    }
    localEditor.apply();
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator2 = localMap.entrySet().iterator();
    while (localIterator2.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator2.next();
      localHashMap.put(((String)localEntry.getKey()).substring("source_stats_".length()), localEntry.getValue());
    }
    return localHashMap;
  }
  
  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.mPrefController.unregisterChangeListener(paramOnSharedPreferenceChangeListener);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchSettingsImpl
 * JD-Core Version:    0.7.0.1
 */