package com.google.analytics.tracking.android;

import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tracker
{
  private static final DecimalFormat DF = new DecimalFormat("0.######", new DecimalFormatSymbols(Locale.US));
  private final TrackerHandler mHandler;
  private boolean mIsThrottlingEnabled = true;
  private volatile boolean mIsTrackerClosed = false;
  private volatile boolean mIsTrackingStarted = false;
  private long mLastTrackTime;
  private final SimpleModel mModel;
  private long mTokens = 120000L;
  
  Tracker()
  {
    this.mHandler = null;
    this.mModel = null;
  }
  
  Tracker(String paramString, TrackerHandler paramTrackerHandler)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("trackingId cannot be null");
    }
    this.mHandler = paramTrackerHandler;
    this.mModel = new SimpleModel(null);
    this.mModel.set("trackingId", paramString);
    this.mModel.set("sampleRate", "100");
    this.mModel.setForNextHit("sessionControl", "start");
    this.mModel.set("useSecure", Boolean.toString(true));
  }
  
  private void assertTrackerOpen()
  {
    if (this.mIsTrackerClosed) {
      throw new IllegalStateException("Tracker closed");
    }
  }
  
  private void internalSend(String paramString, Map<String, String> paramMap)
  {
    this.mIsTrackingStarted = true;
    if (paramMap == null) {
      paramMap = new HashMap();
    }
    paramMap.put("hitType", paramString);
    this.mModel.setAll(paramMap, Boolean.valueOf(true));
    if (!tokensAvailable()) {
      Log.wDebug("Too many hits sent too quickly, throttling invoked.");
    }
    for (;;)
    {
      this.mModel.clearTemporaryValues();
      return;
      this.mHandler.sendHit(this.mModel.getKeysAndValues());
    }
  }
  
  public Map<String, String> constructEvent(String paramString1, String paramString2, String paramString3, Long paramLong)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("eventCategory", paramString1);
    localHashMap.put("eventAction", paramString2);
    localHashMap.put("eventLabel", paramString3);
    if (paramLong != null) {
      localHashMap.put("eventValue", Long.toString(paramLong.longValue()));
    }
    GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_EVENT);
    return localHashMap;
  }
  
  public Map<String, String> constructTiming(String paramString1, long paramLong, String paramString2, String paramString3)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("timingCategory", paramString1);
    localHashMap.put("timingValue", Long.toString(paramLong));
    localHashMap.put("timingVar", paramString2);
    localHashMap.put("timingLabel", paramString3);
    GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_TIMING);
    return localHashMap;
  }
  
  public void sendEvent(String paramString1, String paramString2, String paramString3, Long paramLong)
  {
    assertTrackerOpen();
    GAUsage.getInstance().setUsage(GAUsage.Field.TRACK_EVENT);
    GAUsage.getInstance().setDisableUsage(true);
    internalSend("event", constructEvent(paramString1, paramString2, paramString3, paramLong));
    GAUsage.getInstance().setDisableUsage(false);
  }
  
  public void sendTiming(String paramString1, long paramLong, String paramString2, String paramString3)
  {
    assertTrackerOpen();
    GAUsage.getInstance().setUsage(GAUsage.Field.TRACK_TIMING);
    GAUsage.getInstance().setDisableUsage(true);
    internalSend("timing", constructTiming(paramString1, paramLong, paramString2, paramString3));
    GAUsage.getInstance().setDisableUsage(false);
  }
  
  public void sendView(String paramString)
  {
    assertTrackerOpen();
    if (TextUtils.isEmpty(paramString)) {
      throw new IllegalStateException("trackView requires a appScreen to be set");
    }
    GAUsage.getInstance().setUsage(GAUsage.Field.TRACK_VIEW_WITH_APPSCREEN);
    this.mModel.set("description", paramString);
    internalSend("appview", null);
  }
  
  public void setSampleRate(double paramDouble)
  {
    GAUsage.getInstance().setUsage(GAUsage.Field.SET_SAMPLE_RATE);
    this.mModel.set("sampleRate", Double.toString(paramDouble));
  }
  
  public void setStartSession(boolean paramBoolean)
  {
    assertTrackerOpen();
    GAUsage.getInstance().setUsage(GAUsage.Field.SET_START_SESSION);
    SimpleModel localSimpleModel = this.mModel;
    if (paramBoolean) {}
    for (String str = "start";; str = null)
    {
      localSimpleModel.setForNextHit("sessionControl", str);
      return;
    }
  }
  
  boolean tokensAvailable()
  {
    boolean bool1 = true;
    for (;;)
    {
      try
      {
        boolean bool2 = this.mIsThrottlingEnabled;
        if (!bool2) {
          return bool1;
        }
        long l1 = System.currentTimeMillis();
        if (this.mTokens < 120000L)
        {
          long l2 = l1 - this.mLastTrackTime;
          if (l2 > 0L) {
            this.mTokens = Math.min(120000L, l2 + this.mTokens);
          }
        }
        this.mLastTrackTime = l1;
        if (this.mTokens >= 2000L)
        {
          this.mTokens -= 2000L;
          continue;
        }
        Log.wDebug("Excessive tracking detected.  Tracking call ignored.");
      }
      finally {}
      bool1 = false;
    }
  }
  
  private static class SimpleModel
  {
    private Map<String, String> permanentMap = new HashMap();
    private Map<String, String> temporaryMap = new HashMap();
    
    public void clearTemporaryValues()
    {
      try
      {
        this.temporaryMap.clear();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public Map<String, String> getKeysAndValues()
    {
      try
      {
        HashMap localHashMap = new HashMap(this.permanentMap);
        localHashMap.putAll(this.temporaryMap);
        return localHashMap;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public void set(String paramString1, String paramString2)
    {
      try
      {
        this.permanentMap.put(paramString1, paramString2);
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    /* Error */
    public void setAll(Map<String, String> paramMap, Boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_2
      //   3: invokevirtual 49	java/lang/Boolean:booleanValue	()Z
      //   6: ifeq +16 -> 22
      //   9: aload_0
      //   10: getfield 17	com/google/analytics/tracking/android/Tracker$SimpleModel:temporaryMap	Ljava/util/Map;
      //   13: aload_1
      //   14: invokeinterface 35 2 0
      //   19: aload_0
      //   20: monitorexit
      //   21: return
      //   22: aload_0
      //   23: getfield 19	com/google/analytics/tracking/android/Tracker$SimpleModel:permanentMap	Ljava/util/Map;
      //   26: aload_1
      //   27: invokeinterface 35 2 0
      //   32: goto -13 -> 19
      //   35: astore_3
      //   36: aload_0
      //   37: monitorexit
      //   38: aload_3
      //   39: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	40	0	this	SimpleModel
      //   0	40	1	paramMap	Map<String, String>
      //   0	40	2	paramBoolean	Boolean
      //   35	4	3	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   2	19	35	finally
      //   22	32	35	finally
    }
    
    public void setForNextHit(String paramString1, String paramString2)
    {
      try
      {
        this.temporaryMap.put(paramString1, paramString2);
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.Tracker
 * JD-Core Version:    0.7.0.1
 */