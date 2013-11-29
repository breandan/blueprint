package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Suggestion;

public abstract class FilteringPromoter
  implements Promoter
{
  private final SuggestionFilter mFilter;
  
  protected FilteringPromoter(SuggestionFilter paramSuggestionFilter)
  {
    this.mFilter = paramSuggestionFilter;
  }
  
  protected boolean accept(SuggestionList paramSuggestionList, Suggestion paramSuggestion)
  {
    if (this.mFilter != null) {
      return this.mFilter.accept(paramSuggestionList, paramSuggestion);
    }
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.FilteringPromoter
 * JD-Core Version:    0.7.0.1
 */