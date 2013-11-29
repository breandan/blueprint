package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public abstract class PagedViewWithDraggableItems
  extends PagedView
  implements View.OnLongClickListener, View.OnTouchListener
{
  private float mDragSlopeThreshold;
  private boolean mIsDragEnabled;
  private boolean mIsDragging;
  private View mLastTouchedItem;
  private Launcher mLauncher;
  
  public PagedViewWithDraggableItems(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagedViewWithDraggableItems(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PagedViewWithDraggableItems(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mLauncher = ((Launcher)paramContext);
  }
  
  private void handleTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (0xFF & paramMotionEvent.getAction())
    {
    }
    do
    {
      return;
      cancelDragging();
      this.mIsDragEnabled = true;
      return;
    } while ((this.mTouchState == 1) || (this.mIsDragging) || (!this.mIsDragEnabled));
    determineDraggingStart(paramMotionEvent);
  }
  
  protected boolean beginDragging(View paramView)
  {
    boolean bool = this.mIsDragging;
    this.mIsDragging = true;
    return !bool;
  }
  
  protected void cancelDragging()
  {
    this.mIsDragging = false;
    this.mLastTouchedItem = null;
    this.mIsDragEnabled = false;
  }
  
  protected void determineDraggingStart(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
    float f1 = paramMotionEvent.getX(i);
    float f2 = paramMotionEvent.getY(i);
    int j = (int)Math.abs(f1 - this.mLastMotionX);
    int k = (int)Math.abs(f2 - this.mLastMotionY);
    int m;
    if (k > this.mTouchSlop)
    {
      m = 1;
      if (k / j <= this.mDragSlopeThreshold) {
        break label142;
      }
    }
    label142:
    for (int n = 1;; n = 0)
    {
      if ((n != 0) && (m != 0) && (this.mLastTouchedItem != null))
      {
        beginDragging(this.mLastTouchedItem);
        if (this.mAllowLongPress)
        {
          this.mAllowLongPress = false;
          View localView = getPageAt(this.mCurrentPage);
          if (localView != null) {
            localView.cancelLongPress();
          }
        }
      }
      return;
      m = 0;
      break;
    }
  }
  
  protected void determineScrollingStart(MotionEvent paramMotionEvent)
  {
    if (!this.mIsDragging) {
      super.determineScrollingStart(paramMotionEvent);
    }
  }
  
  protected void onDetachedFromWindow()
  {
    cancelDragging();
    super.onDetachedFromWindow();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    handleTouchEvent(paramMotionEvent);
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public boolean onLongClick(View paramView)
  {
    if (!paramView.isInTouchMode()) {}
    while ((this.mNextPage != -1) || (!this.mLauncher.isAllAppsVisible()) || (this.mLauncher.getWorkspace().isSwitchingState()) || (!this.mLauncher.isDraggingEnabled())) {
      return false;
    }
    return beginDragging(paramView);
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    this.mLastTouchedItem = paramView;
    this.mIsDragEnabled = true;
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    handleTouchEvent(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setDragSlopeThreshold(float paramFloat)
  {
    this.mDragSlopeThreshold = paramFloat;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewWithDraggableItems
 * JD-Core Version:    0.7.0.1
 */