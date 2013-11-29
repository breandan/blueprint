package com.google.android.search.core.sdch;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class DictionaryMetadata
  extends MessageMicro
{
  private int cachedSize = -1;
  private String clientHash_ = "";
  private String domain_ = "";
  private long fetchTimeMs_ = 0L;
  private String formatVersion_ = "";
  private boolean hasClientHash;
  private boolean hasDomain;
  private boolean hasFetchTimeMs;
  private boolean hasFormatVersion;
  private boolean hasPath;
  private boolean hasPort;
  private boolean hasServerHash;
  private String path_ = "";
  private int port_ = 0;
  private String serverHash_ = "";
  
  public static DictionaryMetadata parseFrom(byte[] paramArrayOfByte)
    throws InvalidProtocolBufferMicroException
  {
    return (DictionaryMetadata)new DictionaryMetadata().mergeFrom(paramArrayOfByte);
  }
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public String getClientHash()
  {
    return this.clientHash_;
  }
  
  public String getDomain()
  {
    return this.domain_;
  }
  
  public long getFetchTimeMs()
  {
    return this.fetchTimeMs_;
  }
  
  public String getFormatVersion()
  {
    return this.formatVersion_;
  }
  
  public String getPath()
  {
    return this.path_;
  }
  
  public int getPort()
  {
    return this.port_;
  }
  
  public int getSerializedSize()
  {
    boolean bool = hasClientHash();
    int i = 0;
    if (bool) {
      i = 0 + CodedOutputStreamMicro.computeStringSize(1, getClientHash());
    }
    if (hasServerHash()) {
      i += CodedOutputStreamMicro.computeStringSize(2, getServerHash());
    }
    if (hasDomain()) {
      i += CodedOutputStreamMicro.computeStringSize(3, getDomain());
    }
    if (hasPath()) {
      i += CodedOutputStreamMicro.computeStringSize(4, getPath());
    }
    if (hasFormatVersion()) {
      i += CodedOutputStreamMicro.computeStringSize(5, getFormatVersion());
    }
    if (hasFetchTimeMs()) {
      i += CodedOutputStreamMicro.computeInt64Size(6, getFetchTimeMs());
    }
    if (hasPort()) {
      i += CodedOutputStreamMicro.computeInt32Size(7, getPort());
    }
    this.cachedSize = i;
    return i;
  }
  
  public String getServerHash()
  {
    return this.serverHash_;
  }
  
  public boolean hasClientHash()
  {
    return this.hasClientHash;
  }
  
  public boolean hasDomain()
  {
    return this.hasDomain;
  }
  
  public boolean hasFetchTimeMs()
  {
    return this.hasFetchTimeMs;
  }
  
  public boolean hasFormatVersion()
  {
    return this.hasFormatVersion;
  }
  
  public boolean hasPath()
  {
    return this.hasPath;
  }
  
  public boolean hasPort()
  {
    return this.hasPort;
  }
  
  public boolean hasServerHash()
  {
    return this.hasServerHash;
  }
  
  public DictionaryMetadata mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setClientHash(paramCodedInputStreamMicro.readString());
        break;
      case 18: 
        setServerHash(paramCodedInputStreamMicro.readString());
        break;
      case 26: 
        setDomain(paramCodedInputStreamMicro.readString());
        break;
      case 34: 
        setPath(paramCodedInputStreamMicro.readString());
        break;
      case 42: 
        setFormatVersion(paramCodedInputStreamMicro.readString());
        break;
      case 48: 
        setFetchTimeMs(paramCodedInputStreamMicro.readInt64());
        break;
      }
      setPort(paramCodedInputStreamMicro.readInt32());
    }
  }
  
  public DictionaryMetadata setClientHash(String paramString)
  {
    this.hasClientHash = true;
    this.clientHash_ = paramString;
    return this;
  }
  
  public DictionaryMetadata setDomain(String paramString)
  {
    this.hasDomain = true;
    this.domain_ = paramString;
    return this;
  }
  
  public DictionaryMetadata setFetchTimeMs(long paramLong)
  {
    this.hasFetchTimeMs = true;
    this.fetchTimeMs_ = paramLong;
    return this;
  }
  
  public DictionaryMetadata setFormatVersion(String paramString)
  {
    this.hasFormatVersion = true;
    this.formatVersion_ = paramString;
    return this;
  }
  
  public DictionaryMetadata setPath(String paramString)
  {
    this.hasPath = true;
    this.path_ = paramString;
    return this;
  }
  
  public DictionaryMetadata setPort(int paramInt)
  {
    this.hasPort = true;
    this.port_ = paramInt;
    return this;
  }
  
  public DictionaryMetadata setServerHash(String paramString)
  {
    this.hasServerHash = true;
    this.serverHash_ = paramString;
    return this;
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    if (hasClientHash()) {
      paramCodedOutputStreamMicro.writeString(1, getClientHash());
    }
    if (hasServerHash()) {
      paramCodedOutputStreamMicro.writeString(2, getServerHash());
    }
    if (hasDomain()) {
      paramCodedOutputStreamMicro.writeString(3, getDomain());
    }
    if (hasPath()) {
      paramCodedOutputStreamMicro.writeString(4, getPath());
    }
    if (hasFormatVersion()) {
      paramCodedOutputStreamMicro.writeString(5, getFormatVersion());
    }
    if (hasFetchTimeMs()) {
      paramCodedOutputStreamMicro.writeInt64(6, getFetchTimeMs());
    }
    if (hasPort()) {
      paramCodedOutputStreamMicro.writeInt32(7, getPort());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.DictionaryMetadata
 * JD-Core Version:    0.7.0.1
 */