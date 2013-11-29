package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class AllAppsList
{
  public ArrayList<AppInfo> added = new ArrayList(42);
  public ArrayList<AppInfo> data = new ArrayList(42);
  private AppFilter mAppFilter;
  private IconCache mIconCache;
  public ArrayList<AppInfo> modified = new ArrayList();
  public ArrayList<AppInfo> removed = new ArrayList();
  
  public AllAppsList(IconCache paramIconCache, AppFilter paramAppFilter)
  {
    this.mIconCache = paramIconCache;
    this.mAppFilter = paramAppFilter;
  }
  
  static List<ResolveInfo> findActivitiesForPackage(Context paramContext, String paramString)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    Intent localIntent = new Intent("android.intent.action.MAIN", null);
    localIntent.addCategory("android.intent.category.LAUNCHER");
    localIntent.setPackage(paramString);
    List localList = localPackageManager.queryIntentActivities(localIntent, 0);
    if (localList != null) {
      return localList;
    }
    return new ArrayList();
  }
  
  private static boolean findActivity(ArrayList<AppInfo> paramArrayList, ComponentName paramComponentName)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      if (((AppInfo)paramArrayList.get(j)).componentName.equals(paramComponentName)) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean findActivity(List<ResolveInfo> paramList, ComponentName paramComponentName)
  {
    String str = paramComponentName.getClassName();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      if (((ResolveInfo)localIterator.next()).activityInfo.name.equals(str)) {
        return true;
      }
    }
    return false;
  }
  
  private AppInfo findApplicationInfoLocked(String paramString1, String paramString2)
  {
    Iterator localIterator = this.data.iterator();
    while (localIterator.hasNext())
    {
      AppInfo localAppInfo = (AppInfo)localIterator.next();
      ComponentName localComponentName = localAppInfo.intent.getComponent();
      if ((paramString1.equals(localComponentName.getPackageName())) && (paramString2.equals(localComponentName.getClassName()))) {
        return localAppInfo;
      }
    }
    return null;
  }
  
  public void add(AppInfo paramAppInfo)
  {
    if ((this.mAppFilter != null) && (!this.mAppFilter.shouldShowApp(paramAppInfo.componentName))) {}
    while (findActivity(this.data, paramAppInfo.componentName)) {
      return;
    }
    this.data.add(paramAppInfo);
    this.added.add(paramAppInfo);
  }
  
  public void addPackage(Context paramContext, String paramString)
  {
    List localList = findActivitiesForPackage(paramContext, paramString);
    if (localList.size() > 0)
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
        add(new AppInfo(paramContext.getPackageManager(), localResolveInfo, this.mIconCache, null));
      }
    }
  }
  
  public void clear()
  {
    this.data.clear();
    this.added.clear();
    this.removed.clear();
    this.modified.clear();
  }
  
  public void removePackage(String paramString)
  {
    ArrayList localArrayList = this.data;
    for (int i = -1 + localArrayList.size(); i >= 0; i--)
    {
      AppInfo localAppInfo = (AppInfo)localArrayList.get(i);
      if (paramString.equals(localAppInfo.intent.getComponent().getPackageName()))
      {
        this.removed.add(localAppInfo);
        localArrayList.remove(i);
      }
    }
    this.mIconCache.flush();
  }
  
  public void updatePackage(Context paramContext, String paramString)
  {
    List localList = findActivitiesForPackage(paramContext, paramString);
    if (localList.size() > 0)
    {
      for (int j = -1 + this.data.size(); j >= 0; j--)
      {
        AppInfo localAppInfo3 = (AppInfo)this.data.get(j);
        ComponentName localComponentName2 = localAppInfo3.intent.getComponent();
        if ((paramString.equals(localComponentName2.getPackageName())) && (!findActivity(localList, localComponentName2)))
        {
          this.removed.add(localAppInfo3);
          this.mIconCache.remove(localComponentName2);
          this.data.remove(j);
        }
      }
      int k = localList.size();
      int m = 0;
      if (m < k)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localList.get(m);
        AppInfo localAppInfo2 = findApplicationInfoLocked(localResolveInfo.activityInfo.applicationInfo.packageName, localResolveInfo.activityInfo.name);
        if (localAppInfo2 == null) {
          add(new AppInfo(paramContext.getPackageManager(), localResolveInfo, this.mIconCache, null));
        }
        for (;;)
        {
          m++;
          break;
          this.mIconCache.remove(localAppInfo2.componentName);
          this.mIconCache.getTitleAndIcon(localAppInfo2, localResolveInfo, null);
          this.modified.add(localAppInfo2);
        }
      }
    }
    else
    {
      for (int i = -1 + this.data.size(); i >= 0; i--)
      {
        AppInfo localAppInfo1 = (AppInfo)this.data.get(i);
        ComponentName localComponentName1 = localAppInfo1.intent.getComponent();
        if (paramString.equals(localComponentName1.getPackageName()))
        {
          this.removed.add(localAppInfo1);
          this.mIconCache.remove(localComponentName1);
          this.data.remove(i);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AllAppsList
 * JD-Core Version:    0.7.0.1
 */