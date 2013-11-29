package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ActionProtos
{
  public static final class Contact
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<Integer> digit_ = Collections.emptyList();
    private String email_ = "";
    private boolean hasEmail;
    private boolean hasName;
    private String name_ = "";
    
    public Contact addDigit(int paramInt)
    {
      if (this.digit_.isEmpty()) {
        this.digit_ = new ArrayList();
      }
      this.digit_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<Integer> getDigitList()
    {
      return this.digit_;
    }
    
    public String getEmail()
    {
      return this.email_;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      int j = 0;
      Iterator localIterator = getDigitList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
      }
      int k = i + j + 1 * getDigitList().size();
      if (hasEmail()) {
        k += CodedOutputStreamMicro.computeStringSize(3, getEmail());
      }
      this.cachedSize = k;
      return k;
    }
    
    public boolean hasEmail()
    {
      return this.hasEmail;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public Contact mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setName(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          addDigit(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setEmail(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Contact setEmail(String paramString)
    {
      this.hasEmail = true;
      this.email_ = paramString;
      return this;
    }
    
    public Contact setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      Iterator localIterator = getDigitList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(2, ((Integer)localIterator.next()).intValue());
      }
      if (hasEmail()) {
        paramCodedOutputStreamMicro.writeString(3, getEmail());
      }
    }
  }
  
  public static final class Email
    extends MessageMicro
  {
    private String body_ = "";
    private int cachedSize = -1;
    private List<ActionProtos.Contact> cc_ = Collections.emptyList();
    private boolean hasBody;
    private boolean hasSubject;
    private String subject_ = "";
    private List<ActionProtos.Contact> to_ = Collections.emptyList();
    
    public Email addCc(ActionProtos.Contact paramContact)
    {
      if (paramContact == null) {
        throw new NullPointerException();
      }
      if (this.cc_.isEmpty()) {
        this.cc_ = new ArrayList();
      }
      this.cc_.add(paramContact);
      return this;
    }
    
    public Email addTo(ActionProtos.Contact paramContact)
    {
      if (paramContact == null) {
        throw new NullPointerException();
      }
      if (this.to_.isEmpty()) {
        this.to_ = new ArrayList();
      }
      this.to_.add(paramContact);
      return this;
    }
    
    public String getBody()
    {
      return this.body_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<ActionProtos.Contact> getCcList()
    {
      return this.cc_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getToList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (ActionProtos.Contact)localIterator1.next());
      }
      Iterator localIterator2 = getCcList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (ActionProtos.Contact)localIterator2.next());
      }
      if (hasSubject()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getSubject());
      }
      if (hasBody()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getBody());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSubject()
    {
      return this.subject_;
    }
    
    public List<ActionProtos.Contact> getToList()
    {
      return this.to_;
    }
    
    public boolean hasBody()
    {
      return this.hasBody;
    }
    
    public boolean hasSubject()
    {
      return this.hasSubject;
    }
    
    public Email mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 18: 
          ActionProtos.Contact localContact2 = new ActionProtos.Contact();
          paramCodedInputStreamMicro.readMessage(localContact2);
          addTo(localContact2);
          break;
        case 26: 
          ActionProtos.Contact localContact1 = new ActionProtos.Contact();
          paramCodedInputStreamMicro.readMessage(localContact1);
          addCc(localContact1);
          break;
        case 34: 
          setSubject(paramCodedInputStreamMicro.readString());
          break;
        }
        setBody(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Email setBody(String paramString)
    {
      this.hasBody = true;
      this.body_ = paramString;
      return this;
    }
    
    public Email setSubject(String paramString)
    {
      this.hasSubject = true;
      this.subject_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getToList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (ActionProtos.Contact)localIterator1.next());
      }
      Iterator localIterator2 = getCcList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (ActionProtos.Contact)localIterator2.next());
      }
      if (hasSubject()) {
        paramCodedOutputStreamMicro.writeString(4, getSubject());
      }
      if (hasBody()) {
        paramCodedOutputStreamMicro.writeString(5, getBody());
      }
    }
  }
  
  public static final class IdentifyAudio
    extends MessageMicro
  {
    private int cachedSize = -1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      this.cachedSize = 0;
      return 0;
    }
    
    public IdentifyAudio mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      int i;
      do
      {
        i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        }
      } while (parseUnknownField(paramCodedInputStreamMicro, i));
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro) {}
  }
  
  public static final class Navigate
    extends MessageMicro
  {
    private int cachedSize = -1;
    private LatLngProtos.LatLng destination_ = null;
    private boolean hasDestination;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public LatLngProtos.LatLng getDestination()
    {
      return this.destination_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDestination();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getDestination());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasDestination()
    {
      return this.hasDestination;
    }
    
    public Navigate mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        LatLngProtos.LatLng localLatLng = new LatLngProtos.LatLng();
        paramCodedInputStreamMicro.readMessage(localLatLng);
        setDestination(localLatLng);
      }
    }
    
    public Navigate setDestination(LatLngProtos.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasDestination = true;
      this.destination_ = paramLatLng;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDestination()) {
        paramCodedOutputStreamMicro.writeMessage(1, getDestination());
      }
    }
  }
  
  public static final class Phone
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<ActionProtos.Contact> contact_ = Collections.emptyList();
    
    public Phone addContact(ActionProtos.Contact paramContact)
    {
      if (paramContact == null) {
        throw new NullPointerException();
      }
      if (this.contact_.isEmpty()) {
        this.contact_ = new ArrayList();
      }
      this.contact_.add(paramContact);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<ActionProtos.Contact> getContactList()
    {
      return this.contact_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getContactList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (ActionProtos.Contact)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public Phone mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        ActionProtos.Contact localContact = new ActionProtos.Contact();
        paramCodedInputStreamMicro.readMessage(localContact);
        addContact(localContact);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getContactList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (ActionProtos.Contact)localIterator.next());
      }
    }
  }
  
  public static final class Sms
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<ActionProtos.Contact> contact_ = Collections.emptyList();
    private boolean hasMessageContent;
    private String messageContent_ = "";
    
    public Sms addContact(ActionProtos.Contact paramContact)
    {
      if (paramContact == null) {
        throw new NullPointerException();
      }
      if (this.contact_.isEmpty()) {
        this.contact_ = new ArrayList();
      }
      this.contact_.add(paramContact);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<ActionProtos.Contact> getContactList()
    {
      return this.contact_;
    }
    
    public String getMessageContent()
    {
      return this.messageContent_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getContactList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (ActionProtos.Contact)localIterator.next());
      }
      if (hasMessageContent()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getMessageContent());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasMessageContent()
    {
      return this.hasMessageContent;
    }
    
    public Sms mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          ActionProtos.Contact localContact = new ActionProtos.Contact();
          paramCodedInputStreamMicro.readMessage(localContact);
          addContact(localContact);
          break;
        }
        setMessageContent(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Sms setMessageContent(String paramString)
    {
      this.hasMessageContent = true;
      this.messageContent_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getContactList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (ActionProtos.Contact)localIterator.next());
      }
      if (hasMessageContent()) {
        paramCodedOutputStreamMicro.writeString(2, getMessageContent());
      }
    }
  }
  
  public static final class TvControl
    extends MessageMicro
  {
    private int action_ = 0;
    private int cachedSize = -1;
    private int channel_ = 0;
    private boolean hasAction;
    private boolean hasChannel;
    
    public int getAction()
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
    
    public int getChannel()
    {
      return this.channel_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAction();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getAction());
      }
      if (hasChannel()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getChannel());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAction()
    {
      return this.hasAction;
    }
    
    public boolean hasChannel()
    {
      return this.hasChannel;
    }
    
    public TvControl mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAction(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setChannel(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public TvControl setAction(int paramInt)
    {
      this.hasAction = true;
      this.action_ = paramInt;
      return this;
    }
    
    public TvControl setChannel(int paramInt)
    {
      this.hasChannel = true;
      this.channel_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAction()) {
        paramCodedOutputStreamMicro.writeInt32(1, getAction());
      }
      if (hasChannel()) {
        paramCodedOutputStreamMicro.writeInt32(2, getChannel());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ActionProtos
 * JD-Core Version:    0.7.0.1
 */