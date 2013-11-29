package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PackageChangedReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getData().getSchemeSpecificPart();
    if ((str == null) || (str.length() == 0)) {
      return;
    }
    LauncherAppState.setApplicationContext(paramContext.getApplicationContext());
    WidgetPreviewLoader.removePackageFromDb(LauncherAppState.getInstance().getWidgetPreviewCacheDb(), str);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PackageChangedReceiver
 * JD-Core Version:    0.7.0.1
 */