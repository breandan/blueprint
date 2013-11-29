package com.android.photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;

abstract interface SimpleBitmapRegionDecoder
{
  public abstract Bitmap decodeRegion(Rect paramRect, BitmapFactory.Options paramOptions);
  
  public abstract int getHeight();
  
  public abstract int getWidth();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.SimpleBitmapRegionDecoder
 * JD-Core Version:    0.7.0.1
 */