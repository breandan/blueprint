package com.embryo.android.voicesearch.logger;

import android.util.Log;

public class BugLogger {
    public static void record(int paramInt) {
        Log.w("BugLogger", "Bug [" + paramInt + "]");
        EventLogger.recordClientEvent(29, Integer.valueOf(paramInt));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     BugLogger

 * JD-Core Version:    0.7.0.1

 */