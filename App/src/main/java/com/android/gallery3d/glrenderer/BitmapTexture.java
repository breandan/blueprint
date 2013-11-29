package com.android.gallery3d.glrenderer;

import android.graphics.Bitmap;
import junit.framework.Assert;

public class BitmapTexture
  extends UploadedTexture
{
  protected Bitmap mContentBitmap;
  
  public BitmapTexture(Bitmap paramBitmap)
  {
    this(paramBitmap, false);
  }
  
  public BitmapTexture(Bitmap paramBitmap, boolean paramBoolean)
  {
    super(paramBoolean);
    if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {}
    for (boolean bool = true;; bool = false)
    {
      Assert.assertTrue(bool);
      this.mContentBitmap = paramBitmap;
      return;
    }
  }
  
  protected void onFreeBitmap(Bitmap paramBitmap) {}
  
  protected Bitmap onGetBitmap()
  {
    return this.mContentBitmap;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.BitmapTexture
 * JD-Core Version:    0.7.0.1
 */