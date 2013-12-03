package com.google.android.voicesearch.bluetooth;

import com.google.android.search.core.state.QueryState;

public class BluetoothCarListener
        implements BluetoothListener {
    private boolean mCarConnected;
    private final BluetoothCarClassifier mClassifier;
    private final QueryState mQueryState;

    public BluetoothCarListener(QueryState paramQueryState, BluetoothCarClassifier paramBluetoothCarClassifier) {
        this.mQueryState = paramQueryState;
        this.mClassifier = paramBluetoothCarClassifier;
    }

    /* Error */
    public void onDeviceStateChanged(int paramInt1, int paramInt2, @javax.annotation.Nullable BluetoothShim.BluetoothDevice paramBluetoothDevice) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 26	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mCarConnected	Z
        //   6: istore 5
        //   8: aload_3
        //   9: ifnonnull +35 -> 44
        //   12: iconst_0
        //   13: istore 7
        //   15: aload_0
        //   16: iload 7
        //   18: putfield 26	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mCarConnected	Z
        //   21: aload_0
        //   22: getfield 26	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mCarConnected	Z
        //   25: iload 5
        //   27: if_icmpeq +14 -> 41
        //   30: aload_0
        //   31: getfield 19	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mQueryState	Lcom/google/android/search/core/state/QueryState;
        //   34: aload_0
        //   35: getfield 26	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mCarConnected	Z
        //   38: invokevirtual 32	com/google/android/search/core/state/QueryState:onCarConnectedChanged	(Z)V
        //   41: aload_0
        //   42: monitorexit
        //   43: return
        //   44: aload_0
        //   45: getfield 21	com/google/android/voicesearch/bluetooth/BluetoothCarListener:mClassifier	Lcom/google/android/voicesearch/bluetooth/BluetoothCarClassifier;
        //   48: aload_3
        //   49: invokevirtual 38	com/google/android/voicesearch/bluetooth/BluetoothCarClassifier:isDeviceACar	(Lcom/google/android/voicesearch/bluetooth/BluetoothShim$BluetoothDevice;)Z
        //   52: istore 6
        //   54: iload 6
        //   56: istore 7
        //   58: goto -43 -> 15
        //   61: astore 4
        //   63: aload_0
        //   64: monitorexit
        //   65: aload 4
        //   67: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	68	0	this	BluetoothCarListener
        //   0	68	1	paramInt1	int
        //   0	68	2	paramInt2	int
        //   0	68	3	paramBluetoothDevice	BluetoothShim.BluetoothDevice
        //   61	5	4	localObject	Object
        //   6	22	5	bool1	boolean
        //   52	3	6	bool2	boolean
        //   13	44	7	bool3	boolean
        // Exception table:
        //   from	to	target	type
        //   2	8	61	finally
        //   15	41	61	finally
        //   44	54	61	finally
    }

    public void onScoStateChanged(int paramInt1, int paramInt2) {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.bluetooth.BluetoothCarListener

 * JD-Core Version:    0.7.0.1

 */