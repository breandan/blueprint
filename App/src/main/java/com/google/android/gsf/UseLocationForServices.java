package com.google.android.gsf;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;

public class UseLocationForServices
{
  private static final String[] GOOGLE_GEOLOCATION_ORIGINS = { "http://www.google.com", "http://www.google.co.uk" };
  
  public static int getUseLocationForServices(Context paramContext)
  {
    return GoogleSettingsContract.Partner.getInt(paramContext.getContentResolver(), "use_location_for_services", 2);
  }
  
  public static void registerUseLocationForServicesObserver(Context paramContext, ContentObserver paramContentObserver)
  {
    Uri localUri = GoogleSettingsContract.Partner.getUriFor("use_location_for_services");
    paramContext.getContentResolver().registerContentObserver(localUri, false, paramContentObserver);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gsf.UseLocationForServices
 * JD-Core Version:    0.7.0.1
 */