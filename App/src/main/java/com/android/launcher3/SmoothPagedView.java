package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public abstract class SmoothPagedView
  extends PagedView
{
  private static final float SMOOTHING_CONSTANT = (float)(0.016D / Math.log(0.75D));
  private float mBaseLineFlingVelocity;
  private float mFlingVelocityInfluence;
  private Interpolator mScrollInterpolator;
  int mScrollMode;
  
  public SmoothPagedView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SmoothPagedView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mUsePagingTouchSlop = false;
    if (this.mScrollMode != i) {}
    for (;;)
    {
      this.mDeferScrollUpdate = i;
      return;
      i = 0;
    }
  }
  
  private void snapToPageWithVelocity(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = Math.max(0, Math.min(paramInt1, -1 + getChildCount()));
    int j = Math.max(1, Math.abs(i - this.mCurrentPage));
    int k = getScrollForPage(i) - this.mUnboundedScrollX;
    int m = 100 * (j + 1);
    if (!this.mScroller.isFinished()) {
      this.mScroller.abortAnimation();
    }
    int n;
    if (paramBoolean)
    {
      ((OvershootInterpolator)this.mScrollInterpolator).setDistance(j);
      n = Math.abs(paramInt2);
      if (n <= 0) {
        break label146;
      }
    }
    label146:
    for (int i1 = (int)(m + m / (n / this.mBaseLineFlingVelocity) * this.mFlingVelocityInfluence);; i1 = m + 100)
    {
      snapToPage(i, k, i1);
      return;
      ((OvershootInterpolator)this.mScrollInterpolator).disableSettle();
      break;
    }
  }
  
  public void computeScroll()
  {
    if (this.mScrollMode == 1) {
      super.computeScroll();
    }
    float f3;
    do
    {
      do
      {
        return;
      } while ((computeScrollHelper()) || (this.mTouchState != 1));
      float f1 = (float)System.nanoTime() / 1.0E+009F;
      float f2 = (float)Math.exp((f1 - this.mSmoothingTime) / SMOOTHING_CONSTANT);
      f3 = this.mTouchX - this.mUnboundedScrollX;
      scrollTo(Math.round(this.mUnboundedScrollX + f3 * f2), getScrollY());
      this.mSmoothingTime = f1;
    } while ((f3 <= 1.0F) && (f3 >= -1.0F));
    invalidate();
  }
  
  protected int getScrollMode()
  {
    return 1;
  }
  
  protected void init()
  {
    super.init();
    this.mScrollMode = getScrollMode();
    if (this.mScrollMode == 0)
    {
      this.mBaseLineFlingVelocity = 2500.0F;
      this.mFlingVelocityInfluence = 0.4F;
      this.mScrollInterpolator = new OvershootInterpolator();
      this.mScroller = new Scroller(getContext(), this.mScrollInterpolator);
    }
  }
  
  protected void snapToDestination()
  {
    if (this.mScrollMode == 1)
    {
      super.snapToDestination();
      return;
    }
    snapToPageWithVelocity(getPageNearestToCenterOfScreen(), 0);
  }
  
  protected void snapToPage(int paramInt)
  {
    if (this.mScrollMode == 1)
    {
      super.snapToPage(paramInt);
      return;
    }
    snapToPageWithVelocity(paramInt, 0, false);
  }
  
  protected void snapToPageWithVelocity(int paramInt1, int paramInt2)
  {
    if (this.mScrollMode == 1)
    {
      super.snapToPageWithVelocity(paramInt1, paramInt2);
      return;
    }
    snapToPageWithVelocity(paramInt1, 0, true);
  }
  
  public static class OvershootInterpolator
    implements Interpolator
  {
    private float mTension = 1.3F;
    
    public void disableSettle()
    {
      this.mTension = 0.0F;
    }
    
    public float getInterpolation(float paramFloat)
    {
      float f = paramFloat - 1.0F;
      return 1.0F + f * f * (f * (1.0F + this.mTension) + this.mTension);
    }
    
    public void setDistance(int paramInt)
    {
      float f = 1.3F;
      if (paramInt > 0) {
        f /= paramInt;
      }
      this.mTension = f;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.SmoothPagedView
 * JD-Core Version:    0.7.0.1
 */