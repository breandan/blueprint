package com.google.android.sidekick.main.calendar;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.notifications.NowNotificationManager.NotificationType;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.ClientUserData;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.UploadCalendarData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

class CalendarIntentHandler
{
  private static final String TAG = Tag.getTag(CalendarIntentHandler.class);
  private static final BackoffTime sCheckNetworkBackoffTime = new BackoffTime(null);
  private final AlarmHelper mAlarmHelper;
  private final Context mAppContext;
  private final CalendarAccessor mCalendarAccessor;
  private final CalendarDataProvider mCalendarDataProvider;
  private final Clock mClock;
  private final EntryAdapterFactory<EntryNotification> mEntryNotificationFactory;
  private final LocationOracle mLocationOracle;
  private final NetworkClient mNetworkClient;
  private final NowNotificationManager mNowNotificationManager;
  private final PredictiveCardsPreferences mPredictiveCardsPreferences;
  
  CalendarIntentHandler(Context paramContext, CalendarDataProvider paramCalendarDataProvider, Clock paramClock, LocationOracle paramLocationOracle, NetworkClient paramNetworkClient, NowNotificationManager paramNowNotificationManager, EntryAdapterFactory<EntryNotification> paramEntryAdapterFactory, AlarmHelper paramAlarmHelper, PredictiveCardsPreferences paramPredictiveCardsPreferences)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mClock = paramClock;
    this.mCalendarAccessor = new CalendarAccessor(this.mAppContext);
    this.mLocationOracle = paramLocationOracle;
    this.mNetworkClient = paramNetworkClient;
    this.mNowNotificationManager = paramNowNotificationManager;
    this.mEntryNotificationFactory = paramEntryAdapterFactory;
    this.mAlarmHelper = paramAlarmHelper;
    this.mPredictiveCardsPreferences = paramPredictiveCardsPreferences;
  }
  
  private Sidekick.RequestPayload buildNotificationRequestPayload(Iterable<Sidekick.UploadCalendarData> paramIterable, boolean paramBoolean)
  {
    ArrayList localArrayList = Lists.newArrayList(paramIterable);
    if (localArrayList.isEmpty()) {
      return null;
    }
    if (!this.mLocationOracle.hasLocation())
    {
      Log.e(TAG, "All locations are stale; not sending request to server");
      return null;
    }
    Sidekick.ClientUserData localClientUserData = new Sidekick.ClientUserData();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext()) {
      localClientUserData.addCalendarData((Sidekick.UploadCalendarData)localIterator.next());
    }
    localClientUserData.setGettingCalendarEventsFailed(paramBoolean);
    return new Sidekick.RequestPayload().setEntryQuery(new Sidekick.EntryQuery().addInterest(new Sidekick.Interest().setTargetDisplay(3).addEntryTypeRestrict(14)).setClientUserData(localClientUserData));
  }
  
  private boolean executeNotificationRequest()
  {
    Sidekick.RequestPayload localRequestPayload = buildNotificationRequestPayload(this.mCalendarDataProvider.getCalendarDataForNotify(), this.mCalendarDataProvider.didGettingEventsFail());
    if (localRequestPayload == null) {}
    Sidekick.EntryResponse localEntryResponse;
    do
    {
      return false;
      Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithLocation(localRequestPayload);
      if ((localResponsePayload == null) || (!localResponsePayload.hasEntryResponse()))
      {
        setCheckNotificationAlarm(2, this.mClock.elapsedRealtime() + sCheckNetworkBackoffTime.getRetryAlarmOffsetMillis());
        Log.w(TAG, "Failed to get response for notification query from server");
        return false;
      }
      sCheckNetworkBackoffTime.clearBackoff();
      localEntryResponse = localResponsePayload.getEntryResponse();
    } while ((localEntryResponse.getEntryTreeCount() == 0) || (!localEntryResponse.getEntryTree(0).hasRoot()) || (localEntryResponse.getEntryTree(0).getRoot().getEntryCount() == 0));
    Sidekick.EntryTree localEntryTree = localEntryResponse.getEntryTree(0);
    if (localEntryTree.hasExpirationTimestampSeconds()) {
      setCheckNotificationAlarm(0, Math.max(1000L * localEntryTree.getExpirationTimestampSeconds(), 300000L + this.mClock.currentTimeMillis()));
    }
    boolean bool = this.mCalendarDataProvider.updateWithNewEntryTreeFromServer(localEntryTree);
    setNextUserNotifyAlarm();
    return bool;
  }
  
  private void presentPendingNotifications()
  {
    int i = this.mPredictiveCardsPreferences.getNextMeetingNotificationType();
    int j = 0;
    Notification localNotification = null;
    Sidekick.Entry localEntry = null;
    NowNotificationManager.NotificationType localNotificationType = null;
    long l = 0L;
    Iterator localIterator = this.mCalendarDataProvider.getNotifyingCalendarData().iterator();
    if (localIterator.hasNext())
    {
      Calendar.CalendarData localCalendarData = (Calendar.CalendarData)localIterator.next();
      if ((i == -1) || (localCalendarData.getEventData().getEndTimeSeconds() < this.mClock.currentTimeMillis() / 1000L)) {}
      for (;;)
      {
        label89:
        this.mCalendarDataProvider.markEventAsNotified(localCalendarData.getEventData().getProviderId());
        j = 1;
        break;
        if (localCalendarData.getServerData().hasEntryFromServer()) {}
        EntryNotification localEntryNotification;
        for (localEntry = localCalendarData.getServerData().getEntryFromServer();; localEntry = new Sidekick.Entry().setType(14).setCalendarEntry(new Sidekick.CalendarEntry().setHash(localCalendarData.getServerData().getServerHash())))
        {
          if (!localEntry.hasNotification()) {
            localEntry.setNotification(new Sidekick.Notification().setType(2));
          }
          localEntryNotification = (EntryNotification)this.mEntryNotificationFactory.create(localEntry);
          if (localEntryNotification == null) {
            break label89;
          }
          localNotification = this.mNowNotificationManager.createNotification(localEntryNotification, CalendarIntentService.createDismissPendingIntent(this.mAppContext, localCalendarData.getServerData().getServerHash()), true);
          if (localNotification != null) {
            break label262;
          }
          Log.w(TAG, "createNotification surprisingly returned null");
          break;
        }
        label262:
        localNotificationType = localEntryNotification.getNotificationId();
        l = localCalendarData.getEventData().getEndTimeSeconds();
      }
    }
    if (localNotification != null)
    {
      this.mNowNotificationManager.cancelNotification(localNotificationType);
      this.mNowNotificationManager.showNotification(localNotification, localNotificationType);
      this.mNowNotificationManager.sendDeliverActiveNotification(localEntry);
      setClearNotificationAlarm(1000L * l);
      this.mNowNotificationManager.setLastNotificationTime();
    }
    if (j != 0) {
      setNextUserNotifyAlarm();
    }
  }
  
  private void setCheckNotificationAlarm(int paramInt, long paramLong)
  {
    CalendarIntentService.setAlarmForAction(this.mAppContext, CalendarIntentService.CHECK_NOTIFICATIONS_ACTION, paramInt, paramLong);
  }
  
  private void setClearNotificationAlarm(long paramLong)
  {
    CalendarIntentService.setAlarmForAction(this.mAppContext, CalendarIntentService.USER_NOTIFY_EXPIRE_ACTION, 0, paramLong);
  }
  
  private void setNextUserNotifyAlarm()
  {
    Long localLong = this.mCalendarDataProvider.getEarliestNotificationTimeSecs();
    if (localLong != null) {}
    for (long l = 1000L * localLong.longValue();; l = 0L)
    {
      setUserNotifyAlarm(l);
      return;
    }
  }
  
  private void setUserNotifyAlarm(long paramLong)
  {
    CalendarIntentService.setAlarmForAction(this.mAppContext, CalendarIntentService.USER_NOTIFY_ACTION, 0, paramLong);
  }
  
  private boolean updateCalendarDataProvider()
  {
    Collection localCollection1 = this.mCalendarAccessor.getUpcomingEvents(this.mClock.currentTimeMillis(), 86400000);
    Collection localCollection2 = this.mCalendarAccessor.getCalendarsList();
    boolean bool = this.mCalendarDataProvider.updateWithNewEventData(localCollection1, localCollection2, this.mCalendarAccessor.didGettingEventsFail());
    if (bool) {}
    return bool;
  }
  
  void handle(Intent paramIntent)
  {
    
    if (CalendarIntentService.USER_NOTIFY_ACTION.equals(paramIntent.getAction())) {
      presentPendingNotifications();
    }
    Calendar.CalendarData localCalendarData;
    do
    {
      String str;
      do
      {
        return;
        if (!CalendarIntentService.NOTIFICATION_DISMISS_ACTION.equals(paramIntent.getAction())) {
          break;
        }
        str = CalendarIntentService.getHashFromIntent(paramIntent);
      } while (str == null);
      localCalendarData = this.mCalendarDataProvider.getCalendarDataByServerHash(str);
    } while ((localCalendarData == null) || (!localCalendarData.hasEventData()) || (!localCalendarData.getEventData().hasProviderId()));
    this.mCalendarDataProvider.markEventNotificationAsDismissed(localCalendarData.getEventData().getProviderId());
    return;
    if (CalendarIntentService.USER_NOTIFY_EXPIRE_ACTION.equals(paramIntent.getAction()))
    {
      this.mNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.CALENDAR_TIME_TO_LEAVE_NOTIFICATION);
      return;
    }
    if ((CalendarIntentService.INITIALIZE_ACTION.equals(paramIntent.getAction())) && (!CalendarIntentService.hasPendingIntentWithAction(this.mAppContext, CalendarIntentService.UPDATE_CALENDAR_ACTION))) {}
    for (int i = 1;; i = 0)
    {
      if (i != 0)
      {
        this.mCalendarDataProvider.clearAllEventNotifiedMarkers();
        setNextUserNotifyAlarm();
      }
      if ((updateCalendarDataProvider()) || (CalendarIntentService.CHECK_NOTIFICATIONS_ACTION.equals(paramIntent.getAction()))) {
        executeNotificationRequest();
      }
      if ((i == 0) && (!CalendarIntentService.UPDATE_CALENDAR_ACTION.equals(paramIntent.getAction()))) {
        break;
      }
      this.mAlarmHelper.scheduleNextAlarmOccurrence(900000L, CalendarIntentService.getPendingIntentWithAction(this.mAppContext, CalendarIntentService.UPDATE_CALENDAR_ACTION), "Calendar_alarm_UPDATE_CALENDAR_ACTION", false);
      return;
    }
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
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarIntentHandler
 * JD-Core Version:    0.7.0.1
 */