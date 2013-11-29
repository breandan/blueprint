package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Location;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class DirectionsLauncher
{
  private static final String TAG = Tag.getTag(DirectionsLauncher.class);
  private final ActivityHelper mActivityHelper;
  private final Context mContext;
  
  public DirectionsLauncher(Context paramContext, ActivityHelper paramActivityHelper)
  {
    this.mContext = paramContext;
    this.mActivityHelper = paramActivityHelper;
  }
  
  private Uri buildDriveAboutUri(String paramString, Sidekick.Location paramLocation, @Nullable List<Sidekick.Location> paramList, @Nullable MapsLauncher.TravelMode paramTravelMode)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = "d";
    if (paramTravelMode == MapsLauncher.TravelMode.WALKING) {}
    for (str = "w";; str = "b") {
      try
      {
        do
        {
          localStringBuilder.append("google.navigation:").append("title=").append(URLEncoder.encode(paramString, "UTF-8"));
          if ((paramLocation.hasLat()) && (paramLocation.hasLng())) {
            localStringBuilder.append("&ll=").append(paramLocation.getLat()).append(",").append(paramLocation.getLng());
          }
          localStringBuilder.append("&").append(String.format(Locale.ENGLISH, "mode=%s", new Object[] { str })).append("&").append("entry=r");
          if (paramList == null) {
            break;
          }
          Iterator localIterator = paramList.iterator();
          while (localIterator.hasNext())
          {
            Sidekick.Location localLocation = (Sidekick.Location)localIterator.next();
            localStringBuilder.append("&altvia=").append(localLocation.getLat()).append(",").append(localLocation.getLng());
          }
        } while (paramTravelMode != MapsLauncher.TravelMode.BIKING);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Log.e(TAG, "Encoding Error while attempting to encode location label: " + paramString, localUnsupportedEncodingException);
        return null;
      }
    }
    if (paramLocation.hasGeocodeToken()) {
      localStringBuilder.append("&token=").append(paramLocation.getGeocodeToken());
    }
    return Uri.parse(localStringBuilder.toString());
  }
  
  private Intent createIntent(@Nullable MapsLauncher.TravelMode paramTravelMode, String paramString1, Sidekick.Location paramLocation, @Nullable List<Sidekick.Location> paramList, @Nullable String paramString2)
  {
    if (modeSupportsNavigation(paramTravelMode)) {}
    for (Uri localUri = buildDriveAboutUri(paramString1, paramLocation, paramList, paramTravelMode); localUri == null; localUri = MapsLauncher.buildMapsUri(paramLocation, paramTravelMode, paramString2))
    {
      Log.e(TAG, "uri was null when try to launch navigation");
      return null;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
    localIntent.setFlags(268435456);
    return localIntent;
  }
  
  @Nullable
  public Intent getLauncherIntent(Sidekick.Location paramLocation, @Nullable List<Sidekick.Location> paramList, @Nullable MapsLauncher.TravelMode paramTravelMode, @Nullable String paramString)
  {
    String str = paramLocation.getName();
    if (TextUtils.isEmpty(str)) {
      str = paramLocation.getAddress();
    }
    return createIntent(paramTravelMode, str, paramLocation, paramList, paramString);
  }
  
  public MapsLauncher.TravelMode getTravelMode(NavigationContext paramNavigationContext, @Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    Integer localInteger = CommuteSummaryUtil.getTravelMode(paramCommuteSummary);
    if (localInteger == null) {
      localInteger = paramNavigationContext.getTravelModePreference(CommuteSummaryUtil.getTravelModeSetting(paramCommuteSummary));
    }
    return MapsLauncher.TravelMode.fromSidekickProtoTravelMode(localInteger.intValue());
  }
  
  public boolean modeSupportsNavigation(@Nullable MapsLauncher.TravelMode paramTravelMode)
  {
    return paramTravelMode != MapsLauncher.TravelMode.TRANSIT;
  }
  
  public void start(Sidekick.Location paramLocation, @Nullable List<Sidekick.Location> paramList, MapsLauncher.TravelMode paramTravelMode, @Nullable String paramString)
  {
    Intent localIntent = getLauncherIntent(paramLocation, paramList, paramTravelMode, paramString);
    if (localIntent == null) {
      return;
    }
    this.mActivityHelper.safeStartActivityWithMessage(this.mContext, localIntent, 2131363303);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.DirectionsLauncher
 * JD-Core Version:    0.7.0.1
 */