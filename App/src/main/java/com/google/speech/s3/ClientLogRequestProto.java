package com.google.speech.s3;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.logs.VoicesearchClientLogProto.VoiceSearchClientLog;
import java.io.IOException;

public final class ClientLogRequestProto
{
  public static final class ClientLogRequest
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasVoiceSearch;
    private VoicesearchClientLogProto.VoiceSearchClientLog voiceSearch_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasVoiceSearch();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getVoiceSearch());
      }
      this.cachedSize = i;
      return i;
    }
    
    public VoicesearchClientLogProto.VoiceSearchClientLog getVoiceSearch()
    {
      return this.voiceSearch_;
    }
    
    public boolean hasVoiceSearch()
    {
      return this.hasVoiceSearch;
    }
    
    public ClientLogRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        VoicesearchClientLogProto.VoiceSearchClientLog localVoiceSearchClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
        paramCodedInputStreamMicro.readMessage(localVoiceSearchClientLog);
        setVoiceSearch(localVoiceSearchClientLog);
      }
    }
    
    public ClientLogRequest setVoiceSearch(VoicesearchClientLogProto.VoiceSearchClientLog paramVoiceSearchClientLog)
    {
      if (paramVoiceSearchClientLog == null) {
        throw new NullPointerException();
      }
      this.hasVoiceSearch = true;
      this.voiceSearch_ = paramVoiceSearchClientLog;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasVoiceSearch()) {
        paramCodedOutputStreamMicro.writeMessage(1, getVoiceSearch());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.ClientLogRequestProto
 * JD-Core Version:    0.7.0.1
 */