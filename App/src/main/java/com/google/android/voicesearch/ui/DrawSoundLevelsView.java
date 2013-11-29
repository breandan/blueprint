package com.google.android.voicesearch.ui;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.android.shared.util.SpeechLevelSource;

public class DrawSoundLevelsView
  extends View
{
  private TimeAnimator mAnimator;
  private int mCurrentVolume;
  private int mDisableBackgroundColor;
  private int mEnableBackgroundColor;
  private final int mLevelSize;
  private SpeechLevelSource mLevelSource;
  private Paint mPaint;
  
  public DrawSoundLevelsView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DrawSoundLevelsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DrawSoundLevelsView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DrawSoundLevelView, paramInt, 0);
    this.mEnableBackgroundColor = localTypedArray.getColor(0, Color.parseColor("#66FFFFFF"));
    this.mDisableBackgroundColor = localTypedArray.getColor(1, -1);
    int i = localTypedArray.getColor(2, -1);
    this.mLevelSize = getBaseLevelSize(localTypedArray);
    localTypedArray.recycle();
    this.mLevelSource = new SpeechLevelSource();
    this.mLevelSource.setSpeechLevel(0);
    this.mAnimator = new TimeAnimator();
    this.mAnimator.setRepeatCount(-1);
    this.mAnimator.setDuration(1000L);
    this.mAnimator.setTimeListener(new TimeAnimator.TimeListener()
    {
      public void onTimeUpdate(TimeAnimator paramAnonymousTimeAnimator, long paramAnonymousLong1, long paramAnonymousLong2)
      {
        int i = DrawSoundLevelsView.this.mLevelSource.getSpeechLevel();
        if (i > DrawSoundLevelsView.this.mCurrentVolume) {
          DrawSoundLevelsView.access$102(DrawSoundLevelsView.this, Math.min(i, 10 + DrawSoundLevelsView.this.mCurrentVolume));
        }
        for (;;)
        {
          DrawSoundLevelsView.this.invalidate();
          return;
          DrawSoundLevelsView.access$102(DrawSoundLevelsView.this, Math.max(i, -10 + DrawSoundLevelsView.this.mCurrentVolume));
        }
      }
    });
    this.mPaint = new Paint(1);
    this.mPaint.setColor(i);
    startAnimator();
  }
  
  private void drawLevel(Canvas paramCanvas, int paramInt)
  {
    paramCanvas.drawCircle(getWidth() / 2, getWidth() / 2, paramInt, this.mPaint);
  }
  
  private int getBaseLevelSize(TypedArray paramTypedArray)
  {
    int i = paramTypedArray.getResourceId(3, 2130837988);
    return BitmapFactory.decodeResource(getResources(), i).getWidth();
  }
  
  private void startAnimator()
  {
    if (!this.mAnimator.isStarted()) {
      this.mAnimator.start();
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    startAnimator();
  }
  
  protected void onDetachedFromWindow()
  {
    this.mAnimator.cancel();
    super.onDetachedFromWindow();
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    if (isEnabled())
    {
      paramCanvas.drawColor(this.mEnableBackgroundColor);
      drawLevel(paramCanvas, ((getWidth() - this.mLevelSize) * this.mCurrentVolume / 100 + this.mLevelSize) / 2);
      return;
    }
    paramCanvas.drawColor(this.mDisableBackgroundColor);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
    }
    SavedState localSavedState;
    do
    {
      return;
      localSavedState = (SavedState)paramParcelable;
      super.onRestoreInstanceState(localSavedState.getSuperState());
    } while (!localSavedState.mAnimationStarted);
    startAnimator();
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.mAnimationStarted = this.mAnimator.isStarted();
    return localSavedState;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (paramBoolean)
    {
      startAnimator();
      return;
    }
    this.mAnimator.cancel();
  }
  
  public void setLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mLevelSource = paramSpeechLevelSource;
  }
  
  public static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public DrawSoundLevelsView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new DrawSoundLevelsView.SavedState(paramAnonymousParcel, null);
      }
      
      public DrawSoundLevelsView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new DrawSoundLevelsView.SavedState[paramAnonymousInt];
      }
    };
    boolean mAnimationStarted;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      if (paramParcel.readInt() == i) {}
      for (;;)
      {
        this.mAnimationStarted = i;
        return;
        i = 0;
      }
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      if (this.mAnimationStarted) {}
      for (byte b = 1;; b = 0)
      {
        paramParcel.writeByte(b);
        return;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ui.DrawSoundLevelsView
 * JD-Core Version:    0.7.0.1
 */