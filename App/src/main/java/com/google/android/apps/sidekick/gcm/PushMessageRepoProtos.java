package com.google.android.apps.sidekick.gcm;

import com.google.geo.sidekick.Sidekick.Interest;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PushMessageRepoProtos
{
  public static final class AccumulatedRefreshState
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<PushMessageRepoProtos.TargetDisplayUpdate> targetDisplayUpdate_ = Collections.emptyList();
    
    public AccumulatedRefreshState addTargetDisplayUpdate(PushMessageRepoProtos.TargetDisplayUpdate paramTargetDisplayUpdate)
    {
      if (paramTargetDisplayUpdate == null) {
        throw new NullPointerException();
      }
      if (this.targetDisplayUpdate_.isEmpty()) {
        this.targetDisplayUpdate_ = new ArrayList();
      }
      this.targetDisplayUpdate_.add(paramTargetDisplayUpdate);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getTargetDisplayUpdateList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (PushMessageRepoProtos.TargetDisplayUpdate)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public PushMessageRepoProtos.TargetDisplayUpdate getTargetDisplayUpdate(int paramInt)
    {
      return (PushMessageRepoProtos.TargetDisplayUpdate)this.targetDisplayUpdate_.get(paramInt);
    }
    
    public int getTargetDisplayUpdateCount()
    {
      return this.targetDisplayUpdate_.size();
    }
    
    public List<PushMessageRepoProtos.TargetDisplayUpdate> getTargetDisplayUpdateList()
    {
      return this.targetDisplayUpdate_;
    }
    
    public AccumulatedRefreshState mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        PushMessageRepoProtos.TargetDisplayUpdate localTargetDisplayUpdate = new PushMessageRepoProtos.TargetDisplayUpdate();
        paramCodedInputStreamMicro.readMessage(localTargetDisplayUpdate);
        addTargetDisplayUpdate(localTargetDisplayUpdate);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getTargetDisplayUpdateList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (PushMessageRepoProtos.TargetDisplayUpdate)localIterator.next());
      }
    }
  }
  
  public static final class TargetDisplayUpdate
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasInterest;
    private boolean hasTargetDisplay;
    private Sidekick.Interest interest_ = null;
    private int targetDisplay_ = 1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Sidekick.Interest getInterest()
    {
      return this.interest_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTargetDisplay();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getTargetDisplay());
      }
      if (hasInterest()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getInterest());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getTargetDisplay()
    {
      return this.targetDisplay_;
    }
    
    public boolean hasInterest()
    {
      return this.hasInterest;
    }
    
    public boolean hasTargetDisplay()
    {
      return this.hasTargetDisplay;
    }
    
    public TargetDisplayUpdate mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTargetDisplay(paramCodedInputStreamMicro.readInt32());
          break;
        }
        Sidekick.Interest localInterest = new Sidekick.Interest();
        paramCodedInputStreamMicro.readMessage(localInterest);
        setInterest(localInterest);
      }
    }
    
    public TargetDisplayUpdate setInterest(Sidekick.Interest paramInterest)
    {
      if (paramInterest == null) {
        throw new NullPointerException();
      }
      this.hasInterest = true;
      this.interest_ = paramInterest;
      return this;
    }
    
    public TargetDisplayUpdate setTargetDisplay(int paramInt)
    {
      this.hasTargetDisplay = true;
      this.targetDisplay_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTargetDisplay()) {
        paramCodedOutputStreamMicro.writeInt32(1, getTargetDisplay());
      }
      if (hasInterest()) {
        paramCodedOutputStreamMicro.writeMessage(2, getInterest());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.gcm.PushMessageRepoProtos
 * JD-Core Version:    0.7.0.1
 */