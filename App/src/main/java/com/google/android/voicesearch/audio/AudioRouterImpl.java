package com.google.android.voicesearch.audio;

import android.media.AudioManager;
import android.util.Log;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.StopWatch;
import com.google.android.shared.util.Util;
import com.google.android.voicesearch.bluetooth.BluetoothController;
import com.google.android.voicesearch.bluetooth.BluetoothListener;
import com.google.android.voicesearch.bluetooth.BluetoothShim;
import com.google.android.voicesearch.settings.Settings;

import java.util.concurrent.Executor;

public class AudioRouterImpl
        implements AudioRouter, BluetoothListener {
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int paramAnonymousInt) {
            Log.i("AudioRouter", "Audio focus change " + paramAnonymousInt);
        }
    };
    private final AudioManager mAudioManager;
    private final BluetoothController mBluetoothController;
    private final Executor mExecutor;
    private final ExtraPreconditions.ThreadCheck mExecutorThreadCheck;
    private final Object mLock = new Object();
    private final ExtraPreconditions.ThreadCheck mNotExecutorThreadCheck;
    private final Settings mSettings;
    private boolean mAudioFocusObtained = false;
    private int mAwaitState = 12;
    private int mRoute = 3;
    private AudioRouter.AudioRouteListener mRouteListener = null;
    private boolean mScoFailed = false;
    private boolean mSynchronizePending = false;

    public AudioRouterImpl(Settings paramSettings, AudioManager paramAudioManager, Executor paramExecutor, ExtraPreconditions.ThreadCheck paramThreadCheck1, ExtraPreconditions.ThreadCheck paramThreadCheck2, BluetoothController paramBluetoothController) {
        this.mSettings = paramSettings;
        this.mAudioManager = paramAudioManager;
        this.mBluetoothController = paramBluetoothController;
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
        ExtraPreconditions.checkHoldsLock(this.mLock);
        return isBluetoothRoute(this.mRoute);
    }

    private boolean isRouteActive() {
        ExtraPreconditions.checkHoldsLock(this.mLock);
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
                    int focusType = Util.SDK_INT >= 0x13 ? 0x4 : 0x2;
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

    private void synchronizeBluetoothState() {
        mExecutorThreadCheck.check();
        synchronized (mLock) {
            if (mSynchronizePending) {
                mSynchronizePending = false;
                mLock.notify();
            }
            if (isBluetoothRoute()) {
                mBluetoothController.ensureInitialized();
                if (mScoFailed) {
                    return;
                }
                if ((mBluetoothController.getScoState() == 0xa) && (mBluetoothController.getDeviceState() == 0x1) && (shouldUseBluetoothDevice(mBluetoothController.getDevice()))) {
                    Log.i("AudioRouter", "BT required, starting SCO");
                    mBluetoothController.startSco();
                }
            } else if (mBluetoothController.getScoState() != 0xa) {
                Log.i("AudioRouter", "BT not required, stopping SCO");
                mBluetoothController.stopSco();
            }
        }
    }

    public boolean awaitRouting() {
        this.mNotExecutorThreadCheck.check();
        ExtraPreconditions.checkNotMainThread();
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
            if (this.mBluetoothController.getScoState() == 12) {
                return 3;
            }
            if (isWiredHeadsetConnected()) {
                return 2;
            }
        }
        return 1;
    }

    public int getOutputStream() {
        synchronized (this.mLock) {
            if (this.mBluetoothController.getScoState() == 12) {
                return 0;
            }
            return 1;
        }
    }

    public void onDeviceStateChanged(int prevState, int deviceState, BluetoothShim.BluetoothDevice device) {
        mExecutorThreadCheck.check();
        synchronized (mLock) {
            synchronizeBluetoothState();
            mLock.notify();
        }
    }

    public void onScoStateChanged(int prevState, int scoState) {
        mExecutorThreadCheck.check();
        synchronized (mLock) {
            if ((isBluetoothRoute()) && (scoState == 0xa) && (!mScoFailed)) {
                mScoFailed = true;
                if (prevState == 0xc) {
                    Log.i("AudioRouter", "BT route lost");
                    if (mRouteListener != null) {
                        final AudioRouter.AudioRouteListener listener = mRouteListener;
                        mExecutor.execute(new Runnable() {
                            public void run() {
                                listener.onRouteLost();
                            }
                        });
                    }
                } else if (prevState == 0xb) {
                    Log.i("AudioRouter", "BT connection failed");
                }
            }
            synchronizeBluetoothState();
            mLock.notify();
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

    public void updateRoute(int route, AudioRouter.AudioRouteListener routeListener) {
        if ((route == 0x1) && (!mSettings.isBluetoothHeadsetEnabled())) {
            route = 0x2;
        }
        synchronized (mLock) {
            if (route != 0x3) {
                maybeRequestAudioFocus();
            } else {
                maybeAbandonAudioFocus();
            }
            mRouteListener = routeListener;
            if (route == mRoute) {
            } else {
                Log.i("AudioRouter", enumIntToString(mRoute) + "->" + enumIntToString(route));
                int prevRoute = mRoute;
                mRoute = route;
                if ((!isBluetoothRoute(prevRoute)) && (isBluetoothRoute(route))) {
                    mScoFailed = false;
                } else {
                    mSynchronizePending = true;
                    mExecutor.execute(new Runnable() {

                        public void run() {
                            AudioRouterImpl.this.synchronizeBluetoothState();
                        }
                    });
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.audio.AudioRouterImpl

 * JD-Core Version:    0.7.0.1

 */