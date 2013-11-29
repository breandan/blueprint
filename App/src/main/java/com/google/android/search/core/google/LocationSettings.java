package com.google.android.search.core.google;

import android.content.Intent;

public abstract interface LocationSettings
{
  public abstract void addUseLocationObserver(Observer paramObserver);
  
  public abstract boolean canUseLocationForGoogleApps();
  
  public abstract boolean canUseLocationForSearch();
  
  public abstract Intent getGoogleSettingIntent(String paramString);
  
  public abstract int getKlpLocationMode();
  
  public abstract boolean isGmsCoreLocationSettingAvailable();
  
  public abstract boolean isGoogleLocationEnabled();
  
  public abstract boolean isGoogleSettingForAllApps();
  
  public abstract boolean isSystemLocationEnabled();
  
  public abstract void maybeShowLegacyOptIn();
  
  public abstract void removeUseLocationObserver(Observer paramObserver);
  
  public static abstract interface Observer
  {
    public abstract void onUseLocationChanged(boolean paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.LocationSettings
 * JD-Core Version:    0.7.0.1
 */