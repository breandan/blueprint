package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;

public class PreloadReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    final LauncherProvider localLauncherProvider = LauncherAppState.getLauncherProvider();
    String str;
    if (localLauncherProvider != null)
    {
      str = paramIntent.getStringExtra("com.android.launcher3.action.EXTRA_WORKSPACE_NAME");
      if (TextUtils.isEmpty(str)) {
        break label61;
      }
    }
    label61:
    for (final int i = paramContext.getResources().getIdentifier(str, "xml", "com.android.launcher3");; i = 0)
    {
      new Thread(new Runnable()
      {
        public void run()
        {
          localLauncherProvider.loadDefaultFavoritesIfNecessary(i);
        }
      }).start();
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PreloadReceiver
 * JD-Core Version:    0.7.0.1
 */