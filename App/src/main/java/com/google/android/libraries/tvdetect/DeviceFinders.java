package com.google.android.libraries.tvdetect;

import android.content.Context;
import com.google.android.libraries.tvdetect.impl.DeviceFindersImpl;

public class DeviceFinders
{
  public static DeviceFinder newDeviceFinder(Context paramContext, ProductInfoService paramProductInfoService)
  {
    return DeviceFindersImpl.newDeviceFinder(paramContext, paramProductInfoService);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.DeviceFinders
 * JD-Core Version:    0.7.0.1
 */