package com.embryo.android.shared.util;

import java.util.concurrent.Executor;

public abstract interface CancellableSingleThreadedExecutor
        extends Executor {
    public abstract void cancelExecute(Runnable paramRunnable);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     CancellableSingleThreadedExecutor

 * JD-Core Version:    0.7.0.1

 */