package com.google.android.search.shared.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.shared.api.Suggestion;

public class SuggestionViewFactory
{
  public View createSuggestionView(Context paramContext, Suggestion paramSuggestion, ViewGroup paramViewGroup)
  {
    int i = 2130968847;
    switch (getSuggestionViewType(paramSuggestion))
    {
    }
    for (;;)
    {
      return LayoutInflater.from(paramContext).inflate(i, paramViewGroup, false);
      i = 2130968927;
      continue;
      i = 2130968753;
      continue;
      i = 2130968641;
    }
  }
  
  public int getNumSuggestionViewTypes()
  {
    return 4;
  }
  
  public int getSuggestionViewType(Suggestion paramSuggestion)
  {
    if (paramSuggestion.isWebSearchSuggestion()) {
      return 1;
    }
    if (paramSuggestion.isNavSuggestion()) {
      return 2;
    }
    if (paramSuggestion.isContactSuggestion()) {
      return 3;
    }
    return 0;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionViewFactory
 * JD-Core Version:    0.7.0.1
 */