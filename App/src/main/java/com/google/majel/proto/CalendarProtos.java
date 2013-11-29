package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class CalendarProtos
{
  public static final class AgendaItem
    extends MessageMicro
  {
    private boolean allDay_ = false;
    private int cachedSize = -1;
    private CarRentalProtos.CarRentalEntry carRental_ = null;
    private CalendarProtos.DefaultExpansion defaultExpansion_ = null;
    private CalendarProtos.CalendarDateTime endTime_ = null;
    private int eventType_ = 0;
    private CalendarProtos.Event event_ = null;
    private FlightProtos.FlightStatusEntry flight_ = null;
    private boolean hasAllDay;
    private boolean hasCarRental;
    private boolean hasDefaultExpansion;
    private boolean hasEndTime;
    private boolean hasEvent;
    private boolean hasEventType;
    private boolean hasFlight;
    private boolean hasIcon;
    private boolean hasReservation;
    private boolean hasStartTime;
    private boolean hasStatus;
    private boolean hasSubtitle;
    private boolean hasTitle;
    private boolean hasTtsMultipleItemDescription;
    private boolean hasTtsSingleItemDescription;
    private int icon_ = 0;
    private ReservationProtos.Reservation reservation_ = null;
    private CalendarProtos.CalendarDateTime startTime_ = null;
    private int status_ = 0;
    private CalendarProtos.AgendaItemSubtitle subtitle_ = null;
    private String title_ = "";
    private String ttsMultipleItemDescription_ = "";
    private String ttsSingleItemDescription_ = "";
    
    public boolean getAllDay()
    {
      return this.allDay_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public CarRentalProtos.CarRentalEntry getCarRental()
    {
      return this.carRental_;
    }
    
    public CalendarProtos.DefaultExpansion getDefaultExpansion()
    {
      return this.defaultExpansion_;
    }
    
    public CalendarProtos.CalendarDateTime getEndTime()
    {
      return this.endTime_;
    }
    
    public CalendarProtos.Event getEvent()
    {
      return this.event_;
    }
    
    public int getEventType()
    {
      return this.eventType_;
    }
    
    public FlightProtos.FlightStatusEntry getFlight()
    {
      return this.flight_;
    }
    
    public int getIcon()
    {
      return this.icon_;
    }
    
    public ReservationProtos.Reservation getReservation()
    {
      return this.reservation_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTitle();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTitle());
      }
      if (hasStartTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getStartTime());
      }
      if (hasEndTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getEndTime());
      }
      if (hasAllDay()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getAllDay());
      }
      if (hasStatus()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getStatus());
      }
      if (hasSubtitle()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getSubtitle());
      }
      if (hasIcon()) {
        i += CodedOutputStreamMicro.computeInt32Size(7, getIcon());
      }
      if (hasDefaultExpansion()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, getDefaultExpansion());
      }
      if (hasEvent()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getEvent());
      }
      if (hasFlight()) {
        i += CodedOutputStreamMicro.computeMessageSize(11, getFlight());
      }
      if (hasCarRental()) {
        i += CodedOutputStreamMicro.computeMessageSize(12, getCarRental());
      }
      if (hasReservation()) {
        i += CodedOutputStreamMicro.computeMessageSize(13, getReservation());
      }
      if (hasEventType()) {
        i += CodedOutputStreamMicro.computeInt32Size(14, getEventType());
      }
      if (hasTtsSingleItemDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getTtsSingleItemDescription());
      }
      if (hasTtsMultipleItemDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getTtsMultipleItemDescription());
      }
      this.cachedSize = i;
      return i;
    }
    
    public CalendarProtos.CalendarDateTime getStartTime()
    {
      return this.startTime_;
    }
    
    public int getStatus()
    {
      return this.status_;
    }
    
    public CalendarProtos.AgendaItemSubtitle getSubtitle()
    {
      return this.subtitle_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getTtsMultipleItemDescription()
    {
      return this.ttsMultipleItemDescription_;
    }
    
    public String getTtsSingleItemDescription()
    {
      return this.ttsSingleItemDescription_;
    }
    
    public boolean hasAllDay()
    {
      return this.hasAllDay;
    }
    
    public boolean hasCarRental()
    {
      return this.hasCarRental;
    }
    
    public boolean hasDefaultExpansion()
    {
      return this.hasDefaultExpansion;
    }
    
    public boolean hasEndTime()
    {
      return this.hasEndTime;
    }
    
    public boolean hasEvent()
    {
      return this.hasEvent;
    }
    
    public boolean hasEventType()
    {
      return this.hasEventType;
    }
    
    public boolean hasFlight()
    {
      return this.hasFlight;
    }
    
    public boolean hasIcon()
    {
      return this.hasIcon;
    }
    
    public boolean hasReservation()
    {
      return this.hasReservation;
    }
    
    public boolean hasStartTime()
    {
      return this.hasStartTime;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public boolean hasSubtitle()
    {
      return this.hasSubtitle;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasTtsMultipleItemDescription()
    {
      return this.hasTtsMultipleItemDescription;
    }
    
    public boolean hasTtsSingleItemDescription()
    {
      return this.hasTtsSingleItemDescription;
    }
    
    public AgendaItem mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          CalendarProtos.CalendarDateTime localCalendarDateTime2 = new CalendarProtos.CalendarDateTime();
          paramCodedInputStreamMicro.readMessage(localCalendarDateTime2);
          setStartTime(localCalendarDateTime2);
          break;
        case 26: 
          CalendarProtos.CalendarDateTime localCalendarDateTime1 = new CalendarProtos.CalendarDateTime();
          paramCodedInputStreamMicro.readMessage(localCalendarDateTime1);
          setEndTime(localCalendarDateTime1);
          break;
        case 32: 
          setAllDay(paramCodedInputStreamMicro.readBool());
          break;
        case 40: 
          setStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 50: 
          CalendarProtos.AgendaItemSubtitle localAgendaItemSubtitle = new CalendarProtos.AgendaItemSubtitle();
          paramCodedInputStreamMicro.readMessage(localAgendaItemSubtitle);
          setSubtitle(localAgendaItemSubtitle);
          break;
        case 56: 
          setIcon(paramCodedInputStreamMicro.readInt32());
          break;
        case 66: 
          CalendarProtos.DefaultExpansion localDefaultExpansion = new CalendarProtos.DefaultExpansion();
          paramCodedInputStreamMicro.readMessage(localDefaultExpansion);
          setDefaultExpansion(localDefaultExpansion);
          break;
        case 82: 
          CalendarProtos.Event localEvent = new CalendarProtos.Event();
          paramCodedInputStreamMicro.readMessage(localEvent);
          setEvent(localEvent);
          break;
        case 90: 
          FlightProtos.FlightStatusEntry localFlightStatusEntry = new FlightProtos.FlightStatusEntry();
          paramCodedInputStreamMicro.readMessage(localFlightStatusEntry);
          setFlight(localFlightStatusEntry);
          break;
        case 98: 
          CarRentalProtos.CarRentalEntry localCarRentalEntry = new CarRentalProtos.CarRentalEntry();
          paramCodedInputStreamMicro.readMessage(localCarRentalEntry);
          setCarRental(localCarRentalEntry);
          break;
        case 106: 
          ReservationProtos.Reservation localReservation = new ReservationProtos.Reservation();
          paramCodedInputStreamMicro.readMessage(localReservation);
          setReservation(localReservation);
          break;
        case 112: 
          setEventType(paramCodedInputStreamMicro.readInt32());
          break;
        case 122: 
          setTtsSingleItemDescription(paramCodedInputStreamMicro.readString());
          break;
        }
        setTtsMultipleItemDescription(paramCodedInputStreamMicro.readString());
      }
    }
    
    public AgendaItem setAllDay(boolean paramBoolean)
    {
      this.hasAllDay = true;
      this.allDay_ = paramBoolean;
      return this;
    }
    
    public AgendaItem setCarRental(CarRentalProtos.CarRentalEntry paramCarRentalEntry)
    {
      if (paramCarRentalEntry == null) {
        throw new NullPointerException();
      }
      this.hasCarRental = true;
      this.carRental_ = paramCarRentalEntry;
      return this;
    }
    
    public AgendaItem setDefaultExpansion(CalendarProtos.DefaultExpansion paramDefaultExpansion)
    {
      if (paramDefaultExpansion == null) {
        throw new NullPointerException();
      }
      this.hasDefaultExpansion = true;
      this.defaultExpansion_ = paramDefaultExpansion;
      return this;
    }
    
    public AgendaItem setEndTime(CalendarProtos.CalendarDateTime paramCalendarDateTime)
    {
      if (paramCalendarDateTime == null) {
        throw new NullPointerException();
      }
      this.hasEndTime = true;
      this.endTime_ = paramCalendarDateTime;
      return this;
    }
    
    public AgendaItem setEvent(CalendarProtos.Event paramEvent)
    {
      if (paramEvent == null) {
        throw new NullPointerException();
      }
      this.hasEvent = true;
      this.event_ = paramEvent;
      return this;
    }
    
    public AgendaItem setEventType(int paramInt)
    {
      this.hasEventType = true;
      this.eventType_ = paramInt;
      return this;
    }
    
    public AgendaItem setFlight(FlightProtos.FlightStatusEntry paramFlightStatusEntry)
    {
      if (paramFlightStatusEntry == null) {
        throw new NullPointerException();
      }
      this.hasFlight = true;
      this.flight_ = paramFlightStatusEntry;
      return this;
    }
    
    public AgendaItem setIcon(int paramInt)
    {
      this.hasIcon = true;
      this.icon_ = paramInt;
      return this;
    }
    
    public AgendaItem setReservation(ReservationProtos.Reservation paramReservation)
    {
      if (paramReservation == null) {
        throw new NullPointerException();
      }
      this.hasReservation = true;
      this.reservation_ = paramReservation;
      return this;
    }
    
    public AgendaItem setStartTime(CalendarProtos.CalendarDateTime paramCalendarDateTime)
    {
      if (paramCalendarDateTime == null) {
        throw new NullPointerException();
      }
      this.hasStartTime = true;
      this.startTime_ = paramCalendarDateTime;
      return this;
    }
    
    public AgendaItem setStatus(int paramInt)
    {
      this.hasStatus = true;
      this.status_ = paramInt;
      return this;
    }
    
    public AgendaItem setSubtitle(CalendarProtos.AgendaItemSubtitle paramAgendaItemSubtitle)
    {
      if (paramAgendaItemSubtitle == null) {
        throw new NullPointerException();
      }
      this.hasSubtitle = true;
      this.subtitle_ = paramAgendaItemSubtitle;
      return this;
    }
    
    public AgendaItem setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public AgendaItem setTtsMultipleItemDescription(String paramString)
    {
      this.hasTtsMultipleItemDescription = true;
      this.ttsMultipleItemDescription_ = paramString;
      return this;
    }
    
    public AgendaItem setTtsSingleItemDescription(String paramString)
    {
      this.hasTtsSingleItemDescription = true;
      this.ttsSingleItemDescription_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(1, getTitle());
      }
      if (hasStartTime()) {
        paramCodedOutputStreamMicro.writeMessage(2, getStartTime());
      }
      if (hasEndTime()) {
        paramCodedOutputStreamMicro.writeMessage(3, getEndTime());
      }
      if (hasAllDay()) {
        paramCodedOutputStreamMicro.writeBool(4, getAllDay());
      }
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeInt32(5, getStatus());
      }
      if (hasSubtitle()) {
        paramCodedOutputStreamMicro.writeMessage(6, getSubtitle());
      }
      if (hasIcon()) {
        paramCodedOutputStreamMicro.writeInt32(7, getIcon());
      }
      if (hasDefaultExpansion()) {
        paramCodedOutputStreamMicro.writeMessage(8, getDefaultExpansion());
      }
      if (hasEvent()) {
        paramCodedOutputStreamMicro.writeMessage(10, getEvent());
      }
      if (hasFlight()) {
        paramCodedOutputStreamMicro.writeMessage(11, getFlight());
      }
      if (hasCarRental()) {
        paramCodedOutputStreamMicro.writeMessage(12, getCarRental());
      }
      if (hasReservation()) {
        paramCodedOutputStreamMicro.writeMessage(13, getReservation());
      }
      if (hasEventType()) {
        paramCodedOutputStreamMicro.writeInt32(14, getEventType());
      }
      if (hasTtsSingleItemDescription()) {
        paramCodedOutputStreamMicro.writeString(15, getTtsSingleItemDescription());
      }
      if (hasTtsMultipleItemDescription()) {
        paramCodedOutputStreamMicro.writeString(16, getTtsMultipleItemDescription());
      }
    }
  }
  
  public static final class AgendaItemSubtitle
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasStatus;
    private String status_ = "";
    private List<String> textSegment_ = Collections.emptyList();
    
    public AgendaItemSubtitle addTextSegment(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.textSegment_.isEmpty()) {
        this.textSegment_ = new ArrayList();
      }
      this.textSegment_.add(paramString);
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
      boolean bool = hasStatus();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getStatus());
      }
      int j = 0;
      Iterator localIterator = getTextSegmentList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getTextSegmentList().size();
      this.cachedSize = k;
      return k;
    }
    
    public String getStatus()
    {
      return this.status_;
    }
    
    public String getTextSegment(int paramInt)
    {
      return (String)this.textSegment_.get(paramInt);
    }
    
    public int getTextSegmentCount()
    {
      return this.textSegment_.size();
    }
    
    public List<String> getTextSegmentList()
    {
      return this.textSegment_;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public AgendaItemSubtitle mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setStatus(paramCodedInputStreamMicro.readString());
          break;
        }
        addTextSegment(paramCodedInputStreamMicro.readString());
      }
    }
    
    public AgendaItemSubtitle setStatus(String paramString)
    {
      this.hasStatus = true;
      this.status_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeString(1, getStatus());
      }
      Iterator localIterator = getTextSegmentList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(2, (String)localIterator.next());
      }
    }
  }
  
  public static final class CalendarDateTime
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean dateUnspecified_ = false;
    private ActionDateTimeProtos.ActionDate date_ = null;
    private boolean hasDate;
    private boolean hasDateUnspecified;
    private boolean hasOffsetMs;
    private boolean hasTime;
    private boolean hasTimeMs;
    private boolean hasTimeUnspecified;
    private int offsetMs_ = 0;
    private long timeMs_ = 0L;
    private boolean timeUnspecified_ = false;
    private ActionDateTimeProtos.ActionTime time_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public ActionDateTimeProtos.ActionDate getDate()
    {
      return this.date_;
    }
    
    public boolean getDateUnspecified()
    {
      return this.dateUnspecified_;
    }
    
    public int getOffsetMs()
    {
      return this.offsetMs_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTimeMs();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getTimeMs());
      }
      if (hasOffsetMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getOffsetMs());
      }
      if (hasDate()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getDate());
      }
      if (hasDateUnspecified()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getDateUnspecified());
      }
      if (hasTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getTime());
      }
      if (hasTimeUnspecified()) {
        i += CodedOutputStreamMicro.computeBoolSize(6, getTimeUnspecified());
      }
      this.cachedSize = i;
      return i;
    }
    
    public ActionDateTimeProtos.ActionTime getTime()
    {
      return this.time_;
    }
    
    public long getTimeMs()
    {
      return this.timeMs_;
    }
    
    public boolean getTimeUnspecified()
    {
      return this.timeUnspecified_;
    }
    
    public boolean hasDate()
    {
      return this.hasDate;
    }
    
    public boolean hasDateUnspecified()
    {
      return this.hasDateUnspecified;
    }
    
    public boolean hasOffsetMs()
    {
      return this.hasOffsetMs;
    }
    
    public boolean hasTime()
    {
      return this.hasTime;
    }
    
    public boolean hasTimeMs()
    {
      return this.hasTimeMs;
    }
    
    public boolean hasTimeUnspecified()
    {
      return this.hasTimeUnspecified;
    }
    
    public CalendarDateTime mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTimeMs(paramCodedInputStreamMicro.readInt64());
          break;
        case 16: 
          setOffsetMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          ActionDateTimeProtos.ActionDate localActionDate = new ActionDateTimeProtos.ActionDate();
          paramCodedInputStreamMicro.readMessage(localActionDate);
          setDate(localActionDate);
          break;
        case 32: 
          setDateUnspecified(paramCodedInputStreamMicro.readBool());
          break;
        case 42: 
          ActionDateTimeProtos.ActionTime localActionTime = new ActionDateTimeProtos.ActionTime();
          paramCodedInputStreamMicro.readMessage(localActionTime);
          setTime(localActionTime);
          break;
        }
        setTimeUnspecified(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public CalendarDateTime setDate(ActionDateTimeProtos.ActionDate paramActionDate)
    {
      if (paramActionDate == null) {
        throw new NullPointerException();
      }
      this.hasDate = true;
      this.date_ = paramActionDate;
      return this;
    }
    
    public CalendarDateTime setDateUnspecified(boolean paramBoolean)
    {
      this.hasDateUnspecified = true;
      this.dateUnspecified_ = paramBoolean;
      return this;
    }
    
    public CalendarDateTime setOffsetMs(int paramInt)
    {
      this.hasOffsetMs = true;
      this.offsetMs_ = paramInt;
      return this;
    }
    
    public CalendarDateTime setTime(ActionDateTimeProtos.ActionTime paramActionTime)
    {
      if (paramActionTime == null) {
        throw new NullPointerException();
      }
      this.hasTime = true;
      this.time_ = paramActionTime;
      return this;
    }
    
    public CalendarDateTime setTimeMs(long paramLong)
    {
      this.hasTimeMs = true;
      this.timeMs_ = paramLong;
      return this;
    }
    
    public CalendarDateTime setTimeUnspecified(boolean paramBoolean)
    {
      this.hasTimeUnspecified = true;
      this.timeUnspecified_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(1, getTimeMs());
      }
      if (hasOffsetMs()) {
        paramCodedOutputStreamMicro.writeInt32(2, getOffsetMs());
      }
      if (hasDate()) {
        paramCodedOutputStreamMicro.writeMessage(3, getDate());
      }
      if (hasDateUnspecified()) {
        paramCodedOutputStreamMicro.writeBool(4, getDateUnspecified());
      }
      if (hasTime()) {
        paramCodedOutputStreamMicro.writeMessage(5, getTime());
      }
      if (hasTimeUnspecified()) {
        paramCodedOutputStreamMicro.writeBool(6, getTimeUnspecified());
      }
    }
  }
  
  public static final class CalendarEvent
    extends MessageMicro
  {
    private List<Attendee> attendee_ = Collections.emptyList();
    private int cachedSize = -1;
    private String description_ = "";
    private CalendarProtos.CalendarDateTime endTime_ = null;
    private boolean hasDescription;
    private boolean hasEndTime;
    private boolean hasHtmlLink;
    private boolean hasIsAllDay;
    private boolean hasLocation;
    private boolean hasOtherAttendeesExcluded;
    private boolean hasStartTime;
    private boolean hasSummary;
    private String htmlLink_ = "";
    private boolean isAllDay_ = false;
    private String location_ = "";
    private boolean otherAttendeesExcluded_ = false;
    private List<Reminder> reminder_ = Collections.emptyList();
    private CalendarProtos.CalendarDateTime startTime_ = null;
    private String summary_ = "";
    
    public CalendarEvent addAttendee(Attendee paramAttendee)
    {
      if (paramAttendee == null) {
        throw new NullPointerException();
      }
      if (this.attendee_.isEmpty()) {
        this.attendee_ = new ArrayList();
      }
      this.attendee_.add(paramAttendee);
      return this;
    }
    
    public CalendarEvent addReminder(Reminder paramReminder)
    {
      if (paramReminder == null) {
        throw new NullPointerException();
      }
      if (this.reminder_.isEmpty()) {
        this.reminder_ = new ArrayList();
      }
      this.reminder_.add(paramReminder);
      return this;
    }
    
    public List<Attendee> getAttendeeList()
    {
      return this.attendee_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDescription()
    {
      return this.description_;
    }
    
    public CalendarProtos.CalendarDateTime getEndTime()
    {
      return this.endTime_;
    }
    
    public String getHtmlLink()
    {
      return this.htmlLink_;
    }
    
    public boolean getIsAllDay()
    {
      return this.isAllDay_;
    }
    
    public String getLocation()
    {
      return this.location_;
    }
    
    public boolean getOtherAttendeesExcluded()
    {
      return this.otherAttendeesExcluded_;
    }
    
    public int getReminderCount()
    {
      return this.reminder_.size();
    }
    
    public List<Reminder> getReminderList()
    {
      return this.reminder_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasHtmlLink();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getHtmlLink());
      }
      if (hasSummary()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSummary());
      }
      if (hasDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getDescription());
      }
      if (hasLocation()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getLocation());
      }
      if (hasStartTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getStartTime());
      }
      if (hasEndTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getEndTime());
      }
      Iterator localIterator1 = getAttendeeList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, (Attendee)localIterator1.next());
      }
      if (hasOtherAttendeesExcluded()) {
        i += CodedOutputStreamMicro.computeBoolSize(8, getOtherAttendeesExcluded());
      }
      if (hasIsAllDay()) {
        i += CodedOutputStreamMicro.computeBoolSize(9, getIsAllDay());
      }
      Iterator localIterator2 = getReminderList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, (Reminder)localIterator2.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public CalendarProtos.CalendarDateTime getStartTime()
    {
      return this.startTime_;
    }
    
    public String getSummary()
    {
      return this.summary_;
    }
    
    public boolean hasDescription()
    {
      return this.hasDescription;
    }
    
    public boolean hasEndTime()
    {
      return this.hasEndTime;
    }
    
    public boolean hasHtmlLink()
    {
      return this.hasHtmlLink;
    }
    
    public boolean hasIsAllDay()
    {
      return this.hasIsAllDay;
    }
    
    public boolean hasLocation()
    {
      return this.hasLocation;
    }
    
    public boolean hasOtherAttendeesExcluded()
    {
      return this.hasOtherAttendeesExcluded;
    }
    
    public boolean hasStartTime()
    {
      return this.hasStartTime;
    }
    
    public boolean hasSummary()
    {
      return this.hasSummary;
    }
    
    public CalendarEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setHtmlLink(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setSummary(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setDescription(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setLocation(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          CalendarProtos.CalendarDateTime localCalendarDateTime2 = new CalendarProtos.CalendarDateTime();
          paramCodedInputStreamMicro.readMessage(localCalendarDateTime2);
          setStartTime(localCalendarDateTime2);
          break;
        case 50: 
          CalendarProtos.CalendarDateTime localCalendarDateTime1 = new CalendarProtos.CalendarDateTime();
          paramCodedInputStreamMicro.readMessage(localCalendarDateTime1);
          setEndTime(localCalendarDateTime1);
          break;
        case 58: 
          Attendee localAttendee = new Attendee();
          paramCodedInputStreamMicro.readMessage(localAttendee);
          addAttendee(localAttendee);
          break;
        case 64: 
          setOtherAttendeesExcluded(paramCodedInputStreamMicro.readBool());
          break;
        case 72: 
          setIsAllDay(paramCodedInputStreamMicro.readBool());
          break;
        }
        Reminder localReminder = new Reminder();
        paramCodedInputStreamMicro.readMessage(localReminder);
        addReminder(localReminder);
      }
    }
    
    public CalendarEvent setDescription(String paramString)
    {
      this.hasDescription = true;
      this.description_ = paramString;
      return this;
    }
    
    public CalendarEvent setEndTime(CalendarProtos.CalendarDateTime paramCalendarDateTime)
    {
      if (paramCalendarDateTime == null) {
        throw new NullPointerException();
      }
      this.hasEndTime = true;
      this.endTime_ = paramCalendarDateTime;
      return this;
    }
    
    public CalendarEvent setHtmlLink(String paramString)
    {
      this.hasHtmlLink = true;
      this.htmlLink_ = paramString;
      return this;
    }
    
    public CalendarEvent setIsAllDay(boolean paramBoolean)
    {
      this.hasIsAllDay = true;
      this.isAllDay_ = paramBoolean;
      return this;
    }
    
    public CalendarEvent setLocation(String paramString)
    {
      this.hasLocation = true;
      this.location_ = paramString;
      return this;
    }
    
    public CalendarEvent setOtherAttendeesExcluded(boolean paramBoolean)
    {
      this.hasOtherAttendeesExcluded = true;
      this.otherAttendeesExcluded_ = paramBoolean;
      return this;
    }
    
    public CalendarEvent setStartTime(CalendarProtos.CalendarDateTime paramCalendarDateTime)
    {
      if (paramCalendarDateTime == null) {
        throw new NullPointerException();
      }
      this.hasStartTime = true;
      this.startTime_ = paramCalendarDateTime;
      return this;
    }
    
    public CalendarEvent setSummary(String paramString)
    {
      this.hasSummary = true;
      this.summary_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasHtmlLink()) {
        paramCodedOutputStreamMicro.writeString(1, getHtmlLink());
      }
      if (hasSummary()) {
        paramCodedOutputStreamMicro.writeString(2, getSummary());
      }
      if (hasDescription()) {
        paramCodedOutputStreamMicro.writeString(3, getDescription());
      }
      if (hasLocation()) {
        paramCodedOutputStreamMicro.writeString(4, getLocation());
      }
      if (hasStartTime()) {
        paramCodedOutputStreamMicro.writeMessage(5, getStartTime());
      }
      if (hasEndTime()) {
        paramCodedOutputStreamMicro.writeMessage(6, getEndTime());
      }
      Iterator localIterator1 = getAttendeeList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(7, (Attendee)localIterator1.next());
      }
      if (hasOtherAttendeesExcluded()) {
        paramCodedOutputStreamMicro.writeBool(8, getOtherAttendeesExcluded());
      }
      if (hasIsAllDay()) {
        paramCodedOutputStreamMicro.writeBool(9, getIsAllDay());
      }
      Iterator localIterator2 = getReminderList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(10, (Reminder)localIterator2.next());
      }
    }
    
    public static final class Attendee
      extends MessageMicro
    {
      private int cachedSize = -1;
      private String displayName_ = "";
      private String email_ = "";
      private boolean hasDisplayName;
      private boolean hasEmail;
      private boolean hasOptionalAttendee;
      private boolean hasResource;
      private boolean hasResponseComment;
      private boolean hasResponseStatus;
      private boolean optionalAttendee_ = false;
      private boolean resource_ = false;
      private String responseComment_ = "";
      private int responseStatus_ = 0;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public String getDisplayName()
      {
        return this.displayName_;
      }
      
      public String getEmail()
      {
        return this.email_;
      }
      
      public boolean getOptionalAttendee()
      {
        return this.optionalAttendee_;
      }
      
      public boolean getResource()
      {
        return this.resource_;
      }
      
      public String getResponseComment()
      {
        return this.responseComment_;
      }
      
      public int getResponseStatus()
      {
        return this.responseStatus_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasEmail();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getEmail());
        }
        if (hasDisplayName()) {
          i += CodedOutputStreamMicro.computeStringSize(2, getDisplayName());
        }
        if (hasResource()) {
          i += CodedOutputStreamMicro.computeBoolSize(3, getResource());
        }
        if (hasOptionalAttendee()) {
          i += CodedOutputStreamMicro.computeBoolSize(4, getOptionalAttendee());
        }
        if (hasResponseStatus()) {
          i += CodedOutputStreamMicro.computeInt32Size(5, getResponseStatus());
        }
        if (hasResponseComment()) {
          i += CodedOutputStreamMicro.computeStringSize(6, getResponseComment());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasDisplayName()
      {
        return this.hasDisplayName;
      }
      
      public boolean hasEmail()
      {
        return this.hasEmail;
      }
      
      public boolean hasOptionalAttendee()
      {
        return this.hasOptionalAttendee;
      }
      
      public boolean hasResource()
      {
        return this.hasResource;
      }
      
      public boolean hasResponseComment()
      {
        return this.hasResponseComment;
      }
      
      public boolean hasResponseStatus()
      {
        return this.hasResponseStatus;
      }
      
      public Attendee mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setEmail(paramCodedInputStreamMicro.readString());
            break;
          case 18: 
            setDisplayName(paramCodedInputStreamMicro.readString());
            break;
          case 24: 
            setResource(paramCodedInputStreamMicro.readBool());
            break;
          case 32: 
            setOptionalAttendee(paramCodedInputStreamMicro.readBool());
            break;
          case 40: 
            setResponseStatus(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setResponseComment(paramCodedInputStreamMicro.readString());
        }
      }
      
      public Attendee setDisplayName(String paramString)
      {
        this.hasDisplayName = true;
        this.displayName_ = paramString;
        return this;
      }
      
      public Attendee setEmail(String paramString)
      {
        this.hasEmail = true;
        this.email_ = paramString;
        return this;
      }
      
      public Attendee setOptionalAttendee(boolean paramBoolean)
      {
        this.hasOptionalAttendee = true;
        this.optionalAttendee_ = paramBoolean;
        return this;
      }
      
      public Attendee setResource(boolean paramBoolean)
      {
        this.hasResource = true;
        this.resource_ = paramBoolean;
        return this;
      }
      
      public Attendee setResponseComment(String paramString)
      {
        this.hasResponseComment = true;
        this.responseComment_ = paramString;
        return this;
      }
      
      public Attendee setResponseStatus(int paramInt)
      {
        this.hasResponseStatus = true;
        this.responseStatus_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasEmail()) {
          paramCodedOutputStreamMicro.writeString(1, getEmail());
        }
        if (hasDisplayName()) {
          paramCodedOutputStreamMicro.writeString(2, getDisplayName());
        }
        if (hasResource()) {
          paramCodedOutputStreamMicro.writeBool(3, getResource());
        }
        if (hasOptionalAttendee()) {
          paramCodedOutputStreamMicro.writeBool(4, getOptionalAttendee());
        }
        if (hasResponseStatus()) {
          paramCodedOutputStreamMicro.writeInt32(5, getResponseStatus());
        }
        if (hasResponseComment()) {
          paramCodedOutputStreamMicro.writeString(6, getResponseComment());
        }
      }
    }
    
    public static final class Reminder
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasMethod;
      private boolean hasMinutes;
      private int method_ = 0;
      private int minutes_ = 0;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getMethod()
      {
        return this.method_;
      }
      
      public int getMinutes()
      {
        return this.minutes_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasMinutes();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getMinutes());
        }
        if (hasMethod()) {
          i += CodedOutputStreamMicro.computeInt32Size(2, getMethod());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasMethod()
      {
        return this.hasMethod;
      }
      
      public boolean hasMinutes()
      {
        return this.hasMinutes;
      }
      
      public Reminder mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setMinutes(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setMethod(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public Reminder setMethod(int paramInt)
      {
        this.hasMethod = true;
        this.method_ = paramInt;
        return this;
      }
      
      public Reminder setMinutes(int paramInt)
      {
        this.hasMinutes = true;
        this.minutes_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasMinutes()) {
          paramCodedOutputStreamMicro.writeInt32(1, getMinutes());
        }
        if (hasMethod()) {
          paramCodedOutputStreamMicro.writeInt32(2, getMethod());
        }
      }
    }
  }
  
  public static final class CalendarQuery
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String content_ = "";
    private long earliestStartTimeMs_ = 0L;
    private boolean hasContent;
    private boolean hasEarliestStartTimeMs;
    private boolean hasLatestStartTimeMs;
    private boolean hasMaxResults;
    private boolean hasSearchType;
    private boolean hasTitleOnly;
    private long latestStartTimeMs_ = 0L;
    private int maxResults_ = 0;
    private int searchType_ = 0;
    private boolean titleOnly_ = false;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getContent()
    {
      return this.content_;
    }
    
    public long getEarliestStartTimeMs()
    {
      return this.earliestStartTimeMs_;
    }
    
    public long getLatestStartTimeMs()
    {
      return this.latestStartTimeMs_;
    }
    
    public int getMaxResults()
    {
      return this.maxResults_;
    }
    
    public int getSearchType()
    {
      return this.searchType_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasEarliestStartTimeMs();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getEarliestStartTimeMs());
      }
      if (hasLatestStartTimeMs()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getLatestStartTimeMs());
      }
      if (hasMaxResults()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getMaxResults());
      }
      if (hasContent()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getContent());
      }
      if (hasTitleOnly()) {
        i += CodedOutputStreamMicro.computeBoolSize(5, getTitleOnly());
      }
      if (hasSearchType()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getSearchType());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean getTitleOnly()
    {
      return this.titleOnly_;
    }
    
    public boolean hasContent()
    {
      return this.hasContent;
    }
    
    public boolean hasEarliestStartTimeMs()
    {
      return this.hasEarliestStartTimeMs;
    }
    
    public boolean hasLatestStartTimeMs()
    {
      return this.hasLatestStartTimeMs;
    }
    
    public boolean hasMaxResults()
    {
      return this.hasMaxResults;
    }
    
    public boolean hasSearchType()
    {
      return this.hasSearchType;
    }
    
    public boolean hasTitleOnly()
    {
      return this.hasTitleOnly;
    }
    
    public CalendarQuery mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setEarliestStartTimeMs(paramCodedInputStreamMicro.readInt64());
          break;
        case 16: 
          setLatestStartTimeMs(paramCodedInputStreamMicro.readInt64());
          break;
        case 24: 
          setMaxResults(paramCodedInputStreamMicro.readInt32());
          break;
        case 34: 
          setContent(paramCodedInputStreamMicro.readString());
          break;
        case 40: 
          setTitleOnly(paramCodedInputStreamMicro.readBool());
          break;
        }
        setSearchType(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public CalendarQuery setContent(String paramString)
    {
      this.hasContent = true;
      this.content_ = paramString;
      return this;
    }
    
    public CalendarQuery setEarliestStartTimeMs(long paramLong)
    {
      this.hasEarliestStartTimeMs = true;
      this.earliestStartTimeMs_ = paramLong;
      return this;
    }
    
    public CalendarQuery setLatestStartTimeMs(long paramLong)
    {
      this.hasLatestStartTimeMs = true;
      this.latestStartTimeMs_ = paramLong;
      return this;
    }
    
    public CalendarQuery setMaxResults(int paramInt)
    {
      this.hasMaxResults = true;
      this.maxResults_ = paramInt;
      return this;
    }
    
    public CalendarQuery setSearchType(int paramInt)
    {
      this.hasSearchType = true;
      this.searchType_ = paramInt;
      return this;
    }
    
    public CalendarQuery setTitleOnly(boolean paramBoolean)
    {
      this.hasTitleOnly = true;
      this.titleOnly_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasEarliestStartTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(1, getEarliestStartTimeMs());
      }
      if (hasLatestStartTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(2, getLatestStartTimeMs());
      }
      if (hasMaxResults()) {
        paramCodedOutputStreamMicro.writeInt32(3, getMaxResults());
      }
      if (hasContent()) {
        paramCodedOutputStreamMicro.writeString(4, getContent());
      }
      if (hasTitleOnly()) {
        paramCodedOutputStreamMicro.writeBool(5, getTitleOnly());
      }
      if (hasSearchType()) {
        paramCodedOutputStreamMicro.writeInt32(6, getSearchType());
      }
    }
  }
  
  public static final class DefaultExpansion
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLabel;
    private boolean hasUrl;
    private boolean hasUrlType;
    private String label_ = "";
    private int urlType_ = 0;
    private String url_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getLabel()
    {
      return this.label_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUrl();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUrl());
      }
      if (hasLabel()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getLabel());
      }
      if (hasUrlType()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getUrlType());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public int getUrlType()
    {
      return this.urlType_;
    }
    
    public boolean hasLabel()
    {
      return this.hasLabel;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public boolean hasUrlType()
    {
      return this.hasUrlType;
    }
    
    public DefaultExpansion mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setUrl(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setLabel(paramCodedInputStreamMicro.readString());
          break;
        }
        setUrlType(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public DefaultExpansion setLabel(String paramString)
    {
      this.hasLabel = true;
      this.label_ = paramString;
      return this;
    }
    
    public DefaultExpansion setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public DefaultExpansion setUrlType(int paramInt)
    {
      this.hasUrlType = true;
      this.urlType_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(1, getUrl());
      }
      if (hasLabel()) {
        paramCodedOutputStreamMicro.writeString(2, getLabel());
      }
      if (hasUrlType()) {
        paramCodedOutputStreamMicro.writeInt32(3, getUrlType());
      }
    }
  }
  
  public static final class Event
    extends MessageMicro
  {
    private List<String> attendee_ = Collections.emptyList();
    private int cachedSize = -1;
    private int calendarColor_ = 0;
    private String calendarName_ = "";
    private String description_ = "";
    private long eventId_ = 0L;
    private boolean hasCalendarColor;
    private boolean hasCalendarName;
    private boolean hasDescription;
    private boolean hasEventId;
    private boolean hasIsGplusEvent;
    private boolean hasLocation;
    private boolean hasOwnerName;
    private boolean hasTotalAttendeeCount;
    private boolean hasUrl;
    private boolean isGplusEvent_ = false;
    private String location_ = "";
    private String ownerName_ = "";
    private int totalAttendeeCount_ = 0;
    private String url_ = "";
    
    public Event addAttendee(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.attendee_.isEmpty()) {
        this.attendee_ = new ArrayList();
      }
      this.attendee_.add(paramString);
      return this;
    }
    
    public String getAttendee(int paramInt)
    {
      return (String)this.attendee_.get(paramInt);
    }
    
    public int getAttendeeCount()
    {
      return this.attendee_.size();
    }
    
    public List<String> getAttendeeList()
    {
      return this.attendee_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getCalendarColor()
    {
      return this.calendarColor_;
    }
    
    public String getCalendarName()
    {
      return this.calendarName_;
    }
    
    public String getDescription()
    {
      return this.description_;
    }
    
    public long getEventId()
    {
      return this.eventId_;
    }
    
    public boolean getIsGplusEvent()
    {
      return this.isGplusEvent_;
    }
    
    public String getLocation()
    {
      return this.location_;
    }
    
    public String getOwnerName()
    {
      return this.ownerName_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDescription();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDescription());
      }
      if (hasLocation()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getLocation());
      }
      int j = 0;
      Iterator localIterator = getAttendeeList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getAttendeeList().size();
      if (hasUrl()) {
        k += CodedOutputStreamMicro.computeStringSize(4, getUrl());
      }
      if (hasEventId()) {
        k += CodedOutputStreamMicro.computeInt64Size(5, getEventId());
      }
      if (hasCalendarName()) {
        k += CodedOutputStreamMicro.computeStringSize(6, getCalendarName());
      }
      if (hasIsGplusEvent()) {
        k += CodedOutputStreamMicro.computeBoolSize(7, getIsGplusEvent());
      }
      if (hasCalendarColor()) {
        k += CodedOutputStreamMicro.computeInt32Size(8, getCalendarColor());
      }
      if (hasOwnerName()) {
        k += CodedOutputStreamMicro.computeStringSize(9, getOwnerName());
      }
      if (hasTotalAttendeeCount()) {
        k += CodedOutputStreamMicro.computeInt32Size(10, getTotalAttendeeCount());
      }
      this.cachedSize = k;
      return k;
    }
    
    public int getTotalAttendeeCount()
    {
      return this.totalAttendeeCount_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasCalendarColor()
    {
      return this.hasCalendarColor;
    }
    
    public boolean hasCalendarName()
    {
      return this.hasCalendarName;
    }
    
    public boolean hasDescription()
    {
      return this.hasDescription;
    }
    
    public boolean hasEventId()
    {
      return this.hasEventId;
    }
    
    public boolean hasIsGplusEvent()
    {
      return this.hasIsGplusEvent;
    }
    
    public boolean hasLocation()
    {
      return this.hasLocation;
    }
    
    public boolean hasOwnerName()
    {
      return this.hasOwnerName;
    }
    
    public boolean hasTotalAttendeeCount()
    {
      return this.hasTotalAttendeeCount;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public Event mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDescription(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setLocation(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          addAttendee(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setUrl(paramCodedInputStreamMicro.readString());
          break;
        case 40: 
          setEventId(paramCodedInputStreamMicro.readInt64());
          break;
        case 50: 
          setCalendarName(paramCodedInputStreamMicro.readString());
          break;
        case 56: 
          setIsGplusEvent(paramCodedInputStreamMicro.readBool());
          break;
        case 64: 
          setCalendarColor(paramCodedInputStreamMicro.readInt32());
          break;
        case 74: 
          setOwnerName(paramCodedInputStreamMicro.readString());
          break;
        }
        setTotalAttendeeCount(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public Event setCalendarColor(int paramInt)
    {
      this.hasCalendarColor = true;
      this.calendarColor_ = paramInt;
      return this;
    }
    
    public Event setCalendarName(String paramString)
    {
      this.hasCalendarName = true;
      this.calendarName_ = paramString;
      return this;
    }
    
    public Event setDescription(String paramString)
    {
      this.hasDescription = true;
      this.description_ = paramString;
      return this;
    }
    
    public Event setEventId(long paramLong)
    {
      this.hasEventId = true;
      this.eventId_ = paramLong;
      return this;
    }
    
    public Event setIsGplusEvent(boolean paramBoolean)
    {
      this.hasIsGplusEvent = true;
      this.isGplusEvent_ = paramBoolean;
      return this;
    }
    
    public Event setLocation(String paramString)
    {
      this.hasLocation = true;
      this.location_ = paramString;
      return this;
    }
    
    public Event setOwnerName(String paramString)
    {
      this.hasOwnerName = true;
      this.ownerName_ = paramString;
      return this;
    }
    
    public Event setTotalAttendeeCount(int paramInt)
    {
      this.hasTotalAttendeeCount = true;
      this.totalAttendeeCount_ = paramInt;
      return this;
    }
    
    public Event setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDescription()) {
        paramCodedOutputStreamMicro.writeString(1, getDescription());
      }
      if (hasLocation()) {
        paramCodedOutputStreamMicro.writeString(2, getLocation());
      }
      Iterator localIterator = getAttendeeList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(3, (String)localIterator.next());
      }
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(4, getUrl());
      }
      if (hasEventId()) {
        paramCodedOutputStreamMicro.writeInt64(5, getEventId());
      }
      if (hasCalendarName()) {
        paramCodedOutputStreamMicro.writeString(6, getCalendarName());
      }
      if (hasIsGplusEvent()) {
        paramCodedOutputStreamMicro.writeBool(7, getIsGplusEvent());
      }
      if (hasCalendarColor()) {
        paramCodedOutputStreamMicro.writeInt32(8, getCalendarColor());
      }
      if (hasOwnerName()) {
        paramCodedOutputStreamMicro.writeString(9, getOwnerName());
      }
      if (hasTotalAttendeeCount()) {
        paramCodedOutputStreamMicro.writeInt32(10, getTotalAttendeeCount());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.CalendarProtos
 * JD-Core Version:    0.7.0.1
 */