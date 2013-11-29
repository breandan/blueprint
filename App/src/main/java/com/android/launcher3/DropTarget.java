package com.android.launcher3;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

public abstract interface DropTarget
{
  public abstract boolean acceptDrop(DragObject paramDragObject);
  
  public abstract void getHitRectRelativeToDragLayer(Rect paramRect);
  
  public abstract boolean isDropEnabled();
  
  public abstract void onDragEnter(DragObject paramDragObject);
  
  public abstract void onDragExit(DragObject paramDragObject);
  
  public abstract void onDragOver(DragObject paramDragObject);
  
  public abstract void onDrop(DragObject paramDragObject);
  
  public abstract void onFlingToDelete(DragObject paramDragObject, int paramInt1, int paramInt2, PointF paramPointF);
  
  public static class DragEnforcer
    implements DragController.DragListener
  {
    int dragParity = 0;
    
    public DragEnforcer(Context paramContext)
    {
      ((Launcher)paramContext).getDragController().addDragListener(this);
    }
    
    public void onDragEnd()
    {
      if (this.dragParity != 0) {
        Log.e("DropTarget", "onDragExit: Drag contract violated: " + this.dragParity);
      }
    }
    
    void onDragEnter()
    {
      this.dragParity = (1 + this.dragParity);
      if (this.dragParity != 1) {
        Log.e("DropTarget", "onDragEnter: Drag contract violated: " + this.dragParity);
      }
    }
    
    void onDragExit()
    {
      this.dragParity = (-1 + this.dragParity);
      if (this.dragParity != 0) {
        Log.e("DropTarget", "onDragExit: Drag contract violated: " + this.dragParity);
      }
    }
    
    public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt)
    {
      if (this.dragParity != 0) {
        Log.e("DropTarget", "onDragEnter: Drag contract violated: " + this.dragParity);
      }
    }
  }
  
  public static class DragObject
  {
    public boolean cancelled = false;
    public boolean deferDragViewCleanupPostAnimation = true;
    public boolean dragComplete = false;
    public Object dragInfo = null;
    public DragSource dragSource = null;
    public DragView dragView = null;
    public Runnable postAnimationRunnable = null;
    public int x = -1;
    public int xOffset = -1;
    public int y = -1;
    public int yOffset = -1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DropTarget
 * JD-Core Version:    0.7.0.1
 */