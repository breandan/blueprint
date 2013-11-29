package com.google.android.search.shared.ui;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.SpeechLevelSource;

public class SoundLevels
  extends View
{
  private boolean mCenterDefined;
  private final int mCenterTranslationX;
  private final int mCenterTranslationY;
  private int mCenterX;
  private int mCenterY;
  private float mCurrentVolume;
  private boolean mIsEnabled;
  private SpeechLevelSource mLevelSource;
  private final float mMaximumLevelSize;
  private final float mMinimumLevel;
  private final Paint mPrimaryLevelPaint;
  private final TimeAnimator mSpeechLevelsAnimator;
  
  public SoundLevels(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SoundLevels(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SoundLevels(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SoundLevels, paramInt, 0);
    this.mLevelSource = new SpeechLevelSource();
    this.mLevelSource.setSpeechLevel(0);
    this.mCenterTranslationX = localTypedArray.getDimensionPixelOffset(3, 0);
    this.mCenterTranslationY = localTypedArray.getDimensionPixelOffset(4, 0);
    this.mMaximumLevelSize = localTypedArray.getDimensionPixelOffset(2, 0);
    this.mMinimumLevel = (localTypedArray.getDimensionPixelOffset(1, 0) / this.mMaximumLevelSize);
    this.mPrimaryLevelPaint = new Paint();
    this.mPrimaryLevelPaint.setColor(localTypedArray.getColor(0, -16777216));
    this.mPrimaryLevelPaint.setFlags(1);
    localTypedArray.recycle();
    this.mSpeechLevelsAnimator = new TimeAnimator();
    this.mSpeechLevelsAnimator.setRepeatCount(-1);
    this.mSpeechLevelsAnimator.setTimeListener(new TimeAnimator.TimeListener()
    {
      public void onTimeUpdate(TimeAnimator paramAnonymousTimeAnimator, long paramAnonymousLong1, long paramAnonymousLong2)
      {
        SoundLevels.this.invalidate();
      }
    });
  }
  
  private void startSpeechLevelsAnimator()
  {
    if (!this.mSpeechLevelsAnimator.isStarted()) {
      this.mSpeechLevelsAnimator.start();
    }
  }
  
  private void stopSpeechLevelsAnimator()
  {
    if (this.mSpeechLevelsAnimator.isStarted()) {
      this.mSpeechLevelsAnimator.end();
    }
  }
  
  private void updateSpeechLevelsAnimatorState()
  {
    if (this.mIsEnabled)
    {
      startSpeechLevelsAnimator();
      return;
    }
    stopSpeechLevelsAnimator();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    stopSpeechLevelsAnimator();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (!this.mIsEnabled) {
      return;
    }
    int k;
    int i;
    if (!this.mCenterDefined)
    {
      boolean bool = LayoutUtils.isLayoutRtl(this);
      int j = getWidth() / 2;
      if (bool)
      {
        k = -this.mCenterTranslationX;
        this.mCenterX = (k + j);
        this.mCenterY = (this.mCenterTranslationY + getWidth() / 2);
        this.mCenterDefined = true;
      }
    }
    else
    {
      i = this.mLevelSource.getSpeechLevel();
      if (i <= this.mCurrentVolume) {
        break label161;
      }
    }
    label161:
    for (this.mCurrentVolume += (i - this.mCurrentVolume) / 4.0F;; this.mCurrentVolume = (0.95F * this.mCurrentVolume))
    {
      float f = this.mMinimumLevel + (1.0F - this.mMinimumLevel) * this.mCurrentVolume / 100.0F;
      paramCanvas.drawCircle(this.mCenterX, this.mCenterY, f * this.mMaximumLevelSize, this.mPrimaryLevelPaint);
      return;
      k = this.mCenterTranslationX;
      break;
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(SoundLevels.class.getCanonicalName());
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (paramBoolean == this.mIsEnabled) {
      return;
    }
    super.setEnabled(paramBoolean);
    this.mIsEnabled = paramBoolean;
    setKeepScreenOn(paramBoolean);
    updateSpeechLevelsAnimatorState();
  }
  
  public void setLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mLevelSource = paramSpeechLevelSource;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SoundLevels
 * JD-Core Version:    0.7.0.1
 */