package com.google.android.libraries.tvdetect.util;

public class StringUtil
{
  public static void appendKeyValue(StringBuilder paramStringBuilder, String paramString1, String paramString2)
  {
    if (paramStringBuilder.length() > 1) {
      paramStringBuilder.append(", ");
    }
    paramStringBuilder.append(paramString1).append(": ").append(paramString2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.StringUtil
 * JD-Core Version:    0.7.0.1
 */