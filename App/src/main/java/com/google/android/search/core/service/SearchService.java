package com.google.android.search.core.service;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.Suggestion;

public abstract interface SearchService
{
  public abstract void cancel();
  
  public abstract void commit(Query paramQuery);
  
  public abstract void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats);
  
  public abstract void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats);
  
  public abstract void removeSuggestionFromHistory(Suggestion paramSuggestion);
  
  public abstract void set(Query paramQuery);
  
  public abstract void setHotwordDetectionEnabled(boolean paramBoolean);
  
  public abstract void startQueryEdit();
  
  public abstract void stopListening();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.SearchService
 * JD-Core Version:    0.7.0.1
 */