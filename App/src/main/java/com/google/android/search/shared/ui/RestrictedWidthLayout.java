package com.google.android.search.shared.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.base.Preconditions;

public class RestrictedWidthLayout
  extends LinearLayout
{
  public RestrictedWidthLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(RestrictedWidthLayout.class.getCanonicalName());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      int i = View.MeasureSpec.getSize(paramInt1);
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(LayoutUtils.getMaxContentWidth(getContext(), i, true), 1073741824), paramInt2);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.RestrictedWidthLayout
 * JD-Core Version:    0.7.0.1
 */