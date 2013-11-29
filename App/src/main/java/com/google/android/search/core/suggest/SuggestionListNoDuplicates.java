package com.google.android.search.core.suggest;

import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import java.util.HashSet;
import java.util.Iterator;

public class SuggestionListNoDuplicates
  extends MutableSuggestionListImpl
{
  private final HashSet<String> mSuggestionKeys = new HashSet();
  
  public SuggestionListNoDuplicates(String paramString, Query paramQuery)
  {
    super(paramString, paramQuery);
  }
  
  private int findSuggestionPosByKey(String paramString)
  {
    for (int i = 0; i < getCount(); i++) {
      if (TextUtils.equals(get(i).getSuggestionKey(), paramString)) {
        return i;
      }
    }
    return -1;
  }
  
  public boolean add(Suggestion paramSuggestion)
  {
    String str = paramSuggestion.getSuggestionKey();
    boolean bool2;
    if (this.mSuggestionKeys.add(str)) {
      bool2 = super.add(paramSuggestion);
    }
    boolean bool1;
    do
    {
      return bool2;
      bool1 = paramSuggestion.isHistorySuggestion();
      bool2 = false;
    } while (!bool1);
    int i = findSuggestionPosByKey(paramSuggestion.getSuggestionKey());
    SuggestionBuilder localSuggestionBuilder = new SuggestionBuilder().fromSuggestion(get(i));
    if (paramSuggestion.isHistorySuggestion()) {
      localSuggestionBuilder.isHistory(true);
    }
    replace(i, localSuggestionBuilder.build());
    return false;
  }
  
  public int addAll(Iterable<Suggestion> paramIterable)
  {
    int i = 0;
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext()) {
      if (add((Suggestion)localIterator.next())) {
        i++;
      }
    }
    return i;
  }
  
  public void remove(int paramInt)
  {
    this.mSuggestionKeys.remove(get(paramInt).getSuggestionKey());
    super.remove(paramInt);
  }
  
  public void replace(int paramInt, Suggestion paramSuggestion)
  {
    this.mSuggestionKeys.remove(get(paramInt).getSuggestionKey());
    if (this.mSuggestionKeys.add(paramSuggestion.getSuggestionKey()))
    {
      super.replace(paramInt, paramSuggestion);
      return;
    }
    remove(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionListNoDuplicates
 * JD-Core Version:    0.7.0.1
 */