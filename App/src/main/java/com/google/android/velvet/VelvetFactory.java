package com.google.android.velvet;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.google.android.search.core.AdClickHandler;
import com.google.android.search.core.AdClickHandler.Client;
import com.google.android.search.core.AgsaExtJavascriptInterface;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.GservicesUpdateTask;
import com.google.android.search.core.JavascriptExtensions;
import com.google.android.search.core.JavascriptExtensions.PageEventListener;
import com.google.android.search.core.JavascriptExtensions.TrustPolicy;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.DeletedQueryRemovingSourceWrapper;
import com.google.android.search.core.google.DownloadExperimentConfigTask;
import com.google.android.search.core.google.RefreshAuthTokensTask;
import com.google.android.search.core.google.RefreshSearchDomainAndCookiesTask;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.WebSuggestSource;
import com.google.android.search.core.google.ZeroQueryCachingWebSuggestSource;
import com.google.android.search.core.google.complete.CompleteServerClient;
import com.google.android.search.core.google.complete.GwsSuggestionFetcher;
import com.google.android.search.core.google.complete.GwsSuggestionParser;
import com.google.android.search.core.prefetch.SearchResultCache;
import com.google.android.search.core.prefetch.SearchResultFetcher;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.DiscoveryState;
import com.google.android.search.core.state.LoggingState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.suggest.CachingPromoter;
import com.google.android.search.core.suggest.CorrectionPromoter;
import com.google.android.search.core.suggest.OriginalQueryFilter;
import com.google.android.search.core.suggest.Promoter;
import com.google.android.search.core.suggest.SuggestionLauncher;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.suggest.WebPromoter;
import com.google.android.search.core.suggest.presenter.IcingInitialization;
import com.google.android.search.core.suggest.presenter.SuggestionsPresenter;
import com.google.android.search.core.summons.FirstNonEmptySummonsPromoter;
import com.google.android.search.core.summons.SingleSourcePromoter;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.icing.IcingFactory;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaService;
import com.google.android.search.core.ui.ErrorView;
import com.google.android.search.core.util.GelStartupPrefsWriter;
import com.google.android.search.core.util.LatencyTracker;
import com.google.android.search.core.webview.GsaWebViewController;
import com.google.android.search.core.webview.WebViewControllerClient;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.SuggestionListView;
import com.google.android.search.shared.ui.SuggestionViewFactory;
import com.google.android.search.shared.ui.ViewRecycler;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.NamingDelayedTaskExecutor;
import com.google.android.shared.util.NoOpConsumer;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SimpleIntentStarter;
import com.google.android.sidekick.main.TgPredictiveCardContainer;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.client.PredictiveCardRefreshManager;
import com.google.android.sidekick.shared.client.ViewActionRecorder;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.speech.params.RequestIdGenerator;
import com.google.android.velvet.actions.CardDecisionFactory;
import com.google.android.velvet.presenter.ActionDiscoveryPresenter;
import com.google.android.velvet.presenter.ConnectionErrorPresenter;
import com.google.android.velvet.presenter.ContextHeaderPresenter;
import com.google.android.velvet.presenter.ContextHeaderUi;
import com.google.android.velvet.presenter.FooterPresenter;
import com.google.android.velvet.presenter.FooterUi;
import com.google.android.velvet.presenter.MainContentPresenter;
import com.google.android.velvet.presenter.ResultsPresenter;
import com.google.android.velvet.presenter.SearchPlatePresenter;
import com.google.android.velvet.presenter.SuggestFragmentPresenter;
import com.google.android.velvet.presenter.SummonsPresenter;
import com.google.android.velvet.presenter.VelvetFragmentPresenter;
import com.google.android.velvet.presenter.VelvetPresenter;
import com.google.android.velvet.presenter.VelvetSearchPlateUi;
import com.google.android.velvet.presenter.VelvetUi;
import com.google.android.velvet.presenter.VoiceCorrectionPresenter;
import com.google.android.velvet.presenter.VoicesearchLanguagePresenter;
import com.google.android.velvet.presenter.inappwebpage.InAppWebPageFactory;
import com.google.android.velvet.presenter.inappwebpage.InAppWebPagePresenter;
import com.google.android.velvet.ui.GetGoogleNowView;
import com.google.android.velvet.ui.InAppWebPageActivity;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.velvet.util.AttachedActivityContext;
import com.google.android.voicesearch.CardFactory;
import com.google.android.voicesearch.DialogCardController;
import com.google.android.voicesearch.SearchCardController;
import com.google.android.voicesearch.VelvetCardController;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.fragments.ControllerFactory;
import com.google.android.voicesearch.fragments.VoiceSearchController;
import com.google.android.voicesearch.fragments.executor.ActionExecutorFactory;
import com.google.android.voicesearch.fragments.reminders.ReminderSaver;
import com.google.android.voicesearch.greco3.languagepack.UpdateLanguagePacksTask;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Random;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public class VelvetFactory
{
  private final Context mAppContext;
  private final SuggestionViewFactory mSuggestionViewFactory = new SuggestionViewFactory();
  private final VelvetServices mVelvetServices;
  
  public VelvetFactory(VelvetServices paramVelvetServices, Context paramContext)
  {
    this.mAppContext = paramContext;
    this.mVelvetServices = paramVelvetServices;
  }
  
  private CardDecisionFactory createCardDecisionFactory()
  {
    CoreSearchServices localCoreSearchServices = getCoreServices();
    VoiceSearchServices localVoiceSearchServices = getVoiceSearchServices();
    Settings localSettings = localVoiceSearchServices.getSettings();
    return new CardDecisionFactory(this.mAppContext.getResources(), localCoreSearchServices.getGsaConfigFlags(), localCoreSearchServices.getDiscourseContext(), localSettings.getDefaultActionCountDownMs(), localVoiceSearchServices.isFollowOnEnabled(localCoreSearchServices.getGsaConfigFlags(), localSettings.getSpokenLocaleBcp47()), localVoiceSearchServices.getNetworkInformation());
  }
  
  public static ErrorView createErrorCard(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return (ErrorView)getActivityLayoutInflater(paramVelvetFragmentPresenter.getVelvetPresenter()).inflate(2130968670, paramViewGroup, false);
  }
  
  public static View createNoSummonsMessageView(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return getActivityLayoutInflater(paramVelvetFragmentPresenter.getVelvetPresenter()).inflate(2130968760, paramViewGroup, false);
  }
  
  private ResultsPresenter createResultsPresenter(MainContentView paramMainContentView)
  {
    VoiceSearchServices localVoiceSearchServices = getVoiceSearchServices();
    return new ResultsPresenter(paramMainContentView, localVoiceSearchServices.getTtsAudioPlayer(), localVoiceSearchServices.getLocalTtsManager(), this, new CardFactory(paramMainContentView.getContext()));
  }
  
  private SuggestionListView createSuggestionListView(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup, int paramInt)
  {
    SuggestionListView localSuggestionListView = (SuggestionListView)getActivityLayoutInflater(paramVelvetFragmentPresenter.getVelvetPresenter()).inflate(paramInt, paramViewGroup, false);
    localSuggestionListView.init(this.mSuggestionViewFactory, getGlobalSearchServices().getSuggestionFormatter(), this.mVelvetServices.getIconLoader(), paramVelvetFragmentPresenter.getVelvetPresenter().getSuggestionViewRecycler());
    localSuggestionListView.setSuggestionClickListener(paramVelvetFragmentPresenter.getVelvetPresenter().getSuggestionClickListener());
    return localSuggestionListView;
  }
  
  private CachingPromoter createSuggestionsCachingPromoter(Promoter paramPromoter)
  {
    return new CachingPromoter(paramPromoter, this.mVelvetServices.getGsaConfigFlags().getMaxPromotedSuggestions());
  }
  
  private Promoter createWebPromoter()
  {
    getConfig();
    return new WebPromoter(new OriginalQueryFilter(), getCoreServices().getGsaConfigFlags());
  }
  
  private WebSuggestSource createWebSuggestSource(@Nullable SearchResultFetcher paramSearchResultFetcher)
  {
    VelvetStrictMode.checkStartupAtLeast(6);
    CoreSearchServices localCoreSearchServices = getCoreServices();
    return new ZeroQueryCachingWebSuggestSource(new CompleteServerClient(new GwsSuggestionFetcher(localCoreSearchServices.getConfig(), localCoreSearchServices.getHttpHelper(), localCoreSearchServices.getSearchUrlHelper(), localCoreSearchServices.getLoginHelper(), paramSearchResultFetcher), new GwsSuggestionParser(localCoreSearchServices.getConfig(), localCoreSearchServices.getClock(), this.mAppContext, localCoreSearchServices.getSearchUrlHelper()), this.mAppContext, localCoreSearchServices), localCoreSearchServices.getSearchSettings(), localCoreSearchServices.getLoginHelper(), localCoreSearchServices.getSearchHistoryChangedObservable());
  }
  
  private static LayoutInflater getActivityLayoutInflater(VelvetPresenter paramVelvetPresenter)
  {
    return paramVelvetPresenter.getLayoutInflater();
  }
  
  private ActivityLifecycleObserver getActivityLifecycleObserver()
  {
    return (VelvetApplication)this.mAppContext;
  }
  
  private AsyncServices getAsyncServices()
  {
    return this.mVelvetServices.getAsyncServices();
  }
  
  private SearchConfig getConfig()
  {
    return getCoreServices().getConfig();
  }
  
  private CoreSearchServices getCoreServices()
  {
    return this.mVelvetServices.getCoreServices();
  }
  
  private GlobalSearchServices getGlobalSearchServices()
  {
    return this.mVelvetServices.getGlobalSearchServices();
  }
  
  private GsaConfigFlags getGsaConfigFlags()
  {
    return getCoreServices().getGsaConfigFlags();
  }
  
  private LocationOracle getLocationOracle()
  {
    return this.mVelvetServices.getLocationOracle();
  }
  
  private GsaPreferenceController getPreferenceController()
  {
    return this.mVelvetServices.getPreferenceController();
  }
  
  private LayoutInflater getRetainedLayoutInflater()
  {
    return (LayoutInflater)this.mAppContext.getSystemService("layout_inflater");
  }
  
  private SidekickInjector getSidekickInjector()
  {
    return this.mVelvetServices.getSidekickInjector();
  }
  
  private int getVersionCode()
  {
    return VelvetApplication.getVersionCode();
  }
  
  private VoiceSearchServices getVoiceSearchServices()
  {
    return this.mVelvetServices.getVoiceSearchServices();
  }
  
  public ActionDiscoveryPresenter createActionDiscoveryPresenter(MainContentView paramMainContentView)
  {
    return new ActionDiscoveryPresenter(paramMainContentView, new ContextThemeWrapper(paramMainContentView.getContext(), 2131624107), getCoreServices(), getAsyncServices().getUiThreadExecutor());
  }
  
  public ActionExecutorFactory createActionExecutorFactory(IntentStarter paramIntentStarter)
  {
    return new ActionExecutorFactory(this, this.mAppContext, getAsyncServices().getUiThreadExecutor(), getVoiceSearchServices().getExecutorService(), getCoreServices().getLoginHelper(), paramIntentStarter, this.mAppContext.getPackageManager());
  }
  
  public ActionState createActionState(VelvetEventBus paramVelvetEventBus)
  {
    return new ActionState(paramVelvetEventBus);
  }
  
  public AdClickHandler createAdClickHandler(AdClickHandler.Client paramClient)
  {
    return new AdClickHandler(getAsyncServices().getUiThreadExecutor(), getAsyncServices().getNamedUserFacingTaskExecutor("ad-click"), getCoreServices().getHttpHelper(), paramClient);
  }
  
  public Callable<Void> createBackgroundTask(String paramString, boolean paramBoolean)
  {
    if (paramString.equals("refresh_search_history")) {
      new Callable()
      {
        public Void call()
        {
          Log.i("Velvet.VelvetFactory", "refreshing search history.");
          VelvetFactory.this.createRefreshSearchHistorySource().getSuggestions(Query.EMPTY.fromHistoryRefresh(), new NoOpConsumer());
          return null;
        }
      };
    }
    if (paramString.equals("flush_analytics")) {
      new Callable()
      {
        public Void call()
        {
          VelvetFactory.this.getCoreServices().getUserInteractionLogger().flushEvents();
          return null;
        }
      };
    }
    if (paramString.equals("refresh_search_domain_and_cookies")) {
      return new RefreshSearchDomainAndCookiesTask(getCoreServices().getClock(), getCoreServices().getConfig(), getCoreServices().getSearchSettings(), getCoreServices().getLoginHelper(), getCoreServices().getSearchUrlHelper(), getCoreServices().getHttpHelper(), getCoreServices().getCookies(), getCoreServices().getCookiesLock(), getAsyncServices().getUiThreadExecutor(), this, paramBoolean);
    }
    if (paramString.equals("update_gservices_config")) {
      return new GservicesUpdateTask(this.mAppContext, getCoreServices(), getVoiceSearchServices().getSettings(), getVersionCode());
    }
    if (paramString.equals("update_icing_corpora"))
    {
      Log.i("Velvet.VelvetFactory", "refreshing internal icing corpora");
      new Callable()
      {
        public Void call()
        {
          VelvetFactory.this.mAppContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.createPeriodicUpdateIntent(VelvetFactory.this.mAppContext));
          return null;
        }
      };
    }
    if (paramString.equals("send_gsa_home_request")) {
      return new DownloadExperimentConfigTask(getCoreServices().getSearchSettings(), getCoreServices().getSearchUrlHelper(), getCoreServices().getHttpHelper(), this.mVelvetServices.getGsaConfigFlags(), false);
    }
    if (paramString.equals("send_gsa_home_request_then_crash")) {
      return new DownloadExperimentConfigTask(getCoreServices().getSearchSettings(), getCoreServices().getSearchUrlHelper(), getCoreServices().getHttpHelper(), this.mVelvetServices.getGsaConfigFlags(), true);
    }
    if (paramString.equals("delete_local_search_history")) {
      new Callable()
      {
        public Void call()
        {
          VelvetFactory.this.mAppContext.deleteDatabase("qsb-history.db");
          return null;
        }
      };
    }
    if (paramString.equals("update_language_packs"))
    {
      Log.i("Velvet.VelvetFactory", "checking for language pack updates");
      return new UpdateLanguagePacksTask(getVoiceSearchServices().getLanguageUpdateController(), getCoreServices().getNetworkInfo(), getVoiceSearchServices().getSettings(), getGsaConfigFlags());
    }
    if (paramString.equals("refresh_auth_tokens")) {
      return new RefreshAuthTokensTask(getCoreServices().getLoginHelper(), getConfig());
    }
    if (paramString.equals("send_training_answers")) {
      new Callable()
      {
        public Void call()
        {
          VelvetFactory.this.getSidekickInjector().getTrainingQuestionManager().sendAnswersSync();
          return null;
        }
      };
    }
    if (paramString.equals("clear_training_data")) {
      new Callable()
      {
        public Void call()
        {
          VelvetFactory.this.getSidekickInjector().getTrainingQuestionManager().clearDataSync();
          return null;
        }
      };
    }
    if (paramString.equals("sync_gel_prefs")) {
      new Callable()
      {
        public Void call()
        {
          GelStartupPrefsWriter localGelStartupPrefsWriter = VelvetFactory.this.getPreferenceController().getGelStartupPrefs();
          if (!localGelStartupPrefsWriter.contains("GEL.GSAPrefs.now_enabled")) {
            localGelStartupPrefsWriter.commit("GEL.GSAPrefs.now_enabled", VelvetFactory.this.getCoreServices().getNowOptInSettings().isUserOptedIn());
          }
          localGelStartupPrefsWriter.commit("GSAPrefs.first_run_screens_shown", VelvetFactory.this.getCoreServices().getNowOptInSettings().userHasSeenFirstRunScreens());
          return null;
        }
      };
    }
    Preconditions.checkArgument(false, "Can't create task for " + paramString);
    return null;
  }
  
  public SearchCardController createCardController(MainContentPresenter paramMainContentPresenter, VelvetEventBus paramVelvetEventBus, Query paramQuery, ActionData paramActionData)
  {
    return new SearchCardController(this.mAppContext, getVoiceSearchServices().getExecutorService(), getCoreServices().getClock(), getAsyncServices().getUiThreadExecutor(), paramVelvetEventBus, getCoreServices().getDiscourseContext(), createCardDecisionFactory(), paramMainContentPresenter, paramQuery, paramActionData);
  }
  
  public ConnectionErrorPresenter createConnectionErrorPresenter(MainContentView paramMainContentView)
  {
    return new ConnectionErrorPresenter(paramMainContentView);
  }
  
  public ContextHeaderPresenter createContextHeaderPresenter(ContextHeaderUi paramContextHeaderUi)
  {
    return new ContextHeaderPresenter(paramContextHeaderUi);
  }
  
  public ControllerFactory createControllerFactory(IntentStarter paramIntentStarter)
  {
    return new ControllerFactory(this, getVoiceSearchServices().getContactLookup(), this.mAppContext, getVoiceSearchServices().getExecutorService(), getCoreServices().getDeviceCapabilityManager(), getSidekickInjector().getNetworkClient(), getSidekickInjector().getEntryProvider(), getCoreServices().getClock(), createActionExecutorFactory(paramIntentStarter));
  }
  
  public View createCorpusSelector(FooterPresenter paramFooterPresenter, ViewGroup paramViewGroup, Corpus paramCorpus)
  {
    return getActivityLayoutInflater(paramFooterPresenter.getVelvetPresenter()).inflate(paramCorpus.getSelectorLayoutId(), paramViewGroup, false);
  }
  
  public CachingPromoter createCorrectionCachingPromoter()
  {
    return createSuggestionsCachingPromoter(new CorrectionPromoter());
  }
  
  public DialogCardController createDialogCardController()
  {
    return new DialogCardController(this.mAppContext, getVoiceSearchServices(), createCardDecisionFactory(), getCoreServices().getClock(), getCoreServices().getDiscourseContext());
  }
  
  public DiscoveryState createDiscoveryState(VelvetEventBus paramVelvetEventBus)
  {
    return new DiscoveryState(paramVelvetEventBus, getAsyncServices().getUiThreadExecutor(), getConfig(), getCoreServices().getSearchSettings());
  }
  
  public FooterPresenter createFooterPresenter(FooterUi paramFooterUi)
  {
    return new FooterPresenter(getCoreServices().getCorpora(), paramFooterUi);
  }
  
  public GetGoogleNowView createGetGoogleNowView(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return (GetGoogleNowView)getActivityLayoutInflater(paramVelvetFragmentPresenter.getVelvetPresenter()).inflate(2130968703, paramViewGroup, false);
  }
  
  public WebSuggestSource createGoogleExternalSource()
  {
    CoreSearchServices localCoreSearchServices = getCoreServices();
    return new CompleteServerClient(new GwsSuggestionFetcher(localCoreSearchServices.getConfig(), localCoreSearchServices.getHttpHelper(), localCoreSearchServices.getSearchUrlHelper()), new GwsSuggestionParser(localCoreSearchServices.getConfig(), localCoreSearchServices.getClock(), this.mAppContext, localCoreSearchServices.getSearchUrlHelper()), this.mAppContext, localCoreSearchServices);
  }
  
  public WebSuggestSource createGoogleSource(SearchResultFetcher paramSearchResultFetcher)
  {
    return new DeletedQueryRemovingSourceWrapper(createWebSuggestSource((SearchResultFetcher)Preconditions.checkNotNull(paramSearchResultFetcher)), getCoreServices().getConfig(), getCoreServices().getClock());
  }
  
  public InAppWebPagePresenter createInAppWebPagePresenter(InAppWebPageActivity paramInAppWebPageActivity)
  {
    return new InAppWebPageFactory(this, getCoreServices(), getAsyncServices()).createInAppWebPagePresenter(paramInAppWebPageActivity, this.mAppContext);
  }
  
  public AgsaExtJavascriptInterface createJavascriptExtensions(SimpleIntentStarter paramSimpleIntentStarter, JavascriptExtensions.TrustPolicy paramTrustPolicy, JavascriptExtensions.PageEventListener paramPageEventListener)
  {
    return new JavascriptExtensions(this.mAppContext, paramSimpleIntentStarter, getCoreServices().getSearchUrlHelper(), paramTrustPolicy, paramPageEventListener);
  }
  
  public AgsaExtJavascriptInterface createJavascriptExtensionsForSearchResults(SimpleIntentStarter paramSimpleIntentStarter)
  {
    SearchUrlHelper localSearchUrlHelper = getCoreServices().getSearchUrlHelper();
    JavascriptExtensions.TrustPolicy localTrustPolicy = JavascriptExtensions.searchResultsTrustPolicy(localSearchUrlHelper);
    return new JavascriptExtensions(this.mAppContext, paramSimpleIntentStarter, localSearchUrlHelper, localTrustPolicy);
  }
  
  public LoggingState createLoggingState()
  {
    return new LoggingState();
  }
  
  public MainContentPresenter createMainContentPresenter(String paramString, MainContentView paramMainContentView)
  {
    if ("results".equals(paramString)) {
      return createResultsPresenter(paramMainContentView);
    }
    if ("suggest".equals(paramString)) {
      return createSuggestPresenter(paramMainContentView);
    }
    if ("summons".equals(paramString)) {
      return createSummonsPresenter(paramMainContentView);
    }
    if ("error".equals(paramString)) {
      return createConnectionErrorPresenter(paramMainContentView);
    }
    if ("actiondiscovery".equals(paramString)) {
      return createActionDiscoveryPresenter(paramMainContentView);
    }
    if ("voicesearchlang".equals(paramString)) {
      return createVoicesearchLanguagePresenter(paramMainContentView);
    }
    if ("voicecorrection".equals(paramString)) {
      return createVoiceCorrectionPresenter(paramMainContentView);
    }
    Preconditions.checkArgument(false, "Don't know how to create presenter " + paramString);
    return null;
  }
  
  public View createMoreCorporaSelector(FooterPresenter paramFooterPresenter, ViewGroup paramViewGroup)
  {
    return getActivityLayoutInflater(paramFooterPresenter.getVelvetPresenter()).inflate(2130968645, paramViewGroup, false);
  }
  
  public WebView createOffscreenWebView()
  {
    return new WebView(this.mAppContext);
  }
  
  public VelvetPresenter createPresenter(VelvetUi paramVelvetUi)
  {
    return new VelvetPresenter(this.mAppContext, paramVelvetUi, getCoreServices(), getAsyncServices(), getGlobalSearchServices(), this, getActivityLifecycleObserver(), getLocationOracle(), DebugFeatures.getInstance());
  }
  
  public QueryState createQueryState(VelvetEventBus paramVelvetEventBus)
  {
    return new QueryState(paramVelvetEventBus, getConfig(), getVoiceSearchServices().getSettings(), getCoreServices().getClock(), new Random(), getCoreServices().getSearchUrlHelper(), getCoreServices().getSearchBoxLogging(), new LatencyTracker(getCoreServices().getClock()), getCoreServices().getNetworkInfo());
  }
  
  public WebSuggestSource createRefreshSearchHistorySource()
  {
    return createWebSuggestSource(null);
  }
  
  public ReminderSaver createReminderSaver()
  {
    return new ReminderSaver(getCoreServices().getHttpHelper(), getCoreServices().getSearchUrlHelper(), getVoiceSearchServices().getExecutorService(), getSidekickInjector().getDataBackendVersionStore(), getCoreServices().getLoginHelper(), this.mVelvetServices.getGsaConfigFlags());
  }
  
  public WebView createResultsWebView()
  {
    AttachedActivityContext localAttachedActivityContext = new AttachedActivityContext(this.mAppContext);
    WebView localWebView = (WebView)getRetainedLayoutInflater().cloneInContext(localAttachedActivityContext).inflate(2130968814, null, false);
    localAttachedActivityContext.setView(localWebView);
    return localWebView;
  }
  
  public SearchController createSearchController()
  {
    return new SearchController(this.mAppContext, getCoreServices(), getVoiceSearchServices(), this, getAsyncServices().getUiThreadExecutor());
  }
  
  public SearchPlatePresenter createSearchPlatePresenter(VelvetSearchPlateUi paramVelvetSearchPlateUi)
  {
    return new SearchPlatePresenter(paramVelvetSearchPlateUi);
  }
  
  public SearchResultCache createSearchResultCache()
  {
    return new SearchResultCache(getCoreServices().getConfig(), getCoreServices().getSearchUrlHelper());
  }
  
  public SearchResultFetcher createSearchResultFetcher(SearchResultCache paramSearchResultCache)
  {
    NamingDelayedTaskExecutor localNamingDelayedTaskExecutor = getCoreServices().getHttpExecutor();
    ScheduledSingleThreadedExecutor localScheduledSingleThreadedExecutor = getAsyncServices().getUiThreadExecutor();
    return new SearchResultFetcher(getConfig(), getCoreServices().getClock(), getCoreServices().getLoginHelper(), getCoreServices().getSearchUrlHelper(), getCoreServices().getHttpHelper(), paramSearchResultCache, localScheduledSingleThreadedExecutor, localNamingDelayedTaskExecutor, RequestIdGenerator.INSTANCE, getCoreServices().getSdchManager());
  }
  
  public CachingPromoter createSingleSourcePromoter(Source paramSource)
  {
    return new CachingPromoter(new SingleSourcePromoter(paramSource), getConfig().getMaxResultsPerSource());
  }
  
  public SuggestFragmentPresenter createSuggestPresenter(MainContentView paramMainContentView)
  {
    ViewActionRecorder localViewActionRecorder = new ViewActionRecorder(this.mAppContext, getCoreServices().getClock(), getSidekickInjector().getExecutedUserActionStore());
    PredictiveCardRefreshManager localPredictiveCardRefreshManager = new PredictiveCardRefreshManager(this.mAppContext, getAsyncServices().getUiThreadExecutor(), getSidekickInjector().getEntryCardViewFactory(), getSidekickInjector().getActivityHelper(), getSidekickInjector().getNowRemoteClient(), localViewActionRecorder, new FifeImageUrlUtil(), this.mVelvetServices.getImageLoader());
    TgPredictiveCardContainer localTgPredictiveCardContainer = new TgPredictiveCardContainer(this.mAppContext, getCoreServices().getUserInteractionLogger(), getSidekickInjector().getNetworkClient(), getSidekickInjector().getEntryProvider(), getCoreServices().getClock(), getSidekickInjector().getDataBackendVersionStore(), getSidekickInjector().getNowConfigurationPreferencesSupplier(), getSidekickInjector().getLocationReportingOptInHelper(), getSidekickInjector().getCalendarDataProvider(), getCoreServices().getLoginHelper(), getGlobalSearchServices().getSearchHistoryHelper(), getVoiceSearchServices().getSpeechLevelSource(), getSidekickInjector().getStaticMapLoader(), getSidekickInjector().getTrainingQuestionManager(), CardRenderingContext.EMPTY_CARD_RENDERING_CONTEXT, this.mVelvetServices.getImageLoader(), this.mVelvetServices.getNonCachingImageLoader(), getSidekickInjector().getVelvetImageGalleryHelper(), getSidekickInjector().getFirstUseCardHandler(), getSidekickInjector().getUndoDismissManager(), getSidekickInjector().getReminderSmartActionUtil());
    SuggestFragmentPresenter localSuggestFragmentPresenter = new SuggestFragmentPresenter(getAsyncServices().getUiThreadExecutor(), getCoreServices().getConfig(), getCoreServices().getSearchBoxLogging(), paramMainContentView, localPredictiveCardRefreshManager, getCoreServices().getNowOptInSettings(), localTgPredictiveCardContainer, getSidekickInjector().getNowRemoteClient());
    localTgPredictiveCardContainer.setTgPresenter(localSuggestFragmentPresenter);
    localPredictiveCardRefreshManager.setPresenter(localSuggestFragmentPresenter);
    return localSuggestFragmentPresenter;
  }
  
  public ViewRecycler createSuggestionViewRecycler()
  {
    return new ViewRecycler(this.mSuggestionViewFactory.getNumSuggestionViewTypes(), getConfig().getSuggestionViewRecycleBinSize());
  }
  
  public SuggestionsController createSuggestionsController()
  {
    return new SuggestionsController(getAsyncServices().getUiThreadExecutor());
  }
  
  public SuggestionLauncher createSuggestionsLauncher(SimpleIntentStarter paramSimpleIntentStarter, QueryState paramQueryState, SuggestionsPresenter paramSuggestionsPresenter)
  {
    return new SuggestionLauncher(this.mAppContext, getConfig(), this.mAppContext.getPackageName(), paramSuggestionsPresenter, getGlobalSearchServices(), paramSimpleIntentStarter, paramQueryState);
  }
  
  public SuggestionsPresenter createSuggestionsPresenter(Supplier<SearchResultFetcher> paramSupplier)
  {
    CoreSearchServices localCoreSearchServices = getCoreServices();
    GlobalSearchServices localGlobalSearchServices = getGlobalSearchServices();
    NamingDelayedTaskExecutor localNamingDelayedTaskExecutor = getAsyncServices().getNamedUserFacingTaskExecutor("suggestions_presenter");
    IcingInitialization localIcingInitialization = new IcingInitialization(localCoreSearchServices, localNamingDelayedTaskExecutor, this.mAppContext);
    return new SuggestionsPresenter(getAsyncServices().getUiThreadExecutor(), localNamingDelayedTaskExecutor, localCoreSearchServices, localGlobalSearchServices, this, localCoreSearchServices.getSearchBoxLogging(), localCoreSearchServices.getClock(), localGlobalSearchServices.getIcingFactory().getConnectionToIcing(), localGlobalSearchServices.getShouldQueryStrategy(), localIcingInitialization, paramSupplier);
  }
  
  public CachingPromoter createSummonsCachingPromoter()
  {
    return new CachingPromoter(new FirstNonEmptySummonsPromoter(), getConfig().getMaxPromotedSummons());
  }
  
  public SuggestionListView createSummonsListView(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return createSuggestionListView(paramVelvetFragmentPresenter, paramViewGroup, 2130968849);
  }
  
  public SuggestionListView createSummonsListViewForSuggest(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return createSuggestionListView(paramVelvetFragmentPresenter, paramViewGroup, 2130968850);
  }
  
  public SummonsPresenter createSummonsPresenter(MainContentView paramMainContentView)
  {
    return new SummonsPresenter(paramMainContentView, getGlobalSearchServices().getSourceRanker(), getAsyncServices().getUiThreadExecutor(), getCoreServices().getSearchBoxLogging(), getConfig().getMaxPromotedSummonsPerSourceInitial(), getConfig().getMaxPromotedSummonsPerSourceIncrease());
  }
  
  public TtsState createTtsState(VelvetEventBus paramVelvetEventBus, Settings paramSettings)
  {
    return new TtsState(paramVelvetEventBus, paramSettings);
  }
  
  public UiState createUiState(VelvetEventBus paramVelvetEventBus)
  {
    return new UiState(paramVelvetEventBus);
  }
  
  public VelvetCardController createVelvetCardController(VelvetEventBus paramVelvetEventBus)
  {
    return new VelvetCardController(getCoreServices(), this.mAppContext, getVoiceSearchServices(), paramVelvetEventBus, getCoreServices().getConfig(), getCoreServices().getDiscourseContext(), createCardDecisionFactory());
  }
  
  public VelvetEventBus createVelvetEventBus()
  {
    return new VelvetEventBus(this, getVoiceSearchServices().getSettings(), getCoreServices().getClock().uptimeMillis());
  }
  
  public VoiceCorrectionPresenter createVoiceCorrectionPresenter(MainContentView paramMainContentView)
  {
    return new VoiceCorrectionPresenter(paramMainContentView);
  }
  
  public View createVoiceCorrectionSuggestionView(VoiceCorrectionPresenter paramVoiceCorrectionPresenter)
  {
    return getActivityLayoutInflater(paramVoiceCorrectionPresenter.getVelvetPresenter()).inflate(2130968912, null, false);
  }
  
  public View createVoiceCorrectionView(VoiceCorrectionPresenter paramVoiceCorrectionPresenter, ViewGroup paramViewGroup)
  {
    return getActivityLayoutInflater(paramVoiceCorrectionPresenter.getVelvetPresenter()).inflate(2130968911, paramViewGroup, false);
  }
  
  public VoiceSearchController createVoiceSearchController()
  {
    return getVoiceSearchServices().createVoiceSearchController(getCoreServices().getClock(), getCoreServices().getSearchUrlHelper());
  }
  
  public VoicesearchLanguagePresenter createVoicesearchLanguagePresenter(MainContentView paramMainContentView)
  {
    return new VoicesearchLanguagePresenter(paramMainContentView);
  }
  
  public View createVoicesearchLanguageView(VoicesearchLanguagePresenter paramVoicesearchLanguagePresenter, ViewGroup paramViewGroup)
  {
    return getActivityLayoutInflater(paramVoicesearchLanguagePresenter.getVelvetPresenter()).inflate(2130968914, paramViewGroup, false);
  }
  
  public View createWebResultsText(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return getActivityLayoutInflater(paramVelvetFragmentPresenter.getVelvetPresenter()).inflate(2130968926, paramViewGroup, false);
  }
  
  public SuggestionListView createWebSuggestionListView(VelvetFragmentPresenter paramVelvetFragmentPresenter, ViewGroup paramViewGroup)
  {
    return createSuggestionListView(paramVelvetFragmentPresenter, paramViewGroup, 2130968929);
  }
  
  public CachingPromoter createWebSuggestionsCachingPromoter()
  {
    return createSuggestionsCachingPromoter(createWebPromoter());
  }
  
  public GsaWebViewController createWebViewController(WebViewControllerClient paramWebViewControllerClient, QueryState paramQueryState)
  {
    return new GsaWebViewController(getConfig(), getCoreServices().getClock(), getCoreServices().getSearchUrlHelper(), getAsyncServices().getUiThreadExecutor(), paramWebViewControllerClient, paramQueryState, getCoreServices().getLocationSettings(), getAsyncServices().getNamedUserFacingTaskExecutor("webview-controller"), getCoreServices().getUserAgentHelper(), getCoreServices().getCookies(), this.mAppContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.VelvetFactory
 * JD-Core Version:    0.7.0.1
 */