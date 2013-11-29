package com.android.launcher3;

import android.content.pm.ActivityInfo;

class PendingAddShortcutInfo
  extends PendingAddItemInfo
{
  ActivityInfo shortcutActivityInfo;
  
  public PendingAddShortcutInfo(ActivityInfo paramActivityInfo)
  {
    this.shortcutActivityInfo = paramActivityInfo;
  }
  
  public String toString()
  {
    return "Shortcut: " + this.shortcutActivityInfo.packageName;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PendingAddShortcutInfo
 * JD-Core Version:    0.7.0.1
 */