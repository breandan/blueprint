package com.google.speech.speech.s3;

import com.google.audio.ears.proto.EarsService.EarsLookupRequest;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.audio.ears.proto.EarsService.EarsStreamRequest;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class SoundSearch
{
  public static final class SoundSearchInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLookupRequest;
    private boolean hasStreamRequest;
    private boolean hasTtsOutputEnabled;
    private EarsService.EarsLookupRequest lookupRequest_ = null;
    private EarsService.EarsStreamRequest streamRequest_ = null;
    private boolean ttsOutputEnabled_ = false;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EarsService.EarsLookupRequest getLookupRequest()
    {
      return this.lookupRequest_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLookupRequest();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getLookupRequest());
      }
      if (hasStreamRequest()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getStreamRequest());
      }
      if (hasTtsOutputEnabled()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getTtsOutputEnabled());
      }
      this.cachedSize = i;
      return i;
    }
    
    public EarsService.EarsStreamRequest getStreamRequest()
    {
      return this.streamRequest_;
    }
    
    public boolean getTtsOutputEnabled()
    {
      return this.ttsOutputEnabled_;
    }
    
    public boolean hasLookupRequest()
    {
      return this.hasLookupRequest;
    }
    
    public boolean hasStreamRequest()
    {
      return this.hasStreamRequest;
    }
    
    public boolean hasTtsOutputEnabled()
    {
      return this.hasTtsOutputEnabled;
    }
    
    public SoundSearchInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EarsService.EarsLookupRequest localEarsLookupRequest = new EarsService.EarsLookupRequest();
          paramCodedInputStreamMicro.readMessage(localEarsLookupRequest);
          setLookupRequest(localEarsLookupRequest);
          break;
        case 18: 
          EarsService.EarsStreamRequest localEarsStreamRequest = new EarsService.EarsStreamRequest();
          paramCodedInputStreamMicro.readMessage(localEarsStreamRequest);
          setStreamRequest(localEarsStreamRequest);
          break;
        }
        setTtsOutputEnabled(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public SoundSearchInfo setLookupRequest(EarsService.EarsLookupRequest paramEarsLookupRequest)
    {
      if (paramEarsLookupRequest == null) {
        throw new NullPointerException();
      }
      this.hasLookupRequest = true;
      this.lookupRequest_ = paramEarsLookupRequest;
      return this;
    }
    
    public SoundSearchInfo setStreamRequest(EarsService.EarsStreamRequest paramEarsStreamRequest)
    {
      if (paramEarsStreamRequest == null) {
        throw new NullPointerException();
      }
      this.hasStreamRequest = true;
      this.streamRequest_ = paramEarsStreamRequest;
      return this;
    }
    
    public SoundSearchInfo setTtsOutputEnabled(boolean paramBoolean)
    {
      this.hasTtsOutputEnabled = true;
      this.ttsOutputEnabled_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLookupRequest()) {
        paramCodedOutputStreamMicro.writeMessage(1, getLookupRequest());
      }
      if (hasStreamRequest()) {
        paramCodedOutputStreamMicro.writeMessage(2, getStreamRequest());
      }
      if (hasTtsOutputEnabled()) {
        paramCodedOutputStreamMicro.writeBool(3, getTtsOutputEnabled());
      }
    }
  }
  
  public static final class SoundSearchServiceEvent
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasResultsResponse;
    private EarsService.EarsResultsResponse resultsResponse_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EarsService.EarsResultsResponse getResultsResponse()
    {
      return this.resultsResponse_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasResultsResponse();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getResultsResponse());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasResultsResponse()
    {
      return this.hasResultsResponse;
    }
    
    public SoundSearchServiceEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        EarsService.EarsResultsResponse localEarsResultsResponse = new EarsService.EarsResultsResponse();
        paramCodedInputStreamMicro.readMessage(localEarsResultsResponse);
        setResultsResponse(localEarsResultsResponse);
      }
    }
    
    public SoundSearchServiceEvent setResultsResponse(EarsService.EarsResultsResponse paramEarsResultsResponse)
    {
      if (paramEarsResultsResponse == null) {
        throw new NullPointerException();
      }
      this.hasResultsResponse = true;
      this.resultsResponse_ = paramEarsResultsResponse;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasResultsResponse()) {
        paramCodedOutputStreamMicro.writeMessage(1, getResultsResponse());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.speech.s3.SoundSearch
 * JD-Core Version:    0.7.0.1
 */