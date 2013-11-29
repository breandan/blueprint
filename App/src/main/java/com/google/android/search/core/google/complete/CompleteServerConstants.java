package com.google.android.search.core.google.complete;

import java.util.regex.Pattern;

public class CompleteServerConstants
{
  public static final String LOG_TYPE_DEVICE_HISTORY = String.valueOf(41);
  public static final String LOG_TYPE_DEVICE_OFFLINE = String.valueOf(77);
  public static final String LOG_TYPE_DEVICE_UNDO_REWRITE = String.valueOf(78);
  public static final String LOG_TYPE_NAV;
  public static final String LOG_TYPE_QUERY = String.valueOf(0);
  public static final String LOG_TYPE_SEARCH_HISTORY;
  public static final Pattern SPELLING_CORRECTION_TAG_PATTERN = Pattern.compile("<sc>(.*?)</sc>");
  public static final Pattern SPELLING_ERROR_TAG_PATTERN = Pattern.compile("<se>(.*?)</se>");
  public static final Pattern SPELLING_ESCAPE_PATTERN = Pattern.compile("<(/?((se)|(sc)))>");
  
  static
  {
    LOG_TYPE_NAV = String.valueOf(5);
    LOG_TYPE_SEARCH_HISTORY = String.valueOf(25);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.CompleteServerConstants
 * JD-Core Version:    0.7.0.1
 */