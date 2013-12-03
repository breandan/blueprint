package com.google.android.voicesearch.audio;

import android.media.AudioManager;
import android.util.Log;

import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.StopWatch;
import com.google.android.shared.util.Util;
import com.google.android.voicesearch.bluetooth.BluetoothController;
import com.google.android.voicesearch.bluetooth.BluetoothListener;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class AudioRouterImpl
        implements AudioRouter, BluetoothListener {
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int paramAnonymousInt) {
            Log.i("AudioRouter", "Audio focus change " + paramAnonymousInt);
        }
    };
    private boolean mAudioFocusObtained = false;
    private final AudioManager mAudioManager;
    private int mAwaitState = 12;
    private final BluetoothController mBluetoothController;
    private final Clock mClock;
    private final Executor mExecutor;
    private final ExtraPreconditions.ThreadCheck mExecutorThreadCheck;
    private final Object mLock = new Object();
    private final ExtraPreconditions.ThreadCheck mNotExecutorThreadCheck;
    private int mRoute = 3;
    private AudioRouter.AudioRouteListener mRouteListener = null;
    private boolean mScoFailed = false;
    private final Settings mSettings;
    private boolean mSynchronizePending = false;

    public AudioRouterImpl(Clock paramClock, Settings paramSettings, AudioManager paramAudioManager, Executor paramExecutor, ExtraPreconditions.ThreadCheck paramThreadCheck1, ExtraPreconditions.ThreadCheck paramThreadCheck2, BluetoothController paramBluetoothController) {
        this.mClock = paramClock;
        this.mSettings = paramSettings;
        this.mAudioManager = paramAudioManager;
        this.mBluetoothController = paramBluetoothController;
        this.mExecutor = paramExecutor;
        this.mExecutorThreadCheck = paramThreadCheck1;
        this.mNotExecutorThreadCheck = paramThreadCheck2;
    }

    private boolean awaitBluetoothDeviceLocked() {
        ExtraPreconditions.checkHoldsLock(this.mLock);
        if (this.mRoute == 0) {
        }
        for (long l1 = 1000L; ; l1 = 200L) {
            long l2 = l1 + this.mClock.uptimeMillis();
            for (; ; ) {
                if ((this.mBluetoothController.getDeviceState() != 0) || (l1 <= 0L) || (this.mAwaitState == 11)) {
                    break label99;
                }
                try {
                    this.mLock.wait(l1);
                    l1 = l2 - this.mClock.uptimeMillis();
                } catch (InterruptedException localInterruptedException) {
                    Log.w("AudioRouter", "Thread was interrupted, aborting await", localInterruptedException);
                }
            }
        }
        label99:
        int i;
        BluetoothShim.BluetoothDevice localBluetoothDevice;
        do {
            do {
                return false;
            } while (this.mAwaitState == 11);
            i = this.mBluetoothController.getDeviceState();
            localBluetoothDevice = this.mBluetoothController.getDevice();
            if (i == 0) {
                Log.w("AudioRouter", "Timed out waiting for BT device state");
                this.mScoFailed = true;
                return false;
            }
        }
        while ((i == 2) || (localBluetoothDevice == null) || (!shouldUseBluetoothDevice(this.mBluetoothController.getDevice())));
        return true;
    }

    private boolean awaitBluetoothRoutingLocked() {
        boolean bool1 = true;
        ExtraPreconditions.checkHoldsLock(this.mLock);
        try {
            if (this.mAwaitState == 12) {
            }
            for (boolean bool2 = bool1; ; bool2 = false) {
                Preconditions.checkState(bool2, "awaitBluetoothRouting can only be run by one thread concurrently");
                this.mAwaitState = 10;
                int i = this.mBluetoothController.getScoState();
                if (i != 12) {
                    break;
                }
                return bool1;
            }
            if (this.mScoFailed) {
                Log.w("AudioRouter", "SCO connection has failed");
                int m = this.mRoute;
                if (m != 0) {
                }
                for (; ; ) {
                    return bool1;
                    bool1 = false;
                }
            }
            if (!awaitBluetoothDeviceLocked()) {
                int k = this.mRoute;
                if (k != 0) {
                }
                for (; ; ) {
                    return bool1;
                    bool1 = false;
                }
            }
            if (!awaitBluetoothScoConnectionLocked()) {
                int j = this.mRoute;
                if (j != 0) {
                }
                for (; ; ) {
                    return bool1;
                    bool1 = false;
                }
            }
            return bool1;
        } finally {
            this.mAwaitState = 12;
        }
    }

    private boolean awaitBluetoothScoConnectionLocked() {
        ExtraPreconditions.checkHoldsLock(this.mLock);
        long l1 = this.mSettings.getConfiguration().getBluetooth().getScoConnectionTimeoutMs();
        long l2 = l1 + this.mClock.uptimeMillis();
        for (; ; ) {
            if (((this.mBluetoothController.getScoState() == 11) || (this.mSynchronizePending)) && (l1 > 0L) && (this.mAwaitState != 11)) {
                try {
                    this.mLock.wait(l1);
                    l1 = l2 - this.mClock.uptimeMillis();
                } catch (InterruptedException localInterruptedException) {
                    Log.w("AudioRouter", "Thread was interrupted, aborting await", localInterruptedException);
                }
            }
        }
        while (this.mAwaitState == 11) {
            return false;
        }
        int i = this.mBluetoothController.getScoState();
        if (i == 11) {
            Log.w("AudioRouter", "SCO connection timed out");
            this.mScoFailed = true;
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    AudioRouterImpl.this.mBluetoothController.stopSco();
                }
            });
            return false;
        }
        if (i == 10) {
            Log.w("AudioRouter", "SCO connection attempt failed");
            return false;
        }
        return true;
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

    private boolean isBluetoothRoute() {
        ExtraPreconditions.checkHoldsLock(this.mLock);
        return isBluetoothRoute(this.mRoute);
    }

    private static boolean isBluetoothRoute(int paramInt) {
        return (paramInt == 0) || (paramInt == 1);
    }

    private boolean isRouteActive() {
        ExtraPreconditions.checkHoldsLock(this.mLock);
        return this.mRoute != 3;
    }

    private boolean isWiredHeadsetConnected() {
        return this.mAudioManager.isWiredHeadsetOn();
    }

    private void maybeAbandonAudioFocus() {
        this.mExecutor.execute(new Runnable() {
            public void run() {
                if (AudioRouterImpl.this.mAudioFocusObtained) {
                    if (AudioRouterImpl.this.mAudioManager.abandonAudioFocus(AudioRouterImpl.this.mAudioFocusChangeListener) == 1) {
                        AudioRouterImpl.access$202(AudioRouterImpl.this, false);
                    }
                } else {
                    return;
                }
                Log.w("AudioRouter", "Unable to release STREAM_SYSTEM audio focus");
            }
        });
    }

    private void maybeRequestAudioFocus() {
        this.mExecutor.execute(new Runnable() {
            public void run() {
                if (!AudioRouterImpl.this.mAudioFocusObtained) {
                    if (Util.SDK_INT < 19) {
                        break label53;
                    }
                }
                label53:
                for (int i = 4; AudioRouterImpl.this.mAudioManager.requestAudioFocus(AudioRouterImpl.this.mAudioFocusChangeListener, 1, i) == 1; i = 2) {
                    AudioRouterImpl.access$202(AudioRouterImpl.this, true);
                    return;
                }
                Log.w("AudioRouter", "Unable to obtain STREAM_SYSTEM audio focus");
            }
        });
    }

    private boolean shouldUseBluetoothDevice(BluetoothShim.BluetoothDevice paramBluetoothDevice) {
        return true;
    }

    private void synchronizeBluetoothState() {
        this.mExecutorThreadCheck.check();
        for (; ; ) {
            synchronized (this.mLock) {
                if (this.mSynchronizePending) {
                    this.mSynchronizePending = false;
                    this.mLock.notify();
                }
                if (isBluetoothRoute()) {
                    this.mBluetoothController.ensureInitialized();
                    if (this.mScoFailed) {
                        return;
                    }
                    if ((this.mBluetoothController.getScoState() == 10) && (this.mBluetoothController.getDeviceState() == 1) && (shouldUseBluetoothDevice(this.mBluetoothController.getDevice()))) {
                        Log.i("AudioRouter", "BT required, starting SCO");
                        this.mBluetoothController.startSco();
                    }
                    return;
                }
            }
            if (this.mBluetoothController.getScoState() != 10) {
                Log.i("AudioRouter", "BT not required, stopping SCO");
                this.mBluetoothController.stopSco();
            }
        }
    }

    public boolean awaitRouting() {
        this.mNotExecutorThreadCheck.check();
        ExtraPreconditions.checkNotMainThread();
        StopWatch localStopWatch = new StopWatch().start();
        Object localObject1 = this.mLock;
        try {
            boolean bool;
            if (isBluetoothRoute()) {
                bool = awaitBluetoothRoutingLocked();
            }
            try {
                int k = localStopWatch.getElapsedTime();
                if (k > 200L) {
                    Log.w("AudioRouter", "awaitRouting took " + k + "ms");
                }
                return bool;
            } finally {
            }
            int j = localStopWatch.getElapsedTime();
            if (j > 200L) {
                Log.w("AudioRouter", "awaitRouting took " + j + "ms");
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

    public void onDeviceStateChanged(int paramInt1, int paramInt2, @Nullable BluetoothShim.BluetoothDevice paramBluetoothDevice) {
        this.mExecutorThreadCheck.check();
        synchronized (this.mLock) {
            synchronizeBluetoothState();
            this.mLock.notify();
            return;
        }
    }

    public void onScoStateChanged(int paramInt1, int paramInt2) {
        this.mExecutorThreadCheck.check();
        synchronized (this.mLock) {
            if ((isBluetoothRoute()) && (paramInt2 == 10) && (!this.mScoFailed)) {
                this.mScoFailed = true;
                if (paramInt1 != 12) {
                    break label104;
                }
                Log.i("AudioRouter", "BT route lost");
                if (this.mRouteListener != null) {
                    localAudioRouteListener = this.mRouteListener;
                    this.mExecutor.execute(new Runnable() {
                        public void run() {
                            localAudioRouteListener.onRouteLost();
                        }
                    });
                }
            }
            label104:
            while (paramInt1 != 11) {
                final AudioRouter.AudioRouteListener localAudioRouteListener;
                synchronizeBluetoothState();
                this.mLock.notify();
                return;
            }
            Log.i("AudioRouter", "BT connection failed");
        }
    }

    public void onStartListening(boolean paramBoolean) {
        Object localObject1 = this.mLock;
        if (paramBoolean) {
        }
        try {
            if (!isRouteActive()) {
                maybeRequestAudioFocus();
            }
            return;
        } finally {
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

    public void onStopListening(boolean paramBoolean) {
        Object localObject1 = this.mLock;
        if (paramBoolean) {
        }
        try {
            if (!isRouteActive()) {
                maybeAbandonAudioFocus();
            }
            return;
        } finally {
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

    public void updateRoute(int paramInt, @Nullable AudioRouter.AudioRouteListener paramAudioRouteListener) {
        if ((paramInt == 1) && (!this.mSettings.isBluetoothHeadsetEnabled())) {
            paramInt = 2;
        }
        Object localObject1 = this.mLock;
        if (paramInt != 3) {
        }
        for (; ; ) {
            try {
                maybeRequestAudioFocus();
                this.mRouteListener = paramAudioRouteListener;
                if (paramInt != this.mRoute) {
                    break label63;
                }
                return;
            } finally {
            }
            maybeAbandonAudioFocus();
            continue;
            label63:
            Log.i("AudioRouter", enumIntToString(this.mRoute) + "->" + enumIntToString(paramInt));
            int i = this.mRoute;
            this.mRoute = paramInt;
            if ((!isBluetoothRoute(i)) && (isBluetoothRoute(paramInt))) {
                this.mScoFailed = false;
            }
            this.mSynchronizePending = true;
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    AudioRouterImpl.this.synchronizeBluetoothState();
                }
            });
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.audio.AudioRouterImpl

 * JD-Core Version:    0.7.0.1

 */