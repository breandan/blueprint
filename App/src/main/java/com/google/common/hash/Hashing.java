package com.google.common.hash;

public final class Hashing
{
  private static final HashFunction MD5 = new MessageDigestHashFunction("MD5");
  private static final Murmur3_128HashFunction MURMUR3_128;
  private static final Murmur3_32HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
  private static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1");
  private static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256");
  private static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512");
  
  static
  {
    MURMUR3_128 = new Murmur3_128HashFunction(0);
  }
  
  public static HashFunction sha1()
  {
    return SHA_1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.hash.Hashing
 * JD-Core Version:    0.7.0.1
 */