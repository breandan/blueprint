package com.google.android.search.core.preferences;

import android.accounts.Account;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.sidekick.main.sync.MessageMicroUtil;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.android.sidekick.main.sync.StateDifference;
import com.google.android.velvet.VelvetApplication;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.DisplayConfiguration;
import com.google.geo.sidekick.Sidekick.LocaleConfiguration;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.NextMeeting;
import com.google.geo.sidekick.Sidekick.SidekickConfigurationChanges;
import com.google.geo.sidekick.Sidekick.StateChanges;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import javax.annotation.Nullable;

public class PredictiveCardsPreferences
{
  static final String CONFIGURATION_ACCOUNT_KEY = "configuration_account";
  private static final Object MASTER_WORKING_LOCK = new Object();
  public static final RepeatedMessageInfo REPEATED_MESSAGE_INFO = new RepeatedMessageInfo();
  private NowConfigurationPreferences mNowConfigurationPreferences;
  private final Object mNowConfigurationPreferencesLock = new Object();
  private final GsaPreferenceController mPrefController;
  
  public PredictiveCardsPreferences(GsaPreferenceController paramGsaPreferenceController)
  {
    this.mPrefController = paramGsaPreferenceController;
  }
  
  private void copyMasterToWorkingFor(Account paramAccount, Sidekick.Configuration paramConfiguration)
  {
    NowConfigurationPreferences localNowConfigurationPreferences = getWorkingPreferences();
    localNowConfigurationPreferences.setBackingConfiguration(paramConfiguration.getSidekickConfiguration());
    SharedPreferences.Editor localEditor = localNowConfigurationPreferences.edit();
    localEditor.putString("configuration_account", paramAccount.name).putInt("configuration_version", VelvetApplication.getVersionCode());
    if ((paramConfiguration.hasLocaleConfiguration()) && (paramConfiguration.getLocaleConfiguration().hasBikingDirectionsEnabled())) {
      localEditor.putBoolean("locale_configuration.biking_directions_enabled", paramConfiguration.getLocaleConfiguration().getBikingDirectionsEnabled());
    }
    if (paramConfiguration.hasLocationReportingConfiguration()) {
      localEditor.putBoolean("location_reporting_configuration", true);
    }
    localEditor.apply();
  }
  
  private StateDifference<Sidekick.SidekickConfiguration> diffWorkingAgainstMasterConfigurationFor(Account paramAccount)
  {
    synchronized (MASTER_WORKING_LOCK)
    {
      if (!hasWorkingConfigurationFor(paramAccount)) {
        throw new IllegalArgumentException("Attemp to diff settings across accounts");
      }
    }
    if (getWorkingConfiguration() == null) {
      copyMasterToWorkingFor(paramAccount);
    }
    StateDifference localStateDifference = StateDifference.newStateDifference(getMasterConfigurationFor(paramAccount).getSidekickConfiguration(), getWorkingConfiguration(), REPEATED_MESSAGE_INFO);
    return localStateDifference;
  }
  
  private SharedPreferencesExt getPrefs()
  {
    return this.mPrefController.getMainPreferences();
  }
  
  private static String getSavedConfigurationKey(Account paramAccount)
  {
    return "configuration_bytes_key_" + paramAccount.name;
  }
  
  public void clearWorkingConfiguration()
  {
    synchronized (MASTER_WORKING_LOCK)
    {
      NowConfigurationPreferences localNowConfigurationPreferences = getWorkingPreferences();
      localNowConfigurationPreferences.edit().remove("configuration_account").apply();
      localNowConfigurationPreferences.clearBackingConfiguration();
      return;
    }
  }
  
  public void copyMasterToWorkingFor(Account paramAccount)
  {
    Sidekick.Configuration localConfiguration;
    synchronized (MASTER_WORKING_LOCK)
    {
      localConfiguration = getMasterConfigurationFor(paramAccount);
      if (localConfiguration == null) {
        throw new IllegalStateException("Account " + paramAccount.name + " does not have a master configuration");
      }
    }
    copyMasterToWorkingFor(paramAccount, localConfiguration);
  }
  
  @Nullable
  public Sidekick.DisplayConfiguration getDisplayConfigurationFor(Account paramAccount)
  {
    Sidekick.Configuration localConfiguration = getMasterConfigurationFor(paramAccount);
    if (localConfiguration != null) {
      return localConfiguration.getDisplayConfiguration();
    }
    return null;
  }
  
  @Nullable
  public Sidekick.Configuration getMasterConfigurationFor(Account paramAccount)
  {
    byte[] arrayOfByte = getPrefs().getBytes(getSavedConfigurationKey(paramAccount), null);
    if (arrayOfByte == null) {
      return null;
    }
    Sidekick.Configuration localConfiguration = new Sidekick.Configuration();
    try
    {
      MessageMicroUtil.decodeFromByteArray(localConfiguration, arrayOfByte);
      return localConfiguration;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      getPrefs().edit().remove(getSavedConfigurationKey(paramAccount)).apply();
    }
    return null;
  }
  
  @Nullable
  public int getNextMeetingNotificationType()
  {
    Sidekick.SidekickConfiguration localSidekickConfiguration = getWorkingPreferences().getBackingConfiguration();
    if (localSidekickConfiguration == null) {
      return -1;
    }
    return localSidekickConfiguration.getNextMeeting().getNotificationSetting();
  }
  
  @Nullable
  public Sidekick.StateChanges getSettingsChanges(Account paramAccount)
  {
    synchronized (MASTER_WORKING_LOCK)
    {
      if (!getWorkingPreferences().isDirty()) {
        return null;
      }
      StateDifference localStateDifference = diffWorkingAgainstMasterConfigurationFor(paramAccount);
      Sidekick.SidekickConfigurationChanges localSidekickConfigurationChanges = new Sidekick.SidekickConfigurationChanges();
      Sidekick.SidekickConfiguration localSidekickConfiguration1 = (Sidekick.SidekickConfiguration)localStateDifference.compareForAdds();
      if (!MessageMicroUtil.isEmpty(localSidekickConfiguration1)) {
        localSidekickConfigurationChanges.setAdds(localSidekickConfiguration1);
      }
      Sidekick.SidekickConfiguration localSidekickConfiguration2 = (Sidekick.SidekickConfiguration)localStateDifference.compareForDeletes();
      if (!MessageMicroUtil.isEmpty(localSidekickConfiguration2)) {
        localSidekickConfigurationChanges.setDeletes(localSidekickConfiguration2);
      }
      Sidekick.SidekickConfiguration localSidekickConfiguration3 = (Sidekick.SidekickConfiguration)localStateDifference.compareForUpdates();
      if (!MessageMicroUtil.isEmpty(localSidekickConfiguration3)) {
        localSidekickConfigurationChanges.setUpdates(localSidekickConfiguration3);
      }
      return new Sidekick.StateChanges().setSidekickConfigurationChanges(localSidekickConfigurationChanges);
    }
  }
  
  public int getTravelMode(int paramInt)
  {
    Sidekick.SidekickConfiguration localSidekickConfiguration = getWorkingPreferences().getBackingConfiguration();
    if (localSidekickConfiguration == null) {
      return -1;
    }
    if (paramInt == 1) {
      return localSidekickConfiguration.getCommuteTravelMode();
    }
    return localSidekickConfiguration.getOtherTravelMode();
  }
  
  public int getUnits()
  {
    Sidekick.SidekickConfiguration localSidekickConfiguration = getWorkingPreferences().getBackingConfiguration();
    if (localSidekickConfiguration == null) {
      return -1;
    }
    return localSidekickConfiguration.getUnits();
  }
  
  @Nullable
  public Sidekick.SidekickConfiguration getWorkingConfiguration()
  {
    return getWorkingPreferences().getBackingConfiguration();
  }
  
  public NowConfigurationPreferences getWorkingPreferences()
  {
    synchronized (this.mNowConfigurationPreferencesLock)
    {
      if (this.mNowConfigurationPreferences == null) {
        this.mNowConfigurationPreferences = new NowConfigurationPreferences(REPEATED_MESSAGE_INFO, getPrefs(), "configuration_working");
      }
      NowConfigurationPreferences localNowConfigurationPreferences = this.mNowConfigurationPreferences;
      return localNowConfigurationPreferences;
    }
  }
  
  public boolean hasWorkingConfigurationFor(Account paramAccount)
  {
    String str = getPrefs().getString("configuration_account", null);
    return paramAccount.name.equals(str);
  }
  
  public boolean isSavedConfigurationVersionCurrent()
  {
    int i = VelvetApplication.getVersionCode();
    int j = getPrefs().getInt("configuration_version", 0);
    boolean bool = false;
    if (i == j) {
      bool = true;
    }
    return bool;
  }
  
  public void setMasterConfigurationFor(Account paramAccount, Sidekick.Configuration paramConfiguration, boolean paramBoolean)
  {
    synchronized (MASTER_WORKING_LOCK)
    {
      Sidekick.Configuration localConfiguration1 = getMasterConfigurationFor(paramAccount);
      byte[] arrayOfByte1 = MessageMicroUtil.encodeToByteArray(paramConfiguration);
      byte[] arrayOfByte2 = arrayOfByte1;
      if (localConfiguration1 != null) {
        localConfiguration2 = localConfiguration1;
      }
      try
      {
        localConfiguration2.mergeFrom(arrayOfByte2);
        if (localConfiguration1 != null) {
          arrayOfByte2 = MessageMicroUtil.encodeToByteArray(localConfiguration2);
        }
        SharedPreferencesExt.Editor localEditor = getPrefs().edit();
        localEditor.putBytes(getSavedConfigurationKey(paramAccount), arrayOfByte2);
        localEditor.apply();
        if (paramBoolean) {
          copyMasterToWorkingFor(paramAccount, localConfiguration2);
        }
        return;
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
      {
        throw new RuntimeException(localInvalidProtocolBufferMicroException);
      }
      Sidekick.Configuration localConfiguration2 = new Sidekick.Configuration();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PredictiveCardsPreferences
 * JD-Core Version:    0.7.0.1
 */