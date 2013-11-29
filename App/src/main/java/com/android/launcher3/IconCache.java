package com.android.launcher3;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class IconCache
{
  private final HashMap<ComponentName, CacheEntry> mCache = new HashMap(50);
  private final Context mContext;
  private final Bitmap mDefaultIcon;
  private int mIconDpi;
  private final PackageManager mPackageManager;
  
  public IconCache(Context paramContext)
  {
    ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService("activity");
    this.mContext = paramContext;
    this.mPackageManager = paramContext.getPackageManager();
    this.mIconDpi = localActivityManager.getLauncherLargeIconDensity();
    this.mDefaultIcon = makeDefaultIcon();
  }
  
  private CacheEntry cacheLocked(ComponentName paramComponentName, ResolveInfo paramResolveInfo, HashMap<Object, CharSequence> paramHashMap)
  {
    CacheEntry localCacheEntry = (CacheEntry)this.mCache.get(paramComponentName);
    ComponentName localComponentName;
    if (localCacheEntry == null)
    {
      localCacheEntry = new CacheEntry(null);
      this.mCache.put(paramComponentName, localCacheEntry);
      localComponentName = LauncherModel.getComponentNameFromResolveInfo(paramResolveInfo);
      if ((paramHashMap == null) || (!paramHashMap.containsKey(localComponentName))) {
        break label115;
      }
      localCacheEntry.title = ((CharSequence)paramHashMap.get(localComponentName)).toString();
    }
    for (;;)
    {
      if (localCacheEntry.title == null) {
        localCacheEntry.title = paramResolveInfo.activityInfo.name;
      }
      localCacheEntry.icon = Utilities.createIconBitmap(getFullResIcon(paramResolveInfo), this.mContext);
      return localCacheEntry;
      label115:
      localCacheEntry.title = paramResolveInfo.loadLabel(this.mPackageManager).toString();
      if (paramHashMap != null) {
        paramHashMap.put(localComponentName, localCacheEntry.title);
      }
    }
  }
  
  private Bitmap makeDefaultIcon()
  {
    Drawable localDrawable = getFullResDefaultActivityIcon();
    Bitmap localBitmap = Bitmap.createBitmap(Math.max(localDrawable.getIntrinsicWidth(), 1), Math.max(localDrawable.getIntrinsicHeight(), 1), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    localDrawable.setBounds(0, 0, localBitmap.getWidth(), localBitmap.getHeight());
    localDrawable.draw(localCanvas);
    localCanvas.setBitmap(null);
    return localBitmap;
  }
  
  public void flush()
  {
    synchronized (this.mCache)
    {
      this.mCache.clear();
      return;
    }
  }
  
  public void flushInvalidIcons(DeviceProfile paramDeviceProfile)
  {
    synchronized (this.mCache)
    {
      Iterator localIterator = this.mCache.entrySet().iterator();
      while (localIterator.hasNext())
      {
        CacheEntry localCacheEntry = (CacheEntry)((Map.Entry)localIterator.next()).getValue();
        if ((localCacheEntry.icon.getWidth() < paramDeviceProfile.iconSizePx) || (localCacheEntry.icon.getHeight() < paramDeviceProfile.iconSizePx)) {
          localIterator.remove();
        }
      }
    }
  }
  
  public Drawable getFullResDefaultActivityIcon()
  {
    return getFullResIcon(Resources.getSystem(), 17629184);
  }
  
  public Drawable getFullResIcon(ActivityInfo paramActivityInfo)
  {
    try
    {
      Resources localResources2 = this.mPackageManager.getResourcesForApplication(paramActivityInfo.applicationInfo);
      localResources1 = localResources2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        int i;
        Resources localResources1 = null;
      }
    }
    if (localResources1 != null)
    {
      i = paramActivityInfo.getIconResource();
      if (i != 0) {
        return getFullResIcon(localResources1, i);
      }
    }
    return getFullResDefaultActivityIcon();
  }
  
  public Drawable getFullResIcon(ResolveInfo paramResolveInfo)
  {
    return getFullResIcon(paramResolveInfo.activityInfo);
  }
  
  public Drawable getFullResIcon(Resources paramResources, int paramInt)
  {
    try
    {
      Drawable localDrawable2 = paramResources.getDrawableForDensity(paramInt, this.mIconDpi);
      localDrawable1 = localDrawable2;
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      for (;;)
      {
        Drawable localDrawable1 = null;
      }
    }
    if (localDrawable1 != null) {
      return localDrawable1;
    }
    return getFullResDefaultActivityIcon();
  }
  
  public Drawable getFullResIcon(String paramString, int paramInt)
  {
    try
    {
      Resources localResources2 = this.mPackageManager.getResourcesForApplication(paramString);
      localResources1 = localResources2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Resources localResources1 = null;
      }
    }
    if ((localResources1 != null) && (paramInt != 0)) {
      return getFullResIcon(localResources1, paramInt);
    }
    return getFullResDefaultActivityIcon();
  }
  
  /* Error */
  public Bitmap getIcon(ComponentName paramComponentName, ResolveInfo paramResolveInfo, HashMap<Object, CharSequence> paramHashMap)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 27	com/android/launcher3/IconCache:mCache	Ljava/util/HashMap;
    //   4: astore 4
    //   6: aload 4
    //   8: monitorenter
    //   9: aload_2
    //   10: ifnull +7 -> 17
    //   13: aload_1
    //   14: ifnonnull +8 -> 22
    //   17: aload 4
    //   19: monitorexit
    //   20: aconst_null
    //   21: areturn
    //   22: aload_0
    //   23: aload_1
    //   24: aload_2
    //   25: aload_3
    //   26: invokespecial 253	com/android/launcher3/IconCache:cacheLocked	(Landroid/content/ComponentName;Landroid/content/pm/ResolveInfo;Ljava/util/HashMap;)Lcom/android/launcher3/IconCache$CacheEntry;
    //   29: getfield 116	com/android/launcher3/IconCache$CacheEntry:icon	Landroid/graphics/Bitmap;
    //   32: astore 6
    //   34: aload 4
    //   36: monitorexit
    //   37: aload 6
    //   39: areturn
    //   40: astore 5
    //   42: aload 4
    //   44: monitorexit
    //   45: aload 5
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	IconCache
    //   0	48	1	paramComponentName	ComponentName
    //   0	48	2	paramResolveInfo	ResolveInfo
    //   0	48	3	paramHashMap	HashMap<Object, CharSequence>
    //   4	39	4	localHashMap	HashMap
    //   40	6	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   17	20	40	finally
    //   22	37	40	finally
    //   42	45	40	finally
  }
  
  public Bitmap getIcon(Intent paramIntent)
  {
    synchronized (this.mCache)
    {
      ResolveInfo localResolveInfo = this.mPackageManager.resolveActivity(paramIntent, 0);
      ComponentName localComponentName = paramIntent.getComponent();
      if ((localResolveInfo == null) || (localComponentName == null))
      {
        Bitmap localBitmap1 = this.mDefaultIcon;
        return localBitmap1;
      }
      Bitmap localBitmap2 = cacheLocked(localComponentName, localResolveInfo, null).icon;
      return localBitmap2;
    }
  }
  
  public void getTitleAndIcon(AppInfo paramAppInfo, ResolveInfo paramResolveInfo, HashMap<Object, CharSequence> paramHashMap)
  {
    synchronized (this.mCache)
    {
      CacheEntry localCacheEntry = cacheLocked(paramAppInfo.componentName, paramResolveInfo, paramHashMap);
      paramAppInfo.title = localCacheEntry.title;
      paramAppInfo.iconBitmap = localCacheEntry.icon;
      return;
    }
  }
  
  public boolean isDefaultIcon(Bitmap paramBitmap)
  {
    return this.mDefaultIcon == paramBitmap;
  }
  
  public void remove(ComponentName paramComponentName)
  {
    synchronized (this.mCache)
    {
      this.mCache.remove(paramComponentName);
      return;
    }
  }
  
  private static class CacheEntry
  {
    public Bitmap icon;
    public String title;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.IconCache
 * JD-Core Version:    0.7.0.1
 */