package com.google.android.search.shared.ui;

import android.graphics.drawable.Drawable;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.UriLoader;

public abstract interface SuggestionView
{
  public abstract boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter);
  
  public abstract Suggestion getBoundSuggestion();
  
  public abstract void setClickListener(ClickListener paramClickListener);
  
  public abstract void setDividerVisibility(int paramInt);
  
  public abstract void setEnabled(boolean paramBoolean);
  
  public abstract void setIconLoader(UriLoader<Drawable> paramUriLoader);
  
  public abstract void setIconMode(int paramInt);
  
  public static abstract interface ClickListener
  {
    public abstract void onSuggestionClicked(Suggestion paramSuggestion);
    
    public abstract void onSuggestionQueryRefineClicked(Suggestion paramSuggestion);
    
    public abstract void onSuggestionQuickContactClicked(Suggestion paramSuggestion);
    
    public abstract void onSuggestionRemoveFromHistoryClicked(Suggestion paramSuggestion);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionView
 * JD-Core Version:    0.7.0.1
 */