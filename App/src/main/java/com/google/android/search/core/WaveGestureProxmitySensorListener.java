package com.google.android.search.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.google.android.velvet.VelvetStrictMode;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;

public class WaveGestureProxmitySensorListener
  implements SensorEventListener, WaveGestureListener
{
  protected static final int FAR_PROXIMITY = 1;
  protected static final int MAX_WAVE_DURATION_MS = 500;
  protected static final int NEAR_PROXIMITY = 0;
  protected static final long PROXMITY_READING_MAX_AGE_MS = 1500L;
  private long mLastTriggeredTimestamp = -1L;
  private WaveGestureListener.Observer mObserver;
  private final List<ProximityReading> mProximityReadings = Lists.newLinkedList();
  private Sensor mSensor;
  private SensorManager mSensorManager;
  
  protected void clearExpiredReadings(List<ProximityReading> paramList, final long paramLong)
  {
    Iterables.removeIf(paramList, new Predicate()
    {
      public boolean apply(WaveGestureProxmitySensorListener.ProximityReading paramAnonymousProximityReading)
      {
        return paramLong - paramAnonymousProximityReading.timestamp > 1500L;
      }
    });
  }
  
  protected int findNumWaveGestures(List<ProximityReading> paramList)
  {
    int i;
    if (paramList.isEmpty()) {
      i = 0;
    }
    for (;;)
    {
      return i;
      i = 0;
      Object localObject = (ProximityReading)paramList.get(-1 + paramList.size());
      for (int j = -2 + paramList.size(); j >= 0; j--)
      {
        ProximityReading localProximityReading = (ProximityReading)paramList.get(j);
        if (localProximityReading.proximity != ((ProximityReading)localObject).proximity)
        {
          if (((ProximityReading)localObject).timestamp - localProximityReading.timestamp < 500L) {
            i++;
          }
          localObject = localProximityReading;
        }
      }
    }
  }
  
  protected void handleSensorChanged(long paramLong, int paramInt)
  {
    if ((this.mLastTriggeredTimestamp != -1L) && (paramLong - this.mLastTriggeredTimestamp < 500L)) {}
    do
    {
      do
      {
        return;
        this.mProximityReadings.add(new ProximityReading(paramLong, paramInt));
        clearExpiredReadings(this.mProximityReadings, paramLong);
      } while (findNumWaveGestures(this.mProximityReadings) < 2);
      this.mProximityReadings.clear();
    } while (this.mObserver == null);
    this.mLastTriggeredTimestamp = paramLong;
    this.mObserver.onWaveGesture();
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent)
  {
    if (this.mObserver == null)
    {
      VelvetStrictMode.logW("WaveGestureProxmitySensorListener", "Getting sensor events, but has no observer", new Throwable());
      return;
    }
    boolean bool = paramSensorEvent.values[0] < 0.0F;
    int i = 0;
    if (!bool) {}
    for (;;)
    {
      handleSensorChanged(System.currentTimeMillis(), i);
      return;
      i = 1;
    }
  }
  
  public void startListening(Context paramContext, WaveGestureListener.Observer paramObserver)
  {
    if (this.mObserver != null) {
      stopListening(paramContext, this.mObserver);
    }
    this.mObserver = paramObserver;
    this.mSensorManager = ((SensorManager)paramContext.getSystemService("sensor"));
    this.mSensor = this.mSensorManager.getDefaultSensor(8);
    this.mSensorManager.registerListener(this, this.mSensor, 3);
  }
  
  public void stopListening(Context paramContext, WaveGestureListener.Observer paramObserver)
  {
    if (this.mObserver == paramObserver)
    {
      this.mSensorManager.unregisterListener(this);
      this.mProximityReadings.clear();
      this.mObserver = null;
      return;
    }
    Log.w("WaveGestureProxmitySensorListener", "Stop listening for an observer that's not registered");
  }
  
  protected static class ProximityReading
  {
    public int proximity;
    public long timestamp;
    
    public ProximityReading(long paramLong, int paramInt)
    {
      this.timestamp = paramLong;
      this.proximity = paramInt;
    }
    
    public String toString()
    {
      return "Proximity: " + this.proximity + ", Time: " + this.timestamp % 10000L;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.WaveGestureProxmitySensorListener
 * JD-Core Version:    0.7.0.1
 */