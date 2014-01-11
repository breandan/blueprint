package com.embryo.android.shared.util;

import java.util.concurrent.Future;

public abstract interface NamedTaskExecutor
{
  public abstract void cancelPendingTasks();
  
  public abstract void execute(NamedTask paramNamedTask);
  
  public abstract Future<?> submit(NamedTask paramNamedTask);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.NamedTaskExecutor

 * JD-Core Version:    0.7.0.1

 */