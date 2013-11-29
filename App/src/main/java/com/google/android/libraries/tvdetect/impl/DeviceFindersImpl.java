package com.google.android.libraries.tvdetect.impl;

import android.content.Context;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.ProductInfoService;
import com.google.android.libraries.tvdetect.net.AndroidNetworkAccessor;
import com.google.android.libraries.tvdetect.util.DefaultHttpFetcher;
import com.google.android.libraries.tvdetect.util.SystemClock;

public class DeviceFindersImpl
{
  public static DeviceFinder newDeviceFinder(Context paramContext, ProductInfoService paramProductInfoService)
  {
    return new DualDeviceFinder(AndroidNetworkAccessor.create(paramContext), SharedPrefDeviceCache.create(paramContext), paramProductInfoService, DefaultHttpFetcher.create(), SystemClock.INSTANCE);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.DeviceFindersImpl
 * JD-Core Version:    0.7.0.1
 */