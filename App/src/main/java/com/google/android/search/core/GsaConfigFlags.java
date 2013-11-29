package com.google.android.search.core;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.shared.util.Util;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.GsaExperiments;
import com.google.gws.plugins.searchapp.GsaConfigurationProto.KeyValuePair;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class GsaConfigFlags
{
  static final int ARRAY_JSON = 2;
  static final int ARRAY_STRICT_CSV = 1;
  private Integer[] mExperimentIds = new Integer[0];
  private Map<String, GsaConfigurationProto.KeyValuePair> mGsaConfigFlags;
  private final Resources mResources;
  private long mTimestamp = -1L;
  
  public GsaConfigFlags(Resources paramResources, SearchSettings paramSearchSettings)
  {
    this.mResources = paramResources;
    updateGsaConfig(paramSearchSettings.getGsaConfigServer(), paramSearchSettings.getGsaConfigOverride());
  }
  
  private String[] getFollowOnLocales()
  {
    return getStringArrayFlag(2131492907, 1);
  }
  
  private String[] getPumpkinLocales()
  {
    return getStringArrayFlag(2131492906, 1);
  }
  
  public boolean areImageDownloadsEnabled()
  {
    return getBooleanFlag(2131755071);
  }
  
  public void clearGsaConfigFlags()
  {
    this.mGsaConfigFlags.clear();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    if ((this.mGsaConfigFlags != null) && (this.mGsaConfigFlags.size() > 0))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Experiment flags:");
      Iterator localIterator = this.mGsaConfigFlags.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        GsaConfigurationProto.KeyValuePair localKeyValuePair = (GsaConfigurationProto.KeyValuePair)this.mGsaConfigFlags.get(str);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print(str + ":");
        if (localKeyValuePair.hasBoolValue()) {
          paramPrintWriter.println(localKeyValuePair.getBoolValue());
        } else if (localKeyValuePair.hasIntValue()) {
          paramPrintWriter.println(localKeyValuePair.getIntValue());
        } else if (localKeyValuePair.hasStringValue()) {
          paramPrintWriter.println(localKeyValuePair.getStringValue());
        }
      }
    }
    if ((this.mExperimentIds != null) && (this.mExperimentIds.length > 0))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Experiment Ids:");
      for (Integer localInteger : this.mExperimentIds) {
        paramPrintWriter.print(localInteger.toString() + ",");
      }
      paramPrintWriter.println();
    }
  }
  
  public boolean getAddReminderRecurrenceEnabledVersion()
  {
    return getBooleanFlag(2131755069);
  }
  
  protected boolean getBooleanFlag(int paramInt)
  {
    GsaConfigurationProto.KeyValuePair localKeyValuePair = getFlag(paramInt);
    if (localKeyValuePair != null) {
      return localKeyValuePair.getBoolValue();
    }
    return this.mResources.getBoolean(paramInt);
  }
  
  public String[] getEnabledRemindersSettingsLanguages()
  {
    return getStringArrayFlag(2131492910, 1);
  }
  
  public String[] getEnabledRemindersSettingsLocales()
  {
    return getStringArrayFlag(2131492911, 1);
  }
  
  public Integer[] getExperimentIds()
  {
    return this.mExperimentIds;
  }
  
  @Nullable
  protected GsaConfigurationProto.KeyValuePair getFlag(int paramInt)
  {
    String str = this.mResources.getResourceEntryName(paramInt);
    if (getGsaConfigFlags().containsKey(str)) {
      return (GsaConfigurationProto.KeyValuePair)getGsaConfigFlags().get(str);
    }
    return null;
  }
  
  public Map<String, GsaConfigurationProto.KeyValuePair> getGsaConfigFlags()
  {
    return this.mGsaConfigFlags;
  }
  
  public String getGwsFetchReminderConfirmationUrlPath()
  {
    return getStringFlag(2131362031);
  }
  
  public int getIcingContactsDisplayNameSectionWeight()
  {
    return getIntegerFlag(2131427443);
  }
  
  public int getIcingContactsEmailsSectionWeight()
  {
    return getIntegerFlag(2131427445);
  }
  
  public int getIcingContactsGivenNamesSectionWeight()
  {
    return getIntegerFlag(2131427444);
  }
  
  public boolean getIcingContactsIndexEmail()
  {
    return getBooleanFlag(2131755052);
  }
  
  public boolean getIcingContactsIndexNote()
  {
    return getBooleanFlag(2131755053);
  }
  
  public boolean getIcingContactsIndexOrganization()
  {
    return getBooleanFlag(2131755054);
  }
  
  public boolean getIcingContactsIndexPhoneNumbers()
  {
    return getBooleanFlag(2131755055);
  }
  
  public boolean getIcingContactsIndexPostalAddress()
  {
    return getBooleanFlag(2131755056);
  }
  
  public boolean getIcingContactsIndexPrefixesDisplayName()
  {
    return getBooleanFlag(2131755057);
  }
  
  public boolean getIcingContactsIndexPrefixesEmail()
  {
    return getBooleanFlag(2131755060);
  }
  
  public boolean getIcingContactsIndexPrefixesGivenNames()
  {
    return getBooleanFlag(2131755058);
  }
  
  public boolean getIcingContactsIndexPrefixesNickname()
  {
    return getBooleanFlag(2131755059);
  }
  
  public boolean getIcingContactsIndexPrefixesNote()
  {
    return getBooleanFlag(2131755061);
  }
  
  public boolean getIcingContactsIndexPrefixesOrganization()
  {
    return getBooleanFlag(2131755062);
  }
  
  public boolean getIcingContactsIndexPrefixesPhoneNumbers()
  {
    return getBooleanFlag(2131755063);
  }
  
  public boolean getIcingContactsIndexPrefixesPostalAddress()
  {
    return getBooleanFlag(2131755064);
  }
  
  public boolean getIcingContactsMatchGlobalNicknames()
  {
    return getBooleanFlag(2131755065);
  }
  
  public int getIcingContactsNicknameSectionWeight()
  {
    return getIntegerFlag(2131427446);
  }
  
  public int getIcingContactsNotesSectionWeight()
  {
    return getIntegerFlag(2131427447);
  }
  
  public int getIcingContactsOrganizationSectionWeight()
  {
    return getIntegerFlag(2131427448);
  }
  
  public int getIcingContactsPhoneNumbersSectionWeight()
  {
    return getIntegerFlag(2131427449);
  }
  
  public int getIcingContactsPostalAddressSectionWeight()
  {
    return getIntegerFlag(2131427450);
  }
  
  public String getImageMetatDataUrlPattern()
  {
    return getStringFlag(2131362029);
  }
  
  public String[] getInAppWebPageAuthFailureUris()
  {
    return getStringArrayFlag(2131492909, 1);
  }
  
  protected int getIntegerFlag(int paramInt)
  {
    GsaConfigurationProto.KeyValuePair localKeyValuePair = getFlag(paramInt);
    if (localKeyValuePair != null) {
      return localKeyValuePair.getIntValue();
    }
    return this.mResources.getInteger(paramInt);
  }
  
  public String getLaunchUrlQueryGen204Pattern()
  {
    return getStringFlag(2131362033);
  }
  
  public String getManageSearchHistoryUrlFormat()
  {
    return getStringFlag(2131362032);
  }
  
  public int getMarinerBackgroundPartialRefreshRateLimitMinutes()
  {
    return getIntegerFlag(2131427451);
  }
  
  public int getMarinerBackgroundRefreshRateLimitMinutes()
  {
    return getIntegerFlag(2131427452);
  }
  
  public int getMaxPromotedSuggestions()
  {
    return getIntegerFlag(2131427439);
  }
  
  public String[] getMolinoInjectTopContacts()
  {
    return getStringArrayFlag(2131492908, 1);
  }
  
  public int getPumpkinAlternatesToTag()
  {
    return getIntegerFlag(2131427440);
  }
  
  public int getPumpkinEnabledActions()
  {
    return getIntegerFlag(2131427441);
  }
  
  public int getRefreshSearchHistoryDelay()
  {
    return getIntegerFlag(2131427455);
  }
  
  public String getReminderTokenType()
  {
    return getStringFlag(2131362030);
  }
  
  public int getS3MaxTopContactNamesToSend()
  {
    return getIntegerFlag(2131427442);
  }
  
  public String getSearchSourceParam()
  {
    return getStringFlag(2131362035);
  }
  
  public String getSearchUrlFormat()
  {
    return getStringFlag(2131362034);
  }
  
  public int getServerEndpointingActivityTimeoutMs()
  {
    return getIntegerFlag(2131427456);
  }
  
  public int getShowLastFailedQueryInZeroPrefixSuggestMin()
  {
    return getIntegerFlag(2131427454);
  }
  
  public int getShowLastQueryInZeroPrefixSuggestMin()
  {
    return getIntegerFlag(2131427453);
  }
  
  public boolean getShowLastQueryOnSuggestListBottom()
  {
    return getBooleanFlag(2131755075);
  }
  
  public boolean getShowOriginalQueryInGenieResultSuggest()
  {
    return getBooleanFlag(2131755074);
  }
  
  protected String[] getStringArrayFlag(int paramInt1, int paramInt2)
  {
    GsaConfigurationProto.KeyValuePair localKeyValuePair = getFlag(paramInt1);
    if (localKeyValuePair != null)
    {
      String str = localKeyValuePair.getStringValue();
      if (TextUtils.isEmpty(str)) {
        return new String[0];
      }
      if (paramInt2 == 1) {
        return str.split(",");
      }
      if (paramInt2 == 2) {
        return Util.jsonToStringArray(str);
      }
      throw new IllegalArgumentException("Unknown string array encoding: " + paramInt2);
    }
    return this.mResources.getStringArray(paramInt1);
  }
  
  protected String getStringFlag(int paramInt)
  {
    GsaConfigurationProto.KeyValuePair localKeyValuePair = getFlag(paramInt);
    if (localKeyValuePair != null) {
      return localKeyValuePair.getStringValue();
    }
    return this.mResources.getString(paramInt);
  }
  
  public long getTimestampUsec()
  {
    return this.mTimestamp;
  }
  
  public boolean getZeroPrefixAppSuggestEnabled()
  {
    return getBooleanFlag(2131755066);
  }
  
  public boolean hasFollowOnLocale(String paramString)
  {
    return Util.arrayContains(getFollowOnLocales(), paramString);
  }
  
  public boolean hasPumpkinLocale(String paramString)
  {
    return Util.arrayContains(getPumpkinLocales(), paramString);
  }
  
  public boolean isBluetoothDeviceLoggingEnabled()
  {
    return getBooleanFlag(2131755079);
  }
  
  public boolean isEmbeddedEndpointingEnabled()
  {
    return getBooleanFlag(2131755078);
  }
  
  public boolean isLangagePackAutoUpdateEnabled()
  {
    return getBooleanFlag(2131755072);
  }
  
  public boolean isPumpkinOpenAppFastEnabled()
  {
    return getBooleanFlag(2131755076);
  }
  
  public boolean isSearchHistoryInAppEnabled()
  {
    return (getBooleanFlag(2131755068)) || (Feature.SEARCH_HISTORY_IN_APP.isEnabled());
  }
  
  public boolean isServerEndpointingEnabled()
  {
    return getBooleanFlag(2131755077);
  }
  
  public boolean isSpdyEnabledForInAppWebPage()
  {
    return getBooleanFlag(2131755067);
  }
  
  public boolean isVoiceCorrectionEnabled()
  {
    return getBooleanFlag(2131755080);
  }
  
  public boolean logLaunchUrlWithGen_204()
  {
    return getBooleanFlag(2131755070);
  }
  
  public boolean shouldUseAmrWbEncoding()
  {
    return getBooleanFlag(2131755073);
  }
  
  public void updateGsaConfig(GsaConfigurationProto.GsaExperiments paramGsaExperiments1, GsaConfigurationProto.GsaExperiments paramGsaExperiments2)
  {
    HashMap localHashMap;
    try
    {
      this.mTimestamp = paramGsaExperiments1.getEventTimestamp();
      this.mExperimentIds = new Integer[paramGsaExperiments1.getClientExperimentIdsCount()];
      Iterator localIterator1 = paramGsaExperiments1.getClientExperimentIdsList().iterator();
      int j;
      for (int i = 0; localIterator1.hasNext(); i = j)
      {
        Integer localInteger = (Integer)localIterator1.next();
        Integer[] arrayOfInteger = this.mExperimentIds;
        j = i + 1;
        arrayOfInteger[i] = Integer.valueOf(localInteger.intValue());
      }
      localHashMap = Maps.newHashMap();
      Iterator localIterator2 = paramGsaExperiments1.getKeyValuePairList().iterator();
      while (localIterator2.hasNext())
      {
        GsaConfigurationProto.KeyValuePair localKeyValuePair2 = (GsaConfigurationProto.KeyValuePair)localIterator2.next();
        localHashMap.put(localKeyValuePair2.getKey(), localKeyValuePair2);
      }
      localIterator3 = paramGsaExperiments2.getKeyValuePairList().iterator();
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.w("GsaConfigFlags", "Unable to parse GsaConfig.");
      return;
    }
    Iterator localIterator3;
    while (localIterator3.hasNext())
    {
      GsaConfigurationProto.KeyValuePair localKeyValuePair1 = (GsaConfigurationProto.KeyValuePair)localIterator3.next();
      if (localHashMap.containsKey(localKeyValuePair1.getKey()))
      {
        ((GsaConfigurationProto.KeyValuePair)localHashMap.get(localKeyValuePair1.getKey())).mergeFrom(localKeyValuePair1.toByteArray());
      }
      else
      {
        Log.i("GsaConfigFlags", "Override config contains a flag that is no longer in the server config, this must be a custom override:" + localKeyValuePair1.getKey());
        localHashMap.put(localKeyValuePair1.getKey(), localKeyValuePair1);
      }
    }
    this.mGsaConfigFlags = ImmutableMap.copyOf(localHashMap);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GsaConfigFlags
 * JD-Core Version:    0.7.0.1
 */