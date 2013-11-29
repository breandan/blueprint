package com.google.android.search.core.util;

import com.google.android.shared.util.Util;

public final class Utf8StringPool
{
  private final Object[] pool = new Object[512];
  
  private int bucket(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = paramInt1; j < paramInt1 + paramInt2; j++) {
      i = i * 31 + (0xFF & paramArrayOfByte[j]);
    }
    int k = i ^ i >>> 20 ^ i >>> 12;
    return (k ^ k >>> 7 ^ k >>> 4) & -1 + this.pool.length;
  }
  
  private static boolean contentEquals(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramString.length() != paramInt2) {
      return false;
    }
    for (int i = 0;; i++)
    {
      if (i >= paramInt2) {
        break label40;
      }
      if (paramArrayOfByte[(paramInt1 + i)] != paramString.charAt(i)) {
        break;
      }
    }
    label40:
    return true;
  }
  
  private static boolean contentEquals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte1.length != paramInt2) {
      return false;
    }
    for (int i = 0;; i++)
    {
      if (i >= paramInt2) {
        break label36;
      }
      if (paramArrayOfByte2[(paramInt1 + i)] != paramArrayOfByte1[i]) {
        break;
      }
    }
    label36:
    return true;
  }
  
  private byte[] extractBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    if (paramInt2 != 0) {
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    }
    return arrayOfByte;
  }
  
  public byte[] getBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 > 512) {
      return extractBytes(paramArrayOfByte, paramInt1, paramInt2);
    }
    int i = bucket(paramArrayOfByte, paramInt1, paramInt2);
    Object localObject = this.pool[i];
    if (((localObject instanceof byte[])) && (contentEquals((byte[])localObject, paramArrayOfByte, paramInt1, paramInt2))) {
      return (byte[])localObject;
    }
    byte[] arrayOfByte = extractBytes(paramArrayOfByte, paramInt1, paramInt2);
    this.pool[i] = arrayOfByte;
    return arrayOfByte;
  }
  
  public String getString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 > 512) {
      return new String(paramArrayOfByte, paramInt1, paramInt2, Util.UTF_8);
    }
    int i = bucket(paramArrayOfByte, paramInt1, paramInt2);
    Object localObject = this.pool[i];
    if (((localObject instanceof String)) && (contentEquals((String)localObject, paramArrayOfByte, paramInt1, paramInt2))) {
      return (String)localObject;
    }
    String str = new String(paramArrayOfByte, paramInt1, paramInt2, Util.UTF_8);
    if (str.length() == paramInt2) {
      this.pool[i] = str;
    }
    return str;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.Utf8StringPool
 * JD-Core Version:    0.7.0.1
 */