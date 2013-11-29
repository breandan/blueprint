package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class AliasProto
{
  public static final class Alias
    extends MessageMicro
  {
    private boolean aliasLocationAvailable_ = false;
    private int aliasType_ = 0;
    private int cachedSize = -1;
    private boolean hasAliasLocationAvailable;
    private boolean hasAliasType;
    
    public boolean getAliasLocationAvailable()
    {
      return this.aliasLocationAvailable_;
    }
    
    public int getAliasType()
    {
      return this.aliasType_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAliasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getAliasType());
      }
      if (hasAliasLocationAvailable()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getAliasLocationAvailable());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAliasLocationAvailable()
    {
      return this.hasAliasLocationAvailable;
    }
    
    public boolean hasAliasType()
    {
      return this.hasAliasType;
    }
    
    public Alias mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAliasType(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setAliasLocationAvailable(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public Alias setAliasLocationAvailable(boolean paramBoolean)
    {
      this.hasAliasLocationAvailable = true;
      this.aliasLocationAvailable_ = paramBoolean;
      return this;
    }
    
    public Alias setAliasType(int paramInt)
    {
      this.hasAliasType = true;
      this.aliasType_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAliasType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getAliasType());
      }
      if (hasAliasLocationAvailable()) {
        paramCodedOutputStreamMicro.writeBool(2, getAliasLocationAvailable());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.AliasProto
 * JD-Core Version:    0.7.0.1
 */