package com.google.android.shared.util;

import java.util.concurrent.Executor;

public abstract interface CancellableSingleThreadedExecutor
        extends Executor {
    public abstract void cancelExecute(Runnable paramRunnable);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.CancellableSingleThreadedExecutor

 * JD-Core Version:    0.7.0.1

 */