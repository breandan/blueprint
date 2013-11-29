package com.google.android.search.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import com.google.android.search.core.google.SearchUrlHelper;
import java.util.Locale;

class WebViewUtils
{
  public static String getCurrentUserAgent(Context paramContext)
  {
    Locale localLocale = Locale.getDefault();
    StringBuffer localStringBuffer = new StringBuffer();
    String str1 = Build.VERSION.RELEASE;
    if (str1.length() > 0) {
      if (Character.isDigit(str1.charAt(0)))
      {
        localStringBuffer.append(str1);
        localStringBuffer.append("; ");
        String str2 = localLocale.getLanguage();
        if (str2 == null) {
          break label241;
        }
        localStringBuffer.append(SearchUrlHelper.convertObsoleteLanguageCodeToNew(str2));
        String str7 = localLocale.getCountry();
        if (str7 != null)
        {
          localStringBuffer.append("-");
          localStringBuffer.append(str7.toLowerCase(Locale.US));
        }
      }
    }
    for (;;)
    {
      localStringBuffer.append(";");
      if ("REL".equals(Build.VERSION.CODENAME))
      {
        String str6 = Build.MODEL;
        if (str6.length() > 0)
        {
          localStringBuffer.append(" ");
          localStringBuffer.append(str6);
        }
      }
      String str3 = Build.ID;
      if (str3.length() > 0)
      {
        localStringBuffer.append(" Build/");
        localStringBuffer.append(str3);
      }
      String str4 = paramContext.getResources().getText(2131363706).toString();
      String str5 = paramContext.getResources().getText(2131363705).toString();
      return String.format(Locale.US, str5, new Object[] { localStringBuffer, str4 });
      localStringBuffer.append("4.1");
      break;
      localStringBuffer.append("1.0");
      break;
      label241:
      localStringBuffer.append("en");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.WebViewUtils
 * JD-Core Version:    0.7.0.1
 */