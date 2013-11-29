package com.google.android.search.core.google;

import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;

public class WebSuggestSourceWrapper
  implements WebSuggestSource
{
  private final WebSuggestSource mWrapped;
  
  protected WebSuggestSourceWrapper(WebSuggestSource paramWebSuggestSource)
  {
    this.mWrapped = paramWebSuggestSource;
  }
  
  public void close()
  {
    this.mWrapped.close();
  }
  
  public SuggestionList getCachedSuggestions(Query paramQuery)
  {
    return this.mWrapped.getCachedSuggestions(paramQuery);
  }
  
  public String getSourceName()
  {
    return this.mWrapped.getSourceName();
  }
  
  public void getSuggestions(Query paramQuery, Consumer<SuggestionList> paramConsumer)
  {
    this.mWrapped.getSuggestions(paramQuery, paramConsumer);
  }
  
  public SuggestionList queryExternal(String paramString)
  {
    return this.mWrapped.queryExternal(paramString);
  }
  
  public boolean removeFromHistory(String paramString)
  {
    return this.mWrapped.removeFromHistory(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.WebSuggestSourceWrapper
 * JD-Core Version:    0.7.0.1
 */