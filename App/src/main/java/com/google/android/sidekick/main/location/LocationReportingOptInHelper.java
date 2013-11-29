package com.google.android.sidekick.main.location;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class LocationReportingOptInHelper
{
  static final String GMM_LOCATION_REPORTING_OPT_IN_ACTION = "com.google.android.apps.maps.googlenav.friend.internal.OPT_IN";
  private static final String[] GMM_RESULT_MESSAGES_DBG = { "Success", "Package or certificate invalid", "Different user signed in to GMM", "No user signed in to GMM", "Unsupported version", "Invalid request", "Server error: failed to opt-in", "Cannot log into non-primary account" };
  static final int GMM_WHAT_SUCCESS = 0;
  static final int GMM_WHAT_TIMEOUT = -1;
  static final String LOCATION_OPT_IN_START_TIME_IN_MILLIS_PREF = "location_opt_in_start_time";
  static final long LOCATION_OPT_IN_TIMEOUT_MILLIS = 86400000L;
  static final long OPTIN_TIMEOUT_MILLIS = 15000L;
  private static final String TAG = Tag.getTag(LocationReportingOptInHelper.class);
  private final AlarmHelper mAlarmHelper;
  private final Clock mClock;
  private final Context mContext;
  private final GmsLocationReportingHelper mGmsLocationReportingHelper;
  private final LoginHelper mLoginHelper;
  private final Supplier<SharedPreferences> mMainPreferencesSupplier;
  private final PowerManager mPowerManager;
  private final Executor mUiExecutor;
  
  public LocationReportingOptInHelper(Context paramContext, Supplier<SharedPreferences> paramSupplier, Clock paramClock, AlarmHelper paramAlarmHelper, LoginHelper paramLoginHelper, PowerManager paramPowerManager, GmsLocationReportingHelper paramGmsLocationReportingHelper, Executor paramExecutor)
  {
    this.mContext = paramContext;
    this.mMainPreferencesSupplier = paramSupplier;
    this.mClock = paramClock;
    this.mAlarmHelper = paramAlarmHelper;
    this.mLoginHelper = paramLoginHelper;
    this.mPowerManager = paramPowerManager;
    this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
    this.mUiExecutor = paramExecutor;
  }
  
  private void cancelOptInRetryAlarm()
  {
    PendingIntent localPendingIntent = getOptInRetryIntent();
    this.mAlarmHelper.cancel(localPendingIntent);
    localPendingIntent.cancel();
    SharedPreferences.Editor localEditor = ((SharedPreferences)this.mMainPreferencesSupplier.get()).edit();
    localEditor.remove("location_opt_in_start_time");
    localEditor.apply();
  }
  
  private boolean hasOptInExpired()
  {
    long l = ((SharedPreferences)this.mMainPreferencesSupplier.get()).getLong("location_opt_in_start_time", 0L);
    return this.mClock.currentTimeMillis() - l > 86400000L;
  }
  
  private void tryGmmOptIn(Account paramAccount)
  {
    PowerManager.WakeLock localWakeLock = createWakeLock(1, "gmm_location_reporting_opt_in_handler");
    localWakeLock.acquire(15100L);
    PendingIntent localPendingIntent = getIdentifierIntent();
    GmmLocationReportingOptInHandler localGmmLocationReportingOptInHandler = new GmmLocationReportingOptInHandler(getLooper(), localWakeLock, localPendingIntent, this);
    Intent localIntent = new Intent("com.google.android.apps.maps.googlenav.friend.internal.OPT_IN");
    localIntent.addFlags(268435456);
    localIntent.putExtra("account", paramAccount);
    localIntent.putExtra("messenger", new Messenger(localGmmLocationReportingOptInHandler));
    localIntent.putExtra("sender", localPendingIntent);
    localIntent.putExtra("version", 1);
    try
    {
      this.mContext.startService(localIntent);
      label109:
      localGmmLocationReportingOptInHandler.sendEmptyMessageDelayed(-1, 15000L);
      return;
    }
    catch (SecurityException localSecurityException)
    {
      break label109;
    }
  }
  
  private void tryGmsOptIn(Account paramAccount)
  {
    final PowerManager.WakeLock localWakeLock = createWakeLock(1, "gms_location_reporting_opt_in_handler");
    localWakeLock.setReferenceCounted(false);
    localWakeLock.acquire(15100L);
    this.mGmsLocationReportingHelper.tryOptIn(paramAccount).addConsumer(new Consumer()
    {
      public boolean consume(@Nullable Integer paramAnonymousInteger)
      {
        if (paramAnonymousInteger != null) {}
        try
        {
          if (paramAnonymousInteger.intValue() == 0) {
            LocationReportingOptInHelper.this.cancelOptInRetryAlarm();
          }
          return true;
        }
        finally
        {
          localWakeLock.release();
        }
      }
    }, this.mUiExecutor);
  }
  
  PowerManager.WakeLock createWakeLock(int paramInt, String paramString)
  {
    return this.mPowerManager.newWakeLock(paramInt, paramString);
  }
  
  PendingIntent getIdentifierIntent()
  {
    return PendingIntent.getActivity(this.mContext, 0, new Intent("identity"), 1073741824);
  }
  
  Looper getLooper()
  {
    Looper localLooper = Looper.myLooper();
    Preconditions.checkNotNull(localLooper);
    return localLooper;
  }
  
  PendingIntent getOptInRetryIntent()
  {
    Intent localIntent = new Intent(this.mContext, LocationReportingOptInRetryReceiver.class);
    return PendingIntent.getBroadcast(this.mContext, 0, localIntent, 0);
  }
  
  public void optIntoLocationReportingAsync()
  {
    long l = this.mClock.currentTimeMillis();
    SharedPreferences.Editor localEditor = ((SharedPreferences)this.mMainPreferencesSupplier.get()).edit();
    localEditor.putLong("location_opt_in_start_time", l);
    localEditor.apply();
    PendingIntent localPendingIntent = getOptInRetryIntent();
    this.mAlarmHelper.setInexactRepeating(0, l, 3600000L, localPendingIntent);
  }
  
  void tryOptIn()
  {
    if (hasOptInExpired())
    {
      cancelOptInRetryAlarm();
      return;
    }
    final Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null)
    {
      Log.e(TAG, "No account to opt-in");
      return;
    }
    this.mGmsLocationReportingHelper.getReportingState(this.mLoginHelper.getAccount()).addConsumer(new Consumer()
    {
      public boolean consume(@Nullable ReportingState paramAnonymousReportingState)
      {
        if ((paramAnonymousReportingState == null) || (paramAnonymousReportingState.isDeferringToMaps())) {}
        for (int i = 1; i != 0; i = 0)
        {
          LocationReportingOptInHelper.this.tryGmmOptIn(localAccount);
          return true;
        }
        if (paramAnonymousReportingState.isAllowed())
        {
          LocationReportingOptInHelper.this.tryGmsOptIn(localAccount);
          return true;
        }
        LocationReportingOptInHelper.this.cancelOptInRetryAlarm();
        return true;
      }
    }, this.mUiExecutor);
  }
  
  static class GmmLocationReportingOptInHandler
    extends Handler
  {
    private final PendingIntent mIdentifierIntent;
    private final WeakReference<LocationReportingOptInHelper> mOptInHelper;
    private final PowerManager.WakeLock mWakeLock;
    
    public GmmLocationReportingOptInHandler(Looper paramLooper, PowerManager.WakeLock paramWakeLock, PendingIntent paramPendingIntent, LocationReportingOptInHelper paramLocationReportingOptInHelper)
    {
      super();
      this.mWakeLock = paramWakeLock;
      this.mIdentifierIntent = paramPendingIntent;
      this.mOptInHelper = new WeakReference(paramLocationReportingOptInHelper);
    }
    
    public void handleMessage(Message paramMessage)
    {
      try
      {
        PendingIntent localPendingIntent = this.mIdentifierIntent;
        if (localPendingIntent == null) {
          return;
        }
        this.mIdentifierIntent.cancel();
        if (paramMessage.what == 0)
        {
          LocationReportingOptInHelper localLocationReportingOptInHelper = (LocationReportingOptInHelper)this.mOptInHelper.get();
          if (localLocationReportingOptInHelper != null) {
            localLocationReportingOptInHelper.cancelOptInRetryAlarm();
          }
        }
        return;
      }
      finally
      {
        if (this.mWakeLock.isHeld()) {
          this.mWakeLock.release();
        }
      }
    }
  }
  
  public static class LocationReportingOptInRetryReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      VelvetServices.get().getSidekickInjector().getLocationReportingOptInHelper().tryOptIn();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationReportingOptInHelper
 * JD-Core Version:    0.7.0.1
 */