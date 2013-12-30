package com.google.android.shared.util;

import android.os.Process;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentUtils {
    private static ThreadFactory createBackgroundThreadFactory(final String threadName) {
        return new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger(0);

            public Thread newThread(final Runnable paramAnonymousRunnable) {
                return new Thread(threadName + "-" + this.counter.incrementAndGet()) {
                    public void run() {
                        Process.setThreadPriority(10);
                        paramAnonymousRunnable.run();
                    }
                };
            }
        };
    }

    private static ScheduledExecutorService createLoggingScheduledExecutorService(int paramInt, ThreadFactory paramThreadFactory, final String paramString) {
        Preconditions.checkArgument(paramInt > 0);
        return new ScheduledThreadPoolExecutor(paramInt, paramThreadFactory) {
            protected void afterExecute(Runnable paramAnonymousRunnable, Throwable paramAnonymousThrowable) {
                if ((paramAnonymousThrowable instanceof RuntimeException)) {
                    Log.w(paramString, "RuntimeException occured", paramAnonymousThrowable);
                }
                Future localFuture = null;
                if ((paramAnonymousRunnable instanceof Future)) {
                    localFuture = (Future) paramAnonymousRunnable;
                    try {
                        localFuture.get();
                        super.afterExecute(paramAnonymousRunnable, paramAnonymousThrowable);
                    } catch (ExecutionException localExecutionException) {
                        Log.w(paramString, "ExecutionException occured", localExecutionException.getCause());
                    } catch (InterruptedException localInterruptedException) {
                        Thread.currentThread().interrupt();
                    } catch (CancellationException localCancellationException) {
                        return;
                    }
                }
            }
        };
    }

    public static ScheduledExecutorService createSafeScheduledExecutorService(int paramInt, String paramString) {
        return createSafeScheduledExecutorService(paramInt, createBackgroundThreadFactory(paramString), false);
    }

    public static ScheduledExecutorService createSafeScheduledExecutorService(int coreSize, ThreadFactory factory, boolean allowCoreThreadTimeout) {
        Preconditions.checkArgument((coreSize > 0));
        final int MAX_EXPECTED_QUEUE_SIZE = coreSize * 0x2;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(coreSize, factory) {

            public void execute(Runnable r) {
                int queueBefore = getQueue().size();
                super.execute(r);
                int queueAfter = getQueue().size();
                if (queueAfter > MAX_EXPECTED_QUEUE_SIZE) {
                    Log.w("Search.ConcurrentUtils", "Executor queue length is now " + queueAfter + ". Perhaps some tasks are too long, or the pool is too small. [" + Thread.currentThread().getName() + "]");
                }
            }

            protected void afterExecute(Runnable r, Throwable t) {
                if ((t instanceof RuntimeException)) {
                    Log.w(this.getClass().getName(), "RuntimeException occured", t);
                }
                Future localFuture = null;
                if ((r instanceof Future)) {
                    localFuture = (Future) r;
                    try {
                        localFuture.get();
                        super.afterExecute(r, t);
                    } catch (ExecutionException localExecutionException) {
                        Log.w(this.getClass().getName(), "ExecutionException occured", localExecutionException.getCause());
                    } catch (InterruptedException localInterruptedException) {
                        Thread.currentThread().interrupt();
                    } catch (CancellationException localCancellationException) {
                        return;
                    }
                }
            }
        };
        if (allowCoreThreadTimeout) {
            executor.setKeepAliveTime(0x3c, TimeUnit.SECONDS);
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }

    public static ScheduledExecutorService createSingleThreadedScheduledExecutorService(String paramString) {
        return createLoggingScheduledExecutorService(1, createThreadFactory(paramString), paramString);
    }

    private static ThreadFactory createThreadFactory(String paramString) {
        return new ThreadFactoryBuilder().setNameFormat(paramString + "-%d").build();
    }

    public static ExecutorService newSingleThreadExecutor(String paramString) {
        return Executors.newSingleThreadExecutor(createBackgroundThreadFactory(paramString));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.ConcurrentUtils

 * JD-Core Version:    0.7.0.1

 */