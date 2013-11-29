package com.google.android.libraries.tvdetect;

public abstract interface ProductInfoService
{
  public abstract ProductInfo getProductInfo(DeviceModel paramDeviceModel)
    throws ProductInfoServiceException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.ProductInfoService
 * JD-Core Version:    0.7.0.1
 */