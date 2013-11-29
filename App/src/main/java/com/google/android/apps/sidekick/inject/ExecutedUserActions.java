package com.google.android.apps.sidekick.inject;

import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ExecutedUserActions
  extends MessageMicro
{
  private int cachedSize = -1;
  private List<Sidekick.ExecutedUserAction> executedUserAction_ = Collections.emptyList();
  
  public ExecutedUserActions addExecutedUserAction(Sidekick.ExecutedUserAction paramExecutedUserAction)
  {
    if (paramExecutedUserAction == null) {
      throw new NullPointerException();
    }
    if (this.executedUserAction_.isEmpty()) {
      this.executedUserAction_ = new ArrayList();
    }
    this.executedUserAction_.add(paramExecutedUserAction);
    return this;
  }
  
  public final ExecutedUserActions clear()
  {
    clearExecutedUserAction();
    this.cachedSize = -1;
    return this;
  }
  
  public ExecutedUserActions clearExecutedUserAction()
  {
    this.executedUserAction_ = Collections.emptyList();
    return this;
  }
  
  public int getCachedSize()
  {
    if (this.cachedSize < 0) {
      getSerializedSize();
    }
    return this.cachedSize;
  }
  
  public int getExecutedUserActionCount()
  {
    return this.executedUserAction_.size();
  }
  
  public List<Sidekick.ExecutedUserAction> getExecutedUserActionList()
  {
    return this.executedUserAction_;
  }
  
  public int getSerializedSize()
  {
    int i = 0;
    Iterator localIterator = getExecutedUserActionList().iterator();
    while (localIterator.hasNext()) {
      i += CodedOutputStreamMicro.computeMessageSize(1, (Sidekick.ExecutedUserAction)localIterator.next());
    }
    this.cachedSize = i;
    return i;
  }
  
  public ExecutedUserActions mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
      Sidekick.ExecutedUserAction localExecutedUserAction = new Sidekick.ExecutedUserAction();
      paramCodedInputStreamMicro.readMessage(localExecutedUserAction);
      addExecutedUserAction(localExecutedUserAction);
    }
  }
  
  public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
    throws IOException
  {
    Iterator localIterator = getExecutedUserActionList().iterator();
    while (localIterator.hasNext()) {
      paramCodedOutputStreamMicro.writeMessage(1, (Sidekick.ExecutedUserAction)localIterator.next());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.inject.ExecutedUserActions
 * JD-Core Version:    0.7.0.1
 */