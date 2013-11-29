package com.google.android.shared.util;

public class NamingDelayedTaskExecutor
  extends NamingTaskExecutor
{
  private final NamedDelayedTaskExecutor mDelayedExecutor;
  
  public NamingDelayedTaskExecutor(String paramString, NamedDelayedTaskExecutor paramNamedDelayedTaskExecutor)
  {
    super(paramString, paramNamedDelayedTaskExecutor);
    this.mDelayedExecutor = paramNamedDelayedTaskExecutor;
  }
  
  public void executeDelayed(Runnable paramRunnable, long paramLong)
  {
    this.mDelayedExecutor.executeDelayed(createTask(paramRunnable), paramLong);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.NamingDelayedTaskExecutor
 * JD-Core Version:    0.7.0.1
 */