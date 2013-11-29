package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class CarRentalProtos
{
  public static final class CarRentalEntry
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String confirmationNumber_ = "";
    private AgendaProtos.GmailReference gmailReference_ = null;
    private boolean hasConfirmationNumber;
    private boolean hasGmailReference;
    private boolean hasManageReservationUrl;
    private boolean hasPickupLocation;
    private boolean hasPickupPhone;
    private boolean hasProviderName;
    private boolean hasRenterName;
    private boolean hasReturnLocation;
    private boolean hasReturnPhone;
    private boolean hasType;
    private String manageReservationUrl_ = "";
    private AgendaProtos.AgendaItemLocation pickupLocation_ = null;
    private String pickupPhone_ = "";
    private String providerName_ = "";
    private String renterName_ = "";
    private AgendaProtos.AgendaItemLocation returnLocation_ = null;
    private String returnPhone_ = "";
    private int type_ = 1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getConfirmationNumber()
    {
      return this.confirmationNumber_;
    }
    
    public AgendaProtos.GmailReference getGmailReference()
    {
      return this.gmailReference_;
    }
    
    public String getManageReservationUrl()
    {
      return this.manageReservationUrl_;
    }
    
    public AgendaProtos.AgendaItemLocation getPickupLocation()
    {
      return this.pickupLocation_;
    }
    
    public String getPickupPhone()
    {
      return this.pickupPhone_;
    }
    
    public String getProviderName()
    {
      return this.providerName_;
    }
    
    public String getRenterName()
    {
      return this.renterName_;
    }
    
    public AgendaProtos.AgendaItemLocation getReturnLocation()
    {
      return this.returnLocation_;
    }
    
    public String getReturnPhone()
    {
      return this.returnPhone_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasRenterName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getRenterName());
      }
      if (hasConfirmationNumber()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getConfirmationNumber());
      }
      if (hasPickupLocation()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getPickupLocation());
      }
      if (hasPickupPhone()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getPickupPhone());
      }
      if (hasReturnLocation()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getReturnLocation());
      }
      if (hasReturnPhone()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getReturnPhone());
      }
      if (hasGmailReference()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getGmailReference());
      }
      if (hasType()) {
        i += CodedOutputStreamMicro.computeInt32Size(8, getType());
      }
      if (hasManageReservationUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getManageReservationUrl());
      }
      if (hasProviderName()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getProviderName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasConfirmationNumber()
    {
      return this.hasConfirmationNumber;
    }
    
    public boolean hasGmailReference()
    {
      return this.hasGmailReference;
    }
    
    public boolean hasManageReservationUrl()
    {
      return this.hasManageReservationUrl;
    }
    
    public boolean hasPickupLocation()
    {
      return this.hasPickupLocation;
    }
    
    public boolean hasPickupPhone()
    {
      return this.hasPickupPhone;
    }
    
    public boolean hasProviderName()
    {
      return this.hasProviderName;
    }
    
    public boolean hasRenterName()
    {
      return this.hasRenterName;
    }
    
    public boolean hasReturnLocation()
    {
      return this.hasReturnLocation;
    }
    
    public boolean hasReturnPhone()
    {
      return this.hasReturnPhone;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public CarRentalEntry mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setRenterName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setConfirmationNumber(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          AgendaProtos.AgendaItemLocation localAgendaItemLocation2 = new AgendaProtos.AgendaItemLocation();
          paramCodedInputStreamMicro.readMessage(localAgendaItemLocation2);
          setPickupLocation(localAgendaItemLocation2);
          break;
        case 34: 
          setPickupPhone(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          AgendaProtos.AgendaItemLocation localAgendaItemLocation1 = new AgendaProtos.AgendaItemLocation();
          paramCodedInputStreamMicro.readMessage(localAgendaItemLocation1);
          setReturnLocation(localAgendaItemLocation1);
          break;
        case 50: 
          setReturnPhone(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          AgendaProtos.GmailReference localGmailReference = new AgendaProtos.GmailReference();
          paramCodedInputStreamMicro.readMessage(localGmailReference);
          setGmailReference(localGmailReference);
          break;
        case 64: 
          setType(paramCodedInputStreamMicro.readInt32());
          break;
        case 74: 
          setManageReservationUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setProviderName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public CarRentalEntry setConfirmationNumber(String paramString)
    {
      this.hasConfirmationNumber = true;
      this.confirmationNumber_ = paramString;
      return this;
    }
    
    public CarRentalEntry setGmailReference(AgendaProtos.GmailReference paramGmailReference)
    {
      if (paramGmailReference == null) {
        throw new NullPointerException();
      }
      this.hasGmailReference = true;
      this.gmailReference_ = paramGmailReference;
      return this;
    }
    
    public CarRentalEntry setManageReservationUrl(String paramString)
    {
      this.hasManageReservationUrl = true;
      this.manageReservationUrl_ = paramString;
      return this;
    }
    
    public CarRentalEntry setPickupLocation(AgendaProtos.AgendaItemLocation paramAgendaItemLocation)
    {
      if (paramAgendaItemLocation == null) {
        throw new NullPointerException();
      }
      this.hasPickupLocation = true;
      this.pickupLocation_ = paramAgendaItemLocation;
      return this;
    }
    
    public CarRentalEntry setPickupPhone(String paramString)
    {
      this.hasPickupPhone = true;
      this.pickupPhone_ = paramString;
      return this;
    }
    
    public CarRentalEntry setProviderName(String paramString)
    {
      this.hasProviderName = true;
      this.providerName_ = paramString;
      return this;
    }
    
    public CarRentalEntry setRenterName(String paramString)
    {
      this.hasRenterName = true;
      this.renterName_ = paramString;
      return this;
    }
    
    public CarRentalEntry setReturnLocation(AgendaProtos.AgendaItemLocation paramAgendaItemLocation)
    {
      if (paramAgendaItemLocation == null) {
        throw new NullPointerException();
      }
      this.hasReturnLocation = true;
      this.returnLocation_ = paramAgendaItemLocation;
      return this;
    }
    
    public CarRentalEntry setReturnPhone(String paramString)
    {
      this.hasReturnPhone = true;
      this.returnPhone_ = paramString;
      return this;
    }
    
    public CarRentalEntry setType(int paramInt)
    {
      this.hasType = true;
      this.type_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasRenterName()) {
        paramCodedOutputStreamMicro.writeString(1, getRenterName());
      }
      if (hasConfirmationNumber()) {
        paramCodedOutputStreamMicro.writeString(2, getConfirmationNumber());
      }
      if (hasPickupLocation()) {
        paramCodedOutputStreamMicro.writeMessage(3, getPickupLocation());
      }
      if (hasPickupPhone()) {
        paramCodedOutputStreamMicro.writeString(4, getPickupPhone());
      }
      if (hasReturnLocation()) {
        paramCodedOutputStreamMicro.writeMessage(5, getReturnLocation());
      }
      if (hasReturnPhone()) {
        paramCodedOutputStreamMicro.writeString(6, getReturnPhone());
      }
      if (hasGmailReference()) {
        paramCodedOutputStreamMicro.writeMessage(7, getGmailReference());
      }
      if (hasType()) {
        paramCodedOutputStreamMicro.writeInt32(8, getType());
      }
      if (hasManageReservationUrl()) {
        paramCodedOutputStreamMicro.writeString(9, getManageReservationUrl());
      }
      if (hasProviderName()) {
        paramCodedOutputStreamMicro.writeString(10, getProviderName());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.CarRentalProtos
 * JD-Core Version:    0.7.0.1
 */