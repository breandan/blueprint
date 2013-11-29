package com.android.launcher3;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class PagedViewCellLayoutChildren
  extends ViewGroup
{
  private int mCellHeight;
  private int mCellWidth;
  private boolean mCenterContent;
  private int mHeightGap;
  private int mWidthGap;
  
  public PagedViewCellLayoutChildren(Context paramContext)
  {
    super(paramContext);
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).cancelLongPress();
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    boolean bool = this.mCenterContent;
    int j = 0;
    if (bool)
    {
      j = 0;
      if (i > 0)
      {
        int i1 = 0;
        int i2 = 2147483647;
        for (int i3 = 0; i3 < i; i3++)
        {
          View localView2 = getChildAt(i3);
          if (localView2.getVisibility() != 8)
          {
            PagedViewCellLayout.LayoutParams localLayoutParams2 = (PagedViewCellLayout.LayoutParams)localView2.getLayoutParams();
            i2 = Math.min(i2, localLayoutParams2.x);
            i1 = Math.max(i1, localLayoutParams2.x + localLayoutParams2.width);
          }
        }
        int i4 = i1 - i2;
        j = (getMeasuredWidth() - i4) / 2;
      }
    }
    for (int k = 0; k < i; k++)
    {
      View localView1 = getChildAt(k);
      if (localView1.getVisibility() != 8)
      {
        PagedViewCellLayout.LayoutParams localLayoutParams1 = (PagedViewCellLayout.LayoutParams)localView1.getLayoutParams();
        int m = j + localLayoutParams1.x;
        int n = localLayoutParams1.y;
        localView1.layout(m, n, m + localLayoutParams1.width, n + localLayoutParams1.height);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    if ((i == 0) || (k == 0)) {
      throw new RuntimeException("CellLayout cannot have UNSPECIFIED dimensions");
    }
    int n = getChildCount();
    for (int i1 = 0; i1 < n; i1++)
    {
      View localView = getChildAt(i1);
      PagedViewCellLayout.LayoutParams localLayoutParams = (PagedViewCellLayout.LayoutParams)localView.getLayoutParams();
      localLayoutParams.setup(getContext(), this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, getPaddingLeft(), getPaddingTop());
      localView.measure(View.MeasureSpec.makeMeasureSpec(localLayoutParams.width, 1073741824), View.MeasureSpec.makeMeasureSpec(localLayoutParams.height, 1073741824));
    }
    setMeasuredDimension(j, m);
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    if (paramView1 != null)
    {
      Rect localRect = new Rect();
      paramView1.getDrawingRect(localRect);
      requestRectangleOnScreen(localRect);
    }
  }
  
  public void setCellDimensions(int paramInt1, int paramInt2)
  {
    this.mCellWidth = paramInt1;
    this.mCellHeight = paramInt2;
    requestLayout();
  }
  
  protected void setChildrenDrawingCacheEnabled(boolean paramBoolean)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      localView.setDrawingCacheEnabled(paramBoolean);
      if (!localView.isHardwareAccelerated()) {
        localView.buildDrawingCache(true);
      }
    }
  }
  
  public void setGap(int paramInt1, int paramInt2)
  {
    this.mWidthGap = paramInt1;
    this.mHeightGap = paramInt2;
    requestLayout();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewCellLayoutChildren
 * JD-Core Version:    0.7.0.1
 */