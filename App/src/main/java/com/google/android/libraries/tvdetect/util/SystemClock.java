package com.google.android.libraries.tvdetect.util;

public class SystemClock
  implements Clock
{
  public static final SystemClock INSTANCE = new SystemClock();
  
  public long getCurrentTimeMillis()
  {
    return System.currentTimeMillis();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.SystemClock
 * JD-Core Version:    0.7.0.1
 */