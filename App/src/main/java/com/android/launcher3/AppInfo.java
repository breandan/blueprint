package com.android.launcher3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class AppInfo
  extends ItemInfo
{
  ComponentName componentName;
  long firstInstallTime;
  int flags = 0;
  Bitmap iconBitmap;
  Intent intent;
  
  AppInfo()
  {
    this.itemType = 1;
  }
  
  public AppInfo(PackageManager paramPackageManager, ResolveInfo paramResolveInfo, IconCache paramIconCache, HashMap<Object, CharSequence> paramHashMap)
  {
    String str = paramResolveInfo.activityInfo.applicationInfo.packageName;
    this.componentName = new ComponentName(str, paramResolveInfo.activityInfo.name);
    this.container = -1L;
    setActivity(this.componentName, 270532608);
    try
    {
      PackageInfo localPackageInfo = paramPackageManager.getPackageInfo(str, 0);
      this.flags = initFlags(localPackageInfo);
      this.firstInstallTime = initFirstInstallTime(localPackageInfo);
      paramIconCache.getTitleAndIcon(this, paramResolveInfo, paramHashMap);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Log.d("Launcher3.AppInfo", "PackageManager.getApplicationInfo failed for " + str);
      }
    }
  }
  
  public static void dumpApplicationInfoList(String paramString1, String paramString2, ArrayList<AppInfo> paramArrayList)
  {
    Log.d(paramString1, paramString2 + " size=" + paramArrayList.size());
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      AppInfo localAppInfo = (AppInfo)localIterator.next();
      Log.d(paramString1, "   title=\"" + localAppInfo.title + "\" iconBitmap=" + localAppInfo.iconBitmap + " firstInstallTime=" + localAppInfo.firstInstallTime);
    }
  }
  
  public static long initFirstInstallTime(PackageInfo paramPackageInfo)
  {
    return paramPackageInfo.firstInstallTime;
  }
  
  public static int initFlags(PackageInfo paramPackageInfo)
  {
    int i = paramPackageInfo.applicationInfo.flags;
    int j = i & 0x1;
    int k = 0;
    if (j == 0)
    {
      k = 0x0 | 0x1;
      if ((i & 0x80) != 0) {
        k |= 0x2;
      }
    }
    return k;
  }
  
  protected Intent getIntent()
  {
    return this.intent;
  }
  
  public ShortcutInfo makeShortcut()
  {
    return new ShortcutInfo(this);
  }
  
  final void setActivity(ComponentName paramComponentName, int paramInt)
  {
    this.intent = new Intent("android.intent.action.MAIN");
    this.intent.addCategory("android.intent.category.LAUNCHER");
    this.intent.setComponent(paramComponentName);
    this.intent.setFlags(paramInt);
    this.itemType = 0;
  }
  
  public String toString()
  {
    return "ApplicationInfo(title=" + this.title.toString() + " id=" + this.id + " type=" + this.itemType + " container=" + this.container + " screen=" + this.screenId + " cellX=" + this.cellX + " cellY=" + this.cellY + " spanX=" + this.spanX + " spanY=" + this.spanY + " dropPos=" + this.dropPos + ")";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppInfo
 * JD-Core Version:    0.7.0.1
 */