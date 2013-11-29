package com.google.common.base;

import javax.annotation.Nullable;

public final class Strings
{
  public static boolean isNullOrEmpty(@Nullable String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }
  
  public static String nullToEmpty(@Nullable String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    return paramString;
  }
  
  public static String repeat(String paramString, int paramInt)
  {
    Preconditions.checkNotNull(paramString);
    if (paramInt <= 1)
    {
      if (paramInt >= 0) {}
      for (boolean bool = true;; bool = false)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(paramInt);
        Preconditions.checkArgument(bool, "invalid count: %s", arrayOfObject);
        if (paramInt == 0) {
          paramString = "";
        }
        return paramString;
      }
    }
    int i = paramString.length();
    long l = i * paramInt;
    int j = (int)l;
    if (j != l) {
      throw new ArrayIndexOutOfBoundsException("Required array size too large: " + String.valueOf(l));
    }
    char[] arrayOfChar = new char[j];
    paramString.getChars(0, i, arrayOfChar, 0);
    int k = i;
    while (k < j - k)
    {
      System.arraycopy(arrayOfChar, 0, arrayOfChar, k, k);
      k <<= 1;
    }
    System.arraycopy(arrayOfChar, 0, arrayOfChar, k, j - k);
    return new String(arrayOfChar);
  }
  
  static boolean validSurrogatePairAt(CharSequence paramCharSequence, int paramInt)
  {
    return (paramInt >= 0) && (paramInt <= -2 + paramCharSequence.length()) && (Character.isHighSurrogate(paramCharSequence.charAt(paramInt))) && (Character.isLowSurrogate(paramCharSequence.charAt(paramInt + 1)));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.base.Strings
 * JD-Core Version:    0.7.0.1
 */