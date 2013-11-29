package com.google.android.search.core;

import android.accounts.Account;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.search.core.util.GelStartupPrefsWriter;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.DasherConfiguration;
import com.google.geo.sidekick.Sidekick.FetchConfigurationQuery;
import com.google.geo.sidekick.Sidekick.FetchConfigurationResponse;
import com.google.geo.sidekick.Sidekick.LocaleConfiguration;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class NowOptInSettingsImpl
  implements NowOptInSettings
{
  private static final String TAG = Tag.getTag(NowOptInSettingsImpl.class);
  private final Executor mBgExecutor;
  private final PredictiveCardsPreferences mCardsPrefs;
  private final Clock mClock;
  private final Context mContext;
  private final Object mFetchConfigLock = new Object();
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final LoginHelper mLoginHelper;
  private final Object mOptInPrefLock = new Object();
  private final Set<Account> mPendingConfigFetches = Sets.newHashSet();
  private final GsaPreferenceController mPrefController;
  private final SearchConfig mSearchConfig;
  private final VelvetServices mVelvetServices;
  
  public NowOptInSettingsImpl(VelvetServices paramVelvetServices, Context paramContext, GsaPreferenceController paramGsaPreferenceController, Clock paramClock, Executor paramExecutor, LoginHelper paramLoginHelper, SearchConfig paramSearchConfig, PredictiveCardsPreferences paramPredictiveCardsPreferences, GmsLocationReportingHelper paramGmsLocationReportingHelper)
  {
    this.mVelvetServices = paramVelvetServices;
    this.mContext = paramContext;
    this.mPrefController = paramGsaPreferenceController;
    this.mClock = paramClock;
    this.mBgExecutor = paramExecutor;
    this.mLoginHelper = paramLoginHelper;
    this.mSearchConfig = paramSearchConfig;
    this.mCardsPrefs = paramPredictiveCardsPreferences;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
  }
  
  private void doFetchConfig(Account paramAccount)
  {
    GmsClientWrapper.GmsFuture localGmsFuture = this.mGmsLocationReportingHelper.getReportingState(paramAccount);
    Sidekick.ResponsePayload localResponsePayload = this.mVelvetServices.getSidekickInjector().getNetworkClient().sendRequestWithoutLocation(new Sidekick.RequestPayload().setFetchConfigurationQuery(new Sidekick.FetchConfigurationQuery()));
    if ((localResponsePayload != null) && (localResponsePayload.hasFetchConfigurationResponse()) && (localResponsePayload.getFetchConfigurationResponse().hasConfiguration()))
    {
      localConfiguration = localResponsePayload.getFetchConfigurationResponse().getConfiguration();
      localReportingState = (ReportingState)localGmsFuture.safeGet();
      saveConfiguration(localConfiguration, paramAccount, localReportingState);
      getStartupPrefs().edit().putInt("any_account_can_run_the_google", 1).apply();
      setAccountCanRunNow(userCanRunNow(localConfiguration, paramAccount, localReportingState), paramAccount);
    }
    while (getStartupPrefs().contains(getUserCanRunNowPrefKey(paramAccount)))
    {
      Sidekick.Configuration localConfiguration;
      ReportingState localReportingState;
      return;
    }
    Log.w(TAG, "Failed to fetch default configuration");
    setAccountCanRunNow(3, paramAccount);
  }
  
  private String getAccountOptedInKey(String paramString)
  {
    return "opted_in_version_" + paramString;
  }
  
  private String getConfigurationTimestampKey(Account paramAccount)
  {
    return "last_configuration_saved_time_" + paramAccount.name;
  }
  
  @Nullable
  private Account getLoggedInAccount()
  {
    return this.mLoginHelper.getAccount();
  }
  
  private int getOptInPageVersion()
  {
    return this.mContext.getResources().getInteger(2131427459);
  }
  
  private SharedPreferencesExt getStartupPrefs()
  {
    return this.mPrefController.getStartupPreferences();
  }
  
  private String getUserCanRunNowPrefKey(Account paramAccount)
  {
    return "user_can_run_the_google_" + paramAccount.name;
  }
  
  private boolean isAccountWhitelisted(Account paramAccount)
  {
    ImmutableList localImmutableList = ImmutableList.copyOf(this.mSearchConfig.getDomainWhitelist());
    String str = paramAccount.name;
    int i = str.indexOf('@');
    return (i >= 0) && (localImmutableList.contains(str.substring(i + 1).toLowerCase(Locale.US)));
  }
  
  public static boolean isStartupSetting(String paramString)
  {
    return (paramString.equals("first_run_screens")) || (paramString.equals("any_account_can_run_the_google")) || (paramString.startsWith("opted_in_version_")) || (paramString.startsWith("user_can_run_the_google_")) || (paramString.startsWith("last_configuration_saved_time_"));
  }
  
  private boolean isUserWhitelisted()
  {
    Account localAccount = getLoggedInAccount();
    if (localAccount == null) {
      return false;
    }
    return isAccountWhitelisted(localAccount);
  }
  
  private void setAccountCanRunNow(int paramInt, Account paramAccount)
  {
    getStartupPrefs().edit().putInt(getUserCanRunNowPrefKey(paramAccount), paramInt).apply();
  }
  
  private void setGetGoogleNowButtonDismissed(boolean paramBoolean)
  {
    getStartupPrefs().edit().putBoolean("GSAPrefs.now_promo_dismissed", paramBoolean).apply();
    this.mPrefController.getGelStartupPrefs().commit("GSAPrefs.now_promo_dismissed", paramBoolean);
  }
  
  private void timeStampConfigurationSave(Account paramAccount)
  {
    SharedPreferencesExt.Editor localEditor = getStartupPrefs().edit();
    localEditor.putLong(getConfigurationTimestampKey(paramAccount), this.mClock.currentTimeMillis());
    localEditor.apply();
  }
  
  private void updateGelOptInPrefs(@Nullable Account paramAccount, boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (this.mOptInPrefLock)
    {
      if (Objects.equal(paramAccount, getLoggedInAccount()))
      {
        this.mPrefController.getGelStartupPrefs().commit("GEL.GSAPrefs.now_enabled", paramBoolean1);
        this.mPrefController.getGelStartupPrefs().commit("GEL.GSAPrefs.can_optin_to_now", paramBoolean2);
      }
      return;
    }
  }
  
  public int canAccountRunNow(final Account paramAccount)
  {
    if (paramAccount == null)
    {
      getStartupPrefs().edit().putInt("any_account_can_run_the_google", 3).apply();
      i = 3;
      return i;
    }
    int i = getStartupPrefs().getInt(getUserCanRunNowPrefKey(paramAccount), 0);
    if (isUserWhitelisted()) {}
    for (int j = this.mSearchConfig.getInt(2131427386);; j = this.mSearchConfig.getInt(2131427385))
    {
      long l = getSavedConfigurationTimestamp(paramAccount) + 1000L * j;
      boolean bool = this.mClock.currentTimeMillis() < l;
      int k = 0;
      if (bool) {
        k = 1;
      }
      if ((i != 0) && (i != 3) && (k == 0)) {
        break;
      }
      synchronized (this.mFetchConfigLock)
      {
        if (!this.mPendingConfigFetches.contains(paramAccount)) {
          this.mBgExecutor.execute(new Runnable()
          {
            public void run()
            {
              NowOptInSettingsImpl.this.updateWhetherAccountCanRunNow(paramAccount);
            }
          });
        }
        return i;
      }
    }
  }
  
  public int canLoggedInAccountRunNow()
  {
    return canAccountRunNow(getLoggedInAccount());
  }
  
  public void cleanUpAccountData()
  {
    this.mCardsPrefs.clearWorkingConfiguration();
    SidekickInjector localSidekickInjector = this.mVelvetServices.getSidekickInjector();
    localSidekickInjector.getEntryProvider().invalidate();
    localSidekickInjector.getCalendarDataProvider().clearData();
    localSidekickInjector.getExecutedUserActionStore().postDeleteStore();
    localSidekickInjector.getNowNotificationManager().cancelAll();
    localSidekickInjector.getTrainingQuestionManager().clearData();
  }
  
  public void disableForAccount(Account paramAccount)
  {
    this.mVelvetServices.stopNowServices();
    optAccountOut(paramAccount);
    cleanUpAccountData();
  }
  
  public boolean domainIsBlockedFromNow(Sidekick.Configuration paramConfiguration, Account paramAccount)
  {
    return (!isAccountWhitelisted(paramAccount)) && (paramConfiguration.hasDasherConfiguration()) && (!paramConfiguration.getDasherConfiguration().getPredictiveUiEnabled());
  }
  
  public Sidekick.FetchConfigurationResponse fetchAccountConfiguration(Account paramAccount)
  {
    ExtraPreconditions.checkNotMainThread();
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setFetchConfigurationQuery(new Sidekick.FetchConfigurationQuery());
    Sidekick.ResponsePayload localResponsePayload = this.mVelvetServices.getSidekickInjector().getNetworkClient().sendRequestWithoutLocationWithAccount(localRequestPayload, paramAccount);
    if ((localResponsePayload != null) && (localResponsePayload.hasFetchConfigurationResponse())) {
      return localResponsePayload.getFetchConfigurationResponse();
    }
    return null;
  }
  
  @Nullable
  public Sidekick.Configuration getSavedConfiguration(Account paramAccount)
  {
    Sidekick.Configuration localConfiguration = this.mCardsPrefs.getMasterConfigurationFor(paramAccount);
    if (localConfiguration == null) {
      getStartupPrefs().edit().remove(getConfigurationTimestampKey(paramAccount)).apply();
    }
    return localConfiguration;
  }
  
  public long getSavedConfigurationTimestamp(Account paramAccount)
  {
    return getStartupPrefs().getLong(getConfigurationTimestampKey(paramAccount), 0L);
  }
  
  public boolean isAccountOptedIn(@Nullable Account paramAccount)
  {
    if (paramAccount == null) {}
    String str;
    do
    {
      return false;
      str = getAccountOptedInKey(paramAccount.name);
    } while (getStartupPrefs().getInt(str, -1) < getOptInPageVersion());
    return true;
  }
  
  public boolean isPreferenceKeyForAnOptInChange(String paramString)
  {
    return (paramString != null) && ((paramString.equals("any_account_can_run_the_google")) || (paramString.startsWith("opted_in_version_")));
  }
  
  public boolean isUserOptedIn()
  {
    return isAccountOptedIn(getLoggedInAccount());
  }
  
  public boolean localeIsBlockedFromNow(Sidekick.Configuration paramConfiguration)
  {
    return (paramConfiguration.hasLocaleConfiguration()) && (!paramConfiguration.getLocaleConfiguration().getEnabled());
  }
  
  public void onAccountChanged(@Nullable Account paramAccount)
  {
    int i = 1;
    boolean bool = isAccountOptedIn(paramAccount);
    if (canAccountRunNow(paramAccount) == i) {}
    for (;;)
    {
      updateGelOptInPrefs(paramAccount, bool, i);
      return;
      int j = 0;
    }
  }
  
  public void optAccountIn(Account paramAccount)
  {
    Object localObject1 = this.mOptInPrefLock;
    if (paramAccount != null) {}
    try
    {
      String str = getAccountOptedInKey(paramAccount.name);
      getStartupPrefs().edit().putInt(str, getOptInPageVersion()).apply();
      updateGelOptInPrefs(paramAccount, true, true);
      setGetGoogleNowButtonDismissed(false);
      return;
    }
    finally {}
  }
  
  public void optAccountOut(Account paramAccount)
  {
    int j;
    for (int i = 1;; j = 0) {
      synchronized (this.mOptInPrefLock)
      {
        if (isAccountOptedIn(paramAccount))
        {
          String str = getAccountOptedInKey(paramAccount.name);
          getStartupPrefs().edit().remove(str).apply();
          if (canAccountRunNow(paramAccount) == i) {
            updateGelOptInPrefs(paramAccount, false, i);
          }
        }
        else
        {
          return;
        }
      }
    }
  }
  
  public void saveConfiguration(Sidekick.Configuration paramConfiguration, Account paramAccount, @Nullable ReportingState paramReportingState)
  {
    int i = 1;
    this.mCardsPrefs.setMasterConfigurationFor(paramAccount, paramConfiguration, false);
    int j = userCanRunNow(paramConfiguration, paramAccount, paramReportingState);
    getStartupPrefs().edit().putInt(getUserCanRunNowPrefKey(paramAccount), j).apply();
    timeStampConfigurationSave(paramAccount);
    boolean bool = isAccountOptedIn(paramAccount);
    if (j == i) {}
    for (;;)
    {
      updateGelOptInPrefs(paramAccount, bool, i);
      return;
      i = 0;
    }
  }
  
  public void setFirstRunScreensShown()
  {
    int i = this.mContext.getResources().getInteger(2131427460);
    getStartupPrefs().edit().putInt("first_run_screens", i).apply();
    this.mPrefController.getGelStartupPrefs().commit("GSAPrefs.first_run_screens_shown", true);
  }
  
  public void setGetGoogleNowButtonDismissed()
  {
    setGetGoogleNowButtonDismissed(true);
  }
  
  public boolean stopServicesIfUserOptedOut()
  {
    if (isUserOptedIn()) {
      return false;
    }
    this.mVelvetServices.getAsyncServices().getUiThreadExecutor().execute(new Runnable()
    {
      public void run()
      {
        NowOptInSettingsImpl.this.mVelvetServices.stopNowServices();
      }
    });
    return true;
  }
  
  public boolean updateConfigurationForAccount(Account paramAccount, Sidekick.Configuration paramConfiguration, @Nullable ReportingState paramReportingState)
  {
    this.mCardsPrefs.setMasterConfigurationFor(paramAccount, paramConfiguration, true);
    timeStampConfigurationSave(paramAccount);
    return userCanRunNow(paramConfiguration, paramAccount, paramReportingState) == 1;
  }
  
  public void updateSidekickConfigurationForCurrentAccount(Sidekick.SidekickConfiguration paramSidekickConfiguration)
  {
    Account localAccount = getLoggedInAccount();
    if (localAccount == null) {
      return;
    }
    Sidekick.Configuration localConfiguration = getSavedConfiguration(localAccount);
    localConfiguration.setSidekickConfiguration(paramSidekickConfiguration);
    updateConfigurationForAccount(localAccount, localConfiguration, null);
    saveConfiguration(localConfiguration, localAccount, null);
  }
  
  /* Error */
  public void updateWhetherAccountCanRunNow(Account paramAccount)
  {
    // Byte code:
    //   0: invokestatic 454	com/google/android/shared/util/ExtraPreconditions:checkNotMainThread	()V
    //   3: aload_0
    //   4: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   7: astore_2
    //   8: aload_2
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   14: aload_1
    //   15: invokeinterface 358 2 0
    //   20: istore 4
    //   22: iconst_0
    //   23: istore 5
    //   25: iload 4
    //   27: ifne +17 -> 44
    //   30: aload_0
    //   31: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   34: aload_1
    //   35: invokeinterface 549 2 0
    //   40: pop
    //   41: iconst_1
    //   42: istore 5
    //   44: aload_2
    //   45: monitorexit
    //   46: iload 5
    //   48: ifeq +95 -> 143
    //   51: aload_0
    //   52: aload_1
    //   53: invokespecial 551	com/google/android/search/core/NowOptInSettingsImpl:doFetchConfig	(Landroid/accounts/Account;)V
    //   56: aload_0
    //   57: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   60: astore 24
    //   62: aload 24
    //   64: monitorenter
    //   65: aload_0
    //   66: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   69: aload_1
    //   70: invokeinterface 553 2 0
    //   75: pop
    //   76: aload_0
    //   77: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   80: invokevirtual 556	java/lang/Object:notifyAll	()V
    //   83: aload 24
    //   85: monitorexit
    //   86: return
    //   87: astore_3
    //   88: aload_2
    //   89: monitorexit
    //   90: aload_3
    //   91: athrow
    //   92: astore 25
    //   94: aload 24
    //   96: monitorexit
    //   97: aload 25
    //   99: athrow
    //   100: astore 20
    //   102: aload_0
    //   103: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   106: astore 21
    //   108: aload 21
    //   110: monitorenter
    //   111: aload_0
    //   112: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   115: aload_1
    //   116: invokeinterface 553 2 0
    //   121: pop
    //   122: aload_0
    //   123: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   126: invokevirtual 556	java/lang/Object:notifyAll	()V
    //   129: aload 21
    //   131: monitorexit
    //   132: aload 20
    //   134: athrow
    //   135: astore 22
    //   137: aload 21
    //   139: monitorexit
    //   140: aload 22
    //   142: athrow
    //   143: aload_0
    //   144: getfield 66	com/google/android/search/core/NowOptInSettingsImpl:mClock	Lcom/google/android/shared/util/Clock;
    //   147: invokeinterface 322 1 0
    //   152: lstore 7
    //   154: lconst_0
    //   155: lstore 9
    //   157: aload_0
    //   158: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   161: astore 11
    //   163: aload 11
    //   165: monitorenter
    //   166: aload_0
    //   167: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   170: aload_1
    //   171: invokeinterface 358 2 0
    //   176: istore 13
    //   178: iload 13
    //   180: ifeq +69 -> 249
    //   183: lload 9
    //   185: ldc2_w 557
    //   188: lcmp
    //   189: ifge +60 -> 249
    //   192: aload_0
    //   193: getfield 50	com/google/android/search/core/NowOptInSettingsImpl:mFetchConfigLock	Ljava/lang/Object;
    //   196: ldc2_w 557
    //   199: lload 9
    //   201: lsub
    //   202: invokevirtual 562	java/lang/Object:wait	(J)V
    //   205: aload_0
    //   206: getfield 66	com/google/android/search/core/NowOptInSettingsImpl:mClock	Lcom/google/android/shared/util/Clock;
    //   209: invokeinterface 322 1 0
    //   214: lstore 18
    //   216: lload 18
    //   218: lload 7
    //   220: lsub
    //   221: lstore 9
    //   223: goto -57 -> 166
    //   226: astore 16
    //   228: getstatic 42	com/google/android/search/core/NowOptInSettingsImpl:TAG	Ljava/lang/String;
    //   231: ldc_w 564
    //   234: invokestatic 567	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   237: pop
    //   238: goto -72 -> 166
    //   241: astore 12
    //   243: aload 11
    //   245: monitorexit
    //   246: aload 12
    //   248: athrow
    //   249: aload_0
    //   250: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   253: aload_1
    //   254: invokeinterface 358 2 0
    //   259: ifeq +44 -> 303
    //   262: getstatic 42	com/google/android/search/core/NowOptInSettingsImpl:TAG	Ljava/lang/String;
    //   265: new 194	java/lang/StringBuilder
    //   268: dup
    //   269: invokespecial 195	java/lang/StringBuilder:<init>	()V
    //   272: ldc_w 569
    //   275: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: aload_1
    //   279: getfield 213	android/accounts/Account:name	Ljava/lang/String;
    //   282: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   288: invokestatic 567	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   291: pop
    //   292: aload_0
    //   293: getfield 58	com/google/android/search/core/NowOptInSettingsImpl:mPendingConfigFetches	Ljava/util/Set;
    //   296: aload_1
    //   297: invokeinterface 553 2 0
    //   302: pop
    //   303: aload 11
    //   305: monitorexit
    //   306: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	307	0	this	NowOptInSettingsImpl
    //   0	307	1	paramAccount	Account
    //   7	82	2	localObject1	Object
    //   87	4	3	localObject2	Object
    //   20	6	4	bool1	boolean
    //   23	24	5	i	int
    //   152	67	7	l1	long
    //   155	67	9	l2	long
    //   241	6	12	localObject4	Object
    //   176	3	13	bool2	boolean
    //   226	1	16	localInterruptedException	java.lang.InterruptedException
    //   214	3	18	l3	long
    //   100	33	20	localObject5	Object
    //   106	32	21	localObject6	Object
    //   135	6	22	localObject7	Object
    //   60	35	24	localObject8	Object
    //   92	6	25	localObject9	Object
    // Exception table:
    //   from	to	target	type
    //   10	22	87	finally
    //   30	41	87	finally
    //   44	46	87	finally
    //   88	90	87	finally
    //   65	86	92	finally
    //   94	97	92	finally
    //   51	56	100	finally
    //   111	132	135	finally
    //   137	140	135	finally
    //   192	216	226	java/lang/InterruptedException
    //   166	178	241	finally
    //   192	216	241	finally
    //   228	238	241	finally
    //   243	246	241	finally
    //   249	303	241	finally
    //   303	306	241	finally
  }
  
  public int userCanRunNow(Sidekick.Configuration paramConfiguration, Account paramAccount, @Nullable ReportingState paramReportingState)
  {
    int i = 2;
    if (paramConfiguration == null) {
      i = 3;
    }
    while ((domainIsBlockedFromNow(paramConfiguration, paramAccount)) || (localeIsBlockedFromNow(paramConfiguration)) || ((paramReportingState != null) && (!paramReportingState.isDeferringToMaps()) && (!paramReportingState.isAllowed()) && (!isAccountOptedIn(paramAccount)))) {
      return i;
    }
    return 1;
  }
  
  public boolean userHasDismissedGetGoogleNowButton()
  {
    return getStartupPrefs().getBoolean("GSAPrefs.now_promo_dismissed", false);
  }
  
  public boolean userHasSeenFirstRunScreens()
  {
    int i = this.mContext.getResources().getInteger(2131427460);
    return getStartupPrefs().getInt("first_run_screens", -1) >= i;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.NowOptInSettingsImpl
 * JD-Core Version:    0.7.0.1
 */