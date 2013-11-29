package com.android.launcher3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabWidget;

public class FocusOnlyTabWidget
  extends TabWidget
{
  public FocusOnlyTabWidget(Context paramContext)
  {
    super(paramContext);
  }
  
  public FocusOnlyTabWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FocusOnlyTabWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getChildTabIndex(View paramView)
  {
    int i = getTabCount();
    for (int j = 0; j < i; j++) {
      if (getChildTabViewAt(j) == paramView) {
        return j;
      }
    }
    return -1;
  }
  
  public View getSelectedTab()
  {
    int i = getTabCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildTabViewAt(j);
      if (localView.isSelected()) {
        return localView;
      }
    }
    return null;
  }
  
  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if ((paramView == this) && (paramBoolean) && (getTabCount() > 0)) {
      getSelectedTab().requestFocus();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FocusOnlyTabWidget
 * JD-Core Version:    0.7.0.1
 */