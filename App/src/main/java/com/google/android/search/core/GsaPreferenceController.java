package com.google.android.search.core;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesProto;
import com.google.android.search.core.util.GelStartupPrefsWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GsaPreferenceController
{
  private final Context mContext;
  private final GelStartupPrefsWriter mGelStartupPrefs;
  private final Object mLock = new Object();
  private SharedPreferencesExt mMainPrefs;
  private ArrayList<SharedPreferences.OnSharedPreferenceChangeListener> mPendingListeners;
  private final SharedPreferencesExt mStartupPrefs;
  private boolean mVersionChecked;
  private int mWriteDelayCounter;
  
  public GsaPreferenceController(Context paramContext)
  {
    this.mContext = paramContext;
    this.mStartupPrefs = openProtoPreferences("StartupSettings");
    this.mGelStartupPrefs = new GelStartupPrefsWriter(paramContext);
  }
  
  private void checkVersionLocked()
  {
    if (!this.mVersionChecked)
    {
      int i = this.mStartupPrefs.getInt("settings_version", -1);
      if (i < 10)
      {
        initMainPrefsLocked();
        GsaPreferenceUpgrader.upgrade(this.mContext, this.mStartupPrefs, this.mMainPrefs, "settings_version", i, 10);
      }
      this.mVersionChecked = true;
    }
  }
  
  private void initMainPrefsLocked()
  {
    if (this.mMainPrefs == null)
    {
      this.mMainPrefs = openProtoPreferences("SearchSettings");
      if (this.mPendingListeners != null)
      {
        Iterator localIterator = this.mPendingListeners.iterator();
        while (localIterator.hasNext())
        {
          SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = (SharedPreferences.OnSharedPreferenceChangeListener)localIterator.next();
          this.mMainPrefs.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
        }
        this.mPendingListeners = null;
      }
      if (this.mWriteDelayCounter != 0) {
        this.mMainPrefs.delayWrites();
      }
    }
  }
  
  private SharedPreferencesExt openProtoPreferences(String paramString)
  {
    return new SharedPreferencesProto(new File(this.mContext.getDir("shared_prefs", 0), paramString + ".bin"));
  }
  
  public static void useMainPreferences(PreferenceManager paramPreferenceManager)
  {
    paramPreferenceManager.setSharedPreferencesName("SearchSettings");
    paramPreferenceManager.setSharedPreferencesMode(0);
  }
  
  public void allowWrites()
  {
    synchronized (this.mLock)
    {
      this.mWriteDelayCounter = (-1 + this.mWriteDelayCounter);
      if (this.mWriteDelayCounter == 0)
      {
        this.mStartupPrefs.allowWrites();
        if (this.mMainPrefs != null) {
          this.mMainPrefs.allowWrites();
        }
      }
      return;
    }
  }
  
  public void delayWrites()
  {
    synchronized (this.mLock)
    {
      if (this.mWriteDelayCounter == 0)
      {
        this.mStartupPrefs.delayWrites();
        if (this.mMainPrefs != null) {
          this.mMainPrefs.delayWrites();
        }
      }
      this.mWriteDelayCounter = (1 + this.mWriteDelayCounter);
      return;
    }
  }
  
  public GelStartupPrefsWriter getGelStartupPrefs()
  {
    return this.mGelStartupPrefs;
  }
  
  public SharedPreferencesExt getMainPreferences()
  {
    synchronized (this.mLock)
    {
      if (this.mMainPrefs == null)
      {
        if (!this.mVersionChecked) {
          checkVersionLocked();
        }
        initMainPrefsLocked();
      }
      SharedPreferencesExt localSharedPreferencesExt = this.mMainPrefs;
      return localSharedPreferencesExt;
    }
  }
  
  public SharedPreferencesExt getStartupPreferences()
  {
    synchronized (this.mLock)
    {
      if (!this.mVersionChecked) {
        checkVersionLocked();
      }
      SharedPreferencesExt localSharedPreferencesExt = this.mStartupPrefs;
      return localSharedPreferencesExt;
    }
  }
  
  public boolean isMainPreferencesName(String paramString, int paramInt)
  {
    if ("SearchSettings".equals(paramString))
    {
      if (paramInt != 0) {}
      return true;
    }
    return false;
  }
  
  public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (this.mLock)
    {
      getStartupPreferences().registerOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
      if (this.mMainPrefs != null)
      {
        this.mMainPrefs.registerOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
        return;
      }
      if (this.mPendingListeners == null) {
        this.mPendingListeners = new ArrayList();
      }
      this.mPendingListeners.add(paramOnSharedPreferenceChangeListener);
    }
  }
  
  public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (this.mLock)
    {
      getStartupPreferences().unregisterOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
      if (this.mMainPrefs != null) {
        this.mMainPrefs.unregisterOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
      }
      while (this.mPendingListeners == null) {
        return;
      }
      this.mPendingListeners.remove(paramOnSharedPreferenceChangeListener);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GsaPreferenceController
 * JD-Core Version:    0.7.0.1
 */