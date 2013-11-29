package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import java.util.Iterator;

public class CorrectionPromoter
  implements Promoter
{
  public void pickPromoted(Suggestions paramSuggestions, int paramInt, MutableSuggestionList paramMutableSuggestionList)
  {
    if (!paramSuggestions.getQuery().isVoiceSearch())
    {
      SuggestionList localSuggestionList = paramSuggestions.getWebResult();
      if (localSuggestionList != null)
      {
        Iterator localIterator = localSuggestionList.iterator();
        while (localIterator.hasNext())
        {
          Suggestion localSuggestion = (Suggestion)localIterator.next();
          if (localSuggestion.isCorrectionSuggestion()) {
            paramMutableSuggestionList.add(localSuggestion);
          }
        }
        if (paramSuggestions.areWebResultsDone()) {
          paramMutableSuggestionList.setFinal();
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.CorrectionPromoter
 * JD-Core Version:    0.7.0.1
 */