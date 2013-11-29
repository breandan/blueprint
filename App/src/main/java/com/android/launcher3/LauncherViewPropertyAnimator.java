package com.android.launcher3;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import java.util.ArrayList;
import java.util.EnumSet;

public class LauncherViewPropertyAnimator
  extends Animator
  implements Animator.AnimatorListener
{
  float mAlpha;
  long mDuration;
  FirstFrameAnimatorHelper mFirstFrameHelper;
  TimeInterpolator mInterpolator;
  ArrayList<Animator.AnimatorListener> mListeners;
  EnumSet<Properties> mPropertiesToSet = EnumSet.noneOf(Properties.class);
  float mRotationY;
  boolean mRunning = false;
  float mScaleX;
  float mScaleY;
  long mStartDelay;
  View mTarget;
  float mTranslationX;
  float mTranslationY;
  ViewPropertyAnimator mViewPropertyAnimator;
  
  public LauncherViewPropertyAnimator(View paramView)
  {
    this.mTarget = paramView;
    this.mListeners = new ArrayList();
  }
  
  public void addListener(Animator.AnimatorListener paramAnimatorListener)
  {
    this.mListeners.add(paramAnimatorListener);
  }
  
  public LauncherViewPropertyAnimator alpha(float paramFloat)
  {
    this.mPropertiesToSet.add(Properties.ALPHA);
    this.mAlpha = paramFloat;
    return this;
  }
  
  public void cancel()
  {
    if (this.mViewPropertyAnimator != null) {
      this.mViewPropertyAnimator.cancel();
    }
  }
  
  public Animator clone()
  {
    throw new RuntimeException("Not implemented");
  }
  
  public void end()
  {
    throw new RuntimeException("Not implemented");
  }
  
  public long getDuration()
  {
    return this.mDuration;
  }
  
  public ArrayList<Animator.AnimatorListener> getListeners()
  {
    return this.mListeners;
  }
  
  public long getStartDelay()
  {
    return this.mStartDelay;
  }
  
  public boolean isRunning()
  {
    return this.mRunning;
  }
  
  public boolean isStarted()
  {
    return this.mViewPropertyAnimator != null;
  }
  
  public void onAnimationCancel(Animator paramAnimator)
  {
    for (int i = 0; i < this.mListeners.size(); i++) {
      ((Animator.AnimatorListener)this.mListeners.get(i)).onAnimationCancel(this);
    }
    this.mRunning = false;
  }
  
  public void onAnimationEnd(Animator paramAnimator)
  {
    for (int i = 0; i < this.mListeners.size(); i++) {
      ((Animator.AnimatorListener)this.mListeners.get(i)).onAnimationEnd(this);
    }
    this.mRunning = false;
  }
  
  public void onAnimationRepeat(Animator paramAnimator)
  {
    for (int i = 0; i < this.mListeners.size(); i++) {
      ((Animator.AnimatorListener)this.mListeners.get(i)).onAnimationRepeat(this);
    }
  }
  
  public void onAnimationStart(Animator paramAnimator)
  {
    this.mFirstFrameHelper.onAnimationStart(paramAnimator);
    for (int i = 0; i < this.mListeners.size(); i++) {
      ((Animator.AnimatorListener)this.mListeners.get(i)).onAnimationStart(this);
    }
    this.mRunning = true;
  }
  
  public void removeAllListeners()
  {
    this.mListeners.clear();
  }
  
  public void removeListener(Animator.AnimatorListener paramAnimatorListener)
  {
    this.mListeners.remove(paramAnimatorListener);
  }
  
  public LauncherViewPropertyAnimator scaleX(float paramFloat)
  {
    this.mPropertiesToSet.add(Properties.SCALE_X);
    this.mScaleX = paramFloat;
    return this;
  }
  
  public LauncherViewPropertyAnimator scaleY(float paramFloat)
  {
    this.mPropertiesToSet.add(Properties.SCALE_Y);
    this.mScaleY = paramFloat;
    return this;
  }
  
  public Animator setDuration(long paramLong)
  {
    this.mPropertiesToSet.add(Properties.DURATION);
    this.mDuration = paramLong;
    return this;
  }
  
  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    this.mPropertiesToSet.add(Properties.INTERPOLATOR);
    this.mInterpolator = paramTimeInterpolator;
  }
  
  public void setStartDelay(long paramLong)
  {
    this.mPropertiesToSet.add(Properties.START_DELAY);
    this.mStartDelay = paramLong;
  }
  
  public void setTarget(Object paramObject)
  {
    throw new RuntimeException("Not implemented");
  }
  
  public void setupEndValues() {}
  
  public void setupStartValues() {}
  
  public void start()
  {
    this.mViewPropertyAnimator = this.mTarget.animate();
    this.mFirstFrameHelper = new FirstFrameAnimatorHelper(this.mViewPropertyAnimator, this.mTarget);
    if (this.mPropertiesToSet.contains(Properties.TRANSLATION_X)) {
      this.mViewPropertyAnimator.translationX(this.mTranslationX);
    }
    if (this.mPropertiesToSet.contains(Properties.TRANSLATION_Y)) {
      this.mViewPropertyAnimator.translationY(this.mTranslationY);
    }
    if (this.mPropertiesToSet.contains(Properties.SCALE_X)) {
      this.mViewPropertyAnimator.scaleX(this.mScaleX);
    }
    if (this.mPropertiesToSet.contains(Properties.ROTATION_Y)) {
      this.mViewPropertyAnimator.rotationY(this.mRotationY);
    }
    if (this.mPropertiesToSet.contains(Properties.SCALE_Y)) {
      this.mViewPropertyAnimator.scaleY(this.mScaleY);
    }
    if (this.mPropertiesToSet.contains(Properties.ALPHA)) {
      this.mViewPropertyAnimator.alpha(this.mAlpha);
    }
    if (this.mPropertiesToSet.contains(Properties.START_DELAY)) {
      this.mViewPropertyAnimator.setStartDelay(this.mStartDelay);
    }
    if (this.mPropertiesToSet.contains(Properties.DURATION)) {
      this.mViewPropertyAnimator.setDuration(this.mDuration);
    }
    if (this.mPropertiesToSet.contains(Properties.INTERPOLATOR)) {
      this.mViewPropertyAnimator.setInterpolator(this.mInterpolator);
    }
    if (this.mPropertiesToSet.contains(Properties.WITH_LAYER)) {
      this.mViewPropertyAnimator.withLayer();
    }
    this.mViewPropertyAnimator.setListener(this);
    this.mViewPropertyAnimator.start();
    LauncherAnimUtils.cancelOnDestroyActivity(this);
  }
  
  public LauncherViewPropertyAnimator translationY(float paramFloat)
  {
    this.mPropertiesToSet.add(Properties.TRANSLATION_Y);
    this.mTranslationY = paramFloat;
    return this;
  }
  
  static enum Properties
  {
    static
    {
      SCALE_X = new Properties("SCALE_X", 2);
      SCALE_Y = new Properties("SCALE_Y", 3);
      ROTATION_Y = new Properties("ROTATION_Y", 4);
      ALPHA = new Properties("ALPHA", 5);
      START_DELAY = new Properties("START_DELAY", 6);
      DURATION = new Properties("DURATION", 7);
      INTERPOLATOR = new Properties("INTERPOLATOR", 8);
      WITH_LAYER = new Properties("WITH_LAYER", 9);
      Properties[] arrayOfProperties = new Properties[10];
      arrayOfProperties[0] = TRANSLATION_X;
      arrayOfProperties[1] = TRANSLATION_Y;
      arrayOfProperties[2] = SCALE_X;
      arrayOfProperties[3] = SCALE_Y;
      arrayOfProperties[4] = ROTATION_Y;
      arrayOfProperties[5] = ALPHA;
      arrayOfProperties[6] = START_DELAY;
      arrayOfProperties[7] = DURATION;
      arrayOfProperties[8] = INTERPOLATOR;
      arrayOfProperties[9] = WITH_LAYER;
      $VALUES = arrayOfProperties;
    }
    
    private Properties() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherViewPropertyAnimator
 * JD-Core Version:    0.7.0.1
 */