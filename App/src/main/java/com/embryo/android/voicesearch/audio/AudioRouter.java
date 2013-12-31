package com.embryo.android.voicesearch.audio;

import javax.annotation.Nullable;

public abstract interface AudioRouter {
    public abstract boolean awaitRouting();

    public abstract void cancelPendingAwaitRouting();

    public abstract int getInputDeviceToLog();

    public abstract int getOutputStream();

    public abstract void onStartListening(boolean paramBoolean);

    public abstract void onStartTtsPlayback();

    public abstract void onStopListening(boolean paramBoolean);

    public abstract void onStopTtsPlayback();

    public abstract void updateRoute(int paramInt, @Nullable AudioRouteListener paramAudioRouteListener);

    public static abstract interface AudioRouteListener {
        public abstract void onRouteLost();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     AudioRouter

 * JD-Core Version:    0.7.0.1

 */