package com.google.android.sidekick.shared.ui;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.google.android.search.shared.ui.BakedBezierInterpolator;

public class NowProgressBar
  extends View
{
  private static final TimeInterpolator INTERPOLATOR = BakedBezierInterpolator.INSTANCE;
  private int mBlue;
  private final RectF mClipRect = new RectF();
  private long mFinishTime;
  private int mGreen;
  private final Paint mPaint = new Paint();
  private int mRed;
  private ObjectAnimator mReleaseAnimator;
  private boolean mRunning;
  private long mStartTime;
  private float mTriggerPercentage;
  private int mYellow;
  
  public NowProgressBar(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  public NowProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public NowProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void drawCircle(Canvas paramCanvas, float paramFloat1, float paramFloat2, int paramInt, float paramFloat3)
  {
    this.mPaint.setColor(paramInt);
    paramCanvas.drawCircle(paramFloat1, paramFloat2, paramFloat1 * INTERPOLATOR.getInterpolation(paramFloat3), this.mPaint);
  }
  
  private void init(Context paramContext)
  {
    this.mGreen = paramContext.getResources().getColor(2131230822);
    this.mRed = paramContext.getResources().getColor(2131230823);
    this.mBlue = paramContext.getResources().getColor(2131230824);
    this.mYellow = paramContext.getResources().getColor(2131230825);
    setLayerType(0, null);
  }
  
  public void cancelReleaseTrigger()
  {
    if (this.mReleaseAnimator != null)
    {
      this.mReleaseAnimator.cancel();
      this.mReleaseAnimator = null;
    }
    setTriggerPercentage(0.0F);
  }
  
  public boolean isRunning()
  {
    return this.mRunning;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    int i = getWidth();
    int j = getHeight();
    int k = i / 2;
    int m = j / 2;
    if ((this.mRunning) || (this.mFinishTime > 0L))
    {
      l1 = AnimationUtils.currentAnimationTimeMillis();
      l2 = (l1 - this.mStartTime) % 2000L;
      l3 = (l1 - this.mStartTime) / 2000L;
      f1 = (float)l2 / 20.0F;
      if (!this.mRunning) {
        if (l1 - this.mFinishTime >= 1000L) {
          this.mFinishTime = 0L;
        }
      }
    }
    while ((this.mTriggerPercentage <= 0.0F) || (this.mTriggerPercentage > 1.0D))
    {
      long l1;
      long l2;
      long l3;
      float f1;
      return;
      float f7 = (float)((l1 - this.mFinishTime) % 1000L) / 10.0F / 100.0F;
      float f8 = i * INTERPOLATOR.getInterpolation(f7);
      this.mClipRect.set(k - f8, 0.0F, f8 + k, j);
      paramCanvas.clipRect(this.mClipRect, Region.Op.DIFFERENCE);
      if (l3 == 0L) {
        paramCanvas.drawColor(this.mGreen);
      }
      for (;;)
      {
        if ((f1 >= 0.0F) && (f1 <= 25.0F))
        {
          float f6 = 2.0F * (25.0F + f1) / 100.0F;
          drawCircle(paramCanvas, k, m, this.mGreen, f6);
        }
        if ((f1 >= 0.0F) && (f1 <= 50.0F))
        {
          float f5 = 2.0F * f1 / 100.0F;
          drawCircle(paramCanvas, k, m, this.mRed, f5);
        }
        if ((f1 >= 25.0F) && (f1 <= 75.0F))
        {
          float f4 = 2.0F * (f1 - 25.0F) / 100.0F;
          drawCircle(paramCanvas, k, m, this.mBlue, f4);
        }
        if ((f1 >= 50.0F) && (f1 <= 100.0F))
        {
          float f3 = 2.0F * (f1 - 50.0F) / 100.0F;
          drawCircle(paramCanvas, k, m, this.mYellow, f3);
        }
        if ((f1 >= 75.0F) && (f1 <= 100.0F))
        {
          float f2 = 2.0F * (f1 - 75.0F) / 100.0F;
          drawCircle(paramCanvas, k, m, this.mGreen, f2);
        }
        postInvalidateOnAnimation();
        return;
        if ((f1 >= 0.0F) && (f1 < 25.0F)) {
          paramCanvas.drawColor(this.mYellow);
        } else if ((f1 >= 25.0F) && (f1 < 50.0F)) {
          paramCanvas.drawColor(this.mGreen);
        } else if ((f1 >= 50.0F) && (f1 < 75.0F)) {
          paramCanvas.drawColor(this.mRed);
        } else {
          paramCanvas.drawColor(this.mBlue);
        }
      }
    }
    this.mPaint.setColor(this.mGreen);
    paramCanvas.drawCircle(k, m, k * this.mTriggerPercentage, this.mPaint);
  }
  
  public void releaseTrigger()
  {
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = this.mTriggerPercentage;
    arrayOfFloat[1] = 0.0F;
    this.mReleaseAnimator = ObjectAnimator.ofFloat(this, "triggerPercentage", arrayOfFloat).setDuration(300L);
    this.mReleaseAnimator.start();
  }
  
  public void setTriggerPercentage(float paramFloat)
  {
    this.mTriggerPercentage = paramFloat;
    this.mStartTime = 0L;
    postInvalidate();
  }
  
  public void start()
  {
    if (!this.mRunning)
    {
      this.mTriggerPercentage = 0.0F;
      this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
      this.mRunning = true;
      postInvalidate();
    }
  }
  
  public void stop()
  {
    if (this.mRunning)
    {
      this.mTriggerPercentage = 0.0F;
      this.mFinishTime = AnimationUtils.currentAnimationTimeMillis();
      this.mRunning = false;
      postInvalidate();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.NowProgressBar
 * JD-Core Version:    0.7.0.1
 */