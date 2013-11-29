package com.android.launcher3;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;

public class DragView
  extends View
{
  private static float sDragAlpha = 1.0F;
  ValueAnimator mAnim;
  private Bitmap mBitmap;
  private Bitmap mCrossFadeBitmap;
  private float mCrossFadeProgress = 0.0F;
  private DragLayer mDragLayer = null;
  private Rect mDragRegion = null;
  private Point mDragVisualizeOffset = null;
  private boolean mHasDrawn = false;
  private float mInitialScale = 1.0F;
  private float mOffsetX = 0.0F;
  private float mOffsetY = 0.0F;
  private Paint mPaint;
  private int mRegistrationX;
  private int mRegistrationY;
  
  public DragView(Launcher paramLauncher, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, final float paramFloat)
  {
    super(paramLauncher);
    this.mDragLayer = paramLauncher.getDragLayer();
    this.mInitialScale = paramFloat;
    Resources localResources = getResources();
    final float f1 = localResources.getDimensionPixelSize(2131689541);
    final float f2 = localResources.getDimensionPixelSize(2131689542);
    final float f3 = (localResources.getDimensionPixelSize(2131689543) + paramInt5) / paramInt5;
    setScaleX(paramFloat);
    setScaleY(paramFloat);
    this.mAnim = LauncherAnimUtils.ofFloat(this, new float[] { 0.0F, 1.0F });
    this.mAnim.setDuration(150L);
    this.mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        int i = (int)(f * f1 - DragView.this.mOffsetX);
        int j = (int)(f * f2 - DragView.this.mOffsetY);
        DragView.access$016(DragView.this, i);
        DragView.access$116(DragView.this, j);
        DragView.this.setScaleX(paramFloat + f * (f3 - paramFloat));
        DragView.this.setScaleY(paramFloat + f * (f3 - paramFloat));
        if (DragView.sDragAlpha != 1.0F) {
          DragView.this.setAlpha(f * DragView.sDragAlpha + (1.0F - f));
        }
        if (DragView.this.getParent() == null)
        {
          paramAnonymousValueAnimator.cancel();
          return;
        }
        DragView.this.setTranslationX(DragView.this.getTranslationX() + i);
        DragView.this.setTranslationY(DragView.this.getTranslationY() + j);
      }
    });
    this.mBitmap = Bitmap.createBitmap(paramBitmap, paramInt3, paramInt4, paramInt5, paramInt6);
    setDragRegion(new Rect(0, 0, paramInt5, paramInt6));
    this.mRegistrationX = paramInt1;
    this.mRegistrationY = paramInt2;
    int i = View.MeasureSpec.makeMeasureSpec(0, 0);
    measure(i, i);
    this.mPaint = new Paint(2);
  }
  
  public void cancelAnimation()
  {
    if ((this.mAnim != null) && (this.mAnim.isRunning())) {
      this.mAnim.cancel();
    }
  }
  
  public void crossFade(int paramInt)
  {
    ValueAnimator localValueAnimator = LauncherAnimUtils.ofFloat(this, new float[] { 0.0F, 1.0F });
    localValueAnimator.setDuration(paramInt);
    localValueAnimator.setInterpolator(new DecelerateInterpolator(1.5F));
    localValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        DragView.access$302(DragView.this, paramAnonymousValueAnimator.getAnimatedFraction());
      }
    });
    localValueAnimator.start();
  }
  
  public Rect getDragRegion()
  {
    return this.mDragRegion;
  }
  
  public int getDragRegionTop()
  {
    return this.mDragRegion.top;
  }
  
  public Point getDragVisualizeOffset()
  {
    return this.mDragVisualizeOffset;
  }
  
  public float getInitialScale()
  {
    return this.mInitialScale;
  }
  
  public boolean hasDrawn()
  {
    return this.mHasDrawn;
  }
  
  void move(int paramInt1, int paramInt2)
  {
    setTranslationX(paramInt1 - this.mRegistrationX + (int)this.mOffsetX);
    setTranslationY(paramInt2 - this.mRegistrationY + (int)this.mOffsetY);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    boolean bool = true;
    this.mHasDrawn = bool;
    if ((this.mCrossFadeProgress > 0.0F) && (this.mCrossFadeBitmap != null)) {
      if (bool) {
        if (!bool) {
          break label156;
        }
      }
    }
    label156:
    for (int i = (int)(255.0F * (1.0F - this.mCrossFadeProgress));; i = 255)
    {
      this.mPaint.setAlpha(i);
      paramCanvas.drawBitmap(this.mBitmap, 0.0F, 0.0F, this.mPaint);
      if (bool)
      {
        this.mPaint.setAlpha((int)(255.0F * this.mCrossFadeProgress));
        paramCanvas.save();
        paramCanvas.scale(1.0F * this.mBitmap.getWidth() / this.mCrossFadeBitmap.getWidth(), 1.0F * this.mBitmap.getHeight() / this.mCrossFadeBitmap.getHeight());
        paramCanvas.drawBitmap(this.mCrossFadeBitmap, 0.0F, 0.0F, this.mPaint);
        paramCanvas.restore();
      }
      return;
      bool = false;
      break;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(this.mBitmap.getWidth(), this.mBitmap.getHeight());
  }
  
  void remove()
  {
    if (getParent() != null) {
      this.mDragLayer.removeView(this);
    }
  }
  
  public void resetLayoutParams()
  {
    this.mOffsetY = 0.0F;
    this.mOffsetX = 0.0F;
    requestLayout();
  }
  
  public void setAlpha(float paramFloat)
  {
    super.setAlpha(paramFloat);
    this.mPaint.setAlpha((int)(255.0F * paramFloat));
    invalidate();
  }
  
  public void setColor(int paramInt)
  {
    if (this.mPaint == null) {
      this.mPaint = new Paint(2);
    }
    if (paramInt != 0) {
      this.mPaint.setColorFilter(new PorterDuffColorFilter(paramInt, PorterDuff.Mode.SRC_ATOP));
    }
    for (;;)
    {
      invalidate();
      return;
      this.mPaint.setColorFilter(null);
    }
  }
  
  public void setCrossFadeBitmap(Bitmap paramBitmap)
  {
    this.mCrossFadeBitmap = paramBitmap;
  }
  
  public void setDragRegion(Rect paramRect)
  {
    this.mDragRegion = paramRect;
  }
  
  public void setDragVisualizeOffset(Point paramPoint)
  {
    this.mDragVisualizeOffset = paramPoint;
  }
  
  public void show(int paramInt1, int paramInt2)
  {
    this.mDragLayer.addView(this);
    DragLayer.LayoutParams localLayoutParams = new DragLayer.LayoutParams(0, 0);
    localLayoutParams.width = this.mBitmap.getWidth();
    localLayoutParams.height = this.mBitmap.getHeight();
    localLayoutParams.customPosition = true;
    setLayoutParams(localLayoutParams);
    setTranslationX(paramInt1 - this.mRegistrationX);
    setTranslationY(paramInt2 - this.mRegistrationY);
    post(new Runnable()
    {
      public void run()
      {
        DragView.this.mAnim.start();
      }
    });
  }
  
  public void updateInitialScaleToCurrentScale()
  {
    this.mInitialScale = getScaleX();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DragView
 * JD-Core Version:    0.7.0.1
 */