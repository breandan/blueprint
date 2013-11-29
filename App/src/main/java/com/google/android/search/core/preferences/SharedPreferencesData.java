package com.google.android.search.core.preferences;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class SharedPreferencesData
  extends MessageMicro
{
  private int cachedSize = -1;
  private List<SharedPreferenceEntry> entry_ = Collections.emptyList();
  
  public SharedPreferencesData addEntry(SharedPreferenceEntry paramSharedPreferenceEntry)
  {
    if (paramSharedPreferenceEntry == null) {
      throw new NullPointerException();
    }
    if (this.entry_.isEmpty()) {
      this.entry_ = new ArrayList();
    }
    this.entry_.add(paramSharedPreferenceEntry);
    return this;
  }
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public List<SharedPreferenceEntry> getEntryList()
  {
    return this.entry_;
  }
  
  public int getSerializedSize()
  {
    int i = 0;
    Iterator localIterator = getEntryList().iterator();
    while (localIterator.hasNext()) {
      i += CodedOutputStreamMicro.computeMessageSize(1, (SharedPreferenceEntry)localIterator.next());
    }
    this.cachedSize = i;
    return i;
  }
  
  public SharedPreferencesData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
      SharedPreferenceEntry localSharedPreferenceEntry = new SharedPreferenceEntry();
      paramCodedInputStreamMicro.readMessage(localSharedPreferenceEntry);
      addEntry(localSharedPreferenceEntry);
    }
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    Iterator localIterator = getEntryList().iterator();
    while (localIterator.hasNext()) {
      paramCodedOutputStreamMicro.writeMessage(1, (SharedPreferenceEntry)localIterator.next());
    }
  }
  
  public static final class SharedPreferenceEntry
    extends MessageMicro
  {
    private boolean boolValue_ = false;
    private ByteStringMicro bytesValue_ = ByteStringMicro.EMPTY;
    private int cachedSize = -1;
    private float floatValue_ = 0.0F;
    private boolean hasBoolValue;
    private boolean hasBytesValue;
    private boolean hasFloatValue;
    private boolean hasIntValue;
    private boolean hasKey;
    private boolean hasLongValue;
    private boolean hasStringValue;
    private int intValue_ = 0;
    private String key_ = "";
    private long longValue_ = 0L;
    private List<String> stringSetValue_ = Collections.emptyList();
    private String stringValue_ = "";
    
    public SharedPreferenceEntry addStringSetValue(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.stringSetValue_.isEmpty()) {
        this.stringSetValue_ = new ArrayList();
      }
      this.stringSetValue_.add(paramString);
      return this;
    }
    
    public boolean getBoolValue()
    {
      return this.boolValue_;
    }
    
    public ByteStringMicro getBytesValue()
    {
      return this.bytesValue_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getFloatValue()
    {
      return this.floatValue_;
    }
    
    public int getIntValue()
    {
      return this.intValue_;
    }
    
    public String getKey()
    {
      return this.key_;
    }
    
    public long getLongValue()
    {
      return this.longValue_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasKey();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getKey());
      }
      if (hasBoolValue()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getBoolValue());
      }
      if (hasFloatValue()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getFloatValue());
      }
      if (hasIntValue()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getIntValue());
      }
      if (hasLongValue()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getLongValue());
      }
      if (hasStringValue()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getStringValue());
      }
      int j = 0;
      Iterator localIterator = getStringSetValueList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getStringSetValueList().size();
      if (hasBytesValue()) {
        k += CodedOutputStreamMicro.computeBytesSize(8, getBytesValue());
      }
      this.cachedSize = k;
      return k;
    }
    
    public String getStringSetValue(int paramInt)
    {
      return (String)this.stringSetValue_.get(paramInt);
    }
    
    public int getStringSetValueCount()
    {
      return this.stringSetValue_.size();
    }
    
    public List<String> getStringSetValueList()
    {
      return this.stringSetValue_;
    }
    
    public String getStringValue()
    {
      return this.stringValue_;
    }
    
    public boolean hasBoolValue()
    {
      return this.hasBoolValue;
    }
    
    public boolean hasBytesValue()
    {
      return this.hasBytesValue;
    }
    
    public boolean hasFloatValue()
    {
      return this.hasFloatValue;
    }
    
    public boolean hasIntValue()
    {
      return this.hasIntValue;
    }
    
    public boolean hasKey()
    {
      return this.hasKey;
    }
    
    public boolean hasLongValue()
    {
      return this.hasLongValue;
    }
    
    public boolean hasStringValue()
    {
      return this.hasStringValue;
    }
    
    public SharedPreferenceEntry mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 16: 
          setBoolValue(paramCodedInputStreamMicro.readBool());
          break;
        case 29: 
          setFloatValue(paramCodedInputStreamMicro.readFloat());
          break;
        case 32: 
          setIntValue(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setLongValue(paramCodedInputStreamMicro.readInt64());
          break;
        case 50: 
          setStringValue(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          addStringSetValue(paramCodedInputStreamMicro.readString());
          break;
        }
        setBytesValue(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public SharedPreferenceEntry setBoolValue(boolean paramBoolean)
    {
      this.hasBoolValue = true;
      this.boolValue_ = paramBoolean;
      return this;
    }
    
    public SharedPreferenceEntry setBytesValue(ByteStringMicro paramByteStringMicro)
    {
      this.hasBytesValue = true;
      this.bytesValue_ = paramByteStringMicro;
      return this;
    }
    
    public SharedPreferenceEntry setFloatValue(float paramFloat)
    {
      this.hasFloatValue = true;
      this.floatValue_ = paramFloat;
      return this;
    }
    
    public SharedPreferenceEntry setIntValue(int paramInt)
    {
      this.hasIntValue = true;
      this.intValue_ = paramInt;
      return this;
    }
    
    public SharedPreferenceEntry setKey(String paramString)
    {
      this.hasKey = true;
      this.key_ = paramString;
      return this;
    }
    
    public SharedPreferenceEntry setLongValue(long paramLong)
    {
      this.hasLongValue = true;
      this.longValue_ = paramLong;
      return this;
    }
    
    public SharedPreferenceEntry setStringValue(String paramString)
    {
      this.hasStringValue = true;
      this.stringValue_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasKey()) {
        paramCodedOutputStreamMicro.writeString(1, getKey());
      }
      if (hasBoolValue()) {
        paramCodedOutputStreamMicro.writeBool(2, getBoolValue());
      }
      if (hasFloatValue()) {
        paramCodedOutputStreamMicro.writeFloat(3, getFloatValue());
      }
      if (hasIntValue()) {
        paramCodedOutputStreamMicro.writeInt32(4, getIntValue());
      }
      if (hasLongValue()) {
        paramCodedOutputStreamMicro.writeInt64(5, getLongValue());
      }
      if (hasStringValue()) {
        paramCodedOutputStreamMicro.writeString(6, getStringValue());
      }
      Iterator localIterator = getStringSetValueList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(7, (String)localIterator.next());
      }
      if (hasBytesValue()) {
        paramCodedOutputStreamMicro.writeBytes(8, getBytesValue());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SharedPreferencesData
 * JD-Core Version:    0.7.0.1
 */