package com.google.android.velvet.gallery;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GalleryDetailBarLayout
  extends LinearLayout
{
  public GalleryDetailBarLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public GalleryDetailBarLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GalleryDetailBarLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected boolean fitSystemWindows(Rect paramRect)
  {
    setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + paramRect.bottom);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.gallery.GalleryDetailBarLayout
 * JD-Core Version:    0.7.0.1
 */