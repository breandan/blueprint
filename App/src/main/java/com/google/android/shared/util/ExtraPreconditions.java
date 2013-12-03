package com.google.android.shared.util;

import android.os.Looper;
import android.util.Log;

import com.google.common.base.Preconditions;

public class ExtraPreconditions {
    private static boolean sThreadChecksDisabled;

    public static void checkHoldsLock(Object paramObject) {
    }

    public static void checkMainThread() {
        if ((Looper.getMainLooper().getThread() == Thread.currentThread()) || (sThreadChecksDisabled)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            return;
        }
    }

    public static void checkNotMainThread() {
        if ((Looper.getMainLooper().getThread() != Thread.currentThread()) || (sThreadChecksDisabled)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            return;
        }
    }

    public static ThreadCheck createAnyThreadCheck() {
        return new ThreadCheck();
    }

    public static ThreadCheck createNotSetThreadsCheck(String... paramVarArgs) {
        return new ThreadCheck();
    }

    public static ThreadCheck createSameThreadCheck() {
        return new ThreadCheck();
    }

    public static ThreadCheck createSetThreadsCheck(String... paramVarArgs) {
        return new ThreadCheck();
    }

    public static void setThreadChecksEnabled(boolean paramBoolean) {
        if (!paramBoolean) {
        }
        for (boolean bool = true; ; bool = false) {
            sThreadChecksDisabled = bool;
            return;
        }
    }

    public static class DebugSameThread
            extends ExtraPreconditions.ThreadCheck {
        private Thread mThread;
        private Throwable mThrowable;

        public ExtraPreconditions.ThreadCheck check() {
            try {
                Thread localThread = Thread.currentThread();
                if (this.mThread == null) {
                    this.mThread = localThread;
                    this.mThrowable = new Throwable().fillInStackTrace();
                }
                if (this.mThread != localThread) {
                    Log.e("SameThread", "Expected thread: " + this.mThread.getName(), this.mThrowable);
                    Log.e("SameThread", "Current thread: " + localThread.getName(), new Throwable().fillInStackTrace());
                    throw new IllegalStateException("Different threads");
                }
            } finally {
            }
            return this;
        }

        public void reset() {
            try {
                this.mThread = null;
                return;
            } finally {
                localObject =finally;
                throw localObject;
            }
        }
    }

    public static class ThreadCheck {
        public ThreadCheck check() {
            return this;
        }

        public void reset() {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.ExtraPreconditions

 * JD-Core Version:    0.7.0.1

 */