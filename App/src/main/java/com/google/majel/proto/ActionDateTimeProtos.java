package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class ActionDateTimeProtos
{
  public static final class ActionDate
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int day_ = 0;
    private boolean hasDay;
    private boolean hasMonth;
    private boolean hasYear;
    private int month_ = 0;
    private int year_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDay()
    {
      return this.day_;
    }
    
    public int getMonth()
    {
      return this.month_;
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
      this.cachedSize = i;
      return i;
    }
    
    public int getYear()
    {
      return this.year_;
    }
    
    public boolean hasDay()
    {
      return this.hasDay;
    }
    
    public boolean hasMonth()
    {
      return this.hasMonth;
    }
    
    public boolean hasYear()
    {
      return this.hasYear;
    }
    
    public ActionDate mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        }
        setDay(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public ActionDate setDay(int paramInt)
    {
      this.hasDay = true;
      this.day_ = paramInt;
      return this;
    }
    
    public ActionDate setMonth(int paramInt)
    {
      this.hasMonth = true;
      this.month_ = paramInt;
      return this;
    }
    
    public ActionDate setYear(int paramInt)
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
    }
  }
  
  public static final class ActionTime
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
    
    public ActionTime mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
    
    public ActionTime setHour(int paramInt)
    {
      this.hasHour = true;
      this.hour_ = paramInt;
      return this;
    }
    
    public ActionTime setMinute(int paramInt)
    {
      this.hasMinute = true;
      this.minute_ = paramInt;
      return this;
    }
    
    public ActionTime setSecond(int paramInt)
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


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.ActionDateTimeProtos
 * JD-Core Version:    0.7.0.1
 */