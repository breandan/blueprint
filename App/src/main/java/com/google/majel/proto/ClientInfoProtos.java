package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class ClientInfoProtos
{
  public static final class BrowserParams
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String googleDomain_ = "";
    private boolean hasGoogleDomain;
    private boolean hasHeightPixels;
    private boolean hasSearchLanguage;
    private boolean hasUseMetricUnits;
    private boolean hasUsePreciseGeolocation;
    private boolean hasUserAgent;
    private boolean hasWidthPixels;
    private int heightPixels_ = 0;
    private String searchLanguage_ = "";
    private boolean useMetricUnits_ = false;
    private boolean usePreciseGeolocation_ = true;
    private String userAgent_ = "";
    private int widthPixels_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getGoogleDomain()
    {
      return this.googleDomain_;
    }
    
    public int getHeightPixels()
    {
      return this.heightPixels_;
    }
    
    public String getSearchLanguage()
    {
      return this.searchLanguage_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasWidthPixels();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getWidthPixels());
      }
      if (hasHeightPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getHeightPixels());
      }
      if (hasUserAgent()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getUserAgent());
      }
      if (hasGoogleDomain()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getGoogleDomain());
      }
      if (hasSearchLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getSearchLanguage());
      }
      if (hasUseMetricUnits()) {
        i += CodedOutputStreamMicro.computeBoolSize(6, getUseMetricUnits());
      }
      if (hasUsePreciseGeolocation()) {
        i += CodedOutputStreamMicro.computeBoolSize(7, getUsePreciseGeolocation());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean getUseMetricUnits()
    {
      return this.useMetricUnits_;
    }
    
    public boolean getUsePreciseGeolocation()
    {
      return this.usePreciseGeolocation_;
    }
    
    public String getUserAgent()
    {
      return this.userAgent_;
    }
    
    public int getWidthPixels()
    {
      return this.widthPixels_;
    }
    
    public boolean hasGoogleDomain()
    {
      return this.hasGoogleDomain;
    }
    
    public boolean hasHeightPixels()
    {
      return this.hasHeightPixels;
    }
    
    public boolean hasSearchLanguage()
    {
      return this.hasSearchLanguage;
    }
    
    public boolean hasUseMetricUnits()
    {
      return this.hasUseMetricUnits;
    }
    
    public boolean hasUsePreciseGeolocation()
    {
      return this.hasUsePreciseGeolocation;
    }
    
    public boolean hasUserAgent()
    {
      return this.hasUserAgent;
    }
    
    public boolean hasWidthPixels()
    {
      return this.hasWidthPixels;
    }
    
    public BrowserParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setWidthPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setHeightPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          setUserAgent(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setGoogleDomain(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setSearchLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 48: 
          setUseMetricUnits(paramCodedInputStreamMicro.readBool());
          break;
        }
        setUsePreciseGeolocation(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public BrowserParams setGoogleDomain(String paramString)
    {
      this.hasGoogleDomain = true;
      this.googleDomain_ = paramString;
      return this;
    }
    
    public BrowserParams setHeightPixels(int paramInt)
    {
      this.hasHeightPixels = true;
      this.heightPixels_ = paramInt;
      return this;
    }
    
    public BrowserParams setSearchLanguage(String paramString)
    {
      this.hasSearchLanguage = true;
      this.searchLanguage_ = paramString;
      return this;
    }
    
    public BrowserParams setUseMetricUnits(boolean paramBoolean)
    {
      this.hasUseMetricUnits = true;
      this.useMetricUnits_ = paramBoolean;
      return this;
    }
    
    public BrowserParams setUsePreciseGeolocation(boolean paramBoolean)
    {
      this.hasUsePreciseGeolocation = true;
      this.usePreciseGeolocation_ = paramBoolean;
      return this;
    }
    
    public BrowserParams setUserAgent(String paramString)
    {
      this.hasUserAgent = true;
      this.userAgent_ = paramString;
      return this;
    }
    
    public BrowserParams setWidthPixels(int paramInt)
    {
      this.hasWidthPixels = true;
      this.widthPixels_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(1, getWidthPixels());
      }
      if (hasHeightPixels()) {
        paramCodedOutputStreamMicro.writeInt32(2, getHeightPixels());
      }
      if (hasUserAgent()) {
        paramCodedOutputStreamMicro.writeString(3, getUserAgent());
      }
      if (hasGoogleDomain()) {
        paramCodedOutputStreamMicro.writeString(4, getGoogleDomain());
      }
      if (hasSearchLanguage()) {
        paramCodedOutputStreamMicro.writeString(5, getSearchLanguage());
      }
      if (hasUseMetricUnits()) {
        paramCodedOutputStreamMicro.writeBool(6, getUseMetricUnits());
      }
      if (hasUsePreciseGeolocation()) {
        paramCodedOutputStreamMicro.writeBool(7, getUsePreciseGeolocation());
      }
    }
  }
  
  public static final class PreviewParams
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasMapHeightPixels;
    private boolean hasMapWidthPixels;
    private boolean hasUrlHeightPixels;
    private boolean hasUrlPreviewType;
    private boolean hasUrlWidthPixels;
    private int mapHeightPixels_ = 320;
    private int mapWidthPixels_ = 640;
    private int urlHeightPixels_ = 400;
    private int urlPreviewType_ = 0;
    private int urlWidthPixels_ = 400;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getMapHeightPixels()
    {
      return this.mapHeightPixels_;
    }
    
    public int getMapWidthPixels()
    {
      return this.mapWidthPixels_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUrlWidthPixels();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getUrlWidthPixels());
      }
      if (hasUrlHeightPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getUrlHeightPixels());
      }
      if (hasUrlPreviewType()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getUrlPreviewType());
      }
      if (hasMapWidthPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getMapWidthPixels());
      }
      if (hasMapHeightPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getMapHeightPixels());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getUrlHeightPixels()
    {
      return this.urlHeightPixels_;
    }
    
    public int getUrlPreviewType()
    {
      return this.urlPreviewType_;
    }
    
    public int getUrlWidthPixels()
    {
      return this.urlWidthPixels_;
    }
    
    public boolean hasMapHeightPixels()
    {
      return this.hasMapHeightPixels;
    }
    
    public boolean hasMapWidthPixels()
    {
      return this.hasMapWidthPixels;
    }
    
    public boolean hasUrlHeightPixels()
    {
      return this.hasUrlHeightPixels;
    }
    
    public boolean hasUrlPreviewType()
    {
      return this.hasUrlPreviewType;
    }
    
    public boolean hasUrlWidthPixels()
    {
      return this.hasUrlWidthPixels;
    }
    
    public PreviewParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setUrlWidthPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setUrlHeightPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setUrlPreviewType(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setMapWidthPixels(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setMapHeightPixels(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public PreviewParams setMapHeightPixels(int paramInt)
    {
      this.hasMapHeightPixels = true;
      this.mapHeightPixels_ = paramInt;
      return this;
    }
    
    public PreviewParams setMapWidthPixels(int paramInt)
    {
      this.hasMapWidthPixels = true;
      this.mapWidthPixels_ = paramInt;
      return this;
    }
    
    public PreviewParams setUrlHeightPixels(int paramInt)
    {
      this.hasUrlHeightPixels = true;
      this.urlHeightPixels_ = paramInt;
      return this;
    }
    
    public PreviewParams setUrlPreviewType(int paramInt)
    {
      this.hasUrlPreviewType = true;
      this.urlPreviewType_ = paramInt;
      return this;
    }
    
    public PreviewParams setUrlWidthPixels(int paramInt)
    {
      this.hasUrlWidthPixels = true;
      this.urlWidthPixels_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUrlWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(1, getUrlWidthPixels());
      }
      if (hasUrlHeightPixels()) {
        paramCodedOutputStreamMicro.writeInt32(2, getUrlHeightPixels());
      }
      if (hasUrlPreviewType()) {
        paramCodedOutputStreamMicro.writeInt32(3, getUrlPreviewType());
      }
      if (hasMapWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(4, getMapWidthPixels());
      }
      if (hasMapHeightPixels()) {
        paramCodedOutputStreamMicro.writeInt32(5, getMapHeightPixels());
      }
    }
  }
  
  public static final class ScreenParams
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int densityDpi_ = 0;
    private int dpiBucket_ = 0;
    private boolean hasDensityDpi;
    private boolean hasDpiBucket;
    private boolean hasHeightPixels;
    private boolean hasWidthPixels;
    private int heightPixels_ = 0;
    private int widthPixels_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDensityDpi()
    {
      return this.densityDpi_;
    }
    
    public int getDpiBucket()
    {
      return this.dpiBucket_;
    }
    
    public int getHeightPixels()
    {
      return this.heightPixels_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasWidthPixels();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getWidthPixels());
      }
      if (hasHeightPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getHeightPixels());
      }
      if (hasDensityDpi()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getDensityDpi());
      }
      if (hasDpiBucket()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getDpiBucket());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getWidthPixels()
    {
      return this.widthPixels_;
    }
    
    public boolean hasDensityDpi()
    {
      return this.hasDensityDpi;
    }
    
    public boolean hasDpiBucket()
    {
      return this.hasDpiBucket;
    }
    
    public boolean hasHeightPixels()
    {
      return this.hasHeightPixels;
    }
    
    public boolean hasWidthPixels()
    {
      return this.hasWidthPixels;
    }
    
    public ScreenParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setWidthPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setHeightPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setDensityDpi(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setDpiBucket(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public ScreenParams setDensityDpi(int paramInt)
    {
      this.hasDensityDpi = true;
      this.densityDpi_ = paramInt;
      return this;
    }
    
    public ScreenParams setDpiBucket(int paramInt)
    {
      this.hasDpiBucket = true;
      this.dpiBucket_ = paramInt;
      return this;
    }
    
    public ScreenParams setHeightPixels(int paramInt)
    {
      this.hasHeightPixels = true;
      this.heightPixels_ = paramInt;
      return this;
    }
    
    public ScreenParams setWidthPixels(int paramInt)
    {
      this.hasWidthPixels = true;
      this.widthPixels_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(1, getWidthPixels());
      }
      if (hasHeightPixels()) {
        paramCodedOutputStreamMicro.writeInt32(2, getHeightPixels());
      }
      if (hasDensityDpi()) {
        paramCodedOutputStreamMicro.writeInt32(3, getDensityDpi());
      }
      if (hasDpiBucket()) {
        paramCodedOutputStreamMicro.writeInt32(4, getDpiBucket());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ClientInfoProtos
 * JD-Core Version:    0.7.0.1
 */