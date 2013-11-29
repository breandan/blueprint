package com.google.android.shared.util;

import android.os.Process;
import android.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentUtils
{
  private static ThreadFactory createBackgroundThreadFactory(String paramString)
  {
    new ThreadFactory()
    {
      private AtomicInteger counter = new AtomicInteger(0);
      
      public Thread newThread(final Runnable paramAnonymousRunnable)
      {
        new Thread(this.val$threadName + "-" + this.counter.incrementAndGet())
        {
          public void run()
          {
            Process.setThreadPriority(10);
            paramAnonymousRunnable.run();
          }
        };
      }
    };
  }
  
  private static ScheduledExecutorService createLoggingScheduledExecutorService(int paramInt, ThreadFactory paramThreadFactory, final String paramString)
  {
    if (paramInt > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      new ScheduledThreadPoolExecutor(paramInt, paramThreadFactory)
      {
        protected void afterExecute(Runnable paramAnonymousRunnable, Throwable paramAnonymousThrowable)
        {
          if ((paramAnonymousThrowable instanceof RuntimeException)) {
            Log.w(paramString, "RuntimeException occured", paramAnonymousThrowable);
          }
          Future localFuture;
          if ((paramAnonymousRunnable instanceof Future)) {
            localFuture = (Future)paramAnonymousRunnable;
          }
          try
          {
            localFuture.get();
            label37:
            super.afterExecute(paramAnonymousRunnable, paramAnonymousThrowable);
            return;
          }
          catch (ExecutionException localExecutionException)
          {
            for (;;)
            {
              Log.w(paramString, "ExecutionException occured", localExecutionException.getCause());
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            for (;;)
            {
              Thread.currentThread().interrupt();
            }
          }
          catch (CancellationException localCancellationException)
          {
            break label37;
          }
        }
      };
    }
  }
  
  public static ScheduledExecutorService createSafeScheduledExecutorService(int paramInt, String paramString)
  {
    return createSafeScheduledExecutorService(paramInt, createBackgroundThreadFactory(paramString), false);
  }
  
  public static ScheduledExecutorService createSafeScheduledExecutorService(int paramInt, ThreadFactory paramThreadFactory, boolean paramBoolean)
  {
    if (paramInt > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      ScheduledThreadPoolExecutor local2 = new ScheduledThreadPoolExecutor(paramInt, paramThreadFactory)
      {
        protected void afterExecute(Runnable paramAnonymousRunnable, Throwable paramAnonymousThrowable)
        {
          if ((paramAnonymousThrowable instanceof RuntimeException)) {
            throw ((RuntimeException)paramAnonymousThrowable);
          }
          Future localFuture;
          if ((paramAnonymousRunnable instanceof Future)) {
            localFuture = (Future)paramAnonymousRunnable;
          }
          try
          {
            localFuture.get();
            label31:
            super.afterExecute(paramAnonymousRunnable, paramAnonymousThrowable);
            return;
          }
          catch (ExecutionException localExecutionException)
          {
            throw new RuntimeException(localExecutionException);
          }
          catch (InterruptedException localInterruptedException)
          {
            for (;;)
            {
              Thread.currentThread().interrupt();
            }
          }
          catch (CancellationException localCancellationException)
          {
            break label31;
          }
        }
        
        public void execute(Runnable paramAnonymousRunnable)
        {
          getQueue().size();
          super.execute(paramAnonymousRunnable);
          int i = getQueue().size();
          if (i > this.val$MAX_EXPECTED_QUEUE_SIZE) {
            Log.w("Search.ConcurrentUtils", "Executor queue length is now " + i + ". Perhaps some tasks are too long, or the pool is too small. [" + Thread.currentThread().getName() + "]");
          }
        }
      };
      if (paramBoolean)
      {
        local2.setKeepAliveTime(60L, TimeUnit.SECONDS);
        local2.allowCoreThreadTimeOut(true);
      }
      return local2;
    }
  }
  
  public static ScheduledExecutorService createSingleThreadedScheduledExecutorService(String paramString)
  {
    return createLoggingScheduledExecutorService(1, createThreadFactory(paramString), paramString);
  }
  
  private static ThreadFactory createThreadFactory(String paramString)
  {
    return new ThreadFactoryBuilder().setNameFormat(paramString + "-%d").build();
  }
  
  public static ExecutorService newSingleThreadExecutor(String paramString)
  {
    return Executors.newSingleThreadExecutor(createBackgroundThreadFactory(paramString));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.ConcurrentUtils
 * JD-Core Version:    0.7.0.1
 */