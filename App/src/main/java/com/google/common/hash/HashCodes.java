package com.google.common.hash;

final class HashCodes
{
  static HashCode fromBytes(byte[] paramArrayOfByte)
  {
    return new BytesHashCode(paramArrayOfByte);
  }
  
  static HashCode fromInt(int paramInt)
  {
    return new IntHashCode(paramInt);
  }
  
  private static class BytesHashCode
    extends HashCode
  {
    final byte[] bytes;
    
    BytesHashCode(byte[] paramArrayOfByte)
    {
      this.bytes = paramArrayOfByte;
    }
    
    public byte[] asBytes()
    {
      return (byte[])this.bytes.clone();
    }
    
    public int asInt()
    {
      return 0xFF & this.bytes[0] | (0xFF & this.bytes[1]) << 8 | (0xFF & this.bytes[2]) << 16 | (0xFF & this.bytes[3]) << 24;
    }
  }
  
  private static class IntHashCode
    extends HashCode
  {
    final int hash;
    
    IntHashCode(int paramInt)
    {
      this.hash = paramInt;
    }
    
    public byte[] asBytes()
    {
      byte[] arrayOfByte = new byte[4];
      arrayOfByte[0] = ((byte)this.hash);
      arrayOfByte[1] = ((byte)(this.hash >> 8));
      arrayOfByte[2] = ((byte)(this.hash >> 16));
      arrayOfByte[3] = ((byte)(this.hash >> 24));
      return arrayOfByte;
    }
    
    public int asInt()
    {
      return this.hash;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.hash.HashCodes
 * JD-Core Version:    0.7.0.1
 */