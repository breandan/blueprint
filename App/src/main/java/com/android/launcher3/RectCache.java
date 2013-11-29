package com.android.launcher3;

import android.graphics.Rect;

class RectCache
  extends SoftReferenceThreadLocal<Rect>
{
  protected Rect initialValue()
  {
    return new Rect();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.RectCache
 * JD-Core Version:    0.7.0.1
 */