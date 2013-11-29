package com.google.android.search.core.suggest.presenter;

import com.google.android.search.core.suggest.Suggestions;
import javax.annotation.Nonnull;

public abstract interface SuggestionsClient
{
  public abstract boolean ignoreClearSuggestionsOnStop();
  
  public abstract void indicateRemoveFromHistoryFailed();
  
  public abstract void setWebSuggestionsEnabled(boolean paramBoolean);
  
  public abstract void showSuggestions(@Nonnull Suggestions paramSuggestions);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.presenter.SuggestionsClient
 * JD-Core Version:    0.7.0.1
 */