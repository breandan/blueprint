package com.google.android.search.shared.ui;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SuggestionGridLayout
  extends ViewGroup
{
  private final ArrayList<View> mAnimatingViews = new ArrayList();
  private int mAppearTransitionCount = 0;
  @ViewDebug.ExportedProperty(category="velvet")
  private int mColCount;
  private int mColWidth;
  private int mContentWidth;
  private int mDealingIndex = 0;
  private ImageView mDragImageView;
  private StackGridItem mDragStack = null;
  private final ArrayList<GridItem> mGridItems = new ArrayList();
  private Rect mHitRect = new Rect();
  private int mHorizontalItemMargin;
  private boolean mIsDragging = false;
  private int[] mItemBottoms;
  private LayoutTransition mLayoutTransition;
  @ViewDebug.ExportedProperty(category="velvet")
  private int mMaxColumnWidth;
  private OnDismissListener mOnDismissListener;
  private OnStackChangeListener mOnStackChangeListener;
  boolean mReinitStackBitmap = false;
  private Resources mResources;
  private Bitmap mStackBitmap = null;
  private final StackOrderComparator mStackOrderComparator = new StackOrderComparator(null);
  private SwipeHelper mSwiper;
  private int mVerticalItemMargin;
  
  public SuggestionGridLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SuggestionGridLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SuggestionGridLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SuggestionGridLayout, paramInt, 0);
    this.mMaxColumnWidth = localTypedArray.getDimensionPixelSize(1, 2147483647);
    this.mColCount = localTypedArray.getInteger(0, 1);
    this.mItemBottoms = new int[this.mColCount];
    this.mVerticalItemMargin = localTypedArray.getDimensionPixelSize(3, 0);
    this.mHorizontalItemMargin = localTypedArray.getDimensionPixelSize(2, 0);
    localTypedArray.recycle();
    this.mLayoutTransition = getLayoutTransition();
    if (this.mLayoutTransition != null) {
      configureTransition(this.mLayoutTransition);
    }
    setClipToPadding(false);
    setClipChildren(false);
    setChildrenDrawingOrderEnabled(true);
    float f = paramContext.getResources().getDisplayMetrics().density;
    int i = ViewConfiguration.get(paramContext).getScaledPagingTouchSlop();
    this.mSwiper = new SwipeHelper(0, new SwipeCallback(null), f, i);
    this.mResources = paramContext.getResources();
    this.mDragImageView = new ImageView(paramContext);
    LayoutParams localLayoutParams = (LayoutParams)generateDefaultLayoutParams();
    localLayoutParams.appearAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.NONE;
    localLayoutParams.disappearAnimationType = SuggestionGridLayout.LayoutParams.AnimationType.NONE;
    this.mDragImageView.setLayoutParams(localLayoutParams);
    super.addView(this.mDragImageView, -1);
    this.mDragImageView.setVisibility(4);
  }
  
  private void addNewCardsToDeal(GridItem paramGridItem)
  {
    addNewCardsToDeal(paramGridItem.getViews());
  }
  
  private void addNewCardsToDeal(Collection<View> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (CardAnimator.usesAlpha(localLayoutParams.appearAnimationType)) {
        localView.setLayerType(2, null);
      }
      localLayoutParams.animationIndex = this.mDealingIndex;
      this.mDealingIndex = (1 + this.mDealingIndex);
    }
  }
  
  private void configureTransition(LayoutTransition paramLayoutTransition)
  {
    paramLayoutTransition.enableTransitionType(4);
    paramLayoutTransition.setDuration(300L);
    paramLayoutTransition.enableTransitionType(2);
    paramLayoutTransition.enableTransitionType(3);
    int i = getContext().getResources().getDisplayMetrics().heightPixels;
    paramLayoutTransition.setAnimator(2, new CardAnimator(true, i));
    paramLayoutTransition.setInterpolator(2, new DecelerateInterpolator(2.5F));
    paramLayoutTransition.setAnimator(3, new CardAnimator(false, i));
    paramLayoutTransition.setInterpolator(3, new DecelerateInterpolator(1.5F));
    paramLayoutTransition.addTransitionListener(new LayoutTransition.TransitionListener()
    {
      public void endTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
      {
        if (paramAnonymousInt == 2) {
          SuggestionGridLayout.access$210(SuggestionGridLayout.this);
        }
      }
      
      public void startTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
      {
        if (paramAnonymousInt == 2) {
          SuggestionGridLayout.access$208(SuggestionGridLayout.this);
        }
      }
    });
    this.mLayoutTransition.setAnimateParentHierarchy(false);
  }
  
  private GridItem getGridItemForView(View paramView)
  {
    int i = this.mGridItems.size();
    for (int j = 0; j < i; j++)
    {
      GridItem localGridItem = (GridItem)this.mGridItems.get(j);
      if (localGridItem.getViews().contains(paramView)) {
        return localGridItem;
      }
    }
    return null;
  }
  
  private void setupDragImage(StackGridItem paramStackGridItem)
  {
    if ((this.mStackBitmap == null) || (paramStackGridItem.getMeasuredWidth() > this.mStackBitmap.getWidth()) || (paramStackGridItem.getMeasuredHeight() > this.mStackBitmap.getHeight())) {
      initStackBitmap();
    }
    Canvas localCanvas = new Canvas(this.mStackBitmap);
    localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    paramStackGridItem.draw(localCanvas);
    this.mDragImageView.setImageBitmap(this.mStackBitmap);
    this.mDragImageView.measure(this.mStackBitmap.getWidth(), this.mStackBitmap.getHeight());
    this.mDragImageView.layout(paramStackGridItem.left, paramStackGridItem.top, paramStackGridItem.left + this.mStackBitmap.getWidth(), paramStackGridItem.top + this.mStackBitmap.getHeight());
    this.mDragImageView.setTranslationX(0.0F);
    this.mDragImageView.setTranslationY(0.0F);
  }
  
  private LayoutParams setupLayoutParams(View paramView, int paramInt)
  {
    if (paramInt >= this.mColCount) {
      throw new RuntimeException("Column exceeds column count.");
    }
    ViewGroup.LayoutParams localLayoutParams1 = paramView.getLayoutParams();
    if (localLayoutParams1 == null) {
      localLayoutParams1 = generateDefaultLayoutParams();
    }
    ViewGroup.LayoutParams localLayoutParams2;
    LayoutParams localLayoutParams;
    if (checkLayoutParams(localLayoutParams1))
    {
      localLayoutParams2 = localLayoutParams1;
      localLayoutParams = (LayoutParams)localLayoutParams2;
      if (localLayoutParams.column == -2) {
        if (!LayoutUtils.isLayoutRtl(this)) {
          break label134;
        }
      }
    }
    label134:
    for (paramInt = 0;; paramInt = -1 + this.mColCount)
    {
      if (paramInt != -3) {
        localLayoutParams.column = paramInt;
      }
      if ((localLayoutParams.noPadding) && (localLayoutParams.column != -1))
      {
        localLayoutParams.noPadding = false;
        Log.w("SuggestionGridLayout", "only spanAllColumns views can have no padding");
      }
      paramView.setLayoutParams(localLayoutParams);
      return localLayoutParams;
      localLayoutParams2 = generateLayoutParams(localLayoutParams1);
      break;
    }
  }
  
  private boolean toggleStackExpansion(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 1)
    {
      int i = (int)(0.5F + paramMotionEvent.getX());
      int j = (int)(0.5F + paramMotionEvent.getY());
      int k = -1 + getChildCount();
      if (k >= 0)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() != 0) {}
        StackGridItem localStackGridItem;
        do
        {
          GridItem localGridItem;
          do
          {
            do
            {
              k--;
              break;
              localView.getHitRect(this.mHitRect);
            } while (!this.mHitRect.contains(i, j));
            localGridItem = getGridItemForView(localView);
          } while ((localGridItem == null) || (!(localGridItem instanceof StackGridItem)));
          localStackGridItem = (StackGridItem)localGridItem;
          if ((j > localStackGridItem.mCollapsedCardsTop) && (j < localStackGridItem.mCollapsedCardsBottom))
          {
            if (localStackGridItem.isExpanded())
            {
              localStackGridItem.bringToFrontAndCollapse(localView);
              return true;
            }
            localStackGridItem.setExpanded(true);
            return true;
          }
        } while ((!localStackGridItem.isCollapsible()) || (!localStackGridItem.isExpanded()));
        localStackGridItem.setExpanded(false);
        return true;
      }
    }
    return false;
  }
  
  public void addStackToColumn(ArrayList<View> paramArrayList, int paramInt)
  {
    if (paramInt >= this.mColCount) {
      throw new RuntimeException("Column exceeds column count.");
    }
    Collections.sort(paramArrayList, this.mStackOrderComparator);
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext()) {
      ((View)localIterator1.next()).setLayoutParams(generateDefaultLayoutParams());
    }
    StackGridItem localStackGridItem = new StackGridItem(getContext(), paramArrayList, paramInt);
    this.mGridItems.add(localStackGridItem);
    addNewCardsToDeal(localStackGridItem);
    Iterator localIterator2 = paramArrayList.iterator();
    while (localIterator2.hasNext()) {
      super.addView((View)localIterator2.next(), -1);
    }
    this.mReinitStackBitmap = true;
  }
  
  public void addView(View paramView)
  {
    addView(paramView, -1);
  }
  
  public void addView(View paramView, int paramInt)
  {
    addViewWithIndexAndColumn(paramView, paramInt, -3);
  }
  
  public void addViewToColumn(View paramView, int paramInt)
  {
    if ((paramView instanceof DismissableChildContainer)) {
      ((DismissableChildContainer)paramView).setAllowedSwipeDirections(this.mSwiper.mAllowSwipeTowardsStart, this.mSwiper.mAllowSwipeTowardsEnd);
    }
    LayoutParams localLayoutParams = setupLayoutParams(paramView, paramInt);
    SimpleGridItem localSimpleGridItem = new SimpleGridItem(paramView);
    this.mGridItems.add(localSimpleGridItem);
    addNewCardsToDeal(localSimpleGridItem);
    super.addView(paramView, localLayoutParams);
  }
  
  public void addViewWithIndexAndColumn(View paramView, int paramInt1, int paramInt2)
  {
    if ((paramView instanceof DismissableChildContainer)) {
      ((DismissableChildContainer)paramView).setAllowedSwipeDirections(this.mSwiper.mAllowSwipeTowardsStart, this.mSwiper.mAllowSwipeTowardsEnd);
    }
    LayoutParams localLayoutParams = setupLayoutParams(paramView, paramInt2);
    SimpleGridItem localSimpleGridItem = new SimpleGridItem(paramView);
    if (paramInt1 == -1) {
      this.mGridItems.add(localSimpleGridItem);
    }
    for (;;)
    {
      addNewCardsToDeal(localSimpleGridItem);
      super.addView(paramView, paramInt1, localLayoutParams);
      return;
      this.mGridItems.add(paramInt1, localSimpleGridItem);
    }
  }
  
  public boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return ((paramLayoutParams instanceof LayoutParams)) && (paramLayoutParams.width == -1);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    GridItem localGridItem = getGridItemForView(paramView);
    boolean bool1 = localGridItem instanceof StackGridItem;
    int i = 0;
    if (bool1)
    {
      boolean bool3 = this.mIsDragging;
      i = 0;
      if (!bool3)
      {
        int j = this.mAppearTransitionCount;
        i = 0;
        if (j == 0)
        {
          StackGridItem localStackGridItem = (StackGridItem)localGridItem;
          boolean bool4 = localStackGridItem.clippingDisabled();
          i = 0;
          if (!bool4)
          {
            paramCanvas.save();
            i = 1;
            paramCanvas.clipRect(localStackGridItem.getChildClipRect(paramView));
          }
        }
      }
    }
    boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    if (i != 0) {
      paramCanvas.restore();
    }
    return bool2;
  }
  
  public ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -2, 0);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mAnimatingViews.isEmpty()) {
      return paramInt2;
    }
    int i = paramInt1 - this.mAnimatingViews.size();
    if (paramInt2 >= i) {
      return indexOfChild((View)this.mAnimatingViews.get(paramInt2 - i));
    }
    int j = paramInt2;
    for (int k = 0; k <= j; k++)
    {
      View localView = getChildAt(k);
      if (this.mAnimatingViews.contains(localView)) {
        j++;
      }
    }
    return j;
  }
  
  public int getColumnCount()
  {
    return this.mColCount;
  }
  
  void initStackBitmap()
  {
    int i = this.mGridItems.size();
    int j = 0;
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      GridItem localGridItem = (GridItem)this.mGridItems.get(m);
      if (((localGridItem instanceof StackGridItem)) && (((StackGridItem)localGridItem).isCollapsible()))
      {
        int n = localGridItem.getMeasuredWidth();
        int i1 = localGridItem.getMeasuredHeight();
        if (n > j) {
          j = n;
        }
        if (i1 > k) {
          k = i1;
        }
      }
    }
    if ((j > 0) && (k > 0) && ((this.mStackBitmap == null) || (j > this.mStackBitmap.getWidth()) || (k > this.mStackBitmap.getHeight()))) {
      this.mStackBitmap = Bitmap.createBitmap(j, k, Bitmap.Config.ARGB_8888);
    }
  }
  
  public boolean isViewLocallyVisible(View paramView)
  {
    GridItem localGridItem = getGridItemForView(paramView);
    if (localGridItem == null) {
      return false;
    }
    if ((localGridItem instanceof StackGridItem)) {
      return ((StackGridItem)localGridItem).isTopCard(paramView);
    }
    return true;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(SuggestionGridLayout.class.getCanonicalName());
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = this.mSwiper.onInterceptTouchEvent(paramMotionEvent);
    if (!bool) {
      bool = toggleStackExpansion(paramMotionEvent);
    }
    if (bool) {
      getParent().requestDisallowInterceptTouchEvent(true);
    }
    return (bool) || (super.onInterceptTouchEvent(paramMotionEvent));
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Arrays.fill(this.mItemBottoms, getPaddingTop());
    int i = (getMeasuredWidth() - this.mContentWidth) / 2;
    int j = this.mGridItems.size();
    int k = 0;
    if (k < j)
    {
      GridItem localGridItem = (GridItem)this.mGridItems.get(k);
      if (localGridItem.isGone()) {}
      for (;;)
      {
        k++;
        break;
        LayoutParams localLayoutParams = localGridItem.getGridLayoutParams();
        int m;
        if (localLayoutParams.column == -1)
        {
          m = 0;
          label94:
          if (!localLayoutParams.noPadding) {
            break label201;
          }
        }
        int i3;
        label201:
        for (int n = 0;; n = i + m * (this.mColWidth + this.mHorizontalItemMargin))
        {
          int i1 = this.mItemBottoms[m] + localLayoutParams.topMargin;
          int i2 = n + localGridItem.getMeasuredWidth();
          i3 = i1 + localGridItem.getMeasuredHeight();
          localGridItem.gridLayout(n, i1, i2, i3);
          if (localLayoutParams.column != -1) {
            break label221;
          }
          Arrays.fill(this.mItemBottoms, i3 + this.mVerticalItemMargin + localLayoutParams.bottomMargin);
          break;
          m = localLayoutParams.column;
          break label94;
        }
        label221:
        this.mItemBottoms[localLayoutParams.column] = (i3 + this.mVerticalItemMargin + localLayoutParams.bottomMargin);
      }
    }
    if (this.mReinitStackBitmap)
    {
      this.mReinitStackBitmap = false;
      initStackBitmap();
    }
    this.mDealingIndex = 0;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = this.mHorizontalItemMargin * (-1 + this.mColCount);
    int i1 = 0;
    int i2 = 0;
    int i5;
    int i6;
    int i7;
    int i8;
    Object localObject;
    int i9;
    GridItem localGridItem;
    switch (i)
    {
    default: 
    case 1073741824: 
    case -2147483648: 
      for (;;)
      {
        this.mContentWidth = (n + i1 * this.mColCount);
        this.mColWidth = i1;
        i5 = View.MeasureSpec.makeMeasureSpec(i1, 1073741824);
        i6 = View.MeasureSpec.makeMeasureSpec(this.mContentWidth, 1073741824);
        i7 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
        Arrays.fill(this.mItemBottoms, 0);
        i8 = this.mGridItems.size();
        localObject = null;
        for (i9 = 0;; i9++)
        {
          if (i9 >= i8) {
            break label470;
          }
          localGridItem = (GridItem)this.mGridItems.get(i9);
          if (!localGridItem.isGone()) {
            break;
          }
        }
        i2 = k;
        int i21 = i2 - getPaddingLeft() - getPaddingRight();
        i1 = Math.min(this.mMaxColumnWidth, (i21 - n) / this.mColCount);
        continue;
        int i3 = getPaddingLeft() + getPaddingRight();
        int i4 = k - i3;
        i1 = Math.min(this.mMaxColumnWidth, (i4 - n) / this.mColCount);
        i2 = i3 + (n + i1 * this.mColCount);
      }
    }
    throw new IllegalArgumentException("Cannot measure SuggestionGridLayout with mode UNSPECIFIED");
    LayoutParams localLayoutParams2 = localGridItem.getGridLayoutParams();
    int i17 = View.MeasureSpec.makeMeasureSpec(0, 0);
    int i18 = localLayoutParams2.column;
    int i19;
    if (i18 == -1) {
      if (localLayoutParams2.noPadding)
      {
        if (localLayoutParams2.inheritPadding) {
          localGridItem.inheritPadding();
        }
        i19 = i7;
        label350:
        i18 = 0;
      }
    }
    for (;;)
    {
      int i20 = i8 - 1;
      if ((i9 == i20) && (localLayoutParams2.fillViewport)) {
        localObject = localGridItem;
      }
      localGridItem.gridMeasure(i19, i17);
      int[] arrayOfInt = this.mItemBottoms;
      arrayOfInt[i18] += localLayoutParams2.topMargin + localGridItem.getMeasuredHeight() + this.mVerticalItemMargin + localLayoutParams2.bottomMargin;
      if (localLayoutParams2.column != -1) {
        break;
      }
      Arrays.fill(this.mItemBottoms, this.mItemBottoms[i18]);
      break;
      i19 = i6;
      break label350;
      i19 = i5;
    }
    label470:
    int i10 = m;
    if (j != 1073741824)
    {
      int i13 = 0;
      for (int i14 = 0;; i14++)
      {
        int i15 = this.mColCount;
        if (i14 >= i15) {
          break;
        }
        int i16 = this.mItemBottoms[i14] - this.mVerticalItemMargin;
        if (i16 > i13) {
          i13 = i16;
        }
      }
      i10 = i13 + getPaddingTop() + getPaddingBottom();
    }
    LayoutParams localLayoutParams1;
    if (localObject != null)
    {
      localLayoutParams1 = localObject.getGridLayoutParams();
      if (localLayoutParams1.column != -1) {
        break label634;
      }
    }
    label634:
    for (int i11 = 0;; i11 = localLayoutParams1.column)
    {
      int i12 = this.mItemBottoms[i11] - this.mVerticalItemMargin - localObject.getMeasuredHeight();
      localObject.gridMeasure(View.MeasureSpec.makeMeasureSpec(localObject.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i10 - i12, 1073741824));
      setMeasuredDimension(i2, i10);
      return;
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.mSwiper.onTouchEvent(paramMotionEvent)) {
      return super.onTouchEvent(paramMotionEvent);
    }
    return true;
  }
  
  public void removeAllViews()
  {
    for (int i = -1 + getChildCount(); i >= 0; i--) {
      removeGridItem(getChildAt(i));
    }
  }
  
  public void removeGridItem(View paramView)
  {
    if (this.mAnimatingViews.contains(paramView)) {
      this.mSwiper.cancelOngoingDrag();
    }
    GridItem localGridItem = getGridItemForView(paramView);
    if (localGridItem != null)
    {
      localGridItem.removeView(paramView);
      if (localGridItem.getViews().isEmpty()) {
        this.mGridItems.remove(localGridItem);
      }
      this.mAnimatingViews.remove(paramView);
      super.removeView(paramView);
    }
    while (paramView == this.mDragImageView) {
      return;
    }
    Log.w("SuggestionGridLayout", "removeGridItem with non-grid item " + paramView);
  }
  
  public void removeView(View paramView)
  {
    removeGridItem(paramView);
  }
  
  public void removeViewsWithTag(int paramInt)
  {
    for (int i = -1 + getChildCount(); i >= 0; i--)
    {
      View localView = getChildAt(i);
      if (localView.getTag(paramInt) != null) {
        removeView(localView);
      }
    }
  }
  
  public void resetChildDismissState(View paramView)
  {
    paramView.setTranslationX(0.0F);
    paramView.setAlpha(1.0F);
  }
  
  public void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mSwiper.mAllowSwipeTowardsStart = paramBoolean1;
    this.mSwiper.mAllowSwipeTowardsEnd = paramBoolean2;
  }
  
  public void setLayoutTransitionStartDelay(int paramInt, long paramLong)
  {
    if (this.mLayoutTransition != null) {
      this.mLayoutTransition.setStartDelay(paramInt, 0L);
    }
  }
  
  public void setLayoutTransitionsEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (LayoutTransition localLayoutTransition = this.mLayoutTransition;; localLayoutTransition = null)
    {
      setLayoutTransition(localLayoutTransition);
      return;
    }
  }
  
  public void setOnDismissListener(OnDismissListener paramOnDismissListener)
  {
    this.mOnDismissListener = paramOnDismissListener;
  }
  
  public void setOnStackChangeListener(OnStackChangeListener paramOnStackChangeListener)
  {
    this.mOnStackChangeListener = paramOnStackChangeListener;
  }
  
  public static abstract interface CardWrapper
  {
    public abstract View getCardView();
    
    public abstract boolean getOverdrawRect(Rect paramRect);
    
    public abstract int getStackOrderOverride();
    
    public abstract void setForcedCardHeight(int paramInt);
    
    public abstract void setStackOrderOverride(int paramInt);
  }
  
  public static abstract interface DismissableChildContainer
  {
    public abstract boolean isDismissableViewAtPosition(SuggestionGridLayout paramSuggestionGridLayout, MotionEvent paramMotionEvent);
    
    public abstract void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2);
  }
  
  static abstract interface GridItem
  {
    public abstract SuggestionGridLayout.LayoutParams getGridLayoutParams();
    
    public abstract int getMeasuredHeight();
    
    public abstract int getMeasuredWidth();
    
    public abstract ArrayList<View> getViews();
    
    public abstract void gridLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
    
    public abstract void gridMeasure(int paramInt1, int paramInt2);
    
    public abstract void inheritPadding();
    
    public abstract boolean isGone();
    
    public abstract void removeView(View paramView);
    
    public abstract void setDismissed(View paramView, boolean paramBoolean);
    
    public abstract void update();
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    @ViewDebug.ExportedProperty(category="velvet")
    public int animationIndex;
    public AnimationType appearAnimationType;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean canDismiss = true;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean canDrag = true;
    @ViewDebug.ExportedProperty(category="velvet")
    public int column;
    public AnimationType disappearAnimationType;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean fillViewport = false;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean inheritPadding = false;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean noPadding = false;
    @ViewDebug.ExportedProperty(category="velvet")
    public boolean removeOnDismiss = true;
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      this.column = paramInt3;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SuggestionGridLayout_Layout);
      this.column = localTypedArray.getInteger(0, 0);
      this.canDismiss = localTypedArray.getBoolean(2, true);
      this.canDrag = localTypedArray.getBoolean(1, true);
      this.noPadding = localTypedArray.getBoolean(4, false);
      this.inheritPadding = localTypedArray.getBoolean(5, false);
      this.fillViewport = localTypedArray.getBoolean(6, false);
      this.removeOnDismiss = localTypedArray.getBoolean(3, true);
      this.appearAnimationType = getAnimationType(localTypedArray, 7, AnimationType.DEAL);
      this.disappearAnimationType = getAnimationType(localTypedArray, 8, AnimationType.NONE);
      localTypedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super(paramLayoutParams.height);
      if ((paramLayoutParams instanceof LayoutParams))
      {
        LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
        this.column = localLayoutParams.column;
        this.canDismiss = localLayoutParams.canDismiss;
        this.canDrag = localLayoutParams.canDrag;
        this.noPadding = localLayoutParams.noPadding;
        this.inheritPadding = localLayoutParams.inheritPadding;
        this.appearAnimationType = localLayoutParams.appearAnimationType;
        this.disappearAnimationType = localLayoutParams.disappearAnimationType;
        this.animationIndex = localLayoutParams.animationIndex;
      }
    }
    
    private AnimationType getAnimationType(TypedArray paramTypedArray, int paramInt, AnimationType paramAnimationType)
    {
      int i = paramTypedArray.getInt(paramInt, -1);
      if (i >= 0) {
        paramAnimationType = AnimationType.values()[i];
      }
      return paramAnimationType;
    }
    
    public static enum AnimationType
    {
      static
      {
        SLIDE_DOWN = new AnimationType("SLIDE_DOWN", 2);
        FADE = new AnimationType("FADE", 3);
        NONE = new AnimationType("NONE", 4);
        FADE_AFTER_DEAL = new AnimationType("FADE_AFTER_DEAL", 5);
        AnimationType[] arrayOfAnimationType = new AnimationType[6];
        arrayOfAnimationType[0] = DEAL;
        arrayOfAnimationType[1] = SLIDE_UP;
        arrayOfAnimationType[2] = SLIDE_DOWN;
        arrayOfAnimationType[3] = FADE;
        arrayOfAnimationType[4] = NONE;
        arrayOfAnimationType[5] = FADE_AFTER_DEAL;
        $VALUES = arrayOfAnimationType;
      }
      
      private AnimationType() {}
    }
  }
  
  public static abstract interface OnDismissListener
  {
    public abstract void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss);
  }
  
  public static abstract interface OnStackChangeListener
  {
    public abstract boolean preStackViewOrderChange(List<View> paramList);
  }
  
  private class PendingViewDismissImpl
    extends PendingViewDismiss
  {
    private final SuggestionGridLayout.GridItem mGridItem;
    
    PendingViewDismissImpl(SuggestionGridLayout.GridItem paramGridItem, View paramView)
    {
      super();
      this.mGridItem = paramGridItem;
    }
    
    PendingViewDismissImpl(Collection<View> paramCollection)
    {
      super();
      this.mGridItem = paramCollection;
    }
    
    public void doCommit()
    {
      if (this.mGridItem.getGridLayoutParams().removeOnDismiss)
      {
        Iterator localIterator = getDismissedViews().iterator();
        while (localIterator.hasNext())
        {
          View localView = (View)localIterator.next();
          this.mGridItem.removeView(localView);
          SuggestionGridLayout.this.removeView(localView);
        }
        if (this.mGridItem.getViews().isEmpty()) {
          SuggestionGridLayout.this.mGridItems.remove(this.mGridItem);
        }
        SuggestionGridLayout.this.invalidate();
      }
    }
    
    public void doRestore()
    {
      if (this.mGridItem.getGridLayoutParams().removeOnDismiss)
      {
        SuggestionGridLayout.this.addNewCardsToDeal(getDismissedViews());
        Iterator localIterator = getDismissedViews().iterator();
        while (localIterator.hasNext())
        {
          View localView = (View)localIterator.next();
          SuggestionGridLayout.this.mSwiper.resetTranslation(localView);
          this.mGridItem.setDismissed(localView, false);
        }
        this.mGridItem.update();
        SuggestionGridLayout.this.invalidate();
      }
    }
  }
  
  class SimpleGridItem
    implements SuggestionGridLayout.GridItem
  {
    View mView;
    ArrayList<View> mViews = new ArrayList();
    
    public SimpleGridItem(View paramView)
    {
      this.mView = paramView;
      this.mViews.add(paramView);
    }
    
    public SuggestionGridLayout.LayoutParams getGridLayoutParams()
    {
      return (SuggestionGridLayout.LayoutParams)this.mView.getLayoutParams();
    }
    
    public int getMeasuredHeight()
    {
      return this.mView.getMeasuredHeight();
    }
    
    public int getMeasuredWidth()
    {
      return this.mView.getMeasuredWidth();
    }
    
    public ArrayList<View> getViews()
    {
      return this.mViews;
    }
    
    public void gridLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.mView.layout(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public void gridMeasure(int paramInt1, int paramInt2)
    {
      this.mView.measure(paramInt1, paramInt2);
    }
    
    public void inheritPadding()
    {
      this.mView.setPadding(SuggestionGridLayout.this.getPaddingLeft(), this.mView.getPaddingTop(), SuggestionGridLayout.this.getPaddingRight(), this.mView.getPaddingBottom());
    }
    
    public boolean isGone()
    {
      return this.mView.getVisibility() == 8;
    }
    
    public void removeView(View paramView)
    {
      this.mViews.remove(paramView);
    }
    
    public void setDismissed(View paramView, boolean paramBoolean)
    {
      if (paramBoolean) {}
      for (int i = 8;; i = 0)
      {
        paramView.setVisibility(i);
        return;
      }
    }
    
    public void update() {}
  }
  
  class StackGridItem
    implements SuggestionGridLayout.GridItem
  {
    public int bottom;
    public int left;
    int mChildHeight = -1;
    int mCollapsedCardsBottom = -1;
    int mCollapsedCardsTop = -1;
    final float mDensity;
    boolean mDisableClipping = true;
    boolean mExpanded = false;
    int mExpandedOverlapAmount;
    SuggestionGridLayout.LayoutParams mLp;
    final int mMaxExpandedOverlapAmount;
    int mMeasuredHeight;
    int mMeasuredWidth;
    int mNonDismissedChildCount = 0;
    int mPaddingAdjustment = 0;
    Rect mTmpClipRect = new Rect();
    boolean mTopItemBeingDragged = false;
    int mTotalCollapsedSpacing;
    ArrayList<View> mViews;
    public int right;
    public int top;
    
    public StackGridItem(ArrayList<View> paramArrayList, int paramInt)
    {
      this.mViews = paramInt;
      Iterator localIterator = paramInt.iterator();
      while (localIterator.hasNext()) {
        if (((View)localIterator.next()).getVisibility() != 8) {
          this.mNonDismissedChildCount = (1 + this.mNonDismissedChildCount);
        }
      }
      int i;
      this.mLp = new SuggestionGridLayout.LayoutParams(-1, -2, i);
      this.mDensity = paramArrayList.getResources().getDisplayMetrics().density;
      updateTotalCollapsedSpacing();
      this.mMaxExpandedOverlapAmount = ((int)(95.0F * this.mDensity));
      updateItemBackgrounds();
      setExpanded(false);
    }
    
    private void drawReorderedStack(final View paramView)
    {
      paramView.bringToFront();
      setDisableClipping(true);
      SuggestionGridLayout.this.invalidate();
      SuggestionGridLayout.this.postDelayed(new Runnable()
      {
        public void run()
        {
          SuggestionGridLayout.StackGridItem.this.mViews.remove(paramView);
          SuggestionGridLayout.StackGridItem.this.mViews.add(paramView);
          if (SuggestionGridLayout.StackGridItem.this.mExpanded) {
            SuggestionGridLayout.StackGridItem.this.setExpanded(false);
          }
          for (;;)
          {
            SuggestionGridLayout.StackGridItem.this.updateItemBackgrounds();
            return;
            SuggestionGridLayout.StackGridItem.this.setDisableClipping(false);
            SuggestionGridLayout.this.requestLayout();
          }
        }
      }, 70L);
    }
    
    private int getTallestChildHeight(int paramInt1, int paramInt2)
    {
      int i = 0;
      int j = 0;
      if (j < this.mViews.size())
      {
        View localView = (View)this.mViews.get(j);
        if (localView.getVisibility() == 8) {}
        for (;;)
        {
          j++;
          break;
          localView.measure(paramInt1, paramInt2);
          int k = localView.getMeasuredHeight();
          Drawable localDrawable = unwrapCardView(localView).getBackground();
          if (localDrawable != null)
          {
            localDrawable.getPadding(this.mTmpClipRect);
            k -= this.mTmpClipRect.top;
          }
          i = Math.max(i, k);
        }
      }
      return i;
    }
    
    private View unwrapCardView(View paramView)
    {
      if ((paramView instanceof SuggestionGridLayout.CardWrapper)) {
        paramView = ((SuggestionGridLayout.CardWrapper)paramView).getCardView();
      }
      return paramView;
    }
    
    private void updateItemBackgrounds()
    {
      int i = 1;
      int j = 0;
      while (j < this.mViews.size())
      {
        View localView1 = (View)this.mViews.get(j);
        if (localView1.getVisibility() == 8)
        {
          j++;
        }
        else
        {
          View localView2 = unwrapCardView(localView1);
          Resources localResources = SuggestionGridLayout.this.mResources;
          if (i != 0) {}
          for (int k = 2130837532;; k = 2130837537)
          {
            localView2.setBackground(localResources.getDrawable(k));
            i = 0;
            break;
          }
        }
      }
    }
    
    public void bringToFrontAndCollapse(final View paramView)
    {
      if (isTopCard(paramView))
      {
        if (this.mExpanded) {
          setExpanded(false);
        }
        return;
      }
      SuggestionGridLayout.OnStackChangeListener localOnStackChangeListener = SuggestionGridLayout.this.mOnStackChangeListener;
      boolean bool = false;
      if (localOnStackChangeListener != null)
      {
        ArrayList localArrayList = Lists.newArrayList(this.mViews);
        localArrayList.remove(paramView);
        localArrayList.add(paramView);
        int i = localArrayList.size();
        for (int j = 0; j < i; j++)
        {
          View localView = (View)localArrayList.get(j);
          if ((localView instanceof SuggestionGridLayout.CardWrapper)) {
            ((SuggestionGridLayout.CardWrapper)localView).setStackOrderOverride(j);
          }
        }
        bool = SuggestionGridLayout.this.mOnStackChangeListener.preStackViewOrderChange(ImmutableList.copyOf(localArrayList));
      }
      if (bool)
      {
        SuggestionGridLayout.this.invalidate();
        SuggestionGridLayout.this.postDelayed(new Runnable()
        {
          public void run()
          {
            SuggestionGridLayout.StackGridItem.this.drawReorderedStack(paramView);
          }
        }, 300L);
        return;
      }
      drawReorderedStack(paramView);
    }
    
    boolean clippingDisabled()
    {
      return this.mDisableClipping;
    }
    
    public void draw(Canvas paramCanvas)
    {
      if (this.mViews.size() == 0) {
        return;
      }
      int i = 0;
      Object localObject = null;
      int j = 0;
      label18:
      View localView;
      if (j < this.mViews.size())
      {
        localView = (View)this.mViews.get(j);
        if (localView.getVisibility() != 8) {
          break label60;
        }
      }
      for (;;)
      {
        j++;
        break label18;
        break;
        label60:
        if (localObject == null) {
          localObject = localView;
        }
        int k = localView.getTop() - localObject.getTop();
        paramCanvas.translate(0.0F, k - i);
        localView.draw(paramCanvas);
        i = k;
      }
    }
    
    public void enableAnimation(boolean paramBoolean)
    {
      int i = 0;
      if (i < this.mViews.size())
      {
        ViewGroup.LayoutParams localLayoutParams = ((View)this.mViews.get(i)).getLayoutParams();
        SuggestionGridLayout.LayoutParams localLayoutParams1;
        SuggestionGridLayout.LayoutParams.AnimationType localAnimationType1;
        if ((localLayoutParams instanceof SuggestionGridLayout.LayoutParams))
        {
          localLayoutParams1 = (SuggestionGridLayout.LayoutParams)localLayoutParams;
          if (!paramBoolean) {
            break label79;
          }
          localAnimationType1 = SuggestionGridLayout.LayoutParams.AnimationType.DEAL;
          label50:
          localLayoutParams1.appearAnimationType = localAnimationType1;
          if (!paramBoolean) {
            break label87;
          }
        }
        label79:
        label87:
        for (SuggestionGridLayout.LayoutParams.AnimationType localAnimationType2 = SuggestionGridLayout.LayoutParams.AnimationType.FADE;; localAnimationType2 = SuggestionGridLayout.LayoutParams.AnimationType.NONE)
        {
          localLayoutParams1.disappearAnimationType = localAnimationType2;
          i++;
          break;
          localAnimationType1 = SuggestionGridLayout.LayoutParams.AnimationType.NONE;
          break label50;
        }
      }
    }
    
    Rect getChildClipRect(View paramView)
    {
      int i = paramView.getLeft();
      int j = paramView.getTop();
      int k = paramView.getRight();
      int m = paramView.getBottom();
      if (((paramView instanceof SuggestionGridLayout.CardWrapper)) && (((SuggestionGridLayout.CardWrapper)paramView).getOverdrawRect(this.mTmpClipRect)))
      {
        if (this.mTmpClipRect.left > 0) {
          i -= this.mTmpClipRect.left;
        }
        if (this.mTmpClipRect.top > 0) {
          j -= this.mTmpClipRect.top;
        }
        if (this.mTmpClipRect.right > 0) {
          k += this.mTmpClipRect.right;
        }
        if (this.mTmpClipRect.bottom > 0) {
          m += this.mTmpClipRect.bottom;
        }
      }
      int n = this.mViews.indexOf(paramView);
      if (n != -1) {}
      for (int i1 = n + 1;; i1++) {
        if (i1 < this.mViews.size())
        {
          View localView = (View)this.mViews.get(i1);
          if (localView.getVisibility() != 8) {
            m = localView.getTop() + unwrapCardView(localView).getPaddingTop();
          }
        }
        else
        {
          this.mTmpClipRect.set(i, j, k, m);
          return this.mTmpClipRect;
        }
      }
    }
    
    public SuggestionGridLayout.LayoutParams getGridLayoutParams()
    {
      return this.mLp;
    }
    
    public int getMeasuredHeight()
    {
      return this.mMeasuredHeight;
    }
    
    public int getMeasuredWidth()
    {
      return this.mMeasuredWidth;
    }
    
    public ArrayList<View> getViews()
    {
      return this.mViews;
    }
    
    public void gridLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      onLayout(true, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public void gridMeasure(int paramInt1, int paramInt2)
    {
      onMeasure(paramInt1, paramInt2);
    }
    
    public void inheritPadding()
    {
      Log.w("SuggestionGridLayout", "Stacks can't inherit padding");
    }
    
    boolean isCollapsible()
    {
      return this.mNonDismissedChildCount >= 3;
    }
    
    public boolean isExpanded()
    {
      return this.mExpanded;
    }
    
    public boolean isGone()
    {
      Iterator localIterator = this.mViews.iterator();
      while (localIterator.hasNext()) {
        if (((View)localIterator.next()).getVisibility() != 8) {
          return false;
        }
      }
      return true;
    }
    
    public boolean isTopCard(View paramView)
    {
      for (int i = -1 + this.mViews.size();; i--)
      {
        Object localObject = null;
        if (i >= 0)
        {
          View localView = (View)this.mViews.get(i);
          if (localView.getVisibility() != 8) {
            localObject = localView;
          }
        }
        else
        {
          if (localObject != paramView) {
            break;
          }
          return true;
        }
      }
      return false;
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = this.mExpandedOverlapAmount;
      this.left = paramInt1;
      this.right = paramInt3;
      this.top = paramInt2;
      this.bottom = paramInt4;
      int j = this.top;
      this.mCollapsedCardsTop = j;
      int k = 0;
      label275:
      if (k < this.mViews.size())
      {
        View localView = (View)this.mViews.get(k);
        if (localView.getVisibility() == 8) {}
        label151:
        label301:
        for (;;)
        {
          k++;
          break;
          float f;
          if (!this.mExpanded)
          {
            if (this.mNonDismissedChildCount != 1) {
              break label251;
            }
            f = 0.0F;
            label103:
            i = (int)((float)Math.pow(f, 2.0D) * this.mTotalCollapsedSpacing);
            j = i + this.top;
            if (k > 0) {
              j -= this.mPaddingAdjustment;
            }
          }
          int m = k + 1;
          int n = this.mViews.size();
          int i1 = 0;
          if (m < n)
          {
            if (((View)this.mViews.get(m)).getVisibility() != 8) {
              i1 = 1;
            }
          }
          else
          {
            if (i1 == 0) {
              break label275;
            }
            localView.layout(this.left, j, this.right, j + this.mChildHeight);
          }
          for (;;)
          {
            if (!this.mExpanded) {
              break label301;
            }
            j += i;
            if (k != 0) {
              break;
            }
            j -= this.mPaddingAdjustment;
            break;
            label251:
            f = 1.0F * k / (-1 + this.mNonDismissedChildCount);
            break label103;
            m++;
            break label151;
            this.mCollapsedCardsBottom = j;
            localView.layout(this.left, j, this.right, this.bottom);
          }
        }
      }
    }
    
    public void onMeasure(int paramInt1, int paramInt2)
    {
      int i = View.MeasureSpec.getMode(paramInt2);
      int j = View.MeasureSpec.getSize(paramInt2);
      int k = View.MeasureSpec.getSize(paramInt1);
      if (this.mNonDismissedChildCount == 0)
      {
        setMeasuredDimension(k, 0);
        return;
      }
      int i4;
      int i5;
      int i6;
      label87:
      View localView1;
      if (i == 1073741824)
      {
        i4 = j;
        this.mChildHeight = (j - this.mTotalCollapsedSpacing);
        this.mPaddingAdjustment = 0;
        this.mExpandedOverlapAmount = this.mMaxExpandedOverlapAmount;
        i5 = View.MeasureSpec.makeMeasureSpec(this.mChildHeight + this.mPaddingAdjustment, 1073741824);
        i6 = 0;
        if (i6 >= this.mViews.size()) {
          break label422;
        }
        localView1 = (View)this.mViews.get(i6);
        if (localView1.getVisibility() != 8) {
          break label384;
        }
      }
      for (;;)
      {
        i6++;
        break label87;
        int m;
        int n;
        label176:
        View localView2;
        if (i == -2147483648)
        {
          this.mChildHeight = getTallestChildHeight(paramInt1, View.MeasureSpec.makeMeasureSpec(j - this.mMaxExpandedOverlapAmount * (-1 + this.mNonDismissedChildCount), -2147483648));
          this.mPaddingAdjustment = 0;
          m = 0;
          n = 0;
          if (n >= this.mViews.size()) {
            break label296;
          }
          localView2 = (View)this.mViews.get(n);
          if (localView2.getVisibility() != 8) {
            break label235;
          }
        }
        for (;;)
        {
          n++;
          break label176;
          this.mChildHeight = getTallestChildHeight(paramInt1, View.MeasureSpec.makeMeasureSpec(0, 0));
          break;
          label235:
          Drawable localDrawable = unwrapCardView(localView2).getBackground();
          if (localDrawable != null)
          {
            localDrawable.getPadding(this.mTmpClipRect);
            this.mPaddingAdjustment = Math.max(this.mPaddingAdjustment, this.mTmpClipRect.top);
            m = Math.max(m, this.mTmpClipRect.bottom);
          }
        }
        label296:
        if (this.mExpanded)
        {
          this.mExpandedOverlapAmount = Math.min(this.mChildHeight - m, this.mMaxExpandedOverlapAmount);
          i4 = this.mChildHeight + this.mExpandedOverlapAmount * (-1 + this.mNonDismissedChildCount);
          break;
        }
        int i1 = this.mChildHeight;
        int i2 = this.mTotalCollapsedSpacing;
        if (this.mNonDismissedChildCount > 1) {}
        for (int i3 = 1;; i3 = 0)
        {
          i4 = i1 + i3 * i2;
          break;
        }
        label384:
        if ((localView1 instanceof SuggestionGridLayout.CardWrapper)) {
          ((SuggestionGridLayout.CardWrapper)localView1).setForcedCardHeight(this.mChildHeight + this.mPaddingAdjustment);
        }
        localView1.measure(paramInt1, i5);
      }
      label422:
      setMeasuredDimension(k, i4);
    }
    
    public void removeView(View paramView)
    {
      this.mViews.remove(paramView);
      if (paramView.getVisibility() != 8) {
        this.mNonDismissedChildCount = (-1 + this.mNonDismissedChildCount);
      }
      update();
    }
    
    void setDisableClipping(boolean paramBoolean)
    {
      this.mDisableClipping = paramBoolean;
    }
    
    public void setDismissed(View paramView, boolean paramBoolean)
    {
      int i = 0;
      int j = 0;
      if (j < this.mViews.size())
      {
        View localView = (View)this.mViews.get(j);
        if (localView == paramView) {
          if (!paramBoolean) {
            break label71;
          }
        }
        label71:
        for (int k = 8;; k = 0)
        {
          localView.setVisibility(k);
          if (localView.getVisibility() != 8) {
            i++;
          }
          j++;
          break;
        }
      }
      this.mNonDismissedChildCount = i;
    }
    
    void setExpanded(boolean paramBoolean)
    {
      if (!isCollapsible()) {
        paramBoolean = true;
      }
      if (paramBoolean != this.mExpanded)
      {
        this.mExpanded = paramBoolean;
        setDisableClipping(true);
        SuggestionGridLayout.this.requestLayout();
      }
      SuggestionGridLayout.this.postDelayed(new Runnable()
      {
        public void run()
        {
          SuggestionGridLayout.StackGridItem.this.setDisableClipping(false);
        }
      }, 300L);
    }
    
    public void setInvisible(boolean paramBoolean)
    {
      int i = 0;
      if (i < this.mViews.size())
      {
        View localView = (View)this.mViews.get(i);
        if (localView.getVisibility() != 8) {
          if (!paramBoolean) {
            break label53;
          }
        }
        label53:
        for (int j = 4;; j = 0)
        {
          localView.setVisibility(j);
          i++;
          break;
        }
      }
    }
    
    void setMeasuredDimension(int paramInt1, int paramInt2)
    {
      this.mMeasuredWidth = paramInt1;
      this.mMeasuredHeight = paramInt2;
    }
    
    public void update()
    {
      if ((!isCollapsible()) && (this.mNonDismissedChildCount > 0)) {
        setExpanded(true);
      }
      updateItemBackgrounds();
      updateTotalCollapsedSpacing();
    }
    
    void updateTotalCollapsedSpacing()
    {
      this.mTotalCollapsedSpacing = ((int)(115.0F * this.mDensity));
      float f = Math.min(1.0F, 1.0F * Math.max(0, -2 + this.mNonDismissedChildCount) / 3.0F);
      this.mTotalCollapsedSpacing = ((int)(this.mTotalCollapsedSpacing / 2 * (1.0F + f)));
    }
  }
  
  private static class StackOrderComparator
    implements Comparator<View>
  {
    public int compare(View paramView1, View paramView2)
    {
      if ((!(paramView1 instanceof SuggestionGridLayout.CardWrapper)) || (!(paramView2 instanceof SuggestionGridLayout.CardWrapper))) {
        return 0;
      }
      return ((SuggestionGridLayout.CardWrapper)paramView1).getStackOrderOverride() - ((SuggestionGridLayout.CardWrapper)paramView2).getStackOrderOverride();
    }
  }
  
  private class SwipeCallback
    implements SwipeHelper.Callback
  {
    private SwipeCallback() {}
    
    public boolean canChildBeDismissed(View paramView)
    {
      if (paramView.getParent() == null) {}
      SuggestionGridLayout.GridItem localGridItem;
      do
      {
        return false;
        if (SuggestionGridLayout.this.mDragImageView == paramView) {
          return true;
        }
        localGridItem = SuggestionGridLayout.this.getGridItemForView(paramView);
      } while (localGridItem == null);
      return localGridItem.getGridLayoutParams().canDismiss;
    }
    
    void dragEnd(View paramView)
    {
      if (paramView == SuggestionGridLayout.this.mDragImageView)
      {
        SuggestionGridLayout.this.mDragStack.setInvisible(false);
        SuggestionGridLayout.this.mDragStack.enableAnimation(true);
        SuggestionGridLayout.this.mDragImageView.setVisibility(4);
        SuggestionGridLayout.this.mAnimatingViews.remove(SuggestionGridLayout.this.mDragImageView);
        SuggestionGridLayout.access$602(SuggestionGridLayout.this, null);
      }
      for (;;)
      {
        SuggestionGridLayout.access$302(SuggestionGridLayout.this, false);
        paramView.setLayerType(0, null);
        SuggestionGridLayout.this.invalidate();
        return;
        SuggestionGridLayout.GridItem localGridItem = SuggestionGridLayout.this.getGridItemForView(paramView);
        if (localGridItem != null) {
          SuggestionGridLayout.this.mAnimatingViews.removeAll(localGridItem.getViews());
        }
      }
    }
    
    public View getChildAtPosition(MotionEvent paramMotionEvent)
    {
      View localView;
      if (SuggestionGridLayout.this.mIsDragging) {
        localView = null;
      }
      SuggestionGridLayout.StackGridItem localStackGridItem;
      do
      {
        SuggestionGridLayout.GridItem localGridItem;
        do
        {
          return localView;
          int i = (int)(0.5F + paramMotionEvent.getX());
          int j = (int)(0.5F + paramMotionEvent.getY());
          int k = -1 + SuggestionGridLayout.this.getChildCount();
          if (k < 0) {
            break;
          }
          localView = SuggestionGridLayout.this.getChildAt(k);
          if (localView.getVisibility() != 0) {}
          do
          {
            do
            {
              k--;
              break;
              localView.getHitRect(SuggestionGridLayout.this.mHitRect);
            } while ((!SuggestionGridLayout.this.mHitRect.contains(i, j)) || (((localView instanceof SuggestionGridLayout.DismissableChildContainer)) && (((SuggestionGridLayout.DismissableChildContainer)localView).isDismissableViewAtPosition(SuggestionGridLayout.this, paramMotionEvent))));
            localGridItem = SuggestionGridLayout.this.getGridItemForView(localView);
          } while ((localGridItem == null) || (!localGridItem.getGridLayoutParams().canDrag));
        } while ((localGridItem instanceof SuggestionGridLayout.SimpleGridItem));
        localStackGridItem = (SuggestionGridLayout.StackGridItem)localGridItem;
      } while ((localStackGridItem.isExpanded()) || (localStackGridItem.isTopCard(localView)));
      SuggestionGridLayout.access$602(SuggestionGridLayout.this, localStackGridItem);
      SuggestionGridLayout.this.setupDragImage(localStackGridItem);
      return SuggestionGridLayout.this.mDragImageView;
      return null;
    }
    
    public void onBeginDrag(View paramView)
    {
      SuggestionGridLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
      if (paramView == SuggestionGridLayout.this.mDragImageView)
      {
        SuggestionGridLayout.this.mDragImageView.setVisibility(0);
        SuggestionGridLayout.this.mDragStack.enableAnimation(false);
        SuggestionGridLayout.this.mDragStack.setInvisible(true);
        SuggestionGridLayout.this.mAnimatingViews.add(SuggestionGridLayout.this.mDragImageView);
      }
      for (;;)
      {
        SuggestionGridLayout.access$302(SuggestionGridLayout.this, true);
        SuggestionGridLayout.this.invalidate();
        return;
        SuggestionGridLayout.GridItem localGridItem = SuggestionGridLayout.this.getGridItemForView(paramView);
        if (localGridItem != null) {
          SuggestionGridLayout.this.mAnimatingViews.addAll(localGridItem.getViews());
        }
        paramView.setLayerType(2, null);
      }
    }
    
    public void onChildDismissed(View paramView)
    {
      SuggestionGridLayout.PendingViewDismissImpl localPendingViewDismissImpl;
      if (paramView == SuggestionGridLayout.this.mDragImageView)
      {
        SuggestionGridLayout.this.mDragImageView.setVisibility(4);
        SuggestionGridLayout.this.mAnimatingViews.remove(paramView);
        SuggestionGridLayout.StackGridItem localStackGridItem = SuggestionGridLayout.this.mDragStack;
        localPendingViewDismissImpl = null;
        if (localStackGridItem != null)
        {
          localPendingViewDismissImpl = new SuggestionGridLayout.PendingViewDismissImpl(SuggestionGridLayout.this, SuggestionGridLayout.this.mDragStack, SuggestionGridLayout.this.mDragStack.getViews());
          SuggestionGridLayout.access$602(SuggestionGridLayout.this, null);
        }
      }
      while (localPendingViewDismissImpl != null)
      {
        SuggestionGridLayout.this.mAnimatingViews.removeAll(localPendingViewDismissImpl.mGridItem.getViews());
        if (localPendingViewDismissImpl.mGridItem.getGridLayoutParams().removeOnDismiss)
        {
          Iterator localIterator = localPendingViewDismissImpl.getDismissedViews().iterator();
          for (;;)
          {
            if (localIterator.hasNext())
            {
              View localView = (View)localIterator.next();
              localPendingViewDismissImpl.mGridItem.setDismissed(localView, true);
              continue;
              SuggestionGridLayout.GridItem localGridItem = SuggestionGridLayout.this.getGridItemForView(paramView);
              localPendingViewDismissImpl = null;
              if (localGridItem == null) {
                break;
              }
              localPendingViewDismissImpl = new SuggestionGridLayout.PendingViewDismissImpl(SuggestionGridLayout.this, localGridItem, paramView);
              break;
            }
          }
          localPendingViewDismissImpl.mGridItem.update();
        }
        if (SuggestionGridLayout.this.mOnDismissListener != null) {
          SuggestionGridLayout.this.mOnDismissListener.onViewsDismissed(localPendingViewDismissImpl);
        }
        if (!localPendingViewDismissImpl.isIntercepted()) {
          localPendingViewDismissImpl.commit();
        }
      }
      SuggestionGridLayout.access$302(SuggestionGridLayout.this, false);
      SuggestionGridLayout.this.invalidate();
    }
    
    public void onDragCancelled(View paramView) {}
    
    public void onSnapBackCompleted(View paramView)
    {
      dragEnd(paramView);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionGridLayout
 * JD-Core Version:    0.7.0.1
 */