package com.google.android.apps.sidekick;

import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class EntryProviderData
  extends MessageMicro
{
  private int cachedSize = -1;
  private Sidekick.EntryResponse entryResponse_ = null;
  private boolean hasEntryResponse;
  private boolean hasIncludesMoreCards;
  private boolean hasLastRefreshMillis;
  private boolean hasLocale;
  private boolean hasLocation;
  private boolean includesMoreCards_ = false;
  private long lastRefreshMillis_ = 0L;
  private String locale_ = "";
  private Sidekick.Location location_ = null;
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public Sidekick.EntryResponse getEntryResponse()
  {
    return this.entryResponse_;
  }
  
  public boolean getIncludesMoreCards()
  {
    return this.includesMoreCards_;
  }
  
  public long getLastRefreshMillis()
  {
    return this.lastRefreshMillis_;
  }
  
  public String getLocale()
  {
    return this.locale_;
  }
  
  public Sidekick.Location getLocation()
  {
    return this.location_;
  }
  
  public int getSerializedSize()
  {
    boolean bool = hasLastRefreshMillis();
    int i = 0;
    if (bool) {
      i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getLastRefreshMillis());
    }
    if (hasLocation()) {
      i += CodedOutputStreamMicro.computeMessageSize(2, getLocation());
    }
    if (hasEntryResponse()) {
      i += CodedOutputStreamMicro.computeMessageSize(3, getEntryResponse());
    }
    if (hasIncludesMoreCards()) {
      i += CodedOutputStreamMicro.computeBoolSize(4, getIncludesMoreCards());
    }
    if (hasLocale()) {
      i += CodedOutputStreamMicro.computeStringSize(5, getLocale());
    }
    this.cachedSize = i;
    return i;
  }
  
  public boolean hasEntryResponse()
  {
    return this.hasEntryResponse;
  }
  
  public boolean hasIncludesMoreCards()
  {
    return this.hasIncludesMoreCards;
  }
  
  public boolean hasLastRefreshMillis()
  {
    return this.hasLastRefreshMillis;
  }
  
  public boolean hasLocale()
  {
    return this.hasLocale;
  }
  
  public boolean hasLocation()
  {
    return this.hasLocation;
  }
  
  public EntryProviderData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setLastRefreshMillis(paramCodedInputStreamMicro.readInt64());
        break;
      case 18: 
        Sidekick.Location localLocation = new Sidekick.Location();
        paramCodedInputStreamMicro.readMessage(localLocation);
        setLocation(localLocation);
        break;
      case 26: 
        Sidekick.EntryResponse localEntryResponse = new Sidekick.EntryResponse();
        paramCodedInputStreamMicro.readMessage(localEntryResponse);
        setEntryResponse(localEntryResponse);
        break;
      case 32: 
        setIncludesMoreCards(paramCodedInputStreamMicro.readBool());
        break;
      }
      setLocale(paramCodedInputStreamMicro.readString());
    }
  }
  
  public EntryProviderData setEntryResponse(Sidekick.EntryResponse paramEntryResponse)
  {
    if (paramEntryResponse == null) {
      throw new NullPointerException();
    }
    this.hasEntryResponse = true;
    this.entryResponse_ = paramEntryResponse;
    return this;
  }
  
  public EntryProviderData setIncludesMoreCards(boolean paramBoolean)
  {
    this.hasIncludesMoreCards = true;
    this.includesMoreCards_ = paramBoolean;
    return this;
  }
  
  public EntryProviderData setLastRefreshMillis(long paramLong)
  {
    this.hasLastRefreshMillis = true;
    this.lastRefreshMillis_ = paramLong;
    return this;
  }
  
  public EntryProviderData setLocale(String paramString)
  {
    this.hasLocale = true;
    this.locale_ = paramString;
    return this;
  }
  
  public EntryProviderData setLocation(Sidekick.Location paramLocation)
  {
    if (paramLocation == null) {
      throw new NullPointerException();
    }
    this.hasLocation = true;
    this.location_ = paramLocation;
    return this;
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    if (hasLastRefreshMillis()) {
      paramCodedOutputStreamMicro.writeInt64(1, getLastRefreshMillis());
    }
    if (hasLocation()) {
      paramCodedOutputStreamMicro.writeMessage(2, getLocation());
    }
    if (hasEntryResponse()) {
      paramCodedOutputStreamMicro.writeMessage(3, getEntryResponse());
    }
    if (hasIncludesMoreCards()) {
      paramCodedOutputStreamMicro.writeBool(4, getIncludesMoreCards());
    }
    if (hasLocale()) {
      paramCodedOutputStreamMicro.writeString(5, getLocale());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.EntryProviderData
 * JD-Core Version:    0.7.0.1
 */