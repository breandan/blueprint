package com.google.android.search.core.util;

import com.google.android.shared.util.Clock;

public class Latency
{
  private final Clock mClock;
  private long mStartTime;
  
  public Latency(Clock paramClock)
  {
    this.mClock = paramClock;
    this.mStartTime = this.mClock.elapsedRealtime();
  }
  
  protected long getCurrentTime()
  {
    return this.mClock.elapsedRealtime();
  }
  
  public int getLatency()
  {
    return (int)(this.mClock.elapsedRealtime() - this.mStartTime);
  }
  
  protected long getStartTime()
  {
    return this.mStartTime;
  }
  
  public void reset()
  {
    this.mStartTime = this.mClock.elapsedRealtime();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.Latency
 * JD-Core Version:    0.7.0.1
 */