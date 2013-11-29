package com.google.android.search.core.debug;

import android.os.Looper;
import android.util.Log;
import com.google.android.search.core.SearchSettings;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;

public class DebugFeatures
{
  private static final DebugFeatures sInstance = new DebugFeatures();
  private int mDebugLevel = -1;
  private final List<IllegalStateException> mPrematureCalls = Lists.newArrayListWithCapacity(0);
  private SearchSettings mSearchSettings;
  
  private static void checkIsSet()
  {
    if (sInstance.mDebugLevel == -1)
    {
      if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
        sInstance.mPrematureCalls.add(new IllegalStateException("call to DebugFeatures too early from main thread"));
      }
    }
    else {
      return;
    }
    setDebugLevel();
  }
  
  public static DebugFeatures getInstance()
  {
    return sInstance;
  }
  
  public static boolean isSet()
  {
    return sInstance.mDebugLevel != -1;
  }
  
  public static void setDebugLevel()
  {
    Preconditions.checkNotNull(sInstance.mSearchSettings, "Tried to set debug level before setting SearchSettings.");
    sInstance.mDebugLevel = sInstance.mSearchSettings.getDebugFeaturesLevel();
    if ((!sInstance.mPrematureCalls.isEmpty()) && (sInstance.teamDebugEnabled())) {
      throw ((IllegalStateException)sInstance.mPrematureCalls.get(0));
    }
    if (sInstance.dogfoodDebugEnabled()) {
      while (!sInstance.mPrematureCalls.isEmpty()) {
        Log.e("DebugFeatures", "premature call to get method", (IllegalStateException)sInstance.mPrematureCalls.remove(0));
      }
    }
    sInstance.mPrematureCalls.clear();
  }
  
  public static void setSearchSettings(SearchSettings paramSearchSettings)
  {
    sInstance.mSearchSettings = paramSearchSettings;
    if (sInstance.mDebugLevel != -1) {
      setDebugLevel();
    }
  }
  
  public boolean dogfoodDebugEnabled()
  {
    checkIsSet();
    return this.mDebugLevel >= 1;
  }
  
  public boolean teamDebugEnabled()
  {
    checkIsSet();
    return this.mDebugLevel == 2;
  }
  
  public String toString()
  {
    return "DebugFeatures{" + this.mDebugLevel + "}";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.debug.DebugFeatures
 * JD-Core Version:    0.7.0.1
 */