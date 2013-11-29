package com.google.android.sidekick.main.calendar;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nullable;

public class CalendarIntentService
  extends IntentService
{
  static final String CHECK_NOTIFICATIONS_ACTION;
  static final String INITIALIZE_ACTION;
  static final String NOTIFICATION_DISMISS_ACTION = CalendarIntentService.class.getName() + ".NOTIFICATION_DISMISS_ACTION";
  private static final String TAG = Tag.getTag(CalendarIntentService.class);
  static final String UPDATE_CALENDAR_ACTION;
  static final String USER_NOTIFY_ACTION;
  static final String USER_NOTIFY_EXPIRE_ACTION;
  private static final Set<String> VALID_ACTION_SET = ImmutableSet.of(INITIALIZE_ACTION, UPDATE_CALENDAR_ACTION, CHECK_NOTIFICATIONS_ACTION, USER_NOTIFY_ACTION, USER_NOTIFY_EXPIRE_ACTION, NOTIFICATION_DISMISS_ACTION, new String[0]);
  private static final BroadcastReceiver sLocationChangedReceiver = new LocationChangedSignificantlyReceiver(null);
  private CalendarIntentHandler mHandler;
  private SidekickInjector mSidekickInjector;
  
  static
  {
    INITIALIZE_ACTION = CalendarIntentService.class.getName() + ".INIT_CALENDAR_ACTION";
    UPDATE_CALENDAR_ACTION = CalendarIntentService.class.getName() + ".UPDATE_CALENDAR_ACTION";
    CHECK_NOTIFICATIONS_ACTION = CalendarIntentService.class.getName() + ".CHECK_NOTIFICATIONS_ACTION";
    USER_NOTIFY_ACTION = CalendarIntentService.class.getName() + ".USER_NOTIFY_ACTION";
    USER_NOTIFY_EXPIRE_ACTION = CalendarIntentService.class.getName() + ".USER_NOTIFY_EXPIRE_ACTION";
  }
  
  public CalendarIntentService()
  {
    super("CalendarIntentService");
    setIntentRedelivery(false);
  }
  
  static PendingIntent createDismissPendingIntent(Context paramContext, String paramString)
  {
    Context localContext = paramContext.getApplicationContext();
    Uri localUri = new Uri.Builder().scheme("calendar_notification").authority(paramString).build();
    return PendingIntent.getService(localContext, 0, new Intent(NOTIFICATION_DISMISS_ACTION, localUri, localContext, CalendarIntentService.class), 1073741824);
  }
  
  @Nullable
  static String getHashFromIntent(Intent paramIntent)
  {
    Uri localUri = paramIntent.getData();
    if (localUri == null) {
      return null;
    }
    return localUri.getAuthority();
  }
  
  static PendingIntent getPendingIntentWithAction(Context paramContext, String paramString)
  {
    Context localContext = paramContext.getApplicationContext();
    return PendingIntent.getBroadcast(localContext, 0, new Intent(paramString, null, localContext, CalendarReceiver.class), 134217728);
  }
  
  static boolean hasPendingIntentWithAction(Context paramContext, String paramString)
  {
    Context localContext = paramContext.getApplicationContext();
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(localContext, 0, new Intent(paramString, null, localContext, CalendarReceiver.class), 536870912);
    boolean bool = false;
    if (localPendingIntent != null) {
      bool = true;
    }
    return bool;
  }
  
  static void registerForSignificantLocationChange(SidekickInjector paramSidekickInjector)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.apps.sidekick.LOCATION_CHANGED_SIGNIFICANTLY");
    paramSidekickInjector.getLocalBroadcastManager().registerReceiver(sLocationChangedReceiver, localIntentFilter);
  }
  
  static void sendIntentWithAction(Context paramContext, String paramString)
  {
    if (!VALID_ACTION_SET.contains(paramString))
    {
      Log.w(TAG, "sendIntentWithAction ignoring call with unexpected action: '" + paramString + "'");
      return;
    }
    paramContext.startService(new Intent(paramString, null, paramContext.getApplicationContext(), CalendarIntentService.class));
  }
  
  static void setAlarmForAction(Context paramContext, String paramString, int paramInt, long paramLong)
  {
    AlarmHelper localAlarmHelper = VelvetServices.get().getCoreServices().getAlarmHelper();
    PendingIntent localPendingIntent = getPendingIntentWithAction(paramContext, paramString);
    if (paramLong <= 0L)
    {
      localAlarmHelper.cancel(localPendingIntent);
      return;
    }
    localAlarmHelper.setExact(paramInt, paramLong, localPendingIntent);
  }
  
  public static void startUpdateAlarm(Context paramContext)
  {
    sendIntentWithAction(paramContext, INITIALIZE_ACTION);
  }
  
  static void stopUpdateAlarm(Context paramContext)
  {
    PendingIntent localPendingIntent1 = getPendingIntentWithAction(paramContext, UPDATE_CALENDAR_ACTION);
    PendingIntent localPendingIntent2 = getPendingIntentWithAction(paramContext, CHECK_NOTIFICATIONS_ACTION);
    PendingIntent localPendingIntent3 = getPendingIntentWithAction(paramContext, USER_NOTIFY_ACTION);
    PendingIntent localPendingIntent4 = getPendingIntentWithAction(paramContext, USER_NOTIFY_EXPIRE_ACTION);
    AlarmHelper localAlarmHelper = VelvetServices.get().getCoreServices().getAlarmHelper();
    localAlarmHelper.cancel(localPendingIntent1);
    localAlarmHelper.cancel(localPendingIntent2);
    localAlarmHelper.cancel(localPendingIntent3);
    localAlarmHelper.cancel(localPendingIntent4);
    localPendingIntent1.cancel();
    localPendingIntent2.cancel();
    localPendingIntent3.cancel();
    localPendingIntent4.cancel();
  }
  
  static void unregisterForSignificantLocationChange(SidekickInjector paramSidekickInjector)
  {
    paramSidekickInjector.getLocalBroadcastManager().unregisterReceiver(sLocationChangedReceiver);
  }
  
  public void onCreate()
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    localVelvetServices.maybeRegisterSidekickAlarms();
    this.mSidekickInjector = localVelvetServices.getSidekickInjector();
    this.mHandler = new CalendarIntentHandler(getApplicationContext(), this.mSidekickInjector.getCalendarDataProvider(), localVelvetServices.getCoreServices().getClock(), this.mSidekickInjector.getLocationOracle(), this.mSidekickInjector.getNetworkClient(), this.mSidekickInjector.getNowNotificationManager(), this.mSidekickInjector.getEntryNotificationFactory(), localVelvetServices.getCoreServices().getAlarmHelper(), localVelvetServices.getCoreServices().getPredictiveCardsPreferences());
    super.onCreate();
  }
  
  public void onDestroy()
  {
    super.onDestroy();
  }
  
  protected void onHandleIntent(Intent paramIntent)
  {
    if ((paramIntent == null) || (paramIntent.getAction() == null))
    {
      Log.w(TAG, "onHandleIntent: received unexpected null or empty Intent");
      return;
    }
    String str = paramIntent.getAction();
    if (!VALID_ACTION_SET.contains(str))
    {
      Log.w(TAG, "onHandleIntent: received Intent with unexpect action: '" + str + "'");
      return;
    }
    PowerManager.WakeLock localWakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, TAG);
    try
    {
      localWakeLock.acquire();
      boolean bool = VelvetServices.get().getCoreServices().getNowOptInSettings().stopServicesIfUserOptedOut();
      if (bool) {
        return;
      }
      this.mHandler.handle(paramIntent);
      return;
    }
    finally
    {
      localWakeLock.release();
    }
  }
  
  public static class CalendarReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction() != null) {
        CalendarIntentService.sendIntentWithAction(paramContext.getApplicationContext(), paramIntent.getAction());
      }
    }
  }
  
  private static class LocationChangedSignificantlyReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction() == "com.google.android.apps.sidekick.LOCATION_CHANGED_SIGNIFICANTLY") {
        CalendarIntentService.sendIntentWithAction(paramContext.getApplicationContext(), CalendarIntentService.CHECK_NOTIFICATIONS_ACTION);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.calendar.CalendarIntentService
 * JD-Core Version:    0.7.0.1
 */