package com.google.android.search.shared.ui;

import com.google.android.search.shared.api.Suggestion;

public abstract interface SuggestionClickListener
{
  public abstract void onSuggestionClicked(Suggestion paramSuggestion);
  
  public abstract void onSuggestionQueryRefineClicked(Suggestion paramSuggestion);
  
  public abstract void onSuggestionQuickContactClicked(Suggestion paramSuggestion);
  
  public abstract void onSuggestionRemoveFromHistoryClicked(Suggestion paramSuggestion);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionClickListener
 * JD-Core Version:    0.7.0.1
 */