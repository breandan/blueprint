package com.google.android.search.core.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import com.google.android.e100.SMSBroadcastReceiver;
import com.google.android.e100.SMSBroadcastReceiver.Observer;
import com.google.android.search.core.ChargingStateBroadcastReceiver;
import com.google.android.search.core.ChargingStateListener;
import com.google.android.search.core.ChargingStateListener.Observer;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.SearchController.SearchClient;
import com.google.android.search.core.SearchControllerCache;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.WaveGestureListener;
import com.google.android.search.core.WaveGestureListener.Observer;
import com.google.android.search.core.WaveGestureProxmitySensorListener;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.core.suggest.CachingPromoter;
import com.google.android.search.core.suggest.CorrectionPromoter;
import com.google.android.search.core.suggest.NowPromoPromoter;
import com.google.android.search.core.suggest.OriginalQueryFilter;
import com.google.android.search.core.suggest.SuggestionLauncher;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.suggest.SuggestionsUi;
import com.google.android.search.core.suggest.WebPromoter;
import com.google.android.search.core.summons.FirstNonEmptySummonsPromoter;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.SearchBoxStats.Builder;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.service.ClientConfig;
import com.google.android.search.shared.service.SearchServiceUiCallback;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.shared.util.SpeechLevelSource.Listener;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationOracle.RunningLock;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.util.IntentUtils;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LocalSearchService
  implements ChargingStateListener.Observer, SearchController.SearchClient, WaveGestureListener.Observer, SearchService, VelvetEventBus.Observer, SuggestionsUi
{
  private boolean mBluetoothShouldForeground;
  private SearchServiceUiCallback mCallback;
  private boolean mCharging = false;
  private ChargingStateListener mChargingStateListener;
  private ClientConfig mConfig;
  private final Context mContext;
  private final SearchServiceUiCallback mDefaultCallback;
  private ClientConfig mDefaultConfig;
  private final VelvetEventBus mEventBus;
  private final VelvetFactory mFactory;
  private boolean mForegroundService;
  private final GsaConfigFlags mGsaConfigFlags;
  private boolean mIsHotwordActive;
  private boolean mIsHotwordSupported = true;
  private final SpeechLevelSource.Listener mLevelsListener;
  private final LocationOracle mLocationOracle;
  @Nullable
  private LocationOracle.RunningLock mLocationOracleLock;
  private final LocationSettings mLocationSettings;
  private boolean mQueryShouldForeground;
  private final QueryState mQueryState;
  private SMSBroadcastReceiver mSMSBroadcastReceiver;
  private final SearchBoxLogging mSearchBoxLogging;
  private final SearchConfig mSearchConfig;
  private final SearchController mSearchController;
  private final SearchControllerCache mSearchControllerCache;
  private final int mSearchControllerToken;
  private SearchPlateUi mSearchPlateUi;
  private final SearchServiceImpl.SearchServiceBinder mServiceBinder;
  private final ServiceForegroundHelper mServiceForegroundHelper;
  private final SpeechLevelSource mSpeechLevelSource;
  private SuggestionLauncher mSuggestionLauncher;
  private SuggestionsController mSuggestionsController;
  private boolean mTransitioningToResultsActivity;
  private WaveGestureListener mWaveGestureListener;
  
  public LocalSearchService(Context paramContext, VelvetServices paramVelvetServices, SearchServiceImpl.SearchServiceBinder paramSearchServiceBinder, ServiceForegroundHelper paramServiceForegroundHelper)
  {
    this.mContext = paramContext;
    this.mServiceBinder = paramSearchServiceBinder;
    this.mServiceForegroundHelper = paramServiceForegroundHelper;
    this.mSearchControllerCache = paramVelvetServices.getCoreServices().getSearchControllerCache();
    this.mSearchControllerToken = this.mSearchControllerCache.acquireToken();
    this.mSearchController = ((SearchController)this.mSearchControllerCache.get(this.mSearchControllerToken));
    this.mEventBus = this.mSearchController.getEventBus();
    this.mQueryState = this.mEventBus.getQueryState();
    this.mFactory = paramVelvetServices.getFactory();
    this.mSearchConfig = paramVelvetServices.getCoreServices().getConfig();
    this.mGsaConfigFlags = paramVelvetServices.getCoreServices().getGsaConfigFlags();
    this.mLocationSettings = paramVelvetServices.getCoreServices().getLocationSettings();
    this.mLocationOracle = paramVelvetServices.getLocationOracle();
    this.mDefaultCallback = new DefaultServiceUiCallback(paramContext);
    this.mCallback = this.mDefaultCallback;
    this.mDefaultConfig = new ClientConfig(2);
    this.mConfig = this.mDefaultConfig;
    this.mSearchBoxLogging = paramVelvetServices.getCoreServices().getSearchBoxLogging();
    this.mLevelsListener = new SpeechLevelSource.Listener()
    {
      public void onSpeechLevel(int paramAnonymousInt)
      {
        LocalSearchService.this.mCallback.updateSpeechLevel(paramAnonymousInt);
      }
    };
    this.mSpeechLevelSource = paramVelvetServices.getVoiceSearchServices().getSpeechLevelSource();
    this.mSpeechLevelSource.setListener(this.mLevelsListener);
  }
  
  private SuggestionLauncher getSuggestionLauncher()
  {
    if (this.mSuggestionLauncher == null) {
      this.mSuggestionLauncher = this.mFactory.createSuggestionsLauncher(this, this.mQueryState, this.mSearchController.getSuggestionsPresenter());
    }
    return this.mSuggestionLauncher;
  }
  
  private void initSuggestions()
  {
    CachingPromoter localCachingPromoter = new CachingPromoter(new AggregatePromoter(this.mSearchConfig.getMinWebSuggestions(), this.mSearchConfig.getMaxWebSuggestions(), this.mSearchConfig.getMaxTotalSuggestions(), new WebPromoter(new OriginalQueryFilter(), this.mGsaConfigFlags), new FirstNonEmptySummonsPromoter(), new CorrectionPromoter(), new NowPromoPromoter(), this.mGsaConfigFlags), this.mSearchConfig.getMaxTotalSuggestions());
    this.mSuggestionsController = this.mFactory.createSuggestionsController();
    this.mSuggestionsController.addSuggestionsView("combinedsuggest", localCachingPromoter, this.mSearchBoxLogging.captureShownWebSuggestions(this));
    this.mSuggestionsController.start();
  }
  
  private void refreshSearchHint()
  {
    String str = "";
    this.mIsHotwordSupported = this.mQueryState.isHotwordSupported();
    this.mIsHotwordActive = this.mQueryState.isHotwordActive();
    boolean bool = this.mIsHotwordSupported;
    int i = 0;
    if (bool) {
      i = 0x0 | 0x2;
    }
    if ((this.mIsHotwordActive) && (!this.mSearchController.maybeInitializeGreco3DataManager()))
    {
      str = this.mSearchController.getHotwordPrompt();
      i |= 0x1;
      if (this.mSearchController.shouldShowHotwordHint()) {
        i |= 0x4;
      }
      if (this.mSearchController.hasHotwordPrompt()) {
        i |= 0x10;
      }
    }
    if (this.mEventBus.getTtsState().isPlaying()) {
      i |= 0x20;
    }
    this.mSearchPlateUi.setExternalFlags(i, str, false);
  }
  
  private boolean shouldSwitchToResultsActivity()
  {
    Query localQuery = this.mQueryState.getCommittedQuery();
    if (this.mQueryState.getError() != null) {}
    do
    {
      do
      {
        return false;
      } while ((localQuery.isTvSearch()) || (localQuery.isEyesFree()));
      if (localQuery.isTextOrVoiceWebSearchWithQueryChars()) {
        return true;
      }
    } while ((!localQuery.isValidSearch()) || (!this.mEventBus.getActionState().hasDataForQuery(localQuery)));
    return true;
  }
  
  private void updateForegroundState()
  {
    if ((this.mQueryShouldForeground) || (this.mBluetoothShouldForeground)) {}
    for (boolean bool = true;; bool = false)
    {
      if (bool != this.mForegroundService)
      {
        this.mForegroundService = bool;
        this.mServiceForegroundHelper.setForeground(bool);
      }
      return;
    }
  }
  
  private void updateSearchPlate()
  {
    SearchError localSearchError = this.mQueryState.getError();
    if (localSearchError != null)
    {
      this.mSearchPlateUi.showErrorMessage(this.mContext.getResources().getString(localSearchError.getErrorMessageResId()));
      return;
    }
    Query localQuery = this.mQueryState.getCommittedQuery();
    int i = 1;
    if (shouldSwitchToResultsActivity()) {
      if (localQuery.isVoiceSearch())
      {
        i = 8;
        if (!localQuery.isTriggeredFromHotword()) {
          break label183;
        }
      }
    }
    label183:
    for (int j = 1;; j = 0)
    {
      this.mSearchPlateUi.setSearchPlateMode(i, j, false);
      this.mSearchPlateUi.setQuery(this.mQueryState.get());
      return;
      i = 7;
      break;
      if (localQuery.isFromPredictive()) {
        break;
      }
      if (this.mQueryState.isEditingQuery())
      {
        i = 2;
        break;
      }
      if (localQuery.isVoiceSearch())
      {
        if (this.mQueryState.isMusicDetected())
        {
          i = 4;
          break;
        }
        i = 3;
        break;
      }
      if (localQuery.isMusicSearch())
      {
        i = 5;
        break;
      }
      if (!localQuery.isTvSearch()) {
        break;
      }
      i = 6;
      break;
    }
  }
  
  public void cancel()
  {
    if (this.mSearchController.isAttached(this)) {
      this.mQueryState.goBack();
    }
  }
  
  public void commit(Query paramQuery)
  {
    if (paramQuery.getSearchBoxStats() == null) {
      VelvetStrictMode.logW("LocalSearchService", "Any committed query should have SearchBoxStats.");
    }
    if ((this.mSearchController.isAttached(this)) && (!this.mTransitioningToResultsActivity))
    {
      this.mQueryState.resetSearchClient();
      this.mQueryState.commit(paramQuery);
    }
  }
  
  public void create()
  {
    this.mSearchPlateUi = new MySearchPlateUi(null);
    initSuggestions();
    this.mTransitioningToResultsActivity = false;
    this.mSearchController.attachStopped(this);
    this.mQueryState.pushSearchClient();
    this.mSearchController.start(this);
    this.mEventBus.addObserver(this);
    this.mQueryState.onStartupComplete();
    if (this.mLocationSettings.canUseLocationForSearch())
    {
      this.mLocationOracleLock = this.mLocationOracle.newRunningLock("searchservice");
      this.mLocationOracleLock.acquire();
      this.mLocationOracle.requestRecentLocation(1000L * this.mSearchConfig.getLocationExpiryTimeSeconds());
    }
  }
  
  public void destroy()
  {
    this.mEventBus.removeObserver(this);
    if (!this.mTransitioningToResultsActivity)
    {
      this.mQueryState.setHotwordDetectionEnabled(false);
      this.mSearchController.stop(this);
    }
    this.mSearchController.detach(this);
    this.mQueryState.popSearchClient();
    this.mSearchControllerCache.releaseToken(this.mSearchControllerToken);
    this.mCallback = this.mDefaultCallback;
    this.mConfig = this.mDefaultConfig;
    this.mSpeechLevelSource.clearListener(this.mLevelsListener);
    this.mSuggestionsController.removeSuggestionsView("combinedsuggest");
    if (this.mLocationOracleLock != null) {
      this.mLocationOracleLock.release();
    }
  }
  
  public void dump(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print("Atatached to SearchController: ");
    paramPrintWriter.println(this.mSearchController.isAttached(this));
    paramPrintWriter.print("ClientCallbacks?: ");
    if (this.mCallback == null) {
      paramPrintWriter.println("null");
    }
    for (;;)
    {
      paramPrintWriter.print("TransitioningToResults: ");
      paramPrintWriter.println(this.mTransitioningToResultsActivity);
      paramPrintWriter.print("IsHotwordActive: ");
      paramPrintWriter.println(this.mIsHotwordActive);
      paramPrintWriter.print("IsHotwordSupported: ");
      paramPrintWriter.println(this.mIsHotwordSupported);
      paramPrintWriter.print("ForegroundService: ");
      paramPrintWriter.println(this.mForegroundService);
      paramPrintWriter.print("Binder Detached: ");
      paramPrintWriter.println(this.mServiceBinder.isDetached());
      this.mSearchController.dump("", paramPrintWriter);
      return;
      if (this.mCallback == this.mDefaultCallback) {
        paramPrintWriter.println("default");
      } else {
        paramPrintWriter.println("attached");
      }
    }
  }
  
  public void ensureAttachedToSearchController()
  {
    if (!this.mSearchController.isAttached(this)) {
      create();
    }
    this.mServiceBinder.setDetached(false);
  }
  
  void forceForegroundOnEyesFreeCommitHack()
  {
    if (!this.mSearchController.isAttached()) {
      create();
    }
    this.mQueryShouldForeground = true;
    updateForegroundState();
  }
  
  public ClientConfig getClientConfig()
  {
    return this.mConfig;
  }
  
  public int getMinimumHotwordQuality()
  {
    return 1;
  }
  
  public SearchBoxStats getSearchBoxStats()
  {
    return SearchBoxStats.newBuilder("gel", "android-search-app").build();
  }
  
  public SearchPlateUi getSearchPlateUi()
  {
    return this.mSearchPlateUi;
  }
  
  public boolean ignoreClearSuggestionsOnStop()
  {
    return true;
  }
  
  public void indicateRemoveFromHistoryFailed()
  {
    this.mCallback.onRemoveSuggestionFromHistoryFailed();
  }
  
  public void onChargingStateChanged(boolean paramBoolean)
  {
    this.mQueryState.onCharging(paramBoolean);
  }
  
  public void onDetachForced()
  {
    Log.w("LocalSearchService", "LocalSearchService forcibly detach from SearchController.");
    this.mServiceBinder.setDetached(true);
    this.mQueryState.popSearchClient();
  }
  
  public void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    if (this.mSearchController.isAttached(this)) {
      getSuggestionLauncher().onSuggestionQuickContactClicked(paramSuggestion, paramSearchBoxStats);
    }
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if ((Feature.SEARCH_ON_WAVE_GESTURE.isEnabled()) && (this.mWaveGestureListener != null) && (this.mCharging != this.mQueryState.isCharging()))
    {
      this.mCharging = this.mQueryState.isCharging();
      if (this.mCharging) {
        this.mWaveGestureListener.startListening(this.mContext, this);
      }
    }
    else
    {
      if ((!this.mTransitioningToResultsActivity) && (!this.mServiceBinder.isDetached())) {
        break label97;
      }
    }
    label97:
    do
    {
      do
      {
        return;
        this.mWaveGestureListener.stopListening(this.mContext, this);
        break;
        if ((paramEvent.hasTtsChanged()) || (paramEvent.hasQueryChanged()))
        {
          this.mQueryShouldForeground = this.mQueryState.queryShouldKeepSearchServiceInForeground();
          updateForegroundState();
        }
      } while (!paramEvent.hasQueryChanged());
      updateSearchPlate();
      if ((this.mIsHotwordActive == this.mQueryState.isHotwordActive()) || ((this.mIsHotwordSupported != this.mQueryState.isHotwordSupported()) || (this.mIsHotwordActive != this.mQueryState.isHotwordActive()) || (paramEvent.hasTtsChanged()))) {
        refreshSearchHint();
      }
    } while (!shouldSwitchToResultsActivity());
    this.mTransitioningToResultsActivity = true;
    this.mQueryState.commitSearchClient();
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = IntentUtils.createResumeVelvetWithSearchControllerIntent(this.mContext, this.mSearchControllerCache.acquireToken(), this.mQueryState.get(), this.mEventBus.getActionState().getActionData());
    startActivity(arrayOfIntent);
  }
  
  public void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    if (this.mSearchController.isAttached(this)) {
      getSuggestionLauncher().onSuggestionClicked(paramSuggestion, paramSearchBoxStats);
    }
  }
  
  public void onTrimMemory()
  {
    this.mSearchController.onTrimMemory();
  }
  
  public void onWaveGesture()
  {
    this.mQueryState.commit(Query.EMPTY.withSearchBoxStats(getSearchBoxStats()).voiceSearchFromWaveGesture());
  }
  
  public void openUrlInSystem(UriRequest paramUriRequest) {}
  
  public void registerE100Listeners()
  {
    if (Feature.SEARCH_ON_WAVE_GESTURE.isEnabled()) {
      this.mWaveGestureListener = new WaveGestureProxmitySensorListener();
    }
    if (Feature.HOTWORD_WHEN_CHARGING.isEnabled())
    {
      this.mChargingStateListener = new ChargingStateBroadcastReceiver();
      this.mChargingStateListener.startListening(this.mContext, this);
    }
    if (Feature.CAR_SMS_NOTIFICATIONS.isEnabled())
    {
      this.mSMSBroadcastReceiver = new SMSBroadcastReceiver();
      this.mSMSBroadcastReceiver.startListening(this.mContext, new SMSBroadcastReceiver.Observer()
      {
        public void onSMSRecieved(String paramAnonymousString1, String paramAnonymousString2)
        {
          LocalSearchService.this.mSearchController.onMessageReceived(paramAnonymousString2, paramAnonymousString1);
        }
      });
    }
  }
  
  public void removeSuggestionFromHistory(Suggestion paramSuggestion)
  {
    if (this.mSearchController.isAttached(this)) {
      getSuggestionLauncher().onSuggestionRemoveFromHistoryClicked(paramSuggestion);
    }
  }
  
  public boolean resolveIntent(Intent paramIntent)
  {
    return this.mCallback.resolveIntent(paramIntent);
  }
  
  public void set(Query paramQuery)
  {
    if (this.mSearchController.isAttached(this)) {
      this.mQueryState.set(paramQuery.withCommitIdFrom(this.mQueryState.get()));
    }
  }
  
  void setBluetoothShouldForeground(boolean paramBoolean)
  {
    this.mBluetoothShouldForeground = paramBoolean;
    updateForegroundState();
  }
  
  public void setHotwordDetectionEnabled(boolean paramBoolean)
  {
    if (this.mSearchConfig.isHotwordFromLauncherEnabled()) {
      if (this.mSearchController.isAttached(this)) {
        this.mQueryState.setHotwordDetectionEnabled(paramBoolean);
      }
    }
    while (!paramBoolean) {
      return;
    }
    Log.w("LocalSearchService", "Not requesting hotword detection as it's disabled by config");
  }
  
  public void setSearchServiceUiCallback(SearchServiceUiCallback paramSearchServiceUiCallback, ClientConfig paramClientConfig)
  {
    if (paramSearchServiceUiCallback == null) {
      paramSearchServiceUiCallback = this.mDefaultCallback;
    }
    this.mCallback = paramSearchServiceUiCallback;
    if (paramClientConfig == null) {
      paramClientConfig = this.mDefaultConfig;
    }
    this.mConfig = paramClientConfig;
  }
  
  public void setWebSuggestionsEnabled(boolean paramBoolean)
  {
    this.mSuggestionsController.setWebSuggestionsEnabled(paramBoolean);
  }
  
  public boolean shouldUseMusicHotworder()
  {
    return false;
  }
  
  public void showSuggestions(SuggestionList paramSuggestionList, int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mCallback.showSuggestions(paramSuggestionList.getUserQuery(), paramSuggestionList.getSuggestions().subList(0, paramInt), paramSuggestionList.isFinal(), SearchBoxLogging.createSuggestionsLogInfo(paramSuggestionList.getSuggestions().subList(0, paramInt)));
      return;
    }
    this.mCallback.hideSuggestions();
  }
  
  public void showSuggestions(Suggestions paramSuggestions)
  {
    this.mSuggestionsController.setSuggestions(paramSuggestions);
  }
  
  public boolean startActivity(Intent... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      paramVarArgs[j];
    }
    if (!Feature.EYES_FREE.isEnabled()) {
      if (paramVarArgs.length != 1) {
        break label57;
      }
    }
    label57:
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mCallback.launchIntent(paramVarArgs[0]);
      return true;
    }
  }
  
  public void startQueryEdit()
  {
    if ((this.mSearchController.isAttached(this)) && (!this.mTransitioningToResultsActivity))
    {
      this.mQueryState.resetSearchClient();
      this.mQueryState.startQueryEdit();
    }
  }
  
  public void stopListening()
  {
    if (this.mSearchController.isAttached(this)) {
      this.mQueryState.stopListening(this.mQueryState.getCommittedQuery());
    }
  }
  
  public boolean supportWickedFast()
  {
    return false;
  }
  
  public void unregisterE100Listeners()
  {
    if (this.mWaveGestureListener != null)
    {
      this.mWaveGestureListener.stopListening(this.mContext, this);
      this.mWaveGestureListener = null;
    }
    if (this.mChargingStateListener != null)
    {
      this.mChargingStateListener.stopListening(this.mContext, this);
      this.mChargingStateListener = null;
    }
    if (this.mSMSBroadcastReceiver != null)
    {
      this.mSMSBroadcastReceiver.stopListening(this.mContext);
      this.mSMSBroadcastReceiver = null;
    }
    this.mSearchController.onStoppedListeningForMessages();
  }
  
  private class MySearchPlateUi
    implements SearchPlateUi
  {
    private MySearchPlateUi() {}
    
    public void setExternalFlags(int paramInt, String paramString, boolean paramBoolean)
    {
      LocalSearchService.this.mCallback.setExternalFlags(paramInt, paramString);
    }
    
    public void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence)
    {
      LocalSearchService.this.mCallback.setFinalRecognizedText(paramCharSequence.toString());
    }
    
    public void setQuery(Query paramQuery)
    {
      LocalSearchService.this.mCallback.setQuery(paramQuery);
    }
    
    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      LocalSearchService.this.mCallback.setSearchPlateMode(paramInt1, paramInt2, paramBoolean);
    }
    
    public void showErrorMessage(String paramString)
    {
      LocalSearchService.this.mCallback.showErrorMessage(paramString);
    }
    
    public void showRecognitionState(int paramInt)
    {
      LocalSearchService.this.mCallback.showRecognitionState(paramInt);
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
    {
      LocalSearchService.this.mCallback.updateRecognizedText(paramString1, paramString2);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.LocalSearchService
 * JD-Core Version:    0.7.0.1
 */