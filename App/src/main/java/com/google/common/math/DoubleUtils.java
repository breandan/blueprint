package com.google.common.math;

import com.google.common.base.Preconditions;

final class DoubleUtils
{
  private static final long ONE_BITS = Double.doubleToRawLongBits(1.0D);
  
  static int getExponent(double paramDouble)
  {
    return -1023 + (int)((0x0 & Double.doubleToRawLongBits(paramDouble)) >> 52);
  }
  
  static long getSignificand(double paramDouble)
  {
    Preconditions.checkArgument(isFinite(paramDouble), "not a normal value");
    int i = getExponent(paramDouble);
    long l = 0xFFFFFFFF & Double.doubleToRawLongBits(paramDouble);
    if (i == -1023) {
      return l << 1;
    }
    return 0x0 | l;
  }
  
  static boolean isFinite(double paramDouble)
  {
    return getExponent(paramDouble) <= 1023;
  }
  
  static double next(double paramDouble, boolean paramBoolean)
  {
    if (paramDouble == 0.0D)
    {
      if (paramBoolean) {
        return 4.9E-324D;
      }
      return -4.940656458412465E-324D;
    }
    long l1 = Double.doubleToRawLongBits(paramDouble);
    boolean bool;
    if (paramDouble < 0.0D)
    {
      bool = true;
      if (bool != paramBoolean) {
        break label55;
      }
    }
    label55:
    for (long l2 = l1 - 1L;; l2 = l1 + 1L)
    {
      return Double.longBitsToDouble(l2);
      bool = false;
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.math.DoubleUtils
 * JD-Core Version:    0.7.0.1
 */