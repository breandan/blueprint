package com.google.android.search.gel;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.android.launcher3.Insettable;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.common.base.Preconditions;

public class ScrimView
  extends View
  implements Insettable
{
  private final ValueAnimator mCircleAnimator;
  private Point mCircleCenter;
  private final Paint mCirclePaint = new Paint();
  private float mCircleRadiusRatio;
  private Callbacks mListener;
  private float mMaxCircleRadius;
  
  public ScrimView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mCirclePaint.setColor(Color.parseColor("#EFEEEE"));
    this.mCirclePaint.setAntiAlias(true);
    this.mCircleAnimator = new ValueAnimator();
    this.mCircleAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    this.mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        ScrimView.access$002(ScrimView.this, ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
        ScrimView.this.invalidate();
      }
    });
    this.mCircleAnimator.setDuration(500L);
  }
  
  private void setCircleExpanded(boolean paramBoolean1, boolean paramBoolean2)
  {
    float f;
    if (paramBoolean1)
    {
      f = 1.0F;
      if (!paramBoolean2) {
        break label38;
      }
      if (!isAnimating()) {
        break label23;
      }
    }
    label23:
    label38:
    while (isAnimating())
    {
      return;
      f = 0.0F;
      break;
      this.mCircleCenter = null;
      this.mCircleRadiusRatio = f;
      invalidate();
      return;
    }
    this.mCircleCenter = null;
    ValueAnimator localValueAnimator = this.mCircleAnimator;
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = this.mCircleRadiusRatio;
    arrayOfFloat[1] = f;
    localValueAnimator.setFloatValues(arrayOfFloat);
    this.mCircleAnimator.start();
  }
  
  public boolean isAnimating()
  {
    return this.mCircleAnimator.isRunning();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mCircleCenter == null)
    {
      this.mCircleCenter = this.mListener.getCircleCenter();
      this.mMaxCircleRadius = QuantumPaperUtils.getFillRadius(getWidth(), getHeight(), this.mCircleCenter.x, this.mCircleCenter.y);
    }
    paramCanvas.drawCircle(this.mCircleCenter.x, this.mCircleCenter.y, this.mMaxCircleRadius * this.mCircleRadiusRatio, this.mCirclePaint);
  }
  
  public void setInsets(Rect paramRect) {}
  
  public void setListener(Callbacks paramCallbacks)
  {
    if ((this.mListener == null) || (paramCallbacks == null)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mListener = paramCallbacks;
      return;
    }
  }
  
  public void setMode(int paramInt, boolean paramBoolean)
  {
    if ((paramInt == 7) || (paramInt == 8)) {
      setCircleExpanded(true, paramBoolean);
    }
    for (;;)
    {
      invalidate();
      return;
      setCircleExpanded(false, true);
    }
  }
  
  public static abstract interface Callbacks
  {
    public abstract Point getCircleCenter();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.ScrimView
 * JD-Core Version:    0.7.0.1
 */