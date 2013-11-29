package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;

public class FirstFrameAnimatorHelper
  extends AnimatorListenerAdapter
  implements ValueAnimator.AnimatorUpdateListener
{
  private static ViewTreeObserver.OnDrawListener sGlobalDrawListener;
  private static long sGlobalFrameCounter;
  private static boolean sVisible;
  private boolean mAdjustedSecondFrameTime;
  private boolean mHandlingOnAnimationUpdate;
  private long mStartFrame;
  private long mStartTime = -1L;
  private View mTarget;
  
  public FirstFrameAnimatorHelper(ValueAnimator paramValueAnimator, View paramView)
  {
    this.mTarget = paramView;
    paramValueAnimator.addUpdateListener(this);
  }
  
  public FirstFrameAnimatorHelper(ViewPropertyAnimator paramViewPropertyAnimator, View paramView)
  {
    this.mTarget = paramView;
    paramViewPropertyAnimator.setListener(this);
  }
  
  public static void initializeDrawListener(View paramView)
  {
    if (sGlobalDrawListener != null) {
      paramView.getViewTreeObserver().removeOnDrawListener(sGlobalDrawListener);
    }
    sGlobalDrawListener = new ViewTreeObserver.OnDrawListener()
    {
      private long mTime = System.currentTimeMillis();
      
      public void onDraw()
      {
        FirstFrameAnimatorHelper.access$008();
      }
    };
    paramView.getViewTreeObserver().addOnDrawListener(sGlobalDrawListener);
    sVisible = true;
  }
  
  public static void setIsVisible(boolean paramBoolean)
  {
    sVisible = paramBoolean;
  }
  
  public void onAnimationStart(Animator paramAnimator)
  {
    ValueAnimator localValueAnimator = (ValueAnimator)paramAnimator;
    localValueAnimator.addUpdateListener(this);
    onAnimationUpdate(localValueAnimator);
  }
  
  public void onAnimationUpdate(final ValueAnimator paramValueAnimator)
  {
    long l1 = System.currentTimeMillis();
    if (this.mStartTime == -1L)
    {
      this.mStartFrame = sGlobalFrameCounter;
      this.mStartTime = l1;
    }
    long l2;
    if ((!this.mHandlingOnAnimationUpdate) && (sVisible) && (paramValueAnimator.getCurrentPlayTime() < paramValueAnimator.getDuration()))
    {
      this.mHandlingOnAnimationUpdate = true;
      l2 = sGlobalFrameCounter - this.mStartFrame;
      if ((l2 != 0L) || (l1 >= 1000L + this.mStartTime)) {
        break label108;
      }
      this.mTarget.getRootView().invalidate();
      paramValueAnimator.setCurrentPlayTime(0L);
    }
    for (;;)
    {
      this.mHandlingOnAnimationUpdate = false;
      return;
      label108:
      if ((l2 == 1L) && (l1 < 1000L + this.mStartTime) && (!this.mAdjustedSecondFrameTime) && (l1 > 16L + this.mStartTime))
      {
        paramValueAnimator.setCurrentPlayTime(16L);
        this.mAdjustedSecondFrameTime = true;
      }
      else if (l2 > 1L)
      {
        this.mTarget.post(new Runnable()
        {
          public void run()
          {
            paramValueAnimator.removeUpdateListener(FirstFrameAnimatorHelper.this);
          }
        });
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FirstFrameAnimatorHelper
 * JD-Core Version:    0.7.0.1
 */