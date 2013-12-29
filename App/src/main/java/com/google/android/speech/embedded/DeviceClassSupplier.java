package com.google.android.speech.embedded;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.google.common.base.Supplier;

public class DeviceClassSupplier
        implements Supplier<Integer> {
    private final ActivityManager mActivityManager;
    private Integer mDeviceClass;

    public DeviceClassSupplier(Context paramContext) {
        this.mActivityManager = ((ActivityManager) paramContext.getSystemService("activity"));
    }

    private Integer calculateDeviceClass() {
        if (!hasNeon()) {
            return Integer.valueOf(5);
        }
        ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
        this.mActivityManager.getMemoryInfo(localMemoryInfo);
        if (localMemoryInfo.totalMem > 700000000L) {
            return Integer.valueOf(100);
        }
        return Integer.valueOf(10);
    }

    private static boolean hasNeon() {
        //TODO ARM NEON commpliance (assume true for now)
        return true;
    }

    public Integer get() {
        try {
            if (this.mDeviceClass == null) {
                this.mDeviceClass = calculateDeviceClass();
            }
            Integer localInteger = this.mDeviceClass;
            return localInteger;
        } finally {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.DeviceClassSupplier

 * JD-Core Version:    0.7.0.1

 */