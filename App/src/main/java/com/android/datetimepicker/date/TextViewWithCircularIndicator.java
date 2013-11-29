package com.android.datetimepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.dimen;
import com.android.datetimepicker.R.string;

public class TextViewWithCircularIndicator
  extends TextView
{
  private final int mCircleColor;
  Paint mCirclePaint = new Paint();
  private boolean mDrawCircle;
  private final String mItemIsSelectedText;
  private final int mRadius;
  
  public TextViewWithCircularIndicator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = paramContext.getResources();
    this.mCircleColor = localResources.getColor(R.color.blue);
    this.mRadius = localResources.getDimensionPixelOffset(R.dimen.month_select_circle_radius);
    this.mItemIsSelectedText = paramContext.getResources().getString(R.string.item_is_selected);
    init();
  }
  
  private void init()
  {
    this.mCirclePaint.setFakeBoldText(true);
    this.mCirclePaint.setAntiAlias(true);
    this.mCirclePaint.setColor(this.mCircleColor);
    this.mCirclePaint.setTextAlign(Paint.Align.CENTER);
    this.mCirclePaint.setStyle(Paint.Style.FILL);
    this.mCirclePaint.setAlpha(60);
  }
  
  public void drawIndicator(boolean paramBoolean)
  {
    this.mDrawCircle = paramBoolean;
  }
  
  public CharSequence getContentDescription()
  {
    Object localObject = getText();
    if (this.mDrawCircle) {
      localObject = String.format(this.mItemIsSelectedText, new Object[] { localObject });
    }
    return localObject;
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mDrawCircle)
    {
      int i = getWidth();
      int j = getHeight();
      int k = Math.min(i, j) / 2;
      paramCanvas.drawCircle(i / 2, j / 2, k, this.mCirclePaint);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.TextViewWithCircularIndicator
 * JD-Core Version:    0.7.0.1
 */