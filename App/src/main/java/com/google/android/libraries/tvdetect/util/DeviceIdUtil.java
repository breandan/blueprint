package com.google.android.libraries.tvdetect.util;

public class DeviceIdUtil
{
  public static String getUuidFromUsn(String paramString)
  {
    if (!paramString.startsWith("uuid:")) {
      return null;
    }
    int i = paramString.indexOf("::");
    if (i == -1) {
      return paramString.substring("uuid:".length());
    }
    return paramString.substring("uuid:".length(), i);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.DeviceIdUtil
 * JD-Core Version:    0.7.0.1
 */