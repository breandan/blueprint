package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import javax.annotation.Nullable;

public class WebSearchUtils
{
  public static void startWebSearch(Context paramContext, String paramString, @Nullable Location paramLocation)
  {
    Intent localIntent = new Intent("com.google.android.googlequicksearchbox.INTERNAL_GOOGLE_SEARCH");
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity");
    localIntent.addFlags(268435456);
    localIntent.putExtra("query", paramString);
    localIntent.putExtra("from-predictive", true);
    if (paramLocation != null) {
      localIntent.putExtra("location", paramLocation);
    }
    paramContext.startActivity(localIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.WebSearchUtils
 * JD-Core Version:    0.7.0.1
 */