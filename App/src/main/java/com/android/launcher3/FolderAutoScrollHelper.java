package com.android.launcher3;

import android.support.v4.widget.AutoScrollHelper;
import android.widget.ScrollView;

public class FolderAutoScrollHelper
  extends AutoScrollHelper
{
  private final ScrollView mTarget;
  
  public FolderAutoScrollHelper(ScrollView paramScrollView)
  {
    super(paramScrollView);
    this.mTarget = paramScrollView;
    setActivationDelay(0);
    setEdgeType(1);
    setExclusive(true);
    setMaximumVelocity(1500.0F, 1500.0F);
    setRampDownDuration(0);
    setRampUpDuration(0);
  }
  
  public boolean canTargetScrollHorizontally(int paramInt)
  {
    return false;
  }
  
  public boolean canTargetScrollVertically(int paramInt)
  {
    return this.mTarget.canScrollVertically(paramInt);
  }
  
  public void scrollTargetBy(int paramInt1, int paramInt2)
  {
    this.mTarget.scrollBy(paramInt1, paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.FolderAutoScrollHelper
 * JD-Core Version:    0.7.0.1
 */