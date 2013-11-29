package com.google.android.search.gel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import com.google.android.search.shared.api.ExternalGelSearch;
import com.google.android.search.shared.api.ExternalGelSearch.Callback;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.search.shared.imageloader.CachingImageLoader;
import com.google.android.search.shared.imageloader.ContentProviderImageLoader;
import com.google.android.search.shared.imageloader.ResizingImageLoader;
import com.google.android.search.shared.imageloader.ResourceImageLoader;
import com.google.android.search.shared.service.ClientConfig;
import com.google.android.search.shared.service.SearchServiceClient;
import com.google.android.search.shared.service.SearchServiceClient.ConnectionListener;
import com.google.android.search.shared.service.SearchServiceUiCallback;
import com.google.android.search.shared.ui.LevenshteinSuggestionFormatter;
import com.google.android.search.shared.ui.RecognizerView;
import com.google.android.search.shared.ui.SearchPlate;
import com.google.android.search.shared.ui.SearchPlate.Callback;
import com.google.android.search.shared.ui.SearchPlate.ModeListener;
import com.google.android.search.shared.ui.SuggestionClickListener;
import com.google.android.search.shared.ui.SuggestionFormatter;
import com.google.android.search.shared.ui.SuggestionUiUtils;
import com.google.android.search.shared.ui.SuggestionViewFactory;
import com.google.android.search.shared.ui.TextAppearanceFactory;
import com.google.android.search.shared.ui.ViewRecycler;
import com.google.android.search.shared.ui.util.SearchFormulationLogging;
import com.google.android.shared.util.Animations;
import com.google.android.shared.util.BackgroundUriLoader;
import com.google.android.shared.util.CascadingUriLoader;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.HandlerScheduledExecutor;
import com.google.android.shared.util.PostToExecutorLoader;
import com.google.android.shared.util.PriorityThreadFactory;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.shared.util.SynchronousLoader;
import com.google.android.shared.util.SystemClockImpl;
import com.google.android.shared.util.UriLoader;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchOverlayImpl
  implements SearchOverlay
{
  private View mCircleCenter;
  private final SearchOverlayClipState mClipState;
  private final Clock mClock;
  private final SearchOverlayLayout mContainer;
  private final Context mContext;
  private boolean mDestroyed;
  private final ExternalSearchImpl mExternalGelSearch = new ExternalSearchImpl(null);
  @Nullable
  private SearchFormulationLogging mFormulationLogging;
  private boolean mHasWindowFocus;
  private boolean mHotwordDetectionEnabled;
  private boolean mHotwordDetectionSupported;
  private final boolean mIsLowRamDevice;
  private SearchOverlay.Listener mListener;
  private int mMaxSearchPlateTranslationY;
  private int mNowScroll;
  private final ViewGroup mParent;
  private Intent mPendingLaunchIntent;
  private boolean mPendingQueryCommit;
  private boolean mPendingQueryEdit;
  private boolean mPendingShowKeyboard;
  private float mProximityToNow;
  private Query mQuery = Query.EMPTY;
  private final ScrimView mScrim;
  private final SearchPlate mSearchPlate;
  private final GelSearchPlateContainer mSearchPlateContainer;
  private final SearchPlateUi mSearchPlateUi = new MySearchPlateUi(null);
  private int mSearchPlateUiMode;
  private final SearchServiceClient mSearchService;
  private boolean mSearchStarted;
  private final SpeechLevelSource mSpeechLevelSource = new SpeechLevelSource();
  private boolean mStartingNewActivity;
  private final GelSuggestionsContainer mSuggestionsContainer;
  private final GelSuggestionsController mSuggestionsController;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public SearchOverlayImpl(ViewGroup paramViewGroup, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, GoogleNowPromoController paramGoogleNowPromoController)
  {
    this.mParent = paramViewGroup;
    this.mContext = paramViewGroup.getContext();
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    Resources localResources = this.mContext.getResources();
    LayoutInflater.from(new ContextThemeWrapper(this.mContext, 2131624105)).inflate(2130968816, paramViewGroup);
    this.mContainer = ((SearchOverlayLayout)paramViewGroup.findViewById(2131296959));
    this.mScrim = ((ScrimView)paramViewGroup.findViewById(2131296958));
    this.mSearchPlateContainer = ((GelSearchPlateContainer)paramViewGroup.findViewById(2131296960));
    this.mSearchPlate = ((SearchPlate)paramViewGroup.findViewById(2131296961));
    this.mSuggestionsContainer = ((GelSuggestionsContainer)paramViewGroup.findViewById(2131296962));
    this.mSuggestionsContainer.setClipOffset(localResources.getDimensionPixelSize(2131689576));
    this.mSuggestionsContainer.setSlideDistance(localResources.getDimensionPixelSize(2131689578));
    this.mSuggestionsContainer.setOnVerticalScrollListener(new SuggestionsVerticalScrollListener(null));
    this.mSearchPlate.setSpeechLevelSource(this.mSpeechLevelSource);
    this.mSearchPlate.setModeListener(new SearchPlateModeListener(null));
    SuggestionViewFactory localSuggestionViewFactory = new SuggestionViewFactory();
    int i = localResources.getInteger(2131427376);
    this.mSuggestionsController = new GelSuggestionsController(i, localResources.getInteger(2131427378) - i, this.mSuggestionsContainer, localSuggestionViewFactory, createSuggestionFormatter(), createIconLoader(), createSuggestionViewRecycler(localSuggestionViewFactory), paramGoogleNowPromoController);
    RecognizerView localRecognizerView = (RecognizerView)this.mSearchPlateContainer.findViewById(2131296528);
    SearchOverlayClipAnimation localSearchOverlayClipAnimation = new SearchOverlayClipAnimation(this.mContainer);
    localSearchOverlayClipAnimation.addView(this.mSearchPlate, this.mSearchPlate);
    localSearchOverlayClipAnimation.addView(this.mSearchPlateContainer, this.mSearchPlateContainer);
    localSearchOverlayClipAnimation.addView(this.mSuggestionsContainer, this.mSuggestionsContainer);
    localSearchOverlayClipAnimation.addView(localRecognizerView, localRecognizerView);
    this.mClipState = new SearchOverlayClipState(this.mContainer, this.mSearchPlateContainer, this.mSuggestionsContainer, this.mSearchPlateContainer.findViewById(2131296528), localResources.getDimensionPixelSize(2131689602), localResources.getDimensionPixelSize(2131689562), -1 * localResources.getDimensionPixelSize(2131689575), this.mSuggestionsController, localSearchOverlayClipAnimation, paramGoogleNowPromoController, localResources.getDimensionPixelSize(2131689564));
    this.mIsLowRamDevice = Util.isLowRamDevice(this.mContext);
    setSearchPlateMode(11, 0, true);
    SearchServiceConnectionListener localSearchServiceConnectionListener = new SearchServiceConnectionListener(null);
    this.mSearchPlate.setCallback(new SearchPlateCallbacks(null));
    this.mScrim.setOnTouchListener(new ScrimTouchListener(null));
    this.mScrim.setListener(new ScrimCallbacks(null));
    this.mSearchService = new SearchServiceClient(this.mContext, localSearchServiceConnectionListener, new SearchServiceUiCallbacks(null), new ClientConfig(1));
    this.mSuggestionsController.setSummonsFooterClickListener(new SummonsFooterClickListener(null));
    this.mSuggestionsController.setSuggestionClickListener(new SearchOverlaySuggestionClickListener(null));
    this.mContainer.setPreImeKeyListener(new PreImeKeyListener(null));
    this.mParent.addOnLayoutChangeListener(new ParentLayoutChangeListener(null));
    this.mClock = new SystemClockImpl(this.mContext);
  }
  
  private void clear(boolean paramBoolean)
  {
    if (!paramBoolean) {}
    for (boolean bool = true;; bool = false)
    {
      setSearchPlateMode(11, 0, bool);
      this.mExternalGelSearch.dispose(false);
      this.mPendingQueryCommit = false;
      this.mPendingQueryEdit = false;
      this.mStartingNewActivity = false;
      this.mQuery = Query.EMPTY;
      this.mSearchPlate.setQuery(this.mQuery, false);
      return;
    }
  }
  
  private void clearSearchPlateHint()
  {
    SearchPlate localSearchPlate = this.mSearchPlate;
    if (this.mHotwordDetectionSupported) {}
    for (int i = 2;; i = 0)
    {
      localSearchPlate.setExternalFlags(i, null, true);
      return;
    }
  }
  
  private void commitQuery(Query paramQuery)
  {
    this.mQuery = paramQuery.withSearchBoxStats(this.mFormulationLogging.build());
    if (this.mSearchService.isConnected())
    {
      this.mSearchService.commit(this.mQuery);
      return;
    }
    this.mPendingQueryCommit = true;
  }
  
  private UriLoader<Drawable> createIconLoader()
  {
    int i = Math.round(this.mContext.getResources().getDimension(2131689567));
    return CascadingUriLoader.create(ImmutableList.of(new CachingImageLoader(createUiThreadPostingBackgroundLoader(new ResizingImageLoader(i, i, new ContentProviderImageLoader(this.mContext)))), new ResourceImageLoader(this.mContext)));
  }
  
  private ExecutorService createPooledBackgroundExecutorService()
  {
    return Executors.newCachedThreadPool(new PriorityThreadFactory(10));
  }
  
  private SuggestionFormatter createSuggestionFormatter()
  {
    return new LevenshteinSuggestionFormatter(new TextAppearanceFactory(this.mContext));
  }
  
  private Executor createUiThreadExecutor()
  {
    return new HandlerScheduledExecutor(new Handler(Looper.getMainLooper()), Looper.myQueue());
  }
  
  private <A> UriLoader<A> createUiThreadPostingBackgroundLoader(SynchronousLoader<A> paramSynchronousLoader)
  {
    BackgroundUriLoader localBackgroundUriLoader = new BackgroundUriLoader(createPooledBackgroundExecutorService(), paramSynchronousLoader);
    return new PostToExecutorLoader(createUiThreadExecutor(), localBackgroundUriLoader);
  }
  
  private Query getQuery()
  {
    return this.mQuery;
  }
  
  private String maybeOverrideNowSource(String paramString)
  {
    if (this.mProximityToNow == 1.0F) {
      paramString = "android-search-app";
    }
    return paramString;
  }
  
  private void resetFormulationLogging(Query paramQuery, String paramString)
  {
    Clock localClock = this.mClock;
    if (this.mProximityToNow == 1.0F) {}
    for (boolean bool = true;; bool = false)
    {
      this.mFormulationLogging = new SearchFormulationLogging("gel", paramString, localClock, paramQuery, bool);
      return;
    }
  }
  
  private void resetSearchBoxClientStats(Query paramQuery)
  {
    resetFormulationLogging(paramQuery, "android-search-app");
  }
  
  private void resetSearchBoxClientStats(String paramString)
  {
    resetFormulationLogging(this.mQuery, paramString);
  }
  
  private void setSearchStarted(boolean paramBoolean)
  {
    if (this.mSearchStarted != paramBoolean)
    {
      if (paramBoolean) {
        this.mExternalGelSearch.dispose(false);
      }
      this.mSearchStarted = paramBoolean;
      updateSearchServiceConnection();
    }
  }
  
  private boolean shouldListenForHotword()
  {
    return (this.mHotwordDetectionEnabled) && (this.mHotwordDetectionSupported);
  }
  
  private static void show(View paramView, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, long paramLong2)
  {
    int i = 8;
    if (paramBoolean2)
    {
      if (paramBoolean1) {}
      for (ViewPropertyAnimator localViewPropertyAnimator = Animations.showAndFadeIn(paramView);; localViewPropertyAnimator = Animations.fadeOutAndHide(paramView, i))
      {
        if (paramLong1 > 0L) {
          localViewPropertyAnimator.setDuration(paramLong1);
        }
        if (paramLong2 > 0L) {
          localViewPropertyAnimator.setStartDelay(paramLong2);
        }
        return;
      }
    }
    paramView.animate().cancel();
    if (paramBoolean1) {
      i = 0;
    }
    paramView.setVisibility(i);
    paramView.setAlpha(1.0F);
  }
  
  private void showScrim(boolean paramBoolean1, boolean paramBoolean2)
  {
    show(this.mScrim, paramBoolean1, paramBoolean2, 100L, 0L);
  }
  
  private void updateSearchPlateTranslation()
  {
    this.mContainer.setTranslationY(getSearchPlateTranslation());
  }
  
  private void updateSearchServiceConnection()
  {
    if (((shouldListenForHotword()) || (this.mSearchStarted) || (this.mExternalGelSearch.isActive())) && (!this.mStartingNewActivity) && (!this.mDestroyed))
    {
      if (!this.mSearchService.isConnected()) {
        this.mSearchService.connect();
      }
      return;
    }
    this.mSearchService.disconnect();
  }
  
  public void clearSearchPlate()
  {
    setSearchPlateMode(11, 2, true);
  }
  
  public ViewRecycler createSuggestionViewRecycler(SuggestionViewFactory paramSuggestionViewFactory)
  {
    return new ViewRecycler(paramSuggestionViewFactory.getNumSuggestionViewTypes(), this.mContext.getResources().getInteger(2131427393));
  }
  
  public View getContainer()
  {
    return this.mContainer;
  }
  
  public ExternalGelSearch getGelSearch()
  {
    return this.mExternalGelSearch;
  }
  
  float getSearchPlateTranslation()
  {
    if (this.mSearchPlateUiMode == 11) {
      return -Math.min(this.mMaxSearchPlateTranslationY, this.mNowScroll * this.mProximityToNow);
    }
    return 0.0F;
  }
  
  public void onDestroy()
  {
    this.mDestroyed = true;
    updateSearchServiceConnection();
  }
  
  public void onDoodleChanged(boolean paramBoolean)
  {
    this.mSearchPlateContainer.onDoodleChanged(paramBoolean);
  }
  
  public void onNowScroll(int paramInt)
  {
    this.mNowScroll = Math.min(this.mMaxSearchPlateTranslationY, paramInt);
    updateSearchPlateTranslation();
  }
  
  public void onResume()
  {
    if (this.mStartingNewActivity)
    {
      setSearchStarted(false);
      clear(true);
    }
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    this.mHasWindowFocus = paramBoolean;
    if ((paramBoolean) && (this.mPendingShowKeyboard)) {
      this.mUiThreadExecutor.execute(new Runnable()
      {
        public void run()
        {
          if (SearchOverlayImpl.this.mPendingShowKeyboard)
          {
            SearchOverlayImpl.this.mSearchPlate.focusQueryAndShowKeyboard(false);
            SearchOverlayImpl.access$1302(SearchOverlayImpl.this, false);
          }
        }
      });
    }
  }
  
  public void setHotwordDetectionEnabled(boolean paramBoolean)
  {
    if (this.mIsLowRamDevice) {}
    do
    {
      do
      {
        return;
      } while ((this.mHotwordDetectionEnabled == paramBoolean) && (this.mHotwordDetectionSupported));
      this.mHotwordDetectionEnabled = paramBoolean;
      if (!paramBoolean) {
        break;
      }
      this.mHotwordDetectionSupported = true;
      updateSearchServiceConnection();
    } while (!this.mSearchService.isConnected());
    this.mSearchService.setHotwordDetectionEnabled(paramBoolean);
    return;
    if (this.mSearchService.isConnected())
    {
      this.mSearchService.setHotwordDetectionEnabled(paramBoolean);
      updateSearchServiceConnection();
    }
    clearSearchPlateHint();
  }
  
  public void setListener(SearchOverlay.Listener paramListener)
  {
    this.mListener = paramListener;
    SearchOverlay.Listener localListener;
    if (this.mListener != null)
    {
      localListener = this.mListener;
      if (this.mSearchPlateUiMode == 11) {
        break label36;
      }
    }
    label36:
    for (boolean bool = true;; bool = false)
    {
      localListener.onSearchPlateShown(bool);
      return;
    }
  }
  
  void setMaxSearchPlateTranslationY(int paramInt)
  {
    this.mMaxSearchPlateTranslationY = paramInt;
  }
  
  public void setProximityToNow(float paramFloat)
  {
    this.mProximityToNow = paramFloat;
    this.mSearchPlate.setProximityToNow(paramFloat);
    this.mSearchPlateContainer.setProximityToNow(paramFloat);
    updateSearchPlateTranslation();
  }
  
  void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.mSearchPlateUi.setSearchPlateMode(paramInt1, paramInt2, paramBoolean);
  }
  
  public void startTextSearch(boolean paramBoolean, String paramString)
  {
    if (this.mStartingNewActivity) {
      return;
    }
    this.mQuery = Query.EMPTY;
    resetSearchBoxClientStats(maybeOverrideNowSource(paramString));
    SearchPlate localSearchPlate = this.mSearchPlate;
    Query localQuery = this.mQuery;
    boolean bool1;
    if (!paramBoolean)
    {
      bool1 = true;
      localSearchPlate.setQuery(localQuery, bool1);
      setSearchStarted(true);
      if (paramBoolean) {
        break label94;
      }
    }
    label94:
    for (boolean bool2 = true;; bool2 = false)
    {
      setSearchPlateMode(2, 0, bool2);
      if (!this.mSearchService.isConnected()) {
        break label100;
      }
      this.mSearchService.startQueryEdit();
      return;
      bool1 = false;
      break;
    }
    label100:
    this.mPendingQueryEdit = true;
  }
  
  public void startVoiceSearch(String paramString)
  {
    if (this.mStartingNewActivity) {
      return;
    }
    resetSearchBoxClientStats(maybeOverrideNowSource(paramString));
    setSearchStarted(true);
    setSearchPlateMode(3, 0, false);
    commitQuery(Query.EMPTY.voiceSearchFromGui());
  }
  
  public boolean stopSearch(boolean paramBoolean)
  {
    if ((this.mSearchPlateUiMode == 11) && (!this.mExternalGelSearch.isActive())) {}
    for (int i = 1;; i = 0)
    {
      if ((this.mSearchService.isConnected()) && (!this.mStartingNewActivity)) {
        this.mSearchService.cancel();
      }
      setSearchStarted(false);
      updateSearchServiceConnection();
      clear(paramBoolean);
      if (i != 0) {
        break;
      }
      return true;
    }
    return false;
  }
  
  private class ExternalSearchImpl
    implements ExternalGelSearch
  {
    @Nullable
    private ExternalGelSearch.Callback mCallback;
    private boolean mInError;
    
    private ExternalSearchImpl() {}
    
    public void commit(Query paramQuery, ExternalGelSearch.Callback paramCallback)
    {
      Preconditions.checkArgument(paramQuery.isFromPredictive());
      ExtraPreconditions.checkMainThread();
      if (SearchOverlayImpl.this.mSearchStarted)
      {
        Log.w("SearchOverlay", "Ignoring external search request. Search Started.");
        return;
      }
      if ((isActive()) && (this.mCallback != paramCallback)) {
        paramCallback.onStatusChanged(0, null);
      }
      this.mCallback = paramCallback;
      SearchOverlayImpl.this.resetSearchBoxClientStats(paramQuery);
      SearchOverlayImpl.this.updateSearchServiceConnection();
      SearchOverlayImpl.this.commitQuery(paramQuery);
      this.mCallback.onStatusChanged(1, SearchOverlayImpl.this.mSpeechLevelSource);
    }
    
    void dispose(boolean paramBoolean)
    {
      
      ExternalGelSearch.Callback localCallback;
      if (isActive())
      {
        localCallback = this.mCallback;
        this.mCallback = null;
        if (!paramBoolean) {
          break label40;
        }
      }
      label40:
      for (int i = 2;; i = 0)
      {
        localCallback.onStatusChanged(i, null);
        this.mInError = paramBoolean;
        return;
      }
    }
    
    boolean isActive()
    {
      return this.mCallback != null;
    }
    
    boolean isInError()
    {
      return this.mInError;
    }
  }
  
  private class MySearchPlateUi
    implements SearchPlateUi
  {
    private MySearchPlateUi() {}
    
    public void setExternalFlags(int paramInt, String paramString, boolean paramBoolean)
    {
      SearchOverlayImpl localSearchOverlayImpl = SearchOverlayImpl.this;
      if ((paramInt & 0x2) != 0) {}
      for (boolean bool = true;; bool = false)
      {
        SearchOverlayImpl.access$2002(localSearchOverlayImpl, bool);
        SearchOverlayImpl.this.updateSearchServiceConnection();
        SearchOverlayImpl.this.mSearchPlate.setExternalFlags(paramInt, paramString, paramBoolean);
        return;
      }
    }
    
    public void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence)
    {
      SearchOverlayImpl.this.mSearchPlate.setFinalRecognizedText(paramCharSequence);
    }
    
    public void setQuery(Query paramQuery)
    {
      SearchOverlayImpl.access$1702(SearchOverlayImpl.this, paramQuery);
      SearchOverlayImpl.this.mSearchService.set(paramQuery);
      SearchOverlayImpl.this.mSearchPlate.setQuery(paramQuery, false);
      if (SearchOverlayImpl.this.mFormulationLogging != null) {
        SearchOverlayImpl.this.mFormulationLogging.registerQueryEdit(SearchOverlayImpl.this.mQuery);
      }
    }
    
    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (paramInt1 == 1) {
        paramInt1 = 11;
      }
      while ((SearchOverlayImpl.this.mSearchPlateUiMode == paramInt1) && ((paramInt2 & 0x2) == 0))
      {
        return;
        if (paramInt1 != 11) {
          SearchOverlayImpl.this.setSearchStarted(true);
        }
      }
      SearchOverlayImpl.this.mSearchPlate.setMode(paramInt1, paramInt2, paramBoolean);
    }
    
    public void showErrorMessage(String paramString)
    {
      SearchOverlayImpl.this.mSearchPlate.showErrorMessage(paramString);
    }
    
    public void showRecognitionState(int paramInt)
    {
      SearchOverlayImpl.this.mSearchPlate.showRecognitionState(paramInt, false);
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
    {
      SearchOverlayImpl.this.mSearchPlate.updateRecognizedText(paramString1, paramString2);
    }
  }
  
  private class ParentLayoutChangeListener
    implements View.OnLayoutChangeListener
  {
    private ParentLayoutChangeListener() {}
    
    public void onLayoutChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
    {
      Rect localRect = new Rect(0, 0, SearchOverlayImpl.this.mSearchPlateContainer.getWidth(), SearchOverlayImpl.this.mSearchPlateContainer.getHeight());
      SearchOverlayImpl.this.mParent.offsetDescendantRectToMyCoords(SearchOverlayImpl.this.mSearchPlateContainer, localRect);
      SearchOverlayImpl.access$2402(SearchOverlayImpl.this, localRect.bottom);
      SearchOverlayImpl.this.updateSearchPlateTranslation();
    }
  }
  
  private class PreImeKeyListener
    implements View.OnKeyListener
  {
    private PreImeKeyListener() {}
    
    public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
    {
      if ((paramInt == 4) && (paramKeyEvent.getAction() == 0) && (SearchOverlayImpl.this.mSearchPlateUiMode == 2) && (SearchOverlayImpl.this.mQuery.isTextSearch()) && (TextUtils.isEmpty(SearchOverlayImpl.this.mQuery.getQueryString())))
      {
        SearchOverlayImpl.this.stopSearch(true);
        return true;
      }
      return false;
    }
  }
  
  private class ScrimCallbacks
    implements ScrimView.Callbacks
  {
    private ScrimCallbacks() {}
    
    public Point getCircleCenter()
    {
      View localView = SearchOverlayImpl.this.mCircleCenter;
      if (localView == null) {
        return SearchOverlayImpl.this.mContainer.getLastTouch();
      }
      Rect localRect = new Rect(0, 0, localView.getWidth(), localView.getHeight());
      ((ViewGroup)SearchOverlayImpl.this.mScrim.getParent()).offsetDescendantRectToMyCoords(localView, localRect);
      return new Point(localRect.centerX(), localRect.centerY());
    }
  }
  
  private class ScrimTouchListener
    implements View.OnTouchListener
  {
    private ScrimTouchListener() {}
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      if (paramMotionEvent.getActionMasked() == 0)
      {
        if ((SearchOverlayImpl.this.mSearchPlateUiMode != 7) && (SearchOverlayImpl.this.mSearchPlateUiMode != 8)) {
          SearchOverlayImpl.this.stopSearch(true);
        }
        return true;
      }
      return false;
    }
  }
  
  private class SearchOverlaySuggestionClickListener
    implements SuggestionClickListener
  {
    private SearchOverlaySuggestionClickListener() {}
    
    public void onSuggestionClicked(Suggestion paramSuggestion)
    {
      SearchOverlayImpl.access$3702(SearchOverlayImpl.this, null);
      if (paramSuggestion.isWebSearchSuggestion())
      {
        SearchOverlayImpl.this.setSearchPlateMode(7, 0, false);
        SearchOverlayImpl.this.mSearchPlate.setQuery(SearchOverlayImpl.this.mQuery.withQueryChars(paramSuggestion.getSuggestionQuery()), true);
      }
      SearchOverlayImpl.this.mSearchService.onSuggestionClicked(paramSuggestion, SearchOverlayImpl.this.mFormulationLogging.registerSuggestClick(paramSuggestion).build());
    }
    
    public void onSuggestionQueryRefineClicked(Suggestion paramSuggestion)
    {
      SearchOverlayImpl.this.mFormulationLogging.registerQueryRefinement(paramSuggestion);
      SearchOverlayImpl.this.mSearchPlateUi.setQuery(SearchOverlayImpl.this.getQuery().withQueryChars(paramSuggestion.getSuggestionQuery() + " "));
    }
    
    public void onSuggestionQuickContactClicked(Suggestion paramSuggestion)
    {
      SearchOverlayImpl.this.mSearchService.onQuickContactClicked(paramSuggestion, SearchOverlayImpl.this.mFormulationLogging.registerSuggestClick(paramSuggestion).build());
    }
    
    public void onSuggestionRemoveFromHistoryClicked(final Suggestion paramSuggestion)
    {
      if (paramSuggestion.isHistorySuggestion()) {
        SuggestionUiUtils.showRemoveFromHistoryDialog(SearchOverlayImpl.this.mContext, paramSuggestion, new Runnable()
        {
          public void run()
          {
            SearchOverlayImpl.this.mSearchService.removeSuggestionFromHistory(paramSuggestion);
          }
        });
      }
    }
  }
  
  private class SearchPlateCallbacks
    implements SearchPlate.Callback
  {
    private SearchPlateCallbacks() {}
    
    public void onCancelRecordingClicked()
    {
      SearchOverlayImpl.this.stopSearch(true);
    }
    
    public void onClearButtonClick()
    {
      SearchOverlayImpl.this.mSearchPlateUi.setQuery(SearchOverlayImpl.this.getQuery().withQueryChars(""));
      if (SearchOverlayImpl.this.mFormulationLogging != null) {
        SearchOverlayImpl.this.mFormulationLogging.registerQueryEdit(SearchOverlayImpl.this.getQuery());
      }
    }
    
    public void onCorrectionEnd() {}
    
    public void onLauncherSearchButtonClick()
    {
      SearchOverlayImpl.this.startTextSearch(true, "android-launcher-search");
    }
    
    public void onPromotedSoundSearchClick()
    {
      if (!SearchOverlayImpl.this.mStartingNewActivity)
      {
        Query localQuery = SearchOverlayImpl.this.getQuery().musicSearchFromPromotedQuery();
        SearchOverlayImpl.this.resetSearchBoxClientStats(localQuery);
        SearchOverlayImpl.this.commitQuery(localQuery);
        SearchOverlayImpl.this.mSearchPlate.setMode(5, 0, false);
      }
    }
    
    public void onQueryTextChanged(CharSequence paramCharSequence, int paramInt, boolean paramBoolean)
    {
      SearchOverlayImpl.this.mSearchPlateUi.setQuery(SearchOverlayImpl.this.getQuery().withQueryCharsAndSelection(paramCharSequence, paramInt));
    }
    
    public boolean onSearchBoxEditorAction(int paramInt)
    {
      if (paramInt == 3)
      {
        SearchOverlayImpl.this.commitQuery(SearchOverlayImpl.access$4300(SearchOverlayImpl.this));
        return true;
      }
      return false;
    }
    
    public void onSearchBoxKeyboardFocused()
    {
      if (SearchOverlayImpl.this.mFormulationLogging != null) {
        SearchOverlayImpl.this.mFormulationLogging.registerSearchBoxReady();
      }
    }
    
    public void onSearchBoxTouched()
    {
      if (SearchOverlayImpl.this.mSearchPlateUiMode == 11) {
        SearchOverlayImpl.this.startTextSearch(true, "android-launcher-search");
      }
    }
    
    public void onSearchButtonClick()
    {
      SearchOverlayImpl.this.commitQuery(SearchOverlayImpl.access$4300(SearchOverlayImpl.this));
    }
    
    public void onStartRecordingClicked()
    {
      SearchOverlayImpl.this.startVoiceSearch("android-search-app");
    }
    
    public void onStartVoiceSearchClicked()
    {
      SearchOverlayImpl.this.startVoiceSearch("android-search-app");
    }
    
    public void onStopRecordingClicked()
    {
      if (SearchOverlayImpl.this.mSearchService.isConnected()) {
        SearchOverlayImpl.this.mSearchService.stopListening();
      }
    }
    
    public void onTextSelected(CharSequence paramCharSequence, boolean paramBoolean, int paramInt1, int paramInt2) {}
  }
  
  private class SearchPlateModeListener
    implements SearchPlate.ModeListener
  {
    private SearchPlateModeListener() {}
    
    public void onSearchPlateModeChanged(int paramInt, boolean paramBoolean)
    {
      SearchOverlayImpl.this.mSearchPlateContainer.setMode(paramInt);
      SearchOverlayImpl.this.mScrim.setMode(paramInt, paramBoolean);
      SearchOverlayImpl.this.mClipState.setMode(paramInt, SearchOverlayImpl.this.mSearchPlateUiMode, paramBoolean);
      SearchOverlayImpl.this.mSuggestionsController.setMode(paramInt, paramBoolean, SearchOverlayImpl.this.mFormulationLogging);
      SearchOverlayLayout localSearchOverlayLayout = SearchOverlayImpl.this.mContainer;
      boolean bool1;
      boolean bool2;
      if (paramInt == 3)
      {
        bool1 = true;
        localSearchOverlayLayout.setReverseChildrenDrawingOrder(bool1);
        if (paramBoolean) {
          break label172;
        }
        bool2 = true;
        label90:
        if (paramInt != 11) {
          break label178;
        }
        SearchOverlayImpl.this.showScrim(false, bool2);
        SearchOverlayImpl.this.mSearchPlate.unfocusQueryAndHideKeyboard(true);
        SearchOverlayImpl.access$1302(SearchOverlayImpl.this, false);
        if (SearchOverlayImpl.this.mListener != null) {
          SearchOverlayImpl.this.mListener.onSearchPlateShown(false);
        }
      }
      label172:
      label178:
      label346:
      for (;;)
      {
        SearchOverlayImpl.access$1602(SearchOverlayImpl.this, paramInt);
        SearchOverlayImpl.this.updateSearchPlateTranslation();
        return;
        bool1 = false;
        break;
        bool2 = false;
        break label90;
        SearchOverlayImpl.this.showScrim(true, bool2);
        if (paramInt == 2) {
          if (SearchOverlayImpl.this.mHasWindowFocus)
          {
            SearchOverlayImpl.this.mSearchPlate.focusQueryAndShowKeyboard(true);
            SearchOverlayImpl.access$1302(SearchOverlayImpl.this, false);
            label223:
            SearchOverlayImpl.access$3702(SearchOverlayImpl.this, SearchOverlayImpl.this.mContainer.findViewById(2131296524));
          }
        }
        for (;;)
        {
          if ((SearchOverlayImpl.this.mSearchPlateUiMode != 11) || (SearchOverlayImpl.this.mListener == null)) {
            break label346;
          }
          SearchOverlayImpl.this.mListener.onSearchPlateShown(true);
          break;
          SearchOverlayImpl.access$1302(SearchOverlayImpl.this, true);
          break label223;
          SearchOverlayImpl.this.mSearchPlate.unfocusQueryAndHideKeyboard(true);
          SearchOverlayImpl.access$1302(SearchOverlayImpl.this, false);
          if ((paramInt != 8) && (paramInt != 7)) {
            SearchOverlayImpl.access$3702(SearchOverlayImpl.this, SearchOverlayImpl.this.mContainer.findViewById(2131296528));
          }
        }
      }
    }
    
    public void onSearchPlateModeTransitionsFinished()
    {
      if (SearchOverlayImpl.this.mPendingLaunchIntent != null)
      {
        SearchOverlayImpl.access$3902(SearchOverlayImpl.this, true);
        SearchOverlayImpl.this.mContext.startActivity(SearchOverlayImpl.this.mPendingLaunchIntent);
        SearchOverlayImpl.access$3802(SearchOverlayImpl.this, null);
        SearchOverlayImpl.this.updateSearchServiceConnection();
      }
    }
  }
  
  private class SearchServiceConnectionListener
    implements SearchServiceClient.ConnectionListener
  {
    private SearchServiceConnectionListener() {}
    
    public void onServiceConnected()
    {
      if (SearchOverlayImpl.this.mFormulationLogging != null) {
        SearchOverlayImpl.this.mFormulationLogging.registerServiceConnected();
      }
      if (SearchOverlayImpl.this.mPendingQueryCommit) {
        SearchOverlayImpl.this.mSearchService.commit(SearchOverlayImpl.this.mQuery);
      }
      for (;;)
      {
        SearchOverlayImpl.access$4402(SearchOverlayImpl.this, false);
        SearchOverlayImpl.access$4502(SearchOverlayImpl.this, false);
        SearchOverlayImpl.this.mSearchService.setHotwordDetectionEnabled(SearchOverlayImpl.this.mHotwordDetectionEnabled);
        return;
        if (SearchOverlayImpl.this.mPendingQueryEdit) {
          SearchOverlayImpl.this.mSearchService.startQueryEdit();
        } else {
          SearchOverlayImpl.this.mSearchService.set(SearchOverlayImpl.this.mQuery);
        }
      }
    }
    
    public void onServiceDisconnected() {}
  }
  
  private class SearchServiceUiCallbacks
    implements SearchServiceUiCallback
  {
    private SearchServiceUiCallbacks() {}
    
    public void hideSuggestions()
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSuggestionsController.greyOutSuggestions();
    }
    
    public void launchIntent(Intent paramIntent)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      if (SearchOverlayImpl.this.mSearchPlate.isTransitionRunning())
      {
        SearchOverlayImpl.access$3802(SearchOverlayImpl.this, paramIntent);
        return;
      }
      SearchOverlayImpl.access$3902(SearchOverlayImpl.this, true);
      SearchOverlayImpl.this.mContext.startActivity(paramIntent);
      SearchOverlayImpl.this.updateSearchServiceConnection();
    }
    
    public void onRemoveSuggestionFromHistoryFailed()
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SuggestionUiUtils.showRemoveFromHistoryFailedToast(SearchOverlayImpl.this.mContext);
    }
    
    public boolean resolveIntent(Intent paramIntent)
    {
      return SearchOverlayImpl.this.mContext.getPackageManager().resolveActivity(paramIntent, 65536) != null;
    }
    
    public void setExternalFlags(int paramInt, String paramString)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSearchPlateUi.setExternalFlags(paramInt, paramString, false);
    }
    
    public void setFinalRecognizedText(String paramString)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSearchPlateUi.setFinalRecognizedText(paramString);
    }
    
    public void setQuery(Query paramQuery)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {}
      while ((!TextUtils.equals(SearchOverlayImpl.this.mQuery.getQueryString(), paramQuery.getQueryString())) && (!paramQuery.isVoiceSearch())) {
        return;
      }
      SearchOverlayImpl.access$1702(SearchOverlayImpl.this, paramQuery);
      SearchOverlayImpl.this.mSearchPlate.setQuery(paramQuery, true);
    }
    
    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.setSearchPlateMode(paramInt1, paramInt2, paramBoolean);
    }
    
    public void showErrorMessage(String paramString)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {}
      do
      {
        return;
        if (SearchOverlayImpl.this.mExternalGelSearch.isActive())
        {
          SearchOverlayImpl.this.mExternalGelSearch.dispose(true);
          return;
        }
      } while (SearchOverlayImpl.this.mExternalGelSearch.isInError());
      SearchOverlayImpl.this.mSearchPlateUi.showErrorMessage(paramString);
    }
    
    public void showRecognitionState(int paramInt)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSearchPlateUi.showRecognitionState(paramInt);
    }
    
    public void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.access$1702(SearchOverlayImpl.this, paramQuery);
      SearchOverlayImpl.this.mSuggestionsController.onUpdateStart();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Suggestion localSuggestion = (Suggestion)localIterator.next();
        if (localSuggestion.isCorrectionSuggestion())
        {
          CharSequence localCharSequence = localSuggestion.getSuggestionText1();
          if ((localCharSequence != null) && ((localCharSequence instanceof Spanned))) {
            SearchOverlayImpl.this.mSearchPlate.setTextQueryCorrections((Spanned)localCharSequence, false);
          }
        }
        else
        {
          SearchOverlayImpl.this.mSuggestionsController.onUpdateAdd(paramQuery, localSuggestion);
        }
      }
      SearchOverlayImpl.this.mSuggestionsController.onUpdateEnd(paramQuery, paramBoolean, paramSuggestionLogInfo);
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSearchPlateUi.updateRecognizedText(paramString1, paramString2);
    }
    
    public void updateSpeechLevel(int paramInt)
    {
      if (!SearchOverlayImpl.this.mSearchService.isConnected()) {
        return;
      }
      SearchOverlayImpl.this.mSpeechLevelSource.setSpeechLevel(paramInt);
    }
  }
  
  private class SuggestionsVerticalScrollListener
    implements GelSuggestionsContainer.OnVerticalScrollListener
  {
    private SuggestionsVerticalScrollListener() {}
    
    public void onVerticalScrollDetected()
    {
      SearchOverlayImpl.this.mSearchPlate.unfocusQueryAndHideKeyboard(true);
      SearchOverlayImpl.access$1302(SearchOverlayImpl.this, false);
    }
  }
  
  private class SummonsFooterClickListener
    implements View.OnClickListener
  {
    private SummonsFooterClickListener() {}
    
    public void onClick(View paramView)
    {
      SearchOverlayImpl.this.commitQuery(SearchOverlayImpl.this.mQuery.withCorpus("summons"));
      SearchOverlayImpl.this.setSearchPlateMode(7, 0, false);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.SearchOverlayImpl
 * JD-Core Version:    0.7.0.1
 */