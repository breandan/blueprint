package com.android.datetimepicker.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SimpleMonthView
  extends MonthView
{
  public SimpleMonthView(Context paramContext)
  {
    super(paramContext);
  }
  
  public void drawMonthDay(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    if (this.mSelectedDay == paramInt3) {
      paramCanvas.drawCircle(paramInt4, paramInt5 - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, this.mSelectedCirclePaint);
    }
    if ((this.mHasToday) && (this.mToday == paramInt3)) {
      this.mMonthNumPaint.setColor(this.mTodayNumberColor);
    }
    for (;;)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt3);
      paramCanvas.drawText(String.format("%d", arrayOfObject), paramInt4, paramInt5, this.mMonthNumPaint);
      return;
      this.mMonthNumPaint.setColor(this.mDayTextColor);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.SimpleMonthView
 * JD-Core Version:    0.7.0.1
 */