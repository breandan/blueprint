package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class AgendaProtos
{
  public static final class AgendaItemLocation
    extends MessageMicro
  {
    private String address_ = "";
    private int cachedSize = -1;
    private boolean hasAddress;
    private boolean hasLat;
    private boolean hasLng;
    private boolean hasName;
    private boolean hasStaticMapUrl;
    private double lat_ = 0.0D;
    private double lng_ = 0.0D;
    private String name_ = "";
    private String staticMapUrl_ = "";
    
    public String getAddress()
    {
      return this.address_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public double getLat()
    {
      return this.lat_;
    }
    
    public double getLng()
    {
      return this.lng_;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLat();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeDoubleSize(1, getLat());
      }
      if (hasLng()) {
        i += CodedOutputStreamMicro.computeDoubleSize(2, getLng());
      }
      if (hasName()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getName());
      }
      if (hasAddress()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getAddress());
      }
      if (hasStaticMapUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getStaticMapUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getStaticMapUrl()
    {
      return this.staticMapUrl_;
    }
    
    public boolean hasAddress()
    {
      return this.hasAddress;
    }
    
    public boolean hasLat()
    {
      return this.hasLat;
    }
    
    public boolean hasLng()
    {
      return this.hasLng;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasStaticMapUrl()
    {
      return this.hasStaticMapUrl;
    }
    
    public AgendaItemLocation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 9: 
          setLat(paramCodedInputStreamMicro.readDouble());
          break;
        case 17: 
          setLng(paramCodedInputStreamMicro.readDouble());
          break;
        case 26: 
          setName(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setAddress(paramCodedInputStreamMicro.readString());
          break;
        }
        setStaticMapUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public AgendaItemLocation setAddress(String paramString)
    {
      this.hasAddress = true;
      this.address_ = paramString;
      return this;
    }
    
    public AgendaItemLocation setLat(double paramDouble)
    {
      this.hasLat = true;
      this.lat_ = paramDouble;
      return this;
    }
    
    public AgendaItemLocation setLng(double paramDouble)
    {
      this.hasLng = true;
      this.lng_ = paramDouble;
      return this;
    }
    
    public AgendaItemLocation setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public AgendaItemLocation setStaticMapUrl(String paramString)
    {
      this.hasStaticMapUrl = true;
      this.staticMapUrl_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLat()) {
        paramCodedOutputStreamMicro.writeDouble(1, getLat());
      }
      if (hasLng()) {
        paramCodedOutputStreamMicro.writeDouble(2, getLng());
      }
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(3, getName());
      }
      if (hasAddress()) {
        paramCodedOutputStreamMicro.writeString(4, getAddress());
      }
      if (hasStaticMapUrl()) {
        paramCodedOutputStreamMicro.writeString(5, getStaticMapUrl());
      }
    }
  }
  
  public static final class GmailReference
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String emailUrl_ = "";
    private boolean hasEmailUrl;
    private boolean hasSenderDisplayName;
    private boolean hasSenderEmailAddress;
    private String senderDisplayName_ = "";
    private String senderEmailAddress_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getEmailUrl()
    {
      return this.emailUrl_;
    }
    
    public String getSenderDisplayName()
    {
      return this.senderDisplayName_;
    }
    
    public String getSenderEmailAddress()
    {
      return this.senderEmailAddress_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSenderEmailAddress();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSenderEmailAddress());
      }
      if (hasEmailUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getEmailUrl());
      }
      if (hasSenderDisplayName()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getSenderDisplayName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasEmailUrl()
    {
      return this.hasEmailUrl;
    }
    
    public boolean hasSenderDisplayName()
    {
      return this.hasSenderDisplayName;
    }
    
    public boolean hasSenderEmailAddress()
    {
      return this.hasSenderEmailAddress;
    }
    
    public GmailReference mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 10: 
          setSenderEmailAddress(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setEmailUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setSenderDisplayName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public GmailReference setEmailUrl(String paramString)
    {
      this.hasEmailUrl = true;
      this.emailUrl_ = paramString;
      return this;
    }
    
    public GmailReference setSenderDisplayName(String paramString)
    {
      this.hasSenderDisplayName = true;
      this.senderDisplayName_ = paramString;
      return this;
    }
    
    public GmailReference setSenderEmailAddress(String paramString)
    {
      this.hasSenderEmailAddress = true;
      this.senderEmailAddress_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSenderEmailAddress()) {
        paramCodedOutputStreamMicro.writeString(1, getSenderEmailAddress());
      }
      if (hasEmailUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getEmailUrl());
      }
      if (hasSenderDisplayName()) {
        paramCodedOutputStreamMicro.writeString(3, getSenderDisplayName());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.AgendaProtos
 * JD-Core Version:    0.7.0.1
 */