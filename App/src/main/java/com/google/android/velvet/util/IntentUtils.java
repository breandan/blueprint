package com.google.android.velvet.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.googlequicksearchbox.SearchActivity;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.ui.InAppWebPageActivity;
import com.google.common.base.Objects;
import javax.annotation.Nullable;

public class IntentUtils
{
  public static boolean containsSafeExternalActivity(Context paramContext, @Nullable Bundle paramBundle)
  {
    Intent localIntent = getExternalActivityLaunchIntent(paramBundle);
    if (localIntent != null) {
      return isExternalActivitySafeToLaunch(paramContext, localIntent);
    }
    return false;
  }
  
  public static Intent createAssistIntent(Context paramContext, int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.ASSIST");
    localIntent.setClass(paramContext, SearchActivity.class);
    localIntent.putExtra("assist_intent_source", paramInt);
    return localIntent;
  }
  
  public static Bundle createBundleForRelaunchableExternalActivity(Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("com.google.android.googlequicksearchbox.EXTERNAL_ACTIVITY_LAUNCH_INTENT", paramIntent);
    return localBundle;
  }
  
  public static Intent createResumeVelvetIntent(Context paramContext)
  {
    Intent localIntent = new Intent("com.google.android.googlequicksearchbox.RESUME_VELVET");
    localIntent.setFlags(268435456);
    localIntent.setComponent(new ComponentName(paramContext, "com.google.android.velvet.ui.VelvetActivity"));
    return localIntent;
  }
  
  public static Intent createResumeVelvetWithQueryIntent(Context paramContext, Query paramQuery)
  {
    Intent localIntent = createResumeVelvetIntent(paramContext);
    localIntent.putExtra("resume-velvet-with-new-query", paramQuery);
    return localIntent;
  }
  
  public static Intent createResumeVelvetWithQueryIntent(Context paramContext, Query paramQuery, @Nullable Intent paramIntent)
  {
    Intent localIntent = createResumeVelvetWithQueryIntent(paramContext, paramQuery);
    localIntent.putExtra("com.google.android.googlequicksearchbox.EXTERNAL_ACTIVITY_LAUNCH_INTENT", paramIntent);
    return localIntent;
  }
  
  public static Intent createResumeVelvetWithSearchControllerIntent(Context paramContext, int paramInt, Query paramQuery, @Nullable ActionData paramActionData)
  {
    Intent localIntent = createResumeVelvetIntent(paramContext);
    localIntent.addFlags(32768);
    localIntent.addFlags(65536);
    localIntent.putExtra("search-controller-token", paramInt);
    localIntent.putExtra("resume-velvet-query", paramQuery);
    if (paramActionData != null) {
      localIntent.putExtra("resume-velvet-action", paramActionData);
    }
    return localIntent;
  }
  
  public static Intent createSearchIntent(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.search.action.GLOBAL_SEARCH");
    localIntent.setClass(paramContext, SearchActivity.class);
    localIntent.setFlags(268435456);
    setSourceParam(localIntent, paramString);
    return localIntent;
  }
  
  public static Intent createVoiceSearchIntent(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.speech.action.WEB_SEARCH");
    localIntent.setPackage(paramContext.getPackageName());
    setSourceParam(localIntent, paramString);
    return localIntent;
  }
  
  public static Bundle getAppSearchData(Intent paramIntent)
  {
    return paramIntent.getBundleExtra("app_data");
  }
  
  public static Intent getExternalActivityLaunchIntent(@Nullable Bundle paramBundle)
  {
    if (paramBundle != null) {
      return (Intent)paramBundle.getParcelable("com.google.android.googlequicksearchbox.EXTERNAL_ACTIVITY_LAUNCH_INTENT");
    }
    return null;
  }
  
  public static Location getLocationOverride(Intent paramIntent)
  {
    return (Location)paramIntent.getParcelableExtra("location");
  }
  
  public static String getQueryString(Intent paramIntent)
  {
    if ("android.intent.action.SEND".equals(paramIntent.getAction())) {
      return paramIntent.getStringExtra("android.intent.extra.TEXT");
    }
    return paramIntent.getStringExtra("query");
  }
  
  @Nullable
  public static ActionData getResumeVelvetAction(Intent paramIntent)
  {
    return (ActionData)paramIntent.getParcelableExtra("resume-velvet-action");
  }
  
  @Nullable
  public static Query getResumeVelvetNewQuery(Intent paramIntent)
  {
    return (Query)paramIntent.getParcelableExtra("resume-velvet-with-new-query");
  }
  
  @Nullable
  public static Query getResumeVelvetQuery(Intent paramIntent)
  {
    return (Query)paramIntent.getParcelableExtra("resume-velvet-query");
  }
  
  public static int getSearchControllerToken(Intent paramIntent)
  {
    return paramIntent.getIntExtra("search-controller-token", -1);
  }
  
  public static String getSourceParam(Intent paramIntent, String paramString)
  {
    Bundle localBundle = getAppSearchData(paramIntent);
    String str = null;
    if (localBundle != null) {
      str = localBundle.getString("source");
    }
    if (str == null) {
      str = paramString;
    }
    return "android-" + str;
  }
  
  @Nullable
  public static Uri getVoiceSearchRecordedAudioUrl(Intent paramIntent)
  {
    if (("com.google.android.googlequicksearchbox.VOICE_SEARCH_RECORDED_AUDIO".equals(paramIntent.getAction())) && ("com.google.android.googlequicksearchbox.RecordedVoiceSearchActivity".equals(paramIntent.getComponent().getClassName()))) {
      return paramIntent.getData();
    }
    return null;
  }
  
  public static boolean hasQueryStringExtra(Intent paramIntent)
  {
    if ("android.intent.action.SEND".equals(paramIntent.getAction())) {
      return paramIntent.hasExtra("android.intent.extra.TEXT");
    }
    return paramIntent.hasExtra("query");
  }
  
  private static boolean isExternalActivitySafeToLaunch(Context paramContext, Intent paramIntent)
  {
    if (paramIntent == null) {}
    ComponentName localComponentName;
    do
    {
      return false;
      localComponentName = paramIntent.getComponent();
    } while ((localComponentName == null) || (!Objects.equal(localComponentName.getPackageName(), paramContext.getPackageName())) || (!InAppWebPageActivity.class.getName().equals(localComponentName.getClassName())));
    return true;
  }
  
  public static boolean isFromPredictive(Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("from-predictive", false);
  }
  
  public static boolean isGelDefaultLauncher(Context paramContext)
  {
    String str = paramContext.getPackageName();
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.HOME");
    ResolveInfo localResolveInfo = paramContext.getPackageManager().resolveActivity(localIntent, 0);
    if ((localResolveInfo == null) || (localResolveInfo.activityInfo == null)) {
      Log.v("GoogleSearch", "Unable to resolve home activity: " + localResolveInfo);
    }
    while ((!str.equals(localResolveInfo.activityInfo.applicationInfo.packageName)) || (!"com.google.android.launcher.GEL".equals(localResolveInfo.activityInfo.name))) {
      return false;
    }
    return true;
  }
  
  public static boolean isGlobalSearchIntent(Intent paramIntent)
  {
    return TextUtils.equals(paramIntent.getAction(), "android.search.action.GLOBAL_SEARCH");
  }
  
  public static boolean isLaunchFromFirstRunActivity(Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("from-first-run", false);
  }
  
  public static boolean isResumeFromHistory(Intent paramIntent)
  {
    return (0x100000 & paramIntent.getFlags()) != 0;
  }
  
  public static boolean isResumeVelvetIntent(Intent paramIntent)
  {
    return "com.google.android.googlequicksearchbox.RESUME_VELVET".equals(paramIntent.getAction());
  }
  
  public static boolean isSearchIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    return (TextUtils.equals(str, "android.intent.action.WEB_SEARCH")) || (TextUtils.equals(str, "com.google.android.googlequicksearchbox.GOOGLE_SEARCH")) || (TextUtils.equals(str, "com.google.android.googlequicksearchbox.INTERNAL_GOOGLE_SEARCH")) || (isGlobalSearchIntent(paramIntent));
  }
  
  public static boolean isSoundSearchIntent(Intent paramIntent)
  {
    return "com.google.android.googlequicksearchbox.MUSIC_SEARCH".equals(paramIntent.getAction());
  }
  
  public static boolean isTheGoogleIntent(Intent paramIntent)
  {
    return (TextUtils.equals(paramIntent.getAction(), "android.intent.action.MAIN")) || (TextUtils.equals(paramIntent.getAction(), "android.intent.action.ASSIST")) || (TextUtils.equals(paramIntent.getAction(), "com.google.android.googlequicksearchbox.GOOGLE_ICON"));
  }
  
  public static boolean isVoiceSearchIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    return ("android.intent.action.SEARCH_LONG_PRESS".equals(str)) || ("android.speech.action.WEB_SEARCH".equals(str)) || ("android.intent.action.VOICE_ASSIST".equals(str)) || ("com.google.android.googlequicksearchbox.VOICE_SEARCH_RECORDED_AUDIO".equals(str));
  }
  
  private static void setSourceParam(Intent paramIntent, String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("source", paramString);
    paramIntent.putExtra("app_data", localBundle);
  }
  
  public static boolean shouldDisableMarinerOptIn(Intent paramIntent)
  {
    return paramIntent.hasExtra("disable-opt-in");
  }
  
  public static boolean shouldSelectAllQuery(Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("select_query", false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.util.IntentUtils
 * JD-Core Version:    0.7.0.1
 */