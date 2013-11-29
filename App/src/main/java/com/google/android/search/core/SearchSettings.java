package com.google.android.search.core;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import com.google.android.search.core.summons.Source;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.GsaExperiments;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract interface SearchSettings
{
  public abstract void addMenuItems(Menu paramMenu, boolean paramBoolean);
  
  public abstract void broadcastSettingsChanged();
  
  public abstract byte[] getActionDiscoveryData(@Nonnull String paramString);
  
  public abstract String getActionDiscoveryDataUri(@Nonnull String paramString);
  
  public abstract Map<String, String> getAllSourceClickStats();
  
  public abstract long getBackgroundTaskEarliestNextRun(String paramString);
  
  public abstract long getBackgroundTaskForcedRun(String paramString);
  
  public abstract String getCachedUserAgentBase();
  
  public abstract String getCachedZeroQueryWebResults();
  
  public abstract int getDebugFeaturesLevel();
  
  public abstract Set<String> getDebugGsaConfigOverridableFlags();
  
  public abstract String getDebugSearchDomainOverride();
  
  public abstract String getDebugSearchHostParam();
  
  public abstract String getDebugSearchSchemeOverride();
  
  public abstract String getDebugWeinreServerAddress();
  
  public abstract long getFirstHotwordHintShownAtTime();
  
  @Nullable
  public abstract String getGoogleAccountToUse();
  
  public abstract String getGsaConfigChecksum();
  
  public abstract GsaConfigurationProto.GsaExperiments getGsaConfigOverride();
  
  public abstract GsaConfigurationProto.GsaExperiments getGsaConfigServer();
  
  public abstract String getHotwordUsageStatsJson();
  
  public abstract long getLastApplicationLaunch();
  
  public abstract String getLastFailedQuery();
  
  public abstract long getLastFailedQueryUpdated();
  
  public abstract String getLastQuery();
  
  public abstract long getLastQueryUpdated();
  
  public abstract String getLastRunSystemBuild();
  
  public abstract int getLastRunVersion();
  
  public abstract boolean getPersonalizedSearchEnabled();
  
  public abstract long getRefreshWebViewCookiesAt();
  
  public abstract RelationshipContactInfo getRelationshipContactInfo();
  
  public abstract String getSafeSearch();
  
  public abstract String getSearchDomainCountryCode();
  
  public abstract String getSearchDomainLanguage();
  
  public abstract String getSearchDomainPreference();
  
  public abstract String getSearchDomainSchemePreference();
  
  public abstract boolean getSignedOut();
  
  public abstract String getTextSearchTokenTypeRefreshed();
  
  public abstract int getVoiceActionDiscoveryInstantPeekCount();
  
  public abstract String getVoiceSearchInstallId();
  
  public abstract byte[] getWebCorpora();
  
  public abstract String getWebCorporaConfigUrl();
  
  public abstract String getWebViewLoggedInAccount();
  
  public abstract String getWebViewLoggedInDomain();
  
  public abstract void incrementVoiceActionDiscoveryInstantPeekCount();
  
  public abstract boolean isActionDiscoveryDataAvailable(@Nonnull String paramString);
  
  public abstract boolean isAppHistoryReportingEnabled();
  
  public abstract boolean isDebugWeinreEnabled();
  
  public abstract boolean isSourceEnabled(Source paramSource);
  
  public abstract boolean needLegacyClickStatsUpgrade();
  
  public abstract void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener);
  
  public abstract void setActionDiscoveryData(@Nonnull String paramString, byte[] paramArrayOfByte);
  
  public abstract void setActionDiscoveryDataUri(@Nonnull String paramString1, String paramString2);
  
  public abstract void setBackgroundTaskEarliestNextRun(String paramString, long paramLong);
  
  public abstract void setBackgroundTaskForcedRun(String paramString, long paramLong);
  
  public abstract void setCachedUserAgentBase(String paramString);
  
  public abstract void setCachedZeroQueryWebResults(String paramString);
  
  public abstract void setDebugFeaturesLevel(int paramInt);
  
  public abstract void setDebugGsaConfigOverridableFlags(Set<String> paramSet);
  
  public abstract void setFirstHotwordHintShownAtTime(long paramLong);
  
  public abstract void setGoogleAccountToUse(@Nullable String paramString);
  
  public abstract void setGsaConfigChecksum(String paramString);
  
  public abstract void setGsaConfigOverride(GsaConfigurationProto.GsaExperiments paramGsaExperiments);
  
  public abstract void setGsaConfigServer(GsaConfigurationProto.GsaExperiments paramGsaExperiments);
  
  public abstract void setGservicesOverridesJson(String paramString);
  
  public abstract void setHotwordUsageStatsJson(String paramString);
  
  public abstract void setLastApplicationLaunch(long paramLong);
  
  public abstract void setLastFailedQuery(String paramString, long paramLong);
  
  public abstract void setLastQuery(String paramString, long paramLong);
  
  public abstract void setLastRunSystemBuild(String paramString);
  
  public abstract void setLastRunVersion(int paramInt);
  
  public abstract void setRefreshWebViewCookiesAt(long paramLong);
  
  public abstract void setRelationshipContactInfo(RelationshipContactInfo paramRelationshipContactInfo);
  
  public abstract void setSearchDomain(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void setSignedOut(boolean paramBoolean);
  
  public abstract void setSourceClickStats(String paramString1, String paramString2);
  
  public abstract void setSourceClickStatsUpgradeNoLongerNeeded();
  
  public abstract void setTextSearchTokenTypeRefreshed(String paramString);
  
  public abstract void setUseGoogleCom(boolean paramBoolean);
  
  public abstract void setWebCorpora(byte[] paramArrayOfByte);
  
  public abstract void setWebCorporaConfigUrl(String paramString);
  
  public abstract void setWebViewLoggedInAccount(String paramString);
  
  public abstract void setWebViewLoggedInDomain(String paramString);
  
  public abstract boolean shouldUseGoogleCom();
  
  public abstract Map<String, String> takeAllLegacySourceClickStats();
  
  public abstract void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchSettings
 * JD-Core Version:    0.7.0.1
 */