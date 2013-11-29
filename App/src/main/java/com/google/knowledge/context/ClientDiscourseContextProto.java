package com.google.knowledge.context;

import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ClientDiscourseContextProto
{
  public static final class ClientDiscourseContext
    extends MessageMicro
  {
    private List<ActionV2Protos.ActionV2> actions_ = Collections.emptyList();
    private int cachedSize = -1;
    private List<ClientDiscourseContextProto.ClientEntity> entity_ = Collections.emptyList();
    
    public ClientDiscourseContext addActions(ActionV2Protos.ActionV2 paramActionV2)
    {
      if (paramActionV2 == null) {
        throw new NullPointerException();
      }
      if (this.actions_.isEmpty()) {
        this.actions_ = new ArrayList();
      }
      this.actions_.add(paramActionV2);
      return this;
    }
    
    public ClientDiscourseContext addEntity(ClientDiscourseContextProto.ClientEntity paramClientEntity)
    {
      if (paramClientEntity == null) {
        throw new NullPointerException();
      }
      if (this.entity_.isEmpty()) {
        this.entity_ = new ArrayList();
      }
      this.entity_.add(paramClientEntity);
      return this;
    }
    
    public List<ActionV2Protos.ActionV2> getActionsList()
    {
      return this.actions_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<ClientDiscourseContextProto.ClientEntity> getEntityList()
    {
      return this.entity_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getEntityList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (ClientDiscourseContextProto.ClientEntity)localIterator1.next());
      }
      Iterator localIterator2 = getActionsList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (ActionV2Protos.ActionV2)localIterator2.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public ClientDiscourseContext mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          ClientDiscourseContextProto.ClientEntity localClientEntity = new ClientDiscourseContextProto.ClientEntity();
          paramCodedInputStreamMicro.readMessage(localClientEntity);
          addEntity(localClientEntity);
          break;
        }
        ActionV2Protos.ActionV2 localActionV2 = new ActionV2Protos.ActionV2();
        paramCodedInputStreamMicro.readMessage(localActionV2);
        addActions(localActionV2);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getEntityList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (ClientDiscourseContextProto.ClientEntity)localIterator1.next());
      }
      Iterator localIterator2 = getActionsList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (ActionV2Protos.ActionV2)localIterator2.next());
      }
    }
  }
  
  public static final class ClientEntity
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String canonicalText_ = "";
    private String clientEntityId_ = "";
    private int gender_ = 0;
    private boolean hasCanonicalText;
    private boolean hasClientEntityId;
    private boolean hasGender;
    private List<ClientDiscourseContextProto.ClientMention> mention_ = Collections.emptyList();
    private List<Integer> type_ = Collections.emptyList();
    
    public ClientEntity addMention(ClientDiscourseContextProto.ClientMention paramClientMention)
    {
      if (paramClientMention == null) {
        throw new NullPointerException();
      }
      if (this.mention_.isEmpty()) {
        this.mention_ = new ArrayList();
      }
      this.mention_.add(paramClientMention);
      return this;
    }
    
    public ClientEntity addType(int paramInt)
    {
      if (this.type_.isEmpty()) {
        this.type_ = new ArrayList();
      }
      this.type_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCanonicalText()
    {
      return this.canonicalText_;
    }
    
    public String getClientEntityId()
    {
      return this.clientEntityId_;
    }
    
    public int getGender()
    {
      return this.gender_;
    }
    
    public List<ClientDiscourseContextProto.ClientMention> getMentionList()
    {
      return this.mention_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getMentionList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (ClientDiscourseContextProto.ClientMention)localIterator1.next());
      }
      if (hasCanonicalText()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getCanonicalText());
      }
      int j = 0;
      Iterator localIterator2 = getTypeList().iterator();
      while (localIterator2.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator2.next()).intValue());
      }
      int k = i + j + 1 * getTypeList().size();
      if (hasGender()) {
        k += CodedOutputStreamMicro.computeInt32Size(4, getGender());
      }
      if (hasClientEntityId()) {
        k += CodedOutputStreamMicro.computeStringSize(5, getClientEntityId());
      }
      this.cachedSize = k;
      return k;
    }
    
    public List<Integer> getTypeList()
    {
      return this.type_;
    }
    
    public boolean hasCanonicalText()
    {
      return this.hasCanonicalText;
    }
    
    public boolean hasClientEntityId()
    {
      return this.hasClientEntityId;
    }
    
    public boolean hasGender()
    {
      return this.hasGender;
    }
    
    public ClientEntity mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          ClientDiscourseContextProto.ClientMention localClientMention = new ClientDiscourseContextProto.ClientMention();
          paramCodedInputStreamMicro.readMessage(localClientMention);
          addMention(localClientMention);
          break;
        case 18: 
          setCanonicalText(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          addType(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setGender(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setClientEntityId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public ClientEntity setCanonicalText(String paramString)
    {
      this.hasCanonicalText = true;
      this.canonicalText_ = paramString;
      return this;
    }
    
    public ClientEntity setClientEntityId(String paramString)
    {
      this.hasClientEntityId = true;
      this.clientEntityId_ = paramString;
      return this;
    }
    
    public ClientEntity setGender(int paramInt)
    {
      this.hasGender = true;
      this.gender_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getMentionList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (ClientDiscourseContextProto.ClientMention)localIterator1.next());
      }
      if (hasCanonicalText()) {
        paramCodedOutputStreamMicro.writeString(2, getCanonicalText());
      }
      Iterator localIterator2 = getTypeList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(3, ((Integer)localIterator2.next()).intValue());
      }
      if (hasGender()) {
        paramCodedOutputStreamMicro.writeInt32(4, getGender());
      }
      if (hasClientEntityId()) {
        paramCodedOutputStreamMicro.writeString(5, getClientEntityId());
      }
    }
  }
  
  public static final class ClientMention
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasUnixTimeMs;
    private long unixTimeMs_ = 0L;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUnixTimeMs();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getUnixTimeMs());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getUnixTimeMs()
    {
      return this.unixTimeMs_;
    }
    
    public boolean hasUnixTimeMs()
    {
      return this.hasUnixTimeMs;
    }
    
    public ClientMention mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setUnixTimeMs(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public ClientMention setUnixTimeMs(long paramLong)
    {
      this.hasUnixTimeMs = true;
      this.unixTimeMs_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUnixTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(1, getUnixTimeMs());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.knowledge.context.ClientDiscourseContextProto
 * JD-Core Version:    0.7.0.1
 */