package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

public class AccessibleTabView
  extends TextView
{
  public AccessibleTabView(Context paramContext)
  {
    super(paramContext);
  }
  
  public AccessibleTabView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public AccessibleTabView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return (FocusHelper.handleTabKeyEvent(this, paramInt, paramKeyEvent)) || (super.onKeyDown(paramInt, paramKeyEvent));
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return (FocusHelper.handleTabKeyEvent(this, paramInt, paramKeyEvent)) || (super.onKeyUp(paramInt, paramKeyEvent));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AccessibleTabView
 * JD-Core Version:    0.7.0.1
 */