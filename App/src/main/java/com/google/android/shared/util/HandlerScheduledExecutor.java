package com.google.android.shared.util;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import com.google.common.collect.Maps;
import java.util.Map;

public class HandlerScheduledExecutor
  extends HandlerExecutor
  implements ScheduledSingleThreadedExecutor
{
  private final MessageQueue mQueue;
  private final Map<Runnable, MessageQueue.IdleHandler> mQueuedIdleHandlers;
  
  public HandlerScheduledExecutor(Handler paramHandler, MessageQueue paramMessageQueue)
  {
    super(paramHandler);
    this.mQueue = paramMessageQueue;
    this.mQueuedIdleHandlers = Maps.newHashMap();
  }
  
  private MessageQueue.IdleHandler dequeueIdleHandler(Runnable paramRunnable)
  {
    synchronized (this.mQueuedIdleHandlers)
    {
      if (this.mQueuedIdleHandlers.containsKey(paramRunnable))
      {
        MessageQueue.IdleHandler localIdleHandler = (MessageQueue.IdleHandler)this.mQueuedIdleHandlers.remove(paramRunnable);
        return localIdleHandler;
      }
      return null;
    }
  }
  
  public void cancelExecute(Runnable paramRunnable)
  {
    super.cancelExecute(paramRunnable);
    this.mQueue.removeIdleHandler(dequeueIdleHandler(paramRunnable));
  }
  
  public void executeDelayed(Runnable paramRunnable, long paramLong)
  {
    this.mHandler.postDelayed(paramRunnable, paramLong);
  }
  
  public void executeOnIdle(Runnable paramRunnable)
  {
    ExecuteOnIdle localExecuteOnIdle = new ExecuteOnIdle(paramRunnable);
    synchronized (this.mQueuedIdleHandlers)
    {
      this.mQueuedIdleHandlers.put(paramRunnable, localExecuteOnIdle);
      this.mQueue.addIdleHandler(localExecuteOnIdle);
      return;
    }
  }
  
  public Handler getHandler()
  {
    return this.mHandler;
  }
  
  public boolean isThisThread()
  {
    return Looper.myLooper() == this.mHandler.getLooper();
  }
  
  private class ExecuteOnIdle
    implements MessageQueue.IdleHandler
  {
    private final Runnable mCommand;
    
    public ExecuteOnIdle(Runnable paramRunnable)
    {
      this.mCommand = paramRunnable;
    }
    
    public boolean queueIdle()
    {
      HandlerScheduledExecutor.this.dequeueIdleHandler(this.mCommand);
      HandlerScheduledExecutor.this.execute(this.mCommand);
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.HandlerScheduledExecutor
 * JD-Core Version:    0.7.0.1
 */