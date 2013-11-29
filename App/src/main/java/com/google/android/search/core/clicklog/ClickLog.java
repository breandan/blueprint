package com.google.android.search.core.clicklog;

import com.google.android.search.shared.api.Suggestion;
import java.util.Map;

public abstract interface ClickLog
{
  public abstract Map<String, Integer> getSourceScores();
  
  public abstract void reportClick(Suggestion paramSuggestion);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.clicklog.ClickLog
 * JD-Core Version:    0.7.0.1
 */