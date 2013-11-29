package com.google.android.sidekick.main.tv;

import com.google.android.libraries.tvdetect.Device;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public abstract interface TvDetector
{
  public abstract Collection<Device> getDetectedDevices(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean);
  
  public abstract void startDetection(@Nullable Observer paramObserver);
  
  public abstract void stopDetection();
  
  public static abstract interface Factory
  {
    public abstract TvDetector newTvDetector();
  }
  
  public static abstract interface Observer
  {
    public abstract void onTvDetectionFinished();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.tv.TvDetector
 * JD-Core Version:    0.7.0.1
 */