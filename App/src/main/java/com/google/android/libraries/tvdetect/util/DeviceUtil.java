package com.google.android.libraries.tvdetect.util;

import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.Device.BuildException;
import com.google.android.libraries.tvdetect.Device.Builder;
import com.google.android.libraries.tvdetect.DeviceModel;
import com.google.android.libraries.tvdetect.ProductInfo;
import com.google.android.libraries.tvdetect.ProductInfoService;
import com.google.android.libraries.tvdetect.ProductInfoServiceException;
import com.google.android.libraries.tvdetect.ProductType;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class DeviceUtil
{
  public static final long MAX_KNOWN_PRODUCT_INFO_AGE_MILLIS = TimeUnit.DAYS.toMillis(7L);
  public static final long MAX_UNKNOWN_PRODUCT_INFO_AGE_MILLIS = TimeUnit.DAYS.toMillis(2L);
  public static final long MAX_VALID_PRODUCT_INFO_AGE_MILLIS = TimeUnit.DAYS.toMillis(10L);
  
  public static boolean deviceExistsAtDeviceDescriptionUrl(HttpFetcher paramHttpFetcher, Device paramDevice)
  {
    try
    {
      byte[] arrayOfByte = paramHttpFetcher.fetchUrl(paramDevice.deviceDescriptionUrl);
      if ((arrayOfByte != null) && (arrayOfByte.length != 0))
      {
        String str = new String(arrayOfByte, "UTF-8");
        if (paramDevice.deviceModel == null) {
          return false;
        }
        if (str.contains("urn:schemas-upnp-org")) {
          if ((paramDevice.deviceModel.manufacturer != null) && (paramDevice.deviceModel.modelName != null))
          {
            if ((str.contains(paramDevice.deviceModel.manufacturer)) && (str.contains(paramDevice.deviceModel.modelName))) {
              return true;
            }
          }
          else
          {
            if (paramDevice.deviceModel.manufacturer != null) {
              return str.contains(paramDevice.deviceModel.manufacturer);
            }
            if (paramDevice.deviceModel.modelName != null)
            {
              boolean bool = str.contains(paramDevice.deviceModel.modelName);
              return bool;
            }
          }
        }
      }
    }
    catch (HttpFetcherException localHttpFetcherException)
    {
      return false;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return false;
  }
  
  public static Device fetchDeviceDetailsFromDeviceDescriptionUrl(HttpFetcher paramHttpFetcher, String paramString1, String paramString2, String paramString3)
  {
    try
    {
      byte[] arrayOfByte = paramHttpFetcher.fetchUrl(paramString1);
      return XmlUtil.parseDeviceDetails(arrayOfByte, paramString1, paramString3, paramString2);
    }
    catch (HttpFetcherException localHttpFetcherException) {}
    return null;
  }
  
  public static boolean mustFetchProductInfoForDevice(Device paramDevice, Clock paramClock, boolean paramBoolean)
  {
    if ((paramBoolean) || (paramDevice.productInfo == null) || (!paramDevice.productInfo.isComplete())) {}
    for (;;)
    {
      return true;
      if (paramDevice.productInfoUpdateTimeMillis != 0L)
      {
        if (paramDevice.productInfo.type == ProductType.UNKNOWN) {}
        for (long l = MAX_UNKNOWN_PRODUCT_INFO_AGE_MILLIS; paramClock.getCurrentTimeMillis() - paramDevice.productInfoUpdateTimeMillis < l; l = MAX_KNOWN_PRODUCT_INFO_AGE_MILLIS) {
          return false;
        }
      }
    }
  }
  
  public static Device populateProductInfo(Device paramDevice, ProductInfoService paramProductInfoService, Clock paramClock)
    throws Device.BuildException
  {
    if (paramDevice.deviceModel == null) {
      throw new Device.BuildException("Missing deviceModel");
    }
    try
    {
      ProductInfo localProductInfo = paramProductInfoService.getProductInfo(paramDevice.deviceModel);
      Device localDevice = Device.newBuilder(paramDevice).setProductInfo(localProductInfo, paramClock.getCurrentTimeMillis()).build();
      return localDevice;
    }
    catch (ProductInfoServiceException localProductInfoServiceException)
    {
      throw new Device.BuildException("Could not get device information", localProductInfoServiceException);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.DeviceUtil
 * JD-Core Version:    0.7.0.1
 */