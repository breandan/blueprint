package com.google.android.libraries.tvdetect;

import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.libraries.tvdetect.util.StringUtil;
import com.google.android.libraries.tvdetect.util.UriUtil;

public class DeviceModel
{
  public final String category;
  public final String manufacturer;
  public final String manufacturerUrl;
  public final String modelDescription;
  public final String modelId;
  public final String modelName;
  public final String modelNumber;
  public final String modelUrl;
  
  private DeviceModel(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
  {
    this.manufacturer = paramString1;
    this.manufacturerUrl = paramString2;
    this.modelName = paramString3;
    this.modelId = paramString4;
    this.modelUrl = paramString5;
    this.modelDescription = paramString6;
    this.modelNumber = paramString7;
    this.category = paramString8;
  }
  
  public static DeviceModel deserializeFromString(String paramString)
  {
    Uri localUri = Uri.parse(paramString);
    Builder localBuilder = newBuilder();
    localBuilder.setManufacturer(localUri.getQueryParameter("manufacturer"));
    localBuilder.setManufacturerUrl(localUri.getQueryParameter("manufacturerUrl"));
    localBuilder.setModelName(localUri.getQueryParameter("modelName"));
    localBuilder.setModelId(localUri.getQueryParameter("modelId"));
    localBuilder.setModelUrl(localUri.getQueryParameter("modelUrl"));
    localBuilder.setModelDescription(localUri.getQueryParameter("modelDescription"));
    localBuilder.setModelNumber(localUri.getQueryParameter("modelNumber"));
    localBuilder.setCategory(localUri.getQueryParameter("category"));
    return localBuilder.build();
  }
  
  public static Builder newBuilder()
  {
    return new Builder();
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DeviceModel)) {
      return false;
    }
    return serializeToString().equals(((DeviceModel)paramObject).serializeToString());
  }
  
  public int hashCode()
  {
    return serializeToString().hashCode();
  }
  
  public String serializeToString()
  {
    Uri.Builder localBuilder = UriUtil.newUriBuilder(DeviceModel.class);
    UriUtil.appendKeyValue(localBuilder, "manufacturer", this.manufacturer);
    UriUtil.appendKeyValue(localBuilder, "manufacturerUrl", this.manufacturerUrl);
    UriUtil.appendKeyValue(localBuilder, "modelName", this.modelName);
    UriUtil.appendKeyValue(localBuilder, "modelId", this.modelId);
    UriUtil.appendKeyValue(localBuilder, "modelUrl", this.modelUrl);
    UriUtil.appendKeyValue(localBuilder, "modelDescription", this.modelDescription);
    UriUtil.appendKeyValue(localBuilder, "modelNumber", this.modelNumber);
    UriUtil.appendKeyValue(localBuilder, "category", this.category);
    return localBuilder.build().toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("{");
    StringUtil.appendKeyValue(localStringBuilder, "manufacturer", this.manufacturer);
    StringUtil.appendKeyValue(localStringBuilder, "manufacturerUrl", this.manufacturerUrl);
    StringUtil.appendKeyValue(localStringBuilder, "modelName", this.modelName);
    StringUtil.appendKeyValue(localStringBuilder, "modelId", this.modelId);
    StringUtil.appendKeyValue(localStringBuilder, "modelUrl", this.modelUrl);
    StringUtil.appendKeyValue(localStringBuilder, "modelDescription", this.modelDescription);
    StringUtil.appendKeyValue(localStringBuilder, "modelNumber", this.modelNumber);
    StringUtil.appendKeyValue(localStringBuilder, "category", this.category);
    return "}";
  }
  
  public static final class Builder
  {
    private String category;
    private String manufacturer;
    private String manufacturerUrl;
    private String modelDescription;
    private String modelId;
    private String modelName;
    private String modelNumber;
    private String modelUrl;
    
    public DeviceModel build()
    {
      return new DeviceModel(this.manufacturer, this.manufacturerUrl, this.modelName, this.modelId, this.modelUrl, this.modelDescription, this.modelNumber, this.category, null);
    }
    
    public Builder setCategory(String paramString)
    {
      this.category = paramString;
      return this;
    }
    
    public Builder setManufacturer(String paramString)
    {
      this.manufacturer = paramString;
      return this;
    }
    
    public Builder setManufacturerUrl(String paramString)
    {
      this.manufacturerUrl = paramString;
      return this;
    }
    
    public Builder setModelDescription(String paramString)
    {
      this.modelDescription = paramString;
      return this;
    }
    
    public Builder setModelId(String paramString)
    {
      this.modelId = paramString;
      return this;
    }
    
    public Builder setModelName(String paramString)
    {
      this.modelName = paramString;
      return this;
    }
    
    public Builder setModelNumber(String paramString)
    {
      this.modelNumber = paramString;
      return this;
    }
    
    public Builder setModelUrl(String paramString)
    {
      this.modelUrl = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.DeviceModel
 * JD-Core Version:    0.7.0.1
 */