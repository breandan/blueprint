package com.google.android.shared.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorServiceAdapter
  implements ExecutorService
{
  public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public void execute(Runnable paramRunnable)
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection)
    throws InterruptedException
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection)
    throws InterruptedException, ExecutionException
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public boolean isShutdown()
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public boolean isTerminated()
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public void shutdown()
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public List<Runnable> shutdownNow()
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public Future<?> submit(Runnable paramRunnable)
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> Future<T> submit(Runnable paramRunnable, T paramT)
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
  
  public <T> Future<T> submit(Callable<T> paramCallable)
  {
    throw new UnsupportedOperationException("DummyExecutorService: operations not supported.");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.ExecutorServiceAdapter
 * JD-Core Version:    0.7.0.1
 */