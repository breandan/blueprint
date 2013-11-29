package com.google.android.search.core.util;

import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class ExecutorObservable
  extends Observable
{
  private static final Object NULL = new Object();
  private final Executor mExecutor;
  private final Queue<Object> mPendingData = new ConcurrentLinkedQueue();
  
  public ExecutorObservable(Executor paramExecutor)
  {
    this.mExecutor = paramExecutor;
  }
  
  public void notifyObservers()
  {
    notifyObservers(null);
  }
  
  public void notifyObservers(Object paramObject)
  {
    try
    {
      if (hasChanged())
      {
        Queue localQueue = this.mPendingData;
        if (paramObject == null) {
          paramObject = NULL;
        }
        localQueue.add(paramObject);
        this.mExecutor.execute(new Runnable()
        {
          public void run()
          {
            for (;;)
            {
              Object localObject = ExecutorObservable.this.mPendingData.poll();
              if (localObject == null) {
                break;
              }
              ExecutorObservable localExecutorObservable = ExecutorObservable.this;
              if (localObject == ExecutorObservable.NULL) {
                localObject = null;
              }
              localExecutorObservable.notifyObservers(localObject);
            }
          }
        });
      }
      return;
    }
    finally {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.ExecutorObservable
 * JD-Core Version:    0.7.0.1
 */