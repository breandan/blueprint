package com.google.common.primitives;

import com.google.common.base.Preconditions;

public final class Ints
{
  public static int checkedCast(long paramLong)
  {
    int i = (int)paramLong;
    if (i == paramLong) {}
    for (boolean bool = true;; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(paramLong);
      Preconditions.checkArgument(bool, "Out of range: %s", arrayOfObject);
      return i;
    }
  }
  
  public static int compare(int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2) {
      return -1;
    }
    if (paramInt1 > paramInt2) {
      return 1;
    }
    return 0;
  }
  
  public static int saturatedCast(long paramLong)
  {
    if (paramLong > 2147483647L) {
      return 2147483647;
    }
    if (paramLong < -2147483648L) {
      return -2147483648;
    }
    return (int)paramLong;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.primitives.Ints
 * JD-Core Version:    0.7.0.1
 */