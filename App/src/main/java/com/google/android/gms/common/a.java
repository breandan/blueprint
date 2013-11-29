package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class a
  implements ServiceConnection
{
  boolean mt = false;
  private final BlockingQueue<IBinder> mu = new LinkedBlockingQueue();
  
  public IBinder aK()
    throws InterruptedException
  {
    if (this.mt) {
      throw new IllegalStateException();
    }
    this.mt = true;
    return (IBinder)this.mu.take();
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    try
    {
      this.mu.put(paramIBinder);
      return;
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.common.a
 * JD-Core Version:    0.7.0.1
 */