package com.google.android.shared.util;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.google.common.base.Preconditions;

public class Animations
{
  public static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();
  
  public static ViewPropertyAnimator fadeOutAndHide(View paramView)
  {
    return fadeOutAndHide(paramView, 4);
  }
  
  public static ViewPropertyAnimator fadeOutAndHide(View paramView, int paramInt)
  {
    if ((paramInt == 4) || (paramInt == 8)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return getAnimator(paramView).alpha(0.0F).setListener(hideOnAnimationEnd(paramView, paramInt));
    }
  }
  
  public static ViewPropertyAnimator fadeScaleUpdateText(TextView paramTextView, final CharSequence paramCharSequence, final float paramFloat)
  {
    getAnimator(paramTextView).alpha(0.0F).withEndAction(new Runnable()
    {
      public void run()
      {
        this.val$view.setText(paramCharSequence);
        this.val$view.setScaleX(paramFloat);
        this.val$view.setScaleY(paramFloat);
        Animations.getAnimator(this.val$view).alpha(1.0F).scaleX(1.0F).scaleY(1.0F);
      }
    });
  }
  
  public static ViewPropertyAnimator fadeUpdateText(TextView paramTextView, String paramString)
  {
    return fadeScaleUpdateText(paramTextView, paramString, 1.0F);
  }
  
  private static ViewPropertyAnimator getAnimator(View paramView)
  {
    ExtraPreconditions.checkMainThread();
    return paramView.animate().setInterpolator(INTERPOLATOR).setDuration(300L).setStartDelay(0L);
  }
  
  public static Animator.AnimatorListener hideOnAnimationEnd(View paramView, int paramInt)
  {
    return new SetVisibilityOnAnimationEnd(paramView, paramInt);
  }
  
  public static ViewPropertyAnimator showAndFadeIn(View paramView)
  {
    if (paramView.getVisibility() != 0)
    {
      paramView.setVisibility(0);
      paramView.setAlpha(0.0F);
    }
    return getAnimator(paramView).alpha(1.0F).setListener(null);
  }
  
  public static void stagger(long paramLong, float paramFloat, ViewPropertyAnimator... paramVarArgs)
  {
    int i = 0;
    int j = paramVarArgs.length;
    for (int k = 0; k < j; k++)
    {
      ViewPropertyAnimator localViewPropertyAnimator = paramVarArgs[k];
      localViewPropertyAnimator.setStartDelay(paramLong * i).setDuration((paramFloat * (float)localViewPropertyAnimator.getDuration()));
      i++;
    }
  }
  
  private static class SetVisibilityOnAnimationEnd
    extends AnimatorListenerAdapter
  {
    private boolean mCancelled;
    private final View mView;
    private final int mVisibility;
    
    SetVisibilityOnAnimationEnd(View paramView, int paramInt)
    {
      this.mView = paramView;
      this.mVisibility = paramInt;
    }
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      this.mCancelled = true;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (!this.mCancelled) {
        this.mView.setVisibility(this.mVisibility);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.Animations
 * JD-Core Version:    0.7.0.1
 */