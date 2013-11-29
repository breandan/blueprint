package com.google.android.search.core.google;

import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;

public abstract interface GoogleSource
{
  public abstract SuggestionList getCachedSuggestions(Query paramQuery);
  
  public abstract String getSourceName();
  
  public abstract void getSuggestions(Query paramQuery, Consumer<SuggestionList> paramConsumer);
  
  public abstract boolean removeFromHistory(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.GoogleSource
 * JD-Core Version:    0.7.0.1
 */