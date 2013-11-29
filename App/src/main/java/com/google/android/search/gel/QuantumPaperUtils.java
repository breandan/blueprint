package com.google.android.search.gel;

public class QuantumPaperUtils
{
  public static float getFillRadius(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return (float)Math.sqrt(Math.pow(Math.max(paramInt1 - paramInt3, paramInt3), 2.0D) + Math.pow(Math.max(paramInt2 - paramInt4, paramInt4), 2.0D));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.QuantumPaperUtils
 * JD-Core Version:    0.7.0.1
 */