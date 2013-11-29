package com.google.android.search.core.google.complete;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;
import com.google.android.common.base.StringUtil;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.suggest.web.WebSuggestions;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GwsSuggestionParser
  extends SuggestionParser
{
  private final Context mContext;
  private final SearchUrlHelper mSearchUrlHelper;
  
  public GwsSuggestionParser(SearchConfig paramSearchConfig, Clock paramClock, Context paramContext, SearchUrlHelper paramSearchUrlHelper)
  {
    super(paramSearchConfig, paramClock);
    this.mContext = ((Context)Preconditions.checkNotNull(paramContext));
    this.mSearchUrlHelper = ((SearchUrlHelper)Preconditions.checkNotNull(paramSearchUrlHelper));
  }
  
  private boolean arrayContainsInt(@Nullable JSONArray paramJSONArray, int paramInt)
  {
    if (paramJSONArray != null) {
      for (int i = 0; i < paramJSONArray.length(); i++) {
        if (paramJSONArray.optInt(i, -1) == paramInt) {
          return true;
        }
      }
    }
    return false;
  }
  
  @Nullable
  private String getExtrasValue(JSONObject paramJSONObject, String paramString, boolean paramBoolean)
  {
    try
    {
      Object localObject = paramJSONObject.getString(paramString);
      if (paramBoolean)
      {
        String str = StringUtil.unescapeHTML((String)localObject);
        localObject = str;
      }
      return localObject;
    }
    catch (JSONException localJSONException)
    {
      Log.w("Search.SuggestionParser1", "Couldn't get value from extras", localJSONException);
    }
    return null;
  }
  
  protected void addSuggestionsFromJson(JSONArray paramJSONArray, List<Suggestion> paramList)
    throws JSONException
  {
    int i = paramJSONArray.optInt(1, -1);
    if (i == -1)
    {
      Log.w("Search.SuggestionParser1", "Suggesion missing type. Defaulting to query suggestion.");
      i = 0;
    }
    int j = paramJSONArray.length();
    boolean bool = false;
    if (j > 2) {
      bool = arrayContainsInt(paramJSONArray.optJSONArray(2), 39);
    }
    if (i == 0)
    {
      paramList.add(WebSuggestions.createWebSuggestion(StringUtil.unescapeHTML(paramJSONArray.getString(0)), bool));
      return;
    }
    if (i == 5)
    {
      String str1 = StringUtil.unescapeHTML(paramJSONArray.getString(0));
      int k = paramJSONArray.length();
      String str2 = null;
      Uri localUri = null;
      if (k > 3)
      {
        JSONObject localJSONObject = paramJSONArray.getJSONObject(3);
        str2 = getExtrasValue(localJSONObject, "b", true);
        String str3 = getExtrasValue(localJSONObject, "a", false);
        localUri = this.mSearchUrlHelper.getResultTargetUri(str3);
      }
      if (localUri == null) {
        localUri = Uri.parse(URLUtil.guessUrl(str1));
      }
      if (str2 == null) {
        str2 = str1;
      }
      paramList.add(WebSuggestions.createNavSuggestion(str1, str2, localUri, bool, this.mContext));
      return;
    }
    if (i == 50)
    {
      Log.w("Search.SuggestionParser1", "Discarding word-by-word suggestions because parsing is not implemented.");
      return;
    }
    Log.w("Search.SuggestionParser1", "Unknown suggestion type " + i + ": " + paramJSONArray.toString());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.GwsSuggestionParser
 * JD-Core Version:    0.7.0.1
 */