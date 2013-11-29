package com.google.android.apps.sidekick;

import com.google.geo.sidekick.Sidekick.FetchStaticEntitiesResponse;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class StaticEntitiesData
  extends MessageMicro
{
  private int cachedSize = -1;
  private boolean hasLastRefreshMillis;
  private boolean hasStaticEntities;
  private long lastRefreshMillis_ = 0L;
  private Sidekick.FetchStaticEntitiesResponse staticEntities_ = null;
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public long getLastRefreshMillis()
  {
    return this.lastRefreshMillis_;
  }
  
  public int getSerializedSize()
  {
    boolean bool = hasLastRefreshMillis();
    int i = 0;
    if (bool) {
      i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getLastRefreshMillis());
    }
    if (hasStaticEntities()) {
      i += CodedOutputStreamMicro.computeMessageSize(2, getStaticEntities());
    }
    this.cachedSize = i;
    return i;
  }
  
  public Sidekick.FetchStaticEntitiesResponse getStaticEntities()
  {
    return this.staticEntities_;
  }
  
  public boolean hasLastRefreshMillis()
  {
    return this.hasLastRefreshMillis;
  }
  
  public boolean hasStaticEntities()
  {
    return this.hasStaticEntities;
  }
  
  public StaticEntitiesData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
      }
      Sidekick.FetchStaticEntitiesResponse localFetchStaticEntitiesResponse = new Sidekick.FetchStaticEntitiesResponse();
      paramCodedInputStreamMicro.readMessage(localFetchStaticEntitiesResponse);
      setStaticEntities(localFetchStaticEntitiesResponse);
    }
  }
  
  public StaticEntitiesData setLastRefreshMillis(long paramLong)
  {
    this.hasLastRefreshMillis = true;
    this.lastRefreshMillis_ = paramLong;
    return this;
  }
  
  public StaticEntitiesData setStaticEntities(Sidekick.FetchStaticEntitiesResponse paramFetchStaticEntitiesResponse)
  {
    if (paramFetchStaticEntitiesResponse == null) {
      throw new NullPointerException();
    }
    this.hasStaticEntities = true;
    this.staticEntities_ = paramFetchStaticEntitiesResponse;
    return this;
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    if (hasLastRefreshMillis()) {
      paramCodedOutputStreamMicro.writeInt64(1, getLastRefreshMillis());
    }
    if (hasStaticEntities()) {
      paramCodedOutputStreamMicro.writeMessage(2, getStaticEntities());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.StaticEntitiesData
 * JD-Core Version:    0.7.0.1
 */