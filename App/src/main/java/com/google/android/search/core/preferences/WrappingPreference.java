package com.google.android.search.core.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class WrappingPreference
  extends Preference
{
  public WrappingPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public WrappingPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    TextView localTextView = (TextView)paramView.findViewById(16908310);
    if (localTextView != null)
    {
      localTextView.setSingleLine(false);
      localTextView.setMaxLines(3);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.WrappingPreference
 * JD-Core Version:    0.7.0.1
 */