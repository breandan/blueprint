package com.google.android.shared.ui;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;

public class ScrollHelper
{
  private int mActivePointerId = -1;
  private final Context mContext;
  private int mCurrentScrollAnimationTarget = -1;
  private boolean mDragging;
  private EdgeEffect mEdgeGlow;
  private final int mEdgeGlowSize;
  private final SwitchableInterpolator mInterpolator;
  private float mLastPosition;
  private int mMovement;
  private int mOverscroll;
  private boolean mOverscrolling;
  private boolean mPositiveOverscroll;
  private final ScrollViewControl mScrollViewControl;
  private final OverScroller mScroller;
  private boolean mShowingOverscrollEffect;
  private int mTotalMovement;
  private boolean mTrackingVelocityForInterceptedFling;
  private VelocityTracker mVelocityTracker;
  private int mVerticalScrollFactor;
  private final View mView;
  private final ViewConfiguration mViewConfiguration;
  
  public ScrollHelper(Context paramContext, ScrollViewControl paramScrollViewControl, View paramView, int paramInt)
  {
    this.mContext = paramContext;
    this.mScrollViewControl = paramScrollViewControl;
    this.mView = paramView;
    this.mEdgeGlowSize = paramInt;
    this.mInterpolator = new SwitchableInterpolator();
    this.mScroller = createOverscroller();
    this.mViewConfiguration = ViewConfiguration.get(paramContext);
  }
  
  private boolean canScroll(boolean paramBoolean)
  {
    if (paramBoolean) {
      if (this.mScrollViewControl.getScrollY() >= this.mScrollViewControl.getMaxScrollY()) {}
    }
    while (this.mScrollViewControl.getScrollY() > 0)
    {
      return true;
      return false;
    }
    return false;
  }
  
  private int clampScrollDelta(int paramInt)
  {
    if (paramInt > 0) {
      paramInt = Math.min(paramInt, this.mScrollViewControl.getMaxScrollY() - this.mScrollViewControl.getScrollY());
    }
    while (paramInt >= 0) {
      return paramInt;
    }
    return Math.max(paramInt, -this.mScrollViewControl.getScrollY());
  }
  
  private void endDrag(boolean paramBoolean)
  {
    if ((this.mDragging) && (paramBoolean)) {
      startFlingIfFastEnough(this.mActivePointerId);
    }
    if (!isAnimatingScroll()) {
      this.mScrollViewControl.notifyScrollFinished();
    }
    this.mDragging = false;
    this.mActivePointerId = -1;
    if ((this.mVelocityTracker != null) && (!this.mTrackingVelocityForInterceptedFling))
    {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
    if (this.mShowingOverscrollEffect) {
      this.mEdgeGlow.onRelease();
    }
    this.mOverscrolling = false;
    this.mOverscroll = 0;
  }
  
  private int getVerticalScrollFactor()
  {
    if (this.mVerticalScrollFactor == 0)
    {
      TypedValue localTypedValue = new TypedValue();
      if (!this.mContext.getTheme().resolveAttribute(16842829, localTypedValue, true)) {
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
      }
      this.mVerticalScrollFactor = ((int)localTypedValue.getDimension(this.mContext.getResources().getDisplayMetrics()));
    }
    return this.mVerticalScrollFactor;
  }
  
  private boolean hasMovedEnough()
  {
    return Math.abs(this.mTotalMovement) >= this.mViewConfiguration.getScaledTouchSlop();
  }
  
  private void setDownPosition(MotionEvent paramMotionEvent)
  {
    if (isAnimatingScroll())
    {
      this.mDragging = true;
      this.mCurrentScrollAnimationTarget = -1;
      this.mScroller.abortAnimation();
    }
    this.mActivePointerId = paramMotionEvent.getPointerId(0);
    this.mLastPosition = paramMotionEvent.getY();
    this.mOverscrolling = false;
    this.mTotalMovement = 0;
  }
  
  private void startFlingIfFastEnough(int paramInt)
  {
    float f1 = this.mViewConfiguration.getScaledMaximumFlingVelocity();
    float f2 = this.mViewConfiguration.getScaledMinimumFlingVelocity();
    this.mVelocityTracker.computeCurrentVelocity(1000, f1);
    int i = (int)this.mVelocityTracker.getYVelocity(paramInt);
    if (Math.abs(i) > f2) {}
    for (;;)
    {
      if (i != 0)
      {
        this.mInterpolator.setInterpolatorOverride(null);
        this.mScroller.fling(this.mView.getScrollX(), this.mView.getScrollY(), 0, -i, 0, 0, 0, this.mScrollViewControl.getMaxScrollY());
        this.mCurrentScrollAnimationTarget = -2;
        this.mView.postInvalidateOnAnimation();
      }
      return;
      i = 0;
    }
  }
  
  private void trackVelocity(MotionEvent paramMotionEvent)
  {
    if (this.mVelocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
  }
  
  private void updateMovement(MotionEvent paramMotionEvent)
  {
    if (this.mActivePointerId == -1) {}
    do
    {
      int i;
      do
      {
        return;
        i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
      } while (i < 0);
      float f1 = paramMotionEvent.getY(i);
      if (f1 == this.mLastPosition) {
        break;
      }
      float f2 = this.mLastPosition - f1;
      this.mMovement = ((int)f2);
      this.mLastPosition = (f1 - (this.mMovement - f2));
      this.mTotalMovement += this.mMovement;
    } while (!this.mOverscrolling);
    this.mOverscroll += this.mMovement;
    return;
    this.mMovement = 0;
  }
  
  private void updateOverscrollEffect()
  {
    if ((this.mOverscrolling) && (this.mMovement != 0))
    {
      if (this.mEdgeGlow == null)
      {
        this.mEdgeGlow = new EdgeEffect(this.mContext);
        if (this.mView.willNotDraw()) {
          Log.w("Velvet.ScrollHelper", "Can't draw overscroll effects if the view doesn't draw");
        }
      }
      this.mShowingOverscrollEffect = true;
    }
    if (this.mShowingOverscrollEffect)
    {
      if (this.mOverscrolling)
      {
        float f1 = this.mMovement;
        if (this.mPositiveOverscroll) {
          f1 = -f1;
        }
        float f2 = f1 / this.mView.getHeight();
        this.mEdgeGlow.onPull(f2);
        this.mView.invalidate();
      }
    }
    else {
      return;
    }
    this.mEdgeGlow.onRelease();
    this.mEdgeGlow.finish();
    this.mShowingOverscrollEffect = false;
  }
  
  private int updateOverscrollStateAndGetScrollAmount()
  {
    boolean bool1 = true;
    int k;
    label40:
    int i;
    if (this.mOverscrolling)
    {
      if (this.mMovement == 0) {
        break label118;
      }
      if (this.mMovement > 0)
      {
        k = bool1;
        if (k != this.mPositiveOverscroll) {
          break label118;
        }
        this.mTotalMovement = 0;
      }
    }
    else
    {
      boolean bool2 = this.mOverscrolling;
      i = 0;
      if (!bool2)
      {
        i = clampScrollDelta(this.mMovement);
        this.mTotalMovement = (this.mMovement - i);
        if (hasMovedEnough())
        {
          this.mOverscrolling = bool1;
          this.mMovement = this.mTotalMovement;
          this.mOverscroll = this.mTotalMovement;
          if (this.mOverscroll <= 0) {
            break label220;
          }
        }
      }
    }
    for (;;)
    {
      this.mPositiveOverscroll = bool1;
      return i;
      k = 0;
      break;
      label118:
      if (hasMovedEnough())
      {
        if (this.mTotalMovement > 0) {}
        for (boolean bool3 = bool1;; bool3 = false)
        {
          if (!canScroll(bool3)) {
            break label171;
          }
          this.mOverscrolling = false;
          this.mOverscroll = 0;
          this.mMovement = this.mTotalMovement;
          break;
        }
      }
      label171:
      if (this.mOverscroll > 0) {}
      for (int j = bool1;; j = 0)
      {
        if (j == this.mPositiveOverscroll) {
          break label218;
        }
        this.mOverscrolling = false;
        this.mMovement = this.mOverscroll;
        this.mTotalMovement = this.mMovement;
        break;
      }
      label218:
      break label40;
      label220:
      bool1 = false;
    }
  }
  
  public void computeScroll()
  {
    if (isAnimatingScroll())
    {
      if (this.mScroller.computeScrollOffset())
      {
        this.mView.setScrollY(this.mScroller.getCurrY());
        this.mView.postInvalidateOnAnimation();
      }
    }
    else {
      return;
    }
    this.mCurrentScrollAnimationTarget = -1;
    this.mScrollViewControl.notifyScrollAnimationFinished();
  }
  
  OverScroller createOverscroller()
  {
    return new OverScroller(this.mContext, this.mInterpolator);
  }
  
  public void drawOverscrollEffect(Canvas paramCanvas)
  {
    int i;
    int j;
    int k;
    if ((this.mShowingOverscrollEffect) && (!this.mEdgeGlow.isFinished()))
    {
      i = paramCanvas.save();
      j = this.mView.getScrollY();
      k = this.mView.getWidth();
      int m = this.mView.getHeight();
      if (!this.mPositiveOverscroll) {
        break label113;
      }
      paramCanvas.rotate(180.0F);
      paramCanvas.translate(-k, -j - m);
      this.mEdgeGlow.setSize(k, this.mEdgeGlowSize);
      if (!this.mEdgeGlow.draw(paramCanvas)) {
        break label136;
      }
      this.mView.postInvalidateOnAnimation();
    }
    for (;;)
    {
      paramCanvas.restoreToCount(i);
      return;
      label113:
      paramCanvas.translate(0.0F, j);
      this.mEdgeGlow.setSize(k, this.mEdgeGlowSize);
      break;
      label136:
      this.mShowingOverscrollEffect = false;
    }
  }
  
  int getOverscrollAmount()
  {
    return this.mOverscroll;
  }
  
  public boolean isAnimatingScroll()
  {
    return this.mCurrentScrollAnimationTarget != -1;
  }
  
  public boolean isFlinging()
  {
    return this.mCurrentScrollAnimationTarget == -2;
  }
  
  boolean isOverscrolling()
  {
    return this.mOverscrolling;
  }
  
  boolean isShowingOverscrollEffect()
  {
    return this.mShowingOverscrollEffect;
  }
  
  public boolean maybeStartInterceptedFling(int paramInt)
  {
    boolean bool1 = this.mTrackingVelocityForInterceptedFling;
    boolean bool2 = false;
    if (bool1)
    {
      this.mTrackingVelocityForInterceptedFling = false;
      startFlingIfFastEnough(paramInt);
      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }
      bool2 = isAnimatingScroll();
    }
    return bool2;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (((0x2002 & paramMotionEvent.getSource()) != 0) && ((0x8 & paramMotionEvent.getActionMasked()) != 0))
    {
      int i = clampScrollDelta((int)(paramMotionEvent.getAxisValue(9) * -getVerticalScrollFactor()));
      if (i != 0) {
        this.mScrollViewControl.setScrollY(i + this.mScrollViewControl.getScrollY());
      }
      return true;
    }
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if ((i == 3) || (i == 1))
    {
      endDrag(false);
      return false;
    }
    if ((i != 0) && (this.mDragging)) {
      return this.mDragging;
    }
    switch (i)
    {
    }
    for (;;)
    {
      return this.mDragging;
      setDownPosition(paramMotionEvent);
      trackVelocity(paramMotionEvent);
      continue;
      updateMovement(paramMotionEvent);
      if (!this.mDragging)
      {
        this.mDragging = hasMovedEnough();
        if (this.mDragging)
        {
          requestDisallowInterceptTouchEvent();
          trackVelocity(paramMotionEvent);
        }
      }
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 20)
    {
      if (this.mScrollViewControl.getScrollY() < this.mScrollViewControl.getMaxScrollY())
      {
        int j = Math.min(this.mScrollViewControl.getScrollY() + Math.round(0.2F * this.mScrollViewControl.getMaxScrollY()), this.mScrollViewControl.getMaxScrollY());
        this.mScrollViewControl.setScrollY(j);
        return true;
      }
    }
    else if ((paramInt == 19) && (this.mScrollViewControl.getScrollY() > 0))
    {
      int i = Math.max(0, this.mScrollViewControl.getScrollY() - Math.round(0.2F * this.mScrollViewControl.getMaxScrollY()));
      this.mScrollViewControl.setScrollY(i);
      return true;
    }
    return false;
  }
  
  public void onMaxScrollChanged()
  {
    int i = this.mScrollViewControl.getScrollY();
    int j = this.mScrollViewControl.getMaxScrollY();
    int k;
    switch (this.mCurrentScrollAnimationTarget)
    {
    default: 
      if (this.mCurrentScrollAnimationTarget > j) {
        k = 1;
      }
      break;
    }
    for (;;)
    {
      if (k != 0)
      {
        if (isAnimatingScroll())
        {
          this.mScroller.abortAnimation();
          this.mCurrentScrollAnimationTarget = -1;
        }
        smoothScrollTo(j);
      }
      return;
      if (this.mScroller.getFinalY() > j) {}
      for (k = 1;; k = 0) {
        break;
      }
      if (i > j) {}
      for (k = 1;; k = 0) {
        break;
      }
      k = 0;
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = this.mOverscrolling;
    int i = paramMotionEvent.getActionMasked();
    trackVelocity(paramMotionEvent);
    switch (i)
    {
    default: 
      if ((bool) && (!this.mOverscrolling)) {
        this.mScrollViewControl.notifyOverscrollFinish();
      }
      break;
    }
    do
    {
      return true;
      setDownPosition(paramMotionEvent);
      break;
      updateMovement(paramMotionEvent);
      if (!this.mDragging)
      {
        this.mDragging = hasMovedEnough();
        if (this.mDragging) {
          requestDisallowInterceptTouchEvent();
        }
      }
      if (!this.mDragging) {
        break;
      }
      int j = updateOverscrollStateAndGetScrollAmount();
      if (j != 0) {
        this.mScrollViewControl.setScrollY(j + this.mScrollViewControl.getScrollY());
      }
      updateOverscrollEffect();
      break;
      endDrag(true);
      break;
      endDrag(false);
      break;
      if ((!bool) && (this.mOverscrolling))
      {
        this.mScrollViewControl.notifyOverscrollStart();
        return true;
      }
    } while (!this.mOverscrolling);
    this.mScrollViewControl.notifyOverscroll(this.mOverscroll);
    return true;
  }
  
  void requestDisallowInterceptTouchEvent()
  {
    ViewParent localViewParent = this.mView.getParent();
    if (localViewParent != null) {
      localViewParent.requestDisallowInterceptTouchEvent(true);
    }
  }
  
  public boolean smoothScrollTo(int paramInt)
  {
    return smoothScrollTo(paramInt, null, -1);
  }
  
  public boolean smoothScrollTo(int paramInt1, TimeInterpolator paramTimeInterpolator, int paramInt2)
  {
    if (isAnimatingScroll()) {
      this.mScroller.forceFinished(true);
    }
    int i = this.mScrollViewControl.getScrollY();
    int j = Math.min(this.mScrollViewControl.getMaxScrollY(), Math.max(paramInt1, 0));
    boolean bool = false;
    if (j != i)
    {
      this.mCurrentScrollAnimationTarget = j;
      this.mInterpolator.setInterpolatorOverride(paramTimeInterpolator);
      if (paramInt2 != -1) {
        break label103;
      }
      this.mScroller.startScroll(0, i, 0, j - i);
    }
    for (;;)
    {
      this.mView.postInvalidateOnAnimation();
      bool = true;
      return bool;
      label103:
      this.mScroller.startScroll(0, i, 0, j - i, paramInt2);
    }
  }
  
  public void trackVelocityForFlingIntercept(MotionEvent paramMotionEvent)
  {
    this.mTrackingVelocityForInterceptedFling = true;
    trackVelocity(paramMotionEvent);
  }
  
  private static class SwitchableInterpolator
    implements Interpolator
  {
    private TimeInterpolator mUseInterpolator;
    private final float mViscousFluidNormalize = 1.0F / viscousFluid(1.0F, 1.0F, 8.0F);
    
    private static float viscousFluid(float paramFloat1, float paramFloat2, float paramFloat3)
    {
      float f1 = paramFloat1 * paramFloat3;
      if (f1 < 1.0F) {}
      for (float f2 = f1 - (1.0F - (float)Math.exp(-f1));; f2 = 0.3678795F + (1.0F - (float)Math.exp(1.0F - f1)) * (1.0F - 0.3678795F)) {
        return f2 * paramFloat2;
      }
    }
    
    public float getInterpolation(float paramFloat)
    {
      if (this.mUseInterpolator != null) {
        return this.mUseInterpolator.getInterpolation(paramFloat);
      }
      return viscousFluid(paramFloat, this.mViscousFluidNormalize, 8.0F);
    }
    
    public void setInterpolatorOverride(TimeInterpolator paramTimeInterpolator)
    {
      this.mUseInterpolator = paramTimeInterpolator;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.ScrollHelper
 * JD-Core Version:    0.7.0.1
 */