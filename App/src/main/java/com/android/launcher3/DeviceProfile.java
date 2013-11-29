package com.android.launcher3;

import android.appwidget.AppWidgetHostView;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

class DeviceProfile
{
  int allAppsCellHeightPx;
  int allAppsCellPaddingPx;
  int allAppsCellWidthPx;
  int allAppsIconSizePx;
  int allAppsIconTextSizePx;
  int allAppsNumCols;
  int allAppsNumRows;
  int availableHeightPx;
  int availableWidthPx;
  int cellHeightPx;
  int cellWidthPx;
  int defaultPageSpacingPx;
  Rect defaultWidgetPadding;
  int desiredWorkspaceLeftRightMarginPx;
  int edgeMarginPx;
  int folderBackgroundOffset;
  int folderCellHeightPx;
  int folderCellWidthPx;
  int folderIconSizePx;
  int heightPx;
  int hotseatAllAppsRank;
  int hotseatBarHeightPx;
  int hotseatCellHeightPx;
  int hotseatCellWidthPx;
  private float hotseatIconSize;
  int hotseatIconSizePx;
  private int iconDrawablePaddingOriginalPx;
  int iconDrawablePaddingPx;
  private float iconSize;
  int iconSizePx;
  private float iconTextSize;
  int iconTextSizePx;
  boolean isLandscape;
  boolean isLargeTablet;
  boolean isTablet;
  private ArrayList<DeviceProfileCallbacks> mCallbacks = new ArrayList();
  float minHeightDps;
  float minWidthDps;
  String name;
  float numColumns;
  float numHotseatIcons;
  float numRows;
  int pageIndicatorHeightPx;
  int searchBarHeightPx;
  int searchBarSpaceHeightPx;
  int searchBarSpaceMaxWidthPx;
  int searchBarSpaceWidthPx;
  boolean transposeLayoutWithOrientation;
  int widthPx;
  
  DeviceProfile(Context paramContext, ArrayList<DeviceProfile> paramArrayList, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Resources paramResources)
  {
    DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
    ArrayList localArrayList = new ArrayList();
    this.transposeLayoutWithOrientation = paramResources.getBoolean(2131755015);
    this.minWidthDps = paramFloat1;
    this.minHeightDps = paramFloat2;
    this.defaultWidgetPadding = AppWidgetHostView.getDefaultPaddingForWidget(paramContext, new ComponentName(paramContext.getPackageName(), getClass().getName()), null);
    this.edgeMarginPx = paramResources.getDimensionPixelSize(2131689505);
    this.desiredWorkspaceLeftRightMarginPx = (2 * this.edgeMarginPx);
    this.pageIndicatorHeightPx = paramResources.getDimensionPixelSize(2131689508);
    this.defaultPageSpacingPx = paramResources.getDimensionPixelSize(2131689511);
    this.allAppsCellPaddingPx = paramResources.getDimensionPixelSize(2131689510);
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
    {
      DeviceProfile localDeviceProfile6 = (DeviceProfile)localIterator1.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile6.minWidthDps, localDeviceProfile6.minHeightDps, localDeviceProfile6.numRows));
    }
    this.numRows = Math.round(invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList));
    localArrayList.clear();
    Iterator localIterator2 = paramArrayList.iterator();
    while (localIterator2.hasNext())
    {
      DeviceProfile localDeviceProfile5 = (DeviceProfile)localIterator2.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile5.minWidthDps, localDeviceProfile5.minHeightDps, localDeviceProfile5.numColumns));
    }
    this.numColumns = Math.round(invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList));
    localArrayList.clear();
    Iterator localIterator3 = paramArrayList.iterator();
    while (localIterator3.hasNext())
    {
      DeviceProfile localDeviceProfile4 = (DeviceProfile)localIterator3.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile4.minWidthDps, localDeviceProfile4.minHeightDps, localDeviceProfile4.numHotseatIcons));
    }
    this.numHotseatIcons = Math.round(invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList));
    this.hotseatAllAppsRank = ((int)(this.numHotseatIcons / 2.0F));
    localArrayList.clear();
    Iterator localIterator4 = paramArrayList.iterator();
    while (localIterator4.hasNext())
    {
      DeviceProfile localDeviceProfile3 = (DeviceProfile)localIterator4.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile3.minWidthDps, localDeviceProfile3.minHeightDps, localDeviceProfile3.iconSize));
    }
    this.iconSize = invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList);
    this.allAppsIconSizePx = DynamicGrid.pxFromDp(this.iconSize, localDisplayMetrics);
    localArrayList.clear();
    Iterator localIterator5 = paramArrayList.iterator();
    while (localIterator5.hasNext())
    {
      DeviceProfile localDeviceProfile2 = (DeviceProfile)localIterator5.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile2.minWidthDps, localDeviceProfile2.minHeightDps, localDeviceProfile2.iconTextSize));
    }
    this.iconTextSize = invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList);
    this.iconDrawablePaddingOriginalPx = paramResources.getDimensionPixelSize(2131689509);
    this.allAppsIconTextSizePx = DynamicGrid.pxFromDp(this.iconTextSize, localDisplayMetrics);
    localArrayList.clear();
    Iterator localIterator6 = paramArrayList.iterator();
    while (localIterator6.hasNext())
    {
      DeviceProfile localDeviceProfile1 = (DeviceProfile)localIterator6.next();
      localArrayList.add(new DeviceProfileQuery(localDeviceProfile1.minWidthDps, localDeviceProfile1.minHeightDps, localDeviceProfile1.hotseatIconSize));
    }
    this.hotseatIconSize = invDistWeightedInterpolate(paramFloat1, paramFloat2, localArrayList);
    updateFromConfiguration(paramContext, paramResources, paramInt1, paramInt2, paramInt3, paramInt4);
    updateAvailableDimensions(paramContext);
  }
  
  DeviceProfile(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if ((!AppsCustomizePagedView.DISABLE_ALL_APPS) && (paramFloat7 % 2.0F == 0.0F)) {
      throw new RuntimeException("All Device Profiles must have an odd number of hotseat spaces");
    }
    this.name = paramString;
    this.minWidthDps = paramFloat1;
    this.minHeightDps = paramFloat2;
    this.numRows = paramFloat3;
    this.numColumns = paramFloat4;
    this.iconSize = paramFloat5;
    this.iconTextSize = paramFloat6;
    this.numHotseatIcons = paramFloat7;
    this.hotseatIconSize = paramFloat8;
  }
  
  private float dist(PointF paramPointF1, PointF paramPointF2)
  {
    return (float)Math.sqrt((paramPointF2.x - paramPointF1.x) * (paramPointF2.x - paramPointF1.x) + (paramPointF2.y - paramPointF1.y) * (paramPointF2.y - paramPointF1.y));
  }
  
  private float invDistWeightedInterpolate(float paramFloat1, float paramFloat2, ArrayList<DeviceProfileQuery> paramArrayList)
  {
    float f1 = 0.0F;
    float f2 = 0.0F;
    final PointF localPointF = new PointF(paramFloat1, paramFloat2);
    Collections.sort(paramArrayList, new Comparator()
    {
      public int compare(DeviceProfileQuery paramAnonymousDeviceProfileQuery1, DeviceProfileQuery paramAnonymousDeviceProfileQuery2)
      {
        return (int)(DeviceProfile.this.dist(localPointF, paramAnonymousDeviceProfileQuery1.dimens) - DeviceProfile.this.dist(localPointF, paramAnonymousDeviceProfileQuery2.dimens));
      }
    });
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      DeviceProfileQuery localDeviceProfileQuery2 = (DeviceProfileQuery)paramArrayList.get(i);
      if (i < 3.0F)
      {
        float f3 = weight(localPointF, localDeviceProfileQuery2.dimens, 5.0F);
        if (f3 == (1.0F / 1.0F)) {
          return localDeviceProfileQuery2.value;
        }
        f2 += f3;
      }
    }
    for (int j = 0; j < paramArrayList.size(); j++)
    {
      DeviceProfileQuery localDeviceProfileQuery1 = (DeviceProfileQuery)paramArrayList.get(j);
      if (j < 3.0F) {
        f1 += weight(localPointF, localDeviceProfileQuery1.dimens, 5.0F) * localDeviceProfileQuery1.value / f2;
      }
    }
    return f1;
  }
  
  private void updateAvailableDimensions(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    Resources localResources = paramContext.getResources();
    DisplayMetrics localDisplayMetrics = localResources.getDisplayMetrics();
    Configuration localConfiguration = localResources.getConfiguration();
    Point localPoint1 = new Point();
    Point localPoint2 = new Point();
    Point localPoint3 = new Point();
    localDisplay.getSize(localPoint1);
    localDisplay.getCurrentSizeRange(localPoint2, localPoint3);
    this.availableWidthPx = localPoint1.x;
    if (localConfiguration.orientation == 2) {}
    for (this.availableHeightPx = localPoint2.y;; this.availableHeightPx = localPoint3.y)
    {
      Rect localRect = getWorkspacePadding();
      float f1 = 1.0F;
      int i = this.iconDrawablePaddingOriginalPx;
      updateIconSize(1.0F, i, localResources, localDisplayMetrics);
      float f2 = this.cellHeightPx * this.numRows;
      int j = this.availableHeightPx - localRect.top - localRect.bottom;
      if (f2 > j)
      {
        f1 = j / f2;
        i = 0;
      }
      updateIconSize(f1, i, localResources, localDisplayMetrics);
      Iterator localIterator = this.mCallbacks.iterator();
      while (localIterator.hasNext()) {
        ((DeviceProfileCallbacks)localIterator.next()).onAvailableSizeChanged(this);
      }
    }
  }
  
  private void updateIconSize(float paramFloat, int paramInt, Resources paramResources, DisplayMetrics paramDisplayMetrics)
  {
    this.iconSizePx = ((int)(paramFloat * DynamicGrid.pxFromDp(this.iconSize, paramDisplayMetrics)));
    this.iconTextSizePx = ((int)(paramFloat * DynamicGrid.pxFromSp(this.iconTextSize, paramDisplayMetrics)));
    this.iconDrawablePaddingPx = paramInt;
    this.hotseatIconSizePx = ((int)(paramFloat * DynamicGrid.pxFromDp(this.hotseatIconSize, paramDisplayMetrics)));
    this.searchBarSpaceMaxWidthPx = paramResources.getDimensionPixelSize(2131689506);
    this.searchBarHeightPx = paramResources.getDimensionPixelSize(2131689507);
    this.searchBarSpaceWidthPx = Math.min(this.searchBarSpaceMaxWidthPx, this.widthPx);
    this.searchBarSpaceHeightPx = (this.searchBarHeightPx + 2 * this.edgeMarginPx);
    Paint localPaint = new Paint();
    localPaint.setTextSize(this.iconTextSizePx);
    Paint.FontMetrics localFontMetrics = localPaint.getFontMetrics();
    this.cellWidthPx = this.iconSizePx;
    this.cellHeightPx = (this.iconSizePx + this.iconDrawablePaddingPx + (int)Math.ceil(localFontMetrics.bottom - localFontMetrics.top));
    this.hotseatBarHeightPx = (this.iconSizePx + 4 * this.edgeMarginPx);
    this.hotseatCellWidthPx = this.iconSizePx;
    this.hotseatCellHeightPx = this.iconSizePx;
    this.folderCellWidthPx = (this.cellWidthPx + 3 * this.edgeMarginPx);
    this.folderCellHeightPx = (this.cellHeightPx + this.edgeMarginPx);
    this.folderBackgroundOffset = (-this.edgeMarginPx);
    this.folderIconSizePx = (this.iconSizePx + 2 * -this.folderBackgroundOffset);
    int i;
    int j;
    int k;
    int m;
    int n;
    if (this.isLandscape)
    {
      i = 0;
      getWorkspacePadding(i);
      paramResources.getDimensionPixelSize(2131689538);
      this.allAppsCellWidthPx = this.allAppsIconSizePx;
      this.allAppsCellHeightPx = (paramInt + this.allAppsIconSizePx + this.iconTextSizePx);
      j = paramResources.getInteger(2131427329);
      k = paramResources.getInteger(2131427330);
      m = paramResources.getInteger(2131427331);
      if (!this.isLandscape) {
        break label437;
      }
      n = k;
      label342:
      if (!this.isLandscape) {
        break label444;
      }
    }
    label437:
    label444:
    for (int i1 = j;; i1 = k)
    {
      this.allAppsNumRows = ((this.availableHeightPx - this.pageIndicatorHeightPx) / (this.allAppsCellHeightPx + this.allAppsCellPaddingPx));
      this.allAppsNumRows = Math.max(m, Math.min(n, this.allAppsNumRows));
      this.allAppsNumCols = (this.availableWidthPx / (this.allAppsCellWidthPx + this.allAppsCellPaddingPx));
      this.allAppsNumCols = Math.max(m, Math.min(i1, this.allAppsNumCols));
      return;
      i = 1;
      break;
      n = j;
      break label342;
    }
  }
  
  private float weight(PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    float f = dist(paramPointF1, paramPointF2);
    if (f == 0.0F) {
      return (1.0F / 1.0F);
    }
    return (float)(1.0D / Math.pow(f, paramFloat));
  }
  
  void addCallback(DeviceProfileCallbacks paramDeviceProfileCallbacks)
  {
    this.mCallbacks.add(paramDeviceProfileCallbacks);
    paramDeviceProfileCallbacks.onAvailableSizeChanged(this);
  }
  
  int calculateCellHeight(int paramInt1, int paramInt2)
  {
    return paramInt1 / paramInt2;
  }
  
  int calculateCellWidth(int paramInt1, int paramInt2)
  {
    return paramInt1 / paramInt2;
  }
  
  Rect getHotseatRect()
  {
    if (isVerticalBarLayout()) {
      return new Rect(this.availableWidthPx - this.hotseatBarHeightPx, 0, 2147483647, this.availableHeightPx);
    }
    return new Rect(0, this.availableHeightPx - this.hotseatBarHeightPx, this.availableWidthPx, 2147483647);
  }
  
  Rect getWorkspacePadding()
  {
    if (this.isLandscape) {}
    for (int i = 0;; i = 1) {
      return getWorkspacePadding(i);
    }
  }
  
  Rect getWorkspacePadding(int paramInt)
  {
    Rect localRect = new Rect();
    if ((paramInt == 0) && (this.transposeLayoutWithOrientation))
    {
      localRect.set(this.searchBarSpaceHeightPx, this.edgeMarginPx, this.hotseatBarHeightPx, this.edgeMarginPx);
      return localRect;
    }
    if (isTablet())
    {
      if (paramInt == 0) {}
      for (int i = Math.max(this.widthPx, this.heightPx);; i = Math.min(this.widthPx, this.heightPx))
      {
        int j = (int)((i - 2 * this.edgeMarginPx - this.numColumns * this.cellWidthPx) / (2.0F * (1.0F + this.numColumns)));
        localRect.set(j + this.edgeMarginPx, this.searchBarSpaceHeightPx, j + this.edgeMarginPx, this.hotseatBarHeightPx + this.pageIndicatorHeightPx);
        return localRect;
      }
    }
    localRect.set(this.desiredWorkspaceLeftRightMarginPx - this.defaultWidgetPadding.left, this.searchBarSpaceHeightPx, this.desiredWorkspaceLeftRightMarginPx - this.defaultWidgetPadding.right, this.hotseatBarHeightPx + this.pageIndicatorHeightPx);
    return localRect;
  }
  
  int getWorkspacePageSpacing(int paramInt)
  {
    if ((paramInt == 0) && (this.transposeLayoutWithOrientation)) {
      return this.defaultPageSpacingPx;
    }
    return 2 * getWorkspacePadding().left;
  }
  
  boolean isLargeTablet()
  {
    return this.isLargeTablet;
  }
  
  boolean isPhone()
  {
    return (!this.isTablet) && (!this.isLargeTablet);
  }
  
  boolean isTablet()
  {
    return this.isTablet;
  }
  
  boolean isVerticalBarLayout()
  {
    return (this.isLandscape) && (this.transposeLayoutWithOrientation);
  }
  
  public void layout(Launcher paramLauncher)
  {
    paramLauncher.getResources();
    boolean bool = isVerticalBarLayout();
    SearchDropTargetBar localSearchDropTargetBar = paramLauncher.getSearchBar();
    FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)localSearchDropTargetBar.getLayoutParams();
    View localView2;
    label128:
    int i;
    label167:
    View localView3;
    FrameLayout.LayoutParams localLayoutParams3;
    label287:
    View localView4;
    if (bool)
    {
      localLayoutParams1.gravity = 51;
      localLayoutParams1.width = this.searchBarSpaceHeightPx;
      localLayoutParams1.height = -1;
      localSearchDropTargetBar.setPadding(0, 2 * this.edgeMarginPx, 0, 2 * this.edgeMarginPx);
      localSearchDropTargetBar.setLayoutParams(localLayoutParams1);
      View localView1 = paramLauncher.getQsbBar();
      ViewGroup.LayoutParams localLayoutParams = localView1.getLayoutParams();
      localLayoutParams.width = -1;
      localLayoutParams.height = -1;
      localView1.setLayoutParams(localLayoutParams);
      localView2 = paramLauncher.findViewById(2131296756);
      if ((localView2 != null) && (!bool)) {
        break label657;
      }
      PagedView localPagedView = (PagedView)paramLauncher.findViewById(2131296746);
      FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)localPagedView.getLayoutParams();
      localLayoutParams2.gravity = 17;
      if (!this.isLandscape) {
        break label710;
      }
      i = 0;
      Rect localRect1 = getWorkspacePadding(i);
      localPagedView.setLayoutParams(localLayoutParams2);
      localPagedView.setPadding(localRect1.left, localRect1.top, localRect1.right, localRect1.bottom);
      localPagedView.setPageSpacing(getWorkspacePageSpacing(i));
      localView3 = paramLauncher.findViewById(2131296747);
      localLayoutParams3 = (FrameLayout.LayoutParams)localView3.getLayoutParams();
      if (!bool) {
        break label716;
      }
      localLayoutParams3.gravity = 5;
      localLayoutParams3.width = this.hotseatBarHeightPx;
      localLayoutParams3.height = -1;
      localView3.findViewById(2131296698).setPadding(0, 2 * this.edgeMarginPx, 0, 2 * this.edgeMarginPx);
      localView3.setLayoutParams(localLayoutParams3);
      localView4 = paramLauncher.findViewById(2131296755);
      if (localView4 != null)
      {
        if (!bool) {
          break label917;
        }
        localView4.setVisibility(8);
      }
    }
    for (;;)
    {
      AppsCustomizeTabHost localAppsCustomizeTabHost = (AppsCustomizeTabHost)paramLauncher.findViewById(2131296754);
      if (localAppsCustomizeTabHost != null)
      {
        int j = (int)(this.pageIndicatorHeightPx * Math.min(1.0F, this.allAppsIconSizePx / DynamicGrid.DEFAULT_ICON_SIZE_PX));
        View localView5 = localAppsCustomizeTabHost.findViewById(2131296343);
        if (localView5 != null)
        {
          FrameLayout.LayoutParams localLayoutParams4 = (FrameLayout.LayoutParams)localView5.getLayoutParams();
          localLayoutParams4.gravity = 81;
          localLayoutParams4.width = -2;
          localLayoutParams4.height = j;
          localView5.setLayoutParams(localLayoutParams4);
        }
        AppsCustomizePagedView localAppsCustomizePagedView = (AppsCustomizePagedView)localAppsCustomizeTabHost.findViewById(2131296342);
        Rect localRect2 = new Rect();
        if (localAppsCustomizePagedView != null)
        {
          int k = (this.availableWidthPx - this.allAppsCellWidthPx * this.allAppsNumCols) / (2 * (1 + this.allAppsNumCols));
          int m = (this.availableHeightPx - this.allAppsCellHeightPx * this.allAppsNumRows) / (2 * (1 + this.allAppsNumRows));
          int n = Math.min(k, (int)(0.75F * (k + m)));
          int i1 = Math.min(m, (int)(0.75F * (n + m)));
          int i2 = this.allAppsNumCols * (this.allAppsCellWidthPx + n * 2);
          int i3 = (this.availableWidthPx - i2) / 2;
          if (i3 > this.allAppsCellWidthPx / 4)
          {
            localRect2.right = i3;
            localRect2.left = i3;
          }
          localRect2.bottom = Math.max(0, j - i1);
          localAppsCustomizePagedView.setAllAppsPadding(localRect2);
          localAppsCustomizePagedView.setWidgetsPageIndicatorPadding(j);
        }
      }
      return;
      localLayoutParams1.gravity = 49;
      localLayoutParams1.width = this.searchBarSpaceWidthPx;
      localLayoutParams1.height = this.searchBarSpaceHeightPx;
      localSearchDropTargetBar.setPadding(2 * this.edgeMarginPx, 2 * this.edgeMarginPx, 2 * this.edgeMarginPx, 0);
      break;
      label657:
      FrameLayout.LayoutParams localLayoutParams6 = (FrameLayout.LayoutParams)localView2.getLayoutParams();
      localLayoutParams6.gravity = 8388661;
      localLayoutParams6.width = ((this.widthPx - this.searchBarSpaceWidthPx) / 2 + 2 * this.iconSizePx);
      localLayoutParams6.height = this.searchBarSpaceHeightPx;
      break label128;
      label710:
      i = 1;
      break label167;
      label716:
      if (isTablet())
      {
        int i4 = (int)((this.widthPx - 2 * this.edgeMarginPx - this.numColumns * this.cellWidthPx) / (2.0F * (1.0F + this.numColumns)));
        int i5 = (int)Math.max(0.0F, ((int)(this.numColumns * this.cellWidthPx + (this.numColumns - 1.0F) * i4) - this.numHotseatIcons * this.hotseatCellWidthPx) / (this.numHotseatIcons - 1.0F));
        localLayoutParams3.gravity = 80;
        localLayoutParams3.width = -1;
        localLayoutParams3.height = this.hotseatBarHeightPx;
        localView3.setPadding(i5 + (i4 + 2 * this.edgeMarginPx), 0, i5 + (i4 + 2 * this.edgeMarginPx), 2 * this.edgeMarginPx);
        break label287;
      }
      localLayoutParams3.gravity = 80;
      localLayoutParams3.width = -1;
      localLayoutParams3.height = this.hotseatBarHeightPx;
      localView3.findViewById(2131296698).setPadding(2 * this.edgeMarginPx, 0, 2 * this.edgeMarginPx, 0);
      break label287;
      label917:
      FrameLayout.LayoutParams localLayoutParams5 = (FrameLayout.LayoutParams)localView4.getLayoutParams();
      localLayoutParams5.gravity = 81;
      localLayoutParams5.width = -2;
      localLayoutParams5.height = -2;
      localLayoutParams5.bottomMargin = this.hotseatBarHeightPx;
      localView4.setLayoutParams(localLayoutParams5);
    }
  }
  
  boolean shouldFadeAdjacentWorkspaceScreens()
  {
    return (isVerticalBarLayout()) || (isLargeTablet());
  }
  
  void updateFromConfiguration(Context paramContext, Resources paramResources, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramResources.getConfiguration().orientation == 2) {}
    for (boolean bool = true;; bool = false)
    {
      this.isLandscape = bool;
      this.isTablet = paramResources.getBoolean(2131755009);
      this.isLargeTablet = paramResources.getBoolean(2131755010);
      this.widthPx = paramInt1;
      this.heightPx = paramInt2;
      this.availableWidthPx = paramInt3;
      this.availableHeightPx = paramInt4;
      updateAvailableDimensions(paramContext);
      return;
    }
  }
  
  public static abstract interface DeviceProfileCallbacks
  {
    public abstract void onAvailableSizeChanged(DeviceProfile paramDeviceProfile);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DeviceProfile
 * JD-Core Version:    0.7.0.1
 */