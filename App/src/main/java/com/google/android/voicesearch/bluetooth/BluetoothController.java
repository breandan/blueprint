package com.google.android.voicesearch.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.shared.util.StopWatch;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BluetoothController
  extends BluetoothShim.BroadcastReceiver
  implements BluetoothShim.BluetoothProfile.ServiceListener
{
  private final AudioManager mAudioManager;
  private BluetoothShim.BluetoothAdapter mBluetoothAdapter;
  @Nullable
  private BluetoothShim.BluetoothDevice mBluetoothDevice;
  private BluetoothShim.BluetoothHeadset mBluetoothHeadset;
  private boolean mBroadcastReceiverRegistered = false;
  private final Context mContext;
  private final DeviceCapabilityManager mDeviceCapabilityManager;
  @Nonnull
  private int mDeviceState = 0;
  private final Executor mExecutor;
  private final ExtraPreconditions.ThreadCheck mExecutorThreadCheck;
  private final List<BluetoothListener> mListeners = Lists.newArrayListWithExpectedSize(2);
  private final Object mLock = new Object();
  @Nonnull
  private int mScoState = 10;
  
  public BluetoothController(DeviceCapabilityManager paramDeviceCapabilityManager, AudioManager paramAudioManager, Context paramContext, Executor paramExecutor, ExtraPreconditions.ThreadCheck paramThreadCheck)
  {
    this.mDeviceCapabilityManager = paramDeviceCapabilityManager;
    this.mAudioManager = paramAudioManager;
    this.mContext = paramContext;
    this.mExecutor = paramExecutor;
    this.mExecutorThreadCheck = paramThreadCheck;
  }
  
  public static final String enumIntToString(int paramInt)
  {
    if (paramInt == 0) {
      return "DEVICE_STATE_UNKNOWN";
    }
    if (paramInt == 1) {
      return "DEVICE_STATE_CONNECTED";
    }
    if (paramInt == 2) {
      return "DEVICE_STATE_NONE";
    }
    if (paramInt == 10) {
      return "SCO_STATE_DISCONNECTED";
    }
    if (paramInt == 11) {
      return "SCO_STATE_CONNECTING";
    }
    if (paramInt == 12) {
      return "SCO_STATE_CONNECTED";
    }
    return "[Illegal value]";
  }
  
  static String[] getDeviceDetailsToLog(BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    if (paramBluetoothDevice == null) {
      return null;
    }
    String[] arrayOfString = new String[2];
    arrayOfString[0] = paramBluetoothDevice.getName();
    arrayOfString[1] = paramBluetoothDevice.getAddress();
    return arrayOfString;
  }
  
  private void initializeLocked()
  {
    ExtraPreconditions.checkHoldsLock(this.mLock);
    this.mBluetoothAdapter = getBluetoothAdapter();
    if ((this.mBluetoothAdapter == null) || (!this.mAudioManager.isBluetoothScoAvailableOffCall()))
    {
      Log.i("BluetoothController", "BT not available: no off call adapter");
      this.mDeviceState = 2;
    }
    int i;
    do
    {
      return;
      if (!this.mBluetoothAdapter.getProfileProxy(this.mContext, (BluetoothShim.BluetoothProfile.ServiceListener)ThreadChanger.createNonBlockingThreadChangeProxy(this.mExecutor, BluetoothShim.BluetoothProfile.ServiceListener.class, this), 1))
      {
        Log.i("BluetoothController", "BT not available: no headset profile");
        this.mDeviceState = 2;
        return;
      }
      if (!this.mBluetoothAdapter.isEnabled())
      {
        this.mDeviceState = 2;
        return;
      }
      i = this.mBluetoothAdapter.getProfileConnectionState(1);
    } while ((i != 3) && (i != 0));
    this.mDeviceState = 2;
  }
  
  private void maybeRegisterBroadcastReceiver()
  {
    if (!this.mBroadcastReceiverRegistered)
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
      localIntentFilter.addAction("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
      this.mContext.registerReceiver(this, localIntentFilter);
      this.mBroadcastReceiverRegistered = true;
    }
  }
  
  private void maybeUnregisterBroadcastReceiver()
  {
    if (this.mBroadcastReceiverRegistered)
    {
      this.mContext.unregisterReceiver(this);
      this.mBroadcastReceiverRegistered = false;
    }
  }
  
  private void onConnectionStateChange(int paramInt1, int paramInt2, BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    if (paramInt1 == 2)
    {
      setDevice(paramBluetoothDevice);
      return;
    }
    setDevice(null);
  }
  
  private void onScoStateChange(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 12) {
      if (this.mScoState != 11) {
        Log.d("BluetoothController", "onScoStateChange: Not expecting a transition to STATE_AUDIO_CONNECTED when mScoState == " + enumIntToString(this.mScoState) + ". Caused by another app, probably the dialer.");
      }
    }
    do
    {
      return;
      EventLogger.recordClientEvent(106, getDeviceDetailsToLog(this.mBluetoothDevice));
      setScoState(12);
      return;
      if (paramInt1 == 10)
      {
        if (this.mScoState == 11) {
          EventLogger.recordClientEvent(107, getDeviceDetailsToLog(this.mBluetoothDevice));
        }
        setScoState(10);
        return;
      }
    } while (this.mScoState == 11);
    Log.w("BluetoothController", "Not expecting STATE_AUDIO_CONNECTING");
  }
  
  private void setDevice(@Nullable BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    String str;
    if (paramBluetoothDevice == null)
    {
      str = "No BT device";
      Log.i("BluetoothController", str);
      this.mExecutorThreadCheck.check();
    }
    for (;;)
    {
      synchronized (this.mLock)
      {
        int i = this.mScoState;
        int j = this.mDeviceState;
        if (paramBluetoothDevice != null) {
          break label164;
        }
        this.mScoState = 10;
        this.mDeviceState = 2;
        this.mBluetoothDevice = null;
        Iterator localIterator = this.mListeners.iterator();
        if (!localIterator.hasNext()) {
          break label177;
        }
        BluetoothListener localBluetoothListener = (BluetoothListener)localIterator.next();
        if (this.mScoState != i) {
          localBluetoothListener.onScoStateChanged(i, this.mScoState);
        }
        if (this.mDeviceState == j) {
          continue;
        }
        localBluetoothListener.onDeviceStateChanged(j, this.mDeviceState, paramBluetoothDevice);
      }
      str = "BT device connected";
      break;
      label164:
      this.mBluetoothDevice = paramBluetoothDevice;
      this.mDeviceState = 1;
    }
    label177:
  }
  
  private void setScoState(int paramInt)
  {
    this.mExecutorThreadCheck.check();
    synchronized (this.mLock)
    {
      int i = this.mScoState;
      this.mScoState = paramInt;
      Iterator localIterator = this.mListeners.iterator();
      while (localIterator.hasNext())
      {
        BluetoothListener localBluetoothListener = (BluetoothListener)localIterator.next();
        if (this.mScoState != i) {
          localBluetoothListener.onScoStateChanged(i, this.mScoState);
        }
      }
    }
  }
  
  private boolean startVoiceRecognition()
  {
    Log.i("BluetoothController", "Starting VR");
    EventLogger.recordLatencyStart(9);
    if (this.mDeviceCapabilityManager.isTelephoneCapable()) {
      return this.mBluetoothHeadset.startVoiceRecognition(this.mBluetoothDevice);
    }
    return this.mBluetoothHeadset.startScoUsingVirtualVoiceCall(this.mBluetoothDevice);
  }
  
  private boolean stopVoiceRecognition()
  {
    Log.i("BluetoothController", "Stopping VR");
    if (this.mDeviceCapabilityManager.isTelephoneCapable()) {
      return this.mBluetoothHeadset.stopVoiceRecognition(this.mBluetoothDevice);
    }
    return this.mBluetoothHeadset.stopScoUsingVirtualVoiceCall(this.mBluetoothDevice);
  }
  
  public void addListener(BluetoothListener paramBluetoothListener, Executor paramExecutor)
  {
    synchronized (this.mLock)
    {
      this.mListeners.add(ThreadChanger.createNonBlockingThreadChangeProxy(paramExecutor, BluetoothListener.class, paramBluetoothListener));
      return;
    }
  }
  
  public void ensureInitialized()
  {
    this.mExecutorThreadCheck.check();
    synchronized (this.mLock)
    {
      if ((this.mBluetoothAdapter != null) || (this.mDeviceState != 0)) {
        return;
      }
      new StopWatch().start();
      initializeLocked();
      return;
    }
  }
  
  protected BluetoothShim.BluetoothAdapter getBluetoothAdapter()
  {
    return BluetoothShim.getDefaultAdapter();
  }
  
  @Nullable
  public BluetoothShim.BluetoothDevice getDevice()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mDeviceState == 1)
        {
          localBluetoothDevice = this.mBluetoothDevice;
          return localBluetoothDevice;
        }
      }
      BluetoothShim.BluetoothDevice localBluetoothDevice = null;
    }
  }
  
  public int getDeviceState()
  {
    synchronized (this.mLock)
    {
      int i = this.mDeviceState;
      return i;
    }
  }
  
  public int getScoState()
  {
    synchronized (this.mLock)
    {
      int i = this.mScoState;
      return i;
    }
  }
  
  protected void onReceive(Context paramContext, final Intent paramIntent, final BluetoothShim.BluetoothDevice paramBluetoothDevice)
  {
    this.mExecutor.execute(new Runnable()
    {
      public void run()
      {
        String str = paramIntent.getAction();
        int i = paramIntent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
        int j = paramIntent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", -1);
        if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(str)) {
          BluetoothController.this.onConnectionStateChange(i, j, paramBluetoothDevice);
        }
        while (!"android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED".equals(str)) {
          return;
        }
        BluetoothController.this.onScoStateChange(i, j);
      }
    });
  }
  
  public void onServiceConnected(int paramInt, BluetoothShim.BluetoothProfile paramBluetoothProfile)
  {
    this.mExecutorThreadCheck.check();
    maybeRegisterBroadcastReceiver();
    this.mBluetoothHeadset = ((BluetoothShim.BluetoothHeadset)paramBluetoothProfile);
    List localList = this.mBluetoothHeadset.getConnectedDevices();
    if (localList.isEmpty())
    {
      setDevice(null);
      return;
    }
    BluetoothShim.BluetoothDevice localBluetoothDevice = (BluetoothShim.BluetoothDevice)localList.get(0);
    if (this.mBluetoothHeadset.getConnectionState(localBluetoothDevice) == 2)
    {
      setDevice(localBluetoothDevice);
      return;
    }
    setDevice(null);
  }
  
  public void onServiceDisconnected(int paramInt)
  {
    this.mExecutorThreadCheck.check();
    maybeUnregisterBroadcastReceiver();
    this.mBluetoothHeadset = null;
    setDevice(null);
  }
  
  public void startSco()
  {
    this.mExecutorThreadCheck.check();
    ensureInitialized();
    new StopWatch().start();
    if (this.mScoState != 10) {}
    do
    {
      return;
      setScoState(11);
    } while (startVoiceRecognition());
    Log.i("BluetoothController", "startSco: startVoiceRecognition failed");
    setScoState(10);
  }
  
  public void stopSco()
  {
    this.mExecutorThreadCheck.check();
    ensureInitialized();
    new StopWatch().start();
    if (this.mScoState == 10) {}
    do
    {
      return;
      setScoState(10);
    } while (stopVoiceRecognition());
    Log.i("BluetoothController", "stopSco: stopVoiceRecognition failed");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.bluetooth.BluetoothController
 * JD-Core Version:    0.7.0.1
 */