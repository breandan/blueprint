package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import java.util.Iterator;
import java.util.List;

public class MutableSuggestionListImpl
  extends SuggestionListImpl
  implements MutableSuggestionList
{
  public MutableSuggestionListImpl(SuggestionList paramSuggestionList)
  {
    this(paramSuggestionList.getSourceName(), paramSuggestionList.getUserQuery(), paramSuggestionList.getCreationTime());
    addAll(paramSuggestionList);
    setAccount(paramSuggestionList.getAccount());
    setLatency(paramSuggestionList.getLatency());
    setRequestFailed(paramSuggestionList.isRequestFailed());
    setRequestMade(paramSuggestionList.wasRequestMade());
    setSourceSuggestions(paramSuggestionList.getSourceSuggestions());
    setFromCache(paramSuggestionList.isFromCache());
  }
  
  public MutableSuggestionListImpl(String paramString, Query paramQuery)
  {
    super(paramString, paramQuery, 16);
  }
  
  public MutableSuggestionListImpl(String paramString, Query paramQuery, long paramLong)
  {
    super(paramString, paramQuery, 16, paramLong);
  }
  
  public MutableSuggestionListImpl(String paramString, Query paramQuery, List<Suggestion> paramList, long paramLong)
  {
    super(paramString, paramQuery, paramList, paramLong);
  }
  
  public boolean add(Suggestion paramSuggestion)
  {
    this.mSuggestions.add(paramSuggestion);
    return true;
  }
  
  public int addAll(Iterable<Suggestion> paramIterable)
  {
    int i = getCount();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Suggestion localSuggestion = (Suggestion)localIterator.next();
      this.mSuggestions.add(localSuggestion);
    }
    return getCount() - i;
  }
  
  public void remove(int paramInt)
  {
    this.mSuggestions.remove(paramInt);
  }
  
  public void replace(int paramInt, Suggestion paramSuggestion)
  {
    this.mSuggestions.set(paramInt, paramSuggestion);
  }
  
  public void setAccount(String paramString)
  {
    this.mAccount = paramString;
  }
  
  public void setFinal()
  {
    this.mIsFinal = true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.MutableSuggestionListImpl
 * JD-Core Version:    0.7.0.1
 */