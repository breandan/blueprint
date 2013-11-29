package com.google.android.search.shared.ui;

import android.text.Spannable;
import javax.annotation.Nullable;

public abstract class SuggestionFormatter
{
  private TextAppearanceFactory mSpanFactory;
  
  protected SuggestionFormatter(TextAppearanceFactory paramTextAppearanceFactory)
  {
    this.mSpanFactory = paramTextAppearanceFactory;
  }
  
  protected void applyTextStyle(int paramInt1, Spannable paramSpannable, int paramInt2, int paramInt3)
  {
    if (paramInt2 == paramInt3) {
      return;
    }
    paramSpannable.setSpan(this.mSpanFactory.createTextAppearance(paramInt1), paramInt2, paramInt3, 0);
  }
  
  @Nullable
  public CharSequence formatSuggestion(String paramString, @Nullable CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    if (paramCharSequence != null) {
      return formatSuggestion(paramString, paramCharSequence.toString(), paramInt1, paramInt2);
    }
    return null;
  }
  
  protected abstract CharSequence formatSuggestion(String paramString1, String paramString2, int paramInt1, int paramInt2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionFormatter
 * JD-Core Version:    0.7.0.1
 */