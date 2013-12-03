package com.google.android.shared.util;

public abstract interface NamedTask
        extends Runnable {
    public abstract void cancelExecution();

    public abstract String getName();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.NamedTask

 * JD-Core Version:    0.7.0.1

 */