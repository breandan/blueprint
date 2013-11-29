package com.google.android.search.core.preferences;

import android.accounts.Account;
import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.googlequicksearchbox.SearchActivity;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.velvet.tg.FirstRunActivity;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public class GoogleAccountSettingsController
  extends SettingsControllerBase
  implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  private final DataSetObserver mAccountObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      GoogleAccountSettingsController.this.updateUi();
    }
  };
  private final Activity mActivity;
  private final SearchHistoryHelper mCloudHistoryHelper;
  protected SwitchPreference mCloudSearchHistoryPreference;
  @Nullable
  private Boolean mCloudWebHistorySetting;
  private final GsaConfigFlags mFlags;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final IntentUtils mIntentUtils;
  private final LocationSettings mLocationSettings;
  private final LoginHelper mLoginHelper;
  protected Preference mManageLocationHistoryPreference;
  private ManageSearchHistoryHelper mManageSearchHistoryHelper;
  protected Preference mManageSearchHistoryPreference;
  private boolean mMapsDisabled;
  private final NetworkClient mNetworkClient;
  private final SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
  {
    public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
    {
      if (paramAnonymousString == "webview_logged_in_account") {
        GoogleAccountSettingsController.this.updateUi();
      }
    }
  };
  protected PreferenceGroup mScreen;
  private final DataSetObservable mSearchHistoryChangedObservable;
  protected SelectAccountPreference mSelectAccountPreference;
  private final SearchSettings mSettings;
  private final Executor mUiExecutor;
  private final SearchUrlHelper mUrlHelper;
  private AtomicInteger mWebHistorySettingRefreshCount = new AtomicInteger(0);
  
  public GoogleAccountSettingsController(SearchSettings paramSearchSettings, LoginHelper paramLoginHelper, GsaConfigFlags paramGsaConfigFlags, Activity paramActivity, SearchUrlHelper paramSearchUrlHelper, DataSetObservable paramDataSetObservable, NetworkClient paramNetworkClient, IntentUtils paramIntentUtils, SearchHistoryHelper paramSearchHistoryHelper, LocationSettings paramLocationSettings, GmsLocationReportingHelper paramGmsLocationReportingHelper, Executor paramExecutor)
  {
    super(paramSearchSettings);
    this.mSettings = paramSearchSettings;
    this.mSettings.registerOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
    this.mLoginHelper = paramLoginHelper;
    this.mActivity = paramActivity;
    this.mFlags = paramGsaConfigFlags;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mSearchHistoryChangedObservable = paramDataSetObservable;
    this.mNetworkClient = paramNetworkClient;
    this.mIntentUtils = paramIntentUtils;
    this.mCloudHistoryHelper = paramSearchHistoryHelper;
    this.mLocationSettings = paramLocationSettings;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
    this.mUiExecutor = paramExecutor;
  }
  
  private LoginHelper getLoginHelper()
  {
    return this.mLoginHelper;
  }
  
  private String getManageSearchHistorySummaryText(String paramString)
  {
    if (this.mFlags.isSearchHistoryInAppEnabled()) {
      return getResources().getString(2131363165);
    }
    Resources localResources = getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(paramString);
    return localResources.getString(2131363164, arrayOfObject);
  }
  
  private boolean hasAccounts()
  {
    return (this.mLoginHelper != null) && (this.mLoginHelper.getAllAccounts() != null);
  }
  
  private boolean haveSelectedAccount()
  {
    return (this.mLoginHelper != null) && (this.mLoginHelper.getAccount() != null);
  }
  
  private void manageSearchHistory()
  {
    this.mManageSearchHistoryHelper = new ManageSearchHistoryHelper(this.mActivity, this.mFlags, this.mLoginHelper, this.mUrlHelper);
    this.mManageSearchHistoryHelper.start();
  }
  
  private void setCloudSearchHistoryPreference(Preference paramPreference)
  {
    this.mCloudSearchHistoryPreference = ((SwitchPreference)paramPreference);
    this.mCloudSearchHistoryPreference.setOnPreferenceChangeListener(this);
  }
  
  private void setManageSearchHistoryPreference(Preference paramPreference)
  {
    this.mManageSearchHistoryPreference = paramPreference;
    this.mManageSearchHistoryPreference.setOnPreferenceClickListener(this);
  }
  
  private void setPreferenceVisible(Preference paramPreference, boolean paramBoolean)
  {
    if (!paramBoolean) {
      this.mScreen.removePreference(paramPreference);
    }
    while (this.mScreen.findPreference(paramPreference.getKey()) != null) {
      return;
    }
    this.mScreen.addPreference(paramPreference);
  }
  
  private void setSelectAccountPreference(Preference paramPreference)
  {
    this.mSelectAccountPreference = ((SelectAccountPreference)paramPreference);
    this.mSelectAccountPreference.setNetworkClient(this.mNetworkClient);
    this.mSelectAccountPreference.setOnPreferenceChangeListener(this);
    updateAccounts();
  }
  
  private void updateAccounts()
  {
    Object localObject = this.mLoginHelper.getAllAccountNames();
    String str = this.mLoginHelper.getAccountName();
    if (str != null)
    {
      String[] arrayOfString = new String[1 + localObject.length];
      System.arraycopy(localObject, 0, arrayOfString, 0, localObject.length);
      arrayOfString[(-1 + arrayOfString.length)] = getResources().getString(2131363168);
      localObject = arrayOfString;
    }
    this.mSelectAccountPreference.setEntries((CharSequence[])localObject);
    this.mSelectAccountPreference.setEntryValues((CharSequence[])localObject);
    if (localObject.length > 0)
    {
      this.mSelectAccountPreference.setEnabled(true);
      if (str != null) {
        this.mSelectAccountPreference.setValue(str);
      }
    }
    for (;;)
    {
      updateUi();
      return;
      this.mSelectAccountPreference.setEnabled(false);
    }
  }
  
  private void updateUi()
  {
    if (!hasAccounts())
    {
      this.mSelectAccountPreference.setSummary(2131363167);
      this.mManageSearchHistoryPreference.setTitle(2131363162);
      this.mManageSearchHistoryPreference.setSummary(2131363163);
      this.mManageSearchHistoryPreference.setEnabled(false);
      this.mManageLocationHistoryPreference.setTitle(2131363159);
      this.mManageLocationHistoryPreference.setSummary(2131363163);
      this.mManageLocationHistoryPreference.setEnabled(false);
    }
    do
    {
      do
      {
        return;
        if (!haveSelectedAccount()) {
          break;
        }
        String str = getLoginHelper().getAccountName();
        this.mSelectAccountPreference.setSummary(getResources().getString(2131363169, new Object[] { str }));
        this.mManageSearchHistoryPreference.setTitle(2131363162);
        this.mManageSearchHistoryPreference.setSummary(getManageSearchHistorySummaryText(str));
        this.mManageSearchHistoryPreference.setEnabled(true);
        this.mManageLocationHistoryPreference.setTitle(2131363159);
        this.mManageLocationHistoryPreference.setSummary(getResources().getString(2131363160, new Object[] { str }));
      } while (this.mMapsDisabled);
      this.mManageLocationHistoryPreference.setEnabled(true);
      return;
      this.mSelectAccountPreference.setSummary(getResources().getString(2131363171));
    } while (this.mScreen == null);
    this.mScreen.removePreference(this.mManageLocationHistoryPreference);
    this.mScreen.removePreference(this.mCloudSearchHistoryPreference);
    this.mScreen.removePreference(this.mManageSearchHistoryPreference);
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !isPsuggestAvailable();
  }
  
  public Resources getResources()
  {
    return this.mActivity.getResources();
  }
  
  public void handlePreference(Preference paramPreference)
  {
    String str = paramPreference.getKey();
    if ("cloud_search_history".equals(str)) {
      setCloudSearchHistoryPreference(paramPreference);
    }
    do
    {
      return;
      if ("manage_search_history".equals(str))
      {
        setManageSearchHistoryPreference(paramPreference);
        return;
      }
      if ("manage_location_history".equals(str))
      {
        setManageLocationHistoryPreference(paramPreference);
        return;
      }
    } while (!"google_account".equals(str));
    setSelectAccountPreference(paramPreference);
  }
  
  protected boolean isPsuggestAvailable()
  {
    return true;
  }
  
  public void onCreateComplete(Bundle paramBundle)
  {
    super.onCreateComplete(paramBundle);
    if ((isPsuggestAvailable()) && (this.mLoginHelper != null)) {
      this.mLoginHelper.registerDataSetObserver(this.mAccountObserver);
    }
  }
  
  public void onDestroy()
  {
    if ((isPsuggestAvailable()) && (this.mLoginHelper != null)) {
      this.mLoginHelper.unregisterDataSetObserver(this.mAccountObserver);
    }
    this.mSettings.unregisterOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
    super.onDestroy();
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    String str;
    Intent localIntent;
    if (paramPreference == this.mSelectAccountPreference)
    {
      this.mSearchHistoryChangedObservable.notifyChanged();
      str = (String)paramObject;
      if (str.equals(getResources().getString(2131363168)))
      {
        this.mSelectAccountPreference.setSummary(getResources().getString(2131363170));
        localIntent = new Intent(this.mActivity, SearchActivity.class);
        localIntent.setFlags(67108864);
        this.mLoginHelper.logOut();
        this.mActivity.startActivity(localIntent);
        this.mActivity.finish();
      }
    }
    final SwitchPreference localSwitchPreference;
    do
    {
      do
      {
        for (;;)
        {
          return true;
          if (!str.equals(this.mLoginHelper.getAccountName())) {
            try
            {
              this.mLoginHelper.setAccountToUseByName(str);
              localIntent = new Intent(this.mActivity, FirstRunActivity.class);
              localIntent.putExtra("account_name", str);
              localIntent.putExtra("skip_to_end", true);
            }
            catch (AccountsException localAccountsException)
            {
              return false;
            }
          }
        }
      } while (paramPreference != this.mCloudSearchHistoryPreference);
      localSwitchPreference = (SwitchPreference)paramPreference;
      if (this.mCloudWebHistorySetting == null) {
        return false;
      }
    } while (this.mCloudWebHistorySetting.equals(paramObject));
    final boolean bool = ((Boolean)paramObject).booleanValue();
    showToast(2131363155);
    localSwitchPreference.setEnabled(false);
    final int i = this.mWebHistorySettingRefreshCount.get();
    this.mCloudHistoryHelper.setHistoryEnabledAsync(this.mLoginHelper.getAccount(), bool, new Consumer()
    {
      public boolean consume(Boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean.booleanValue())
        {
          GoogleAccountSettingsController.access$102(GoogleAccountSettingsController.this, Boolean.valueOf(bool));
          GoogleAccountSettingsController.this.showToast(2131363156);
          localSwitchPreference.setEnabled(true);
        }
        do
        {
          return true;
          GoogleAccountSettingsController.this.showToast(2131363158);
        } while (i != GoogleAccountSettingsController.this.mWebHistorySettingRefreshCount.get());
        localSwitchPreference.setChecked(GoogleAccountSettingsController.this.mCloudWebHistorySetting.booleanValue());
        localSwitchPreference.setEnabled(true);
        return true;
      }
    });
    return true;
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if (paramPreference == this.mManageSearchHistoryPreference) {
      manageSearchHistory();
    }
    return false;
  }
  
  public void onResume()
  {
    super.onResume();
    updateSearchHistorySettingVisibility();
    updateLocationHistorySettingVisibility();
    if (isPsuggestAvailable()) {
      updateAccounts();
    }
    refreshCloudHistoryPreference(this.mLoginHelper.getAccount());
  }
  
  public void onStop()
  {
    if (this.mManageSearchHistoryHelper != null)
    {
      this.mManageSearchHistoryHelper.cancel();
      this.mManageSearchHistoryHelper = null;
    }
    super.onStop();
  }
  
  void refreshCloudHistoryPreference(final Account paramAccount)
  {
    this.mCloudWebHistorySetting = null;
    if (paramAccount == null)
    {
      this.mCloudSearchHistoryPreference.setEnabled(false);
      return;
    }
    this.mCloudSearchHistoryPreference.setEnabled(false);
    this.mCloudSearchHistoryPreference.setSummary(2131363152);
    this.mWebHistorySettingRefreshCount.incrementAndGet();
    this.mCloudHistoryHelper.getHistoryEnabled(paramAccount, new Consumer()
    {
      public boolean consume(Boolean paramAnonymousBoolean)
      {
        GoogleAccountSettingsController.access$102(GoogleAccountSettingsController.this, paramAnonymousBoolean);
        if (paramAccount.name.equals(GoogleAccountSettingsController.this.mSelectAccountPreference.getValue()))
        {
          if (paramAnonymousBoolean == null)
          {
            GoogleAccountSettingsController.this.mCloudSearchHistoryPreference.setEnabled(false);
            GoogleAccountSettingsController.this.showToast(2131363153);
          }
        }
        else {
          return true;
        }
        GoogleAccountSettingsController.this.mCloudSearchHistoryPreference.setEnabled(true);
        GoogleAccountSettingsController.this.mCloudSearchHistoryPreference.setChecked(paramAnonymousBoolean.booleanValue());
        SwitchPreference localSwitchPreference = GoogleAccountSettingsController.this.mCloudSearchHistoryPreference;
        Resources localResources = GoogleAccountSettingsController.this.getResources();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramAccount.name;
        localSwitchPreference.setSummary(localResources.getString(2131363154, arrayOfObject));
        return true;
      }
    });
  }
  
  void setManageLocationHistoryPreference(Preference paramPreference)
  {
    this.mManageLocationHistoryPreference = paramPreference;
    if (!this.mIntentUtils.isIntentHandled(this.mActivity, this.mManageLocationHistoryPreference.getIntent())) {}
    for (boolean bool = true;; bool = false)
    {
      this.mMapsDisabled = bool;
      if (!this.mMapsDisabled) {
        break;
      }
      this.mManageLocationHistoryPreference.setEnabled(false);
      return;
    }
    this.mManageLocationHistoryPreference.setOnPreferenceClickListener(this);
  }
  
  public void setScreen(PreferenceScreen paramPreferenceScreen)
  {
    super.setScreen(paramPreferenceScreen);
    if (this.mScreen != paramPreferenceScreen) {
      this.mScreen = paramPreferenceScreen;
    }
  }
  
  protected void showToast(int paramInt)
  {
    Toast.makeText(this.mActivity, paramInt, 0).show();
  }
  
  void updateLocationHistorySettingVisibility()
  {
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null)
    {
      Preference localPreference = this.mManageLocationHistoryPreference;
      if (!this.mLocationSettings.isGmsCoreLocationSettingAvailable()) {}
      for (boolean bool = true;; bool = false)
      {
        setPreferenceVisible(localPreference, bool);
        return;
      }
    }
    this.mGmsLocationReportingHelper.getReportingState(localAccount).addConsumer(new Consumer()
    {
      public boolean consume(@Nullable ReportingState paramAnonymousReportingState)
      {
        if ((paramAnonymousReportingState != null) && (paramAnonymousReportingState.isDeferringToMaps())) {}
        for (int i = 1;; i = 0)
        {
          boolean bool1;
          if (i == 0)
          {
            boolean bool2 = GoogleAccountSettingsController.this.mLocationSettings.isGmsCoreLocationSettingAvailable();
            bool1 = false;
            if (bool2) {}
          }
          else
          {
            bool1 = true;
          }
          GoogleAccountSettingsController.this.setPreferenceVisible(GoogleAccountSettingsController.this.mManageLocationHistoryPreference, bool1);
          return true;
        }
      }
    }, this.mUiExecutor);
  }
  
  void updateSearchHistorySettingVisibility()
  {
    if (!TextUtils.isEmpty(this.mSelectAccountPreference.getValue())) {}
    for (boolean bool = true;; bool = false)
    {
      setPreferenceVisible(this.mManageSearchHistoryPreference, bool);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.GoogleAccountSettingsController
 * JD-Core Version:    0.7.0.1
 */