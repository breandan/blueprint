package com.google.android.search.shared.ondevice;

public class GelVelAppFilter
{
  public static boolean shouldShowApp(String paramString1, String paramString2, boolean paramBoolean)
  {
    if ("com.google.android.launcher".equals(paramString1)) {}
    while (("com.google.android.googlequicksearchbox".equals(paramString1)) && (((paramBoolean) && ("com.google.android.googlequicksearchbox.SearchActivity".equals(paramString2))) || ("com.google.android.googlequicksearchbox.VoiceSearchActivity".equals(paramString2)) || ("com.google.android.googlequicksearchbox.SearchWidgetProvider".equals(paramString2)) || ("com.google.android.sidekick.main.widget.PredictiveCardsWidgetProvider".equals(paramString2)))) {
      return false;
    }
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ondevice.GelVelAppFilter
 * JD-Core Version:    0.7.0.1
 */