package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.util.ArrayList;

public class DynamicGrid
{
  static float DEFAULT_ICON_SIZE_DP = 60.0F;
  static float DEFAULT_ICON_SIZE_PX = 0.0F;
  private float mMinHeight;
  private float mMinWidth;
  private DeviceProfile mProfile;
  
  public DynamicGrid(Context paramContext, Resources paramResources, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    label48:
    int k;
    label87:
    int m;
    label127:
    int n;
    label167:
    int i1;
    label207:
    float f;
    if (!AppsCustomizePagedView.DISABLE_ALL_APPS)
    {
      i = 1;
      DEFAULT_ICON_SIZE_PX = pxFromDp(DEFAULT_ICON_SIZE_DP, localDisplayMetrics);
      if (i == 0) {
        break label436;
      }
      j = 5;
      localArrayList.add(new DeviceProfile("Super Short Stubby", 255.0F, 300.0F, 2.0F, 3.0F, 48.0F, 13.0F, j, 48.0F));
      if (i == 0) {
        break label442;
      }
      k = 5;
      localArrayList.add(new DeviceProfile("Shorter Stubby", 255.0F, 400.0F, 3.0F, 3.0F, 48.0F, 13.0F, k, 48.0F));
      if (i == 0) {
        break label448;
      }
      m = 5;
      localArrayList.add(new DeviceProfile("Short Stubby", 275.0F, 420.0F, 3.0F, 4.0F, 48.0F, 13.0F, m, 48.0F));
      if (i == 0) {
        break label454;
      }
      n = 5;
      localArrayList.add(new DeviceProfile("Stubby", 255.0F, 450.0F, 3.0F, 4.0F, 48.0F, 13.0F, n, 48.0F));
      if (i == 0) {
        break label460;
      }
      i1 = 5;
      localArrayList.add(new DeviceProfile("Nexus S", 296.0F, 491.32999F, 4.0F, 4.0F, 48.0F, 13.0F, i1, 48.0F));
      f = DEFAULT_ICON_SIZE_DP;
      if (i == 0) {
        break label466;
      }
    }
    label436:
    label442:
    label448:
    label454:
    label460:
    label466:
    for (int i2 = 5;; i2 = 4)
    {
      localArrayList.add(new DeviceProfile("Nexus 4", 359.0F, 518.0F, 4.0F, 4.0F, f, 13.0F, i2, 56.0F));
      localArrayList.add(new DeviceProfile("Nexus 7", 575.0F, 904.0F, 6.0F, 6.0F, 72.0F, 14.4F, 7.0F, 60.0F));
      localArrayList.add(new DeviceProfile("Nexus 10", 727.0F, 1207.0F, 5.0F, 8.0F, 80.0F, 14.4F, 9.0F, 64.0F));
      localArrayList.add(new DeviceProfile("20-inch Tablet", 1527.0F, 2527.0F, 7.0F, 7.0F, 100.0F, 20.0F, 7.0F, 72.0F));
      this.mMinWidth = dpiFromPx(paramInt1, localDisplayMetrics);
      this.mMinHeight = dpiFromPx(paramInt2, localDisplayMetrics);
      this.mProfile = new DeviceProfile(paramContext, localArrayList, this.mMinWidth, this.mMinHeight, paramInt3, paramInt4, paramInt5, paramInt6, paramResources);
      return;
      i = 0;
      break;
      j = 4;
      break label48;
      k = 4;
      break label87;
      m = 4;
      break label127;
      n = 4;
      break label167;
      i1 = 4;
      break label207;
    }
  }
  
  public static float dpiFromPx(int paramInt, DisplayMetrics paramDisplayMetrics)
  {
    float f = paramDisplayMetrics.densityDpi / 160.0F;
    return paramInt / f;
  }
  
  public static int pxFromDp(float paramFloat, DisplayMetrics paramDisplayMetrics)
  {
    return Math.round(TypedValue.applyDimension(1, paramFloat, paramDisplayMetrics));
  }
  
  public static int pxFromSp(float paramFloat, DisplayMetrics paramDisplayMetrics)
  {
    return Math.round(TypedValue.applyDimension(2, paramFloat, paramDisplayMetrics));
  }
  
  DeviceProfile getDeviceProfile()
  {
    return this.mProfile;
  }
  
  public String toString()
  {
    return "-------- DYNAMIC GRID ------- \nWd: " + this.mProfile.minWidthDps + ", Hd: " + this.mProfile.minHeightDps + ", W: " + this.mProfile.widthPx + ", H: " + this.mProfile.heightPx + " [r: " + this.mProfile.numRows + ", c: " + this.mProfile.numColumns + ", is: " + this.mProfile.iconSizePx + ", its: " + this.mProfile.iconTextSizePx + ", cw: " + this.mProfile.cellWidthPx + ", ch: " + this.mProfile.cellHeightPx + ", hc: " + this.mProfile.numHotseatIcons + ", his: " + this.mProfile.hotseatIconSizePx + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DynamicGrid
 * JD-Core Version:    0.7.0.1
 */