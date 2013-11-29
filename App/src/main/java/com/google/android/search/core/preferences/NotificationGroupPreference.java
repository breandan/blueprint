package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.googlequicksearchbox.R.styleable;

public class NotificationGroupPreference
  extends CheckBoxPreference
{
  private boolean mMultiline;
  
  public NotificationGroupPreference(Context paramContext)
  {
    super(paramContext);
  }
  
  public NotificationGroupPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public NotificationGroupPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet)
  {
    if (paramAttributeSet != null) {
      this.mMultiline = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NotificationGroupPreference).getBoolean(0, false);
    }
  }
  
  private void makeMultiline(View paramView)
  {
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      for (int i = 0; i < localViewGroup.getChildCount(); i++) {
        makeMultiline(localViewGroup.getChildAt(i));
      }
    }
    if ((paramView instanceof TextView))
    {
      TextView localTextView = (TextView)paramView;
      localTextView.setSingleLine(false);
      localTextView.setEllipsize(null);
    }
  }
  
  protected boolean getPersistedBoolean(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1; getPersistedInt(i) == 1; i = 2) {
      return true;
    }
    return false;
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    if (this.mMultiline) {
      makeMultiline(paramView);
    }
  }
  
  protected boolean persistBoolean(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1;; i = 2) {
      return persistInt(i);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.NotificationGroupPreference
 * JD-Core Version:    0.7.0.1
 */