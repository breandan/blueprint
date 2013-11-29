package com.google.android.libraries.tvdetect.util;

import android.net.Uri.Builder;

public class UriUtil
{
  public static void appendKeyValue(Uri.Builder paramBuilder, String paramString1, String paramString2)
  {
    if (paramString2 != null) {
      paramBuilder.appendQueryParameter(paramString1, paramString2);
    }
  }
  
  public static Uri.Builder newUriBuilder(Class paramClass)
  {
    Uri.Builder localBuilder = new Uri.Builder();
    localBuilder.scheme("tvd");
    localBuilder.authority(paramClass.getCanonicalName());
    localBuilder.path("/");
    return localBuilder;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.UriUtil
 * JD-Core Version:    0.7.0.1
 */