package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

public class SlideAnimator
  extends ValueAnimator
{
  private final SlideAnimatorViewGroup mContainer;
  private int mDefaultMaxTranslationY;
  private int mMaxTranslationY;
  private View mTarget;
  
  public SlideAnimator(boolean paramBoolean, SlideAnimatorViewGroup paramSlideAnimatorViewGroup)
  {
    this.mContainer = paramSlideAnimatorViewGroup;
    setInterpolator(BakedBezierInterpolator.INSTANCE);
    addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        ((SlideAnimator)paramAnonymousValueAnimator).doUpdate(((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
      }
    });
    addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        ((SlideAnimator)paramAnonymousAnimator).onEnd();
      }
    });
    if (paramBoolean)
    {
      setFloatValues(new float[] { 0.0F, -1.0F });
      return;
    }
    setFloatValues(new float[] { -1.0F, 0.0F });
  }
  
  public void doUpdate(float paramFloat)
  {
    if (this.mMaxTranslationY != 0)
    {
      setTranslationY(paramFloat * this.mMaxTranslationY);
      return;
    }
    if (this.mTarget.getMeasuredHeight() > 0)
    {
      this.mMaxTranslationY = this.mTarget.getMeasuredHeight();
      setTranslationY(paramFloat * this.mMaxTranslationY);
      this.mTarget.setAlpha(1.0F);
      return;
    }
    this.mTarget.setAlpha(0.0F);
  }
  
  public void onEnd()
  {
    this.mTarget.setAlpha(1.0F);
    this.mTarget.setTranslationY(0.0F);
  }
  
  public void setMaxTranslation(int paramInt)
  {
    this.mDefaultMaxTranslationY = Math.abs(paramInt);
  }
  
  public void setTarget(Object paramObject)
  {
    this.mTarget = ((View)paramObject);
    this.mMaxTranslationY = this.mDefaultMaxTranslationY;
  }
  
  public void setTranslationY(float paramFloat)
  {
    View localView = this.mContainer.getNearestPreviousVisibleSibling(this.mTarget);
    if (localView != null) {
      paramFloat += localView.getTranslationY();
    }
    this.mTarget.setTranslationY(paramFloat);
  }
  
  public static abstract interface SlideAnimatorViewGroup
  {
    public abstract View getNearestPreviousVisibleSibling(View paramView);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SlideAnimator
 * JD-Core Version:    0.7.0.1
 */