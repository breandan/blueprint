package com.google.android.velvet;

import android.app.Application;
import android.content.pm.PackageInfo;

import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.Set;

public class VelvetApplication
        extends Application
        implements ActivityLifecycleNotifier, ActivityLifecycleObserver {
    private static VelvetApplication sApplication;
    private static PackageInfo sThisPackageInfo;
    private final Set<ActivityLifecycleObserver> mActivityLifecycleObservers;

    /* Error */
    public VelvetApplication() {
        // Byte code:
        //   0: aload_0
        //   1: invokespecial 19	android/app/Application:<init>	()V
        //   4: ldc 2
        //   6: monitorenter
        //   7: getstatic 21	com/google/android/velvet/VelvetApplication:sApplication	Lcom/google/android/velvet/VelvetApplication;
        //   10: ifnonnull +31 -> 41
        //   13: iconst_1
        //   14: istore_2
        //   15: iload_2
        //   16: invokestatic 27	com/google/common/base/Preconditions:checkState	(Z)V
        //   19: aload_0
        //   20: putstatic 21	com/google/android/velvet/VelvetApplication:sApplication	Lcom/google/android/velvet/VelvetApplication;
        //   23: ldc 2
        //   25: monitorexit
        //   26: aload_0
        //   27: invokestatic 33	com/google/android/velvet/WorkaroundUncaughtExceptionHandler:install	(Lcom/google/android/velvet/VelvetApplication;)V
        //   30: invokestatic 38	com/google/android/voicesearch/logger/EventLogger:init	()V
        //   33: aload_0
        //   34: invokestatic 44	com/google/common/collect/Sets:newHashSet	()Ljava/util/HashSet;
        //   37: putfield 46	com/google/android/velvet/VelvetApplication:mActivityLifecycleObservers	Ljava/util/Set;
        //   40: return
        //   41: iconst_0
        //   42: istore_2
        //   43: goto -28 -> 15
        //   46: astore_1
        //   47: ldc 2
        //   49: monitorexit
        //   50: aload_1
        //   51: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	52	0	this	VelvetApplication
        //   46	5	1	localObject	java.lang.Object
        //   14	29	2	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   7	13	46	finally
        //   15	26	46	finally
        //   47	50	46	finally
    }

    static VelvetApplication get() {
        Preconditions.checkNotNull(sApplication);
        return sApplication;
    }

    /* Error */
    private static PackageInfo getPkgInfo() {
        // Byte code:
        //   0: ldc 2
        //   2: monitorenter
        //   3: getstatic 58	com/google/android/velvet/VelvetApplication:sThisPackageInfo	Landroid/content/pm/PackageInfo;
        //   6: astore_1
        //   7: aload_1
        //   8: ifnonnull +22 -> 30
        //   11: getstatic 21	com/google/android/velvet/VelvetApplication:sApplication	Lcom/google/android/velvet/VelvetApplication;
        //   14: invokevirtual 62	com/google/android/velvet/VelvetApplication:getPackageManager	()Landroid/content/pm/PackageManager;
        //   17: getstatic 21	com/google/android/velvet/VelvetApplication:sApplication	Lcom/google/android/velvet/VelvetApplication;
        //   20: invokevirtual 66	com/google/android/velvet/VelvetApplication:getPackageName	()Ljava/lang/String;
        //   23: iconst_0
        //   24: invokevirtual 72	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   27: putstatic 58	com/google/android/velvet/VelvetApplication:sThisPackageInfo	Landroid/content/pm/PackageInfo;
        //   30: getstatic 58	com/google/android/velvet/VelvetApplication:sThisPackageInfo	Landroid/content/pm/PackageInfo;
        //   33: astore_2
        //   34: ldc 2
        //   36: monitorexit
        //   37: aload_2
        //   38: areturn
        //   39: astore_3
        //   40: new 74	java/lang/RuntimeException
        //   43: dup
        //   44: aload_3
        //   45: invokespecial 77	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
        //   48: athrow
        //   49: astore_0
        //   50: ldc 2
        //   52: monitorexit
        //   53: aload_0
        //   54: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   49	5	0	localObject	java.lang.Object
        //   6	2	1	localPackageInfo1	PackageInfo
        //   33	5	2	localPackageInfo2	PackageInfo
        //   39	6	3	localNameNotFoundException	android.content.pm.PackageManager.NameNotFoundException
        // Exception table:
        //   from	to	target	type
        //   11	30	39	android/content/pm/PackageManager$NameNotFoundException
        //   3	7	49	finally
        //   11	30	49	finally
        //   30	34	49	finally
        //   40	49	49	finally
    }

    public static int getVersionCode() {
        return getPkgInfo().versionCode;
    }

    public static String getVersionCodeString() {
        return Integer.toString(getVersionCode());
    }

    public static String getVersionName() {
        String str = getPkgInfo().versionName;
        if (str == null) {
            str = "3.1.8.eclipse";
        }
        return str;
    }

    static void warnIfNotInMainProcess() {
    }

    public void addActivityLifecycleObserver(ActivityLifecycleObserver paramActivityLifecycleObserver) {
        this.mActivityLifecycleObservers.add(paramActivityLifecycleObserver);
    }

    public void onActivityStart() {
        Iterator localIterator = this.mActivityLifecycleObservers.iterator();
        while (localIterator.hasNext()) {
            ((ActivityLifecycleObserver) localIterator.next()).onActivityStart();
        }
    }

    public void onActivityStop() {
        Iterator localIterator = this.mActivityLifecycleObservers.iterator();
        while (localIterator.hasNext()) {
            ((ActivityLifecycleObserver) localIterator.next()).onActivityStop();
        }
    }

    public void onCreate() {
        super.onCreate();
        VelvetStrictMode.init(this);
        EventLogger.recordLatencyStart(3);
        EventLogger.recordBreakdownEvent(16);
        EventLogger.recordBreakdownEvent(17);
        VelvetStrictMode.onStartupPoint(1);
    }

    public void onTrimMemory(int paramInt) {
        super.onTrimMemory(paramInt);
        VelvetServices.maybeTrimMemory(paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.VelvetApplication

 * JD-Core Version:    0.7.0.1

 */