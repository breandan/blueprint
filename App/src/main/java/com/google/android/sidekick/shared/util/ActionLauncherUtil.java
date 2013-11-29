package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;

public class ActionLauncherUtil
{
  public static Intent createActionLauncherIntent(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.sidekick.main.actions.ActionLauncherActivity");
    return localIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ActionLauncherUtil
 * JD-Core Version:    0.7.0.1
 */