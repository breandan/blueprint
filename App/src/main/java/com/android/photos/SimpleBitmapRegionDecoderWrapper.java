package com.android.photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

class SimpleBitmapRegionDecoderWrapper
  implements SimpleBitmapRegionDecoder
{
  BitmapRegionDecoder mDecoder;
  
  private SimpleBitmapRegionDecoderWrapper(BitmapRegionDecoder paramBitmapRegionDecoder)
  {
    this.mDecoder = paramBitmapRegionDecoder;
  }
  
  public static SimpleBitmapRegionDecoderWrapper newInstance(InputStream paramInputStream, boolean paramBoolean)
  {
    try
    {
      BitmapRegionDecoder localBitmapRegionDecoder = BitmapRegionDecoder.newInstance(paramInputStream, paramBoolean);
      if (localBitmapRegionDecoder != null)
      {
        SimpleBitmapRegionDecoderWrapper localSimpleBitmapRegionDecoderWrapper = new SimpleBitmapRegionDecoderWrapper(localBitmapRegionDecoder);
        return localSimpleBitmapRegionDecoderWrapper;
      }
    }
    catch (IOException localIOException)
    {
      Log.w("BitmapRegionTileSource", "getting decoder failed", localIOException);
      return null;
    }
    return null;
  }
  
  public static SimpleBitmapRegionDecoderWrapper newInstance(String paramString, boolean paramBoolean)
  {
    try
    {
      BitmapRegionDecoder localBitmapRegionDecoder = BitmapRegionDecoder.newInstance(paramString, paramBoolean);
      if (localBitmapRegionDecoder != null)
      {
        SimpleBitmapRegionDecoderWrapper localSimpleBitmapRegionDecoderWrapper = new SimpleBitmapRegionDecoderWrapper(localBitmapRegionDecoder);
        return localSimpleBitmapRegionDecoderWrapper;
      }
    }
    catch (IOException localIOException)
    {
      Log.w("BitmapRegionTileSource", "getting decoder failed for path " + paramString, localIOException);
      return null;
    }
    return null;
  }
  
  public Bitmap decodeRegion(Rect paramRect, BitmapFactory.Options paramOptions)
  {
    return this.mDecoder.decodeRegion(paramRect, paramOptions);
  }
  
  public int getHeight()
  {
    return this.mDecoder.getHeight();
  }
  
  public int getWidth()
  {
    return this.mDecoder.getWidth();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.SimpleBitmapRegionDecoderWrapper
 * JD-Core Version:    0.7.0.1
 */