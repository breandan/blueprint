package com.android.launcher3;

import android.graphics.Paint;

class PaintCache
  extends SoftReferenceThreadLocal<Paint>
{
  protected Paint initialValue()
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PaintCache
 * JD-Core Version:    0.7.0.1
 */