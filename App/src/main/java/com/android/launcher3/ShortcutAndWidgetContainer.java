package com.android.launcher3;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class ShortcutAndWidgetContainer
  extends ViewGroup
{
  private int mCellHeight;
  private int mCellWidth;
  private int mCountX;
  private int mCountY;
  private int mHeightGap;
  private boolean mInvertIfRtl = false;
  private boolean mIsHotseatLayout;
  private final int[] mTmpCellXY = new int[2];
  private final WallpaperManager mWallpaperManager;
  private int mWidthGap;
  
  public ShortcutAndWidgetContainer(Context paramContext)
  {
    super(paramContext);
    this.mWallpaperManager = WallpaperManager.getInstance(paramContext);
  }
  
  private boolean invertLayoutHorizontally()
  {
    return (this.mInvertIfRtl) && (isLayoutRtl());
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).cancelLongPress();
    }
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
  }
  
  int getCellContentHeight()
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = getMeasuredHeight();
    if (this.mIsHotseatLayout) {}
    for (int j = localDeviceProfile.hotseatCellHeightPx;; j = localDeviceProfile.cellHeightPx) {
      return Math.min(i, j);
    }
  }
  
  int getCellContentWidth()
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = getMeasuredHeight();
    if (this.mIsHotseatLayout) {}
    for (int j = localDeviceProfile.hotseatCellWidthPx;; j = localDeviceProfile.cellWidthPx) {
      return Math.min(i, j);
    }
  }
  
  public View getChildAt(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
      if ((localLayoutParams.cellX <= paramInt1) && (paramInt1 < localLayoutParams.cellX + localLayoutParams.cellHSpan) && (localLayoutParams.cellY <= paramInt2) && (paramInt2 < localLayoutParams.cellY + localLayoutParams.cellVSpan)) {
        return localView;
      }
    }
    return null;
  }
  
  public boolean isLayoutRtl()
  {
    return getLayoutDirection() == 1;
  }
  
  public void measureChild(View paramView)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = this.mCellWidth;
    int j = this.mCellHeight;
    CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)paramView.getLayoutParams();
    if (!localLayoutParams.isFullscreen)
    {
      localLayoutParams.setup(i, j, this.mWidthGap, this.mHeightGap, invertLayoutHorizontally(), this.mCountX);
      if (!(paramView instanceof LauncherAppWidgetHostView)) {}
    }
    for (;;)
    {
      paramView.measure(View.MeasureSpec.makeMeasureSpec(localLayoutParams.width, 1073741824), View.MeasureSpec.makeMeasureSpec(localLayoutParams.height, 1073741824));
      return;
      int k = getCellContentHeight();
      int m = (int)Math.max(0.0F, (localLayoutParams.height - k) / 2.0F);
      int n = (int)(localDeviceProfile.edgeMarginPx / 2.0F);
      paramView.setPadding(n, m, n, 0);
      continue;
      localLayoutParams.x = 0;
      localLayoutParams.y = 0;
      localLayoutParams.width = getMeasuredWidth();
      localLayoutParams.height = getMeasuredHeight();
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
        int k = localLayoutParams.x;
        int m = localLayoutParams.y;
        localView.layout(k, m, k + localLayoutParams.width, m + localLayoutParams.height);
        if (localLayoutParams.dropped)
        {
          localLayoutParams.dropped = false;
          int[] arrayOfInt = this.mTmpCellXY;
          getLocationOnScreen(arrayOfInt);
          this.mWallpaperManager.sendWallpaperCommand(getWindowToken(), "android.home.drop", k + arrayOfInt[0] + localLayoutParams.width / 2, m + arrayOfInt[1] + localLayoutParams.height / 2, 0, null);
        }
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8) {
        measureChild(localView);
      }
    }
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
  
  public void setCellDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this.mCellWidth = paramInt1;
    this.mCellHeight = paramInt2;
    this.mWidthGap = paramInt3;
    this.mHeightGap = paramInt4;
    this.mCountX = paramInt5;
    this.mCountY = paramInt6;
  }
  
  protected void setChildrenDrawingCacheEnabled(boolean paramBoolean)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      localView.setDrawingCacheEnabled(paramBoolean);
      if ((!localView.isHardwareAccelerated()) && (paramBoolean)) {
        localView.buildDrawingCache(true);
      }
    }
  }
  
  protected void setChildrenDrawnWithCacheEnabled(boolean paramBoolean)
  {
    super.setChildrenDrawnWithCacheEnabled(paramBoolean);
  }
  
  public void setInvertIfRtl(boolean paramBoolean)
  {
    this.mInvertIfRtl = paramBoolean;
  }
  
  public void setIsHotseat(boolean paramBoolean)
  {
    this.mIsHotseatLayout = paramBoolean;
  }
  
  public void setupLp(CellLayout.LayoutParams paramLayoutParams)
  {
    paramLayoutParams.setup(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, invertLayoutHorizontally(), this.mCountX);
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.ShortcutAndWidgetContainer
 * JD-Core Version:    0.7.0.1
 */