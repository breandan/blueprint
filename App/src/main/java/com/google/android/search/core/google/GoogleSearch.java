package com.google.android.search.core.google;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.util.UriRequest;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoogleSearch
  extends Activity
{
  private static boolean DBG = false;
  
  public static Intent createLaunchUriIntentFromSearchIntent(Intent paramIntent, Context paramContext, boolean paramBoolean, Location paramLocation, Clock paramClock, SearchUrlHelper paramSearchUrlHelper, UriRewriter paramUriRewriter, String paramString)
  {
    String str1 = paramIntent.getStringExtra("query");
    if (TextUtils.isEmpty(str1))
    {
      Log.w("QSB.GoogleSearch", "Got search intent with no query.");
      return null;
    }
    String str2 = paramIntent.getStringExtra("com.android.browser.application_id");
    if (str2 == null) {
      str2 = paramContext.getPackageName();
    }
    boolean bool1 = paramIntent.getBooleanExtra("from_self", false);
    boolean bool2 = paramIntent.getBooleanExtra("new_search", false);
    long l = paramIntent.getLongExtra("query_submit_ts", paramClock.currentTimeMillis());
    String str3 = IntentUtils.getSourceParam(paramIntent, "unknown");
    SearchUrlHelper.Builder localBuilder = paramSearchUrlHelper.getSearchBaseUrl().setStaticParams().setQueryString(str1).setExcludeCookies().setSource(str3).setSubmissionTime(l).setOrDisableLocation(paramBoolean, paramLocation, null);
    if (bool1) {
      localBuilder.setRlz(paramString);
    }
    UriRequest localUriRequest = localBuilder.buildAndRewrite(paramUriRewriter);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(localUriRequest.getUri().toString()));
    localIntent.putExtra("com.android.browser.application_id", str2);
    if (bool2) {
      localIntent.putExtra("create_new_tab", true);
    }
    if (DBG) {
      Log.d("QSB.GoogleSearch", "Formatting location " + paramLocation);
    }
    if ((paramBoolean) && (paramLocation != null))
    {
      Bundle localBundle = Util.stringMapToBundle(localUriRequest.getHeaders());
      if (DBG) {
        Log.d("QSB.GoogleSearch", "Headers: " + localBundle);
      }
      localIntent.putExtra("com.android.browser.headers", localBundle);
    }
    localIntent.addFlags(268435456);
    return localIntent;
  }
  
  private LocationSettings getLocationSettings()
  {
    return getServices().getCoreServices().getLocationSettings();
  }
  
  private VelvetServices getServices()
  {
    return VelvetServices.get();
  }
  
  private void handleViewIntent(Intent paramIntent)
  {
    if (paramIntent.getData() == null)
    {
      Log.w("QSB.GoogleSearch", "Got ACTION_VIEW with no data.");
      return;
    }
    Intent localIntent = new Intent(paramIntent);
    localIntent.setComponent(null);
    localIntent.putExtra("intent_extra_data_key", (String)null);
    launchIntent(localIntent);
  }
  
  private void handleWebSearchIntent(Intent paramIntent)
  {
    PendingIntent localPendingIntent = (PendingIntent)paramIntent.getParcelableExtra("web_search_pendingintent");
    boolean bool;
    if ((getLocationSettings().canUseLocationForSearch()) && (senderAllowedPreciseLocation(localPendingIntent, getPackageManager())))
    {
      bool = true;
      if (!bool) {
        break label134;
      }
    }
    label134:
    for (Location localLocation = getServices().getLocationOracle().getBestLocation();; localLocation = null)
    {
      CoreSearchServices localCoreSearchServices = getServices().getCoreServices();
      Intent localIntent = createLaunchUriIntentFromSearchIntent(paramIntent, this, bool, localLocation, localCoreSearchServices.getClock(), localCoreSearchServices.getSearchUrlHelper(), localCoreSearchServices.getUriRewriter(), localCoreSearchServices.getRlzHelper().getRlzForSearch());
      if (((localPendingIntent == null) || (!launchPendingIntent(localPendingIntent, localIntent))) && (localIntent != null)) {
        launchIntent(localIntent);
      }
      return;
      bool = false;
      break;
    }
  }
  
  private void launchIntent(Intent paramIntent)
  {
    try
    {
      Log.i("QSB.GoogleSearch", "Launching intent: " + paramIntent.toUri(0));
      startActivity(paramIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.w("QSB.GoogleSearch", "No activity found to handle: " + paramIntent);
    }
  }
  
  private boolean launchPendingIntent(PendingIntent paramPendingIntent, Intent paramIntent)
  {
    try
    {
      paramPendingIntent.send(this, -1, paramIntent);
      return true;
    }
    catch (PendingIntent.CanceledException localCanceledException)
    {
      Log.i("QSB.GoogleSearch", "Pending intent cancelled: " + paramPendingIntent);
    }
    return false;
  }
  
  private void logIntent(Intent paramIntent) {}
  
  static boolean senderAllowedPreciseLocation(@Nullable PendingIntent paramPendingIntent, @Nonnull PackageManager paramPackageManager)
  {
    boolean bool = true;
    Preconditions.checkNotNull(paramPackageManager);
    if (paramPendingIntent == null) {
      if (DBG) {
        Log.d("QSB.GoogleSearch", "No pending intent");
      }
    }
    for (;;)
    {
      return bool;
      String str = paramPendingIntent.getTargetPackage();
      if (TextUtils.isEmpty(str))
      {
        if (DBG) {
          Log.d("QSB.GoogleSearch", "No sender package");
        }
        return false;
      }
      if (paramPackageManager.checkPermission("android.permission.ACCESS_FINE_LOCATION", str) == 0) {}
      while (DBG)
      {
        Log.d("QSB.GoogleSearch", "Sender package allowed: " + bool);
        return bool;
        bool = false;
      }
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    Log.i("QSB.GoogleSearch", "Got intent: " + localIntent.toUri(0));
    String str = localIntent.getAction();
    if (("android.intent.action.WEB_SEARCH".equals(str)) || ("android.intent.action.SEARCH".equals(str))) {
      handleWebSearchIntent(localIntent);
    }
    for (;;)
    {
      logIntent(localIntent);
      finish();
      return;
      if ("android.intent.action.VIEW".equals(str)) {
        handleViewIntent(localIntent);
      } else {
        Log.w("QSB.GoogleSearch", "Unhandled intent: " + localIntent);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.GoogleSearch
 * JD-Core Version:    0.7.0.1
 */