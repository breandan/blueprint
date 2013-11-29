package com.google.android.search.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebView;
import com.google.android.e100.MessageBuffer;
import com.google.android.e100.MessageNotificationReference;
import com.google.android.e100.MessageUtil;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.discoursecontext.Mention;
import com.google.android.search.core.ears.SoundSearchController;
import com.google.android.search.core.ears.SoundSearchController.SoundSearchListener;
import com.google.android.search.core.ears.SoundSearchError;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.LocationSettings.Observer;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.SearchUrlHelper.Builder;
import com.google.android.search.core.prefetch.SearchResult;
import com.google.android.search.core.prefetch.SearchResultCache;
import com.google.android.search.core.prefetch.SearchResultFetcher;
import com.google.android.search.core.sdch.SdchManager;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.DiscoveryState;
import com.google.android.search.core.state.LoggingState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.core.suggest.presenter.SuggestionsClient;
import com.google.android.search.core.suggest.presenter.SuggestionsPresenter;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaTask;
import com.google.android.search.core.util.ForceableLock;
import com.google.android.search.core.util.ForceableLock.Owner;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.core.webview.GsaCommunicationJsHelper;
import com.google.android.search.core.webview.GsaWebViewController;
import com.google.android.search.core.webview.WebViewControllerClient;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.RecognitionUi;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.service.ClientConfig;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExpiringSum;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.IntentStarter.ResultCallback;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SimpleIntentStarter;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.embedded.Greco3Container;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.embedded.PumpkinTagger;
import com.google.android.speech.embedded.TaggerResult;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.SoundSearchRecognizeException;
import com.google.android.speech.test.TestPlatformLog;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.Corpora;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.VelvetUpgradeTasks;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.velvet.util.IntentUtils;
import com.google.android.voicesearch.VelvetCardController;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioRouter;
import com.google.android.voicesearch.audio.AudioRouter.AudioRouteListener;
import com.google.android.voicesearch.audio.TtsAudioPlayer;
import com.google.android.voicesearch.audio.TtsAudioPlayer.Callback;
import com.google.android.voicesearch.bluetooth.BluetoothCarListener;
import com.google.android.voicesearch.bluetooth.BluetoothController;
import com.google.android.voicesearch.fragments.VoiceSearchController;
import com.google.android.voicesearch.fragments.VoiceSearchController.Listener;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.fragments.executor.ActionExecutor;
import com.google.android.voicesearch.fragments.executor.ActionExecutorFactory;
import com.google.android.voicesearch.hotword.HotwordDetector;
import com.google.android.voicesearch.hotword.HotwordDetector.HotwordListener;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.logger.EventLoggerService;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilderImpl;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.wireless.voicesearch.proto.CardMetdataProtos.CardMetadata;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchController
  implements SharedPreferences.OnSharedPreferenceChangeListener, LocationSettings.Observer, VelvetEventBus.Observer, ForceableLock.Owner
{
  private static final RecognitionUi NO_OP_UI = new RecognitionUi()
  {
    public void setFinalRecognizedText(CharSequence paramAnonymousCharSequence) {}
    
    public void showRecognitionState(int paramAnonymousInt) {}
    
    public void updateRecognizedText(String paramAnonymousString1, String paramAnonymousString2) {}
  };
  private static final ImmutableSet<String> PREFERENCE_KEYS = ImmutableSet.of("use_google_com", "search_domain", "debug_search_scheme_override", "debug_search_domain_override", "debug_js_injection_enabled", "debug_js_server_address", new String[] { "search_domain_scheme", "search_domain_country_code", "google_account", "signed_out", "personalized_search_bool", "personalized_search", "safe_search", "web_corpora_config", "gservices_overrides", "webview_logged_in_account", "webview_logged_in_domain" });
  private final AdClickHandler mAdClickHandler;
  private final Runnable mAudioFocusSettled = new Runnable()
  {
    public void run()
    {
      SearchController.this.onAudioFocusSettled();
    }
  };
  private final AudioRouter mAudioRouter;
  private VelvetCardController mCardController;
  private ChromePrerenderer mChromePrerenderer;
  private SearchClient mClient;
  private final SearchConfig mConfig;
  private final Context mContext;
  private final ForceableLock mCookiesLock;
  private final DataSetObserver mCookiesLockObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      SearchController.this.checkCookiesAccessAllowed();
    }
  };
  private final CoreSearchServices mCoreServices;
  private final Corpora mCorpora;
  private int mCurrentAudioRoute = 3;
  @Nonnull
  private Query mCurrentVoiceOrSoundSearch;
  private final VelvetEventBus mEventBus;
  private ActionExecutorFactory mExecutorFactory;
  private final VelvetFactory mFactory;
  private final GsaConfigFlags mGsaConfig;
  private HotwordDetector mHotwordDetector;
  private HotwordDetector.HotwordListener mHotwordListener;
  @Nullable
  private ExpiringSum mHotwordStats;
  private boolean mInitialized;
  private final boolean mIsLowRamDevice;
  @Nullable
  private SearchResult mLastShownSearchResultDbg;
  private boolean mListenersRegistered;
  private MessageBuffer mMessageBuffer;
  @Nullable
  private MyPumpkinInitListener mPumpkinInitListener;
  private String mPumpkinLocale;
  private PumpkinTagger mPumpkinTagger;
  private final QueryState mQueryState;
  private SearchResultFetcher mSearchResultFetcher;
  private final SearchSettings mSettings;
  private SoundSearchController mSoundSearch;
  private SearchResultCache mSrpCache;
  private boolean mStarted;
  private SuggestionsPresenter mSuggestionsPresenter;
  private final ScheduledSingleThreadedExecutor mUiThread;
  private final SearchUrlHelper mUrlHelper;
  private VoiceSearchController mVoiceSearchController;
  private final VoiceSearchServices mVoiceSearchServices;
  private boolean mWasAudioAvailable = true;
  private WebView mWebView;
  private boolean mWebViewActive;
  private GsaWebViewController mWebViewController;
  
  public SearchController(Context paramContext, CoreSearchServices paramCoreSearchServices, VoiceSearchServices paramVoiceSearchServices, VelvetFactory paramVelvetFactory, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mContext = paramContext;
    this.mCoreServices = paramCoreSearchServices;
    this.mVoiceSearchServices = paramVoiceSearchServices;
    this.mAudioRouter = this.mVoiceSearchServices.getAudioRouter();
    this.mFactory = paramVelvetFactory;
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mIsLowRamDevice = Util.isLowRamDevice(this.mContext);
    this.mSettings = paramCoreSearchServices.getSearchSettings();
    this.mCorpora = paramCoreSearchServices.getCorpora();
    this.mConfig = paramCoreSearchServices.getConfig();
    this.mGsaConfig = paramCoreSearchServices.getGsaConfigFlags();
    this.mUrlHelper = paramCoreSearchServices.getSearchUrlHelper();
    this.mCookiesLock = paramCoreSearchServices.getCookiesLock();
    this.mEventBus = paramVelvetFactory.createVelvetEventBus();
    this.mQueryState = this.mEventBus.getQueryState();
    this.mAdClickHandler = paramVelvetFactory.createAdClickHandler(new MyAdClickHandlerClient(null));
    this.mCurrentVoiceOrSoundSearch = Query.EMPTY;
    this.mMessageBuffer = this.mCoreServices.getMessageBuffer();
    VelvetUpgradeTasks.maybeExecuteUpgradeTasks(this.mContext, this.mCoreServices.getSearchSettings(), this.mCoreServices.getConfig(), this.mCoreServices.getBackgroundTasks());
    getDataManager().addInitializationCallback(new DataManagerInitCallback(null));
  }
  
  private void checkCookiesAccessAllowed()
  {
    if (haveBadCookies())
    {
      this.mQueryState.setCookiesAccessAllowed(false);
      this.mCookiesLock.release(this);
      this.mCoreServices.getBackgroundTasks().forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
      return;
    }
    if (this.mCookiesLock.tryObtain(this))
    {
      this.mQueryState.setCookiesAccessAllowed(true);
      return;
    }
    this.mQueryState.setCookiesAccessAllowed(false);
  }
  
  private void clearCachesAndForceReload()
  {
    if (this.mSuggestionsPresenter != null) {
      this.mSuggestionsPresenter.updateSuggestions();
    }
    if (this.mSrpCache != null) {
      this.mSrpCache.clear();
    }
    this.mQueryState.forceReloadIfPossible();
  }
  
  private void createNotificationAnnouncement(String paramString)
  {
    if (!Feature.EYES_FREE.isEnabled()) {
      Log.wtf("SearchController", "Notification announcement but e100 not enabled.");
    }
    ((DiscourseContext)this.mCoreServices.getDiscourseContext().get()).mention(new MessageNotificationReference(paramString), new Mention(this.mCoreServices.getClock().currentTimeMillis()));
    this.mQueryState.commit(Query.EMPTY.notificationAnnouncement(MessageUtil.getAnnouncementStringForSender(paramString, ContactLookup.newInstance(this.mContext), this.mContext), paramString));
  }
  
  private void detachInternal(SearchClient paramSearchClient, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mClient == paramSearchClient)
    {
      if (this.mStarted) {
        stopForClient();
      }
      if ((paramBoolean1) && (this.mChromePrerenderer != null)) {
        this.mChromePrerenderer.detach();
      }
      if (paramBoolean2) {
        this.mClient.onDetachForced();
      }
      this.mClient = null;
    }
  }
  
  private void dumpIcingProviderState(String paramString, PrintWriter paramPrintWriter)
  {
    Cursor localCursor = this.mContext.getContentResolver().query(InternalIcingCorporaProvider.DUMP_QUERY_URI, null, paramString, null, null);
    if (localCursor != null) {}
    try
    {
      if (localCursor.moveToNext())
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = localCursor.getString(0);
        DumpUtils.println(paramPrintWriter, arrayOfObject);
      }
      localCursor.close();
      InternalIcingCorporaProvider.UpdateCorporaTask.dump(VelvetServices.get().getPreferenceController().getMainPreferences(), paramString, paramPrintWriter);
      return;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  private void forceRefreshCookies()
  {
    this.mQueryState.setCookiesAccessAllowed(false);
    this.mSettings.setRefreshWebViewCookiesAt(0L);
    if (TextUtils.isEmpty(this.mConfig.getTextSearchTokenType())) {
      this.mCoreServices.getBackgroundTasks().forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
    }
    for (;;)
    {
      clearCachesAndForceReload();
      return;
      this.mCoreServices.getBackgroundTasks().forceRunInterruptingOngoing("refresh_auth_tokens");
    }
  }
  
  private ActionExecutorFactory getActionExecutorFactory()
  {
    if (this.mExecutorFactory == null) {
      this.mExecutorFactory = this.mFactory.createActionExecutorFactory(new DynamicIntentStarter(null));
    }
    return this.mExecutorFactory;
  }
  
  private String getCurrentSpokenLocale()
  {
    return this.mVoiceSearchServices.getSettings().getSpokenLocaleBcp47();
  }
  
  private Greco3DataManager getDataManager()
  {
    return this.mVoiceSearchServices.getGreco3Container().getGreco3DataManager();
  }
  
  private ExpiringSum getHotwordStats()
  {
    if (this.mHotwordStats == null)
    {
      long l1 = this.mConfig.getRememberHotwordSuccessForMillis();
      String str = this.mSettings.getHotwordUsageStatsJson();
      long l2 = Math.max(1000L, l1 / 5L);
      this.mHotwordStats = new ExpiringSum(this.mCoreServices.getClock(), l1, l2, str);
    }
    return this.mHotwordStats;
  }
  
  private int getNumSuccessfulHotwords()
  {
    return getHotwordStats().getTotal();
  }
  
  private RecognitionUi getRecognitionUi()
  {
    if (this.mClient == null) {}
    for (Object localObject = null;; localObject = this.mClient.getSearchPlateUi())
    {
      if (localObject == null) {
        localObject = NO_OP_UI;
      }
      return localObject;
    }
  }
  
  @Nonnull
  private SearchResultFetcher getSearchResultFetcher()
  {
    if (this.mSearchResultFetcher == null) {
      this.mSearchResultFetcher = this.mFactory.createSearchResultFetcher(getSearchResultCache());
    }
    return this.mSearchResultFetcher;
  }
  
  private GsaWebViewController getWebViewController()
  {
    Preconditions.checkState(this.mInitialized);
    if (this.mWebViewController == null)
    {
      this.mWebViewController = this.mFactory.createWebViewController(new MyWebViewControllerClient(null), this.mQueryState);
      this.mWebView = this.mFactory.createResultsWebView();
      this.mWebView.setTag("RESULTS");
      EntryProvider localEntryProvider = VelvetServices.get().getSidekickInjector().getEntryProvider();
      GsaJsEventController localGsaJsEventController = new GsaJsEventController(this.mCoreServices, this, this.mWebViewController, this.mContext.getResources(), localEntryProvider, new DynamicIntentStarter(null), this.mContext.getPackageName(), this.mUiThread, this.mCoreServices.getSearchControllerCache());
      GsaCommunicationJsHelper localGsaCommunicationJsHelper = new GsaCommunicationJsHelper(this.mWebView, this.mConfig, this.mSettings, localGsaJsEventController);
      AgsaExtJavascriptInterface localAgsaExtJavascriptInterface = this.mFactory.createJavascriptExtensionsForSearchResults(new DynamicIntentStarter(null));
      this.mWebView.addJavascriptInterface(localAgsaExtJavascriptInterface, "agsa_ext");
      this.mCoreServices.getUserAgentHelper().onWebViewCreated(this.mWebView);
      this.mWebViewController.setWebViewAndGsaCommunicationJsHelper(this.mWebView, localGsaCommunicationJsHelper);
    }
    return this.mWebViewController;
  }
  
  private void handleNewTextSearch(Query paramQuery)
  {
    SearchResult localSearchResult = getSearchResultFetcher().obtainSearchResult(paramQuery);
    if (localSearchResult != null)
    {
      this.mQueryState.onNetworkSearchResult(paramQuery, localSearchResult);
      return;
    }
    WebSearchConnectionError localWebSearchConnectionError = new WebSearchConnectionError(400, "Page not in cache");
    this.mQueryState.onNetworkLoadError(paramQuery, localWebSearchConnectionError);
  }
  
  private void handleNewVoiceOrSoundSearch(Query paramQuery)
  {
    int i;
    if (paramQuery.isVoiceSearch()) {
      if (!paramQuery.isRestoredState())
      {
        if (!paramQuery.isTriggeredFromHotword()) {
          break label59;
        }
        i = 65;
        EventLogger.recordClientEvent(i);
        this.mCoreServices.getPinholeParamsBuilder().setVoiceSearchQueryForLogging(paramQuery);
        getVoiceSearchController().start(paramQuery, new MyVoiceSearchControllerListener(paramQuery));
      }
    }
    label59:
    label108:
    do
    {
      do
      {
        return;
        i = 20;
        break;
        if (!paramQuery.isTvSearch()) {
          break label108;
        }
      } while (this.mSoundSearch != null);
      this.mSoundSearch = this.mVoiceSearchServices.createSoundSearchController();
      this.mSoundSearch.start(new MySoundSearchListener(paramQuery), paramQuery);
      return;
      if (!paramQuery.isMusicSearch()) {
        break label177;
      }
      if (!this.mConfig.getSoundSearchEnabled()) {
        break label161;
      }
    } while (this.mSoundSearch != null);
    this.mSoundSearch = this.mVoiceSearchServices.createSoundSearchController();
    this.mSoundSearch.start(new MySoundSearchListener(paramQuery), paramQuery);
    return;
    label161:
    this.mQueryState.onNetworkLoadError(paramQuery, new SoundSearchUnavailableError());
    return;
    label177:
    VelvetStrictMode.logW("SearchController", "Unrecognized query type from takeQueryToCommitToMajel");
  }
  
  private boolean isCurrentSpokenLocaleSet()
  {
    return this.mVoiceSearchServices.getSettings().isSpokenLocaleBcp47Set();
  }
  
  private void maybeCancelOrStopCurrentVoiceOrSoundSearch()
  {
    boolean bool1 = true;
    if (this.mCurrentVoiceOrSoundSearch == Query.EMPTY) {}
    label132:
    label175:
    do
    {
      return;
      if (this.mQueryState.shouldCancel(this.mCurrentVoiceOrSoundSearch))
      {
        if ((this.mCurrentVoiceOrSoundSearch.isMusicSearch()) || (this.mCurrentVoiceOrSoundSearch.isTvSearch()))
        {
          if (this.mSoundSearch != null)
          {
            this.mSoundSearch.cancel();
            this.mSoundSearch = null;
          }
          this.mEventBus.getTtsState().requestStop();
        }
        while (!this.mCurrentVoiceOrSoundSearch.isVoiceSearch())
        {
          this.mCurrentVoiceOrSoundSearch = Query.EMPTY;
          return;
        }
        boolean bool2;
        VoiceSearchController localVoiceSearchController;
        if (((!this.mQueryState.getCommittedQuery().isMusicSearch()) && (!this.mQueryState.getCommittedQuery().isTvSearch())) || (this.mQueryState.isCommittedQuerySoundSearchWithResult()))
        {
          bool2 = bool1;
          localVoiceSearchController = getVoiceSearchController();
          if (this.mQueryState.isQueryInBackStack(this.mCurrentVoiceOrSoundSearch)) {
            break label175;
          }
        }
        for (;;)
        {
          localVoiceSearchController.cancel(bool2, bool1);
          this.mEventBus.getTtsState().requestStop();
          break;
          bool2 = false;
          break label132;
          bool1 = false;
        }
      }
    } while (!this.mQueryState.takeStopListening(this.mCurrentVoiceOrSoundSearch));
    getVoiceSearchController().stopListening();
  }
  
  private void maybeExecuteAction(VoiceAction paramVoiceAction)
  {
    ActionExecutor localActionExecutor = getActionExecutorFactory().getActionExecutor(paramVoiceAction);
    if ((paramVoiceAction.canExecute()) && (localActionExecutor.canExecute(paramVoiceAction))) {
      localActionExecutor.execute(paramVoiceAction);
    }
  }
  
  private void maybeInit()
  {
    if ((this.mQueryState.shouldInitSearchController()) && (!this.mInitialized))
    {
      this.mInitialized = true;
      new CorporaLoadObserver(this.mCorpora, this.mQueryState).start();
      getSuggestionsPresenter().initialize();
      this.mCoreServices.getSdchManager().initCache();
      maybeCreatePumpkinTagger();
    }
    if (!this.mListenersRegistered)
    {
      this.mListenersRegistered = true;
      this.mCoreServices.getLocationSettings().addUseLocationObserver(this);
      this.mCoreServices.getSearchSettings().registerOnSharedPreferenceChangeListener(this);
      this.mVoiceSearchServices.getBluetoothController().addListener(new BluetoothCarListener(this.mQueryState, this.mVoiceSearchServices.getBluetoothCarClassifier()), this.mVoiceSearchServices.getMainThreadExecutor());
    }
  }
  
  private void maybeRefreshSearchHistory()
  {
    if (this.mQueryState.takeNewlyLoadedWebQuery() != null)
    {
      this.mCoreServices.getBackgroundTasks().forceRun("refresh_search_history", this.mGsaConfig.getRefreshSearchHistoryDelay());
      Query localQuery = this.mQueryState.getCommittedQuery();
      if ((localQuery.isVoiceSearch()) && (localQuery.isTriggeredFromHotword())) {
        recordSuccessfulHotwordUse();
      }
    }
  }
  
  private void maybeStartTtsPlayback()
  {
    TtsState localTtsState = getEventBus().getTtsState();
    int i = localTtsState.takePlay();
    if (i == 2)
    {
      localTtsAudioPlayer = this.mVoiceSearchServices.getTtsAudioPlayer();
      localTtsAudioPlayer.setAudio(localTtsState.getNetworkTts());
      localTtsAudioPlayer.requestPlayback(new TtsAudioPlayer.Callback()
      {
        public void onComplete()
        {
          SearchController.this.setTtsDone();
        }
      });
    }
    while (i != 1)
    {
      TtsAudioPlayer localTtsAudioPlayer;
      return;
    }
    String str1 = localTtsState.getLocalTts();
    final boolean bool = this.mQueryState.getCommittedQuery().isNotificationAnnouncement();
    if (bool) {}
    for (final String str2 = this.mQueryState.getCommittedQuery().getExtras().getString("notification-sender");; str2 = null)
    {
      this.mVoiceSearchServices.getLocalTtsManager().enqueue(str1, new Runnable()
      {
        public void run()
        {
          SearchController.this.setTtsDone();
          if (bool) {
            SearchController.this.mMessageBuffer.onSenderAnnounced(str2);
          }
        }
      });
      return;
    }
  }
  
  private void maybeStopTtsPlayback()
  {
    if (this.mEventBus.getTtsState().takeStop())
    {
      this.mVoiceSearchServices.getLocalTtsManager().stop();
      this.mVoiceSearchServices.getTtsAudioPlayer().stopAudioPlayback();
    }
  }
  
  private void onAudioFocusSettled()
  {
    this.mQueryState.setAudioFocusSettling(false);
  }
  
  private void openUrlInSystem(UriRequest paramUriRequest)
  {
    if (isAttached()) {
      getClient().openUrlInSystem(paramUriRequest);
    }
  }
  
  private void recordSuccessfulHotwordUse()
  {
    getHotwordStats().increment();
    this.mSettings.setHotwordUsageStatsJson(getHotwordStats().getJson());
  }
  
  private void setAudioFocusSettling()
  {
    this.mQueryState.setAudioFocusSettling(true);
    this.mUiThread.cancelExecute(this.mAudioFocusSettled);
    this.mUiThread.executeDelayed(this.mAudioFocusSettled, 500L);
  }
  
  private void setTtsDone()
  {
    getEventBus().getTtsState().setDone(this.mQueryState.getCommittedQuery());
  }
  
  private void startForClient()
  {
    getSuggestionsPresenter().start(this.mClient, this.mEventBus);
    ActionState localActionState;
    VoiceAction localVoiceAction;
    ActionData localActionData;
    if (Feature.DISCOURSE_CONTEXT.isEnabled())
    {
      localActionState = this.mEventBus.getActionState();
      localVoiceAction = localActionState.getTopMostVoiceAction();
      if (localVoiceAction != null)
      {
        localActionData = localActionState.getActionData();
        if ((localActionData == null) || (!localActionData.hasActionV2(0))) {
          break label109;
        }
      }
    }
    label109:
    for (ActionV2Protos.ActionV2 localActionV2 = localActionData.getActionV2(0);; localActionV2 = null)
    {
      ((DiscourseContext)this.mCoreServices.getDiscourseContext().get()).mentionVoiceActionExperimental(localActionV2, localVoiceAction, localActionState.getCardDecision(localVoiceAction), localActionState.getActionData().getEventId());
      this.mEventBus.addObserver(this);
      return;
    }
  }
  
  private void stopForClient()
  {
    if (this.mSuggestionsPresenter != null) {
      this.mSuggestionsPresenter.stop(this.mClient, this.mEventBus);
    }
    if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
      ((DiscourseContext)this.mCoreServices.getDiscourseContext().get()).clearCurrentActionCancel();
    }
    this.mEventBus.removeObserver(this);
    this.mEventBus.getUiState().forceReportShouldShowKeyboardChanged();
  }
  
  private void updateAudioRouting()
  {
    Object localObject = null;
    Query localQuery;
    int i;
    if ((this.mStarted) && (!this.mQueryState.isPaused(this.mQueryState.get())) && (this.mQueryState.shouldKeepAudioOpen()))
    {
      localQuery = this.mQueryState.getCommittedQuery();
      if ((localQuery.isMusicSearch()) || (localQuery.isTvSearch()) || (localQuery.isTriggeredFromWiredHeadset())) {
        i = 2;
      }
    }
    for (;;)
    {
      this.mCurrentAudioRoute = i;
      this.mAudioRouter.updateRoute(i, (AudioRouter.AudioRouteListener)localObject);
      do
      {
        return;
        if (localQuery.isTriggeredFromBluetoothHandsfree())
        {
          localObject = new RouteListener(localQuery);
          i = 0;
          break;
        }
      } while (localQuery.isFollowOn());
      localObject = new RouteListener(localQuery);
      i = 1;
      continue;
      i = 3;
      int j = this.mCurrentAudioRoute;
      localObject = null;
      if (j != i)
      {
        setAudioFocusSettling();
        localObject = null;
      }
    }
  }
  
  private void updateHotwordDetector()
  {
    int i = 1;
    if (maybeInitializeGreco3DataManager()) {}
    while (!isCurrentSpokenLocaleSet()) {
      return;
    }
    int j = getDataManager().getHotwordQuality(getCurrentSpokenLocale());
    int k = this.mQueryState.shouldListenForHotword(j, this.mIsLowRamDevice);
    if (k == 0)
    {
      if (this.mHotwordDetector == null)
      {
        this.mHotwordDetector = this.mVoiceSearchServices.getHotwordDetector();
        this.mHotwordListener = new MyHotwordListener();
      }
      this.mHotwordDetector.start(this.mHotwordListener, this.mClient.shouldUseMusicHotworder());
      return;
    }
    if (this.mHotwordDetector != null) {
      this.mHotwordDetector.stop();
    }
    QueryState localQueryState = this.mQueryState;
    if (k == i) {}
    for (;;)
    {
      localQueryState.onListeningForHotwordChanged(i, false);
      return;
      i = 0;
    }
  }
  
  public void attach(SearchClient paramSearchClient)
  {
    Preconditions.checkNotNull(paramSearchClient);
    if (paramSearchClient != this.mClient)
    {
      if (this.mClient != null)
      {
        boolean bool1 = paramSearchClient.supportWickedFast();
        boolean bool2 = false;
        if (!bool1)
        {
          boolean bool3 = this.mClient.supportWickedFast();
          bool2 = false;
          if (bool3) {
            bool2 = true;
          }
        }
        detachInternal(this.mClient, bool2, true);
      }
      this.mClient = paramSearchClient;
      this.mQueryState.setMinimumHotwordQuality(this.mClient.getMinimumHotwordQuality());
      if (this.mStarted) {
        startForClient();
      }
    }
  }
  
  public void attachStopped(SearchClient paramSearchClient)
  {
    if (this.mClient != null) {
      stop(this.mClient);
    }
    attach(paramSearchClient);
  }
  
  public void detach(SearchClient paramSearchClient)
  {
    detachInternal(paramSearchClient, true, false);
  }
  
  public void dispose()
  {
    if (!this.mStarted) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (this.mClient != null) {
        detachInternal(this.mClient, true, true);
      }
      if (this.mListenersRegistered)
      {
        this.mCoreServices.getLocationSettings().removeUseLocationObserver(this);
        this.mCoreServices.getSearchSettings().unregisterOnSharedPreferenceChangeListener(this);
      }
      if (this.mInitialized) {
        this.mSuggestionsPresenter.dispose();
      }
      if (this.mWebViewController != null) {
        this.mWebViewController.dispose();
      }
      return;
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    VelvetServices.get().dump(paramString, paramPrintWriter);
    paramPrintWriter.println();
    DumpUtils.println(paramPrintWriter, new Object[] { paramString, "SearchController state:" });
    String str = paramString + "  ";
    this.mConfig.dump(str, paramPrintWriter);
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = str;
    arrayOfObject[1] = "mWebViewActive: ";
    arrayOfObject[2] = Boolean.valueOf(this.mWebViewActive);
    DumpUtils.println(paramPrintWriter, arrayOfObject);
    this.mEventBus.dump(str, paramPrintWriter);
    if (this.mWebViewController != null) {
      this.mWebViewController.dump(str, paramPrintWriter);
    }
    paramPrintWriter.println();
    if (this.mSrpCache != null) {
      this.mSrpCache.dump(str, paramPrintWriter);
    }
    for (;;)
    {
      if (this.mSuggestionsPresenter != null) {
        this.mSuggestionsPresenter.dump(str, paramPrintWriter);
      }
      dumpIcingProviderState(str, paramPrintWriter);
      paramPrintWriter.println();
      return;
      DumpUtils.println(paramPrintWriter, new Object[] { str, "mSrpCache: null" });
    }
  }
  
  public void dumpLastSearchResultsHtml(PrintWriter paramPrintWriter)
  {
    if (this.mLastShownSearchResultDbg != null) {
      this.mLastShownSearchResultDbg.dumpContent(paramPrintWriter);
    }
  }
  
  public void forceReleaseLock()
  {
    this.mQueryState.setCookiesAccessAllowed(false);
  }
  
  public VelvetCardController getCardController()
  {
    if (this.mCardController == null) {
      this.mCardController = this.mFactory.createVelvetCardController(this.mEventBus);
    }
    return this.mCardController;
  }
  
  public ChromePrerenderer getChromePrerenderer()
  {
    if (this.mChromePrerenderer == null) {
      this.mChromePrerenderer = new ChromePrerenderer(this.mContext, this.mConfig, VelvetServices.get().getAsyncServices().getPooledBackgroundExecutorService());
    }
    return this.mChromePrerenderer;
  }
  
  SearchClient getClient()
  {
    Preconditions.checkNotNull(this.mClient);
    return this.mClient;
  }
  
  public VelvetEventBus getEventBus()
  {
    return this.mEventBus;
  }
  
  public String getHotwordPrompt()
  {
    return getDataManager().getHotwordPrompt(getCurrentSpokenLocale());
  }
  
  @Nonnull
  SearchResultCache getSearchResultCache()
  {
    if (this.mSrpCache == null) {
      this.mSrpCache = this.mFactory.createSearchResultCache();
    }
    return this.mSrpCache;
  }
  
  @Nonnull
  public SuggestionsPresenter getSuggestionsPresenter()
  {
    if (this.mSuggestionsPresenter == null) {
      this.mSuggestionsPresenter = this.mFactory.createSuggestionsPresenter(new Supplier()
      {
        public SearchResultFetcher get()
        {
          return SearchController.this.getSearchResultFetcher();
        }
      });
    }
    return this.mSuggestionsPresenter;
  }
  
  public VoiceSearchController getVoiceSearchController()
  {
    if (this.mVoiceSearchController == null) {
      this.mVoiceSearchController = this.mFactory.createVoiceSearchController();
    }
    return this.mVoiceSearchController;
  }
  
  @Nullable
  public View getWebView(boolean paramBoolean)
  {
    if (paramBoolean) {
      getWebViewController();
    }
    return this.mWebView;
  }
  
  public boolean hasHotwordPrompt()
  {
    return getDataManager().hasHotwordPrompt(getCurrentSpokenLocale());
  }
  
  boolean haveBadCookies()
  {
    boolean bool1 = TextUtils.isEmpty(this.mConfig.getTextSearchTokenType());
    boolean bool2 = false;
    if (bool1)
    {
      String str = this.mSettings.getWebViewLoggedInAccount();
      boolean bool3 = TextUtils.isEmpty(str);
      bool2 = false;
      if (!bool3)
      {
        boolean bool4 = TextUtils.equals(str, this.mSettings.getGoogleAccountToUse());
        bool2 = false;
        if (!bool4) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public boolean isAttached()
  {
    return this.mClient != null;
  }
  
  public boolean isAttached(SearchClient paramSearchClient)
  {
    return this.mClient == paramSearchClient;
  }
  
  final boolean isStarted()
  {
    return this.mStarted;
  }
  
  public boolean isWebViewActive()
  {
    return this.mWebViewActive;
  }
  
  void maybeCreatePumpkinTagger()
  {
    if (!this.mConfig.isEmbeddedParserEnabled()) {}
    do
    {
      String str;
      boolean bool;
      do
      {
        return;
        str = getCurrentSpokenLocale();
        bool = this.mVoiceSearchServices.canCreatePumpkinTagger(this.mCoreServices.getGsaConfigFlags(), str);
        if (!bool) {
          break;
        }
        if ((!TextUtils.equals(str, this.mPumpkinLocale)) && (bool))
        {
          this.mPumpkinInitListener = null;
          this.mQueryState.onPumpkinDestroyed();
          this.mPumpkinTagger = this.mVoiceSearchServices.createPumpkinTagger(str);
          this.mPumpkinLocale = str;
          return;
        }
      } while ((this.mPumpkinTagger != null) || (!bool));
      this.mPumpkinInitListener = null;
      this.mQueryState.onPumpkinDestroyed();
      this.mPumpkinTagger = this.mVoiceSearchServices.createPumpkinTagger(str);
      return;
    } while (this.mPumpkinTagger == null);
    this.mQueryState.onPumpkinDestroyed();
    this.mQueryState.onPumpkinInitialized(false);
    this.mPumpkinInitListener = null;
    this.mPumpkinTagger = null;
  }
  
  void maybeHandleUrlInQueryString()
  {
    if (this.mCoreServices.getGsaConfigFlags().logLaunchUrlWithGen_204())
    {
      Uri localUri = this.mQueryState.getUrlInCommittedQuery();
      if (localUri != null)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
        Query localQuery = this.mQueryState.getCommittedQuery().externalActivitySentinel(IntentUtils.createBundleForRelaunchableExternalActivity(localIntent));
        this.mCoreServices.getSearchBoxLogging().logUrlQueryWithGen_204(this.mUrlHelper, this.mQueryState.get().getQueryString(), localUri.toString());
        this.mQueryState.switchQuery(this.mQueryState.getCommittedQuery(), localQuery);
      }
    }
  }
  
  void maybeInitPumpkinTagger()
  {
    if ((this.mPumpkinTagger != null) && (this.mQueryState.canPumpkinHandleCurrentCommit()) && (this.mPumpkinInitListener == null))
    {
      this.mPumpkinInitListener = new MyPumpkinInitListener(null);
      this.mPumpkinTagger.maybeInit(this.mPumpkinInitListener);
    }
  }
  
  public boolean maybeInitializeGreco3DataManager()
  {
    if (getDataManager().isInitialized()) {
      return false;
    }
    getDataManager().initialize();
    return true;
  }
  
  public boolean onBackPressed()
  {
    if (this.mWebViewController != null) {
      return this.mWebViewController.onBackPressed();
    }
    return false;
  }
  
  public void onMessageReceived(String paramString1, String paramString2)
  {
    MessageBuffer localMessageBuffer = this.mMessageBuffer;
    if (!this.mQueryState.shouldKeepAudioOpen()) {}
    for (boolean bool = true;; bool = false)
    {
      if (localMessageBuffer.onMessageReceived(paramString1, paramString2, bool)) {
        createNotificationAnnouncement(paramString1);
      }
      return;
    }
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if ((PREFERENCE_KEYS.contains(paramString)) && (this.mInitialized)) {
      clearCachesAndForceReload();
    }
    do
    {
      do
      {
        return;
        if ((!paramString.startsWith("enable_corpus_")) || (!this.mInitialized)) {
          break;
        }
      } while (this.mSuggestionsPresenter == null);
      this.mSuggestionsPresenter.updateSuggestions();
      return;
    } while (!"spoken-language-bcp-47".equals(paramString));
    this.mQueryState.onSpokenLocaleChanged();
    this.mCardController = null;
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    maybeHandleUrlInQueryString();
    maybeInit();
    maybeCancelOrStopCurrentVoiceOrSoundSearch();
    maybeStopTtsPlayback();
    updateHotwordDetector();
    updateAudioRouting();
    Query localQuery1;
    if (!this.mQueryState.shouldKeepAudioOpen())
    {
      if (!this.mWasAudioAvailable)
      {
        String str2 = this.mMessageBuffer.onAudioAvailable();
        if (!TextUtils.isEmpty(str2)) {
          createNotificationAnnouncement(str2);
        }
      }
      this.mWasAudioAvailable = true;
      localQuery1 = this.mQueryState.takeNewNetworkQuery();
      if (localQuery1 != null)
      {
        if (!localQuery1.isTextOrVoiceWebSearchWithQueryChars()) {
          break label703;
        }
        handleNewTextSearch(localQuery1);
      }
    }
    for (;;)
    {
      maybeInitPumpkinTagger();
      Query localQuery2 = this.mQueryState.takeQueryToCommitToPumpkin();
      if (localQuery2 != null) {
        this.mPumpkinTagger.handleQuery(localQuery2, new MyPumpkinSuccessListener(localQuery2), new MyPumpkinNoMatchListener(localQuery2), new MyPumpkinDisambiguationFailureListener(localQuery2));
      }
      ActionState localActionState = this.mEventBus.getActionState();
      if ((localActionState.takeClearDiscourseContext()) && (Feature.DISCOURSE_CONTEXT.isEnabled())) {
        ((DiscourseContext)this.mCoreServices.getDiscourseContext().get()).clearCurrentActionCancel();
      }
      Query localQuery3 = this.mQueryState.getCommittedQuery();
      if (localQuery3.isEyesFree())
      {
        if (paramEvent.hasActionChanged())
        {
          VoiceAction localVoiceAction2 = localActionState.getTopMostVoiceAction();
          if (localVoiceAction2 != null)
          {
            CardDecision localCardDecision = localActionState.getCardDecision(localVoiceAction2);
            if ((localCardDecision != null) && (localCardDecision.shouldPlayTts())) {
              getEventBus().getTtsState().requestPlay(localVoiceAction2, localCardDecision.getVocalizedPrompt());
            }
          }
        }
        if ((localQuery3.isNotificationAnnouncement()) && (!getEventBus().getTtsState().isDone()))
        {
          String str1 = localQuery3.getExtras().getString("notification-message");
          getEventBus().getTtsState().requestPlay(null, str1);
        }
        if (paramEvent.hasTtsChanged()) {
          maybeStartTtsPlayback();
        }
        if (getEventBus().getTtsState().noMoreTtsToPlay())
        {
          List localList = localActionState.getVoiceActions();
          if ((localList != null) && (!localList.isEmpty())) {
            maybeExecuteAction((VoiceAction)localList.get(0));
          }
        }
      }
      if (paramEvent.hasActionChanged())
      {
        VoiceAction localVoiceAction1 = localActionState.takeActionToExecute();
        if (localVoiceAction1 != null) {
          maybeExecuteAction(localVoiceAction1);
        }
      }
      if (isAttached())
      {
        ActionData localActionData3 = localActionState.takeActionDataToHandle();
        if (localActionData3 != null)
        {
          this.mQueryState.reportLatencyEvent(38);
          getCardController().handleAction(this.mQueryState.getCommittedQuery(), localActionData3);
        }
      }
      ActionData localActionData1 = localActionState.getActionData();
      if (localActionData1 != null)
      {
        int i = this.mEventBus.getLoggingState().takeGwsUnloggedEvents(localActionData1);
        CardMetdataProtos.CardMetadata localCardMetadata = localActionData1.getCardMetadata();
        if ((i != 0) && (localCardMetadata != null))
        {
          SearchResult localSearchResult2 = this.mQueryState.getCurrentSearchResultForLogging();
          if (localSearchResult2 != null) {
            this.mCoreServices.getSearchBoxLogging().logEventsToGws(i, localCardMetadata.getLoggingUrls(), null, localSearchResult2.getSpeechRequestId(), this.mUrlHelper, -1L);
          }
        }
      }
      ActionData localActionData2 = this.mQueryState.takeUnusedNetworkActionToLog();
      if (localActionData2 != null) {
        EventLogger.recordClientEvent(108, Integer.valueOf(localActionData2.getActionTypeLog((DiscourseContext)this.mCoreServices.getDiscourseContext().get())));
      }
      if (this.mInitialized)
      {
        SearchResult localSearchResult1 = this.mQueryState.takeReadySearchResult();
        if (localSearchResult1 != null)
        {
          if ((this.mClient != null) && (this.mClient.supportWickedFast())) {
            getChromePrerenderer().attach();
          }
          getWebViewController().showSearchResult(localSearchResult1.getSrpQuery(), localSearchResult1);
          this.mLastShownSearchResultDbg = localSearchResult1;
        }
      }
      maybeRefreshSearchHistory();
      Query localQuery4 = this.mQueryState.takeLaunchExternalActivity();
      if (localQuery4 != null)
      {
        SearchClient localSearchClient = getClient();
        Intent[] arrayOfIntent = new Intent[1];
        arrayOfIntent[0] = IntentUtils.getExternalActivityLaunchIntent(localQuery4.getExtras());
        localSearchClient.startActivity(arrayOfIntent);
      }
      return;
      this.mWasAudioAvailable = false;
      break;
      label703:
      this.mCurrentVoiceOrSoundSearch = localQuery1;
      handleNewVoiceOrSoundSearch(localQuery1);
    }
  }
  
  public void onStoppedListeningForMessages()
  {
    this.mMessageBuffer.clear();
  }
  
  public void onTrimMemory()
  {
    if (this.mWebView != null) {
      this.mWebView.freeMemory();
    }
    if (this.mSrpCache != null) {
      this.mSrpCache.onTrimMemory();
    }
  }
  
  public void onUseLocationChanged(boolean paramBoolean)
  {
    if (this.mInitialized) {
      clearCachesAndForceReload();
    }
  }
  
  public void setBrowserDimensions(Point paramPoint)
  {
    this.mUrlHelper.setBrowserDimensions(paramPoint);
  }
  
  public boolean shouldShowHotwordHint()
  {
    if (getNumSuccessfulHotwords() >= this.mConfig.getSuccessfulHotwordUsesToHideHint()) {
      return false;
    }
    long l1 = this.mSettings.getFirstHotwordHintShownAtTime();
    long l2 = this.mCoreServices.getClock().currentTimeMillis();
    if (l1 == 0L) {
      this.mSettings.setFirstHotwordHintShownAtTime(this.mCoreServices.getClock().currentTimeMillis());
    }
    while (l2 - l1 <= this.mConfig.getShowHotwordHintForMillis()) {
      return true;
    }
    return false;
  }
  
  public void start(SearchClient paramSearchClient)
  {
    attach(paramSearchClient);
    if (!this.mStarted)
    {
      EventLoggerService.cancelSendEvents(this.mContext);
      getSuggestionsPresenter().connectToIcing();
      if (this.mInitialized) {
        maybeCreatePumpkinTagger();
      }
      if (this.mWebView != null) {
        this.mWebView.onResume();
      }
      TestPlatformLog.setEnabled(this.mConfig.isTestPlatformLoggingEnabled());
      startForClient();
      this.mCookiesLock.registerObserver(this.mCookiesLockObserver);
      checkCookiesAccessAllowed();
      this.mStarted = true;
      updateAudioRouting();
    }
  }
  
  public void stop(SearchClient paramSearchClient)
  {
    if ((this.mStarted) && (paramSearchClient == this.mClient))
    {
      this.mCookiesLock.unregisterObserver(this.mCookiesLockObserver);
      this.mCookiesLock.release(this);
      if (this.mSuggestionsPresenter != null) {
        this.mSuggestionsPresenter.disconnectFromIcing();
      }
      if (this.mQueryState.adClickInProgress()) {
        this.mQueryState.onAdClickComplete();
      }
      if (this.mWebView != null) {
        this.mWebView.onPause();
      }
      this.mQueryState.setHotwordDetectionEnabled(false);
      if (this.mVoiceSearchController != null)
      {
        getRecognitionUi().showRecognitionState(7);
        this.mVoiceSearchController.cancel(true, true);
        this.mVoiceSearchServices.getOfflineActionsManager().maybeScheduleGrammarCompilation();
      }
      if (this.mSoundSearch != null)
      {
        getRecognitionUi().showRecognitionState(2);
        this.mSoundSearch.cancel();
        this.mSoundSearch = null;
      }
      this.mEventBus.getTtsState().requestStop();
      this.mCoreServices.getHttpHelper().scheduleCacheFlush();
      stopForClient();
      this.mQueryState.onRecognitionPaused(this.mCurrentVoiceOrSoundSearch);
      EventLoggerService.scheduleSendEvents(this.mContext);
      this.mStarted = false;
      updateAudioRouting();
    }
  }
  
  private static class CorporaLoadObserver
    extends DataSetObserver
  {
    private final Corpora mCorpora;
    private final QueryState mQueryState;
    
    CorporaLoadObserver(Corpora paramCorpora, QueryState paramQueryState)
    {
      this.mCorpora = paramCorpora;
      this.mQueryState = paramQueryState;
    }
    
    public void onChanged()
    {
      if (this.mCorpora.areWebCorporaLoaded())
      {
        this.mQueryState.onWebCorporaAvailable();
        this.mCorpora.unregisterObserver(this);
      }
    }
    
    public void start()
    {
      this.mCorpora.registerObserver(this);
      this.mCorpora.initializeDelayed();
      onChanged();
    }
  }
  
  private class DataManagerInitCallback
    implements Runnable
  {
    private DataManagerInitCallback() {}
    
    public void run()
    {
      SearchController.this.mQueryState.onGreco3DataManagerInitialized();
    }
  }
  
  private class DynamicIntentStarter
    implements IntentStarter
  {
    private DynamicIntentStarter() {}
    
    public boolean resolveIntent(Intent paramIntent)
    {
      return SearchController.this.getClient().resolveIntent(paramIntent);
    }
    
    public boolean startActivity(Intent... paramVarArgs)
    {
      if (SearchController.this.isAttached()) {
        return SearchController.this.getClient().startActivity(paramVarArgs);
      }
      return false;
    }
    
    public boolean startActivityForResult(Intent paramIntent, IntentStarter.ResultCallback paramResultCallback)
    {
      return false;
    }
  }
  
  private class MyAdClickHandlerClient
    implements AdClickHandler.Client
  {
    private MyAdClickHandlerClient() {}
    
    public void onAdClickRedirectError()
    {
      SearchController.this.mQueryState.onAdClickComplete();
    }
    
    public void onReceivedAdClickRedirect(Uri paramUri)
    {
      if (SearchController.this.mQueryState.adClickInProgress()) {
        SearchController.this.openUrlInSystem(new UriRequest(paramUri));
      }
    }
  }
  
  class MyHotwordListener
    implements HotwordDetector.HotwordListener
  {
    MyHotwordListener() {}
    
    public void onHotword(long paramLong)
    {
      Log.i("SearchController", "#onHotword");
      SearchController.this.mQueryState.commit(Query.EMPTY.voiceSearchFromHotword(SearchController.this.mClient.getClientConfig().isEyesFree()).withSearchBoxStats(SearchController.this.mClient.getSearchBoxStats()));
    }
    
    public void onHotwordDetectorNotStarted()
    {
      Log.i("SearchController", "#onHotwordDetectorNotStarted");
      SearchController.this.mQueryState.onListeningForHotwordChanged(false, false);
    }
    
    public void onHotwordDetectorStarted()
    {
      Log.i("SearchController", "#onHotwordDetectorStarted");
      SearchController.this.mQueryState.onListeningForHotwordChanged(true, true);
    }
    
    public void onHotwordDetectorStopped(boolean paramBoolean)
    {
      Log.i("SearchController", "#onHotwordDetectorStopped");
      QueryState localQueryState = SearchController.this.mQueryState;
      if (!paramBoolean) {}
      for (boolean bool = true;; bool = false)
      {
        localQueryState.onListeningForHotwordChanged(bool, false);
        return;
      }
    }
    
    public void onMusicDetected()
    {
      SearchController.this.mQueryState.onMusicDetected();
    }
  }
  
  private class MyPumpkinDisambiguationFailureListener
    extends SearchController.MyPumpkinResultsListener
  {
    public MyPumpkinDisambiguationFailureListener(Query paramQuery)
    {
      super(paramQuery);
    }
    
    public void onResult(TaggerResult paramTaggerResult)
    {
      SearchController.this.mQueryState.onPumpkinActionData(this.mQuery, ActionData.NONE);
      EventLogger.recordClientEvent(137);
    }
  }
  
  private class MyPumpkinInitListener
    implements SimpleCallback<Boolean>
  {
    private MyPumpkinInitListener() {}
    
    public void onResult(Boolean paramBoolean)
    {
      if (SearchController.this.mPumpkinInitListener == this) {
        SearchController.this.mQueryState.onPumpkinInitialized(paramBoolean.booleanValue());
      }
    }
  }
  
  private class MyPumpkinNoMatchListener
    extends SearchController.MyPumpkinResultsListener
  {
    public MyPumpkinNoMatchListener(Query paramQuery)
    {
      super(paramQuery);
    }
    
    public void onResult(TaggerResult paramTaggerResult)
    {
      SearchController.this.mQueryState.onPumpkinActionData(this.mQuery, ActionData.NONE);
      EventLogger.recordClientEvent(98);
    }
  }
  
  abstract class MyPumpkinResultsListener
    implements SimpleCallback<TaggerResult>
  {
    protected final Query mQuery;
    
    protected MyPumpkinResultsListener(Query paramQuery)
    {
      this.mQuery = paramQuery;
    }
  }
  
  private class MyPumpkinSuccessListener
    extends SearchController.MyPumpkinResultsListener
  {
    public MyPumpkinSuccessListener(Query paramQuery)
    {
      super(paramQuery);
    }
    
    public void onResult(TaggerResult paramTaggerResult)
    {
      SearchController.this.mQueryState.onPumpkinActionData(this.mQuery, ActionData.fromPumpkinTaggerResult(paramTaggerResult));
    }
  }
  
  private class MySoundSearchListener
    implements SoundSearchController.SoundSearchListener
  {
    private final Query mQuery;
    
    public MySoundSearchListener(Query paramQuery)
    {
      this.mQuery = paramQuery;
    }
    
    public void onDone()
    {
      SearchController.this.mQueryState.onVoiceSearchResultsDone(this.mQuery);
    }
    
    public void onListening()
    {
      if (!SearchController.this.mQueryState.isCurrentCommit(this.mQuery)) {
        return;
      }
      TestPlatformLog.log("SPEAK_NOW");
      SearchController.this.getRecognitionUi().showRecognitionState(4);
    }
    
    public void onNoSoundSearchMatch(SoundSearchRecognizeException paramSoundSearchRecognizeException)
    {
      if (!SearchController.this.mQueryState.isCurrentCommit(this.mQuery)) {
        return;
      }
      SearchController.this.getRecognitionUi().showRecognitionState(2);
      SearchController.this.mQueryState.onNetworkLoadError(this.mQuery, new SoundSearchError(paramSoundSearchRecognizeException));
    }
    
    public void onSoundSearchError(SoundSearchRecognizeException paramSoundSearchRecognizeException)
    {
      if (!SearchController.this.mQueryState.isCurrentCommit(this.mQuery)) {
        return;
      }
      SearchController.this.getRecognitionUi().showRecognitionState(2);
      SearchController.this.mQueryState.onNetworkLoadError(this.mQuery, new SoundSearchError(paramSoundSearchRecognizeException));
    }
    
    public void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse)
    {
      if (!SearchController.this.mQueryState.isCurrentCommit(this.mQuery)) {
        return;
      }
      SearchController.this.getRecognitionUi().showRecognitionState(2);
      SearchController.this.mQueryState.onNetworkActionData(this.mQuery, ActionData.fromEarsResponse(paramEarsResultsResponse));
    }
    
    public void onTtsAvailable(byte[] paramArrayOfByte)
    {
      if (!SearchController.this.mQueryState.isCurrentCommit(this.mQuery)) {
        return;
      }
      SearchController.this.mEventBus.getTtsState().onNetworkTtsAvailable(this.mQuery, paramArrayOfByte);
    }
  }
  
  private class MyVoiceSearchControllerListener
    implements VoiceSearchController.Listener
  {
    private final Query mQuery;
    
    public MyVoiceSearchControllerListener(Query paramQuery)
    {
      this.mQuery = paramQuery;
    }
    
    public void onDone()
    {
      SearchController.this.mQueryState.onVoiceSearchResultsDone(this.mQuery);
    }
    
    public void onError(RecognizeException paramRecognizeException, @Nullable String paramString)
    {
      SearchController.this.mQueryState.onNetworkLoadError(this.mQuery, new VoiceSearchError(paramRecognizeException, SearchController.this.mVoiceSearchServices.getVoiceSearchAudioStore(), paramString));
    }
    
    public void onInitializing()
    {
      SearchController.this.getRecognitionUi().showRecognitionState(1);
    }
    
    public void onMusicDetected()
    {
      SearchController.this.mQueryState.onMusicDetected();
    }
    
    public void onNoMatch(NoMatchRecognizeException paramNoMatchRecognizeException, String paramString)
    {
      SearchController.this.mQueryState.onNetworkLoadError(this.mQuery, new VoiceSearchError(paramNoMatchRecognizeException, SearchController.this.mVoiceSearchServices.getVoiceSearchAudioStore(), paramString));
    }
    
    public void onNoSpeechDetected()
    {
      SearchController.this.mQueryState.onRecognitionPaused(this.mQuery);
      SearchController.this.getRecognitionUi().showRecognitionState(2);
    }
    
    public void onReadyForSpeech()
    {
      SearchController.this.getRecognitionUi().showRecognitionState(4);
      SearchController.this.mEventBus.getDiscoveryState().onReadyForSpeech(this.mQuery);
    }
    
    public void onRecognitionResult(CharSequence paramCharSequence, ImmutableList<CharSequence> paramImmutableList, SearchResult paramSearchResult)
    {
      Preconditions.checkNotNull(paramSearchResult);
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        SearchController.this.getSearchResultCache().add(paramSearchResult);
        Query localQuery = paramSearchResult.getSrpQuery();
        SearchController.this.mQueryState.onTextRecognized(localQuery, paramCharSequence, paramImmutableList);
        SearchController.this.mQueryState.onNetworkSearchResult(localQuery, paramSearchResult);
      }
    }
    
    public void onRecognizing()
    {
      SearchController.this.getRecognitionUi().showRecognitionState(6);
    }
    
    public void onSpeechDetected()
    {
      SearchController.this.getRecognitionUi().showRecognitionState(5);
      SearchController.this.mEventBus.getDiscoveryState().onSpeechDetected(this.mQuery);
    }
    
    public void onTtsAvailable(byte[] paramArrayOfByte)
    {
      SearchController.this.mEventBus.getTtsState().onNetworkTtsAvailable(this.mQuery, paramArrayOfByte);
    }
    
    public void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence)
    {
      SearchController.this.getRecognitionUi().setFinalRecognizedText(paramCharSequence);
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
    {
      SearchController.this.getRecognitionUi().updateRecognizedText(paramString1, paramString2);
    }
  }
  
  private class MyWebViewControllerClient
    implements WebViewControllerClient
  {
    private MyWebViewControllerClient() {}
    
    public void onEndResultsPage(Query paramQuery)
    {
      SearchController.this.mQueryState.resultsPageEnd(paramQuery);
    }
    
    public void onLinkClicked(Uri paramUri1, @Nullable Uri paramUri2)
    {
      SearchController.this.mAdClickHandler.cancel();
      SearchUrlHelper.Builder localBuilder = SearchController.this.mUrlHelper.getAdUrlBuilderForRedirectHandling(paramUri1);
      if (localBuilder != null)
      {
        SearchController.this.mAdClickHandler.onAdClicked(localBuilder);
        SearchController.this.mQueryState.onAdClickStart();
        return;
      }
      Pair localPair = SearchController.this.mUrlHelper.getResultTargetAndLogUrl(paramUri1, paramUri2);
      if (localPair.second != null) {
        SearchController.this.mCoreServices.getSearchBoxLogging().logResultClick((Supplier)localPair.second);
      }
      SearchController.this.openUrlInSystem((UriRequest)localPair.first);
    }
    
    public void onLogoutRedirect()
    {
      SearchController.this.forceRefreshCookies();
    }
    
    public void onNewQuery(Query paramQuery)
    {
      SearchController.this.mQueryState.newQueryFromWebView(paramQuery.fromWebView());
    }
    
    public void onPageError(Query paramQuery, int paramInt, String paramString)
    {
      SearchController.this.mQueryState.resultsPageError(paramQuery, new WebSearchConnectionError(paramInt, paramString));
    }
    
    public void onShowedPrefetchedSrp(Query paramQuery, String paramString)
    {
      
      if (paramQuery.isPrefetch())
      {
        Query localQuery = SearchController.this.mQueryState.getCommittedQuery();
        SearchController.this.getSearchResultCache().notifyQueryFulfilled(localQuery);
        SearchController.this.mCoreServices.getSearchBoxLogging().sendGen204(localQuery, paramString, SearchController.this.mUrlHelper);
      }
      ActionState localActionState = SearchController.this.mEventBus.getActionState();
      CardDecision localCardDecision = localActionState.getCardDecision(localActionState.getTopMostVoiceAction());
      int i = SearchController.this.mEventBus.getLoggingState().takePumpkinUnloggedEvents(localActionState.getActionData());
      if (localCardDecision.shouldAutoExecute()) {}
      for (long l = localCardDecision.getCountDownDurationMs();; l = -1L)
      {
        SearchBoxLogging localSearchBoxLogging = SearchController.this.mCoreServices.getSearchBoxLogging();
        if (localSearchBoxLogging != null) {
          localSearchBoxLogging.logEventsToGws(i, null, paramString, null, SearchController.this.mUrlHelper, l);
        }
        return;
      }
    }
    
    public void onStartResultsPage(Query paramQuery)
    {
      SearchController.this.mQueryState.resultsPageStart(paramQuery);
    }
    
    public void onStateChanged(boolean paramBoolean)
    {
      if (paramBoolean != SearchController.this.mWebViewActive)
      {
        SearchController.access$1002(SearchController.this, paramBoolean);
        SearchController.this.mQueryState.webViewReadyToShowChanged(paramBoolean);
      }
    }
  }
  
  private class RouteListener
    implements AudioRouter.AudioRouteListener
  {
    private final Query mQuery;
    
    public RouteListener(Query paramQuery)
    {
      this.mQuery = paramQuery;
    }
    
    public void onRouteLost()
    {
      Log.i("SearchController", "mStopAudio.onRouteLost");
      SearchController.this.mUiThread.execute(new Runnable()
      {
        public void run()
        {
          SearchController.this.mQueryState.stopListening(SearchController.RouteListener.this.mQuery);
        }
      });
    }
  }
  
  public static abstract interface SearchClient
    extends SuggestionsClient, SimpleIntentStarter
  {
    public abstract ClientConfig getClientConfig();
    
    public abstract int getMinimumHotwordQuality();
    
    public abstract SearchBoxStats getSearchBoxStats();
    
    public abstract SearchPlateUi getSearchPlateUi();
    
    public abstract void onDetachForced();
    
    public abstract void openUrlInSystem(UriRequest paramUriRequest);
    
    public abstract boolean shouldUseMusicHotworder();
    
    public abstract boolean supportWickedFast();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchController
 * JD-Core Version:    0.7.0.1
 */