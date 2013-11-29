package com.google.android.sidekick.main;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.notifications.NowNotificationManager.NotificationType;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class TrafficIntentService
  extends IntentService
{
  static final long DEFAULT_TIME_BETWEEN_QUERIES_IN_MILLIS = 900000L;
  private static final String TAG = Tag.getTag(TrafficIntentService.class);
  private AlarmHelper mAlarmHelper;
  private Clock mClock;
  private LocationOracle mLocationOracle;
  private NetworkClient mNetworkClient;
  private NowNotificationManager mNowNotificationManager;
  private PendingIntentFactory mPendingIntentFactory;
  private PowerManager mPowerManager;
  
  public TrafficIntentService()
  {
    super(TAG);
  }
  
  private void broadcastEntry(Location paramLocation, Sidekick.Entry paramEntry)
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.NOTIFICATION_ENTRY_ACTION");
    localIntent.putExtra("notification_entry", paramEntry.toByteArray());
    localIntent.putExtra("location_key", paramLocation);
    localIntent.setPackage(getPackageName());
    sendBroadcast(localIntent);
  }
  
  private static Sidekick.RequestPayload buildRequestPayload()
  {
    Sidekick.Interest localInterest = new Sidekick.Interest().setTargetDisplay(3).addEntryTypeRestrict(1);
    Sidekick.EntryQuery localEntryQuery = new Sidekick.EntryQuery().addInterest(localInterest);
    return new Sidekick.RequestPayload().setEntryQuery(localEntryQuery);
  }
  
  private long checkTrafficReport()
  {
    long l = 0L;
    if (!this.mLocationOracle.hasLocation()) {
      this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.TRAFFIC_NOTIFICATION);
    }
    int i;
    do
    {
      return l;
      Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithLocation(buildRequestPayload());
      if ((localResponsePayload == null) || (!localResponsePayload.hasEntryResponse()))
      {
        this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.TRAFFIC_NOTIFICATION);
        return l;
      }
      l = 0L;
      boolean bool1 = localResponsePayload.hasEntryResponse();
      i = 0;
      if (bool1)
      {
        int j = localResponsePayload.getEntryResponse().getEntryTreeCount();
        i = 0;
        if (j > 0)
        {
          Sidekick.EntryTree localEntryTree = localResponsePayload.getEntryResponse().getEntryTree(0);
          int k = -1;
          if (localEntryTree.hasError()) {
            k = localEntryTree.getError();
          }
          if (localEntryTree.hasExpirationTimestampSeconds()) {
            l = 1000L * localEntryTree.getExpirationTimestampSeconds();
          }
          boolean bool2 = localEntryTree.hasRoot();
          i = 0;
          if (bool2)
          {
            i = 0;
            if (k == -1)
            {
              Sidekick.EntryTreeNode localEntryTreeNode = localEntryTree.getRoot();
              int m = localEntryTreeNode.getEntryCount();
              i = 0;
              if (m > 0)
              {
                Sidekick.Entry localEntry = localEntryTreeNode.getEntry(0);
                boolean bool3 = localEntry.hasNotification();
                i = 0;
                if (bool3)
                {
                  int n = localEntry.getNotification().getType();
                  i = 0;
                  if (n != 3)
                  {
                    broadcastEntry(this.mLocationOracle.getBestLocation(), localEntry);
                    i = 1;
                  }
                }
              }
            }
          }
        }
      }
    } while (i != 0);
    this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.TRAFFIC_NOTIFICATION);
    return l;
  }
  
  public static void ensureScheduled(Context paramContext, PendingIntentFactory paramPendingIntentFactory)
  {
    if (!trafficIntentExists(paramContext, paramPendingIntentFactory)) {
      paramContext.startService(new Intent(paramContext, TrafficIntentService.class));
    }
  }
  
  private static PendingIntent getTrafficIntent(Context paramContext, PendingIntentFactory paramPendingIntentFactory, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, TrafficIntentService.class);
    int i = 1073741824;
    if (!paramBoolean) {
      i |= 0x20000000;
    }
    return paramPendingIntentFactory.getService(0, localIntent, i);
  }
  
  private void resetAlarmForScheduledUpdates(int paramInt, long paramLong)
  {
    PendingIntent localPendingIntent = getTrafficIntent(getApplicationContext(), this.mPendingIntentFactory, true);
    this.mAlarmHelper.setExact(paramInt, paramLong, localPendingIntent);
  }
  
  private static boolean trafficIntentExists(Context paramContext, PendingIntentFactory paramPendingIntentFactory)
  {
    PendingIntent localPendingIntent = getTrafficIntent(paramContext, paramPendingIntentFactory, false);
    boolean bool = false;
    if (localPendingIntent != null) {
      bool = true;
    }
    return bool;
  }
  
  void injectDependencies(LocationOracle paramLocationOracle, NetworkClient paramNetworkClient, NowNotificationManager paramNowNotificationManager, PowerManager paramPowerManager, Clock paramClock, AlarmHelper paramAlarmHelper, PendingIntentFactory paramPendingIntentFactory)
  {
    this.mLocationOracle = paramLocationOracle;
    this.mNetworkClient = paramNetworkClient;
    this.mNowNotificationManager = paramNowNotificationManager;
    this.mPowerManager = paramPowerManager;
    this.mClock = paramClock;
    this.mAlarmHelper = paramAlarmHelper;
    this.mPendingIntentFactory = paramPendingIntentFactory;
  }
  
  public void onCreate()
  {
    super.onCreate();
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    injectDependencies(localSidekickInjector.getLocationOracle(), localSidekickInjector.getNetworkClient(), localSidekickInjector.getNowNotificationManager(), (PowerManager)getSystemService("power"), localVelvetServices.getCoreServices().getClock(), localVelvetServices.getCoreServices().getAlarmHelper(), localVelvetServices.getCoreServices().getPendingIntentFactory());
    localVelvetServices.maybeRegisterSidekickAlarms();
  }
  
  /* Error */
  public void onHandleIntent(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 251	com/google/android/sidekick/main/TrafficIntentService:mPowerManager	Landroid/os/PowerManager;
    //   4: iconst_1
    //   5: new 314	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 315	java/lang/StringBuilder:<init>	()V
    //   12: getstatic 34	com/google/android/sidekick/main/TrafficIntentService:TAG	Ljava/lang/String;
    //   15: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: ldc_w 321
    //   21: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: invokevirtual 324	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   27: invokevirtual 328	android/os/PowerManager:newWakeLock	(ILjava/lang/String;)Landroid/os/PowerManager$WakeLock;
    //   30: astore_2
    //   31: aload_2
    //   32: invokevirtual 333	android/os/PowerManager$WakeLock:acquire	()V
    //   35: aload_1
    //   36: ifnull +49 -> 85
    //   39: ldc_w 335
    //   42: aload_1
    //   43: invokevirtual 338	android/content/Intent:getAction	()Ljava/lang/String;
    //   46: invokevirtual 344	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   49: ifeq +36 -> 85
    //   52: aload_0
    //   53: invokevirtual 235	com/google/android/sidekick/main/TrafficIntentService:getApplicationContext	()Landroid/content/Context;
    //   56: aload_0
    //   57: getfield 237	com/google/android/sidekick/main/TrafficIntentService:mPendingIntentFactory	Lcom/google/android/sidekick/main/inject/PendingIntentFactory;
    //   60: iconst_0
    //   61: invokestatic 239	com/google/android/sidekick/main/TrafficIntentService:getTrafficIntent	(Landroid/content/Context;Lcom/google/android/sidekick/main/inject/PendingIntentFactory;Z)Landroid/app/PendingIntent;
    //   64: astore 8
    //   66: aload 8
    //   68: ifnull +12 -> 80
    //   71: aload_0
    //   72: getfield 241	com/google/android/sidekick/main/TrafficIntentService:mAlarmHelper	Lcom/google/android/search/core/util/AlarmHelper;
    //   75: aload 8
    //   77: invokevirtual 348	com/google/android/search/core/util/AlarmHelper:cancel	(Landroid/app/PendingIntent;)V
    //   80: aload_2
    //   81: invokevirtual 351	android/os/PowerManager$WakeLock:release	()V
    //   84: return
    //   85: aload_0
    //   86: invokespecial 353	com/google/android/sidekick/main/TrafficIntentService:checkTrafficReport	()J
    //   89: lstore 4
    //   91: lload 4
    //   93: lconst_0
    //   94: lcmp
    //   95: ifle +46 -> 141
    //   98: aload_0
    //   99: getfield 253	com/google/android/sidekick/main/TrafficIntentService:mClock	Lcom/google/android/shared/util/Clock;
    //   102: invokeinterface 358 1 0
    //   107: lstore 6
    //   109: lload 4
    //   111: lload 6
    //   113: lsub
    //   114: ldc2_w 359
    //   117: lcmp
    //   118: ifge +11 -> 129
    //   121: lload 6
    //   123: ldc2_w 359
    //   126: ladd
    //   127: lstore 4
    //   129: aload_0
    //   130: iconst_0
    //   131: lload 4
    //   133: invokespecial 362	com/google/android/sidekick/main/TrafficIntentService:resetAlarmForScheduledUpdates	(IJ)V
    //   136: aload_2
    //   137: invokevirtual 351	android/os/PowerManager$WakeLock:release	()V
    //   140: return
    //   141: aload_1
    //   142: ifnull +14 -> 156
    //   145: aload_1
    //   146: ldc_w 364
    //   149: iconst_0
    //   150: invokevirtual 368	android/content/Intent:getBooleanExtra	(Ljava/lang/String;Z)Z
    //   153: ifne -17 -> 136
    //   156: aload_0
    //   157: iconst_2
    //   158: ldc2_w 7
    //   161: aload_0
    //   162: getfield 253	com/google/android/sidekick/main/TrafficIntentService:mClock	Lcom/google/android/shared/util/Clock;
    //   165: invokeinterface 371 1 0
    //   170: ladd
    //   171: invokespecial 362	com/google/android/sidekick/main/TrafficIntentService:resetAlarmForScheduledUpdates	(IJ)V
    //   174: goto -38 -> 136
    //   177: astore_3
    //   178: aload_2
    //   179: invokevirtual 351	android/os/PowerManager$WakeLock:release	()V
    //   182: aload_3
    //   183: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	this	TrafficIntentService
    //   0	184	1	paramIntent	Intent
    //   30	149	2	localWakeLock	android.os.PowerManager.WakeLock
    //   177	6	3	localObject	java.lang.Object
    //   89	43	4	l1	long
    //   107	15	6	l2	long
    //   64	12	8	localPendingIntent	PendingIntent
    // Exception table:
    //   from	to	target	type
    //   31	35	177	finally
    //   39	66	177	finally
    //   71	80	177	finally
    //   85	91	177	finally
    //   98	109	177	finally
    //   129	136	177	finally
    //   145	156	177	finally
    //   156	174	177	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.TrafficIntentService
 * JD-Core Version:    0.7.0.1
 */