package com.google.common.collect;

final class Hashing
{
  static int smear(int paramInt)
  {
    int i = paramInt ^ paramInt >>> 20 ^ paramInt >>> 12;
    return i ^ i >>> 7 ^ i >>> 4;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Hashing
 * JD-Core Version:    0.7.0.1
 */