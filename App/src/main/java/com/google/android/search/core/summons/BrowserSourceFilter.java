package com.google.android.search.core.summons;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.SuggestionFilter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Suggestion;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BrowserSourceFilter
  implements SuggestionFilter
{
  private static final Pattern ACCEPTED_URI_SCHEMA = Pattern.compile("(?i)((?:http|https|file):\\/\\/|(?:inline|data|about|javascript):)(.*)");
  private int mAcceptedResults;
  private final SearchConfig mConfig;
  private final int mMaxResults;
  private final String mSearchAuthorityPrefix;
  
  public BrowserSourceFilter(Context paramContext, SearchConfig paramSearchConfig, int paramInt)
  {
    this.mSearchAuthorityPrefix = paramContext.getString(2131361996);
    this.mConfig = paramSearchConfig;
    this.mMaxResults = paramInt;
  }
  
  private static String fixUrl(@Nonnull String paramString)
  {
    int i = paramString.indexOf(':');
    boolean bool = true;
    int j = 0;
    char c;
    if (j < i)
    {
      c = paramString.charAt(j);
      if (Character.isLetter(c)) {}
    }
    else
    {
      if ((!paramString.startsWith("http://")) && (!paramString.startsWith("https://"))) {
        break label107;
      }
    }
    label107:
    while ((!paramString.startsWith("http:")) && (!paramString.startsWith("https:")))
    {
      return paramString;
      bool &= Character.isLowerCase(c);
      if ((j == i - 1) && (!bool)) {
        paramString = paramString.substring(0, i).toLowerCase() + paramString.substring(i);
      }
      j++;
      break;
    }
    if ((paramString.startsWith("http:/")) || (paramString.startsWith("https:/"))) {
      return paramString.replaceFirst("/", "//");
    }
    return paramString.replaceFirst(":", "://");
  }
  
  private boolean isPreviousSearchResultsPage(Suggestion paramSuggestion)
  {
    Uri localUri = Uri.parse(paramSuggestion.getSuggestionIntentDataString());
    return (localUri.getAuthority().startsWith(this.mSearchAuthorityPrefix)) && (this.mConfig.isGoogleSearchUrlPath(localUri.getPath()));
  }
  
  private boolean isText1Uri(Suggestion paramSuggestion)
  {
    return TextUtils.equals(paramSuggestion.getSuggestionText1(), paramSuggestion.getSuggestionText2Url());
  }
  
  public static boolean isUrlLikeByBrowserHeuristics(@Nonnull String paramString)
  {
    String str = fixUrl(paramString).trim();
    return (Patterns.WEB_URL.matcher(str).matches()) || (ACCEPTED_URI_SCHEMA.matcher(str).matches());
  }
  
  public boolean accept(@Nullable SuggestionList paramSuggestionList, Suggestion paramSuggestion)
  {
    if (this.mAcceptedResults >= this.mMaxResults) {}
    String str;
    do
    {
      do
      {
        return false;
      } while ((isText1Uri(paramSuggestion)) || (isPreviousSearchResultsPage(paramSuggestion)));
      str = paramSuggestion.getSuggestionIntentDataString();
    } while ((!TextUtils.isEmpty(str)) && (!isUrlLikeByBrowserHeuristics(str)));
    this.mAcceptedResults = (1 + this.mAcceptedResults);
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.BrowserSourceFilter
 * JD-Core Version:    0.7.0.1
 */