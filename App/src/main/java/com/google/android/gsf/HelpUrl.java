package com.google.android.gsf;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;

public class HelpUrl
{
  public static Uri getHelpUrl(Context paramContext, String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      throw new IllegalArgumentException("getHelpUrl(): fromWhere must be non-empty");
    }
    Uri.Builder localBuilder = Uri.parse(replaceLocale(Gservices.getString(paramContext.getContentResolver(), "context_sensitive_help_url", "http://www.google.com/support/mobile/?hl=%locale%"))).buildUpon();
    localBuilder.appendQueryParameter("p", paramString);
    try
    {
      localBuilder.appendQueryParameter("version", String.valueOf(paramContext.getPackageManager().getPackageInfo(paramContext.getApplicationInfo().packageName, 0).versionCode));
      return localBuilder.build();
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Log.e("HelpUrl", "Error finding package " + paramContext.getApplicationInfo().packageName);
      }
    }
  }
  
  private static String replaceLocale(String paramString)
  {
    if (paramString.contains("%locale%"))
    {
      Locale localLocale = Locale.getDefault();
      paramString = paramString.replace("%locale%", localLocale.getLanguage() + "-" + localLocale.getCountry().toLowerCase());
    }
    return paramString;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gsf.HelpUrl
 * JD-Core Version:    0.7.0.1
 */