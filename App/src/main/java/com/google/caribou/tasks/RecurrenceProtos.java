package com.google.caribou.tasks;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class RecurrenceProtos
{
  public static final class Recurrence
    extends MessageMicro
  {
    private int cachedSize = -1;
    private DailyPattern dailyPattern_ = null;
    private int every_ = 1;
    private int frequency_ = 0;
    private boolean hasDailyPattern;
    private boolean hasEvery;
    private boolean hasFrequency;
    private boolean hasMonthlyPattern;
    private boolean hasRecurrenceEnd;
    private boolean hasRecurrenceStart;
    private boolean hasWeeklyPattern;
    private boolean hasYearlyPattern;
    private MonthlyPattern monthlyPattern_ = null;
    private RecurrenceEnd recurrenceEnd_ = null;
    private RecurrenceStart recurrenceStart_ = null;
    private WeeklyPattern weeklyPattern_ = null;
    private YearlyPattern yearlyPattern_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public DailyPattern getDailyPattern()
    {
      return this.dailyPattern_;
    }
    
    public int getEvery()
    {
      return this.every_;
    }
    
    public int getFrequency()
    {
      return this.frequency_;
    }
    
    public MonthlyPattern getMonthlyPattern()
    {
      return this.monthlyPattern_;
    }
    
    public RecurrenceEnd getRecurrenceEnd()
    {
      return this.recurrenceEnd_;
    }
    
    public RecurrenceStart getRecurrenceStart()
    {
      return this.recurrenceStart_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFrequency();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getFrequency());
      }
      if (hasEvery()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getEvery());
      }
      if (hasRecurrenceStart()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getRecurrenceStart());
      }
      if (hasRecurrenceEnd()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getRecurrenceEnd());
      }
      if (hasDailyPattern()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getDailyPattern());
      }
      if (hasWeeklyPattern()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getWeeklyPattern());
      }
      if (hasMonthlyPattern()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getMonthlyPattern());
      }
      if (hasYearlyPattern()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, getYearlyPattern());
      }
      this.cachedSize = i;
      return i;
    }
    
    public WeeklyPattern getWeeklyPattern()
    {
      return this.weeklyPattern_;
    }
    
    public YearlyPattern getYearlyPattern()
    {
      return this.yearlyPattern_;
    }
    
    public boolean hasDailyPattern()
    {
      return this.hasDailyPattern;
    }
    
    public boolean hasEvery()
    {
      return this.hasEvery;
    }
    
    public boolean hasFrequency()
    {
      return this.hasFrequency;
    }
    
    public boolean hasMonthlyPattern()
    {
      return this.hasMonthlyPattern;
    }
    
    public boolean hasRecurrenceEnd()
    {
      return this.hasRecurrenceEnd;
    }
    
    public boolean hasRecurrenceStart()
    {
      return this.hasRecurrenceStart;
    }
    
    public boolean hasWeeklyPattern()
    {
      return this.hasWeeklyPattern;
    }
    
    public boolean hasYearlyPattern()
    {
      return this.hasYearlyPattern;
    }
    
    public Recurrence mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFrequency(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setEvery(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          RecurrenceStart localRecurrenceStart = new RecurrenceStart();
          paramCodedInputStreamMicro.readMessage(localRecurrenceStart);
          setRecurrenceStart(localRecurrenceStart);
          break;
        case 34: 
          RecurrenceEnd localRecurrenceEnd = new RecurrenceEnd();
          paramCodedInputStreamMicro.readMessage(localRecurrenceEnd);
          setRecurrenceEnd(localRecurrenceEnd);
          break;
        case 42: 
          DailyPattern localDailyPattern = new DailyPattern();
          paramCodedInputStreamMicro.readMessage(localDailyPattern);
          setDailyPattern(localDailyPattern);
          break;
        case 50: 
          WeeklyPattern localWeeklyPattern = new WeeklyPattern();
          paramCodedInputStreamMicro.readMessage(localWeeklyPattern);
          setWeeklyPattern(localWeeklyPattern);
          break;
        case 58: 
          MonthlyPattern localMonthlyPattern = new MonthlyPattern();
          paramCodedInputStreamMicro.readMessage(localMonthlyPattern);
          setMonthlyPattern(localMonthlyPattern);
          break;
        }
        YearlyPattern localYearlyPattern = new YearlyPattern();
        paramCodedInputStreamMicro.readMessage(localYearlyPattern);
        setYearlyPattern(localYearlyPattern);
      }
    }
    
    public Recurrence setDailyPattern(DailyPattern paramDailyPattern)
    {
      if (paramDailyPattern == null) {
        throw new NullPointerException();
      }
      this.hasDailyPattern = true;
      this.dailyPattern_ = paramDailyPattern;
      return this;
    }
    
    public Recurrence setEvery(int paramInt)
    {
      this.hasEvery = true;
      this.every_ = paramInt;
      return this;
    }
    
    public Recurrence setFrequency(int paramInt)
    {
      this.hasFrequency = true;
      this.frequency_ = paramInt;
      return this;
    }
    
    public Recurrence setMonthlyPattern(MonthlyPattern paramMonthlyPattern)
    {
      if (paramMonthlyPattern == null) {
        throw new NullPointerException();
      }
      this.hasMonthlyPattern = true;
      this.monthlyPattern_ = paramMonthlyPattern;
      return this;
    }
    
    public Recurrence setRecurrenceEnd(RecurrenceEnd paramRecurrenceEnd)
    {
      if (paramRecurrenceEnd == null) {
        throw new NullPointerException();
      }
      this.hasRecurrenceEnd = true;
      this.recurrenceEnd_ = paramRecurrenceEnd;
      return this;
    }
    
    public Recurrence setRecurrenceStart(RecurrenceStart paramRecurrenceStart)
    {
      if (paramRecurrenceStart == null) {
        throw new NullPointerException();
      }
      this.hasRecurrenceStart = true;
      this.recurrenceStart_ = paramRecurrenceStart;
      return this;
    }
    
    public Recurrence setWeeklyPattern(WeeklyPattern paramWeeklyPattern)
    {
      if (paramWeeklyPattern == null) {
        throw new NullPointerException();
      }
      this.hasWeeklyPattern = true;
      this.weeklyPattern_ = paramWeeklyPattern;
      return this;
    }
    
    public Recurrence setYearlyPattern(YearlyPattern paramYearlyPattern)
    {
      if (paramYearlyPattern == null) {
        throw new NullPointerException();
      }
      this.hasYearlyPattern = true;
      this.yearlyPattern_ = paramYearlyPattern;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFrequency()) {
        paramCodedOutputStreamMicro.writeInt32(1, getFrequency());
      }
      if (hasEvery()) {
        paramCodedOutputStreamMicro.writeInt32(2, getEvery());
      }
      if (hasRecurrenceStart()) {
        paramCodedOutputStreamMicro.writeMessage(3, getRecurrenceStart());
      }
      if (hasRecurrenceEnd()) {
        paramCodedOutputStreamMicro.writeMessage(4, getRecurrenceEnd());
      }
      if (hasDailyPattern()) {
        paramCodedOutputStreamMicro.writeMessage(5, getDailyPattern());
      }
      if (hasWeeklyPattern()) {
        paramCodedOutputStreamMicro.writeMessage(6, getWeeklyPattern());
      }
      if (hasMonthlyPattern()) {
        paramCodedOutputStreamMicro.writeMessage(7, getMonthlyPattern());
      }
      if (hasYearlyPattern()) {
        paramCodedOutputStreamMicro.writeMessage(8, getYearlyPattern());
      }
    }
    
    public static final class DailyPattern
      extends MessageMicro
    {
      private int cachedSize = -1;
      private int dayPeriod_ = 1;
      private boolean hasDayPeriod;
      private boolean hasTimeOfDay;
      private DateTimeProtos.DateTime.Time timeOfDay_ = null;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getDayPeriod()
      {
        return this.dayPeriod_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasTimeOfDay();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getTimeOfDay());
        }
        if (hasDayPeriod()) {
          i += CodedOutputStreamMicro.computeInt32Size(2, getDayPeriod());
        }
        this.cachedSize = i;
        return i;
      }
      
      public DateTimeProtos.DateTime.Time getTimeOfDay()
      {
        return this.timeOfDay_;
      }
      
      public boolean hasDayPeriod()
      {
        return this.hasDayPeriod;
      }
      
      public boolean hasTimeOfDay()
      {
        return this.hasTimeOfDay;
      }
      
      public DailyPattern mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            DateTimeProtos.DateTime.Time localTime = new DateTimeProtos.DateTime.Time();
            paramCodedInputStreamMicro.readMessage(localTime);
            setTimeOfDay(localTime);
            break;
          }
          setDayPeriod(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public DailyPattern setDayPeriod(int paramInt)
      {
        this.hasDayPeriod = true;
        this.dayPeriod_ = paramInt;
        return this;
      }
      
      public DailyPattern setTimeOfDay(DateTimeProtos.DateTime.Time paramTime)
      {
        if (paramTime == null) {
          throw new NullPointerException();
        }
        this.hasTimeOfDay = true;
        this.timeOfDay_ = paramTime;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasTimeOfDay()) {
          paramCodedOutputStreamMicro.writeMessage(1, getTimeOfDay());
        }
        if (hasDayPeriod()) {
          paramCodedOutputStreamMicro.writeInt32(2, getDayPeriod());
        }
      }
    }
    
    public static final class MonthlyPattern
      extends MessageMicro
    {
      public static final int LAST_DAY_FIELD_NUMBER = 2;
      public static final int LAST_WEEK_FIELD_NUMBER = 5;
      public static final int MONTH_DAY_FIELD_NUMBER = 1;
      public static final int WEEK_DAY_FIELD_NUMBER = 3;
      public static final int WEEK_DAY_NUMBER_FIELD_NUMBER = 4;
      private int cachedSize = -1;
      private boolean hasLastDay;
      private boolean hasLastWeek;
      private boolean hasWeekDay;
      private boolean hasWeekDayNumber;
      private boolean lastDay_ = false;
      private boolean lastWeek_ = false;
      private List<Integer> monthDay_ = Collections.emptyList();
      private int weekDayNumber_ = 0;
      private int weekDay_ = 1;
      
      public static MonthlyPattern parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        return new MonthlyPattern().mergeFrom(paramCodedInputStreamMicro);
      }
      
      public static MonthlyPattern parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferMicroException
      {
        return (MonthlyPattern)new MonthlyPattern().mergeFrom(paramArrayOfByte);
      }
      
      public MonthlyPattern addMonthDay(int paramInt)
      {
        if (this.monthDay_.isEmpty()) {
          this.monthDay_ = new ArrayList();
        }
        this.monthDay_.add(Integer.valueOf(paramInt));
        return this;
      }
      
      public final MonthlyPattern clear()
      {
        clearMonthDay();
        clearLastDay();
        clearWeekDay();
        clearWeekDayNumber();
        clearLastWeek();
        this.cachedSize = -1;
        return this;
      }
      
      public MonthlyPattern clearLastDay()
      {
        this.hasLastDay = false;
        this.lastDay_ = false;
        return this;
      }
      
      public MonthlyPattern clearLastWeek()
      {
        this.hasLastWeek = false;
        this.lastWeek_ = false;
        return this;
      }
      
      public MonthlyPattern clearMonthDay()
      {
        this.monthDay_ = Collections.emptyList();
        return this;
      }
      
      public MonthlyPattern clearWeekDay()
      {
        this.hasWeekDay = false;
        this.weekDay_ = 1;
        return this;
      }
      
      public MonthlyPattern clearWeekDayNumber()
      {
        this.hasWeekDayNumber = false;
        this.weekDayNumber_ = 0;
        return this;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public boolean getLastDay()
      {
        return this.lastDay_;
      }
      
      public boolean getLastWeek()
      {
        return this.lastWeek_;
      }
      
      public int getMonthDay(int paramInt)
      {
        return ((Integer)this.monthDay_.get(paramInt)).intValue();
      }
      
      public int getMonthDayCount()
      {
        return this.monthDay_.size();
      }
      
      public List<Integer> getMonthDayList()
      {
        return this.monthDay_;
      }
      
      public int getSerializedSize()
      {
        int i = 0;
        Iterator localIterator = getMonthDayList().iterator();
        while (localIterator.hasNext()) {
          i += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
        }
        int j = 0 + i + 1 * getMonthDayList().size();
        if (hasLastDay()) {
          j += CodedOutputStreamMicro.computeBoolSize(2, getLastDay());
        }
        if (hasWeekDay()) {
          j += CodedOutputStreamMicro.computeInt32Size(3, getWeekDay());
        }
        if (hasWeekDayNumber()) {
          j += CodedOutputStreamMicro.computeInt32Size(4, getWeekDayNumber());
        }
        if (hasLastWeek()) {
          j += CodedOutputStreamMicro.computeBoolSize(5, getLastWeek());
        }
        this.cachedSize = j;
        return j;
      }
      
      public int getWeekDay()
      {
        return this.weekDay_;
      }
      
      public int getWeekDayNumber()
      {
        return this.weekDayNumber_;
      }
      
      public boolean hasLastDay()
      {
        return this.hasLastDay;
      }
      
      public boolean hasLastWeek()
      {
        return this.hasLastWeek;
      }
      
      public boolean hasWeekDay()
      {
        return this.hasWeekDay;
      }
      
      public boolean hasWeekDayNumber()
      {
        return this.hasWeekDayNumber;
      }
      
      public final boolean isInitialized()
      {
        return true;
      }
      
      public MonthlyPattern mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            addMonthDay(paramCodedInputStreamMicro.readInt32());
            break;
          case 16: 
            setLastDay(paramCodedInputStreamMicro.readBool());
            break;
          case 24: 
            setWeekDay(paramCodedInputStreamMicro.readInt32());
            break;
          case 32: 
            setWeekDayNumber(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setLastWeek(paramCodedInputStreamMicro.readBool());
        }
      }
      
      public MonthlyPattern setLastDay(boolean paramBoolean)
      {
        this.hasLastDay = true;
        this.lastDay_ = paramBoolean;
        return this;
      }
      
      public MonthlyPattern setLastWeek(boolean paramBoolean)
      {
        this.hasLastWeek = true;
        this.lastWeek_ = paramBoolean;
        return this;
      }
      
      public MonthlyPattern setMonthDay(int paramInt1, int paramInt2)
      {
        this.monthDay_.set(paramInt1, Integer.valueOf(paramInt2));
        return this;
      }
      
      public MonthlyPattern setWeekDay(int paramInt)
      {
        this.hasWeekDay = true;
        this.weekDay_ = paramInt;
        return this;
      }
      
      public MonthlyPattern setWeekDayNumber(int paramInt)
      {
        this.hasWeekDayNumber = true;
        this.weekDayNumber_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        Iterator localIterator = getMonthDayList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeInt32(1, ((Integer)localIterator.next()).intValue());
        }
        if (hasLastDay()) {
          paramCodedOutputStreamMicro.writeBool(2, getLastDay());
        }
        if (hasWeekDay()) {
          paramCodedOutputStreamMicro.writeInt32(3, getWeekDay());
        }
        if (hasWeekDayNumber()) {
          paramCodedOutputStreamMicro.writeInt32(4, getWeekDayNumber());
        }
        if (hasLastWeek()) {
          paramCodedOutputStreamMicro.writeBool(5, getLastWeek());
        }
      }
    }
    
    public static final class RecurrenceEnd
      extends MessageMicro
    {
      private boolean autoRenew_ = false;
      private int cachedSize = -1;
      private DateTimeProtos.DateTime endDateTime_ = null;
      private long endMillis_ = 0L;
      private boolean hasAutoRenew;
      private boolean hasEndDateTime;
      private boolean hasEndMillis;
      private boolean hasNumOccurences;
      private int numOccurences_ = 0;
      
      public boolean getAutoRenew()
      {
        return this.autoRenew_;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public DateTimeProtos.DateTime getEndDateTime()
      {
        return this.endDateTime_;
      }
      
      public long getEndMillis()
      {
        return this.endMillis_;
      }
      
      public int getNumOccurences()
      {
        return this.numOccurences_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasEndDateTime();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getEndDateTime());
        }
        if (hasEndMillis()) {
          i += CodedOutputStreamMicro.computeInt64Size(2, getEndMillis());
        }
        if (hasNumOccurences()) {
          i += CodedOutputStreamMicro.computeInt32Size(3, getNumOccurences());
        }
        if (hasAutoRenew()) {
          i += CodedOutputStreamMicro.computeBoolSize(4, getAutoRenew());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasAutoRenew()
      {
        return this.hasAutoRenew;
      }
      
      public boolean hasEndDateTime()
      {
        return this.hasEndDateTime;
      }
      
      public boolean hasEndMillis()
      {
        return this.hasEndMillis;
      }
      
      public boolean hasNumOccurences()
      {
        return this.hasNumOccurences;
      }
      
      public RecurrenceEnd mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            DateTimeProtos.DateTime localDateTime = new DateTimeProtos.DateTime();
            paramCodedInputStreamMicro.readMessage(localDateTime);
            setEndDateTime(localDateTime);
            break;
          case 16: 
            setEndMillis(paramCodedInputStreamMicro.readInt64());
            break;
          case 24: 
            setNumOccurences(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setAutoRenew(paramCodedInputStreamMicro.readBool());
        }
      }
      
      public RecurrenceEnd setAutoRenew(boolean paramBoolean)
      {
        this.hasAutoRenew = true;
        this.autoRenew_ = paramBoolean;
        return this;
      }
      
      public RecurrenceEnd setEndDateTime(DateTimeProtos.DateTime paramDateTime)
      {
        if (paramDateTime == null) {
          throw new NullPointerException();
        }
        this.hasEndDateTime = true;
        this.endDateTime_ = paramDateTime;
        return this;
      }
      
      public RecurrenceEnd setEndMillis(long paramLong)
      {
        this.hasEndMillis = true;
        this.endMillis_ = paramLong;
        return this;
      }
      
      public RecurrenceEnd setNumOccurences(int paramInt)
      {
        this.hasNumOccurences = true;
        this.numOccurences_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasEndDateTime()) {
          paramCodedOutputStreamMicro.writeMessage(1, getEndDateTime());
        }
        if (hasEndMillis()) {
          paramCodedOutputStreamMicro.writeInt64(2, getEndMillis());
        }
        if (hasNumOccurences()) {
          paramCodedOutputStreamMicro.writeInt32(3, getNumOccurences());
        }
        if (hasAutoRenew()) {
          paramCodedOutputStreamMicro.writeBool(4, getAutoRenew());
        }
      }
    }
    
    public static final class RecurrenceStart
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasStartDateTime;
      private boolean hasStartMillis;
      private DateTimeProtos.DateTime startDateTime_ = null;
      private long startMillis_ = 0L;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasStartDateTime();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getStartDateTime());
        }
        if (hasStartMillis()) {
          i += CodedOutputStreamMicro.computeInt64Size(2, getStartMillis());
        }
        this.cachedSize = i;
        return i;
      }
      
      public DateTimeProtos.DateTime getStartDateTime()
      {
        return this.startDateTime_;
      }
      
      public long getStartMillis()
      {
        return this.startMillis_;
      }
      
      public boolean hasStartDateTime()
      {
        return this.hasStartDateTime;
      }
      
      public boolean hasStartMillis()
      {
        return this.hasStartMillis;
      }
      
      public RecurrenceStart mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            DateTimeProtos.DateTime localDateTime = new DateTimeProtos.DateTime();
            paramCodedInputStreamMicro.readMessage(localDateTime);
            setStartDateTime(localDateTime);
            break;
          }
          setStartMillis(paramCodedInputStreamMicro.readInt64());
        }
      }
      
      public RecurrenceStart setStartDateTime(DateTimeProtos.DateTime paramDateTime)
      {
        if (paramDateTime == null) {
          throw new NullPointerException();
        }
        this.hasStartDateTime = true;
        this.startDateTime_ = paramDateTime;
        return this;
      }
      
      public RecurrenceStart setStartMillis(long paramLong)
      {
        this.hasStartMillis = true;
        this.startMillis_ = paramLong;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasStartDateTime()) {
          paramCodedOutputStreamMicro.writeMessage(1, getStartDateTime());
        }
        if (hasStartMillis()) {
          paramCodedOutputStreamMicro.writeInt64(2, getStartMillis());
        }
      }
    }
    
    public static final class WeeklyPattern
      extends MessageMicro
    {
      public static final int WEEK_DAY_FIELD_NUMBER = 1;
      private int cachedSize = -1;
      private List<Integer> weekDay_ = Collections.emptyList();
      
      public static WeeklyPattern parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        return new WeeklyPattern().mergeFrom(paramCodedInputStreamMicro);
      }
      
      public static WeeklyPattern parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferMicroException
      {
        return (WeeklyPattern)new WeeklyPattern().mergeFrom(paramArrayOfByte);
      }
      
      public WeeklyPattern addWeekDay(int paramInt)
      {
        if (this.weekDay_.isEmpty()) {
          this.weekDay_ = new ArrayList();
        }
        this.weekDay_.add(Integer.valueOf(paramInt));
        return this;
      }
      
      public final WeeklyPattern clear()
      {
        clearWeekDay();
        this.cachedSize = -1;
        return this;
      }
      
      public WeeklyPattern clearWeekDay()
      {
        this.weekDay_ = Collections.emptyList();
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
        Iterator localIterator = getWeekDayList().iterator();
        while (localIterator.hasNext()) {
          i += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
        }
        int j = 0 + i + 1 * getWeekDayList().size();
        this.cachedSize = j;
        return j;
      }
      
      public int getWeekDay(int paramInt)
      {
        return ((Integer)this.weekDay_.get(paramInt)).intValue();
      }
      
      public int getWeekDayCount()
      {
        return this.weekDay_.size();
      }
      
      public List<Integer> getWeekDayList()
      {
        return this.weekDay_;
      }
      
      public final boolean isInitialized()
      {
        return true;
      }
      
      public WeeklyPattern mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          addWeekDay(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public WeeklyPattern setWeekDay(int paramInt1, int paramInt2)
      {
        this.weekDay_.set(paramInt1, Integer.valueOf(paramInt2));
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        Iterator localIterator = getWeekDayList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeInt32(1, ((Integer)localIterator.next()).intValue());
        }
      }
    }
    
    public static final class YearlyPattern
      extends MessageMicro
    {
      public static final int MONTHLY_PATTERN_FIELD_NUMBER = 1;
      public static final int YEAR_MONTH_FIELD_NUMBER = 2;
      private int cachedSize = -1;
      private boolean hasMonthlyPattern;
      private RecurrenceProtos.Recurrence.MonthlyPattern monthlyPattern_ = null;
      private List<Integer> yearMonth_ = Collections.emptyList();
      
      public static YearlyPattern parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
        throws IOException
      {
        return new YearlyPattern().mergeFrom(paramCodedInputStreamMicro);
      }
      
      public static YearlyPattern parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferMicroException
      {
        return (YearlyPattern)new YearlyPattern().mergeFrom(paramArrayOfByte);
      }
      
      public YearlyPattern addYearMonth(int paramInt)
      {
        if (this.yearMonth_.isEmpty()) {
          this.yearMonth_ = new ArrayList();
        }
        this.yearMonth_.add(Integer.valueOf(paramInt));
        return this;
      }
      
      public final YearlyPattern clear()
      {
        clearMonthlyPattern();
        clearYearMonth();
        this.cachedSize = -1;
        return this;
      }
      
      public YearlyPattern clearMonthlyPattern()
      {
        this.hasMonthlyPattern = false;
        this.monthlyPattern_ = null;
        return this;
      }
      
      public YearlyPattern clearYearMonth()
      {
        this.yearMonth_ = Collections.emptyList();
        return this;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public RecurrenceProtos.Recurrence.MonthlyPattern getMonthlyPattern()
      {
        return this.monthlyPattern_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasMonthlyPattern();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getMonthlyPattern());
        }
        int j = 0;
        Iterator localIterator = getYearMonthList().iterator();
        while (localIterator.hasNext()) {
          j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
        }
        int k = i + j + 1 * getYearMonthList().size();
        this.cachedSize = k;
        return k;
      }
      
      public int getYearMonth(int paramInt)
      {
        return ((Integer)this.yearMonth_.get(paramInt)).intValue();
      }
      
      public int getYearMonthCount()
      {
        return this.yearMonth_.size();
      }
      
      public List<Integer> getYearMonthList()
      {
        return this.yearMonth_;
      }
      
      public boolean hasMonthlyPattern()
      {
        return this.hasMonthlyPattern;
      }
      
      public final boolean isInitialized()
      {
        return true;
      }
      
      public YearlyPattern mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            RecurrenceProtos.Recurrence.MonthlyPattern localMonthlyPattern = new RecurrenceProtos.Recurrence.MonthlyPattern();
            paramCodedInputStreamMicro.readMessage(localMonthlyPattern);
            setMonthlyPattern(localMonthlyPattern);
            break;
          }
          addYearMonth(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public YearlyPattern setMonthlyPattern(RecurrenceProtos.Recurrence.MonthlyPattern paramMonthlyPattern)
      {
        if (paramMonthlyPattern == null) {
          throw new NullPointerException();
        }
        this.hasMonthlyPattern = true;
        this.monthlyPattern_ = paramMonthlyPattern;
        return this;
      }
      
      public YearlyPattern setYearMonth(int paramInt1, int paramInt2)
      {
        this.yearMonth_.set(paramInt1, Integer.valueOf(paramInt2));
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasMonthlyPattern()) {
          paramCodedOutputStreamMicro.writeMessage(1, getMonthlyPattern());
        }
        Iterator localIterator = getYearMonthList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeInt32(2, ((Integer)localIterator.next()).intValue());
        }
      }
    }
  }
  
  public static final class RecurrenceId
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasId;
    private String id_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getId()
    {
      return this.id_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasId()
    {
      return this.hasId;
    }
    
    public RecurrenceId mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RecurrenceId setId(String paramString)
    {
      this.hasId = true;
      this.id_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasId()) {
        paramCodedOutputStreamMicro.writeString(1, getId());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.caribou.tasks.RecurrenceProtos
 * JD-Core Version:    0.7.0.1
 */