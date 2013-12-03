package com.google.android.velvet.presenter;

import android.accounts.Account;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.SearchController.SearchClient;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.core.suggest.SuggestionLauncher;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaService;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.service.ClientConfig;
import com.google.android.search.shared.ui.SuggestionClickListener;
import com.google.android.search.shared.ui.ViewRecycler;
import com.google.android.search.shared.ui.util.SearchFormulationLogging;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.CoScrollContainer.LayoutParams;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.util.Animations;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SendGoogleFeedback;
import com.google.android.sidekick.main.RemindersListActivity;
import com.google.android.sidekick.main.TestLauncherActivity;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationOracle.RunningLock;
import com.google.android.sidekick.main.notifications.NowNotificationManager.NotificationType;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.ActivityLifecycleObserver;
import com.google.android.velvet.Help;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.tg.FirstRunActivity;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.velvet.util.IntentUtils;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VelvetPresenter
        implements SearchController.SearchClient, VelvetEventBus.Observer {
    private final Context mAppContext;
    @Nullable
    private MainContentPresenter mBackPresenter;
    private final Runnable mChangeModeTask = new Runnable() {
        public void run() {
            VelvetPresenter.this.enterPendingMode();
        }
    };
    private ClientConfig mClientConfig = new ClientConfig(4);
    private final SearchConfig mConfig;
    private ContextHeaderPresenter mContextHeaderPresenter;
    private final CoreSearchServices mCoreServices;
    private UiMode mCurrentMode = UiMode.NONE;
    private final DebugFeatures mDebugFeatures;
    private final Runnable mDelayedInitializeTask = new Runnable() {
        public void run() {
            VelvetPresenter.this.initializeDelayed();
        }
    };
    private boolean mDestroyed;
    private VelvetEventBus mEventBus;
    private boolean mFocused;
    private FooterPresenter mFooterPresenter;
    private SearchFormulationLogging mFormulationLogging;
    @Nullable
    private MainContentPresenter mFrontPresenter;
    private final GsaConfigFlags mGsaConfig;
    private final GlobalSearchServices mGss;
    private final Supplier<String> mHelpContextSupplier = new Supplier() {
        public String get() {
            return VelvetPresenter.this.getCurrentHelpContext();
        }
    };
    private String mIntentTypeToLog;
    private final ActivityLifecycleObserver mLifecycleObserver;
    private final LocationOracle mLocationOracle;
    private LocationOracle.RunningLock mLocationOracleLock;
    private final LocationSettings mLocationSettings;
    private final Runnable mLogIdleTask = new Runnable() {
        public void run() {
            VelvetPresenter.this.logIdle();
        }
    };
    private final LoginHelper mLoginHelper;
    private final UiModeManager mModeManager;
    private Intent mNewIntent;
    private final NowOptInSettings mNowOptInSettings;
    private UiMode mPendingMode = UiMode.NONE;
    private QueryState mQueryState;
    private boolean mRestoredInstance;
    private boolean mResumed;
    private final SearchBoxLogging mSearchBoxLogging;
    private SearchController mSearchController;
    private SearchPlatePresenter mSearchPlatePresenter;
    private final SearchSettings mSettings;
    private boolean mStarted;
    private SuggestionClickListener mSuggestionClickListener;
    private SuggestionLauncher mSuggestionLauncher;
    private ViewRecycler mSuggestionViewRecycler;
    private final SuggestionsController mSuggestionsController;
    private final VelvetUi mUi;
    private final ScheduledSingleThreadedExecutor mUiThread;
    private final Runnable mUpdateMainContentTask = new Runnable() {
        public void run() {
            VelvetPresenter.this.updateMainContent();
        }
    };
    private final SearchUrlHelper mUrlHelper;
    private final UserInteractionLogger mUserInteractionLogger;
    private final VelvetFactory mVelvetFactory;
    @Nullable
    private View mWebView;

    public VelvetPresenter(Context paramContext, VelvetUi paramVelvetUi, CoreSearchServices paramCoreSearchServices, AsyncServices paramAsyncServices, GlobalSearchServices paramGlobalSearchServices, VelvetFactory paramVelvetFactory, ActivityLifecycleObserver paramActivityLifecycleObserver, LocationOracle paramLocationOracle, DebugFeatures paramDebugFeatures) {
        this.mCoreServices = paramCoreSearchServices;
        this.mAppContext = paramContext;
        this.mUi = paramVelvetUi;
        this.mVelvetFactory = paramVelvetFactory;
        this.mUiThread = paramAsyncServices.getUiThreadExecutor();
        this.mUrlHelper = paramCoreSearchServices.getSearchUrlHelper();
        this.mLocationSettings = paramCoreSearchServices.getLocationSettings();
        this.mSearchBoxLogging = paramCoreSearchServices.getSearchBoxLogging();
        this.mLoginHelper = paramCoreSearchServices.getLoginHelper();
        this.mGss = paramGlobalSearchServices;
        this.mSettings = paramCoreSearchServices.getSearchSettings();
        this.mNowOptInSettings = paramCoreSearchServices.getNowOptInSettings();
        this.mConfig = paramCoreSearchServices.getConfig();
        this.mGsaConfig = paramCoreSearchServices.getGsaConfigFlags();
        this.mDebugFeatures = paramDebugFeatures;
        this.mSuggestionsController = this.mVelvetFactory.createSuggestionsController();
        this.mLifecycleObserver = paramActivityLifecycleObserver;
        this.mUserInteractionLogger = paramCoreSearchServices.getUserInteractionLogger();
        this.mModeManager = new UiModeManager();
        this.mLocationOracle = paramLocationOracle;
    }

    private void addFeedbackMenuItem(Menu paramMenu) {
        paramMenu.add(2131363570).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                Context localContext = VelvetPresenter.this.mAppContext;
                if (VelvetPresenter.this.mCurrentMode.isPredictiveMode()) {
                }
                for (Object localObject = VelvetPresenter.this.mUi.getScrollingContainer(); ; localObject = VelvetPresenter.this.getActivity().getWindow().getDecorView().getRootView()) {
                    SendGoogleFeedback.launchGoogleFeedback(localContext, (View) localObject);
                    VelvetPresenter.this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "SEND_FEEDBACK");
                    if ((VelvetPresenter.this.mModeManager.shouldUsePredictiveInMode(VelvetPresenter.this.mCurrentMode, VelvetPresenter.this.mQueryState.isZeroQuery())) && (VelvetPresenter.this.isNowEnabled()) && (VelvetPresenter.this.mQueryState.isZeroQuery())) {
                        VelvetPresenter.this.doUserRefresh(true);
                    }
                    return true;
                }
            }
        });
    }

    private void cancelIdleTasks() {
        this.mUiThread.cancelExecute(this.mDelayedInitializeTask);
        this.mUiThread.cancelExecute(this.mLogIdleTask);
    }

    private void cancelModeChangeTasks() {
        this.mUiThread.cancelExecute(this.mChangeModeTask);
    }

    private void clearMainContent() {
        updateBackPresenter(null, null);
        updateFrontPresenter(null, null);
    }

    private void createFormulationLogging() {
        String str1 = this.mSearchBoxLogging.getClientId();
        String str2 = this.mSearchBoxLogging.getSource();
        Clock localClock = this.mCoreServices.getClock();
        Query localQuery = this.mQueryState.get();
        if (UiMode.PREDICTIVE == this.mCurrentMode) {
        }
        for (boolean bool = true; ; bool = false) {
            this.mFormulationLogging = new SearchFormulationLogging(str1, str2, localClock, localQuery, bool);
            return;
        }
    }

    private void doUserRefresh(boolean paramBoolean) {
        if (this.mFrontPresenter != null) {
            this.mFrontPresenter.doUserRefresh(paramBoolean);
        }
        if (this.mBackPresenter != null) {
            this.mBackPresenter.doUserRefresh(paramBoolean);
        }
    }

    private boolean enterMode(UiMode paramUiMode) {
        Preconditions.checkState(this.mUiThread.isThisThread());
        Preconditions.checkNotNull(paramUiMode);
        cancelModeChangeTasks();
        this.mPendingMode = paramUiMode;
        if (paramUiMode == this.mCurrentMode) {
            return false;
        }
        if ((UiMode.SUGGEST == this.mPendingMode) || (UiMode.RESULTS_SUGGEST == this.mPendingMode)) {
            createFormulationLogging();
        }
        if (this.mResumed) {
            boolean bool1 = this.mDestroyed;
            boolean bool2 = false;
            if (!bool1) {
                bool2 = true;
            }
            Preconditions.checkState(bool2);
            this.mUiThread.execute(this.mChangeModeTask);
        }
        return true;
    }

    private void enterPendingMode() {
        Preconditions.checkNotNull(this.mPendingMode);
        if (this.mPendingMode == UiMode.NONE) {
            this.mUi.finish();
        }
        UiMode localUiMode = this.mCurrentMode;
        this.mCurrentMode = this.mPendingMode;
        updateMode(localUiMode);
    }

    private void initializeDelayed() {
        VelvetStrictMode.onStartupPoint(6);
        EventLogger.recordBreakdownEvent(40);
        DebugFeatures.setDebugLevel();
        if (!this.mDebugFeatures.dogfoodDebugEnabled()) {
        }
        for (; ; ) {
            VelvetServices localVelvetServices = VelvetServices.get();
            final GsaPreferenceController localGsaPreferenceController = localVelvetServices.getPreferenceController();
            localGsaPreferenceController.delayWrites();
            this.mUiThread.executeDelayed(new Runnable() {
                public void run() {
                    localGsaPreferenceController.allowWrites();
                }
            }, 2000L);
            this.mModeManager.setReadyToShowSuggest();
            if (this.mCurrentMode == UiMode.PREDICTIVE) {
                this.mModeManager.setReadyToShowPredictive();
            }
            updateMainContent();
            if ((this.mCurrentMode != UiMode.PREDICTIVE) && (!this.mDestroyed)) {
                this.mModeManager.setReadyToShowPredictive();
                this.mUiThread.execute(this.mUpdateMainContentTask);
            }
            this.mCoreServices.getBackgroundTasks().notifyUiLaunched();
            this.mCoreServices.getBackgroundTasks().maybeStartTasks();
            localVelvetServices.maybeRegisterSidekickAlarms();
            this.mModeManager.setStartupComplete();
            this.mQueryState.onStartupComplete();
            EventLogger.recordBreakdownEvent(28);
            VelvetStrictMode.onStartupPoint(7);
            return;
            if (!this.mConfig.getHideDogfoodIndicator()) {
                this.mUi.showDogfoodIndicator();
            }
        }
    }

    private void logCurrentMode() {
        if (this.mCurrentMode != UiMode.NONE) {
            this.mUserInteractionLogger.logView(this.mCurrentMode.name());
        }
    }

    private void logIdle() {
        EventLogger.recordBreakdownEvent(32, this.mIntentTypeToLog);
        this.mIntentTypeToLog = null;
    }

    private void logStartIntent(Intent paramIntent) {
        String str1 = "UNKNOWN";
        String str2;
        if (!TextUtils.isEmpty(paramIntent.getAction())) {
            str2 = paramIntent.getAction();
            if (!str2.equals("android.intent.action.ASSIST")) {
                break label268;
            }
            if (!IntentUtils.isLaunchFromFirstRunActivity(paramIntent)) {
                break label52;
            }
            str1 = "FIRST_RUN";
        }
        for (; ; ) {
            this.mUserInteractionLogger.logAnalyticsAction("START", str1);
            return;
            label52:
            NowNotificationManager.NotificationType localNotificationType = NowNotificationManager.NotificationType.typeFromIntent(paramIntent);
            if (paramIntent.hasExtra("notificationEntriesKey")) {
                Iterator localIterator = ProtoUtils.getEntriesFromIntent(paramIntent, "notificationEntriesKey").iterator();
                while (localIterator.hasNext()) {
                    Sidekick.Entry localEntry2 = (Sidekick.Entry) localIterator.next();
                    this.mUserInteractionLogger.logMetricsAction("NOTIFICATION_CLICK", localEntry2, null);
                }
            }
            if (localNotificationType != null) {
                str1 = localNotificationType.name();
            } else if (paramIntent.hasExtra("assist_intent_source")) {
                if (paramIntent.getIntExtra("assist_intent_source", -1) == 1) {
                    str1 = "PREDICTIVE_WIDGET";
                    Sidekick.Entry localEntry1 = ProtoUtils.getEntryFromIntent(paramIntent, "target_entry");
                    if (localEntry1 != null) {
                        EntryAdapterFactory localEntryAdapterFactory = VelvetServices.get().getSidekickInjector().getEntryCardViewFactory();
                        EntryCardViewAdapter localEntryCardViewAdapter = (EntryCardViewAdapter) localEntryAdapterFactory.create(localEntry1);
                        if (localEntryCardViewAdapter == null) {
                            Sidekick.EntryTreeNode localEntryTreeNode = ProtoUtils.getEntryTreeNodeFromIntent(paramIntent, "target_group_entry_tree");
                            if (localEntryTreeNode != null) {
                                localEntryCardViewAdapter = (EntryCardViewAdapter) localEntryAdapterFactory.createForGroup(localEntryTreeNode);
                            }
                        }
                        if (localEntryCardViewAdapter != null) {
                            this.mUserInteractionLogger.logUiActionOnEntryAdapter("WIDGET_PRESS", localEntryCardViewAdapter);
                        }
                    }
                } else {
                    str1 = "UNKNOWN_ASSIST_SOURCE";
                }
            } else {
                str1 = "ASSIST_GESTURE";
                continue;
                label268:
                if ((str2.equals("android.intent.action.MAIN")) || (str2.equals("com.google.android.googlequicksearchbox.GOOGLE_ICON"))) {
                    str1 = "LAUNCHER";
                } else if (str2.equals("android.intent.action.WEB_SEARCH")) {
                    str1 = "SYSTEM_WEB_SEARCH";
                } else if (str2.equals("android.search.action.GLOBAL_SEARCH")) {
                    str1 = "SEARCH_WIDGET";
                } else if (str2.equals("android.intent.action.SEARCH_LONG_PRESS")) {
                    str1 = "SEARCH_LONG_PRESS";
                } else if (str2.equals("android.speech.action.WEB_SEARCH")) {
                    str1 = "VOICE_SEARCH";
                } else if (str2.equals("android.intent.action.VOICE_ASSIST")) {
                    str1 = "VOICE_ASSIST";
                } else if (str2.equals("android.intent.action.SEND")) {
                    str1 = "SHARE_INTENT";
                } else if (str2.equals("com.google.android.googlequicksearchbox.GOOGLE_SEARCH")) {
                    str1 = "GOOGLE_SEARCH_INTENT";
                }
            }
        }
    }

    private void maybeBindWebView(boolean paramBoolean) {
        if (this.mWebView == null) {
            this.mWebView = this.mSearchController.getWebView(paramBoolean);
            if (this.mWebView != null) {
                if (this.mWebView.getLayoutParams() == null) {
                    this.mWebView.setLayoutParams(this.mUi.getScrollingContainer().generateOffscreenLayoutParams());
                }
                this.mUi.getScrollingContainer().addView(this.mWebView);
            }
        }
    }

    private void maybeCreateFormulationLogging() {
        if (this.mFormulationLogging == null) {
            createFormulationLogging();
        }
    }

    private void maybeReattachToSearchController() {
        if (this.mStarted) {
            this.mSearchController.start(this);
            return;
        }
        this.mSearchController.attachStopped(this);
    }

    private boolean maybeShowMarinerFirstRunScreens(Intent paramIntent) {
        Account localAccount = this.mLoginHelper.getAccount();
        boolean bool = this.mNowOptInSettings.isAccountOptedIn(localAccount);
        int i = 0;
        if (!bool) {
            int j = paramIntent.getIntExtra("assist_intent_source", -1);
            i = 0;
            if (j == 1) {
                i = 1;
            }
        }
        if ((!this.mNowOptInSettings.userHasSeenFirstRunScreens()) && (!IntentUtils.isLaunchFromFirstRunActivity(paramIntent)) && (!IntentUtils.isVoiceSearchIntent(paramIntent)) && (!IntentUtils.isSoundSearchIntent(paramIntent)) && (!IntentUtils.shouldDisableMarinerOptIn(paramIntent))) {
            i = 1;
        }
        if (i != 0) {
            Intent localIntent = new Intent(this.mAppContext, FirstRunActivity.class);
            localIntent.addFlags(268468224);
            this.mUi.startActivity(localIntent);
            return true;
        }
        return false;
    }

    private MainContentPresenter setMainPresenter(MainContentPresenter paramMainContentPresenter, boolean paramBoolean) {
        if (paramBoolean) {
            this.mFrontPresenter = paramMainContentPresenter;
            return paramMainContentPresenter;
        }
        this.mBackPresenter = paramMainContentPresenter;
        return paramMainContentPresenter;
    }

    private void setSourceParams(Intent paramIntent) {
        String str1 = this.mSearchBoxLogging.getClientId();
        String str2 = IntentUtils.getSourceParam(paramIntent, this.mGsaConfig.getSearchSourceParam());
        Query localQuery = IntentUtils.getResumeVelvetNewQuery(paramIntent);
        if (localQuery == null) {
            localQuery = IntentUtils.getResumeVelvetQuery(paramIntent);
        }
        if (localQuery != null) {
            SearchBoxStats localSearchBoxStats = localQuery.getSearchBoxStats();
            if (localSearchBoxStats != null) {
                str1 = localSearchBoxStats.getClientId();
                str2 = localSearchBoxStats.getSource();
            }
        }
        this.mSearchBoxLogging.setFallbackClientIdAndSource(str1, str2);
    }

    private void setupFromIntent(Intent paramIntent) {
        Log.i("Velvet.Presenter", "setupFromIntent(" + paramIntent + ")");
        logStartIntent(paramIntent);
        cancelModeChangeTasks();
        this.mPendingMode = UiMode.NONE;
        this.mCurrentMode = UiMode.NONE;
        maybeReattachToSearchController();
        if (IntentUtils.isResumeVelvetIntent(paramIntent)) {
            localQuery2 = IntentUtils.getResumeVelvetNewQuery(paramIntent);
            if (localQuery2 != null) {
                localBundle = paramIntent.getExtras();
                if (IntentUtils.containsSafeExternalActivity(this.mAppContext, localBundle)) {
                    localQuery4 = this.mQueryState.get().externalActivitySentinel(localBundle);
                    this.mQueryState.replaceOrPushExternalQueryAndCommit(localQuery4, localQuery2);
                }
            }
        }
        while (maybeShowMarinerFirstRunScreens(paramIntent)) {
            Query localQuery2;
            Bundle localBundle;
            Query localQuery4;
            return;
            this.mQueryState.commit(localQuery2);
            return;
            Log.i("Velvet.Presenter", "Resuming with current state.");
            Query localQuery3 = IntentUtils.getResumeVelvetQuery(paramIntent);
            ActionData localActionData = IntentUtils.getResumeVelvetAction(paramIntent);
            this.mQueryState.onResumeVelvet(localQuery3, localActionData);
            return;
        }
        this.mQueryState.reset();
        Query localQuery1 = getQueryFromIntent(paramIntent, this.mQueryState.get());
        if (shouldCommitQueryFromIntent(paramIntent)) {
            this.mQueryState.commit(localQuery1);
            if ((localQuery1.isSentinel()) && (localQuery1.getSentinelData() == UiMode.SUGGEST)) {
                this.mSearchController.getSuggestionsPresenter().updateSuggestions();
            }
        }
        for (; ; ) {
            clearMainContent();
            return;
            this.mQueryState.commit(Query.EMPTY.sentinel(UiMode.SUGGEST, null));
            this.mQueryState.set(localQuery1);
        }
    }

    private boolean shouldCommitQueryFromIntent(Intent paramIntent) {
        return !IntentUtils.isGlobalSearchIntent(paramIntent);
    }

    private void unbindWebView() {
        if (this.mWebView != null) {
            ((CoScrollContainer.LayoutParams) this.mWebView.getLayoutParams()).setParams(5);
            this.mUi.getScrollingContainer().removeView(this.mWebView);
            this.mWebView.setFocusable(false);
            this.mWebView.setFocusableInTouchMode(false);
            this.mWebView = null;
        }
    }

    private void updateBackPresenter(@Nullable String paramString, @Nullable Bundle paramBundle) {
        updateMainPresenter(false, paramString, paramBundle);
    }

    private void updateFooter(UiMode paramUiMode) {
        boolean bool1;
        boolean bool2;
        boolean bool3;
        int i;
        if (this.mSuggestionsController.getSummonsFetchState() == 3) {
            bool1 = true;
            bool2 = this.mModeManager.shouldShowCorpusBarInMode(this.mCurrentMode, paramUiMode, bool1);
            bool3 = this.mModeManager.shouldShowTgFooterButton(this.mCurrentMode, this.mQueryState.isZeroQuery(), isNowEnabled());
            if (bool3) {
                break label78;
            }
            i = 8;
        }
        for (; ; ) {
            this.mFooterPresenter.updateUi(bool2, bool3, i);
            return;
            bool1 = false;
            break;
            label78:
            boolean bool4 = VelvetServices.get().getSidekickInjector().areRemindersEnabled();
            i = 0;
            if (!bool4) {
                i = 4;
            }
        }
    }

    private void updateFooterStickiness(boolean paramBoolean) {
        UiState localUiState = this.mEventBus.getUiState();
        int i = this.mModeManager.getFooterStickiness(this.mCurrentMode, localUiState.shouldSuppressCorpora());
        this.mUi.setFooterStickiness(i, paramBoolean);
        if (((i == 0) || (i == 4)) && (localUiState.takeShowCorporaRequest())) {
            this.mUi.showFooter();
        }
    }

    private void updateFrontPresenter(@Nullable String paramString, @Nullable Bundle paramBundle) {
        updateMainPresenter(true, paramString, paramBundle);
    }

    private void updateMainContent() {
        updateBackPresenter(this.mCurrentMode.getBackFragmentTag(), null);
        updateFrontPresenter(this.mCurrentMode.getFrontFragmentTag(), null);
    }

    private void updateMainPresenter(boolean paramBoolean, @Nullable String paramString, @Nullable Bundle paramBundle) {
        MainContentPresenter localMainContentPresenter;
        if (paramBoolean) {
            localMainContentPresenter = this.mFrontPresenter;
            if ((localMainContentPresenter != null) || (paramString != null)) {
                break label29;
            }
        }
        label29:
        MainContentView localMainContentView;
        label235:
        do {
            return;
            localMainContentPresenter = this.mBackPresenter;
            break;
            if (paramBoolean) {
            }
            for (localMainContentView = this.mUi.getMainContentFront(); ; localMainContentView = this.mUi.getMainContentBack()) {
                if ((localMainContentPresenter != null) && (!localMainContentPresenter.getTag().equals(paramString))) {
                    if (this.mResumed) {
                        localMainContentPresenter.onPause();
                    }
                    if (this.mStarted) {
                        localMainContentPresenter.onStop();
                    }
                    localMainContentPresenter.onDetach();
                    localMainContentView.setPresenter(null);
                    localMainContentPresenter = setMainPresenter(null, paramBoolean);
                }
                if ((paramString != null) && (localMainContentPresenter == null)) {
                    localMainContentPresenter = setMainPresenter(getFactory().createMainContentPresenter(paramString, localMainContentView), paramBoolean);
                    localMainContentView.setPresenter(localMainContentPresenter);
                    localMainContentPresenter.onAttach(this, paramBundle);
                    if (this.mStarted) {
                        localMainContentPresenter.onStart();
                    }
                    if (this.mResumed) {
                        localMainContentPresenter.onResume();
                    }
                }
                if ((localMainContentPresenter != null) && (this.mCurrentMode == this.mPendingMode)) {
                    localMainContentPresenter.update(this.mModeManager, this.mCurrentMode);
                }
                if (!paramBoolean) {
                    break;
                }
                if (localMainContentPresenter == null) {
                    break label235;
                }
                if (localMainContentView.getVisibility() == 0) {
                    break;
                }
                Animations.showAndFadeIn(localMainContentView);
                return;
            }
        } while (localMainContentView.getVisibility() != 0);
        Animations.fadeOutAndHide(localMainContentView);
    }

    private void updateMode(UiMode paramUiMode) {
        EventLogger.recordBreakdownEvent(25);
        Preconditions.checkNotNull(this.mQueryState);
        if (this.mModeManager.isStartupComplete()) {
            updateMainContent();
        }
        if (paramUiMode == UiMode.NONE) {
        }
        for (boolean bool1 = true; ; bool1 = false) {
            this.mUi.setSearchPlateStickiness(getSearchPlateStickiness(), bool1, true);
            boolean bool2;
            if (!bool1) {
                boolean bool4 = this.mEventBus.getUiState().shouldSuppressCorpora();
                bool2 = false;
                if (!bool4) {
                }
            } else {
                bool2 = true;
            }
            updateFooterStickiness(bool2);
            boolean bool3 = this.mModeManager.shouldShowContextHeader(this.mCurrentMode);
            this.mContextHeaderPresenter.setEnabled(bool3);
            this.mUi.setShowContextHeader(bool3, bool1);
            this.mSearchPlatePresenter.updateSearchPlate(this.mFocused, bool1, this.mCurrentMode, this.mAppContext, this.mSearchController);
            updateFooter(paramUiMode);
            logCurrentMode();
            if (this.mCurrentMode.canShowLocationOptIn()) {
                this.mLocationSettings.maybeShowLegacyOptIn();
            }
            EventLogger.recordBreakdownEvent(26);
            return;
        }
    }

    public boolean canRunTheGoogle() {
        return this.mLoginHelper.canUserOptIntoGoogleNow();
    }

    void clearQuery() {
        startEditingQuery();
        this.mQueryState.set(this.mQueryState.get().clearQuery());
        getFormulationLogging().registerQueryEdit(this.mQueryState.get());
    }

    public void commitTextQuery() {
        this.mQueryState.commit(this.mQueryState.get().withSearchBoxStats(getFormulationLogging().build()));
    }

    public void createMenuItems(Menu paramMenu, boolean paramBoolean) {
        this.mSettings.addMenuItems(paramMenu, paramBoolean);
        addFeedbackMenuItem(paramMenu);
        new Help(this.mAppContext).addHelpMenuItem(paramMenu, this.mHelpContextSupplier, getIntentStarter());
        if (this.mDebugFeatures.teamDebugEnabled()) {
            paramMenu.add("[DEBUG] Sysdump").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    if (ActivityManager.isUserAMonkey()) {
                        Log.d("Velvet.Presenter", "Ignoring debug menu click, user is a monkey.");
                        return false;
                    }
                    StringWriter localStringWriter = new StringWriter();
                    VelvetPresenter.this.mUi.dumpActivityState("", new PrintWriter(localStringWriter));
                    VelvetPresenter.this.mUi.showDebugDialog(localStringWriter.toString());
                    return false;
                }
            });
            paramMenu.add("[DEBUG] Dump SRP HTML").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    if (ActivityManager.isUserAMonkey()) {
                        Log.d("Velvet.Presenter", "Ignoring debug menu click, user is a monkey.");
                        return false;
                    }
                    StringWriter localStringWriter = new StringWriter();
                    VelvetPresenter.this.mSearchController.dumpLastSearchResultsHtml(new PrintWriter(localStringWriter));
                    VelvetPresenter.this.mUi.showDebugDialog(localStringWriter.toString());
                    return false;
                }
            });
            paramMenu.add("[DEBUG] View in Chrome").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    if (ActivityManager.isUserAMonkey()) {
                        Log.d("Velvet.Presenter", "Ignoring debug menu click, user is a monkey.");
                    }
                    Query localQuery;
                    do {
                        return false;
                        localQuery = VelvetPresenter.this.getCommittedQuery();
                    } while (!localQuery.isTextOrVoiceWebSearchWithQueryChars());
                    Uri localUri = VelvetPresenter.this.mUrlHelper.getSearchRequest(localQuery, null).getUri();
                    Uri.Builder localBuilder = localUri.buildUpon();
                    localBuilder.clearQuery();
                    Iterator localIterator = localUri.getQueryParameterNames().iterator();
                    while (localIterator.hasNext()) {
                        String str = (String) localIterator.next();
                        if (!str.equals("tch")) {
                            localBuilder.appendQueryParameter(str, localUri.getQueryParameter(str));
                        }
                    }
                    VelvetPresenter.this.openUrlInSystem(new UriRequest(localBuilder.build()));
                    return false;
                }
            });
            paramMenu.add("[DEBUG] Icing corpora update").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    if (ActivityManager.isUserAMonkey()) {
                        Log.d("Velvet.Presenter", "Ignoring debug menu click, user is a monkey.");
                        return false;
                    }
                    VelvetPresenter.this.mAppContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.createForcedUpdateAllIntent(VelvetPresenter.this.mAppContext));
                    return false;
                }
            });
            paramMenu.add(2131362878).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                    Intent localIntent = new Intent(VelvetPresenter.this.mAppContext, TestLauncherActivity.class);
                    VelvetPresenter.this.mUi.startActivity(localIntent);
                    return true;
                }
            });
        }
    }

    public void dump(String paramString, PrintWriter paramPrintWriter) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("VelvetPresenter state:");
        String str = paramString + "  ";
        Object[] arrayOfObject1 = new Object[5];
        arrayOfObject1[0] = str;
        arrayOfObject1[1] = "Mode: ";
        arrayOfObject1[2] = this.mCurrentMode;
        arrayOfObject1[3] = " Pending: ";
        arrayOfObject1[4] = this.mPendingMode;
        DumpUtils.println(paramPrintWriter, arrayOfObject1);
        Object[] arrayOfObject2 = new Object[5];
        arrayOfObject2[0] = str;
        arrayOfObject2[1] = "mStarted: ";
        arrayOfObject2[2] = Boolean.valueOf(this.mStarted);
        arrayOfObject2[3] = " mResumed: ";
        arrayOfObject2[4] = Boolean.valueOf(this.mResumed);
        DumpUtils.println(paramPrintWriter, arrayOfObject2);
        Object[] arrayOfObject3 = new Object[5];
        arrayOfObject3[0] = str;
        arrayOfObject3[1] = "mFocused: ";
        arrayOfObject3[2] = Boolean.valueOf(this.mFocused);
        arrayOfObject3[3] = " mRestoredInstance: ";
        arrayOfObject3[4] = Boolean.valueOf(this.mRestoredInstance);
        DumpUtils.println(paramPrintWriter, arrayOfObject3);
        paramPrintWriter.print(str);
        paramPrintWriter.print("Attached to searchcontroller: ");
        paramPrintWriter.println(this.mSearchController.isAttached(this));
        paramPrintWriter.print(str);
        paramPrintWriter.print("Webview: ");
        paramPrintWriter.println(this.mWebView);
        if (this.mWebView != null) {
            paramPrintWriter.print(str);
            paramPrintWriter.print("Webview(LP): ");
            paramPrintWriter.println(this.mWebView.getLayoutParams());
        }
        this.mGss.dump(str, paramPrintWriter);
        if (this.mSearchController != null) {
            this.mSearchController.dump(str, paramPrintWriter);
        }
    }

    public Activity getActivity() {
        return this.mUi.getActivity();
    }

    public ClientConfig getClientConfig() {
        return this.mClientConfig;
    }

    public Query getCommittedQuery() {
        return this.mQueryState.getCommittedQuery();
    }

    public SearchConfig getConfig() {
        return this.mConfig;
    }

    @Nonnull
    public String getCurrentHelpContext() {
        switch (13.
        $SwitchMap$com$google$android$velvet$presenter$UiMode[this.mCurrentMode.ordinal()])
        {
            default:
                if (isNowEnabled()) {
                    return "main";
                }
                break;
            case 1:
            case 2:
                return "searchonly";
        }
        return "searchonly";
    }

    int getDimensionPixelSize(int paramInt) {
        return this.mAppContext.getResources().getDimensionPixelSize(paramInt);
    }

    public VelvetFactory getFactory() {
        return this.mVelvetFactory;
    }

    public SearchFormulationLogging getFormulationLogging() {
        maybeCreateFormulationLogging();
        return this.mFormulationLogging;
    }

    public IntentStarter getIntentStarter() {
        return this.mUi.getIntentStarter();
    }

    public LayoutInflater getLayoutInflater() {
        return this.mUi.getLayoutInflater();
    }

    public int getMinimumHotwordQuality() {
        return -1;
    }

    @Nullable
    UiMode getModeToSwitchTo() {
        Query localQuery = this.mQueryState.getCommittedQuery();
        ActionState localActionState = this.mEventBus.getActionState();
        UiState localUiState = this.mEventBus.getUiState();
        if (this.mQueryState.isEditingQuery()) {
            if (this.mQueryState.get().needVoiceCorrection()) {
                return UiMode.VOICE_CORRECTION;
            }
            boolean bool = this.mQueryState.get().isSummonsCorpus();
            if ((this.mQueryState.haveCommit()) && (!localQuery.isPredictiveTvSearch())) {
                if (bool) {
                    return UiMode.SUMMONS;
                }
                return UiMode.RESULTS_SUGGEST;
            }
            if (bool) {
                return UiMode.SUMMONS_SUGGEST;
            }
            return UiMode.SUGGEST;
        }
        if (localQuery.isSentinel()) {
            return UiMode.fromSentinelQuery(localQuery);
        }
        if (localQuery.isSummonsCorpus()) {
            return UiMode.SUMMONS;
        }
        if (localQuery.isPredictiveTvSearch()) {
            return UiMode.PREDICTIVE;
        }
        if (this.mEventBus.getUiState().shouldShowError()) {
            return UiMode.CONNECTION_ERROR;
        }
        if ((localQuery.isVoiceSearch()) && (localQuery.getQueryString().isEmpty()) && (!localUiState.shouldShowWebView()) && (!localUiState.shouldShowCards())) {
            return UiMode.VOICESEARCH;
        }
        if (((localQuery.isMusicSearch()) || (localQuery.isTvSearch())) && (!localActionState.hasDataForQuery(localQuery))) {
            return UiMode.SOUND_SEARCH;
        }
        return UiMode.RESULTS;
    }

    @Nullable
    Query getQueryFromIntent(Intent paramIntent, Query paramQuery) {
        Query localQuery;
        if (IntentUtils.isSearchIntent(paramIntent)) {
            boolean bool3 = IntentUtils.hasQueryStringExtra(paramIntent);
            localQuery = null;
            if (bool3) {
                if (!IntentUtils.isFromPredictive(paramIntent)) {
                    break label83;
                }
                Location localLocation = IntentUtils.getLocationOverride(paramIntent);
                localQuery = paramQuery.fromPredictiveToWeb(IntentUtils.getQueryString(paramIntent), localLocation);
            }
        }
        for (; ; ) {
            if (localQuery == null) {
                localQuery = Query.EMPTY.sentinel(UiMode.SUGGEST, null);
            }
            return localQuery.withAssistContext(paramIntent.getStringExtra("android.intent.extra.ASSIST_PACKAGE"), paramIntent.getBundleExtra("android.intent.extra.ASSIST_CONTEXT"));
            label83:
            localQuery = paramQuery.fromSearchIntent(IntentUtils.getQueryString(paramIntent), IntentUtils.shouldSelectAllQuery(paramIntent));
            continue;
            if ((IntentUtils.isVoiceSearchIntent(paramIntent)) && (!IntentUtils.isResumeFromHistory(paramIntent))) {
                Uri localUri = IntentUtils.getVoiceSearchRecordedAudioUrl(paramIntent);
                if (localUri != null) {
                    localQuery = paramQuery.voiceSearchWithRecordedAudio(localUri);
                } else {
                    localQuery = paramQuery.voiceSearchFromGui();
                }
            } else if ((IntentUtils.isSoundSearchIntent(paramIntent)) && (!IntentUtils.isResumeFromHistory(paramIntent))) {
                localQuery = paramQuery.musicSearchFromIntent();
            } else {
                boolean bool1 = IntentUtils.isTheGoogleIntent(paramIntent);
                localQuery = null;
                if (bool1) {
                    boolean bool2 = isNowEnabled();
                    localQuery = null;
                    if (bool2) {
                        localQuery = paramQuery.sentinel(UiMode.PREDICTIVE, paramIntent.getExtras());
                    }
                }
            }
        }
    }

    public View getReminderPeekView() {
        return this.mUi.getReminderPeekView();
    }

    public View getRemindersFooterIcon() {
        return this.mUi.getRemindersFooterIcon();
    }

    public ScrollViewControl getScrollViewControl() {
        return this.mUi.getScrollingContainer();
    }

    public SearchBoxStats getSearchBoxStats() {
        return getFormulationLogging().build();
    }

    SearchController getSearchController() {
        return this.mSearchController;
    }

    public int getSearchPlateHeight() {
        return this.mUi.getSearchPlateHeight();
    }

    public int getSearchPlateStickiness() {
        return this.mModeManager.getSearchPlateStickiness(this.mCurrentMode, this.mQueryState.isZeroQuery());
    }

    public SearchPlateUi getSearchPlateUi() {
        if (this.mSearchPlatePresenter != null) {
            return this.mSearchPlatePresenter.getSearchPlateUi();
        }
        return null;
    }

    CharSequence getString(int paramInt) {
        return this.mAppContext.getResources().getString(paramInt);
    }

    public SuggestionClickListener getSuggestionClickListener() {

        if (this.mSuggestionClickListener == null) {
            this.mSuggestionClickListener = new SuggestionLauncherWrapper(null);
        }
        return this.mSuggestionClickListener;
    }

    public ViewRecycler getSuggestionViewRecycler() {

        if (this.mSuggestionViewRecycler == null) {
            this.mSuggestionViewRecycler = this.mVelvetFactory.createSuggestionViewRecycler();
        }
        return this.mSuggestionViewRecycler;
    }

    public SuggestionsController getSuggestionsController() {
        return this.mSuggestionsController;
    }

    public View getTrainingFooterIcon() {
        return this.mUi.getTrainingFooterIcon();
    }

    public View getTrainingPeekIcon() {
        return this.mUi.getTrainingPeekIcon();
    }

    public View getTrainingPeekView() {
        return this.mUi.getTrainingPeekView();
    }

    View getWebView() {
        maybeBindWebView(true);
        return this.mWebView;
    }

    boolean goBack() {
        if ((this.mCurrentMode == UiMode.RESULTS) && (this.mSearchController.onBackPressed())) {
        }
        while (this.mQueryState.goBack()) {
            return true;
        }
        return false;
    }

    public boolean ignoreClearSuggestionsOnStop() {
        return false;
    }

    public void indicateRemoveFromHistoryFailed() {
        this.mUi.indicateRemoveFromHistoryFailed();
    }

    public boolean isChangingConfigurations() {
        return this.mUi.isChangingConfigurations();
    }

    public boolean isContextHeaderPresenterEnabled() {
        return (this.mContextHeaderPresenter != null) && (this.mContextHeaderPresenter.isEnabled());
    }

    boolean isCurrentBackFragment(MainContentPresenter paramMainContentPresenter) {
        return paramMainContentPresenter == this.mBackPresenter;
    }

    public boolean isNowEnabled() {
        return this.mLoginHelper.isUserOptedIntoGoogleNow();
    }

    boolean isPredictiveOnlyMode() {
        return this.mCurrentMode.isPredictiveMode();
    }

    public void onBrowserDimensionsAvailable(Point paramPoint) {
        if (!this.mDestroyed) {
            this.mSearchController.setBrowserDimensions(paramPoint);
            this.mQueryState.setBrowserDimensionsAvailable(true);
        }
    }

    public void onCreate(Bundle paramBundle, SearchController paramSearchController) {
        this.mSearchBoxLogging.setFallbackClientIdAndSource("velvet", "android-search-app");
        this.mNewIntent = this.mUi.getIntent();
        this.mIntentTypeToLog = this.mNewIntent.getAction();
        setSourceParams(this.mNewIntent);
        this.mSearchController = paramSearchController;
        this.mSearchController.attach(this);
        this.mUiThread.executeOnIdle(this.mDelayedInitializeTask);
        this.mRestoredInstance = false;
        this.mEventBus = this.mSearchController.getEventBus();
        this.mQueryState = this.mEventBus.getQueryState();
        this.mSuggestionLauncher = this.mVelvetFactory.createSuggestionsLauncher(getIntentStarter(), this.mQueryState, this.mSearchController.getSuggestionsPresenter());
        if (paramBundle != null) {
            this.mRestoredInstance = true;
            this.mEventBus.restoreInstanceState(paramBundle);
        }
        this.mUi.clearIntent();
        EventLogger.recordClientEvent(43);
    }

    public void onCreateOptionsMenu(Menu paramMenu) {
        createMenuItems(paramMenu, true);
    }

    public void onDestroy() {
        this.mDestroyed = true;
        cancelModeChangeTasks();
        cancelIdleTasks();
        this.mUiThread.cancelExecute(this.mUpdateMainContentTask);
        clearMainContent();
        this.mFooterPresenter.onDetach();
        this.mSearchPlatePresenter.onDetach();
        this.mFooterPresenter = null;
        this.mSearchPlatePresenter = null;
        this.mContextHeaderPresenter = null;
        this.mSearchController.setBrowserDimensions(null);
        this.mQueryState.setBrowserDimensionsAvailable(false);
        unbindWebView();
        this.mSearchController.detach(this);
        this.mSearchController = null;
        this.mQueryState = null;
    }

    public void onDetachForced() {
        unbindWebView();
        clearMainContent();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4) {
            if ((this.mFrontPresenter != null) && (this.mFrontPresenter.onBackPressed())) {
            }
            while ((this.mBackPresenter != null) && (this.mBackPresenter.onBackPressed())) {
                return true;
            }
            return goBack();
        }
        if (paramInt == 84) {
            this.mSearchPlatePresenter.focusQueryAndShowKeyboard();
            return true;
        }
        if (this.mCurrentMode.isPredictiveMode()) {
            char c = (char) paramKeyEvent.getUnicodeChar();
            if (Character.isLetterOrDigit(c)) {
                this.mQueryState.startQueryEdit();
                this.mQueryState.set(this.mQueryState.get().withQueryChars(Character.toString(c)));
                return true;
            }
        }
        return false;
    }

    public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent) {
        if ((paramInt == 4) && (paramKeyEvent.getAction() == 0) && (this.mModeManager.shouldGoBackOnPreImeBackPress(this.mCurrentMode, this.mQueryState.isZeroQuery(), this.mQueryState.isEditingQuery()))) {
            return goBack();
        }
        return false;
    }

    public boolean onMainViewTouched() {
        if (this.mModeManager.shouldStopQueryEditOnMainViewClick(this.mCurrentMode, this.mQueryState)) {
            this.mQueryState.stopQueryEdit();
            return true;
        }
        if (this.mCurrentMode.isViewAndEditMode()) {
            this.mSearchPlatePresenter.unfocusQueryAndHideKeyboard();
            return true;
        }
        return false;
    }

    public void onMenuButtonClick(View paramView) {
        this.mUi.showOptionsMenu(paramView);
    }

    public void onNewIntent(Intent paramIntent) {
        this.mIntentTypeToLog = paramIntent.getAction();
        setSourceParams(paramIntent);
        this.mRestoredInstance = false;
        clearMainContent();
        this.mUi.clearIntent();
        setupFromIntent(paramIntent);
        this.mNewIntent = null;
    }

    public void onPause() {
        if (this.mBackPresenter != null) {
            this.mBackPresenter.onPause();
        }
        if (this.mFrontPresenter != null) {
            this.mFrontPresenter.onPause();
        }
        this.mCoreServices.getBackgroundTasks().forceRun("send_gsa_home_request", 0L);
        this.mEventBus.removeObserver(this);
        this.mResumed = false;
        cancelModeChangeTasks();
        if (!this.mUi.isChangingConfigurations()) {
            EventLogger.recordClientEvent(2);
            this.mUi.closeOptionsMenu();
        }
    }

    public void onPostCreate(Bundle paramBundle) {
        if (this.mSearchController != null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mFooterPresenter = this.mVelvetFactory.createFooterPresenter(this.mUi.getFooterUi());
            this.mFooterPresenter.onAttach(this, paramBundle);
            this.mContextHeaderPresenter = this.mVelvetFactory.createContextHeaderPresenter(this.mUi.getContextHeaderUi());
            this.mSearchPlatePresenter = this.mVelvetFactory.createSearchPlatePresenter(this.mUi.getVelvetSearchPlateUi());
            this.mSearchPlatePresenter.onAttach(this, paramBundle);
            updateMode(this.mCurrentMode);
            if (paramBundle != null) {
                updateBackPresenter(paramBundle.getString("velvet:velvet_presenter:back", null), paramBundle);
                updateFrontPresenter(paramBundle.getString("velvet:velvet_presenter:front", null), paramBundle);
            }
            return;
        }
    }

    public void onQueryTextChanged(CharSequence paramCharSequence, int paramInt) {
        Query localQuery = this.mQueryState.get().withQueryCharsAndSelection(paramCharSequence, paramInt);
        this.mQueryState.set(localQuery);
        getFormulationLogging().registerQueryEdit(localQuery);
    }

    public void onQueryTextSelected(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
        Query localQuery = this.mQueryState.get().withQueryCharsAndSelection(paramCharSequence, paramInt1, paramInt2);
        this.mQueryState.set(localQuery);
        getFormulationLogging().registerQueryEdit(localQuery);
    }

    void onRemindersButtonPressed() {
        Intent localIntent = new Intent(getActivity(), RemindersListActivity.class);
        getActivity().startActivity(localIntent);
    }

    public void onResume() {
        maybeReattachToSearchController();
        this.mQueryState.maybePopExternalActivitySentinel();
        this.mResumed = true;
        if ((isPredictiveOnlyMode()) && (!isNowEnabled())) {
            this.mPendingMode = UiMode.SUGGEST;
            clearQuery();
        }
        if (this.mPendingMode != this.mCurrentMode) {
            cancelModeChangeTasks();
            this.mUiThread.execute(this.mChangeModeTask);
        }
        logCurrentMode();
        if (this.mModeManager.isStartupComplete()) {
            this.mCoreServices.getBackgroundTasks().notifyUiLaunched();
        }
        if (!this.mUi.isChangingConfigurations()) {
            EventLogger.recordClientEvent(1);
        }
        this.mUiThread.executeOnIdle(this.mLogIdleTask);
        if (this.mBackPresenter != null) {
            this.mBackPresenter.onResume();
        }
        if (this.mFrontPresenter != null) {
            this.mFrontPresenter.onResume();
        }
        this.mEventBus.addObserver(this);
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        boolean bool = isChangingConfigurations();
        if (!bool) {
            this.mEventBus.saveInstanceState(paramBundle);
        }
        if (this.mFrontPresenter != null) {
            paramBundle.putString("velvet:velvet_presenter:front", this.mFrontPresenter.getTag());
            this.mFrontPresenter.saveInstanceState(paramBundle, bool);
        }
        if (this.mBackPresenter != null) {
            paramBundle.putString("velvet:velvet_presenter:back", this.mBackPresenter.getTag());
            this.mBackPresenter.saveInstanceState(paramBundle, bool);
        }
        this.mUi.getVelvetSearchPlateUi().saveInstanceState(paramBundle);
    }

    public void onSearchBoxKeyboardFocused() {
        getScrollViewControl().smoothScrollToY(0);
        getFormulationLogging().registerSearchBoxReady();
    }

    public void onSearchBoxTouched() {
        startEditingQuery();
    }

    public void onSearchPhoneClickedInSuggest() {
        this.mQueryState.commit(this.mQueryState.get().withCorpus("summons"));
    }

    public void onStart() {
        boolean bool = true;
        this.mStarted = bool;
        if (this.mLocationSettings.canUseLocationForSearch()) {
            if (this.mLocationOracleLock != null) {
                break label165;
            }
        }
        for (; ; ) {
            Preconditions.checkState(bool);
            this.mLocationOracleLock = this.mLocationOracle.newRunningLock("search ui");
            this.mLocationOracleLock.acquire();
            this.mLocationOracle.requestRecentLocation(1000L * this.mConfig.getLocationExpiryTimeSeconds());
            this.mSuggestionsController.start();
            if (this.mNewIntent != null) {
                Intent localIntent = this.mNewIntent;
                this.mNewIntent = null;
                if (!this.mRestoredInstance) {
                    setupFromIntent(localIntent);
                }
            }
            if (this.mLifecycleObserver != null) {
                this.mLifecycleObserver.onActivityStart();
            }
            this.mSearchController.start(this);
            if (this.mBackPresenter != null) {
                this.mBackPresenter.onStart();
            }
            if (this.mFrontPresenter != null) {
                this.mFrontPresenter.onStart();
            }
            return;
            label165:
            bool = false;
        }
    }

    public void onStateChanged(VelvetEventBus.Event paramEvent) {
        this.mUi.assertNotInLayout();
        if (!this.mSearchController.isAttached(this)) {
            Log.w("Velvet.Presenter", "Still observing while detached from SearchController");
            return;
        }
        if (paramEvent.hasQueryChanged()) {
            Query localQuery = this.mQueryState.takeNewlyCommittedWebQuery();
            if ((localQuery != null) && (localQuery.isVoiceSearch()) && (TextUtils.equals(localQuery.getQueryString(), getString(2131363307)))) {
                this.mUi.doABarrelRoll();
            }
            this.mUi.setSearchPlateStickiness(getSearchPlateStickiness(), false, true);
            this.mSearchPlatePresenter.setQuery(this.mQueryState.get());
            updateFooter(this.mCurrentMode);
            UiMode localUiMode = getModeToSwitchTo();
            if ((localUiMode == null) || (localUiMode == this.mPendingMode)) {
                break label253;
            }
            enterMode(localUiMode);
        }
        for (; ; ) {
            if (((paramEvent.hasQueryChanged()) || (paramEvent.hasUiChanged()) || (paramEvent.hasTtsChanged())) && (this.mFocused)) {
                this.mSearchPlatePresenter.updateSearchPlate(this.mFocused, false, this.mCurrentMode, this.mAppContext, this.mSearchController);
            }
            maybeBindWebView(this.mEventBus.getUiState().shouldShowWebView());
            if (!paramEvent.hasUiChanged()) {
                break;
            }
            UiState localUiState = this.mEventBus.getUiState();
            if (localUiState.takeShowSearchPlate()) {
                this.mUi.showSearchPlate();
            }
            updateFooterStickiness(localUiState.shouldSuppressCorpora());
            return;
            label253:
            if (this.mCurrentMode == this.mPendingMode) {
                updateMainContent();
            }
        }
    }

    public void onStop() {
        this.mStarted = false;
        if (this.mBackPresenter != null) {
            this.mBackPresenter.onStop();
        }
        if (this.mFrontPresenter != null) {
            this.mFrontPresenter.onStop();
        }
        if (!isChangingConfigurations()) {
            this.mSearchController.stop(this);
        }
        this.mSuggestionsController.stop();
        if (this.mLocationOracleLock != null) {
            this.mLocationOracleLock.release();
            this.mLocationOracleLock = null;
        }
        if (this.mLifecycleObserver != null) {
            this.mLifecycleObserver.onActivityStop();
        }
    }

    public void onTrainingButtonPressed() {
        IntentDispatcherUtil.dispatchIntent(getActivity(), "com.google.android.googlequicksearchbox.TRAINING_CLOSET");
    }

    public void onWebSuggestionsDismissed() {
        if (this.mModeManager.shouldSwitchToSummonsOnWebSuggestDismiss(this.mCurrentMode, this.mQueryState)) {
            this.mQueryState.startQueryEdit();
            this.mQueryState.set(this.mQueryState.get().withCorpus("summons"));
            return;
        }
        goBack();
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        this.mFocused = paramBoolean;
        if (this.mSearchController.isAttached(this)) {
            this.mQueryState.setHotwordDetectionEnabled(paramBoolean);
        }
        if ((paramBoolean) && (this.mResumed)) {
            this.mSearchPlatePresenter.updateSearchPlate(this.mFocused, false, this.mCurrentMode, this.mAppContext, this.mSearchController);
        }
    }

    public void openUrlInSystem(UriRequest paramUriRequest) {
        try {
            Intent localIntent = this.mUrlHelper.getExternalIntentForUri(paramUriRequest.getUri());
            localIntent.setFlags(268435456);
            Map localMap = paramUriRequest.getHeaders();
            Bundle localBundle;
            if (localMap.size() > 0) {
                localBundle = new Bundle();
                Iterator localIterator = localMap.entrySet().iterator();
                while (localIterator.hasNext()) {
                    Map.Entry localEntry = (Map.Entry) localIterator.next();
                    localBundle.putString((String) localEntry.getKey(), (String) localEntry.getValue());
                }
            }
            return;
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Log.e("Velvet.Presenter", "No activity found to open: " + paramUriRequest.getUri().toString());
            this.mUi.showErrorToast(2131363215);
            return;
            localIntent.putExtra("com.android.browser.headers", localBundle);
            this.mUi.startActivity(localIntent);
            this.mUi.overridePendingTransition(2131034115, 2131034116);
            return;
        } catch (URISyntaxException localURISyntaxException) {
            Log.e("Velvet.Presenter", "Malformed URI: " + paramUriRequest.getUri().toString());
            this.mUi.showErrorToast(2131363215);
        }
    }

    public boolean resolveIntent(Intent paramIntent) {
        return getIntentStarter().resolveIntent(paramIntent);
    }

    public void setContextHeader(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener) {
        this.mContextHeaderPresenter.setContextHeader(paramDrawable, paramBoolean, paramOnClickListener);
        this.mUi.setFadeSearchPlateOverHeader(paramBoolean);
    }

    public void setWebSuggestionsEnabled(boolean paramBoolean) {
        this.mSuggestionsController.setWebSuggestionsEnabled(paramBoolean);
    }

    public boolean shouldUseMusicHotworder() {
        return true;
    }

    public void showSuggestions(Suggestions paramSuggestions) {
        this.mSuggestionsController.setSuggestions(paramSuggestions);
    }

    public boolean startActivity(Intent... paramVarArgs) {
        return getIntentStarter().startActivity(paramVarArgs);
    }

    void startEditingQuery() {
        this.mEventBus.getActionState().cancelCardCountDownByUser();
        this.mQueryState.startQueryEdit();
        if (this.mModeManager.shouldScrollToTopOnSearchBoxTouch(this.mCurrentMode, this.mQueryState.isZeroQuery())) {
            getScrollViewControl().smoothScrollToY(0);
        }
    }

    public boolean supportWickedFast() {
        return true;
    }

    private class SuggestionLauncherWrapper
            implements SuggestionClickListener {
        private SuggestionLauncherWrapper() {
        }

        public void onSuggestionClicked(Suggestion paramSuggestion) {
            VelvetPresenter.this.getFormulationLogging().registerSuggestClick(paramSuggestion);
            VelvetPresenter.this.mSuggestionLauncher.onSuggestionClicked(paramSuggestion, VelvetPresenter.this.getFormulationLogging().build());
        }

        public void onSuggestionQueryRefineClicked(Suggestion paramSuggestion) {
            VelvetPresenter.this.mQueryState.set(VelvetPresenter.this.mQueryState.get().withQueryChars(paramSuggestion.getSuggestionQuery() + " "));
            VelvetPresenter.this.getFormulationLogging().registerQueryRefinement(paramSuggestion);
            VelvetPresenter.this.getFormulationLogging().registerQueryEdit(VelvetPresenter.this.mQueryState.get());
        }

        public void onSuggestionQuickContactClicked(Suggestion paramSuggestion) {
            VelvetPresenter.this.getFormulationLogging().registerSuggestClick(paramSuggestion);
            VelvetPresenter.this.mSuggestionLauncher.onSuggestionQuickContactClicked(paramSuggestion, VelvetPresenter.this.getFormulationLogging().build());
        }

        public void onSuggestionRemoveFromHistoryClicked(final Suggestion paramSuggestion) {
            if (paramSuggestion.isHistorySuggestion()) {
                VelvetPresenter.this.mUi.showRemoveFromHistoryDialog(paramSuggestion, new Runnable() {
                    public void run() {
                        VelvetPresenter.this.mSuggestionLauncher.onSuggestionRemoveFromHistoryClicked(paramSuggestion);
                    }
                });
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.VelvetPresenter

 * JD-Core Version:    0.7.0.1

 */