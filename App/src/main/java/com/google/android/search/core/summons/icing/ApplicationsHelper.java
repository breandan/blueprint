package com.google.android.search.core.summons.icing;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.suggest.AppLaunchLogger;
import com.google.android.search.core.suggest.AppLaunchLogger.AppLaunch;
import com.google.android.search.core.suggest.AppLaunchLogger.ContextFileStreamProvider;
import com.google.android.search.shared.ondevice.GelVelAppFilter;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExpiringSum;
import com.google.android.shared.util.Util;
import com.google.common.collect.Maps;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

final class ApplicationsHelper
{
  private static final long LAUNCH_BUCKET_GRANULARITY_MILLIS = TimeUnit.DAYS.toMillis(1L);
  private final Clock mClock;
  private final PackageManager mPackageManager;
  private final PackageNamePopularity mPackageNamePopularity;
  
  public ApplicationsHelper(PackageManager paramPackageManager, Clock paramClock, PackageNamePopularity paramPackageNamePopularity)
  {
    this.mPackageManager = paramPackageManager;
    this.mClock = paramClock;
    this.mPackageNamePopularity = paramPackageNamePopularity;
  }
  
  private void createLaunchLogTableV7(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS applications_launch_log");
    paramSQLiteDatabase.execSQL("CREATE TABLE applications_launch_log (_id INTEGER PRIMARY KEY AUTOINCREMENT,component_name TEXT,usage_buckets_json TEXT)");
  }
  
  private String getActivityIconUriString(ActivityInfo paramActivityInfo)
  {
    int i = paramActivityInfo.getIconResource();
    if (i == 0) {}
    Uri localUri;
    do
    {
      return null;
      localUri = Util.getResourceUri(this.mPackageManager, paramActivityInfo.applicationInfo, i);
    } while (localUri == null);
    return localUri.toString();
  }
  
  private Collection<ComponentName> getAllApps(SQLiteDatabase paramSQLiteDatabase)
  {
    Cursor localCursor = paramSQLiteDatabase.query("applications", new String[] { "package_name", "class_name" }, null, null, null, null, null);
    HashSet localHashSet = new HashSet();
    if (localCursor != null) {
      try
      {
        int i = localCursor.getColumnIndex("package_name");
        int j = localCursor.getColumnIndex("class_name");
        while (localCursor.moveToNext()) {
          localHashSet.add(new ComponentName(localCursor.getString(i), localCursor.getString(j)));
        }
      }
      finally
      {
        localCursor.close();
      }
    }
    return localHashSet;
  }
  
  private int getCurrentApplicationCount(SQLiteDatabase paramSQLiteDatabase)
  {
    Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT COUNT(*) FROM applications", null);
    if (localCursor != null) {}
    try
    {
      if (localCursor.moveToNext())
      {
        int i = localCursor.getInt(0);
        return i;
      }
      return -1;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  private Map<ComponentName, ExpiringSum> getLaunchLogExpiringSums(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    HashMap localHashMap = Maps.newHashMap();
    Cursor localCursor = paramSQLiteDatabase.query("applications_launch_log", new String[] { "component_name", "usage_buckets_json" }, null, null, null, null, null);
    for (;;)
    {
      try
      {
        int i = localCursor.getColumnIndex("component_name");
        int j = localCursor.getColumnIndex("usage_buckets_json");
        if (!localCursor.moveToNext()) {
          break;
        }
        String str1 = localCursor.getString(i);
        String str2 = localCursor.getString(j);
        ComponentName localComponentName = ComponentName.unflattenFromString(str1);
        if (localComponentName == null) {
          Log.w("Icing.ApplicationsHelper", "Ignoring malformed component name string: " + str1);
        } else {
          localHashMap.put(localComponentName, new ExpiringSum(this.mClock, paramLong, LAUNCH_BUCKET_GRANULARITY_MILLIS, str2));
        }
      }
      finally
      {
        localCursor.close();
      }
    }
    localCursor.close();
    return localHashMap;
  }
  
  private long getMaxAgeMillis(SearchConfig paramSearchConfig)
  {
    return TimeUnit.DAYS.toMillis(paramSearchConfig.getIcingLaunchLogMaxAgeDays());
  }
  
  private Map<ComponentName, Long> getScoresFromDbFor(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, Collection<ComponentName> paramCollection)
  {
    int i = paramSearchConfig.getIcingAppLaunchValueHalfTimeDays();
    int j = paramSearchConfig.getIcingAppBonusHalfTimeDays();
    int k = paramSearchConfig.getIcingNewAppScorePercentOfMax();
    int m = paramSearchConfig.getIcingLaunchLogTrustConstant();
    Map localMap = getLaunchLogScores(getLaunchLogExpiringSums(paramSQLiteDatabase, getMaxAgeMillis(paramSearchConfig)), i);
    if (localMap.values().isEmpty()) {}
    for (double d = 0.0D;; d = ((Double)Collections.max(localMap.values())).doubleValue()) {
      return getScoresFor(paramCollection, getCombinedDecayedScores(paramCollection, localMap, getPackageBonusScores(j, k, d)), getPopularityBoostFactors(paramCollection), getLaunchLogTrustFactor(localMap.values(), m));
    }
  }
  
  private static double sum(Collection<Double> paramCollection)
  {
    double d;
    if ((paramCollection == null) || (paramCollection.isEmpty())) {
      d = 0.0D;
    }
    for (;;)
    {
      return d;
      d = 0.0D;
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        d += ((Double)localIterator.next()).doubleValue();
      }
    }
  }
  
  private Collection<ComponentName> updateLaunchLogScores(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, Context paramContext)
  {
    long l = getMaxAgeMillis(paramSearchConfig);
    Map localMap = getLaunchLogExpiringSums(paramSQLiteDatabase, l);
    List localList = AppLaunchLogger.getLaunches(new AppLaunchLogger.ContextFileStreamProvider(paramContext));
    HashMap localHashMap1 = Maps.newHashMap();
    HashMap localHashMap2 = Maps.newHashMap();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      AppLaunchLogger.AppLaunch localAppLaunch = (AppLaunchLogger.AppLaunch)localIterator.next();
      ExpiringSum localExpiringSum = (ExpiringSum)localMap.get(localAppLaunch.componentName);
      if (localExpiringSum == null)
      {
        localExpiringSum = new ExpiringSum(this.mClock, l, LAUNCH_BUCKET_GRANULARITY_MILLIS);
        localHashMap1.put(localAppLaunch.componentName, localExpiringSum);
        localMap.put(localAppLaunch.componentName, localExpiringSum);
      }
      localExpiringSum.incrementAtTime(localAppLaunch.time);
      localHashMap2.put(localAppLaunch.componentName, localExpiringSum);
    }
    updateLaunchLogTable(paramSQLiteDatabase, localMap, localHashMap1);
    return localHashMap2.keySet();
  }
  
  private void updateLaunchLogTable(SQLiteDatabase paramSQLiteDatabase, Map<ComponentName, ExpiringSum> paramMap1, Map<ComponentName, ExpiringSum> paramMap2)
  {
    ContentValues localContentValues = new ContentValues();
    paramSQLiteDatabase.beginTransaction();
    for (;;)
    {
      ComponentName localComponentName;
      String str;
      try
      {
        Iterator localIterator = paramMap1.entrySet().iterator();
        if (!localIterator.hasNext()) {
          break;
        }
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localComponentName = (ComponentName)localEntry.getKey();
        ExpiringSum localExpiringSum = (ExpiringSum)localEntry.getValue();
        str = localExpiringSum.getJsonIfChanged();
        if (localExpiringSum.getTotal() == 0)
        {
          String[] arrayOfString2 = new String[1];
          arrayOfString2[0] = localComponentName.flattenToString();
          paramSQLiteDatabase.delete("applications_launch_log", "component_name=?", arrayOfString2);
          continue;
        }
        if (str == null) {
          continue;
        }
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      localContentValues.clear();
      localContentValues.put("usage_buckets_json", str);
      if (paramMap2.containsKey(localComponentName))
      {
        localContentValues.put("component_name", localComponentName.flattenToString());
        paramSQLiteDatabase.insert("applications_launch_log", null, localContentValues);
      }
      else
      {
        String[] arrayOfString1 = new String[1];
        arrayOfString1[0] = localComponentName.flattenToString();
        paramSQLiteDatabase.update("applications_launch_log", localContentValues, "component_name=?", arrayOfString1);
      }
    }
    paramSQLiteDatabase.setTransactionSuccessful();
    paramSQLiteDatabase.endTransaction();
  }
  
  public void clearApplicationLaunchLog(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.beginTransaction();
    try
    {
      paramSQLiteDatabase.delete("applications_launch_log", null, null);
      paramSQLiteDatabase.setTransactionSuccessful();
      return;
    }
    finally
    {
      paramSQLiteDatabase.endTransaction();
    }
  }
  
  public void createApplicationsTable(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS applications");
    paramSQLiteDatabase.execSQL("CREATE TABLE applications (_id INTEGER PRIMARY KEY AUTOINCREMENT,display_name TEXT,icon_uri TEXT,package_name TEXT,class_name TEXT,score INTEGER)");
    createLaunchLogTableV7(paramSQLiteDatabase);
  }
  
  public void dump(SQLiteDatabase paramSQLiteDatabase, String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str1 = "extensive";; str1 = "simple")
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramString;
      arrayOfObject1[1] = ("ApplicationsHelper (" + str1 + ") state:");
      DumpUtils.println(paramPrintWriter, arrayOfObject1);
      String str2 = paramString + "  ";
      try
      {
        int i = getCurrentApplicationCount(paramSQLiteDatabase);
        Object[] arrayOfObject3 = new Object[2];
        arrayOfObject3[0] = str2;
        arrayOfObject3[1] = ("Application count: " + i);
        DumpUtils.println(paramPrintWriter, arrayOfObject3);
        DumpUtils.println(paramPrintWriter, new Object[0]);
        if (paramBoolean)
        {
          DumpUtils.dumpSqliteTable(paramSQLiteDatabase, str2, paramPrintWriter, "applications");
          DumpUtils.println(paramPrintWriter, new Object[0]);
          DumpUtils.dumpSqliteTable(paramSQLiteDatabase, str2, paramPrintWriter, "applications_launch_log");
          DumpUtils.println(paramPrintWriter, new Object[0]);
        }
        return;
      }
      catch (Exception localException)
      {
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = str2;
        arrayOfObject2[1] = ("Error dumping " + str1 + "ApplicationsHelper state ");
        arrayOfObject2[2] = localException;
        DumpUtils.println(paramPrintWriter, arrayOfObject2);
      }
    }
  }
  
  Map<ComponentName, Double> getCombinedDecayedScores(Collection<ComponentName> paramCollection, Map<ComponentName, Double> paramMap, Map<String, Double> paramMap1)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = paramCollection.iterator();
    if (localIterator.hasNext())
    {
      ComponentName localComponentName = (ComponentName)localIterator.next();
      double d1;
      if (paramMap.containsKey(localComponentName))
      {
        d1 = ((Double)paramMap.get(localComponentName)).doubleValue();
        label62:
        if (!paramMap1.containsKey(localComponentName.getPackageName())) {
          break label124;
        }
      }
      label124:
      for (double d2 = ((Double)paramMap1.get(localComponentName.getPackageName())).doubleValue();; d2 = 0.0D)
      {
        localHashMap.put(localComponentName, Double.valueOf(d2 + (1.0D + d1)));
        break;
        d1 = 0.0D;
        break label62;
      }
    }
    return localHashMap;
  }
  
  Map<ComponentName, Double> getLaunchLogScores(Map<ComponentName, ExpiringSum> paramMap, long paramLong)
  {
    double d1 = Math.pow(2.0D, -1.0D / paramLong);
    int[] arrayOfInt = new int[0];
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      arrayOfInt = ((ExpiringSum)localEntry.getValue()).getBucketValues(arrayOfInt);
      double d2 = 0.0D;
      double d3 = 1.0D;
      for (int i = 0; i < arrayOfInt.length; i++)
      {
        d2 += d3 * arrayOfInt[i];
        d3 *= d1;
      }
      localHashMap.put(localEntry.getKey(), Double.valueOf(d2));
    }
    return localHashMap;
  }
  
  double getLaunchLogTrustFactor(Collection<Double> paramCollection, long paramLong)
  {
    if (paramLong == 0L) {}
    double d;
    do
    {
      return 1.0D;
      d = sum(paramCollection);
    } while (d > paramLong);
    return d / paramLong;
  }
  
  Map<String, Double> getPackageBonusScores(long paramLong, int paramInt, double paramDouble)
  {
    double d1 = Math.pow(2.0D, -1.0D / paramLong);
    HashMap localHashMap = Maps.newHashMap();
    List localList = this.mPackageManager.getInstalledPackages(0);
    int i = 0;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
      long l1 = this.mClock.currentTimeMillis() - localPackageInfo.firstInstallTime;
      if (l1 < 0L)
      {
        l1 = 0L;
        i++;
      }
      long l2 = l1 / 86400000L;
      double d2 = paramDouble * paramInt / 100.0D * Math.pow(d1, l2);
      localHashMap.put(localPackageInfo.packageName, Double.valueOf(d2));
    }
    if (i > 0) {
      Log.w("Icing.ApplicationsHelper", "System clock may be wrong, " + i + " apps appear to be installed in the future.");
    }
    return localHashMap;
  }
  
  Map<ComponentName, Double> getPopularityBoostFactors(Collection<ComponentName> paramCollection)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = paramCollection.iterator();
    if (localIterator.hasNext())
    {
      ComponentName localComponentName = (ComponentName)localIterator.next();
      int i = this.mPackageNamePopularity.getBucket(localComponentName.getPackageName());
      if (i == -1) {}
      for (double d = 0.0D;; d = 1.0D - i / this.mPackageNamePopularity.getNumBuckets())
      {
        localHashMap.put(localComponentName, Double.valueOf(d));
        break;
      }
    }
    return localHashMap;
  }
  
  double getPopularityWeight(Collection<Double> paramCollection1, Collection<Double> paramCollection2)
  {
    double d1 = sum(paramCollection2);
    double d2 = sum(paramCollection1);
    if (d1 == 0.0D) {
      return 0.0D;
    }
    return Math.max(1.0D, d2) / d1;
  }
  
  Map<ComponentName, Long> getScoresFor(Collection<ComponentName> paramCollection, Map<ComponentName, Double> paramMap1, Map<ComponentName, Double> paramMap2, double paramDouble)
  {
    double d1 = getPopularityWeight(paramMap1.values(), paramMap2.values());
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      ComponentName localComponentName = (ComponentName)localIterator.next();
      double d2 = d1 * ((Double)paramMap2.get(localComponentName)).doubleValue();
      localHashMap.put(localComponentName, Long.valueOf(1L + Math.ceil(paramDouble * Math.max(0.0D, ((Double)paramMap1.get(localComponentName)).doubleValue()) + d2 * (1.0D - paramDouble))));
    }
    return localHashMap;
  }
  
  public Cursor getZeroPrefixApplicationSuggestions(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.rawQuery("SELECT * FROM applications ORDER BY score DESC LIMIT 10", null);
  }
  
  public int updateApplicationScores(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, Context paramContext, boolean paramBoolean)
  {
    Collection localCollection1 = updateLaunchLogScores(paramSQLiteDatabase, paramSearchConfig, paramContext);
    if (paramBoolean) {}
    for (Collection localCollection2 = getAllApps(paramSQLiteDatabase);; localCollection2 = localCollection1)
    {
      Map localMap = getScoresFromDbFor(paramSQLiteDatabase, paramSearchConfig, localCollection2);
      int i = 0;
      paramSQLiteDatabase.beginTransaction();
      try
      {
        ContentValues localContentValues = new ContentValues();
        String[] arrayOfString = new String[2];
        Iterator localIterator = localCollection2.iterator();
        while (localIterator.hasNext())
        {
          ComponentName localComponentName = (ComponentName)localIterator.next();
          long l = ((Long)localMap.get(localComponentName)).longValue();
          localContentValues.clear();
          localContentValues.put("score", Long.valueOf(l));
          arrayOfString[0] = localComponentName.getPackageName();
          arrayOfString[1] = localComponentName.getClassName();
          i += paramSQLiteDatabase.update("applications", localContentValues, "package_name=? AND class_name=?", arrayOfString);
        }
        paramSQLiteDatabase.setTransactionSuccessful();
        return i;
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
    }
  }
  
  public int updateApplicationsList(SQLiteDatabase paramSQLiteDatabase, SearchConfig paramSearchConfig, String paramString)
  {
    SystemClock.elapsedRealtime();
    int i = 0;
    paramSQLiteDatabase.beginTransaction();
    if (paramString != null) {}
    for (;;)
    {
      int j;
      HashMap localHashMap;
      int m;
      try
      {
        j = paramSQLiteDatabase.delete("applications", "package_name=?", new String[] { paramString });
        Intent localIntent = new Intent("android.intent.action.MAIN", null);
        localIntent.addCategory("android.intent.category.LAUNCHER");
        localIntent.setPackage(paramString);
        List localList = this.mPackageManager.queryIntentActivities(localIntent, 0);
        int k;
        ResolveInfo localResolveInfo;
        Object localObject2;
        String str1;
        String str2;
        ComponentName localComponentName1;
        if (localList == null)
        {
          k = 0;
          localHashMap = Maps.newHashMap();
          m = 0;
          if (m >= k) {
            break label297;
          }
          localResolveInfo = (ResolveInfo)localList.get(m);
          localObject2 = localResolveInfo.loadLabel(this.mPackageManager).toString();
          str1 = localResolveInfo.activityInfo.name;
          if (TextUtils.isEmpty((CharSequence)localObject2)) {
            localObject2 = str1;
          }
          str2 = localResolveInfo.activityInfo.applicationInfo.packageName;
          localComponentName1 = new ComponentName(str2, str1);
          if (!GelVelAppFilter.shouldShowApp(str2, str1, true))
          {
            break label432;
            j = paramSQLiteDatabase.delete("applications", "1", null);
          }
        }
        else
        {
          k = localList.size();
          continue;
        }
        String str3 = getActivityIconUriString(localResolveInfo.activityInfo);
        ContentValues localContentValues1 = new ContentValues();
        localContentValues1.put("display_name", (String)localObject2);
        localContentValues1.put("icon_uri", str3);
        localContentValues1.put("package_name", str2);
        localContentValues1.put("class_name", str1);
        localHashMap.put(localComponentName1, localContentValues1);
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      label297:
      Map localMap = getScoresFromDbFor(paramSQLiteDatabase, paramSearchConfig, localHashMap.keySet());
      Iterator localIterator = localHashMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        ComponentName localComponentName2 = (ComponentName)localEntry.getKey();
        ContentValues localContentValues2 = (ContentValues)localEntry.getValue();
        localContentValues2.put("score", Long.valueOf(((Long)localMap.get(localComponentName2)).longValue()));
        if (paramSQLiteDatabase.insert("applications", null, localContentValues2) >= 0L) {
          i++;
        }
      }
      paramSQLiteDatabase.setTransactionSuccessful();
      paramSQLiteDatabase.endTransaction();
      return i + j;
      label432:
      m++;
    }
  }
  
  public void upgradeDbTo6(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS applications_appdatasearch_seqno_table");
  }
  
  public void upgradeDbTo7(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE applications ADD COLUMN score INTEGER");
    createLaunchLogTableV7(paramSQLiteDatabase);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.ApplicationsHelper
 * JD-Core Version:    0.7.0.1
 */