package com.google.android.search.shared.ui;

import android.content.Context;
import android.text.style.TextAppearanceSpan;

public class TextAppearanceFactory
{
  private final Context mContext;
  
  public TextAppearanceFactory(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public Object createTextAppearance(int paramInt)
  {
    return new TextAppearanceSpan(this.mContext, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.TextAppearanceFactory
 * JD-Core Version:    0.7.0.1
 */