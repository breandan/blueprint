package com.google.android.libraries.tvdetect;

public abstract interface DeviceFinder
{
  public abstract boolean search(Callback paramCallback, DeviceFinderOptions paramDeviceFinderOptions);
  
  public abstract void stopSearch();
  
  public static abstract interface Callback
  {
    public abstract void onDeviceFound(Device paramDevice);
    
    public abstract void onProgressChanged(int paramInt1, int paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.DeviceFinder
 * JD-Core Version:    0.7.0.1
 */