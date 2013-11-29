package com.google.analytics.tracking.android;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class Utils
{
  private static final char[] HEXBYTES = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  
  public static String filterCampaign(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    Object localObject = paramString;
    if (paramString.contains("?")) {
      localObject = paramString.split("[\\?]")[1];
    }
    StringBuilder localStringBuilder;
    if (((String)localObject).contains("%3D")) {
      try
      {
        String str = URLDecoder.decode((String)localObject, "UTF-8");
        localObject = str;
        do
        {
          Map localMap = parseURLParameters((String)localObject);
          String[] arrayOfString = { "dclid", "utm_source", "gclid", "utm_campaign", "utm_medium", "utm_term", "utm_content", "utm_id", "gmob_t" };
          localStringBuilder = new StringBuilder();
          for (int i = 0; i < arrayOfString.length; i++) {
            if (!TextUtils.isEmpty((CharSequence)localMap.get(arrayOfString[i])))
            {
              if (localStringBuilder.length() > 0) {
                localStringBuilder.append("&");
              }
              localStringBuilder.append(arrayOfString[i]).append("=").append((String)localMap.get(arrayOfString[i]));
            }
          }
        } while (((String)localObject).contains("="));
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        return null;
      }
    } else {
      return null;
    }
    return localStringBuilder.toString();
  }
  
  static int fromHexDigit(char paramChar)
  {
    int i = paramChar - '0';
    if (i > 9) {
      i -= 7;
    }
    return i;
  }
  
  static String getLanguage(Locale paramLocale)
  {
    if (paramLocale == null) {}
    while (TextUtils.isEmpty(paramLocale.getLanguage())) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramLocale.getLanguage().toLowerCase());
    if (!TextUtils.isEmpty(paramLocale.getCountry())) {
      localStringBuilder.append("-").append(paramLocale.getCountry().toLowerCase());
    }
    return localStringBuilder.toString();
  }
  
  static byte[] hexDecode(String paramString)
  {
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    for (int i = 0; i < arrayOfByte.length; i++) {
      arrayOfByte[i] = ((byte)(fromHexDigit(paramString.charAt(i * 2)) << 4 | fromHexDigit(paramString.charAt(1 + i * 2))));
    }
    return arrayOfByte;
  }
  
  public static Map<String, String> parseURLParameters(String paramString)
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1 = paramString.split("&");
    int i = arrayOfString1.length;
    int j = 0;
    if (j < i)
    {
      String[] arrayOfString2 = arrayOfString1[j].split("=");
      if (arrayOfString2.length > 1) {
        localHashMap.put(arrayOfString2[0], arrayOfString2[1]);
      }
      for (;;)
      {
        j++;
        break;
        if ((arrayOfString2.length == 1) && (arrayOfString2[0].length() != 0)) {
          localHashMap.put(arrayOfString2[0], null);
        }
      }
    }
    return localHashMap;
  }
  
  public static boolean safeParseBoolean(String paramString)
  {
    return Boolean.parseBoolean(paramString);
  }
  
  public static double safeParseDouble(String paramString)
  {
    if (paramString == null) {
      return 0.0D;
    }
    try
    {
      double d = Double.parseDouble(paramString);
      return d;
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0.0D;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.Utils
 * JD-Core Version:    0.7.0.1
 */