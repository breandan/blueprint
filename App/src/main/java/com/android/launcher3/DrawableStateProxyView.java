package com.android.launcher3;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class DrawableStateProxyView
  extends LinearLayout
{
  private View mView;
  private int mViewId;
  
  public DrawableStateProxyView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DrawableStateProxyView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DrawableStateProxyView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DrawableStateProxyView, paramInt, 0);
    this.mViewId = localTypedArray.getResourceId(0, -1);
    localTypedArray.recycle();
    setFocusable(false);
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (this.mView == null) {
      this.mView = ((View)getParent()).findViewById(this.mViewId);
    }
    if (this.mView != null)
    {
      this.mView.setPressed(isPressed());
      this.mView.setHovered(isHovered());
    }
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DrawableStateProxyView
 * JD-Core Version:    0.7.0.1
 */