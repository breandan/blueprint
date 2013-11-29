package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

public class CardAnimator
  extends ValueAnimator
{
  private static final TimeInterpolator DEAL_INTERPOLATOR = BakedBezierInterpolator.INSTANCE;
  private int mAnimationIndex = 0;
  private SuggestionGridLayout.LayoutParams.AnimationType mAnimationType;
  private final int mDisplayHeight;
  private final boolean mIsAppear;
  private View mTargetView;
  
  public CardAnimator(boolean paramBoolean, int paramInt)
  {
    this.mIsAppear = paramBoolean;
    this.mDisplayHeight = paramInt;
    addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        ((CardAnimator)paramAnonymousAnimator).onEnd();
      }
    });
    addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        ((CardAnimator)paramAnonymousValueAnimator).doUpdate();
      }
    });
    if (this.mIsAppear)
    {
      setFloatValues(new float[] { 0.0F, 1.0F });
      return;
    }
    setFloatValues(new float[] { 1.0F, 0.0F });
  }
  
  private void configureTimings()
  {
    long l = 200L;
    if ((this.mIsAppear) && (this.mAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.DEAL))
    {
      setStartDelay(100 * this.mAnimationIndex);
      setInterpolator(DEAL_INTERPOLATOR);
    }
    while (isSlideAnimation(this.mAnimationType))
    {
      if (this.mIsAppear) {
        l = 400L;
      }
      setDuration(l);
      return;
      if ((this.mIsAppear) && (this.mAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.FADE_AFTER_DEAL)) {
        setStartDelay(300 + 100 * this.mAnimationIndex);
      } else {
        setStartDelay(0L);
      }
    }
    if ((this.mIsAppear) && (isFadeAnimation(this.mAnimationType)))
    {
      setDuration(l);
      return;
    }
    setDuration(500L);
  }
  
  private void doUpdate()
  {
    float f = ((Float)getAnimatedValue()).floatValue();
    switch (3.$SwitchMap$com$google$android$search$shared$ui$SuggestionGridLayout$LayoutParams$AnimationType[this.mAnimationType.ordinal()])
    {
    default: 
      cancel();
      return;
    case 1: 
      updateDealAnimation(this.mTargetView, f);
      return;
    case 2: 
    case 3: 
      updateFadeAnimation(this.mTargetView, f);
      return;
    case 4: 
      View localView = this.mTargetView;
      if (!this.mIsAppear) {}
      for (boolean bool = true;; bool = false)
      {
        updateSlideAnimation(localView, f, bool);
        return;
      }
    }
    updateSlideAnimation(this.mTargetView, f, this.mIsAppear);
  }
  
  private static boolean isFadeAnimation(SuggestionGridLayout.LayoutParams.AnimationType paramAnimationType)
  {
    return (paramAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.FADE) || (paramAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.FADE_AFTER_DEAL);
  }
  
  private static boolean isSlideAnimation(SuggestionGridLayout.LayoutParams.AnimationType paramAnimationType)
  {
    return (paramAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.SLIDE_DOWN) || (paramAnimationType == SuggestionGridLayout.LayoutParams.AnimationType.SLIDE_UP);
  }
  
  private void onEnd()
  {
    View localView;
    float f;
    if (this.mAnimationType != SuggestionGridLayout.LayoutParams.AnimationType.NONE)
    {
      this.mTargetView.setTranslationX(0.0F);
      this.mTargetView.setRotation(0.0F);
      this.mTargetView.setAlpha(1.0F);
      localView = this.mTargetView;
      boolean bool = this.mIsAppear;
      f = 0.0F;
      if (!bool) {
        break label75;
      }
    }
    for (;;)
    {
      localView.setTranslationY(f);
      if (usesAlpha(this.mAnimationType)) {
        this.mTargetView.setLayerType(0, null);
      }
      return;
      label75:
      f = this.mDisplayHeight;
    }
  }
  
  private void updateDealAnimation(View paramView, float paramFloat)
  {
    paramView.setTranslationY(this.mDisplayHeight * (1.0F - paramFloat));
  }
  
  private void updateFadeAnimation(View paramView, float paramFloat)
  {
    paramView.setAlpha(paramFloat);
  }
  
  private void updateSlideAnimation(View paramView, float paramFloat, boolean paramBoolean)
  {
    float f1 = 0.0F + 1.0F * paramFloat;
    if (paramBoolean) {}
    for (float f2 = -paramView.getBottom();; f2 = this.mDisplayHeight - paramView.getTop())
    {
      paramView.setTranslationY(f2 * (1.0F - paramFloat));
      paramView.setAlpha(f1);
      return;
    }
  }
  
  public static boolean usesAlpha(SuggestionGridLayout.LayoutParams.AnimationType paramAnimationType)
  {
    return (isSlideAnimation(paramAnimationType)) || (isFadeAnimation(paramAnimationType));
  }
  
  public void setTarget(Object paramObject)
  {
    this.mTargetView = ((View)paramObject);
    SuggestionGridLayout.LayoutParams localLayoutParams;
    SuggestionGridLayout.LayoutParams.AnimationType localAnimationType2;
    SuggestionGridLayout.LayoutParams.AnimationType localAnimationType3;
    label72:
    int i;
    if ((this.mTargetView.getLayoutParams() instanceof SuggestionGridLayout.LayoutParams))
    {
      localLayoutParams = (SuggestionGridLayout.LayoutParams)this.mTargetView.getLayoutParams();
      if (this.mIsAppear)
      {
        localAnimationType2 = localLayoutParams.appearAnimationType;
        this.mAnimationType = localAnimationType2;
        if (this.mAnimationType == null)
        {
          if (!this.mIsAppear) {
            break label187;
          }
          localAnimationType3 = SuggestionGridLayout.LayoutParams.AnimationType.DEAL;
          this.mAnimationType = localAnimationType3;
        }
        this.mAnimationIndex = localLayoutParams.animationIndex;
        if ((this.mIsAppear) && (this.mTargetView.getVisibility() != 0)) {
          this.mAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.NONE;
        }
        if (this.mAnimationType != SuggestionGridLayout.LayoutParams.AnimationType.DEAL) {
          break label221;
        }
        i = 1;
        label123:
        if (this.mAnimationType != SuggestionGridLayout.LayoutParams.AnimationType.NONE) {
          break label226;
        }
      }
    }
    label187:
    label221:
    label226:
    for (int j = 1;; j = 0)
    {
      if ((i == 0) && (j == 0) && (this.mIsAppear)) {
        this.mTargetView.setAlpha(0.0F);
      }
      if (i != 0) {
        this.mTargetView.setTranslationY(this.mDisplayHeight);
      }
      return;
      localAnimationType2 = localLayoutParams.disappearAnimationType;
      break;
      localAnimationType3 = SuggestionGridLayout.LayoutParams.AnimationType.FADE;
      break label72;
      if (this.mIsAppear) {}
      for (SuggestionGridLayout.LayoutParams.AnimationType localAnimationType1 = SuggestionGridLayout.LayoutParams.AnimationType.DEAL;; localAnimationType1 = SuggestionGridLayout.LayoutParams.AnimationType.FADE)
      {
        this.mAnimationType = localAnimationType1;
        break;
      }
      i = 0;
      break label123;
    }
  }
  
  public void start()
  {
    configureTimings();
    super.start();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.CardAnimator
 * JD-Core Version:    0.7.0.1
 */