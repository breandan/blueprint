package com.google.bionics.goggles.api2;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GogglesStructuredResponseProtos
{
  public static final class GogglesGenericResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String fifeImageUrl_ = "";
    private boolean hasFifeImageUrl;
    private boolean hasSubtitle;
    private boolean hasTitle;
    private List<GogglesStructuredResponseProtos.GogglesGenericResult.Link> link_ = Collections.emptyList();
    private String subtitle_ = "";
    private String title_ = "";
    
    public GogglesGenericResult addLink(GogglesStructuredResponseProtos.GogglesGenericResult.Link paramLink)
    {
      if (paramLink == null) {
        throw new NullPointerException();
      }
      if (this.link_.isEmpty()) {
        this.link_ = new ArrayList();
      }
      this.link_.add(paramLink);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getFifeImageUrl()
    {
      return this.fifeImageUrl_;
    }
    
    public List<GogglesStructuredResponseProtos.GogglesGenericResult.Link> getLinkList()
    {
      return this.link_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTitle();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTitle());
      }
      if (hasFifeImageUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getFifeImageUrl());
      }
      if (hasSubtitle()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getSubtitle());
      }
      Iterator localIterator = getLinkList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, (GogglesStructuredResponseProtos.GogglesGenericResult.Link)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSubtitle()
    {
      return this.subtitle_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public boolean hasFifeImageUrl()
    {
      return this.hasFifeImageUrl;
    }
    
    public boolean hasSubtitle()
    {
      return this.hasSubtitle;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public GogglesGenericResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setFifeImageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setSubtitle(paramCodedInputStreamMicro.readString());
          break;
        }
        GogglesStructuredResponseProtos.GogglesGenericResult.Link localLink = new GogglesStructuredResponseProtos.GogglesGenericResult.Link();
        paramCodedInputStreamMicro.readMessage(localLink);
        addLink(localLink);
      }
    }
    
    public GogglesGenericResult setFifeImageUrl(String paramString)
    {
      this.hasFifeImageUrl = true;
      this.fifeImageUrl_ = paramString;
      return this;
    }
    
    public GogglesGenericResult setSubtitle(String paramString)
    {
      this.hasSubtitle = true;
      this.subtitle_ = paramString;
      return this;
    }
    
    public GogglesGenericResult setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(1, getTitle());
      }
      if (hasFifeImageUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getFifeImageUrl());
      }
      if (hasSubtitle()) {
        paramCodedOutputStreamMicro.writeString(3, getSubtitle());
      }
      Iterator localIterator = getLinkList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(7, (GogglesStructuredResponseProtos.GogglesGenericResult.Link)localIterator.next());
      }
    }
  }
  
  public static final class RecognizedContact
    extends MessageMicro
  {
    private List<GogglesStructuredResponseProtos.RecognizedContact.PostalAddress> address_ = Collections.emptyList();
    private int cachedSize = -1;
    private String company_ = "";
    private List<GogglesStructuredResponseProtos.RecognizedContact.Email> email_ = Collections.emptyList();
    private boolean hasCompany;
    private boolean hasName;
    private boolean hasTitle;
    private GogglesStructuredResponseProtos.RecognizedContact.Name name_ = null;
    private List<GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber> phoneNumber_ = Collections.emptyList();
    private String title_ = "";
    private List<String> url_ = Collections.emptyList();
    
    public RecognizedContact addAddress(GogglesStructuredResponseProtos.RecognizedContact.PostalAddress paramPostalAddress)
    {
      if (paramPostalAddress == null) {
        throw new NullPointerException();
      }
      if (this.address_.isEmpty()) {
        this.address_ = new ArrayList();
      }
      this.address_.add(paramPostalAddress);
      return this;
    }
    
    public RecognizedContact addEmail(GogglesStructuredResponseProtos.RecognizedContact.Email paramEmail)
    {
      if (paramEmail == null) {
        throw new NullPointerException();
      }
      if (this.email_.isEmpty()) {
        this.email_ = new ArrayList();
      }
      this.email_.add(paramEmail);
      return this;
    }
    
    public RecognizedContact addPhoneNumber(GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber paramPhoneNumber)
    {
      if (paramPhoneNumber == null) {
        throw new NullPointerException();
      }
      if (this.phoneNumber_.isEmpty()) {
        this.phoneNumber_ = new ArrayList();
      }
      this.phoneNumber_.add(paramPhoneNumber);
      return this;
    }
    
    public RecognizedContact addUrl(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.url_.isEmpty()) {
        this.url_ = new ArrayList();
      }
      this.url_.add(paramString);
      return this;
    }
    
    public List<GogglesStructuredResponseProtos.RecognizedContact.PostalAddress> getAddressList()
    {
      return this.address_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCompany()
    {
      return this.company_;
    }
    
    public List<GogglesStructuredResponseProtos.RecognizedContact.Email> getEmailList()
    {
      return this.email_;
    }
    
    public GogglesStructuredResponseProtos.RecognizedContact.Name getName()
    {
      return this.name_;
    }
    
    public List<GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber> getPhoneNumberList()
    {
      return this.phoneNumber_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getName());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getTitle());
      }
      if (hasCompany()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getCompany());
      }
      int j = 0;
      Iterator localIterator1 = getUrlList().iterator();
      while (localIterator1.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator1.next());
      }
      int k = i + j + 1 * getUrlList().size();
      Iterator localIterator2 = getEmailList().iterator();
      while (localIterator2.hasNext()) {
        k += CodedOutputStreamMicro.computeMessageSize(5, (GogglesStructuredResponseProtos.RecognizedContact.Email)localIterator2.next());
      }
      Iterator localIterator3 = getAddressList().iterator();
      while (localIterator3.hasNext()) {
        k += CodedOutputStreamMicro.computeMessageSize(6, (GogglesStructuredResponseProtos.RecognizedContact.PostalAddress)localIterator3.next());
      }
      Iterator localIterator4 = getPhoneNumberList().iterator();
      while (localIterator4.hasNext()) {
        k += CodedOutputStreamMicro.computeMessageSize(7, (GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber)localIterator4.next());
      }
      this.cachedSize = k;
      return k;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public List<String> getUrlList()
    {
      return this.url_;
    }
    
    public boolean hasCompany()
    {
      return this.hasCompany;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public RecognizedContact mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          GogglesStructuredResponseProtos.RecognizedContact.Name localName = new GogglesStructuredResponseProtos.RecognizedContact.Name();
          paramCodedInputStreamMicro.readMessage(localName);
          setName(localName);
          break;
        case 18: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setCompany(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          addUrl(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          GogglesStructuredResponseProtos.RecognizedContact.Email localEmail = new GogglesStructuredResponseProtos.RecognizedContact.Email();
          paramCodedInputStreamMicro.readMessage(localEmail);
          addEmail(localEmail);
          break;
        case 50: 
          GogglesStructuredResponseProtos.RecognizedContact.PostalAddress localPostalAddress = new GogglesStructuredResponseProtos.RecognizedContact.PostalAddress();
          paramCodedInputStreamMicro.readMessage(localPostalAddress);
          addAddress(localPostalAddress);
          break;
        }
        GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber localPhoneNumber = new GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber();
        paramCodedInputStreamMicro.readMessage(localPhoneNumber);
        addPhoneNumber(localPhoneNumber);
      }
    }
    
    public RecognizedContact setCompany(String paramString)
    {
      this.hasCompany = true;
      this.company_ = paramString;
      return this;
    }
    
    public RecognizedContact setName(GogglesStructuredResponseProtos.RecognizedContact.Name paramName)
    {
      if (paramName == null) {
        throw new NullPointerException();
      }
      this.hasName = true;
      this.name_ = paramName;
      return this;
    }
    
    public RecognizedContact setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeMessage(1, getName());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(2, getTitle());
      }
      if (hasCompany()) {
        paramCodedOutputStreamMicro.writeString(3, getCompany());
      }
      Iterator localIterator1 = getUrlList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeString(4, (String)localIterator1.next());
      }
      Iterator localIterator2 = getEmailList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (GogglesStructuredResponseProtos.RecognizedContact.Email)localIterator2.next());
      }
      Iterator localIterator3 = getAddressList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (GogglesStructuredResponseProtos.RecognizedContact.PostalAddress)localIterator3.next());
      }
      Iterator localIterator4 = getPhoneNumberList().iterator();
      while (localIterator4.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(7, (GogglesStructuredResponseProtos.RecognizedContact.PhoneNumber)localIterator4.next());
      }
    }
  }
  
  public static final class RecognizedText
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<GogglesStructuredResponseProtos.RecognizedText.Word> words_ = Collections.emptyList();
    
    public RecognizedText addWords(GogglesStructuredResponseProtos.RecognizedText.Word paramWord)
    {
      if (paramWord == null) {
        throw new NullPointerException();
      }
      if (this.words_.isEmpty()) {
        this.words_ = new ArrayList();
      }
      this.words_.add(paramWord);
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
      Iterator localIterator = getWordsList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (GogglesStructuredResponseProtos.RecognizedText.Word)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public List<GogglesStructuredResponseProtos.RecognizedText.Word> getWordsList()
    {
      return this.words_;
    }
    
    public RecognizedText mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        GogglesStructuredResponseProtos.RecognizedText.Word localWord = new GogglesStructuredResponseProtos.RecognizedText.Word();
        paramCodedInputStreamMicro.readMessage(localWord);
        addWords(localWord);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getWordsList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (GogglesStructuredResponseProtos.RecognizedText.Word)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.bionics.goggles.api2.GogglesStructuredResponseProtos
 * JD-Core Version:    0.7.0.1
 */