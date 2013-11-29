package com.android.launcher3;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.GridLayout;

public class PagedViewGridLayout
  extends GridLayout
  implements Page
{
  private int mCellCountX;
  private int mCellCountY;
  private Runnable mOnLayoutListener;
  
  public PagedViewGridLayout(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext, null, 0);
    this.mCellCountX = paramInt1;
    this.mCellCountY = paramInt2;
  }
  
  int getCellCountX()
  {
    return this.mCellCountX;
  }
  
  int getCellCountY()
  {
    return this.mCellCountY;
  }
  
  public View getChildOnPageAt(int paramInt)
  {
    return getChildAt(paramInt);
  }
  
  public int getPageChildCount()
  {
    return getChildCount();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mOnLayoutListener = null;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mOnLayoutListener != null) {
      this.mOnLayoutListener.run();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(View.MeasureSpec.makeMeasureSpec(Math.min(getSuggestedMinimumWidth(), View.MeasureSpec.getSize(paramInt1)), 1073741824), paramInt2);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    int i = getPageChildCount();
    if (i > 0)
    {
      int j = getChildOnPageAt(i - 1).getBottom();
      if ((bool) || (paramMotionEvent.getY() < j)) {
        bool = true;
      }
    }
    else
    {
      return bool;
    }
    return false;
  }
  
  public void removeAllViewsOnPage()
  {
    removeAllViews();
    this.mOnLayoutListener = null;
    setLayerType(0, null);
  }
  
  public void resetChildrenOnKeyListeners()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).setOnKeyListener(null);
    }
  }
  
  public void setOnLayoutListener(Runnable paramRunnable)
  {
    this.mOnLayoutListener = paramRunnable;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewGridLayout
 * JD-Core Version:    0.7.0.1
 */