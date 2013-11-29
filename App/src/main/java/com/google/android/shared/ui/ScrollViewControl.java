package com.google.android.shared.ui;

import android.view.View;
import android.view.ViewGroup;

public abstract interface ScrollViewControl
{
  public abstract void addScrollListener(ScrollListener paramScrollListener);
  
  public abstract int getDescendantTop(View paramView);
  
  public abstract int getMaxScrollY();
  
  public abstract int getScrollY();
  
  public abstract int getViewportHeight();
  
  public abstract void notifyOverscroll(int paramInt);
  
  public abstract void notifyOverscrollFinish();
  
  public abstract void notifyOverscrollStart();
  
  public abstract void notifyScrollAnimationFinished();
  
  public abstract void notifyScrollFinished();
  
  public abstract void removeScrollListener(ScrollListener paramScrollListener);
  
  public abstract void scrollToView(View paramView, int paramInt);
  
  public abstract void setScrollY(int paramInt);
  
  public abstract void smoothScrollToY(int paramInt);
  
  public abstract void smoothScrollToYSyncWithTransition(int paramInt1, ViewGroup paramViewGroup, int paramInt2);
  
  public static abstract interface ScrollListener
  {
    public abstract void onOverscroll(int paramInt);
    
    public abstract void onOverscrollFinished();
    
    public abstract void onOverscrollStarted();
    
    public abstract void onScrollAnimationFinished();
    
    public abstract void onScrollChanged(int paramInt1, int paramInt2);
    
    public abstract void onScrollFinished();
    
    public abstract void onScrollMarginConsumed(View paramView, int paramInt1, int paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.ScrollViewControl
 * JD-Core Version:    0.7.0.1
 */