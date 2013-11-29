package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import java.util.ArrayList;

public abstract class PagedView
  extends ViewGroup
  implements ViewGroup.OnHierarchyChangeListener
{
  private int DELETE_SLIDE_IN_SIDE_PAGE_DURATION = 250;
  private int DRAG_TO_DELETE_FADE_OUT_DURATION = 350;
  private int FLING_TO_DELETE_FADE_OUT_DURATION = 350;
  private float FLING_TO_DELETE_FRICTION = 0.035F;
  private float FLING_TO_DELETE_MAX_FLING_DEGREES = 65.0F;
  private int NUM_ANIMATIONS_RUNNING_BEFORE_ZOOM_OUT = 2;
  private int REORDERING_DROP_REPOSITION_DURATION = 200;
  protected int REORDERING_REORDER_REPOSITION_DURATION = 300;
  private int REORDERING_SIDE_PAGE_HOVER_TIMEOUT = 80;
  protected int REORDERING_ZOOM_IN_OUT_DURATION = 250;
  protected int mActivePointerId = -1;
  protected boolean mAllowLongPress = true;
  protected boolean mAllowOverScroll = true;
  private boolean mAllowPagedViewAnimations = true;
  private Rect mAltTmpRect = new Rect();
  private boolean mCancelTap;
  protected int mCellCountX = 0;
  protected int mCellCountY = 0;
  protected boolean mCenterPagesVertically;
  protected int mChildCountOnLastLayout;
  protected boolean mContentIsRefreshable = true;
  protected int mCurrentPage;
  protected boolean mDeferLoadAssociatedPagesUntilScrollCompletes = false;
  protected boolean mDeferScrollUpdate = false;
  private boolean mDeferringForDelete = false;
  private View mDeleteDropTarget;
  protected float mDensity;
  protected ArrayList<Boolean> mDirtyPageContent;
  private float mDownMotionX;
  private float mDownMotionY;
  private float mDownScrollX;
  protected View mDragView;
  private float mDragViewBaselineLeft;
  protected boolean mFadeInAdjacentScreens = false;
  protected boolean mFirstLayout = true;
  protected int mFlingThresholdVelocity;
  protected int mFlingToDeleteThresholdVelocity = -1400;
  protected boolean mForceDrawAllChildrenNextFrame;
  protected boolean mForceScreenScrolled = false;
  private boolean mFreeScroll = false;
  private int mFreeScrollMaxScrollX = -1;
  private int mFreeScrollMinScrollX = -1;
  protected final Rect mInsets = new Rect();
  protected boolean mIsDataReady = false;
  protected boolean mIsPageMoving = false;
  private boolean mIsReordering;
  protected float mLastMotionX;
  protected float mLastMotionXRemainder;
  protected float mLastMotionY;
  private int mLastScreenCenter = -1;
  protected View.OnLongClickListener mLongClickListener;
  protected int mMaxScrollX;
  private int mMaximumVelocity;
  protected int mMinFlingVelocity;
  private float mMinScale = 1.0F;
  protected int mMinSnapVelocity;
  protected int mNextPage = -1;
  private int mNormalChildHeight;
  protected int mOverScrollX;
  private PageIndicator mPageIndicator;
  private int mPageIndicatorViewId;
  protected int mPageLayoutHeightGap;
  protected int mPageLayoutPaddingBottom;
  protected int mPageLayoutPaddingLeft;
  protected int mPageLayoutPaddingRight;
  protected int mPageLayoutPaddingTop;
  protected int mPageLayoutWidthGap;
  private int[] mPageScrolls;
  private int mPageSpacing = 0;
  private PageSwitchListener mPageSwitchListener;
  private int mPagingTouchSlop;
  private float mParentDownMotionX;
  private float mParentDownMotionY;
  private int mPostReorderingPreZoomInRemainingAnimationCount;
  private Runnable mPostReorderingPreZoomInRunnable;
  private boolean mReorderingStarted = false;
  protected int mRestorePage = -1001;
  private boolean mScrollAbortedFromIntercept = false;
  protected Scroller mScroller;
  private int mSidePageHoverIndex = -1;
  private Runnable mSidePageHoverRunnable;
  protected float mSmoothingTime;
  protected int[] mTempVisiblePagesRange = new int[2];
  private int[] mTmpIntPoint = new int[2];
  private Matrix mTmpInvMatrix = new Matrix();
  private float[] mTmpPoint = new float[2];
  private Rect mTmpRect = new Rect();
  private boolean mTopAlignPageWhenShrinkingForBouncer = false;
  protected float mTotalMotionX;
  protected int mTouchSlop;
  protected int mTouchState = 0;
  protected float mTouchX;
  protected int mUnboundedScrollX;
  private boolean mUseMinScale = false;
  protected boolean mUsePagingTouchSlop = true;
  private VelocityTracker mVelocityTracker;
  private Rect mViewport = new Rect();
  
  public PagedView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagedView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PagedView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PagedView, paramInt, 0);
    this.mPageLayoutPaddingTop = localTypedArray.getDimensionPixelSize(2, 0);
    this.mPageLayoutPaddingBottom = localTypedArray.getDimensionPixelSize(3, 0);
    this.mPageLayoutPaddingLeft = localTypedArray.getDimensionPixelSize(4, 0);
    this.mPageLayoutPaddingRight = localTypedArray.getDimensionPixelSize(5, 0);
    this.mPageLayoutWidthGap = localTypedArray.getDimensionPixelSize(0, 0);
    this.mPageLayoutHeightGap = localTypedArray.getDimensionPixelSize(1, 0);
    this.mPageIndicatorViewId = localTypedArray.getResourceId(6, -1);
    localTypedArray.recycle();
    setHapticFeedbackEnabled(false);
    init();
  }
  
  private void abortScrollerAnimation(boolean paramBoolean)
  {
    this.mScroller.abortAnimation();
    if (paramBoolean) {
      this.mNextPage = -1;
    }
  }
  
  private void acquireVelocityTrackerAndAddMovement(MotionEvent paramMotionEvent)
  {
    if (this.mVelocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
  }
  
  private Runnable createPostDeleteAnimationRunnable(final View paramView)
  {
    new Runnable()
    {
      public void run()
      {
        int i = PagedView.this.indexOfChild(paramView);
        PagedView.this.getOverviewModePages(PagedView.this.mTempVisiblePagesRange);
        int j;
        int k;
        label68:
        int m;
        label89:
        int n;
        int i1;
        label120:
        int i2;
        label129:
        ArrayList localArrayList;
        int i3;
        label142:
        View localView;
        int i4;
        if (PagedView.this.mTempVisiblePagesRange[0] == PagedView.this.mTempVisiblePagesRange[1])
        {
          j = 1;
          if ((j == 0) && (i <= PagedView.this.mTempVisiblePagesRange[0])) {
            break label357;
          }
          k = 1;
          if (k != 0) {
            PagedView.this.snapToPageImmediately(i - 1);
          }
          if (j == 0) {
            break label362;
          }
          m = 0;
          n = Math.min(PagedView.this.mTempVisiblePagesRange[1], -1 + PagedView.this.getPageCount());
          if (k == 0) {
            break label376;
          }
          i1 = m;
          if (k == 0) {
            break label384;
          }
          i2 = i - 1;
          localArrayList = new ArrayList();
          i3 = i1;
          if (i3 > i2) {
            break label444;
          }
          localView = PagedView.this.getChildAt(i3);
          if (k == 0) {
            break label415;
          }
          if (i3 != 0) {
            break label391;
          }
          i4 = PagedView.this.getViewportOffsetX() + PagedView.this.getChildOffset(i3) - PagedView.this.getChildWidth(i3) - PagedView.this.mPageSpacing;
        }
        label206:
        for (int i5 = PagedView.this.getViewportOffsetX() + PagedView.this.getChildOffset(i3);; i5 = 0)
        {
          AnimatorSet localAnimatorSet2 = (AnimatorSet)localView.getTag();
          if (localAnimatorSet2 != null) {
            localAnimatorSet2.cancel();
          }
          localView.setAlpha(Math.max(localView.getAlpha(), 0.01F));
          localView.setTranslationX(i4 - i5);
          AnimatorSet localAnimatorSet3 = new AnimatorSet();
          Animator[] arrayOfAnimator = new Animator[2];
          arrayOfAnimator[0] = ObjectAnimator.ofFloat(localView, "translationX", new float[] { 0.0F });
          arrayOfAnimator[1] = ObjectAnimator.ofFloat(localView, "alpha", new float[] { 1.0F });
          localAnimatorSet3.playTogether(arrayOfAnimator);
          localArrayList.add(localAnimatorSet3);
          localView.setTag(100, localAnimatorSet3);
          i3++;
          break label142;
          j = 0;
          break;
          label357:
          k = 0;
          break label68;
          label362:
          m = PagedView.this.mTempVisiblePagesRange[0];
          break label89;
          label376:
          i1 = i + 1;
          break label120;
          i2 = n;
          break label129;
          i4 = PagedView.this.getViewportOffsetX() + PagedView.this.getChildOffset(i3 - 1);
          break label206;
          i4 = PagedView.this.getChildOffset(i3) - PagedView.this.getChildOffset(i3 - 1);
        }
        label384:
        label391:
        label415:
        label444:
        AnimatorSet localAnimatorSet1 = new AnimatorSet();
        localAnimatorSet1.playTogether(localArrayList);
        localAnimatorSet1.setDuration(PagedView.this.DELETE_SLIDE_IN_SIDE_PAGE_DURATION);
        AnimatorListenerAdapter local1 = new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymous2Animator)
          {
            PagedView.access$602(PagedView.this, false);
            PagedView.this.onEndReordering();
            PagedView.this.onRemoveViewAnimationCompleted();
          }
        };
        localAnimatorSet1.addListener(local1);
        localAnimatorSet1.start();
        PagedView.this.removeView(paramView);
        PagedView.this.onRemoveView(paramView, true);
      }
    };
  }
  
  private void forceFinishScroller()
  {
    this.mScroller.forceFinished(true);
    this.mNextPage = -1;
  }
  
  private boolean isHoveringOverDeleteDropTarget(int paramInt1, int paramInt2)
  {
    View localView1 = this.mDeleteDropTarget;
    boolean bool = false;
    if (localView1 != null)
    {
      this.mAltTmpRect.set(0, 0, 0, 0);
      View localView2 = (View)this.mDeleteDropTarget.getParent();
      if (localView2 != null) {
        localView2.getGlobalVisibleRect(this.mAltTmpRect);
      }
      this.mDeleteDropTarget.getGlobalVisibleRect(this.mTmpRect);
      this.mTmpRect.offset(-this.mAltTmpRect.left, -this.mAltTmpRect.top);
      bool = this.mTmpRect.contains(paramInt1, paramInt2);
    }
    return bool;
  }
  
  private boolean isTouchPointInViewportWithBuffer(int paramInt1, int paramInt2)
  {
    this.mTmpRect.set(this.mViewport.left - this.mViewport.width() / 2, this.mViewport.top, this.mViewport.right + this.mViewport.width() / 2, this.mViewport.bottom);
    return this.mTmpRect.contains(paramInt1, paramInt2);
  }
  
  private void onDropToDelete()
  {
    View localView = this.mDragView;
    ArrayList localArrayList = new ArrayList();
    AnimatorSet localAnimatorSet1 = new AnimatorSet();
    localAnimatorSet1.setInterpolator(new DecelerateInterpolator(2.0F));
    Animator[] arrayOfAnimator1 = new Animator[2];
    arrayOfAnimator1[0] = ObjectAnimator.ofFloat(localView, "scaleX", new float[] { 0.0F });
    arrayOfAnimator1[1] = ObjectAnimator.ofFloat(localView, "scaleY", new float[] { 0.0F });
    localAnimatorSet1.playTogether(arrayOfAnimator1);
    localArrayList.add(localAnimatorSet1);
    AnimatorSet localAnimatorSet2 = new AnimatorSet();
    localAnimatorSet2.setInterpolator(new LinearInterpolator());
    Animator[] arrayOfAnimator2 = new Animator[1];
    arrayOfAnimator2[0] = ObjectAnimator.ofFloat(localView, "alpha", new float[] { 0.0F });
    localAnimatorSet2.playTogether(arrayOfAnimator2);
    localArrayList.add(localAnimatorSet2);
    final Runnable localRunnable = createPostDeleteAnimationRunnable(localView);
    AnimatorSet localAnimatorSet3 = new AnimatorSet();
    localAnimatorSet3.playTogether(localArrayList);
    localAnimatorSet3.setDuration(this.DRAG_TO_DELETE_FADE_OUT_DURATION);
    localAnimatorSet3.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        localRunnable.run();
      }
    });
    localAnimatorSet3.start();
    this.mDeferringForDelete = true;
  }
  
  private void onPostReorderingAnimationCompleted()
  {
    this.mPostReorderingPreZoomInRemainingAnimationCount = (-1 + this.mPostReorderingPreZoomInRemainingAnimationCount);
    if ((this.mPostReorderingPreZoomInRunnable != null) && (this.mPostReorderingPreZoomInRemainingAnimationCount == 0))
    {
      this.mPostReorderingPreZoomInRunnable.run();
      this.mPostReorderingPreZoomInRunnable = null;
    }
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = (0xFF00 & paramMotionEvent.getAction()) >> 8;
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i != 0) {
        break label87;
      }
    }
    label87:
    for (int j = 1;; j = 0)
    {
      float f = paramMotionEvent.getX(j);
      this.mDownMotionX = f;
      this.mLastMotionX = f;
      this.mLastMotionY = paramMotionEvent.getY(j);
      this.mLastMotionXRemainder = 0.0F;
      this.mActivePointerId = paramMotionEvent.getPointerId(j);
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.clear();
      }
      return;
    }
  }
  
  private float overScrollInfluenceCurve(float paramFloat)
  {
    float f = paramFloat - 1.0F;
    return 1.0F + f * (f * f);
  }
  
  private void releaseVelocityTracker()
  {
    if (this.mVelocityTracker != null)
    {
      this.mVelocityTracker.clear();
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
  }
  
  private void removeMarkerForView(int paramInt)
  {
    if ((this.mPageIndicator != null) && (!isReordering(false))) {
      this.mPageIndicator.removeMarker(paramInt, this.mAllowPagedViewAnimations);
    }
  }
  
  private void resetTouchState()
  {
    releaseVelocityTracker();
    endReordering();
    this.mCancelTap = false;
    this.mScrollAbortedFromIntercept = false;
    this.mTouchState = 0;
    this.mActivePointerId = -1;
  }
  
  private void sendScrollAccessibilityEvent()
  {
    AccessibilityEvent localAccessibilityEvent;
    if (((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled())
    {
      localAccessibilityEvent = AccessibilityEvent.obtain(4096);
      localAccessibilityEvent.setItemCount(getChildCount());
      localAccessibilityEvent.setFromIndex(this.mCurrentPage);
      localAccessibilityEvent.setToIndex(getNextPage());
      if (getNextPage() < this.mCurrentPage) {
        break label76;
      }
    }
    label76:
    for (int i = 4096;; i = 8192)
    {
      localAccessibilityEvent.setAction(i);
      sendAccessibilityEventUnchecked(localAccessibilityEvent);
      return;
    }
  }
  
  private void setEnableFreeScroll(boolean paramBoolean, int paramInt)
  {
    boolean bool = true;
    this.mFreeScroll = paramBoolean;
    if (paramInt == -1) {
      paramInt = getPageNearestToCenterOfScreen();
    }
    if (!this.mFreeScroll)
    {
      snapToPage(paramInt);
      if (paramBoolean) {
        break label103;
      }
    }
    for (;;)
    {
      setEnableOverscroll(bool);
      return;
      updateFreescrollBounds();
      getOverviewModePages(this.mTempVisiblePagesRange);
      if (getCurrentPage() < this.mTempVisiblePagesRange[0])
      {
        setCurrentPage(this.mTempVisiblePagesRange[0]);
        break;
      }
      if (getCurrentPage() <= this.mTempVisiblePagesRange[bool]) {
        break;
      }
      setCurrentPage(this.mTempVisiblePagesRange[bool]);
      break;
      label103:
      bool = false;
    }
  }
  
  private void setEnableOverscroll(boolean paramBoolean)
  {
    this.mAllowOverScroll = paramBoolean;
  }
  
  protected void acceleratedOverScroll(float paramFloat)
  {
    int i = getViewportWidth();
    float f = 2.0F * (paramFloat / i);
    if (f == 0.0F) {
      return;
    }
    if (Math.abs(f) >= 1.0F) {
      f /= Math.abs(f);
    }
    int j = Math.round(f * i);
    if (paramFloat < 0.0F)
    {
      this.mOverScrollX = j;
      super.scrollTo(0, getScrollY());
    }
    for (;;)
    {
      invalidate();
      return;
      this.mOverScrollX = (j + this.mMaxScrollX);
      super.scrollTo(this.mMaxScrollX, getScrollY());
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    if ((this.mCurrentPage >= 0) && (this.mCurrentPage < getPageCount())) {
      getPageAt(this.mCurrentPage).addFocusables(paramArrayList, paramInt1, paramInt2);
    }
    if (paramInt1 == 17) {
      if (this.mCurrentPage > 0) {
        getPageAt(-1 + this.mCurrentPage).addFocusables(paramArrayList, paramInt1, paramInt2);
      }
    }
    while ((paramInt1 != 66) || (this.mCurrentPage >= -1 + getPageCount())) {
      return;
    }
    getPageAt(1 + this.mCurrentPage).addFocusables(paramArrayList, paramInt1, paramInt2);
  }
  
  public void addFullScreenPage(View paramView)
  {
    LayoutParams localLayoutParams = generateDefaultLayoutParams();
    localLayoutParams.isFullScreenPage = true;
    super.addView(paramView, 0, localLayoutParams);
  }
  
  public boolean allowLongPress()
  {
    return this.mAllowLongPress;
  }
  
  void animateDragViewToOriginalPosition()
  {
    if (this.mDragView != null)
    {
      AnimatorSet localAnimatorSet = new AnimatorSet();
      localAnimatorSet.setDuration(this.REORDERING_DROP_REPOSITION_DURATION);
      Animator[] arrayOfAnimator = new Animator[4];
      arrayOfAnimator[0] = ObjectAnimator.ofFloat(this.mDragView, "translationX", new float[] { 0.0F });
      arrayOfAnimator[1] = ObjectAnimator.ofFloat(this.mDragView, "translationY", new float[] { 0.0F });
      arrayOfAnimator[2] = ObjectAnimator.ofFloat(this.mDragView, "scaleX", new float[] { 1.0F });
      arrayOfAnimator[3] = ObjectAnimator.ofFloat(this.mDragView, "scaleY", new float[] { 1.0F });
      localAnimatorSet.playTogether(arrayOfAnimator);
      localAnimatorSet.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          PagedView.this.onPostReorderingAnimationCompleted();
        }
      });
      localAnimatorSet.start();
    }
  }
  
  protected void cancelCurrentPageLongPress()
  {
    if (this.mAllowLongPress)
    {
      View localView = getPageAt(this.mCurrentPage);
      if (localView != null) {
        localView.cancelLongPress();
      }
    }
  }
  
  public void computeScroll()
  {
    computeScrollHelper();
  }
  
  protected boolean computeScrollHelper()
  {
    if (this.mScroller.computeScrollOffset())
    {
      if ((getScrollX() != this.mScroller.getCurrX()) || (getScrollY() != this.mScroller.getCurrY()) || (this.mOverScrollX != this.mScroller.getCurrX())) {
        if (!this.mFreeScroll) {
          break label94;
        }
      }
      label94:
      for (float f = getScaleX();; f = 1.0F)
      {
        scrollTo((int)(this.mScroller.getCurrX() * (1.0F / f)), this.mScroller.getCurrY());
        invalidate();
        return true;
      }
    }
    if (this.mNextPage != -1)
    {
      sendScrollAccessibilityEvent();
      this.mCurrentPage = Math.max(0, Math.min(this.mNextPage, -1 + getPageCount()));
      this.mNextPage = -1;
      notifyPageSwitchListener();
      if (this.mDeferLoadAssociatedPagesUntilScrollCompletes)
      {
        loadAssociatedPages(this.mCurrentPage);
        this.mDeferLoadAssociatedPagesUntilScrollCompletes = false;
      }
      if (this.mTouchState == 0) {
        pageEndMoving();
      }
      onPostReorderingAnimationCompleted();
      if (((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled()) {
        announceForAccessibility(getCurrentPageDescription());
      }
      return true;
    }
    return false;
  }
  
  protected void dampedOverScroll(float paramFloat)
  {
    int i = getViewportWidth();
    float f1 = paramFloat / i;
    if (f1 == 0.0F) {
      return;
    }
    float f2 = f1 / Math.abs(f1) * overScrollInfluenceCurve(Math.abs(f1));
    if (Math.abs(f2) >= 1.0F) {
      f2 /= Math.abs(f2);
    }
    int j = Math.round(0.14F * f2 * i);
    if (paramFloat < 0.0F)
    {
      this.mOverScrollX = j;
      super.scrollTo(0, getScrollY());
    }
    for (;;)
    {
      invalidate();
      return;
      this.mOverScrollX = (j + this.mMaxScrollX);
      super.scrollTo(this.mMaxScrollX, getScrollY());
    }
  }
  
  protected void determineScrollingStart(MotionEvent paramMotionEvent)
  {
    determineScrollingStart(paramMotionEvent, 1.0F);
  }
  
  protected void determineScrollingStart(MotionEvent paramMotionEvent, float paramFloat)
  {
    int i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
    if (i == -1) {}
    float f1;
    int n;
    label92:
    int i1;
    label102:
    do
    {
      int i2;
      do
      {
        float f2;
        do
        {
          return;
          f1 = paramMotionEvent.getX(i);
          f2 = paramMotionEvent.getY(i);
        } while (!isTouchPointInViewportWithBuffer((int)f1, (int)f2));
        int j = (int)Math.abs(f1 - this.mLastMotionX);
        int k = (int)Math.abs(f2 - this.mLastMotionY);
        int m = Math.round(paramFloat * this.mTouchSlop);
        if (j <= this.mPagingTouchSlop) {
          break;
        }
        n = 1;
        if (j <= m) {
          break label214;
        }
        i1 = 1;
        i2 = 0;
        if (k > m) {
          i2 = 1;
        }
      } while ((i1 == 0) && (n == 0) && (i2 == 0));
      if (!this.mUsePagingTouchSlop) {
        break label220;
      }
    } while (n == 0);
    for (;;)
    {
      this.mTouchState = 1;
      this.mTotalMotionX += Math.abs(this.mLastMotionX - f1);
      this.mLastMotionX = f1;
      this.mLastMotionXRemainder = 0.0F;
      this.mTouchX = (getViewportOffsetX() + getScrollX());
      this.mSmoothingTime = ((float)System.nanoTime() / 1.0E+009F);
      pageBeginMoving();
      return;
      n = 0;
      break label92;
      label214:
      i1 = 0;
      break label102;
      label220:
      if (i1 == 0) {
        break;
      }
    }
  }
  
  protected void disableFreeScroll(int paramInt)
  {
    setEnableFreeScroll(false, paramInt);
  }
  
  protected void disablePagedViewAnimations()
  {
    this.mAllowPagedViewAnimations = false;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    int i = getViewportWidth() / 2 + this.mOverScrollX;
    if ((i != this.mLastScreenCenter) || (this.mForceScreenScrolled))
    {
      this.mForceScreenScrolled = false;
      screenScrolled(i);
      this.mLastScreenCenter = i;
    }
    int j = getChildCount();
    if (j > 0)
    {
      getVisiblePages(this.mTempVisiblePagesRange);
      int k = this.mTempVisiblePagesRange[0];
      int m = this.mTempVisiblePagesRange[1];
      if ((k != -1) && (m != -1))
      {
        long l = getDrawingTime();
        paramCanvas.save();
        paramCanvas.clipRect(getScrollX(), getScrollY(), getScrollX() + getRight() - getLeft(), getScrollY() + getBottom() - getTop());
        int n = j - 1;
        if (n >= 0)
        {
          View localView = getPageAt(n);
          if (localView == this.mDragView) {}
          for (;;)
          {
            n--;
            break;
            if ((this.mForceDrawAllChildrenNextFrame) || ((k <= n) && (n <= m) && (shouldDrawChild(localView)))) {
              drawChild(paramCanvas, localView, l);
            }
          }
        }
        if (this.mDragView != null) {
          drawChild(paramCanvas, this.mDragView, l);
        }
        this.mForceDrawAllChildrenNextFrame = false;
        paramCanvas.restore();
      }
    }
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    if (paramInt == 17)
    {
      if (getCurrentPage() > 0)
      {
        snapToPage(-1 + getCurrentPage());
        return true;
      }
    }
    else if ((paramInt == 66) && (getCurrentPage() < -1 + getPageCount()))
    {
      snapToPage(1 + getCurrentPage());
      return true;
    }
    return super.dispatchUnhandledMove(paramView, paramInt);
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)(0.47123891676382D * (paramFloat - 0.5F)));
  }
  
  protected void enableFreeScroll()
  {
    setEnableFreeScroll(true, -1);
  }
  
  protected void enablePagedViewAnimations()
  {
    this.mAllowPagedViewAnimations = true;
  }
  
  void endReordering()
  {
    if (!this.mReorderingStarted) {}
    final Runnable local3;
    do
    {
      return;
      this.mReorderingStarted = false;
      local3 = new Runnable()
      {
        public void run()
        {
          PagedView.this.onEndReordering();
        }
      };
    } while (this.mDeferringForDelete);
    this.mPostReorderingPreZoomInRunnable = new Runnable()
    {
      public void run()
      {
        local3.run();
        PagedView.this.enableFreeScroll();
      }
    };
    this.mPostReorderingPreZoomInRemainingAnimationCount = this.NUM_ANIMATIONS_RUNNING_BEFORE_ZOOM_OUT;
    snapToPage(indexOfChild(this.mDragView), 0);
    animateDragViewToOriginalPosition();
  }
  
  public void focusableViewAvailable(View paramView)
  {
    View localView1 = getPageAt(this.mCurrentPage);
    for (View localView2 = paramView;; localView2 = (View)localView2.getParent())
    {
      if (localView2 == localView1) {
        super.focusableViewAvailable(paramView);
      }
      while ((localView2 == this) || (!(localView2.getParent() instanceof View))) {
        return;
      }
    }
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  protected int getAssociatedLowerPageBound(int paramInt)
  {
    return Math.max(0, paramInt - 1);
  }
  
  protected int getAssociatedUpperPageBound(int paramInt)
  {
    int i = getChildCount();
    return Math.min(paramInt + 1, i - 1);
  }
  
  protected int getChildOffset(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > -1 + getChildCount())) {
      return 0;
    }
    return getPageAt(paramInt).getLeft() - getViewportOffsetX();
  }
  
  protected int getChildWidth(int paramInt)
  {
    return getPageAt(paramInt).getMeasuredWidth();
  }
  
  int getCurrentPage()
  {
    return this.mCurrentPage;
  }
  
  protected String getCurrentPageDescription()
  {
    String str = getContext().getString(2131361931);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(1 + getNextPage());
    arrayOfObject[1] = Integer.valueOf(getChildCount());
    return String.format(str, arrayOfObject);
  }
  
  public int getLayoutTransitionOffsetForPage(int paramInt)
  {
    if ((this.mPageScrolls == null) || (paramInt >= this.mPageScrolls.length) || (paramInt < 0)) {
      return 0;
    }
    View localView = getChildAt(paramInt);
    boolean bool = ((LayoutParams)localView.getLayoutParams()).isFullScreenPage;
    int i = 0;
    if (!bool) {
      if (!isLayoutRtl()) {
        break label85;
      }
    }
    label85:
    for (i = getPaddingRight();; i = getPaddingLeft())
    {
      int j = i + this.mPageScrolls[paramInt] + getViewportOffsetX();
      return (int)(localView.getX() - j);
    }
  }
  
  protected float getMaxScrollProgress()
  {
    return 1.0F;
  }
  
  int getNearestHoverOverPageIndex()
  {
    if (this.mDragView != null)
    {
      int j = (int)(this.mDragView.getLeft() + this.mDragView.getMeasuredWidth() / 2 + this.mDragView.getTranslationX());
      getOverviewModePages(this.mTempVisiblePagesRange);
      int k = 2147483647;
      i = indexOfChild(this.mDragView);
      for (int m = this.mTempVisiblePagesRange[0]; m <= this.mTempVisiblePagesRange[1]; m++)
      {
        View localView = getPageAt(m);
        int n = Math.abs(j - (localView.getLeft() + localView.getMeasuredWidth() / 2));
        if (n < k)
        {
          i = m;
          k = n;
        }
      }
    }
    int i = -1;
    return i;
  }
  
  int getNextPage()
  {
    if (this.mNextPage != -1) {
      return this.mNextPage;
    }
    return this.mCurrentPage;
  }
  
  public int getNormalChildHeight()
  {
    return this.mNormalChildHeight;
  }
  
  protected void getOverviewModePages(int[] paramArrayOfInt)
  {
    paramArrayOfInt[0] = 0;
    paramArrayOfInt[1] = Math.max(0, -1 + getChildCount());
  }
  
  View getPageAt(int paramInt)
  {
    return getChildAt(paramInt);
  }
  
  int getPageCount()
  {
    return getChildCount();
  }
  
  public int getPageForView(View paramView)
  {
    if (paramView != null)
    {
      ViewParent localViewParent = paramView.getParent();
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        if (localViewParent == getPageAt(j)) {
          return j;
        }
      }
    }
    return -1;
  }
  
  PageIndicator getPageIndicator()
  {
    return this.mPageIndicator;
  }
  
  protected View.OnClickListener getPageIndicatorClickListener()
  {
    return null;
  }
  
  protected String getPageIndicatorDescription()
  {
    return getCurrentPageDescription();
  }
  
  protected PageIndicator.PageMarkerResources getPageIndicatorMarker(int paramInt)
  {
    return new PageIndicator.PageMarkerResources();
  }
  
  int getPageNearestToCenterOfScreen()
  {
    int i = 2147483647;
    int j = -1;
    int k = getViewportOffsetX() + getScrollX() + getViewportWidth() / 2;
    int m = getChildCount();
    for (int n = 0; n < m; n++)
    {
      int i1 = Math.abs(getPageAt(n).getMeasuredWidth() / 2 + (getViewportOffsetX() + getChildOffset(n)) - k);
      if (i1 < i)
      {
        i = i1;
        j = n;
      }
    }
    return j;
  }
  
  public int getScrollForPage(int paramInt)
  {
    if ((this.mPageScrolls == null) || (paramInt >= this.mPageScrolls.length) || (paramInt < 0)) {
      return 0;
    }
    return this.mPageScrolls[paramInt];
  }
  
  protected float getScrollProgress(int paramInt1, View paramView, int paramInt2)
  {
    int i = paramInt1 - (getViewportWidth() / 2 + getScrollForPage(paramInt2));
    int j = getChildCount();
    int k = paramInt2 + 1;
    if (((i < 0) && (!isLayoutRtl())) || ((i > 0) && (isLayoutRtl()))) {
      k = paramInt2 - 1;
    }
    if ((k < 0) || (k > j - 1)) {}
    for (int m = paramView.getMeasuredWidth() + this.mPageSpacing;; m = Math.abs(getScrollForPage(k) - getScrollForPage(paramInt2))) {
      return Math.max(Math.min(i / (1.0F * m), getMaxScrollProgress()), -getMaxScrollProgress());
    }
  }
  
  int getViewportHeight()
  {
    return this.mViewport.height();
  }
  
  int getViewportOffsetX()
  {
    return (getMeasuredWidth() - getViewportWidth()) / 2;
  }
  
  int getViewportOffsetY()
  {
    return (getMeasuredHeight() - getViewportHeight()) / 2;
  }
  
  int getViewportWidth()
  {
    return this.mViewport.width();
  }
  
  protected void getVisiblePages(int[] paramArrayOfInt)
  {
    int i = getChildCount();
    int[] arrayOfInt = this.mTmpIntPoint;
    this.mTmpIntPoint[1] = 0;
    arrayOfInt[0] = 0;
    paramArrayOfInt[0] = -1;
    paramArrayOfInt[1] = -1;
    if (i > 0)
    {
      int j = getViewportWidth();
      int k = 0;
      int m = getChildCount();
      int n = 0;
      View localView;
      if (n < m)
      {
        localView = getPageAt(n);
        this.mTmpIntPoint[0] = 0;
        Utilities.getDescendantCoordRelativeToParent(localView, this, this.mTmpIntPoint, false);
        if (this.mTmpIntPoint[0] > j) {
          if (paramArrayOfInt[0] != -1) {
            break label148;
          }
        }
      }
      for (;;)
      {
        n++;
        break;
        this.mTmpIntPoint[0] = localView.getMeasuredWidth();
        Utilities.getDescendantCoordRelativeToParent(localView, this, this.mTmpIntPoint, false);
        if (this.mTmpIntPoint[0] < 0)
        {
          if (paramArrayOfInt[0] != -1) {
            label148:
            paramArrayOfInt[1] = k;
          }
        }
        else
        {
          k = n;
          if (paramArrayOfInt[0] < 0) {
            paramArrayOfInt[0] = k;
          }
        }
      }
    }
    paramArrayOfInt[0] = -1;
    paramArrayOfInt[1] = -1;
  }
  
  protected int indexToPage(int paramInt)
  {
    return paramInt;
  }
  
  protected void init()
  {
    this.mDirtyPageContent = new ArrayList();
    this.mDirtyPageContent.ensureCapacity(32);
    this.mScroller = new Scroller(getContext(), new ScrollInterpolator());
    this.mCurrentPage = 0;
    this.mCenterPagesVertically = true;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
    this.mTouchSlop = localViewConfiguration.getScaledPagingTouchSlop();
    this.mPagingTouchSlop = localViewConfiguration.getScaledPagingTouchSlop();
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mDensity = getResources().getDisplayMetrics().density;
    this.mFlingToDeleteThresholdVelocity = ((int)(this.mFlingToDeleteThresholdVelocity * this.mDensity));
    this.mFlingThresholdVelocity = ((int)(500.0F * this.mDensity));
    this.mMinFlingVelocity = ((int)(250.0F * this.mDensity));
    this.mMinSnapVelocity = ((int)(1500.0F * this.mDensity));
    setOnHierarchyChangeListener(this);
  }
  
  protected void invalidatePageData()
  {
    invalidatePageData(-1, false);
  }
  
  protected void invalidatePageData(int paramInt)
  {
    invalidatePageData(paramInt, false);
  }
  
  protected void invalidatePageData(int paramInt, boolean paramBoolean)
  {
    if (!this.mIsDataReady) {}
    do
    {
      return;
      if (this.mContentIsRefreshable)
      {
        forceFinishScroller();
        syncPages();
        measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        if (paramInt > -1) {
          setCurrentPage(Math.min(-1 + getPageCount(), paramInt));
        }
        int i = getChildCount();
        this.mDirtyPageContent.clear();
        for (int j = 0; j < i; j++) {
          this.mDirtyPageContent.add(Boolean.valueOf(true));
        }
        loadAssociatedPages(this.mCurrentPage, paramBoolean);
        requestLayout();
      }
    } while (!isPageMoving());
    snapToDestination();
  }
  
  protected boolean isDataReady()
  {
    return this.mIsDataReady;
  }
  
  public boolean isLayoutRtl()
  {
    return getLayoutDirection() == 1;
  }
  
  protected boolean isPageMoving()
  {
    return this.mIsPageMoving;
  }
  
  boolean isReordering(boolean paramBoolean)
  {
    boolean bool1 = this.mIsReordering;
    if (paramBoolean) {
      if (this.mTouchState != 4) {
        break label25;
      }
    }
    label25:
    for (boolean bool2 = true;; bool2 = false)
    {
      bool1 &= bool2;
      return bool1;
    }
  }
  
  protected void loadAssociatedPages(int paramInt)
  {
    loadAssociatedPages(paramInt, false);
  }
  
  protected void loadAssociatedPages(int paramInt, boolean paramBoolean)
  {
    if (this.mContentIsRefreshable)
    {
      int i = getChildCount();
      if (paramInt < i)
      {
        int j = getAssociatedLowerPageBound(paramInt);
        int k = getAssociatedUpperPageBound(paramInt);
        for (int m = 0; m < i; m++)
        {
          Page localPage = (Page)getPageAt(m);
          if ((m < j) || (m > k))
          {
            if (localPage.getPageChildCount() > 0) {
              localPage.removeAllViewsOnPage();
            }
            this.mDirtyPageContent.set(m, Boolean.valueOf(true));
          }
        }
        int n = 0;
        if (n < i)
        {
          if ((n != paramInt) && (paramBoolean)) {}
          while ((j > n) || (n > k) || (!((Boolean)this.mDirtyPageContent.get(n)).booleanValue()))
          {
            n++;
            break;
          }
          if ((n == paramInt) && (paramBoolean)) {}
          for (boolean bool = true;; bool = false)
          {
            syncPageItems(n, bool);
            this.mDirtyPageContent.set(n, Boolean.valueOf(false));
            break;
          }
        }
      }
    }
  }
  
  float[] mapPointFromParentToView(View paramView, float paramFloat1, float paramFloat2)
  {
    this.mTmpPoint[0] = (paramFloat1 - paramView.getLeft());
    this.mTmpPoint[1] = (paramFloat2 - paramView.getTop());
    paramView.getMatrix().invert(this.mTmpInvMatrix);
    this.mTmpInvMatrix.mapPoints(this.mTmpPoint);
    return this.mTmpPoint;
  }
  
  float[] mapPointFromViewToParent(View paramView, float paramFloat1, float paramFloat2)
  {
    this.mTmpPoint[0] = paramFloat1;
    this.mTmpPoint[1] = paramFloat2;
    paramView.getMatrix().mapPoints(this.mTmpPoint);
    float[] arrayOfFloat1 = this.mTmpPoint;
    arrayOfFloat1[0] += paramView.getLeft();
    float[] arrayOfFloat2 = this.mTmpPoint;
    arrayOfFloat2[1] += paramView.getTop();
    return this.mTmpPoint;
  }
  
  protected void notifyPageSwitchListener()
  {
    if (this.mPageSwitchListener != null) {
      this.mPageSwitchListener.onPageSwitch(getPageAt(this.mCurrentPage), this.mCurrentPage);
    }
    if ((this.mPageIndicator != null) && (!isReordering(false))) {
      this.mPageIndicator.setActiveMarker(getNextPage());
    }
  }
  
  public void onAddView(View paramView, int paramInt) {}
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewGroup localViewGroup = (ViewGroup)getParent();
    if ((this.mPageIndicator == null) && (this.mPageIndicatorViewId > -1))
    {
      this.mPageIndicator = ((PageIndicator)localViewGroup.findViewById(this.mPageIndicatorViewId));
      this.mPageIndicator.removeAllMarkers(this.mAllowPagedViewAnimations);
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < getChildCount(); i++) {
        localArrayList.add(getPageIndicatorMarker(i));
      }
      this.mPageIndicator.addMarkers(localArrayList, this.mAllowPagedViewAnimations);
      View.OnClickListener localOnClickListener = getPageIndicatorClickListener();
      if (localOnClickListener != null) {
        this.mPageIndicator.setOnClickListener(localOnClickListener);
      }
      this.mPageIndicator.setContentDescription(getPageIndicatorDescription());
    }
  }
  
  public void onChildViewAdded(View paramView1, View paramView2)
  {
    if ((this.mPageIndicator != null) && (!isReordering(false)))
    {
      int i = indexOfChild(paramView2);
      this.mPageIndicator.addMarker(i, getPageIndicatorMarker(i), this.mAllowPagedViewAnimations);
    }
    this.mForceScreenScrolled = true;
    updateFreescrollBounds();
    invalidate();
  }
  
  public void onChildViewRemoved(View paramView1, View paramView2)
  {
    this.mForceScreenScrolled = true;
    updateFreescrollBounds();
    invalidate();
  }
  
  protected void onDetachedFromWindow()
  {
    this.mPageIndicator = null;
  }
  
  protected void onEndReordering()
  {
    this.mIsReordering = false;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if ((0x2 & paramMotionEvent.getSource()) != 0) {}
    switch (paramMotionEvent.getAction())
    {
    default: 
      return super.onGenericMotionEvent(paramMotionEvent);
    }
    float f1;
    float f2;
    label56:
    int i;
    if ((0x1 & paramMotionEvent.getMetaState()) != 0)
    {
      f1 = 0.0F;
      f2 = paramMotionEvent.getAxisValue(9);
      if ((f2 == 0.0F) && (f1 == 0.0F)) {
        break label124;
      }
      if (!isLayoutRtl()) {
        break label126;
      }
      if (f2 >= 0.0F)
      {
        boolean bool2 = f1 < 0.0F;
        i = 0;
        if (!bool2) {}
      }
      else
      {
        i = 1;
      }
    }
    for (;;)
    {
      if (i == 0) {
        break label151;
      }
      scrollRight();
      return true;
      f1 = -paramMotionEvent.getAxisValue(9);
      f2 = paramMotionEvent.getAxisValue(10);
      break label56;
      label124:
      break;
      label126:
      if (f2 <= 0.0F)
      {
        boolean bool1 = f1 < 0.0F;
        i = 0;
        if (!bool1) {}
      }
      else
      {
        i = 1;
      }
    }
    label151:
    scrollLeft();
    return true;
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setScrollable(true);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    int i = 1;
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    if (getPageCount() > i) {}
    for (;;)
    {
      paramAccessibilityNodeInfo.setScrollable(i);
      if (getCurrentPage() < -1 + getPageCount()) {
        paramAccessibilityNodeInfo.addAction(4096);
      }
      if (getCurrentPage() > 0) {
        paramAccessibilityNodeInfo.addAction(8192);
      }
      return;
      int j = 0;
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 1;
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    if (getChildCount() <= 0) {
      i = super.onInterceptTouchEvent(paramMotionEvent);
    }
    for (;;)
    {
      return i;
      int j = paramMotionEvent.getAction();
      if ((j != 2) || (this.mTouchState != i))
      {
        switch (j & 0xFF)
        {
        }
        while (this.mTouchState == 0)
        {
          return false;
          if (this.mActivePointerId != -1)
          {
            determineScrollingStart(paramMotionEvent);
            continue;
            float f1 = paramMotionEvent.getX();
            float f2 = paramMotionEvent.getY();
            this.mDownMotionX = f1;
            this.mDownMotionY = f2;
            this.mDownScrollX = getScrollX();
            this.mLastMotionX = f1;
            this.mLastMotionY = f2;
            float[] arrayOfFloat = mapPointFromViewToParent(this, f1, f2);
            this.mParentDownMotionX = arrayOfFloat[0];
            this.mParentDownMotionY = arrayOfFloat[i];
            this.mLastMotionXRemainder = 0.0F;
            this.mTotalMotionX = 0.0F;
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            int k = Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX());
            if ((this.mScroller.isFinished()) || (k < this.mTouchSlop)) {}
            int n;
            for (int m = i;; n = 0)
            {
              if (m == 0) {
                break label285;
              }
              this.mTouchState = 0;
              if (this.mScroller.isFinished()) {
                break;
              }
              this.mScrollAbortedFromIntercept = i;
              abortScrollerAnimation(false);
              break;
            }
            label285:
            if (isTouchPointInViewportWithBuffer((int)this.mDownMotionX, (int)this.mDownMotionY))
            {
              this.mTouchState = i;
            }
            else
            {
              this.mTouchState = 0;
              continue;
              if (this.mScrollAbortedFromIntercept) {
                snapToDestination();
              }
              resetTouchState();
              continue;
              onSecondaryPointerUp(paramMotionEvent);
              releaseVelocityTracker();
            }
          }
        }
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((!this.mIsDataReady) || (getChildCount() == 0)) {
      return;
    }
    int i = getChildCount();
    int j = getViewportOffsetX();
    int k = getViewportOffsetY();
    this.mViewport.offset(j, k);
    boolean bool = isLayoutRtl();
    int m;
    int n;
    label69:
    int i1;
    label77:
    int i2;
    int i3;
    label109:
    int i4;
    int i5;
    label148:
    View localView;
    int i7;
    label195:
    int i8;
    if (bool)
    {
      m = i - 1;
      if (!bool) {
        break label285;
      }
      n = -1;
      if (!bool) {
        break label292;
      }
      i1 = -1;
      i2 = getPaddingTop() + getPaddingBottom();
      if (!((LayoutParams)getChildAt(m).getLayoutParams()).isFullScreenPage) {
        break label298;
      }
      i3 = 0;
      i4 = j + i3;
      if ((this.mPageScrolls == null) || (getChildCount() != this.mChildCountOnLastLayout)) {
        this.mPageScrolls = new int[getChildCount()];
      }
      i5 = m;
      if (i5 == n) {
        break label379;
      }
      localView = getPageAt(i5);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (!localLayoutParams.isFullScreenPage) {
          break label307;
        }
        i7 = k;
        i8 = localView.getMeasuredWidth();
        int i9 = localView.getMeasuredHeight();
        localView.layout(i4, i7, i4 + localView.getMeasuredWidth(), i7 + i9);
        if (!localLayoutParams.isFullScreenPage) {
          break label370;
        }
      }
    }
    label285:
    label292:
    label298:
    label307:
    label370:
    for (int i10 = 0;; i10 = getPaddingLeft())
    {
      this.mPageScrolls[i5] = (i4 - i10 - j);
      i4 += i8 + this.mPageSpacing;
      i5 += i1;
      break label148;
      m = 0;
      break;
      n = i;
      break label69;
      i1 = 1;
      break label77;
      i3 = getPaddingLeft();
      break label109;
      i7 = k + getPaddingTop() + this.mInsets.top;
      if (!this.mCenterPagesVertically) {
        break label195;
      }
      i7 += (getViewportHeight() - this.mInsets.top - this.mInsets.bottom - i2 - localView.getMeasuredHeight()) / 2;
      break label195;
    }
    label379:
    if ((this.mFirstLayout) && (this.mCurrentPage >= 0) && (this.mCurrentPage < getChildCount()))
    {
      setHorizontalScrollBarEnabled(false);
      updateCurrentPageScroll();
      setHorizontalScrollBarEnabled(true);
      this.mFirstLayout = false;
    }
    int i6;
    if (i > 0) {
      if (isLayoutRtl())
      {
        i6 = 0;
        label438:
        this.mMaxScrollX = getScrollForPage(i6);
        label448:
        if ((this.mScroller.isFinished()) && (this.mChildCountOnLastLayout != getChildCount()) && (!this.mDeferringForDelete))
        {
          if (this.mRestorePage == -1001) {
            break label539;
          }
          setCurrentPage(this.mRestorePage);
          this.mRestorePage = -1001;
        }
      }
    }
    for (;;)
    {
      this.mChildCountOnLastLayout = getChildCount();
      if (!isReordering(true)) {
        break;
      }
      updateDragViewTranslationDuringDrag();
      return;
      i6 = i - 1;
      break label438;
      this.mMaxScrollX = 0;
      break label448;
      label539:
      setCurrentPage(getNextPage());
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if ((!this.mIsDataReady) || (getChildCount() == 0))
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    int n = Math.max(localDisplayMetrics.widthPixels + this.mInsets.left + this.mInsets.right, localDisplayMetrics.heightPixels + this.mInsets.top + this.mInsets.bottom);
    int i1 = (int)(1.5F * n);
    int i2;
    if (this.mUseMinScale) {
      i2 = (int)(i1 / this.mMinScale);
    }
    for (int i3 = (int)(n / this.mMinScale);; i3 = m)
    {
      this.mViewport.set(0, 0, j, m);
      if ((i != 0) && (k != 0)) {
        break;
      }
      super.onMeasure(paramInt1, paramInt2);
      return;
      i2 = j;
    }
    if ((j <= 0) || (m <= 0))
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int i4 = getPaddingTop() + getPaddingBottom();
    int i5 = getPaddingLeft() + getPaddingRight();
    int i6 = getChildCount();
    int i7 = 0;
    if (i7 < i6)
    {
      View localView = getPageAt(i7);
      int i8;
      label285:
      int i9;
      label300:
      int i10;
      int i11;
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (localLayoutParams.isFullScreenPage) {
          break label397;
        }
        if (localLayoutParams.width != -2) {
          break label381;
        }
        i8 = -2147483648;
        if (localLayoutParams.height != -2) {
          break label389;
        }
        i9 = -2147483648;
        i10 = getViewportWidth() - i5 - this.mInsets.left - this.mInsets.right;
        i11 = getViewportHeight() - i4 - this.mInsets.top - this.mInsets.bottom;
        this.mNormalChildHeight = i11;
      }
      for (;;)
      {
        localView.measure(View.MeasureSpec.makeMeasureSpec(i10, i8), View.MeasureSpec.makeMeasureSpec(i11, i9));
        i7++;
        break;
        label381:
        i8 = 1073741824;
        break label285;
        label389:
        i9 = 1073741824;
        break label300;
        label397:
        i8 = 1073741824;
        i9 = 1073741824;
        i10 = getViewportWidth() - this.mInsets.left - this.mInsets.right;
        i11 = getViewportHeight();
      }
    }
    setMeasuredDimension(i2, i3);
  }
  
  protected void onPageBeginMoving() {}
  
  protected void onPageEndMoving() {}
  
  public void onRemoveView(View paramView, boolean paramBoolean) {}
  
  public void onRemoveViewAnimationCompleted() {}
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    if (this.mNextPage != -1) {}
    for (int i = this.mNextPage;; i = this.mCurrentPage)
    {
      View localView = getPageAt(i);
      if (localView == null) {
        break;
      }
      return localView.requestFocus(paramInt, paramRect);
    }
    return false;
  }
  
  protected void onStartReordering()
  {
    this.mTouchState = 4;
    this.mIsReordering = true;
    invalidate();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    if (getChildCount() <= 0) {
      return super.onTouchEvent(paramMotionEvent);
    }
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    switch (0xFF & paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return true;
      if (!this.mScroller.isFinished()) {
        abortScrollerAnimation(false);
      }
      float f5 = paramMotionEvent.getX();
      this.mLastMotionX = f5;
      this.mDownMotionX = f5;
      float f6 = paramMotionEvent.getY();
      this.mLastMotionY = f6;
      this.mDownMotionY = f6;
      this.mDownScrollX = getScrollX();
      float[] arrayOfFloat3 = mapPointFromViewToParent(this, this.mLastMotionX, this.mLastMotionY);
      this.mParentDownMotionX = arrayOfFloat3[0];
      this.mParentDownMotionY = arrayOfFloat3[1];
      this.mLastMotionXRemainder = 0.0F;
      this.mTotalMotionX = 0.0F;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      if (this.mTouchState == 1)
      {
        pageBeginMoving();
        continue;
        if (this.mTouchState == 1)
        {
          int i13 = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          if (i13 == -1) {
            return true;
          }
          float f3 = paramMotionEvent.getX(i13);
          float f4 = this.mLastMotionX + this.mLastMotionXRemainder - f3;
          this.mTotalMotionX += Math.abs(f4);
          if (Math.abs(f4) >= 1.0F)
          {
            this.mTouchX = (f4 + this.mTouchX);
            this.mSmoothingTime = ((float)System.nanoTime() / 1.0E+009F);
            if (!this.mDeferScrollUpdate) {
              scrollBy((int)f4, 0);
            }
            for (;;)
            {
              this.mLastMotionX = f3;
              this.mLastMotionXRemainder = (f4 - (int)f4);
              break;
              invalidate();
            }
          }
          awakenScrollBars();
        }
        else if (this.mTouchState == 4)
        {
          this.mLastMotionX = paramMotionEvent.getX();
          this.mLastMotionY = paramMotionEvent.getY();
          float[] arrayOfFloat2 = mapPointFromViewToParent(this, this.mLastMotionX, this.mLastMotionY);
          this.mParentDownMotionX = arrayOfFloat2[0];
          this.mParentDownMotionY = arrayOfFloat2[1];
          updateDragViewTranslationDuringDrag();
          final int i11 = indexOfChild(this.mDragView);
          boolean bool4 = isHoveringOverDeleteDropTarget((int)this.mParentDownMotionX, (int)this.mParentDownMotionY);
          setPageHoveringOverDeleteDropTarget(i11, bool4);
          final int i12 = getNearestHoverOverPageIndex();
          if ((i12 > -1) && (i12 != indexOfChild(this.mDragView)) && (!bool4))
          {
            this.mTempVisiblePagesRange[0] = 0;
            this.mTempVisiblePagesRange[1] = (-1 + getPageCount());
            getOverviewModePages(this.mTempVisiblePagesRange);
            if ((this.mTempVisiblePagesRange[0] <= i12) && (i12 <= this.mTempVisiblePagesRange[1]) && (i12 != this.mSidePageHoverIndex) && (this.mScroller.isFinished()))
            {
              this.mSidePageHoverIndex = i12;
              this.mSidePageHoverRunnable = new Runnable()
              {
                public void run()
                {
                  PagedView.this.snapToPage(i12);
                  int i;
                  int j;
                  if (i11 < i12)
                  {
                    i = -1;
                    if (i11 >= i12) {
                      break label230;
                    }
                    j = 1 + i11;
                    label42:
                    if (i11 <= i12) {
                      break label238;
                    }
                  }
                  label230:
                  label238:
                  for (int k = -1 + i11;; k = i12)
                  {
                    for (int m = j; m <= k; m++)
                    {
                      View localView = PagedView.this.getChildAt(m);
                      int n = PagedView.this.getViewportOffsetX() + PagedView.this.getChildOffset(m);
                      int i1 = PagedView.this.getViewportOffsetX() + PagedView.this.getChildOffset(m + i);
                      AnimatorSet localAnimatorSet1 = (AnimatorSet)localView.getTag(100);
                      if (localAnimatorSet1 != null) {
                        localAnimatorSet1.cancel();
                      }
                      localView.setTranslationX(n - i1);
                      AnimatorSet localAnimatorSet2 = new AnimatorSet();
                      localAnimatorSet2.setDuration(PagedView.this.REORDERING_REORDER_REPOSITION_DURATION);
                      Animator[] arrayOfAnimator = new Animator[1];
                      arrayOfAnimator[0] = ObjectAnimator.ofFloat(localView, "translationX", new float[] { 0.0F });
                      localAnimatorSet2.playTogether(arrayOfAnimator);
                      localAnimatorSet2.start();
                      localView.setTag(localAnimatorSet2);
                    }
                    i = 1;
                    break;
                    j = i12;
                    break label42;
                  }
                  PagedView.this.removeView(PagedView.this.mDragView);
                  PagedView.this.onRemoveView(PagedView.this.mDragView, false);
                  PagedView.this.addView(PagedView.this.mDragView, i12);
                  PagedView.this.onAddView(PagedView.this.mDragView, i12);
                  PagedView.access$002(PagedView.this, -1);
                  PagedView.this.mPageIndicator.setActiveMarker(PagedView.this.getNextPage());
                }
              };
              postDelayed(this.mSidePageHoverRunnable, this.REORDERING_SIDE_PAGE_HOVER_TIMEOUT);
            }
          }
          else
          {
            removeCallbacks(this.mSidePageHoverRunnable);
            this.mSidePageHoverIndex = -1;
          }
        }
        else
        {
          determineScrollingStart(paramMotionEvent);
          continue;
          int m;
          int n;
          int i2;
          label701:
          int i3;
          label751:
          int i6;
          int i7;
          label835:
          int i8;
          label848:
          int i10;
          if (this.mTouchState == 1)
          {
            int k = this.mActivePointerId;
            float f1 = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(k));
            VelocityTracker localVelocityTracker = this.mVelocityTracker;
            localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
            m = (int)localVelocityTracker.getXVelocity(k);
            n = (int)(f1 - this.mDownMotionX);
            int i1 = getPageAt(this.mCurrentPage).getMeasuredWidth();
            if (Math.abs(n) > 0.4F * i1)
            {
              i2 = 1;
              this.mTotalMotionX += Math.abs(this.mLastMotionX + this.mLastMotionXRemainder - f1);
              if ((this.mTotalMotionX <= 25.0F) || (Math.abs(m) <= this.mFlingThresholdVelocity)) {
                break label921;
              }
              i3 = 1;
              if (this.mFreeScroll) {
                break label1062;
              }
              boolean bool1 = Math.abs(n) < 0.33F * i1;
              i6 = 0;
              if (bool1)
              {
                boolean bool3 = Math.signum(m) < Math.signum(n);
                i6 = 0;
                if (bool3)
                {
                  i6 = 0;
                  if (i3 != 0) {
                    i6 = 1;
                  }
                }
              }
              boolean bool2 = isLayoutRtl();
              if (!bool2) {
                break label933;
              }
              if (n <= 0) {
                break label927;
              }
              i7 = 1;
              if (!bool2) {
                break label956;
              }
              if (m <= 0) {
                break label950;
              }
              i8 = 1;
              if (((i2 == 0) || (i7 != 0) || (i3 != 0)) && ((i3 == 0) || (i8 != 0) || (this.mCurrentPage <= 0))) {
                break label984;
              }
              if (i6 == 0) {
                break label973;
              }
              i10 = this.mCurrentPage;
              label891:
              snapToPageWithVelocity(i10, m);
            }
          }
          for (;;)
          {
            removeCallbacks(this.mSidePageHoverRunnable);
            resetTouchState();
            break;
            i2 = 0;
            break label701;
            label921:
            i3 = 0;
            break label751;
            label927:
            i7 = 0;
            break label835;
            label933:
            if (n < 0)
            {
              i7 = 1;
              break label835;
            }
            i7 = 0;
            break label835;
            label950:
            i8 = 0;
            break label848;
            label956:
            if (m < 0)
            {
              i8 = 1;
              break label848;
            }
            i8 = 0;
            break label848;
            label973:
            i10 = -1 + this.mCurrentPage;
            break label891;
            label984:
            if (((i2 != 0) && (i7 != 0) && (i3 == 0)) || ((i3 != 0) && (i8 != 0) && (this.mCurrentPage < -1 + getChildCount())))
            {
              if (i6 != 0) {}
              for (int i9 = this.mCurrentPage;; i9 = 1 + this.mCurrentPage)
              {
                snapToPageWithVelocity(i9, m);
                break;
              }
            }
            snapToDestination();
            continue;
            label1062:
            if (!this.mScroller.isFinished()) {
              abortScrollerAnimation(true);
            }
            float f2 = getScaleX();
            int i4 = (int)(f2 * -m);
            int i5 = (int)(f2 * getScrollX());
            this.mScroller.fling(i5, getScrollY(), i4, 0, -2147483648, 2147483647, 0, 0);
            invalidate();
            continue;
            if (this.mTouchState == 2)
            {
              int j = Math.max(0, -1 + this.mCurrentPage);
              if (j != this.mCurrentPage) {
                snapToPage(j);
              } else {
                snapToDestination();
              }
            }
            else if (this.mTouchState == 3)
            {
              int i = Math.min(-1 + getChildCount(), 1 + this.mCurrentPage);
              if (i != this.mCurrentPage) {
                snapToPage(i);
              } else {
                snapToDestination();
              }
            }
            else if (this.mTouchState == 4)
            {
              this.mLastMotionX = paramMotionEvent.getX();
              this.mLastMotionY = paramMotionEvent.getY();
              float[] arrayOfFloat1 = mapPointFromViewToParent(this, this.mLastMotionX, this.mLastMotionY);
              this.mParentDownMotionX = arrayOfFloat1[0];
              this.mParentDownMotionY = arrayOfFloat1[1];
              updateDragViewTranslationDuringDrag();
              if ((0 == 0) && (isHoveringOverDeleteDropTarget((int)this.mParentDownMotionX, (int)this.mParentDownMotionY))) {
                onDropToDelete();
              }
            }
            else if (!this.mCancelTap)
            {
              onUnhandledTap(paramMotionEvent);
            }
          }
          if (this.mTouchState == 1) {
            snapToDestination();
          }
          resetTouchState();
          continue;
          onSecondaryPointerUp(paramMotionEvent);
          releaseVelocityTracker();
        }
      }
    }
  }
  
  protected void onUnhandledTap(MotionEvent paramMotionEvent)
  {
    ((Launcher)getContext()).onClick(this);
  }
  
  protected void overScroll(float paramFloat)
  {
    dampedOverScroll(paramFloat);
  }
  
  protected void pageBeginMoving()
  {
    if (!this.mIsPageMoving)
    {
      this.mIsPageMoving = true;
      onPageBeginMoving();
    }
  }
  
  protected void pageEndMoving()
  {
    if (this.mIsPageMoving)
    {
      this.mIsPageMoving = false;
      onPageEndMoving();
    }
  }
  
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityAction(paramInt, paramBundle)) {
      return true;
    }
    switch (paramInt)
    {
    }
    do
    {
      do
      {
        return false;
      } while (getCurrentPage() >= -1 + getPageCount());
      scrollRight();
      return true;
    } while (getCurrentPage() <= 0);
    scrollLeft();
    return true;
  }
  
  public boolean performLongClick()
  {
    this.mCancelTap = true;
    return super.performLongClick();
  }
  
  public void removeAllViewsInLayout()
  {
    if (this.mPageIndicator != null) {
      this.mPageIndicator.removeAllMarkers(this.mAllowPagedViewAnimations);
    }
    super.removeAllViewsInLayout();
  }
  
  public void removeView(View paramView)
  {
    removeMarkerForView(indexOfChild(paramView));
    super.removeView(paramView);
  }
  
  public void removeViewAt(int paramInt)
  {
    removeViewAt(paramInt);
    super.removeViewAt(paramInt);
  }
  
  public void removeViewInLayout(View paramView)
  {
    removeMarkerForView(indexOfChild(paramView));
    super.removeViewInLayout(paramView);
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    int i = indexToPage(indexOfChild(paramView1));
    if ((i >= 0) && (i != getCurrentPage()) && (!isInTouchMode())) {
      snapToPage(i);
    }
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    int i = indexToPage(indexOfChild(paramView));
    if ((i != this.mCurrentPage) || (!this.mScroller.isFinished()))
    {
      snapToPage(i);
      return true;
    }
    return false;
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    if (paramBoolean) {
      getPageAt(this.mCurrentPage).cancelLongPress();
    }
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  protected void screenScrolled(int paramInt)
  {
    int i;
    if ((this.mOverScrollX < 0) || (this.mOverScrollX > this.mMaxScrollX)) {
      i = 1;
    }
    while ((this.mFadeInAdjacentScreens) && (i == 0))
    {
      int j = 0;
      for (;;)
      {
        if (j < getChildCount())
        {
          View localView = getChildAt(j);
          if (localView != null) {
            localView.setAlpha(1.0F - Math.abs(getScrollProgress(paramInt, localView, j)));
          }
          j++;
          continue;
          i = 0;
          break;
        }
      }
      invalidate();
    }
  }
  
  public void scrollBy(int paramInt1, int paramInt2)
  {
    scrollTo(paramInt1 + this.mUnboundedScrollX, paramInt2 + getScrollY());
  }
  
  public void scrollLeft()
  {
    if (getNextPage() > 0) {
      snapToPage(-1 + getNextPage());
    }
  }
  
  public void scrollRight()
  {
    if (getNextPage() < -1 + getChildCount()) {
      snapToPage(1 + getNextPage());
    }
  }
  
  public void scrollTo(int paramInt1, int paramInt2)
  {
    if (this.mFreeScroll) {
      paramInt1 = Math.max(Math.min(paramInt1, this.mFreeScrollMaxScrollX), this.mFreeScrollMinScrollX);
    }
    boolean bool = isLayoutRtl();
    this.mUnboundedScrollX = paramInt1;
    int i;
    int j;
    if (bool) {
      if (paramInt1 > this.mMaxScrollX)
      {
        i = 1;
        if (!bool) {
          break label182;
        }
        if (paramInt1 >= 0) {
          break label176;
        }
        j = 1;
        label59:
        if (i == 0) {
          break label211;
        }
        super.scrollTo(0, paramInt2);
        if (this.mAllowOverScroll)
        {
          if (!bool) {
            break label202;
          }
          overScroll(paramInt1 - this.mMaxScrollX);
        }
      }
    }
    for (;;)
    {
      this.mTouchX = paramInt1;
      this.mSmoothingTime = ((float)System.nanoTime() / 1.0E+009F);
      if (isReordering(true))
      {
        float[] arrayOfFloat = mapPointFromParentToView(this, this.mParentDownMotionX, this.mParentDownMotionY);
        this.mLastMotionX = arrayOfFloat[0];
        this.mLastMotionY = arrayOfFloat[1];
        updateDragViewTranslationDuringDrag();
      }
      return;
      i = 0;
      break;
      if (paramInt1 < 0)
      {
        i = 1;
        break;
      }
      i = 0;
      break;
      label176:
      j = 0;
      break label59;
      label182:
      if (paramInt1 > this.mMaxScrollX)
      {
        j = 1;
        break label59;
      }
      j = 0;
      break label59;
      label202:
      overScroll(paramInt1);
      continue;
      label211:
      if (j != 0)
      {
        super.scrollTo(this.mMaxScrollX, paramInt2);
        if (this.mAllowOverScroll) {
          if (bool) {
            overScroll(paramInt1);
          } else {
            overScroll(paramInt1 - this.mMaxScrollX);
          }
        }
      }
      else
      {
        this.mOverScrollX = paramInt1;
        super.scrollTo(paramInt1, paramInt2);
      }
    }
  }
  
  public void sendAccessibilityEvent(int paramInt)
  {
    if (paramInt != 4096) {
      super.sendAccessibilityEvent(paramInt);
    }
  }
  
  void setCurrentPage(int paramInt)
  {
    if (!this.mScroller.isFinished()) {
      abortScrollerAnimation(true);
    }
    if (getChildCount() == 0) {
      return;
    }
    this.mForceScreenScrolled = true;
    this.mCurrentPage = Math.max(0, Math.min(paramInt, -1 + getPageCount()));
    updateCurrentPageScroll();
    notifyPageSwitchListener();
    invalidate();
  }
  
  protected void setDataIsReady()
  {
    this.mIsDataReady = true;
  }
  
  public void setMinScale(float paramFloat)
  {
    this.mMinScale = paramFloat;
    this.mUseMinScale = true;
    requestLayout();
  }
  
  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mLongClickListener = paramOnLongClickListener;
    int i = getPageCount();
    for (int j = 0; j < i; j++) {
      getPageAt(j).setOnLongClickListener(paramOnLongClickListener);
    }
    super.setOnLongClickListener(paramOnLongClickListener);
  }
  
  protected void setPageHoveringOverDeleteDropTarget(int paramInt, boolean paramBoolean) {}
  
  public void setPageSpacing(int paramInt)
  {
    this.mPageSpacing = paramInt;
    requestLayout();
  }
  
  void setRestorePage(int paramInt)
  {
    this.mRestorePage = paramInt;
  }
  
  public void setScaleX(float paramFloat)
  {
    super.setScaleX(paramFloat);
    if (isReordering(true))
    {
      float[] arrayOfFloat = mapPointFromParentToView(this, this.mParentDownMotionX, this.mParentDownMotionY);
      this.mLastMotionX = arrayOfFloat[0];
      this.mLastMotionY = arrayOfFloat[1];
      updateDragViewTranslationDuringDrag();
    }
  }
  
  protected boolean shouldDrawChild(View paramView)
  {
    return (paramView.getAlpha() > 0.0F) && (paramView.getVisibility() == 0);
  }
  
  protected void snapToDestination()
  {
    snapToPage(getPageNearestToCenterOfScreen(), 750);
  }
  
  protected void snapToPage(int paramInt)
  {
    snapToPage(paramInt, 750);
  }
  
  protected void snapToPage(int paramInt1, int paramInt2)
  {
    snapToPage(paramInt1, paramInt2, false);
  }
  
  protected void snapToPage(int paramInt1, int paramInt2, int paramInt3)
  {
    snapToPage(paramInt1, paramInt2, paramInt3, false);
  }
  
  protected void snapToPage(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    this.mNextPage = paramInt1;
    View localView = getFocusedChild();
    if ((localView != null) && (paramInt1 != this.mCurrentPage) && (localView == getPageAt(this.mCurrentPage))) {
      localView.clearFocus();
    }
    sendScrollAccessibilityEvent();
    pageBeginMoving();
    awakenScrollBars(paramInt3);
    if (paramBoolean) {
      paramInt3 = 0;
    }
    for (;;)
    {
      if (!this.mScroller.isFinished()) {
        this.mScroller.abortAnimation();
      }
      this.mScroller.startScroll(this.mUnboundedScrollX, 0, paramInt2, 0, paramInt3);
      notifyPageSwitchListener();
      if (paramBoolean) {
        computeScroll();
      }
      this.mDeferLoadAssociatedPagesUntilScrollCompletes = true;
      this.mForceScreenScrolled = true;
      invalidate();
      return;
      if (paramInt3 == 0) {
        paramInt3 = Math.abs(paramInt2);
      }
    }
  }
  
  protected void snapToPage(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = Math.max(0, Math.min(paramInt1, -1 + getPageCount()));
    snapToPage(i, getScrollForPage(i) - this.mUnboundedScrollX, paramInt2, paramBoolean);
  }
  
  protected void snapToPageImmediately(int paramInt)
  {
    snapToPage(paramInt, 750, true);
  }
  
  protected void snapToPageWithVelocity(int paramInt1, int paramInt2)
  {
    int i = Math.max(0, Math.min(paramInt1, -1 + getChildCount()));
    int j = getViewportWidth() / 2;
    int k = getScrollForPage(i) - this.mUnboundedScrollX;
    if (Math.abs(paramInt2) < this.mMinFlingVelocity)
    {
      snapToPage(i, 750);
      return;
    }
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / (j * 2));
    float f2 = j + j * distanceInfluenceForSnapDuration(f1);
    int m = Math.abs(paramInt2);
    snapToPage(i, k, 4 * Math.round(1000.0F * Math.abs(f2 / Math.max(this.mMinSnapVelocity, m))));
  }
  
  public boolean startReordering(View paramView)
  {
    int i = indexOfChild(paramView);
    if (this.mTouchState != 0) {}
    do
    {
      return false;
      this.mTempVisiblePagesRange[0] = 0;
      this.mTempVisiblePagesRange[1] = (-1 + getPageCount());
      getOverviewModePages(this.mTempVisiblePagesRange);
      this.mReorderingStarted = true;
    } while ((this.mTempVisiblePagesRange[0] > i) || (i > this.mTempVisiblePagesRange[1]));
    this.mDragView = getChildAt(i);
    this.mDragView.animate().scaleX(1.15F).scaleY(1.15F).setDuration(100L).start();
    this.mDragViewBaselineLeft = this.mDragView.getLeft();
    disableFreeScroll(-1);
    onStartReordering();
    return true;
  }
  
  void stopScrolling()
  {
    this.mCurrentPage = getNextPage();
    forceFinishScroller();
  }
  
  public abstract void syncPageItems(int paramInt, boolean paramBoolean);
  
  public abstract void syncPages();
  
  protected void updateCurrentPageScroll()
  {
    int i = this.mCurrentPage;
    int j = 0;
    if (i >= 0)
    {
      int k = this.mCurrentPage;
      int m = getPageCount();
      j = 0;
      if (k < m) {
        j = getScrollForPage(this.mCurrentPage);
      }
    }
    scrollTo(j, 0);
    this.mScroller.setFinalX(j);
    forceFinishScroller();
  }
  
  void updateDragViewTranslationDuringDrag()
  {
    if (this.mDragView != null)
    {
      float f1 = this.mLastMotionX - this.mDownMotionX + (getScrollX() - this.mDownScrollX) + (this.mDragViewBaselineLeft - this.mDragView.getLeft());
      float f2 = this.mLastMotionY - this.mDownMotionY;
      this.mDragView.setTranslationX(f1);
      this.mDragView.setTranslationY(f2);
    }
  }
  
  void updateFreescrollBounds()
  {
    getOverviewModePages(this.mTempVisiblePagesRange);
    if (isLayoutRtl())
    {
      this.mFreeScrollMinScrollX = getScrollForPage(this.mTempVisiblePagesRange[1]);
      this.mFreeScrollMaxScrollX = getScrollForPage(this.mTempVisiblePagesRange[0]);
      return;
    }
    this.mFreeScrollMinScrollX = getScrollForPage(this.mTempVisiblePagesRange[0]);
    this.mFreeScrollMaxScrollX = getScrollForPage(this.mTempVisiblePagesRange[1]);
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    public boolean isFullScreenPage = false;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
  }
  
  public static abstract interface PageSwitchListener
  {
    public abstract void onPageSwitch(View paramView, int paramInt);
  }
  
  private static class ScrollInterpolator
    implements Interpolator
  {
    public float getInterpolation(float paramFloat)
    {
      float f = paramFloat - 1.0F;
      return 1.0F + f * (f * (f * (f * f)));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedView
 * JD-Core Version:    0.7.0.1
 */