package com.google.android.search.core;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ExecutionList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;

public abstract class GmsClientWrapper<T extends GooglePlayServicesClient>
{
  static final long CONNECT_TIMEOUT_MS = 10000L;
  private final Executor mBgExecutor;
  private final T mClient;
  private final Runnable mConnectTimeoutTask = new Runnable()
  {
    public void run()
    {
      GmsClientWrapper.this.disconnect();
    }
  };
  private int mConnectionState = 0;
  private final Runnable mIdleTask = new Runnable()
  {
    public void run()
    {
      GmsClientWrapper.this.disconnect();
    }
  };
  private final long mIdleTimeoutMs;
  private final GmsClientWrapper<T>.InternalCallbacks mInternalCallbacks = new InternalCallbacks(null);
  private final List<GmsClientWrapper<T>.FutureGmsTask<?>> mPendingTasks = Lists.newLinkedList();
  private final Object mStateLock = new Object();
  private final String mTag;
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  
  public GmsClientWrapper(String paramString, Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, long paramLong)
  {
    this.mTag = paramString;
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
    this.mBgExecutor = paramExecutor;
    this.mIdleTimeoutMs = paramLong;
    this.mClient = createClient(paramContext, this.mInternalCallbacks, this.mInternalCallbacks);
  }
  
  private void clearConnectTimeout()
  {
    this.mUiExecutor.cancelExecute(this.mConnectTimeoutTask);
  }
  
  private <V> GmsClientWrapper<T>.FutureGmsTask<V> createTask(Callable<V> paramCallable)
  {
    return new FutureGmsTask(paramCallable, null);
  }
  
  private void disconnect()
  {
    
    synchronized (this.mStateLock)
    {
      if (this.mConnectionState == 0) {
        return;
      }
      this.mClient.disconnect();
      this.mConnectionState = 0;
      clearIdleTimeout();
      clearConnectTimeout();
      flushPendingTasks();
      return;
    }
  }
  
  private void flushPendingTasks()
  {
    synchronized (this.mStateLock)
    {
      if (isConnected())
      {
        Iterator localIterator2 = this.mPendingTasks.iterator();
        if (!localIterator2.hasNext()) {
          break label114;
        }
        FutureGmsTask localFutureGmsTask = (FutureGmsTask)localIterator2.next();
        this.mBgExecutor.execute(localFutureGmsTask);
      }
    }
    RemoteException localRemoteException = new RemoteException("Connection failed");
    Iterator localIterator1 = this.mPendingTasks.iterator();
    while (localIterator1.hasNext()) {
      ((FutureGmsTask)localIterator1.next()).setException(localRemoteException);
    }
    label114:
    this.mPendingTasks.clear();
  }
  
  private void resetIdleTimeout()
  {
    clearIdleTimeout();
    this.mUiExecutor.executeDelayed(this.mIdleTask, this.mIdleTimeoutMs);
  }
  
  private void startConnect()
  {
    synchronized (this.mStateLock)
    {
      if ((this.mConnectionState == 2) || (this.mConnectionState == 1)) {
        return;
      }
      this.mConnectionState = 1;
      this.mUiExecutor.executeDelayed(this.mConnectTimeoutTask, 10000L);
      this.mBgExecutor.execute(new Runnable()
      {
        public void run()
        {
          GmsClientWrapper.this.mClient.connect();
        }
      });
      return;
    }
  }
  
  void clearIdleTimeout()
  {
    this.mUiExecutor.cancelExecute(this.mIdleTask);
  }
  
  protected abstract T createClient(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener);
  
  protected T getClient()
  {
    return this.mClient;
  }
  
  protected <V> GmsFuture<V> invoke(Callable<V> paramCallable)
  {
    FutureGmsTask localFutureGmsTask = createTask(paramCallable);
    synchronized (this.mStateLock)
    {
      if (isConnected())
      {
        resetIdleTimeout();
        this.mBgExecutor.execute(localFutureGmsTask);
        return GmsFuture.create(localFutureGmsTask);
      }
      this.mPendingTasks.add(localFutureGmsTask);
      startConnect();
    }
  }
  
  public boolean isConnected()
  {
    for (;;)
    {
      synchronized (this.mStateLock)
      {
        if (this.mConnectionState == 2)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  private class FutureGmsTask<V>
    extends FutureTask<V>
    implements ListenableFuture<V>
  {
    private final ExecutionList mExecutionList = new ExecutionList();
    private final long mStartRealtime = 0L;
    
    private FutureGmsTask()
    {
      super();
    }
    
    public void addListener(Runnable paramRunnable, Executor paramExecutor)
    {
      this.mExecutionList.add(paramRunnable, paramExecutor);
    }
    
    protected void done()
    {
      this.mExecutionList.execute();
    }
    
    public void setException(Throwable paramThrowable)
    {
      super.setException(paramThrowable);
    }
  }
  
  public static class GmsFuture<V>
  {
    private ListenableFuture<V> mFuture;
    
    private GmsFuture(ListenableFuture<V> paramListenableFuture)
    {
      this.mFuture = paramListenableFuture;
    }
    
    public static <V> GmsFuture<V> create(ListenableFuture<V> paramListenableFuture)
    {
      return new GmsFuture(paramListenableFuture);
    }
    
    public static <V> GmsFuture<V> immediateFailedFuture(Throwable paramThrowable)
    {
      return new GmsFuture(Futures.immediateFailedFuture(paramThrowable));
    }
    
    public static <V> GmsFuture<V> immediateFuture(@Nullable V paramV)
    {
      return new GmsFuture(Futures.immediateFuture(paramV));
    }
    
    public void addConsumer(Consumer<V> paramConsumer, Executor paramExecutor)
    {
      Consumers.addFutureConsumer(this.mFuture, paramConsumer, paramExecutor);
    }
    
    @Nullable
    public V get()
      throws ExecutionException, InterruptedException
    {
      ExtraPreconditions.checkNotMainThread();
      return this.mFuture.get();
    }
    
    @Nullable
    public V get(long paramLong, TimeUnit paramTimeUnit)
      throws ExecutionException, InterruptedException, TimeoutException
    {
      ExtraPreconditions.checkNotMainThread();
      return this.mFuture.get(paramLong, paramTimeUnit);
    }
    
    public boolean isDone()
    {
      return this.mFuture.isDone();
    }
    
    public boolean isSuccessful()
    {
      if (isDone()) {}
      try
      {
        this.mFuture.get();
        return true;
      }
      catch (InterruptedException localInterruptedException)
      {
        return false;
      }
      catch (ExecutionException localExecutionException)
      {
        label20:
        break label20;
      }
    }
    
    @Nullable
    public V safeGet()
    {
      
      try
      {
        Object localObject = this.mFuture.get();
        return localObject;
      }
      catch (InterruptedException localInterruptedException)
      {
        Thread.currentThread().interrupt();
        return null;
      }
      catch (ExecutionException localExecutionException)
      {
        label22:
        break label22;
      }
    }
    
    @Nullable
    public V safeGet(long paramLong, TimeUnit paramTimeUnit)
    {
      
      try
      {
        Object localObject = this.mFuture.get(paramLong, paramTimeUnit);
        return localObject;
      }
      catch (InterruptedException localInterruptedException)
      {
        Thread.currentThread().interrupt();
        return null;
      }
      catch (TimeoutException localTimeoutException)
      {
        break label27;
      }
      catch (ExecutionException localExecutionException)
      {
        label27:
        break label27;
      }
    }
  }
  
  private class InternalCallbacks
    implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
  {
    private InternalCallbacks() {}
    
    public void onConnected(Bundle paramBundle)
    {
      GmsClientWrapper.this.clearConnectTimeout();
      GmsClientWrapper.this.resetIdleTimeout();
      synchronized (GmsClientWrapper.this.mStateLock)
      {
        GmsClientWrapper.access$602(GmsClientWrapper.this, 2);
        GmsClientWrapper.this.flushPendingTasks();
        return;
      }
    }
    
    public void onConnectionFailed(ConnectionResult paramConnectionResult)
    {
      GmsClientWrapper.this.disconnect();
    }
    
    public void onDisconnected()
    {
      GmsClientWrapper.this.disconnect();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GmsClientWrapper
 * JD-Core Version:    0.7.0.1
 */