package com.google.android.search.core.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.bluetooth.BluetoothCarClassifier;
import com.google.android.voicesearch.bluetooth.BluetoothShim.BluetoothDevice;
import com.google.android.voicesearch.bluetooth.BluetoothShim.BroadcastReceiver;

public class BluetoothConnectionReceiver
  extends BluetoothShim.BroadcastReceiver
{
  private BluetoothCarClassifier mClassifier = VelvetServices.get().getVoiceSearchServices().getBluetoothCarClassifier();
  
  protected void onReceive(Context paramContext, Intent paramIntent, BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    Log.i("BluetoothConnectionReceiver", "onReceive(context, " + paramIntent + ", " + paramBluetoothDevice + ")");
    if (!this.mClassifier.isDeviceACar(paramBluetoothDevice)) {
      return;
    }
    if ("android.bluetooth.device.action.ACL_CONNECTED".equals(paramIntent.getAction()))
    {
      Intent localIntent = new Intent("com.google.android.search.core.BTSTARTUP", null, paramContext, SearchServiceImpl.class);
      localIntent.putExtras(paramIntent);
      paramContext.startService(localIntent);
      return;
    }
    if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(paramIntent.getAction()))
    {
      paramContext.startService(new Intent("com.google.android.search.core.BTSTOP", null, paramContext, SearchServiceImpl.class));
      return;
    }
    Log.e("BluetoothConnectionReceiver", "Unexpected intent received by BluetoothConnectionReceiver: " + paramIntent);
  }
  
  public void setBluetoothCarClassifier(BluetoothCarClassifier paramBluetoothCarClassifier)
  {
    this.mClassifier = paramBluetoothCarClassifier;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.BluetoothConnectionReceiver
 * JD-Core Version:    0.7.0.1
 */