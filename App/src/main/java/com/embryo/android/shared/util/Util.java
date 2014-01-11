package com.embryo.android.shared.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.embryo.common.base.Preconditions;
import com.embryo.common.collect.Lists;
import com.embryo.common.collect.Maps;
import com.embryo.common.collect.Sets;
import com.embryo.common.io.ByteStreams;
import com.embryo.common.io.Closeables;

public class Util {
    public static final int SDK_INT = Build.VERSION.SDK_INT;

    public static boolean isLowRamDevice(Context paramContext) {
        int i = SDK_INT;
        boolean bool = false;
        if (i >= 19) {
            bool = ((ActivityManager) paramContext.getSystemService("activity")).isLowRamDevice();
        }
        return bool;
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Util

 * JD-Core Version:    0.7.0.1

 */