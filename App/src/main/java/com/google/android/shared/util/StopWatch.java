package com.google.android.shared.util;

import android.os.SystemClock;

public class StopWatch
{
  private long mStart = -1L;
  
  public int getElapsedTime()
  {
    return (int)(SystemClock.elapsedRealtime() - this.mStart);
  }
  
  public StopWatch start()
  {
    this.mStart = SystemClock.elapsedRealtime();
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.StopWatch
 * JD-Core Version:    0.7.0.1
 */