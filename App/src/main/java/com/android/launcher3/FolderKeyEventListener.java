package com.android.launcher3;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

class FolderKeyEventListener
  implements View.OnKeyListener
{
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    return FocusHelper.handleFolderKeyEvent(paramView, paramInt, paramKeyEvent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FolderKeyEventListener
 * JD-Core Version:    0.7.0.1
 */