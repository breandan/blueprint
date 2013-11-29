package com.google.android.search.core.suggest.web;

import android.content.Context;
import android.net.Uri;
import com.google.android.search.core.google.complete.CompleteServerConstants;
import com.google.android.search.core.suggest.SuggestionBuilder;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Util;

public final class WebSuggestions
{
  public static Suggestion createCorrectionSuggestion(CharSequence paramCharSequence)
  {
    return SuggestionBuilder.builder().text1(paramCharSequence).suggestionQuery(paramCharSequence.toString()).isCorrectionSuggestion(true).build();
  }
  
  public static Suggestion createDeviceFailedQuerySuggestion(CharSequence paramCharSequence)
  {
    return SuggestionBuilder.builder().text1(paramCharSequence).intentAction("android.intent.action.WEB_SEARCH").suggestionQuery(paramCharSequence.toString()).logType(CompleteServerConstants.LOG_TYPE_DEVICE_OFFLINE).isHistory(true).build();
  }
  
  public static Suggestion createDeviceQuerySuggestion(CharSequence paramCharSequence)
  {
    return SuggestionBuilder.builder().text1(paramCharSequence).intentAction("android.intent.action.WEB_SEARCH").suggestionQuery(paramCharSequence.toString()).logType(CompleteServerConstants.LOG_TYPE_DEVICE_HISTORY).isHistory(true).build();
  }
  
  public static Suggestion createNavSuggestion(String paramString, CharSequence paramCharSequence, Uri paramUri, boolean paramBoolean, Context paramContext)
  {
    return SuggestionBuilder.builder().text1(paramCharSequence).text2Url(paramString).icon1(Util.toResourceUriString(paramContext.getPackageName(), 2130837746)).intentAction("android.intent.action.VIEW").intentData(paramUri.toString()).suggestionQuery(paramUri.toString()).isHistory(paramBoolean).logType(CompleteServerConstants.LOG_TYPE_NAV).build();
  }
  
  public static Suggestion createUndoSuggestion(CharSequence paramCharSequence)
  {
    return SuggestionBuilder.builder().text1(paramCharSequence).intentAction("android.intent.action.WEB_SEARCH").suggestionQuery(paramCharSequence.toString()).logType(CompleteServerConstants.LOG_TYPE_DEVICE_UNDO_REWRITE).isHistory(true).build();
  }
  
  public static Suggestion createWebSuggestion(CharSequence paramCharSequence, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = CompleteServerConstants.LOG_TYPE_SEARCH_HISTORY;; str = CompleteServerConstants.LOG_TYPE_QUERY) {
      return SuggestionBuilder.builder().text1(paramCharSequence).intentAction("android.intent.action.WEB_SEARCH").suggestionQuery(paramCharSequence.toString()).logType(str).isHistory(paramBoolean).build();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.web.WebSuggestions
 * JD-Core Version:    0.7.0.1
 */