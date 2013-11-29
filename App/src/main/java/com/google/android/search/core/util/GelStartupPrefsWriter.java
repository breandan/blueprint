package com.google.android.search.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.shared.util.GelStartupPrefs;

public class GelStartupPrefsWriter
  extends GelStartupPrefs
{
  public GelStartupPrefsWriter(Context paramContext)
  {
    super(paramContext);
  }
  
  public void commit(String paramString, boolean paramBoolean)
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    if ((!localSharedPreferences.contains(paramString)) || (localSharedPreferences.getBoolean(paramString, false) != paramBoolean)) {
      localSharedPreferences.edit().putBoolean(paramString, paramBoolean).commit();
    }
  }
  
  public void startReloadIfChanged() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.GelStartupPrefsWriter
 * JD-Core Version:    0.7.0.1
 */