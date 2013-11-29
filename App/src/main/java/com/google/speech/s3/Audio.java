package com.google.speech.s3;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class Audio
{
  public static final class S3AudioData
    extends MessageMicro
  {
    private ByteStringMicro audio_ = ByteStringMicro.EMPTY;
    private int cachedSize = -1;
    private boolean hasAudio;
    
    public ByteStringMicro getAudio()
    {
      return this.audio_;
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
      boolean bool = hasAudio();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBytesSize(1, getAudio());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAudio()
    {
      return this.hasAudio;
    }
    
    public S3AudioData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        }
        setAudio(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public S3AudioData setAudio(ByteStringMicro paramByteStringMicro)
    {
      this.hasAudio = true;
      this.audio_ = paramByteStringMicro;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAudio()) {
        paramCodedOutputStreamMicro.writeBytes(1, getAudio());
      }
    }
  }
  
  public static final class S3AudioInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int channelCount_ = 1;
    private int encoding_ = 0;
    private boolean hasChannelCount;
    private boolean hasEncoding;
    private boolean hasSampleRateHz;
    private float sampleRateHz_ = 0.0F;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getChannelCount()
    {
      return this.channelCount_;
    }
    
    public int getEncoding()
    {
      return this.encoding_;
    }
    
    public float getSampleRateHz()
    {
      return this.sampleRateHz_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSampleRateHz();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(2, getSampleRateHz());
      }
      if (hasEncoding()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getEncoding());
      }
      if (hasChannelCount()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getChannelCount());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasChannelCount()
    {
      return this.hasChannelCount;
    }
    
    public boolean hasEncoding()
    {
      return this.hasEncoding;
    }
    
    public boolean hasSampleRateHz()
    {
      return this.hasSampleRateHz;
    }
    
    public S3AudioInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 21: 
          setSampleRateHz(paramCodedInputStreamMicro.readFloat());
          break;
        case 24: 
          setEncoding(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setChannelCount(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public S3AudioInfo setChannelCount(int paramInt)
    {
      this.hasChannelCount = true;
      this.channelCount_ = paramInt;
      return this;
    }
    
    public S3AudioInfo setEncoding(int paramInt)
    {
      this.hasEncoding = true;
      this.encoding_ = paramInt;
      return this;
    }
    
    public S3AudioInfo setSampleRateHz(float paramFloat)
    {
      this.hasSampleRateHz = true;
      this.sampleRateHz_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSampleRateHz()) {
        paramCodedOutputStreamMicro.writeFloat(2, getSampleRateHz());
      }
      if (hasEncoding()) {
        paramCodedOutputStreamMicro.writeInt32(3, getEncoding());
      }
      if (hasChannelCount()) {
        paramCodedOutputStreamMicro.writeInt32(4, getChannelCount());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.Audio
 * JD-Core Version:    0.7.0.1
 */