package com.google.android.velvet.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TextScalingWebview
  extends WebView
{
  public TextScalingWebview(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public TextScalingWebview(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TextScalingWebview(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    setTextZoom();
  }
  
  protected void setTextZoom()
  {
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    int i = Math.round(100.0F * localDisplayMetrics.scaledDensity / localDisplayMetrics.density);
    getSettings().setTextZoom(i);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.widget.TextScalingWebview
 * JD-Core Version:    0.7.0.1
 */