package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.CoScrollContainer.LayoutParams;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;
import com.google.android.voicesearch.CardFactory;
import com.google.android.voicesearch.EffectOnWebResults;
import com.google.android.voicesearch.audio.TtsAudioPlayer;
import com.google.android.voicesearch.audio.TtsAudioPlayer.Callback;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class ResultsPresenter
  extends AbstractActionCardsPresenter
{
  @Nullable
  private View mLastVisibleCard;
  private final LocalTtsManager mLocalTtsManager;
  private final TtsAudioPlayer mTtsAudioPlayer;
  @Nullable
  private View mWebResultsText;
  private CoScrollContainer.LayoutParams mWebResultsTextLayoutParams;
  @Nullable
  private View mWebView;
  private CoScrollContainer.LayoutParams mWebViewLayoutParams;
  private boolean mWebViewShown;
  private boolean mWebViewVisible;
  
  public ResultsPresenter(MainContentView paramMainContentView, TtsAudioPlayer paramTtsAudioPlayer, LocalTtsManager paramLocalTtsManager, VelvetFactory paramVelvetFactory, CardFactory paramCardFactory)
  {
    super("results", paramMainContentView, paramVelvetFactory, paramCardFactory);
    this.mTtsAudioPlayer = paramTtsAudioPlayer;
    this.mLocalTtsManager = paramLocalTtsManager;
  }
  
  private void addWebResultsText()
  {
    post(new MainContentPresenter.Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.getScrollingContainer().addView(ResultsPresenter.this.mWebResultsText);
        ResultsPresenter.access$302(ResultsPresenter.this, (CoScrollContainer.LayoutParams)ResultsPresenter.this.mWebResultsText.getLayoutParams());
        ResultsPresenter.this.mWebResultsTextLayoutParams.setParams(5);
      }
    });
  }
  
  private boolean areWebResultsMajorityOfView()
  {
    if (this.mWebView == null) {}
    while ((this.mWebViewLayoutParams.isOffscreen()) || (getRelativeScrollDistanceFromTop(this.mWebView) > 0.5D)) {
      return false;
    }
    return true;
  }
  
  private int getConsumableCardMargin(View paramView)
  {
    if (getScrollViewControl().getScrollY() > 0) {
      return 0;
    }
    int i = getDimensionPixelSize(2131689582);
    int j = getScrollViewControl().getViewportHeight();
    int k = this.mWebResultsText.getMeasuredHeight();
    int m = getDimensionPixelSize(2131689600);
    return Math.max(0, j - paramView.getBottom() - k - m - i);
  }
  
  private void postClearAll()
  {
    post(new ClearUiTransaction(null));
    this.mWebViewShown = false;
    this.mLastVisibleCard = null;
    clearActionOnly();
  }
  
  private void postShowWebView()
  {
    post(new ShowWebViewTransaction());
    this.mWebViewShown = true;
  }
  
  private void removeWebResultsText()
  {
    post(new MainContentPresenter.Transaction()
    {
      public void commit(MainContentUi paramAnonymousMainContentUi)
      {
        paramAnonymousMainContentUi.getScrollingContainer().removeView(this.val$webResultsText);
      }
    });
  }
  
  private void setupWebView()
  {
    if (this.mWebView == null)
    {
      this.mWebView = getVelvetPresenter().getWebView();
      Preconditions.checkNotNull(this.mWebView);
      this.mWebViewLayoutParams = ((CoScrollContainer.LayoutParams)this.mWebView.getLayoutParams());
    }
  }
  
  private void updateWebViewLayout()
  {
    Preconditions.checkNotNull(this.mWebView);
    Preconditions.checkState(this.mWebViewVisible);
    int i = 4;
    if ((this.mLastVisibleCard != null) && (this.mLastVisibleCard != null))
    {
      int j = getDimensionPixelSize(2131689582);
      boolean bool = getEventBus().getUiState().getEffectOnWebResults().shouldSuppressWebResults();
      int k = 0;
      if (bool)
      {
        i = 0;
        k = getConsumableCardMargin(this.mLastVisibleCard);
        this.mWebResultsTextLayoutParams.setParams(this.mLastVisibleCard, j, k);
        j += this.mWebResultsText.getHeight();
      }
      this.mWebViewLayoutParams.setParams(this.mLastVisibleCard, j, k);
    }
    for (;;)
    {
      this.mWebResultsText.setVisibility(i);
      this.mWebView.setFocusable(true);
      this.mWebView.setFocusableInTouchMode(true);
      return;
      this.mWebViewLayoutParams.setParams(1);
    }
  }
  
  protected void onBeforeCardsShown(MainContentUi paramMainContentUi)
  {
    getEventBus().getQueryState().reportLatencyEvent(39);
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    postSetLayoutTransitionStartDelay(1, 0L);
    postSetMatchPortraitMode(getVelvetPresenter().getConfig().shouldMatchPortraitWidthInLandscape());
    this.mWebResultsText = getFactory().createWebResultsText(this, getCardContainer());
    this.mWebResultsText.setVisibility(4);
    addWebResultsText();
  }
  
  protected void onPreDetach()
  {
    super.onPreDetach();
    removeWebResultsText();
    postSetMatchPortraitMode(false);
    postClearAll();
  }
  
  public void onStart()
  {
    super.onStart();
    getEventBus().addObserver(this);
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    VelvetEventBus localVelvetEventBus = getEventBus();
    QueryState localQueryState = localVelvetEventBus.getQueryState();
    String str;
    if (paramEvent.hasTtsChanged())
    {
      TtsState localTtsState = localVelvetEventBus.getTtsState();
      int i = localTtsState.takePlay();
      if (i != 0)
      {
        if (i == 2) {
          this.mTtsAudioPlayer.setAudio(localTtsState.getNetworkTts());
        }
        if (i != 1) {
          break label184;
        }
        str = localTtsState.getLocalTts();
        post(new PlayTtsTransaction(localVelvetEventBus, localQueryState.getCommittedQuery(), this.mTtsAudioPlayer, this.mLocalTtsManager, str));
      }
    }
    UiState localUiState = getEventBus().getUiState();
    boolean bool1 = localUiState.shouldShowCards();
    boolean bool2 = localUiState.shouldShowWebView();
    boolean bool3 = localUiState.takeActionUiInvalidated();
    if ((paramEvent.hasUiChanged()) && (!bool2))
    {
      if ((!bool3) && ((bool1) || (!this.mWebViewShown))) {
        break label190;
      }
      postClearAll();
    }
    for (;;)
    {
      super.onStateChanged(paramEvent);
      if ((paramEvent.hasUiChanged()) && (bool2) && (!this.mWebViewShown)) {
        postShowWebView();
      }
      return;
      label184:
      str = null;
      break;
      label190:
      if (this.mWebViewShown)
      {
        post(new ClearUiTransaction(false, null));
        this.mWebViewShown = false;
      }
    }
  }
  
  public void onStop()
  {
    super.onStop();
    getEventBus().removeObserver(this);
  }
  
  public void onViewScrolled(boolean paramBoolean)
  {
    super.onViewScrolled(paramBoolean);
    if (areWebResultsMajorityOfView()) {
      getEventBus().getUiState().unsuppressCorpora();
    }
  }
  
  protected void setLastVisibleCard(@Nullable View paramView, MainContentUi paramMainContentUi)
  {
    if (this.mLastVisibleCard != paramView)
    {
      this.mLastVisibleCard = paramView;
      if (this.mWebViewVisible) {
        updateWebViewLayout();
      }
    }
  }
  
  private class ClearUiTransaction
    extends MainContentPresenter.Transaction
  {
    private final boolean mClearCards;
    
    private ClearUiTransaction()
    {
      this(true);
    }
    
    private ClearUiTransaction(boolean paramBoolean)
    {
      this.mClearCards = paramBoolean;
    }
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if (this.mClearCards) {
        paramMainContentUi.getCardsView().removeAllViews();
      }
      paramMainContentUi.getScrollViewControl().setScrollY(0);
      ResultsPresenter.this.mWebResultsTextLayoutParams.setParams(5);
      if (ResultsPresenter.this.mWebView != null)
      {
        ResultsPresenter.this.mWebViewLayoutParams.setParams(5);
        ResultsPresenter.this.mWebView.setFocusable(false);
        ResultsPresenter.this.mWebView.setFocusableInTouchMode(false);
        ResultsPresenter.access$602(ResultsPresenter.this, false);
      }
    }
  }
  
  static class PlayTtsTransaction
    extends MainContentPresenter.Transaction
    implements TtsAudioPlayer.Callback, Runnable
  {
    private final VelvetEventBus mEventBus;
    private final String mLocalTts;
    private final LocalTtsManager mLocalTtsManager;
    private final Query mQuery;
    private final TtsAudioPlayer mTtsAudioPlayer;
    
    PlayTtsTransaction(VelvetEventBus paramVelvetEventBus, Query paramQuery, TtsAudioPlayer paramTtsAudioPlayer, LocalTtsManager paramLocalTtsManager, String paramString)
    {
      this.mEventBus = paramVelvetEventBus;
      this.mQuery = paramQuery;
      this.mTtsAudioPlayer = paramTtsAudioPlayer;
      this.mLocalTtsManager = paramLocalTtsManager;
      this.mLocalTts = paramString;
    }
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if (this.mEventBus.getQueryState().isCurrentCommit(this.mQuery))
      {
        if (this.mLocalTts != null) {
          this.mLocalTtsManager.enqueue(this.mLocalTts, this, 0);
        }
      }
      else {
        return;
      }
      this.mTtsAudioPlayer.requestPlayback(this);
    }
    
    public void onComplete()
    {
      this.mEventBus.getTtsState().setDone(this.mQuery);
    }
    
    public void run()
    {
      onComplete();
    }
  }
  
  class ShowWebViewTransaction
    extends MainContentPresenter.Transaction
  {
    ShowWebViewTransaction() {}
    
    public void commit(MainContentUi paramMainContentUi)
    {
      if (ResultsPresenter.this.isAttached())
      {
        ResultsPresenter.this.setupWebView();
        ResultsPresenter.this.getEventBus().getQueryState().reportLatencyEvent(14);
        ResultsPresenter.access$602(ResultsPresenter.this, true);
        ResultsPresenter.this.updateWebViewLayout();
      }
    }
    
    public String toString()
    {
      return "ShowWebViewTransaction[]";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.ResultsPresenter
 * JD-Core Version:    0.7.0.1
 */