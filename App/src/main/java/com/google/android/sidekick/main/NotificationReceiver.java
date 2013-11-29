package com.google.android.sidekick.main;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.actions.RecordActionTask;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.notifications.NowNotificationManager.NotificationType;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

public class NotificationReceiver
  extends BroadcastReceiver
{
  private static final String TAG = Tag.getTag(NotificationReceiver.class);
  private ExecutorService mBgExecutor;
  private Clock mClock;
  private EntryAdapterFactory<EntryNotification> mEntryNotificationFactory;
  private LocationOracle mLocationOracle;
  private NetworkClient mNetworkClient;
  private NowNotificationManager mNowNotificationManager;
  private GsaPreferenceController mPrefController;
  private UserInteractionLogger mUserInteractionLogger;
  
  private boolean autoDismiss(Context paramContext, EntryNotification paramEntryNotification)
  {
    boolean bool1 = this.mPrefController.getMainPreferences().getString(paramContext.getString(2131362038), paramContext.getString(2131362041)).equals(paramContext.getString(2131362043));
    boolean bool2 = false;
    if (bool1)
    {
      Sidekick.Action localAction = new Sidekick.Action();
      localAction.setType(2);
      Iterator localIterator = paramEntryNotification.getEntries().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
        new RecordActionTask(this.mNetworkClient, localEntry, localAction, this.mClock).execute(new Void[0]);
      }
      this.mNowNotificationManager.cancelNotification(paramEntryNotification.getNotificationId());
      bool2 = true;
    }
    return bool2;
  }
  
  private void initializeDependencies(Context paramContext)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    if (this.mNowNotificationManager == null) {
      this.mNowNotificationManager = localSidekickInjector.getNowNotificationManager();
    }
    if (this.mNetworkClient == null) {
      this.mNetworkClient = localSidekickInjector.getNetworkClient();
    }
    if (this.mClock == null) {
      this.mClock = localVelvetServices.getCoreServices().getClock();
    }
    if (this.mPrefController == null) {
      this.mPrefController = localVelvetServices.getPreferenceController();
    }
    if (this.mEntryNotificationFactory == null) {
      this.mEntryNotificationFactory = localSidekickInjector.getEntryNotificationFactory();
    }
    if (this.mUserInteractionLogger == null) {
      this.mUserInteractionLogger = localVelvetServices.getCoreServices().getUserInteractionLogger();
    }
    if (this.mLocationOracle == null) {
      this.mLocationOracle = localVelvetServices.getLocationOracle();
    }
    if (this.mBgExecutor == null) {
      this.mBgExecutor = localVelvetServices.getAsyncServices().getPooledBackgroundExecutorService();
    }
  }
  
  private void logNotificationAction(Context paramContext, Intent paramIntent, Collection<Sidekick.Entry> paramCollection)
  {
    String str1 = paramIntent.getStringExtra("notificationLogActionKey");
    String str2 = paramIntent.getStringExtra("notificationLoggingNameKey");
    String str3 = "NOTIFICATION_ACTION_PRESS:" + str1;
    this.mUserInteractionLogger.logAnalyticsAction(str3, str2);
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      this.mUserInteractionLogger.logMetricsAction(str3, localEntry, null);
    }
  }
  
  private void showTrafficNotificationAsync(Context paramContext, Intent paramIntent)
  {
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromIntent(paramIntent, "notification_entry");
    Location localLocation = (Location)paramIntent.getParcelableExtra("location_key");
    if ((localEntry != null) && (localLocation != null))
    {
      EntryNotification localEntryNotification = (EntryNotification)this.mEntryNotificationFactory.create(localEntry);
      if (localEntryNotification != null)
      {
        PowerManager.WakeLock localWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "NotificationRefresh_wakelock");
        localWakeLock.setReferenceCounted(false);
        localWakeLock.acquire(5000L);
        this.mBgExecutor.execute(new ShowTrafficNotificationTask(localWakeLock, paramContext.getApplicationContext(), localEntry, localEntryNotification));
      }
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    initializeDependencies(paramContext);
    if ((paramIntent == null) || (paramIntent.getAction() == null)) {}
    String str2;
    Intent localIntent;
    do
    {
      String str1;
      do
      {
        PendingIntent localPendingIntent;
        do
        {
          return;
          str1 = paramIntent.getAction();
          if (str1.equals("com.google.android.apps.sidekick.NOTIFICATION_ENTRY_ACTION"))
          {
            showTrafficNotificationAsync(paramContext, paramIntent);
            return;
          }
          if (!str1.equals("com.google.android.apps.sidekick.NOTIFICATION_DISMISS_ACTION")) {
            break;
          }
          Collection localCollection2 = ProtoUtils.getEntriesFromIntent(paramIntent, "notificationEntriesKey");
          if ((localCollection2 == null) || (localCollection2.isEmpty()))
          {
            Log.e(TAG, "Received notification dismiss without entries!");
            return;
          }
          NowNotificationManager.NotificationType localNotificationType2 = (NowNotificationManager.NotificationType)paramIntent.getSerializableExtra("notificationIdKey");
          if (localNotificationType2 == null)
          {
            Log.e(TAG, "Received notification dismiss without notification type!");
            return;
          }
          this.mNowNotificationManager.dismissNotification(localCollection2, localNotificationType2);
          localPendingIntent = (PendingIntent)paramIntent.getParcelableExtra("notificationDismissCallback");
        } while (localPendingIntent == null);
        try
        {
          localPendingIntent.send();
          return;
        }
        catch (PendingIntent.CanceledException localCanceledException)
        {
          return;
        }
      } while (!str1.equals("com.google.android.apps.sidekick.NOTIFICATION_CALLBACK_ACTION"));
      Collection localCollection1 = ProtoUtils.getEntriesFromIntent(paramIntent, "notificationEntriesKey");
      if ((localCollection1 == null) || (localCollection1.isEmpty()))
      {
        Log.e(TAG, "Received notification action press without entries!");
        return;
      }
      paramContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
      NowNotificationManager.NotificationType localNotificationType1 = (NowNotificationManager.NotificationType)paramIntent.getSerializableExtra("notificationIdKey");
      if (localNotificationType1 == null)
      {
        Log.e(TAG, "Received notification action press without notification type!");
        return;
      }
      this.mNowNotificationManager.cancelNotification(localNotificationType1);
      logNotificationAction(paramContext, paramIntent, localCollection1);
      str2 = paramIntent.getStringExtra("callback_type");
      localIntent = (Intent)paramIntent.getParcelableExtra("notification_callback");
      if ((localIntent == null) || (str2 == null)) {
        Log.e(TAG, "Received notification action press with unknown callback: " + localIntent + "(" + str2 + ")");
      }
      if ("activity".equals(str2))
      {
        paramContext.startActivity(localIntent);
        return;
      }
      if ("broadcast".equals(str2))
      {
        paramContext.sendBroadcast(localIntent);
        return;
      }
    } while (!"service".equals(str2));
    paramContext.startService(localIntent);
  }
  
  void showTrafficNotification(Context paramContext, Sidekick.Entry paramEntry, EntryNotification paramEntryNotification)
  {
    if (autoDismiss(paramContext, paramEntryNotification)) {}
    Notification localNotification;
    do
    {
      return;
      localNotification = this.mNowNotificationManager.createNotification(paramEntryNotification, null, true);
    } while (localNotification == null);
    if (paramEntryNotification.getNotificationType() == 4) {
      this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.TRAFFIC_NOTIFICATION);
    }
    for (;;)
    {
      this.mNowNotificationManager.showNotification(localNotification, paramEntryNotification.getNotificationId());
      if (!paramEntryNotification.isActiveNotification()) {
        break;
      }
      this.mNowNotificationManager.sendDeliverActiveNotification(paramEntry);
      return;
      this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION);
    }
  }
  
  class ShowTrafficNotificationTask
    implements Runnable
  {
    private final Context mAppContext;
    private final Sidekick.Entry mEntry;
    private final EntryNotification mEntryNotification;
    private final PowerManager.WakeLock mWakeLock;
    
    ShowTrafficNotificationTask(PowerManager.WakeLock paramWakeLock, Context paramContext, Sidekick.Entry paramEntry, EntryNotification paramEntryNotification)
    {
      this.mWakeLock = paramWakeLock;
      this.mAppContext = paramContext;
      this.mEntry = paramEntry;
      this.mEntryNotification = paramEntryNotification;
    }
    
    public void run()
    {
      try
      {
        NotificationReceiver.this.showTrafficNotification(this.mAppContext, this.mEntry, this.mEntryNotification);
        return;
      }
      finally
      {
        this.mWakeLock.release();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.NotificationReceiver
 * JD-Core Version:    0.7.0.1
 */