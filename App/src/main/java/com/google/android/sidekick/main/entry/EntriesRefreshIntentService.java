package com.google.android.sidekick.main.entry;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.ProductInfo;
import com.google.android.libraries.tvdetect.ProductType;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GmmPrecacher;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.UserInteractionLogger.UserInteractionTimer;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.VelvetNetworkClient;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.gcm.GcmManager;
import com.google.android.sidekick.main.gcm.GcmManager.RegistrationState;
import com.google.android.sidekick.main.gcm.GcmUtil;
import com.google.android.sidekick.main.gcm.PushMessageRepository;
import com.google.android.sidekick.main.gcm.PushMessageRepository.PendingPartialUpdate;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.NetworkClient.ResponseAndEventId;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.SidekickInteractionManager;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.main.tv.TvConfig;
import com.google.android.sidekick.main.tv.TvDetector;
import com.google.android.sidekick.main.tv.TvDetector.Factory;
import com.google.android.sidekick.main.tv.TvDetector.Observer;
import com.google.android.sidekick.main.tv.TvDetectorImpl.Factory;
import com.google.android.sidekick.shared.client.EntriesRefreshRequestType;
import com.google.android.sidekick.shared.training.TrainingRequestHelper;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.DetectedDevice;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTree.CallbackWithInterest;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.GenericCardEntry;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.MinimumDataVersion;
import com.google.geo.sidekick.Sidekick.PrecacheDirective;
import com.google.geo.sidekick.Sidekick.PushRegistration;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import com.google.geo.sidekick.Sidekick.StateChanges;
import com.google.geo.sidekick.Sidekick.TrainingModeDataQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class EntriesRefreshIntentService
  extends DelayedStopIntentService
{
  private static final String TAG = Tag.getTag(EntriesRefreshIntentService.class);
  private TvDetector mActiveTvDetector;
  private AsyncServices mAsyncServices;
  private SearchConfig mConfig;
  private CoreSearchServices mCoreServices;
  private EntriesRefreshScheduler mEntriesRefreshScheduler;
  private ExecutedUserActionStore mExecutedUserActionStore;
  private GmmPrecacher mGmmPrecacher;
  private SidekickInteractionManager mInteractionManager;
  private SidekickInjector mSidekickInjector;
  private TrainingQuestionManager mTrainingQuestionManager;
  private TvDetector.Factory mTvDetectorFactory;
  private VelvetServices mVelvetServices;
  
  public EntriesRefreshIntentService()
  {
    super(TAG);
    setIntentRedelivery(false);
  }
  
  private Sidekick.EntryQuery buildQuery(CalendarDataProvider paramCalendarDataProvider, String paramString, Collection<Sidekick.MinimumDataVersion> paramCollection, @Nullable Collection<Device> paramCollection1)
  {
    Sidekick.ClientUserData localClientUserData = new Sidekick.ClientUserData();
    paramCalendarDataProvider.addCalendarDataToClientUserData(localClientUserData);
    if (paramCollection1 != null)
    {
      Iterator localIterator2 = paramCollection1.iterator();
      while (localIterator2.hasNext())
      {
        Device localDevice = (Device)localIterator2.next();
        if (localDevice.productInfo.type == ProductType.TV)
        {
          Sidekick.DetectedDevice localDetectedDevice = new Sidekick.DetectedDevice().setDeviceType(1);
          if (localDevice.productInfo.userFriendlyName != null) {
            localDetectedDevice.setModelName(localDevice.productInfo.userFriendlyName);
          }
          localClientUserData.addDetectedDevice(localDetectedDevice);
        }
      }
    }
    localClientUserData.setLowUsageUser(this.mInteractionManager.isIdle());
    localClientUserData.setBackgroundPollingIntervalSeconds(60 * this.mEntriesRefreshScheduler.getBackgroundRefreshIntervalMinutes());
    localClientUserData.setSidekickConfigurationHashId(paramString);
    Iterator localIterator1 = paramCollection.iterator();
    while (localIterator1.hasNext()) {
      localClientUserData.addMinimumDataVersion((Sidekick.MinimumDataVersion)localIterator1.next());
    }
    return new Sidekick.EntryQuery().setClientUserData(localClientUserData);
  }
  
  private Iterable<Sidekick.Interest> getInterestsToSend(int paramInt, ImmutableList<PushMessageRepository.PendingPartialUpdate> paramImmutableList)
  {
    int i = 1;
    ArrayList localArrayList = Lists.newArrayList();
    if (paramInt == 5)
    {
      Iterator localIterator = paramImmutableList.iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(((PushMessageRepository.PendingPartialUpdate)localIterator.next()).getInterest());
      }
    }
    if (EntriesRefreshRequestType.isMore(paramInt))
    {
      Sidekick.Interest localInterest = new Sidekick.Interest();
      localInterest.setConstraintLevel(i);
      if (!EntriesRefreshRequestType.isIncremental(paramInt)) {}
      for (;;)
      {
        localInterest.setIncludeStricterConstraintResponses(i);
        localArrayList.add(localInterest);
        return localArrayList;
        int j = 0;
      }
    }
    localArrayList.add(new Sidekick.Interest());
    return localArrayList;
  }
  
  private boolean hasPartialFailure(Sidekick.EntryResponse paramEntryResponse)
  {
    Iterator localIterator = paramEntryResponse.getEntryTreeList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.EntryTree localEntryTree = (Sidekick.EntryTree)localIterator.next();
      if ((localEntryTree != null) && (localEntryTree.hasError()) && (localEntryTree.getError() == 12)) {
        return true;
      }
    }
    return false;
  }
  
  private void initializeDependencies()
  {
    if (this.mVelvetServices == null)
    {
      this.mVelvetServices = VelvetServices.get();
      this.mCoreServices = this.mVelvetServices.getCoreServices();
      this.mConfig = this.mCoreServices.getConfig();
      this.mSidekickInjector = this.mVelvetServices.getSidekickInjector();
      this.mGmmPrecacher = new GmmPrecacher();
      this.mTvDetectorFactory = new TvDetectorImpl.Factory(getApplicationContext());
      this.mEntriesRefreshScheduler = this.mSidekickInjector.getEntriesRefreshScheduler();
      this.mInteractionManager = this.mSidekickInjector.getInteractionManager();
      this.mAsyncServices = this.mVelvetServices.getAsyncServices();
      this.mTrainingQuestionManager = this.mSidekickInjector.getTrainingQuestionManager();
      this.mExecutedUserActionStore = this.mSidekickInjector.getExecutedUserActionStore();
    }
  }
  
  private boolean isResponseErrorMarker(Sidekick.ResponsePayload paramResponsePayload)
  {
    return paramResponsePayload == VelvetNetworkClient.AUTHENTICATION_FAILURE_RESPONSE;
  }
  
  private Sidekick.EntryTreeNode maybeGetDisabledLocationCard(boolean paramBoolean)
  {
    if (paramBoolean) {}
    Sidekick.GenericCardEntry localGenericCardEntry;
    do
    {
      return null;
      localGenericCardEntry = this.mSidekickInjector.getLocationDisabledCardHelper().createDisabledLocationCard();
    } while (localGenericCardEntry == null);
    Sidekick.Entry localEntry = new Sidekick.Entry();
    localEntry.setGenericCardEntry(localGenericCardEntry);
    Sidekick.EntryTreeNode localEntryTreeNode = new Sidekick.EntryTreeNode();
    localEntryTreeNode.addEntry(localEntry);
    return localEntryTreeNode;
  }
  
  private int refreshEntries(int paramInt, ImmutableList<PushMessageRepository.PendingPartialUpdate> paramImmutableList, DelayedStopIntentService.StopLatch paramStopLatch, boolean paramBoolean1, boolean paramBoolean2)
  {
    ExtraPreconditions.checkNotMainThread();
    final EntryProvider localEntryProvider = this.mSidekickInjector.getEntryProvider();
    localEntryProvider.cancelDelayedRefresh();
    UserInteractionLogger localUserInteractionLogger = this.mCoreServices.getUserInteractionLogger();
    UserInteractionLogger.UserInteractionTimer localUserInteractionTimer1 = localUserInteractionLogger.createRandomSamplingTimer("REFRESH");
    PredictiveCardsPreferences localPredictiveCardsPreferences = this.mCoreServices.getPredictiveCardsPreferences();
    Sidekick.SidekickConfiguration localSidekickConfiguration = localPredictiveCardsPreferences.getWorkingConfiguration();
    Collection localCollection1 = null;
    int i = 0;
    String str2;
    if (localSidekickConfiguration != null)
    {
      NowConfigurationPreferences localNowConfigurationPreferences = localPredictiveCardsPreferences.getWorkingPreferences();
      boolean bool6 = TvConfig.isFeatureEnabled(getApplicationContext(), localNowConfigurationPreferences);
      localCollection1 = null;
      i = 0;
      if (bool6)
      {
        if (this.mActiveTvDetector != null) {
          this.mActiveTvDetector.stopDetection();
        }
        this.mActiveTvDetector = this.mTvDetectorFactory.newTvDetector();
        this.mActiveTvDetector.startDetection(new TvDetectorObserver(paramStopLatch));
        long l1 = this.mCoreServices.getClock().currentTimeMillis();
        UserInteractionLogger.UserInteractionTimer localUserInteractionTimer2 = localUserInteractionLogger.createRandomSamplingTimer("DETECT_TV");
        localCollection1 = this.mActiveTvDetector.getDetectedDevices(this.mConfig.getTvDetectionTimeoutMillis(), TimeUnit.MILLISECONDS, true);
        long l2 = this.mCoreServices.getClock().currentTimeMillis() - l1;
        if (l2 > 200L) {
          Log.i("NowTV", "Waited for " + l2 + "ms to find nearby TVs");
        }
        if (!localCollection1.isEmpty()) {
          break label359;
        }
        str2 = "NO_DEVICES";
        localUserInteractionTimer2.timingComplete(str2);
        i = this.mConfig.getServiceTimeoutForTvDetectionMillis();
      }
    }
    CalendarDataProvider localCalendarDataProvider = this.mSidekickInjector.getCalendarDataProvider();
    Location localLocation = this.mVelvetServices.getLocationOracle().blockingUpdateBestLocation();
    LoginHelper localLoginHelper = this.mCoreServices.getLoginHelper();
    final NowOptInSettings localNowOptInSettings = this.mCoreServices.getNowOptInSettings();
    final Account localAccount = localLoginHelper.getAccount();
    if (localAccount == null) {
      localEntryProvider.invalidate();
    }
    label359:
    label1402:
    for (;;)
    {
      return i;
      str2 = "HAS_DEVICES";
      break;
      if (localLocation == null) {}
      localEntryProvider.notifyAboutRefreshStarting(paramInt);
      String str1 = "";
      if (localPredictiveCardsPreferences.hasWorkingConfigurationFor(localAccount))
      {
        Sidekick.Configuration localConfiguration2 = localNowOptInSettings.getSavedConfiguration(localAccount);
        if ((localConfiguration2 != null) && (localConfiguration2.hasSidekickConfiguration())) {
          str1 = localConfiguration2.getSidekickConfiguration().getHashId();
        }
      }
      Collection localCollection2 = this.mSidekickInjector.getDataBackendVersionStore().getMinimumDataVersions();
      Sidekick.EntryQuery localEntryQuery = buildQuery(localCalendarDataProvider, str1, localCollection2, localCollection1);
      Iterator localIterator1 = getInterestsToSend(paramInt, paramImmutableList).iterator();
      while (localIterator1.hasNext()) {
        localEntryQuery.addInterest((Sidekick.Interest)localIterator1.next());
      }
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setEntryQuery(localEntryQuery);
      Iterable localIterable = this.mTrainingQuestionManager.getPendingAnsweredQuestionsWithEntries();
      if (localIterable.iterator().hasNext()) {
        localRequestPayload.setActionsQuery(TrainingRequestHelper.buildAnsweredQuestionsQuery(localIterable));
      }
      localRequestPayload.setTrainingModeDataQuery(new Sidekick.TrainingModeDataQuery().setMetadata(this.mTrainingQuestionManager.getTrainingModeMetadata()));
      if (paramBoolean1) {
        localRequestPayload.setSaveCallLog(true);
      }
      if (localPredictiveCardsPreferences.hasWorkingConfigurationFor(localAccount))
      {
        Sidekick.StateChanges localStateChanges = localPredictiveCardsPreferences.getSettingsChanges(localAccount);
        if (localStateChanges != null) {
          localRequestPayload.setStateChangeQuery(localStateChanges);
        }
      }
      if (this.mVelvetServices.getPreferenceController().getMainPreferences().getBoolean(getApplicationContext().getString(2131362157), false)) {
        localRequestPayload.setEnableExperimentalApiClients(true);
      }
      GcmManager.RegistrationState localRegistrationState = this.mSidekickInjector.getGCMManager().getRegistrationChangeFor(localAccount);
      if (localRegistrationState != null) {
        localRequestPayload.setPushRegistration(new Sidekick.PushRegistration().setRegistrationId(localRegistrationState.mRegistrationId).setAccountHash(GcmUtil.accountHashFor(localAccount)).setUnregister(false));
      }
      if (EntriesRefreshRequestType.isFullRefresh(paramInt)) {
        this.mExecutedUserActionStore.commitDeferredActions();
      }
      NetworkClient.ResponseAndEventId localResponseAndEventId = this.mSidekickInjector.getNetworkClient().sendRequestWithLocationCaptureEventId(localRequestPayload);
      Sidekick.ResponsePayload localResponsePayload;
      if (localResponseAndEventId != null)
      {
        localResponsePayload = localResponseAndEventId.mPayload;
        if ((localResponsePayload != null) && (localRegistrationState != null)) {
          this.mSidekickInjector.getGCMManager().ackRegistrationChangeFor(localAccount, localRegistrationState);
        }
        if ((localResponsePayload != null) && (localResponsePayload.hasEntryResponse()) && (!isResponseErrorMarker(localResponsePayload))) {
          break label852;
        }
        Log.w(TAG, "Error sending request to the server");
        localEntryProvider.notifyAboutRefreshFailure(paramInt, isResponseErrorMarker(localResponsePayload));
        localUserInteractionTimer1 = null;
      }
      for (;;)
      {
        if (localUserInteractionTimer1 == null) {
          break label1402;
        }
        localUserInteractionTimer1.timingComplete(null);
        return i;
        localResponsePayload = null;
        break;
        this.mCoreServices.getUserInteractionLogger().logInternalActionOncePerDay("REFRESH_SUCCESS", null);
        Sidekick.EntryResponse localEntryResponse = localResponsePayload.getEntryResponse();
        if (hasPartialFailure(localEntryResponse)) {
          Log.w(TAG, "Partial entry source failure from the server");
        }
        boolean bool1 = localEntryResponse.hasConfiguration();
        boolean bool2 = false;
        if (bool1)
        {
          Sidekick.Configuration localConfiguration1 = localEntryResponse.getConfiguration();
          bool2 = localConfiguration1.hasSidekickConfiguration();
          boolean bool4 = localNowOptInSettings.updateConfigurationForAccount(localAccount, localConfiguration1, null);
          localNowOptInSettings.saveConfiguration(localConfiguration1, localAccount, null);
          if ((!bool4) || ((localConfiguration1.hasSidekickConfiguration()) && (localConfiguration1.getSidekickConfiguration().hasNowCardsDisabled()) && (localConfiguration1.getSidekickConfiguration().getNowCardsDisabled())))
          {
            final boolean bool5 = localNowOptInSettings.domainIsBlockedFromNow(localConfiguration1, localAccount);
            this.mAsyncServices.getUiThreadExecutor().execute(new Runnable()
            {
              public void run()
              {
                if (bool5) {
                  localNowOptInSettings.optAccountOut(localAccount);
                }
                for (int i = 2;; i = 3)
                {
                  localEntryProvider.notifyAboutGoogleNowDisabled(i);
                  return;
                  localNowOptInSettings.disableForAccount(localAccount);
                }
              }
            });
          }
        }
        boolean bool3 = EntriesRefreshRequestType.isIncremental(paramInt);
        Sidekick.EntryTreeNode localEntryTreeNode = maybeGetDisabledLocationCard(bool3);
        if ((localEntryTreeNode != null) && (localEntryResponse.getEntryTreeCount() > 0) && (localEntryResponse.getEntryTree(0).hasRoot())) {
          localEntryResponse.getEntryTree(0).getRoot().addChild(localEntryTreeNode);
        }
        if (paramInt == 5) {
          localEntryProvider.updateFromPartialEntryResponse(localEntryResponse);
        }
        for (;;)
        {
          updateAnyCalendarData(localEntryResponse, localCalendarDataProvider);
          if (localResponsePayload.hasTrainingModeDataResponse()) {
            this.mTrainingQuestionManager.updateFromServerResponse(localResponsePayload.getTrainingModeDataResponse(), localIterable);
          }
          maybePrecacheMaps(localEntryResponse);
          PushMessageRepository localPushMessageRepository = this.mSidekickInjector.getPushMessageRespository();
          Iterator localIterator2 = paramImmutableList.iterator();
          while (localIterator2.hasNext()) {
            localPushMessageRepository.partialUpdateComplete((PushMessageRepository.PendingPartialUpdate)localIterator2.next());
          }
          if (bool3) {
            localEntryProvider.appendMoreCardEntries(localEntryResponse, localLocation);
          } else {
            localEntryProvider.updateFromEntryResponse(localEntryResponse, paramInt, localLocation, Locale.getDefault(), paramBoolean2, localResponseAndEventId.mEventId);
          }
        }
        if ((localEntryResponse.getEntryTreeCount() > 0) && (localEntryResponse.getEntryTree(0).getCallbackWithInterestCount() > 0))
        {
          Intent localIntent2 = new Intent(getApplicationContext(), NotificationRefreshService.class);
          localIntent2.setAction("com.google.android.apps.sidekick.notifications.SCHEDULE_REFRESH");
          Sidekick.EntryTree localEntryTree = new Sidekick.EntryTree();
          Iterator localIterator3 = localEntryResponse.getEntryTree(0).getCallbackWithInterestList().iterator();
          while (localIterator3.hasNext()) {
            localEntryTree.addCallbackWithInterest((Sidekick.EntryTree.CallbackWithInterest)localIterator3.next());
          }
          localIntent2.putExtra("com.google.android.apps.sidekick.notifications.NEXT_REFRESH", localEntryTree.toByteArray());
          startService(localIntent2);
        }
        if (bool2)
        {
          Intent localIntent1 = new Intent(getApplicationContext(), NotificationRefreshService.class);
          localIntent1.setAction("com.google.android.apps.sidekick.notifications.REFRESH_ALL_NOTIFICATIONS");
          startService(localIntent1);
        }
        if ((EntriesRefreshRequestType.isUserInitiated(paramInt)) && (EntriesRefreshRequestType.isFullRefresh(paramInt))) {
          this.mEntriesRefreshScheduler.setNextRefreshAlarm(true);
        }
      }
    }
  }
  
  private void updateAnyCalendarData(Sidekick.EntryResponse paramEntryResponse, CalendarDataProvider paramCalendarDataProvider)
  {
    Iterator localIterator = paramEntryResponse.getEntryTreeList().iterator();
    while (localIterator.hasNext()) {
      paramCalendarDataProvider.updateWithNewEntryTreeFromServer((Sidekick.EntryTree)localIterator.next());
    }
  }
  
  void injectDependeciesForTest(VelvetServices paramVelvetServices, CoreSearchServices paramCoreSearchServices, SearchConfig paramSearchConfig, SidekickInjector paramSidekickInjector, GmmPrecacher paramGmmPrecacher, TvDetector.Factory paramFactory, EntriesRefreshScheduler paramEntriesRefreshScheduler, SidekickInteractionManager paramSidekickInteractionManager, AsyncServices paramAsyncServices, TrainingQuestionManager paramTrainingQuestionManager, ExecutedUserActionStore paramExecutedUserActionStore)
  {
    this.mVelvetServices = paramVelvetServices;
    this.mCoreServices = paramCoreSearchServices;
    this.mConfig = paramSearchConfig;
    this.mSidekickInjector = paramSidekickInjector;
    this.mGmmPrecacher = paramGmmPrecacher;
    this.mTvDetectorFactory = paramFactory;
    this.mEntriesRefreshScheduler = paramEntriesRefreshScheduler;
    this.mInteractionManager = paramSidekickInteractionManager;
    this.mAsyncServices = paramAsyncServices;
    this.mTrainingQuestionManager = paramTrainingQuestionManager;
    this.mExecutedUserActionStore = paramExecutedUserActionStore;
  }
  
  void maybePrecacheMaps(Sidekick.EntryResponse paramEntryResponse)
  {
    Iterator localIterator3;
    do
    {
      Iterator localIterator1 = paramEntryResponse.getEntryTreeList().iterator();
      Iterator localIterator2;
      while (!localIterator2.hasNext())
      {
        Sidekick.EntryTree localEntryTree;
        do
        {
          if (!localIterator1.hasNext()) {
            break;
          }
          localEntryTree = (Sidekick.EntryTree)localIterator1.next();
        } while (!localEntryTree.hasRoot());
        localIterator2 = localEntryTree.getRoot().getChildList().iterator();
      }
      localIterator3 = ((Sidekick.EntryTreeNode)localIterator2.next()).getEntryList().iterator();
    } while (!localIterator3.hasNext());
    Iterator localIterator4 = ((Sidekick.Entry)localIterator3.next()).getPrecacheDirectiveList().iterator();
    label171:
    label211:
    for (;;)
    {
      label110:
      Sidekick.PrecacheDirective localPrecacheDirective;
      ImmutableList localImmutableList;
      if (localIterator4.hasNext())
      {
        localPrecacheDirective = (Sidekick.PrecacheDirective)localIterator4.next();
        if (!localPrecacheDirective.hasCentroid()) {
          break label171;
        }
        localImmutableList = ImmutableList.of(LocationUtilities.sidekickLocationToAndroidLocation(localPrecacheDirective.getCentroid()));
      }
      for (;;)
      {
        if (localImmutableList == null) {
          break label211;
        }
        this.mGmmPrecacher.precache(this, localImmutableList);
        break label110;
        break;
        int i = localPrecacheDirective.getBoundsCount();
        localImmutableList = null;
        if (i == 2) {
          localImmutableList = ImmutableList.of(LocationUtilities.sidekickLocationToAndroidLocation(localPrecacheDirective.getBounds(0)), LocationUtilities.sidekickLocationToAndroidLocation(localPrecacheDirective.getBounds(1)));
        }
      }
    }
  }
  
  public void onCreate()
  {
    initializeDependencies();
    super.onCreate();
    this.mVelvetServices.maybeRegisterSidekickAlarms();
  }
  
  public void onDestroy()
  {
    if (this.mActiveTvDetector != null) {
      this.mActiveTvDetector.stopDetection();
    }
    super.onDestroy();
  }
  
  @SuppressLint({"Wakelock"})
  protected int onHandleIntent(Intent paramIntent, DelayedStopIntentService.StopLatch paramStopLatch)
  {
    if (paramIntent == null) {
      return 0;
    }
    String str = paramIntent.getAction();
    if ((!"com.google.android.apps.sidekick.REFRESH".equals(str)) && (!"com.google.android.apps.sidekick.PARTIAL_REFRESH".equals(str))) {
      return 0;
    }
    int i = paramIntent.getIntExtra("com.google.android.apps.sidekick.TYPE", 1);
    ImmutableList localImmutableList = this.mSidekickInjector.getPushMessageRespository().getPendingPartialUpdates();
    if ((i == 5) && (localImmutableList.isEmpty())) {
      return 0;
    }
    PowerManager.WakeLock localWakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, "EntriesRefresh_wakelock");
    try
    {
      localWakeLock.acquire();
      boolean bool1 = this.mCoreServices.getNowOptInSettings().stopServicesIfUserOptedOut();
      if (bool1) {
        return 0;
      }
      this.mEntriesRefreshScheduler.setNextRefreshAlarm(false);
      EntryProvider localEntryProvider = this.mSidekickInjector.getEntryProvider();
      boolean bool2 = localEntryProvider.isInitializedFromStorage();
      if (!bool2) {
        return 0;
      }
      if (!EntriesRefreshRequestType.isUserInitiated(i))
      {
        boolean bool5 = localEntryProvider.hasPendingRefresh();
        if (bool5) {
          return 0;
        }
      }
      boolean bool3 = "com.google.android.apps.sidekick.PARTIAL_REFRESH".equals(str);
      PendingIntent localPendingIntent = null;
      if (bool3) {
        localPendingIntent = PendingIntent.getService(this, 0, paramIntent, 0);
      }
      boolean bool4 = paramIntent.getBooleanExtra("reminder_updated", false);
      long l = this.mCoreServices.getClock().elapsedRealtime();
      if (this.mSidekickInjector.getEntriesRefreshThrottle().isRefreshAllowed(i, localPendingIntent, l))
      {
        int j = refreshEntries(i, localImmutableList, paramStopLatch, paramIntent.getBooleanExtra("com.google.android.apps.sidekick.SAVE_CALL_LOG", false), bool4);
        return j;
      }
      return 0;
    }
    finally
    {
      localWakeLock.release();
    }
  }
  
  private static final class TvDetectorObserver
    implements TvDetector.Observer
  {
    private final DelayedStopIntentService.StopLatch mStopLatch;
    
    public TvDetectorObserver(DelayedStopIntentService.StopLatch paramStopLatch)
    {
      this.mStopLatch = paramStopLatch;
    }
    
    public void onTvDetectionFinished()
    {
      this.mStopLatch.release();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntriesRefreshIntentService
 * JD-Core Version:    0.7.0.1
 */