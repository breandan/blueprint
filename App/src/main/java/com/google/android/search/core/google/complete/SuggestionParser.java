package com.google.android.search.core.google.complete;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.web.WebSuggestions;
import com.google.android.search.shared.api.CorrectionSpan;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Clock;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class SuggestionParser
{
  private final Clock mClock;
  private final SearchConfig mConfig;
  
  public SuggestionParser(SearchConfig paramSearchConfig, Clock paramClock)
  {
    this.mConfig = ((SearchConfig)Preconditions.checkNotNull(paramSearchConfig));
    this.mClock = ((Clock)Preconditions.checkNotNull(paramClock));
  }
  
  private void extractCorrections(JSONArray paramJSONArray, List<Suggestion> paramList, String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = paramJSONArray.getJSONObject(2);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString);
    if ((!localJSONObject.isNull("o")) && (!localJSONObject.isNull("p")))
    {
      String str1 = unescapeForSpelling(localJSONObject.getString("o"));
      String str2 = unescapeForSpelling(localJSONObject.getString("p"));
      int i = 0;
      Matcher localMatcher1 = CompleteServerConstants.SPELLING_ERROR_TAG_PATTERN.matcher(str2);
      Matcher localMatcher2 = CompleteServerConstants.SPELLING_CORRECTION_TAG_PATTERN.matcher(str1);
      while (localMatcher1.find())
      {
        String str3 = localMatcher1.group(1);
        int j = localMatcher1.start() - i;
        int k = j + str3.length();
        i += localMatcher1.end() - localMatcher1.start() - str3.length();
        Preconditions.checkState(localMatcher2.find());
        localSpannableStringBuilder.setSpan(new CorrectionSpan(localMatcher2.group(1)), j, k, 33);
      }
    }
    paramList.add(WebSuggestions.createCorrectionSuggestion(localSpannableStringBuilder));
  }
  
  private SuggestionList extractSuggestions(Query paramQuery, JSONArray paramJSONArray, String paramString)
    throws JSONException
  {
    String str = paramJSONArray.getString(0);
    ArrayList localArrayList = Lists.newArrayList();
    JSONArray localJSONArray = paramJSONArray.getJSONArray(1);
    int i = 0;
    for (;;)
    {
      if (i < localJSONArray.length()) {
        try
        {
          addSuggestionsFromJson(localJSONArray.getJSONArray(i), localArrayList);
          i++;
        }
        catch (JSONException localJSONException2)
        {
          for (;;)
          {
            Log.w("Search.SuggestionParser", "Could not parse suggestion at position " + i + ": " + localJSONArray);
          }
        }
      }
    }
    if (this.mConfig.isCorrectionsEnabled()) {}
    try
    {
      extractCorrections(paramJSONArray, localArrayList, str);
      MutableSuggestionListImpl localMutableSuggestionListImpl = new MutableSuggestionListImpl("complete-server", paramQuery.withQueryChars(str), localArrayList, this.mClock.uptimeMillis());
      if (paramString != null) {
        localMutableSuggestionListImpl.setAccount(paramString);
      }
      return localMutableSuggestionListImpl;
    }
    catch (JSONException localJSONException1)
    {
      for (;;)
      {
        Log.w("Search.SuggestionParser", "Error parsing JSON correction span data");
      }
    }
  }
  
  private String unescapeForSpelling(String paramString)
  {
    return Html.fromHtml(CompleteServerConstants.SPELLING_ESCAPE_PATTERN.matcher(paramString).replaceAll("&lt;$1&gt;")).toString();
  }
  
  protected abstract void addSuggestionsFromJson(JSONArray paramJSONArray, List<Suggestion> paramList)
    throws JSONException;
  
  ParsedSuggestions parseJson(Query paramQuery, String paramString1, String paramString2)
    throws JSONException
  {
    JSONArray localJSONArray1 = new JSONArray(paramString1);
    SuggestionList localSuggestionList1 = extractSuggestions(paramQuery, localJSONArray1, paramString2);
    Object localObject1 = Collections.emptyList();
    if (this.mConfig.isSuggestLookAheadEnabled()) {}
    for (;;)
    {
      Object localObject2;
      try
      {
        localObject1 = Lists.newArrayList();
        JSONObject localJSONObject = localJSONArray1.optJSONObject(2);
        if (localJSONObject == null)
        {
          localObject2 = null;
          break label154;
          if (i < localObject2.length())
          {
            JSONArray localJSONArray2 = localObject2.getJSONArray(i);
            localSuggestionList2.getUserQuery().getQueryStringForSuggest();
            localSuggestionList2 = extractSuggestions(paramQuery, localJSONArray2, paramString2);
            ((List)localObject1).add(localSuggestionList2);
            i++;
            continue;
          }
        }
        else
        {
          JSONArray localJSONArray3 = localJSONObject.optJSONArray("m");
          localObject2 = localJSONArray3;
        }
      }
      catch (JSONException localJSONException)
      {
        Log.w("Search.SuggestionParser", "Error parsing JSON look ahead suggestion data");
      }
      label154:
      while (localObject2 == null) {
        return new ParsedSuggestions(localSuggestionList1, (List)localObject1, null);
      }
      SuggestionList localSuggestionList2 = localSuggestionList1;
      int i = 0;
    }
  }
  
  public static final class ParsedSuggestions
  {
    @Nonnull
    public final List<SuggestionList> mLookaheadSuggestions;
    @Nonnull
    public final SuggestionList mMainSuggestions;
    
    private ParsedSuggestions(SuggestionList paramSuggestionList, List<SuggestionList> paramList)
    {
      this.mMainSuggestions = ((SuggestionList)Preconditions.checkNotNull(paramSuggestionList));
      this.mLookaheadSuggestions = ((List)Preconditions.checkNotNull(paramList));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.SuggestionParser
 * JD-Core Version:    0.7.0.1
 */