package com.google.android.search.core.util;

import com.google.android.speech.callback.SimpleCallback;
import com.google.common.util.concurrent.SettableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleCallbackFuture<T>
  implements SimpleCallback<T>, Future<T>
{
  private final SettableFuture<T> mSettableFuture = SettableFuture.create();
  
  public boolean cancel(boolean paramBoolean)
  {
    return false;
  }
  
  public T get()
    throws InterruptedException, ExecutionException
  {
    return this.mSettableFuture.get();
  }
  
  public T get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return this.mSettableFuture.get(paramLong, paramTimeUnit);
  }
  
  public boolean isCancelled()
  {
    return this.mSettableFuture.isCancelled();
  }
  
  public boolean isDone()
  {
    return this.mSettableFuture.isDone();
  }
  
  public void onResult(T paramT)
  {
    this.mSettableFuture.set(paramT);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.SimpleCallbackFuture
 * JD-Core Version:    0.7.0.1
 */