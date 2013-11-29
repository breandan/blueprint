package com.google.android.voicesearch.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BluetoothShim
{
  public static BluetoothAdapter getDefaultAdapter()
  {
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (localBluetoothAdapter == null) {
      return null;
    }
    return new BluetoothAdapter(localBluetoothAdapter, null);
  }
  
  private static Object reflect(Method paramMethod, Object paramObject, Object... paramVarArgs)
  {
    if (paramMethod == null)
    {
      Log.e("VS.BluetoothShim", "Unable to invoke method");
      return null;
    }
    try
    {
      Object localObject = paramMethod.invoke(paramObject, paramVarArgs);
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Log.e("VS.BluetoothShim", "Error invoking " + paramMethod.getName(), localIllegalArgumentException);
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Log.e("VS.BluetoothShim", "Error invoking " + paramMethod.getName(), localIllegalAccessException);
      return null;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      Log.e("VS.BluetoothShim", "Error invoking " + paramMethod.getName(), localInvocationTargetException);
    }
    return null;
  }
  
  public static class BluetoothAdapter
  {
    private final BluetoothAdapter mBluetoothAdapter;
    
    private BluetoothAdapter(BluetoothAdapter paramBluetoothAdapter)
    {
      this.mBluetoothAdapter = paramBluetoothAdapter;
    }
    
    public int getProfileConnectionState(int paramInt)
    {
      return this.mBluetoothAdapter.getProfileConnectionState(paramInt);
    }
    
    public boolean getProfileProxy(Context paramContext, final BluetoothShim.BluetoothProfile.ServiceListener paramServiceListener, int paramInt)
    {
      this.mBluetoothAdapter.getProfileProxy(paramContext, new BluetoothProfile.ServiceListener()
      {
        public void onServiceConnected(int paramAnonymousInt, BluetoothProfile paramAnonymousBluetoothProfile)
        {
          paramServiceListener.onServiceConnected(paramAnonymousInt, new BluetoothShim.BluetoothHeadset((BluetoothHeadset)paramAnonymousBluetoothProfile));
        }
        
        public void onServiceDisconnected(int paramAnonymousInt)
        {
          paramServiceListener.onServiceDisconnected(paramAnonymousInt);
        }
      }, paramInt);
    }
    
    public boolean isEnabled()
    {
      return this.mBluetoothAdapter.isEnabled();
    }
  }
  
  public static class BluetoothDevice
  {
    private static Method sGetAliasMethod = null;
    private static Method sGetAliasNameMethod = null;
    private final BluetoothDevice mBluetoothDevice;
    
    static
    {
      try
      {
        sGetAliasMethod = BluetoothDevice.class.getMethod("getAlias", new Class[0]);
        sGetAliasNameMethod = BluetoothDevice.class.getMethod("getAliasName", new Class[0]);
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        Log.e("VS.BluetoothShim", "Error locating alias methods", localNoSuchMethodException);
      }
    }
    
    BluetoothDevice(BluetoothDevice paramBluetoothDevice)
    {
      this.mBluetoothDevice = paramBluetoothDevice;
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof BluetoothDevice)) {
        return this.mBluetoothDevice.equals(((BluetoothDevice)paramObject).mBluetoothDevice);
      }
      return false;
    }
    
    public String getAddress()
    {
      return this.mBluetoothDevice.getAddress();
    }
    
    public String getAliasName()
    {
      return (String)BluetoothShim.reflect(sGetAliasNameMethod, this.mBluetoothDevice, new Object[0]);
    }
    
    public String getName()
    {
      return this.mBluetoothDevice.getName();
    }
    
    public int hashCode()
    {
      return this.mBluetoothDevice.hashCode();
    }
    
    public String toString()
    {
      return this.mBluetoothDevice.toString();
    }
  }
  
  public static class BluetoothHeadset
    extends BluetoothShim.BluetoothProfile
  {
    private static Method sStartScoUsingVirtualVoiceCallMethod;
    private static Method sStopScoUsingVirtualVoiceCallMethod;
    private final BluetoothHeadset mBluetoothHeadset;
    
    static
    {
      try
      {
        sStartScoUsingVirtualVoiceCallMethod = BluetoothHeadset.class.getMethod("startScoUsingVirtualVoiceCall", new Class[] { BluetoothDevice.class });
        sStopScoUsingVirtualVoiceCallMethod = BluetoothHeadset.class.getMethod("stopScoUsingVirtualVoiceCall", new Class[] { BluetoothDevice.class });
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        sStartScoUsingVirtualVoiceCallMethod = null;
        sStopScoUsingVirtualVoiceCallMethod = null;
        Log.e("VS.BluetoothShim", "Error locating SCO method", localNoSuchMethodException);
      }
    }
    
    BluetoothHeadset(BluetoothHeadset paramBluetoothHeadset)
    {
      this.mBluetoothHeadset = paramBluetoothHeadset;
    }
    
    public List<BluetoothShim.BluetoothDevice> getConnectedDevices()
    {
      List localList = this.mBluetoothHeadset.getConnectedDevices();
      ArrayList localArrayList = new ArrayList(localList.size());
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        BluetoothDevice localBluetoothDevice = (BluetoothDevice)localIterator.next();
        if (localBluetoothDevice != null) {
          localArrayList.add(new BluetoothShim.BluetoothDevice(localBluetoothDevice));
        }
      }
      return localArrayList;
    }
    
    public int getConnectionState(BluetoothShim.BluetoothDevice paramBluetoothDevice)
    {
      return this.mBluetoothHeadset.getConnectionState(paramBluetoothDevice.mBluetoothDevice);
    }
    
    public boolean startScoUsingVirtualVoiceCall(BluetoothShim.BluetoothDevice paramBluetoothDevice)
    {
      Method localMethod = sStartScoUsingVirtualVoiceCallMethod;
      BluetoothHeadset localBluetoothHeadset = this.mBluetoothHeadset;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramBluetoothDevice.mBluetoothDevice;
      return ((Boolean)BluetoothShim.reflect(localMethod, localBluetoothHeadset, arrayOfObject)).booleanValue();
    }
    
    public boolean startVoiceRecognition(BluetoothShim.BluetoothDevice paramBluetoothDevice)
    {
      return this.mBluetoothHeadset.startVoiceRecognition(paramBluetoothDevice.mBluetoothDevice);
    }
    
    public boolean stopScoUsingVirtualVoiceCall(BluetoothShim.BluetoothDevice paramBluetoothDevice)
    {
      Method localMethod = sStopScoUsingVirtualVoiceCallMethod;
      BluetoothHeadset localBluetoothHeadset = this.mBluetoothHeadset;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramBluetoothDevice.mBluetoothDevice;
      return ((Boolean)BluetoothShim.reflect(localMethod, localBluetoothHeadset, arrayOfObject)).booleanValue();
    }
    
    public boolean stopVoiceRecognition(BluetoothShim.BluetoothDevice paramBluetoothDevice)
    {
      return this.mBluetoothHeadset.stopVoiceRecognition(paramBluetoothDevice.mBluetoothDevice);
    }
  }
  
  public static class BluetoothProfile
  {
    public static abstract interface ServiceListener
    {
      public abstract void onServiceConnected(int paramInt, BluetoothShim.BluetoothProfile paramBluetoothProfile);
      
      public abstract void onServiceDisconnected(int paramInt);
    }
  }
  
  public static abstract class BroadcastReceiver
    extends BroadcastReceiver
  {
    public final void onReceive(Context paramContext, Intent paramIntent)
    {
      boolean bool = paramIntent.hasExtra("android.bluetooth.device.extra.DEVICE");
      BluetoothShim.BluetoothDevice localBluetoothDevice = null;
      if (bool) {
        localBluetoothDevice = new BluetoothShim.BluetoothDevice((BluetoothDevice)paramIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
      }
      onReceive(paramContext, paramIntent, localBluetoothDevice);
    }
    
    protected abstract void onReceive(Context paramContext, Intent paramIntent, BluetoothShim.BluetoothDevice paramBluetoothDevice);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.bluetooth.BluetoothShim
 * JD-Core Version:    0.7.0.1
 */