package com.google.speech.s3;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PinholeStream
{
  public static final class PinholeCgiParam
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasKey;
    private boolean hasValue;
    private String key_ = "";
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getKey()
    {
      return this.key_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasKey();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getKey());
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
    
    public boolean hasKey()
    {
      return this.hasKey;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public PinholeCgiParam mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setKey(paramCodedInputStreamMicro.readString());
          break;
        }
        setValue(paramCodedInputStreamMicro.readString());
      }
    }
    
    public PinholeCgiParam setKey(String paramString)
    {
      this.hasKey = true;
      this.key_ = paramString;
      return this;
    }
    
    public PinholeCgiParam setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasKey()) {
        paramCodedOutputStreamMicro.writeString(1, getKey());
      }
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(2, getValue());
      }
    }
  }
  
  public static final class PinholeHeader
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasKey;
    private boolean hasOverwrite;
    private boolean hasValue;
    private String key_ = "";
    private int overwrite_ = 0;
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getKey()
    {
      return this.key_;
    }
    
    public int getOverwrite()
    {
      return this.overwrite_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasKey();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getKey());
      }
      if (hasValue()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getValue());
      }
      if (hasOverwrite()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getOverwrite());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getValue()
    {
      return this.value_;
    }
    
    public boolean hasKey()
    {
      return this.hasKey;
    }
    
    public boolean hasOverwrite()
    {
      return this.hasOverwrite;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public PinholeHeader mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setKey(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setValue(paramCodedInputStreamMicro.readString());
          break;
        }
        setOverwrite(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public PinholeHeader setKey(String paramString)
    {
      this.hasKey = true;
      this.key_ = paramString;
      return this;
    }
    
    public PinholeHeader setOverwrite(int paramInt)
    {
      this.hasOverwrite = true;
      this.overwrite_ = paramInt;
      return this;
    }
    
    public PinholeHeader setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasKey()) {
        paramCodedOutputStreamMicro.writeString(1, getKey());
      }
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(2, getValue());
      }
      if (hasOverwrite()) {
        paramCodedOutputStreamMicro.writeInt32(3, getOverwrite());
      }
    }
  }
  
  public static final class PinholeOutput
    extends MessageMicro
  {
    private int cachedSize = -1;
    private ByteStringMicro gwsBodyFragment_ = ByteStringMicro.EMPTY;
    private boolean gwsHeaderComplete_ = false;
    private String gwsHeaderFragment_ = "";
    private boolean gwsResponseComplete_ = false;
    private boolean hasGwsBodyFragment;
    private boolean hasGwsHeaderComplete;
    private boolean hasGwsHeaderFragment;
    private boolean hasGwsResponseComplete;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public ByteStringMicro getGwsBodyFragment()
    {
      return this.gwsBodyFragment_;
    }
    
    public boolean getGwsHeaderComplete()
    {
      return this.gwsHeaderComplete_;
    }
    
    public String getGwsHeaderFragment()
    {
      return this.gwsHeaderFragment_;
    }
    
    public boolean getGwsResponseComplete()
    {
      return this.gwsResponseComplete_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasGwsHeaderFragment();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getGwsHeaderFragment());
      }
      if (hasGwsHeaderComplete()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getGwsHeaderComplete());
      }
      if (hasGwsBodyFragment()) {
        i += CodedOutputStreamMicro.computeBytesSize(3, getGwsBodyFragment());
      }
      if (hasGwsResponseComplete()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getGwsResponseComplete());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasGwsBodyFragment()
    {
      return this.hasGwsBodyFragment;
    }
    
    public boolean hasGwsHeaderComplete()
    {
      return this.hasGwsHeaderComplete;
    }
    
    public boolean hasGwsHeaderFragment()
    {
      return this.hasGwsHeaderFragment;
    }
    
    public boolean hasGwsResponseComplete()
    {
      return this.hasGwsResponseComplete;
    }
    
    public PinholeOutput mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setGwsHeaderFragment(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          setGwsHeaderComplete(paramCodedInputStreamMicro.readBool());
          break;
        case 26: 
          setGwsBodyFragment(paramCodedInputStreamMicro.readBytes());
          break;
        }
        setGwsResponseComplete(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public PinholeOutput setGwsBodyFragment(ByteStringMicro paramByteStringMicro)
    {
      this.hasGwsBodyFragment = true;
      this.gwsBodyFragment_ = paramByteStringMicro;
      return this;
    }
    
    public PinholeOutput setGwsHeaderComplete(boolean paramBoolean)
    {
      this.hasGwsHeaderComplete = true;
      this.gwsHeaderComplete_ = paramBoolean;
      return this;
    }
    
    public PinholeOutput setGwsHeaderFragment(String paramString)
    {
      this.hasGwsHeaderFragment = true;
      this.gwsHeaderFragment_ = paramString;
      return this;
    }
    
    public PinholeOutput setGwsResponseComplete(boolean paramBoolean)
    {
      this.hasGwsResponseComplete = true;
      this.gwsResponseComplete_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasGwsHeaderFragment()) {
        paramCodedOutputStreamMicro.writeString(1, getGwsHeaderFragment());
      }
      if (hasGwsHeaderComplete()) {
        paramCodedOutputStreamMicro.writeBool(2, getGwsHeaderComplete());
      }
      if (hasGwsBodyFragment()) {
        paramCodedOutputStreamMicro.writeBytes(3, getGwsBodyFragment());
      }
      if (hasGwsResponseComplete()) {
        paramCodedOutputStreamMicro.writeBool(4, getGwsResponseComplete());
      }
    }
  }
  
  public static final class PinholeParams
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<PinholeStream.PinholeCgiParam> cgiParams_ = Collections.emptyList();
    private boolean hasUrlPath;
    private List<PinholeStream.PinholeHeader> headers_ = Collections.emptyList();
    private String urlPath_ = "/search";
    
    public PinholeParams addCgiParams(PinholeStream.PinholeCgiParam paramPinholeCgiParam)
    {
      if (paramPinholeCgiParam == null) {
        throw new NullPointerException();
      }
      if (this.cgiParams_.isEmpty()) {
        this.cgiParams_ = new ArrayList();
      }
      this.cgiParams_.add(paramPinholeCgiParam);
      return this;
    }
    
    public PinholeParams addHeaders(PinholeStream.PinholeHeader paramPinholeHeader)
    {
      if (paramPinholeHeader == null) {
        throw new NullPointerException();
      }
      if (this.headers_.isEmpty()) {
        this.headers_ = new ArrayList();
      }
      this.headers_.add(paramPinholeHeader);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<PinholeStream.PinholeCgiParam> getCgiParamsList()
    {
      return this.cgiParams_;
    }
    
    public List<PinholeStream.PinholeHeader> getHeadersList()
    {
      return this.headers_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getCgiParamsList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (PinholeStream.PinholeCgiParam)localIterator1.next());
      }
      Iterator localIterator2 = getHeadersList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (PinholeStream.PinholeHeader)localIterator2.next());
      }
      if (hasUrlPath()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getUrlPath());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getUrlPath()
    {
      return this.urlPath_;
    }
    
    public boolean hasUrlPath()
    {
      return this.hasUrlPath;
    }
    
    public PinholeParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          PinholeStream.PinholeCgiParam localPinholeCgiParam = new PinholeStream.PinholeCgiParam();
          paramCodedInputStreamMicro.readMessage(localPinholeCgiParam);
          addCgiParams(localPinholeCgiParam);
          break;
        case 18: 
          PinholeStream.PinholeHeader localPinholeHeader = new PinholeStream.PinholeHeader();
          paramCodedInputStreamMicro.readMessage(localPinholeHeader);
          addHeaders(localPinholeHeader);
          break;
        }
        setUrlPath(paramCodedInputStreamMicro.readString());
      }
    }
    
    public PinholeParams setUrlPath(String paramString)
    {
      this.hasUrlPath = true;
      this.urlPath_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getCgiParamsList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (PinholeStream.PinholeCgiParam)localIterator1.next());
      }
      Iterator localIterator2 = getHeadersList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (PinholeStream.PinholeHeader)localIterator2.next());
      }
      if (hasUrlPath()) {
        paramCodedOutputStreamMicro.writeString(3, getUrlPath());
      }
    }
  }
  
  public static final class PinholeTtsBridgeParams
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasTtsStringPrefix;
    private boolean hasTtsStringSuffix;
    private String ttsStringPrefix_ = "\\u003C!-- ectanstts ";
    private String ttsStringSuffix_ = " ectanstts --\\u003E";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTtsStringPrefix();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTtsStringPrefix());
      }
      if (hasTtsStringSuffix()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getTtsStringSuffix());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTtsStringPrefix()
    {
      return this.ttsStringPrefix_;
    }
    
    public String getTtsStringSuffix()
    {
      return this.ttsStringSuffix_;
    }
    
    public boolean hasTtsStringPrefix()
    {
      return this.hasTtsStringPrefix;
    }
    
    public boolean hasTtsStringSuffix()
    {
      return this.hasTtsStringSuffix;
    }
    
    public PinholeTtsBridgeParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTtsStringPrefix(paramCodedInputStreamMicro.readString());
          break;
        }
        setTtsStringSuffix(paramCodedInputStreamMicro.readString());
      }
    }
    
    public PinholeTtsBridgeParams setTtsStringPrefix(String paramString)
    {
      this.hasTtsStringPrefix = true;
      this.ttsStringPrefix_ = paramString;
      return this;
    }
    
    public PinholeTtsBridgeParams setTtsStringSuffix(String paramString)
    {
      this.hasTtsStringSuffix = true;
      this.ttsStringSuffix_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTtsStringPrefix()) {
        paramCodedOutputStreamMicro.writeString(1, getTtsStringPrefix());
      }
      if (hasTtsStringSuffix()) {
        paramCodedOutputStreamMicro.writeString(2, getTtsStringSuffix());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.PinholeStream
 * JD-Core Version:    0.7.0.1
 */