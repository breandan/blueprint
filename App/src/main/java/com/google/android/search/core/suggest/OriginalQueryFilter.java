package com.google.android.search.core.suggest;

import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import javax.annotation.Nullable;

public class OriginalQueryFilter
  implements SuggestionFilter
{
  public boolean accept(@Nullable SuggestionList paramSuggestionList, Suggestion paramSuggestion)
  {
    return (paramSuggestionList == null) || (!paramSuggestion.isWebSearchSuggestion()) || (!TextUtils.equals(paramSuggestion.getSuggestionQuery(), paramSuggestionList.getUserQuery().getQueryStringForSearch()));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.OriginalQueryFilter
 * JD-Core Version:    0.7.0.1
 */