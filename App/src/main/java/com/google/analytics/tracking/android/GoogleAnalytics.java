package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GoogleAnalytics
  implements TrackerHandler
{
  private static GoogleAnalytics sInstance;
  private AdHitIdGenerator mAdHitIdGenerator;
  private volatile Boolean mAppOptOut;
  private volatile String mClientId;
  private Context mContext;
  private Tracker mDefaultTracker;
  private String mLastTrackingId;
  private AnalyticsThread mThread;
  private final Map<String, Tracker> mTrackers = new HashMap();
  
  GoogleAnalytics() {}
  
  private GoogleAnalytics(Context paramContext)
  {
    this(paramContext, GAThread.getInstance(paramContext));
  }
  
  private GoogleAnalytics(Context paramContext, AnalyticsThread paramAnalyticsThread)
  {
    if (paramContext == null) {
      throw new IllegalArgumentException("context cannot be null");
    }
    this.mContext = paramContext.getApplicationContext();
    this.mThread = paramAnalyticsThread;
    this.mAdHitIdGenerator = new AdHitIdGenerator();
    this.mThread.requestAppOptOut(new AppOptOutCallback()
    {
      public void reportAppOptOut(boolean paramAnonymousBoolean)
      {
        GoogleAnalytics.access$002(GoogleAnalytics.this, Boolean.valueOf(paramAnonymousBoolean));
      }
    });
    this.mThread.requestClientId(new AnalyticsThread.ClientIdCallback()
    {
      public void reportClientId(String paramAnonymousString)
      {
        GoogleAnalytics.access$102(GoogleAnalytics.this, paramAnonymousString);
      }
    });
  }
  
  public static GoogleAnalytics getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null) {
        sInstance = new GoogleAnalytics(paramContext);
      }
      GoogleAnalytics localGoogleAnalytics = sInstance;
      return localGoogleAnalytics;
    }
    finally {}
  }
  
  public Tracker getTracker(String paramString)
  {
    if (paramString == null) {
      try
      {
        throw new IllegalArgumentException("trackingId cannot be null");
      }
      finally {}
    }
    Tracker localTracker = (Tracker)this.mTrackers.get(paramString);
    if (localTracker == null)
    {
      localTracker = new Tracker(paramString, this);
      this.mTrackers.put(paramString, localTracker);
      if (this.mDefaultTracker == null) {
        this.mDefaultTracker = localTracker;
      }
    }
    GAUsage.getInstance().setUsage(GAUsage.Field.GET_TRACKER);
    return localTracker;
  }
  
  public void sendHit(Map<String, String> paramMap)
  {
    if (paramMap == null) {
      try
      {
        throw new IllegalArgumentException("hit cannot be null");
      }
      finally {}
    }
    paramMap.put("language", Utils.getLanguage(Locale.getDefault()));
    paramMap.put("adSenseAdMobHitId", Integer.toString(this.mAdHitIdGenerator.getAdHitId()));
    paramMap.put("screenResolution", this.mContext.getResources().getDisplayMetrics().widthPixels + "x" + this.mContext.getResources().getDisplayMetrics().heightPixels);
    paramMap.put("usage", GAUsage.getInstance().getAndClearSequence());
    GAUsage.getInstance().getAndClearUsage();
    this.mThread.sendHit(paramMap);
    this.mLastTrackingId = ((String)paramMap.get("trackingId"));
  }
  
  public static abstract interface AppOptOutCallback
  {
    public abstract void reportAppOptOut(boolean paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.GoogleAnalytics
 * JD-Core Version:    0.7.0.1
 */