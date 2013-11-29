package com.android.launcher3;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.util.Pair;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class DeferredHandler
{
  private Impl mHandler = new Impl(null);
  private MessageQueue mMessageQueue = Looper.myQueue();
  private LinkedList<Pair<Runnable, Integer>> mQueue = new LinkedList();
  
  public void cancelAllRunnablesOfType(int paramInt)
  {
    synchronized (this.mQueue)
    {
      ListIterator localListIterator = this.mQueue.listIterator();
      while (localListIterator.hasNext()) {
        if (((Integer)((Pair)localListIterator.next()).second).intValue() == paramInt) {
          localListIterator.remove();
        }
      }
    }
  }
  
  public void flush()
  {
    LinkedList localLinkedList1 = new LinkedList();
    synchronized (this.mQueue)
    {
      localLinkedList1.addAll(this.mQueue);
      this.mQueue.clear();
      Iterator localIterator = localLinkedList1.iterator();
      if (localIterator.hasNext()) {
        ((Runnable)((Pair)localIterator.next()).first).run();
      }
    }
  }
  
  public void post(Runnable paramRunnable)
  {
    post(paramRunnable, 0);
  }
  
  public void post(Runnable paramRunnable, int paramInt)
  {
    synchronized (this.mQueue)
    {
      this.mQueue.add(new Pair(paramRunnable, Integer.valueOf(paramInt)));
      if (this.mQueue.size() == 1) {
        scheduleNextLocked();
      }
      return;
    }
  }
  
  public void postIdle(Runnable paramRunnable)
  {
    postIdle(paramRunnable, 0);
  }
  
  public void postIdle(Runnable paramRunnable, int paramInt)
  {
    post(new IdleRunnable(paramRunnable), paramInt);
  }
  
  void scheduleNextLocked()
  {
    if (this.mQueue.size() > 0)
    {
      if (((Runnable)((Pair)this.mQueue.getFirst()).first instanceof IdleRunnable)) {
        this.mMessageQueue.addIdleHandler(this.mHandler);
      }
    }
    else {
      return;
    }
    this.mHandler.sendEmptyMessage(1);
  }
  
  private class IdleRunnable
    implements Runnable
  {
    Runnable mRunnable;
    
    IdleRunnable(Runnable paramRunnable)
    {
      this.mRunnable = paramRunnable;
    }
    
    public void run()
    {
      this.mRunnable.run();
    }
  }
  
  private class Impl
    extends Handler
    implements MessageQueue.IdleHandler
  {
    private Impl() {}
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (DeferredHandler.this.mQueue)
      {
        if (DeferredHandler.this.mQueue.size() == 0) {
          return;
        }
        Runnable localRunnable = (Runnable)((Pair)DeferredHandler.this.mQueue.removeFirst()).first;
        localRunnable.run();
        synchronized (DeferredHandler.this.mQueue)
        {
          DeferredHandler.this.scheduleNextLocked();
          return;
        }
      }
    }
    
    public boolean queueIdle()
    {
      handleMessage(null);
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DeferredHandler
 * JD-Core Version:    0.7.0.1
 */