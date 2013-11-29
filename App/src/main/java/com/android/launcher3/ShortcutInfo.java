package com.android.launcher3;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;

class ShortcutInfo
  extends ItemInfo
{
  boolean customIcon;
  long firstInstallTime;
  int flags = 0;
  Intent.ShortcutIconResource iconResource;
  Intent intent;
  private Bitmap mIcon;
  boolean usingFallbackIcon;
  
  ShortcutInfo()
  {
    this.itemType = 1;
  }
  
  public ShortcutInfo(AppInfo paramAppInfo)
  {
    super(paramAppInfo);
    this.title = paramAppInfo.title.toString();
    this.intent = new Intent(paramAppInfo.intent);
    this.customIcon = false;
    this.flags = paramAppInfo.flags;
    this.firstInstallTime = paramAppInfo.firstInstallTime;
  }
  
  public static PackageInfo getPackageInfo(Context paramContext, String paramString)
  {
    try
    {
      PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramString, 0);
      return localPackageInfo;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.d("ShortcutInfo", "PackageManager.getPackageInfo failed for " + paramString);
    }
    return null;
  }
  
  public Bitmap getIcon(IconCache paramIconCache)
  {
    if (this.mIcon == null) {
      updateIcon(paramIconCache);
    }
    return this.mIcon;
  }
  
  protected Intent getIntent()
  {
    return this.intent;
  }
  
  void initFlagsAndFirstInstallTime(PackageInfo paramPackageInfo)
  {
    this.flags = AppInfo.initFlags(paramPackageInfo);
    this.firstInstallTime = AppInfo.initFirstInstallTime(paramPackageInfo);
  }
  
  void onAddToDatabase(ContentValues paramContentValues)
  {
    super.onAddToDatabase(paramContentValues);
    String str1;
    String str2;
    if (this.title != null)
    {
      str1 = this.title.toString();
      paramContentValues.put("title", str1);
      if (this.intent == null) {
        break label81;
      }
      str2 = this.intent.toUri(0);
      label43:
      paramContentValues.put("intent", str2);
      if (!this.customIcon) {
        break label86;
      }
      paramContentValues.put("iconType", Integer.valueOf(1));
      writeBitmap(paramContentValues, this.mIcon);
    }
    label81:
    label86:
    do
    {
      return;
      str1 = null;
      break;
      str2 = null;
      break label43;
      if (!this.usingFallbackIcon) {
        writeBitmap(paramContentValues, this.mIcon);
      }
      paramContentValues.put("iconType", Integer.valueOf(0));
    } while (this.iconResource == null);
    paramContentValues.put("iconPackage", this.iconResource.packageName);
    paramContentValues.put("iconResource", this.iconResource.resourceName);
  }
  
  final void setActivity(Context paramContext, ComponentName paramComponentName, int paramInt)
  {
    this.intent = new Intent("android.intent.action.MAIN");
    this.intent.addCategory("android.intent.category.LAUNCHER");
    this.intent.setComponent(paramComponentName);
    this.intent.setFlags(paramInt);
    this.itemType = 0;
    initFlagsAndFirstInstallTime(getPackageInfo(paramContext, this.intent.getComponent().getPackageName()));
  }
  
  public void setIcon(Bitmap paramBitmap)
  {
    this.mIcon = paramBitmap;
  }
  
  public String toString()
  {
    return "ShortcutInfo(title=" + this.title.toString() + "intent=" + this.intent + "id=" + this.id + " type=" + this.itemType + " container=" + this.container + " screen=" + this.screenId + " cellX=" + this.cellX + " cellY=" + this.cellY + " spanX=" + this.spanX + " spanY=" + this.spanY + " dropPos=" + this.dropPos + ")";
  }
  
  public void updateIcon(IconCache paramIconCache)
  {
    this.mIcon = paramIconCache.getIcon(this.intent);
    this.usingFallbackIcon = paramIconCache.isDefaultIcon(this.mIcon);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.ShortcutInfo
 * JD-Core Version:    0.7.0.1
 */