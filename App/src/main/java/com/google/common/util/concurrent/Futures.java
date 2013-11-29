package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class Futures
{
  private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function()
  {
    public Boolean apply(Constructor<?> paramAnonymousConstructor)
    {
      return Boolean.valueOf(Arrays.asList(paramAnonymousConstructor.getParameterTypes()).contains(String.class));
    }
  }).reverse();
  
  public static <V> void addCallback(ListenableFuture<V> paramListenableFuture, final FutureCallback<? super V> paramFutureCallback, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramFutureCallback);
    paramListenableFuture.addListener(new Runnable()
    {
      public void run()
      {
        try
        {
          Object localObject = Uninterruptibles.getUninterruptibly(this.val$future);
          paramFutureCallback.onSuccess(localObject);
          return;
        }
        catch (ExecutionException localExecutionException)
        {
          paramFutureCallback.onFailure(localExecutionException.getCause());
          return;
        }
        catch (RuntimeException localRuntimeException)
        {
          paramFutureCallback.onFailure(localRuntimeException);
          return;
        }
        catch (Error localError)
        {
          paramFutureCallback.onFailure(localError);
        }
      }
    }, paramExecutor);
  }
  
  public static <V> V getUnchecked(Future<V> paramFuture)
  {
    Preconditions.checkNotNull(paramFuture);
    try
    {
      Object localObject = Uninterruptibles.getUninterruptibly(paramFuture);
      return localObject;
    }
    catch (ExecutionException localExecutionException)
    {
      wrapAndThrowUnchecked(localExecutionException.getCause());
      throw new AssertionError();
    }
  }
  
  public static <V> ListenableFuture<V> immediateFailedFuture(Throwable paramThrowable)
  {
    Preconditions.checkNotNull(paramThrowable);
    SettableFuture localSettableFuture = SettableFuture.create();
    localSettableFuture.setException(paramThrowable);
    return localSettableFuture;
  }
  
  public static <V> ListenableFuture<V> immediateFuture(@Nullable V paramV)
  {
    SettableFuture localSettableFuture = SettableFuture.create();
    localSettableFuture.set(paramV);
    return localSettableFuture;
  }
  
  public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> paramIterable)
  {
    return new ListFuture(ImmutableList.copyOf(paramIterable), false, MoreExecutors.sameThreadExecutor());
  }
  
  private static void wrapAndThrowUnchecked(Throwable paramThrowable)
  {
    if ((paramThrowable instanceof Error)) {
      throw new ExecutionError((Error)paramThrowable);
    }
    throw new UncheckedExecutionException(paramThrowable);
  }
  
  private static class ListFuture<V>
    extends AbstractFuture<List<V>>
  {
    final boolean allMustSucceed;
    ImmutableList<? extends ListenableFuture<? extends V>> futures;
    final AtomicInteger remaining;
    List<V> values;
    
    ListFuture(ImmutableList<? extends ListenableFuture<? extends V>> paramImmutableList, boolean paramBoolean, Executor paramExecutor)
    {
      this.futures = paramImmutableList;
      this.values = Lists.newArrayListWithCapacity(paramImmutableList.size());
      this.allMustSucceed = paramBoolean;
      this.remaining = new AtomicInteger(paramImmutableList.size());
      init(paramExecutor);
    }
    
    private void callAllGets()
      throws InterruptedException
    {
      ImmutableList localImmutableList = this.futures;
      if ((localImmutableList != null) && (!isDone())) {
        label42:
        do
        {
          Iterator localIterator = localImmutableList.iterator();
          break label42;
          if (!localIterator.hasNext()) {
            break;
          }
          ListenableFuture localListenableFuture = (ListenableFuture)localIterator.next();
          while (!localListenableFuture.isDone()) {
            try
            {
              localListenableFuture.get();
            }
            catch (Error localError)
            {
              throw localError;
            }
            catch (InterruptedException localInterruptedException)
            {
              throw localInterruptedException;
            }
            catch (Throwable localThrowable) {}
          }
        } while (!this.allMustSucceed);
      }
    }
    
    private void init(Executor paramExecutor)
    {
      addListener(new Runnable()
      {
        public void run()
        {
          Futures.ListFuture.this.values = null;
          Futures.ListFuture.this.futures = null;
        }
      }, MoreExecutors.sameThreadExecutor());
      if (this.futures.isEmpty()) {
        set(Lists.newArrayList(this.values));
      }
      for (;;)
      {
        return;
        for (int i = 0; i < this.futures.size(); i++) {
          this.values.add(null);
        }
        ImmutableList localImmutableList = this.futures;
        for (final int j = 0; j < localImmutableList.size(); j++)
        {
          final ListenableFuture localListenableFuture = (ListenableFuture)localImmutableList.get(j);
          localListenableFuture.addListener(new Runnable()
          {
            public void run()
            {
              Futures.ListFuture.this.setOneValue(j, localListenableFuture);
            }
          }, paramExecutor);
        }
      }
    }
    
    private void setOneValue(int paramInt, Future<? extends V> paramFuture)
    {
      boolean bool = true;
      List localList1 = this.values;
      if ((isDone()) || (localList1 == null)) {
        Preconditions.checkState(this.allMustSucceed, "Future was done before all dependencies completed");
      }
      label106:
      int i;
      for (;;)
      {
        return;
        try
        {
          Preconditions.checkState(paramFuture.isDone(), "Tried to set value from future which is not done");
          localList1.set(paramInt, Uninterruptibles.getUninterruptibly(paramFuture));
          int i1 = this.remaining.decrementAndGet();
          if (i1 >= 0) {}
          for (;;)
          {
            Preconditions.checkState(bool, "Less than 0 remaining futures");
            if (i1 != 0) {
              break;
            }
            List localList7 = this.values;
            if (localList7 == null) {
              break label106;
            }
            set(Lists.newArrayList(localList7));
            return;
            bool = false;
          }
          Preconditions.checkState(isDone());
          return;
        }
        catch (CancellationException localCancellationException)
        {
          if (this.allMustSucceed) {
            cancel(false);
          }
          int n = this.remaining.decrementAndGet();
          if (n >= 0) {}
          for (;;)
          {
            Preconditions.checkState(bool, "Less than 0 remaining futures");
            if (n != 0) {
              break;
            }
            List localList6 = this.values;
            if (localList6 == null) {
              break label181;
            }
            set(Lists.newArrayList(localList6));
            return;
            bool = false;
          }
          Preconditions.checkState(isDone());
          return;
        }
        catch (ExecutionException localExecutionException)
        {
          if (this.allMustSucceed) {
            setException(localExecutionException.getCause());
          }
          int m = this.remaining.decrementAndGet();
          if (m >= 0) {}
          for (;;)
          {
            Preconditions.checkState(bool, "Less than 0 remaining futures");
            if (m != 0) {
              break;
            }
            List localList5 = this.values;
            if (localList5 == null) {
              break label260;
            }
            set(Lists.newArrayList(localList5));
            return;
            bool = false;
          }
          Preconditions.checkState(isDone());
          return;
        }
        catch (RuntimeException localRuntimeException)
        {
          if (this.allMustSucceed) {
            setException(localRuntimeException);
          }
          int k = this.remaining.decrementAndGet();
          if (k >= 0) {}
          for (;;)
          {
            Preconditions.checkState(bool, "Less than 0 remaining futures");
            if (k != 0) {
              break;
            }
            List localList4 = this.values;
            if (localList4 == null) {
              break label336;
            }
            set(Lists.newArrayList(localList4));
            return;
            bool = false;
          }
          Preconditions.checkState(isDone());
          return;
        }
        catch (Error localError)
        {
          label181:
          label336:
          setException(localError);
          label260:
          int j = this.remaining.decrementAndGet();
          if (j >= 0) {}
          for (;;)
          {
            Preconditions.checkState(bool, "Less than 0 remaining futures");
            if (j != 0) {
              break;
            }
            List localList3 = this.values;
            if (localList3 == null) {
              break label405;
            }
            set(Lists.newArrayList(localList3));
            return;
            bool = false;
          }
          label405:
          Preconditions.checkState(isDone());
          return;
        }
        finally
        {
          i = this.remaining.decrementAndGet();
          if (i < 0) {
            break label464;
          }
        }
      }
      Preconditions.checkState(bool, "Less than 0 remaining futures");
      if (i == 0)
      {
        List localList2 = this.values;
        if (localList2 == null) {
          break label469;
        }
        set(Lists.newArrayList(localList2));
      }
      for (;;)
      {
        throw localObject;
        label464:
        bool = false;
        break;
        label469:
        Preconditions.checkState(isDone());
      }
    }
    
    public List<V> get()
      throws InterruptedException, ExecutionException
    {
      callAllGets();
      return (List)super.get();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Futures
 * JD-Core Version:    0.7.0.1
 */