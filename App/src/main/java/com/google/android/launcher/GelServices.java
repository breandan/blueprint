package com.google.android.launcher;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.GelStartupPrefs;
import com.google.android.shared.util.HandlerScheduledExecutor;
import com.google.android.shared.util.PriorityThreadFactory;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GelServices
{
  private static GelServices sInstance;
  private final Executor mBgExecutor;
  private final GelStartupPrefs mGelStartupPrefs;
  private final NowRemoteClient mNowRemoteClient;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  private GelServices(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, NowRemoteClient paramNowRemoteClient, GelStartupPrefs paramGelStartupPrefs)
  {
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mBgExecutor = paramExecutor;
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mGelStartupPrefs = paramGelStartupPrefs;
  }
  
  private static GelServices createGelServices(Context paramContext)
  {
    HandlerScheduledExecutor localHandlerScheduledExecutor = new HandlerScheduledExecutor(new Handler(Looper.getMainLooper()), Looper.myQueue());
    ExecutorService localExecutorService = Executors.newCachedThreadPool(new PriorityThreadFactory(10));
    return new GelServices(localHandlerScheduledExecutor, localExecutorService, new NowRemoteClient(paramContext, localExecutorService, localHandlerScheduledExecutor), new GelStartupPrefs(paramContext));
  }
  
  public static GelServices get(Context paramContext)
  {
    try
    {
      ExtraPreconditions.checkMainThread();
      if (sInstance == null) {
        sInstance = createGelServices(paramContext);
      }
      GelServices localGelServices = sInstance;
      return localGelServices;
    }
    finally {}
  }
  
  public GelStartupPrefs getGelStartupPrefs()
  {
    return this.mGelStartupPrefs;
  }
  
  public NowRemoteClient getNowRemoteClient()
  {
    return this.mNowRemoteClient;
  }
  
  public ScheduledSingleThreadedExecutor getUiThreadExecutor()
  {
    return this.mUiThreadExecutor;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.launcher.GelServices
 * JD-Core Version:    0.7.0.1
 */