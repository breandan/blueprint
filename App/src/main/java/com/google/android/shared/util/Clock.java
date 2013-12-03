package com.google.android.shared.util;

public abstract interface Clock {
    public abstract long currentTimeMillis();

    public abstract long elapsedRealtime();

    public abstract void registerTimeResetListener(TimeResetListener paramTimeResetListener);

    public abstract void registerTimeTickListener(TimeTickListener paramTimeTickListener);

    public abstract void unregisterTimeResetListener(TimeResetListener paramTimeResetListener);

    public abstract void unregisterTimeTickListener(TimeTickListener paramTimeTickListener);

    public abstract long uptimeMillis();

    public static abstract interface TimeResetListener {
        public abstract void onTimeReset();
    }

    public static abstract interface TimeTickListener {
        public abstract void onTimeTick();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.Clock

 * JD-Core Version:    0.7.0.1

 */