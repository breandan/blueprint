package com.google.android.googlequicksearchbox;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Process;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

public class GelStubAppWatcher
  extends BroadcastReceiver
{
  public static final String[] GEL_COMPONENTS = { "com.android.launcher3.LauncherProvider", "com.android.launcher3.PreloadReceiver", "com.android.launcher3.InstallShortcutReceiver", "com.android.launcher3.UninstallShortcutReceiver", "com.android.launcher3.UserInitializeReceiver", "com.android.launcher3.PackageChangedReceiver", "com.android.launcher3.WallpaperCropActivity", "com.google.android.launcher.GelWallpaperPickerActivity", "com.google.android.launcher.MarketUpdateReceiver", "com.google.android.velvet.tg.SetupWizardOptInIntroActivity", "com.google.android.launcher.GEL" };
  private static final String TAG = GelStubAppWatcher.class.getSimpleName();
  
  private static int getGelComponentEnabledSetting(Context paramContext, PackageManager paramPackageManager)
  {
    int i = paramPackageManager.checkSignatures(paramContext.getPackageName(), "com.google.android.launcher");
    if (i != 0)
    {
      if (i == -3) {
        Log.e(TAG, "Error: Found stub APK with bad signature");
      }
      return 2;
    }
    switch (paramPackageManager.getApplicationEnabledSetting("com.google.android.launcher"))
    {
    default: 
      Log.v(TAG, "stub is enabled");
      return 1;
    }
    Log.v(TAG, "stub is disabled");
    return 2;
  }
  
  private static void killHomeProcessIfRunning(Context paramContext)
  {
    Iterator localIterator = ((ActivityManager)paramContext.getSystemService("activity")).getRunningAppProcesses().iterator();
    while (localIterator.hasNext())
    {
      ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator.next();
      if (localRunningAppProcessInfo.processName.equals(paramContext.getPackageName()))
      {
        Log.v(TAG, "killing old home process, pid=" + localRunningAppProcessInfo.pid);
        Process.killProcess(localRunningAppProcessInfo.pid);
      }
    }
  }
  
  public static boolean updateHomeActivityVisibility(Context paramContext)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    int i = getGelComponentEnabledSetting(paramContext, localPackageManager);
    boolean bool = false;
    String[] arrayOfString = GEL_COMPONENTS;
    int j = arrayOfString.length;
    int k = 0;
    if (k < j)
    {
      ComponentName localComponentName = new ComponentName(paramContext, arrayOfString[k]);
      String str1;
      StringBuilder localStringBuilder;
      if (i != localPackageManager.getComponentEnabledSetting(localComponentName))
      {
        str1 = TAG;
        localStringBuilder = new StringBuilder().append("setting GEL component (").append(localComponentName).append(") ");
        if (i != 1) {
          break label128;
        }
      }
      label128:
      for (String str2 = "enabled";; str2 = "disabled")
      {
        Log.v(str1, str2);
        localPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
        bool = true;
        k++;
        break;
      }
    }
    return bool;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    Log.v(TAG, "onReceive: " + str);
    if (("android.intent.action.MY_PACKAGE_REPLACED".equals(str)) || ("android.intent.action.PACKAGE_FIRST_LAUNCH".equals(str)) || ("android.intent.action.BOOT_COMPLETED".equals(str)) || ("android.intent.action.PRE_BOOT_COMPLETED".equals(str))) {
      if ((updateHomeActivityVisibility(paramContext)) && (!"android.intent.action.PRE_BOOT_COMPLETED".equals(str))) {
        killHomeProcessIfRunning(paramContext);
      }
    }
    Uri localUri;
    do
    {
      return;
      localUri = paramIntent.getData();
    } while ((localUri == null) || (!"package".equals(localUri.getScheme())) || (!"com.google.android.launcher".equals(localUri.getEncodedSchemeSpecificPart())) || (!updateHomeActivityVisibility(paramContext)));
    killHomeProcessIfRunning(paramContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.googlequicksearchbox.GelStubAppWatcher
 * JD-Core Version:    0.7.0.1
 */