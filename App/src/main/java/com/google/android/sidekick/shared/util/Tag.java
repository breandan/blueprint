package com.google.android.sidekick.shared.util;

public class Tag
{
  public static String getTag(Class<?> paramClass)
  {
    StringBuilder localStringBuilder = new StringBuilder("Sidekick_");
    String str = paramClass.getName();
    localStringBuilder.append(str.substring(1 + str.lastIndexOf('.')));
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.Tag
 * JD-Core Version:    0.7.0.1
 */