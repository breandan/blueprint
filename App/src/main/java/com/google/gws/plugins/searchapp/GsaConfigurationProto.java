package com.google.gws.plugins.searchapp;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GsaConfigurationProto
{
  public static final class GsaExperiments
    extends MessageMicro
  {
    public static final int CLIENT_EXPERIMENT_IDS_FIELD_NUMBER = 3;
    public static final int EVENT_TIMESTAMP_FIELD_NUMBER = 2;
    public static final int KEY_VALUE_PAIR_FIELD_NUMBER = 1;
    private int cachedSize = -1;
    private List<Integer> clientExperimentIds_ = Collections.emptyList();
    private long eventTimestamp_ = 0L;
    private boolean hasEventTimestamp;
    private List<GsaConfigurationProto.KeyValuePair> keyValuePair_ = Collections.emptyList();
    
    public static GsaExperiments parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      return new GsaExperiments().mergeFrom(paramCodedInputStreamMicro);
    }
    
    public static GsaExperiments parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferMicroException
    {
      return (GsaExperiments)new GsaExperiments().mergeFrom(paramArrayOfByte);
    }
    
    public GsaExperiments addClientExperimentIds(int paramInt)
    {
      if (this.clientExperimentIds_.isEmpty()) {
        this.clientExperimentIds_ = new ArrayList();
      }
      this.clientExperimentIds_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public GsaExperiments addKeyValuePair(GsaConfigurationProto.KeyValuePair paramKeyValuePair)
    {
      if (paramKeyValuePair == null) {
        throw new NullPointerException();
      }
      if (this.keyValuePair_.isEmpty()) {
        this.keyValuePair_ = new ArrayList();
      }
      this.keyValuePair_.add(paramKeyValuePair);
      return this;
    }
    
    public final GsaExperiments clear()
    {
      clearKeyValuePair();
      clearEventTimestamp();
      clearClientExperimentIds();
      this.cachedSize = -1;
      return this;
    }
    
    public GsaExperiments clearClientExperimentIds()
    {
      this.clientExperimentIds_ = Collections.emptyList();
      return this;
    }
    
    public GsaExperiments clearEventTimestamp()
    {
      this.hasEventTimestamp = false;
      this.eventTimestamp_ = 0L;
      return this;
    }
    
    public GsaExperiments clearKeyValuePair()
    {
      this.keyValuePair_ = Collections.emptyList();
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getClientExperimentIds(int paramInt)
    {
      return ((Integer)this.clientExperimentIds_.get(paramInt)).intValue();
    }
    
    public int getClientExperimentIdsCount()
    {
      return this.clientExperimentIds_.size();
    }
    
    public List<Integer> getClientExperimentIdsList()
    {
      return this.clientExperimentIds_;
    }
    
    public long getEventTimestamp()
    {
      return this.eventTimestamp_;
    }
    
    public GsaConfigurationProto.KeyValuePair getKeyValuePair(int paramInt)
    {
      return (GsaConfigurationProto.KeyValuePair)this.keyValuePair_.get(paramInt);
    }
    
    public int getKeyValuePairCount()
    {
      return this.keyValuePair_.size();
    }
    
    public List<GsaConfigurationProto.KeyValuePair> getKeyValuePairList()
    {
      return this.keyValuePair_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getKeyValuePairList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (GsaConfigurationProto.KeyValuePair)localIterator1.next());
      }
      if (hasEventTimestamp()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getEventTimestamp());
      }
      int j = 0;
      Iterator localIterator2 = getClientExperimentIdsList().iterator();
      while (localIterator2.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator2.next()).intValue());
      }
      int k = i + j + 1 * getClientExperimentIdsList().size();
      this.cachedSize = k;
      return k;
    }
    
    public boolean hasEventTimestamp()
    {
      return this.hasEventTimestamp;
    }
    
    public final boolean isInitialized()
    {
      return true;
    }
    
    public GsaExperiments mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          GsaConfigurationProto.KeyValuePair localKeyValuePair = new GsaConfigurationProto.KeyValuePair();
          paramCodedInputStreamMicro.readMessage(localKeyValuePair);
          addKeyValuePair(localKeyValuePair);
          break;
        case 16: 
          setEventTimestamp(paramCodedInputStreamMicro.readInt64());
          break;
        }
        addClientExperimentIds(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public GsaExperiments setClientExperimentIds(int paramInt1, int paramInt2)
    {
      this.clientExperimentIds_.set(paramInt1, Integer.valueOf(paramInt2));
      return this;
    }
    
    public GsaExperiments setEventTimestamp(long paramLong)
    {
      this.hasEventTimestamp = true;
      this.eventTimestamp_ = paramLong;
      return this;
    }
    
    public GsaExperiments setKeyValuePair(int paramInt, GsaConfigurationProto.KeyValuePair paramKeyValuePair)
    {
      if (paramKeyValuePair == null) {
        throw new NullPointerException();
      }
      this.keyValuePair_.set(paramInt, paramKeyValuePair);
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getKeyValuePairList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (GsaConfigurationProto.KeyValuePair)localIterator1.next());
      }
      if (hasEventTimestamp()) {
        paramCodedOutputStreamMicro.writeInt64(2, getEventTimestamp());
      }
      Iterator localIterator2 = getClientExperimentIdsList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(3, ((Integer)localIterator2.next()).intValue());
      }
    }
  }
  
  public static final class KeyValuePair
    extends MessageMicro
  {
    private boolean boolValue_ = false;
    private int cachedSize = -1;
    private boolean hasBoolValue;
    private boolean hasIntValue;
    private boolean hasKey;
    private boolean hasStringValue;
    private int intValue_ = 0;
    private String key_ = "";
    private String stringValue_ = "";
    
    public boolean getBoolValue()
    {
      return this.boolValue_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getIntValue()
    {
      return this.intValue_;
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
      if (hasBoolValue()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getBoolValue());
      }
      if (hasStringValue()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getStringValue());
      }
      if (hasIntValue()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getIntValue());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getStringValue()
    {
      return this.stringValue_;
    }
    
    public boolean hasBoolValue()
    {
      return this.hasBoolValue;
    }
    
    public boolean hasIntValue()
    {
      return this.hasIntValue;
    }
    
    public boolean hasKey()
    {
      return this.hasKey;
    }
    
    public boolean hasStringValue()
    {
      return this.hasStringValue;
    }
    
    public KeyValuePair mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 26: 
          setStringValue(paramCodedInputStreamMicro.readString());
          break;
        }
        setIntValue(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public KeyValuePair setBoolValue(boolean paramBoolean)
    {
      this.hasBoolValue = true;
      this.boolValue_ = paramBoolean;
      return this;
    }
    
    public KeyValuePair setIntValue(int paramInt)
    {
      this.hasIntValue = true;
      this.intValue_ = paramInt;
      return this;
    }
    
    public KeyValuePair setKey(String paramString)
    {
      this.hasKey = true;
      this.key_ = paramString;
      return this;
    }
    
    public KeyValuePair setStringValue(String paramString)
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
      if (hasStringValue()) {
        paramCodedOutputStreamMicro.writeString(3, getStringValue());
      }
      if (hasIntValue()) {
        paramCodedOutputStreamMicro.writeInt32(4, getIntValue());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.gws.plugins.searchapp.GsaConfigurationProto
 * JD-Core Version:    0.7.0.1
 */