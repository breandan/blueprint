package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import com.google.android.shared.util.LayoutUtils;

public class SearchPlateAnimator
  extends ValueAnimator
{
  private final boolean mIsAppear;
  private float mMaxTranslationX;
  private View mTargetView;
  
  public SearchPlateAnimator(boolean paramBoolean)
  {
    this.mIsAppear = paramBoolean;
    addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        ((SearchPlateAnimator)paramAnonymousAnimator).onEnd();
      }
    });
    addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        ((SearchPlateAnimator)paramAnonymousValueAnimator).doUpdate();
      }
    });
    if (this.mIsAppear)
    {
      setFloatValues(new float[] { 0.0F, 1.0F });
      return;
    }
    setFloatValues(new float[] { 1.0F, 0.0F });
  }
  
  private void doUpdate()
  {
    float f1 = ((Float)getAnimatedValue()).floatValue();
    this.mTargetView.setAlpha(f1);
    if (this.mMaxTranslationX != 0.0F)
    {
      float f2 = (float)((1.0D - f1) * this.mMaxTranslationX);
      this.mTargetView.setTranslationX(f2);
    }
  }
  
  private void onEnd()
  {
    if (this.mIsAppear) {
      this.mTargetView.setAlpha(1.0F);
    }
    if (this.mMaxTranslationX != 0.0F) {
      this.mTargetView.setTranslationX(0.0F);
    }
    this.mTargetView.setLayerType(0, null);
  }
  
  public void setTarget(Object paramObject)
  {
    this.mTargetView = ((View)paramObject);
    float f1;
    if (this.mTargetView.getId() == 2131296524)
    {
      this.mMaxTranslationX = (-this.mTargetView.getMeasuredWidth());
      f1 = this.mMaxTranslationX;
      if (!LayoutUtils.isLayoutRtl(this.mTargetView)) {
        break label76;
      }
    }
    label76:
    for (float f2 = -1.0F;; f2 = 1.0F)
    {
      this.mMaxTranslationX = (f2 * f1);
      this.mTargetView.setLayerType(2, null);
      return;
      this.mMaxTranslationX = 0.0F;
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SearchPlateAnimator
 * JD-Core Version:    0.7.0.1
 */