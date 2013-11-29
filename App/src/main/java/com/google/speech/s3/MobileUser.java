package com.google.speech.s3;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class MobileUser
{
  public static final class MobileUserInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasNetworkMcc;
    private boolean hasNetworkMnc;
    private boolean hasNetworkType;
    private boolean hasSimMcc;
    private boolean hasSimMnc;
    private int networkMcc_ = 0;
    private int networkMnc_ = 0;
    private int networkType_ = 0;
    private int simMcc_ = 0;
    private int simMnc_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getNetworkMcc()
    {
      return this.networkMcc_;
    }
    
    public int getNetworkMnc()
    {
      return this.networkMnc_;
    }
    
    public int getNetworkType()
    {
      return this.networkType_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasNetworkMcc();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getNetworkMcc());
      }
      if (hasNetworkMnc()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getNetworkMnc());
      }
      if (hasSimMcc()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getSimMcc());
      }
      if (hasSimMnc()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getSimMnc());
      }
      if (hasNetworkType()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getNetworkType());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getSimMcc()
    {
      return this.simMcc_;
    }
    
    public int getSimMnc()
    {
      return this.simMnc_;
    }
    
    public boolean hasNetworkMcc()
    {
      return this.hasNetworkMcc;
    }
    
    public boolean hasNetworkMnc()
    {
      return this.hasNetworkMnc;
    }
    
    public boolean hasNetworkType()
    {
      return this.hasNetworkType;
    }
    
    public boolean hasSimMcc()
    {
      return this.hasSimMcc;
    }
    
    public boolean hasSimMnc()
    {
      return this.hasSimMnc;
    }
    
    public MobileUserInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setNetworkMcc(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setNetworkMnc(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setSimMcc(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setSimMnc(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setNetworkType(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public MobileUserInfo setNetworkMcc(int paramInt)
    {
      this.hasNetworkMcc = true;
      this.networkMcc_ = paramInt;
      return this;
    }
    
    public MobileUserInfo setNetworkMnc(int paramInt)
    {
      this.hasNetworkMnc = true;
      this.networkMnc_ = paramInt;
      return this;
    }
    
    public MobileUserInfo setNetworkType(int paramInt)
    {
      this.hasNetworkType = true;
      this.networkType_ = paramInt;
      return this;
    }
    
    public MobileUserInfo setSimMcc(int paramInt)
    {
      this.hasSimMcc = true;
      this.simMcc_ = paramInt;
      return this;
    }
    
    public MobileUserInfo setSimMnc(int paramInt)
    {
      this.hasSimMnc = true;
      this.simMnc_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasNetworkMcc()) {
        paramCodedOutputStreamMicro.writeInt32(1, getNetworkMcc());
      }
      if (hasNetworkMnc()) {
        paramCodedOutputStreamMicro.writeInt32(2, getNetworkMnc());
      }
      if (hasSimMcc()) {
        paramCodedOutputStreamMicro.writeInt32(3, getSimMcc());
      }
      if (hasSimMnc()) {
        paramCodedOutputStreamMicro.writeInt32(4, getSimMnc());
      }
      if (hasNetworkType()) {
        paramCodedOutputStreamMicro.writeInt32(5, getNetworkType());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.MobileUser
 * JD-Core Version:    0.7.0.1
 */