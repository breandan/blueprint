package com.google.android.velvet.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.google.common.base.Preconditions;

public class SquareFrameLayout
  extends FrameLayout
{
  static
  {
    if (!SquareFrameLayout.class.desiredAssertionStatus()) {}
    for (boolean bool = true;; bool = false)
    {
      $assertionsDisabled = bool;
      return;
    }
  }
  
  public SquareFrameLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public SquareFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SquareFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (getLayoutParams().height == -2) {}
    int i;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      i = View.MeasureSpec.getMode(paramInt1);
      if (i != 0) {
        break;
      }
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    assert (i == 1073741824);
    super.onMeasure(paramInt1, paramInt1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.SquareFrameLayout
 * JD-Core Version:    0.7.0.1
 */