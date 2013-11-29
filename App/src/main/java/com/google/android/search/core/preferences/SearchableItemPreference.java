package com.google.android.search.core.preferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.CheckBoxPreference;
import android.view.View;
import android.widget.ImageView;

public class SearchableItemPreference
  extends CheckBoxPreference
{
  private Drawable mIcon;
  
  public SearchableItemPreference(Context paramContext)
  {
    super(paramContext);
    setLayoutResource(2130968820);
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    ((ImageView)paramView.findViewById(2131296310)).setImageDrawable(this.mIcon);
    paramView.setBackgroundResource(0);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SearchableItemPreference
 * JD-Core Version:    0.7.0.1
 */