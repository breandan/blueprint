package com.android.gallery3d.common;

import android.os.Build;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;

public class Utils
{
  private static final boolean IS_DEBUG_BUILD;
  private static long[] sCrcTable = new long[256];
  
  static
  {
    boolean bool;
    if ((Build.TYPE.equals("eng")) || (Build.TYPE.equals("userdebug")))
    {
      bool = true;
      IS_DEBUG_BUILD = bool;
    }
    for (int i = 0;; i++)
    {
      if (i >= 256) {
        return;
      }
      long l1 = i;
      int j = 0;
      label51:
      if (j < 8)
      {
        if ((0x1 & (int)l1) != 0) {}
        for (long l2 = -7661587058870466123L;; l2 = 0L)
        {
          l1 = l2 ^ l1 >> 1;
          j++;
          break label51;
          bool = false;
          break;
        }
      }
      sCrcTable[i] = l1;
    }
  }
  
  public static void assertTrue(boolean paramBoolean)
  {
    if (!paramBoolean) {
      throw new AssertionError();
    }
  }
  
  public static int ceilLog2(float paramFloat)
  {
    for (int i = 0;; i++) {
      if ((i >= 31) || (1 << i >= paramFloat)) {
        return i;
      }
    }
  }
  
  public static int clamp(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 > paramInt3) {
      return paramInt3;
    }
    if (paramInt1 < paramInt2) {
      return paramInt2;
    }
    return paramInt1;
  }
  
  public static void closeSilently(Closeable paramCloseable)
  {
    if (paramCloseable == null) {
      return;
    }
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException)
    {
      Log.w("Utils", "close fail ", localIOException);
    }
  }
  
  public static int floorLog2(float paramFloat)
  {
    for (int i = 0;; i++) {
      if ((i >= 31) || (1 << i > paramFloat)) {
        return i - 1;
      }
    }
  }
  
  public static int nextPowerOf2(int paramInt)
  {
    if ((paramInt <= 0) || (paramInt > 1073741824)) {
      throw new IllegalArgumentException("n is invalid: " + paramInt);
    }
    int i = paramInt - 1;
    int j = i | i >> 16;
    int k = j | j >> 8;
    int m = k | k >> 4;
    int n = m | m >> 2;
    return 1 + (n | n >> 1);
  }
  
  public static int prevPowerOf2(int paramInt)
  {
    if (paramInt <= 0) {
      throw new IllegalArgumentException();
    }
    return Integer.highestOneBit(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.common.Utils
 * JD-Core Version:    0.7.0.1
 */