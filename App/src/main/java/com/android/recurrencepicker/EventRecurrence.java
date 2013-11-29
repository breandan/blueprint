package com.android.recurrencepicker;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;

public class EventRecurrence
{
  private static String TAG = "EventRecur";
  private static final HashMap<String, Integer> sParseFreqMap;
  private static HashMap<String, PartParser> sParsePartMap = new HashMap();
  private static final HashMap<String, Integer> sParseWeekdayMap;
  public int[] byday;
  public int bydayCount;
  public int[] bydayNum;
  public int[] byhour;
  public int byhourCount;
  public int[] byminute;
  public int byminuteCount;
  public int[] bymonth;
  public int bymonthCount;
  public int[] bymonthday;
  public int bymonthdayCount;
  public int[] bysecond;
  public int bysecondCount;
  public int[] bysetpos;
  public int bysetposCount;
  public int[] byweekno;
  public int byweeknoCount;
  public int[] byyearday;
  public int byyeardayCount;
  public int count;
  public int freq;
  public int interval;
  public Time startDate;
  public String until;
  public int wkst;
  
  static
  {
    sParsePartMap.put("FREQ", new ParseFreq(null));
    sParsePartMap.put("UNTIL", new ParseUntil(null));
    sParsePartMap.put("COUNT", new ParseCount(null));
    sParsePartMap.put("INTERVAL", new ParseInterval(null));
    sParsePartMap.put("BYSECOND", new ParseBySecond(null));
    sParsePartMap.put("BYMINUTE", new ParseByMinute(null));
    sParsePartMap.put("BYHOUR", new ParseByHour(null));
    sParsePartMap.put("BYDAY", new ParseByDay(null));
    sParsePartMap.put("BYMONTHDAY", new ParseByMonthDay(null));
    sParsePartMap.put("BYYEARDAY", new ParseByYearDay(null));
    sParsePartMap.put("BYWEEKNO", new ParseByWeekNo(null));
    sParsePartMap.put("BYMONTH", new ParseByMonth(null));
    sParsePartMap.put("BYSETPOS", new ParseBySetPos(null));
    sParsePartMap.put("WKST", new ParseWkst(null));
    sParseFreqMap = new HashMap();
    sParseFreqMap.put("SECONDLY", Integer.valueOf(1));
    sParseFreqMap.put("MINUTELY", Integer.valueOf(2));
    sParseFreqMap.put("HOURLY", Integer.valueOf(3));
    sParseFreqMap.put("DAILY", Integer.valueOf(4));
    sParseFreqMap.put("WEEKLY", Integer.valueOf(5));
    sParseFreqMap.put("MONTHLY", Integer.valueOf(6));
    sParseFreqMap.put("YEARLY", Integer.valueOf(7));
    sParseWeekdayMap = new HashMap();
    sParseWeekdayMap.put("SU", Integer.valueOf(65536));
    sParseWeekdayMap.put("MO", Integer.valueOf(131072));
    sParseWeekdayMap.put("TU", Integer.valueOf(262144));
    sParseWeekdayMap.put("WE", Integer.valueOf(524288));
    sParseWeekdayMap.put("TH", Integer.valueOf(1048576));
    sParseWeekdayMap.put("FR", Integer.valueOf(2097152));
    sParseWeekdayMap.put("SA", Integer.valueOf(4194304));
  }
  
  private void appendByDay(StringBuilder paramStringBuilder, int paramInt)
  {
    if (this.bydayNum != null)
    {
      int i = this.bydayNum[paramInt];
      if (i != 0) {
        paramStringBuilder.append(i);
      }
    }
    paramStringBuilder.append(day2String(this.byday[paramInt]));
  }
  
  private static void appendNumbers(StringBuilder paramStringBuilder, String paramString, int paramInt, int[] paramArrayOfInt)
  {
    if (paramInt > 0)
    {
      paramStringBuilder.append(paramString);
      int i = paramInt - 1;
      for (int j = 0; j < i; j++)
      {
        paramStringBuilder.append(paramArrayOfInt[j]);
        paramStringBuilder.append(",");
      }
      paramStringBuilder.append(paramArrayOfInt[i]);
    }
  }
  
  private static boolean arraysEqual(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2)
  {
    if (paramInt1 != paramInt2) {
      return false;
    }
    for (int i = 0;; i++)
    {
      if (i >= paramInt1) {
        break label33;
      }
      if (paramArrayOfInt1[i] != paramArrayOfInt2[i]) {
        break;
      }
    }
    label33:
    return true;
  }
  
  private static String day2String(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("bad day argument: " + paramInt);
    case 65536: 
      return "SU";
    case 131072: 
      return "MO";
    case 262144: 
      return "TU";
    case 524288: 
      return "WE";
    case 1048576: 
      return "TH";
    case 2097152: 
      return "FR";
    }
    return "SA";
  }
  
  public static int day2TimeDay(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new RuntimeException("bad day of week: " + paramInt);
    case 65536: 
      return 0;
    case 131072: 
      return 1;
    case 262144: 
      return 2;
    case 524288: 
      return 3;
    case 1048576: 
      return 4;
    case 2097152: 
      return 5;
    }
    return 6;
  }
  
  private void resetFields()
  {
    this.until = null;
    this.bysetposCount = 0;
    this.bymonthCount = 0;
    this.byweeknoCount = 0;
    this.byyeardayCount = 0;
    this.bymonthdayCount = 0;
    this.bydayCount = 0;
    this.byhourCount = 0;
    this.byminuteCount = 0;
    this.bysecondCount = 0;
    this.interval = 0;
    this.count = 0;
    this.freq = 0;
  }
  
  public static int timeDay2Day(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new RuntimeException("bad day of week: " + paramInt);
    case 0: 
      return 65536;
    case 1: 
      return 131072;
    case 2: 
      return 262144;
    case 3: 
      return 524288;
    case 4: 
      return 1048576;
    case 5: 
      return 2097152;
    }
    return 4194304;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    label332:
    label347:
    for (;;)
    {
      return true;
      if (!(paramObject instanceof EventRecurrence)) {
        return false;
      }
      EventRecurrence localEventRecurrence = (EventRecurrence)paramObject;
      if (this.startDate == null) {
        if (localEventRecurrence.startDate == null) {
          if (this.freq == localEventRecurrence.freq)
          {
            if (this.until != null) {
              break label332;
            }
            if (localEventRecurrence.until != null) {}
          }
        }
      }
      for (;;)
      {
        if ((this.count == localEventRecurrence.count) && (this.interval == localEventRecurrence.interval) && (this.wkst == localEventRecurrence.wkst) && (arraysEqual(this.bysecond, this.bysecondCount, localEventRecurrence.bysecond, localEventRecurrence.bysecondCount)) && (arraysEqual(this.byminute, this.byminuteCount, localEventRecurrence.byminute, localEventRecurrence.byminuteCount)) && (arraysEqual(this.byhour, this.byhourCount, localEventRecurrence.byhour, localEventRecurrence.byhourCount)) && (arraysEqual(this.byday, this.bydayCount, localEventRecurrence.byday, localEventRecurrence.bydayCount)) && (arraysEqual(this.bydayNum, this.bydayCount, localEventRecurrence.bydayNum, localEventRecurrence.bydayCount)) && (arraysEqual(this.bymonthday, this.bymonthdayCount, localEventRecurrence.bymonthday, localEventRecurrence.bymonthdayCount)) && (arraysEqual(this.byyearday, this.byyeardayCount, localEventRecurrence.byyearday, localEventRecurrence.byyeardayCount)) && (arraysEqual(this.byweekno, this.byweeknoCount, localEventRecurrence.byweekno, localEventRecurrence.byweeknoCount)) && (arraysEqual(this.bymonth, this.bymonthCount, localEventRecurrence.bymonth, localEventRecurrence.bymonthCount)) && (arraysEqual(this.bysetpos, this.bysetposCount, localEventRecurrence.bysetpos, localEventRecurrence.bysetposCount))) {
          break label347;
        }
        do
        {
          do
          {
            return false;
          } while (Time.compare(this.startDate, localEventRecurrence.startDate) != 0);
          break;
        } while (!this.until.equals(localEventRecurrence.until));
      }
    }
  }
  
  public int hashCode()
  {
    throw new UnsupportedOperationException();
  }
  
  public void parse(String paramString)
  {
    resetFields();
    int i = 0;
    String[] arrayOfString = paramString.toUpperCase(Locale.getDefault()).split(";");
    int j = arrayOfString.length;
    int k = 0;
    if (k < j)
    {
      String str1 = arrayOfString[k];
      if (TextUtils.isEmpty(str1)) {}
      for (;;)
      {
        k++;
        break;
        int m = str1.indexOf('=');
        if (m <= 0) {
          throw new InvalidFormatException("Missing LHS in " + str1);
        }
        String str2 = str1.substring(0, m);
        String str3 = str1.substring(m + 1);
        if (str3.length() == 0) {
          throw new InvalidFormatException("Missing RHS in " + str1);
        }
        PartParser localPartParser = (PartParser)sParsePartMap.get(str2);
        if (localPartParser == null)
        {
          if (!str2.startsWith("X-")) {
            throw new InvalidFormatException("Couldn't find parser for " + str2);
          }
        }
        else
        {
          int n = localPartParser.parsePart(str3, this);
          if ((i & n) != 0) {
            throw new InvalidFormatException("Part " + str2 + " was specified twice");
          }
          i |= n;
        }
      }
    }
    if ((i & 0x2000) == 0) {
      this.wkst = 131072;
    }
    if ((i & 0x1) == 0) {
      throw new InvalidFormatException("Must specify a FREQ value");
    }
    if ((i & 0x6) == 6) {
      Log.w(TAG, "Warning: rrule has both UNTIL and COUNT: " + paramString);
    }
  }
  
  public boolean repeatsOnEveryWeekDay()
  {
    if (this.freq != 5) {}
    int i;
    do
    {
      return false;
      i = this.bydayCount;
    } while (i != 5);
    for (int j = 0;; j++)
    {
      if (j >= i) {
        break label52;
      }
      int k = this.byday[j];
      if ((k == 65536) || (k == 4194304)) {
        break;
      }
    }
    label52:
    return true;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FREQ=");
    switch (this.freq)
    {
    }
    int j;
    for (;;)
    {
      if (!TextUtils.isEmpty(this.until))
      {
        localStringBuilder.append(";UNTIL=");
        localStringBuilder.append(this.until);
      }
      if (this.count != 0)
      {
        localStringBuilder.append(";COUNT=");
        localStringBuilder.append(this.count);
      }
      if (this.interval != 0)
      {
        localStringBuilder.append(";INTERVAL=");
        localStringBuilder.append(this.interval);
      }
      if (this.wkst != 0)
      {
        localStringBuilder.append(";WKST=");
        localStringBuilder.append(day2String(this.wkst));
      }
      appendNumbers(localStringBuilder, ";BYSECOND=", this.bysecondCount, this.bysecond);
      appendNumbers(localStringBuilder, ";BYMINUTE=", this.byminuteCount, this.byminute);
      appendNumbers(localStringBuilder, ";BYSECOND=", this.byhourCount, this.byhour);
      int i = this.bydayCount;
      if (i <= 0) {
        break label343;
      }
      localStringBuilder.append(";BYDAY=");
      j = i - 1;
      for (int k = 0; k < j; k++)
      {
        appendByDay(localStringBuilder, k);
        localStringBuilder.append(",");
      }
      localStringBuilder.append("SECONDLY");
      continue;
      localStringBuilder.append("MINUTELY");
      continue;
      localStringBuilder.append("HOURLY");
      continue;
      localStringBuilder.append("DAILY");
      continue;
      localStringBuilder.append("WEEKLY");
      continue;
      localStringBuilder.append("MONTHLY");
      continue;
      localStringBuilder.append("YEARLY");
    }
    appendByDay(localStringBuilder, j);
    label343:
    appendNumbers(localStringBuilder, ";BYMONTHDAY=", this.bymonthdayCount, this.bymonthday);
    appendNumbers(localStringBuilder, ";BYYEARDAY=", this.byyeardayCount, this.byyearday);
    appendNumbers(localStringBuilder, ";BYWEEKNO=", this.byweeknoCount, this.byweekno);
    appendNumbers(localStringBuilder, ";BYMONTH=", this.bymonthCount, this.bymonth);
    appendNumbers(localStringBuilder, ";BYSETPOS=", this.bysetposCount, this.bysetpos);
    return localStringBuilder.toString();
  }
  
  public static class InvalidFormatException
    extends RuntimeException
  {
    InvalidFormatException(String paramString)
    {
      super();
    }
  }
  
  private static class ParseByDay
    extends EventRecurrence.PartParser
  {
    private static void parseWday(String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
    {
      int i = -2 + paramString.length();
      if (i > 0) {
        paramArrayOfInt2[paramInt] = parseIntRange(paramString.substring(0, i), -53, 53, false);
      }
      Integer localInteger;
      for (String str = paramString.substring(i);; str = paramString)
      {
        localInteger = (Integer)EventRecurrence.sParseWeekdayMap.get(str);
        if (localInteger != null) {
          break;
        }
        throw new EventRecurrence.InvalidFormatException("Invalid BYDAY value: " + paramString);
      }
      paramArrayOfInt1[paramInt] = localInteger.intValue();
    }
    
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int j;
      int[] arrayOfInt1;
      int[] arrayOfInt2;
      if (paramString.indexOf(",") < 0)
      {
        j = 1;
        arrayOfInt1 = new int[1];
        arrayOfInt2 = new int[1];
        parseWday(paramString, arrayOfInt1, arrayOfInt2, 0);
      }
      for (;;)
      {
        paramEventRecurrence.byday = arrayOfInt1;
        paramEventRecurrence.bydayNum = arrayOfInt2;
        paramEventRecurrence.bydayCount = j;
        return 128;
        String[] arrayOfString = paramString.split(",");
        int i = arrayOfString.length;
        j = i;
        arrayOfInt1 = new int[i];
        arrayOfInt2 = new int[i];
        for (int k = 0; k < i; k++) {
          parseWday(arrayOfString[k], arrayOfInt1, arrayOfInt2, k);
        }
      }
    }
  }
  
  private static class ParseByHour
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, 0, 23, true);
      paramEventRecurrence.byhour = arrayOfInt;
      paramEventRecurrence.byhourCount = arrayOfInt.length;
      return 64;
    }
  }
  
  private static class ParseByMinute
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, 0, 59, true);
      paramEventRecurrence.byminute = arrayOfInt;
      paramEventRecurrence.byminuteCount = arrayOfInt.length;
      return 32;
    }
  }
  
  private static class ParseByMonth
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, 1, 12, false);
      paramEventRecurrence.bymonth = arrayOfInt;
      paramEventRecurrence.bymonthCount = arrayOfInt.length;
      return 2048;
    }
  }
  
  private static class ParseByMonthDay
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, -31, 31, false);
      paramEventRecurrence.bymonthday = arrayOfInt;
      paramEventRecurrence.bymonthdayCount = arrayOfInt.length;
      return 256;
    }
  }
  
  private static class ParseBySecond
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, 0, 59, true);
      paramEventRecurrence.bysecond = arrayOfInt;
      paramEventRecurrence.bysecondCount = arrayOfInt.length;
      return 16;
    }
  }
  
  private static class ParseBySetPos
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, -2147483648, 2147483647, true);
      paramEventRecurrence.bysetpos = arrayOfInt;
      paramEventRecurrence.bysetposCount = arrayOfInt.length;
      return 4096;
    }
  }
  
  private static class ParseByWeekNo
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, -53, 53, false);
      paramEventRecurrence.byweekno = arrayOfInt;
      paramEventRecurrence.byweeknoCount = arrayOfInt.length;
      return 1024;
    }
  }
  
  private static class ParseByYearDay
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      int[] arrayOfInt = parseNumberList(paramString, -366, 366, false);
      paramEventRecurrence.byyearday = arrayOfInt;
      paramEventRecurrence.byyeardayCount = arrayOfInt.length;
      return 512;
    }
  }
  
  private static class ParseCount
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      paramEventRecurrence.count = parseIntRange(paramString, -2147483648, 2147483647, true);
      if (paramEventRecurrence.count < 0)
      {
        Log.d(EventRecurrence.TAG, "Invalid Count. Forcing COUNT to 1 from " + paramString);
        paramEventRecurrence.count = 1;
      }
      return 4;
    }
  }
  
  private static class ParseFreq
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      Integer localInteger = (Integer)EventRecurrence.sParseFreqMap.get(paramString);
      if (localInteger == null) {
        throw new EventRecurrence.InvalidFormatException("Invalid FREQ value: " + paramString);
      }
      paramEventRecurrence.freq = localInteger.intValue();
      return 1;
    }
  }
  
  private static class ParseInterval
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      paramEventRecurrence.interval = parseIntRange(paramString, -2147483648, 2147483647, true);
      if (paramEventRecurrence.interval < 1)
      {
        Log.d(EventRecurrence.TAG, "Invalid Interval. Forcing INTERVAL to 1 from " + paramString);
        paramEventRecurrence.interval = 1;
      }
      return 8;
    }
  }
  
  private static class ParseUntil
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      paramEventRecurrence.until = paramString;
      return 2;
    }
  }
  
  private static class ParseWkst
    extends EventRecurrence.PartParser
  {
    public int parsePart(String paramString, EventRecurrence paramEventRecurrence)
    {
      Integer localInteger = (Integer)EventRecurrence.sParseWeekdayMap.get(paramString);
      if (localInteger == null) {
        throw new EventRecurrence.InvalidFormatException("Invalid WKST value: " + paramString);
      }
      paramEventRecurrence.wkst = localInteger.intValue();
      return 8192;
    }
  }
  
  static abstract class PartParser
  {
    public static int parseIntRange(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int i;
      try
      {
        if (paramString.charAt(0) == '+') {
          paramString = paramString.substring(1);
        }
        i = Integer.parseInt(paramString);
        if ((i < paramInt1) || (i > paramInt2) || ((i == 0) && (!paramBoolean))) {
          throw new EventRecurrence.InvalidFormatException("Integer value out of range: " + paramString);
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new EventRecurrence.InvalidFormatException("Invalid integer value: " + paramString);
      }
      return i;
    }
    
    public static int[] parseNumberList(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int[] arrayOfInt;
      if (paramString.indexOf(",") < 0)
      {
        arrayOfInt = new int[1];
        arrayOfInt[0] = parseIntRange(paramString, paramInt1, paramInt2, paramBoolean);
      }
      for (;;)
      {
        return arrayOfInt;
        String[] arrayOfString = paramString.split(",");
        int i = arrayOfString.length;
        arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) {
          arrayOfInt[j] = parseIntRange(arrayOfString[j], paramInt1, paramInt2, paramBoolean);
        }
      }
    }
    
    public abstract int parsePart(String paramString, EventRecurrence paramEventRecurrence);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.recurrencepicker.EventRecurrence
 * JD-Core Version:    0.7.0.1
 */