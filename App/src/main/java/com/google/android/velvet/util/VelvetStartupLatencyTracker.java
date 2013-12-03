package com.google.android.velvet.util;

import android.os.SystemClock;

public class VelvetStartupLatencyTracker {
    private static final long FIRST_JAVA_RUN = ;
    private static long sStartupStart = FIRST_JAVA_RUN;
    private static int sStartupType = 0;

    public static void registerActivityCreate() {
        long l = SystemClock.elapsedRealtime();
        if (sStartupStart < l - 5000L) {
            sStartupType = 1;
            sStartupStart = l;
        }
    }

    public static void registerActivityRestart() {
        sStartupStart = SystemClock.elapsedRealtime();
        sStartupType = 2;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.util.VelvetStartupLatencyTracker

 * JD-Core Version:    0.7.0.1

 */