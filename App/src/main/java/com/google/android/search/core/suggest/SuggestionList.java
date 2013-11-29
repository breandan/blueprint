package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import java.util.List;

public abstract interface SuggestionList
  extends Iterable<Suggestion>
{
  public abstract Suggestion get(int paramInt);
  
  public abstract String getAccount();
  
  public abstract int getCount();
  
  public abstract long getCreationTime();
  
  public abstract int getLatency();
  
  public abstract String getSourceName();
  
  public abstract Suggestions getSourceSuggestions();
  
  public abstract List<Suggestion> getSuggestions();
  
  public abstract Query getUserQuery();
  
  public abstract boolean isFinal();
  
  public abstract boolean isFromCache();
  
  public abstract boolean isRequestFailed();
  
  public abstract void setFromCache(boolean paramBoolean);
  
  public abstract void setLatency(int paramInt);
  
  public abstract void setSourceSuggestions(Suggestions paramSuggestions);
  
  public abstract boolean wasRequestMade();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionList
 * JD-Core Version:    0.7.0.1
 */