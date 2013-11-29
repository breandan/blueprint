package com.google.android.launcher;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Pair;
import com.android.launcher3.WallpaperPickerActivity;

public class GelWallpaperPickerActivity
  extends WallpaperPickerActivity
{
  public Pair<ApplicationInfo, Integer> getWallpaperArrayResourceId()
  {
    try
    {
      ApplicationInfo localApplicationInfo = getPackageManager().getApplicationInfo("com.google.android.launcher", 128);
      if (localApplicationInfo != null)
      {
        Bundle localBundle = localApplicationInfo.metaData;
        if (localBundle != null)
        {
          Pair localPair = new Pair(localApplicationInfo, Integer.valueOf(localBundle.getInt("wallpapers", 0)));
          return localPair;
        }
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.launcher.GelWallpaperPickerActivity
 * JD-Core Version:    0.7.0.1
 */