package com.google.android.sidekick.main.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri.Builder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.actions.BatchRecordActionTask;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTree.CallbackWithInterest;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.MinimumDataVersion;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public class NotificationRefreshService
  extends IntentService
{
  private static final String TAG = Tag.getTag(NotificationRefreshService.class);
  private static final Set<String> VALID_ACTIONS = ImmutableSet.of("com.google.android.apps.sidekick.notifications.REFRESH", "com.google.android.apps.sidekick.notifications.SCHEDULE_REFRESH", "com.google.android.apps.sidekick.notifications.SHOW_NOTIFICATIONS", "com.google.android.apps.sidekick.notifications.EXPIRE_NOTIFICATIONS", "com.google.android.apps.sidekick.notifications.NOTIFICATION_DISMISS_ACTION", "com.google.android.apps.sidekick.notifications.NOTIFICATION_DELETE_ACTION", new String[] { "com.google.android.apps.sidekick.notifications.NOTIFICATION_TRIGGER_ACTION", "com.google.android.apps.sidekick.notifications.INITIALIZE", "com.google.android.apps.sidekick.notifications.SHUTDOWN", "com.google.android.apps.sidekick.notifications.REFRESH_ALL_NOTIFICATIONS" });
  private static final BackoffTime sCheckNetworkBackoffTime = new BackoffTime(null);
  private AlarmHelper mAlarmHelper;
  private Clock mClock;
  private DataBackendVersionStore mDataBackendVersionStore;
  private EntryAdapterFactory<EntryNotification> mEntryNotificationFactory;
  private EntryProvider mEntryProvider;
  private NetworkClient mNetworkClient;
  private NotificationStore mNotificationStore;
  private NowNotificationManager mNowNotificationManager;
  private NowOptInSettings mNowOptInSettings;
  
  public NotificationRefreshService()
  {
    super(TAG);
  }
  
  private void deleteNotification(Intent paramIntent)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = ProtoUtils.getEntriesFromIntent(paramIntent, "notification_entries").iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)localIterator.next();
      Sidekick.Entry localEntry2 = this.mNotificationStore.getStoredEntry(localEntry1);
      if ((localEntry2 != null) && (this.mNotificationStore.deleteNotification(localEntry1)))
      {
        expireNotification(localEntry2);
        localArrayList.add(localEntry1);
      }
    }
    if (!localArrayList.isEmpty())
    {
      int i = paramIntent.getIntExtra("actions_to_execute", -1);
      if (i != -1)
      {
        BatchRecordActionTask localBatchRecordActionTask = new BatchRecordActionTask(this.mNetworkClient, localArrayList, i, this.mClock);
        if (paramIntent.getBooleanExtra("invalidate_after_action", false)) {
          localBatchRecordActionTask.setInvalidateOnSuccess(this.mEntryProvider, this.mDataBackendVersionStore);
        }
        localBatchRecordActionTask.execute(new Void[0]);
      }
      setNextExpirationAlarm();
      setNextUserNotifyAlarm();
    }
  }
  
  private void dismissNotification(Intent paramIntent)
  {
    Iterator localIterator = ProtoUtils.getEntriesFromIntent(paramIntent, "notification_entries").iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      this.mNotificationStore.markEntryNotificationDismissed(localEntry);
    }
  }
  
  private void expireNotification(Sidekick.Entry paramEntry)
  {
    EntryNotification localEntryNotification = (EntryNotification)this.mEntryNotificationFactory.create(paramEntry);
    if (localEntryNotification != null)
    {
      this.mNowNotificationManager.cancelNotification(localEntryNotification.getNotificationId());
      return;
    }
    Log.w(TAG, "unable to find the notification!");
  }
  
  private void expireNotifications()
  {
    Collection localCollection1 = this.mNotificationStore.getEntriesWithNotificationToBringDown();
    Collection localCollection2 = this.mNotificationStore.getEntriesWithNotificationCurrentlyShownAndValid();
    Iterator localIterator = Sets.difference(getNotificationTypes(localCollection1), getNotificationTypes(localCollection2)).iterator();
    while (localIterator.hasNext())
    {
      NowNotificationManager.NotificationType localNotificationType = (NowNotificationManager.NotificationType)localIterator.next();
      this.mNowNotificationManager.cancelNotification(localNotificationType);
    }
    setNextExpirationAlarm();
  }
  
  public static Intent getDeleteNotificationIntent(Context paramContext, Collection<Sidekick.Entry> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    if (!paramCollection.isEmpty()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.NOTIFICATION_DELETE_ACTION", null, paramContext, NotificationRefreshService.class);
      ProtoUtils.putEntriesInIntent(localIntent, "notification_entries", paramCollection);
      return localIntent;
    }
  }
  
  private PendingIntent getExpirationPendingIntent()
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.EXPIRE_NOTIFICATIONS", null, getApplicationContext(), NotificationRefreshService.class);
    return PendingIntent.getService(getApplicationContext(), 0, localIntent, 134217728);
  }
  
  public static PendingIntent getNotificationDismissIntent(Context paramContext, Collection<Sidekick.Entry> paramCollection, NowNotificationManager.NotificationType paramNotificationType)
  {
    Preconditions.checkNotNull(paramCollection);
    if (!paramCollection.isEmpty()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.NOTIFICATION_DISMISS_ACTION", new Uri.Builder().scheme("notification_refresh_dismiss").authority(Integer.toString(paramNotificationType.getNotificationId())).build(), paramContext, NotificationRefreshService.class);
      ProtoUtils.putEntriesInIntent(localIntent, "notification_entries", paramCollection);
      return PendingIntent.getService(paramContext, 0, localIntent, 268435456);
    }
  }
  
  private Set<NowNotificationManager.NotificationType> getNotificationTypes(Collection<Sidekick.Entry> paramCollection)
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      EntryNotification localEntryNotification = (EntryNotification)this.mEntryNotificationFactory.create(localEntry);
      if (localEntryNotification != null) {
        localHashSet.add(localEntryNotification.getNotificationId());
      }
    }
    return localHashSet;
  }
  
  private PendingIntent getNotifyPendingIntent()
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.SHOW_NOTIFICATIONS", null, getApplicationContext(), NotificationRefreshService.class);
    return PendingIntent.getService(getApplicationContext(), 0, localIntent, 134217728);
  }
  
  private PendingIntent getRefreshPendingIntent()
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.REFRESH", null, getApplicationContext(), NotificationRefreshService.class);
    return PendingIntent.getService(getApplicationContext(), 0, localIntent, 134217728);
  }
  
  public static Intent getTriggerIntent(Context paramContext, @Nullable Set<Sidekick.Entry> paramSet1, @Nullable Set<Sidekick.Entry> paramSet2)
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.notifications.NOTIFICATION_TRIGGER_ACTION");
    localIntent.setClass(paramContext, NotificationRefreshService.class);
    if ((paramSet1 != null) && (!paramSet1.isEmpty())) {
      ProtoUtils.putEntriesInIntent(localIntent, "triggered_notification_entries", paramSet1);
    }
    if ((paramSet2 != null) && (!paramSet2.isEmpty())) {
      ProtoUtils.putEntriesInIntent(localIntent, "concluded_notification_entries", paramSet2);
    }
    return localIntent;
  }
  
  private void presentPendingNotifications()
  {
    LinkedListMultimap localLinkedListMultimap = LinkedListMultimap.create();
    Iterator localIterator1 = this.mNotificationStore.getEntriesToNotify().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Entry localEntry3 = (Sidekick.Entry)localIterator1.next();
      localLinkedListMultimap.put(Integer.valueOf(localEntry3.getType()), localEntry3);
      this.mNotificationStore.markEntryNotified(localEntry3);
    }
    HashMap localHashMap = Maps.newHashMap();
    ArrayList localArrayList = Lists.newArrayList(localLinkedListMultimap.get(Integer.valueOf(43)));
    if ((localArrayList != null) && (localArrayList.size() > 1))
    {
      MultiReminderNotification localMultiReminderNotification = new MultiReminderNotification(localArrayList);
      localHashMap.put(localMultiReminderNotification.getNotificationId(), localMultiReminderNotification);
      localLinkedListMultimap.removeAll(Integer.valueOf(43));
    }
    Iterator localIterator2 = localLinkedListMultimap.values().iterator();
    while (localIterator2.hasNext())
    {
      Sidekick.Entry localEntry2 = (Sidekick.Entry)localIterator2.next();
      EntryNotification localEntryNotification2 = (EntryNotification)this.mEntryNotificationFactory.create(localEntry2);
      if (localEntryNotification2 != null)
      {
        NowNotificationManager.NotificationType localNotificationType2 = localEntryNotification2.getNotificationId();
        if (!localHashMap.containsKey(localNotificationType2)) {
          localHashMap.put(localNotificationType2, localEntryNotification2);
        }
      }
      else
      {
        Log.w(TAG, "Failed to get an EntryNotification for entry of type " + localEntry2.getType());
      }
    }
    Iterator localIterator3 = localHashMap.entrySet().iterator();
    while (localIterator3.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator3.next();
      NowNotificationManager.NotificationType localNotificationType1 = (NowNotificationManager.NotificationType)localEntry.getKey();
      EntryNotification localEntryNotification1 = (EntryNotification)localEntry.getValue();
      Notification localNotification = this.mNowNotificationManager.createNotification(localEntryNotification1, getNotificationDismissIntent(getApplicationContext(), localEntryNotification1.getEntries(), localNotificationType1), true);
      if (localNotification == null)
      {
        Log.w(TAG, "createNotification surprisingly returned null");
      }
      else
      {
        this.mNowNotificationManager.cancelNotification(localNotificationType1);
        this.mNowNotificationManager.showNotification(localNotification, localNotificationType1);
        if (localEntryNotification1.isActiveNotification())
        {
          Iterator localIterator4 = localEntryNotification1.getEntries().iterator();
          while (localIterator4.hasNext())
          {
            Sidekick.Entry localEntry1 = (Sidekick.Entry)localIterator4.next();
            this.mNowNotificationManager.sendDeliverActiveNotification(localEntry1);
          }
        }
        this.mNowNotificationManager.setLastNotificationTime();
      }
    }
    setNextExpirationAlarm();
    setNextUserNotifyAlarm();
  }
  
  private void requestNotifications()
  {
    requestNotificationsForInterests(this.mNotificationStore.getPendingRefreshInterests());
  }
  
  private void requestNotificationsForInterests(@Nullable List<Sidekick.Interest> paramList)
  {
    if ((paramList == null) || (paramList.isEmpty()))
    {
      Log.w(TAG, "Skipping notification refresh, no interests to query.");
      return;
    }
    long l1 = this.mClock.currentTimeMillis();
    Sidekick.EntryQuery localEntryQuery = new Sidekick.EntryQuery();
    Iterator localIterator1 = paramList.iterator();
    while (localIterator1.hasNext()) {
      localEntryQuery.addInterest((Sidekick.Interest)localIterator1.next());
    }
    Sidekick.ClientUserData localClientUserData = new Sidekick.ClientUserData();
    Iterator localIterator2 = this.mDataBackendVersionStore.getMinimumDataVersions().iterator();
    while (localIterator2.hasNext()) {
      localClientUserData.addMinimumDataVersion((Sidekick.MinimumDataVersion)localIterator2.next());
    }
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setEntryQuery(localEntryQuery);
    Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithLocation(localRequestPayload);
    if ((localResponsePayload == null) || (!localResponsePayload.hasEntryResponse()))
    {
      setCheckNotificationAlarm(2, this.mClock.elapsedRealtime() + sCheckNetworkBackoffTime.getRetryAlarmOffsetMillis());
      Log.w(TAG, "Failed to get response for notification query from server");
      return;
    }
    sCheckNetworkBackoffTime.clearBackoff();
    Sidekick.EntryResponse localEntryResponse = localResponsePayload.getEntryResponse();
    if (localEntryResponse.getEntryTreeCount() != paramList.size()) {
      Log.e(TAG, "got back " + localEntryResponse.getEntryTreeCount() + " entry trees for " + paramList.size() + " interests");
    }
    for (;;)
    {
      setCheckNotificationAlarm();
      return;
      if (localEntryResponse.getEntryTreeCount() != 0)
      {
        for (int i = 0; i < localEntryResponse.getEntryTreeCount(); i++)
        {
          Sidekick.Interest localInterest = (Sidekick.Interest)paramList.get(i);
          Sidekick.EntryTree localEntryTree = localEntryResponse.getEntryTree(i);
          if (localEntryTree.hasExpirationTimestampSeconds())
          {
            long l2 = Math.max(localEntryTree.getExpirationTimestampSeconds(), (300000L + l1) / 1000L);
            this.mNotificationStore.updateNextRefreshTime(l2, localInterest, true);
          }
          this.mNotificationStore.updatePendingNotifications(localEntryTree, localInterest);
          this.mNotificationStore.recordRefresh(l1 / 1000L, localInterest);
        }
        setNextUserNotifyAlarm();
      }
    }
  }
  
  private void setCheckNotificationAlarm()
  {
    Long localLong = this.mNotificationStore.getNextRefreshTimeMillis();
    if (localLong != null) {
      setCheckNotificationAlarm(0, localLong.longValue());
    }
  }
  
  private void setCheckNotificationAlarm(int paramInt, long paramLong)
  {
    this.mAlarmHelper.setExact(paramInt, paramLong, getRefreshPendingIntent());
  }
  
  private void setNextExpirationAlarm()
  {
    Long localLong = this.mNotificationStore.getNextNotificationBringDownTimeMillis();
    PendingIntent localPendingIntent = getExpirationPendingIntent();
    if (localLong == null)
    {
      this.mAlarmHelper.cancel(localPendingIntent);
      return;
    }
    this.mAlarmHelper.setExact(0, localLong.longValue(), localPendingIntent);
  }
  
  private void setNextUserNotifyAlarm()
  {
    PendingIntent localPendingIntent = getNotifyPendingIntent();
    Long localLong1 = this.mNotificationStore.getNextNotificationTimeMillis();
    if (localLong1 == null)
    {
      this.mAlarmHelper.cancel(localPendingIntent);
      return;
    }
    long l = this.mClock.currentTimeMillis();
    Long localLong2 = Long.valueOf(Math.max(localLong1.longValue(), 5000L + l));
    this.mAlarmHelper.setExact(0, localLong2.longValue(), localPendingIntent);
  }
  
  private void updateNextRefreshTime(Sidekick.EntryTree paramEntryTree)
  {
    Iterator localIterator = paramEntryTree.getCallbackWithInterestList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.EntryTree.CallbackWithInterest localCallbackWithInterest = (Sidekick.EntryTree.CallbackWithInterest)localIterator.next();
      this.mNotificationStore.updateNextRefreshTime(localCallbackWithInterest.getCallbackTimeSeconds(), localCallbackWithInterest.getInterest(), false);
    }
  }
  
  private void updateNotificationTriggers(Intent paramIntent)
  {
    Collection localCollection1 = ProtoUtils.getEntriesFromIntent(paramIntent, "triggered_notification_entries");
    Collection localCollection2 = ProtoUtils.getEntriesFromIntent(paramIntent, "concluded_notification_entries");
    if (localCollection2 != null)
    {
      Iterator localIterator = localCollection2.iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
        expireNotification(localEntry);
        this.mNotificationStore.resetNotification(localEntry);
      }
    }
    if ((localCollection1 != null) && (!localCollection1.isEmpty())) {
      presentPendingNotifications();
    }
  }
  
  public void cancelAlarms()
  {
    this.mAlarmHelper.cancel(getNotifyPendingIntent());
    this.mAlarmHelper.cancel(getRefreshPendingIntent());
    this.mAlarmHelper.cancel(getExpirationPendingIntent());
  }
  
  public void onCreate()
  {
    super.onCreate();
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    CoreSearchServices localCoreSearchServices = localVelvetServices.getCoreServices();
    this.mClock = localCoreSearchServices.getClock();
    this.mNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
    this.mNetworkClient = localSidekickInjector.getNetworkClient();
    this.mNowNotificationManager = localSidekickInjector.getNowNotificationManager();
    this.mNotificationStore = localSidekickInjector.getNotificationStore();
    this.mAlarmHelper = localCoreSearchServices.getAlarmHelper();
    this.mEntryNotificationFactory = localSidekickInjector.getEntryNotificationFactory();
    this.mDataBackendVersionStore = localSidekickInjector.getDataBackendVersionStore();
    this.mEntryProvider = localSidekickInjector.getEntryProvider();
  }
  
  protected void onHandleIntent(Intent paramIntent)
  {
    if (paramIntent == null) {}
    String str;
    do
    {
      return;
      str = paramIntent.getAction();
    } while ((!VALID_ACTIONS.contains(str)) || ((!this.mNowOptInSettings.isUserOptedIn()) && (!"com.google.android.apps.sidekick.notifications.SHUTDOWN".equals(str))));
    PowerManager.WakeLock localWakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, "NotificationRefresh_wakelock");
    for (;;)
    {
      try
      {
        localWakeLock.acquire();
        if (!this.mNotificationStore.isInitialized()) {
          this.mNotificationStore.initialize();
        }
        if ("com.google.android.apps.sidekick.notifications.SCHEDULE_REFRESH".equals(str))
        {
          if (paramIntent.hasExtra("com.google.android.apps.sidekick.notifications.NEXT_REFRESH"))
          {
            byte[] arrayOfByte = paramIntent.getByteArrayExtra("com.google.android.apps.sidekick.notifications.NEXT_REFRESH");
            Sidekick.EntryTree localEntryTree = (Sidekick.EntryTree)ProtoUtils.getFromByteArray(new Sidekick.EntryTree(), arrayOfByte);
            if (localEntryTree != null) {
              updateNextRefreshTime(localEntryTree);
            }
          }
          setCheckNotificationAlarm();
          return;
        }
        if ("com.google.android.apps.sidekick.notifications.REFRESH".equals(str))
        {
          requestNotifications();
          continue;
        }
        if (!"com.google.android.apps.sidekick.notifications.SHOW_NOTIFICATIONS".equals(str)) {
          break label188;
        }
      }
      finally
      {
        localWakeLock.release();
      }
      presentPendingNotifications();
      continue;
      label188:
      if ("com.google.android.apps.sidekick.notifications.INITIALIZE".equals(str))
      {
        this.mNotificationStore.clearNotifiedMarkers();
        presentPendingNotifications();
        setCheckNotificationAlarm();
        setNextUserNotifyAlarm();
        setNextExpirationAlarm();
      }
      else if ("com.google.android.apps.sidekick.notifications.EXPIRE_NOTIFICATIONS".equals(str))
      {
        expireNotifications();
      }
      else if ("com.google.android.apps.sidekick.notifications.NOTIFICATION_DISMISS_ACTION".equals(str))
      {
        dismissNotification(paramIntent);
      }
      else if ("com.google.android.apps.sidekick.notifications.NOTIFICATION_DELETE_ACTION".equals(str))
      {
        deleteNotification(paramIntent);
      }
      else if ("com.google.android.apps.sidekick.notifications.NOTIFICATION_TRIGGER_ACTION".equals(str))
      {
        updateNotificationTriggers(paramIntent);
      }
      else if ("com.google.android.apps.sidekick.notifications.REFRESH_ALL_NOTIFICATIONS".equals(str))
      {
        refreshPendingNotifications();
      }
      else if ("com.google.android.apps.sidekick.notifications.SHUTDOWN".equals(str))
      {
        cancelAlarms();
        this.mNotificationStore.clearAll();
      }
    }
  }
  
  public void refreshPendingNotifications()
  {
    requestNotificationsForInterests(this.mNotificationStore.getStoredNotificationInterests());
  }
  
  private static class BackoffTime
  {
    private final int[] mBackoffTimesMins = { 1, 3, 5, 15, 30 };
    private volatile int mLastBackoffIdx = -1;
    private final int mRandomVarianceSecs = new Random().nextInt(30);
    
    void clearBackoff()
    {
      this.mLastBackoffIdx = -1;
    }
    
    long getRetryAlarmOffsetMillis()
    {
      int i = 1 + this.mLastBackoffIdx;
      this.mLastBackoffIdx = i;
      int j = Math.min(i, -1 + this.mBackoffTimesMins.length);
      return 60000L * this.mBackoffTimesMins[j] + 1000L * this.mRandomVarianceSecs;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NotificationRefreshService
 * JD-Core Version:    0.7.0.1
 */