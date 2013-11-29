package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ReservationProtos
{
  public static final class Reservation
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<AgendaProtos.GmailReference> gmailReference_ = Collections.emptyList();
    private boolean hasLocation;
    private boolean hasPartySize;
    private boolean hasReservationUrl;
    private boolean hasType;
    private AgendaProtos.AgendaItemLocation location_ = null;
    private int partySize_ = 0;
    private String reservationUrl_ = "";
    private int type_ = 1;
    
    public Reservation addGmailReference(AgendaProtos.GmailReference paramGmailReference)
    {
      if (paramGmailReference == null) {
        throw new NullPointerException();
      }
      if (this.gmailReference_.isEmpty()) {
        this.gmailReference_ = new ArrayList();
      }
      this.gmailReference_.add(paramGmailReference);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public AgendaProtos.GmailReference getGmailReference(int paramInt)
    {
      return (AgendaProtos.GmailReference)this.gmailReference_.get(paramInt);
    }
    
    public int getGmailReferenceCount()
    {
      return this.gmailReference_.size();
    }
    
    public List<AgendaProtos.GmailReference> getGmailReferenceList()
    {
      return this.gmailReference_;
    }
    
    public AgendaProtos.AgendaItemLocation getLocation()
    {
      return this.location_;
    }
    
    public int getPartySize()
    {
      return this.partySize_;
    }
    
    public String getReservationUrl()
    {
      return this.reservationUrl_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasLocation()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getLocation());
      }
      if (hasReservationUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getReservationUrl());
      }
      Iterator localIterator = getGmailReferenceList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (AgendaProtos.GmailReference)localIterator.next());
      }
      if (hasPartySize()) {
        i += CodedOutputStreamMicro.computeUInt32Size(5, getPartySize());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasLocation()
    {
      return this.hasLocation;
    }
    
    public boolean hasPartySize()
    {
      return this.hasPartySize;
    }
    
    public boolean hasReservationUrl()
    {
      return this.hasReservationUrl;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public Reservation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setType(paramCodedInputStreamMicro.readInt32());
          break;
        case 18: 
          AgendaProtos.AgendaItemLocation localAgendaItemLocation = new AgendaProtos.AgendaItemLocation();
          paramCodedInputStreamMicro.readMessage(localAgendaItemLocation);
          setLocation(localAgendaItemLocation);
          break;
        case 26: 
          setReservationUrl(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          AgendaProtos.GmailReference localGmailReference = new AgendaProtos.GmailReference();
          paramCodedInputStreamMicro.readMessage(localGmailReference);
          addGmailReference(localGmailReference);
          break;
        }
        setPartySize(paramCodedInputStreamMicro.readUInt32());
      }
    }
    
    public Reservation setLocation(AgendaProtos.AgendaItemLocation paramAgendaItemLocation)
    {
      if (paramAgendaItemLocation == null) {
        throw new NullPointerException();
      }
      this.hasLocation = true;
      this.location_ = paramAgendaItemLocation;
      return this;
    }
    
    public Reservation setPartySize(int paramInt)
    {
      this.hasPartySize = true;
      this.partySize_ = paramInt;
      return this;
    }
    
    public Reservation setReservationUrl(String paramString)
    {
      this.hasReservationUrl = true;
      this.reservationUrl_ = paramString;
      return this;
    }
    
    public Reservation setType(int paramInt)
    {
      this.hasType = true;
      this.type_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getType());
      }
      if (hasLocation()) {
        paramCodedOutputStreamMicro.writeMessage(2, getLocation());
      }
      if (hasReservationUrl()) {
        paramCodedOutputStreamMicro.writeString(3, getReservationUrl());
      }
      Iterator localIterator = getGmailReferenceList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (AgendaProtos.GmailReference)localIterator.next());
      }
      if (hasPartySize()) {
        paramCodedOutputStreamMicro.writeUInt32(5, getPartySize());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ReservationProtos
 * JD-Core Version:    0.7.0.1
 */