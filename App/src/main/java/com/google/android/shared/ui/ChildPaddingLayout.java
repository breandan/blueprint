package com.google.android.shared.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.base.Preconditions;

public class ChildPaddingLayout
  extends FrameLayout
{
  private boolean mMatchPortraitMode;
  
  public ChildPaddingLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ChildPaddingLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ChildPaddingLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(ChildPaddingLayout.class.getCanonicalName());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool;
    int i;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824)
    {
      bool = true;
      Preconditions.checkState(bool);
      if (getChildCount() > 0)
      {
        i = View.MeasureSpec.getSize(paramInt1);
        if (!this.mMatchPortraitMode) {
          break label84;
        }
      }
    }
    label84:
    for (int j = LayoutUtils.getContentPaddingToMatchPortrait(getContext(), i);; j = LayoutUtils.getContentPadding(getContext(), i))
    {
      View localView = getChildAt(0);
      localView.setPadding(j, localView.getPaddingTop(), j, localView.getPaddingBottom());
      super.onMeasure(paramInt1, paramInt2);
      return;
      bool = false;
      break;
    }
  }
  
  public void setMatchPortraitMode(boolean paramBoolean)
  {
    this.mMatchPortraitMode = paramBoolean;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.ChildPaddingLayout
 * JD-Core Version:    0.7.0.1
 */