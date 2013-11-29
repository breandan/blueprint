package com.android.launcher3;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

class HotseatIconKeyEventListener
  implements View.OnKeyListener
{
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    return FocusHelper.handleHotseatButtonKeyEvent(paramView, paramInt, paramKeyEvent, paramView.getResources().getConfiguration().orientation);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HotseatIconKeyEventListener
 * JD-Core Version:    0.7.0.1
 */