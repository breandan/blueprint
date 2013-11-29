package com.google.android.search.shared.ui;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.URLUtil;
import com.google.android.search.shared.api.Suggestion;

public class NavSuggestionView
  extends BaseSuggestionView
{
  public NavSuggestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private String removeTrailingSlash(String paramString)
  {
    if ((paramString != null) && (TextUtils.equals(Uri.parse(URLUtil.guessUrl(paramString)).getPath(), "/"))) {
      paramString = paramString.substring(0, -1 + paramString.length());
    }
    return paramString;
  }
  
  public boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    if (super.bindAsSuggestion(paramSuggestion, paramString, paramSuggestionFormatter))
    {
      setText1(paramSuggestionFormatter.formatSuggestion(paramString, paramSuggestion.getSuggestionText1(), 2131624083, 2131624080));
      setText2(removeTrailingSlash(paramSuggestion.getSuggestionText2Url()));
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.NavSuggestionView
 * JD-Core Version:    0.7.0.1
 */