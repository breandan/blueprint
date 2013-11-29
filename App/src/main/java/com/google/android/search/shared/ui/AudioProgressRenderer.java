package com.google.android.search.shared.ui;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.shared.util.SpeechLevelSource;
import java.util.ArrayList;

public class AudioProgressRenderer
  extends View
{
  private long mAnimationStartTimeInMs;
  private final TimeAnimator mAnimator = new TimeAnimator();
  private int mBarCount;
  private ArrayList<ClipDrawable> mBarDrawables = new ArrayList();
  private ArrayList<Drawable> mBarFullDrawables = new ArrayList();
  private final Drawable mBlueBar = getResources().getDrawable(2130838045);
  private int mCurrentMicReading;
  private final Drawable mEmptyBar = getResources().getDrawable(2130838046);
  private boolean mInitialized = false;
  private int[] mLevelArray;
  private int[] mMicReadings = new int[300];
  private int mScaledBarWidth;
  private SpeechLevelSource mSpeechLevelSource;
  
  public AudioProgressRenderer(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AudioProgressRenderer(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AudioProgressRenderer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mAnimator.setRepeatCount(-1);
    this.mAnimator.setDuration(1000L);
  }
  
  private int getBarHeightPixels()
  {
    return this.mEmptyBar.getIntrinsicHeight();
  }
  
  private int getBarLevelFromVolume(int paramInt)
  {
    return 1111 * (Math.min(Math.max(paramInt, 1111), 10000) / 1111);
  }
  
  private int getBarWidthPixels()
  {
    return this.mEmptyBar.getIntrinsicWidth();
  }
  
  private int getMeanVolumeFromReadings(int paramInt)
  {
    int i = this.mMicReadings.length / this.mBarCount;
    int j = 0;
    int k = Math.max(i * (paramInt - 1), 0);
    int m = Math.min(this.mMicReadings.length, paramInt * i);
    for (int n = k; n < m; n++) {
      if (n < this.mCurrentMicReading) {
        j += this.mMicReadings[n];
      }
    }
    if (m - k == 0) {
      return 0;
    }
    return j / (m - k);
  }
  
  private int getVolume()
  {
    if (this.mSpeechLevelSource == null) {
      return 0;
    }
    return 100 * this.mSpeechLevelSource.getSpeechLevel();
  }
  
  private void setSize(int paramInt1, int paramInt2)
  {
    (1.0D * paramInt2 / getBarHeightPixels());
    this.mScaledBarWidth = getBarWidthPixels();
    this.mBarCount = ((int)Math.round(1.0D * paramInt1 / this.mScaledBarWidth));
    this.mLevelArray = new int[this.mBarCount];
  }
  
  private void startAnimator()
  {
    if (!this.mAnimator.isStarted()) {
      this.mAnimator.start();
    }
  }
  
  private void stopAnimator()
  {
    this.mAnimator.cancel();
  }
  
  public void drawCurrentAnimation(Canvas paramCanvas)
  {
    long l = SystemClock.uptimeMillis();
    int i = 15000 / this.mBarCount;
    int j = (int)(l - this.mAnimationStartTimeInMs);
    int k = Math.min(-1 + this.mMicReadings.length, j / 50);
    int m = getVolume();
    for (int n = this.mCurrentMicReading; n <= k; n++) {
      this.mMicReadings[n] = m;
    }
    this.mCurrentMicReading = (k + 1);
    int i1 = j / i;
    if (i1 >= this.mBarCount) {
      i1 = -1 + this.mBarCount;
    }
    for (int i2 = this.mBarDrawables.size(); i2 <= i1; i2++)
    {
      int i8 = getMeanVolumeFromReadings(i2);
      Drawable localDrawable = this.mBlueBar;
      ClipDrawable localClipDrawable2 = new ClipDrawable(localDrawable, 80, 2);
      this.mLevelArray[i2] = getBarLevelFromVolume(i8);
      this.mBarDrawables.add(localClipDrawable2);
      this.mBarFullDrawables.add(localDrawable);
    }
    paramCanvas.drawColor(0);
    for (int i3 = 0; i3 < this.mBarCount; i3++)
    {
      this.mEmptyBar.setBounds(i3 * this.mScaledBarWidth, 0, i3 * this.mScaledBarWidth + this.mScaledBarWidth, getBarHeightPixels());
      this.mEmptyBar.draw(paramCanvas);
    }
    int i4 = 0;
    if (i4 < this.mBarDrawables.size())
    {
      ((Drawable)this.mBarFullDrawables.get(i4)).setBounds(i4 * this.mScaledBarWidth, 0, i4 * this.mScaledBarWidth + this.mScaledBarWidth, getBarHeightPixels());
      ClipDrawable localClipDrawable1 = (ClipDrawable)this.mBarDrawables.get(i4);
      int i5 = this.mLevelArray[i4];
      int i6 = (int)(l - (this.mAnimationStartTimeInMs + i4 * i));
      if (i6 < 300) {
        i5 = i5 * i6 / 300;
      }
      for (;;)
      {
        localClipDrawable1.setLevel(i5);
        localClipDrawable1.setBounds(i4 * this.mScaledBarWidth, 0, i4 * this.mScaledBarWidth + this.mScaledBarWidth, getBarHeightPixels());
        localClipDrawable1.draw(paramCanvas);
        i4++;
        break;
        if (i6 < 5300)
        {
          int i7 = i6 - 300;
          i5 += (int)Math.round(1000.0D * Math.cos(3.141592653589793D * (2.0D * (2.0D * i7)) / 1000.0D) / Math.exp(0.7D * i7 / 1000.0D));
        }
      }
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAnimator.setTimeListener(new TimeAnimator.TimeListener()
    {
      public void onTimeUpdate(TimeAnimator paramAnonymousTimeAnimator, long paramAnonymousLong1, long paramAnonymousLong2)
      {
        AudioProgressRenderer.this.invalidate();
      }
    });
  }
  
  protected void onDetachedFromWindow()
  {
    this.mAnimator.cancel();
    this.mAnimator.setTimeListener(null);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (!this.mInitialized) {
      return;
    }
    drawCurrentAnimation(paramCanvas);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof Bundle)) {
      super.onRestoreInstanceState(paramParcelable);
    }
    Bundle localBundle = (Bundle)paramParcelable;
    this.mAnimationStartTimeInMs = localBundle.getLong("AudioProgressRenderer.animationStartTimeMs");
    this.mMicReadings = localBundle.getIntArray("AudioProgressRenderer.micReadingsArray");
    this.mCurrentMicReading = localBundle.getInt("AudioProgressRenderer.currentMicReading");
    super.onRestoreInstanceState(localBundle.getParcelable("parentState"));
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("parentState", super.onSaveInstanceState());
    localBundle.putLong("AudioProgressRenderer.animationStartTimeMs", this.mAnimationStartTimeInMs);
    localBundle.putIntArray("AudioProgressRenderer.micReadingsArray", this.mMicReadings);
    localBundle.putInt("AudioProgressRenderer.currentMicReading", this.mCurrentMicReading);
    return localBundle;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    setSize(paramInt1, paramInt2);
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt != 0) {
      stopAnimation();
    }
  }
  
  public void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mSpeechLevelSource = paramSpeechLevelSource;
  }
  
  public void startAnimation()
  {
    if (this.mAnimationStartTimeInMs == 0L) {
      this.mAnimationStartTimeInMs = SystemClock.uptimeMillis();
    }
    this.mBarFullDrawables.clear();
    this.mBarDrawables.clear();
    startAnimator();
    this.mInitialized = true;
  }
  
  public void stopAnimation()
  {
    if (!this.mInitialized) {
      return;
    }
    stopAnimator();
    this.mInitialized = false;
    this.mAnimationStartTimeInMs = 0L;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.AudioProgressRenderer
 * JD-Core Version:    0.7.0.1
 */