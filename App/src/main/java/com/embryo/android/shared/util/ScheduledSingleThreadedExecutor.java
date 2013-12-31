package com.embryo.android.shared.util;

import android.os.Handler;

public abstract interface ScheduledSingleThreadedExecutor
        extends CancellableSingleThreadedExecutor {
    public abstract void executeDelayed(Runnable paramRunnable, long paramLong);

    public abstract void executeOnIdle(Runnable paramRunnable);

    public abstract Handler getHandler();

    public abstract boolean isThisThread();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ScheduledSingleThreadedExecutor

 * JD-Core Version:    0.7.0.1

 */