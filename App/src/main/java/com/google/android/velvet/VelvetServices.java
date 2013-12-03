package com.google.android.velvet;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.AsyncServicesImpl;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.CoreSearchServicesImpl;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.GlobalSearchServicesImpl;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.RelationshipManager;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.imageloader.DataUriImageLoader;
import com.google.android.search.core.imageloader.NetworkImageLoader;
import com.google.android.search.core.preferences.PreferenceController;
import com.google.android.search.core.preferences.SearchPreferenceControllerFactory;
import com.google.android.search.shared.imageloader.CachingImageLoader;
import com.google.android.search.shared.imageloader.ContentProviderImageLoader;
import com.google.android.search.shared.imageloader.ResizingImageLoader;
import com.google.android.search.shared.imageloader.ResourceImageLoader;
import com.google.android.shared.util.BackgroundUriLoader;
import com.google.android.shared.util.CascadingUriLoader;
import com.google.android.shared.util.PostToExecutorLoader;
import com.google.android.shared.util.SynchronousLoader;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.TrafficIntentService;
import com.google.android.sidekick.main.calendar.CalendarIntentService;
import com.google.android.sidekick.main.inject.DefaultSidekickInjector;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.location.LocationOracle.RunningLock;
import com.google.android.speech.contacts.RelationshipNameLookup;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.io.PrintWriter;

public class VelvetServices {
    private static VelvetServices sServices;
    private final Context mAppContext;
    private final AsyncServices mAsyncServices;
    private final Object mBackgroundInitLock = new Object();
    private CoreSearchServices mCoreServices;
    private VelvetFactory mFactory;
    private GlobalSearchServices mGlobalSearchServices;
    private GsaConfigFlags mGsaConfigFlags;
    private UriLoader<Drawable> mIconLoader;
    private CachingImageLoader mImageLoader;
    private LocationOracle.RunningLock mLocationOracleLockForNow;
    private UriLoader<Drawable> mNonCachingImageLoader;
    private GsaPreferenceController mPrefController;
    private RelationshipManager mRelationshipManager;
    private RelationshipNameLookup mRelationshipNameLookup;
    private boolean mSidekickAlarmsRegistered;
    private SidekickInjector mSidekickInjector;
    private boolean mSidekickServicesStarted;
    private VoiceSearchServices mVoiceSearchServices;

    VelvetServices(Context paramContext) {
        this.mAppContext = paramContext;
        this.mAsyncServices = new AsyncServicesImpl();
    }

    private UriLoader<Drawable> createIconLoader() {
        int i = Math.round(this.mAppContext.getResources().getDimension(2131689567));
        return CascadingUriLoader.create(ImmutableList.of(new CachingImageLoader(createUiThreadPostingBackgroundLoader(new ResizingImageLoader(i, i, new ContentProviderImageLoader(this.mAppContext)))), new ResourceImageLoader(this.mAppContext)));
    }

    private UriLoader<Drawable> createNonCachingImageLoader() {
        return CascadingUriLoader.create(ImmutableList.of(createUiThreadPostingBackgroundLoader(new NetworkImageLoader(getCoreServices().getHttpHelper(), this.mAppContext.getResources())), createUiThreadPostingBackgroundLoader(new DataUriImageLoader(this.mAppContext.getResources())), createUiThreadPostingBackgroundLoader(new ContentProviderImageLoader(this.mAppContext)), new ResourceImageLoader(this.mAppContext)));
    }

    private <A> UriLoader<A> createUiThreadPostingBackgroundLoader(SynchronousLoader<A> paramSynchronousLoader) {
        BackgroundUriLoader localBackgroundUriLoader = new BackgroundUriLoader(this.mAsyncServices.getPooledBackgroundExecutorService(), paramSynchronousLoader);
        return new PostToExecutorLoader(this.mAsyncServices.getUiThreadExecutor(), localBackgroundUriLoader);
    }

    public static VelvetServices get() {
        for (; ; ) {
            try {
                VelvetApplication.warnIfNotInMainProcess();
                if (sServices == null) {
                    sServices = new VelvetServices(VelvetApplication.get());
                    VelvetServices localVelvetServices = sServices;
                    return localVelvetServices;
                }
                boolean bool;
                if (sServices.mAppContext == VelvetApplication.get()) {
                    bool = true;
                    Preconditions.checkState(bool);
                } else {
                    bool = false;
                }
            } finally {
            }
        }
    }

    public static void maybeTrimMemory(int paramInt) {
        try {
            if ((sServices != null) && ((paramInt == 15) || (paramInt == 10) || (paramInt > 60))) {
                if (sServices.mImageLoader != null) {
                    sServices.mImageLoader.clearCache();
                }
                if (sServices.mIconLoader != null) {
                    sServices.mIconLoader.clearCache();
                }
            }
            return;
        } finally {
        }
    }

    private void registerSidekickAlarms() {
        synchronized (this.mBackgroundInitLock) {
            if ((this.mSidekickAlarmsRegistered) || (!getCoreServices().getNowOptInSettings().isUserOptedIn())) {
                return;
            }
            this.mSidekickAlarmsRegistered = true;
            TrafficIntentService.ensureScheduled(this.mAppContext, getCoreServices().getPendingIntentFactory());
            getSidekickInjector().getEntriesRefreshScheduler().setNextRefreshAlarm(false);
            CalendarIntentService.startUpdateAlarm(this.mAppContext);
            return;
        }
    }

    public PreferenceController createPreferenceController(Activity paramActivity) {
        VoiceSearchServices localVoiceSearchServices = getVoiceSearchServices();
        return new SearchPreferenceControllerFactory(getCoreServices(), localVoiceSearchServices.getPersonalizationHelper(), paramActivity, getGlobalSearchServices(), localVoiceSearchServices.getGreco3Container().getDeviceClassSupplier(), localVoiceSearchServices.getGreco3Container().getGreco3DataManager(), getSidekickInjector().getNetworkClient(), getGlobalSearchServices().getSearchHistoryHelper(), getPreferenceController(), getCoreServices().getGmsLocationReportingHelper(), localVoiceSearchServices.getSettings(), getAsyncServices().getUiThreadExecutor());
    }

    public void dump(String paramString, PrintWriter paramPrintWriter) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("VelvetServices state:");
        String str = paramString + "  ";
        DumpUtils.println(paramPrintWriter, new Object[]{str, "PROD", " BUILD"});
        Feature[] arrayOfFeature = Feature.values();
        int i = arrayOfFeature.length;
        for (int j = 0; j < i; j++) {
            DumpUtils.println(paramPrintWriter, new Object[]{str, arrayOfFeature[j]});
        }
        getGsaConfigFlags().dump(str, paramPrintWriter);
        getVoiceSearchServices().dump(str, paramPrintWriter);
    }

    public AsyncServices getAsyncServices() {
        return this.mAsyncServices;
    }

    public CoreSearchServices getCoreServices() {
        try {
            if (this.mCoreServices == null) {
                this.mCoreServices = new CoreSearchServicesImpl(this.mAppContext, this, this);
                DebugFeatures.setSearchSettings(this.mCoreServices.getSearchSettings());
            }
            CoreSearchServices localCoreSearchServices = this.mCoreServices;
            return localCoreSearchServices;
        } finally {
        }
    }

    public VelvetFactory getFactory() {
        try {
            if (this.mFactory == null) {
                this.mFactory = new VelvetFactory(this, this.mAppContext);
            }
            VelvetFactory localVelvetFactory = this.mFactory;
            return localVelvetFactory;
        } finally {
        }
    }

    public GlobalSearchServices getGlobalSearchServices() {
        try {
            if (this.mGlobalSearchServices == null) {
                this.mGlobalSearchServices = new GlobalSearchServicesImpl(this.mAppContext, getCoreServices(), this.mAsyncServices, this);
            }
            GlobalSearchServices localGlobalSearchServices = this.mGlobalSearchServices;
            return localGlobalSearchServices;
        } finally {
        }
    }

    public GsaConfigFlags getGsaConfigFlags() {
        try {
            if (this.mGsaConfigFlags == null) {
                this.mGsaConfigFlags = new GsaConfigFlags(this.mAppContext.getResources(), getCoreServices().getSearchSettings());
            }
            GsaConfigFlags localGsaConfigFlags = this.mGsaConfigFlags;
            return localGsaConfigFlags;
        } finally {
        }
    }

    public UriLoader<Drawable> getIconLoader() {
        try {
            if (this.mIconLoader == null) {
                this.mIconLoader = createIconLoader();
            }
            UriLoader localUriLoader = this.mIconLoader;
            return localUriLoader;
        } finally {
        }
    }

    public UriLoader<Drawable> getImageLoader() {
        try {
            if (this.mImageLoader == null) {
                this.mImageLoader = new CachingImageLoader(getNonCachingImageLoader());
            }
            CachingImageLoader localCachingImageLoader = this.mImageLoader;
            return localCachingImageLoader;
        } finally {
        }
    }

    public LocationOracle getLocationOracle() {
        try {
            LocationOracle localLocationOracle = getSidekickInjector().getLocationOracle();
            return localLocationOracle;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public UriLoader<Drawable> getNonCachingImageLoader() {
        try {
            if (this.mNonCachingImageLoader == null) {
                this.mNonCachingImageLoader = createNonCachingImageLoader();
            }
            UriLoader localUriLoader = this.mNonCachingImageLoader;
            return localUriLoader;
        } finally {
        }
    }

    public GsaPreferenceController getPreferenceController() {
        try {
            if (this.mPrefController == null) {
                this.mPrefController = new GsaPreferenceController(this.mAppContext);
            }
            GsaPreferenceController localGsaPreferenceController = this.mPrefController;
            return localGsaPreferenceController;
        } finally {
        }
    }

    public RelationshipManager getRelationshipManager() {
        try {
            if (this.mRelationshipManager == null) {
                this.mRelationshipManager = new RelationshipManager(getCoreServices().getSearchSettings(), this.mAppContext, getRelationshipNameLookup());
            }
            RelationshipManager localRelationshipManager = this.mRelationshipManager;
            return localRelationshipManager;
        } finally {
        }
    }

    public RelationshipNameLookup getRelationshipNameLookup() {
        try {
            if (this.mRelationshipNameLookup == null) {
                this.mRelationshipNameLookup = new RelationshipNameLookup(getVoiceSearchServices().getSettings().getSpokenLocaleBcp47());
            }
            RelationshipNameLookup localRelationshipNameLookup = this.mRelationshipNameLookup;
            return localRelationshipNameLookup;
        } finally {
        }
    }

    public SidekickInjector getSidekickInjector() {
        try {
            if (this.mSidekickInjector == null) {
                this.mSidekickInjector = new DefaultSidekickInjector(this.mAppContext, getPreferenceController(), getCoreServices(), this.mAsyncServices, (VelvetApplication) this.mAppContext, this);
                this.mAsyncServices.getPooledBackgroundExecutorService().execute(new Runnable() {
                    public void run() {
                        VelvetServices.this.startNowServices();
                    }
                });
            }
            SidekickInjector localSidekickInjector = this.mSidekickInjector;
            return localSidekickInjector;
        } finally {
        }
    }

    public VoiceSearchServices getVoiceSearchServices() {
        try {
            if (this.mVoiceSearchServices == null) {
                this.mVoiceSearchServices = new VoiceSearchServices(this.mAppContext, this.mAsyncServices, getPreferenceController(), getCoreServices(), this);
                this.mVoiceSearchServices.init();
            }
            VoiceSearchServices localVoiceSearchServices = this.mVoiceSearchServices;
            return localVoiceSearchServices;
        } finally {
        }
    }

    public void maybeRegisterSidekickAlarms() {
        this.mAsyncServices.getPooledBackgroundExecutorService().execute(new Runnable() {
            public void run() {
                VelvetServices.this.registerSidekickAlarms();
            }
        });
    }

    /* Error */
    public void startNowServices() {
        // Byte code:
        //   0: iconst_1
        //   1: istore_1
        //   2: aload_0
        //   3: monitorenter
        //   4: aload_0
        //   5: getfield 456	com/google/android/velvet/VelvetServices:mSidekickServicesStarted	Z
        //   8: ifeq +6 -> 14
        //   11: aload_0
        //   12: monitorexit
        //   13: return
        //   14: aload_0
        //   15: invokevirtual 121	com/google/android/velvet/VelvetServices:getCoreServices	()Lcom/google/android/search/core/CoreSearchServices;
        //   18: invokeinterface 195 1 0
        //   23: invokeinterface 201 1 0
        //   28: ifne +11 -> 39
        //   31: aload_0
        //   32: monitorexit
        //   33: return
        //   34: astore_2
        //   35: aload_0
        //   36: monitorexit
        //   37: aload_2
        //   38: athrow
        //   39: aload_0
        //   40: iconst_1
        //   41: putfield 456	com/google/android/velvet/VelvetServices:mSidekickServicesStarted	Z
        //   44: aload_0
        //   45: monitorexit
        //   46: aload_0
        //   47: monitorenter
        //   48: aload_0
        //   49: getfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   52: ifnonnull +85 -> 137
        //   55: iload_1
        //   56: invokestatic 177	com/google/common/base/Preconditions:checkState	(Z)V
        //   59: aload_0
        //   60: aload_0
        //   61: invokevirtual 459	com/google/android/velvet/VelvetServices:getLocationOracle	()Lcom/google/android/sidekick/main/location/LocationOracle;
        //   64: ldc_w 461
        //   67: invokeinterface 467 2 0
        //   72: putfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   75: aload_0
        //   76: getfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   79: invokeinterface 472 1 0
        //   84: aload_0
        //   85: monitorexit
        //   86: aload_0
        //   87: invokevirtual 215	com/google/android/velvet/VelvetServices:getSidekickInjector	()Lcom/google/android/sidekick/main/inject/SidekickInjector;
        //   90: astore 4
        //   92: aload 4
        //   94: invokeinterface 476 1 0
        //   99: aload_0
        //   100: invokevirtual 215	com/google/android/velvet/VelvetServices:getSidekickInjector	()Lcom/google/android/sidekick/main/inject/SidekickInjector;
        //   103: invokeinterface 482 2 0
        //   108: aload 4
        //   110: invokeinterface 486 1 0
        //   115: invokeinterface 491 1 0
        //   120: aload_0
        //   121: monitorenter
        //   122: aload_0
        //   123: iconst_1
        //   124: putfield 456	com/google/android/velvet/VelvetServices:mSidekickServicesStarted	Z
        //   127: aload_0
        //   128: monitorexit
        //   129: return
        //   130: astore 5
        //   132: aload_0
        //   133: monitorexit
        //   134: aload 5
        //   136: athrow
        //   137: iconst_0
        //   138: istore_1
        //   139: goto -84 -> 55
        //   142: astore_3
        //   143: aload_0
        //   144: monitorexit
        //   145: aload_3
        //   146: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	147	0	this	VelvetServices
        //   1	138	1	bool	boolean
        //   34	4	2	localObject1	Object
        //   142	4	3	localObject2	Object
        //   90	19	4	localSidekickInjector	SidekickInjector
        //   130	5	5	localObject3	Object
        // Exception table:
        //   from	to	target	type
        //   4	13	34	finally
        //   14	33	34	finally
        //   35	37	34	finally
        //   39	46	34	finally
        //   122	129	130	finally
        //   132	134	130	finally
        //   48	55	142	finally
        //   55	86	142	finally
        //   143	145	142	finally
    }

    /* Error */
    public void stopNowServices() {
        // Byte code:
        //   0: invokestatic 497	com/google/android/shared/util/ExtraPreconditions:checkMainThread	()V
        //   3: aload_0
        //   4: getfield 50	com/google/android/velvet/VelvetServices:mAppContext	Landroid/content/Context;
        //   7: new 499	android/content/Intent
        //   10: dup
        //   11: ldc_w 501
        //   14: aconst_null
        //   15: aload_0
        //   16: getfield 50	com/google/android/velvet/VelvetServices:mAppContext	Landroid/content/Context;
        //   19: ldc 207
        //   21: invokespecial 504	android/content/Intent:<init>	(Ljava/lang/String;Landroid/net/Uri;Landroid/content/Context;Ljava/lang/Class;)V
        //   24: invokevirtual 508	android/content/Context:startService	(Landroid/content/Intent;)Landroid/content/ComponentName;
        //   27: pop
        //   28: aload_0
        //   29: getfield 50	com/google/android/velvet/VelvetServices:mAppContext	Landroid/content/Context;
        //   32: new 499	android/content/Intent
        //   35: dup
        //   36: ldc_w 510
        //   39: aconst_null
        //   40: aload_0
        //   41: getfield 50	com/google/android/velvet/VelvetServices:mAppContext	Landroid/content/Context;
        //   44: ldc_w 512
        //   47: invokespecial 504	android/content/Intent:<init>	(Ljava/lang/String;Landroid/net/Uri;Landroid/content/Context;Ljava/lang/Class;)V
        //   50: invokevirtual 508	android/content/Context:startService	(Landroid/content/Intent;)Landroid/content/ComponentName;
        //   53: pop
        //   54: aload_0
        //   55: monitorenter
        //   56: aload_0
        //   57: getfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   60: ifnull +17 -> 77
        //   63: aload_0
        //   64: getfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   67: invokeinterface 515 1 0
        //   72: aload_0
        //   73: aconst_null
        //   74: putfield 458	com/google/android/velvet/VelvetServices:mLocationOracleLockForNow	Lcom/google/android/sidekick/main/location/LocationOracle$RunningLock;
        //   77: aload_0
        //   78: monitorexit
        //   79: aload_0
        //   80: getfield 426	com/google/android/velvet/VelvetServices:mSidekickInjector	Lcom/google/android/sidekick/main/inject/SidekickInjector;
        //   83: invokeinterface 476 1 0
        //   88: aload_0
        //   89: invokevirtual 215	com/google/android/velvet/VelvetServices:getSidekickInjector	()Lcom/google/android/sidekick/main/inject/SidekickInjector;
        //   92: invokeinterface 518 2 0
        //   97: aload_0
        //   98: getfield 426	com/google/android/velvet/VelvetServices:mSidekickInjector	Lcom/google/android/sidekick/main/inject/SidekickInjector;
        //   101: invokeinterface 221 1 0
        //   106: invokevirtual 521	com/google/android/sidekick/main/entry/EntriesRefreshScheduler:unregisterRefreshAlarm	()V
        //   109: aload_0
        //   110: monitorenter
        //   111: aload_0
        //   112: iconst_0
        //   113: putfield 456	com/google/android/velvet/VelvetServices:mSidekickServicesStarted	Z
        //   116: aload_0
        //   117: monitorexit
        //   118: aload_0
        //   119: getfield 48	com/google/android/velvet/VelvetServices:mBackgroundInitLock	Ljava/lang/Object;
        //   122: astore 5
        //   124: aload 5
        //   126: monitorenter
        //   127: aload_0
        //   128: iconst_0
        //   129: putfield 191	com/google/android/velvet/VelvetServices:mSidekickAlarmsRegistered	Z
        //   132: aload 5
        //   134: monitorexit
        //   135: return
        //   136: astore_3
        //   137: aload_0
        //   138: monitorexit
        //   139: aload_3
        //   140: athrow
        //   141: astore 4
        //   143: aload_0
        //   144: monitorexit
        //   145: aload 4
        //   147: athrow
        //   148: astore 6
        //   150: aload 5
        //   152: monitorexit
        //   153: aload 6
        //   155: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	156	0	this	VelvetServices
        //   136	4	3	localObject1	Object
        //   141	5	4	localObject2	Object
        //   148	6	6	localObject4	Object
        // Exception table:
        //   from	to	target	type
        //   56	77	136	finally
        //   77	79	136	finally
        //   137	139	136	finally
        //   111	118	141	finally
        //   143	145	141	finally
        //   127	135	148	finally
        //   150	153	148	finally
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.VelvetServices

 * JD-Core Version:    0.7.0.1

 */