package com.google.android.sidekick.shared.util;

import android.content.Intent;

public class EntryRefreshUtil
{
  public static Intent createRefreshIntent()
  {
    Intent localIntent = new Intent();
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.sidekick.main.entry.EntriesRefreshIntentService");
    return localIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.EntryRefreshUtil
 * JD-Core Version:    0.7.0.1
 */