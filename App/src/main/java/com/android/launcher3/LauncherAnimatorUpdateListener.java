package com.android.launcher3;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

abstract class LauncherAnimatorUpdateListener
  implements ValueAnimator.AnimatorUpdateListener
{
  abstract void onAnimationUpdate(float paramFloat1, float paramFloat2);
  
  public void onAnimationUpdate(ValueAnimator paramValueAnimator)
  {
    float f = ((Float)paramValueAnimator.getAnimatedValue()).floatValue();
    onAnimationUpdate(1.0F - f, f);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAnimatorUpdateListener
 * JD-Core Version:    0.7.0.1
 */