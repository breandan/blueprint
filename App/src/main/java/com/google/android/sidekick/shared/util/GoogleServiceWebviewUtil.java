package com.google.android.sidekick.shared.util;

import android.content.Intent;
import android.net.Uri;

public class GoogleServiceWebviewUtil
{
  public static final String[] ALL_URL_PREFIXES = { "*" };
  public static final String[] GMAIL_URL_PREFIXES = { "mail.google." };
  
  public static Intent createIntent(Uri paramUri)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", paramUri);
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.sidekick.main.GoogleServiceWebviewWrapper");
    return localIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil
 * JD-Core Version:    0.7.0.1
 */