package com.embryo.android.voicesearch.audio;

import android.media.AudioManager;
import android.util.Log;

import com.embryo.android.shared.util.StopWatch;
import com.google.android.voicesearch.bluetooth.BluetoothShim;

import java.util.concurrent.Executor;

public class AudioRouterImpl
        implements AudioRouter {
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int paramAnonymousInt) {
            Log.i("AudioRouter", "Audio focus change " + paramAnonymousInt);
        }
    };
    private final AudioManager mAudioManager;
    private final Executor mExecutor;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mExecutorThreadCheck;
    private final Object mLock = new Object();
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mNotExecutorThreadCheck;
    private final com.embryo.android.voicesearch.settings.Settings mSettings;
    private boolean mAudioFocusObtained = false;
    private int mAwaitState = 12;
    private int mRoute = 3;
    private AudioRouter.AudioRouteListener mRouteListener = null;
    private boolean mScoFailed = false;
    private boolean mSynchronizePending = false;

    public AudioRouterImpl(com.embryo.android.voicesearch.settings.Settings paramSettings, AudioManager paramAudioManager, Executor paramExecutor, com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck paramThreadCheck1, com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck paramThreadCheck2) {
        this.mSettings = paramSettings;
        this.mAudioManager = paramAudioManager;
        this.mExecutor = paramExecutor;
        this.mExecutorThreadCheck = paramThreadCheck1;
        this.mNotExecutorThreadCheck = paramThreadCheck2;
    }

    private static final String enumIntToString(int paramInt) {
        if (paramInt == 0) {
            return "ROUTE_BLUETOOTH_WANTED";
        }
        if (paramInt == 1) {
            return "ROUTE_BLUETOOTH_PREFERRED";
        }
        if (paramInt == 2) {
            return "ROUTE_NO_BLUETOOTH";
        }
        if (paramInt == 3) {
            return "ROUTE_NONE";
        }
        if (paramInt == 10) {
            return "AWAIT_STATE_AWAITING";
        }
        if (paramInt == 11) {
            return "AWAIT_STATE_CANCELLED";
        }
        if (paramInt == 12) {
            return "AWAIT_STATE_NONE";
        }
        return "[Illegal value]";
    }

    private static boolean isBluetoothRoute(int paramInt) {
        return (paramInt == 0) || (paramInt == 1);
    }

    private boolean isBluetoothRoute() {
        com.embryo.android.shared.util.ExtraPreconditions.checkHoldsLock(this.mLock);
        return isBluetoothRoute(this.mRoute);
    }

    private boolean isRouteActive() {
        com.embryo.android.shared.util.ExtraPreconditions.checkHoldsLock(this.mLock);
        return this.mRoute != 3;
    }

    private boolean isWiredHeadsetConnected() {
        return this.mAudioManager.isWiredHeadsetOn();
    }

    private void maybeAbandonAudioFocus() {
        mExecutor.execute(new Runnable() {

            public void run() {
                if (mAudioFocusObtained) {
                    int result = mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                    if (result == 0x1) {
                        mAudioFocusObtained = false;
                        return;
                    }
                    Log.w("AudioRouter", "Unable to release STREAM_SYSTEM audio focus");
                }
            }
        });
    }

    private void maybeRequestAudioFocus() {
        mExecutor.execute(new Runnable() {
            public void run() {
                if (!mAudioFocusObtained) {
                    int focusType = com.embryo.android.shared.util.Util.SDK_INT >= 0x13 ? 0x4 : 0x2;
                    int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, 0x1, focusType);
                    if (result == 0x1) {
                        mAudioFocusObtained = true;
                        return;
                    }
                    Log.w("AudioRouter", "Unable to obtain STREAM_SYSTEM audio focus");
                }
            }
        });
    }

    private boolean shouldUseBluetoothDevice(BluetoothShim.BluetoothDevice paramBluetoothDevice) {
        return false;
    }

    public boolean awaitRouting() {
        this.mNotExecutorThreadCheck.check();
        com.embryo.android.shared.util.ExtraPreconditions.checkNotMainThread();
        StopWatch localStopWatch = new StopWatch().start();
        try {
            try {
                int k = localStopWatch.getElapsedTime();
                if (k > 200L) {
                    Log.w("AudioRouter", "awaitRouting took " + k + "ms");
                }
            } finally {
                int j = localStopWatch.getElapsedTime();
                if (j > 200L) {
                    Log.w("AudioRouter", "awaitRouting took " + j + "ms");
                }
            }
            return true;
        } finally {
            int i = localStopWatch.getElapsedTime();
            if (i > 200L) {
                Log.w("AudioRouter", "awaitRouting took " + i + "ms");
            }
        }
    }

    public void cancelPendingAwaitRouting() {
        synchronized (this.mLock) {
            if (this.mAwaitState == 10) {
                this.mAwaitState = 11;
                this.mLock.notify();
            }
            return;
        }
    }

    public int getInputDeviceToLog() {
        synchronized (this.mLock) {
            if (isWiredHeadsetConnected()) {
                return 2;
            }
        }
        return 1;
    }

    public int getOutputStream() {
        synchronized (this.mLock) {
            return 1;
        }
    }

    public void onStartListening(boolean requestAudioFocus) {
        synchronized (mLock) {
            if ((requestAudioFocus) && (!isRouteActive())) {
                maybeRequestAudioFocus();
            }
        }
    }

    public void onStartTtsPlayback() {
        synchronized (this.mLock) {
            if (!isRouteActive()) {
                maybeRequestAudioFocus();
            }
            return;
        }
    }

    public void onStopListening(boolean releaseAudioFocus) {
        synchronized (mLock) {
            if ((releaseAudioFocus) && (!isRouteActive())) {
                maybeAbandonAudioFocus();
            }
        }
    }

    public void onStopTtsPlayback() {
        synchronized (this.mLock) {
            if (!isRouteActive()) {
                maybeAbandonAudioFocus();
            }
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioRouterImpl

 * JD-Core Version:    0.7.0.1

 */