package com.android.launcher3;

import android.graphics.Bitmap;

class BitmapCache
  extends SoftReferenceThreadLocal<Bitmap>
{
  protected Bitmap initialValue()
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.BitmapCache
 * JD-Core Version:    0.7.0.1
 */