package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Suggestion;
import java.util.Iterator;
import java.util.List;

public class NowPromoPromoter
  implements Promoter
{
  public void pickPromoted(Suggestions paramSuggestions, int paramInt, MutableSuggestionList paramMutableSuggestionList)
  {
    Iterator localIterator1 = paramSuggestions.getSourceResults().iterator();
    Suggestion localSuggestion;
    do
    {
      Iterator localIterator2;
      while (!localIterator2.hasNext())
      {
        SuggestionList localSuggestionList;
        do
        {
          if (!localIterator1.hasNext()) {
            break;
          }
          localSuggestionList = (SuggestionList)localIterator1.next();
        } while (!"NowPromo".equals(localSuggestionList.getSourceName()));
        localIterator2 = localSuggestionList.getSuggestions().iterator();
      }
      localSuggestion = (Suggestion)localIterator2.next();
    } while (!localSuggestion.isNowPromo());
    paramMutableSuggestionList.add(localSuggestion);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.NowPromoPromoter
 * JD-Core Version:    0.7.0.1
 */