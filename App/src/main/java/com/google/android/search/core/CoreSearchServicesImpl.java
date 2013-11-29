package com.google.android.search.core;

import android.accounts.AccountManager;
import android.content.Context;
import android.database.DataSetObservable;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import com.google.android.e100.MessageBuffer;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.discoursecontext.DiscourseContextProtoHelper;
import com.google.android.search.core.google.LocationOptIn;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.PartnerInfo;
import com.google.android.search.core.google.RlzHelper;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.UriRewriter;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.AccountManagerGoogleAuthAdapter;
import com.google.android.search.core.google.gaia.CachingGoogleAuthAdapter;
import com.google.android.search.core.google.gaia.FallingBackGoogleAuthAdapter;
import com.google.android.search.core.google.gaia.GoogleAuthAdapter;
import com.google.android.search.core.google.gaia.GoogleAuthAdapterImpl;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.sdch.SdchDictionaryCache;
import com.google.android.search.core.sdch.SdchFetcher;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.search.core.util.ForceableLock;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.JavaNetHttpHelper;
import com.google.android.search.core.util.NetworkBytesLoader;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.shared.util.SystemClockImpl;
import com.google.android.sidekick.main.GCoreUlrController;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.android.sidekick.main.inject.SystemPendingIntentFactory;
import com.google.android.speech.network.ConnectionFactory;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.velvet.ActionDiscoveryData;
import com.google.android.velvet.Cookies;
import com.google.android.velvet.Corpora;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.VelvetBackgroundTasksImpl;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.gallery.ImageMetadataController;
import com.google.android.velvet.gallery.ImageMetadataParser;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilderImpl;
import com.google.android.voicesearch.speechservice.spdy.SpdyConnectionFactory;
import com.google.common.base.Supplier;
import java.util.Locale;
import java.util.Random;

public class CoreSearchServicesImpl
  implements CoreSearchServices
{
  private ActionDiscoveryData mActionDiscoveryData;
  private final AlarmHelper mAlarmHelper;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Context mContext;
  private final Cookies mCookies;
  private ForceableLock mCookiesLock;
  private final Corpora mCorpora;
  private final Object mCreationLock;
  private final DeviceCapabilityManager mDeviceCapabilityManager;
  private DiscourseContext mDiscourseContext;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final GooglePlayServicesHelper mGooglePlayServicesHelper;
  private final NamingDelayedTaskExecutor mHttpExecutor;
  private final HttpHelper mHttpHelper;
  private ImageMetadataController mImageMetadataController;
  private ImageMetadataParser mImageMetadataParser;
  private final LocationSettings mLocationSettings;
  private final LoginHelper mLoginHelper;
  private MessageBuffer mMessageBuffer;
  private NetworkInformation mNetworkInfo;
  private final NowOptInSettings mNowOptInSettings;
  private final PendingIntentFactory mPendingIntentFactory;
  private PinholeParamsBuilderImpl mPinholeParamsBuilder;
  private final PredictiveCardsPreferences mPredictiveCardsPreferences;
  private final RlzHelper mRlzHelper;
  private SdchManager mSdchManager;
  private final SearchBoxLogging mSearchBoxLogging;
  private SearchControllerCache mSearchControllerCache;
  private final DataSetObservable mSearchHistoryChangedObservable;
  private final VelvetServices mServices;
  private final SearchSettings mSettings;
  private SpdyConnectionFactory mSpdyConnectionFactory;
  private final UriRewriter mUriRewriter;
  private SearchUrlHelper mUrlHelper;
  private final UserAgentHelper mUserAgentHelper;
  private UserInteractionLogger mUserInteractionLogger;
  private VelvetBackgroundTasks mVelvetBackgroundTasks;
  
  public CoreSearchServicesImpl(Context paramContext, VelvetServices paramVelvetServices, Object paramObject)
  {
    this.mCreationLock = paramObject;
    this.mContext = paramContext;
    this.mServices = paramVelvetServices;
    this.mConfig = createConfig();
    this.mClock = createClock(paramContext);
    this.mSettings = createSearchSettings();
    this.mUriRewriter = createUriRewriter();
    this.mUserAgentHelper = new UserAgentHelper(paramContext, this.mConfig, this.mSettings);
    this.mHttpExecutor = this.mServices.getAsyncServices().getNamedUserFacingTaskExecutor("http");
    this.mHttpHelper = createHttpHelper();
    this.mRlzHelper = createRlzHelper();
    this.mLocationSettings = createLocationSettings();
    this.mGooglePlayServicesHelper = new GooglePlayServicesHelper(paramContext, this.mServices.getAsyncServices().getNamedUserFacingTaskExecutor("gms-helper"), this.mServices.getAsyncServices().getUiThreadExecutor());
    this.mLoginHelper = createLoginHelper();
    this.mSearchBoxLogging = createSearchBoxLogging();
    this.mCookies = Cookies.create(paramContext);
    this.mPredictiveCardsPreferences = new PredictiveCardsPreferences(paramVelvetServices.getPreferenceController());
    this.mCorpora = createCorpora();
    this.mCorpora.init();
    this.mAlarmHelper = new AlarmHelper(paramContext, this.mClock, paramVelvetServices.getPreferenceController(), new Random());
    this.mSearchHistoryChangedObservable = new DataSetObservable();
    this.mDeviceCapabilityManager = createDeviceCapabilitiesManager();
    this.mPendingIntentFactory = new SystemPendingIntentFactory(paramContext);
    this.mGmsLocationReportingHelper = new GmsLocationReportingHelper(paramContext, this.mServices.getAsyncServices().getUiThreadExecutor(), this.mServices.getAsyncServices().getPooledBackgroundExecutorService(), this.mLoginHelper, this.mClock);
    this.mNowOptInSettings = createNowOptInSettings();
    if (getConfig().isGCoreUlrBurstModeEnabled()) {
      new GCoreUlrController(this.mPredictiveCardsPreferences, this.mLoginHelper, this.mGmsLocationReportingHelper);
    }
  }
  
  private GoogleAuthAdapter createAuthUtil()
  {
    return new CachingGoogleAuthAdapter(getConfig(), getClock(), new FallingBackGoogleAuthAdapter(new GoogleAuthAdapterImpl(), new AccountManagerGoogleAuthAdapter(getAccountManager()), getGooglePlayServicesHelper()));
  }
  
  private VelvetBackgroundTasks createBackgroundTasks()
  {
    return new VelvetBackgroundTasksImpl(getServices(), getClock(), getConfig(), getSearchSettings(), getServices().getAsyncServices().getNamedBackgroundTaskExecutor("background-tasks"), getServices().getFactory(), this.mAlarmHelper, getPendingIntentFactory(), this.mContext);
  }
  
  private Clock createClock(Context paramContext)
  {
    return new SystemClockImpl(paramContext);
  }
  
  private SearchConfig createConfig()
  {
    return new SearchConfig(getContext(), this.mServices.getPreferenceController());
  }
  
  private ForceableLock createCookiesLock()
  {
    return new ForceableLock(this.mServices.getAsyncServices().getUiThreadExecutor());
  }
  
  private Corpora createCorpora()
  {
    return new Corpora(this.mContext, getConfig(), getSearchSettings(), new NetworkBytesLoader(getHttpHelper(), 9), getServices().getAsyncServices().getUiThreadExecutor(), getServices().getAsyncServices().getNamedUserFacingTaskExecutor("corpora"), VelvetApplication.getVersionCode());
  }
  
  private DeviceCapabilityManagerImpl createDeviceCapabilitiesManager()
  {
    return new DeviceCapabilityManagerImpl(getContext());
  }
  
  private DiscourseContext createDiscourseContext()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mDiscourseContext == null) {
        this.mDiscourseContext = new DiscourseContext();
      }
      DiscourseContext localDiscourseContext = this.mDiscourseContext;
      return localDiscourseContext;
    }
  }
  
  private HttpHelper createHttpHelper()
  {
    return new JavaNetHttpHelper(this.mConfig, this.mUriRewriter, this.mUserAgentHelper, this.mHttpExecutor, getContext());
  }
  
  private ImageMetadataController createImageMetadataController()
  {
    return new ImageMetadataController(this.mContext, getHttpHelper(), getSearchUrlHelper(), getServices().getAsyncServices().getNamedUserFacingTaskExecutor("imagemetadata"), getServices().getAsyncServices().getScheduledBackgroundExecutorService(), getImageMetadataParser());
  }
  
  private LocationSettings createLocationSettings()
  {
    return new LocationOptIn(this.mContext, getServices().getAsyncServices().getUiThreadExecutor(), getServices().getAsyncServices().getNamedUserFacingTaskExecutor("Location-updater"));
  }
  
  private LoginHelper createLoginHelper()
  {
    return new LoginHelper(getContext(), getSearchSettings(), this.mServices.getAsyncServices().getNamedUserFacingTaskExecutor("gaia"), createAuthUtil(), getAccountManager(), getNowOptInSettingsSupplier());
  }
  
  private NowOptInSettings createNowOptInSettings()
  {
    return new NowOptInSettingsImpl(this.mServices, this.mContext, this.mServices.getPreferenceController(), getClock(), this.mServices.getAsyncServices().getNamedUserFacingTaskExecutor("predictivecardssettings"), getLoginHelper(), this.mConfig, getPredictiveCardsPreferences(), this.mGmsLocationReportingHelper);
  }
  
  private RlzHelper createRlzHelper()
  {
    return new RlzHelper((VelvetApplication)getContext(), this.mServices.getAsyncServices().getNamedUserFacingTaskExecutor("rlz"), this.mContext, getConfig());
  }
  
  private SearchBoxLogging createSearchBoxLogging()
  {
    return new SearchBoxLogging(getConfig(), getHttpHelper(), this.mServices.getAsyncServices().getNamedBackgroundTaskExecutor("clicklog"), this.mClock, getSearchSettings(), getLocationSettings(), getLoginHelper());
  }
  
  private SearchSettings createSearchSettings()
  {
    return new SearchSettingsImpl(getContext(), this.mServices.getPreferenceController());
  }
  
  private SearchUrlHelper createSearchUrlHelper()
  {
    Supplier local1 = new Supplier()
    {
      public Locale get()
      {
        return Locale.getDefault();
      }
    };
    return new SearchUrlHelper(getSearchSettings(), getConfig(), getGsaConfigFlags(), getPredictiveCardsPreferences(), getClock(), getCorpora(), getServices(), getSearchBoxLogging(), getRlzHelper(), local1, new PartnerInfo(getContext()), this.mCookies, this.mServices.getVoiceSearchServices().getSettings(), new DiscourseContextProtoHelper(getDiscourseContext(), this.mClock, getContext().getResources()), getLoginHelper(), VelvetApplication.getVersionName());
  }
  
  private UriRewriter createUriRewriter()
  {
    return new UriRewriter(this.mContext);
  }
  
  private UserInteractionLogger createUserInteractionLogger()
  {
    return new UserInteractionLogger(this.mContext, this.mClock, this.mServices.getPreferenceController(), getConfig());
  }
  
  private AccountManager getAccountManager()
  {
    synchronized (this.mCreationLock)
    {
      AccountManager localAccountManager = AccountManager.get(this.mContext);
      return localAccountManager;
    }
  }
  
  private Context getContext()
  {
    return this.mContext;
  }
  
  private ImageMetadataParser getImageMetadataParser()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mImageMetadataParser == null) {
        this.mImageMetadataParser = new ImageMetadataParser();
      }
      ImageMetadataParser localImageMetadataParser = this.mImageMetadataParser;
      return localImageMetadataParser;
    }
  }
  
  private Supplier<NowOptInSettings> getNowOptInSettingsSupplier()
  {
    new Supplier()
    {
      public NowOptInSettings get()
      {
        return CoreSearchServicesImpl.this.getNowOptInSettings();
      }
    };
  }
  
  private VelvetServices getServices()
  {
    return this.mServices;
  }
  
  public ActionDiscoveryData getActionDiscoveryData()
  {
    for (;;)
    {
      synchronized (this.mCreationLock)
      {
        Settings localSettings = this.mServices.getVoiceSearchServices().getSettings();
        if (localSettings.isSpokenLocaleBcp47Set())
        {
          str = localSettings.getSpokenLocaleBcp47();
          if ((this.mActionDiscoveryData == null) || (!this.mActionDiscoveryData.isSupported(str))) {
            this.mActionDiscoveryData = new ActionDiscoveryData(this.mConfig, this.mSettings, new NetworkBytesLoader(getHttpHelper(), 12), getServices().getAsyncServices().getNamedUserFacingTaskExecutor("actiondiscovery"), str);
          }
          ActionDiscoveryData localActionDiscoveryData = this.mActionDiscoveryData;
          return localActionDiscoveryData;
        }
      }
      String str = null;
    }
  }
  
  public AlarmHelper getAlarmHelper()
  {
    return this.mAlarmHelper;
  }
  
  public VelvetBackgroundTasks getBackgroundTasks()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mVelvetBackgroundTasks == null) {
        this.mVelvetBackgroundTasks = createBackgroundTasks();
      }
      VelvetBackgroundTasks localVelvetBackgroundTasks = this.mVelvetBackgroundTasks;
      return localVelvetBackgroundTasks;
    }
  }
  
  public Clock getClock()
  {
    return this.mClock;
  }
  
  public SearchConfig getConfig()
  {
    return this.mConfig;
  }
  
  public Cookies getCookies()
  {
    return this.mCookies;
  }
  
  public ForceableLock getCookiesLock()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mCookiesLock == null) {
        this.mCookiesLock = createCookiesLock();
      }
      ForceableLock localForceableLock = this.mCookiesLock;
      return localForceableLock;
    }
  }
  
  public Corpora getCorpora()
  {
    return this.mCorpora;
  }
  
  public DeviceCapabilityManager getDeviceCapabilityManager()
  {
    return this.mDeviceCapabilityManager;
  }
  
  public Supplier<DiscourseContext> getDiscourseContext()
  {
    new Supplier()
    {
      public DiscourseContext get()
      {
        return CoreSearchServicesImpl.this.createDiscourseContext();
      }
    };
  }
  
  public GmsLocationReportingHelper getGmsLocationReportingHelper()
  {
    return this.mGmsLocationReportingHelper;
  }
  
  public GooglePlayServicesHelper getGooglePlayServicesHelper()
  {
    return this.mGooglePlayServicesHelper;
  }
  
  public GsaConfigFlags getGsaConfigFlags()
  {
    return getServices().getGsaConfigFlags();
  }
  
  public NamingDelayedTaskExecutor getHttpExecutor()
  {
    return this.mHttpExecutor;
  }
  
  public HttpHelper getHttpHelper()
  {
    return this.mHttpHelper;
  }
  
  public ImageMetadataController getImageMetadataController()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mImageMetadataController == null) {
        this.mImageMetadataController = createImageMetadataController();
      }
      ImageMetadataController localImageMetadataController = this.mImageMetadataController;
      return localImageMetadataController;
    }
  }
  
  public LocationSettings getLocationSettings()
  {
    return this.mLocationSettings;
  }
  
  public LoginHelper getLoginHelper()
  {
    return this.mLoginHelper;
  }
  
  public MessageBuffer getMessageBuffer()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mMessageBuffer == null) {
        this.mMessageBuffer = new MessageBuffer();
      }
      MessageBuffer localMessageBuffer = this.mMessageBuffer;
      return localMessageBuffer;
    }
  }
  
  public NetworkInformation getNetworkInfo()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mNetworkInfo == null) {
        this.mNetworkInfo = new NetworkInformation((TelephonyManager)this.mContext.getSystemService("phone"), (ConnectivityManager)this.mContext.getSystemService("connectivity"));
      }
      NetworkInformation localNetworkInformation = this.mNetworkInfo;
      return localNetworkInformation;
    }
  }
  
  public NowOptInSettings getNowOptInSettings()
  {
    return this.mNowOptInSettings;
  }
  
  public PendingIntentFactory getPendingIntentFactory()
  {
    return this.mPendingIntentFactory;
  }
  
  public PinholeParamsBuilderImpl getPinholeParamsBuilder()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mPinholeParamsBuilder == null) {
        this.mPinholeParamsBuilder = new PinholeParamsBuilderImpl(getSearchUrlHelper(), this.mUserAgentHelper);
      }
      PinholeParamsBuilderImpl localPinholeParamsBuilderImpl = this.mPinholeParamsBuilder;
      return localPinholeParamsBuilderImpl;
    }
  }
  
  public PredictiveCardsPreferences getPredictiveCardsPreferences()
  {
    return this.mPredictiveCardsPreferences;
  }
  
  public RlzHelper getRlzHelper()
  {
    return this.mRlzHelper;
  }
  
  public SdchManager getSdchManager()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mSdchManager == null) {
        this.mSdchManager = new SdchManager(new SdchFetcher(getHttpHelper(), getHttpExecutor()), new SdchDictionaryCache(this.mContext, getServices().getAsyncServices().getPooledBackgroundExecutorService(), this.mClock), getClock(), getConfig());
      }
      SdchManager localSdchManager = this.mSdchManager;
      return localSdchManager;
    }
  }
  
  public SearchBoxLogging getSearchBoxLogging()
  {
    return this.mSearchBoxLogging;
  }
  
  public SearchControllerCache getSearchControllerCache()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mSearchControllerCache == null) {
        this.mSearchControllerCache = new SearchControllerCache(this.mServices.getFactory());
      }
      SearchControllerCache localSearchControllerCache = this.mSearchControllerCache;
      return localSearchControllerCache;
    }
  }
  
  public DataSetObservable getSearchHistoryChangedObservable()
  {
    return this.mSearchHistoryChangedObservable;
  }
  
  public SearchSettings getSearchSettings()
  {
    return this.mSettings;
  }
  
  public SearchUrlHelper getSearchUrlHelper()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mUrlHelper == null) {
        this.mUrlHelper = createSearchUrlHelper();
      }
      SearchUrlHelper localSearchUrlHelper = this.mUrlHelper;
      return localSearchUrlHelper;
    }
  }
  
  public ConnectionFactory getSpdyConnectionFactory()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mSpdyConnectionFactory == null) {
        this.mSpdyConnectionFactory = new SpdyConnectionFactory(getHttpHelper());
      }
      SpdyConnectionFactory localSpdyConnectionFactory = this.mSpdyConnectionFactory;
      return localSpdyConnectionFactory;
    }
  }
  
  public UriRewriter getUriRewriter()
  {
    return this.mUriRewriter;
  }
  
  public UserAgentHelper getUserAgentHelper()
  {
    return this.mUserAgentHelper;
  }
  
  public UserInteractionLogger getUserInteractionLogger()
  {
    synchronized (this.mCreationLock)
    {
      if (this.mUserInteractionLogger == null) {
        this.mUserInteractionLogger = createUserInteractionLogger();
      }
      UserInteractionLogger localUserInteractionLogger = this.mUserInteractionLogger;
      return localUserInteractionLogger;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.CoreSearchServicesImpl
 * JD-Core Version:    0.7.0.1
 */