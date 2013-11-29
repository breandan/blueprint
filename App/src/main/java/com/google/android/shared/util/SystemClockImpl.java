package com.google.android.shared.util;

import android.content.Context;
import android.os.SystemClock;

public class SystemClockImpl
  implements Clock
{
  private final Context mContext;
  private ListenerManager<Clock.TimeResetListener> mListenManagerTimeReset;
  private ListenerManager<Clock.TimeTickListener> mListenManagerTimeTick;
  
  public SystemClockImpl(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public long currentTimeMillis()
  {
    return System.currentTimeMillis();
  }
  
  public long elapsedRealtime()
  {
    return SystemClock.elapsedRealtime();
  }
  
  public void registerTimeResetListener(Clock.TimeResetListener paramTimeResetListener)
  {
    if (this.mListenManagerTimeReset == null) {
      this.mListenManagerTimeReset = new ListenerManager(this.mContext, "android.intent.action.TIME_SET", new ListenerManager.Dispatcher()
      {
        public void dispatch(Clock.TimeResetListener paramAnonymousTimeResetListener)
        {
          paramAnonymousTimeResetListener.onTimeReset();
        }
      });
    }
    this.mListenManagerTimeReset.registerListener(paramTimeResetListener);
  }
  
  public void registerTimeTickListener(Clock.TimeTickListener paramTimeTickListener)
  {
    if (this.mListenManagerTimeTick == null) {
      this.mListenManagerTimeTick = new ListenerManager(this.mContext, "android.intent.action.TIME_TICK", new ListenerManager.Dispatcher()
      {
        public void dispatch(Clock.TimeTickListener paramAnonymousTimeTickListener)
        {
          paramAnonymousTimeTickListener.onTimeTick();
        }
      });
    }
    this.mListenManagerTimeTick.registerListener(paramTimeTickListener);
  }
  
  public void unregisterTimeResetListener(Clock.TimeResetListener paramTimeResetListener)
  {
    if (this.mListenManagerTimeReset != null)
    {
      this.mListenManagerTimeReset.unRegisterListener(paramTimeResetListener);
      if (this.mListenManagerTimeReset.isEmpty()) {
        this.mListenManagerTimeReset = null;
      }
    }
  }
  
  public void unregisterTimeTickListener(Clock.TimeTickListener paramTimeTickListener)
  {
    if (this.mListenManagerTimeTick != null)
    {
      this.mListenManagerTimeTick.unRegisterListener(paramTimeTickListener);
      if (this.mListenManagerTimeTick.isEmpty()) {
        this.mListenManagerTimeTick = null;
      }
    }
  }
  
  public long uptimeMillis()
  {
    return SystemClock.uptimeMillis();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.SystemClockImpl
 * JD-Core Version:    0.7.0.1
 */