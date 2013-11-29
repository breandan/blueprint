package com.android.launcher3;

import android.view.View;

public abstract interface DragSource
{
  public abstract void onDropCompleted(View paramView, DropTarget.DragObject paramDragObject, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void onFlingToDeleteCompleted();
  
  public abstract boolean supportsFlingToDelete();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DragSource
 * JD-Core Version:    0.7.0.1
 */