package com.android.launcher3;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class WallpaperRootView
  extends RelativeLayout
{
  private final WallpaperPickerActivity a;
  
  public WallpaperRootView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.a = ((WallpaperPickerActivity)paramContext);
  }
  
  public WallpaperRootView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.a = ((WallpaperPickerActivity)paramContext);
  }
  
  protected boolean fitSystemWindows(Rect paramRect)
  {
    this.a.setWallpaperStripYOffset(paramRect.bottom);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.WallpaperRootView
 * JD-Core Version:    0.7.0.1
 */