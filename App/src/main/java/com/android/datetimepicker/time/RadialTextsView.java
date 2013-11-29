package com.android.datetimepicker.time;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.string;

public class RadialTextsView
  extends View
{
  private float mAmPmCircleRadiusMultiplier;
  private float mAnimationRadiusMultiplier;
  private float mCircleRadius;
  private float mCircleRadiusMultiplier;
  ObjectAnimator mDisappearAnimator;
  private boolean mDrawValuesReady;
  private boolean mHasInnerCircle;
  private float mInnerNumbersRadiusMultiplier;
  private float[] mInnerTextGridHeights;
  private float[] mInnerTextGridWidths;
  private float mInnerTextSize;
  private float mInnerTextSizeMultiplier;
  private String[] mInnerTexts;
  private InvalidateUpdateListener mInvalidateUpdateListener;
  private boolean mIs24HourMode;
  private boolean mIsInitialized = false;
  private float mNumbersRadiusMultiplier;
  private final Paint mPaint = new Paint();
  ObjectAnimator mReappearAnimator;
  private float[] mTextGridHeights;
  private boolean mTextGridValuesDirty;
  private float[] mTextGridWidths;
  private float mTextSize;
  private float mTextSizeMultiplier;
  private String[] mTexts;
  private float mTransitionEndRadiusMultiplier;
  private float mTransitionMidRadiusMultiplier;
  private Typeface mTypefaceLight;
  private Typeface mTypefaceRegular;
  private int mXCenter;
  private int mYCenter;
  
  public RadialTextsView(Context paramContext)
  {
    super(paramContext);
  }
  
  private void calculateGridSizes(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f1 = paramFloat1 * (float)Math.sqrt(3.0D) / 2.0F;
    float f2 = paramFloat1 / 2.0F;
    this.mPaint.setTextSize(paramFloat4);
    float f3 = paramFloat3 - (this.mPaint.descent() + this.mPaint.ascent()) / 2.0F;
    paramArrayOfFloat1[0] = (f3 - paramFloat1);
    paramArrayOfFloat2[0] = (paramFloat2 - paramFloat1);
    paramArrayOfFloat1[1] = (f3 - f1);
    paramArrayOfFloat2[1] = (paramFloat2 - f1);
    paramArrayOfFloat1[2] = (f3 - f2);
    paramArrayOfFloat2[2] = (paramFloat2 - f2);
    paramArrayOfFloat1[3] = f3;
    paramArrayOfFloat2[3] = paramFloat2;
    paramArrayOfFloat1[4] = (f3 + f2);
    paramArrayOfFloat2[4] = (paramFloat2 + f2);
    paramArrayOfFloat1[5] = (f3 + f1);
    paramArrayOfFloat2[5] = (paramFloat2 + f1);
    paramArrayOfFloat1[6] = (f3 + paramFloat1);
    paramArrayOfFloat2[6] = (paramFloat2 + paramFloat1);
  }
  
  private void drawTexts(Canvas paramCanvas, float paramFloat, Typeface paramTypeface, String[] paramArrayOfString, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.mPaint.setTextSize(paramFloat);
    this.mPaint.setTypeface(paramTypeface);
    paramCanvas.drawText(paramArrayOfString[0], paramArrayOfFloat1[3], paramArrayOfFloat2[0], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[1], paramArrayOfFloat1[4], paramArrayOfFloat2[1], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[2], paramArrayOfFloat1[5], paramArrayOfFloat2[2], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[3], paramArrayOfFloat1[6], paramArrayOfFloat2[3], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[4], paramArrayOfFloat1[5], paramArrayOfFloat2[4], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[5], paramArrayOfFloat1[4], paramArrayOfFloat2[5], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[6], paramArrayOfFloat1[3], paramArrayOfFloat2[6], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[7], paramArrayOfFloat1[2], paramArrayOfFloat2[5], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[8], paramArrayOfFloat1[1], paramArrayOfFloat2[4], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[9], paramArrayOfFloat1[0], paramArrayOfFloat2[3], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[10], paramArrayOfFloat1[1], paramArrayOfFloat2[2], this.mPaint);
    paramCanvas.drawText(paramArrayOfString[11], paramArrayOfFloat1[2], paramArrayOfFloat2[1], this.mPaint);
  }
  
  private void renderAnimations()
  {
    this.mDisappearAnimator = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[] { PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", new Keyframe[] { Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(0.2F, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, this.mTransitionEndRadiusMultiplier) }), PropertyValuesHolder.ofKeyframe("alpha", new Keyframe[] { Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(1.0F, 0.0F) }) }).setDuration(500);
    this.mDisappearAnimator.addUpdateListener(this.mInvalidateUpdateListener);
    int i = (int)((1.0F + 0.25F) * 500);
    float f1 = 0.25F * 500 / i;
    float f2 = 1.0F - 0.2F * (1.0F - f1);
    this.mReappearAnimator = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[] { PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", new Keyframe[] { Keyframe.ofFloat(0.0F, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(f1, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(f2, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, 1.0F) }), PropertyValuesHolder.ofKeyframe("alpha", new Keyframe[] { Keyframe.ofFloat(0.0F, 0.0F), Keyframe.ofFloat(f1, 0.0F), Keyframe.ofFloat(1.0F, 1.0F) }) }).setDuration(i);
    this.mReappearAnimator.addUpdateListener(this.mInvalidateUpdateListener);
  }
  
  public ObjectAnimator getDisappearAnimator()
  {
    if ((!this.mIsInitialized) || (!this.mDrawValuesReady) || (this.mDisappearAnimator == null))
    {
      Log.e("RadialTextsView", "RadialTextView was not ready for animation.");
      return null;
    }
    return this.mDisappearAnimator;
  }
  
  public ObjectAnimator getReappearAnimator()
  {
    if ((!this.mIsInitialized) || (!this.mDrawValuesReady) || (this.mReappearAnimator == null))
    {
      Log.e("RadialTextsView", "RadialTextView was not ready for animation.");
      return null;
    }
    return this.mReappearAnimator;
  }
  
  public boolean hasOverlappingRendering()
  {
    return false;
  }
  
  public void initialize(Resources paramResources, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = -1;
    if (this.mIsInitialized)
    {
      Log.e("RadialTextsView", "This RadialTextsView may only be initialized once.");
      return;
    }
    int j = paramResources.getColor(R.color.numbers_text_color);
    this.mPaint.setColor(j);
    this.mTypefaceLight = Typeface.create(paramResources.getString(R.string.radial_numbers_typeface), 0);
    this.mTypefaceRegular = Typeface.create(paramResources.getString(R.string.sans_serif), 0);
    this.mPaint.setAntiAlias(true);
    this.mPaint.setTextAlign(Paint.Align.CENTER);
    this.mTexts = paramArrayOfString1;
    this.mInnerTexts = paramArrayOfString2;
    this.mIs24HourMode = paramBoolean1;
    boolean bool = false;
    if (paramArrayOfString2 != null) {
      bool = true;
    }
    this.mHasInnerCircle = bool;
    if (paramBoolean1)
    {
      this.mCircleRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.circle_radius_multiplier_24HourMode));
      this.mTextGridHeights = new float[7];
      this.mTextGridWidths = new float[7];
      if (!this.mHasInnerCircle) {
        break label333;
      }
      this.mNumbersRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.numbers_radius_multiplier_outer));
      this.mTextSizeMultiplier = Float.parseFloat(paramResources.getString(R.string.text_size_multiplier_outer));
      this.mInnerNumbersRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.numbers_radius_multiplier_inner));
      this.mInnerTextSizeMultiplier = Float.parseFloat(paramResources.getString(R.string.text_size_multiplier_inner));
      this.mInnerTextGridHeights = new float[7];
      this.mInnerTextGridWidths = new float[7];
      label231:
      this.mAnimationRadiusMultiplier = 1.0F;
      if (!paramBoolean2) {
        break label364;
      }
    }
    label333:
    label364:
    for (int k = i;; k = 1)
    {
      this.mTransitionMidRadiusMultiplier = (1.0F + 0.05F * k);
      if (paramBoolean2) {
        i = 1;
      }
      this.mTransitionEndRadiusMultiplier = (1.0F + 0.3F * i);
      this.mInvalidateUpdateListener = new InvalidateUpdateListener(null);
      this.mTextGridValuesDirty = true;
      this.mIsInitialized = true;
      return;
      this.mCircleRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.circle_radius_multiplier));
      this.mAmPmCircleRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.ampm_circle_radius_multiplier));
      break;
      this.mNumbersRadiusMultiplier = Float.parseFloat(paramResources.getString(R.string.numbers_radius_multiplier_normal));
      this.mTextSizeMultiplier = Float.parseFloat(paramResources.getString(R.string.text_size_multiplier_normal));
      break label231;
    }
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    if ((getWidth() == 0) || (!this.mIsInitialized)) {}
    do
    {
      return;
      if (!this.mDrawValuesReady)
      {
        this.mXCenter = (getWidth() / 2);
        this.mYCenter = (getHeight() / 2);
        this.mCircleRadius = (Math.min(this.mXCenter, this.mYCenter) * this.mCircleRadiusMultiplier);
        if (!this.mIs24HourMode)
        {
          float f = this.mCircleRadius * this.mAmPmCircleRadiusMultiplier;
          this.mYCenter = ((int)(this.mYCenter - f / 2.0F));
        }
        this.mTextSize = (this.mCircleRadius * this.mTextSizeMultiplier);
        if (this.mHasInnerCircle) {
          this.mInnerTextSize = (this.mCircleRadius * this.mInnerTextSizeMultiplier);
        }
        renderAnimations();
        this.mTextGridValuesDirty = true;
        this.mDrawValuesReady = true;
      }
      if (this.mTextGridValuesDirty)
      {
        calculateGridSizes(this.mCircleRadius * this.mNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, this.mXCenter, this.mYCenter, this.mTextSize, this.mTextGridHeights, this.mTextGridWidths);
        if (this.mHasInnerCircle) {
          calculateGridSizes(this.mCircleRadius * this.mInnerNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, this.mXCenter, this.mYCenter, this.mInnerTextSize, this.mInnerTextGridHeights, this.mInnerTextGridWidths);
        }
        this.mTextGridValuesDirty = false;
      }
      drawTexts(paramCanvas, this.mTextSize, this.mTypefaceLight, this.mTexts, this.mTextGridWidths, this.mTextGridHeights);
    } while (!this.mHasInnerCircle);
    drawTexts(paramCanvas, this.mInnerTextSize, this.mTypefaceRegular, this.mInnerTexts, this.mInnerTextGridWidths, this.mInnerTextGridHeights);
  }
  
  public void setAnimationRadiusMultiplier(float paramFloat)
  {
    this.mAnimationRadiusMultiplier = paramFloat;
    this.mTextGridValuesDirty = true;
  }
  
  void setTheme(Context paramContext, boolean paramBoolean)
  {
    Resources localResources = paramContext.getResources();
    if (paramBoolean) {}
    for (int i = localResources.getColor(R.color.white);; i = localResources.getColor(R.color.numbers_text_color))
    {
      this.mPaint.setColor(i);
      return;
    }
  }
  
  private class InvalidateUpdateListener
    implements ValueAnimator.AnimatorUpdateListener
  {
    private InvalidateUpdateListener() {}
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      RadialTextsView.this.invalidate();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.time.RadialTextsView
 * JD-Core Version:    0.7.0.1
 */