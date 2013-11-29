package com.google.android.sidekick.main.inject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.InProcessStaticMapLoader;
import com.google.android.sidekick.main.NowOptInHelper;
import com.google.android.sidekick.main.SensorSignalsOracle;
import com.google.android.sidekick.main.SessionManagerImpl;
import com.google.android.sidekick.main.SignedCipherHelperImpl;
import com.google.android.sidekick.main.UserClientIdManager;
import com.google.android.sidekick.main.VelvetNetworkClient;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.calendar.CalendarController;
import com.google.android.sidekick.main.calendar.CalendarControllerImpl;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.contextprovider.CardRenderingContextProviders;
import com.google.android.sidekick.main.contextprovider.EntryRenderingContextAdapter;
import com.google.android.sidekick.main.contextprovider.RenderingContextAdapterFactory;
import com.google.android.sidekick.main.contextprovider.RenderingContextPopulator;
import com.google.android.sidekick.main.entry.EntriesRefreshScheduler;
import com.google.android.sidekick.main.entry.EntriesRefreshThrottle;
import com.google.android.sidekick.main.entry.EntriesRefreshThrottleImpl;
import com.google.android.sidekick.main.entry.EntryInvalidator;
import com.google.android.sidekick.main.entry.EntryInvalidatorImpl;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.entry.EntryProviderImpl;
import com.google.android.sidekick.main.entry.EntryTreePruner;
import com.google.android.sidekick.main.entry.EntryValidator;
import com.google.android.sidekick.main.entry.LocationDisabledCardHelper;
import com.google.android.sidekick.main.file.AsyncFileStorage;
import com.google.android.sidekick.main.file.AsyncFileStorageImpl;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.main.gcm.GcmManager;
import com.google.android.sidekick.main.gcm.PushMessageRepository;
import com.google.android.sidekick.main.location.LocationManagerInjectable;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationOracleImpl;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.location.LocationStorage;
import com.google.android.sidekick.main.location.SystemLocationManagerInjectable;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.main.notifications.EntryNotificationFactory;
import com.google.android.sidekick.main.notifications.NotificationGeofencer;
import com.google.android.sidekick.main.notifications.NotificationStore;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.notifications.NowNotificationManagerImpl;
import com.google.android.sidekick.main.training.TrainingQuestionManagerImpl;
import com.google.android.sidekick.main.trigger.TriggerConditionEvaluator;
import com.google.android.sidekick.main.widget.WidgetManagerImpl;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapterFactory;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.velvet.ActivityLifecycleNotifier;
import com.google.android.velvet.Help;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.location.GmsLocationProvider;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.common.base.Supplier;
import java.util.Locale;

public class DefaultSidekickInjector
  implements SidekickInjector
{
  private ActivityHelper mActivityHelper;
  private final ActivityLifecycleNotifier mActivityLifecycleNotifier;
  private final Context mAppContext;
  private final AsyncFileStorage mAsyncFileStorage;
  private final AsyncServices mAsyncServices;
  private CalendarController mCalendarController;
  private CalendarDataProvider mCalendarDataProvider;
  private CardRenderingContextProviders mCardRenderingContextProviders;
  private final CoreSearchServices mCoreServices;
  private final Object mCreationLock;
  private DataBackendVersionStore mDataBackendVersionStore;
  private DirectionsLauncher mDirectionsLauncher;
  private EntriesRefreshScheduler mEntriesRefreshScheduler;
  private EntriesRefreshThrottle mEntriesRefreshThrottle;
  private EntryInvalidator mEntryInvalidator;
  private EntryAdapterFactory<EntryCardViewAdapter> mEntryItemFactory;
  private EntryAdapterFactory<EntryNotification> mEntryNotificationFactory;
  private EntryProvider mEntryProvider;
  private EntryTreePruner mEntryTreePruner;
  private EntryValidator mEntryValidator;
  private ExecutedUserActionStore mExecutedUserActionStore;
  private final FileBytesReader mFileBytesReader;
  private final FileBytesWriter mFileBytesWriter;
  private final FirstUseCardHandler mFirstUseCardHandler;
  private GcmManager mGCMManager;
  private final GmsLocationProvider mGmsLocationProvider;
  private SidekickInteractionManager mInteractionManager;
  private final LocalBroadcastManager mLocalBroadcastManager;
  private final LocationDisabledCardHelper mLocationDisabledCardHelper;
  private final LocationManagerInjectable mLocationManager;
  private final LocationOracle mLocationOracle;
  private LocationReportingOptInHelper mLocationReportingOptInHelper;
  private final MainPreferencesSupplier mMainPreferencesSupplier;
  private NetworkClient mNetworkClient;
  private final NotificationStore mNotificationStore;
  private final NowConfigurationSupplier mNowConfigurationSupplier;
  private NowNotificationManager mNowNotificationManager;
  private NowOptInHelper mNowOptInHelper;
  private NowRemoteClient mNowRemoteClient;
  private final GsaPreferenceController mPrefController;
  private PushMessageRepository mPushMessageRepository;
  private ReminderSmartActionUtil mReminderSmartActionUtil;
  private EntryAdapterFactory<EntryRenderingContextAdapter> mRenderingContextAdapterFactory;
  private RenderingContextPopulator mRenderingContextPopulator;
  private SensorSignalsOracle mSensorSignalsOracle;
  private final SignedCipherHelper mSignedCipherHelper;
  private StaticMapCache mStaticMapCache;
  private StaticMapLoader mStaticMapLoader;
  private TrainingQuestionManager mTrainingQuestionManager;
  private final TriggerConditionEvaluator mTriggerConditionEvaluator;
  private final UndoDismissManager mUndoDismissManager;
  private UserClientIdManager mUserClientIdManager;
  private VelvetImageGalleryHelper mVelvetImageGalleryHelper;
  private WidgetManager mWidgetManager;
  
  public DefaultSidekickInjector(Context paramContext, GsaPreferenceController paramGsaPreferenceController, CoreSearchServices paramCoreSearchServices, AsyncServices paramAsyncServices, ActivityLifecycleNotifier paramActivityLifecycleNotifier, Object paramObject)
  {
    this.mAppContext = paramContext;
    this.mPrefController = paramGsaPreferenceController;
    this.mCoreServices = paramCoreSearchServices;
    this.mAsyncServices = paramAsyncServices;
    this.mActivityLifecycleNotifier = paramActivityLifecycleNotifier;
    this.mCreationLock = paramObject;
    this.mMainPreferencesSupplier = new MainPreferencesSupplier(paramGsaPreferenceController, null);
    this.mNowConfigurationSupplier = new NowConfigurationSupplier(paramCoreSearchServices.getPredictiveCardsPreferences(), null);
    this.mLocationManager = new SystemLocationManagerInjectable((LocationManager)paramContext.getSystemService("location"), paramCoreSearchServices.getLocationSettings());
    this.mGmsLocationProvider = new GmsLocationProvider(paramContext, paramAsyncServices.getUiThreadExecutor(), paramAsyncServices.getPooledBackgroundExecutorService());
    this.mSignedCipherHelper = new SignedCipherHelperImpl(paramGsaPreferenceController);
    this.mFileBytesReader = new FileBytesReader(paramContext, this.mSignedCipherHelper);
    this.mFileBytesWriter = new FileBytesWriter(paramContext, this.mSignedCipherHelper);
    this.mAsyncFileStorage = new AsyncFileStorageImpl(this.mFileBytesReader, this.mFileBytesWriter);
    this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(paramContext);
    this.mLocationOracle = new LocationOracleImpl(this.mLocationManager, this.mGmsLocationProvider, paramCoreSearchServices.getClock(), this.mFileBytesReader, this.mFileBytesWriter, paramCoreSearchServices.getLocationSettings(), DebugFeatures.getInstance(), paramGsaPreferenceController, this.mLocalBroadcastManager, new LocationStorage(this.mSignedCipherHelper));
    this.mNotificationStore = new NotificationStore(this.mFileBytesReader, this.mFileBytesWriter, this.mCoreServices.getClock(), this.mAppContext, this.mAsyncServices);
    this.mLocationOracle.addLightweightGeofencer(new NotificationGeofencer(this.mAppContext, this.mNotificationStore));
    this.mTriggerConditionEvaluator = new TriggerConditionEvaluator(this.mLocationOracle);
    this.mFirstUseCardHandler = new FirstUseCardHandler(this.mPrefController.getMainPreferences(), this.mCoreServices.getClock());
    this.mUndoDismissManager = new UndoDismissManager(paramContext, paramAsyncServices.getUiThreadExecutor());
    this.mLocationDisabledCardHelper = new LocationDisabledCardHelper(this.mAppContext, this.mMainPreferencesSupplier, this.mLocationManager, this.mCoreServices.getLocationSettings(), this.mCoreServices.getLoginHelper());
  }
  
  private boolean checkContains(String paramString, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfString[j].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  private DirectionsLauncher getDirectionsLauncher()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mDirectionsLauncher == null) {
        this.mDirectionsLauncher = new DirectionsLauncher(this.mAppContext, getActivityHelper());
      }
      DirectionsLauncher localDirectionsLauncher = this.mDirectionsLauncher;
      return localDirectionsLauncher;
    }
  }
  
  private EntryValidator getEntryValidator()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryValidator == null) {
        this.mEntryValidator = new EntryValidator(getCalendarDataProvider(), this.mAppContext);
      }
      EntryValidator localEntryValidator = this.mEntryValidator;
      return localEntryValidator;
    }
  }
  
  public boolean areRemindersEnabled()
  {
    Locale localLocale = Locale.getDefault();
    String str = localLocale.getLanguage();
    if (Locale.ENGLISH.getLanguage().equals(str)) {}
    GsaConfigFlags localGsaConfigFlags;
    do
    {
      return true;
      localGsaConfigFlags = this.mCoreServices.getGsaConfigFlags();
    } while ((checkContains(str, localGsaConfigFlags.getEnabledRemindersSettingsLanguages())) || (checkContains(localLocale.toString(), localGsaConfigFlags.getEnabledRemindersSettingsLocales())));
    return false;
  }
  
  public ActivityHelper getActivityHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mActivityHelper == null) {
        this.mActivityHelper = new ActivityHelper(this.mAsyncServices.getUiThreadExecutor());
      }
      ActivityHelper localActivityHelper = this.mActivityHelper;
      return localActivityHelper;
    }
  }
  
  public AsyncFileStorage getAsyncFileStorage()
  {
    return this.mAsyncFileStorage;
  }
  
  public CalendarController getCalendarController()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mCalendarController == null) {
        this.mCalendarController = new CalendarControllerImpl(this.mAppContext, this.mCoreServices.getClock());
      }
      CalendarController localCalendarController = this.mCalendarController;
      return localCalendarController;
    }
  }
  
  public CalendarDataProvider getCalendarDataProvider()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mCalendarDataProvider == null) {
        this.mCalendarDataProvider = getCalendarController().newCalendarDataProvider();
      }
      CalendarDataProvider localCalendarDataProvider = this.mCalendarDataProvider;
      return localCalendarDataProvider;
    }
  }
  
  public CardRenderingContextProviders getCardRenderingContextProviders()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mCardRenderingContextProviders == null) {
        this.mCardRenderingContextProviders = new CardRenderingContextProviders(this.mAppContext, this.mCoreServices.getPredictiveCardsPreferences(), this.mNowConfigurationSupplier, this.mCalendarDataProvider, this.mCoreServices.getLoginHelper(), new Help(this.mAppContext));
      }
      CardRenderingContextProviders localCardRenderingContextProviders = this.mCardRenderingContextProviders;
      return localCardRenderingContextProviders;
    }
  }
  
  public DataBackendVersionStore getDataBackendVersionStore()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mDataBackendVersionStore == null) {
        this.mDataBackendVersionStore = new DataBackendVersionStore(this.mAppContext, this.mMainPreferencesSupplier, this.mCoreServices.getClock());
      }
      DataBackendVersionStore localDataBackendVersionStore = this.mDataBackendVersionStore;
      return localDataBackendVersionStore;
    }
  }
  
  public EntriesRefreshScheduler getEntriesRefreshScheduler()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntriesRefreshScheduler == null) {
        this.mEntriesRefreshScheduler = new EntriesRefreshScheduler(this.mAppContext, this.mCoreServices.getAlarmHelper(), this.mCoreServices.getConfig(), getInteractionManager(), this.mCoreServices.getPendingIntentFactory());
      }
      EntriesRefreshScheduler localEntriesRefreshScheduler = this.mEntriesRefreshScheduler;
      return localEntriesRefreshScheduler;
    }
  }
  
  public EntriesRefreshThrottle getEntriesRefreshThrottle()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntriesRefreshThrottle == null) {
        this.mEntriesRefreshThrottle = new EntriesRefreshThrottleImpl(this.mCoreServices.getAlarmHelper(), this.mPrefController, this.mCoreServices.getGsaConfigFlags());
      }
      EntriesRefreshThrottle localEntriesRefreshThrottle = this.mEntriesRefreshThrottle;
      return localEntriesRefreshThrottle;
    }
  }
  
  public EntryAdapterFactory<EntryCardViewAdapter> getEntryCardViewFactory()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryItemFactory == null) {
        this.mEntryItemFactory = new EntryCardViewAdapterFactory(this.mCoreServices.getClock(), getDirectionsLauncher(), (WifiManager)this.mAppContext.getSystemService("wifi"), getActivityHelper(), this.mAsyncServices.getUiThreadExecutor());
      }
      return this.mEntryItemFactory;
    }
  }
  
  public EntryInvalidator getEntryInvalidator()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryInvalidator == null) {
        this.mEntryInvalidator = new EntryInvalidatorImpl(getEntryProvider(), getNowNotificationManager(), getLocationDisabledCardHelper());
      }
      EntryInvalidator localEntryInvalidator = this.mEntryInvalidator;
      return localEntryInvalidator;
    }
  }
  
  public EntryAdapterFactory<EntryNotification> getEntryNotificationFactory()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryNotificationFactory == null) {
        this.mEntryNotificationFactory = new EntryNotificationFactory(getLocationOracle(), getCalendarDataProvider(), getDirectionsLauncher(), this.mCoreServices.getClock(), getEntryValidator(), getReminderSmartActionUtil());
      }
      return this.mEntryNotificationFactory;
    }
  }
  
  public EntryProvider getEntryProvider()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryProvider == null)
      {
        this.mEntryProvider = new EntryProviderImpl(this.mCoreServices.getClock(), this.mAppContext, this.mAsyncFileStorage, this.mNotificationStore, this.mLocationOracle, this.mCoreServices.getConfig(), this.mAsyncServices.getPooledBackgroundExecutorService(), this, this.mAsyncServices.getScheduledBackgroundExecutorService(), this.mCoreServices.getPredictiveCardsPreferences(), getEntryNotificationFactory(), getEntryTreePruner());
        this.mEntryProvider.registerEntryProviderObserver(this.mNotificationStore);
      }
      EntryProvider localEntryProvider = this.mEntryProvider;
      return localEntryProvider;
    }
  }
  
  public EntryAdapterFactory<EntryRenderingContextAdapter> getEntryRenderingContextAdapterFactory()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mRenderingContextAdapterFactory == null) {
        this.mRenderingContextAdapterFactory = new RenderingContextAdapterFactory(getCalendarDataProvider(), this.mMainPreferencesSupplier, getEntryValidator());
      }
      return this.mRenderingContextAdapterFactory;
    }
  }
  
  public EntryTreePruner getEntryTreePruner()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mEntryTreePruner == null) {
        this.mEntryTreePruner = new EntryTreePruner(getEntryValidator());
      }
      EntryTreePruner localEntryTreePruner = this.mEntryTreePruner;
      return localEntryTreePruner;
    }
  }
  
  public ExecutedUserActionStore getExecutedUserActionStore()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mExecutedUserActionStore == null) {
        this.mExecutedUserActionStore = new ExecutedUserActionStoreImpl(this.mAppContext, this.mFileBytesReader, this.mFileBytesWriter, this.mCoreServices.getClock(), this.mActivityLifecycleNotifier);
      }
      ExecutedUserActionStore localExecutedUserActionStore = this.mExecutedUserActionStore;
      return localExecutedUserActionStore;
    }
  }
  
  public FirstUseCardHandler getFirstUseCardHandler()
  {
    return this.mFirstUseCardHandler;
  }
  
  public GcmManager getGCMManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mGCMManager == null) {
        this.mGCMManager = new GcmManager(this.mAppContext, GoogleCloudMessaging.getInstance(this.mAppContext), this.mCoreServices.getClock(), this.mMainPreferencesSupplier);
      }
      GcmManager localGcmManager = this.mGCMManager;
      return localGcmManager;
    }
  }
  
  public SidekickInteractionManager getInteractionManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mInteractionManager == null) {
        this.mInteractionManager = new SidekickInteractionManager(this.mMainPreferencesSupplier, this.mCoreServices.getConfig(), getWidgetManager(), this.mCoreServices.getClock());
      }
      SidekickInteractionManager localSidekickInteractionManager = this.mInteractionManager;
      return localSidekickInteractionManager;
    }
  }
  
  public LocalBroadcastManager getLocalBroadcastManager()
  {
    return this.mLocalBroadcastManager;
  }
  
  public LocationDisabledCardHelper getLocationDisabledCardHelper()
  {
    return this.mLocationDisabledCardHelper;
  }
  
  public LocationOracle getLocationOracle()
  {
    return this.mLocationOracle;
  }
  
  public LocationReportingOptInHelper getLocationReportingOptInHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mLocationReportingOptInHelper == null) {
        this.mLocationReportingOptInHelper = new LocationReportingOptInHelper(this.mAppContext, this.mMainPreferencesSupplier, this.mCoreServices.getClock(), this.mCoreServices.getAlarmHelper(), this.mCoreServices.getLoginHelper(), (PowerManager)this.mAppContext.getSystemService("power"), this.mCoreServices.getGmsLocationReportingHelper(), this.mAsyncServices.getUiThreadExecutor());
      }
      LocationReportingOptInHelper localLocationReportingOptInHelper = this.mLocationReportingOptInHelper;
      return localLocationReportingOptInHelper;
    }
  }
  
  public NetworkClient getNetworkClient()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mNetworkClient == null)
      {
        ConnectivityManager localConnectivityManager = (ConnectivityManager)this.mAppContext.getSystemService("connectivity");
        SessionManagerImpl localSessionManagerImpl = new SessionManagerImpl(this.mPrefController, this.mCoreServices.getClock(), this.mSignedCipherHelper);
        this.mNetworkClient = new VelvetNetworkClient(this.mAppContext, this.mCoreServices, DebugFeatures.getInstance(), VelvetApplication.getVersionName(), this.mCoreServices.getHttpHelper(), this.mCoreServices.getLoginHelper(), getSensorSignalsOracle(), localConnectivityManager, getExecutedUserActionStore(), localSessionManagerImpl, this.mCoreServices.getGooglePlayServicesHelper(), getUserClientIdManager(), this.mPrefController);
      }
      NetworkClient localNetworkClient = this.mNetworkClient;
      return localNetworkClient;
    }
  }
  
  public NotificationStore getNotificationStore()
  {
    return this.mNotificationStore;
  }
  
  public Supplier<NowConfigurationPreferences> getNowConfigurationPreferencesSupplier()
  {
    return this.mNowConfigurationSupplier;
  }
  
  public NowNotificationManager getNowNotificationManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mNowNotificationManager == null)
      {
        SystemNotificationManagerInjectable localSystemNotificationManagerInjectable = new SystemNotificationManagerInjectable(this.mAppContext);
        this.mNowNotificationManager = new NowNotificationManagerImpl(this.mAppContext, this.mCoreServices.getClock(), getEntryProvider(), this.mPrefController, this.mCoreServices.getUserInteractionLogger(), localSystemNotificationManagerInjectable, getNetworkClient(), this.mCoreServices.getPendingIntentFactory(), getEntryRenderingContextAdapterFactory(), getLocationOracle(), getCardRenderingContextProviders());
      }
      NowNotificationManager localNowNotificationManager = this.mNowNotificationManager;
      return localNowNotificationManager;
    }
  }
  
  public NowOptInHelper getNowOptInHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mNowOptInHelper == null) {
        this.mNowOptInHelper = new NowOptInHelper(this.mAsyncServices.getUiThreadExecutor(), this.mCoreServices.getLoginHelper(), getNetworkClient(), this.mCoreServices.getPredictiveCardsPreferences(), this.mCoreServices.getNowOptInSettings(), getLocationReportingOptInHelper(), VelvetServices.get(), getEntryProvider());
      }
      NowOptInHelper localNowOptInHelper = this.mNowOptInHelper;
      return localNowOptInHelper;
    }
  }
  
  public NowRemoteClient getNowRemoteClient()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mNowRemoteClient == null) {
        this.mNowRemoteClient = new NowRemoteClient(this.mAppContext, this.mAsyncServices.getPooledBackgroundExecutorService(), this.mAsyncServices.getUiThreadExecutor());
      }
      NowRemoteClient localNowRemoteClient = this.mNowRemoteClient;
      return localNowRemoteClient;
    }
  }
  
  public PushMessageRepository getPushMessageRespository()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mPushMessageRepository == null) {
        this.mPushMessageRepository = new PushMessageRepository(this.mFileBytesReader, this.mFileBytesWriter);
      }
      PushMessageRepository localPushMessageRepository = this.mPushMessageRepository;
      return localPushMessageRepository;
    }
  }
  
  public ReminderSmartActionUtil getReminderSmartActionUtil()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mReminderSmartActionUtil == null) {
        this.mReminderSmartActionUtil = new ReminderSmartActionUtil(this.mCoreServices.getDeviceCapabilityManager());
      }
      ReminderSmartActionUtil localReminderSmartActionUtil = this.mReminderSmartActionUtil;
      return localReminderSmartActionUtil;
    }
  }
  
  public RenderingContextPopulator getRenderingContextPopulator()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mRenderingContextPopulator == null) {
        this.mRenderingContextPopulator = new RenderingContextPopulator(getCardRenderingContextProviders(), getEntryRenderingContextAdapterFactory());
      }
      RenderingContextPopulator localRenderingContextPopulator = this.mRenderingContextPopulator;
      return localRenderingContextPopulator;
    }
  }
  
  public SensorSignalsOracle getSensorSignalsOracle()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mSensorSignalsOracle == null) {
        this.mSensorSignalsOracle = new SensorSignalsOracle(this.mAppContext, this.mCoreServices.getClock(), this.mLocationOracle, this.mCoreServices.getDeviceCapabilityManager(), getWidgetManager(), this.mCoreServices.getGmsLocationReportingHelper(), (WifiManager)this.mAppContext.getSystemService("wifi"));
      }
      SensorSignalsOracle localSensorSignalsOracle = this.mSensorSignalsOracle;
      return localSensorSignalsOracle;
    }
  }
  
  public StaticMapCache getStaticMapCache()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mStaticMapCache == null) {
        this.mStaticMapCache = new StaticMapCacheImpl(getEntryProvider(), this.mAppContext, getNetworkClient());
      }
      StaticMapCache localStaticMapCache = this.mStaticMapCache;
      return localStaticMapCache;
    }
  }
  
  public StaticMapLoader getStaticMapLoader()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mStaticMapLoader == null) {
        this.mStaticMapLoader = new InProcessStaticMapLoader(this.mAppContext.getResources(), this.mAsyncServices.getUiThreadExecutor(), this.mAsyncServices.getPooledBackgroundExecutorService(), getStaticMapCache());
      }
      StaticMapLoader localStaticMapLoader = this.mStaticMapLoader;
      return localStaticMapLoader;
    }
  }
  
  public TrainingQuestionManager getTrainingQuestionManager()
  {
    NetworkClient localNetworkClient = getNetworkClient();
    synchronized (this.mCreationLock)
    {
      if (this.mTrainingQuestionManager == null) {
        this.mTrainingQuestionManager = new TrainingQuestionManagerImpl(this.mFileBytesReader, this.mFileBytesWriter, localNetworkClient, this.mCoreServices.getClock(), this.mAsyncServices.getPooledBackgroundExecutorService(), this.mCoreServices.getBackgroundTasks(), this.mFirstUseCardHandler, getCalendarDataProvider(), this.mCoreServices.getNowOptInSettings());
      }
      TrainingQuestionManager localTrainingQuestionManager = this.mTrainingQuestionManager;
      return localTrainingQuestionManager;
    }
  }
  
  public TriggerConditionEvaluator getTriggerConditionEvaluator()
  {
    return this.mTriggerConditionEvaluator;
  }
  
  public UndoDismissManager getUndoDismissManager()
  {
    return this.mUndoDismissManager;
  }
  
  public UserClientIdManager getUserClientIdManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mUserClientIdManager == null) {
        this.mUserClientIdManager = new UserClientIdManager(this.mAppContext, this.mMainPreferencesSupplier);
      }
      UserClientIdManager localUserClientIdManager = this.mUserClientIdManager;
      return localUserClientIdManager;
    }
  }
  
  public VelvetImageGalleryHelper getVelvetImageGalleryHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mVelvetImageGalleryHelper == null) {
        this.mVelvetImageGalleryHelper = new VelvetImageGalleryHelper(this.mAppContext, this.mCoreServices.getImageMetadataController());
      }
      VelvetImageGalleryHelper localVelvetImageGalleryHelper = this.mVelvetImageGalleryHelper;
      return localVelvetImageGalleryHelper;
    }
  }
  
  public WidgetManager getWidgetManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mWidgetManager == null) {
        this.mWidgetManager = new WidgetManagerImpl(this.mAppContext, this.mAsyncServices.getUiThreadExecutor());
      }
      WidgetManager localWidgetManager = this.mWidgetManager;
      return localWidgetManager;
    }
  }
  
  private static final class MainPreferencesSupplier
    implements Supplier<SharedPreferences>
  {
    private final GsaPreferenceController mPrefController;
    
    private MainPreferencesSupplier(GsaPreferenceController paramGsaPreferenceController)
    {
      this.mPrefController = paramGsaPreferenceController;
    }
    
    public SharedPreferences get()
    {
      return this.mPrefController.getMainPreferences();
    }
  }
  
  private static final class NowConfigurationSupplier
    implements Supplier<NowConfigurationPreferences>
  {
    private final PredictiveCardsPreferences mPredictiveCardsPreferences;
    
    private NowConfigurationSupplier(PredictiveCardsPreferences paramPredictiveCardsPreferences)
    {
      this.mPredictiveCardsPreferences = paramPredictiveCardsPreferences;
    }
    
    public NowConfigurationPreferences get()
    {
      try
      {
        NowConfigurationPreferences localNowConfigurationPreferences = this.mPredictiveCardsPreferences.getWorkingPreferences();
        return localNowConfigurationPreferences;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.DefaultSidekickInjector
 * JD-Core Version:    0.7.0.1
 */