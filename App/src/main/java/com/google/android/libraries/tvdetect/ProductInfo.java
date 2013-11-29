package com.google.android.libraries.tvdetect;

import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.libraries.tvdetect.util.L;
import com.google.android.libraries.tvdetect.util.StringUtil;
import com.google.android.libraries.tvdetect.util.UriUtil;

public class ProductInfo
{
  public final ProductType type;
  public final String userFriendlyName;
  
  private ProductInfo(ProductType paramProductType, String paramString)
  {
    this.type = paramProductType;
    this.userFriendlyName = paramString;
  }
  
  public static ProductInfo deserializeFromString(String paramString)
  {
    Uri localUri = Uri.parse(paramString);
    localBuilder = newBuilder();
    try
    {
      String str = localUri.getQueryParameter("type");
      if (str != null) {
        localBuilder.setType(ProductType.valueOf(str));
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        L.e("Could not extract product type");
        localBuilder.setType(ProductType.UNKNOWN);
      }
    }
    localBuilder.setUserFriendlyName(localUri.getQueryParameter("userFriendlyName"));
    return localBuilder.build();
  }
  
  public static Builder newBuilder()
  {
    return new Builder();
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ProductInfo)) {
      return false;
    }
    return serializeToString().equals(((ProductInfo)paramObject).serializeToString());
  }
  
  public int hashCode()
  {
    return serializeToString().hashCode();
  }
  
  public boolean isComplete()
  {
    return (this.type != null) && (this.userFriendlyName != null);
  }
  
  public String serializeToString()
  {
    Uri.Builder localBuilder = UriUtil.newUriBuilder(ProductInfo.class);
    UriUtil.appendKeyValue(localBuilder, "type", this.type.name());
    UriUtil.appendKeyValue(localBuilder, "userFriendlyName", this.userFriendlyName);
    return localBuilder.build().toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    StringUtil.appendKeyValue(localStringBuilder, "type", this.type.name());
    StringUtil.appendKeyValue(localStringBuilder, "userFriendlyName", this.userFriendlyName);
    return localStringBuilder.toString();
  }
  
  public static final class Builder
  {
    private ProductType type = ProductType.UNKNOWN;
    private String userFriendlyName;
    
    public ProductInfo build()
    {
      return new ProductInfo(this.type, this.userFriendlyName, null);
    }
    
    public Builder setType(ProductType paramProductType)
    {
      this.type = paramProductType;
      return this;
    }
    
    public Builder setUserFriendlyName(String paramString)
    {
      this.userFriendlyName = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.ProductInfo
 * JD-Core Version:    0.7.0.1
 */