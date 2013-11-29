package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Suggestion;

public abstract interface MutableSuggestionList
  extends SuggestionList
{
  public abstract boolean add(Suggestion paramSuggestion);
  
  public abstract int addAll(Iterable<Suggestion> paramIterable);
  
  public abstract void remove(int paramInt);
  
  public abstract void setAccount(String paramString);
  
  public abstract void setFinal();
  
  public abstract void setRequestFailed(boolean paramBoolean);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.MutableSuggestionList
 * JD-Core Version:    0.7.0.1
 */