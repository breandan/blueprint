package com.android.launcher3;

import android.graphics.Canvas;

class CanvasCache
  extends SoftReferenceThreadLocal<Canvas>
{
  protected Canvas initialValue()
  {
    return new Canvas();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.CanvasCache
 * JD-Core Version:    0.7.0.1
 */