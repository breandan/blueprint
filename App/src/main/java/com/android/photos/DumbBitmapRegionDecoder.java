package com.android.photos;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.io.InputStream;

class DumbBitmapRegionDecoder
  implements SimpleBitmapRegionDecoder
{
  Bitmap mBuffer;
  Canvas mTempCanvas;
  Paint mTempPaint;
  
  private DumbBitmapRegionDecoder(Bitmap paramBitmap)
  {
    this.mBuffer = paramBitmap;
  }
  
  public static DumbBitmapRegionDecoder newInstance(InputStream paramInputStream)
  {
    Bitmap localBitmap = BitmapFactory.decodeStream(paramInputStream);
    if (localBitmap != null) {
      return new DumbBitmapRegionDecoder(localBitmap);
    }
    return null;
  }
  
  public static DumbBitmapRegionDecoder newInstance(String paramString)
  {
    Bitmap localBitmap = BitmapFactory.decodeFile(paramString);
    if (localBitmap != null) {
      return new DumbBitmapRegionDecoder(localBitmap);
    }
    return null;
  }
  
  public Bitmap decodeRegion(Rect paramRect, BitmapFactory.Options paramOptions)
  {
    if (this.mTempCanvas == null)
    {
      this.mTempCanvas = new Canvas();
      this.mTempPaint = new Paint();
      this.mTempPaint.setFilterBitmap(true);
    }
    int i = Math.max(paramOptions.inSampleSize, 1);
    Bitmap localBitmap = Bitmap.createBitmap(paramRect.width() / i, paramRect.height() / i, Bitmap.Config.ARGB_8888);
    this.mTempCanvas.setBitmap(localBitmap);
    this.mTempCanvas.save();
    this.mTempCanvas.scale(1.0F / i, 1.0F / i);
    this.mTempCanvas.drawBitmap(this.mBuffer, -paramRect.left, -paramRect.top, this.mTempPaint);
    this.mTempCanvas.restore();
    this.mTempCanvas.setBitmap(null);
    return localBitmap;
  }
  
  public int getHeight()
  {
    return this.mBuffer.getHeight();
  }
  
  public int getWidth()
  {
    return this.mBuffer.getWidth();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.DumbBitmapRegionDecoder
 * JD-Core Version:    0.7.0.1
 */