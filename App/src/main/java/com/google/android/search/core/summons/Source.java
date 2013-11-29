package com.google.android.search.core.summons;

import android.content.pm.ApplicationInfo;

public abstract interface Source
{
  public abstract ApplicationInfo getApplicationInfo();
  
  public abstract String getCanonicalName();
  
  public abstract String getDefaultIntentAction();
  
  public abstract String getDefaultIntentData();
  
  public abstract int getLabelResourceId();
  
  public abstract String getName();
  
  public abstract String getPackageName();
  
  public abstract int getSettingsDescriptionResourceId();
  
  public abstract int getSourceIconResource();
  
  public abstract boolean hasFullSizeIcon();
  
  public abstract boolean isContactsSource();
  
  public abstract boolean isEnabledByDefault();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.Source
 * JD-Core Version:    0.7.0.1
 */