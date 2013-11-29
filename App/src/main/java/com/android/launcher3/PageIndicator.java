package com.android.launcher3;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class PageIndicator
  extends LinearLayout
{
  private int mActiveMarkerIndex;
  private LayoutInflater mLayoutInflater;
  private ArrayList<PageIndicatorMarker> mMarkers = new ArrayList();
  private int mMaxWindowSize;
  private int[] mWindowRange = new int[2];
  
  public PageIndicator(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PageIndicator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PageIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PageIndicator, paramInt, 0);
    this.mMaxWindowSize = localTypedArray.getInteger(0, 15);
    this.mWindowRange[0] = 0;
    this.mWindowRange[1] = 0;
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    localTypedArray.recycle();
    getLayoutTransition().setDuration(175L);
  }
  
  private void disableLayoutTransitions()
  {
    LayoutTransition localLayoutTransition = getLayoutTransition();
    localLayoutTransition.disableTransitionType(2);
    localLayoutTransition.disableTransitionType(3);
    localLayoutTransition.disableTransitionType(0);
    localLayoutTransition.disableTransitionType(1);
  }
  
  private void enableLayoutTransitions()
  {
    LayoutTransition localLayoutTransition = getLayoutTransition();
    localLayoutTransition.enableTransitionType(2);
    localLayoutTransition.enableTransitionType(3);
    localLayoutTransition.enableTransitionType(0);
    localLayoutTransition.enableTransitionType(1);
  }
  
  void addMarker(int paramInt, PageMarkerResources paramPageMarkerResources, boolean paramBoolean)
  {
    int i = Math.max(0, Math.min(paramInt, this.mMarkers.size()));
    PageIndicatorMarker localPageIndicatorMarker = (PageIndicatorMarker)this.mLayoutInflater.inflate(2130968771, this, false);
    localPageIndicatorMarker.setMarkerDrawables(paramPageMarkerResources.activeId, paramPageMarkerResources.inactiveId);
    this.mMarkers.add(i, localPageIndicatorMarker);
    offsetWindowCenterTo(this.mActiveMarkerIndex, paramBoolean);
  }
  
  void addMarkers(ArrayList<PageMarkerResources> paramArrayList, boolean paramBoolean)
  {
    for (int i = 0; i < paramArrayList.size(); i++) {
      addMarker(2147483647, (PageMarkerResources)paramArrayList.get(i), paramBoolean);
    }
  }
  
  void offsetWindowCenterTo(int paramInt, boolean paramBoolean)
  {
    if (paramInt < 0) {
      new Throwable().printStackTrace();
    }
    int i = Math.min(this.mMarkers.size(), this.mMaxWindowSize);
    int j = i / 2;
    (i / 2.0F);
    int k = Math.max(0, paramInt - j);
    int m = Math.min(this.mMarkers.size(), k + this.mMaxWindowSize);
    int n = m - Math.min(this.mMarkers.size(), i);
    (n + (m - n) / 2);
    if (n == 0)
    {
      if (m != this.mMarkers.size()) {
        break label209;
      }
      label112:
      if ((this.mWindowRange[0] == n) && (this.mWindowRange[1] == m)) {
        break label212;
      }
    }
    label209:
    label212:
    for (boolean bool = true;; bool = false)
    {
      if (!paramBoolean) {
        disableLayoutTransitions();
      }
      for (int i1 = -1 + getChildCount(); i1 >= 0; i1--)
      {
        PageIndicatorMarker localPageIndicatorMarker2 = (PageIndicatorMarker)getChildAt(i1);
        int i3 = this.mMarkers.indexOf(localPageIndicatorMarker2);
        if ((i3 < n) || (i3 >= m)) {
          removeView(localPageIndicatorMarker2);
        }
      }
      break;
      break label112;
    }
    int i2 = 0;
    if (i2 < this.mMarkers.size())
    {
      PageIndicatorMarker localPageIndicatorMarker1 = (PageIndicatorMarker)this.mMarkers.get(i2);
      if ((n <= i2) && (i2 < m))
      {
        if (indexOfChild(localPageIndicatorMarker1) < 0) {
          addView(localPageIndicatorMarker1, i2 - n);
        }
        if (i2 == paramInt) {
          localPageIndicatorMarker1.activate(bool);
        }
      }
      for (;;)
      {
        i2++;
        break;
        localPageIndicatorMarker1.inactivate(bool);
        continue;
        localPageIndicatorMarker1.inactivate(true);
      }
    }
    if (!paramBoolean) {
      enableLayoutTransitions();
    }
    this.mWindowRange[0] = n;
    this.mWindowRange[1] = m;
  }
  
  void removeAllMarkers(boolean paramBoolean)
  {
    while (this.mMarkers.size() > 0) {
      removeMarker(2147483647, paramBoolean);
    }
  }
  
  void removeMarker(int paramInt, boolean paramBoolean)
  {
    if (this.mMarkers.size() > 0)
    {
      int i = Math.max(0, Math.min(-1 + this.mMarkers.size(), paramInt));
      this.mMarkers.remove(i);
      offsetWindowCenterTo(this.mActiveMarkerIndex, paramBoolean);
    }
  }
  
  void setActiveMarker(int paramInt)
  {
    this.mActiveMarkerIndex = paramInt;
    offsetWindowCenterTo(paramInt, false);
  }
  
  void updateMarker(int paramInt, PageMarkerResources paramPageMarkerResources)
  {
    ((PageIndicatorMarker)this.mMarkers.get(paramInt)).setMarkerDrawables(paramPageMarkerResources.activeId, paramPageMarkerResources.inactiveId);
  }
  
  public static class PageMarkerResources
  {
    int activeId;
    int inactiveId;
    
    public PageMarkerResources()
    {
      this.activeId = 2130837817;
      this.inactiveId = 2130837818;
    }
    
    public PageMarkerResources(int paramInt1, int paramInt2)
    {
      this.activeId = paramInt1;
      this.inactiveId = paramInt2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PageIndicator
 * JD-Core Version:    0.7.0.1
 */