package com.google.android.shared.util;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ExecutorAsyncTask<Params, Result> {
    private final AtomicBoolean mCancelled = new AtomicBoolean(false);
    private final Executor mExecutor;
    private final Executor mMainThread;

    public ExecutorAsyncTask(Executor paramExecutor, String paramString, NamedTaskExecutor paramNamedTaskExecutor) {
        this(paramExecutor, new NamingTaskExecutor(paramString, paramNamedTaskExecutor));
    }

    public ExecutorAsyncTask(Executor paramExecutor1, Executor paramExecutor2) {
        this.mMainThread = paramExecutor1;
        this.mExecutor = paramExecutor2;
    }

    private void postResult(final Result paramResult) {
        if (this.mCancelled.get()) {
            return;
        }
        this.mMainThread.execute(new Runnable() {
            public void run() {
                ExecutorAsyncTask.this.onPostExecute(paramResult);
            }
        });
    }

    public void cancel() {
        this.mCancelled.set(true);
    }

    protected abstract Result doInBackground(Params... paramVarArgs);

    public void execute(final Params... paramVarArgs) {
        onPreExecute();
        this.mExecutor.execute(new Runnable() {
            public void run() {
                Object localObject = ExecutorAsyncTask.this.doInBackground(paramVarArgs);
                ExecutorAsyncTask.this.postResult(localObject);
            }
        });
    }

    protected void onPostExecute(Result paramResult) {
    }

    protected void onPreExecute() {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.ExecutorAsyncTask

 * JD-Core Version:    0.7.0.1

 */