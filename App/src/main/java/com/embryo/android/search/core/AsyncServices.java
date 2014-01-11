package com.embryo.android.search.core;

import com.embryo.android.shared.util.NamedTaskExecutor;
import com.embryo.android.shared.util.NamingDelayedTaskExecutor;
import com.embryo.android.shared.util.ScheduledSingleThreadedExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public abstract interface AsyncServices
{
  public abstract NamingDelayedTaskExecutor getNamedBackgroundTaskExecutor(String paramString);
  
  public abstract NamingDelayedTaskExecutor getNamedUserFacingTaskExecutor(String paramString);
  
  public abstract ExecutorService getPooledBackgroundExecutorService();
  
  public abstract NamedTaskExecutor getPooledBackgroundTaskExecutor();
  
  public abstract NamedTaskExecutor getPooledUserFacingTaskExecutor();
  
  public abstract ScheduledExecutorService getScheduledBackgroundExecutorService();
  
  public abstract ScheduledSingleThreadedExecutor getUiThreadExecutor();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.search.core.AsyncServices

 * JD-Core Version:    0.7.0.1

 */