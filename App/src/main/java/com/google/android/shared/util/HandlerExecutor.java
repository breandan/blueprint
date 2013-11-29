package com.google.android.shared.util;

import android.os.Handler;

public class HandlerExecutor
  implements CancellableSingleThreadedExecutor
{
  protected final Handler mHandler;
  
  public HandlerExecutor(Handler paramHandler)
  {
    this.mHandler = paramHandler;
  }
  
  public void cancelExecute(Runnable paramRunnable)
  {
    this.mHandler.removeCallbacks(paramRunnable);
  }
  
  public void execute(Runnable paramRunnable)
  {
    this.mHandler.post(paramRunnable);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.HandlerExecutor
 * JD-Core Version:    0.7.0.1
 */