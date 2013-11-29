package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MajelProtos
{
  public static final class MajelResponse
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String debug_ = "";
    private boolean hasDebug;
    private boolean hasQueryId;
    private List<PeanutProtos.Peanut> peanut_ = Collections.emptyList();
    private String queryId_ = "";
    private List<CookieProtos.MajelCookie> setCookie_ = Collections.emptyList();
    
    public MajelResponse addPeanut(PeanutProtos.Peanut paramPeanut)
    {
      if (paramPeanut == null) {
        throw new NullPointerException();
      }
      if (this.peanut_.isEmpty()) {
        this.peanut_ = new ArrayList();
      }
      this.peanut_.add(paramPeanut);
      return this;
    }
    
    public MajelResponse addSetCookie(CookieProtos.MajelCookie paramMajelCookie)
    {
      if (paramMajelCookie == null) {
        throw new NullPointerException();
      }
      if (this.setCookie_.isEmpty()) {
        this.setCookie_ = new ArrayList();
      }
      this.setCookie_.add(paramMajelCookie);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDebug()
    {
      return this.debug_;
    }
    
    public PeanutProtos.Peanut getPeanut(int paramInt)
    {
      return (PeanutProtos.Peanut)this.peanut_.get(paramInt);
    }
    
    public int getPeanutCount()
    {
      return this.peanut_.size();
    }
    
    public List<PeanutProtos.Peanut> getPeanutList()
    {
      return this.peanut_;
    }
    
    public String getQueryId()
    {
      return this.queryId_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getPeanutList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (PeanutProtos.Peanut)localIterator1.next());
      }
      if (hasDebug()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getDebug());
      }
      if (hasQueryId()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getQueryId());
      }
      Iterator localIterator2 = getSetCookieList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (CookieProtos.MajelCookie)localIterator2.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public List<CookieProtos.MajelCookie> getSetCookieList()
    {
      return this.setCookie_;
    }
    
    public boolean hasDebug()
    {
      return this.hasDebug;
    }
    
    public boolean hasQueryId()
    {
      return this.hasQueryId;
    }
    
    public MajelResponse mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          PeanutProtos.Peanut localPeanut = new PeanutProtos.Peanut();
          paramCodedInputStreamMicro.readMessage(localPeanut);
          addPeanut(localPeanut);
          break;
        case 18: 
          setDebug(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setQueryId(paramCodedInputStreamMicro.readString());
          break;
        }
        CookieProtos.MajelCookie localMajelCookie = new CookieProtos.MajelCookie();
        paramCodedInputStreamMicro.readMessage(localMajelCookie);
        addSetCookie(localMajelCookie);
      }
    }
    
    public MajelResponse setDebug(String paramString)
    {
      this.hasDebug = true;
      this.debug_ = paramString;
      return this;
    }
    
    public MajelResponse setQueryId(String paramString)
    {
      this.hasQueryId = true;
      this.queryId_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getPeanutList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (PeanutProtos.Peanut)localIterator1.next());
      }
      if (hasDebug()) {
        paramCodedOutputStreamMicro.writeString(2, getDebug());
      }
      if (hasQueryId()) {
        paramCodedOutputStreamMicro.writeString(3, getQueryId());
      }
      Iterator localIterator2 = getSetCookieList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (CookieProtos.MajelCookie)localIterator2.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.MajelProtos
 * JD-Core Version:    0.7.0.1
 */