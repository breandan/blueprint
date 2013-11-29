package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;

public class Histogram
  extends View
{
  private int mBarPadding;
  private int mBarSize;
  private List<Bar> mBars = Lists.newLinkedList();
  
  public Histogram(Context paramContext)
  {
    super(paramContext);
  }
  
  public Histogram(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    parseAttrs(paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Histogram));
  }
  
  public Histogram(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    parseAttrs(paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Histogram, paramInt, 0));
  }
  
  private void parseAttrs(TypedArray paramTypedArray)
  {
    this.mBarSize = paramTypedArray.getDimensionPixelSize(0, 0);
    this.mBarPadding = paramTypedArray.getDimensionPixelSize(1, 0);
    paramTypedArray.recycle();
  }
  
  public void addBar(float paramFloat, int paramInt)
  {
    if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Paint localPaint = new Paint();
      localPaint.setColor(paramInt);
      this.mBars.add(new Bar(paramFloat, localPaint));
      return;
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (isEnabled())
    {
      int i = getWidth();
      for (int j = 0; j < this.mBars.size(); j++)
      {
        Bar localBar = (Bar)this.mBars.get(j);
        float f = j * (this.mBarSize + this.mBarPadding);
        paramCanvas.drawRect(0.0F, f, localBar.length * i, f + this.mBarSize, localBar.paint);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt2);
    int j = 0;
    if (i == 0)
    {
      j = this.mBars.size() * this.mBarSize;
      if (this.mBars.size() > 1) {
        j += (-1 + this.mBars.size()) * this.mBarPadding;
      }
    }
    int k = Math.max(j, getSuggestedMinimumHeight());
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), paramInt1), getDefaultSize(k, paramInt2));
  }
  
  private class Bar
  {
    public float length;
    public Paint paint;
    
    public Bar(float paramFloat, Paint paramPaint)
    {
      this.length = paramFloat;
      this.paint = paramPaint;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.Histogram
 * JD-Core Version:    0.7.0.1
 */