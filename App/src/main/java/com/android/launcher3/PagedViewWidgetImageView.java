package com.android.launcher3;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

class PagedViewWidgetImageView
  extends ImageView
{
  public boolean mAllowRequestLayout = true;
  
  public PagedViewWidgetImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    paramCanvas.save();
    paramCanvas.clipRect(getScrollX() + getPaddingLeft(), getScrollY() + getPaddingTop(), getScrollX() + getRight() - getLeft() - getPaddingRight(), getScrollY() + getBottom() - getTop() - getPaddingBottom());
    super.onDraw(paramCanvas);
    paramCanvas.restore();
  }
  
  public void requestLayout()
  {
    if (this.mAllowRequestLayout) {
      super.requestLayout();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewWidgetImageView
 * JD-Core Version:    0.7.0.1
 */