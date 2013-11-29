package com.google.android.search.core.preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public abstract interface PreferenceController
{
  public abstract boolean filterPreference(Preference paramPreference);
  
  public abstract void handlePreference(Preference paramPreference);
  
  public abstract void onCreateComplete(Bundle paramBundle);
  
  public abstract void onDestroy();
  
  public abstract void onPause();
  
  public abstract void onResume();
  
  public abstract void onSaveInstanceState(Bundle paramBundle);
  
  public abstract void onStop();
  
  public abstract void setScreen(PreferenceScreen paramPreferenceScreen);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.PreferenceController
 * JD-Core Version:    0.7.0.1
 */