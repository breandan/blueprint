package com.google.android.search.shared.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import com.google.android.googlequicksearchbox.R.styleable;

public class RoundedCornerWebImageView
  extends WebImageView
{
  private boolean mApplyShader = true;
  private Paint mPaint;
  private float mRadius;
  private Shape mRoundedRectangle;
  private BitmapShader mShader;
  
  public RoundedCornerWebImageView(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public RoundedCornerWebImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public RoundedCornerWebImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (paramAttributeSet != null)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundedCornerWebImageView, paramInt, 0);
      this.mRadius = localTypedArray.getDimension(1, 0.0F);
      int i = localTypedArray.getInteger(0, 0);
      localTypedArray.recycle();
      createRoundedCornerMask(this.mRadius, i);
      return;
    }
    this.mRoundedRectangle = null;
    this.mPaint = null;
  }
  
  private void createRoundedCornerMask(float paramFloat, int paramInt)
  {
    if ((paramFloat > 0.0F) && (paramInt > 0))
    {
      float[] arrayOfFloat = new float[8];
      if ((paramInt & 0x1) != 0)
      {
        arrayOfFloat[0] = paramFloat;
        arrayOfFloat[1] = paramFloat;
      }
      if ((paramInt & 0x2) != 0)
      {
        arrayOfFloat[2] = paramFloat;
        arrayOfFloat[3] = paramFloat;
      }
      if ((paramInt & 0x4) != 0)
      {
        arrayOfFloat[4] = paramFloat;
        arrayOfFloat[5] = paramFloat;
      }
      if ((paramInt & 0x8) != 0)
      {
        arrayOfFloat[6] = paramFloat;
        arrayOfFloat[7] = paramFloat;
      }
      this.mRoundedRectangle = new RoundRectShape(arrayOfFloat, null, null);
      this.mPaint = new Paint();
      this.mPaint.setAntiAlias(true);
      return;
    }
    this.mRoundedRectangle = null;
    this.mPaint = null;
  }
  
  private void updateApplyShader()
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable == null) || (!(localDrawable instanceof BitmapDrawable)) || (this.mShader == null) || (this.mPaint == null)) {
      this.mApplyShader = false;
    }
    int i;
    int j;
    int k;
    int m;
    do
    {
      ImageView.ScaleType localScaleType;
      do
      {
        return;
        this.mApplyShader = true;
        localScaleType = getScaleType();
      } while ((localScaleType != ImageView.ScaleType.CENTER) && (localScaleType != ImageView.ScaleType.CENTER_INSIDE));
      i = getWidth() - getPaddingRight() - getPaddingLeft();
      j = getHeight() - getPaddingTop() - getPaddingBottom();
      k = localDrawable.getIntrinsicWidth();
      m = localDrawable.getIntrinsicHeight();
      if (localScaleType == ImageView.ScaleType.CENTER_INSIDE)
      {
        float f = Math.min(i / k, j / m);
        k = (int)(0.5F + f * k);
        m = (int)(0.5F + f * m);
      }
    } while ((k >= i) && (m >= j));
    this.mApplyShader = false;
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable == null) || (!(localDrawable instanceof BitmapDrawable)) || (this.mShader == null) || (this.mPaint == null) || (!this.mApplyShader))
    {
      super.onDraw(paramCanvas);
      return;
    }
    this.mShader.setLocalMatrix(getImageMatrix());
    this.mPaint.setShader(this.mShader);
    this.mRoundedRectangle.resize(getWidth(), getHeight());
    this.mRoundedRectangle.draw(paramCanvas, this.mPaint);
  }
  
  public void setCornerRadii(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F) && (paramFloat3 == 0.0F) && (paramFloat4 == 0.0F))
    {
      this.mRoundedRectangle = null;
      this.mPaint = null;
      this.mShader = null;
    }
    for (;;)
    {
      invalidate();
      return;
      this.mRoundedRectangle = new RoundRectShape(new float[] { paramFloat1, paramFloat1, paramFloat2, paramFloat2, paramFloat3, paramFloat3, paramFloat4, paramFloat4 }, null, null);
      if (this.mPaint == null)
      {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
      }
      if ((getDrawable() instanceof BitmapDrawable)) {
        this.mShader = new BitmapShader(((BitmapDrawable)getDrawable()).getBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      }
    }
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    updateApplyShader();
    return bool;
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    super.setImageDrawable(paramDrawable);
    if ((this.mRoundedRectangle != null) && ((getDrawable() instanceof BitmapDrawable))) {
      this.mShader = new BitmapShader(((BitmapDrawable)getDrawable()).getBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }
    updateApplyShader();
  }
  
  public void setRoundedCorners(int paramInt)
  {
    createRoundedCornerMask(this.mRadius, paramInt);
    invalidate();
  }
  
  public void setScaleType(ImageView.ScaleType paramScaleType)
  {
    super.setScaleType(paramScaleType);
    updateApplyShader();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.RoundedCornerWebImageView
 * JD-Core Version:    0.7.0.1
 */