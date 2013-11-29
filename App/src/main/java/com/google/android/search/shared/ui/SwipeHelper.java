package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.google.android.shared.util.LayoutUtils;

public class SwipeHelper
{
  public static float ALPHA_FADE_START = 0.15F;
  private static LinearInterpolator sLinearInterpolator = new LinearInterpolator();
  private int DEFAULT_ESCAPE_ANIMATION_DURATION = 75;
  private int MAX_DISMISS_VELOCITY = 2000;
  private int MAX_ESCAPE_ANIMATION_DURATION = 150;
  private float SWIPE_ESCAPE_VELOCITY = 100.0F;
  public boolean mAllowSwipeTowardsEnd = true;
  public boolean mAllowSwipeTowardsStart = true;
  private Callback mCallback;
  private boolean mCanCurrViewBeDimissed;
  private View mCurrView;
  private float mDensityScale;
  private boolean mDragging;
  private float mInitialTouchPos;
  private float mMinAlpha = 0.0F;
  private float mPagingTouchSlop;
  private boolean mRtl;
  private int mSwipeDirection;
  private VelocityTracker mVelocityTracker;
  
  public SwipeHelper(int paramInt, Callback paramCallback, float paramFloat1, float paramFloat2)
  {
    this.mCallback = paramCallback;
    this.mSwipeDirection = paramInt;
    this.mVelocityTracker = VelocityTracker.obtain();
    this.mDensityScale = paramFloat1;
    this.mPagingTouchSlop = paramFloat2;
  }
  
  private ObjectAnimator createTranslationAnimation(View paramView, float paramFloat)
  {
    if (this.mSwipeDirection == 0) {}
    for (Property localProperty = View.TRANSLATION_X;; localProperty = View.TRANSLATION_Y) {
      return ObjectAnimator.ofFloat(paramView, localProperty, new float[] { paramFloat });
    }
  }
  
  private void dismissChild(final View paramView, float paramFloat)
  {
    final boolean bool = this.mCallback.canChildBeDismissed(paramView);
    float f;
    int i;
    if ((paramFloat < 0.0F) || ((paramFloat == 0.0F) && (getTranslation(paramView) < 0.0F)) || ((paramFloat == 0.0F) && (getTranslation(paramView) == 0.0F) && (this.mSwipeDirection == 1)))
    {
      f = -getSize(paramView);
      i = this.MAX_ESCAPE_ANIMATION_DURATION;
      if (paramFloat == 0.0F) {
        break label176;
      }
    }
    label176:
    for (int j = Math.min(i, (int)(1000.0F * Math.abs(f - getTranslation(paramView)) / Math.abs(paramFloat)));; j = this.DEFAULT_ESCAPE_ANIMATION_DURATION)
    {
      ObjectAnimator localObjectAnimator = createTranslationAnimation(paramView, f);
      localObjectAnimator.setInterpolator(sLinearInterpolator);
      localObjectAnimator.setDuration(j);
      localObjectAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          SwipeHelper.this.mCallback.onChildDismissed(paramView);
          if (bool) {
            paramView.setAlpha(1.0F);
          }
        }
      });
      localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          if (bool) {
            paramView.setAlpha(SwipeHelper.this.getAlphaForOffset(paramView));
          }
        }
      });
      localObjectAnimator.start();
      return;
      f = getSize(paramView);
      break;
    }
  }
  
  private void endSwipe(VelocityTracker paramVelocityTracker)
  {
    paramVelocityTracker.computeCurrentVelocity(1000, this.MAX_DISMISS_VELOCITY * this.mDensityScale);
    float f1 = getVelocity(paramVelocityTracker);
    float f2 = getPerpendicularVelocity(paramVelocityTracker);
    float f3 = this.SWIPE_ESCAPE_VELOCITY * this.mDensityScale;
    float f4 = getTranslation(this.mCurrView);
    int i;
    int m;
    label107:
    int n;
    label117:
    int j;
    label127:
    int k;
    label165:
    View localView;
    if (Math.abs(f4) > 0.6D * getSize(this.mCurrView))
    {
      i = 1;
      if ((Math.abs(f1) <= f3) || (Math.abs(f1) <= Math.abs(f2))) {
        break label207;
      }
      if (f1 <= 0.0F) {
        break label195;
      }
      m = 1;
      if (f4 <= 0.0F) {
        break label201;
      }
      n = 1;
      if (m != n) {
        break label207;
      }
      j = 1;
      if ((!this.mCallback.canChildBeDismissed(this.mCurrView)) || (!isValidSwipeDirection(f4)) || ((j == 0) && (i == 0))) {
        break label213;
      }
      k = 1;
      if (k == 0) {
        break label224;
      }
      localView = this.mCurrView;
      if (j == 0) {
        break label219;
      }
    }
    for (;;)
    {
      dismissChild(localView, f1);
      return;
      i = 0;
      break;
      label195:
      m = 0;
      break label107;
      label201:
      n = 0;
      break label117;
      label207:
      j = 0;
      break label127;
      label213:
      k = 0;
      break label165;
      label219:
      f1 = 0.0F;
    }
    label224:
    this.mCallback.onDragCancelled(this.mCurrView);
    snapChild(this.mCurrView, f1);
  }
  
  private float getAlphaForOffset(View paramView)
  {
    float f1 = getSize(paramView);
    float f2 = 0.65F * f1;
    float f3 = 1.0F;
    float f4 = getTranslation(paramView);
    if (f4 >= f1 * ALPHA_FADE_START) {
      f3 = 1.0F - (f4 - f1 * ALPHA_FADE_START) / f2;
    }
    for (;;)
    {
      float f5 = Math.max(Math.min(f3, 1.0F), 0.0F);
      return Math.max(this.mMinAlpha, f5);
      if (f4 < f1 * (1.0F - ALPHA_FADE_START)) {
        f3 = 1.0F + (f4 + f1 * ALPHA_FADE_START) / f2;
      }
    }
  }
  
  private float getPerpendicularVelocity(VelocityTracker paramVelocityTracker)
  {
    if (this.mSwipeDirection == 0) {
      return paramVelocityTracker.getYVelocity();
    }
    return paramVelocityTracker.getXVelocity();
  }
  
  private float getPos(MotionEvent paramMotionEvent)
  {
    if (this.mSwipeDirection == 0) {
      return paramMotionEvent.getX();
    }
    return paramMotionEvent.getY();
  }
  
  private float getSize(View paramView)
  {
    DisplayMetrics localDisplayMetrics = paramView.getContext().getResources().getDisplayMetrics();
    if (this.mSwipeDirection == 0) {
      return localDisplayMetrics.widthPixels;
    }
    return localDisplayMetrics.heightPixels;
  }
  
  private float getTranslation(View paramView)
  {
    if (this.mSwipeDirection == 0) {
      return paramView.getTranslationX();
    }
    return paramView.getTranslationY();
  }
  
  private float getVelocity(VelocityTracker paramVelocityTracker)
  {
    if (this.mSwipeDirection == 0) {
      return paramVelocityTracker.getXVelocity();
    }
    return paramVelocityTracker.getYVelocity();
  }
  
  private boolean isValidSwipeDirection(float paramFloat)
  {
    if (this.mSwipeDirection == 0)
    {
      if (this.mRtl)
      {
        if (paramFloat <= 0.0F) {
          return this.mAllowSwipeTowardsEnd;
        }
        return this.mAllowSwipeTowardsStart;
      }
      if (paramFloat <= 0.0F) {
        return this.mAllowSwipeTowardsStart;
      }
      return this.mAllowSwipeTowardsEnd;
    }
    return true;
  }
  
  private void setSwipeAmount(float paramFloat)
  {
    float f1;
    float f2;
    if ((!isValidSwipeDirection(paramFloat)) || (!this.mCallback.canChildBeDismissed(this.mCurrView)))
    {
      f1 = getSize(this.mCurrView);
      f2 = 0.15F * f1;
      if (Math.abs(paramFloat) < f1) {
        break label97;
      }
      if (paramFloat <= 0.0F) {
        break label91;
      }
      paramFloat = f2;
    }
    for (;;)
    {
      setTranslation(this.mCurrView, paramFloat);
      if (this.mCanCurrViewBeDimissed)
      {
        float f3 = getAlphaForOffset(this.mCurrView);
        this.mCurrView.setAlpha(f3);
      }
      return;
      label91:
      paramFloat = -f2;
      continue;
      label97:
      paramFloat = f2 * (float)Math.sin(1.570796326794897D * (paramFloat / f1));
    }
  }
  
  private void setTranslation(View paramView, float paramFloat)
  {
    if (this.mSwipeDirection == 0)
    {
      paramView.setTranslationX(paramFloat);
      return;
    }
    paramView.setTranslationY(paramFloat);
  }
  
  private void snapChild(final View paramView, float paramFloat)
  {
    final boolean bool = this.mCallback.canChildBeDismissed(paramView);
    ObjectAnimator localObjectAnimator = createTranslationAnimation(paramView, 0.0F);
    localObjectAnimator.setDuration('');
    localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        if (bool) {
          paramView.setAlpha(SwipeHelper.this.getAlphaForOffset(paramView));
        }
      }
    });
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (bool) {
          paramView.setAlpha(1.0F);
        }
        SwipeHelper.this.mCallback.onSnapBackCompleted(paramView);
      }
    });
    localObjectAnimator.start();
  }
  
  public void cancelOngoingDrag()
  {
    if (this.mDragging)
    {
      if (this.mCurrView != null)
      {
        this.mCallback.onDragCancelled(this.mCurrView);
        setTranslation(this.mCurrView, 0.0F);
        this.mCallback.onSnapBackCompleted(this.mCurrView);
        this.mCurrView = null;
      }
      this.mDragging = false;
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return this.mDragging;
      this.mDragging = false;
      this.mCurrView = this.mCallback.getChildAtPosition(paramMotionEvent);
      this.mVelocityTracker.clear();
      if (this.mCurrView != null)
      {
        this.mRtl = LayoutUtils.isLayoutRtl(this.mCurrView);
        this.mCanCurrViewBeDimissed = this.mCallback.canChildBeDismissed(this.mCurrView);
        this.mVelocityTracker.addMovement(paramMotionEvent);
        this.mInitialTouchPos = getPos(paramMotionEvent);
      }
      else
      {
        this.mCanCurrViewBeDimissed = false;
        continue;
        if (this.mCurrView != null)
        {
          this.mVelocityTracker.addMovement(paramMotionEvent);
          if (Math.abs(getPos(paramMotionEvent) - this.mInitialTouchPos) > this.mPagingTouchSlop)
          {
            this.mCallback.onBeginDrag(this.mCurrView);
            this.mDragging = true;
            this.mInitialTouchPos = (getPos(paramMotionEvent) - getTranslation(this.mCurrView));
            continue;
            this.mDragging = false;
            this.mCurrView = null;
          }
        }
      }
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((!this.mDragging) && (!onInterceptTouchEvent(paramMotionEvent))) {
      return this.mCanCurrViewBeDimissed;
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return true;
      if (this.mCurrView != null)
      {
        setSwipeAmount(getPos(paramMotionEvent) - this.mInitialTouchPos);
        continue;
        if (this.mCurrView != null) {
          endSwipe(this.mVelocityTracker);
        }
      }
    }
  }
  
  public void resetTranslation(View paramView)
  {
    setTranslation(paramView, 0.0F);
  }
  
  public static abstract interface Callback
  {
    public abstract boolean canChildBeDismissed(View paramView);
    
    public abstract View getChildAtPosition(MotionEvent paramMotionEvent);
    
    public abstract void onBeginDrag(View paramView);
    
    public abstract void onChildDismissed(View paramView);
    
    public abstract void onDragCancelled(View paramView);
    
    public abstract void onSnapBackCompleted(View paramView);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SwipeHelper
 * JD-Core Version:    0.7.0.1
 */