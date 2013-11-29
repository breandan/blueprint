package com.android.launcher3;

import android.graphics.BitmapFactory.Options;

class BitmapFactoryOptionsCache
  extends SoftReferenceThreadLocal<BitmapFactory.Options>
{
  protected BitmapFactory.Options initialValue()
  {
    return new BitmapFactory.Options();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.BitmapFactoryOptionsCache
 * JD-Core Version:    0.7.0.1
 */