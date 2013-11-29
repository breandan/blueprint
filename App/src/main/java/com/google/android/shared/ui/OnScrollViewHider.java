package com.google.android.shared.ui;

import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewPropertyAnimator;

public class OnScrollViewHider
  implements ScrollViewControl.ScrollListener
{
  private int mAnimationStartDelay;
  private final boolean mAtTop;
  private int mCurrentMaxScrollY;
  private int mCurrentOffset;
  private int mCurrentScrollY;
  private boolean mFadeWithTranslation;
  private boolean mFadeWithTranslationClearPending;
  private OnScrollViewHider mForceShowOrHideOnScrollFinishedDelegate;
  private boolean mLocked;
  private int mMaxHeightHidden;
  private int mOffsetFromEdge;
  private OnScrollViewHider mRevealAtScrollEndDelegate;
  private int mStickiness;
  private final View mView;
  private int mViewHeight;
  
  public OnScrollViewHider(View paramView, ScrollViewControl paramScrollViewControl, boolean paramBoolean)
  {
    this.mView = paramView;
    this.mAtTop = paramBoolean;
    this.mForceShowOrHideOnScrollFinishedDelegate = this;
    this.mRevealAtScrollEndDelegate = this;
    this.mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
    {
      public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
      {
        int i = paramAnonymousInt4 - paramAnonymousInt2;
        if (i != OnScrollViewHider.this.mViewHeight) {
          OnScrollViewHider.this.onViewHeightChanged(i);
        }
      }
    });
    paramScrollViewControl.addScrollListener(this);
  }
  
  private boolean allowOverscrollBounce()
  {
    if (this.mStickiness == 2) {}
    do
    {
      do
      {
        return true;
        if (this.mOffsetFromEdge == 0) {
          break label46;
        }
        if (!this.mAtTop) {
          break;
        }
      } while (this.mCurrentScrollY < 0);
      return false;
    } while (this.mCurrentScrollY > this.mCurrentMaxScrollY);
    return false;
    label46:
    return false;
  }
  
  private float getAlphaToFadeWithTranslation(int paramInt1, int paramInt2)
  {
    if (this.mFadeWithTranslationClearPending)
    {
      this.mFadeWithTranslationClearPending = false;
      this.mFadeWithTranslation = false;
    }
    do
    {
      return 1.0F;
      if (paramInt1 <= -paramInt2) {
        return 0.0F;
      }
    } while (paramInt1 >= 0);
    return 1.0F - paramInt1 / -paramInt2;
  }
  
  private int getDistanceFromScrollEnd()
  {
    return Math.max(this.mCurrentMaxScrollY - this.mCurrentScrollY, 0);
  }
  
  private int getScrollDistanceFromEdge()
  {
    if (this.mAtTop) {}
    for (int i = this.mCurrentScrollY;; i = this.mCurrentMaxScrollY - this.mCurrentScrollY) {
      return Math.max(0, i);
    }
  }
  
  private boolean isAtLeastHalfVisible()
  {
    return (this.mViewHeight == 0) || (-this.mCurrentOffset < this.mViewHeight / 2);
  }
  
  private boolean isScrollDistanceNearToEnd(int paramInt)
  {
    return paramInt <= this.mViewHeight + this.mOffsetFromEdge;
  }
  
  private boolean shouldHideOrShowOnScrollEnd()
  {
    int i = 1;
    if ((this.mStickiness == 2) || (this.mStickiness == i) || (this.mStickiness == 3)) {
      i = 0;
    }
    while (((!this.mAtTop) && (this.mOffsetFromEdge == 0)) || (this.mOffsetFromEdge + this.mViewHeight <= getScrollDistanceFromEdge())) {
      return i;
    }
    return false;
  }
  
  private void updateTranslation(int paramInt1, int paramInt2)
  {
    if (this.mViewHeight == 0) {}
    while (this.mLocked) {
      return;
    }
    int i = getScrollDistanceFromEdge();
    int m;
    label34:
    int n;
    int i1;
    label80:
    int i2;
    if (this.mCurrentScrollY < 0)
    {
      m = -this.mCurrentScrollY;
      n = this.mViewHeight;
      if (this.mMaxHeightHidden != 0) {
        n = Math.min(this.mMaxHeightHidden, n);
      }
      if ((this.mStickiness != 3) && ((paramInt2 & 0x4) == 0)) {
        break label231;
      }
      i1 = -(n + m);
      if (i1 == this.mCurrentOffset) {
        break label356;
      }
      i2 = i1;
      if (!allowOverscrollBounce()) {
        i2 = Math.min(i2, this.mOffsetFromEdge);
      }
      if (!this.mAtTop) {
        i2 = -i2;
      }
      if ((Math.abs(i1 - this.mCurrentOffset) > Math.abs(paramInt1)) && ((paramInt2 & 0x1) == 0)) {
        break label358;
      }
      this.mView.animate().cancel();
      this.mView.setTranslationY(i2);
      if (this.mFadeWithTranslation) {
        this.mView.setAlpha(getAlphaToFadeWithTranslation(i1, n));
      }
    }
    for (;;)
    {
      this.mCurrentOffset = i1;
      return;
      int j = this.mCurrentScrollY;
      int k = this.mCurrentMaxScrollY;
      m = 0;
      if (j <= k) {
        break label34;
      }
      m = this.mCurrentScrollY - this.mCurrentMaxScrollY;
      break label34;
      label231:
      if (this.mStickiness == 2)
      {
        int i4 = this.mOffsetFromEdge - i;
        i1 = Math.max(-n, i4 + m);
        break label80;
      }
      if (this.mOffsetFromEdge > i)
      {
        i1 = m + (this.mOffsetFromEdge - i);
        break label80;
      }
      if ((this.mStickiness == 1) || ((paramInt2 & 0x2) != 0))
      {
        i1 = m;
        break label80;
      }
      int i3 = paramInt1 + this.mCurrentOffset;
      i1 = Math.min(m, Math.max(-(n + m), i3));
      if (this.mStickiness != 4) {
        break label80;
      }
      i1 = Math.max(-(m + getDistanceFromScrollEnd()), i1);
      break label80;
      label356:
      break;
      label358:
      ViewPropertyAnimator localViewPropertyAnimator = this.mView.animate().translationY(i2);
      if (this.mFadeWithTranslation) {
        localViewPropertyAnimator.alpha(getAlphaToFadeWithTranslation(i1, n));
      }
      localViewPropertyAnimator.setStartDelay(this.mAnimationStartDelay);
    }
  }
  
  public void onOverscroll(int paramInt) {}
  
  public void onOverscrollFinished() {}
  
  public void onOverscrollStarted() {}
  
  public void onScrollAnimationFinished()
  {
    onScrollFinished();
  }
  
  public void onScrollChanged(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != this.mCurrentScrollY) || (paramInt2 != this.mCurrentMaxScrollY))
    {
      int i = this.mCurrentScrollY - paramInt1;
      this.mCurrentScrollY = paramInt1;
      this.mCurrentMaxScrollY = paramInt2;
      updateTranslation(i, 0);
    }
  }
  
  public void onScrollFinished()
  {
    int i = 4;
    boolean bool;
    if (shouldHideOrShowOnScrollEnd())
    {
      if (this.mCurrentScrollY > 0) {
        break label31;
      }
      bool = true;
    }
    for (;;)
    {
      if (bool) {
        i = 2;
      }
      updateTranslation(0, i);
      return;
      label31:
      if (this.mStickiness == i)
      {
        int j = getDistanceFromScrollEnd();
        if ((this.mForceShowOrHideOnScrollFinishedDelegate.isAtLeastHalfVisible()) || (this.mRevealAtScrollEndDelegate.isScrollDistanceNearToEnd(j))) {}
        for (bool = true;; bool = false) {
          break;
        }
      }
      if (this.mCurrentScrollY >= this.mCurrentMaxScrollY) {
        bool = false;
      } else {
        bool = this.mForceShowOrHideOnScrollFinishedDelegate.isAtLeastHalfVisible();
      }
    }
  }
  
  public void onScrollMarginConsumed(View paramView, int paramInt1, int paramInt2) {}
  
  protected void onViewHeightChanged(int paramInt)
  {
    int i = 1;
    int j;
    if (this.mViewHeight != paramInt)
    {
      if (this.mViewHeight != 0) {
        break label35;
      }
      j = i;
      this.mViewHeight = paramInt;
      if (j == 0) {
        break label40;
      }
    }
    for (;;)
    {
      updateTranslation(0, i);
      return;
      label35:
      j = 0;
      break;
      label40:
      i = 0;
    }
  }
  
  public void setAnimationStartDelay(int paramInt)
  {
    this.mAnimationStartDelay = paramInt;
  }
  
  public void setFadeWithTranslation(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mFadeWithTranslation = true;
      this.mFadeWithTranslationClearPending = false;
      return;
    }
    this.mFadeWithTranslationClearPending = true;
  }
  
  public void setForceShowOrHideOnScrollFinishedDelegate(OnScrollViewHider paramOnScrollViewHider)
  {
    this.mForceShowOrHideOnScrollFinishedDelegate = paramOnScrollViewHider;
  }
  
  public void setOffsetFromEdge(int paramInt, boolean paramBoolean)
  {
    if (paramInt != this.mOffsetFromEdge)
    {
      this.mOffsetFromEdge = paramInt;
      if (!paramBoolean) {
        break label26;
      }
    }
    label26:
    for (int i = 1;; i = 0)
    {
      updateTranslation(0, i);
      return;
    }
  }
  
  public void setPartialHide(int paramInt)
  {
    if (paramInt != this.mMaxHeightHidden)
    {
      this.mMaxHeightHidden = paramInt;
      if ((this.mMaxHeightHidden != 0) && (this.mCurrentOffset < -paramInt)) {
        updateTranslation(0, 1);
      }
    }
  }
  
  public void setRevealAtScrollEndDelegate(OnScrollViewHider paramOnScrollViewHider)
  {
    this.mRevealAtScrollEndDelegate = paramOnScrollViewHider;
  }
  
  public void setStickiness(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 2;
    int j = 1;
    int k;
    if (paramInt != this.mStickiness)
    {
      if (this.mStickiness != i) {
        break label56;
      }
      k = j;
      this.mStickiness = paramInt;
      if ((k == 0) || (!paramBoolean2)) {
        break label62;
      }
      label41:
      if (!paramBoolean1) {
        break label68;
      }
    }
    for (;;)
    {
      updateTranslation(0, j | i);
      return;
      label56:
      k = 0;
      break;
      label62:
      i = 0;
      break label41;
      label68:
      j = 0;
    }
  }
  
  public void show()
  {
    if ((this.mStickiness == 0) || (this.mStickiness == 4)) {
      updateTranslation(0, 2);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.OnScrollViewHider
 * JD-Core Version:    0.7.0.1
 */