package com.google.android.search.core.suggest;

import android.util.Log;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Util;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuggestionListFactory
{
  private static final Comparator<Suggestion> LAST_ACCESS_TIME_COMP = new SuggestionsComparator(true);
  private static final Comparator<Suggestion> NON_LAST_ACCESS_TIME_COMP = new SuggestionsComparator(false);
  
  public static SuggestionList createSuggestionList(String paramString, Query paramQuery, List<Suggestion> paramList, boolean paramBoolean)
  {
    if ((!paramBoolean) || (paramBoolean)) {}
    for (Comparator localComparator = LAST_ACCESS_TIME_COMP;; localComparator = NON_LAST_ACCESS_TIME_COMP)
    {
      Collections.sort(paramList, localComparator);
      if ((!paramList.isEmpty()) && (paramList.get(-1 + paramList.size()) == null)) {
        Log.w("Search.SuggestionListFactory", "null in Suggestion list");
      }
      return new SuggestionListImpl(paramString, paramQuery, paramList);
    }
  }
  
  private static final class SuggestionsComparator
    implements Comparator<Suggestion>
  {
    private final boolean mContainsLastAccessTime;
    
    public SuggestionsComparator(boolean paramBoolean)
    {
      this.mContainsLastAccessTime = paramBoolean;
    }
    
    public int compare(Suggestion paramSuggestion1, Suggestion paramSuggestion2)
    {
      int j;
      int i;
      if (paramSuggestion1 == null) {
        if (paramSuggestion2 == null)
        {
          j = 0;
          i = j;
        }
      }
      label50:
      do
      {
        do
        {
          return i;
          j = 1;
          break;
          if (paramSuggestion2 == null) {
            return -1;
          }
          if (!this.mContainsLastAccessTime) {
            break label50;
          }
          i = (int)(paramSuggestion2.getLastAccessTime() - paramSuggestion1.getLastAccessTime());
        } while (i != 0);
        i = Util.compareAsStrings(paramSuggestion1.getSuggestionText1(), paramSuggestion2.getSuggestionText1());
      } while (i != 0);
      return Util.compareAsStrings(paramSuggestion1.getSuggestionText2(), paramSuggestion2.getSuggestionText2());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionListFactory
 * JD-Core Version:    0.7.0.1
 */