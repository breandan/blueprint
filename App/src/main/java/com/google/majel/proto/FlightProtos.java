package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class FlightProtos
{
  public static final class FlightStatusEntry
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<Flight> flight_ = Collections.emptyList();
    
    public FlightStatusEntry addFlight(Flight paramFlight)
    {
      if (paramFlight == null) {
        throw new NullPointerException();
      }
      if (this.flight_.isEmpty()) {
        this.flight_ = new ArrayList();
      }
      this.flight_.add(paramFlight);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Flight getFlight(int paramInt)
    {
      return (Flight)this.flight_.get(paramInt);
    }
    
    public List<Flight> getFlightList()
    {
      return this.flight_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getFlightList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (Flight)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public FlightStatusEntry mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        Flight localFlight = new Flight();
        paramCodedInputStreamMicro.readMessage(localFlight);
        addFlight(localFlight);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getFlightList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (Flight)localIterator.next());
      }
    }
    
    public static final class Airport
      extends MessageMicro
    {
      private int cachedSize = -1;
      private String code_ = "";
      private boolean hasCode;
      private boolean hasUserAtAirport;
      private boolean userAtAirport_ = false;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public String getCode()
      {
        return this.code_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasCode();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getCode());
        }
        if (hasUserAtAirport()) {
          i += CodedOutputStreamMicro.computeBoolSize(4, getUserAtAirport());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean getUserAtAirport()
      {
        return this.userAtAirport_;
      }
      
      public boolean hasCode()
      {
        return this.hasCode;
      }
      
      public boolean hasUserAtAirport()
      {
        return this.hasUserAtAirport;
      }
      
      public Airport mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setCode(paramCodedInputStreamMicro.readString());
            break;
          }
          setUserAtAirport(paramCodedInputStreamMicro.readBool());
        }
      }
      
      public Airport setCode(String paramString)
      {
        this.hasCode = true;
        this.code_ = paramString;
        return this;
      }
      
      public Airport setUserAtAirport(boolean paramBoolean)
      {
        this.hasUserAtAirport = true;
        this.userAtAirport_ = paramBoolean;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasCode()) {
          paramCodedOutputStreamMicro.writeString(1, getCode());
        }
        if (hasUserAtAirport()) {
          paramCodedOutputStreamMicro.writeBool(4, getUserAtAirport());
        }
      }
    }
    
    public static final class Flight
      extends MessageMicro
    {
      private String airlineCode_ = "";
      private String airlineName_ = "";
      private FlightProtos.FlightStatusEntry.Airport arrivalAirport_ = null;
      private String arrivalGate_ = "";
      private String arrivalTerminal_ = "";
      private FlightProtos.FlightStatusEntry.Time arrivalTime_ = null;
      private int cachedSize = -1;
      private FlightProtos.FlightStatusEntry.Airport departureAirport_ = null;
      private String departureGate_ = "";
      private String departureTerminal_ = "";
      private FlightProtos.FlightStatusEntry.Time departureTime_ = null;
      private String detailsUrl_ = "";
      private FlightProtos.FlightStatusEntry.Airport diversionAirport_ = null;
      private String diversionGate_ = "";
      private String diversionTerminal_ = "";
      private String flightNumber_ = "";
      private List<AgendaProtos.GmailReference> gmailReference_ = Collections.emptyList();
      private boolean hasAirlineCode;
      private boolean hasAirlineName;
      private boolean hasArrivalAirport;
      private boolean hasArrivalGate;
      private boolean hasArrivalTerminal;
      private boolean hasArrivalTime;
      private boolean hasDepartureAirport;
      private boolean hasDepartureGate;
      private boolean hasDepartureTerminal;
      private boolean hasDepartureTime;
      private boolean hasDetailsUrl;
      private boolean hasDiversionAirport;
      private boolean hasDiversionGate;
      private boolean hasDiversionTerminal;
      private boolean hasFlightNumber;
      private boolean hasLastUpdatedSecondsSinceEpoch;
      private boolean hasNotificationDetails;
      private boolean hasOperatingAirlineCode;
      private boolean hasOperatingAirlineName;
      private boolean hasStatus;
      private boolean hasStatusCode;
      private long lastUpdatedSecondsSinceEpoch_ = 0L;
      private FlightProtos.FlightStatusEntry.NotificationDetails notificationDetails_ = null;
      private String operatingAirlineCode_ = "";
      private String operatingAirlineName_ = "";
      private int statusCode_ = 0;
      private String status_ = "";
      
      public Flight addGmailReference(AgendaProtos.GmailReference paramGmailReference)
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
      
      public String getAirlineCode()
      {
        return this.airlineCode_;
      }
      
      public String getAirlineName()
      {
        return this.airlineName_;
      }
      
      public FlightProtos.FlightStatusEntry.Airport getArrivalAirport()
      {
        return this.arrivalAirport_;
      }
      
      public String getArrivalGate()
      {
        return this.arrivalGate_;
      }
      
      public String getArrivalTerminal()
      {
        return this.arrivalTerminal_;
      }
      
      public FlightProtos.FlightStatusEntry.Time getArrivalTime()
      {
        return this.arrivalTime_;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public FlightProtos.FlightStatusEntry.Airport getDepartureAirport()
      {
        return this.departureAirport_;
      }
      
      public String getDepartureGate()
      {
        return this.departureGate_;
      }
      
      public String getDepartureTerminal()
      {
        return this.departureTerminal_;
      }
      
      public FlightProtos.FlightStatusEntry.Time getDepartureTime()
      {
        return this.departureTime_;
      }
      
      public String getDetailsUrl()
      {
        return this.detailsUrl_;
      }
      
      public FlightProtos.FlightStatusEntry.Airport getDiversionAirport()
      {
        return this.diversionAirport_;
      }
      
      public String getDiversionGate()
      {
        return this.diversionGate_;
      }
      
      public String getDiversionTerminal()
      {
        return this.diversionTerminal_;
      }
      
      public String getFlightNumber()
      {
        return this.flightNumber_;
      }
      
      public List<AgendaProtos.GmailReference> getGmailReferenceList()
      {
        return this.gmailReference_;
      }
      
      public long getLastUpdatedSecondsSinceEpoch()
      {
        return this.lastUpdatedSecondsSinceEpoch_;
      }
      
      public FlightProtos.FlightStatusEntry.NotificationDetails getNotificationDetails()
      {
        return this.notificationDetails_;
      }
      
      public String getOperatingAirlineCode()
      {
        return this.operatingAirlineCode_;
      }
      
      public String getOperatingAirlineName()
      {
        return this.operatingAirlineName_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasStatusCode();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatusCode());
        }
        if (hasStatus()) {
          i += CodedOutputStreamMicro.computeStringSize(2, getStatus());
        }
        if (hasLastUpdatedSecondsSinceEpoch()) {
          i += CodedOutputStreamMicro.computeInt64Size(3, getLastUpdatedSecondsSinceEpoch());
        }
        if (hasAirlineCode()) {
          i += CodedOutputStreamMicro.computeStringSize(4, getAirlineCode());
        }
        if (hasAirlineName()) {
          i += CodedOutputStreamMicro.computeStringSize(5, getAirlineName());
        }
        if (hasDepartureAirport()) {
          i += CodedOutputStreamMicro.computeMessageSize(6, getDepartureAirport());
        }
        if (hasDepartureTime()) {
          i += CodedOutputStreamMicro.computeMessageSize(7, getDepartureTime());
        }
        if (hasDepartureTerminal()) {
          i += CodedOutputStreamMicro.computeStringSize(8, getDepartureTerminal());
        }
        if (hasDepartureGate()) {
          i += CodedOutputStreamMicro.computeStringSize(9, getDepartureGate());
        }
        if (hasArrivalAirport()) {
          i += CodedOutputStreamMicro.computeMessageSize(10, getArrivalAirport());
        }
        if (hasArrivalTime()) {
          i += CodedOutputStreamMicro.computeMessageSize(11, getArrivalTime());
        }
        if (hasArrivalTerminal()) {
          i += CodedOutputStreamMicro.computeStringSize(12, getArrivalTerminal());
        }
        if (hasArrivalGate()) {
          i += CodedOutputStreamMicro.computeStringSize(13, getArrivalGate());
        }
        if (hasDiversionAirport()) {
          i += CodedOutputStreamMicro.computeMessageSize(14, getDiversionAirport());
        }
        if (hasDiversionTerminal()) {
          i += CodedOutputStreamMicro.computeStringSize(15, getDiversionTerminal());
        }
        if (hasDiversionGate()) {
          i += CodedOutputStreamMicro.computeStringSize(16, getDiversionGate());
        }
        if (hasFlightNumber()) {
          i += CodedOutputStreamMicro.computeStringSize(17, getFlightNumber());
        }
        if (hasDetailsUrl()) {
          i += CodedOutputStreamMicro.computeStringSize(18, getDetailsUrl());
        }
        Iterator localIterator = getGmailReferenceList().iterator();
        while (localIterator.hasNext()) {
          i += CodedOutputStreamMicro.computeMessageSize(19, (AgendaProtos.GmailReference)localIterator.next());
        }
        if (hasNotificationDetails()) {
          i += CodedOutputStreamMicro.computeMessageSize(20, getNotificationDetails());
        }
        if (hasOperatingAirlineName()) {
          i += CodedOutputStreamMicro.computeStringSize(21, getOperatingAirlineName());
        }
        if (hasOperatingAirlineCode()) {
          i += CodedOutputStreamMicro.computeStringSize(22, getOperatingAirlineCode());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getStatus()
      {
        return this.status_;
      }
      
      public int getStatusCode()
      {
        return this.statusCode_;
      }
      
      public boolean hasAirlineCode()
      {
        return this.hasAirlineCode;
      }
      
      public boolean hasAirlineName()
      {
        return this.hasAirlineName;
      }
      
      public boolean hasArrivalAirport()
      {
        return this.hasArrivalAirport;
      }
      
      public boolean hasArrivalGate()
      {
        return this.hasArrivalGate;
      }
      
      public boolean hasArrivalTerminal()
      {
        return this.hasArrivalTerminal;
      }
      
      public boolean hasArrivalTime()
      {
        return this.hasArrivalTime;
      }
      
      public boolean hasDepartureAirport()
      {
        return this.hasDepartureAirport;
      }
      
      public boolean hasDepartureGate()
      {
        return this.hasDepartureGate;
      }
      
      public boolean hasDepartureTerminal()
      {
        return this.hasDepartureTerminal;
      }
      
      public boolean hasDepartureTime()
      {
        return this.hasDepartureTime;
      }
      
      public boolean hasDetailsUrl()
      {
        return this.hasDetailsUrl;
      }
      
      public boolean hasDiversionAirport()
      {
        return this.hasDiversionAirport;
      }
      
      public boolean hasDiversionGate()
      {
        return this.hasDiversionGate;
      }
      
      public boolean hasDiversionTerminal()
      {
        return this.hasDiversionTerminal;
      }
      
      public boolean hasFlightNumber()
      {
        return this.hasFlightNumber;
      }
      
      public boolean hasLastUpdatedSecondsSinceEpoch()
      {
        return this.hasLastUpdatedSecondsSinceEpoch;
      }
      
      public boolean hasNotificationDetails()
      {
        return this.hasNotificationDetails;
      }
      
      public boolean hasOperatingAirlineCode()
      {
        return this.hasOperatingAirlineCode;
      }
      
      public boolean hasOperatingAirlineName()
      {
        return this.hasOperatingAirlineName;
      }
      
      public boolean hasStatus()
      {
        return this.hasStatus;
      }
      
      public boolean hasStatusCode()
      {
        return this.hasStatusCode;
      }
      
      public Flight mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setStatusCode(paramCodedInputStreamMicro.readInt32());
            break;
          case 18: 
            setStatus(paramCodedInputStreamMicro.readString());
            break;
          case 24: 
            setLastUpdatedSecondsSinceEpoch(paramCodedInputStreamMicro.readInt64());
            break;
          case 34: 
            setAirlineCode(paramCodedInputStreamMicro.readString());
            break;
          case 42: 
            setAirlineName(paramCodedInputStreamMicro.readString());
            break;
          case 50: 
            FlightProtos.FlightStatusEntry.Airport localAirport3 = new FlightProtos.FlightStatusEntry.Airport();
            paramCodedInputStreamMicro.readMessage(localAirport3);
            setDepartureAirport(localAirport3);
            break;
          case 58: 
            FlightProtos.FlightStatusEntry.Time localTime2 = new FlightProtos.FlightStatusEntry.Time();
            paramCodedInputStreamMicro.readMessage(localTime2);
            setDepartureTime(localTime2);
            break;
          case 66: 
            setDepartureTerminal(paramCodedInputStreamMicro.readString());
            break;
          case 74: 
            setDepartureGate(paramCodedInputStreamMicro.readString());
            break;
          case 82: 
            FlightProtos.FlightStatusEntry.Airport localAirport2 = new FlightProtos.FlightStatusEntry.Airport();
            paramCodedInputStreamMicro.readMessage(localAirport2);
            setArrivalAirport(localAirport2);
            break;
          case 90: 
            FlightProtos.FlightStatusEntry.Time localTime1 = new FlightProtos.FlightStatusEntry.Time();
            paramCodedInputStreamMicro.readMessage(localTime1);
            setArrivalTime(localTime1);
            break;
          case 98: 
            setArrivalTerminal(paramCodedInputStreamMicro.readString());
            break;
          case 106: 
            setArrivalGate(paramCodedInputStreamMicro.readString());
            break;
          case 114: 
            FlightProtos.FlightStatusEntry.Airport localAirport1 = new FlightProtos.FlightStatusEntry.Airport();
            paramCodedInputStreamMicro.readMessage(localAirport1);
            setDiversionAirport(localAirport1);
            break;
          case 122: 
            setDiversionTerminal(paramCodedInputStreamMicro.readString());
            break;
          case 130: 
            setDiversionGate(paramCodedInputStreamMicro.readString());
            break;
          case 138: 
            setFlightNumber(paramCodedInputStreamMicro.readString());
            break;
          case 146: 
            setDetailsUrl(paramCodedInputStreamMicro.readString());
            break;
          case 154: 
            AgendaProtos.GmailReference localGmailReference = new AgendaProtos.GmailReference();
            paramCodedInputStreamMicro.readMessage(localGmailReference);
            addGmailReference(localGmailReference);
            break;
          case 162: 
            FlightProtos.FlightStatusEntry.NotificationDetails localNotificationDetails = new FlightProtos.FlightStatusEntry.NotificationDetails();
            paramCodedInputStreamMicro.readMessage(localNotificationDetails);
            setNotificationDetails(localNotificationDetails);
            break;
          case 170: 
            setOperatingAirlineName(paramCodedInputStreamMicro.readString());
            break;
          }
          setOperatingAirlineCode(paramCodedInputStreamMicro.readString());
        }
      }
      
      public Flight setAirlineCode(String paramString)
      {
        this.hasAirlineCode = true;
        this.airlineCode_ = paramString;
        return this;
      }
      
      public Flight setAirlineName(String paramString)
      {
        this.hasAirlineName = true;
        this.airlineName_ = paramString;
        return this;
      }
      
      public Flight setArrivalAirport(FlightProtos.FlightStatusEntry.Airport paramAirport)
      {
        if (paramAirport == null) {
          throw new NullPointerException();
        }
        this.hasArrivalAirport = true;
        this.arrivalAirport_ = paramAirport;
        return this;
      }
      
      public Flight setArrivalGate(String paramString)
      {
        this.hasArrivalGate = true;
        this.arrivalGate_ = paramString;
        return this;
      }
      
      public Flight setArrivalTerminal(String paramString)
      {
        this.hasArrivalTerminal = true;
        this.arrivalTerminal_ = paramString;
        return this;
      }
      
      public Flight setArrivalTime(FlightProtos.FlightStatusEntry.Time paramTime)
      {
        if (paramTime == null) {
          throw new NullPointerException();
        }
        this.hasArrivalTime = true;
        this.arrivalTime_ = paramTime;
        return this;
      }
      
      public Flight setDepartureAirport(FlightProtos.FlightStatusEntry.Airport paramAirport)
      {
        if (paramAirport == null) {
          throw new NullPointerException();
        }
        this.hasDepartureAirport = true;
        this.departureAirport_ = paramAirport;
        return this;
      }
      
      public Flight setDepartureGate(String paramString)
      {
        this.hasDepartureGate = true;
        this.departureGate_ = paramString;
        return this;
      }
      
      public Flight setDepartureTerminal(String paramString)
      {
        this.hasDepartureTerminal = true;
        this.departureTerminal_ = paramString;
        return this;
      }
      
      public Flight setDepartureTime(FlightProtos.FlightStatusEntry.Time paramTime)
      {
        if (paramTime == null) {
          throw new NullPointerException();
        }
        this.hasDepartureTime = true;
        this.departureTime_ = paramTime;
        return this;
      }
      
      public Flight setDetailsUrl(String paramString)
      {
        this.hasDetailsUrl = true;
        this.detailsUrl_ = paramString;
        return this;
      }
      
      public Flight setDiversionAirport(FlightProtos.FlightStatusEntry.Airport paramAirport)
      {
        if (paramAirport == null) {
          throw new NullPointerException();
        }
        this.hasDiversionAirport = true;
        this.diversionAirport_ = paramAirport;
        return this;
      }
      
      public Flight setDiversionGate(String paramString)
      {
        this.hasDiversionGate = true;
        this.diversionGate_ = paramString;
        return this;
      }
      
      public Flight setDiversionTerminal(String paramString)
      {
        this.hasDiversionTerminal = true;
        this.diversionTerminal_ = paramString;
        return this;
      }
      
      public Flight setFlightNumber(String paramString)
      {
        this.hasFlightNumber = true;
        this.flightNumber_ = paramString;
        return this;
      }
      
      public Flight setLastUpdatedSecondsSinceEpoch(long paramLong)
      {
        this.hasLastUpdatedSecondsSinceEpoch = true;
        this.lastUpdatedSecondsSinceEpoch_ = paramLong;
        return this;
      }
      
      public Flight setNotificationDetails(FlightProtos.FlightStatusEntry.NotificationDetails paramNotificationDetails)
      {
        if (paramNotificationDetails == null) {
          throw new NullPointerException();
        }
        this.hasNotificationDetails = true;
        this.notificationDetails_ = paramNotificationDetails;
        return this;
      }
      
      public Flight setOperatingAirlineCode(String paramString)
      {
        this.hasOperatingAirlineCode = true;
        this.operatingAirlineCode_ = paramString;
        return this;
      }
      
      public Flight setOperatingAirlineName(String paramString)
      {
        this.hasOperatingAirlineName = true;
        this.operatingAirlineName_ = paramString;
        return this;
      }
      
      public Flight setStatus(String paramString)
      {
        this.hasStatus = true;
        this.status_ = paramString;
        return this;
      }
      
      public Flight setStatusCode(int paramInt)
      {
        this.hasStatusCode = true;
        this.statusCode_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasStatusCode()) {
          paramCodedOutputStreamMicro.writeInt32(1, getStatusCode());
        }
        if (hasStatus()) {
          paramCodedOutputStreamMicro.writeString(2, getStatus());
        }
        if (hasLastUpdatedSecondsSinceEpoch()) {
          paramCodedOutputStreamMicro.writeInt64(3, getLastUpdatedSecondsSinceEpoch());
        }
        if (hasAirlineCode()) {
          paramCodedOutputStreamMicro.writeString(4, getAirlineCode());
        }
        if (hasAirlineName()) {
          paramCodedOutputStreamMicro.writeString(5, getAirlineName());
        }
        if (hasDepartureAirport()) {
          paramCodedOutputStreamMicro.writeMessage(6, getDepartureAirport());
        }
        if (hasDepartureTime()) {
          paramCodedOutputStreamMicro.writeMessage(7, getDepartureTime());
        }
        if (hasDepartureTerminal()) {
          paramCodedOutputStreamMicro.writeString(8, getDepartureTerminal());
        }
        if (hasDepartureGate()) {
          paramCodedOutputStreamMicro.writeString(9, getDepartureGate());
        }
        if (hasArrivalAirport()) {
          paramCodedOutputStreamMicro.writeMessage(10, getArrivalAirport());
        }
        if (hasArrivalTime()) {
          paramCodedOutputStreamMicro.writeMessage(11, getArrivalTime());
        }
        if (hasArrivalTerminal()) {
          paramCodedOutputStreamMicro.writeString(12, getArrivalTerminal());
        }
        if (hasArrivalGate()) {
          paramCodedOutputStreamMicro.writeString(13, getArrivalGate());
        }
        if (hasDiversionAirport()) {
          paramCodedOutputStreamMicro.writeMessage(14, getDiversionAirport());
        }
        if (hasDiversionTerminal()) {
          paramCodedOutputStreamMicro.writeString(15, getDiversionTerminal());
        }
        if (hasDiversionGate()) {
          paramCodedOutputStreamMicro.writeString(16, getDiversionGate());
        }
        if (hasFlightNumber()) {
          paramCodedOutputStreamMicro.writeString(17, getFlightNumber());
        }
        if (hasDetailsUrl()) {
          paramCodedOutputStreamMicro.writeString(18, getDetailsUrl());
        }
        Iterator localIterator = getGmailReferenceList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeMessage(19, (AgendaProtos.GmailReference)localIterator.next());
        }
        if (hasNotificationDetails()) {
          paramCodedOutputStreamMicro.writeMessage(20, getNotificationDetails());
        }
        if (hasOperatingAirlineName()) {
          paramCodedOutputStreamMicro.writeString(21, getOperatingAirlineName());
        }
        if (hasOperatingAirlineCode()) {
          paramCodedOutputStreamMicro.writeString(22, getOperatingAirlineCode());
        }
      }
    }
    
    public static final class NotificationDetails
      extends MessageMicro
    {
      private int arriveMinutesBefore_ = 0;
      private int cachedSize = -1;
      private boolean hasArriveMinutesBefore;
      private boolean hasLeaveByTimeSecondsSinceEpoch;
      private long leaveByTimeSecondsSinceEpoch_ = 0L;
      
      public int getArriveMinutesBefore()
      {
        return this.arriveMinutesBefore_;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public long getLeaveByTimeSecondsSinceEpoch()
      {
        return this.leaveByTimeSecondsSinceEpoch_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasLeaveByTimeSecondsSinceEpoch();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getLeaveByTimeSecondsSinceEpoch());
        }
        if (hasArriveMinutesBefore()) {
          i += CodedOutputStreamMicro.computeInt32Size(2, getArriveMinutesBefore());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasArriveMinutesBefore()
      {
        return this.hasArriveMinutesBefore;
      }
      
      public boolean hasLeaveByTimeSecondsSinceEpoch()
      {
        return this.hasLeaveByTimeSecondsSinceEpoch;
      }
      
      public NotificationDetails mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setLeaveByTimeSecondsSinceEpoch(paramCodedInputStreamMicro.readInt64());
            break;
          }
          setArriveMinutesBefore(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public NotificationDetails setArriveMinutesBefore(int paramInt)
      {
        this.hasArriveMinutesBefore = true;
        this.arriveMinutesBefore_ = paramInt;
        return this;
      }
      
      public NotificationDetails setLeaveByTimeSecondsSinceEpoch(long paramLong)
      {
        this.hasLeaveByTimeSecondsSinceEpoch = true;
        this.leaveByTimeSecondsSinceEpoch_ = paramLong;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasLeaveByTimeSecondsSinceEpoch()) {
          paramCodedOutputStreamMicro.writeInt64(1, getLeaveByTimeSecondsSinceEpoch());
        }
        if (hasArriveMinutesBefore()) {
          paramCodedOutputStreamMicro.writeInt32(2, getArriveMinutesBefore());
        }
      }
    }
    
    public static final class Time
      extends MessageMicro
    {
      private long actualTimeSecondsSinceEpoch_ = 0L;
      private int cachedSize = -1;
      private boolean hasActualTimeSecondsSinceEpoch;
      private boolean hasScheduledTimeSecondsSinceEpoch;
      private boolean hasTimeZoneId;
      private boolean hasTimeZoneOffsetSeconds;
      private long scheduledTimeSecondsSinceEpoch_ = 0L;
      private String timeZoneId_ = "";
      private int timeZoneOffsetSeconds_ = 0;
      
      public long getActualTimeSecondsSinceEpoch()
      {
        return this.actualTimeSecondsSinceEpoch_;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public long getScheduledTimeSecondsSinceEpoch()
      {
        return this.scheduledTimeSecondsSinceEpoch_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasScheduledTimeSecondsSinceEpoch();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getScheduledTimeSecondsSinceEpoch());
        }
        if (hasActualTimeSecondsSinceEpoch()) {
          i += CodedOutputStreamMicro.computeInt64Size(2, getActualTimeSecondsSinceEpoch());
        }
        if (hasTimeZoneOffsetSeconds()) {
          i += CodedOutputStreamMicro.computeInt32Size(3, getTimeZoneOffsetSeconds());
        }
        if (hasTimeZoneId()) {
          i += CodedOutputStreamMicro.computeStringSize(4, getTimeZoneId());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getTimeZoneId()
      {
        return this.timeZoneId_;
      }
      
      public int getTimeZoneOffsetSeconds()
      {
        return this.timeZoneOffsetSeconds_;
      }
      
      public boolean hasActualTimeSecondsSinceEpoch()
      {
        return this.hasActualTimeSecondsSinceEpoch;
      }
      
      public boolean hasScheduledTimeSecondsSinceEpoch()
      {
        return this.hasScheduledTimeSecondsSinceEpoch;
      }
      
      public boolean hasTimeZoneId()
      {
        return this.hasTimeZoneId;
      }
      
      public boolean hasTimeZoneOffsetSeconds()
      {
        return this.hasTimeZoneOffsetSeconds;
      }
      
      public Time mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setScheduledTimeSecondsSinceEpoch(paramCodedInputStreamMicro.readInt64());
            break;
          case 16: 
            setActualTimeSecondsSinceEpoch(paramCodedInputStreamMicro.readInt64());
            break;
          case 24: 
            setTimeZoneOffsetSeconds(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setTimeZoneId(paramCodedInputStreamMicro.readString());
        }
      }
      
      public Time setActualTimeSecondsSinceEpoch(long paramLong)
      {
        this.hasActualTimeSecondsSinceEpoch = true;
        this.actualTimeSecondsSinceEpoch_ = paramLong;
        return this;
      }
      
      public Time setScheduledTimeSecondsSinceEpoch(long paramLong)
      {
        this.hasScheduledTimeSecondsSinceEpoch = true;
        this.scheduledTimeSecondsSinceEpoch_ = paramLong;
        return this;
      }
      
      public Time setTimeZoneId(String paramString)
      {
        this.hasTimeZoneId = true;
        this.timeZoneId_ = paramString;
        return this;
      }
      
      public Time setTimeZoneOffsetSeconds(int paramInt)
      {
        this.hasTimeZoneOffsetSeconds = true;
        this.timeZoneOffsetSeconds_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasScheduledTimeSecondsSinceEpoch()) {
          paramCodedOutputStreamMicro.writeInt64(1, getScheduledTimeSecondsSinceEpoch());
        }
        if (hasActualTimeSecondsSinceEpoch()) {
          paramCodedOutputStreamMicro.writeInt64(2, getActualTimeSecondsSinceEpoch());
        }
        if (hasTimeZoneOffsetSeconds()) {
          paramCodedOutputStreamMicro.writeInt32(3, getTimeZoneOffsetSeconds());
        }
        if (hasTimeZoneId()) {
          paramCodedOutputStreamMicro.writeString(4, getTimeZoneId());
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.FlightProtos
 * JD-Core Version:    0.7.0.1
 */