package com.android.launcher3;

import android.content.Context;
import android.view.View;

public class AppsCustomizeCellLayout
  extends CellLayout
  implements Page
{
  public AppsCustomizeCellLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public int getPageChildCount()
  {
    return getChildCount();
  }
  
  public void removeAllViewsOnPage()
  {
    removeAllViews();
    setLayerType(0, null);
  }
  
  public void resetChildrenOnKeyListeners()
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = getShortcutsAndWidgets();
    int i = localShortcutAndWidgetContainer.getChildCount();
    for (int j = 0; j < i; j++) {
      localShortcutAndWidgetContainer.getChildAt(j).setOnKeyListener(null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppsCustomizeCellLayout
 * JD-Core Version:    0.7.0.1
 */