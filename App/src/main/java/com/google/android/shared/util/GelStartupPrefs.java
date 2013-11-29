package com.google.android.shared.util;

import android.content.Context;
import android.content.SharedPreferences;

public class GelStartupPrefs
{
  static final String PREFS_NAME = "GEL.GSAPrefs";
  protected final Context mContext;
  private SharedPreferences mPrefs;
  
  public GelStartupPrefs(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public boolean contains(String paramString)
  {
    return getSharedPreferences().contains(paramString);
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    return getSharedPreferences().getBoolean(paramString, paramBoolean);
  }
  
  protected SharedPreferences getSharedPreferences()
  {
    try
    {
      if (this.mPrefs == null) {
        this.mPrefs = this.mContext.getSharedPreferences("GEL.GSAPrefs", 0);
      }
      SharedPreferences localSharedPreferences = this.mPrefs;
      return localSharedPreferences;
    }
    finally {}
  }
  
  public void startReloadIfChanged()
  {
    this.mContext.getSharedPreferences("GEL.GSAPrefs", 4);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.GelStartupPrefs
 * JD-Core Version:    0.7.0.1
 */