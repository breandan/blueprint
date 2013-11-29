package com.google.android.gms.location.settings;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SettingsClient
{
  private static void c(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent(paramString);
    localIntent.setFlags(268435456);
    localIntent.setPackage("com.google.android.gms");
    try
    {
      paramContext.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.e("GCoreLocationSettings", "Problem while starting settings activity" + localIntent);
    }
  }
  
  public static void launchGoogleLocationSettings(Context paramContext)
  {
    c(paramContext, "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.settings.SettingsClient
 * JD-Core Version:    0.7.0.1
 */