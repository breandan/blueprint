package com.google.caribou.tasks;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class DateTimeProtos
{
  public static final class DateTime
    extends MessageMicro
  {
    private long absoluteTimeMs_ = 0L;
    private int cachedSize = -1;
    private int dateRange_ = 1;
    private int day_ = 0;
    private boolean hasAbsoluteTimeMs;
    private boolean hasDateRange;
    private boolean hasDay;
    private boolean hasMonth;
    private boolean hasPeriod;
    private boolean hasTime;
    private boolean hasUnspecifiedFutureTime;
    private boolean hasYear;
    private int month_ = 0;
    private int period_ = 1;
    private Time time_ = null;
    private boolean unspecifiedFutureTime_ = false;
    private int year_ = 0;
    
    public long getAbsoluteTimeMs()
    {
      return this.absoluteTimeMs_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDateRange()
    {
      return this.dateRange_;
    }
    
    public int getDay()
    {
      return this.day_;
    }
    
    public int getMonth()
    {
      return this.month_;
    }
    
    public int getPeriod()
    {
      return this.period_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasYear();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getYear());
      }
      if (hasMonth()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getMonth());
      }
      if (hasDay()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getDay());
      }
      if (hasTime()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getTime());
      }
      if (hasPeriod()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getPeriod());
      }
      if (hasDateRange()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getDateRange());
      }
      if (hasAbsoluteTimeMs()) {
        i += CodedOutputStreamMicro.computeFixed64Size(7, getAbsoluteTimeMs());
      }
      if (hasUnspecifiedFutureTime()) {
        i += CodedOutputStreamMicro.computeBoolSize(8, getUnspecifiedFutureTime());
      }
      this.cachedSize = i;
      return i;
    }
    
    public Time getTime()
    {
      return this.time_;
    }
    
    public boolean getUnspecifiedFutureTime()
    {
      return this.unspecifiedFutureTime_;
    }
    
    public int getYear()
    {
      return this.year_;
    }
    
    public boolean hasAbsoluteTimeMs()
    {
      return this.hasAbsoluteTimeMs;
    }
    
    public boolean hasDateRange()
    {
      return this.hasDateRange;
    }
    
    public boolean hasDay()
    {
      return this.hasDay;
    }
    
    public boolean hasMonth()
    {
      return this.hasMonth;
    }
    
    public boolean hasPeriod()
    {
      return this.hasPeriod;
    }
    
    public boolean hasTime()
    {
      return this.hasTime;
    }
    
    public boolean hasUnspecifiedFutureTime()
    {
      return this.hasUnspecifiedFutureTime;
    }
    
    public boolean hasYear()
    {
      return this.hasYear;
    }
    
    public DateTime mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setYear(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setMonth(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setDay(paramCodedInputStreamMicro.readInt32());
          break;
        case 34: 
          Time localTime = new Time();
          paramCodedInputStreamMicro.readMessage(localTime);
          setTime(localTime);
          break;
        case 40: 
          setPeriod(paramCodedInputStreamMicro.readInt32());
          break;
        case 48: 
          setDateRange(paramCodedInputStreamMicro.readInt32());
          break;
        case 57: 
          setAbsoluteTimeMs(paramCodedInputStreamMicro.readFixed64());
          break;
        }
        setUnspecifiedFutureTime(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public DateTime setAbsoluteTimeMs(long paramLong)
    {
      this.hasAbsoluteTimeMs = true;
      this.absoluteTimeMs_ = paramLong;
      return this;
    }
    
    public DateTime setDateRange(int paramInt)
    {
      this.hasDateRange = true;
      this.dateRange_ = paramInt;
      return this;
    }
    
    public DateTime setDay(int paramInt)
    {
      this.hasDay = true;
      this.day_ = paramInt;
      return this;
    }
    
    public DateTime setMonth(int paramInt)
    {
      this.hasMonth = true;
      this.month_ = paramInt;
      return this;
    }
    
    public DateTime setPeriod(int paramInt)
    {
      this.hasPeriod = true;
      this.period_ = paramInt;
      return this;
    }
    
    public DateTime setTime(Time paramTime)
    {
      if (paramTime == null) {
        throw new NullPointerException();
      }
      this.hasTime = true;
      this.time_ = paramTime;
      return this;
    }
    
    public DateTime setUnspecifiedFutureTime(boolean paramBoolean)
    {
      this.hasUnspecifiedFutureTime = true;
      this.unspecifiedFutureTime_ = paramBoolean;
      return this;
    }
    
    public DateTime setYear(int paramInt)
    {
      this.hasYear = true;
      this.year_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasYear()) {
        paramCodedOutputStreamMicro.writeInt32(1, getYear());
      }
      if (hasMonth()) {
        paramCodedOutputStreamMicro.writeInt32(2, getMonth());
      }
      if (hasDay()) {
        paramCodedOutputStreamMicro.writeInt32(3, getDay());
      }
      if (hasTime()) {
        paramCodedOutputStreamMicro.writeMessage(4, getTime());
      }
      if (hasPeriod()) {
        paramCodedOutputStreamMicro.writeInt32(5, getPeriod());
      }
      if (hasDateRange()) {
        paramCodedOutputStreamMicro.writeInt32(6, getDateRange());
      }
      if (hasAbsoluteTimeMs()) {
        paramCodedOutputStreamMicro.writeFixed64(7, getAbsoluteTimeMs());
      }
      if (hasUnspecifiedFutureTime()) {
        paramCodedOutputStreamMicro.writeBool(8, getUnspecifiedFutureTime());
      }
    }
    
    public static final class Time
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasHour;
      private boolean hasMinute;
      private boolean hasSecond;
      private int hour_ = 0;
      private int minute_ = 0;
      private int second_ = 0;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getHour()
      {
        return this.hour_;
      }
      
      public int getMinute()
      {
        return this.minute_;
      }
      
      public int getSecond()
      {
        return this.second_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasHour();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getHour());
        }
        if (hasMinute()) {
          i += CodedOutputStreamMicro.computeInt32Size(2, getMinute());
        }
        if (hasSecond()) {
          i += CodedOutputStreamMicro.computeInt32Size(3, getSecond());
        }
        this.cachedSize = i;
        return i;
      }
      
      public boolean hasHour()
      {
        return this.hasHour;
      }
      
      public boolean hasMinute()
      {
        return this.hasMinute;
      }
      
      public boolean hasSecond()
      {
        return this.hasSecond;
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
            setHour(paramCodedInputStreamMicro.readInt32());
            break;
          case 16: 
            setMinute(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setSecond(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public Time setHour(int paramInt)
      {
        this.hasHour = true;
        this.hour_ = paramInt;
        return this;
      }
      
      public Time setMinute(int paramInt)
      {
        this.hasMinute = true;
        this.minute_ = paramInt;
        return this;
      }
      
      public Time setSecond(int paramInt)
      {
        this.hasSecond = true;
        this.second_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasHour()) {
          paramCodedOutputStreamMicro.writeInt32(1, getHour());
        }
        if (hasMinute()) {
          paramCodedOutputStreamMicro.writeInt32(2, getMinute());
        }
        if (hasSecond()) {
          paramCodedOutputStreamMicro.writeInt32(3, getSecond());
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.caribou.tasks.DateTimeProtos
 * JD-Core Version:    0.7.0.1
 */