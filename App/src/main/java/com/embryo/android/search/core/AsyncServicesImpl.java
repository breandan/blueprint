package com.embryo.android.search.core;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

import com.embryo.android.shared.util.HandlerScheduledExecutor;
import com.embryo.android.shared.util.NamedDelayedTaskExecutor;
import com.embryo.android.shared.util.NamingDelayedTaskExecutor;
import com.embryo.android.shared.util.PriorityThreadFactory;
import com.embryo.android.shared.util.ScheduledSingleThreadedExecutor;
import com.embryo.android.velvet.VelvetStrictMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class AsyncServicesImpl
  implements AsyncServices
{
  private final int BACKGROUND_THREAD_POOL_SIZE = 2;
  private final int USER_FACING_THREAD_POOL_SIZE = 5;
  private final ExecutorService mBackgroundExecutorService;
  private final NamedDelayedTaskExecutor mBackgroundTaskThreadPool;
  private final ScheduledExecutorService mScheduledBackgroundExecutorService;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  private final NamedDelayedTaskExecutor mUserFacingTaskThreadPool;
  
  public AsyncServicesImpl()
  {
    Handler localHandler = new Handler(Looper.getMainLooper());
    this.mUiThreadExecutor = VelvetStrictMode.maybeTrackUiExecutor(new HandlerScheduledExecutor(localHandler, getLooperQueueForThread(localHandler)));
    ThreadFactory localThreadFactory = VelvetStrictMode.applyThreadPolicy(new PriorityThreadFactory(10));
    this.mUserFacingTaskThreadPool = new PoolingNamedTaskExecutor(5, localThreadFactory, false);
    this.mBackgroundExecutorService = Executors.newCachedThreadPool(localThreadFactory);
    this.mBackgroundTaskThreadPool = new PoolingNamedTaskExecutor(2, localThreadFactory, true);
    this.mScheduledBackgroundExecutorService = Executors.newScheduledThreadPool(1, localThreadFactory);
  }
  
  private MessageQueue getLooperQueueForThread(Handler paramHandler)
  {
    if (Looper.myLooper() == paramHandler.getLooper()) {
      return Looper.myQueue();
    }
    final ConditionVariable localConditionVariable = new ConditionVariable();
    final MessageQueue[] arrayOfMessageQueue = new MessageQueue[1];
    paramHandler.post(new Runnable()
    {
      public void run()
      {
        arrayOfMessageQueue[0] = Looper.myQueue();
        localConditionVariable.open();
      }
    });
    localConditionVariable.block();
    return arrayOfMessageQueue[0];
  }
  
  public NamingDelayedTaskExecutor getNamedBackgroundTaskExecutor(String paramString)
  {
    return new NamingDelayedTaskExecutor(paramString, this.mBackgroundTaskThreadPool);
  }
  
  public NamingDelayedTaskExecutor getNamedUserFacingTaskExecutor(String paramString)
  {
    return new NamingDelayedTaskExecutor(paramString, getPooledUserFacingTaskExecutor());
  }
  
  public ExecutorService getPooledBackgroundExecutorService()
  {
    return this.mBackgroundExecutorService;
  }
  
  public NamedDelayedTaskExecutor getPooledBackgroundTaskExecutor()
  {
    return this.mBackgroundTaskThreadPool;
  }
  
  public NamedDelayedTaskExecutor getPooledUserFacingTaskExecutor()
  {
    return this.mUserFacingTaskThreadPool;
  }
  
  public ScheduledExecutorService getScheduledBackgroundExecutorService()
  {
    return this.mScheduledBackgroundExecutorService;
  }
  
  public ScheduledSingleThreadedExecutor getUiThreadExecutor()
  {
    return this.mUiThreadExecutor;
  }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.search.core.AsyncServicesImpl

 * JD-Core Version:    0.7.0.1

 */