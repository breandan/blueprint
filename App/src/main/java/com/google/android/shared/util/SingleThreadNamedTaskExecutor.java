package com.google.android.shared.util;

import android.util.Log;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public class SingleThreadNamedTaskExecutor
        implements NamedTaskExecutor {
    private final LinkedBlockingQueue<NamedTask> mQueue = new LinkedBlockingQueue();
    private final Thread mWorker;

    public SingleThreadNamedTaskExecutor(ThreadFactory paramThreadFactory) {
        this.mWorker = paramThreadFactory.newThread(new Worker(null));
        this.mWorker.start();
    }

    public static Factory<NamedTaskExecutor> factory(ThreadFactory paramThreadFactory) {
        new Factory() {
            public NamedTaskExecutor create() {
                return new SingleThreadNamedTaskExecutor(this.val$threadFactory);
            }
        };
    }

    public void cancelPendingTasks() {
        this.mQueue.clear();
    }

    public void execute(NamedTask paramNamedTask) {
        this.mQueue.add(paramNamedTask);
    }

    public Future<?> submit(NamedTask paramNamedTask) {
        throw new UnsupportedOperationException("submit() method not supported.");
    }

    private class Worker
            implements Runnable {
        private Worker() {
        }

        private void loop() {
            Thread localThread = Thread.currentThread();
            String str = localThread.getName();
            try {
                for (; ; ) {
                    NamedTask localNamedTask = (NamedTask) SingleThreadNamedTaskExecutor.this.mQueue.take();
                    localThread.setName(str + " " + localNamedTask.getName());
                    try {
                        localNamedTask.run();
                    } catch (RuntimeException localRuntimeException) {
                        Log.e("QSB.SingleThreadNamedTaskExecutor", "Task " + localNamedTask.getName() + " failed", localRuntimeException);
                    }
                }
            } catch (InterruptedException localInterruptedException) {
            }
        }

        public void run() {
            loop();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.SingleThreadNamedTaskExecutor

 * JD-Core Version:    0.7.0.1

 */