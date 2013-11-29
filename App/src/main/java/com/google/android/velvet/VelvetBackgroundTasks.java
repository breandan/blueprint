package com.google.android.velvet;

public abstract interface VelvetBackgroundTasks
{
  public abstract void forceRun(String paramString, long paramLong);
  
  public abstract void forceRunInterruptingOngoing(String paramString);
  
  public abstract void maybeStartTasks();
  
  public abstract void notifyUiLaunched();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.VelvetBackgroundTasks
 * JD-Core Version:    0.7.0.1
 */