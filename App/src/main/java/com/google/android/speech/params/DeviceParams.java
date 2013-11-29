package com.google.android.speech.params;

import android.util.DisplayMetrics;
import javax.annotation.Nullable;

public abstract interface DeviceParams
{
  public abstract String getApplicationVersion();
  
  @Nullable
  public abstract DisplayMetrics getDisplayMetrics();
  
  public abstract String getSearchDomainCountryCode();
  
  public abstract String getUserAgent();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.DeviceParams
 * JD-Core Version:    0.7.0.1
 */