package com.google.android.search.core.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.DataSetObservable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import com.google.android.search.core.CommuteSharingSettingController;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.shared.util.IntentUtilsImpl;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.voicesearch.personalization.PersonalizationHelper;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class SearchPreferenceControllerFactory
  implements SharedPreferences.OnSharedPreferenceChangeListener, PreferenceController
{
  private static final Set<String> USER_ACCOUNT_SETTINGS = ImmutableSet.of("google_account", "use_google_com");
  private final Activity mActivity;
  private final VelvetBackgroundTasks mBackgroundTasks;
  private final PredictiveCardsPreferences mCardsPrefs;
  private final GsaConfigFlags mConfigFlags;
  private final Map<String, PreferenceController> mControllers;
  private final CoreSearchServices mCoreSearchServices;
  private final Supplier<Integer> mDeviceClassSupplier;
  private final GooglePlayServicesHelper mGmsHelper;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final Greco3DataManager mGreco3DataManager;
  private final GsaPreferenceController mGsaPreferenceController;
  private final GlobalSearchServices mGss;
  private final LocationSettings mLocationSettings;
  private final LoginHelper mLoginHelper;
  private final NetworkClient mNetworkClient;
  private final NetworkInformation mNetworkInformation;
  private final PersonalizationHelper mPersonalizationHelper;
  private PreferenceScreen mScreen;
  private final SearchConfig mSearchConfig;
  private final DataSetObservable mSearchHistoryChangedObservable;
  private final SearchHistoryHelper mSearchHistoryHelper;
  private final SearchSettings mSettings;
  private final Executor mUiExecutor;
  private final SearchUrlHelper mUrlHelper;
  private final Settings mVoiceSettings;
  
  public SearchPreferenceControllerFactory(CoreSearchServices paramCoreSearchServices, PersonalizationHelper paramPersonalizationHelper, Activity paramActivity, GlobalSearchServices paramGlobalSearchServices, Supplier<Integer> paramSupplier, Greco3DataManager paramGreco3DataManager, NetworkClient paramNetworkClient, SearchHistoryHelper paramSearchHistoryHelper, GsaPreferenceController paramGsaPreferenceController, GmsLocationReportingHelper paramGmsLocationReportingHelper, Settings paramSettings, Executor paramExecutor)
  {
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mSearchConfig = paramCoreSearchServices.getConfig();
    this.mConfigFlags = paramCoreSearchServices.getGsaConfigFlags();
    this.mSettings = paramCoreSearchServices.getSearchSettings();
    this.mVoiceSettings = paramSettings;
    this.mPersonalizationHelper = paramPersonalizationHelper;
    this.mLoginHelper = paramCoreSearchServices.getLoginHelper();
    this.mUrlHelper = paramCoreSearchServices.getSearchUrlHelper();
    this.mActivity = paramActivity;
    this.mLocationSettings = paramCoreSearchServices.getLocationSettings();
    this.mControllers = Maps.newHashMap();
    this.mGss = paramGlobalSearchServices;
    this.mSearchHistoryChangedObservable = paramCoreSearchServices.getSearchHistoryChangedObservable();
    this.mDeviceClassSupplier = paramSupplier;
    this.mGreco3DataManager = paramGreco3DataManager;
    this.mNetworkClient = paramNetworkClient;
    this.mBackgroundTasks = paramCoreSearchServices.getBackgroundTasks();
    this.mSettings.registerOnSharedPreferenceChangeListener(this);
    this.mSearchHistoryHelper = paramSearchHistoryHelper;
    this.mNetworkInformation = paramCoreSearchServices.getNetworkInfo();
    this.mGsaPreferenceController = paramGsaPreferenceController;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
    this.mUiExecutor = paramExecutor;
    this.mCardsPrefs = this.mCoreSearchServices.getPredictiveCardsPreferences();
    this.mGmsHelper = this.mCoreSearchServices.getGooglePlayServicesHelper();
  }
  
  private void addController(String paramString, PreferenceController paramPreferenceController)
  {
    this.mControllers.put(paramString, paramPreferenceController);
  }
  
  private PreferenceController createControllerFor(Preference paramPreference)
  {
    String str = getControllerId(paramPreference);
    if ("google_location_settings".equals(str)) {
      return new LocationSettingsController(this.mSettings, this.mActivity, this.mLocationSettings);
    }
    if ("commute_sharing".equals(str)) {
      return new CommuteSharingSettingController(this.mCardsPrefs);
    }
    if ("search_sources".equals(str)) {
      return new SearchableItemsController(this.mSettings, this.mGss, this.mActivity);
    }
    if ("manage_search_history".equals(str)) {
      return new GoogleAccountSettingsController(this.mSettings, this.mLoginHelper, this.mConfigFlags, this.mActivity, this.mUrlHelper, this.mSearchHistoryChangedObservable, this.mNetworkClient, new IntentUtilsImpl(), this.mSearchHistoryHelper, this.mLocationSettings, this.mGmsLocationReportingHelper, this.mUiExecutor);
    }
    if ("use_google_com".equals(str)) {
      return new UseGoogleComSettingController(this.mSearchConfig, this.mSettings, this.mUrlHelper, this.mActivity);
    }
    if ("language".equals(str)) {
      return new LanguageSettingController(this.mSettings, this.mVoiceSettings, this.mCoreSearchServices, this.mActivity);
    }
    if ("ttsMode".equals(str)) {
      return new TtsModeSettingController(this.mSettings);
    }
    if ("profanityFilter".equals(str)) {
      return new DefaultSettingController(this.mSettings);
    }
    if ("downloadLanguagePacks".equals(str)) {
      return new DownloadLanguagePacksSettingController(this.mSettings, this.mDeviceClassSupplier);
    }
    if ("managePersonalization".equals(str)) {
      return new ManagePersonalizationSettingController(this.mSettings, this.mActivity, this.mVoiceSettings, this.mPersonalizationHelper);
    }
    if ("personalizedResults".equals(str)) {
      return new PersonalizationSettingController(this.mSettings, this.mActivity, this.mVoiceSettings, this.mPersonalizationHelper);
    }
    if ("bluetoothHeadset".equals(str)) {
      return new BluetoothHeadsetSettingController(this.mSearchConfig);
    }
    if ("hotwordDetector".equals(str)) {
      return new HotwordSettingController(this.mVoiceSettings, this.mGreco3DataManager);
    }
    if ("tos".equals(str)) {
      return new TosPreferenceController(this.mSettings, this.mSearchConfig, this.mUrlHelper);
    }
    if ("location_tos".equals(str)) {
      return new LocationTosPreferenceController(this.mActivity);
    }
    if ("personalized_search_bool".equals(str)) {
      return new PersonalizedSearchSettingsController(this.mSearchConfig, this.mLoginHelper);
    }
    if ("safe_search".equals(str)) {
      return new SafeSearchSettingsController();
    }
    if (("debugS3Server".equals(str)) || ("debugS3Logging".equals(str)) || ("debugPersonalization".equals(str)) || ("debugConfigurationDate".equals(str)) || ("debugConfigurationExperiment".equals(str)) || ("audioLoggingEnabled".equals(str)) || ("debugSendLoggedAudio".equals(str)) || ("s3SandboxOverride".equals(str)) || ("debugRecognitionEngineRestrict".equals(str)) || ("debugTopContacts".equals(str))) {
      return new DebugVoiceController(DebugFeatures.getInstance(), this.mVoiceSettings, this.mPersonalizationHelper, this.mNetworkInformation, this.mActivity, this.mSearchConfig);
    }
    if (("debug_search_domain_override".equals(str)) || ("debug_search_scheme_override".equals(str)) || ("debug_search_host_param".equals(str)) || ("debug_js_injection_enabled".equals(str)) || ("debug_js_server_address".equals(str))) {
      return new DebugSearchController(this.mSettings, this.mSearchConfig, DebugFeatures.getInstance());
    }
    if ("features".equals(str))
    {
      Preconditions.checkState(false);
      return new FeatureController(this.mActivity, this.mGsaPreferenceController.getStartupPreferences(), this.mCoreSearchServices);
    }
    if ("debug_override_settings".equals(str))
    {
      Preconditions.checkState(false);
      return new DebugOverrideSettingsController(this.mActivity, this.mGsaPreferenceController.getStartupPreferences(), this.mCoreSearchServices);
    }
    if ("icing_manage_storage".equals(str)) {
      return new IcingStorageSettingsController(this.mGmsHelper);
    }
    if ("app_history_key".equals(str)) {
      return new AppHistorySettingsController(this.mSettings, this.mActivity);
    }
    return null;
  }
  
  private PreferenceController getControllerFor(Preference paramPreference)
  {
    String str = getControllerId(paramPreference);
    PreferenceController localPreferenceController = (PreferenceController)this.mControllers.get(str);
    if (localPreferenceController == null)
    {
      localPreferenceController = createControllerFor(paramPreference);
      if (localPreferenceController != null) {
        addController(str, localPreferenceController);
      }
    }
    return localPreferenceController;
  }
  
  private String getControllerId(Preference paramPreference)
  {
    String str = paramPreference.getKey();
    if (str == null) {
      str = null;
    }
    do
    {
      return str;
      if (("manage_search_history".equals(str)) || ("manage_location_history".equals(str)) || ("google_account".equals(str)) || ("cloud_search_history".equals(str))) {
        return "manage_search_history";
      }
    } while ((!"app_history_key".equals(str)) && (!"app_history_reset_app_history_key".equals(str)) && (!"app_history_reporting_enabled".equals(str)));
    return "app_history_key";
  }
  
  private void handlePrefenceGroup(PreferenceGroup paramPreferenceGroup)
  {
    int i = -1 + paramPreferenceGroup.getPreferenceCount();
    if (i >= 0)
    {
      Preference localPreference = paramPreferenceGroup.getPreference(i);
      if (filterPreference(localPreference)) {
        paramPreferenceGroup.removePreference(localPreference);
      }
      for (;;)
      {
        i--;
        break;
        handlePreference(localPreference);
      }
    }
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    boolean bool1 = paramPreference instanceof PreferenceGroup;
    PreferenceController localPreferenceController = null;
    if (!bool1) {
      localPreferenceController = getControllerFor(paramPreference);
    }
    boolean bool2 = false;
    if (localPreferenceController != null) {
      bool2 = localPreferenceController.filterPreference(paramPreference);
    }
    return bool2;
  }
  
  public void handlePreference(Preference paramPreference)
  {
    PreferenceController localPreferenceController = getControllerFor(paramPreference);
    if (localPreferenceController != null) {
      localPreferenceController.handlePreference(paramPreference);
    }
    do
    {
      return;
      if ((paramPreference instanceof PreferenceGroup))
      {
        handlePrefenceGroup((PreferenceGroup)paramPreference);
        return;
      }
    } while (paramPreference.getKey() == null);
    throw new UnknownPreferenceException(paramPreference);
  }
  
  public void onCreateComplete(Bundle paramBundle)
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext()) {
      ((PreferenceController)localIterator.next()).onCreateComplete(paramBundle);
    }
  }
  
  public void onDestroy()
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext()) {
      ((PreferenceController)localIterator.next()).onDestroy();
    }
    this.mControllers.clear();
    this.mSettings.unregisterOnSharedPreferenceChangeListener(this);
  }
  
  public void onPause()
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext()) {
      ((PreferenceController)localIterator.next()).onPause();
    }
  }
  
  public void onResume()
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext())
    {
      PreferenceController localPreferenceController = (PreferenceController)localIterator.next();
      localPreferenceController.setScreen(this.mScreen);
      localPreferenceController.onResume();
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext()) {
      ((PreferenceController)localIterator.next()).onSaveInstanceState(paramBundle);
    }
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (USER_ACCOUNT_SETTINGS.contains(paramString)) {
      this.mBackgroundTasks.forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
    }
  }
  
  public void onStop()
  {
    Iterator localIterator = this.mControllers.values().iterator();
    while (localIterator.hasNext()) {
      ((PreferenceController)localIterator.next()).onStop();
    }
  }
  
  public void setScreen(PreferenceScreen paramPreferenceScreen)
  {
    this.mScreen = paramPreferenceScreen;
  }
  
  private static class UnknownPreferenceException
    extends RuntimeException
  {
    public UnknownPreferenceException(Preference paramPreference)
    {
      super();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SearchPreferenceControllerFactory
 * JD-Core Version:    0.7.0.1
 */