package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

public class PagedViewCellLayout
  extends ViewGroup
  implements Page
{
  private int mCellCountX;
  private int mCellCountY;
  private int mCellHeight;
  private int mCellWidth;
  protected PagedViewCellLayoutChildren mChildren;
  private int mHeightGap;
  private int mOriginalCellHeight;
  private int mOriginalCellWidth;
  private int mOriginalHeightGap;
  private int mOriginalWidthGap;
  private int mWidthGap;
  
  public PagedViewCellLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagedViewCellLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PagedViewCellLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setAlwaysDrawnWithCacheEnabled(false);
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = localDeviceProfile.cellWidthPx;
    this.mCellWidth = i;
    this.mOriginalCellWidth = i;
    int j = localDeviceProfile.cellHeightPx;
    this.mCellHeight = j;
    this.mOriginalCellHeight = j;
    this.mCellCountX = ((int)localDeviceProfile.numColumns);
    this.mCellCountY = ((int)localDeviceProfile.numRows);
    this.mHeightGap = -1;
    this.mWidthGap = -1;
    this.mOriginalHeightGap = -1;
    this.mOriginalWidthGap = -1;
    this.mChildren = new PagedViewCellLayoutChildren(paramContext);
    this.mChildren.setCellDimensions(this.mCellWidth, this.mCellHeight);
    this.mChildren.setGap(this.mWidthGap, this.mHeightGap);
    addView(this.mChildren);
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).cancelLongPress();
    }
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public int estimateCellHeight(int paramInt)
  {
    return paramInt * this.mCellHeight;
  }
  
  public int[] estimateCellPosition(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = (getPaddingLeft() + paramInt1 * this.mCellWidth + paramInt1 * this.mWidthGap + this.mCellWidth / 2);
    arrayOfInt[1] = (getPaddingTop() + paramInt2 * this.mCellHeight + paramInt2 * this.mHeightGap + this.mCellHeight / 2);
    return arrayOfInt;
  }
  
  public int estimateCellWidth(int paramInt)
  {
    return paramInt * this.mCellWidth;
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getCellCountX()
  {
    return this.mCellCountX;
  }
  
  public int getCellCountY()
  {
    return this.mCellCountY;
  }
  
  public View getChildOnPageAt(int paramInt)
  {
    return this.mChildren.getChildAt(paramInt);
  }
  
  public int getPageChildCount()
  {
    return this.mChildren.getChildCount();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).layout(getPaddingLeft(), getPaddingTop(), paramInt3 - paramInt1 - getPaddingRight(), paramInt4 - paramInt2 - getPaddingBottom());
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
    int n = -1 + this.mCellCountX;
    int i1 = -1 + this.mCellCountY;
    int i6;
    int i7;
    if ((this.mOriginalWidthGap < 0) || (this.mOriginalHeightGap < 0))
    {
      int i2 = j - getPaddingLeft() - getPaddingRight();
      int i3 = m - getPaddingTop() - getPaddingBottom();
      int i4 = i2 - this.mCellCountX * this.mOriginalCellWidth;
      int i5 = i3 - this.mCellCountY * this.mOriginalCellHeight;
      if (n > 0)
      {
        i6 = i4 / n;
        this.mWidthGap = i6;
        if (i1 <= 0) {
          break label338;
        }
        i7 = i5 / i1;
        label158:
        this.mHeightGap = i7;
        this.mChildren.setGap(this.mWidthGap, this.mHeightGap);
      }
    }
    int i8;
    int i9;
    for (;;)
    {
      i8 = j;
      i9 = m;
      if (i == -2147483648)
      {
        i8 = getPaddingLeft() + getPaddingRight() + this.mCellCountX * this.mCellWidth + (-1 + this.mCellCountX) * this.mWidthGap;
        i9 = getPaddingTop() + getPaddingBottom() + this.mCellCountY * this.mCellHeight + (-1 + this.mCellCountY) * this.mHeightGap;
        setMeasuredDimension(i8, i9);
      }
      int i10 = getChildCount();
      for (int i11 = 0; i11 < i10; i11++) {
        getChildAt(i11).measure(View.MeasureSpec.makeMeasureSpec(i8 - getPaddingLeft() - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec(i9 - getPaddingTop() - getPaddingBottom(), 1073741824));
      }
      i6 = 0;
      break;
      label338:
      i7 = 0;
      break label158;
      this.mWidthGap = this.mOriginalWidthGap;
      this.mHeightGap = this.mOriginalHeightGap;
    }
    setMeasuredDimension(i8, i9);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    int i = getPageChildCount();
    if (i > 0)
    {
      int j = getChildOnPageAt(i - 1).getBottom();
      if ((int)Math.ceil(getPageChildCount() / getCellCountX()) < getCellCountY()) {
        j += this.mCellHeight / 2;
      }
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
    this.mChildren.removeAllViews();
    setLayerType(0, null);
  }
  
  protected void setChildrenDrawingCacheEnabled(boolean paramBoolean)
  {
    this.mChildren.setChildrenDrawingCacheEnabled(paramBoolean);
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    @ViewDebug.ExportedProperty
    public int cellHSpan = 1;
    @ViewDebug.ExportedProperty
    public int cellVSpan = 1;
    @ViewDebug.ExportedProperty
    public int cellX;
    @ViewDebug.ExportedProperty
    public int cellY;
    @ViewDebug.ExportedProperty
    int x;
    @ViewDebug.ExportedProperty
    int y;
    
    public LayoutParams()
    {
      super(-1);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public void setup(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      int i = this.cellHSpan;
      int j = this.cellVSpan;
      int k = this.cellX;
      int m = this.cellY;
      this.width = (i * paramInt1 + paramInt3 * (i - 1) - this.leftMargin - this.rightMargin);
      this.height = (j * paramInt2 + paramInt4 * (j - 1) - this.topMargin - this.bottomMargin);
      if (LauncherAppState.getInstance().isScreenLarge())
      {
        this.x = (paramInt5 + k * (paramInt1 + paramInt3) + this.leftMargin);
        this.y = (paramInt6 + m * (paramInt2 + paramInt4) + this.topMargin);
        return;
      }
      this.x = (k * (paramInt1 + paramInt3) + this.leftMargin);
      this.y = (m * (paramInt2 + paramInt4) + this.topMargin);
    }
    
    public String toString()
    {
      return "(" + this.cellX + ", " + this.cellY + ", " + this.cellHSpan + ", " + this.cellVSpan + ")";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewCellLayout
 * JD-Core Version:    0.7.0.1
 */