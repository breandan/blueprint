package com.google.analytics.tracking.android;

import java.util.Map;

abstract interface TrackerHandler
{
  public abstract void sendHit(Map<String, String> paramMap);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.TrackerHandler
 * JD-Core Version:    0.7.0.1
 */