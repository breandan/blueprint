package com.google.android.search.core.google;

import com.google.android.search.core.suggest.SuggestionList;

public abstract interface WebSuggestSource
  extends GoogleSource
{
  public abstract void close();
  
  public abstract SuggestionList queryExternal(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.WebSuggestSource
 * JD-Core Version:    0.7.0.1
 */