package com.embryo.android.search.core;

import com.embryo.android.shared.util.ConcurrentUtils;
import com.embryo.android.shared.util.NamedDelayedTaskExecutor;
import com.embryo.android.shared.util.NamedTask;
import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class PoolingNamedTaskExecutor
  implements NamedDelayedTaskExecutor
{
  private final ScheduledExecutorService mPool;
  
  public PoolingNamedTaskExecutor(int paramInt, ThreadFactory paramThreadFactory, boolean paramBoolean)
  {
    this.mPool = ConcurrentUtils.createSafeScheduledExecutorService(paramInt, paramThreadFactory, paramBoolean);
  }
  
  public void cancelPendingTasks()
  {
    throw new UnsupportedOperationException();
  }
  
  public void execute(NamedTask paramNamedTask)
  {
    this.mPool.execute(paramNamedTask);
  }
  
  public void executeDelayed(NamedTask paramNamedTask, long paramLong)
  {
    if (paramLong >= 0L) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Delay must not be negative.");
      this.mPool.schedule(paramNamedTask, paramLong, TimeUnit.MILLISECONDS);
      return;
    }
  }
  
  public Future<?> submit(NamedTask paramNamedTask)
  {
    return this.mPool.submit(paramNamedTask);
  }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.search.core.PoolingNamedTaskExecutor

 * JD-Core Version:    0.7.0.1

 */