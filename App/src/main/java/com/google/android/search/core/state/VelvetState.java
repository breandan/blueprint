package com.google.android.search.core.state;

import java.io.PrintWriter;

public abstract class VelvetState
{
  private final VelvetEventBus mEventBus;
  private final int mId;
  private boolean mNotified;
  
  protected VelvetState(VelvetEventBus paramVelvetEventBus, int paramInt)
  {
    this.mId = paramInt;
    this.mEventBus = paramVelvetEventBus;
  }
  
  abstract void dump(String paramString, PrintWriter paramPrintWriter);
  
  int getId()
  {
    return this.mId;
  }
  
  protected void notifyChanged()
  {
    this.mNotified = true;
    this.mEventBus.notifyStateChanged(this.mId);
  }
  
  protected abstract void onStateChanged(VelvetEventBus.Event paramEvent);
  
  boolean takeNotified()
  {
    boolean bool = this.mNotified;
    this.mNotified = false;
    return bool;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.VelvetState
 * JD-Core Version:    0.7.0.1
 */