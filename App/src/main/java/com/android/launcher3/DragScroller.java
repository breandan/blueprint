package com.android.launcher3;

public abstract interface DragScroller
{
  public abstract boolean onEnterScrollArea(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean onExitScrollArea();
  
  public abstract void scrollLeft();
  
  public abstract void scrollRight();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DragScroller
 * JD-Core Version:    0.7.0.1
 */