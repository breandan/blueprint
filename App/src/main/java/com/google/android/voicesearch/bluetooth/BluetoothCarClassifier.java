package com.google.android.voicesearch.bluetooth;

import android.util.Log;
import com.google.android.search.core.Feature;
import java.util.Locale;

public class BluetoothCarClassifier
{
  public boolean isDeviceACar(BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    if (!Feature.EYES_FREE.isEnabled()) {
      return false;
    }
    if (Feature.DETECT_BT_DEVICE_AS_CAR.isEnabled()) {
      return true;
    }
    String str1 = paramBluetoothDevice.getName();
    if (str1 == null)
    {
      Log.w("BluetoothCarClassifier", "No device name available");
      return false;
    }
    String str2 = str1.toLowerCase(Locale.US);
    boolean bool1;
    if ((!str2.contains("car")) && (!str2.contains("bmw")))
    {
      boolean bool2 = str2.contains("audi");
      bool1 = false;
      if (!bool2) {}
    }
    else
    {
      bool1 = true;
    }
    return bool1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.bluetooth.BluetoothCarClassifier
 * JD-Core Version:    0.7.0.1
 */