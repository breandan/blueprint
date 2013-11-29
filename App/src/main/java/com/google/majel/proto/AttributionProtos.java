package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AttributionProtos
{
  public static final class Attribution
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String displayText_ = "";
    private boolean hasDisplayText;
    private boolean hasPageDomain;
    private boolean hasPageTitle;
    private boolean hasPageUrl;
    private String pageDomain_ = "";
    private String pageTitle_ = "";
    private String pageUrl_ = "";
    private List<String> snippet_ = Collections.emptyList();
    
    public Attribution addSnippet(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.snippet_.isEmpty()) {
        this.snippet_ = new ArrayList();
      }
      this.snippet_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDisplayText()
    {
      return this.displayText_;
    }
    
    public String getPageDomain()
    {
      return this.pageDomain_;
    }
    
    public String getPageTitle()
    {
      return this.pageTitle_;
    }
    
    public String getPageUrl()
    {
      return this.pageUrl_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasPageUrl();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPageUrl());
      }
      if (hasPageTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getPageTitle());
      }
      int j = 0;
      Iterator localIterator = getSnippetList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getSnippetList().size();
      if (hasPageDomain()) {
        k += CodedOutputStreamMicro.computeStringSize(4, getPageDomain());
      }
      if (hasDisplayText()) {
        k += CodedOutputStreamMicro.computeStringSize(5, getDisplayText());
      }
      this.cachedSize = k;
      return k;
    }
    
    public List<String> getSnippetList()
    {
      return this.snippet_;
    }
    
    public boolean hasDisplayText()
    {
      return this.hasDisplayText;
    }
    
    public boolean hasPageDomain()
    {
      return this.hasPageDomain;
    }
    
    public boolean hasPageTitle()
    {
      return this.hasPageTitle;
    }
    
    public boolean hasPageUrl()
    {
      return this.hasPageUrl;
    }
    
    public Attribution mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setPageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setPageTitle(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          addSnippet(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setPageDomain(paramCodedInputStreamMicro.readString());
          break;
        }
        setDisplayText(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Attribution setDisplayText(String paramString)
    {
      this.hasDisplayText = true;
      this.displayText_ = paramString;
      return this;
    }
    
    public Attribution setPageDomain(String paramString)
    {
      this.hasPageDomain = true;
      this.pageDomain_ = paramString;
      return this;
    }
    
    public Attribution setPageTitle(String paramString)
    {
      this.hasPageTitle = true;
      this.pageTitle_ = paramString;
      return this;
    }
    
    public Attribution setPageUrl(String paramString)
    {
      this.hasPageUrl = true;
      this.pageUrl_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasPageUrl()) {
        paramCodedOutputStreamMicro.writeString(1, getPageUrl());
      }
      if (hasPageTitle()) {
        paramCodedOutputStreamMicro.writeString(2, getPageTitle());
      }
      Iterator localIterator = getSnippetList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(3, (String)localIterator.next());
      }
      if (hasPageDomain()) {
        paramCodedOutputStreamMicro.writeString(4, getPageDomain());
      }
      if (hasDisplayText()) {
        paramCodedOutputStreamMicro.writeString(5, getDisplayText());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.AttributionProtos
 * JD-Core Version:    0.7.0.1
 */