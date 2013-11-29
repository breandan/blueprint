package com.google.android.shared.ui;

import android.graphics.drawable.Drawable;
import android.view.View;

public class OnScrollViewFader
  implements ScrollViewControl.ScrollListener
{
  private int mCurrentMaxScrollY;
  private int mCurrentScrollY;
  private boolean mFadeBackgroundOnly;
  private boolean mFixedFade;
  private float mOpaqueAlpha;
  private int mOpaqueAtScroll;
  private float mTransparentAlpha;
  private int mTransparentAtScroll;
  private final View mView;
  
  public OnScrollViewFader(View paramView, ScrollViewControl paramScrollViewControl)
  {
    this.mView = paramView;
    paramScrollViewControl.addScrollListener(this);
    this.mCurrentScrollY = paramScrollViewControl.getScrollY();
    this.mCurrentMaxScrollY = paramScrollViewControl.getMaxScrollY();
  }
  
  private void setAlpha(float paramFloat)
  {
    if (this.mFadeBackgroundOnly)
    {
      this.mView.getBackground().setAlpha(Math.min(255, (int)(256.0F * paramFloat)));
      return;
    }
    this.mView.setAlpha(paramFloat);
  }
  
  private void updateFade()
  {
    float f = 1.0F;
    if (this.mOpaqueAtScroll > this.mTransparentAtScroll) {
      if (this.mCurrentScrollY < this.mTransparentAtScroll) {
        f = 0.0F;
      }
    }
    for (;;)
    {
      setAlpha(f * (this.mOpaqueAlpha - this.mTransparentAlpha) + this.mTransparentAlpha);
      return;
      if (this.mCurrentScrollY < this.mOpaqueAtScroll)
      {
        f = (this.mCurrentScrollY - this.mTransparentAtScroll) / (this.mOpaqueAtScroll - this.mTransparentAtScroll);
        continue;
        if (this.mTransparentAtScroll > this.mOpaqueAtScroll) {
          if (this.mCurrentScrollY < this.mOpaqueAtScroll) {
            f = 1.0F;
          } else if (this.mCurrentScrollY < this.mTransparentAtScroll) {
            f = 1.0F - (this.mCurrentScrollY - this.mOpaqueAtScroll) / (this.mTransparentAtScroll - this.mOpaqueAtScroll);
          } else {
            f = 0.0F;
          }
        }
      }
    }
  }
  
  public void onOverscroll(int paramInt) {}
  
  public void onOverscrollFinished() {}
  
  public void onOverscrollStarted() {}
  
  public void onScrollAnimationFinished() {}
  
  public void onScrollChanged(int paramInt1, int paramInt2)
  {
    this.mCurrentScrollY = paramInt1;
    this.mCurrentMaxScrollY = paramInt2;
    if (!this.mFixedFade) {
      updateFade();
    }
  }
  
  public void onScrollFinished() {}
  
  public void onScrollMarginConsumed(View paramView, int paramInt1, int paramInt2) {}
  
  public void setFadeBackgroundOnly(boolean paramBoolean)
  {
    this.mFadeBackgroundOnly = paramBoolean;
  }
  
  public void setFadePoints(int paramInt1, int paramInt2)
  {
    setFadePoints(paramInt1, paramInt2, 0.0F, 1.0F);
  }
  
  public void setFadePoints(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    this.mTransparentAtScroll = paramInt1;
    this.mOpaqueAtScroll = paramInt2;
    this.mTransparentAlpha = paramFloat1;
    this.mOpaqueAlpha = paramFloat2;
    this.mFixedFade = false;
    updateFade();
  }
  
  public void setFixedAlpha(float paramFloat)
  {
    this.mFixedFade = true;
    setAlpha(paramFloat);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.OnScrollViewFader
 * JD-Core Version:    0.7.0.1
 */