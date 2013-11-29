package com.android.launcher3;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

public class LauncherAnimUtils
{
  static WeakHashMap<Animator, Object> sAnimators = new WeakHashMap();
  static Animator.AnimatorListener sEndAnimListener = new Animator.AnimatorListener()
  {
    public void onAnimationCancel(Animator paramAnonymousAnimator)
    {
      LauncherAnimUtils.sAnimators.remove(paramAnonymousAnimator);
    }
    
    public void onAnimationEnd(Animator paramAnonymousAnimator)
    {
      LauncherAnimUtils.sAnimators.remove(paramAnonymousAnimator);
    }
    
    public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
    
    public void onAnimationStart(Animator paramAnonymousAnimator)
    {
      LauncherAnimUtils.sAnimators.put(paramAnonymousAnimator, null);
    }
  };
  
  public static void cancelOnDestroyActivity(Animator paramAnimator)
  {
    paramAnimator.addListener(sEndAnimListener);
  }
  
  public static AnimatorSet createAnimatorSet()
  {
    AnimatorSet localAnimatorSet = new AnimatorSet();
    cancelOnDestroyActivity(localAnimatorSet);
    return localAnimatorSet;
  }
  
  public static ObjectAnimator ofFloat(View paramView, String paramString, float... paramVarArgs)
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    localObjectAnimator.setTarget(paramView);
    localObjectAnimator.setPropertyName(paramString);
    localObjectAnimator.setFloatValues(paramVarArgs);
    cancelOnDestroyActivity(localObjectAnimator);
    new FirstFrameAnimatorHelper(localObjectAnimator, paramView);
    return localObjectAnimator;
  }
  
  public static ValueAnimator ofFloat(View paramView, float... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setFloatValues(paramVarArgs);
    cancelOnDestroyActivity(localValueAnimator);
    return localValueAnimator;
  }
  
  public static ObjectAnimator ofPropertyValuesHolder(View paramView, PropertyValuesHolder... paramVarArgs)
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    localObjectAnimator.setTarget(paramView);
    localObjectAnimator.setValues(paramVarArgs);
    cancelOnDestroyActivity(localObjectAnimator);
    new FirstFrameAnimatorHelper(localObjectAnimator, paramView);
    return localObjectAnimator;
  }
  
  public static ObjectAnimator ofPropertyValuesHolder(Object paramObject, View paramView, PropertyValuesHolder... paramVarArgs)
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    localObjectAnimator.setTarget(paramObject);
    localObjectAnimator.setValues(paramVarArgs);
    cancelOnDestroyActivity(localObjectAnimator);
    new FirstFrameAnimatorHelper(localObjectAnimator, paramView);
    return localObjectAnimator;
  }
  
  public static void onDestroyActivity()
  {
    Iterator localIterator = new HashSet(sAnimators.keySet()).iterator();
    while (localIterator.hasNext())
    {
      Animator localAnimator = (Animator)localIterator.next();
      if (localAnimator.isRunning()) {
        localAnimator.cancel();
      }
      sAnimators.remove(localAnimator);
    }
  }
  
  public static void startAnimationAfterNextDraw(Animator paramAnimator, final View paramView)
  {
    paramView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener()
    {
      private boolean mStarted = false;
      
      public void onDraw()
      {
        if (this.mStarted) {}
        do
        {
          return;
          this.mStarted = true;
        } while (this.val$animator.getDuration() == 0L);
        this.val$animator.start();
        paramView.post(new Runnable()
        {
          public void run()
          {
            LauncherAnimUtils.2.this.val$view.getViewTreeObserver().removeOnDrawListener(jdField_this);
          }
        });
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAnimUtils
 * JD-Core Version:    0.7.0.1
 */