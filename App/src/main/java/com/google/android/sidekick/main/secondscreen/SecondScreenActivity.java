package com.google.android.sidekick.main.secondscreen;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.OnDismissListener;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.OnScrollViewHider;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SendGoogleFeedback;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.InProcessPredictiveCardContainer;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.actions.RemoveFeedbackPromptEntryUpdater;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.inject.VelvetImageGalleryHelper;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.tv.TvConfig;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.EntryTreeConverter;
import com.google.android.sidekick.shared.client.NowCardsViewWrapper;
import com.google.android.sidekick.shared.client.NowCardsViewWrapper.CardsObserver;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.client.ScrollableCardView;
import com.google.android.sidekick.shared.client.TvRecognitionManager;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.client.ViewActionRecorder;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.ui.PullToRefreshHandler;
import com.google.android.sidekick.shared.ui.PullToRefreshHandler.Listener;
import com.google.android.sidekick.shared.util.ClickActionHelper;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.SecondScreenUtil.Options;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.Help;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.android.velvet.ui.PopupMenuController;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class SecondScreenActivity
  extends Activity
  implements SuggestionGridLayout.OnDismissListener, EntriesLoaderFragment.EntriesLoaderListener, NowCardsViewWrapper.CardsObserver, ScrollableCardView, PullToRefreshHandler.Listener
{
  private static final String TAG = Tag.getTag(SecondScreenActivity.class);
  private PredictiveCardContainer mCardContainer;
  private boolean mCardsFinishedLoading;
  private NowCardsViewWrapper mCardsWrapper;
  private SearchConfig mConfig;
  private boolean mDestroyed;
  private EntryTreeConverter mEntryTreeConverter;
  private View mFooter;
  private OnScrollViewHider mFooterHider;
  private SuggestionGridLayout mGridLayout;
  @Nullable
  private PopupMenuController mMenuController;
  private SecondScreenUtil.Options mOptions;
  private NowProgressBar mProgressBar;
  private boolean mReadyForDeal;
  private EntriesLoaderFragment mResultsLoader;
  private CoScrollContainer mScrollView;
  @Nullable
  private Runnable mShowLoadingAnimationRunnable;
  private ImageButton mTrainingButton;
  private View mTrainingPeekView;
  private ScheduledSingleThreadedExecutor mUiExector;
  private UserInteractionLogger mUserInteractionLogger;
  private ViewActionRecorder mViewActionRecorder;
  
  private void initContextHeader(ViewGroup paramViewGroup)
  {
    int i = LayoutUtils.getContentPadding(this, getResources().getDisplayMetrics().widthPixels);
    View localView1 = findViewById(2131296983);
    localView1.setPadding(i, localView1.getPaddingTop(), i, localView1.getPaddingBottom());
    TextView localTextView = (TextView)paramViewGroup.findViewById(2131296382);
    localTextView.setText(this.mOptions.getTitle());
    localTextView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SecondScreenActivity.this.finish();
      }
    });
    Sidekick.Photo localPhoto = this.mOptions.getContextHeaderImage();
    WebImageView localWebImageView;
    Point localPoint;
    int k;
    if ((localPhoto != null) && (!TextUtils.isEmpty(localPhoto.getUrl())))
    {
      localWebImageView = (WebImageView)paramViewGroup.findViewById(2131296484);
      int j = getResources().getDimensionPixelSize(2131689853);
      localPoint = LayoutUtils.getContextHeaderSize(this);
      k = localPoint.y - j;
      localWebImageView.getLayoutParams().width = localPoint.x;
      localWebImageView.getLayoutParams().height = k;
      if (localPhoto.getUrlType() != 1) {
        break label294;
      }
    }
    label294:
    for (Uri localUri = new FifeImageUrlUtil().setImageUrlCenterCrop(localPoint.x, k, localPhoto.getUrl());; localUri = Uri.parse(localPhoto.getUrl()))
    {
      localWebImageView.setImageUri(localUri, this.mCardContainer.getImageLoader());
      if (localPhoto.hasClickAction()) {
        localWebImageView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            ClickActionHelper.performClick(SecondScreenActivity.this.mCardContainer, this.val$clickAction, true);
          }
        });
      }
      if (this.mOptions.isTvButtonEnabled())
      {
        final String str = TvConfig.getTvSearchQuery(this.mConfig, Locale.getDefault());
        if (!TextUtils.isEmpty(str))
        {
          View localView2 = paramViewGroup.findViewById(2131296984);
          localView2.setVisibility(0);
          localView2.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymousView)
            {
              SecondScreenActivity.this.mCardContainer.startWebSearch(str, null);
            }
          });
        }
      }
      return;
    }
  }
  
  private void initFooter()
  {
    this.mFooter.findViewById(2131296719).setVisibility(4);
    this.mTrainingButton.setOnClickListener(new TrainingClickListener(null));
    ImageButton localImageButton = (ImageButton)this.mFooter.findViewById(2131296722);
    if (ViewConfiguration.get(this).hasPermanentMenuKey())
    {
      localImageButton.setVisibility(8);
      this.mFooter.findViewById(2131296721).setVisibility(8);
    }
    for (;;)
    {
      this.mFooterHider = new OnScrollViewHider(this.mFooter, this.mScrollView, false);
      this.mFooterHider.setStickiness(2, true, true);
      int i = getResources().getDimensionPixelSize(2131689479);
      this.mGridLayout.setPadding(this.mGridLayout.getPaddingLeft(), this.mGridLayout.getPaddingTop(), this.mGridLayout.getPaddingRight(), i + this.mGridLayout.getPaddingBottom());
      this.mScrollView.setHeaderAndFooterPadding(0, i);
      return;
      this.mMenuController = new PopupMenuController(this);
      localImageButton.setOnClickListener(this.mMenuController);
    }
  }
  
  private void loadResults()
  {
    scheduleLoadingAnimation();
    this.mReadyForDeal = true;
    preloadResults();
  }
  
  private void maybeDealCards()
  {
    if ((!this.mReadyForDeal) || (!this.mCardsFinishedLoading)) {
      return;
    }
    if (this.mResultsLoader.hasError())
    {
      onError();
      return;
    }
    List localList = this.mEntryTreeConverter.apply(this.mResultsLoader.getEntryTree());
    NowCardsViewWrapper localNowCardsViewWrapper = this.mCardsWrapper;
    CardRenderingContext localCardRenderingContext = this.mResultsLoader.getCardRenderingContext();
    if (!this.mOptions.isCardDismissDisabled()) {}
    for (boolean bool = true;; bool = false)
    {
      localNowCardsViewWrapper.addCards(this, localList, localCardRenderingContext, false, true, bool, null, null, null, 0);
      return;
    }
  }
  
  private void maybeStartHeaderAnimation(SecondScreenContextHeader paramSecondScreenContextHeader, boolean paramBoolean)
  {
    this.mOptions.getLureEntry();
    paramSecondScreenContextHeader.findViewById(2131296983).setAlpha(1.0F);
    paramSecondScreenContextHeader.findViewById(2131296484).setAlpha(1.0F);
    this.mScrollView.setBackgroundColor(getResources().getColor(2131230799));
    loadResults();
  }
  
  private void preloadResults()
  {
    this.mResultsLoader = ((EntriesLoaderFragment)getFragmentManager().findFragmentByTag("results_loader"));
    if (this.mResultsLoader == null)
    {
      this.mResultsLoader = new EntriesLoaderFragment();
      getFragmentManager().beginTransaction().add(this.mResultsLoader, "results_loader").commit();
      this.mResultsLoader.load(this.mOptions.getInterest());
    }
    while (!this.mReadyForDeal) {
      return;
    }
    if (this.mResultsLoader.hasError())
    {
      onError();
      return;
    }
    onEntriesUpdated();
  }
  
  private void scheduleLoadingAnimation()
  {
    this.mShowLoadingAnimationRunnable = new Runnable()
    {
      public void run()
      {
        SecondScreenActivity.this.mProgressBar.start();
        SecondScreenActivity.access$502(SecondScreenActivity.this, null);
      }
    };
    this.mUiExector.executeDelayed(this.mShowLoadingAnimationRunnable, 1000L);
  }
  
  private void stopLoadingAnimation()
  {
    this.mProgressBar.stop();
    if (this.mShowLoadingAnimationRunnable != null)
    {
      this.mUiExector.cancelExecute(this.mShowLoadingAnimationRunnable);
      this.mShowLoadingAnimationRunnable = null;
    }
  }
  
  public ScrollViewControl getScrollViewControl()
  {
    return this.mScrollView;
  }
  
  public SuggestionGridLayout getSuggestionGridLayout()
  {
    return this.mGridLayout;
  }
  
  public boolean isPredictiveOnlyMode()
  {
    return true;
  }
  
  public boolean isVisible()
  {
    return true;
  }
  
  public void notifyCardVisible(PredictiveCardWrapper paramPredictiveCardWrapper)
  {
    paramPredictiveCardWrapper.getEntryCardViewAdapter().onViewVisibleOnScreen(this.mCardContainer);
  }
  
  public void onBackPressed()
  {
    if (this.mCardsWrapper.isTrainingModeShowing())
    {
      this.mCardsWrapper.commitAllFeedback(true);
      this.mUserInteractionLogger.logAnalyticsAction("BACK_BUTTON_CLOSE_FEEDBACK", null);
      return;
    }
    super.onBackPressed();
  }
  
  public void onCardsAdded()
  {
    this.mViewActionRecorder.recordViewStartTimes();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mCardsFinishedLoading = false;
    this.mReadyForDeal = false;
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    this.mUiExector = localVelvetServices.getAsyncServices().getUiThreadExecutor();
    this.mUserInteractionLogger = localVelvetServices.getCoreServices().getUserInteractionLogger();
    this.mEntryTreeConverter = new EntryTreeConverter(localSidekickInjector.getEntryCardViewFactory());
    this.mViewActionRecorder = new ViewActionRecorder(getApplicationContext(), localVelvetServices.getCoreServices().getClock(), localSidekickInjector.getExecutedUserActionStore());
    this.mViewActionRecorder.setScrollableCardView(this);
    this.mConfig = localVelvetServices.getCoreServices().getConfig();
    this.mOptions = ((SecondScreenUtil.Options)getIntent().getParcelableExtra("options"));
    this.mCardContainer = new MyCardContainer(getApplicationContext(), this.mUserInteractionLogger, localSidekickInjector.getNetworkClient(), localSidekickInjector.getNowConfigurationPreferencesSupplier(), localSidekickInjector.getLocationReportingOptInHelper(), localSidekickInjector.getCalendarDataProvider(), localVelvetServices.getCoreServices().getLoginHelper(), localVelvetServices.getGlobalSearchServices().getSearchHistoryHelper(), localSidekickInjector.getStaticMapLoader(), localSidekickInjector.getTrainingQuestionManager(), localVelvetServices.getImageLoader(), localVelvetServices.getNonCachingImageLoader(), localSidekickInjector.getVelvetImageGalleryHelper(), localSidekickInjector.getFirstUseCardHandler(), localSidekickInjector.getUndoDismissManager(), localSidekickInjector.getReminderSmartActionUtil(), null);
    requestWindowFeature(1);
    setContentView(2130968821);
    this.mScrollView = ((CoScrollContainer)findViewById(2131296980));
    this.mGridLayout = ((SuggestionGridLayout)findViewById(2131296790));
    this.mProgressBar = ((NowProgressBar)findViewById(2131296791));
    this.mFooter = findViewById(2131296981);
    this.mTrainingButton = ((ImageButton)this.mFooter.findViewById(2131296720));
    this.mTrainingPeekView = findViewById(2131297129);
    SecondScreenContextHeader localSecondScreenContextHeader = (SecondScreenContextHeader)findViewById(2131296483);
    initContextHeader(localSecondScreenContextHeader);
    initFooter();
    this.mTrainingPeekView.setOnClickListener(new TrainingClickListener(null));
    if (!this.mOptions.isRefreshDisabled()) {
      new PullToRefreshHandler(this, this.mScrollView, this.mGridLayout, this.mProgressBar, this).register();
    }
    this.mCardsWrapper = new NowCardsViewWrapper(this.mUiExector, this, this.mGridLayout, this.mScrollView, this.mTrainingPeekView, findViewById(2131297130), null, this.mTrainingButton, null, this.mCardContainer);
    this.mCardsWrapper.registerListeners();
    this.mGridLayout.setOnDismissListener(this);
    if ((paramBundle != null) && (paramBundle.getBoolean("changing_config", false))) {}
    for (boolean bool = true;; bool = false)
    {
      maybeStartHeaderAnimation(localSecondScreenContextHeader, bool);
      return;
    }
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    super.onCreateOptionsMenu(paramMenu);
    if (this.mOptions.getHelpContextId() != null) {
      new Help(this).addHelpMenuItem(paramMenu, this.mOptions.getHelpContextId());
    }
    paramMenu.add(2131363570).setOnMenuItemClickListener(new FeedbackClickListener(null));
    return true;
  }
  
  protected void onDestroy()
  {
    stopLoadingAnimation();
    super.onDestroy();
    this.mDestroyed = true;
  }
  
  public void onEntriesUpdated()
  {
    if (this.mResultsLoader.getEntryTree() == null) {
      return;
    }
    this.mCardsFinishedLoading = true;
    stopLoadingAnimation();
    maybeDealCards();
  }
  
  public void onError()
  {
    Toast.makeText(getApplicationContext(), 2131362813, 0).show();
    finish();
  }
  
  protected void onPause()
  {
    super.onPause();
    if (this.mMenuController != null) {
      this.mMenuController.ensureClosed();
    }
    this.mViewActionRecorder.recordViewEndTimes();
    this.mViewActionRecorder.removeViewActionListeners();
  }
  
  public void onRefreshRequested()
  {
    this.mCardsWrapper.commitAllFeedback(false);
    this.mResultsLoader.load(this.mOptions.getInterest());
  }
  
  protected void onResume()
  {
    super.onResume();
    this.mViewActionRecorder.addViewActionListeners();
    this.mViewActionRecorder.recordViewStartTimes();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    if (isChangingConfigurations()) {
      paramBundle.putBoolean("changing_config", true);
    }
    super.onSaveInstanceState(paramBundle);
  }
  
  protected void onStop()
  {
    super.onStop();
    if (!isChangingConfigurations()) {
      this.mCardsWrapper.commitAllFeedback(false);
    }
  }
  
  public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss)
  {
    this.mCardsWrapper.onViewsDismissed(paramPendingViewDismiss);
  }
  
  private class FeedbackClickListener
    implements MenuItem.OnMenuItemClickListener
  {
    private FeedbackClickListener() {}
    
    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      SendGoogleFeedback.launchGoogleFeedback(SecondScreenActivity.this.getApplicationContext(), SecondScreenActivity.this.getWindow().getDecorView().getRootView());
      return true;
    }
  }
  
  private class MyCardContainer
    extends InProcessPredictiveCardContainer
  {
    private final IntentStarter mIntentStarter = new ActivityIntentStarter(SecondScreenActivity.this, 0);
    
    private MyCardContainer(UserInteractionLogger paramUserInteractionLogger, NetworkClient paramNetworkClient, Supplier<NowConfigurationPreferences> paramSupplier, LocationReportingOptInHelper paramLocationReportingOptInHelper, CalendarDataProvider paramCalendarDataProvider, LoginHelper paramLoginHelper, SearchHistoryHelper paramSearchHistoryHelper, StaticMapLoader paramStaticMapLoader, TrainingQuestionManager paramTrainingQuestionManager, UriLoader<Drawable> paramUriLoader1, UriLoader<Drawable> paramUriLoader2, VelvetImageGalleryHelper paramVelvetImageGalleryHelper, FirstUseCardHandler paramFirstUseCardHandler, UndoDismissManager paramUndoDismissManager, ReminderSmartActionUtil paramReminderSmartActionUtil)
    {
      super(paramNetworkClient, paramSupplier, paramLocationReportingOptInHelper, paramCalendarDataProvider, paramLoginHelper, paramSearchHistoryHelper, paramStaticMapLoader, paramTrainingQuestionManager, paramUriLoader1, paramUriLoader2, paramVelvetImageGalleryHelper, paramFirstUseCardHandler, paramUndoDismissManager, paramReminderSmartActionUtil, localReminderSmartActionUtil);
    }
    
    protected void dismissEntryImpl(Sidekick.Entry paramEntry)
    {
      SecondScreenActivity.this.mResultsLoader.removeEntry(paramEntry);
      SecondScreenActivity.this.mCardsWrapper.dismissEntry(paramEntry, null);
    }
    
    protected void dismissGroupChildEntryImpl(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
    {
      SecondScreenActivity.this.mResultsLoader.removeGroupChildEntry(paramEntry1, paramEntry2);
      SecondScreenActivity.this.mCardsWrapper.dismissEntry(paramEntry1, ImmutableSet.of(paramEntry2));
    }
    
    public CardRenderingContext getCardRenderingContext()
    {
      if (SecondScreenActivity.this.mResultsLoader != null) {
        return SecondScreenActivity.this.mResultsLoader.getCardRenderingContext();
      }
      return null;
    }
    
    @Nullable
    public IntentStarter getIntentStarter()
    {
      return this.mIntentStarter;
    }
    
    @Nullable
    public TvRecognitionManager getTvRecognitionManager()
    {
      return null;
    }
    
    public void invalidateEntries()
    {
      SecondScreenActivity.this.onRefreshRequested();
    }
    
    public void pulseTrainingIcon()
    {
      SecondScreenActivity.this.mCardsWrapper.pulseTrainIcon();
    }
    
    public void recordFeedbackPromptAction(Sidekick.Entry paramEntry, int paramInt)
    {
      logAction(paramEntry, paramInt, null);
      SecondScreenActivity.this.mResultsLoader.updateEntries(new RemoveFeedbackPromptEntryUpdater(paramEntry));
    }
    
    public void refreshEntries()
    {
      SecondScreenActivity.this.onRefreshRequested();
    }
    
    public void setCardRenderingContext(CardRenderingContext paramCardRenderingContext)
    {
      if (SecondScreenActivity.this.mResultsLoader != null) {
        SecondScreenActivity.this.mResultsLoader.setCardRenderingContext(paramCardRenderingContext);
      }
    }
    
    public void snoozeReminder(Sidekick.Entry paramEntry) {}
    
    public boolean startWebSearch(String paramString, @Nullable Location paramLocation)
    {
      Intent localIntent = IntentUtils.createResumeVelvetWithQueryIntent(SecondScreenActivity.this, Query.EMPTY.withQueryChars(paramString));
      localIntent.putExtra("from_self", true);
      if (paramLocation != null) {
        localIntent.putExtra("location", paramLocation);
      }
      SecondScreenActivity.this.startActivity(localIntent);
      return true;
    }
    
    public void toggleBackOfCard(EntryCardViewAdapter paramEntryCardViewAdapter)
    {
      SecondScreenActivity.this.mCardsWrapper.toggleBackOfCard(paramEntryCardViewAdapter, true, null);
    }
  }
  
  private class TrainingClickListener
    implements View.OnClickListener
  {
    private TrainingClickListener() {}
    
    public void onClick(View paramView)
    {
      IntentDispatcherUtil.dispatchIntent(SecondScreenActivity.this, "com.google.android.googlequicksearchbox.TRAINING_CLOSET");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.secondscreen.SecondScreenActivity
 * JD-Core Version:    0.7.0.1
 */