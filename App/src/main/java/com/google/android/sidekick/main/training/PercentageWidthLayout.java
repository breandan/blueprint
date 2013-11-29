package com.google.android.sidekick.main.training;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.common.base.Preconditions;

public class PercentageWidthLayout
  extends LinearLayout
  implements Runnable
{
  private int mChildWidth;
  private final float mChildWidthPercent;
  private final int mMaxChildWidth;
  private final int mPercentWidthThreshold;
  
  public PercentageWidthLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PercentageWidthLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PercentageWidthLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PercentageWidthLayout, paramInt, 0);
    this.mPercentWidthThreshold = localTypedArray.getDimensionPixelSize(0, 0);
    this.mChildWidthPercent = localTypedArray.getFloat(i, 1.0F);
    if ((this.mChildWidthPercent >= 0.0F) && (this.mChildWidthPercent <= 1.0F)) {}
    for (;;)
    {
      Preconditions.checkArgument(i, "childWidthPercent must be between 0 and 1");
      this.mMaxChildWidth = localTypedArray.getDimensionPixelSize(2, 2147483647);
      localTypedArray.recycle();
      return;
      int j = 0;
    }
  }
  
  private int getChildWidth(int paramInt)
  {
    int i;
    if (paramInt < this.mPercentWidthThreshold) {
      i = -1;
    }
    do
    {
      return i;
      i = (int)(paramInt * this.mChildWidthPercent);
    } while (i <= this.mMaxChildWidth);
    return this.mMaxChildWidth;
  }
  
  protected LinearLayout.LayoutParams generateDefaultLayoutParams()
  {
    if (this.mChildWidth > 0) {}
    for (int i = this.mChildWidth;; i = -1) {
      return new LinearLayout.LayoutParams(i, -2);
    }
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getChildWidth(paramInt1);
    if (i != this.mChildWidth)
    {
      this.mChildWidth = i;
      int j = getChildCount();
      for (int k = 0; k < j; k++) {
        getChildAt(k).getLayoutParams().width = this.mChildWidth;
      }
      post(this);
    }
  }
  
  public void run()
  {
    requestLayout();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.training.PercentageWidthLayout
 * JD-Core Version:    0.7.0.1
 */