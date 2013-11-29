package com.google.android.search.core.discoursecontext;

public class Mention
  implements Comparable<Mention>
{
  private final long mTimeMs;
  
  public Mention(long paramLong)
  {
    this.mTimeMs = paramLong;
  }
  
  public int compareTo(Mention paramMention)
  {
    if (this.mTimeMs < paramMention.mTimeMs) {
      return 1;
    }
    if (this.mTimeMs > paramMention.mTimeMs) {
      return -1;
    }
    return 0;
  }
  
  public long getTimeMs()
  {
    return this.mTimeMs;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.discoursecontext.Mention
 * JD-Core Version:    0.7.0.1
 */