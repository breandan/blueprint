package com.google.android.apps.sidekick.calendar;

import com.google.geo.sidekick.Sidekick.Entry;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Calendar
{
  public static final class CalendarData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private Calendar.ClientActions clientActions_ = null;
    private Calendar.EventData eventData_ = null;
    private boolean hasClientActions;
    private boolean hasEventData;
    private boolean hasId;
    private boolean hasIsPotentialDuplicate;
    private boolean hasServerData;
    private String id_ = "";
    private boolean isPotentialDuplicate_ = false;
    private Calendar.ServerData serverData_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Calendar.ClientActions getClientActions()
    {
      return this.clientActions_;
    }
    
    public Calendar.EventData getEventData()
    {
      return this.eventData_;
    }
    
    public String getId()
    {
      return this.id_;
    }
    
    public boolean getIsPotentialDuplicate()
    {
      return this.isPotentialDuplicate_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasEventData();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getEventData());
      }
      if (hasServerData()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getServerData());
      }
      if (hasClientActions()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getClientActions());
      }
      if (hasIsPotentialDuplicate()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getIsPotentialDuplicate());
      }
      if (hasId()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public Calendar.ServerData getServerData()
    {
      return this.serverData_;
    }
    
    public boolean hasClientActions()
    {
      return this.hasClientActions;
    }
    
    public boolean hasEventData()
    {
      return this.hasEventData;
    }
    
    public boolean hasId()
    {
      return this.hasId;
    }
    
    public boolean hasIsPotentialDuplicate()
    {
      return this.hasIsPotentialDuplicate;
    }
    
    public boolean hasServerData()
    {
      return this.hasServerData;
    }
    
    public CalendarData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          Calendar.EventData localEventData = new Calendar.EventData();
          paramCodedInputStreamMicro.readMessage(localEventData);
          setEventData(localEventData);
          break;
        case 18: 
          Calendar.ServerData localServerData = new Calendar.ServerData();
          paramCodedInputStreamMicro.readMessage(localServerData);
          setServerData(localServerData);
          break;
        case 26: 
          Calendar.ClientActions localClientActions = new Calendar.ClientActions();
          paramCodedInputStreamMicro.readMessage(localClientActions);
          setClientActions(localClientActions);
          break;
        case 32: 
          setIsPotentialDuplicate(paramCodedInputStreamMicro.readBool());
          break;
        }
        setId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public CalendarData setClientActions(Calendar.ClientActions paramClientActions)
    {
      if (paramClientActions == null) {
        throw new NullPointerException();
      }
      this.hasClientActions = true;
      this.clientActions_ = paramClientActions;
      return this;
    }
    
    public CalendarData setEventData(Calendar.EventData paramEventData)
    {
      if (paramEventData == null) {
        throw new NullPointerException();
      }
      this.hasEventData = true;
      this.eventData_ = paramEventData;
      return this;
    }
    
    public CalendarData setId(String paramString)
    {
      this.hasId = true;
      this.id_ = paramString;
      return this;
    }
    
    public CalendarData setIsPotentialDuplicate(boolean paramBoolean)
    {
      this.hasIsPotentialDuplicate = true;
      this.isPotentialDuplicate_ = paramBoolean;
      return this;
    }
    
    public CalendarData setServerData(Calendar.ServerData paramServerData)
    {
      if (paramServerData == null) {
        throw new NullPointerException();
      }
      this.hasServerData = true;
      this.serverData_ = paramServerData;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasEventData()) {
        paramCodedOutputStreamMicro.writeMessage(1, getEventData());
      }
      if (hasServerData()) {
        paramCodedOutputStreamMicro.writeMessage(2, getServerData());
      }
      if (hasClientActions()) {
        paramCodedOutputStreamMicro.writeMessage(3, getClientActions());
      }
      if (hasIsPotentialDuplicate()) {
        paramCodedOutputStreamMicro.writeBool(4, getIsPotentialDuplicate());
      }
      if (hasId()) {
        paramCodedOutputStreamMicro.writeString(5, getId());
      }
    }
  }
  
  public static final class CalendarDataSet
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<Calendar.CalendarData> calendarData_ = Collections.emptyList();
    private List<Calendar.CalendarInfo> calendarInfo_ = Collections.emptyList();
    private boolean gettingEventsFailed_ = false;
    private boolean hasGettingEventsFailed;
    
    public CalendarDataSet addCalendarData(Calendar.CalendarData paramCalendarData)
    {
      if (paramCalendarData == null) {
        throw new NullPointerException();
      }
      if (this.calendarData_.isEmpty()) {
        this.calendarData_ = new ArrayList();
      }
      this.calendarData_.add(paramCalendarData);
      return this;
    }
    
    public CalendarDataSet addCalendarInfo(Calendar.CalendarInfo paramCalendarInfo)
    {
      if (paramCalendarInfo == null) {
        throw new NullPointerException();
      }
      if (this.calendarInfo_.isEmpty()) {
        this.calendarInfo_ = new ArrayList();
      }
      this.calendarInfo_.add(paramCalendarInfo);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<Calendar.CalendarData> getCalendarDataList()
    {
      return this.calendarData_;
    }
    
    public List<Calendar.CalendarInfo> getCalendarInfoList()
    {
      return this.calendarInfo_;
    }
    
    public boolean getGettingEventsFailed()
    {
      return this.gettingEventsFailed_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getCalendarDataList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (Calendar.CalendarData)localIterator1.next());
      }
      Iterator localIterator2 = getCalendarInfoList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (Calendar.CalendarInfo)localIterator2.next());
      }
      if (hasGettingEventsFailed()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getGettingEventsFailed());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasGettingEventsFailed()
    {
      return this.hasGettingEventsFailed;
    }
    
    public CalendarDataSet mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          Calendar.CalendarData localCalendarData = new Calendar.CalendarData();
          paramCodedInputStreamMicro.readMessage(localCalendarData);
          addCalendarData(localCalendarData);
          break;
        case 18: 
          Calendar.CalendarInfo localCalendarInfo = new Calendar.CalendarInfo();
          paramCodedInputStreamMicro.readMessage(localCalendarInfo);
          addCalendarInfo(localCalendarInfo);
          break;
        }
        setGettingEventsFailed(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public CalendarDataSet setGettingEventsFailed(boolean paramBoolean)
    {
      this.hasGettingEventsFailed = true;
      this.gettingEventsFailed_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getCalendarDataList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (Calendar.CalendarData)localIterator1.next());
      }
      Iterator localIterator2 = getCalendarInfoList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (Calendar.CalendarInfo)localIterator2.next());
      }
      if (hasGettingEventsFailed()) {
        paramCodedOutputStreamMicro.writeBool(3, getGettingEventsFailed());
      }
    }
  }
  
  public static final class CalendarInfo
    extends MessageMicro
  {
    private String accountOwner_ = "";
    private int cachedSize = -1;
    private long dbId_ = 0L;
    private String displayName_ = "";
    private boolean hasAccountOwner;
    private boolean hasDbId;
    private boolean hasDisplayName;
    private boolean hasId;
    private String id_ = "";
    
    public String getAccountOwner()
    {
      return this.accountOwner_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public long getDbId()
    {
      return this.dbId_;
    }
    
    public String getDisplayName()
    {
      return this.displayName_;
    }
    
    public String getId()
    {
      return this.id_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDbId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getDbId());
      }
      if (hasId()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getId());
      }
      if (hasAccountOwner()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getAccountOwner());
      }
      if (hasDisplayName()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getDisplayName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAccountOwner()
    {
      return this.hasAccountOwner;
    }
    
    public boolean hasDbId()
    {
      return this.hasDbId;
    }
    
    public boolean hasDisplayName()
    {
      return this.hasDisplayName;
    }
    
    public boolean hasId()
    {
      return this.hasId;
    }
    
    public CalendarInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDbId(paramCodedInputStreamMicro.readInt64());
          break;
        case 18: 
          setId(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setAccountOwner(paramCodedInputStreamMicro.readString());
          break;
        }
        setDisplayName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public CalendarInfo setAccountOwner(String paramString)
    {
      this.hasAccountOwner = true;
      this.accountOwner_ = paramString;
      return this;
    }
    
    public CalendarInfo setDbId(long paramLong)
    {
      this.hasDbId = true;
      this.dbId_ = paramLong;
      return this;
    }
    
    public CalendarInfo setDisplayName(String paramString)
    {
      this.hasDisplayName = true;
      this.displayName_ = paramString;
      return this;
    }
    
    public CalendarInfo setId(String paramString)
    {
      this.hasId = true;
      this.id_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDbId()) {
        paramCodedOutputStreamMicro.writeInt64(1, getDbId());
      }
      if (hasId()) {
        paramCodedOutputStreamMicro.writeString(2, getId());
      }
      if (hasAccountOwner()) {
        paramCodedOutputStreamMicro.writeString(3, getAccountOwner());
      }
      if (hasDisplayName()) {
        paramCodedOutputStreamMicro.writeString(4, getDisplayName());
      }
    }
  }
  
  public static final class ClientActions
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasIsDismissed;
    private boolean hasIsNotificationDismissed;
    private boolean hasIsNotified;
    private boolean isDismissed_ = false;
    private boolean isNotificationDismissed_ = false;
    private boolean isNotified_ = false;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getIsDismissed()
    {
      return this.isDismissed_;
    }
    
    public boolean getIsNotificationDismissed()
    {
      return this.isNotificationDismissed_;
    }
    
    public boolean getIsNotified()
    {
      return this.isNotified_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasIsNotified();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getIsNotified());
      }
      if (hasIsDismissed()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getIsDismissed());
      }
      if (hasIsNotificationDismissed()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getIsNotificationDismissed());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasIsDismissed()
    {
      return this.hasIsDismissed;
    }
    
    public boolean hasIsNotificationDismissed()
    {
      return this.hasIsNotificationDismissed;
    }
    
    public boolean hasIsNotified()
    {
      return this.hasIsNotified;
    }
    
    public ClientActions mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setIsNotified(paramCodedInputStreamMicro.readBool());
          break;
        case 16: 
          setIsDismissed(paramCodedInputStreamMicro.readBool());
          break;
        }
        setIsNotificationDismissed(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public ClientActions setIsDismissed(boolean paramBoolean)
    {
      this.hasIsDismissed = true;
      this.isDismissed_ = paramBoolean;
      return this;
    }
    
    public ClientActions setIsNotificationDismissed(boolean paramBoolean)
    {
      this.hasIsNotificationDismissed = true;
      this.isNotificationDismissed_ = paramBoolean;
      return this;
    }
    
    public ClientActions setIsNotified(boolean paramBoolean)
    {
      this.hasIsNotified = true;
      this.isNotified_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasIsNotified()) {
        paramCodedOutputStreamMicro.writeBool(1, getIsNotified());
      }
      if (hasIsDismissed()) {
        paramCodedOutputStreamMicro.writeBool(2, getIsDismissed());
      }
      if (hasIsNotificationDismissed()) {
        paramCodedOutputStreamMicro.writeBool(3, getIsNotificationDismissed());
      }
    }
  }
  
  public static final class EventData
    extends MessageMicro
  {
    private int accessLevel_ = 0;
    private int cachedSize = -1;
    private long calendarDbId_ = 0L;
    private long endTimeSeconds_ = 0L;
    private long eventId_ = 0L;
    private boolean hasAccessLevel;
    private boolean hasCalendarDbId;
    private boolean hasEndTimeSeconds;
    private boolean hasEventId;
    private boolean hasNumberOfAttendees;
    private boolean hasPotentiallyGeocodableWhereField;
    private boolean hasProviderId;
    private boolean hasSelfAttendeeStatus;
    private boolean hasStartTimeSeconds;
    private boolean hasTitle;
    private boolean hasWhereField;
    private int numberOfAttendees_ = 0;
    private boolean potentiallyGeocodableWhereField_ = false;
    private long providerId_ = 0L;
    private int selfAttendeeStatus_ = 0;
    private long startTimeSeconds_ = 0L;
    private String title_ = "";
    private String whereField_ = "";
    
    public int getAccessLevel()
    {
      return this.accessLevel_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public long getCalendarDbId()
    {
      return this.calendarDbId_;
    }
    
    public long getEndTimeSeconds()
    {
      return this.endTimeSeconds_;
    }
    
    public long getEventId()
    {
      return this.eventId_;
    }
    
    public int getNumberOfAttendees()
    {
      return this.numberOfAttendees_;
    }
    
    public boolean getPotentiallyGeocodableWhereField()
    {
      return this.potentiallyGeocodableWhereField_;
    }
    
    public long getProviderId()
    {
      return this.providerId_;
    }
    
    public int getSelfAttendeeStatus()
    {
      return this.selfAttendeeStatus_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasProviderId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeUInt64Size(1, getProviderId());
      }
      if (hasEventId()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getEventId());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getTitle());
      }
      if (hasStartTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(4, getStartTimeSeconds());
      }
      if (hasEndTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getEndTimeSeconds());
      }
      if (hasWhereField()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getWhereField());
      }
      if (hasAccessLevel()) {
        i += CodedOutputStreamMicro.computeInt32Size(7, getAccessLevel());
      }
      if (hasNumberOfAttendees()) {
        i += CodedOutputStreamMicro.computeUInt32Size(8, getNumberOfAttendees());
      }
      if (hasPotentiallyGeocodableWhereField()) {
        i += CodedOutputStreamMicro.computeBoolSize(9, getPotentiallyGeocodableWhereField());
      }
      if (hasSelfAttendeeStatus()) {
        i += CodedOutputStreamMicro.computeInt32Size(10, getSelfAttendeeStatus());
      }
      if (hasCalendarDbId()) {
        i += CodedOutputStreamMicro.computeInt64Size(11, getCalendarDbId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getStartTimeSeconds()
    {
      return this.startTimeSeconds_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getWhereField()
    {
      return this.whereField_;
    }
    
    public boolean hasAccessLevel()
    {
      return this.hasAccessLevel;
    }
    
    public boolean hasCalendarDbId()
    {
      return this.hasCalendarDbId;
    }
    
    public boolean hasEndTimeSeconds()
    {
      return this.hasEndTimeSeconds;
    }
    
    public boolean hasEventId()
    {
      return this.hasEventId;
    }
    
    public boolean hasNumberOfAttendees()
    {
      return this.hasNumberOfAttendees;
    }
    
    public boolean hasPotentiallyGeocodableWhereField()
    {
      return this.hasPotentiallyGeocodableWhereField;
    }
    
    public boolean hasProviderId()
    {
      return this.hasProviderId;
    }
    
    public boolean hasSelfAttendeeStatus()
    {
      return this.hasSelfAttendeeStatus;
    }
    
    public boolean hasStartTimeSeconds()
    {
      return this.hasStartTimeSeconds;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasWhereField()
    {
      return this.hasWhereField;
    }
    
    public EventData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setProviderId(paramCodedInputStreamMicro.readUInt64());
          break;
        case 16: 
          setEventId(paramCodedInputStreamMicro.readInt64());
          break;
        case 26: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 32: 
          setStartTimeSeconds(paramCodedInputStreamMicro.readInt64());
          break;
        case 40: 
          setEndTimeSeconds(paramCodedInputStreamMicro.readInt64());
          break;
        case 50: 
          setWhereField(paramCodedInputStreamMicro.readString());
          break;
        case 56: 
          setAccessLevel(paramCodedInputStreamMicro.readInt32());
          break;
        case 64: 
          setNumberOfAttendees(paramCodedInputStreamMicro.readUInt32());
          break;
        case 72: 
          setPotentiallyGeocodableWhereField(paramCodedInputStreamMicro.readBool());
          break;
        case 80: 
          setSelfAttendeeStatus(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setCalendarDbId(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public EventData setAccessLevel(int paramInt)
    {
      this.hasAccessLevel = true;
      this.accessLevel_ = paramInt;
      return this;
    }
    
    public EventData setCalendarDbId(long paramLong)
    {
      this.hasCalendarDbId = true;
      this.calendarDbId_ = paramLong;
      return this;
    }
    
    public EventData setEndTimeSeconds(long paramLong)
    {
      this.hasEndTimeSeconds = true;
      this.endTimeSeconds_ = paramLong;
      return this;
    }
    
    public EventData setEventId(long paramLong)
    {
      this.hasEventId = true;
      this.eventId_ = paramLong;
      return this;
    }
    
    public EventData setNumberOfAttendees(int paramInt)
    {
      this.hasNumberOfAttendees = true;
      this.numberOfAttendees_ = paramInt;
      return this;
    }
    
    public EventData setPotentiallyGeocodableWhereField(boolean paramBoolean)
    {
      this.hasPotentiallyGeocodableWhereField = true;
      this.potentiallyGeocodableWhereField_ = paramBoolean;
      return this;
    }
    
    public EventData setProviderId(long paramLong)
    {
      this.hasProviderId = true;
      this.providerId_ = paramLong;
      return this;
    }
    
    public EventData setSelfAttendeeStatus(int paramInt)
    {
      this.hasSelfAttendeeStatus = true;
      this.selfAttendeeStatus_ = paramInt;
      return this;
    }
    
    public EventData setStartTimeSeconds(long paramLong)
    {
      this.hasStartTimeSeconds = true;
      this.startTimeSeconds_ = paramLong;
      return this;
    }
    
    public EventData setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public EventData setWhereField(String paramString)
    {
      this.hasWhereField = true;
      this.whereField_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasProviderId()) {
        paramCodedOutputStreamMicro.writeUInt64(1, getProviderId());
      }
      if (hasEventId()) {
        paramCodedOutputStreamMicro.writeInt64(2, getEventId());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(3, getTitle());
      }
      if (hasStartTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(4, getStartTimeSeconds());
      }
      if (hasEndTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(5, getEndTimeSeconds());
      }
      if (hasWhereField()) {
        paramCodedOutputStreamMicro.writeString(6, getWhereField());
      }
      if (hasAccessLevel()) {
        paramCodedOutputStreamMicro.writeInt32(7, getAccessLevel());
      }
      if (hasNumberOfAttendees()) {
        paramCodedOutputStreamMicro.writeUInt32(8, getNumberOfAttendees());
      }
      if (hasPotentiallyGeocodableWhereField()) {
        paramCodedOutputStreamMicro.writeBool(9, getPotentiallyGeocodableWhereField());
      }
      if (hasSelfAttendeeStatus()) {
        paramCodedOutputStreamMicro.writeInt32(10, getSelfAttendeeStatus());
      }
      if (hasCalendarDbId()) {
        paramCodedOutputStreamMicro.writeInt64(11, getCalendarDbId());
      }
    }
  }
  
  public static final class ServerData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean dataClearedBecauseEventChanged_ = false;
    private Sidekick.Entry entryFromServer_ = null;
    private LatLng geocodedLatLng_ = null;
    private boolean hasDataClearedBecauseEventChanged;
    private boolean hasEntryFromServer;
    private boolean hasGeocodedLatLng;
    private boolean hasIsGeocodable;
    private boolean hasNotifyTimeSeconds;
    private boolean hasServerHash;
    private boolean hasTravelTimeMinutes;
    private boolean isGeocodable_ = false;
    private long notifyTimeSeconds_ = 0L;
    private String serverHash_ = "";
    private int travelTimeMinutes_ = 0;
    
    public final ServerData clear()
    {
      clearServerHash();
      clearGeocodedLatLng();
      clearIsGeocodable();
      clearTravelTimeMinutes();
      clearNotifyTimeSeconds();
      clearDataClearedBecauseEventChanged();
      clearEntryFromServer();
      this.cachedSize = -1;
      return this;
    }
    
    public ServerData clearDataClearedBecauseEventChanged()
    {
      this.hasDataClearedBecauseEventChanged = false;
      this.dataClearedBecauseEventChanged_ = false;
      return this;
    }
    
    public ServerData clearEntryFromServer()
    {
      this.hasEntryFromServer = false;
      this.entryFromServer_ = null;
      return this;
    }
    
    public ServerData clearGeocodedLatLng()
    {
      this.hasGeocodedLatLng = false;
      this.geocodedLatLng_ = null;
      return this;
    }
    
    public ServerData clearIsGeocodable()
    {
      this.hasIsGeocodable = false;
      this.isGeocodable_ = false;
      return this;
    }
    
    public ServerData clearNotifyTimeSeconds()
    {
      this.hasNotifyTimeSeconds = false;
      this.notifyTimeSeconds_ = 0L;
      return this;
    }
    
    public ServerData clearServerHash()
    {
      this.hasServerHash = false;
      this.serverHash_ = "";
      return this;
    }
    
    public ServerData clearTravelTimeMinutes()
    {
      this.hasTravelTimeMinutes = false;
      this.travelTimeMinutes_ = 0;
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getDataClearedBecauseEventChanged()
    {
      return this.dataClearedBecauseEventChanged_;
    }
    
    public Sidekick.Entry getEntryFromServer()
    {
      return this.entryFromServer_;
    }
    
    public LatLng getGeocodedLatLng()
    {
      return this.geocodedLatLng_;
    }
    
    public boolean getIsGeocodable()
    {
      return this.isGeocodable_;
    }
    
    public long getNotifyTimeSeconds()
    {
      return this.notifyTimeSeconds_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasServerHash();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getServerHash());
      }
      if (hasGeocodedLatLng()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getGeocodedLatLng());
      }
      if (hasIsGeocodable()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getIsGeocodable());
      }
      if (hasTravelTimeMinutes()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getTravelTimeMinutes());
      }
      if (hasNotifyTimeSeconds()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getNotifyTimeSeconds());
      }
      if (hasDataClearedBecauseEventChanged()) {
        i += CodedOutputStreamMicro.computeBoolSize(6, getDataClearedBecauseEventChanged());
      }
      if (hasEntryFromServer()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getEntryFromServer());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getServerHash()
    {
      return this.serverHash_;
    }
    
    public int getTravelTimeMinutes()
    {
      return this.travelTimeMinutes_;
    }
    
    public boolean hasDataClearedBecauseEventChanged()
    {
      return this.hasDataClearedBecauseEventChanged;
    }
    
    public boolean hasEntryFromServer()
    {
      return this.hasEntryFromServer;
    }
    
    public boolean hasGeocodedLatLng()
    {
      return this.hasGeocodedLatLng;
    }
    
    public boolean hasIsGeocodable()
    {
      return this.hasIsGeocodable;
    }
    
    public boolean hasNotifyTimeSeconds()
    {
      return this.hasNotifyTimeSeconds;
    }
    
    public boolean hasServerHash()
    {
      return this.hasServerHash;
    }
    
    public boolean hasTravelTimeMinutes()
    {
      return this.hasTravelTimeMinutes;
    }
    
    public ServerData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setServerHash(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          LatLng localLatLng = new LatLng();
          paramCodedInputStreamMicro.readMessage(localLatLng);
          setGeocodedLatLng(localLatLng);
          break;
        case 24: 
          setIsGeocodable(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          setTravelTimeMinutes(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setNotifyTimeSeconds(paramCodedInputStreamMicro.readInt64());
          break;
        case 48: 
          setDataClearedBecauseEventChanged(paramCodedInputStreamMicro.readBool());
          break;
        }
        Sidekick.Entry localEntry = new Sidekick.Entry();
        paramCodedInputStreamMicro.readMessage(localEntry);
        setEntryFromServer(localEntry);
      }
    }
    
    public ServerData setDataClearedBecauseEventChanged(boolean paramBoolean)
    {
      this.hasDataClearedBecauseEventChanged = true;
      this.dataClearedBecauseEventChanged_ = paramBoolean;
      return this;
    }
    
    public ServerData setEntryFromServer(Sidekick.Entry paramEntry)
    {
      if (paramEntry == null) {
        throw new NullPointerException();
      }
      this.hasEntryFromServer = true;
      this.entryFromServer_ = paramEntry;
      return this;
    }
    
    public ServerData setGeocodedLatLng(LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasGeocodedLatLng = true;
      this.geocodedLatLng_ = paramLatLng;
      return this;
    }
    
    public ServerData setIsGeocodable(boolean paramBoolean)
    {
      this.hasIsGeocodable = true;
      this.isGeocodable_ = paramBoolean;
      return this;
    }
    
    public ServerData setNotifyTimeSeconds(long paramLong)
    {
      this.hasNotifyTimeSeconds = true;
      this.notifyTimeSeconds_ = paramLong;
      return this;
    }
    
    public ServerData setServerHash(String paramString)
    {
      this.hasServerHash = true;
      this.serverHash_ = paramString;
      return this;
    }
    
    public ServerData setTravelTimeMinutes(int paramInt)
    {
      this.hasTravelTimeMinutes = true;
      this.travelTimeMinutes_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasServerHash()) {
        paramCodedOutputStreamMicro.writeString(1, getServerHash());
      }
      if (hasGeocodedLatLng()) {
        paramCodedOutputStreamMicro.writeMessage(2, getGeocodedLatLng());
      }
      if (hasIsGeocodable()) {
        paramCodedOutputStreamMicro.writeBool(3, getIsGeocodable());
      }
      if (hasTravelTimeMinutes()) {
        paramCodedOutputStreamMicro.writeInt32(4, getTravelTimeMinutes());
      }
      if (hasNotifyTimeSeconds()) {
        paramCodedOutputStreamMicro.writeInt64(5, getNotifyTimeSeconds());
      }
      if (hasDataClearedBecauseEventChanged()) {
        paramCodedOutputStreamMicro.writeBool(6, getDataClearedBecauseEventChanged());
      }
      if (hasEntryFromServer()) {
        paramCodedOutputStreamMicro.writeMessage(7, getEntryFromServer());
      }
    }
    
    public static final class LatLng
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasLat;
      private boolean hasLng;
      private double lat_ = 0.0D;
      private double lng_ = 0.0D;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public double getLat()
      {
        return this.lat_;
      }
      
      public double getLng()
      {
        return this.lng_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasLat();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeDoubleSize(1, getLat());
        }
        if (hasLng()) {
          i += CodedOutputStreamMicro.computeDoubleSize(2, getLng());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasLat()
      {
        return this.hasLat;
      }
      
      public boolean hasLng()
      {
        return this.hasLng;
      }
      
      public LatLng mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          case 9: 
            setLat(paramCodedInputStreamMicro.readDouble());
            break;
          }
          setLng(paramCodedInputStreamMicro.readDouble());
        }
      }
      
      public LatLng setLat(double paramDouble)
      {
        this.hasLat = true;
        this.lat_ = paramDouble;
        return this;
      }
      
      public LatLng setLng(double paramDouble)
      {
        this.hasLng = true;
        this.lng_ = paramDouble;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasLat()) {
          paramCodedOutputStreamMicro.writeDouble(1, getLat());
        }
        if (hasLng()) {
          paramCodedOutputStreamMicro.writeDouble(2, getLng());
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.calendar.Calendar
 * JD-Core Version:    0.7.0.1
 */