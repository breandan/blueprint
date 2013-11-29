package com.google.common.util.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors
{
  public static ListeningExecutorService sameThreadExecutor()
  {
    return new SameThreadExecutorService(null);
  }
  
  private static class SameThreadExecutorService
    extends AbstractListeningExecutorService
  {
    private final Lock lock = new ReentrantLock();
    private int runningTasks = 0;
    private boolean shutdown = false;
    private final Condition termination = this.lock.newCondition();
    
    private void endTask()
    {
      this.lock.lock();
      try
      {
        this.runningTasks = (-1 + this.runningTasks);
        if (isTerminated()) {
          this.termination.signalAll();
        }
        return;
      }
      finally
      {
        this.lock.unlock();
      }
    }
    
    private void startTask()
    {
      this.lock.lock();
      try
      {
        if (isShutdown()) {
          throw new RejectedExecutionException("Executor already shutdown");
        }
      }
      finally
      {
        this.lock.unlock();
      }
      this.runningTasks = (1 + this.runningTasks);
      this.lock.unlock();
    }
    
    /* Error */
    public boolean awaitTermination(long paramLong, java.util.concurrent.TimeUnit paramTimeUnit)
      throws InterruptedException
    {
      // Byte code:
      //   0: aload_3
      //   1: lload_1
      //   2: invokevirtual 71	java/util/concurrent/TimeUnit:toNanos	(J)J
      //   5: lstore 4
      //   7: aload_0
      //   8: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   11: invokeinterface 38 1 0
      //   16: aload_0
      //   17: invokevirtual 42	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:isTerminated	()Z
      //   20: istore 7
      //   22: iload 7
      //   24: ifeq +14 -> 38
      //   27: aload_0
      //   28: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   31: invokeinterface 50 1 0
      //   36: iconst_1
      //   37: ireturn
      //   38: lload 4
      //   40: lconst_0
      //   41: lcmp
      //   42: ifgt +14 -> 56
      //   45: aload_0
      //   46: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   49: invokeinterface 50 1 0
      //   54: iconst_0
      //   55: ireturn
      //   56: aload_0
      //   57: getfield 29	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:termination	Ljava/util/concurrent/locks/Condition;
      //   60: lload 4
      //   62: invokeinterface 74 3 0
      //   67: lstore 8
      //   69: lload 8
      //   71: lstore 4
      //   73: goto -57 -> 16
      //   76: astore 6
      //   78: aload_0
      //   79: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   82: invokeinterface 50 1 0
      //   87: aload 6
      //   89: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	90	0	this	SameThreadExecutorService
      //   0	90	1	paramLong	long
      //   0	90	3	paramTimeUnit	java.util.concurrent.TimeUnit
      //   5	67	4	l1	long
      //   76	12	6	localObject	Object
      //   20	3	7	bool	boolean
      //   67	3	8	l2	long
      // Exception table:
      //   from	to	target	type
      //   16	22	76	finally
      //   56	69	76	finally
    }
    
    public void execute(Runnable paramRunnable)
    {
      startTask();
      try
      {
        paramRunnable.run();
        return;
      }
      finally
      {
        endTask();
      }
    }
    
    public boolean isShutdown()
    {
      this.lock.lock();
      try
      {
        boolean bool = this.shutdown;
        return bool;
      }
      finally
      {
        this.lock.unlock();
      }
    }
    
    /* Error */
    public boolean isTerminated()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   4: invokeinterface 38 1 0
      //   9: aload_0
      //   10: getfield 33	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:shutdown	Z
      //   13: ifeq +25 -> 38
      //   16: aload_0
      //   17: getfield 31	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:runningTasks	I
      //   20: istore_3
      //   21: iload_3
      //   22: ifne +16 -> 38
      //   25: iconst_1
      //   26: istore_2
      //   27: aload_0
      //   28: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   31: invokeinterface 50 1 0
      //   36: iload_2
      //   37: ireturn
      //   38: iconst_0
      //   39: istore_2
      //   40: goto -13 -> 27
      //   43: astore_1
      //   44: aload_0
      //   45: getfield 21	com/google/common/util/concurrent/MoreExecutors$SameThreadExecutorService:lock	Ljava/util/concurrent/locks/Lock;
      //   48: invokeinterface 50 1 0
      //   53: aload_1
      //   54: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	55	0	this	SameThreadExecutorService
      //   43	11	1	localObject	Object
      //   26	14	2	bool	boolean
      //   20	2	3	i	int
      // Exception table:
      //   from	to	target	type
      //   9	21	43	finally
    }
    
    public void shutdown()
    {
      this.lock.lock();
      try
      {
        this.shutdown = true;
        return;
      }
      finally
      {
        this.lock.unlock();
      }
    }
    
    public List<Runnable> shutdownNow()
    {
      shutdown();
      return Collections.emptyList();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.MoreExecutors
 * JD-Core Version:    0.7.0.1
 */