package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;

public class PullToRefreshHandler
  implements ScrollViewControl.ScrollListener
{
  private static final Interpolator PULL_INTERPOLATOR = new DecelerateInterpolator();
  private ViewPropertyAnimator mBounceAnimator = null;
  private final Runnable mBounceBack = new BounceBack();
  private int mCurrentOverscroll = 0;
  private int mLeftoverOversroll = 0;
  private final Listener mListener;
  private boolean mOverscrolling = false;
  private final NowProgressBar mProgressBar;
  private final float mRefreshThreshold;
  private final ScrollViewControl mScrollViewControl;
  private final SuggestionGridLayout mSuggestionGridLayout;
  
  public PullToRefreshHandler(Context paramContext, ScrollViewControl paramScrollViewControl, SuggestionGridLayout paramSuggestionGridLayout, NowProgressBar paramNowProgressBar, Listener paramListener)
  {
    this.mScrollViewControl = paramScrollViewControl;
    this.mSuggestionGridLayout = paramSuggestionGridLayout;
    this.mProgressBar = paramNowProgressBar;
    this.mListener = paramListener;
    this.mRefreshThreshold = (-paramContext.getResources().getDimension(2131689623));
  }
  
  void cancelPull()
  {
    if (this.mOverscrolling)
    {
      stopOverscroll();
      this.mProgressBar.releaseTrigger();
    }
  }
  
  public void onOverscroll(int paramInt)
  {
    if (!this.mOverscrolling) {}
    do
    {
      return;
      if (this.mLeftoverOversroll != 0) {
        paramInt += this.mLeftoverOversroll;
      }
      this.mCurrentOverscroll = paramInt;
    } while ((this.mProgressBar.isRunning()) || (paramInt >= 0));
    if (paramInt > this.mRefreshThreshold)
    {
      float f1 = paramInt / this.mRefreshThreshold;
      float f2 = PULL_INTERPOLATOR.getInterpolation(f1);
      if (this.mBounceAnimator != null)
      {
        this.mBounceAnimator.cancel();
        this.mBounceAnimator = null;
      }
      this.mSuggestionGridLayout.setTranslationY(f2 * -this.mRefreshThreshold / 4.0F);
      this.mProgressBar.setTriggerPercentage(f2);
      this.mSuggestionGridLayout.removeCallbacks(this.mBounceBack);
      this.mSuggestionGridLayout.postDelayed(this.mBounceBack, 350L);
      return;
    }
    this.mSuggestionGridLayout.removeCallbacks(this.mBounceBack);
    this.mProgressBar.start();
    this.mListener.onRefreshRequested();
    stopOverscroll();
  }
  
  public void onOverscrollFinished()
  {
    if (this.mOverscrolling) {
      this.mLeftoverOversroll = this.mCurrentOverscroll;
    }
  }
  
  public void onOverscrollStarted()
  {
    this.mOverscrolling = true;
    this.mProgressBar.cancelReleaseTrigger();
  }
  
  public void onScrollAnimationFinished() {}
  
  public void onScrollChanged(int paramInt1, int paramInt2) {}
  
  public void onScrollFinished() {}
  
  public void onScrollMarginConsumed(View paramView, int paramInt1, int paramInt2) {}
  
  public void register()
  {
    this.mScrollViewControl.addScrollListener(this);
  }
  
  void stopOverscroll()
  {
    if (this.mOverscrolling)
    {
      this.mBounceAnimator = this.mSuggestionGridLayout.animate().translationY(0.0F);
      this.mCurrentOverscroll = 0;
      this.mLeftoverOversroll = 0;
      this.mOverscrolling = false;
    }
  }
  
  public void unregister()
  {
    this.mScrollViewControl.removeScrollListener(this);
  }
  
  class BounceBack
    implements Runnable
  {
    BounceBack() {}
    
    public void run()
    {
      PullToRefreshHandler.this.cancelPull();
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onRefreshRequested();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.PullToRefreshHandler
 * JD-Core Version:    0.7.0.1
 */