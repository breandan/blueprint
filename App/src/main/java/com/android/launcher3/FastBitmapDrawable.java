package com.android.launcher3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class FastBitmapDrawable
  extends Drawable
{
  private int mAlpha = 255;
  private Bitmap mBitmap;
  private int mHeight;
  private final Paint mPaint = new Paint(2);
  private int mWidth;
  
  FastBitmapDrawable(Bitmap paramBitmap)
  {
    this.mBitmap = paramBitmap;
    if (paramBitmap != null)
    {
      this.mWidth = this.mBitmap.getWidth();
      this.mHeight = this.mBitmap.getHeight();
      return;
    }
    this.mHeight = 0;
    this.mWidth = 0;
  }
  
  public void draw(Canvas paramCanvas)
  {
    Rect localRect = getBounds();
    paramCanvas.drawBitmap(this.mBitmap, null, localRect, this.mPaint);
  }
  
  public int getAlpha()
  {
    return this.mAlpha;
  }
  
  public Bitmap getBitmap()
  {
    return this.mBitmap;
  }
  
  public int getIntrinsicHeight()
  {
    return this.mHeight;
  }
  
  public int getIntrinsicWidth()
  {
    return this.mWidth;
  }
  
  public int getMinimumHeight()
  {
    return this.mHeight;
  }
  
  public int getMinimumWidth()
  {
    return this.mWidth;
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public void setAlpha(int paramInt)
  {
    this.mAlpha = paramInt;
    this.mPaint.setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    this.mPaint.setColorFilter(paramColorFilter);
  }
  
  public void setFilterBitmap(boolean paramBoolean)
  {
    this.mPaint.setFilterBitmap(paramBoolean);
    this.mPaint.setAntiAlias(paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FastBitmapDrawable
 * JD-Core Version:    0.7.0.1
 */