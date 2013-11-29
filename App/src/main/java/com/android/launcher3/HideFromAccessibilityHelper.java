package com.android.launcher3;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import java.util.HashMap;

public class HideFromAccessibilityHelper
  implements ViewGroup.OnHierarchyChangeListener
{
  boolean mHide = false;
  boolean mOnlyAllApps;
  private HashMap<View, Integer> mPreviousValues = new HashMap();
  
  private boolean hasAncestorOfType(View paramView, Class paramClass)
  {
    return (paramView != null) && ((paramView.getClass().equals(paramClass)) || (((paramView.getParent() instanceof ViewGroup)) && (hasAncestorOfType((ViewGroup)paramView.getParent(), paramClass))));
  }
  
  private boolean includeView(View paramView)
  {
    return (!hasAncestorOfType(paramView, Cling.class)) && ((!this.mOnlyAllApps) || (hasAncestorOfType(paramView, AppsCustomizeTabHost.class)));
  }
  
  private void restoreImportantForAccessibilityHelper(View paramView)
  {
    paramView.setImportantForAccessibility(((Integer)this.mPreviousValues.get(paramView)).intValue());
    this.mPreviousValues.remove(paramView);
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      if ((localViewGroup instanceof ViewGroup.OnHierarchyChangeListener)) {
        localViewGroup.setOnHierarchyChangeListener((ViewGroup.OnHierarchyChangeListener)localViewGroup);
      }
      for (;;)
      {
        for (int i = 0; i < localViewGroup.getChildCount(); i++)
        {
          View localView = localViewGroup.getChildAt(i);
          if (includeView(localView)) {
            restoreImportantForAccessibilityHelper(localView);
          }
        }
        localViewGroup.setOnHierarchyChangeListener(null);
      }
    }
  }
  
  private void setImportantForAccessibilityToNoHelper(View paramView)
  {
    this.mPreviousValues.put(paramView, Integer.valueOf(paramView.getImportantForAccessibility()));
    paramView.setImportantForAccessibility(2);
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      localViewGroup.setOnHierarchyChangeListener(this);
      for (int i = 0; i < localViewGroup.getChildCount(); i++)
      {
        View localView = localViewGroup.getChildAt(i);
        if (includeView(localView)) {
          setImportantForAccessibilityToNoHelper(localView);
        }
      }
    }
  }
  
  public void onChildViewAdded(View paramView1, View paramView2)
  {
    if ((this.mHide) && (includeView(paramView2))) {
      setImportantForAccessibilityToNoHelper(paramView2);
    }
  }
  
  public void onChildViewRemoved(View paramView1, View paramView2)
  {
    if ((this.mHide) && (includeView(paramView2))) {
      restoreImportantForAccessibilityHelper(paramView2);
    }
  }
  
  public void restoreImportantForAccessibility(View paramView)
  {
    if (this.mHide) {
      restoreImportantForAccessibilityHelper(paramView);
    }
    this.mHide = false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HideFromAccessibilityHelper
 * JD-Core Version:    0.7.0.1
 */