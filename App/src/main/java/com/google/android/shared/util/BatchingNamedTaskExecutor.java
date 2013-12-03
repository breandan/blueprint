package com.google.android.shared.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public class BatchingNamedTaskExecutor
        implements NamedTaskExecutor {
    private final NamedTaskExecutor mExecutor;
    private final ArrayList<NamedTask> mQueuedTasks = Lists.newArrayList();
    private final Set<RunningNamedTask> mRunningTasks = Sets.newHashSet();

    public BatchingNamedTaskExecutor(NamedTaskExecutor paramNamedTaskExecutor) {
        this.mExecutor = paramNamedTaskExecutor;
    }

    private void dispatch(NamedTask paramNamedTask) {
        RunningNamedTask localRunningNamedTask = new RunningNamedTask(paramNamedTask);
        synchronized (this.mRunningTasks) {
            this.mRunningTasks.add(localRunningNamedTask);
            this.mExecutor.execute(localRunningNamedTask);
            return;
        }
    }

    private static <T extends NamedTask> T[] getNextBatch(List<T> paramList, int paramInt, T[] paramArrayOfT) {
        try {
            List localList = paramList.subList(0, Math.min(paramList.size(), paramInt));
            NamedTask[] arrayOfNamedTask = (NamedTask[]) localList.toArray(paramArrayOfT);
            localList.clear();
            return arrayOfNamedTask;
        } finally {
        }
    }

    public void cancelPendingTasks() {
        synchronized (this.mQueuedTasks) {
            this.mQueuedTasks.clear();
            this.mExecutor.cancelPendingTasks();
            return;
        }
    }

    public void cancelRunningTasks() {
        synchronized (this.mRunningTasks) {
            Iterator localIterator = this.mRunningTasks.iterator();
            if (localIterator.hasNext()) {
                ((RunningNamedTask) localIterator.next()).cancelExecution();
            }
        }
        this.mRunningTasks.clear();
    }

    public void execute(NamedTask paramNamedTask) {
        synchronized (this.mQueuedTasks) {
            this.mQueuedTasks.add(paramNamedTask);
            return;
        }
    }

    public int executeNextBatch(int paramInt) {
        NamedTask[] arrayOfNamedTask = getNextBatch(this.mQueuedTasks, paramInt, new NamedTask[0]);
        int i = arrayOfNamedTask.length;
        for (int j = 0; j < i; j++) {
            dispatch(arrayOfNamedTask[j]);
        }
        return arrayOfNamedTask.length;
    }

    public Future<?> submit(NamedTask paramNamedTask) {
        throw new UnsupportedOperationException("submit() method not supported.");
    }

    private class RunningNamedTask
            implements NamedTask {
        private final NamedTask mTask;

        public RunningNamedTask(NamedTask paramNamedTask) {
            this.mTask = paramNamedTask;
        }

        private void removeFromRunningTasks() {
            synchronized (BatchingNamedTaskExecutor.this.mRunningTasks) {
                BatchingNamedTaskExecutor.this.mRunningTasks.remove(this);
                return;
            }
        }

        public void cancelExecution() {
            this.mTask.cancelExecution();
        }

        public String getName() {
            return this.mTask.getName();
        }

        public void run() {
            this.mTask.run();
            removeFromRunningTasks();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.BatchingNamedTaskExecutor

 * JD-Core Version:    0.7.0.1

 */