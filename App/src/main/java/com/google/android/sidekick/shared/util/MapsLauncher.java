package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.Location;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import javax.annotation.Nullable;

public class MapsLauncher
{
  private static final String TAG = Tag.getTag(MapsLauncher.class);
  
  @Nullable
  public static Uri buildMapsUri(Sidekick.Location paramLocation, @Nullable TravelMode paramTravelMode, @Nullable String paramString)
  {
    if (paramLocation == null) {
      return null;
    }
    return Uri.parse(buildMapsUrlString(paramLocation, paramTravelMode, paramString));
  }
  
  static String buildMapsUrlForLocationAndQuery(Sidekick.Location paramLocation, String paramString)
  {
    new StringBuilder("http://maps.google.com/maps/?q=%s");
    if ((paramLocation.hasLat()) && (paramLocation.hasLng()))
    {
      Locale localLocale1 = Locale.US;
      Object[] arrayOfObject1 = new Object[1];
      Locale localLocale2 = Locale.US;
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = Double.toString(paramLocation.getLat());
      arrayOfObject2[1] = Double.toString(paramLocation.getLng());
      arrayOfObject2[2] = paramString;
      arrayOfObject1[0] = String.format(localLocale2, "%s,%s(%s)", arrayOfObject2);
      return String.format(localLocale1, "http://maps.google.com/maps/?q=%s", arrayOfObject1);
    }
    return String.format(Locale.US, "http://maps.google.com/maps/?q=%s", new Object[] { paramString });
  }
  
  static String buildMapsUrlString(Sidekick.Location paramLocation, @Nullable TravelMode paramTravelMode, @Nullable String paramString)
  {
    if (paramLocation.hasName()) {}
    String str2;
    for (String str1 = paramLocation.getName();; str1 = paramLocation.getAddress())
    {
      str2 = sanitizeDestinationName(str1);
      if (paramTravelMode != null) {
        break;
      }
      return buildMapsUrlForLocationAndQuery(paramLocation, str2);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramLocation.hasLat()) && (paramLocation.hasLng()))
    {
      Locale localLocale1 = Locale.US;
      String str3 = paramTravelMode.getTemplate();
      Object[] arrayOfObject1 = new Object[1];
      Locale localLocale2 = Locale.US;
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = str2;
      arrayOfObject2[1] = Double.toString(paramLocation.getLat());
      arrayOfObject2[2] = Double.toString(paramLocation.getLng());
      arrayOfObject1[0] = String.format(localLocale2, "%s@%s,%s", arrayOfObject2);
      localStringBuilder.append(String.format(localLocale1, str3, arrayOfObject1));
    }
    for (;;)
    {
      if (paramLocation.hasGeocodeToken()) {
        localStringBuilder.append("&geocode=;").append(Uri.encode(paramLocation.getGeocodeToken()));
      }
      if (paramString != null) {
        localStringBuilder.append("&ptp=").append(Uri.encode(paramString));
      }
      return localStringBuilder.toString();
      localStringBuilder.append(String.format(Locale.US, paramTravelMode.getTemplate(), new Object[] { str2 }));
    }
  }
  
  public static String buildWalkingDirectionsUri(Sidekick.Location paramLocation)
  {
    return buildMapsUrlString(paramLocation, TravelMode.WALKING, null);
  }
  
  @Nullable
  public static String getPersonalizedRouteToken(@Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    if ((paramCommuteSummary != null) && (paramCommuteSummary.hasTransitDetails()) && (paramCommuteSummary.getTransitDetails().hasPersonalizedRouteToken())) {
      return paramCommuteSummary.getTransitDetails().getPersonalizedRouteToken();
    }
    return null;
  }
  
  private static String sanitizeDestinationName(String paramString)
  {
    if (paramString == null) {
      return "";
    }
    try
    {
      String str = URLEncoder.encode(paramString, "utf-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      Log.w(TAG, "Failed to URL encode destination name: " + paramString);
    }
    return paramString.replaceAll("@|&", "");
  }
  
  public static void start(Context paramContext, ActivityHelper paramActivityHelper, Sidekick.Location paramLocation)
  {
    start(paramContext, paramActivityHelper, paramLocation, null, null);
  }
  
  public static void start(Context paramContext, ActivityHelper paramActivityHelper, Sidekick.Location paramLocation, @Nullable TravelMode paramTravelMode, @Nullable String paramString)
  {
    startUsingUri(paramContext, buildMapsUri(paramLocation, paramTravelMode, paramString), paramActivityHelper);
  }
  
  public static void startUsingUri(Context paramContext, Uri paramUri, ActivityHelper paramActivityHelper)
  {
    if (paramUri == null)
    {
      Log.e(TAG, "uri was null when try to launch navigation");
      Toast.makeText(paramContext, 2131363304, 0).show();
      return;
    }
    paramActivityHelper.safeViewUriWithMessage(paramContext, paramUri, true, 2131363302);
  }
  
  public static enum TravelMode
  {
    private final String mTemplate;
    
    static
    {
      TRANSIT = new TravelMode("TRANSIT", 2, "http://maps.google.com/maps/?myl=saddr&daddr=%s&dirflg=r");
      BIKING = new TravelMode("BIKING", 3, "http://maps.google.com/maps/?myl=saddr&daddr=%s&dirflg=b");
      TravelMode[] arrayOfTravelMode = new TravelMode[4];
      arrayOfTravelMode[0] = DRIVING;
      arrayOfTravelMode[1] = WALKING;
      arrayOfTravelMode[2] = TRANSIT;
      arrayOfTravelMode[3] = BIKING;
      $VALUES = arrayOfTravelMode;
    }
    
    private TravelMode(String paramString)
    {
      this.mTemplate = paramString;
    }
    
    public static TravelMode fromSidekickProtoTravelMode(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        throw new IllegalArgumentException("Unknown travel type");
      case 0: 
        return DRIVING;
      case 1: 
        return TRANSIT;
      case 2: 
        return WALKING;
      }
      return BIKING;
    }
    
    private String getTemplate()
    {
      return this.mTemplate;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.MapsLauncher
 * JD-Core Version:    0.7.0.1
 */