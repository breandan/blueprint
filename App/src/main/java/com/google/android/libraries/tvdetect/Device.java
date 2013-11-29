package com.google.android.libraries.tvdetect;

import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.libraries.tvdetect.util.StringUtil;
import com.google.android.libraries.tvdetect.util.UriUtil;

public class Device
{
  public final String deviceDescriptionUrl;
  public final DeviceModel deviceModel;
  public final String friendlyName;
  public final String networkBssid;
  public final ProductInfo productInfo;
  public final long productInfoUpdateTimeMillis;
  public final String uuid;
  
  private Device(String paramString1, String paramString2, String paramString3, String paramString4, DeviceModel paramDeviceModel, ProductInfo paramProductInfo, long paramLong)
  {
    this.uuid = paramString1;
    this.networkBssid = paramString2;
    this.deviceDescriptionUrl = paramString3;
    this.friendlyName = paramString4;
    this.deviceModel = paramDeviceModel;
    this.productInfo = paramProductInfo;
    this.productInfoUpdateTimeMillis = paramLong;
  }
  
  public static Device deserializeFromString(String paramString)
    throws Device.BuildException
  {
    Uri localUri = Uri.parse(paramString);
    String str1 = localUri.getQueryParameter("productInfoUpdateTimeMillis");
    try
    {
      long l = Long.parseLong(str1);
      Builder localBuilder = newBuilder();
      localBuilder.setUuid(localUri.getQueryParameter("uuid"));
      localBuilder.setNetworkBssid(localUri.getQueryParameter("networkBssid"));
      localBuilder.setDeviceDescriptionUrl(localUri.getQueryParameter("deviceDescriptionUrl"));
      localBuilder.setFriendlyName(localUri.getQueryParameter("friendlyName"));
      String str2 = localUri.getQueryParameter("deviceModel");
      if (str2 != null) {
        localBuilder.setDeviceModel(DeviceModel.deserializeFromString(str2));
      }
      String str3 = localUri.getQueryParameter("productInfo");
      if (str3 != null) {
        localBuilder.setProductInfo(ProductInfo.deserializeFromString(str3), l);
      }
      return localBuilder.build();
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new BuildException("Could not parse the product info update time", localNumberFormatException);
    }
  }
  
  public static Builder newBuilder()
  {
    return new Builder();
  }
  
  public static Builder newBuilder(Device paramDevice)
  {
    return new Builder(paramDevice);
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Device)) {
      return false;
    }
    return serializeToString().equals(((Device)paramObject).serializeToString());
  }
  
  public int hashCode()
  {
    return serializeToString().hashCode();
  }
  
  public String serializeToString()
  {
    Uri.Builder localBuilder = UriUtil.newUriBuilder(Device.class);
    UriUtil.appendKeyValue(localBuilder, "uuid", this.uuid);
    UriUtil.appendKeyValue(localBuilder, "networkBssid", this.networkBssid);
    UriUtil.appendKeyValue(localBuilder, "deviceDescriptionUrl", this.deviceDescriptionUrl);
    UriUtil.appendKeyValue(localBuilder, "friendlyName", this.friendlyName);
    if (this.deviceModel != null) {
      UriUtil.appendKeyValue(localBuilder, "deviceModel", this.deviceModel.serializeToString());
    }
    if (this.productInfo != null) {
      UriUtil.appendKeyValue(localBuilder, "productInfo", this.productInfo.serializeToString());
    }
    UriUtil.appendKeyValue(localBuilder, "productInfoUpdateTimeMillis", Long.toString(this.productInfoUpdateTimeMillis));
    return localBuilder.build().toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("{");
    if (this.deviceModel != null) {
      StringUtil.appendKeyValue(localStringBuilder, "deviceModel", this.deviceModel.toString());
    }
    if (this.productInfo != null) {
      StringUtil.appendKeyValue(localStringBuilder, "productInfo", this.productInfo.toString());
    }
    StringUtil.appendKeyValue(localStringBuilder, "productInfoUpdateTimeMillis", Long.toString(this.productInfoUpdateTimeMillis));
    return "}";
  }
  
  public static final class BuildException
    extends Exception
  {
    public BuildException(String paramString)
    {
      super();
    }
    
    public BuildException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
  
  public static final class Builder
  {
    private String deviceDescriptionUrl;
    private DeviceModel deviceModel;
    private String friendlyName;
    private String networkBssid;
    private ProductInfo productInfo;
    private long productInfoUpdateTimeMillis = 0L;
    private String uuid;
    
    public Builder() {}
    
    public Builder(Device paramDevice)
    {
      this.uuid = paramDevice.uuid;
      this.networkBssid = paramDevice.networkBssid;
      this.deviceDescriptionUrl = paramDevice.deviceDescriptionUrl;
      this.friendlyName = paramDevice.friendlyName;
      this.deviceModel = paramDevice.deviceModel;
      this.productInfo = paramDevice.productInfo;
      this.productInfoUpdateTimeMillis = paramDevice.productInfoUpdateTimeMillis;
    }
    
    public Device build()
      throws Device.BuildException
    {
      if (this.uuid == null) {
        throw new Device.BuildException("uuid must not be null");
      }
      if (this.networkBssid == null) {
        throw new Device.BuildException("networkBssid must not be null");
      }
      if (this.deviceDescriptionUrl == null) {
        throw new Device.BuildException("deviceDescriptionUrl must not be null");
      }
      return new Device(this.uuid, this.networkBssid, this.deviceDescriptionUrl, this.friendlyName, this.deviceModel, this.productInfo, this.productInfoUpdateTimeMillis, null);
    }
    
    public Builder setDeviceDescriptionUrl(String paramString)
    {
      this.deviceDescriptionUrl = paramString;
      return this;
    }
    
    public Builder setDeviceModel(DeviceModel paramDeviceModel)
    {
      this.deviceModel = paramDeviceModel;
      return this;
    }
    
    public Builder setFriendlyName(String paramString)
    {
      this.friendlyName = paramString;
      return this;
    }
    
    public Builder setNetworkBssid(String paramString)
    {
      this.networkBssid = paramString;
      return this;
    }
    
    public Builder setProductInfo(ProductInfo paramProductInfo, long paramLong)
    {
      this.productInfo = paramProductInfo;
      if (paramProductInfo == null)
      {
        this.productInfoUpdateTimeMillis = 0L;
        return this;
      }
      this.productInfoUpdateTimeMillis = paramLong;
      return this;
    }
    
    public Builder setUuid(String paramString)
    {
      this.uuid = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.Device
 * JD-Core Version:    0.7.0.1
 */