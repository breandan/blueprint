package com.google.android.search.core.preferences.cards;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

public class PartialSwitchPreference
  extends SwitchPreference
{
  public PartialSwitchPreference(Context paramContext)
  {
    super(paramContext);
  }
  
  public PartialSwitchPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public PartialSwitchPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void setTextStyle(View paramView)
  {
    if (((paramView instanceof TextView)) && (!(paramView instanceof Switch)))
    {
      ((TextView)paramView).setSingleLine(false);
      ((TextView)paramView).setMaxLines(10);
    }
    for (;;)
    {
      return;
      if ((paramView instanceof ViewGroup))
      {
        int i = ((ViewGroup)paramView).getChildCount();
        for (int j = 0; j < i; j++) {
          setTextStyle(((ViewGroup)paramView).getChildAt(j));
        }
      }
    }
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    setTextStyle(paramView);
  }
  
  protected void onClick() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.PartialSwitchPreference
 * JD-Core Version:    0.7.0.1
 */