package com.google.android.search.core;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class RelationshipContactInfo
  extends MessageMicro
{
  public static final int ENTRY_FIELD_NUMBER = 1;
  private int cachedSize = -1;
  private List<RelationshipContactEntry> entry_ = Collections.emptyList();
  
  public static RelationshipContactInfo parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
    throws IOException
  {
    return new RelationshipContactInfo().mergeFrom(paramCodedInputStreamMicro);
  }
  
  public static RelationshipContactInfo parseFrom(byte[] paramArrayOfByte)
    throws InvalidProtocolBufferMicroException
  {
    return (RelationshipContactInfo)new RelationshipContactInfo().mergeFrom(paramArrayOfByte);
  }
  
  public RelationshipContactInfo addEntry(RelationshipContactEntry paramRelationshipContactEntry)
  {
    if (paramRelationshipContactEntry == null) {
      throw new NullPointerException();
    }
    if (this.entry_.isEmpty()) {
      this.entry_ = new ArrayList();
    }
    this.entry_.add(paramRelationshipContactEntry);
    return this;
  }
  
  public final RelationshipContactInfo clear()
  {
    clearEntry();
    this.cachedSize = -1;
    return this;
  }
  
  public RelationshipContactInfo clearEntry()
  {
    this.entry_ = Collections.emptyList();
    return this;
  }
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public RelationshipContactEntry getEntry(int paramInt)
  {
    return (RelationshipContactEntry)this.entry_.get(paramInt);
  }
  
  public int getEntryCount()
  {
    return this.entry_.size();
  }
  
  public List<RelationshipContactEntry> getEntryList()
  {
    return this.entry_;
  }
  
  public int getSerializedSize()
  {
    int i = 0;
    Iterator localIterator = getEntryList().iterator();
    while (localIterator.hasNext()) {
      i += CodedOutputStreamMicro.computeMessageSize(1, (RelationshipContactEntry)localIterator.next());
    }
    this.cachedSize = i;
    return i;
  }
  
  public final boolean isInitialized()
  {
    return true;
  }
  
  public RelationshipContactInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
      RelationshipContactEntry localRelationshipContactEntry = new RelationshipContactEntry();
      paramCodedInputStreamMicro.readMessage(localRelationshipContactEntry);
      addEntry(localRelationshipContactEntry);
    }
  }
  
  public RelationshipContactInfo setEntry(int paramInt, RelationshipContactEntry paramRelationshipContactEntry)
  {
    if (paramRelationshipContactEntry == null) {
      throw new NullPointerException();
    }
    this.entry_.set(paramInt, paramRelationshipContactEntry);
    return this;
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    Iterator localIterator = getEntryList().iterator();
    while (localIterator.hasNext()) {
      paramCodedOutputStreamMicro.writeMessage(1, (RelationshipContactEntry)localIterator.next());
    }
  }
  
  public static final class RelationshipContactEntry
    extends MessageMicro
  {
    public static final int CANONICALRELATIONSHIP_FIELD_NUMBER = 1;
    public static final int CONTACTLOOKUPKEY_FIELD_NUMBER = 2;
    private int cachedSize = -1;
    private String canonicalRelationship_ = "";
    private List<String> contactLookupKey_ = Collections.emptyList();
    private boolean hasCanonicalRelationship;
    
    public static RelationshipContactEntry parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      return new RelationshipContactEntry().mergeFrom(paramCodedInputStreamMicro);
    }
    
    public static RelationshipContactEntry parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferMicroException
    {
      return (RelationshipContactEntry)new RelationshipContactEntry().mergeFrom(paramArrayOfByte);
    }
    
    public RelationshipContactEntry addContactLookupKey(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.contactLookupKey_.isEmpty()) {
        this.contactLookupKey_ = new ArrayList();
      }
      this.contactLookupKey_.add(paramString);
      return this;
    }
    
    public final RelationshipContactEntry clear()
    {
      clearCanonicalRelationship();
      clearContactLookupKey();
      this.cachedSize = -1;
      return this;
    }
    
    public RelationshipContactEntry clearCanonicalRelationship()
    {
      this.hasCanonicalRelationship = false;
      this.canonicalRelationship_ = "";
      return this;
    }
    
    public RelationshipContactEntry clearContactLookupKey()
    {
      this.contactLookupKey_ = Collections.emptyList();
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCanonicalRelationship()
    {
      return this.canonicalRelationship_;
    }
    
    public String getContactLookupKey(int paramInt)
    {
      return (String)this.contactLookupKey_.get(paramInt);
    }
    
    public int getContactLookupKeyCount()
    {
      return this.contactLookupKey_.size();
    }
    
    public List<String> getContactLookupKeyList()
    {
      return this.contactLookupKey_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasCanonicalRelationship();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getCanonicalRelationship());
      }
      int j = 0;
      Iterator localIterator = getContactLookupKeyList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getContactLookupKeyList().size();
      this.cachedSize = k;
      return k;
    }
    
    public boolean hasCanonicalRelationship()
    {
      return this.hasCanonicalRelationship;
    }
    
    public final boolean isInitialized()
    {
      return true;
    }
    
    public RelationshipContactEntry mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setCanonicalRelationship(paramCodedInputStreamMicro.readString());
          break;
        }
        addContactLookupKey(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RelationshipContactEntry setCanonicalRelationship(String paramString)
    {
      this.hasCanonicalRelationship = true;
      this.canonicalRelationship_ = paramString;
      return this;
    }
    
    public RelationshipContactEntry setContactLookupKey(int paramInt, String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      this.contactLookupKey_.set(paramInt, paramString);
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasCanonicalRelationship()) {
        paramCodedOutputStreamMicro.writeString(1, getCanonicalRelationship());
      }
      Iterator localIterator = getContactLookupKeyList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(2, (String)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.RelationshipContactInfo
 * JD-Core Version:    0.7.0.1
 */