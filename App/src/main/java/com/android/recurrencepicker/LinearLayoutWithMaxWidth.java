package com.android.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class LinearLayoutWithMaxWidth
  extends LinearLayout
{
  public LinearLayoutWithMaxWidth(Context paramContext)
  {
    super(paramContext);
  }
  
  public LinearLayoutWithMaxWidth(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public LinearLayoutWithMaxWidth(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    WeekButton.setSuggestedWidth(View.MeasureSpec.getSize(paramInt1) / 7);
    super.onMeasure(paramInt1, paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.recurrencepicker.LinearLayoutWithMaxWidth
 * JD-Core Version:    0.7.0.1
 */