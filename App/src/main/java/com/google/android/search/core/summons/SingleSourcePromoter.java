package com.google.android.search.core.summons;

import android.text.TextUtils;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.Promoter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.shared.api.Suggestion;
import java.util.Iterator;

public class SingleSourcePromoter
  implements Promoter
{
  private final Source mSource;
  
  public SingleSourcePromoter(Source paramSource)
  {
    this.mSource = paramSource;
  }
  
  public void pickPromoted(Suggestions paramSuggestions, int paramInt, MutableSuggestionList paramMutableSuggestionList)
  {
    for (;;)
    {
      try
      {
        Iterator localIterator1 = paramSuggestions.getSourceResults().iterator();
        boolean bool = localIterator1.hasNext();
        Object localObject2 = null;
        if (bool)
        {
          SuggestionList localSuggestionList = (SuggestionList)localIterator1.next();
          if (!TextUtils.equals(localSuggestionList.getSourceName(), this.mSource.getName())) {
            continue;
          }
          localObject2 = localSuggestionList;
        }
        if (localObject2 != null)
        {
          int i = paramInt - paramMutableSuggestionList.getCount();
          Iterator localIterator2 = localObject2.iterator();
          Suggestion localSuggestion;
          if (localIterator2.hasNext())
          {
            localSuggestion = (Suggestion)localIterator2.next();
            if (i >= 1) {}
          }
          else
          {
            paramMutableSuggestionList.setFinal();
            return;
          }
          i--;
          paramMutableSuggestionList.add(localSuggestion);
          continue;
        }
        if (!paramSuggestions.areSummonsDone()) {
          continue;
        }
      }
      finally {}
      paramMutableSuggestionList.setFinal();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SingleSourcePromoter
 * JD-Core Version:    0.7.0.1
 */