package com.google.android.shared.util;

public abstract interface NamedDelayedTaskExecutor
  extends NamedTaskExecutor
{
  public abstract void executeDelayed(NamedTask paramNamedTask, long paramLong);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.NamedDelayedTaskExecutor
 * JD-Core Version:    0.7.0.1
 */