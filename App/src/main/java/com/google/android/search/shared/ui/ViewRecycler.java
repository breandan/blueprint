package com.google.android.search.shared.ui;

import android.view.View;
import com.google.common.collect.Lists;
import java.util.List;

public class ViewRecycler
{
  private final int mMinRecycleBinSize;
  private final List<View>[] mScrapViews;
  private final int mViewTypeCount;
  
  public ViewRecycler(int paramInt1, int paramInt2)
  {
    this.mMinRecycleBinSize = paramInt2;
    this.mViewTypeCount = paramInt1;
    this.mScrapViews = new List[this.mViewTypeCount];
    for (int i = 0; i < this.mViewTypeCount; i++) {
      this.mScrapViews[i] = Lists.newArrayList();
    }
  }
  
  public View getView(int paramInt)
  {
    if (paramInt >= 0) {}
    for (List localList = this.mScrapViews[paramInt];; localList = null)
    {
      View localView = null;
      if (localList != null)
      {
        int i = localList.size();
        localView = null;
        if (i > 0) {
          localView = (View)localList.remove(i - 1);
        }
      }
      return localView;
    }
  }
  
  public void releaseView(View paramView, int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 0)
    {
      List localList = this.mScrapViews[paramInt1];
      if (localList.size() < Math.max(paramInt2, this.mMinRecycleBinSize))
      {
        paramView.setNextFocusUpId(-1);
        paramView.setNextFocusDownId(-1);
        paramView.setNextFocusLeftId(-1);
        paramView.setNextFocusRightId(-1);
        localList.add(paramView);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.ViewRecycler
 * JD-Core Version:    0.7.0.1
 */