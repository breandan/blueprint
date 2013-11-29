package com.google.android.search.shared.ui;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

public class RecognizerView
  extends ImageView
  implements PathClippingView
{
  private Callback mCallback;
  private final int mCircleAlpha = 77;
  private ValueAnimator mCircleAnimator;
  private Paint mCirclePaint;
  private float mHotwordCircleRadius;
  private float mHotwordCircleStrokeWidth;
  private ClipOrFadeLayerDrawable mHotwordDrawable;
  private boolean mHotwordSupported;
  private float mInnerMaxRadius;
  private boolean mIsTtsPlaying;
  private int mMediumGrayColor;
  private ClipOrFadeLayerDrawable mNoHotwordDrawable;
  private int mNotListeningResource;
  private float mOuterMaxRadius;
  private float mProximityToNow;
  private int mRecognizerState;
  private ClipOrFadeLayerDrawable mTextDrawable;
  private ValueAnimator mTtsAnimator;
  private ClipOrFadeLayerDrawable mVoiceDrawable;
  private int mWhiteColor;
  
  public RecognizerView(Context paramContext)
  {
    super(paramContext);
  }
  
  public RecognizerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public RecognizerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void animateCircle(float paramFloat)
  {
    this.mHotwordCircleRadius = (Math.min(600.0F, 800.0F * paramFloat) / 600.0F * this.mOuterMaxRadius);
    this.mHotwordCircleStrokeWidth = Math.max(0.0F, this.mHotwordCircleRadius - paramFloat * this.mInnerMaxRadius);
    this.mCirclePaint.setStrokeWidth(this.mHotwordCircleStrokeWidth);
    invalidate();
  }
  
  private boolean canShowHotwordAnimation()
  {
    return (!this.mIsTtsPlaying) && ((this.mRecognizerState == 5) || (this.mRecognizerState == 8));
  }
  
  private void enablePressedState(boolean paramBoolean)
  {
    this.mHotwordDrawable.setSupportPressedState(paramBoolean);
    this.mNoHotwordDrawable.setSupportPressedState(paramBoolean);
    refreshDrawableState();
  }
  
  private void updateHotwordSupported(boolean paramBoolean)
  {
    if (paramBoolean == this.mHotwordSupported) {
      return;
    }
    this.mHotwordSupported = paramBoolean;
    if (this.mHotwordSupported) {
      this.mTextDrawable = this.mHotwordDrawable;
    }
    for (this.mNotListeningResource = 2130838109;; this.mNotListeningResource = 2130838113)
    {
      refreshUi();
      return;
      this.mTextDrawable = this.mNoHotwordDrawable;
    }
  }
  
  private void updateVoiceDrawable(Drawable paramDrawable)
  {
    this.mVoiceDrawable.setDrawableByLayerId(0, paramDrawable);
  }
  
  float getDrawableAlphaFade()
  {
    return ((ClipOrFadeLayerDrawable)getDrawable()).getAlphaFade();
  }
  
  int getState()
  {
    return this.mRecognizerState;
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    refreshUi();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    int i = getHeight() - getMeasuredHeight();
    int j = getWidth() - getMeasuredWidth();
    if ((i | j) == 0) {
      super.onDraw(paramCanvas);
    }
    for (;;)
    {
      if (this.mHotwordCircleStrokeWidth > 0.0F) {
        paramCanvas.drawCircle(getWidth() / 2, getHeight() / 2, this.mHotwordCircleRadius, this.mCirclePaint);
      }
      return;
      paramCanvas.save(1);
      paramCanvas.translate(j / 2, i / 2);
      super.onDraw(paramCanvas);
      paramCanvas.restore();
    }
  }
  
  public void onFinishInflate()
  {
    setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        switch (RecognizerView.this.mRecognizerState)
        {
        case 1: 
        default: 
          return;
        case 2: 
          RecognizerView.this.mCallback.onCancelRecordingClicked();
          return;
        case 3: 
          RecognizerView.this.mCallback.onStopRecordingClicked();
          return;
        case 4: 
          RecognizerView.this.mCallback.onCancelRecordingClicked();
          return;
        case 0: 
        case 8: 
          RecognizerView.this.mCallback.onStartRecordingClicked();
          return;
        case 5: 
          RecognizerView.this.mCallback.onStartVoiceSearchClicked();
          return;
        case 6: 
          RecognizerView.this.mCallback.onStartVoiceSearchClicked();
          return;
        }
        RecognizerView.this.mCallback.onStopRecordingClicked();
      }
    });
    Resources localResources = getResources();
    this.mHotwordDrawable = new ClipOrFadeLayerDrawable(localResources.getDrawable(2130837807), localResources.getDrawable(2130837805));
    this.mNoHotwordDrawable = new ClipOrFadeLayerDrawable(localResources.getDrawable(2130837662), localResources.getDrawable(2130837660));
    this.mTextDrawable = this.mNoHotwordDrawable;
    this.mNotListeningResource = 2130838113;
    this.mVoiceDrawable = new ClipOrFadeLayerDrawable(localResources.getDrawable(2130838107), localResources.getDrawable(2130838107));
    this.mVoiceDrawable.setId(0, 0);
    this.mVoiceDrawable.setId(1, 1);
    this.mWhiteColor = getContext().getResources().getColor(17170443);
    this.mMediumGrayColor = getContext().getResources().getColor(2131230888);
    this.mOuterMaxRadius = getContext().getResources().getDimensionPixelSize(2131689624);
    this.mInnerMaxRadius = getContext().getResources().getDimensionPixelSize(2131689625);
    this.mCirclePaint = new Paint();
    this.mCirclePaint.setStyle(Paint.Style.STROKE);
    this.mCirclePaint.setColor(this.mWhiteColor);
    this.mCirclePaint.setAlpha(77);
    this.mCirclePaint.setFlags(1);
    this.mCircleAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    this.mCircleAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    this.mCircleAnimator.setDuration(800L);
    this.mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        RecognizerView.this.animateCircle(((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
      }
    });
    this.mTtsAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    this.mTtsAnimator.setDuration(250L);
    this.mTtsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        RecognizerView.this.mVoiceDrawable.setAlphaFade(((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
      }
    });
    setState(5);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(RecognizerView.class.getCanonicalName());
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    this.mRecognizerState = localSavedState.mState;
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.mState = this.mRecognizerState;
    return localSavedState;
  }
  
  public void pauseSpeech()
  {
    if ((this.mRecognizerState == 2) || (this.mRecognizerState == 3) || (this.mRecognizerState == 4)) {
      setState(0);
    }
  }
  
  protected void refreshUi()
  {
    if ((this.mCircleAnimator.isRunning()) && (!canShowHotwordAnimation())) {
      this.mCircleAnimator.end();
    }
    String str = getResources().getString(2131363290);
    Resources localResources = getResources();
    switch (this.mRecognizerState)
    {
    default: 
      if ((!this.mIsTtsPlaying) && ((this.mRecognizerState == 8) || (this.mRecognizerState == 5))) {
        setImageDrawable(this.mTextDrawable);
      }
      break;
    }
    for (;;)
    {
      setContentDescription(str);
      invalidate();
      return;
      updateVoiceDrawable(localResources.getDrawable(2130838111));
      break;
      updateVoiceDrawable(localResources.getDrawable(2130838111));
      str = localResources.getString(2131363563);
      break;
      updateVoiceDrawable(localResources.getDrawable(2130838117));
      str = localResources.getString(2131363562);
      break;
      updateVoiceDrawable(localResources.getDrawable(2130838109));
      str = localResources.getString(2131363563);
      break;
      enablePressedState(true);
      this.mTextDrawable.setAlphaFade(1.0F);
      str = localResources.getString(2131363561);
      break;
      updateVoiceDrawable(localResources.getDrawable(this.mNotListeningResource));
      str = localResources.getString(2131363561);
      break;
      updateVoiceDrawable(localResources.getDrawable(2130838111));
      str = localResources.getString(2131363290);
      break;
      enablePressedState(false);
      this.mTextDrawable.setAlphaFade(this.mProximityToNow);
      str = localResources.getString(2131361907);
      break;
      setImageDrawable(this.mVoiceDrawable);
    }
  }
  
  public void setCallback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }
  
  public void setClipPath(Path paramPath)
  {
    if (this.mProximityToNow > 0.5D) {
      paramPath = null;
    }
    if (paramPath != null) {
      paramPath.offset(-getPaddingLeft(), -getPaddingTop());
    }
    this.mHotwordDrawable.setClipPath(paramPath);
    this.mNoHotwordDrawable.setClipPath(paramPath);
  }
  
  public void setProximityToNow(float paramFloat)
  {
    this.mProximityToNow = paramFloat;
    if (this.mRecognizerState == 8)
    {
      this.mNoHotwordDrawable.setAlphaFade(paramFloat);
      this.mHotwordDrawable.setAlphaFade(paramFloat);
    }
  }
  
  public void setState(int paramInt)
  {
    this.mRecognizerState = paramInt;
    refreshUi();
  }
  
  public void setTtsState(boolean paramBoolean)
  {
    if (paramBoolean == this.mIsTtsPlaying) {
      return;
    }
    this.mIsTtsPlaying = paramBoolean;
    if (this.mIsTtsPlaying)
    {
      this.mTtsAnimator.start();
      return;
    }
    this.mTtsAnimator.reverse();
  }
  
  public void showHotwordIndicator(boolean paramBoolean1, boolean paramBoolean2)
  {
    updateHotwordSupported(paramBoolean1);
    Paint localPaint;
    if ((paramBoolean2) && (canShowHotwordAnimation()))
    {
      localPaint = this.mCirclePaint;
      if ((this.mRecognizerState != 8) || (this.mProximityToNow >= 0.5D)) {
        break label71;
      }
    }
    label71:
    for (int i = this.mWhiteColor;; i = this.mMediumGrayColor)
    {
      localPaint.setColor(i);
      this.mCirclePaint.setAlpha(77);
      this.mCircleAnimator.start();
      return;
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onCancelRecordingClicked();
    
    public abstract void onStartRecordingClicked();
    
    public abstract void onStartVoiceSearchClicked();
    
    public abstract void onStopRecordingClicked();
  }
  
  public static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public RecognizerView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RecognizerView.SavedState(paramAnonymousParcel, null);
      }
      
      public RecognizerView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new RecognizerView.SavedState[paramAnonymousInt];
      }
    };
    int mState;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      this.mState = paramParcel.readInt();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.mState);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.RecognizerView
 * JD-Core Version:    0.7.0.1
 */