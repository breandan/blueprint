package com.android.launcher3;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LauncherModel
  extends BroadcastReceiver
{
  public static final Comparator<AppInfo> APP_INSTALL_TIME_COMPARATOR = new Comparator()
  {
    public final int compare(AppInfo paramAnonymousAppInfo1, AppInfo paramAnonymousAppInfo2)
    {
      if (paramAnonymousAppInfo1.firstInstallTime < paramAnonymousAppInfo2.firstInstallTime) {
        return 1;
      }
      if (paramAnonymousAppInfo1.firstInstallTime > paramAnonymousAppInfo2.firstInstallTime) {
        return -1;
      }
      return 0;
    }
  };
  static final ArrayList<Runnable> mDeferredBindRunnables;
  static final ArrayList<LauncherAppWidgetInfo> sBgAppWidgets;
  static final HashMap<Object, byte[]> sBgDbIconCache;
  static final HashMap<Long, FolderInfo> sBgFolders;
  static final HashMap<Long, ItemInfo> sBgItemsIdMap;
  static final Object sBgLock;
  static final ArrayList<ItemInfo> sBgWorkspaceItems;
  static final ArrayList<Long> sBgWorkspaceScreens;
  private static final Handler sWorker;
  private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
  private boolean mAllAppsLoaded;
  private final LauncherAppState mApp;
  private final boolean mAppsCanBeOnRemoveableStorage;
  AllAppsList mBgAllAppsList;
  private WeakReference<Callbacks> mCallbacks;
  private Bitmap mDefaultIcon;
  private volatile boolean mFlushingWorkerThread;
  private DeferredHandler mHandler = new DeferredHandler();
  private IconCache mIconCache;
  private boolean mIsLoaderTaskRunning;
  private LoaderTask mLoaderTask;
  private final Object mLock = new Object();
  protected int mPreviousConfigMcc;
  private boolean mWorkspaceLoaded;
  
  static
  {
    sWorkerThread.start();
    sWorker = new Handler(sWorkerThread.getLooper());
    mDeferredBindRunnables = new ArrayList();
    sBgLock = new Object();
    sBgItemsIdMap = new HashMap();
    sBgWorkspaceItems = new ArrayList();
    sBgAppWidgets = new ArrayList();
    sBgFolders = new HashMap();
    sBgDbIconCache = new HashMap();
    sBgWorkspaceScreens = new ArrayList();
  }
  
  LauncherModel(LauncherAppState paramLauncherAppState, IconCache paramIconCache, AppFilter paramAppFilter)
  {
    Context localContext = paramLauncherAppState.getContext();
    this.mAppsCanBeOnRemoveableStorage = Environment.isExternalStorageRemovable();
    this.mApp = paramLauncherAppState;
    this.mBgAllAppsList = new AllAppsList(paramIconCache, paramAppFilter);
    this.mIconCache = paramIconCache;
    this.mPreviousConfigMcc = localContext.getResources().getConfiguration().mcc;
  }
  
  static void addItemToDatabase(Context paramContext, final ItemInfo paramItemInfo, long paramLong1, long paramLong2, int paramInt1, int paramInt2, final boolean paramBoolean)
  {
    paramItemInfo.container = paramLong1;
    paramItemInfo.cellX = paramInt1;
    paramItemInfo.cellY = paramInt2;
    if (((paramContext instanceof Launcher)) && (paramLong2 < 0L) && (paramLong1 == -101L)) {}
    for (paramItemInfo.screenId = ((Launcher)paramContext).getHotseat().getOrderInHotseat(paramInt1, paramInt2);; paramItemInfo.screenId = paramLong2)
    {
      final ContentValues localContentValues = new ContentValues();
      ContentResolver localContentResolver = paramContext.getContentResolver();
      paramItemInfo.onAddToDatabase(localContentValues);
      paramItemInfo.id = LauncherAppState.getLauncherProvider().generateNewItemId();
      localContentValues.put("_id", Long.valueOf(paramItemInfo.id));
      paramItemInfo.updateValuesWithCoordinates(localContentValues, paramItemInfo.cellX, paramItemInfo.cellY);
      runOnWorkerThread(new Runnable()
      {
        public void run()
        {
          ContentResolver localContentResolver = this.val$cr;
          Uri localUri;
          if (paramBoolean)
          {
            localUri = LauncherSettings.Favorites.CONTENT_URI;
            localContentResolver.insert(localUri, localContentValues);
          }
          for (;;)
          {
            synchronized (LauncherModel.sBgLock)
            {
              LauncherModel.checkItemInfoLocked(paramItemInfo.id, paramItemInfo, null);
              LauncherModel.sBgItemsIdMap.put(Long.valueOf(paramItemInfo.id), paramItemInfo);
              switch (paramItemInfo.itemType)
              {
              case 3: 
              default: 
                return;
                localUri = LauncherSettings.Favorites.CONTENT_URI_NO_NOTIFICATION;
                break;
              case 2: 
                LauncherModel.sBgFolders.put(Long.valueOf(paramItemInfo.id), (FolderInfo)paramItemInfo);
              case 0: 
              case 1: 
                if ((paramItemInfo.container == -100L) || (paramItemInfo.container == -101L)) {
                  LauncherModel.sBgWorkspaceItems.add(paramItemInfo);
                }
                break;
              }
            }
            if (!LauncherModel.sBgFolders.containsKey(Long.valueOf(paramItemInfo.container)))
            {
              Log.e("Launcher.Model", "adding item: " + paramItemInfo + " to a folder that " + " doesn't exist");
              continue;
              LauncherModel.sBgAppWidgets.add((LauncherAppWidgetInfo)paramItemInfo);
            }
          }
        }
      });
      return;
    }
  }
  
  static void addOrMoveItemInDatabase(Context paramContext, ItemInfo paramItemInfo, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    if (paramItemInfo.container == -1L)
    {
      addItemToDatabase(paramContext, paramItemInfo, paramLong1, paramLong2, paramInt1, paramInt2, false);
      return;
    }
    moveItemInDatabase(paramContext, paramItemInfo, paramLong1, paramLong2, paramInt1, paramInt2);
  }
  
  static void checkItemInfo(ItemInfo paramItemInfo)
  {
    final StackTraceElement[] arrayOfStackTraceElement = new Throwable().getStackTrace();
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        synchronized (LauncherModel.sBgLock)
        {
          LauncherModel.checkItemInfoLocked(this.val$itemId, arrayOfStackTraceElement, this.val$stackTrace);
          return;
        }
      }
    });
  }
  
  static void checkItemInfoLocked(long paramLong, ItemInfo paramItemInfo, StackTraceElement[] paramArrayOfStackTraceElement)
  {
    ItemInfo localItemInfo = (ItemInfo)sBgItemsIdMap.get(Long.valueOf(paramLong));
    if ((localItemInfo != null) && (paramItemInfo != localItemInfo))
    {
      if (((localItemInfo instanceof ShortcutInfo)) && ((paramItemInfo instanceof ShortcutInfo)))
      {
        ShortcutInfo localShortcutInfo1 = (ShortcutInfo)localItemInfo;
        ShortcutInfo localShortcutInfo2 = (ShortcutInfo)paramItemInfo;
        if ((!localShortcutInfo1.title.toString().equals(localShortcutInfo2.title.toString())) || (!localShortcutInfo1.intent.filterEquals(localShortcutInfo2.intent)) || (localShortcutInfo1.id != localShortcutInfo2.id) || (localShortcutInfo1.itemType != localShortcutInfo2.itemType) || (localShortcutInfo1.container != localShortcutInfo2.container) || (localShortcutInfo1.screenId != localShortcutInfo2.screenId) || (localShortcutInfo1.cellX != localShortcutInfo2.cellX) || (localShortcutInfo1.cellY != localShortcutInfo2.cellY) || (localShortcutInfo1.spanX != localShortcutInfo2.spanX) || (localShortcutInfo1.spanY != localShortcutInfo2.spanY) || (((localShortcutInfo1.dropPos != null) || (localShortcutInfo2.dropPos != null)) && ((localShortcutInfo1.dropPos == null) || (localShortcutInfo2.dropPos == null) || (localShortcutInfo1.dropPos[0] != localShortcutInfo2.dropPos[0]) || (localShortcutInfo1.dropPos[1] != localShortcutInfo2.dropPos[1])))) {}
      }
    }
    else {
      return;
    }
    StringBuilder localStringBuilder1 = new StringBuilder().append("item: ");
    String str1;
    label291:
    StringBuilder localStringBuilder2;
    if (paramItemInfo != null)
    {
      str1 = paramItemInfo.toString();
      localStringBuilder2 = localStringBuilder1.append(str1).append("modelItem: ");
      if (localItemInfo == null) {
        break label362;
      }
    }
    label362:
    for (String str2 = localItemInfo.toString();; str2 = "null")
    {
      RuntimeException localRuntimeException = new RuntimeException(str2 + "Error: ItemInfo passed to checkItemInfo doesn't match original");
      if (paramArrayOfStackTraceElement == null) {
        break;
      }
      localRuntimeException.setStackTrace(paramArrayOfStackTraceElement);
      return;
      str1 = "null";
      break label291;
    }
  }
  
  static void deleteFolderContentsFromDatabase(Context paramContext, final FolderInfo paramFolderInfo)
  {
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        this.val$cr.delete(LauncherSettings.Favorites.getContentUri(paramFolderInfo.id, false), null, null);
        synchronized (LauncherModel.sBgLock)
        {
          LauncherModel.sBgItemsIdMap.remove(Long.valueOf(paramFolderInfo.id));
          LauncherModel.sBgFolders.remove(Long.valueOf(paramFolderInfo.id));
          LauncherModel.sBgDbIconCache.remove(paramFolderInfo);
          LauncherModel.sBgWorkspaceItems.remove(paramFolderInfo);
          this.val$cr.delete(LauncherSettings.Favorites.CONTENT_URI_NO_NOTIFICATION, "container=" + paramFolderInfo.id, null);
          synchronized (LauncherModel.sBgLock)
          {
            Iterator localIterator = paramFolderInfo.contents.iterator();
            if (localIterator.hasNext())
            {
              ShortcutInfo localShortcutInfo = (ShortcutInfo)localIterator.next();
              LauncherModel.sBgItemsIdMap.remove(Long.valueOf(localShortcutInfo.id));
              LauncherModel.sBgDbIconCache.remove(localShortcutInfo);
            }
          }
        }
      }
    });
  }
  
  static void deleteItemFromDatabase(Context paramContext, final ItemInfo paramItemInfo)
  {
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        this.val$cr.delete(this.val$uriToDelete, null, null);
        for (;;)
        {
          synchronized (LauncherModel.sBgLock)
          {
            switch (paramItemInfo.itemType)
            {
            case 3: 
            default: 
              LauncherModel.sBgItemsIdMap.remove(Long.valueOf(paramItemInfo.id));
              LauncherModel.sBgDbIconCache.remove(paramItemInfo);
              return;
            case 2: 
              LauncherModel.sBgFolders.remove(Long.valueOf(paramItemInfo.id));
              Iterator localIterator = LauncherModel.sBgItemsIdMap.values().iterator();
              if (localIterator.hasNext())
              {
                ItemInfo localItemInfo = (ItemInfo)localIterator.next();
                if (localItemInfo.container != paramItemInfo.id) {
                  continue;
                }
                Log.e("Launcher.Model", "deleting a folder (" + paramItemInfo + ") which still " + "contains items (" + localItemInfo + ")");
              }
              break;
            }
          }
          LauncherModel.sBgWorkspaceItems.remove(paramItemInfo);
          continue;
          LauncherModel.sBgWorkspaceItems.remove(paramItemInfo);
          continue;
          LauncherModel.sBgAppWidgets.remove((LauncherAppWidgetInfo)paramItemInfo);
        }
      }
    });
  }
  
  static ArrayList<ItemInfo> filterItemInfos(Collection<ItemInfo> paramCollection, ItemInfoFilter paramItemInfoFilter)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramCollection.iterator();
    while (localIterator1.hasNext())
    {
      ItemInfo localItemInfo = (ItemInfo)localIterator1.next();
      if ((localItemInfo instanceof ShortcutInfo))
      {
        ShortcutInfo localShortcutInfo2 = (ShortcutInfo)localItemInfo;
        ComponentName localComponentName3 = localShortcutInfo2.intent.getComponent();
        if ((localComponentName3 != null) && (paramItemInfoFilter.filterItem(null, localShortcutInfo2, localComponentName3))) {
          localHashSet.add(localShortcutInfo2);
        }
      }
      else if ((localItemInfo instanceof FolderInfo))
      {
        FolderInfo localFolderInfo = (FolderInfo)localItemInfo;
        Iterator localIterator2 = localFolderInfo.contents.iterator();
        while (localIterator2.hasNext())
        {
          ShortcutInfo localShortcutInfo1 = (ShortcutInfo)localIterator2.next();
          ComponentName localComponentName2 = localShortcutInfo1.intent.getComponent();
          if ((localComponentName2 != null) && (paramItemInfoFilter.filterItem(localFolderInfo, localShortcutInfo1, localComponentName2))) {
            localHashSet.add(localShortcutInfo1);
          }
        }
      }
      else if ((localItemInfo instanceof LauncherAppWidgetInfo))
      {
        LauncherAppWidgetInfo localLauncherAppWidgetInfo = (LauncherAppWidgetInfo)localItemInfo;
        ComponentName localComponentName1 = localLauncherAppWidgetInfo.providerName;
        if ((localComponentName1 != null) && (paramItemInfoFilter.filterItem(null, localLauncherAppWidgetInfo, localComponentName1))) {
          localHashSet.add(localLauncherAppWidgetInfo);
        }
      }
    }
    return new ArrayList(localHashSet);
  }
  
  static Pair<Long, int[]> findNextAvailableIconSpace(Context paramContext, String paramString, Intent paramIntent, int paramInt, ArrayList<Long> paramArrayList)
  {
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    LauncherModel localLauncherModel = localLauncherAppState.getModel();
    for (;;)
    {
      int k;
      try
      {
        if (sWorkerThread.getThreadId() != Process.myTid()) {
          localLauncherModel.flushWorkerThread();
        }
        ArrayList localArrayList = getItemsInLocalCoordinates(paramContext);
        int i = Math.min(paramInt, paramArrayList.size());
        int j = paramArrayList.size();
        k = i;
        if ((k < j) && (0 == 0))
        {
          int[] arrayOfInt = new int[2];
          if (findNextAvailableIconSpaceInScreen(localArrayList, arrayOfInt, ((Long)paramArrayList.get(k)).longValue()))
          {
            Pair localPair = new Pair(paramArrayList.get(k), arrayOfInt);
            return localPair;
          }
        }
        else
        {
          return null;
        }
      }
      finally {}
      k++;
    }
  }
  
  static boolean findNextAvailableIconSpaceInScreen(ArrayList<ItemInfo> paramArrayList, int[] paramArrayOfInt, long paramLong)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = (int)localDeviceProfile.numColumns;
    int j = (int)localDeviceProfile.numRows;
    int[] arrayOfInt = { i, j };
    boolean[][] arrayOfBoolean = (boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt);
    for (int k = 0; k < paramArrayList.size(); k++)
    {
      ItemInfo localItemInfo = (ItemInfo)paramArrayList.get(k);
      if ((localItemInfo.container == -100L) && (localItemInfo.screenId == paramLong))
      {
        int m = localItemInfo.cellX;
        int n = localItemInfo.cellY;
        int i1 = localItemInfo.spanX;
        int i2 = localItemInfo.spanY;
        for (int i3 = m; (i3 >= 0) && (i3 < m + i1) && (i3 < i); i3++) {
          for (int i4 = n; i4 >= 0; i4++)
          {
            int i5 = n + i2;
            if ((i4 >= i5) || (i4 >= j)) {
              break;
            }
            arrayOfBoolean[i3][i4] = 1;
          }
        }
      }
    }
    return CellLayout.findVacantCell(paramArrayOfInt, 1, 1, i, j, arrayOfBoolean);
  }
  
  private static FolderInfo findOrMakeFolder(HashMap<Long, FolderInfo> paramHashMap, long paramLong)
  {
    FolderInfo localFolderInfo = (FolderInfo)paramHashMap.get(Long.valueOf(paramLong));
    if (localFolderInfo == null)
    {
      localFolderInfo = new FolderInfo();
      paramHashMap.put(Long.valueOf(paramLong), localFolderInfo);
    }
    return localFolderInfo;
  }
  
  private void forceReload()
  {
    resetLoadedState(true, true);
    startLoaderFromBackground();
  }
  
  public static final Comparator<AppInfo> getAppNameComparator()
  {
    new Comparator()
    {
      public final int compare(AppInfo paramAnonymousAppInfo1, AppInfo paramAnonymousAppInfo2)
      {
        int i = this.val$collator.compare(paramAnonymousAppInfo1.title.toString().trim(), paramAnonymousAppInfo2.title.toString().trim());
        if (i == 0) {
          i = paramAnonymousAppInfo1.componentName.compareTo(paramAnonymousAppInfo2.componentName);
        }
        return i;
      }
    };
  }
  
  static int getCellLayoutChildId(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return (0xFF & (int)paramLong1) << 24 | (0xFF & (int)paramLong2) << 16 | (paramInt1 & 0xFF) << 8 | paramInt2 & 0xFF;
  }
  
  static ComponentName getComponentNameFromResolveInfo(ResolveInfo paramResolveInfo)
  {
    if (paramResolveInfo.activityInfo != null) {
      return new ComponentName(paramResolveInfo.activityInfo.packageName, paramResolveInfo.activityInfo.name);
    }
    return new ComponentName(paramResolveInfo.serviceInfo.packageName, paramResolveInfo.serviceInfo.name);
  }
  
  private ArrayList<ItemInfo> getItemInfoForComponentName(final ComponentName paramComponentName)
  {
    ItemInfoFilter local12 = new ItemInfoFilter()
    {
      public boolean filterItem(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2, ComponentName paramAnonymousComponentName)
      {
        return paramAnonymousComponentName.equals(paramComponentName);
      }
    };
    return filterItemInfos(sBgItemsIdMap.values(), local12);
  }
  
  private ArrayList<ItemInfo> getItemInfoForPackageName(final String paramString)
  {
    ItemInfoFilter local11 = new ItemInfoFilter()
    {
      public boolean filterItem(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2, ComponentName paramAnonymousComponentName)
      {
        return paramAnonymousComponentName.getPackageName().equals(paramString);
      }
    };
    return filterItemInfos(sBgItemsIdMap.values(), local11);
  }
  
  /* Error */
  static ArrayList<ItemInfo> getItemsInLocalCoordinates(Context paramContext)
  {
    // Byte code:
    //   0: new 81	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 83	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: aload_0
    //   9: invokevirtual 291	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   12: getstatic 666	com/android/launcher3/LauncherSettings$Favorites:CONTENT_URI	Landroid/net/Uri;
    //   15: bipush 7
    //   17: anewarray 372	java/lang/String
    //   20: dup
    //   21: iconst_0
    //   22: ldc_w 667
    //   25: aastore
    //   26: dup
    //   27: iconst_1
    //   28: ldc_w 668
    //   31: aastore
    //   32: dup
    //   33: iconst_2
    //   34: ldc_w 670
    //   37: aastore
    //   38: dup
    //   39: iconst_3
    //   40: ldc_w 671
    //   43: aastore
    //   44: dup
    //   45: iconst_4
    //   46: ldc_w 672
    //   49: aastore
    //   50: dup
    //   51: iconst_5
    //   52: ldc_w 673
    //   55: aastore
    //   56: dup
    //   57: bipush 6
    //   59: ldc_w 674
    //   62: aastore
    //   63: aconst_null
    //   64: aconst_null
    //   65: aconst_null
    //   66: invokevirtual 680	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   69: astore_2
    //   70: aload_2
    //   71: ldc_w 667
    //   74: invokeinterface 686 2 0
    //   79: istore_3
    //   80: aload_2
    //   81: ldc_w 668
    //   84: invokeinterface 686 2 0
    //   89: istore 4
    //   91: aload_2
    //   92: ldc_w 670
    //   95: invokeinterface 686 2 0
    //   100: istore 5
    //   102: aload_2
    //   103: ldc_w 671
    //   106: invokeinterface 686 2 0
    //   111: istore 6
    //   113: aload_2
    //   114: ldc_w 672
    //   117: invokeinterface 686 2 0
    //   122: istore 7
    //   124: aload_2
    //   125: ldc_w 673
    //   128: invokeinterface 686 2 0
    //   133: istore 8
    //   135: aload_2
    //   136: ldc_w 674
    //   139: invokeinterface 686 2 0
    //   144: istore 9
    //   146: aload_2
    //   147: invokeinterface 689 1 0
    //   152: ifeq +136 -> 288
    //   155: new 257	com/android/launcher3/ItemInfo
    //   158: dup
    //   159: invokespecial 690	com/android/launcher3/ItemInfo:<init>	()V
    //   162: astore 12
    //   164: aload 12
    //   166: aload_2
    //   167: iload 6
    //   169: invokeinterface 694 2 0
    //   174: putfield 264	com/android/launcher3/ItemInfo:cellX	I
    //   177: aload 12
    //   179: aload_2
    //   180: iload 7
    //   182: invokeinterface 694 2 0
    //   187: putfield 267	com/android/launcher3/ItemInfo:cellY	I
    //   190: aload 12
    //   192: iconst_1
    //   193: aload_2
    //   194: iload 8
    //   196: invokeinterface 694 2 0
    //   201: invokestatic 697	java/lang/Math:max	(II)I
    //   204: putfield 581	com/android/launcher3/ItemInfo:spanX	I
    //   207: aload 12
    //   209: iconst_1
    //   210: aload_2
    //   211: iload 9
    //   213: invokeinterface 694 2 0
    //   218: invokestatic 697	java/lang/Math:max	(II)I
    //   221: putfield 582	com/android/launcher3/ItemInfo:spanY	I
    //   224: aload 12
    //   226: aload_2
    //   227: iload 4
    //   229: invokeinterface 694 2 0
    //   234: i2l
    //   235: putfield 261	com/android/launcher3/ItemInfo:container	J
    //   238: aload 12
    //   240: aload_2
    //   241: iload_3
    //   242: invokeinterface 694 2 0
    //   247: putfield 698	com/android/launcher3/ItemInfo:itemType	I
    //   250: aload 12
    //   252: aload_2
    //   253: iload 5
    //   255: invokeinterface 694 2 0
    //   260: i2l
    //   261: putfield 284	com/android/launcher3/ItemInfo:screenId	J
    //   264: aload_1
    //   265: aload 12
    //   267: invokevirtual 699	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   270: pop
    //   271: goto -125 -> 146
    //   274: astore 11
    //   276: aload_1
    //   277: invokevirtual 702	java/util/ArrayList:clear	()V
    //   280: aload_2
    //   281: invokeinterface 705 1 0
    //   286: aload_1
    //   287: areturn
    //   288: aload_2
    //   289: invokeinterface 705 1 0
    //   294: aload_1
    //   295: areturn
    //   296: astore 10
    //   298: aload_2
    //   299: invokeinterface 705 1 0
    //   304: aload 10
    //   306: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	307	0	paramContext	Context
    //   7	288	1	localArrayList	ArrayList
    //   69	230	2	localCursor	Cursor
    //   79	163	3	i	int
    //   89	139	4	j	int
    //   100	154	5	k	int
    //   111	57	6	m	int
    //   122	59	7	n	int
    //   133	62	8	i1	int
    //   144	68	9	i2	int
    //   296	9	10	localObject	Object
    //   274	1	11	localException	Exception
    //   162	104	12	localItemInfo	ItemInfo
    // Exception table:
    //   from	to	target	type
    //   146	271	274	java/lang/Exception
    //   146	271	296	finally
    //   276	280	296	finally
  }
  
  private ShortcutInfo getShortcutInfo(Cursor paramCursor, Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    ShortcutInfo localShortcutInfo = new ShortcutInfo();
    localShortcutInfo.itemType = 1;
    localShortcutInfo.title = paramCursor.getString(paramInt5);
    switch (paramCursor.getInt(paramInt1))
    {
    default: 
      localObject = getFallbackIcon();
      localShortcutInfo.usingFallbackIcon = true;
      localShortcutInfo.customIcon = false;
    }
    for (;;)
    {
      localShortcutInfo.setIcon((Bitmap)localObject);
      return localShortcutInfo;
      String str1 = paramCursor.getString(paramInt2);
      String str2 = paramCursor.getString(paramInt3);
      PackageManager localPackageManager = paramContext.getPackageManager();
      localShortcutInfo.customIcon = false;
      try
      {
        Resources localResources = localPackageManager.getResourcesForApplication(str1);
        localObject = null;
        if (localResources != null)
        {
          int i = localResources.getIdentifier(str2, null, null);
          Bitmap localBitmap = Utilities.createIconBitmap(this.mIconCache.getFullResIcon(localResources, i), paramContext);
          localObject = localBitmap;
        }
      }
      catch (Exception localException)
      {
        for (;;)
        {
          localObject = null;
        }
      }
      if (localObject == null) {
        localObject = getIconFromCursor(paramCursor, paramInt4, paramContext);
      }
      if (localObject == null)
      {
        localObject = getFallbackIcon();
        localShortcutInfo.usingFallbackIcon = true;
        continue;
        localObject = getIconFromCursor(paramCursor, paramInt4, paramContext);
        if (localObject == null)
        {
          localObject = getFallbackIcon();
          localShortcutInfo.customIcon = false;
          localShortcutInfo.usingFallbackIcon = true;
        }
        else
        {
          localShortcutInfo.customIcon = true;
        }
      }
    }
  }
  
  public static ArrayList<Object> getSortedWidgetsAndShortcuts(Context paramContext)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(AppWidgetManager.getInstance(paramContext).getInstalledProviders());
    localArrayList.addAll(localPackageManager.queryIntentActivities(new Intent("android.intent.action.CREATE_SHORTCUT"), 0));
    Collections.sort(localArrayList, new WidgetAndShortcutNameComparator(localPackageManager));
    return localArrayList;
  }
  
  private boolean isPackageDisabled(PackageManager paramPackageManager, String paramString)
  {
    try
    {
      boolean bool1 = paramPackageManager.getPackageInfo(paramString, 0).applicationInfo.enabled;
      boolean bool2 = false;
      if (!bool1) {
        bool2 = true;
      }
      return bool2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return false;
  }
  
  public static boolean isShortcutInfoUpdateable(ItemInfo paramItemInfo)
  {
    if ((paramItemInfo instanceof ShortcutInfo))
    {
      ShortcutInfo localShortcutInfo = (ShortcutInfo)paramItemInfo;
      Intent localIntent = localShortcutInfo.intent;
      ComponentName localComponentName = localIntent.getComponent();
      if ((localShortcutInfo.itemType == 0) && ("android.intent.action.MAIN".equals(localIntent.getAction())) && (localComponentName != null)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isValidPackageComponent(PackageManager paramPackageManager, ComponentName paramComponentName)
  {
    if (paramComponentName == null) {}
    for (;;)
    {
      return false;
      if (!isPackageDisabled(paramPackageManager, paramComponentName.getPackageName())) {
        try
        {
          paramPackageManager.getPackageInfo(paramComponentName.getPackageName(), 0);
          ActivityInfo localActivityInfo = paramPackageManager.getActivityInfo(paramComponentName, 0);
          if (localActivityInfo != null) {
            return true;
          }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
      }
    }
    return false;
  }
  
  private static TreeMap<Integer, Long> loadWorkspaceScreensDb(Context paramContext)
  {
    Cursor localCursor = paramContext.getContentResolver().query(LauncherSettings.WorkspaceScreens.CONTENT_URI, null, null, null, null);
    TreeMap localTreeMap = new TreeMap();
    try
    {
      int i = localCursor.getColumnIndexOrThrow("_id");
      int j = localCursor.getColumnIndexOrThrow("screenRank");
      for (;;)
      {
        boolean bool = localCursor.moveToNext();
        if (!bool) {
          break;
        }
        try
        {
          long l = localCursor.getLong(i);
          localTreeMap.put(Integer.valueOf(localCursor.getInt(j)), Long.valueOf(l));
        }
        catch (Exception localException)
        {
          Launcher.addDumpLog("Launcher.Model", "Desktop items loading interrupted - invalid screens: " + localException, true);
        }
      }
    }
    finally
    {
      localCursor.close();
    }
    return localTreeMap;
  }
  
  static void modifyItemInDatabase(Context paramContext, ItemInfo paramItemInfo, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramItemInfo.container = paramLong1;
    paramItemInfo.cellX = paramInt1;
    paramItemInfo.cellY = paramInt2;
    paramItemInfo.spanX = paramInt3;
    paramItemInfo.spanY = paramInt4;
    if (((paramContext instanceof Launcher)) && (paramLong2 < 0L) && (paramLong1 == -101L)) {}
    for (paramItemInfo.screenId = ((Launcher)paramContext).getHotseat().getOrderInHotseat(paramInt1, paramInt2);; paramItemInfo.screenId = paramLong2)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("container", Long.valueOf(paramItemInfo.container));
      localContentValues.put("cellX", Integer.valueOf(paramItemInfo.cellX));
      localContentValues.put("cellY", Integer.valueOf(paramItemInfo.cellY));
      localContentValues.put("spanX", Integer.valueOf(paramItemInfo.spanX));
      localContentValues.put("spanY", Integer.valueOf(paramItemInfo.spanY));
      localContentValues.put("screen", Long.valueOf(paramItemInfo.screenId));
      updateItemInDatabaseHelper(paramContext, localContentValues, paramItemInfo, "modifyItemInDatabase");
      return;
    }
  }
  
  static void moveItemInDatabase(Context paramContext, ItemInfo paramItemInfo, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    paramItemInfo.container = paramLong1;
    paramItemInfo.cellX = paramInt1;
    paramItemInfo.cellY = paramInt2;
    if (((paramContext instanceof Launcher)) && (paramLong2 < 0L) && (paramLong1 == -101L)) {}
    for (paramItemInfo.screenId = ((Launcher)paramContext).getHotseat().getOrderInHotseat(paramInt1, paramInt2);; paramItemInfo.screenId = paramLong2)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("container", Long.valueOf(paramItemInfo.container));
      localContentValues.put("cellX", Integer.valueOf(paramItemInfo.cellX));
      localContentValues.put("cellY", Integer.valueOf(paramItemInfo.cellY));
      localContentValues.put("screen", Long.valueOf(paramItemInfo.screenId));
      updateItemInDatabaseHelper(paramContext, localContentValues, paramItemInfo, "moveItemInDatabase");
      return;
    }
  }
  
  static void moveItemsInDatabase(Context paramContext, ArrayList<ItemInfo> paramArrayList, long paramLong, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayList.size();
    int j = 0;
    if (j < i)
    {
      ItemInfo localItemInfo = (ItemInfo)paramArrayList.get(j);
      localItemInfo.container = paramLong;
      if (((paramContext instanceof Launcher)) && (paramInt < 0) && (paramLong == -101L)) {}
      for (localItemInfo.screenId = ((Launcher)paramContext).getHotseat().getOrderInHotseat(localItemInfo.cellX, localItemInfo.cellY);; localItemInfo.screenId = paramInt)
      {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("container", Long.valueOf(localItemInfo.container));
        localContentValues.put("cellX", Integer.valueOf(localItemInfo.cellX));
        localContentValues.put("cellY", Integer.valueOf(localItemInfo.cellY));
        localContentValues.put("screen", Long.valueOf(localItemInfo.screenId));
        localArrayList.add(localContentValues);
        j++;
        break;
      }
    }
    updateItemsInDatabaseHelper(paramContext, localArrayList, paramArrayList, "moveItemInDatabase");
  }
  
  private void runOnMainThread(Runnable paramRunnable)
  {
    runOnMainThread(paramRunnable, 0);
  }
  
  private void runOnMainThread(Runnable paramRunnable, int paramInt)
  {
    if (sWorkerThread.getThreadId() == Process.myTid())
    {
      this.mHandler.post(paramRunnable);
      return;
    }
    paramRunnable.run();
  }
  
  private static void runOnWorkerThread(Runnable paramRunnable)
  {
    if (sWorkerThread.getThreadId() == Process.myTid())
    {
      paramRunnable.run();
      return;
    }
    sWorker.post(paramRunnable);
  }
  
  static boolean shortcutExists(Context paramContext, String paramString, Intent paramIntent)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri = LauncherSettings.Favorites.CONTENT_URI;
    String[] arrayOfString1 = { "title", "intent" };
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = paramString;
    arrayOfString2[1] = paramIntent.toUri(0);
    Cursor localCursor = localContentResolver.query(localUri, arrayOfString1, "title=? and intent=?", arrayOfString2, null);
    try
    {
      boolean bool = localCursor.moveToFirst();
      return bool;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  private boolean stopLoaderLocked()
  {
    LoaderTask localLoaderTask = this.mLoaderTask;
    boolean bool1 = false;
    if (localLoaderTask != null)
    {
      boolean bool2 = localLoaderTask.isLaunching();
      bool1 = false;
      if (bool2) {
        bool1 = true;
      }
      localLoaderTask.stopLocked();
    }
    return bool1;
  }
  
  static void updateItemArrays(ItemInfo paramItemInfo, long paramLong, StackTraceElement[] paramArrayOfStackTraceElement)
  {
    for (;;)
    {
      ItemInfo localItemInfo;
      synchronized (sBgLock)
      {
        checkItemInfoLocked(paramLong, paramItemInfo, paramArrayOfStackTraceElement);
        if ((paramItemInfo.container != -100L) && (paramItemInfo.container != -101L) && (!sBgFolders.containsKey(Long.valueOf(paramItemInfo.container)))) {
          Log.e("Launcher.Model", "item: " + paramItemInfo + " container being set to: " + paramItemInfo.container + ", not in the list of folders");
        }
        localItemInfo = (ItemInfo)sBgItemsIdMap.get(Long.valueOf(paramLong));
        if ((localItemInfo.container == -100L) || (localItemInfo.container == -101L))
        {
          switch (localItemInfo.itemType)
          {
          default: 
            return;
          }
          if (sBgWorkspaceItems.contains(localItemInfo)) {
            continue;
          }
          sBgWorkspaceItems.add(localItemInfo);
        }
      }
      sBgWorkspaceItems.remove(localItemInfo);
    }
  }
  
  static void updateItemInDatabase(Context paramContext, ItemInfo paramItemInfo)
  {
    ContentValues localContentValues = new ContentValues();
    paramItemInfo.onAddToDatabase(localContentValues);
    paramItemInfo.updateValuesWithCoordinates(localContentValues, paramItemInfo.cellX, paramItemInfo.cellY);
    updateItemInDatabaseHelper(paramContext, localContentValues, paramItemInfo, "updateItemInDatabase");
  }
  
  static void updateItemInDatabaseHelper(Context paramContext, final ContentValues paramContentValues, final ItemInfo paramItemInfo, String paramString)
  {
    final long l = paramItemInfo.id;
    final Uri localUri = LauncherSettings.Favorites.getContentUri(l, false);
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        this.val$cr.update(localUri, paramContentValues, null, null);
        LauncherModel.updateItemArrays(paramItemInfo, l, this.val$stackTrace);
      }
    });
  }
  
  static void updateItemsInDatabaseHelper(Context paramContext, final ArrayList<ContentValues> paramArrayList, ArrayList<ItemInfo> paramArrayList1, String paramString)
  {
    final ContentResolver localContentResolver = paramContext.getContentResolver();
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        ArrayList localArrayList = new ArrayList();
        int i = this.val$items.size();
        for (int j = 0; j < i; j++)
        {
          ItemInfo localItemInfo = (ItemInfo)this.val$items.get(j);
          long l = localItemInfo.id;
          Uri localUri = LauncherSettings.Favorites.getContentUri(l, false);
          ContentValues localContentValues = (ContentValues)paramArrayList.get(j);
          localArrayList.add(ContentProviderOperation.newUpdate(localUri).withValues(localContentValues).build());
          LauncherModel.updateItemArrays(localItemInfo, l, this.val$stackTrace);
        }
        try
        {
          localContentResolver.applyBatch("com.google.android.launcher.settings", localArrayList);
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    });
  }
  
  public void addAndBindAddedApps(final Context paramContext, final ArrayList<ItemInfo> paramArrayList, final Callbacks paramCallbacks, final ArrayList<AppInfo> paramArrayList1)
  {
    if ((paramArrayList.isEmpty()) && (paramArrayList1.isEmpty())) {
      return;
    }
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        final ArrayList localArrayList1 = new ArrayList();
        final ArrayList localArrayList2 = new ArrayList();
        ArrayList localArrayList3 = new ArrayList();
        TreeMap localTreeMap = LauncherModel.loadWorkspaceScreensDb(paramContext);
        Iterator localIterator1 = localTreeMap.keySet().iterator();
        while (localIterator1.hasNext()) {
          localArrayList3.add(Long.valueOf(((Long)localTreeMap.get((Integer)localIterator1.next())).longValue()));
        }
        for (;;)
        {
          ItemInfo localItemInfo;
          Pair localPair;
          synchronized (LauncherModel.sBgLock)
          {
            Iterator localIterator2 = paramArrayList.iterator();
            if (!localIterator2.hasNext()) {
              break label385;
            }
            localItemInfo = (ItemInfo)localIterator2.next();
            String str = localItemInfo.title.toString();
            Intent localIntent = localItemInfo.getIntent();
            if (LauncherModel.shortcutExists(paramContext, str, localIntent)) {
              continue;
            }
            if (!localArrayList3.isEmpty()) {
              break label435;
            }
            i = 0;
            localPair = LauncherModel.findNextAvailableIconSpace(paramContext, str, localIntent, i, localArrayList3);
            if (localPair == null)
            {
              LauncherProvider localLauncherProvider = LauncherAppState.getLauncherProvider();
              int j = Math.max(1, i + 1 - localArrayList3.size());
              if (j > 0)
              {
                long l = localLauncherProvider.generateNewScreenId();
                localArrayList3.add(Long.valueOf(l));
                localArrayList2.add(Long.valueOf(l));
                j--;
                continue;
              }
              localPair = LauncherModel.findNextAvailableIconSpace(paramContext, str, localIntent, i, localArrayList3);
            }
            if (localPair == null) {
              throw new RuntimeException("Coordinates should not be null");
            }
          }
          if ((localItemInfo instanceof ShortcutInfo)) {}
          for (ShortcutInfo localShortcutInfo = (ShortcutInfo)localItemInfo;; localShortcutInfo = ((AppInfo)localItemInfo).makeShortcut())
          {
            LauncherModel.addItemToDatabase(paramContext, localShortcutInfo, -100L, ((Long)localPair.first).longValue(), ((int[])localPair.second)[0], ((int[])localPair.second)[1], false);
            localArrayList1.add(localShortcutInfo);
            break;
            if (!(localItemInfo instanceof AppInfo)) {
              break label375;
            }
          }
          label375:
          throw new RuntimeException("Unexpected info type");
          label385:
          LauncherModel.this.updateWorkspaceScreenOrder(paramContext, localArrayList3);
          if ((!localArrayList1.isEmpty()) || (!paramArrayList1.isEmpty())) {
            LauncherModel.this.runOnMainThread(new Runnable()
            {
              public void run()
              {
                LauncherModel.Callbacks localCallbacks;
                ArrayList localArrayList1;
                ArrayList localArrayList2;
                long l;
                Iterator localIterator;
                if (LauncherModel.this.mCallbacks != null)
                {
                  localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
                  if ((LauncherModel.1.this.val$callbacks != localCallbacks) || (localCallbacks == null)) {
                    return;
                  }
                  localArrayList1 = new ArrayList();
                  localArrayList2 = new ArrayList();
                  if (!localArrayList1.isEmpty())
                  {
                    l = ((ItemInfo)localArrayList1.get(-1 + localArrayList1.size())).screenId;
                    localIterator = localArrayList1.iterator();
                  }
                }
                else
                {
                  for (;;)
                  {
                    if (!localIterator.hasNext()) {
                      break label162;
                    }
                    ItemInfo localItemInfo = (ItemInfo)localIterator.next();
                    if (localItemInfo.screenId == l)
                    {
                      localArrayList1.add(localItemInfo);
                      continue;
                      localCallbacks = null;
                      break;
                    }
                    localArrayList2.add(localItemInfo);
                  }
                }
                label162:
                LauncherModel.1.this.val$callbacks.bindAppsAdded(localArrayList2, localArrayList2, localArrayList1, LauncherModel.1.this.val$allAppsApps);
              }
            });
          }
          return;
          label435:
          int i = 1;
        }
      }
    });
  }
  
  public void addAndBindAddedApps(Context paramContext, ArrayList<ItemInfo> paramArrayList, ArrayList<AppInfo> paramArrayList1)
  {
    if (this.mCallbacks != null) {}
    for (Callbacks localCallbacks = (Callbacks)this.mCallbacks.get();; localCallbacks = null)
    {
      addAndBindAddedApps(paramContext, paramArrayList, localCallbacks, paramArrayList1);
      return;
    }
  }
  
  void bindRemainingSynchronousPages()
  {
    if (!mDeferredBindRunnables.isEmpty())
    {
      Iterator localIterator = mDeferredBindRunnables.iterator();
      while (localIterator.hasNext())
      {
        Runnable localRunnable = (Runnable)localIterator.next();
        this.mHandler.post(localRunnable, 1);
      }
      mDeferredBindRunnables.clear();
    }
  }
  
  public void dumpState()
  {
    Log.d("Launcher.Model", "mCallbacks=" + this.mCallbacks);
    AppInfo.dumpApplicationInfoList("Launcher.Model", "mAllAppsList.data", this.mBgAllAppsList.data);
    AppInfo.dumpApplicationInfoList("Launcher.Model", "mAllAppsList.added", this.mBgAllAppsList.added);
    AppInfo.dumpApplicationInfoList("Launcher.Model", "mAllAppsList.removed", this.mBgAllAppsList.removed);
    AppInfo.dumpApplicationInfoList("Launcher.Model", "mAllAppsList.modified", this.mBgAllAppsList.modified);
    if (this.mLoaderTask != null)
    {
      this.mLoaderTask.dumpState();
      return;
    }
    Log.d("Launcher.Model", "mLoaderTask=null");
  }
  
  void enqueuePackageUpdated(PackageUpdatedTask paramPackageUpdatedTask)
  {
    sWorker.post(paramPackageUpdatedTask);
  }
  
  public void flushWorkerThread()
  {
    this.mFlushingWorkerThread = true;
    for (;;)
    {
      synchronized (new Runnable()
      {
        public void run()
        {
          try
          {
            notifyAll();
            LauncherModel.access$302(LauncherModel.this, false);
            return;
          }
          finally {}
        }
      })
      {
        runOnWorkerThread(???);
        if (this.mLoaderTask != null) {}
        int i;
        synchronized (this.mLoaderTask)
        {
          this.mLoaderTask.notify();
          i = 0;
          if (i != 0) {}
        }
      }
      return;
    }
  }
  
  public Bitmap getFallbackIcon()
  {
    if (this.mDefaultIcon == null)
    {
      Context localContext = LauncherAppState.getInstance().getContext();
      this.mDefaultIcon = Utilities.createIconBitmap(this.mIconCache.getFullResDefaultActivityIcon(), localContext);
    }
    return Bitmap.createBitmap(this.mDefaultIcon);
  }
  
  FolderInfo getFolderById(Context paramContext, HashMap<Long, FolderInfo> paramHashMap, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri = LauncherSettings.Favorites.CONTENT_URI;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = String.valueOf(paramLong);
    arrayOfString[1] = String.valueOf(2);
    Cursor localCursor = localContentResolver.query(localUri, null, "_id=? and (itemType=? or itemType=?)", arrayOfString, null);
    try
    {
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getColumnIndexOrThrow("itemType");
        int j = localCursor.getColumnIndexOrThrow("title");
        int k = localCursor.getColumnIndexOrThrow("container");
        int m = localCursor.getColumnIndexOrThrow("screen");
        int n = localCursor.getColumnIndexOrThrow("cellX");
        int i1 = localCursor.getColumnIndexOrThrow("cellY");
        int i2 = localCursor.getInt(i);
        Object localObject2 = null;
        switch (i2)
        {
        }
        for (;;)
        {
          ((FolderInfo)localObject2).title = localCursor.getString(j);
          ((FolderInfo)localObject2).id = paramLong;
          ((FolderInfo)localObject2).container = localCursor.getInt(k);
          ((FolderInfo)localObject2).screenId = localCursor.getInt(m);
          ((FolderInfo)localObject2).cellX = localCursor.getInt(n);
          ((FolderInfo)localObject2).cellY = localCursor.getInt(i1);
          return localObject2;
          FolderInfo localFolderInfo = findOrMakeFolder(paramHashMap, paramLong);
          localObject2 = localFolderInfo;
        }
      }
      return null;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  Bitmap getIconFromCursor(Cursor paramCursor, int paramInt, Context paramContext)
  {
    byte[] arrayOfByte = paramCursor.getBlob(paramInt);
    try
    {
      Bitmap localBitmap = Utilities.createIconBitmap(BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length), paramContext);
      return localBitmap;
    }
    catch (Exception localException) {}
    return null;
  }
  
  public ShortcutInfo getShortcutInfo(PackageManager paramPackageManager, Intent paramIntent, Context paramContext)
  {
    return getShortcutInfo(paramPackageManager, paramIntent, paramContext, null, -1, -1, null);
  }
  
  public ShortcutInfo getShortcutInfo(PackageManager paramPackageManager, Intent paramIntent, Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2, HashMap<Object, CharSequence> paramHashMap)
  {
    ComponentName localComponentName1 = paramIntent.getComponent();
    ShortcutInfo localShortcutInfo = new ShortcutInfo();
    if ((localComponentName1 != null) && (!isValidPackageComponent(paramPackageManager, localComponentName1)))
    {
      Log.d("Launcher.Model", "Invalid package found in getShortcutInfo: " + localComponentName1);
      return null;
    }
    Object localObject;
    ComponentName localComponentName3;
    try
    {
      localShortcutInfo.initFlagsAndFirstInstallTime(paramPackageManager.getPackageInfo(localComponentName1.getPackageName(), 0));
      localObject = null;
      ComponentName localComponentName2 = paramIntent.getComponent();
      Intent localIntent = new Intent(paramIntent.getAction(), null);
      localIntent.addCategory("android.intent.category.LAUNCHER");
      localIntent.setPackage(localComponentName2.getPackageName());
      Iterator localIterator = paramPackageManager.queryIntentActivities(localIntent, 0).iterator();
      while (localIterator.hasNext())
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
        if (new ComponentName(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name).equals(localComponentName2)) {
          localObject = localResolveInfo;
        }
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Log.d("Launcher.Model", "getPackInfo failed for package " + localComponentName1.getPackageName());
      }
      if (localObject == null) {
        localObject = paramPackageManager.resolveActivity(paramIntent, 0);
      }
      Bitmap localBitmap = null;
      if (localObject != null) {
        localBitmap = this.mIconCache.getIcon(localComponentName1, (ResolveInfo)localObject, paramHashMap);
      }
      if ((localBitmap == null) && (paramCursor != null)) {
        localBitmap = getIconFromCursor(paramCursor, paramInt1, paramContext);
      }
      if (localBitmap == null)
      {
        localBitmap = getFallbackIcon();
        localShortcutInfo.usingFallbackIcon = true;
      }
      localShortcutInfo.setIcon(localBitmap);
      if (localObject != null)
      {
        localComponentName3 = getComponentNameFromResolveInfo((ResolveInfo)localObject);
        if ((paramHashMap == null) || (!paramHashMap.containsKey(localComponentName3))) {
          break label405;
        }
        localShortcutInfo.title = ((CharSequence)paramHashMap.get(localComponentName3));
      }
    }
    for (;;)
    {
      if ((localShortcutInfo.title == null) && (paramCursor != null)) {
        localShortcutInfo.title = paramCursor.getString(paramInt2);
      }
      if (localShortcutInfo.title == null) {
        localShortcutInfo.title = localComponentName1.getClassName();
      }
      localShortcutInfo.itemType = 0;
      return localShortcutInfo;
      label405:
      localShortcutInfo.title = ((ResolveInfo)localObject).activityInfo.loadLabel(paramPackageManager);
      if (paramHashMap != null) {
        paramHashMap.put(localComponentName3, localShortcutInfo.title);
      }
    }
  }
  
  ShortcutInfo infoFromShortcutIntent(Context paramContext, Intent paramIntent, Bitmap paramBitmap)
  {
    Intent localIntent = (Intent)paramIntent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
    String str = paramIntent.getStringExtra("android.intent.extra.shortcut.NAME");
    Parcelable localParcelable1 = paramIntent.getParcelableExtra("android.intent.extra.shortcut.ICON");
    if (localIntent == null)
    {
      Log.e("Launcher.Model", "Can't construct ShorcutInfo with null intent");
      return null;
    }
    Intent.ShortcutIconResource localShortcutIconResource = null;
    Object localObject;
    boolean bool1;
    ShortcutInfo localShortcutInfo;
    if ((localParcelable1 != null) && ((localParcelable1 instanceof Bitmap)))
    {
      localObject = Utilities.createIconBitmap(new FastBitmapDrawable((Bitmap)localParcelable1), paramContext);
      bool1 = true;
      localShortcutInfo = new ShortcutInfo();
      if (localObject == null)
      {
        if (paramBitmap == null) {
          break label288;
        }
        localObject = paramBitmap;
      }
    }
    for (;;)
    {
      for (;;)
      {
        localShortcutInfo.setIcon((Bitmap)localObject);
        localShortcutInfo.title = str;
        localShortcutInfo.intent = localIntent;
        localShortcutInfo.customIcon = bool1;
        localShortcutInfo.iconResource = localShortcutIconResource;
        return localShortcutInfo;
        Parcelable localParcelable2 = paramIntent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
        bool1 = false;
        localObject = null;
        localShortcutIconResource = null;
        if (localParcelable2 == null) {
          break;
        }
        boolean bool2 = localParcelable2 instanceof Intent.ShortcutIconResource;
        bool1 = false;
        localObject = null;
        localShortcutIconResource = null;
        if (!bool2) {
          break;
        }
        try
        {
          localShortcutIconResource = (Intent.ShortcutIconResource)localParcelable2;
          Resources localResources = paramContext.getPackageManager().getResourcesForApplication(localShortcutIconResource.packageName);
          int i = localResources.getIdentifier(localShortcutIconResource.resourceName, null, null);
          Bitmap localBitmap = Utilities.createIconBitmap(this.mIconCache.getFullResIcon(localResources, i), paramContext);
          localObject = localBitmap;
          bool1 = false;
        }
        catch (Exception localException)
        {
          Log.w("Launcher.Model", "Could not load shortcut icon: " + localParcelable2);
          bool1 = false;
          localObject = null;
        }
      }
      break;
      label288:
      localObject = getFallbackIcon();
      localShortcutInfo.usingFallbackIcon = true;
    }
  }
  
  public void initialize(Callbacks paramCallbacks)
  {
    synchronized (this.mLock)
    {
      this.mCallbacks = new WeakReference(paramCallbacks);
      return;
    }
  }
  
  boolean isLoadingWorkspace()
  {
    synchronized (this.mLock)
    {
      if (this.mLoaderTask != null)
      {
        boolean bool = this.mLoaderTask.isLoadingWorkspace();
        return bool;
      }
      return false;
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str1 = paramIntent.getAction();
    String str2;
    boolean bool1;
    if (("android.intent.action.PACKAGE_CHANGED".equals(str1)) || ("android.intent.action.PACKAGE_REMOVED".equals(str1)) || ("android.intent.action.PACKAGE_ADDED".equals(str1)))
    {
      str2 = paramIntent.getData().getSchemeSpecificPart();
      bool1 = paramIntent.getBooleanExtra("android.intent.extra.REPLACING", false);
      if ((str2 != null) && (str2.length() != 0)) {}
    }
    Callbacks localCallbacks;
    do
    {
      do
      {
        for (;;)
        {
          return;
          int i;
          if ("android.intent.action.PACKAGE_CHANGED".equals(str1)) {
            i = 2;
          }
          while (i != 0)
          {
            enqueuePackageUpdated(new PackageUpdatedTask(i, new String[] { str2 }));
            return;
            if ("android.intent.action.PACKAGE_REMOVED".equals(str1))
            {
              i = 0;
              if (!bool1) {
                i = 3;
              }
            }
            else
            {
              boolean bool2 = "android.intent.action.PACKAGE_ADDED".equals(str1);
              i = 0;
              if (bool2) {
                if (!bool1) {
                  i = 1;
                } else {
                  i = 2;
                }
              }
            }
          }
        }
        if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(str1))
        {
          enqueuePackageUpdated(new PackageUpdatedTask(1, paramIntent.getStringArrayExtra("android.intent.extra.changed_package_list")));
          startLoaderFromBackground();
          return;
        }
        if ("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(str1))
        {
          enqueuePackageUpdated(new PackageUpdatedTask(4, paramIntent.getStringArrayExtra("android.intent.extra.changed_package_list")));
          return;
        }
        if ("android.intent.action.LOCALE_CHANGED".equals(str1))
        {
          forceReload();
          return;
        }
        if ("android.intent.action.CONFIGURATION_CHANGED".equals(str1))
        {
          Configuration localConfiguration = paramContext.getResources().getConfiguration();
          if (this.mPreviousConfigMcc != localConfiguration.mcc)
          {
            Log.d("Launcher.Model", "Reload apps on config change. curr_mcc:" + localConfiguration.mcc + " prevmcc:" + this.mPreviousConfigMcc);
            forceReload();
          }
          this.mPreviousConfigMcc = localConfiguration.mcc;
          return;
        }
      } while (((!"android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED".equals(str1)) && (!"android.search.action.SEARCHABLES_CHANGED".equals(str1))) || (this.mCallbacks == null));
      localCallbacks = (Callbacks)this.mCallbacks.get();
    } while (localCallbacks == null);
    localCallbacks.bindSearchablesChanged();
  }
  
  boolean queueIconToBeChecked(HashMap<Object, byte[]> paramHashMap, ShortcutInfo paramShortcutInfo, Cursor paramCursor, int paramInt)
  {
    if (!this.mAppsCanBeOnRemoveableStorage) {}
    while ((paramShortcutInfo.customIcon) || (paramShortcutInfo.usingFallbackIcon)) {
      return false;
    }
    paramHashMap.put(paramShortcutInfo, paramCursor.getBlob(paramInt));
    return true;
  }
  
  public void resetLoadedState(boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (this.mLock)
    {
      stopLoaderLocked();
      if (paramBoolean1) {
        this.mAllAppsLoaded = false;
      }
      if (paramBoolean2) {
        this.mWorkspaceLoaded = false;
      }
      return;
    }
  }
  
  public void startLoader(boolean paramBoolean, int paramInt)
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        mDeferredBindRunnables.clear();
        if ((this.mCallbacks != null) && (this.mCallbacks.get() != null))
        {
          if (paramBoolean) {
            break label123;
          }
          if (!stopLoaderLocked()) {
            break label129;
          }
          break label123;
          this.mLoaderTask = new LoaderTask(this.mApp.getContext(), bool);
          if ((paramInt > -1) && (this.mAllAppsLoaded) && (this.mWorkspaceLoaded)) {
            this.mLoaderTask.runBindSynchronousPage(paramInt);
          }
        }
        else
        {
          return;
        }
        sWorkerThread.setPriority(5);
        sWorker.post(this.mLoaderTask);
      }
      label123:
      boolean bool = true;
      continue;
      label129:
      bool = false;
    }
  }
  
  public void startLoaderFromBackground()
  {
    WeakReference localWeakReference = this.mCallbacks;
    int i = 0;
    if (localWeakReference != null)
    {
      Callbacks localCallbacks = (Callbacks)this.mCallbacks.get();
      i = 0;
      if (localCallbacks != null)
      {
        boolean bool = localCallbacks.setLoadOnResume();
        i = 0;
        if (!bool) {
          i = 1;
        }
      }
    }
    if (i != 0) {
      startLoader(false, -1);
    }
  }
  
  public void stopLoader()
  {
    synchronized (this.mLock)
    {
      if (this.mLoaderTask != null) {
        this.mLoaderTask.stopLocked();
      }
      return;
    }
  }
  
  public void unbindItemInfosAndClearQueuedBindRunnables()
  {
    if (sWorkerThread.getThreadId() == Process.myTid()) {
      throw new RuntimeException("Expected unbindLauncherItemInfos() to be called from the main thread");
    }
    mDeferredBindRunnables.clear();
    this.mHandler.cancelAllRunnablesOfType(1);
    unbindWorkspaceItemsOnMainThread();
  }
  
  void unbindWorkspaceItemsOnMainThread()
  {
    final ArrayList localArrayList1 = new ArrayList();
    final ArrayList localArrayList2 = new ArrayList();
    synchronized (sBgLock)
    {
      localArrayList1.addAll(sBgWorkspaceItems);
      localArrayList2.addAll(sBgAppWidgets);
      runOnMainThread(new Runnable()
      {
        public void run()
        {
          Iterator localIterator1 = localArrayList1.iterator();
          while (localIterator1.hasNext()) {
            ((ItemInfo)localIterator1.next()).unbind();
          }
          Iterator localIterator2 = localArrayList2.iterator();
          while (localIterator2.hasNext()) {
            ((ItemInfo)localIterator2.next()).unbind();
          }
        }
      });
      return;
    }
  }
  
  void updateSavedIcon(Context paramContext, ShortcutInfo paramShortcutInfo, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {}
    for (;;)
    {
      try
      {
        boolean bool = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length).sameAs(paramShortcutInfo.getIcon(this.mIconCache));
        if (bool) {
          continue;
        }
        i = 1;
      }
      catch (Exception localException)
      {
        int i = 1;
        continue;
      }
      if (i != 0)
      {
        Log.d("Launcher.Model", "going to save icon bitmap for info=" + paramShortcutInfo);
        updateItemInDatabase(paramContext, paramShortcutInfo);
      }
      return;
      i = 0;
      continue;
      i = 1;
    }
  }
  
  void updateWorkspaceScreenOrder(Context paramContext, ArrayList<Long> paramArrayList)
  {
    final ArrayList localArrayList = new ArrayList(paramArrayList);
    final ContentResolver localContentResolver = paramContext.getContentResolver();
    final Uri localUri = LauncherSettings.WorkspaceScreens.CONTENT_URI;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext()) {
      if (((Long)localIterator.next()).longValue() < 0L) {
        localIterator.remove();
      }
    }
    runOnWorkerThread(new Runnable()
    {
      public void run()
      {
        localContentResolver.delete(localUri, null, null);
        int i = localArrayList.size();
        ContentValues[] arrayOfContentValues = new ContentValues[i];
        for (int j = 0; j < i; j++)
        {
          ContentValues localContentValues = new ContentValues();
          localContentValues.put("_id", Long.valueOf(((Long)localArrayList.get(j)).longValue()));
          localContentValues.put("screenRank", Integer.valueOf(j));
          arrayOfContentValues[j] = localContentValues;
        }
        localContentResolver.bulkInsert(localUri, arrayOfContentValues);
        synchronized (LauncherModel.sBgLock)
        {
          LauncherModel.sBgWorkspaceScreens.clear();
          LauncherModel.sBgWorkspaceScreens.addAll(localArrayList);
          return;
        }
      }
    });
  }
  
  public static abstract interface Callbacks
  {
    public abstract void bindAllApplications(ArrayList<AppInfo> paramArrayList);
    
    public abstract void bindAppWidget(LauncherAppWidgetInfo paramLauncherAppWidgetInfo);
    
    public abstract void bindAppsAdded(ArrayList<Long> paramArrayList, ArrayList<ItemInfo> paramArrayList1, ArrayList<ItemInfo> paramArrayList2, ArrayList<AppInfo> paramArrayList3);
    
    public abstract void bindAppsUpdated(ArrayList<AppInfo> paramArrayList);
    
    public abstract void bindComponentsRemoved(ArrayList<String> paramArrayList, ArrayList<AppInfo> paramArrayList1);
    
    public abstract void bindFolders(HashMap<Long, FolderInfo> paramHashMap);
    
    public abstract void bindItems(ArrayList<ItemInfo> paramArrayList, int paramInt1, int paramInt2, boolean paramBoolean);
    
    public abstract void bindPackagesUpdated(ArrayList<Object> paramArrayList);
    
    public abstract void bindScreens(ArrayList<Long> paramArrayList);
    
    public abstract void bindSearchablesChanged();
    
    public abstract void dumpLogsToLocalData();
    
    public abstract void finishBindingItems(boolean paramBoolean);
    
    public abstract int getCurrentWorkspaceScreen();
    
    public abstract boolean isAllAppsButtonRank(int paramInt);
    
    public abstract void onPageBoundSynchronously(int paramInt);
    
    public abstract boolean setLoadOnResume();
    
    public abstract void startBinding();
  }
  
  public static abstract interface ItemInfoFilter
  {
    public abstract boolean filterItem(ItemInfo paramItemInfo1, ItemInfo paramItemInfo2, ComponentName paramComponentName);
  }
  
  private class LoaderTask
    implements Runnable
  {
    private Context mContext;
    private boolean mIsLaunching;
    private boolean mIsLoadingAndBindingWorkspace;
    private HashMap<Object, CharSequence> mLabelCache;
    private boolean mLoadAndBindStepFinished;
    private boolean mStopped;
    
    LoaderTask(Context paramContext, boolean paramBoolean)
    {
      this.mContext = paramContext;
      this.mIsLaunching = paramBoolean;
      this.mLabelCache = new HashMap();
    }
    
    private void bindWorkspace(int paramInt, final boolean paramBoolean)
    {
      final long l = SystemClock.uptimeMillis();
      final LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
      if (localCallbacks == null)
      {
        Log.w("Launcher.Model", "LoaderTask running with no launcher");
        return;
      }
      int i;
      final int j;
      ArrayList localArrayList1;
      ArrayList localArrayList2;
      HashMap localHashMap1;
      HashMap localHashMap2;
      ArrayList localArrayList3;
      if (paramInt > -1)
      {
        i = 1;
        if (i == 0) {
          break label396;
        }
        j = paramInt;
        LauncherModel.this.unbindWorkspaceItemsOnMainThread();
        localArrayList1 = new ArrayList();
        localArrayList2 = new ArrayList();
        localHashMap1 = new HashMap();
        localHashMap2 = new HashMap();
        localArrayList3 = new ArrayList();
      }
      Runnable local10;
      for (;;)
      {
        synchronized (LauncherModel.sBgLock)
        {
          localArrayList1.addAll(LauncherModel.sBgWorkspaceItems);
          localArrayList2.addAll(LauncherModel.sBgAppWidgets);
          localHashMap1.putAll(LauncherModel.sBgFolders);
          localHashMap2.putAll(LauncherModel.sBgItemsIdMap);
          localArrayList3.addAll(LauncherModel.sBgWorkspaceScreens);
          ArrayList localArrayList4 = new ArrayList();
          ArrayList localArrayList5 = new ArrayList();
          ArrayList localArrayList6 = new ArrayList();
          ArrayList localArrayList7 = new ArrayList();
          HashMap localHashMap3 = new HashMap();
          HashMap localHashMap4 = new HashMap();
          filterCurrentWorkspaceItems(j, localArrayList1, localArrayList4, localArrayList5);
          filterCurrentAppWidgets(j, localArrayList2, localArrayList6, localArrayList7);
          filterCurrentFolders(j, localHashMap2, localHashMap1, localHashMap3, localHashMap4);
          sortWorkspaceItemsSpatially(localArrayList4);
          sortWorkspaceItemsSpatially(localArrayList5);
          Runnable local8 = new Runnable()
          {
            public void run()
            {
              LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(localCallbacks);
              if (localCallbacks != null) {
                localCallbacks.startBinding();
              }
            }
          };
          LauncherModel.this.runOnMainThread(local8, 1);
          bindWorkspaceScreens(localCallbacks, localArrayList3);
          bindWorkspaceItems(localCallbacks, localArrayList4, localArrayList6, localHashMap3, null);
          if (i != 0)
          {
            Runnable local9 = new Runnable()
            {
              public void run()
              {
                LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(localCallbacks);
                if (localCallbacks != null) {
                  localCallbacks.onPageBoundSynchronously(j);
                }
              }
            };
            LauncherModel.this.runOnMainThread(local9, 1);
          }
          LauncherModel.mDeferredBindRunnables.clear();
          if (i != 0)
          {
            localArrayList8 = LauncherModel.mDeferredBindRunnables;
            bindWorkspaceItems(localCallbacks, localArrayList5, localArrayList7, localHashMap4, localArrayList8);
            local10 = new Runnable()
            {
              public void run()
              {
                LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(localCallbacks);
                if (localCallbacks != null) {
                  localCallbacks.finishBindingItems(paramBoolean);
                }
                LauncherModel.LoaderTask.access$1802(LauncherModel.LoaderTask.this, false);
              }
            };
            if (i == 0) {
              break label422;
            }
            LauncherModel.mDeferredBindRunnables.add(local10);
            return;
            i = 0;
            break;
            label396:
            j = localCallbacks.getCurrentWorkspaceScreen();
          }
        }
        ArrayList localArrayList8 = null;
      }
      label422:
      LauncherModel.this.runOnMainThread(local10, 1);
    }
    
    private void bindWorkspaceItems(final LauncherModel.Callbacks paramCallbacks, final ArrayList<ItemInfo> paramArrayList, ArrayList<LauncherAppWidgetInfo> paramArrayList1, final HashMap<Long, FolderInfo> paramHashMap, ArrayList<Runnable> paramArrayList2)
    {
      int i;
      int j;
      int k;
      label17:
      final int i2;
      label42:
      Runnable local5;
      if (paramArrayList2 != null)
      {
        i = 1;
        j = paramArrayList.size();
        k = 0;
        if (k >= j) {
          break label106;
        }
        final int i1 = k;
        if (k + 6 > j) {
          break label83;
        }
        i2 = 6;
        local5 = new Runnable()
        {
          public void run()
          {
            LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(paramCallbacks);
            if (localCallbacks != null) {
              localCallbacks.bindItems(paramArrayList, i1, i1 + i2, false);
            }
          }
        };
        if (i == 0) {
          break label93;
        }
        paramArrayList2.add(local5);
      }
      for (;;)
      {
        k += 6;
        break label17;
        i = 0;
        break;
        label83:
        i2 = j - k;
        break label42;
        label93:
        LauncherModel.this.runOnMainThread(local5, 1);
      }
      label106:
      Runnable local6;
      int n;
      label149:
      Runnable local7;
      if (!paramHashMap.isEmpty())
      {
        local6 = new Runnable()
        {
          public void run()
          {
            LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(paramCallbacks);
            if (localCallbacks != null) {
              localCallbacks.bindFolders(paramHashMap);
            }
          }
        };
        if (i != 0) {
          paramArrayList2.add(local6);
        }
      }
      else
      {
        int m = paramArrayList1.size();
        n = 0;
        if (n >= m) {
          return;
        }
        local7 = new Runnable()
        {
          public void run()
          {
            LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(paramCallbacks);
            if (localCallbacks != null) {
              localCallbacks.bindAppWidget(this.val$widget);
            }
          }
        };
        if (i == 0) {
          break label208;
        }
        paramArrayList2.add(local7);
      }
      for (;;)
      {
        n++;
        break label149;
        LauncherModel.this.runOnMainThread(local6, 1);
        break;
        label208:
        LauncherModel.this.runOnMainThread(local7, 1);
      }
    }
    
    private void bindWorkspaceScreens(final LauncherModel.Callbacks paramCallbacks, final ArrayList<Long> paramArrayList)
    {
      Runnable local4 = new Runnable()
      {
        public void run()
        {
          LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(paramCallbacks);
          if (localCallbacks != null) {
            localCallbacks.bindScreens(paramArrayList);
          }
        }
      };
      LauncherModel.this.runOnMainThread(local4, 1);
    }
    
    private boolean checkItemDimensions(ItemInfo paramItemInfo)
    {
      DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
      return (paramItemInfo.cellX + paramItemInfo.spanX > (int)localDeviceProfile.numColumns) || (paramItemInfo.cellY + paramItemInfo.spanY > (int)localDeviceProfile.numRows);
    }
    
    private boolean checkItemPlacement(HashMap<Long, ItemInfo[][]> paramHashMap, ItemInfo paramItemInfo, AtomicBoolean paramAtomicBoolean)
    {
      DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
      int i = (int)localDeviceProfile.numColumns;
      int j = (int)localDeviceProfile.numRows;
      long l = paramItemInfo.screenId;
      if (paramItemInfo.container == -101L)
      {
        if ((LauncherModel.this.mCallbacks == null) || (((LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get()).isAllAppsButtonRank((int)paramItemInfo.screenId)))
        {
          paramAtomicBoolean.set(true);
          return false;
        }
        if (paramHashMap.containsKey(Integer.valueOf(-101)))
        {
          if (((ItemInfo[][])paramHashMap.get(Integer.valueOf(-101)))[((int)paramItemInfo.screenId)][0] != null)
          {
            Log.e("Launcher.Model", "Error loading shortcut into hotseat " + paramItemInfo + " into position (" + paramItemInfo.screenId + ":" + paramItemInfo.cellX + "," + paramItemInfo.cellY + ") occupied by " + ((ItemInfo[][])paramHashMap.get(Integer.valueOf(-101)))[((int)paramItemInfo.screenId)][0]);
            return false;
          }
        }
        else
        {
          ItemInfo[][] arrayOfItemInfo3 = (ItemInfo[][])Array.newInstance(ItemInfo.class, new int[] { i + 1, j + 1 });
          arrayOfItemInfo3[((int)paramItemInfo.screenId)][0] = paramItemInfo;
          paramHashMap.put(Long.valueOf(-101L), arrayOfItemInfo3);
          return true;
        }
      }
      else if (paramItemInfo.container != -100L)
      {
        return true;
      }
      if (!paramHashMap.containsKey(Long.valueOf(paramItemInfo.screenId)))
      {
        ItemInfo[][] arrayOfItemInfo2 = (ItemInfo[][])Array.newInstance(ItemInfo.class, new int[] { i + 1, j + 1 });
        paramHashMap.put(Long.valueOf(paramItemInfo.screenId), arrayOfItemInfo2);
      }
      ItemInfo[][] arrayOfItemInfo1 = (ItemInfo[][])paramHashMap.get(Long.valueOf(paramItemInfo.screenId));
      for (int k = paramItemInfo.cellX; k < paramItemInfo.cellX + paramItemInfo.spanX; k++) {
        for (int i1 = paramItemInfo.cellY; i1 < paramItemInfo.cellY + paramItemInfo.spanY; i1++) {
          if (arrayOfItemInfo1[k][i1] != null)
          {
            Log.e("Launcher.Model", "Error loading shortcut " + paramItemInfo + " into cell (" + l + "-" + paramItemInfo.screenId + ":" + k + "," + i1 + ") occupied by " + arrayOfItemInfo1[k][i1]);
            return false;
          }
        }
      }
      for (int m = paramItemInfo.cellX; m < paramItemInfo.cellX + paramItemInfo.spanX; m++) {
        for (int n = paramItemInfo.cellY; n < paramItemInfo.cellY + paramItemInfo.spanY; n++) {
          arrayOfItemInfo1[m][n] = paramItemInfo;
        }
      }
      return true;
    }
    
    private void clearSBgDataStructures()
    {
      synchronized (LauncherModel.sBgLock)
      {
        LauncherModel.sBgWorkspaceItems.clear();
        LauncherModel.sBgAppWidgets.clear();
        LauncherModel.sBgFolders.clear();
        LauncherModel.sBgItemsIdMap.clear();
        LauncherModel.sBgDbIconCache.clear();
        LauncherModel.sBgWorkspaceScreens.clear();
        return;
      }
    }
    
    private void filterCurrentAppWidgets(int paramInt, ArrayList<LauncherAppWidgetInfo> paramArrayList1, ArrayList<LauncherAppWidgetInfo> paramArrayList2, ArrayList<LauncherAppWidgetInfo> paramArrayList3)
    {
      if (paramInt < 0) {
        paramArrayList2.addAll(paramArrayList1);
      }
      Iterator localIterator = paramArrayList1.iterator();
      while (localIterator.hasNext())
      {
        LauncherAppWidgetInfo localLauncherAppWidgetInfo = (LauncherAppWidgetInfo)localIterator.next();
        if (localLauncherAppWidgetInfo != null) {
          if ((localLauncherAppWidgetInfo.container == -100L) && (localLauncherAppWidgetInfo.screenId == paramInt)) {
            paramArrayList2.add(localLauncherAppWidgetInfo);
          } else {
            paramArrayList3.add(localLauncherAppWidgetInfo);
          }
        }
      }
    }
    
    private void filterCurrentFolders(int paramInt, HashMap<Long, ItemInfo> paramHashMap, HashMap<Long, FolderInfo> paramHashMap1, HashMap<Long, FolderInfo> paramHashMap2, HashMap<Long, FolderInfo> paramHashMap3)
    {
      if (paramInt < 0) {
        paramHashMap2.putAll(paramHashMap1);
      }
      Iterator localIterator = paramHashMap1.keySet().iterator();
      while (localIterator.hasNext())
      {
        long l = ((Long)localIterator.next()).longValue();
        ItemInfo localItemInfo = (ItemInfo)paramHashMap.get(Long.valueOf(l));
        FolderInfo localFolderInfo = (FolderInfo)paramHashMap1.get(Long.valueOf(l));
        if ((localItemInfo != null) && (localFolderInfo != null)) {
          if ((localItemInfo.container == -100L) && (localItemInfo.screenId == paramInt)) {
            paramHashMap2.put(Long.valueOf(l), localFolderInfo);
          } else {
            paramHashMap3.put(Long.valueOf(l), localFolderInfo);
          }
        }
      }
    }
    
    private void filterCurrentWorkspaceItems(int paramInt, ArrayList<ItemInfo> paramArrayList1, ArrayList<ItemInfo> paramArrayList2, ArrayList<ItemInfo> paramArrayList3)
    {
      Iterator localIterator1 = paramArrayList1.iterator();
      while (localIterator1.hasNext()) {
        if ((ItemInfo)localIterator1.next() == null) {
          localIterator1.remove();
        }
      }
      if (paramInt < 0) {
        paramArrayList2.addAll(paramArrayList1);
      }
      HashSet localHashSet = new HashSet();
      Collections.sort(paramArrayList1, new Comparator()
      {
        public int compare(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2)
        {
          return (int)(paramAnonymousItemInfo1.container - paramAnonymousItemInfo2.container);
        }
      });
      Iterator localIterator2 = paramArrayList1.iterator();
      while (localIterator2.hasNext())
      {
        ItemInfo localItemInfo = (ItemInfo)localIterator2.next();
        if (localItemInfo.container == -100L)
        {
          if (localItemInfo.screenId == paramInt)
          {
            paramArrayList2.add(localItemInfo);
            localHashSet.add(Long.valueOf(localItemInfo.id));
          }
          else
          {
            paramArrayList3.add(localItemInfo);
          }
        }
        else if (localItemInfo.container == -101L)
        {
          paramArrayList2.add(localItemInfo);
          localHashSet.add(Long.valueOf(localItemInfo.id));
        }
        else if (localHashSet.contains(Long.valueOf(localItemInfo.container)))
        {
          paramArrayList2.add(localItemInfo);
          localHashSet.add(Long.valueOf(localItemInfo.id));
        }
        else
        {
          paramArrayList3.add(localItemInfo);
        }
      }
    }
    
    private void loadAllApps()
    {
      final LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
      if (localCallbacks == null) {
        Log.w("Launcher.Model", "LoaderTask running with no launcher (loadAllApps)");
      }
      PackageManager localPackageManager;
      List localList;
      do
      {
        return;
        localPackageManager = this.mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN", null);
        localIntent.addCategory("android.intent.category.LAUNCHER");
        LauncherModel.this.mBgAllAppsList.clear();
        localList = localPackageManager.queryIntentActivities(localIntent, 0);
      } while ((localList == null) || (localList.isEmpty()));
      LauncherModel.ShortcutNameComparator localShortcutNameComparator = new LauncherModel.ShortcutNameComparator(localPackageManager, this.mLabelCache);
      Collections.sort(localList, localShortcutNameComparator);
      for (int i = 0; i < localList.size(); i++)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localList.get(i);
        AllAppsList localAllAppsList = LauncherModel.this.mBgAllAppsList;
        AppInfo localAppInfo = new AppInfo(localPackageManager, localResolveInfo, LauncherModel.this.mIconCache, this.mLabelCache);
        localAllAppsList.add(localAppInfo);
      }
      final ArrayList localArrayList = LauncherModel.this.mBgAllAppsList.added;
      LauncherModel.this.mBgAllAppsList.added = new ArrayList();
      DeferredHandler localDeferredHandler = LauncherModel.this.mHandler;
      Runnable local12 = new Runnable()
      {
        public void run()
        {
          SystemClock.uptimeMillis();
          LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(localCallbacks);
          if (localCallbacks != null)
          {
            localCallbacks.bindAllApplications(localArrayList);
            return;
          }
          Log.i("Launcher.Model", "not binding apps: no Launcher activity");
        }
      };
      localDeferredHandler.post(local12);
    }
    
    private void loadAndBindAllApps()
    {
      if (!LauncherModel.this.mAllAppsLoaded)
      {
        loadAllApps();
        try
        {
          if (this.mStopped) {
            return;
          }
          LauncherModel.access$702(LauncherModel.this, true);
          return;
        }
        finally {}
      }
      onlyBindAllApps();
    }
    
    private boolean loadAndBindWorkspace()
    {
      this.mIsLoadingAndBindingWorkspace = true;
      boolean bool1 = LauncherModel.this.mWorkspaceLoaded;
      boolean bool2 = false;
      if (!bool1) {
        bool2 = loadWorkspace();
      }
      try
      {
        if (this.mStopped) {
          return bool2;
        }
        LauncherModel.access$402(LauncherModel.this, true);
        bindWorkspace(-1, bool2);
        return bool2;
      }
      finally {}
    }
    
    private boolean loadWorkspace()
    {
      Context localContext = this.mContext;
      ContentResolver localContentResolver = localContext.getContentResolver();
      PackageManager localPackageManager = localContext.getPackageManager();
      AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(localContext);
      boolean bool1 = localPackageManager.isSafeMode();
      DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
      ((int)localDeviceProfile.numColumns);
      ((int)localDeviceProfile.numRows);
      LauncherAppState.getLauncherProvider().loadDefaultFavoritesIfNecessary(0);
      boolean bool2 = LauncherAppState.getLauncherProvider().justLoadedOldDb();
      label575:
      label605:
      int i16;
      int i15;
      for (;;)
      {
        ArrayList localArrayList1;
        Uri localUri;
        Cursor localCursor;
        HashMap localHashMap;
        int i;
        int k;
        int m;
        int n;
        int i1;
        int i2;
        int i3;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        AtomicBoolean localAtomicBoolean;
        int i12;
        long l9;
        Intent localIntent;
        ComponentName localComponentName;
        synchronized (LauncherModel.sBgLock)
        {
          clearSBgDataStructures();
          localArrayList1 = new ArrayList();
          localUri = LauncherSettings.Favorites.CONTENT_URI;
          localCursor = localContentResolver.query(localUri, null, null, null, null);
          localHashMap = new HashMap();
          try
          {
            i = localCursor.getColumnIndexOrThrow("_id");
            int j = localCursor.getColumnIndexOrThrow("intent");
            k = localCursor.getColumnIndexOrThrow("title");
            m = localCursor.getColumnIndexOrThrow("iconType");
            n = localCursor.getColumnIndexOrThrow("icon");
            i1 = localCursor.getColumnIndexOrThrow("iconPackage");
            i2 = localCursor.getColumnIndexOrThrow("iconResource");
            i3 = localCursor.getColumnIndexOrThrow("container");
            int i4 = localCursor.getColumnIndexOrThrow("itemType");
            i5 = localCursor.getColumnIndexOrThrow("appWidgetId");
            i6 = localCursor.getColumnIndexOrThrow("appWidgetProvider");
            i7 = localCursor.getColumnIndexOrThrow("screen");
            i8 = localCursor.getColumnIndexOrThrow("cellX");
            i9 = localCursor.getColumnIndexOrThrow("cellY");
            i10 = localCursor.getColumnIndexOrThrow("spanX");
            i11 = localCursor.getColumnIndexOrThrow("spanY");
            if ((this.mStopped) || (!localCursor.moveToNext())) {
              break label1725;
            }
            localAtomicBoolean = new AtomicBoolean(false);
            try
            {
              i12 = localCursor.getInt(i4);
              switch (i12)
              {
              case 0: 
              case 1: 
                l9 = localCursor.getLong(i);
                String str4 = localCursor.getString(j);
                try
                {
                  localIntent = Intent.parseUri(str4, 0);
                  localComponentName = localIntent.getComponent();
                  if ((localComponentName == null) || (LauncherModel.this.isValidPackageComponent(localPackageManager, localComponentName))) {
                    break label605;
                  }
                  if (LauncherModel.this.mAppsCanBeOnRemoveableStorage) {
                    break label575;
                  }
                  Launcher.addDumpLog("Launcher.Model", "Invalid package removed: " + localComponentName, true);
                  localArrayList1.add(Long.valueOf(l9));
                }
                catch (URISyntaxException localURISyntaxException)
                {
                  Launcher.addDumpLog("Launcher.Model", "Invalid uri: " + str4, true);
                }
              }
            }
            catch (Exception localException)
            {
              Launcher.addDumpLog("Launcher.Model", "Desktop items loading interrupted: " + localException, true);
            }
            continue;
            localObject2 = finally;
          }
          finally
          {
            if (localCursor != null) {
              localCursor.close();
            }
          }
        }
        Launcher.addDumpLog("Launcher.Model", "Invalid package found: " + localComponentName, true);
        continue;
        ShortcutInfo localShortcutInfo;
        if (i12 == 0) {
          localShortcutInfo = LauncherModel.this.getShortcutInfo(localPackageManager, localIntent, localContext, localCursor, n, k, this.mLabelCache);
        }
        for (;;)
        {
          if (localShortcutInfo != null)
          {
            localShortcutInfo.id = l9;
            localShortcutInfo.intent = localIntent;
            i16 = localCursor.getInt(i3);
            long l10 = i16;
            localShortcutInfo.container = l10;
            long l11 = localCursor.getInt(i7);
            localShortcutInfo.screenId = l11;
            int i17 = localCursor.getInt(i8);
            localShortcutInfo.cellX = i17;
            int i18 = localCursor.getInt(i9);
            localShortcutInfo.cellY = i18;
            localShortcutInfo.spanX = 1;
            localShortcutInfo.spanY = 1;
            if ((i16 == -100) && (checkItemDimensions(localShortcutInfo)))
            {
              Launcher.addDumpLog("Launcher.Model", "Skipped loading out of bounds shortcut: " + localShortcutInfo + ", " + localDeviceProfile.numColumns + "x" + localDeviceProfile.numRows, true);
              break;
              localShortcutInfo = LauncherModel.this.getShortcutInfo(localCursor, localContext, m, i1, i2, n, k);
              if ((localIntent.getAction() == null) || (localIntent.getCategories() == null) || (!localIntent.getAction().equals("android.intent.action.MAIN")) || (!localIntent.getCategories().contains("android.intent.category.LAUNCHER"))) {
                continue;
              }
              localIntent.addFlags(270532608);
              continue;
            }
            localAtomicBoolean.set(false);
            if (checkItemPlacement(localHashMap, localShortcutInfo, localAtomicBoolean)) {
              break label2233;
            }
            if (!localAtomicBoolean.get()) {
              break;
            }
            localArrayList1.add(Long.valueOf(l9));
            break;
            LauncherModel.findOrMakeFolder(LauncherModel.sBgFolders, i16).add(localShortcutInfo);
            for (;;)
            {
              LauncherModel.sBgItemsIdMap.put(Long.valueOf(localShortcutInfo.id), localShortcutInfo);
              LauncherModel.this.queueIconToBeChecked(LauncherModel.sBgDbIconCache, localShortcutInfo, localCursor, n);
              break;
              LauncherModel.sBgWorkspaceItems.add(localShortcutInfo);
            }
          }
        }
        throw new RuntimeException("Unexpected null ShortcutInfo");
        long l8 = localCursor.getLong(i);
        FolderInfo localFolderInfo = LauncherModel.findOrMakeFolder(LauncherModel.sBgFolders, l8);
        localFolderInfo.title = localCursor.getString(k);
        localFolderInfo.id = l8;
        i15 = localCursor.getInt(i3);
        localFolderInfo.container = i15;
        localFolderInfo.screenId = localCursor.getInt(i7);
        localFolderInfo.cellX = localCursor.getInt(i8);
        localFolderInfo.cellY = localCursor.getInt(i9);
        localFolderInfo.spanX = 1;
        localFolderInfo.spanY = 1;
        if ((i15 == -100) && (checkItemDimensions(localFolderInfo)))
        {
          Log.d("Launcher.Model", "Skipped loading out of bounds folder");
        }
        else
        {
          localAtomicBoolean.set(false);
          if (checkItemPlacement(localHashMap, localFolderInfo, localAtomicBoolean)) {
            break label2256;
          }
          if (localAtomicBoolean.get())
          {
            localArrayList1.add(Long.valueOf(l8));
            continue;
            for (;;)
            {
              LauncherModel.sBgItemsIdMap.put(Long.valueOf(localFolderInfo.id), localFolderInfo);
              LauncherModel.sBgFolders.put(Long.valueOf(localFolderInfo.id), localFolderInfo);
              break;
              LauncherModel.sBgWorkspaceItems.add(localFolderInfo);
            }
            int i13 = localCursor.getInt(i5);
            String str1 = localCursor.getString(i6);
            long l7 = localCursor.getLong(i);
            AppWidgetProviderInfo localAppWidgetProviderInfo = localAppWidgetManager.getAppWidgetInfo(i13);
            if ((!bool1) && ((localAppWidgetProviderInfo == null) || (localAppWidgetProviderInfo.provider == null) || (localAppWidgetProviderInfo.provider.getPackageName() == null)))
            {
              String str3 = "Deleting widget that isn't installed anymore: id=" + l7 + " appWidgetId=" + i13;
              Log.e("Launcher.Model", str3);
              Launcher.addDumpLog("Launcher.Model", str3, false);
              localArrayList1.add(Long.valueOf(l7));
            }
            else
            {
              LauncherAppWidgetInfo localLauncherAppWidgetInfo = new LauncherAppWidgetInfo(i13, localAppWidgetProviderInfo.provider);
              localLauncherAppWidgetInfo.id = l7;
              localLauncherAppWidgetInfo.screenId = localCursor.getInt(i7);
              localLauncherAppWidgetInfo.cellX = localCursor.getInt(i8);
              localLauncherAppWidgetInfo.cellY = localCursor.getInt(i9);
              localLauncherAppWidgetInfo.spanX = localCursor.getInt(i10);
              localLauncherAppWidgetInfo.spanY = localCursor.getInt(i11);
              int[] arrayOfInt = Launcher.getMinSpanForWidget(localContext, localAppWidgetProviderInfo);
              localLauncherAppWidgetInfo.minSpanX = arrayOfInt[0];
              localLauncherAppWidgetInfo.minSpanY = arrayOfInt[1];
              int i14 = localCursor.getInt(i3);
              if ((i14 != -100) && (i14 != -101))
              {
                Log.e("Launcher.Model", "Widget found where container != CONTAINER_DESKTOP nor CONTAINER_HOTSEAT - ignoring!");
              }
              else
              {
                localLauncherAppWidgetInfo.container = localCursor.getInt(i3);
                if ((i14 == -100) && (checkItemDimensions(localLauncherAppWidgetInfo)))
                {
                  Log.d("Launcher.Model", "Skipped loading out of bounds app widget");
                }
                else
                {
                  localAtomicBoolean.set(false);
                  if (!checkItemPlacement(localHashMap, localLauncherAppWidgetInfo, localAtomicBoolean))
                  {
                    if (localAtomicBoolean.get()) {
                      localArrayList1.add(Long.valueOf(l7));
                    }
                  }
                  else
                  {
                    String str2 = localAppWidgetProviderInfo.provider.flattenToString();
                    if (!str2.equals(str1))
                    {
                      ContentValues localContentValues = new ContentValues();
                      localContentValues.put("appWidgetProvider", str2);
                      String[] arrayOfString = new String[1];
                      arrayOfString[0] = Integer.toString(localCursor.getInt(i));
                      localContentResolver.update(localUri, localContentValues, "_id= ?", arrayOfString);
                    }
                    LauncherModel.sBgItemsIdMap.put(Long.valueOf(localLauncherAppWidgetInfo.id), localLauncherAppWidgetInfo);
                    LauncherModel.sBgAppWidgets.add(localLauncherAppWidgetInfo);
                    continue;
                    label1725:
                    if (localCursor != null) {
                      localCursor.close();
                    }
                    if (this.mStopped)
                    {
                      clearSBgDataStructures();
                      return false;
                    }
                    if (localArrayList1.size() > 0)
                    {
                      ContentProviderClient localContentProviderClient = localContentResolver.acquireContentProviderClient(LauncherSettings.Favorites.CONTENT_URI);
                      Iterator localIterator5 = localArrayList1.iterator();
                      while (localIterator5.hasNext())
                      {
                        long l6 = ((Long)localIterator5.next()).longValue();
                        try
                        {
                          localContentProviderClient.delete(LauncherSettings.Favorites.getContentUri(l6, false), null, null);
                        }
                        catch (RemoteException localRemoteException)
                        {
                          Log.w("Launcher.Model", "Could not remove id = " + l6);
                        }
                      }
                    }
                    if (bool2)
                    {
                      long l2 = 0L;
                      Iterator localIterator3 = LauncherModel.sBgItemsIdMap.values().iterator();
                      while (localIterator3.hasNext())
                      {
                        ItemInfo localItemInfo2 = (ItemInfo)localIterator3.next();
                        long l5 = localItemInfo2.screenId;
                        if ((localItemInfo2.container == -100L) && (!LauncherModel.sBgWorkspaceScreens.contains(Long.valueOf(l5))))
                        {
                          LauncherModel.sBgWorkspaceScreens.add(Long.valueOf(l5));
                          if (l5 > l2) {
                            l2 = l5;
                          }
                        }
                      }
                      Collections.sort(LauncherModel.sBgWorkspaceScreens);
                      LauncherAppState.getLauncherProvider().updateMaxScreenId(l2);
                      LauncherModel.this.updateWorkspaceScreenOrder(localContext, LauncherModel.sBgWorkspaceScreens);
                      long l3 = 0L;
                      Iterator localIterator4 = LauncherModel.sBgItemsIdMap.values().iterator();
                      while (localIterator4.hasNext())
                      {
                        long l4 = ((ItemInfo)localIterator4.next()).id;
                        l3 = Math.max(l3, l4);
                      }
                      LauncherAppState.getLauncherProvider().updateMaxItemId(l3);
                    }
                    for (;;)
                    {
                      return bool2;
                      TreeMap localTreeMap = LauncherModel.loadWorkspaceScreensDb(this.mContext);
                      Iterator localIterator1 = localTreeMap.keySet().iterator();
                      while (localIterator1.hasNext())
                      {
                        Integer localInteger = (Integer)localIterator1.next();
                        LauncherModel.sBgWorkspaceScreens.add(localTreeMap.get(localInteger));
                      }
                      ArrayList localArrayList2 = new ArrayList(LauncherModel.sBgWorkspaceScreens);
                      Iterator localIterator2 = LauncherModel.sBgItemsIdMap.values().iterator();
                      while (localIterator2.hasNext())
                      {
                        ItemInfo localItemInfo1 = (ItemInfo)localIterator2.next();
                        long l1 = localItemInfo1.screenId;
                        if ((localItemInfo1.container == -100L) && (localArrayList2.contains(Long.valueOf(l1)))) {
                          localArrayList2.remove(Long.valueOf(l1));
                        }
                      }
                      if (localArrayList2.size() != 0)
                      {
                        LauncherModel.sBgWorkspaceScreens.removeAll(localArrayList2);
                        LauncherModel.this.updateWorkspaceScreenOrder(localContext, LauncherModel.sBgWorkspaceScreens);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      label2233:
      switch (i16)
      {
      }
      label2256:
      switch (i15)
      {
      }
    }
    
    private void onlyBindAllApps()
    {
      final LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
      if (localCallbacks == null)
      {
        Log.w("Launcher.Model", "LoaderTask running with no launcher (onlyBindAllApps)");
        return;
      }
      Runnable local11 = new Runnable()
      {
        public void run()
        {
          SystemClock.uptimeMillis();
          LauncherModel.Callbacks localCallbacks = LauncherModel.LoaderTask.this.tryGetCallbacks(localCallbacks);
          if (localCallbacks != null) {
            localCallbacks.bindAllApplications(this.val$list);
          }
        }
      };
      if (LauncherModel.sWorkerThread.getThreadId() != Process.myTid()) {}
      for (int i = 1; i != 0; i = 0)
      {
        local11.run();
        return;
      }
      LauncherModel.this.mHandler.post(local11);
    }
    
    private void sortWorkspaceItemsSpatially(ArrayList<ItemInfo> paramArrayList)
    {
      Collections.sort(paramArrayList, new Comparator()
      {
        public int compare(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2)
        {
          int i = (int)this.val$grid.numColumns;
          int j = i * (int)this.val$grid.numRows;
          int k = j * 6;
          return (int)(paramAnonymousItemInfo1.container * k + paramAnonymousItemInfo1.screenId * j + i * paramAnonymousItemInfo1.cellY + paramAnonymousItemInfo1.cellX - (paramAnonymousItemInfo2.container * k + paramAnonymousItemInfo2.screenId * j + i * paramAnonymousItemInfo2.cellY + paramAnonymousItemInfo2.cellX));
        }
      });
    }
    
    private void verifyApplications()
    {
      Context localContext = LauncherModel.this.mApp.getContext();
      ArrayList localArrayList = new ArrayList();
      synchronized (LauncherModel.sBgLock)
      {
        Iterator localIterator = LauncherModel.this.mBgAllAppsList.data.iterator();
        while (localIterator.hasNext())
        {
          AppInfo localAppInfo = (AppInfo)localIterator.next();
          if (LauncherModel.this.getItemInfoForComponentName(localAppInfo.componentName).isEmpty())
          {
            localArrayList.add(localAppInfo);
            Log.e("Launcher.Model", "Missing Application on load: " + localAppInfo);
          }
        }
      }
      if (!localArrayList.isEmpty()) {
        if (LauncherModel.this.mCallbacks == null) {
          break label171;
        }
      }
      label171:
      for (LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks = null)
      {
        LauncherModel.this.addAndBindAddedApps(localContext, localArrayList, localCallbacks, null);
        return;
      }
    }
    
    private void waitForIdle()
    {
      try
      {
        LauncherModel.this.mHandler.postIdle(new Runnable()
        {
          public void run()
          {
            synchronized (LauncherModel.LoaderTask.this)
            {
              LauncherModel.LoaderTask.access$502(LauncherModel.LoaderTask.this, true);
              LauncherModel.LoaderTask.this.notify();
              return;
            }
          }
        });
        while ((!this.mStopped) && (!this.mLoadAndBindStepFinished))
        {
          boolean bool = LauncherModel.this.mFlushingWorkerThread;
          if (bool) {
            break;
          }
          try
          {
            wait(1000L);
          }
          catch (InterruptedException localInterruptedException) {}
        }
        return;
      }
      finally {}
    }
    
    public void dumpState()
    {
      synchronized (LauncherModel.sBgLock)
      {
        Log.d("Launcher.Model", "mLoaderTask.mContext=" + this.mContext);
        Log.d("Launcher.Model", "mLoaderTask.mIsLaunching=" + this.mIsLaunching);
        Log.d("Launcher.Model", "mLoaderTask.mStopped=" + this.mStopped);
        Log.d("Launcher.Model", "mLoaderTask.mLoadAndBindStepFinished=" + this.mLoadAndBindStepFinished);
        Log.d("Launcher.Model", "mItems size=" + LauncherModel.sBgWorkspaceItems.size());
        return;
      }
    }
    
    boolean isLaunching()
    {
      return this.mIsLaunching;
    }
    
    boolean isLoadingWorkspace()
    {
      return this.mIsLoadingAndBindingWorkspace;
    }
    
    public void run()
    {
      synchronized (LauncherModel.this.mLock)
      {
        LauncherModel.access$902(LauncherModel.this, true);
      }
      synchronized (LauncherModel.this.mLock)
      {
        boolean bool = this.mIsLaunching;
        int i = 0;
        if (bool)
        {
          Process.setThreadPriority(i);
          loadAndBindWorkspace();
          if (this.mStopped)
          {
            synchronized (LauncherModel.sBgLock)
            {
              Iterator localIterator = LauncherModel.sBgDbIconCache.keySet().iterator();
              if (!localIterator.hasNext()) {
                break label238;
              }
              Object localObject13 = localIterator.next();
              LauncherModel.this.updateSavedIcon(this.mContext, (ShortcutInfo)localObject13, (byte[])LauncherModel.sBgDbIconCache.get(localObject13));
            }
            localObject2 = finally;
            throw localObject2;
          }
        }
        else
        {
          i = 10;
        }
      }
      synchronized (LauncherModel.this.mLock)
      {
        for (;;)
        {
          if (this.mIsLaunching) {
            Process.setThreadPriority(10);
          }
          waitForIdle();
          loadAndBindAllApps();
          synchronized (LauncherModel.this.mLock)
          {
            Process.setThreadPriority(0);
          }
        }
      }
      label238:
      LauncherModel.sBgDbIconCache.clear();
      if (AppsCustomizePagedView.DISABLE_ALL_APPS) {
        verifyApplications();
      }
      this.mContext = null;
      synchronized (LauncherModel.this.mLock)
      {
        if (LauncherModel.this.mLoaderTask == this) {
          LauncherModel.access$1002(LauncherModel.this, null);
        }
        LauncherModel.access$902(LauncherModel.this, false);
        return;
      }
    }
    
    void runBindSynchronousPage(int paramInt)
    {
      if (paramInt < 0) {
        throw new RuntimeException("Should not call runBindSynchronousPage() without valid page index");
      }
      if ((!LauncherModel.this.mAllAppsLoaded) || (!LauncherModel.this.mWorkspaceLoaded)) {
        throw new RuntimeException("Expecting AllApps and Workspace to be loaded");
      }
      synchronized (LauncherModel.this.mLock)
      {
        if (LauncherModel.this.mIsLoaderTaskRunning) {
          throw new RuntimeException("Error! Background loading is already running");
        }
      }
      LauncherModel.this.mHandler.flush();
      bindWorkspace(paramInt, false);
      onlyBindAllApps();
    }
    
    public void stopLocked()
    {
      try
      {
        this.mStopped = true;
        notify();
        return;
      }
      finally {}
    }
    
    LauncherModel.Callbacks tryGetCallbacks(LauncherModel.Callbacks paramCallbacks)
    {
      synchronized (LauncherModel.this.mLock)
      {
        if (this.mStopped) {
          return null;
        }
        if (LauncherModel.this.mCallbacks == null) {
          return null;
        }
        LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
        if (localCallbacks != paramCallbacks) {
          return null;
        }
        if (localCallbacks == null)
        {
          Log.w("Launcher.Model", "no mCallbacks");
          return null;
        }
        return localCallbacks;
      }
    }
  }
  
  private class PackageUpdatedTask
    implements Runnable
  {
    int mOp;
    String[] mPackages;
    
    public PackageUpdatedTask(int paramInt, String[] paramArrayOfString)
    {
      this.mOp = paramInt;
      this.mPackages = paramArrayOfString;
    }
    
    public void run()
    {
      Context localContext = LauncherModel.this.mApp.getContext();
      String[] arrayOfString = this.mPackages;
      int i = arrayOfString.length;
      final ArrayList localArrayList1;
      ArrayList localArrayList2;
      ArrayList localArrayList3;
      switch (this.mOp)
      {
      default: 
        localArrayList1 = new ArrayList();
        int k = LauncherModel.this.mBgAllAppsList.added.size();
        localArrayList2 = null;
        if (k > 0)
        {
          localArrayList2 = new ArrayList(LauncherModel.this.mBgAllAppsList.added);
          LauncherModel.this.mBgAllAppsList.added.clear();
        }
        int m = LauncherModel.this.mBgAllAppsList.modified.size();
        localArrayList3 = null;
        if (m > 0)
        {
          ArrayList localArrayList8 = LauncherModel.this.mBgAllAppsList.modified;
          localArrayList3 = new ArrayList(localArrayList8);
          LauncherModel.this.mBgAllAppsList.modified.clear();
        }
        if (LauncherModel.this.mBgAllAppsList.removed.size() > 0)
        {
          localArrayList1.addAll(LauncherModel.this.mBgAllAppsList.removed);
          LauncherModel.this.mBgAllAppsList.removed.clear();
        }
        if (LauncherModel.this.mCallbacks == null) {
          break;
        }
      }
      for (final LauncherModel.Callbacks localCallbacks1 = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks1 = null)
      {
        if (localCallbacks1 != null) {
          break label388;
        }
        Log.w("Launcher.Model", "Nobody to tell about the new app.  Launcher is probably loading.");
        return;
        for (int i2 = 0; i2 < i; i2++) {
          LauncherModel.this.mBgAllAppsList.addPackage(localContext, arrayOfString[i2]);
        }
        for (int i1 = 0; i1 < i; i1++)
        {
          LauncherModel.this.mBgAllAppsList.updatePackage(localContext, arrayOfString[i1]);
          WidgetPreviewLoader.removePackageFromDb(LauncherModel.this.mApp.getWidgetPreviewCacheDb(), arrayOfString[i1]);
        }
        for (int j = 0; j < i; j++)
        {
          LauncherModel.this.mBgAllAppsList.removePackage(arrayOfString[j]);
          WidgetPreviewLoader.removePackageFromDb(LauncherModel.this.mApp.getWidgetPreviewCacheDb(), arrayOfString[j]);
        }
        break;
      }
      label388:
      LauncherModel.Callbacks localCallbacks2;
      if (localArrayList2 != null)
      {
        if (LauncherModel.this.mCallbacks == null) {
          break label557;
        }
        localCallbacks2 = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();
        if (AppsCustomizePagedView.DISABLE_ALL_APPS) {
          break label563;
        }
        LauncherModel.this.addAndBindAddedApps(localContext, new ArrayList(), localCallbacks2, localArrayList2);
      }
      label557:
      label563:
      for (;;)
      {
        label443:
        if (localArrayList3 != null)
        {
          final ArrayList localArrayList6 = localArrayList3;
          Iterator localIterator5 = localArrayList6.iterator();
          for (;;)
          {
            if (localIterator5.hasNext())
            {
              AppInfo localAppInfo2 = (AppInfo)localIterator5.next();
              Iterator localIterator6 = LauncherModel.this.getItemInfoForComponentName(localAppInfo2.componentName).iterator();
              while (localIterator6.hasNext())
              {
                ItemInfo localItemInfo = (ItemInfo)localIterator6.next();
                if (LauncherModel.isShortcutInfoUpdateable(localItemInfo))
                {
                  ShortcutInfo localShortcutInfo = (ShortcutInfo)localItemInfo;
                  localShortcutInfo.title = localAppInfo2.title.toString();
                  LauncherModel.updateItemInDatabase(localContext, localShortcutInfo);
                }
              }
              continue;
              localCallbacks2 = null;
              break;
              ArrayList localArrayList7 = new ArrayList(localArrayList2);
              LauncherModel.this.addAndBindAddedApps(localContext, localArrayList7, localCallbacks2, localArrayList2);
              break label443;
            }
          }
          DeferredHandler localDeferredHandler4 = LauncherModel.this.mHandler;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              if (LauncherModel.this.mCallbacks != null) {}
              for (LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks = null)
              {
                if ((localCallbacks1 == localCallbacks) && (localCallbacks != null)) {
                  localCallbacks1.bindAppsUpdated(localArrayList6);
                }
                return;
              }
            }
          };
          localDeferredHandler4.post(local1);
        }
      }
      final ArrayList localArrayList4 = new ArrayList();
      if (this.mOp == 3) {
        localArrayList4.addAll(Arrays.asList(arrayOfString));
      }
      for (;;)
      {
        Iterator localIterator1 = localArrayList4.iterator();
        while (localIterator1.hasNext())
        {
          String str = (String)localIterator1.next();
          Iterator localIterator4 = LauncherModel.this.getItemInfoForPackageName(str).iterator();
          while (localIterator4.hasNext()) {
            LauncherModel.deleteItemFromDatabase(localContext, (ItemInfo)localIterator4.next());
          }
        }
        if (this.mOp == 2)
        {
          PackageManager localPackageManager = localContext.getPackageManager();
          for (int n = 0; n < i; n++) {
            if (LauncherModel.this.isPackageDisabled(localPackageManager, arrayOfString[n])) {
              localArrayList4.add(arrayOfString[n]);
            }
          }
        }
      }
      Iterator localIterator2 = localArrayList1.iterator();
      while (localIterator2.hasNext())
      {
        AppInfo localAppInfo1 = (AppInfo)localIterator2.next();
        Iterator localIterator3 = LauncherModel.this.getItemInfoForComponentName(localAppInfo1.componentName).iterator();
        while (localIterator3.hasNext()) {
          LauncherModel.deleteItemFromDatabase(localContext, (ItemInfo)localIterator3.next());
        }
      }
      if ((!localArrayList4.isEmpty()) || (!localArrayList1.isEmpty()))
      {
        InstallShortcutReceiver.removeFromInstallQueue(localContext.getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0), localArrayList4);
        DeferredHandler localDeferredHandler1 = LauncherModel.this.mHandler;
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            if (LauncherModel.this.mCallbacks != null) {}
            for (LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks = null)
            {
              if ((localCallbacks1 == localCallbacks) && (localCallbacks != null)) {
                localCallbacks1.bindComponentsRemoved(localArrayList4, localArrayList1);
              }
              return;
            }
          }
        };
        localDeferredHandler1.post(local2);
      }
      final ArrayList localArrayList5 = LauncherModel.getSortedWidgetsAndShortcuts(localContext);
      DeferredHandler localDeferredHandler2 = LauncherModel.this.mHandler;
      Runnable local3 = new Runnable()
      {
        public void run()
        {
          if (LauncherModel.this.mCallbacks != null) {}
          for (LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks = null)
          {
            if ((localCallbacks1 == localCallbacks) && (localCallbacks != null)) {
              localCallbacks1.bindPackagesUpdated(localArrayList5);
            }
            return;
          }
        }
      };
      localDeferredHandler2.post(local3);
      DeferredHandler localDeferredHandler3 = LauncherModel.this.mHandler;
      Runnable local4 = new Runnable()
      {
        public void run()
        {
          if (LauncherModel.this.mCallbacks != null) {}
          for (LauncherModel.Callbacks localCallbacks = (LauncherModel.Callbacks)LauncherModel.this.mCallbacks.get();; localCallbacks = null)
          {
            if ((localCallbacks1 == localCallbacks) && (localCallbacks != null)) {
              localCallbacks1.dumpLogsToLocalData();
            }
            return;
          }
        }
      };
      localDeferredHandler3.post(local4);
    }
  }
  
  public static class ShortcutNameComparator
    implements Comparator<ResolveInfo>
  {
    private Collator mCollator;
    private HashMap<Object, CharSequence> mLabelCache;
    private PackageManager mPackageManager;
    
    ShortcutNameComparator(PackageManager paramPackageManager, HashMap<Object, CharSequence> paramHashMap)
    {
      this.mPackageManager = paramPackageManager;
      this.mLabelCache = paramHashMap;
      this.mCollator = Collator.getInstance();
    }
    
    public final int compare(ResolveInfo paramResolveInfo1, ResolveInfo paramResolveInfo2)
    {
      ComponentName localComponentName1 = LauncherModel.getComponentNameFromResolveInfo(paramResolveInfo1);
      ComponentName localComponentName2 = LauncherModel.getComponentNameFromResolveInfo(paramResolveInfo2);
      Object localObject1;
      Object localObject2;
      if (this.mLabelCache.containsKey(localComponentName1))
      {
        localObject1 = (CharSequence)this.mLabelCache.get(localComponentName1);
        if (!this.mLabelCache.containsKey(localComponentName2)) {
          break label103;
        }
        localObject2 = (CharSequence)this.mLabelCache.get(localComponentName2);
      }
      for (;;)
      {
        return this.mCollator.compare(localObject1, localObject2);
        localObject1 = paramResolveInfo1.loadLabel(this.mPackageManager).toString().trim();
        this.mLabelCache.put(localComponentName1, localObject1);
        break;
        label103:
        localObject2 = paramResolveInfo2.loadLabel(this.mPackageManager).toString().trim();
        this.mLabelCache.put(localComponentName2, localObject2);
      }
    }
  }
  
  public static class WidgetAndShortcutNameComparator
    implements Comparator<Object>
  {
    private Collator mCollator;
    private HashMap<Object, String> mLabelCache;
    private PackageManager mPackageManager;
    
    WidgetAndShortcutNameComparator(PackageManager paramPackageManager)
    {
      this.mPackageManager = paramPackageManager;
      this.mLabelCache = new HashMap();
      this.mCollator = Collator.getInstance();
    }
    
    public final int compare(Object paramObject1, Object paramObject2)
    {
      String str1;
      if (this.mLabelCache.containsKey(paramObject1))
      {
        str1 = (String)this.mLabelCache.get(paramObject1);
        if (this.mLabelCache.containsKey(paramObject2))
        {
          str2 = (String)this.mLabelCache.get(paramObject2);
          return this.mCollator.compare(str1, str2);
        }
      }
      else
      {
        if ((paramObject1 instanceof AppWidgetProviderInfo)) {}
        for (str1 = ((AppWidgetProviderInfo)paramObject1).label;; str1 = ((ResolveInfo)paramObject1).loadLabel(this.mPackageManager).toString().trim())
        {
          this.mLabelCache.put(paramObject1, str1);
          break;
        }
      }
      if ((paramObject2 instanceof AppWidgetProviderInfo)) {}
      for (String str2 = ((AppWidgetProviderInfo)paramObject2).label;; str2 = ((ResolveInfo)paramObject2).loadLabel(this.mPackageManager).toString().trim())
      {
        this.mLabelCache.put(paramObject2, str2);
        break;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherModel
 * JD-Core Version:    0.7.0.1
 */