package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.DismissableChildContainer;
import com.google.android.search.shared.ui.SwipeHelper;
import com.google.android.search.shared.ui.SwipeHelper.Callback;
import com.google.common.collect.ImmutableList;

public class DismissableLinearLayout
  extends LinearLayout
  implements SuggestionGridLayout.DismissableChildContainer
{
  private boolean mDismissEnabled = true;
  private OnDismissListener mDismissListener;
  private ViewGroup mDismissableContainer;
  private Rect mHitRect = new Rect();
  private boolean mIsDragging = false;
  private SwipeHelper.Callback mSwipeCallback;
  private SwipeHelper mSwiper;
  
  public DismissableLinearLayout(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public DismissableLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public DismissableLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private View getChildAtXYPosition(int paramInt1, int paramInt2)
  {
    int i = -1 + this.mDismissableContainer.getChildCount();
    if (i >= 0)
    {
      View localView = this.mDismissableContainer.getChildAt(i);
      if (localView.getVisibility() != 0) {}
      do
      {
        i--;
        break;
        localView.getHitRect(this.mHitRect);
      } while (!this.mHitRect.contains(paramInt1, paramInt2));
      return localView;
    }
    return null;
  }
  
  private Point getEventCoordsInDismissableContainer(ViewGroup paramViewGroup, MotionEvent paramMotionEvent)
  {
    int i = (int)(0.5F + paramMotionEvent.getX());
    int j = (int)(0.5F + paramMotionEvent.getY());
    if ((paramViewGroup == this) && (this.mDismissableContainer == null)) {
      return new Point(i, j);
    }
    Rect localRect = new Rect(i, j, i, j);
    if (this.mDismissableContainer != null) {
      paramViewGroup.offsetRectIntoDescendantCoords(this.mDismissableContainer, localRect);
    }
    for (;;)
    {
      return new Point(localRect.left, localRect.top);
      paramViewGroup.offsetRectIntoDescendantCoords(this, localRect);
    }
  }
  
  private void init()
  {
    Context localContext = getContext();
    float f = localContext.getResources().getDisplayMetrics().density;
    int i = ViewConfiguration.get(localContext).getScaledPagingTouchSlop();
    this.mSwipeCallback = new SwipeCallback(null);
    this.mSwiper = new SwipeHelper(0, this.mSwipeCallback, f, i);
    this.mDismissableContainer = this;
  }
  
  public int getDismissableChildCount()
  {
    int i = 0;
    for (int j = 0; j < this.mDismissableContainer.getChildCount(); j++) {
      if (this.mDismissableContainer.getChildAt(j).getVisibility() != 8) {
        i++;
      }
    }
    return i;
  }
  
  public boolean isDismissableViewAtPosition(SuggestionGridLayout paramSuggestionGridLayout, MotionEvent paramMotionEvent)
  {
    if (!this.mDismissEnabled) {}
    View localView;
    do
    {
      do
      {
        return false;
      } while (getDismissableChildCount() <= 1);
      Point localPoint = getEventCoordsInDismissableContainer(paramSuggestionGridLayout, paramMotionEvent);
      localView = getChildAtXYPosition(localPoint.x, localPoint.y);
    } while (localView == null);
    return this.mSwipeCallback.canChildBeDismissed(localView);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = true;
    if (!this.mDismissEnabled) {
      bool1 = super.onInterceptHoverEvent(paramMotionEvent);
    }
    boolean bool2;
    do
    {
      return bool1;
      bool2 = this.mSwiper.onInterceptTouchEvent(paramMotionEvent);
      if (bool2) {
        getParent().requestDisallowInterceptTouchEvent(bool1);
      }
    } while ((bool2) || (super.onInterceptTouchEvent(paramMotionEvent)));
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.mDismissEnabled) {
      return super.onTouchEvent(paramMotionEvent);
    }
    if (!this.mSwiper.onTouchEvent(paramMotionEvent)) {
      return super.onTouchEvent(paramMotionEvent);
    }
    return true;
  }
  
  public void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mSwiper.mAllowSwipeTowardsStart = paramBoolean1;
    this.mSwiper.mAllowSwipeTowardsEnd = paramBoolean2;
  }
  
  public void setDismissEnabled(boolean paramBoolean)
  {
    this.mDismissEnabled = paramBoolean;
  }
  
  public void setDismissableContainer(ViewGroup paramViewGroup)
  {
    this.mDismissableContainer = paramViewGroup;
  }
  
  public void setOnDismissListener(OnDismissListener paramOnDismissListener)
  {
    this.mDismissListener = paramOnDismissListener;
  }
  
  protected void updateItems() {}
  
  public static abstract interface OnDismissListener
  {
    public abstract void onViewDismissed(PendingViewDismiss paramPendingViewDismiss);
  }
  
  private class PendingViewDismissImpl
    extends PendingViewDismiss
  {
    public PendingViewDismissImpl(View paramView)
    {
      super();
    }
    
    public void doCommit()
    {
      DismissableLinearLayout.this.mDismissableContainer.removeView((View)getDismissedViews().get(0));
    }
    
    public void doRestore()
    {
      View localView = (View)getDismissedViews().get(0);
      localView.setVisibility(0);
      DismissableLinearLayout.this.mSwiper.resetTranslation(localView);
      DismissableLinearLayout.this.updateItems();
    }
  }
  
  private class SwipeCallback
    implements SwipeHelper.Callback
  {
    private SwipeCallback() {}
    
    public boolean canChildBeDismissed(View paramView)
    {
      return true;
    }
    
    void dragEnd(View paramView)
    {
      DismissableLinearLayout.access$102(DismissableLinearLayout.this, false);
      DismissableLinearLayout.this.invalidate();
    }
    
    public View getChildAtPosition(MotionEvent paramMotionEvent)
    {
      if (DismissableLinearLayout.this.mIsDragging) {
        return null;
      }
      Point localPoint = DismissableLinearLayout.this.getEventCoordsInDismissableContainer(DismissableLinearLayout.this, paramMotionEvent);
      return DismissableLinearLayout.this.getChildAtXYPosition(localPoint.x, localPoint.y);
    }
    
    public void onBeginDrag(View paramView)
    {
      DismissableLinearLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
      DismissableLinearLayout.access$102(DismissableLinearLayout.this, true);
      DismissableLinearLayout.this.invalidate();
    }
    
    public void onChildDismissed(View paramView)
    {
      paramView.setVisibility(8);
      DismissableLinearLayout.this.updateItems();
      DismissableLinearLayout.PendingViewDismissImpl localPendingViewDismissImpl = new DismissableLinearLayout.PendingViewDismissImpl(DismissableLinearLayout.this, paramView);
      DismissableLinearLayout.access$102(DismissableLinearLayout.this, false);
      DismissableLinearLayout.this.invalidate();
      if (DismissableLinearLayout.this.mDismissListener != null) {
        DismissableLinearLayout.this.mDismissListener.onViewDismissed(localPendingViewDismissImpl);
      }
      if (!localPendingViewDismissImpl.isIntercepted()) {
        localPendingViewDismissImpl.commit();
      }
    }
    
    public void onDragCancelled(View paramView) {}
    
    public void onSnapBackCompleted(View paramView)
    {
      dragEnd(paramView);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.DismissableLinearLayout
 * JD-Core Version:    0.7.0.1
 */