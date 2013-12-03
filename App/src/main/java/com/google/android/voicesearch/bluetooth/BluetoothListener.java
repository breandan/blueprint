package com.google.android.voicesearch.bluetooth;

import javax.annotation.Nullable;

public abstract interface BluetoothListener {
    public abstract void onDeviceStateChanged(int paramInt1, int paramInt2, @Nullable BluetoothShim.BluetoothDevice paramBluetoothDevice);

    public abstract void onScoStateChanged(int paramInt1, int paramInt2);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.bluetooth.BluetoothListener

 * JD-Core Version:    0.7.0.1

 */