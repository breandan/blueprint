package com.google.android.search.gel;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.OnKeyListener;
import com.google.android.search.shared.ui.RestrictedWidthLayout;

public class SearchOverlayLayout
  extends RestrictedWidthLayout
{
  private Point mLastTouch = new Point();
  private View.OnKeyListener mPreImeKeyListener;
  
  public SearchOverlayLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setChildrenDrawingOrderEnabled(true);
  }
  
  public boolean dispatchKeyEventPreIme(KeyEvent paramKeyEvent)
  {
    if ((this.mPreImeKeyListener != null) && (this.mPreImeKeyListener.onKey(this, paramKeyEvent.getKeyCode(), paramKeyEvent))) {
      return true;
    }
    return super.dispatchKeyEventPreIme(paramKeyEvent);
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    return -1 + (paramInt1 - paramInt2);
  }
  
  public Point getLastTouch()
  {
    return this.mLastTouch;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    this.mLastTouch.x = ((int)paramMotionEvent.getX());
    this.mLastTouch.y = ((int)paramMotionEvent.getY());
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public void setPreImeKeyListener(View.OnKeyListener paramOnKeyListener)
  {
    this.mPreImeKeyListener = paramOnKeyListener;
  }
  
  public void setReverseChildrenDrawingOrder(boolean paramBoolean)
  {
    setChildrenDrawingOrderEnabled(paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.SearchOverlayLayout
 * JD-Core Version:    0.7.0.1
 */