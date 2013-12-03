package com.google.android.shared.util;

import java.util.concurrent.Future;

public class NamingTaskExecutor
        extends ExecutorServiceAdapter {
    protected final NamedTaskExecutor mExecutor;
    protected final String mName;

    public NamingTaskExecutor(String paramString, NamedTaskExecutor paramNamedTaskExecutor) {
        this.mName = paramString;
        this.mExecutor = paramNamedTaskExecutor;
    }

    protected NamedTask createTask(final Runnable paramRunnable) {
        new NonCancellableNamedTask() {
            public String getName() {
                return NamingTaskExecutor.this.mName;
            }

            public void run() {
                paramRunnable.run();
            }
        };
    }

    public void execute(Runnable paramRunnable) {
        this.mExecutor.execute(createTask(paramRunnable));
    }

    public Future<?> submit(Runnable paramRunnable) {
        return this.mExecutor.submit(createTask(paramRunnable));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.NamingTaskExecutor

 * JD-Core Version:    0.7.0.1

 */