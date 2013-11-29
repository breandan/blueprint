package com.google.common.primitives;

import com.google.common.base.Preconditions;

public final class Longs
{
  public static long fromByteArray(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length >= 8) {}
    for (boolean bool = true;; bool = false)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(paramArrayOfByte.length);
      arrayOfObject[1] = Integer.valueOf(8);
      Preconditions.checkArgument(bool, "array too small: %s < %s", arrayOfObject);
      return fromBytes(paramArrayOfByte[0], paramArrayOfByte[1], paramArrayOfByte[2], paramArrayOfByte[3], paramArrayOfByte[4], paramArrayOfByte[5], paramArrayOfByte[6], paramArrayOfByte[7]);
    }
  }
  
  public static long fromBytes(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6, byte paramByte7, byte paramByte8)
  {
    return (0xFF & paramByte1) << 56 | (0xFF & paramByte2) << 48 | (0xFF & paramByte3) << 40 | (0xFF & paramByte4) << 32 | (0xFF & paramByte5) << 24 | (0xFF & paramByte6) << 16 | (0xFF & paramByte7) << 8 | 0xFF & paramByte8;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.primitives.Longs
 * JD-Core Version:    0.7.0.1
 */