package com.google.android.voicesearch.handsfree;

import android.util.Log;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.voicesearch.audio.AudioRouter;
import com.google.android.voicesearch.bluetooth.BluetoothController;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public class AudioRouterHandsfree {
    private final AudioRouter mAudioRouter;
    private final BluetoothController mBluetoothController;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final Executor mUiExecutor;
    private final boolean mWiredHeadsetTriggered;

    public AudioRouterHandsfree(Executor paramExecutor, ScheduledExecutorService paramScheduledExecutorService, AudioRouter paramAudioRouter, BluetoothController paramBluetoothController, boolean paramBoolean) {
        this.mAudioRouter = paramAudioRouter;
        this.mBluetoothController = paramBluetoothController;
        this.mScheduledExecutorService = paramScheduledExecutorService;
        this.mUiExecutor = paramExecutor;
        this.mWiredHeadsetTriggered = paramBoolean;
    }

    public void establishRoute(Listener paramListener) {
        ExtraPreconditions.checkMainThread();
        final Listener localListener = (Listener) ThreadChanger.createNonBlockingThreadChangeProxy(this.mUiExecutor, paramListener);
        final AudioRouter.AudioRouteListener local1 = new AudioRouter.AudioRouteListener() {
            public void onRouteLost() {
                Log.w("VS.AudioRouterHandsfree", "Audio route lost");
                localListener.onAudioRouteFailed();
            }
        };
        this.mScheduledExecutorService.execute(new Runnable() {
            public void run() {
                int i;
                AudioRouter localAudioRouter;
                int j;
                if ((!AudioRouterHandsfree.this.mWiredHeadsetTriggered) && (AudioRouterHandsfree.this.mBluetoothController.getDeviceState() != 2)) {
                    i = 1;
                    localAudioRouter = AudioRouterHandsfree.this.mAudioRouter;
                    j = 0;
                    if (i == 0) {
                        break label81;
                    }
                }
                for (; ; ) {
                    localAudioRouter.updateRoute(j, local1);
                    if (!AudioRouterHandsfree.this.mAudioRouter.awaitRouting()) {
                        break label86;
                    }
                    localListener.onAudioRouteEstablished();
                    return;
                    i = 0;
                    break;
                    label81:
                    j = 2;
                }
                label86:
                if (AudioRouterHandsfree.this.mBluetoothController.getDeviceState() == 2) {
                    Log.i("VS.AudioRouterHandsfree", "VOICE_COMMAND intent not fired from a BT device");
                    localListener.onAudioRouteEstablished();
                    return;
                }
                Log.w("VS.AudioRouterHandsfree", "Bluetooth routing failed");
                AudioRouterHandsfree.this.mAudioRouter.updateRoute(3, null);
                localListener.onAudioRouteFailed();
            }
        });
    }

    public void stopRoute() {
        this.mAudioRouter.updateRoute(3, null);
    }

    public static abstract interface Listener {
        public abstract void onAudioRouteEstablished();

        public abstract void onAudioRouteFailed();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.AudioRouterHandsfree

 * JD-Core Version:    0.7.0.1

 */