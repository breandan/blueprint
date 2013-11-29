package com.google.android.libraries.tvdetect;

import java.util.Set;

public class DeviceFinderOptions
{
  public final boolean forceReloadCachedData;
  public final Set<ProductType> wantedProductTypes;
  
  private DeviceFinderOptions(boolean paramBoolean, Set<ProductType> paramSet)
  {
    this.forceReloadCachedData = paramBoolean;
    this.wantedProductTypes = paramSet;
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
    return toString().equals(paramObject.toString());
  }
  
  public static final class Builder
  {
    private boolean forceReloadCachedData = false;
    private Set<ProductType> wantedProductTypes = ProductTypes.setOfTvOnly();
    
    public DeviceFinderOptions build()
    {
      return new DeviceFinderOptions(this.forceReloadCachedData, this.wantedProductTypes, null);
    }
    
    public Builder setWantedProductTypes(Set<ProductType> paramSet)
    {
      this.wantedProductTypes = paramSet;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.DeviceFinderOptions
 * JD-Core Version:    0.7.0.1
 */