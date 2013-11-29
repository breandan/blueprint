package com.google.android.sidekick.main.notifications;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Notification.Style;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.NotificationReceiver;
import com.google.android.sidekick.main.actions.DismissNotificationAction;
import com.google.android.sidekick.main.actions.RecordActionTask;
import com.google.android.sidekick.main.contextprovider.CardRenderingContextProviders;
import com.google.android.sidekick.main.contextprovider.EntryRenderingContextAdapter;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.NotificationManagerInjectable;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class NowNotificationManagerImpl
  implements NowNotificationManager
{
  private final Context mAppContext;
  private final Clock mClock;
  private final EntryProvider mEntryProvider;
  private final LocationOracle mLocationOracle;
  private final NetworkClient mNetworkClient;
  private final NotificationManagerInjectable mNotificationManager;
  private final PendingIntentFactory mPendingIntentFactory;
  private final GsaPreferenceController mPrefController;
  private final EntryAdapterFactory<EntryRenderingContextAdapter> mRenderingContextAdapterFactory;
  private final CardRenderingContextProviders mRenderingContextProviders;
  private final UserInteractionLogger mUserInteractionLogger;
  
  public NowNotificationManagerImpl(Context paramContext, Clock paramClock, EntryProvider paramEntryProvider, GsaPreferenceController paramGsaPreferenceController, UserInteractionLogger paramUserInteractionLogger, NotificationManagerInjectable paramNotificationManagerInjectable, NetworkClient paramNetworkClient, PendingIntentFactory paramPendingIntentFactory, EntryAdapterFactory<EntryRenderingContextAdapter> paramEntryAdapterFactory, LocationOracle paramLocationOracle, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mClock = paramClock;
    this.mEntryProvider = paramEntryProvider;
    this.mPrefController = paramGsaPreferenceController;
    this.mUserInteractionLogger = paramUserInteractionLogger;
    this.mNotificationManager = paramNotificationManagerInjectable;
    this.mNetworkClient = paramNetworkClient;
    this.mPendingIntentFactory = paramPendingIntentFactory;
    this.mRenderingContextAdapterFactory = paramEntryAdapterFactory;
    this.mLocationOracle = paramLocationOracle;
    this.mRenderingContextProviders = paramCardRenderingContextProviders;
  }
  
  private void addAction(Notification.Builder paramBuilder, EntryNotification paramEntryNotification, NotificationAction paramNotificationAction, int paramInt)
  {
    int i = paramNotificationAction.getActionIcon();
    String str = paramNotificationAction.getActionString(this.mAppContext);
    Intent localIntent = new Intent(this.mAppContext, NotificationReceiver.class);
    localIntent.setAction("com.google.android.apps.sidekick.NOTIFICATION_CALLBACK_ACTION");
    localIntent.setData(Uri.parse("notification_action://" + paramEntryNotification.getNotificationId() + "_" + paramInt));
    ProtoUtils.putEntriesInIntent(localIntent, "notificationEntriesKey", paramEntryNotification.getEntries());
    localIntent.putExtra("notificationIdKey", paramEntryNotification.getNotificationId());
    localIntent.putExtra("notificationLogActionKey", paramNotificationAction.getLogString());
    localIntent.putExtra("notificationLoggingNameKey", paramEntryNotification.getLoggingName());
    localIntent.putExtra("notification_callback", paramNotificationAction.getCallbackIntent(this.mAppContext));
    localIntent.putExtra("callback_type", paramNotificationAction.getCallbackType());
    paramBuilder.addAction(i, str, this.mPendingIntentFactory.getBroadcast(0, localIntent, 134217728));
  }
  
  private void addDeletePendingIntent(Notification.Builder paramBuilder, EntryNotification paramEntryNotification, @Nullable PendingIntent paramPendingIntent)
  {
    Intent localIntent = new Intent("com.google.android.apps.sidekick.NOTIFICATION_DISMISS_ACTION", null, this.mAppContext, NotificationReceiver.class);
    localIntent.setData(Uri.parse("notification_id://" + paramEntryNotification.getNotificationId()));
    ProtoUtils.putEntriesInIntent(localIntent, "notificationEntriesKey", paramEntryNotification.getEntries());
    localIntent.putExtra("notificationIdKey", paramEntryNotification.getNotificationId());
    if (paramPendingIntent != null) {
      localIntent.putExtra("notificationDismissCallback", paramPendingIntent);
    }
    paramBuilder.setDeleteIntent(this.mPendingIntentFactory.getBroadcast(0, localIntent, 1207959552));
  }
  
  public void cancelAll()
  {
    this.mNotificationManager.cancelAll();
  }
  
  public void cancelNotification(NowNotificationManager.NotificationType paramNotificationType)
  {
    this.mNotificationManager.cancel(paramNotificationType.getNotificationId());
  }
  
  public Notification createNotification(EntryNotification paramEntryNotification, @Nullable PendingIntent paramPendingIntent, boolean paramBoolean)
  {
    int i = paramEntryNotification.getNotificationType();
    if (i == -1) {
      return null;
    }
    Location localLocation = LocationUtilities.sidekickLocationToAndroidLocation(this.mEntryProvider.getLastRefreshLocation());
    CardRenderingContext localCardRenderingContext = new CardRenderingContext(this.mLocationOracle.getBestLocation(), localLocation);
    Iterator localIterator = paramEntryNotification.getEntries().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      EntryRenderingContextAdapter localEntryRenderingContextAdapter = (EntryRenderingContextAdapter)this.mRenderingContextAdapterFactory.create(localEntry);
      if (localEntryRenderingContextAdapter != null) {
        localEntryRenderingContextAdapter.addTypeSpecificRenderingContext(localCardRenderingContext, this.mRenderingContextProviders);
      }
    }
    if (i == 4)
    {
      if (Build.VERSION.SDK_INT < 16) {
        return null;
      }
      if (paramEntryNotification.getNotificationContentTitle(this.mAppContext, localCardRenderingContext) == null) {
        return null;
      }
    }
    Notification.Builder localBuilder = new Notification.Builder(this.mAppContext);
    setupNotification(localCardRenderingContext, localBuilder, paramEntryNotification, paramPendingIntent, paramBoolean);
    return localBuilder.build();
  }
  
  public void dismissNotification(Collection<Sidekick.Entry> paramCollection, NowNotificationManager.NotificationType paramNotificationType)
  {
    cancelNotification(paramNotificationType);
    Sidekick.Entry localEntry;
    Sidekick.Action localAction;
    do
    {
      Iterator localIterator1 = paramCollection.iterator();
      Iterator localIterator2;
      while (!localIterator2.hasNext())
      {
        if (!localIterator1.hasNext()) {
          break;
        }
        localEntry = (Sidekick.Entry)localIterator1.next();
        localIterator2 = localEntry.getEntryActionList().iterator();
      }
      localAction = (Sidekick.Action)localIterator2.next();
    } while (localAction.getType() != 2);
    new DismissNotificationAction(this.mAppContext, localEntry, localAction, this.mNetworkClient, this.mClock).run();
  }
  
  public long getLastNotificationTime()
  {
    return this.mPrefController.getMainPreferences().getLong("last_notification_time", 0L);
  }
  
  public void sendDeliverActiveNotification(Sidekick.Entry paramEntry)
  {
    if (paramEntry == null) {
      return;
    }
    Iterator localIterator = paramEntry.getEntryActionList().iterator();
    Sidekick.Action localAction;
    do
    {
      boolean bool = localIterator.hasNext();
      localObject = null;
      if (!bool) {
        break;
      }
      localAction = (Sidekick.Action)localIterator.next();
    } while (localAction.getType() != 12);
    Object localObject = localAction;
    if (localObject == null) {
      localObject = new Sidekick.Action().setType(12);
    }
    new RecordActionTask(this.mNetworkClient, paramEntry, (Sidekick.Action)localObject, this.mClock).execute(new Void[0]);
  }
  
  public void setLastNotificationTime()
  {
    this.mPrefController.getMainPreferences().edit().putLong("last_notification_time", this.mClock.currentTimeMillis()).apply();
  }
  
  void setupLowPriorityNotification(Notification.Builder paramBuilder, EntryNotification paramEntryNotification)
  {
    int i = this.mEntryProvider.getTotalEntryCount();
    String str = null;
    if (i > 0)
    {
      Resources localResources = this.mAppContext.getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(i);
      str = localResources.getQuantityString(2131558432, i, arrayOfObject);
    }
    if (!TextUtils.isEmpty(str)) {
      paramBuilder.setContentInfo(str);
    }
    paramBuilder.setPriority(-2);
  }
  
  void setupNormalNotification(CardRenderingContext paramCardRenderingContext, Notification.Builder paramBuilder, EntryNotification paramEntryNotification, boolean paramBoolean)
  {
    int i = 4;
    SharedPreferencesExt localSharedPreferencesExt;
    if ((paramBoolean) && (paramEntryNotification.isActiveNotification()))
    {
      localSharedPreferencesExt = this.mPrefController.getMainPreferences();
      String str = localSharedPreferencesExt.getString(this.mAppContext.getString(2131362110), null);
      if (str == null) {
        break label123;
      }
      paramBuilder.setSound(Uri.parse(str));
    }
    for (;;)
    {
      if (localSharedPreferencesExt.getBoolean(this.mAppContext.getString(2131362109), true)) {
        i |= 0x2;
      }
      paramBuilder.setDefaults(i);
      paramBuilder.setTicker(paramEntryNotification.getNotificationTickerText(this.mAppContext, paramCardRenderingContext));
      this.mUserInteractionLogger.logUiActionOnEntryNotification("NOTIFY", paramEntryNotification);
      return;
      label123:
      i |= 0x1;
    }
  }
  
  void setupNotification(CardRenderingContext paramCardRenderingContext, Notification.Builder paramBuilder, EntryNotification paramEntryNotification, @Nullable PendingIntent paramPendingIntent, boolean paramBoolean)
  {
    paramBuilder.setOnlyAlertOnce(true);
    paramBuilder.setAutoCancel(true);
    paramBuilder.setWhen(this.mClock.currentTimeMillis());
    paramBuilder.setContentIntent(paramEntryNotification.getNotificationContentIntent(this.mAppContext));
    paramBuilder.setSmallIcon(paramEntryNotification.getNotificationSmallIcon());
    paramBuilder.setContentTitle(paramEntryNotification.getNotificationContentTitle(this.mAppContext, paramCardRenderingContext));
    CharSequence localCharSequence = paramEntryNotification.getNotificationContentText(this.mAppContext, paramCardRenderingContext);
    if (!TextUtils.isEmpty(localCharSequence)) {
      paramBuilder.setContentText(localCharSequence);
    }
    Notification.Style localStyle = paramEntryNotification.getNotificationStyle();
    if (localStyle != null) {
      paramBuilder.setStyle(localStyle);
    }
    addDeletePendingIntent(paramBuilder, paramEntryNotification, paramPendingIntent);
    int i = 0;
    Iterator localIterator = paramEntryNotification.getActions(paramCardRenderingContext).iterator();
    while (localIterator.hasNext())
    {
      NotificationAction localNotificationAction = (NotificationAction)localIterator.next();
      if (localNotificationAction.isActive())
      {
        int j = i + 1;
        addAction(paramBuilder, paramEntryNotification, localNotificationAction, i);
        i = j;
      }
    }
    if (paramEntryNotification.getNotificationType() == 4)
    {
      setupLowPriorityNotification(paramBuilder, paramEntryNotification);
      return;
    }
    setupNormalNotification(paramCardRenderingContext, paramBuilder, paramEntryNotification, paramBoolean);
  }
  
  public void showNotification(Notification paramNotification, NowNotificationManager.NotificationType paramNotificationType)
  {
    this.mNotificationManager.notify(paramNotificationType.getNotificationId(), paramNotification);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NowNotificationManagerImpl
 * JD-Core Version:    0.7.0.1
 */