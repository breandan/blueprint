package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class ContextProtos
{
  public static final class ActionContext
    extends MessageMicro
  {
    private ActionV2Protos.ActionV2 action_ = null;
    private int cachedSize = -1;
    private boolean hasAction;
    
    public ActionV2Protos.ActionV2 getAction()
    {
      return this.action_;
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
      boolean bool = hasAction();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getAction());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAction()
    {
      return this.hasAction;
    }
    
    public ActionContext mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        ActionV2Protos.ActionV2 localActionV2 = new ActionV2Protos.ActionV2();
        paramCodedInputStreamMicro.readMessage(localActionV2);
        setAction(localActionV2);
      }
    }
    
    public ActionContext setAction(ActionV2Protos.ActionV2 paramActionV2)
    {
      if (paramActionV2 == null) {
        throw new NullPointerException();
      }
      this.hasAction = true;
      this.action_ = paramActionV2;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAction()) {
        paramCodedOutputStreamMicro.writeMessage(1, getAction());
      }
    }
  }
  
  public static final class Context
    extends MessageMicro
  {
    private ContextProtos.ActionContext actionContext_ = null;
    private int cachedSize = -1;
    private boolean hasActionContext;
    private boolean hasTextualContext;
    private String textualContext_ = "";
    
    public ContextProtos.ActionContext getActionContext()
    {
      return this.actionContext_;
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
      boolean bool = hasTextualContext();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTextualContext());
      }
      if (hasActionContext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getActionContext());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTextualContext()
    {
      return this.textualContext_;
    }
    
    public boolean hasActionContext()
    {
      return this.hasActionContext;
    }
    
    public boolean hasTextualContext()
    {
      return this.hasTextualContext;
    }
    
    public Context mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTextualContext(paramCodedInputStreamMicro.readString());
          break;
        }
        ContextProtos.ActionContext localActionContext = new ContextProtos.ActionContext();
        paramCodedInputStreamMicro.readMessage(localActionContext);
        setActionContext(localActionContext);
      }
    }
    
    public Context setActionContext(ContextProtos.ActionContext paramActionContext)
    {
      if (paramActionContext == null) {
        throw new NullPointerException();
      }
      this.hasActionContext = true;
      this.actionContext_ = paramActionContext;
      return this;
    }
    
    public Context setTextualContext(String paramString)
    {
      this.hasTextualContext = true;
      this.textualContext_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTextualContext()) {
        paramCodedOutputStreamMicro.writeString(1, getTextualContext());
      }
      if (hasActionContext()) {
        paramCodedOutputStreamMicro.writeMessage(2, getActionContext());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ContextProtos
 * JD-Core Version:    0.7.0.1
 */