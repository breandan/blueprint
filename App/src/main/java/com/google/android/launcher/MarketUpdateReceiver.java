package com.google.android.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MarketUpdateReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str1 = paramIntent.getAction();
    String str2 = "none";
    Uri localUri = paramIntent.getData();
    if (localUri != null) {
      str2 = localUri.getSchemeSpecificPart();
    }
    if ("com.android.launcher.action.ACTION_PACKAGE_ENQUEUED".equals(str1))
    {
      String str3 = "install";
      if (paramIntent.hasExtra("reason")) {
        str3 = paramIntent.getStringExtra("reason");
      }
      Log.d("MarketUpdateReceiver", "market has promised to " + str3 + ": " + str2);
      return;
    }
    if ("com.android.launcher.action.ACTION_PACKAGE_DOWNLOADING".equals(str1))
    {
      int i = paramIntent.getIntExtra("progress", 0);
      Log.d("MarketUpdateReceiver", "market is downloading (" + i + "%): " + str2);
      return;
    }
    if ("com.android.launcher.action.ACTION_PACKAGE_INSTALLING".equals(str1))
    {
      Log.d("MarketUpdateReceiver", "market is installing: " + str2);
      return;
    }
    if ("com.android.launcher.action.ACTION_PACKAGE_DEQUEUED".equals(str1))
    {
      if (paramIntent.getBooleanExtra("com.android.launcher.action.INSTALL_COMPLETED", false))
      {
        Log.d("MarketUpdateReceiver", "market has installed: " + str2);
        return;
      }
      Log.d("MarketUpdateReceiver", "market has decided not to install: " + str2);
      return;
    }
    Log.d("MarketUpdateReceiver", "unknown message " + str1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.launcher.MarketUpdateReceiver
 * JD-Core Version:    0.7.0.1
 */