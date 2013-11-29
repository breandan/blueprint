package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class ButtonDropTarget
  extends TextView
  implements DragController.DragListener, DropTarget
{
  protected boolean mActive;
  private int mBottomDragPadding;
  protected int mHoverColor = 0;
  protected Launcher mLauncher;
  protected SearchDropTargetBar mSearchDropTargetBar;
  protected final int mTransitionDuration;
  
  public ButtonDropTarget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ButtonDropTarget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Resources localResources = getResources();
    this.mTransitionDuration = localResources.getInteger(2131427350);
    this.mBottomDragPadding = localResources.getDimensionPixelSize(2131689539);
  }
  
  private boolean isRtl()
  {
    return getLayoutDirection() == 1;
  }
  
  public boolean acceptDrop(DropTarget.DragObject paramDragObject)
  {
    return false;
  }
  
  protected Drawable getCurrentDrawable()
  {
    Drawable[] arrayOfDrawable = getCompoundDrawablesRelative();
    for (int i = 0; i < arrayOfDrawable.length; i++) {
      if (arrayOfDrawable[i] != null) {
        return arrayOfDrawable[i];
      }
    }
    return null;
  }
  
  public void getHitRectRelativeToDragLayer(Rect paramRect)
  {
    super.getHitRect(paramRect);
    paramRect.bottom += this.mBottomDragPadding;
    int[] arrayOfInt = new int[2];
    this.mLauncher.getDragLayer().getDescendantCoordRelativeToSelf(this, arrayOfInt);
    paramRect.offsetTo(arrayOfInt[0], arrayOfInt[1]);
  }
  
  Rect getIconRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    DragLayer localDragLayer = this.mLauncher.getDragLayer();
    Rect localRect = new Rect();
    localDragLayer.getViewRectRelativeToSelf(this, localRect);
    int j;
    int i;
    if (isRtl())
    {
      j = localRect.right - getPaddingRight();
      i = j - paramInt3;
    }
    for (;;)
    {
      int k = localRect.top + (getMeasuredHeight() - paramInt4) / 2;
      localRect.set(i, k, j, k + paramInt4);
      localRect.offset(-(paramInt1 - paramInt3) / 2, -(paramInt2 - paramInt4) / 2);
      return localRect;
      i = localRect.left + getPaddingLeft();
      j = i + paramInt3;
    }
  }
  
  public boolean isDropEnabled()
  {
    return this.mActive;
  }
  
  public void onDragEnd() {}
  
  public void onDragEnter(DropTarget.DragObject paramDragObject)
  {
    paramDragObject.dragView.setColor(this.mHoverColor);
  }
  
  public void onDragExit(DropTarget.DragObject paramDragObject)
  {
    paramDragObject.dragView.setColor(0);
  }
  
  public void onDragOver(DropTarget.DragObject paramDragObject) {}
  
  public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt) {}
  
  public void onDrop(DropTarget.DragObject paramDragObject) {}
  
  public void onFlingToDelete(DropTarget.DragObject paramDragObject, int paramInt1, int paramInt2, PointF paramPointF) {}
  
  void setLauncher(Launcher paramLauncher)
  {
    this.mLauncher = paramLauncher;
  }
  
  public void setSearchDropTargetBar(SearchDropTargetBar paramSearchDropTargetBar)
  {
    this.mSearchDropTargetBar = paramSearchDropTargetBar;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.ButtonDropTarget
 * JD-Core Version:    0.7.0.1
 */