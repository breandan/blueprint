package com.google.android.voicesearch.logger;

public class EventUtils {
    public static int getGroup(int paramInt) {
        return 0xF0000000 & paramInt;
    }

    public static int getId(int paramInt) {
        return 0xFFFFFFF & paramInt;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.logger.EventUtils

 * JD-Core Version:    0.7.0.1

 */