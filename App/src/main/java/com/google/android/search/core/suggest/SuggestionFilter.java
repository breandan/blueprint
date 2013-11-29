package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Suggestion;
import javax.annotation.Nullable;

public abstract interface SuggestionFilter
{
  public abstract boolean accept(@Nullable SuggestionList paramSuggestionList, Suggestion paramSuggestion);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionFilter
 * JD-Core Version:    0.7.0.1
 */