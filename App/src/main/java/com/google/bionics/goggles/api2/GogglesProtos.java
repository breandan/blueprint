package com.google.bionics.goggles.api2;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GogglesProtos
{
  public static final class GogglesClientLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<GogglesProtos.ClientLogEvent> events_ = Collections.emptyList();
    private boolean hasSessionId;
    private String sessionId_ = "";
    
    public GogglesClientLog addEvents(GogglesProtos.ClientLogEvent paramClientLogEvent)
    {
      if (paramClientLogEvent == null) {
        throw new NullPointerException();
      }
      if (this.events_.isEmpty()) {
        this.events_ = new ArrayList();
      }
      this.events_.add(paramClientLogEvent);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<GogglesProtos.ClientLogEvent> getEventsList()
    {
      return this.events_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSessionId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSessionId());
      }
      Iterator localIterator = getEventsList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (GogglesProtos.ClientLogEvent)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSessionId()
    {
      return this.sessionId_;
    }
    
    public boolean hasSessionId()
    {
      return this.hasSessionId;
    }
    
    public GogglesClientLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSessionId(paramCodedInputStreamMicro.readString());
          break;
        }
        GogglesProtos.ClientLogEvent localClientLogEvent = new GogglesProtos.ClientLogEvent();
        paramCodedInputStreamMicro.readMessage(localClientLogEvent);
        addEvents(localClientLogEvent);
      }
    }
    
    public GogglesClientLog setSessionId(String paramString)
    {
      this.hasSessionId = true;
      this.sessionId_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSessionId()) {
        paramCodedOutputStreamMicro.writeString(1, getSessionId());
      }
      Iterator localIterator = getEventsList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (GogglesProtos.ClientLogEvent)localIterator.next());
      }
    }
  }
  
  public static final class GogglesStreamRequest
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasImage;
    private boolean hasImportantPayload;
    private boolean hasPose;
    private boolean hasSequenceNumber;
    private boolean hasSessionOptions;
    private boolean hasText;
    private GogglesProtos.Image image_ = null;
    private boolean importantPayload_ = false;
    private GogglesProtos.Pose pose_ = null;
    private int sequenceNumber_ = 0;
    private GogglesProtos.SessionOptions sessionOptions_ = null;
    private String text_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public GogglesProtos.Image getImage()
    {
      return this.image_;
    }
    
    public boolean getImportantPayload()
    {
      return this.importantPayload_;
    }
    
    public GogglesProtos.Pose getPose()
    {
      return this.pose_;
    }
    
    public int getSequenceNumber()
    {
      return this.sequenceNumber_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSequenceNumber();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getSequenceNumber());
      }
      if (hasSessionOptions()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getSessionOptions());
      }
      if (hasImage()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getImage());
      }
      if (hasPose()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getPose());
      }
      if (hasText()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getText());
      }
      if (hasImportantPayload()) {
        i += CodedOutputStreamMicro.computeBoolSize(17, getImportantPayload());
      }
      this.cachedSize = i;
      return i;
    }
    
    public GogglesProtos.SessionOptions getSessionOptions()
    {
      return this.sessionOptions_;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public boolean hasImage()
    {
      return this.hasImage;
    }
    
    public boolean hasImportantPayload()
    {
      return this.hasImportantPayload;
    }
    
    public boolean hasPose()
    {
      return this.hasPose;
    }
    
    public boolean hasSequenceNumber()
    {
      return this.hasSequenceNumber;
    }
    
    public boolean hasSessionOptions()
    {
      return this.hasSessionOptions;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public GogglesStreamRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSequenceNumber(paramCodedInputStreamMicro.readInt32());
          break;
        case 18: 
          GogglesProtos.SessionOptions localSessionOptions = new GogglesProtos.SessionOptions();
          paramCodedInputStreamMicro.readMessage(localSessionOptions);
          setSessionOptions(localSessionOptions);
          break;
        case 26: 
          GogglesProtos.Image localImage = new GogglesProtos.Image();
          paramCodedInputStreamMicro.readMessage(localImage);
          setImage(localImage);
          break;
        case 34: 
          GogglesProtos.Pose localPose = new GogglesProtos.Pose();
          paramCodedInputStreamMicro.readMessage(localPose);
          setPose(localPose);
          break;
        case 130: 
          setText(paramCodedInputStreamMicro.readString());
          break;
        }
        setImportantPayload(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public GogglesStreamRequest setImage(GogglesProtos.Image paramImage)
    {
      if (paramImage == null) {
        throw new NullPointerException();
      }
      this.hasImage = true;
      this.image_ = paramImage;
      return this;
    }
    
    public GogglesStreamRequest setImportantPayload(boolean paramBoolean)
    {
      this.hasImportantPayload = true;
      this.importantPayload_ = paramBoolean;
      return this;
    }
    
    public GogglesStreamRequest setPose(GogglesProtos.Pose paramPose)
    {
      if (paramPose == null) {
        throw new NullPointerException();
      }
      this.hasPose = true;
      this.pose_ = paramPose;
      return this;
    }
    
    public GogglesStreamRequest setSequenceNumber(int paramInt)
    {
      this.hasSequenceNumber = true;
      this.sequenceNumber_ = paramInt;
      return this;
    }
    
    public GogglesStreamRequest setSessionOptions(GogglesProtos.SessionOptions paramSessionOptions)
    {
      if (paramSessionOptions == null) {
        throw new NullPointerException();
      }
      this.hasSessionOptions = true;
      this.sessionOptions_ = paramSessionOptions;
      return this;
    }
    
    public GogglesStreamRequest setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSequenceNumber()) {
        paramCodedOutputStreamMicro.writeInt32(1, getSequenceNumber());
      }
      if (hasSessionOptions()) {
        paramCodedOutputStreamMicro.writeMessage(2, getSessionOptions());
      }
      if (hasImage()) {
        paramCodedOutputStreamMicro.writeMessage(3, getImage());
      }
      if (hasPose()) {
        paramCodedOutputStreamMicro.writeMessage(4, getPose());
      }
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(16, getText());
      }
      if (hasImportantPayload()) {
        paramCodedOutputStreamMicro.writeBool(17, getImportantPayload());
      }
    }
  }
  
  public static final class GogglesStreamResponse
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasHighestSequenceNumberComplete;
    private boolean hasHighestSequenceNumberReceived;
    private boolean hasResultSetNumber;
    private boolean hasSessionMetadata;
    private int highestSequenceNumberComplete_ = -1;
    private int highestSequenceNumberReceived_ = -1;
    private int resultSetNumber_ = 0;
    private List<GogglesProtos.Result> results_ = Collections.emptyList();
    private GogglesProtos.SessionMetadata sessionMetadata_ = null;
    
    public GogglesStreamResponse addResults(GogglesProtos.Result paramResult)
    {
      if (paramResult == null) {
        throw new NullPointerException();
      }
      if (this.results_.isEmpty()) {
        this.results_ = new ArrayList();
      }
      this.results_.add(paramResult);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getHighestSequenceNumberComplete()
    {
      return this.highestSequenceNumberComplete_;
    }
    
    public int getHighestSequenceNumberReceived()
    {
      return this.highestSequenceNumberReceived_;
    }
    
    public int getResultSetNumber()
    {
      return this.resultSetNumber_;
    }
    
    public List<GogglesProtos.Result> getResultsList()
    {
      return this.results_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasResultSetNumber();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getResultSetNumber());
      }
      if (hasSessionMetadata()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getSessionMetadata());
      }
      Iterator localIterator = getResultsList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (GogglesProtos.Result)localIterator.next());
      }
      if (hasHighestSequenceNumberReceived()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getHighestSequenceNumberReceived());
      }
      if (hasHighestSequenceNumberComplete()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getHighestSequenceNumberComplete());
      }
      this.cachedSize = i;
      return i;
    }
    
    public GogglesProtos.SessionMetadata getSessionMetadata()
    {
      return this.sessionMetadata_;
    }
    
    public boolean hasHighestSequenceNumberComplete()
    {
      return this.hasHighestSequenceNumberComplete;
    }
    
    public boolean hasHighestSequenceNumberReceived()
    {
      return this.hasHighestSequenceNumberReceived;
    }
    
    public boolean hasResultSetNumber()
    {
      return this.hasResultSetNumber;
    }
    
    public boolean hasSessionMetadata()
    {
      return this.hasSessionMetadata;
    }
    
    public GogglesStreamResponse mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setResultSetNumber(paramCodedInputStreamMicro.readInt32());
          break;
        case 18: 
          GogglesProtos.SessionMetadata localSessionMetadata = new GogglesProtos.SessionMetadata();
          paramCodedInputStreamMicro.readMessage(localSessionMetadata);
          setSessionMetadata(localSessionMetadata);
          break;
        case 26: 
          GogglesProtos.Result localResult = new GogglesProtos.Result();
          paramCodedInputStreamMicro.readMessage(localResult);
          addResults(localResult);
          break;
        case 32: 
          setHighestSequenceNumberReceived(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setHighestSequenceNumberComplete(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public GogglesStreamResponse setHighestSequenceNumberComplete(int paramInt)
    {
      this.hasHighestSequenceNumberComplete = true;
      this.highestSequenceNumberComplete_ = paramInt;
      return this;
    }
    
    public GogglesStreamResponse setHighestSequenceNumberReceived(int paramInt)
    {
      this.hasHighestSequenceNumberReceived = true;
      this.highestSequenceNumberReceived_ = paramInt;
      return this;
    }
    
    public GogglesStreamResponse setResultSetNumber(int paramInt)
    {
      this.hasResultSetNumber = true;
      this.resultSetNumber_ = paramInt;
      return this;
    }
    
    public GogglesStreamResponse setSessionMetadata(GogglesProtos.SessionMetadata paramSessionMetadata)
    {
      if (paramSessionMetadata == null) {
        throw new NullPointerException();
      }
      this.hasSessionMetadata = true;
      this.sessionMetadata_ = paramSessionMetadata;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasResultSetNumber()) {
        paramCodedOutputStreamMicro.writeInt32(1, getResultSetNumber());
      }
      if (hasSessionMetadata()) {
        paramCodedOutputStreamMicro.writeMessage(2, getSessionMetadata());
      }
      Iterator localIterator = getResultsList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (GogglesProtos.Result)localIterator.next());
      }
      if (hasHighestSequenceNumberReceived()) {
        paramCodedOutputStreamMicro.writeInt32(4, getHighestSequenceNumberReceived());
      }
      if (hasHighestSequenceNumberComplete()) {
        paramCodedOutputStreamMicro.writeInt32(5, getHighestSequenceNumberComplete());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.bionics.goggles.api2.GogglesProtos
 * JD-Core Version:    0.7.0.1
 */