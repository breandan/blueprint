package com.google.android.search.core.google;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableMap;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Nullable;

@SuppressLint({"HandlerLeak"})
public class UserInteractionLogger
{
  private static final Map<String, Integer> METRIC_ACTION_MAP = ImmutableMap.of("NOTIFICATION_CLICK", Integer.valueOf(23), "NOTIFICATION_ACTION_PRESS", Integer.valueOf(24), "WIDGET_PRESS", Integer.valueOf(27));
  private static final UserInteractionTimer NO_OP_INTERACTION_TIMER = new UserInteractionTimer()
  {
    public void timingComplete(@Nullable String paramAnonymousString) {}
  };
  private final Context mAppContext;
  private final Clock mClock;
  private final GoogleAnalytics mGaInstance;
  private long mLastSessionEndedMillis;
  private long mLastSessionStartedMillis;
  private String mLastViewName;
  private final GsaPreferenceController mPrefController;
  private final Random mRandom;
  private final SessionTimeoutHandler mSessionTimeoutHandler;
  private final Tracker mTracker;
  
  public UserInteractionLogger(Context paramContext, Clock paramClock, GsaPreferenceController paramGsaPreferenceController, SearchConfig paramSearchConfig)
  {
    this.mClock = paramClock;
    this.mSessionTimeoutHandler = new SessionTimeoutHandler(null);
    this.mGaInstance = GoogleAnalytics.getInstance(paramContext);
    this.mAppContext = paramContext;
    this.mPrefController = paramGsaPreferenceController;
    this.mRandom = new Random();
    lowerGaThreadPriority(this.mGaInstance);
    GAServiceManager.getInstance().setDispatchPeriod(-1);
    this.mTracker = this.mGaInstance.getTracker("UA-25271179-3");
    double d = paramSearchConfig.getGoogleAnalyticsSampleRate();
    if (d < 0.0D) {
      d = 0.0D;
    }
    if (d > 100.0D) {
      d = 100.0D;
    }
    this.mTracker.setSampleRate(d);
  }
  
  private void endSession()
  {
    this.mSessionTimeoutHandler.removeMessages(1);
    if (isInSession())
    {
      this.mLastSessionEndedMillis = this.mClock.currentTimeMillis();
      this.mLastViewName = null;
      sendEmptyEvent();
    }
  }
  
  @Nullable
  private Sidekick.Action getEntryActionForActionType(Sidekick.Entry paramEntry, int paramInt)
  {
    Iterator localIterator = paramEntry.getEntryActionList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if (localAction.getType() == paramInt) {
        return localAction;
      }
    }
    return null;
  }
  
  private ExecutedUserActionStore getExecutedUserActionStore()
  {
    return VelvetServices.get().getSidekickInjector().getExecutedUserActionStore();
  }
  
  private boolean isInSession()
  {
    return this.mLastSessionEndedMillis < this.mLastSessionStartedMillis;
  }
  
  private static void lowerGaThreadPriority(GoogleAnalytics paramGoogleAnalytics)
  {
    try
    {
      Field localField = paramGoogleAnalytics.getClass().getDeclaredField("mThread");
      localField.setAccessible(true);
      Object localObject = localField.get(paramGoogleAnalytics);
      ((LinkedBlockingQueue)localObject.getClass().getDeclaredMethod("getQueue", new Class[0]).invoke(localObject, new Object[0])).add(new Runnable()
      {
        public void run()
        {
          Process.setThreadPriority(Process.myTid(), 10);
        }
      });
      return;
    }
    catch (Exception localException)
    {
      Log.w("QSB.UserInteractionLogger", "Failed to lower GA thread priority: " + localException);
    }
  }
  
  private void sendEmptyEvent()
  {
    this.mTracker.sendEvent("", "", "", null);
  }
  
  private void startNewSession()
  {
    this.mLastSessionStartedMillis = this.mClock.currentTimeMillis();
    this.mTracker.setStartSession(true);
    this.mLastViewName = null;
    sendEmptyEvent();
  }
  
  public boolean cancelMetricsAction(Sidekick.Entry paramEntry, int paramInt)
  {
    Sidekick.Action localAction = getEntryActionForActionType(paramEntry, paramInt);
    if (localAction == null) {
      return false;
    }
    return getExecutedUserActionStore().removeDeferredAction(paramEntry, localAction);
  }
  
  public UserInteractionTimer createRandomSamplingTimer(String paramString)
  {
    SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
    String str = "uil_limiter_timing_" + paramString;
    long l1 = this.mClock.currentTimeMillis();
    if (l1 > localSharedPreferencesExt.getLong(str, 0L))
    {
      long l2 = 64800000L + l1 + this.mRandom.nextInt(43200000);
      localSharedPreferencesExt.edit().putLong(str, l2).apply();
      return createTimer(paramString);
    }
    return NO_OP_INTERACTION_TIMER;
  }
  
  public UserInteractionTimer createTimer(String paramString)
  {
    return new UserInteractionTimerImpl(paramString);
  }
  
  public void flushEvents()
  {
    GAServiceManager.getInstance().dispatch();
  }
  
  public void logAnalyticsAction(String paramString1, String paramString2)
  {
    this.mTracker.sendEvent("UI_ACTION", paramString1, paramString2, null);
  }
  
  public void logAnalyticsAction(String paramString1, String paramString2, long paramLong)
  {
    this.mTracker.sendEvent("UI_ACTION", paramString1, paramString2, Long.valueOf(paramLong));
  }
  
  public void logInternalAction(String paramString1, String paramString2)
  {
    this.mTracker.sendEvent("INTERNAL", paramString1, paramString2, null);
  }
  
  public void logInternalActionOncePerDay(String paramString1, String paramString2)
  {
    SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
    String str = "uil_limiter_internal_" + paramString1;
    long l = this.mClock.currentTimeMillis() / 86400000L;
    if (l > localSharedPreferencesExt.getLong(str, 0L))
    {
      logInternalAction(paramString1, paramString2);
      localSharedPreferencesExt.edit().putLong(str, l).apply();
    }
  }
  
  public void logMetricsAction(Sidekick.Entry paramEntry, int paramInt, @Nullable Sidekick.ClickAction paramClickAction)
  {
    Sidekick.Action localAction = getEntryActionForActionType(paramEntry, paramInt);
    if (localAction == null) {
      return;
    }
    if (Feature.SHOW_LOGGING_TOASTS.isEnabled()) {
      Toast.makeText(this.mAppContext, "Logged: " + paramInt, 0).show();
    }
    ExecutedUserActionStore localExecutedUserActionStore = getExecutedUserActionStore();
    if (paramClickAction != null)
    {
      localExecutedUserActionStore.saveClickAction(paramEntry, localAction, paramClickAction);
      return;
    }
    localExecutedUserActionStore.saveAction(paramEntry, localAction);
  }
  
  public void logMetricsAction(String paramString, Sidekick.Entry paramEntry, @Nullable Sidekick.ClickAction paramClickAction)
  {
    Integer localInteger = (Integer)METRIC_ACTION_MAP.get(paramString);
    if (localInteger != null) {
      logMetricsAction(paramEntry, localInteger.intValue(), paramClickAction);
    }
  }
  
  public void logUiActionOnEntryAdapter(String paramString, EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    logAnalyticsAction(paramString, paramEntryCardViewAdapter.getLoggingName());
    logMetricsAction(paramString, paramEntryCardViewAdapter.getEntry(), null);
  }
  
  public void logUiActionOnEntryNotification(String paramString, EntryNotification paramEntryNotification)
  {
    logAnalyticsAction(paramString, paramEntryNotification.getLoggingName());
    Iterator localIterator = paramEntryNotification.getEntries().iterator();
    while (localIterator.hasNext()) {
      logMetricsAction(paramString, (Sidekick.Entry)localIterator.next(), null);
    }
  }
  
  public void logView(String paramString)
  {
    if (paramString == null) {}
    while (paramString.equals(this.mLastViewName)) {
      return;
    }
    this.mLastViewName = paramString;
    this.mTracker.sendView(paramString);
  }
  
  public void onSessionStart()
  {
    this.mSessionTimeoutHandler.removeMessages(1);
    if (!isInSession()) {
      startNewSession();
    }
  }
  
  public void onSessionStop()
  {
    this.mSessionTimeoutHandler.sendEmptyMessageDelayed(1, 300000L);
  }
  
  public void queueMetricsAction(Sidekick.Entry paramEntry, int paramInt)
  {
    Sidekick.Action localAction = getEntryActionForActionType(paramEntry, paramInt);
    if (localAction == null) {
      return;
    }
    getExecutedUserActionStore().addDeferredAction(paramEntry, localAction);
  }
  
  private class SessionTimeoutHandler
    extends Handler
  {
    private SessionTimeoutHandler()
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1) {
        UserInteractionLogger.this.endSession();
      }
    }
  }
  
  public static abstract interface UserInteractionTimer
  {
    public abstract void timingComplete(@Nullable String paramString);
  }
  
  private class UserInteractionTimerImpl
    implements UserInteractionLogger.UserInteractionTimer
  {
    private final long mStartTime;
    private final String mTimingName;
    
    public UserInteractionTimerImpl(String paramString)
    {
      this.mTimingName = paramString;
      this.mStartTime = UserInteractionLogger.this.mClock.currentTimeMillis();
    }
    
    public void timingComplete(@Nullable String paramString)
    {
      long l = UserInteractionLogger.this.mClock.currentTimeMillis() - this.mStartTime;
      UserInteractionLogger.this.mTracker.sendTiming("TIMING", l, this.mTimingName, paramString);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.UserInteractionLogger
 * JD-Core Version:    0.7.0.1
 */