package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class CookieProtos
{
  public static final class MajelCookie
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasName;
    private boolean hasValue;
    private String name_ = "";
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      if (hasValue()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getValue());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getValue()
    {
      return this.value_;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public MajelCookie mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setName(paramCodedInputStreamMicro.readString());
          break;
        }
        setValue(paramCodedInputStreamMicro.readString());
      }
    }
    
    public MajelCookie setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public MajelCookie setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(2, getValue());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.CookieProtos
 * JD-Core Version:    0.7.0.1
 */