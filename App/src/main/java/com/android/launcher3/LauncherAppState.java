package com.android.launcher3;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import java.lang.ref.WeakReference;

public class LauncherAppState
  implements DeviceProfile.DeviceProfileCallbacks
{
  private static LauncherAppState INSTANCE;
  private static Context sContext;
  private static WeakReference<LauncherProvider> sLauncherProvider;
  private AppFilter mAppFilter;
  private DynamicGrid mDynamicGrid;
  private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler())
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      LauncherAppState.this.mModel.resetLoadedState(false, true);
      LauncherAppState.this.mModel.startLoaderFromBackground();
    }
  };
  private IconCache mIconCache;
  private boolean mIsScreenLarge;
  private int mLongPressTimeout = 300;
  private LauncherModel mModel;
  private float mScreenDensity;
  private WidgetPreviewLoader.CacheDb mWidgetPreviewCacheDb;
  
  private LauncherAppState()
  {
    if (sContext == null) {
      throw new IllegalStateException("LauncherAppState inited before app context set");
    }
    Log.v("Launcher", "LauncherAppState inited");
    if (sContext.getResources().getBoolean(2131755016)) {
      MemoryTracker.startTrackingMe(sContext, "L");
    }
    this.mIsScreenLarge = isScreenLarge(sContext.getResources());
    this.mScreenDensity = sContext.getResources().getDisplayMetrics().density;
    this.mWidgetPreviewCacheDb = new WidgetPreviewLoader.CacheDb(sContext);
    this.mIconCache = new IconCache(sContext);
    this.mAppFilter = AppFilter.loadByName(sContext.getString(2131361858));
    this.mModel = new LauncherModel(this, this.mIconCache, this.mAppFilter);
    IntentFilter localIntentFilter1 = new IntentFilter("android.intent.action.PACKAGE_ADDED");
    localIntentFilter1.addAction("android.intent.action.PACKAGE_REMOVED");
    localIntentFilter1.addAction("android.intent.action.PACKAGE_CHANGED");
    localIntentFilter1.addDataScheme("package");
    sContext.registerReceiver(this.mModel, localIntentFilter1);
    IntentFilter localIntentFilter2 = new IntentFilter();
    localIntentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
    localIntentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
    localIntentFilter2.addAction("android.intent.action.LOCALE_CHANGED");
    localIntentFilter2.addAction("android.intent.action.CONFIGURATION_CHANGED");
    sContext.registerReceiver(this.mModel, localIntentFilter2);
    IntentFilter localIntentFilter3 = new IntentFilter();
    localIntentFilter3.addAction("android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED");
    sContext.registerReceiver(this.mModel, localIntentFilter3);
    IntentFilter localIntentFilter4 = new IntentFilter();
    localIntentFilter4.addAction("android.search.action.SEARCHABLES_CHANGED");
    sContext.registerReceiver(this.mModel, localIntentFilter4);
    sContext.getContentResolver().registerContentObserver(LauncherSettings.Favorites.CONTENT_URI, true, this.mFavoritesObserver);
  }
  
  public static LauncherAppState getInstance()
  {
    if (INSTANCE == null) {
      INSTANCE = new LauncherAppState();
    }
    return INSTANCE;
  }
  
  public static LauncherAppState getInstanceNoCreate()
  {
    return INSTANCE;
  }
  
  static LauncherProvider getLauncherProvider()
  {
    return (LauncherProvider)sLauncherProvider.get();
  }
  
  public static String getSharedPreferencesKey()
  {
    return "com.android.launcher3.prefs";
  }
  
  public static boolean isScreenLandscape(Context paramContext)
  {
    return paramContext.getResources().getConfiguration().orientation == 2;
  }
  
  public static boolean isScreenLarge(Resources paramResources)
  {
    return paramResources.getBoolean(2131755010);
  }
  
  public static void setApplicationContext(Context paramContext)
  {
    if (sContext != null) {
      Log.w("Launcher", "setApplicationContext called twice! old=" + sContext + " new=" + paramContext);
    }
    sContext = paramContext.getApplicationContext();
  }
  
  static void setLauncherProvider(LauncherProvider paramLauncherProvider)
  {
    sLauncherProvider = new WeakReference(paramLauncherProvider);
  }
  
  public Context getContext()
  {
    return sContext;
  }
  
  DynamicGrid getDynamicGrid()
  {
    return this.mDynamicGrid;
  }
  
  IconCache getIconCache()
  {
    return this.mIconCache;
  }
  
  public int getLongPressTimeout()
  {
    return this.mLongPressTimeout;
  }
  
  LauncherModel getModel()
  {
    return this.mModel;
  }
  
  public float getScreenDensity()
  {
    return this.mScreenDensity;
  }
  
  WidgetPreviewLoader.CacheDb getWidgetPreviewCacheDb()
  {
    return this.mWidgetPreviewCacheDb;
  }
  
  DeviceProfile initDynamicGrid(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (this.mDynamicGrid == null)
    {
      this.mDynamicGrid = new DynamicGrid(paramContext, paramContext.getResources(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
      this.mDynamicGrid.getDeviceProfile().addCallback(this);
    }
    DeviceProfile localDeviceProfile = this.mDynamicGrid.getDeviceProfile();
    localDeviceProfile.updateFromConfiguration(paramContext, paramContext.getResources(), paramInt3, paramInt4, paramInt5, paramInt6);
    return localDeviceProfile;
  }
  
  public boolean isScreenLarge()
  {
    return this.mIsScreenLarge;
  }
  
  public void onAvailableSizeChanged(DeviceProfile paramDeviceProfile)
  {
    Utilities.setIconSize(paramDeviceProfile.iconSizePx);
  }
  
  LauncherModel setLauncher(Launcher paramLauncher)
  {
    if (this.mModel == null) {
      throw new IllegalStateException("setLauncher() called before init()");
    }
    this.mModel.initialize(paramLauncher);
    return this.mModel;
  }
  
  boolean shouldShowAppOrWidgetProvider(ComponentName paramComponentName)
  {
    return (this.mAppFilter == null) || (this.mAppFilter.shouldShowApp(paramComponentName));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAppState
 * JD-Core Version:    0.7.0.1
 */