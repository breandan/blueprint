package com.google.common.hash;

public abstract interface Hasher
{
  public abstract HashCode hash();
  
  public abstract Hasher putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.hash.Hasher
 * JD-Core Version:    0.7.0.1
 */