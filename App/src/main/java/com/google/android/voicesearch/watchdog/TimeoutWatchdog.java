package com.google.android.voicesearch.watchdog;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeoutWatchdog {
    private final Runnable mOnTimeoutRunnable;
    private ScheduledFuture<?> mOnTimeoutTask;
    private final ScheduledExecutorService mScheduler;
    private final int mTimeOutMillis;
    private volatile long mTimeoutTimestamp;

    public TimeoutWatchdog(int paramInt, Runnable paramRunnable) {
        this(paramInt, Executors.newScheduledThreadPool(1), paramRunnable);
    }

    public TimeoutWatchdog(int paramInt, ScheduledExecutorService paramScheduledExecutorService, Runnable paramRunnable) {
        this.mOnTimeoutRunnable = paramRunnable;
        this.mTimeOutMillis = paramInt;
        this.mScheduler = paramScheduledExecutorService;
    }

    private void scheduleTask() {
        long l = Math.max(1L, this.mTimeoutTimestamp - System.currentTimeMillis());
        try {
            this.mOnTimeoutTask = this.mScheduler.schedule(new WatchdogTask(null), l, TimeUnit.MILLISECONDS);
            return;
        } catch (RejectedExecutionException localRejectedExecutionException) {
        }
    }

    public void extend() {
        this.mTimeoutTimestamp = (System.currentTimeMillis() + this.mTimeOutMillis);
    }

    public void start() {
        extend();
        scheduleTask();
    }

    public void stop() {
        if (this.mOnTimeoutTask != null) {
            this.mOnTimeoutTask.cancel(true);
        }
        this.mScheduler.shutdownNow();
    }

    private class WatchdogTask
            implements Runnable {
        private WatchdogTask() {
        }

        public void run() {
            if (System.currentTimeMillis() >= TimeoutWatchdog.this.mTimeoutTimestamp) {
                TimeoutWatchdog.this.mOnTimeoutRunnable.run();
                return;
            }
            TimeoutWatchdog.this.scheduleTask();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.watchdog.TimeoutWatchdog

 * JD-Core Version:    0.7.0.1

 */