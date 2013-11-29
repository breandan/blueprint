package com.google.android.googlequicksearchbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.velvet.util.IntentUtils;

public class SearchActivity
  extends Activity
{
  static Intent fillInTargetActivityClass(Context paramContext, Intent paramIntent)
  {
    Intent localIntent = new Intent(paramIntent);
    String str = paramContext.getPackageName();
    if (IntentUtils.isGelDefaultLauncher(paramContext))
    {
      localIntent.setClassName(str, "com.google.android.launcher.GEL");
      return localIntent;
    }
    localIntent.setClassName(str, "com.google.android.velvet.ui.VelvetActivity");
    return localIntent;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = new Intent(getIntent());
    localIntent.setFlags(0xFF7FFFFF & localIntent.getFlags());
    String str1 = getPackageName();
    String str2 = localIntent.getAction();
    if (("com.google.android.googlequicksearchbox.MUSIC_SEARCH".equals(str2)) || ("com.google.android.googlequicksearchbox.GOOGLE_SEARCH".equals(str2)) || ("com.google.android.googlequicksearchbox.INTERNAL_GOOGLE_SEARCH".equals(str2))) {
      localIntent.setClassName(str1, "com.google.android.velvet.ui.VelvetActivity");
    }
    for (;;)
    {
      startActivity(localIntent);
      finish();
      return;
      if ("android.intent.action.MAIN".equals(localIntent.getAction())) {
        localIntent.setAction("com.google.android.googlequicksearchbox.GOOGLE_ICON");
      }
      localIntent = fillInTargetActivityClass(this, localIntent);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.googlequicksearchbox.SearchActivity
 * JD-Core Version:    0.7.0.1
 */