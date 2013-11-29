package com.android.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ToggleButton;

public class WeekButton
  extends ToggleButton
{
  private static int mWidth;
  
  public WeekButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public WeekButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public WeekButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public static void setSuggestedWidth(int paramInt)
  {
    mWidth = paramInt;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = getMeasuredHeight();
    int j = getMeasuredWidth();
    if ((i > 0) && (j > 0))
    {
      if (j >= i) {
        break label55;
      }
      if (View.MeasureSpec.getMode(getMeasuredHeightAndState()) != 1073741824) {
        i = j;
      }
    }
    for (;;)
    {
      setMeasuredDimension(j, i);
      return;
      label55:
      if ((i < j) && (View.MeasureSpec.getMode(getMeasuredWidthAndState()) != 1073741824)) {
        j = i;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.recurrencepicker.WeekButton
 * JD-Core Version:    0.7.0.1
 */