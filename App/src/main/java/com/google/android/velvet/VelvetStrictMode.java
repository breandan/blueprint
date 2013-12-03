package com.google.android.velvet;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;

import java.util.concurrent.ThreadFactory;

public class VelvetStrictMode {
    public static ThreadFactory applyThreadPolicy(ThreadFactory paramThreadFactory) {
        return paramThreadFactory;
    }

    public static void checkStartupAtLeast(int paramInt) {
    }

    public static void init(Context paramContext) {
    }

    public static void logW(String paramString1, String paramString2) {
        Log.w(paramString1, paramString2);
    }

    public static void logW(String paramString1, String paramString2, Throwable paramThrowable) {
        Log.w(paramString1, paramString2, paramThrowable);
    }

    public static void logWDeveloper(String paramString1, String paramString2) {
        Log.w(paramString1, paramString2);
    }

    public static ScheduledSingleThreadedExecutor maybeTrackUiExecutor(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor) {
        return paramScheduledSingleThreadedExecutor;
    }

    public static void onPreImeKeyEvent(KeyEvent paramKeyEvent) {
    }

    public static void onStartupPoint(int paramInt) {
    }

    public static void onUiOperationEnd(String paramString) {
    }

    public static void onUiOperationStart(String paramString) {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.VelvetStrictMode

 * JD-Core Version:    0.7.0.1

 */