package com.embryo.android.shared.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

public class Util {
    public static final int SDK_INT = Build.VERSION.SDK_INT;

    public static boolean isLowRamDevice(Context paramContext) {
        int i = SDK_INT;
        boolean bool = false;
        //TODO
//        if (i >= 19) {
//            bool = ((ActivityManager) paramContext.getSystemService("activity")).isLowRamDevice();
//        }
        return bool;
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Util

 * JD-Core Version:    0.7.0.1

 */