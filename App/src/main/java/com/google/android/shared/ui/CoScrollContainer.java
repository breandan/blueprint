package com.google.android.shared.ui;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;

public class CoScrollContainer
  extends FrameLayout
  implements ScrollViewControl
{
  private boolean mDisallowIntercept;
  @ViewDebug.ExportedProperty(category="velvet")
  private int mFooterPadding;
  private boolean mHaveScrollToConsume;
  @ViewDebug.ExportedProperty(category="velvet")
  private int mHeaderPadding;
  private View.OnTouchListener mInterceptedTouchEventListener;
  private final DecelerateInterpolator mInterpolator = new DecelerateInterpolator(2.5F);
  private LayoutParams mParamsOfChildCurrentlyScrolling;
  private int mPointerIdForFlingInterception = -1;
  private ScrollHelper mScrollHelper;
  private final List<ScrollViewControl.ScrollListener> mScrollListeners = Lists.newArrayList();
  private View mScrollToAfterLayoutDescendent;
  private int mScrollToAfterLayoutDuration;
  private TimeInterpolator mScrollToAfterLayoutInterpolator;
  private int mScrollToAfterLayoutOffset;
  private boolean mScrollableChildHandlingFocusChangeKeyEvent;
  private final Rect mTmpRect = new Rect();
  private final boolean mUseAppearAnimations;
  
  public CoScrollContainer(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CoScrollContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CoScrollContainer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (paramAttributeSet != null)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CoScrollContainer);
      this.mUseAppearAnimations = localTypedArray.getBoolean(0, true);
      localTypedArray.recycle();
      return;
    }
    this.mUseAppearAnimations = true;
  }
  
  private int adjustChildScrollToY(LayoutParams paramLayoutParams, int paramInt)
  {
    int i = getScrollY();
    if (((paramLayoutParams.mTranslationType == 5) || (i < paramLayoutParams.mResolvedTopInScrollableArea)) && (!this.mScrollableChildHandlingFocusChangeKeyEvent)) {
      return 0;
    }
    int j = paramInt + paramLayoutParams.mResolvedTopInScrollableArea;
    int k = 0;
    if (paramInt == 0) {}
    for (;;)
    {
      setTranslation(paramLayoutParams, j);
      this.mScrollableChildHandlingFocusChangeKeyEvent = true;
      super.scrollTo(getScrollX(), k);
      return paramInt;
      k = j;
    }
  }
  
  private int consumeChildVerticalScroll(LayoutParams paramLayoutParams, int paramInt)
  {
    if ((this.mParamsOfChildCurrentlyScrolling != null) && (this.mParamsOfChildCurrentlyScrolling != paramLayoutParams))
    {
      Log.w("Velvet.CoScrollContainer", "Multiple children causing a scroll?");
      LayoutParams.access$802(this.mParamsOfChildCurrentlyScrolling, 0);
    }
    int i = paramLayoutParams.mView.getScrollY();
    this.mParamsOfChildCurrentlyScrolling = paramLayoutParams;
    int j = paramInt - paramLayoutParams.mSpuriousScrollAmount;
    int k = j;
    if ((this.mHaveScrollToConsume) && (j > 0)) {
      j = consumeScrollDelta(j);
    }
    int m = Math.min(Math.max(0, j + getScrollY()), getMaxScrollY());
    super.scrollTo(getScrollX(), m);
    int n = paramLayoutParams.mResolvedTopInScrollableArea;
    int i1 = m - n;
    int i2 = paramLayoutParams.mScrollableChild.getScrollingContentHeight() - getHeight();
    setTranslation(paramLayoutParams, getTranslationForChild(n, i1, i2));
    int i3 = getScrollForChild(i1, i2) - i;
    LayoutParams.access$802(paramLayoutParams, i3 - k);
    return i3;
  }
  
  private int consumeScrollDelta(int paramInt)
  {
    int i = getChildCount();
    int j = 0;
    boolean bool = false;
    for (int k = 0; k < i; k++)
    {
      View localView = getChildAt(k);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (localLayoutParams.mScrollConsumableMargin > 0)
      {
        int m = Math.min(localLayoutParams.mScrollConsumableMargin, paramInt);
        LayoutParams.access$520(localLayoutParams, m);
        LayoutParams.access$720(localLayoutParams, m);
        j = Math.max(j, m);
        if (localLayoutParams != this.mParamsOfChildCurrentlyScrolling) {
          syncChild(localLayoutParams, paramInt);
        }
        if (localLayoutParams.mScrollConsumableMargin > 0) {
          bool = true;
        }
        notifyScrollMarginConsumed(localView, localLayoutParams.mScrollConsumableMargin, localLayoutParams.mOriginalConsumableMargin);
      }
    }
    this.mHaveScrollToConsume = bool;
    return paramInt - j;
  }
  
  private void dispatchScrollableChildKeyEvent(KeyEvent paramKeyEvent, View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!localLayoutParams.mIsScrolling) {}
    int i;
    do
    {
      CoScrollContainer.LayoutParams.ScrollableChild localScrollableChild;
      do
      {
        return;
        localScrollableChild = localLayoutParams.mScrollableChild;
      } while ((paramKeyEvent.getAction() != 0) || (paramKeyEvent.getKeyCode() != 20));
      i = localScrollableChild.getScrollingContentHeight() - getHeight();
    } while (paramView.getScrollY() < i);
    scrollTo(getScrollX(), getMaxScrollY());
  }
  
  private View findChildContainingDescendant(View paramView)
  {
    View localView = paramView;
    ViewParent localViewParent = localView.getParent();
    while (localViewParent != this) {
      if ((localViewParent instanceof View))
      {
        localView = (View)localViewParent;
        localViewParent = localView.getParent();
      }
      else
      {
        throw new IllegalArgumentException("Descendant isn't our descendant?");
      }
    }
    return localView;
  }
  
  private boolean handleKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 20)
    {
      if (shouldScroll(130)) {
        return this.mScrollHelper.onKeyDown(paramInt, paramKeyEvent);
      }
    }
    else if ((paramInt == 19) && (shouldScroll(33))) {
      return this.mScrollHelper.onKeyDown(paramInt, paramKeyEvent);
    }
    return false;
  }
  
  private boolean isFocusChangingKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    return (i == 61) || (i == 19) || (i == 20) || (i == 21) || (i == 22);
  }
  
  private static boolean isViewScrollable(View paramView)
  {
    return (paramView != null) && (((LayoutParams)paramView.getLayoutParams()).mIsScrolling);
  }
  
  private void notifyScroll()
  {
    int i = getScrollY();
    int j = getMaxScrollY();
    if (!this.mScrollHelper.isFlinging()) {
      j = Math.max(i, j);
    }
    for (int k = -1 + this.mScrollListeners.size(); k >= 0; k--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(k)).onScrollChanged(i, j);
    }
  }
  
  private void notifyScrollMarginConsumed(View paramView, int paramInt1, int paramInt2)
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onScrollMarginConsumed(paramView, paramInt1, paramInt2);
    }
  }
  
  private void resolveChildTop(LayoutParams paramLayoutParams)
  {
    int i = paramLayoutParams.mTranslationType;
    int j = 0;
    switch (i)
    {
    }
    for (;;)
    {
      LayoutParams.access$502(paramLayoutParams, j + paramLayoutParams.mScrollConsumableMargin);
      return;
      j = this.mHeaderPadding;
      continue;
      int k = getDescendantTop(paramLayoutParams.mAnchor);
      if (k >= 0)
      {
        j = k + paramLayoutParams.mAnchor.getHeight() + paramLayoutParams.mAnchorMargin;
      }
      else
      {
        Log.w("Velvet.CoScrollContainer", "Scroll anchor is not a descendant");
        j = 0;
        continue;
        j = paramLayoutParams.mView.getTop();
      }
    }
  }
  
  private void scrollToAfterLayout(View paramView, int paramInt1, TimeInterpolator paramTimeInterpolator, int paramInt2)
  {
    this.mScrollToAfterLayoutDescendent = paramView;
    this.mScrollToAfterLayoutOffset = paramInt1;
    this.mScrollToAfterLayoutInterpolator = paramTimeInterpolator;
    this.mScrollToAfterLayoutDuration = paramInt2;
  }
  
  private void setOrAnimateTranslation(LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    int i = (int)paramLayoutParams.mView.getTranslationY();
    if (paramInt1 != i)
    {
      long l1;
      long l2;
      if (i < 0)
      {
        if (this.mUseAppearAnimations)
        {
          i = getScrollY() + getHeight();
          paramLayoutParams.mView.setTranslationY(i);
        }
      }
      else
      {
        l1 = 0L;
        if ((paramLayoutParams.mAnimationEndTime == 0L) || (paramLayoutParams.mAnimationTargetTranslation != paramInt1))
        {
          l2 = SystemClock.uptimeMillis();
          if (Math.abs(i - paramInt1) > Math.abs(paramInt2))
          {
            if ((paramLayoutParams.mAnimationEndTime <= 0L) || (paramInt2 == 0)) {
              break label168;
            }
            l1 = paramLayoutParams.mAnimationEndTime - l2;
          }
        }
      }
      while (l1 > 50L)
      {
        paramLayoutParams.mView.animate().translationY(paramInt1).setInterpolator(this.mInterpolator).setDuration(l1).withEndAction(paramLayoutParams.mEndAction);
        LayoutParams.access$1402(paramLayoutParams, paramInt1);
        return;
        setTranslation(paramLayoutParams, paramInt1);
        return;
        label168:
        l1 = 400L;
        LayoutParams.access$1302(paramLayoutParams, l2 + l1);
      }
      setTranslation(paramLayoutParams, paramInt1);
      return;
    }
    paramLayoutParams.mView.animate().cancel();
    LayoutParams.access$1302(paramLayoutParams, 0L);
  }
  
  private void setTranslation(LayoutParams paramLayoutParams, int paramInt)
  {
    paramLayoutParams.mView.animate().cancel();
    LayoutParams.access$1302(paramLayoutParams, 0L);
    paramLayoutParams.mView.setTranslationY(paramInt);
  }
  
  private boolean shouldScroll(int paramInt)
  {
    View localView1 = findFocus();
    if (localView1 == null) {}
    for (;;)
    {
      return true;
      int i = getScrollY();
      int j = getViewportHeight();
      this.mTmpRect.set(0, 0, localView1.getWidth(), localView1.getHeight());
      offsetDescendantRectToMyCoords(localView1, this.mTmpRect);
      if ((paramInt == 130) && (this.mTmpRect.bottom < i)) {
        return false;
      }
      if ((paramInt == 33) && (this.mTmpRect.top > i + j)) {
        return false;
      }
      View localView2 = localView1.focusSearch(paramInt);
      if (localView2 != null)
      {
        this.mTmpRect.set(0, 0, localView2.getWidth(), localView2.getHeight());
        try
        {
          offsetDescendantRectToMyCoords(localView2, this.mTmpRect);
          if ((paramInt == 33) && (this.mTmpRect.top > i)) {
            return false;
          }
          if (paramInt == 130)
          {
            int k = this.mTmpRect.bottom;
            if (k < i + j) {
              return false;
            }
          }
        }
        catch (IllegalArgumentException localIllegalArgumentException) {}
      }
    }
    return true;
  }
  
  private void syncChild(LayoutParams paramLayoutParams, int paramInt)
  {
    switch (paramLayoutParams.mTranslationType)
    {
    default: 
    case 5: 
      do
      {
        return;
        setTranslation(paramLayoutParams, -getHeight());
      } while (!paramLayoutParams.mIsScrolling);
      paramLayoutParams.mScrollableChild.setScrollYFromContainer(0);
      return;
    case 1: 
    case 2: 
      int i = paramLayoutParams.mResolvedTopInScrollableArea;
      int j = getScrollY() - i;
      if (paramLayoutParams.mIsScrolling)
      {
        CoScrollContainer.LayoutParams.ScrollableChild localScrollableChild = paramLayoutParams.mScrollableChild;
        int k = localScrollableChild.getScrollingContentHeight() - getHeight();
        localScrollableChild.setScrollYFromContainer(getScrollForChild(j, k));
        setOrAnimateTranslation(paramLayoutParams, getTranslationForChild(i, j, k), paramInt);
        return;
      }
      setOrAnimateTranslation(paramLayoutParams, paramLayoutParams.mResolvedTopInScrollableArea, paramInt);
      return;
    case 6: 
      setTranslation(paramLayoutParams, paramLayoutParams.mResolvedTopInScrollableArea);
      return;
    case 3: 
      setTranslation(paramLayoutParams, computeVerticalScrollRange() - this.mFooterPadding);
      return;
    case 4: 
      setOrAnimateTranslation(paramLayoutParams, Math.max(getScrollY(), this.mHeaderPadding), paramInt);
      return;
    }
    setOrAnimateTranslation(paramLayoutParams, paramLayoutParams.mView.getTop(), paramInt);
  }
  
  private void syncChildren(boolean paramBoolean, int paramInt)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(j).getLayoutParams();
      if (localLayoutParams.mTranslationType != 0)
      {
        if (paramBoolean) {
          resolveChildTop(localLayoutParams);
        }
        syncChild(localLayoutParams, paramInt);
      }
    }
  }
  
  public void addScrollListener(ScrollViewControl.ScrollListener paramScrollListener)
  {
    this.mScrollListeners.add(paramScrollListener);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramInt, paramLayoutParams);
    ((LayoutParams)paramView.getLayoutParams()).setView(paramView);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams))
    {
      LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
      if (localLayoutParams.mContainer == null) {
        LayoutParams.access$302(localLayoutParams, this);
      }
      while (localLayoutParams.mContainer == this) {
        return true;
      }
      return false;
    }
    return false;
  }
  
  public void computeScroll()
  {
    this.mScrollHelper.computeScroll();
  }
  
  protected int computeVerticalScrollRange()
  {
    int i = getMeasuredHeight();
    int j = 0;
    if (j < getChildCount())
    {
      View localView = getChildAt(j);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (localLayoutParams.mIsScrolling) {
        if (localLayoutParams.mTranslationType == 5) {}
      }
      for (i = Math.max(i, localLayoutParams.mResolvedTopInScrollableArea + localLayoutParams.mScrollableChild.getScrollingContentHeight() + this.mFooterPadding);; i = Math.max(i, localLayoutParams.mScrollConsumableMargin + localView.getMeasuredHeight()))
      {
        j++;
        break;
      }
    }
    return i;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    View localView = getFocusedChild();
    boolean bool1 = isViewScrollable(localView);
    boolean bool2 = isFocusChangingKeyEvent(paramKeyEvent);
    boolean bool3;
    if ((bool1) && (bool2))
    {
      bool3 = true;
      this.mScrollableChildHandlingFocusChangeKeyEvent = bool3;
      if (!super.dispatchKeyEvent(paramKeyEvent)) {
        break label61;
      }
      if (bool1) {
        dispatchScrollableChildKeyEvent(paramKeyEvent, localView);
      }
    }
    label61:
    while ((!bool1) && (paramKeyEvent.getAction() == 0) && (handleKeyDown(paramKeyEvent.getKeyCode(), paramKeyEvent)))
    {
      return true;
      bool3 = false;
      break;
    }
    return false;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getActionMasked())
    {
    }
    for (;;)
    {
      return super.dispatchTouchEvent(paramMotionEvent);
      this.mPointerIdForFlingInterception = paramMotionEvent.getPointerId(0);
      this.mParamsOfChildCurrentlyScrolling = null;
      continue;
      if (this.mDisallowIntercept)
      {
        this.mScrollHelper.trackVelocityForFlingIntercept(paramMotionEvent);
        continue;
        if ((this.mDisallowIntercept) && (this.mParamsOfChildCurrentlyScrolling != null) && (this.mPointerIdForFlingInterception != -1) && (!this.mScrollHelper.maybeStartInterceptedFling(this.mPointerIdForFlingInterception))) {
          notifyScrollFinished();
        }
        this.mDisallowIntercept = false;
        if (this.mParamsOfChildCurrentlyScrolling != null)
        {
          LayoutParams.access$802(this.mParamsOfChildCurrentlyScrolling, 0);
          this.mParamsOfChildCurrentlyScrolling = null;
        }
        this.mPointerIdForFlingInterception = -1;
      }
    }
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(this);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), this, paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(this, paramLayoutParams);
  }
  
  public LayoutParams generateOffscreenLayoutParams()
  {
    return new LayoutParams(this, 5);
  }
  
  public int getDescendantTop(View paramView)
  {
    this.mTmpRect.setEmpty();
    View localView;
    LayoutParams localLayoutParams;
    try
    {
      offsetDescendantRectToMyCoords(paramView, this.mTmpRect);
      localView = findChildContainingDescendant(paramView);
      localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (localLayoutParams.mTranslationType == 5) {
        return -1;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      return -1;
    }
    int i = this.mTmpRect.top;
    if (localLayoutParams.mIsScrolling) {
      i += localView.getScrollY();
    }
    return i + localLayoutParams.mResolvedTopInScrollableArea;
  }
  
  public int getMaxScrollY()
  {
    return computeVerticalScrollRange() - getHeight();
  }
  
  int getScrollForChild(int paramInt1, int paramInt2)
  {
    if (paramInt1 <= 0) {
      return 0;
    }
    return Math.min(paramInt1, paramInt2);
  }
  
  int getTranslationForChild(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 <= 0) {
      return paramInt1;
    }
    if (paramInt2 < paramInt3) {
      return getScrollY();
    }
    return paramInt1 + paramInt3;
  }
  
  public int getViewportHeight()
  {
    return getHeight();
  }
  
  public boolean isAnimatingScroll()
  {
    return (this.mScrollToAfterLayoutDescendent != null) || (this.mScrollHelper.isAnimatingScroll());
  }
  
  public void notifyOverscroll(int paramInt)
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onOverscroll(paramInt);
    }
  }
  
  public void notifyOverscrollFinish()
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onOverscrollFinished();
    }
  }
  
  public void notifyOverscrollStart()
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onOverscrollStarted();
    }
  }
  
  public void notifyScrollAnimationFinished()
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onScrollAnimationFinished();
    }
  }
  
  public void notifyScrollFinished()
  {
    for (int i = -1 + this.mScrollListeners.size(); i >= 0; i--) {
      ((ScrollViewControl.ScrollListener)this.mScrollListeners.get(i)).onScrollFinished();
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    this.mScrollHelper.drawOverscrollEffect(paramCanvas);
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mScrollHelper = new ScrollHelper(getContext(), this, this, 30);
    setWillNotDraw(false);
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (this.mScrollHelper.onGenericMotionEvent(paramMotionEvent)) {
      return true;
    }
    return super.onGenericMotionEvent(paramMotionEvent);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(CoScrollContainer.class.getCanonicalName());
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mScrollHelper.onInterceptTouchEvent(paramMotionEvent)) {}
    for (boolean bool = true;; bool = super.onInterceptTouchEvent(paramMotionEvent))
    {
      if ((bool) && (this.mInterceptedTouchEventListener != null)) {
        this.mInterceptedTouchEventListener.onTouch(this, paramMotionEvent);
      }
      return bool;
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (handleKeyDown(paramInt, paramKeyEvent)) {
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    syncChildren(true, 0);
    boolean bool;
    if (this.mScrollToAfterLayoutDescendent != null)
    {
      if (this.mScrollToAfterLayoutDescendent != this) {
        break label102;
      }
      if (this.mScrollToAfterLayoutInterpolator != null) {
        break label78;
      }
      bool = this.mScrollHelper.smoothScrollTo(this.mScrollToAfterLayoutOffset);
    }
    for (;;)
    {
      this.mScrollToAfterLayoutDescendent = null;
      if (!bool) {
        notifyScrollAnimationFinished();
      }
      this.mScrollHelper.onMaxScrollChanged();
      notifyScroll();
      return;
      label78:
      bool = this.mScrollHelper.smoothScrollTo(this.mScrollToAfterLayoutOffset, this.mScrollToAfterLayoutInterpolator, this.mScrollToAfterLayoutDuration);
      continue;
      label102:
      int i = getDescendantTop(this.mScrollToAfterLayoutDescendent);
      bool = false;
      if (i >= 0)
      {
        int j = i - this.mScrollToAfterLayoutOffset;
        if (this.mScrollToAfterLayoutInterpolator == null) {
          bool = this.mScrollHelper.smoothScrollTo(j);
        } else {
          bool = this.mScrollHelper.smoothScrollTo(j, this.mScrollToAfterLayoutInterpolator, this.mScrollToAfterLayoutDuration);
        }
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool1 = true;
    boolean bool2;
    label30:
    int i;
    int j;
    int k;
    int m;
    label56:
    View localView;
    LayoutParams localLayoutParams;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824)
    {
      bool2 = bool1;
      Preconditions.checkState(bool2);
      if (View.MeasureSpec.getMode(paramInt2) != 1073741824) {
        break label110;
      }
      Preconditions.checkState(bool1);
      i = View.MeasureSpec.getSize(paramInt1);
      j = View.MeasureSpec.getSize(paramInt2);
      k = View.MeasureSpec.makeMeasureSpec(0, 0);
      m = 0;
      if (m >= getChildCount()) {
        break label151;
      }
      localView = getChildAt(m);
      localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (!localLayoutParams.mIsScrolling) {
        break label115;
      }
      localView.measure(paramInt1, paramInt2);
    }
    for (;;)
    {
      m++;
      break label56;
      bool2 = false;
      break;
      label110:
      bool1 = false;
      break label30;
      label115:
      localView.measure(paramInt1, k);
      if ((localLayoutParams.mFillViewport) && (localView.getMeasuredHeight() < j)) {
        localView.measure(paramInt1, paramInt2);
      }
    }
    label151:
    setMeasuredDimension(i, j);
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    notifyScroll();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mScrollHelper.onTouchEvent(paramMotionEvent)) {
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void removeScrollListener(ScrollViewControl.ScrollListener paramScrollListener)
  {
    this.mScrollListeners.remove(paramScrollListener);
  }
  
  public void removeView(View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (localLayoutParams.mContainer == this) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      super.removeView(paramView);
      LayoutParams.access$302(localLayoutParams, null);
      return;
    }
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    this.mDisallowIntercept = paramBoolean;
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void scrollTo(int paramInt1, int paramInt2)
  {
    int i = getScrollY();
    int j = paramInt2 - i;
    if ((j > 0) && (this.mHaveScrollToConsume)) {
      paramInt2 = i + consumeScrollDelta(j);
    }
    super.scrollTo(paramInt1, paramInt2);
    syncChildren(false, j);
  }
  
  public void scrollToView(View paramView, int paramInt)
  {
    if (isLayoutRequested()) {
      scrollToAfterLayout(paramView, paramInt, null, -1);
    }
    int i;
    do
    {
      return;
      i = getDescendantTop(paramView);
    } while (i < 0);
    this.mScrollHelper.smoothScrollTo(i - paramInt);
  }
  
  public void setHeaderAndFooterPadding(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != this.mHeaderPadding) || (paramInt2 != this.mFooterPadding))
    {
      this.mHeaderPadding = paramInt1;
      this.mFooterPadding = paramInt2;
      syncChildren(true, 0);
    }
  }
  
  public void setInterceptedTouchEventListener(View.OnTouchListener paramOnTouchListener)
  {
    this.mInterceptedTouchEventListener = paramOnTouchListener;
  }
  
  public void smoothScrollToY(int paramInt)
  {
    if (isLayoutRequested())
    {
      scrollToAfterLayout(this, paramInt, null, -1);
      return;
    }
    this.mScrollHelper.smoothScrollTo(paramInt);
  }
  
  public void smoothScrollToYSyncWithTransition(int paramInt1, ViewGroup paramViewGroup, int paramInt2)
  {
    LayoutTransition localLayoutTransition = paramViewGroup.getLayoutTransition();
    if (localLayoutTransition == null) {
      smoothScrollToY(paramInt1);
    }
    Animator localAnimator = localLayoutTransition.getAnimator(paramInt2);
    if (localAnimator == null) {
      smoothScrollToY(paramInt1);
    }
    TimeInterpolator localTimeInterpolator = localLayoutTransition.getInterpolator(paramInt2);
    int i = (int)localAnimator.getDuration();
    if (isLayoutRequested())
    {
      scrollToAfterLayout(this, paramInt1, localTimeInterpolator, i);
      return;
    }
    this.mScrollHelper.smoothScrollTo(paramInt1, localTimeInterpolator, i);
  }
  
  public static class LayoutParams
    extends FrameLayout.LayoutParams
  {
    private View mAnchor;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private int mAnchorMargin;
    private long mAnimationEndTime;
    private long mAnimationTargetTranslation;
    private CoScrollContainer mContainer;
    private final Runnable mEndAction = new Runnable()
    {
      public void run()
      {
        CoScrollContainer.LayoutParams.access$1302(CoScrollContainer.LayoutParams.this, 0L);
      }
    };
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private boolean mFillViewport;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private boolean mIsScrolling;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private int mOriginalConsumableMargin;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private int mResolvedTopInScrollableArea;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private int mScrollConsumableMargin;
    private ScrollableChild mScrollableChild;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer")
    private int mSpuriousScrollAmount;
    @ViewDebug.ExportedProperty(category="layout_CoScrollContainer", mapping={@android.view.ViewDebug.IntToString(from=0, to="regular"), @android.view.ViewDebug.IntToString(from=1, to="header"), @android.view.ViewDebug.IntToString(from=3, to="footer"), @android.view.ViewDebug.IntToString(from=4, to="header onwards"), @android.view.ViewDebug.IntToString(from=5, to="offscreen"), @android.view.ViewDebug.IntToString(from=6, to="draggable")})
    private int mTranslationType;
    private View mView;
    
    public LayoutParams(Context paramContext, CoScrollContainer paramCoScrollContainer, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      this.mContainer = paramCoScrollContainer;
      this.mFillViewport = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CoScrollContainer_Layout).getBoolean(0, false);
    }
    
    public LayoutParams(CoScrollContainer paramCoScrollContainer)
    {
      this(paramCoScrollContainer, 0);
    }
    
    public LayoutParams(CoScrollContainer paramCoScrollContainer, int paramInt)
    {
      super(-1);
      this.mContainer = paramCoScrollContainer;
      this.mTranslationType = paramInt;
    }
    
    public LayoutParams(CoScrollContainer paramCoScrollContainer, ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
      this.mContainer = paramCoScrollContainer;
      if ((paramLayoutParams instanceof LayoutParams))
      {
        LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
        this.mTranslationType = localLayoutParams.mTranslationType;
        this.mFillViewport = localLayoutParams.mFillViewport;
        this.mView = localLayoutParams.mView;
        this.mAnchor = localLayoutParams.mAnchor;
        this.mAnchorMargin = localLayoutParams.mAnchorMargin;
        this.mIsScrolling = localLayoutParams.mIsScrolling;
        this.mScrollableChild = localLayoutParams.mScrollableChild;
        this.mResolvedTopInScrollableArea = localLayoutParams.mResolvedTopInScrollableArea;
      }
    }
    
    private void setParams(int paramInt1, View paramView, int paramInt2, int paramInt3)
    {
      this.mTranslationType = paramInt1;
      this.mAnchor = paramView;
      this.mAnchorMargin = paramInt2;
      this.mScrollConsumableMargin = paramInt3;
      this.mOriginalConsumableMargin = paramInt3;
      if (this.mContainer != null)
      {
        if (this.mScrollConsumableMargin > 0) {
          CoScrollContainer.access$1802(this.mContainer, true);
        }
        this.mContainer.resolveChildTop(this);
        this.mContainer.syncChild(this, 0);
      }
    }
    
    private void setView(View paramView)
    {
      this.mView = paramView;
      this.mIsScrolling = (paramView instanceof ScrollableChild);
      if (this.mIsScrolling) {}
      for (ScrollableChild localScrollableChild = (ScrollableChild)paramView;; localScrollableChild = null)
      {
        this.mScrollableChild = localScrollableChild;
        return;
      }
    }
    
    public int adjustScrollToY(int paramInt)
    {
      if (this.mContainer != null) {
        paramInt = this.mContainer.adjustChildScrollToY(this, paramInt);
      }
      return paramInt;
    }
    
    public int consumeVerticalScroll(int paramInt)
    {
      if (this.mContainer != null) {
        paramInt = this.mContainer.consumeChildVerticalScroll(this, paramInt);
      }
      return paramInt;
    }
    
    public void cropDrawingRectByPadding(Rect paramRect)
    {
      paramRect.top += this.mContainer.mHeaderPadding;
      paramRect.bottom -= this.mContainer.mFooterPadding;
    }
    
    public float getCollapsibleMarginRatio()
    {
      return this.mScrollConsumableMargin / this.mOriginalConsumableMargin;
    }
    
    public boolean isOffscreen()
    {
      return this.mTranslationType == 5;
    }
    
    public void resetParams()
    {
      setParams(0, null, 0, 0);
    }
    
    public void setParams(int paramInt)
    {
      if ((paramInt != 2) && (paramInt != 0)) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        setParams(paramInt, null, 0, 0);
        return;
      }
    }
    
    public void setParams(int paramInt1, int paramInt2)
    {
      int i = 1;
      if ((paramInt1 == i) || (paramInt1 == 6)) {}
      for (;;)
      {
        Preconditions.checkState(i);
        setParams(paramInt1, null, 0, paramInt2);
        return;
        i = 0;
      }
    }
    
    public void setParams(View paramView, int paramInt1, int paramInt2)
    {
      setParams(2, paramView, paramInt1, paramInt2);
    }
    
    public String toString()
    {
      return super.toString();
    }
    
    public static abstract interface ScrollableChild
    {
      public abstract int getScrollingContentHeight();
      
      public abstract void setScrollYFromContainer(int paramInt);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.ui.CoScrollContainer
 * JD-Core Version:    0.7.0.1
 */