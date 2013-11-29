package com.google.android.sidekick.shared.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.search.shared.api.ExternalGelSearch;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SystemClockImpl;
import com.google.android.sidekick.shared.ExecutedUserActionWriter;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapterFactory;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import java.util.List;

public class NowOverlay
{
  private final boolean DBG = false;
  private final String TAG = Tag.getTag(NowOverlay.class);
  private final Context mAppContext;
  private final NowClientCardsView mCardsView;
  private final NowRemoteClient.NowRemoteClientLock mConnectionLock;
  private final NowRemoteClient mNowRemoteClient;
  private boolean mPaused = true;
  private final PredictiveCardRefreshManager mRefreshManager;
  private boolean mResetOnNextHide = false;
  private final CardsResetReceiver mResetReceiver = new CardsResetReceiver(null);
  private boolean mResetReceiverRegistered = false;
  private final UndoDismissManager mUndoDismissManager;
  private boolean mVisible = false;
  
  NowOverlay(Context paramContext, NowRemoteClient paramNowRemoteClient, NowClientCardsView paramNowClientCardsView, PredictiveCardRefreshManager paramPredictiveCardRefreshManager, UndoDismissManager paramUndoDismissManager)
  {
    this.mAppContext = paramContext;
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mCardsView = paramNowClientCardsView;
    this.mRefreshManager = paramPredictiveCardRefreshManager;
    this.mUndoDismissManager = paramUndoDismissManager;
    this.mConnectionLock = this.mNowRemoteClient.newConnectionLock(this.TAG);
  }
  
  public static NowOverlay create(Activity paramActivity, NowRemoteClient paramNowRemoteClient, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, IntentStarter paramIntentStarter, Supplier<ExternalGelSearch> paramSupplier)
  {
    ContextThemeWrapper localContextThemeWrapper = new ContextThemeWrapper(paramActivity, 16973934);
    final NowClientCardsView localNowClientCardsView = (NowClientCardsView)LayoutInflater.from(localContextThemeWrapper).inflate(2130968762, null);
    Context localContext = paramActivity.getApplicationContext();
    SystemClockImpl localSystemClockImpl = new SystemClockImpl(localContext);
    ActivityHelper localActivityHelper = new ActivityHelper(paramScheduledSingleThreadedExecutor);
    ViewActionRecorder localViewActionRecorder = new ViewActionRecorder(localContext, localSystemClockImpl, new NowClientExecutedUserActionWriter(paramNowRemoteClient, null));
    PredictiveCardRefreshManager localPredictiveCardRefreshManager = new PredictiveCardRefreshManager(localContext, paramScheduledSingleThreadedExecutor, new EntryCardViewAdapterFactory(localSystemClockImpl, new DirectionsLauncher(localContext, localActivityHelper), (WifiManager)localContext.getSystemService("wifi"), localActivityHelper, paramScheduledSingleThreadedExecutor), localActivityHelper, paramNowRemoteClient, localViewActionRecorder, new FifeImageUrlUtil(), paramNowRemoteClient.getImageLoader());
    localPredictiveCardRefreshManager.setPresenter(localNowClientCardsView);
    UndoDismissManager localUndoDismissManager = new UndoDismissManager(localContext, paramScheduledSingleThreadedExecutor);
    GelTvRecognitionManager localGelTvRecognitionManager = new GelTvRecognitionManager(paramSupplier);
    ClientCardContainer localClientCardContainer = new ClientCardContainer(localContext, paramIntentStarter, paramNowRemoteClient, localGelTvRecognitionManager, localUndoDismissManager);
    NowCardsViewWrapper.CardsObserver local1 = new NowCardsViewWrapper.CardsObserver()
    {
      public void onCardsAdded()
      {
        this.val$refreshManager.recordViewStartTimes();
        this.val$refreshManager.stopProgressBar();
        localNowClientCardsView.layoutIfGone();
      }
    };
    NowCardsViewWrapper localNowCardsViewWrapper = new NowCardsViewWrapper(paramScheduledSingleThreadedExecutor, local1, localNowClientCardsView.getSuggestionGridLayout(), localNowClientCardsView.getScrollViewControl(), localNowClientCardsView.getTrainingPeekView(), localNowClientCardsView.getTrainingPeekIcon(), localNowClientCardsView.getRemindersPeekView(), localNowClientCardsView.getTrainingFooterIcon(), localNowClientCardsView.getRemindersFooterView(), localClientCardContainer);
    localClientCardContainer.setCardsView(localNowCardsViewWrapper);
    localNowClientCardsView.setDependencies(paramActivity, paramIntentStarter, localPredictiveCardRefreshManager, localNowCardsViewWrapper, paramNowRemoteClient, localUndoDismissManager);
    return new NowOverlay(localContext, paramNowRemoteClient, localNowClientCardsView, localPredictiveCardRefreshManager, localUndoDismissManager);
  }
  
  public void addScrollListener(ScrollViewControl.ScrollListener paramScrollListener)
  {
    this.mCardsView.getScrollViewControl().addScrollListener(paramScrollListener);
  }
  
  public ScrollViewControl getScrollViewControl()
  {
    return this.mCardsView.getScrollViewControl();
  }
  
  public View getView()
  {
    return this.mCardsView;
  }
  
  public void hideViewsForSearch()
  {
    this.mCardsView.hideViewsForSearch();
  }
  
  public boolean isPaused()
  {
    return this.mPaused;
  }
  
  public boolean onBackPressed()
  {
    return this.mCardsView.onBackPressed();
  }
  
  public void onDestroy()
  {
    onHide();
    onPause();
    if (this.mResetReceiverRegistered)
    {
      this.mAppContext.unregisterReceiver(this.mResetReceiver);
      this.mResetReceiverRegistered = false;
    }
    this.mPaused = true;
  }
  
  public void onHide()
  {
    if (!this.mVisible) {}
    do
    {
      return;
      this.mVisible = false;
      this.mUndoDismissManager.dismissPending();
      this.mCardsView.onHide();
      this.mNowRemoteClient.pauseImageLoading();
    } while (!this.mResetOnNextHide);
    this.mRefreshManager.buildView();
    this.mResetOnNextHide = false;
  }
  
  public void onPause()
  {
    this.mPaused = true;
    this.mUndoDismissManager.dismissPending();
    this.mRefreshManager.unregisterPredictiveCardsListeners();
    this.mConnectionLock.release();
  }
  
  public void onResume()
  {
    this.mPaused = false;
    if (!this.mResetReceiverRegistered)
    {
      this.mAppContext.registerReceiver(this.mResetReceiver, new IntentFilter("com.google.android.apps.now.ENTRIES_UPDATED"));
      this.mResetReceiverRegistered = true;
    }
    this.mNowRemoteClient.pauseImageLoading();
    this.mConnectionLock.acquire();
    this.mRefreshManager.registerPredictiveCardsListeners();
    this.mRefreshManager.buildView();
  }
  
  public void onShow(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mVisible) {
      return;
    }
    this.mVisible = true;
    if (this.mNowRemoteClient.isConnected())
    {
      new ShowActionLogger(this.mNowRemoteClient, paramBoolean2, null).run();
      if (!paramBoolean1) {
        break label110;
      }
      this.mCardsView.resetView();
      this.mRefreshManager.reset();
      this.mCardsView.onShow();
      this.mRefreshManager.buildView();
    }
    for (;;)
    {
      this.mNowRemoteClient.resumeImageLoading();
      return;
      this.mNowRemoteClient.registerConnectionListener(new ShowActionLogger(this.mNowRemoteClient, paramBoolean2, null));
      this.mConnectionLock.acquire();
      break;
      label110:
      this.mCardsView.onShow();
    }
  }
  
  public void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mCardsView.setAllowedSwipeDirections(paramBoolean1, paramBoolean2);
  }
  
  public void setDoodleListener(NowClientCardsView.DoodleListener paramDoodleListener)
  {
    this.mCardsView.setDoodleListener(paramDoodleListener);
  }
  
  public void setProximityToNow(float paramFloat)
  {
    this.mCardsView.setProximityToNow(paramFloat);
  }
  
  public void showViewsForSearch()
  {
    this.mCardsView.showViewsForSearch();
  }
  
  private class CardsResetReceiver
    extends BroadcastReceiver
  {
    private CardsResetReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent == null) {}
      do
      {
        return;
        switch (paramIntent.getIntExtra("type", -1))
        {
        case 3: 
        default: 
          return;
        case 0: 
          int i = paramIntent.getIntExtra("refresh_type", 0);
          if ((!NowOverlay.this.mPaused) && (NowOverlay.this.mCardsView.isVisible()) && (i == 1))
          {
            NowOverlay.access$502(NowOverlay.this, true);
            return;
          }
          break;
        }
      } while (!NowOverlay.this.mPaused);
      NowOverlay.this.mRefreshManager.reset();
      NowOverlay.this.mCardsView.resetView();
    }
  }
  
  private static class NowClientExecutedUserActionWriter
    implements ExecutedUserActionWriter
  {
    private final NowRemoteClient mCardsManager;
    
    private NowClientExecutedUserActionWriter(NowRemoteClient paramNowRemoteClient)
    {
      this.mCardsManager = paramNowRemoteClient;
    }
    
    public void saveAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
    {
      this.mCardsManager.recordAction(paramEntry, paramAction.getType());
    }
    
    public void saveClickAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.ClickAction paramClickAction) {}
    
    public void saveExecutedUserActions(List<Sidekick.ExecutedUserAction> paramList)
    {
      this.mCardsManager.recordExecutedUserActions(paramList);
    }
    
    public void saveViewAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, long paramLong, int paramInt, boolean paramBoolean)
    {
      if (paramAction.getType() == 21) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkArgument(bool);
        this.mCardsManager.recordViewAction(paramEntry, paramLong, paramInt, paramBoolean);
        return;
      }
    }
  }
  
  private static class ShowActionLogger
    implements Runnable
  {
    private final boolean mFromAssistGesture;
    private final NowRemoteClient mNowRemoteClient;
    
    private ShowActionLogger(NowRemoteClient paramNowRemoteClient, boolean paramBoolean)
    {
      this.mNowRemoteClient = paramNowRemoteClient;
      this.mFromAssistGesture = paramBoolean;
    }
    
    private String getShowType()
    {
      if (this.mFromAssistGesture) {
        return "AssistGesture";
      }
      return "Swipe";
    }
    
    public void run()
    {
      LoggingRequest localLoggingRequest = LoggingRequest.forAnalyticsAction("LAUNCHER_NOW_SCREEN_SHOWN", getShowType());
      this.mNowRemoteClient.logAction(localLoggingRequest);
      this.mNowRemoteClient.unregisterConnectionListener(this);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.NowOverlay
 * JD-Core Version:    0.7.0.1
 */