package com.google.common.base;

public final class Ascii
{
  public static boolean isUpperCase(char paramChar)
  {
    return (paramChar >= 'A') && (paramChar <= 'Z');
  }
  
  public static char toLowerCase(char paramChar)
  {
    if (isUpperCase(paramChar)) {
      paramChar = (char)(paramChar ^ 0x20);
    }
    return paramChar;
  }
  
  public static String toLowerCase(String paramString)
  {
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++) {
      localStringBuilder.append(toLowerCase(paramString.charAt(j)));
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.base.Ascii
 * JD-Core Version:    0.7.0.1
 */