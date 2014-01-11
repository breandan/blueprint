package com.embryo.android.shared.util;

import android.os.Process;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory
        implements ThreadFactory {
    private final int mPriority;

    public PriorityThreadFactory(int paramInt) {
        this.mPriority = paramInt;
    }

    public Thread newThread(Runnable paramRunnable) {
        return new Thread(paramRunnable) {
            public void run() {
                Process.setThreadPriority(PriorityThreadFactory.this.mPriority);
                super.run();
            }
        };
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.embryo.android.shared.util.PriorityThreadFactory

 * JD-Core Version:    0.7.0.1

 */