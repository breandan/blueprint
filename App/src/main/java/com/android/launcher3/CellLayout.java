package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.DecelerateInterpolator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class CellLayout
  extends ViewGroup
{
  private static final PorterDuffXfermode sAddBlendMode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
  private static final Paint sPaint = new Paint();
  private float FOREGROUND_ALPHA_DAMPER = 0.65F;
  private Drawable mActiveGlowBackground;
  private float mBackgroundAlpha;
  private float mBackgroundAlphaMultiplier = 1.0F;
  private Rect mBackgroundRect;
  private int mCellHeight;
  private final CellInfo mCellInfo = new CellInfo();
  private int mCellWidth;
  private int mCountX;
  private int mCountY;
  private int[] mDirectionVector = new int[2];
  private final int[] mDragCell = new int[2];
  private DropTarget.DragEnforcer mDragEnforcer;
  private float[] mDragOutlineAlphas = new float[this.mDragOutlines.length];
  private InterruptibleInOutAnimator[] mDragOutlineAnims = new InterruptibleInOutAnimator[this.mDragOutlines.length];
  private int mDragOutlineCurrent = 0;
  private final Paint mDragOutlinePaint = new Paint();
  private Rect[] mDragOutlines = new Rect[4];
  private boolean mDragging = false;
  private boolean mDropPending = false;
  private TimeInterpolator mEaseOutInterpolator;
  private int mFixedCellHeight;
  private int mFixedCellWidth;
  private int mFixedHeight = -1;
  private int mFixedWidth = -1;
  private int[] mFolderLeaveBehindCell = { -1, -1 };
  private ArrayList<FolderIcon.FolderRingAnimator> mFolderOuterRings = new ArrayList();
  private int mForegroundAlpha = 0;
  private int mForegroundPadding;
  private Rect mForegroundRect;
  private int mHeightGap;
  private float mHotseatScale = 1.0F;
  private View.OnTouchListener mInterceptTouchListener;
  private ArrayList<View> mIntersectingViews = new ArrayList();
  private boolean mIsDragOverlapping = false;
  private boolean mIsHotseat = false;
  private boolean mItemPlacementDirty = false;
  private boolean mLastDownOnOccupiedCell = false;
  private Launcher mLauncher;
  private int mMaxGap;
  private Drawable mNormalBackground;
  boolean[][] mOccupied;
  private Rect mOccupiedRect = new Rect();
  private int mOriginalHeightGap;
  private int mOriginalWidthGap;
  private Drawable mOverScrollForegroundDrawable;
  private Drawable mOverScrollLeft;
  private Drawable mOverScrollRight;
  private BubbleTextView mPressedOrFocusedIcon;
  int[] mPreviousReorderDirection = new int[2];
  private final Rect mRect = new Rect();
  private HashMap<LayoutParams, Animator> mReorderAnimators = new HashMap();
  private float mReorderHintAnimationMagnitude;
  private boolean mScrollingTransformsDirty = false;
  private HashMap<View, ReorderHintAnimation> mShakeAnimators = new HashMap();
  private ShortcutAndWidgetContainer mShortcutsAndWidgets;
  int[] mTempLocation = new int[2];
  private Rect mTempRect = new Rect();
  private final Stack<Rect> mTempRectStack = new Stack();
  boolean[][] mTmpOccupied;
  private final int[] mTmpPoint = new int[2];
  private final int[] mTmpXY = new int[2];
  boolean mUseActiveGlowBackground = false;
  private int mWidthGap;
  
  public CellLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CellLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CellLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mDragEnforcer = new DropTarget.DragEnforcer(paramContext);
    setWillNotDraw(false);
    setClipToPadding(false);
    this.mLauncher = ((Launcher)paramContext);
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CellLayout, paramInt, 0);
    this.mCellHeight = -1;
    this.mCellWidth = -1;
    this.mFixedCellHeight = -1;
    this.mFixedCellHeight = -1;
    this.mOriginalWidthGap = 0;
    this.mWidthGap = 0;
    this.mOriginalHeightGap = 0;
    this.mHeightGap = 0;
    this.mMaxGap = 2147483647;
    this.mCountX = ((int)localDeviceProfile.numColumns);
    this.mCountY = ((int)localDeviceProfile.numRows);
    int[] arrayOfInt1 = { this.mCountX, this.mCountY };
    this.mOccupied = ((boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt1));
    int[] arrayOfInt2 = { this.mCountX, this.mCountY };
    this.mTmpOccupied = ((boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt2));
    this.mPreviousReorderDirection[0] = -100;
    this.mPreviousReorderDirection[1] = -100;
    localTypedArray.recycle();
    setAlwaysDrawnWithCacheEnabled(false);
    Resources localResources = getResources();
    this.mHotseatScale = (localDeviceProfile.hotseatIconSizePx / localDeviceProfile.iconSizePx);
    this.mNormalBackground = localResources.getDrawable(2130838035);
    this.mActiveGlowBackground = localResources.getDrawable(2130838036);
    this.mOverScrollLeft = localResources.getDrawable(2130838008);
    this.mOverScrollRight = localResources.getDrawable(2130838009);
    this.mForegroundPadding = localResources.getDimensionPixelSize(2131689525);
    this.mReorderHintAnimationMagnitude = (0.12F * localDeviceProfile.iconSizePx);
    this.mNormalBackground.setFilterBitmap(true);
    this.mActiveGlowBackground.setFilterBitmap(true);
    this.mEaseOutInterpolator = new DecelerateInterpolator(2.5F);
    int[] arrayOfInt3 = this.mDragCell;
    this.mDragCell[1] = -1;
    arrayOfInt3[0] = -1;
    for (int i = 0;; i++)
    {
      int j = this.mDragOutlines.length;
      if (i >= j) {
        break;
      }
      this.mDragOutlines[i] = new Rect(-1, -1, -1, -1);
    }
    int k = localResources.getInteger(2131427351);
    float f = localResources.getInteger(2131427352);
    Arrays.fill(this.mDragOutlineAlphas, 0.0F);
    for (int m = 0;; m++)
    {
      int n = this.mDragOutlineAnims.length;
      if (m >= n) {
        break;
      }
      final InterruptibleInOutAnimator localInterruptibleInOutAnimator = new InterruptibleInOutAnimator(this, k, 0.0F, f);
      localInterruptibleInOutAnimator.getAnimator().setInterpolator(this.mEaseOutInterpolator);
      final int i1 = m;
      localInterruptibleInOutAnimator.getAnimator().addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          if ((Bitmap)localInterruptibleInOutAnimator.getTag() == null)
          {
            paramAnonymousValueAnimator.cancel();
            return;
          }
          CellLayout.this.mDragOutlineAlphas[i1] = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          CellLayout.this.invalidate(CellLayout.this.mDragOutlines[i1]);
        }
      });
      localInterruptibleInOutAnimator.getAnimator().addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          if (((Float)((ValueAnimator)paramAnonymousAnimator).getAnimatedValue()).floatValue() == 0.0F) {
            localInterruptibleInOutAnimator.setTag(null);
          }
        }
      });
      this.mDragOutlineAnims[m] = localInterruptibleInOutAnimator;
    }
    this.mBackgroundRect = new Rect();
    this.mForegroundRect = new Rect();
    this.mShortcutsAndWidgets = new ShortcutAndWidgetContainer(paramContext);
    this.mShortcutsAndWidgets.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mCountX, this.mCountY);
    addView(this.mShortcutsAndWidgets);
  }
  
  private boolean addViewToTempLocation(View paramView, Rect paramRect, int[] paramArrayOfInt, ItemConfiguration paramItemConfiguration)
  {
    CellAndSpan localCellAndSpan = (CellAndSpan)paramItemConfiguration.map.get(paramView);
    markCellsForView(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY, this.mTmpOccupied, false);
    markCellsForRect(paramRect, this.mTmpOccupied, true);
    findNearestArea(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY, paramArrayOfInt, this.mTmpOccupied, (boolean[][])null, this.mTempLocation);
    int i = this.mTempLocation[0];
    boolean bool = false;
    if (i >= 0)
    {
      int j = this.mTempLocation[1];
      bool = false;
      if (j >= 0)
      {
        localCellAndSpan.x = this.mTempLocation[0];
        localCellAndSpan.y = this.mTempLocation[1];
        bool = true;
      }
    }
    markCellsForView(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY, this.mTmpOccupied, true);
    return bool;
  }
  
  private boolean addViewsToTempLocation(ArrayList<View> paramArrayList, Rect paramRect, int[] paramArrayOfInt, View paramView, ItemConfiguration paramItemConfiguration)
  {
    boolean bool;
    if (paramArrayList.size() == 0) {
      bool = true;
    }
    for (;;)
    {
      return bool;
      Rect localRect = null;
      Iterator localIterator1 = paramArrayList.iterator();
      while (localIterator1.hasNext())
      {
        View localView5 = (View)localIterator1.next();
        CellAndSpan localCellAndSpan5 = (CellAndSpan)paramItemConfiguration.map.get(localView5);
        if (localRect == null)
        {
          int i6 = localCellAndSpan5.x;
          int i7 = localCellAndSpan5.y;
          int i8 = localCellAndSpan5.x + localCellAndSpan5.spanX;
          int i9 = localCellAndSpan5.y + localCellAndSpan5.spanY;
          localRect = new Rect(i6, i7, i8, i9);
        }
        else
        {
          int i2 = localCellAndSpan5.x;
          int i3 = localCellAndSpan5.y;
          int i4 = localCellAndSpan5.x + localCellAndSpan5.spanX;
          int i5 = localCellAndSpan5.y + localCellAndSpan5.spanY;
          localRect.union(i2, i3, i4, i5);
        }
      }
      Iterator localIterator2 = paramArrayList.iterator();
      while (localIterator2.hasNext())
      {
        View localView4 = (View)localIterator2.next();
        CellAndSpan localCellAndSpan4 = (CellAndSpan)paramItemConfiguration.map.get(localView4);
        markCellsForView(localCellAndSpan4.x, localCellAndSpan4.y, localCellAndSpan4.spanX, localCellAndSpan4.spanY, this.mTmpOccupied, false);
      }
      int[] arrayOfInt = { localRect.width(), localRect.height() };
      boolean[][] arrayOfBoolean = (boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt);
      int i = localRect.top;
      int j = localRect.left;
      Iterator localIterator3 = paramArrayList.iterator();
      while (localIterator3.hasNext())
      {
        View localView3 = (View)localIterator3.next();
        CellAndSpan localCellAndSpan3 = (CellAndSpan)paramItemConfiguration.map.get(localView3);
        markCellsForView(localCellAndSpan3.x - j, localCellAndSpan3.y - i, localCellAndSpan3.spanX, localCellAndSpan3.spanY, arrayOfBoolean, true);
      }
      markCellsForRect(paramRect, this.mTmpOccupied, true);
      findNearestArea(localRect.left, localRect.top, localRect.width(), localRect.height(), paramArrayOfInt, this.mTmpOccupied, arrayOfBoolean, this.mTempLocation);
      int k = this.mTempLocation[0];
      bool = false;
      if (k >= 0)
      {
        int m = this.mTempLocation[1];
        bool = false;
        if (m >= 0)
        {
          int n = this.mTempLocation[0] - localRect.left;
          int i1 = this.mTempLocation[1] - localRect.top;
          Iterator localIterator5 = paramArrayList.iterator();
          while (localIterator5.hasNext())
          {
            View localView2 = (View)localIterator5.next();
            CellAndSpan localCellAndSpan2 = (CellAndSpan)paramItemConfiguration.map.get(localView2);
            localCellAndSpan2.x = (n + localCellAndSpan2.x);
            localCellAndSpan2.y = (i1 + localCellAndSpan2.y);
          }
          bool = true;
        }
      }
      Iterator localIterator4 = paramArrayList.iterator();
      while (localIterator4.hasNext())
      {
        View localView1 = (View)localIterator4.next();
        CellAndSpan localCellAndSpan1 = (CellAndSpan)paramItemConfiguration.map.get(localView1);
        markCellsForView(localCellAndSpan1.x, localCellAndSpan1.y, localCellAndSpan1.spanX, localCellAndSpan1.spanY, this.mTmpOccupied, true);
      }
    }
  }
  
  private void animateItemsToSolution(ItemConfiguration paramItemConfiguration, View paramView, boolean paramBoolean)
  {
    boolean[][] arrayOfBoolean = (boolean[][])this.mTmpOccupied;
    for (int i = 0; i < this.mCountX; i++) {
      for (int m = 0; m < this.mCountY; m++) {
        arrayOfBoolean[i][m] = 0;
      }
    }
    int j = this.mShortcutsAndWidgets.getChildCount();
    int k = 0;
    if (k < j)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(k);
      if (localView == paramView) {}
      for (;;)
      {
        k++;
        break;
        CellAndSpan localCellAndSpan = (CellAndSpan)paramItemConfiguration.map.get(localView);
        if (localCellAndSpan != null)
        {
          animateChildToPosition(localView, localCellAndSpan.x, localCellAndSpan.y, 150, 0, false, false);
          markCellsForView(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY, arrayOfBoolean, true);
        }
      }
    }
    if (paramBoolean) {
      markCellsForView(paramItemConfiguration.dragViewX, paramItemConfiguration.dragViewY, paramItemConfiguration.dragViewSpanX, paramItemConfiguration.dragViewSpanY, arrayOfBoolean, true);
    }
  }
  
  private boolean attemptPushInDirection(ArrayList<View> paramArrayList, Rect paramRect, int[] paramArrayOfInt, View paramView, ItemConfiguration paramItemConfiguration)
  {
    if (Math.abs(paramArrayOfInt[0]) + Math.abs(paramArrayOfInt[1]) > 1)
    {
      int k = paramArrayOfInt[1];
      paramArrayOfInt[1] = 0;
      if (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration)) {}
      int i1;
      do
      {
        int n;
        do
        {
          int m;
          do
          {
            return true;
            paramArrayOfInt[1] = k;
            m = paramArrayOfInt[0];
            paramArrayOfInt[0] = 0;
          } while (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration));
          paramArrayOfInt[0] = m;
          paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
          paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
          n = paramArrayOfInt[1];
          paramArrayOfInt[1] = 0;
        } while (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration));
        paramArrayOfInt[1] = n;
        i1 = paramArrayOfInt[0];
        paramArrayOfInt[0] = 0;
      } while (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration));
      paramArrayOfInt[0] = i1;
      paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
      paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
    }
    for (;;)
    {
      return false;
      if (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
        break;
      }
      paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
      paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
      if (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
        break;
      }
      paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
      paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
      int i = paramArrayOfInt[1];
      paramArrayOfInt[1] = paramArrayOfInt[0];
      paramArrayOfInt[0] = i;
      if (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
        break;
      }
      paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
      paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
      if (pushViewsToTempLocation(paramArrayList, paramRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
        break;
      }
      paramArrayOfInt[0] = (-1 * paramArrayOfInt[0]);
      paramArrayOfInt[1] = (-1 * paramArrayOfInt[1]);
      int j = paramArrayOfInt[1];
      paramArrayOfInt[1] = paramArrayOfInt[0];
      paramArrayOfInt[0] = j;
    }
  }
  
  private void beginOrAdjustHintAnimations(ItemConfiguration paramItemConfiguration, View paramView, int paramInt)
  {
    int i = this.mShortcutsAndWidgets.getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(j);
      if (localView == paramView) {}
      for (;;)
      {
        j++;
        break;
        CellAndSpan localCellAndSpan = (CellAndSpan)paramItemConfiguration.map.get(localView);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (localCellAndSpan != null) {
          new ReorderHintAnimation(localView, localLayoutParams.cellX, localLayoutParams.cellY, localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY).animate();
        }
      }
    }
  }
  
  private void clearOccupiedCells()
  {
    for (int i = 0; i < this.mCountX; i++) {
      for (int j = 0; j < this.mCountY; j++) {
        this.mOccupied[i][j] = 0;
      }
    }
  }
  
  private void clearTagCellInfo()
  {
    CellInfo localCellInfo = this.mCellInfo;
    localCellInfo.cell = null;
    localCellInfo.cellX = -1;
    localCellInfo.cellY = -1;
    localCellInfo.spanX = 0;
    localCellInfo.spanY = 0;
    setTag(localCellInfo);
  }
  
  private void commitTempPlacement()
  {
    for (int i = 0; i < this.mCountX; i++) {
      for (int i1 = 0; i1 < this.mCountY; i1++) {
        this.mOccupied[i][i1] = this.mTmpOccupied[i][i1];
      }
    }
    int j = this.mShortcutsAndWidgets.getChildCount();
    for (int k = 0; k < j; k++)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(k);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      ItemInfo localItemInfo = (ItemInfo)localView.getTag();
      if (localItemInfo != null)
      {
        if ((localItemInfo.cellX != localLayoutParams.tmpCellX) || (localItemInfo.cellY != localLayoutParams.tmpCellY) || (localItemInfo.spanX != localLayoutParams.cellHSpan) || (localItemInfo.spanY != localLayoutParams.cellVSpan)) {
          localItemInfo.requiresDbUpdate = true;
        }
        int m = localLayoutParams.tmpCellX;
        localLayoutParams.cellX = m;
        localItemInfo.cellX = m;
        int n = localLayoutParams.tmpCellY;
        localLayoutParams.cellY = n;
        localItemInfo.cellY = n;
        localItemInfo.spanX = localLayoutParams.cellHSpan;
        localItemInfo.spanY = localLayoutParams.cellVSpan;
      }
    }
    this.mLauncher.getWorkspace().updateItemLocationsInDatabase(this);
  }
  
  private void completeAndClearReorderHintAnimations()
  {
    Iterator localIterator = this.mShakeAnimators.values().iterator();
    while (localIterator.hasNext()) {
      ((ReorderHintAnimation)localIterator.next()).completeAnimationImmediately();
    }
    this.mShakeAnimators.clear();
  }
  
  private void computeDirectionVector(float paramFloat1, float paramFloat2, int[] paramArrayOfInt)
  {
    double d = Math.atan(paramFloat2 / paramFloat1);
    paramArrayOfInt[0] = 0;
    paramArrayOfInt[1] = 0;
    if (Math.abs(Math.cos(d)) > 0.5D) {
      paramArrayOfInt[0] = ((int)Math.signum(paramFloat1));
    }
    if (Math.abs(Math.sin(d)) > 0.5D) {
      paramArrayOfInt[1] = ((int)Math.signum(paramFloat2));
    }
  }
  
  private void copyCurrentStateToSolution(ItemConfiguration paramItemConfiguration, boolean paramBoolean)
  {
    int i = this.mShortcutsAndWidgets.getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(j);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (paramBoolean) {}
      for (CellAndSpan localCellAndSpan = new CellAndSpan(localLayoutParams.tmpCellX, localLayoutParams.tmpCellY, localLayoutParams.cellHSpan, localLayoutParams.cellVSpan);; localCellAndSpan = new CellAndSpan(localLayoutParams.cellX, localLayoutParams.cellY, localLayoutParams.cellHSpan, localLayoutParams.cellVSpan))
      {
        paramItemConfiguration.add(localView, localCellAndSpan);
        j++;
        break;
      }
    }
  }
  
  private void copyOccupiedArray(boolean[][] paramArrayOfBoolean)
  {
    for (int i = 0; i < this.mCountX; i++) {
      for (int j = 0; j < this.mCountY; j++) {
        paramArrayOfBoolean[i][j] = this.mOccupied[i][j];
      }
    }
  }
  
  private void copySolutionToTempState(ItemConfiguration paramItemConfiguration, View paramView)
  {
    for (int i = 0; i < this.mCountX; i++) {
      for (int m = 0; m < this.mCountY; m++) {
        this.mTmpOccupied[i][m] = 0;
      }
    }
    int j = this.mShortcutsAndWidgets.getChildCount();
    int k = 0;
    if (k < j)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(k);
      if (localView == paramView) {}
      for (;;)
      {
        k++;
        break;
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        CellAndSpan localCellAndSpan = (CellAndSpan)paramItemConfiguration.map.get(localView);
        if (localCellAndSpan != null)
        {
          localLayoutParams.tmpCellX = localCellAndSpan.x;
          localLayoutParams.tmpCellY = localCellAndSpan.y;
          localLayoutParams.cellHSpan = localCellAndSpan.spanX;
          localLayoutParams.cellVSpan = localCellAndSpan.spanY;
          markCellsForView(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.spanX, localCellAndSpan.spanY, this.mTmpOccupied, true);
        }
      }
    }
    markCellsForView(paramItemConfiguration.dragViewX, paramItemConfiguration.dragViewY, paramItemConfiguration.dragViewSpanX, paramItemConfiguration.dragViewSpanY, this.mTmpOccupied, true);
  }
  
  private int[] findNearestArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, boolean[][] paramArrayOfBoolean1, boolean[][] paramArrayOfBoolean2, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt1;
    float f1;
    int i;
    int j;
    int k;
    if (paramArrayOfInt2 != null)
    {
      arrayOfInt1 = paramArrayOfInt2;
      f1 = 3.4028235E+38F;
      i = -2147483648;
      j = this.mCountX;
      k = this.mCountY;
    }
    label299:
    for (int m = 0;; m++)
    {
      int n = k - (paramInt4 - 1);
      if (m >= n) {
        break label307;
      }
      int i1 = 0;
      label53:
      if (i1 < j - (paramInt3 - 1))
      {
        int i2 = 0;
        label67:
        int i5;
        if (i2 < paramInt3)
        {
          i5 = 0;
          label76:
          if (i5 < paramInt4) {
            if ((paramArrayOfBoolean1[(i1 + i2)][(m + i5)] == 0) || ((paramArrayOfBoolean2 != null) && (paramArrayOfBoolean2[i2][i5] == 0))) {}
          }
        }
        for (;;)
        {
          i1++;
          break label53;
          arrayOfInt1 = new int[2];
          break;
          i5++;
          break label76;
          i2++;
          break label67;
          float f2 = (float)Math.sqrt((i1 - paramInt1) * (i1 - paramInt1) + (m - paramInt2) * (m - paramInt2));
          int[] arrayOfInt2 = this.mTmpPoint;
          computeDirectionVector(i1 - paramInt1, m - paramInt2, arrayOfInt2);
          int i3 = paramArrayOfInt1[0] * arrayOfInt2[0] + paramArrayOfInt1[1] * arrayOfInt2[1];
          if ((paramArrayOfInt1[0] == arrayOfInt2[0]) && (paramArrayOfInt1[0] == arrayOfInt2[0])) {}
          for (int i4 = 1;; i4 = 0)
          {
            if (((i4 != 0) || (0 == 0)) && ((Float.compare(f2, f1) >= 0) && ((Float.compare(f2, f1) != 0) || (i3 <= i)))) {
              break label299;
            }
            f1 = f2;
            i = i3;
            arrayOfInt1[0] = i1;
            arrayOfInt1[1] = m;
            break;
          }
        }
      }
    }
    label307:
    if (f1 == 3.4028235E+38F)
    {
      arrayOfInt1[0] = -1;
      arrayOfInt1[1] = -1;
    }
    return arrayOfInt1;
  }
  
  static boolean findVacantCell(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean[][] paramArrayOfBoolean)
  {
    for (int i = 0; i < paramInt4; i++) {
      label129:
      label135:
      label141:
      for (int j = 0; j < paramInt3; j++)
      {
        int k;
        if (paramArrayOfBoolean[j][i] == 0) {
          k = 1;
        }
        for (int m = j;; m++)
        {
          if ((m < -1 + (j + paramInt1)) && (j < paramInt3)) {}
          for (int n = i;; n++)
          {
            if ((n >= -1 + (i + paramInt2)) || (i >= paramInt4)) {
              break label135;
            }
            if ((k != 0) && (paramArrayOfBoolean[m][n] == 0)) {}
            for (k = 1;; k = 0)
            {
              if (k != 0) {
                break label129;
              }
              if (k == 0) {
                break label141;
              }
              paramArrayOfInt[0] = j;
              paramArrayOfInt[1] = i;
              return true;
              k = 0;
              break;
            }
          }
        }
      }
    }
    return false;
  }
  
  private void getDirectionVectorForDrop(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, int[] paramArrayOfInt)
  {
    int[] arrayOfInt = new int[2];
    findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt);
    Rect localRect1 = new Rect();
    regionToRect(arrayOfInt[0], arrayOfInt[1], paramInt3, paramInt4, localRect1);
    localRect1.offset(paramInt1 - localRect1.centerX(), paramInt2 - localRect1.centerY());
    Rect localRect2 = new Rect();
    getViewsIntersectingRegion(arrayOfInt[0], arrayOfInt[1], paramInt3, paramInt4, paramView, localRect2, this.mIntersectingViews);
    int i = localRect2.width();
    int j = localRect2.height();
    regionToRect(localRect2.left, localRect2.top, localRect2.width(), localRect2.height(), localRect2);
    int k = (localRect2.centerX() - paramInt1) / paramInt3;
    int m = (localRect2.centerY() - paramInt2) / paramInt4;
    if ((i == this.mCountX) || (paramInt3 == this.mCountX)) {
      k = 0;
    }
    if ((j == this.mCountY) || (paramInt4 == this.mCountY)) {
      m = 0;
    }
    if ((k == 0) && (m == 0))
    {
      paramArrayOfInt[0] = 1;
      paramArrayOfInt[1] = 0;
      return;
    }
    computeDirectionVector(k, m, paramArrayOfInt);
  }
  
  private void getViewsIntersectingRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, Rect paramRect, ArrayList<View> paramArrayList)
  {
    if (paramRect != null) {
      paramRect.set(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    }
    paramArrayList.clear();
    Rect localRect1 = new Rect(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    Rect localRect2 = new Rect();
    int i = this.mShortcutsAndWidgets.getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = this.mShortcutsAndWidgets.getChildAt(j);
      if (localView == paramView) {}
      for (;;)
      {
        j++;
        break;
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        localRect2.set(localLayoutParams.cellX, localLayoutParams.cellY, localLayoutParams.cellX + localLayoutParams.cellHSpan, localLayoutParams.cellY + localLayoutParams.cellVSpan);
        if (Rect.intersects(localRect1, localRect2))
        {
          this.mIntersectingViews.add(localView);
          if (paramRect != null) {
            paramRect.union(localRect2);
          }
        }
      }
    }
  }
  
  private void invalidateBubbleTextView(BubbleTextView paramBubbleTextView)
  {
    int i = paramBubbleTextView.getPressedOrFocusedBackgroundPadding();
    invalidate(paramBubbleTextView.getLeft() + getPaddingLeft() - i, paramBubbleTextView.getTop() + getPaddingTop() - i, i + (paramBubbleTextView.getRight() + getPaddingLeft()), i + (paramBubbleTextView.getBottom() + getPaddingTop()));
  }
  
  private void lazyInitTempRectStack()
  {
    if (this.mTempRectStack.isEmpty()) {
      for (int i = 0; i < this.mCountX * this.mCountY; i++) {
        this.mTempRectStack.push(new Rect());
      }
    }
  }
  
  private void markCellsForRect(Rect paramRect, boolean[][] paramArrayOfBoolean, boolean paramBoolean)
  {
    markCellsForView(paramRect.left, paramRect.top, paramRect.width(), paramRect.height(), paramArrayOfBoolean, paramBoolean);
  }
  
  private void markCellsForView(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean[][] paramArrayOfBoolean, boolean paramBoolean)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0)) {}
    for (;;)
    {
      return;
      for (int i = paramInt1; (i < paramInt1 + paramInt3) && (i < this.mCountX); i++) {
        for (int j = paramInt2; (j < paramInt2 + paramInt4) && (j < this.mCountY); j++) {
          paramArrayOfBoolean[i][j] = paramBoolean;
        }
      }
    }
  }
  
  private boolean pushViewsToTempLocation(ArrayList<View> paramArrayList, Rect paramRect, int[] paramArrayOfInt, View paramView, ItemConfiguration paramItemConfiguration)
  {
    ViewCluster localViewCluster = new ViewCluster(paramArrayList, paramItemConfiguration);
    Rect localRect1 = localViewCluster.getBoundingRect();
    int i = 0;
    int j;
    int k;
    if (paramArrayOfInt[0] < 0)
    {
      j = 0;
      k = localRect1.right - paramRect.left;
    }
    while (k <= 0)
    {
      bool = false;
      return bool;
      if (paramArrayOfInt[0] > 0)
      {
        j = 2;
        k = paramRect.right - localRect1.left;
      }
      else if (paramArrayOfInt[1] < 0)
      {
        j = 1;
        k = localRect1.bottom - paramRect.top;
      }
      else
      {
        j = 3;
        k = paramRect.bottom - localRect1.top;
      }
    }
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
    {
      View localView3 = (View)localIterator1.next();
      CellAndSpan localCellAndSpan3 = (CellAndSpan)paramItemConfiguration.map.get(localView3);
      markCellsForView(localCellAndSpan3.x, localCellAndSpan3.y, localCellAndSpan3.spanX, localCellAndSpan3.spanY, this.mTmpOccupied, false);
    }
    paramItemConfiguration.save();
    localViewCluster.sortConfigurationForEdgePush(j);
    if ((k > 0) && (i == 0))
    {
      Iterator localIterator3 = paramItemConfiguration.sortedViews.iterator();
      for (;;)
      {
        View localView2;
        if (localIterator3.hasNext())
        {
          localView2 = (View)localIterator3.next();
          if ((localViewCluster.views.contains(localView2)) || (localView2 == paramView) || (!localViewCluster.isViewTouchingEdge(localView2, j))) {
            continue;
          }
          if (!((LayoutParams)localView2.getLayoutParams()).canReorder) {
            i = 1;
          }
        }
        else
        {
          k--;
          localViewCluster.shift(j, 1);
          break;
        }
        localViewCluster.addView(localView2);
        CellAndSpan localCellAndSpan2 = (CellAndSpan)paramItemConfiguration.map.get(localView2);
        markCellsForView(localCellAndSpan2.x, localCellAndSpan2.y, localCellAndSpan2.spanX, localCellAndSpan2.spanY, this.mTmpOccupied, false);
      }
    }
    Rect localRect2 = localViewCluster.getBoundingRect();
    if ((i == 0) && (localRect2.left >= 0) && (localRect2.right <= this.mCountX) && (localRect2.top >= 0) && (localRect2.bottom <= this.mCountY)) {}
    for (boolean bool = true;; bool = false)
    {
      Iterator localIterator2 = localViewCluster.views.iterator();
      while (localIterator2.hasNext())
      {
        View localView1 = (View)localIterator2.next();
        CellAndSpan localCellAndSpan1 = (CellAndSpan)paramItemConfiguration.map.get(localView1);
        markCellsForView(localCellAndSpan1.x, localCellAndSpan1.y, localCellAndSpan1.spanX, localCellAndSpan1.spanY, this.mTmpOccupied, true);
      }
      break;
      paramItemConfiguration.restore();
    }
  }
  
  private boolean rearrangementExists(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, View paramView, ItemConfiguration paramItemConfiguration)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0)) {
      return false;
    }
    this.mIntersectingViews.clear();
    this.mOccupiedRect.set(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    if (paramView != null)
    {
      CellAndSpan localCellAndSpan2 = (CellAndSpan)paramItemConfiguration.map.get(paramView);
      if (localCellAndSpan2 != null)
      {
        localCellAndSpan2.x = paramInt1;
        localCellAndSpan2.y = paramInt2;
      }
    }
    Rect localRect1 = new Rect(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    Rect localRect2 = new Rect();
    Iterator localIterator1 = paramItemConfiguration.map.keySet().iterator();
    while (localIterator1.hasNext())
    {
      View localView = (View)localIterator1.next();
      if (localView != paramView)
      {
        CellAndSpan localCellAndSpan1 = (CellAndSpan)paramItemConfiguration.map.get(localView);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        localRect2.set(localCellAndSpan1.x, localCellAndSpan1.y, localCellAndSpan1.x + localCellAndSpan1.spanX, localCellAndSpan1.y + localCellAndSpan1.spanY);
        if (Rect.intersects(localRect1, localRect2))
        {
          if (!localLayoutParams.canReorder) {
            return false;
          }
          this.mIntersectingViews.add(localView);
        }
      }
    }
    if (attemptPushInDirection(this.mIntersectingViews, this.mOccupiedRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
      return true;
    }
    if (addViewsToTempLocation(this.mIntersectingViews, this.mOccupiedRect, paramArrayOfInt, paramView, paramItemConfiguration)) {
      return true;
    }
    Iterator localIterator2 = this.mIntersectingViews.iterator();
    while (localIterator2.hasNext()) {
      if (!addViewToTempLocation((View)localIterator2.next(), this.mOccupiedRect, paramArrayOfInt, paramItemConfiguration)) {
        return false;
      }
    }
    return true;
  }
  
  public static int[] rectToCell(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    if (localDeviceProfile.isLandscape) {}
    int k;
    int m;
    for (int i = 0;; i = 1)
    {
      Rect localRect = localDeviceProfile.getWorkspacePadding(i);
      int j = Math.min(localDeviceProfile.calculateCellWidth(localDeviceProfile.widthPx - localRect.left - localRect.right, (int)localDeviceProfile.numColumns), localDeviceProfile.calculateCellHeight(localDeviceProfile.heightPx - localRect.top - localRect.bottom, (int)localDeviceProfile.numRows));
      k = (int)Math.ceil(paramInt1 / j);
      m = (int)Math.ceil(paramInt2 / j);
      if (paramArrayOfInt != null) {
        break;
      }
      return new int[] { k, m };
    }
    paramArrayOfInt[0] = k;
    paramArrayOfInt[1] = m;
    return paramArrayOfInt;
  }
  
  private void recycleTempRects(Stack<Rect> paramStack)
  {
    while (!paramStack.isEmpty()) {
      this.mTempRectStack.push(paramStack.pop());
    }
  }
  
  public boolean addViewToCellLayout(View paramView, int paramInt1, int paramInt2, LayoutParams paramLayoutParams, boolean paramBoolean)
  {
    BubbleTextView localBubbleTextView;
    if ((paramView instanceof BubbleTextView))
    {
      localBubbleTextView = (BubbleTextView)paramView;
      if (this.mIsHotseat) {
        break label152;
      }
    }
    label152:
    for (boolean bool = true;; bool = false)
    {
      localBubbleTextView.setTextVisibility(bool);
      paramView.setScaleX(getChildrenScale());
      paramView.setScaleY(getChildrenScale());
      if ((paramLayoutParams.cellX < 0) || (paramLayoutParams.cellX > -1 + this.mCountX) || (paramLayoutParams.cellY < 0) || (paramLayoutParams.cellY > -1 + this.mCountY)) {
        break;
      }
      if (paramLayoutParams.cellHSpan < 0) {
        paramLayoutParams.cellHSpan = this.mCountX;
      }
      if (paramLayoutParams.cellVSpan < 0) {
        paramLayoutParams.cellVSpan = this.mCountY;
      }
      paramView.setId(paramInt2);
      this.mShortcutsAndWidgets.addView(paramView, paramInt1, paramLayoutParams);
      if (paramBoolean) {
        markCellsAsOccupiedForView(paramView);
      }
      return true;
    }
    return false;
  }
  
  public boolean animateChildToPosition(final View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = getShortcutsAndWidgets();
    boolean[][] arrayOfBoolean = this.mOccupied;
    if (!paramBoolean1) {
      arrayOfBoolean = this.mTmpOccupied;
    }
    if (localShortcutAndWidgetContainer.indexOfChild(paramView) != -1)
    {
      final LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      ItemInfo localItemInfo = (ItemInfo)paramView.getTag();
      if (this.mReorderAnimators.containsKey(localLayoutParams))
      {
        ((Animator)this.mReorderAnimators.get(localLayoutParams)).cancel();
        this.mReorderAnimators.remove(localLayoutParams);
      }
      final int i = localLayoutParams.x;
      final int j = localLayoutParams.y;
      if (paramBoolean2)
      {
        arrayOfBoolean[localLayoutParams.cellX][localLayoutParams.cellY] = 0;
        arrayOfBoolean[paramInt1][paramInt2] = 1;
      }
      localLayoutParams.isLockedToGrid = true;
      if (paramBoolean1)
      {
        localItemInfo.cellX = paramInt1;
        localLayoutParams.cellX = paramInt1;
        localItemInfo.cellY = paramInt2;
        localLayoutParams.cellY = paramInt2;
      }
      final int k;
      final int m;
      for (;;)
      {
        localShortcutAndWidgetContainer.setupLp(localLayoutParams);
        localLayoutParams.isLockedToGrid = false;
        k = localLayoutParams.x;
        m = localLayoutParams.y;
        localLayoutParams.x = i;
        localLayoutParams.y = j;
        if ((i != k) || (j != m)) {
          break;
        }
        localLayoutParams.isLockedToGrid = true;
        return true;
        localLayoutParams.tmpCellX = paramInt1;
        localLayoutParams.tmpCellY = paramInt2;
      }
      ValueAnimator localValueAnimator = LauncherAnimUtils.ofFloat(paramView, new float[] { 0.0F, 1.0F });
      localValueAnimator.setDuration(paramInt3);
      this.mReorderAnimators.put(localLayoutParams, localValueAnimator);
      localValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          localLayoutParams.x = ((int)((1.0F - f) * i + f * k));
          localLayoutParams.y = ((int)((1.0F - f) * j + f * m));
          paramView.requestLayout();
        }
      });
      localValueAnimator.addListener(new AnimatorListenerAdapter()
      {
        boolean cancelled = false;
        
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
          this.cancelled = true;
        }
        
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          if (!this.cancelled)
          {
            localLayoutParams.isLockedToGrid = true;
            paramView.requestLayout();
          }
          if (CellLayout.this.mReorderAnimators.containsKey(localLayoutParams)) {
            CellLayout.this.mReorderAnimators.remove(localLayoutParams);
          }
        }
      });
      localValueAnimator.setStartDelay(paramInt4);
      localValueAnimator.start();
      return true;
    }
    return false;
  }
  
  public void buildHardwareLayer()
  {
    this.mShortcutsAndWidgets.buildLayer();
  }
  
  public void calculateSpans(ItemInfo paramItemInfo)
  {
    int i;
    if ((paramItemInfo instanceof LauncherAppWidgetInfo)) {
      i = ((LauncherAppWidgetInfo)paramItemInfo).minWidth;
    }
    for (int j = ((LauncherAppWidgetInfo)paramItemInfo).minHeight;; j = ((PendingAddWidgetInfo)paramItemInfo).minHeight)
    {
      int[] arrayOfInt = rectToCell(i, j, null);
      paramItemInfo.spanX = arrayOfInt[0];
      paramItemInfo.spanY = arrayOfInt[1];
      return;
      if (!(paramItemInfo instanceof PendingAddWidgetInfo)) {
        break;
      }
      i = ((PendingAddWidgetInfo)paramItemInfo).minWidth;
    }
    paramItemInfo.spanY = 1;
    paramItemInfo.spanX = 1;
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).cancelLongPress();
    }
  }
  
  void cellToCenterPoint(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    regionToCenterPoint(paramInt1, paramInt2, 1, 1, paramArrayOfInt);
  }
  
  void cellToPoint(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    paramArrayOfInt[0] = (i + paramInt1 * (this.mCellWidth + this.mWidthGap));
    paramArrayOfInt[1] = (j + paramInt2 * (this.mCellHeight + this.mHeightGap));
  }
  
  public void cellToRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect)
  {
    int i = this.mCellWidth;
    int j = this.mCellHeight;
    int k = this.mWidthGap;
    int m = this.mHeightGap;
    int n = getPaddingLeft();
    int i1 = getPaddingTop();
    int i2 = paramInt3 * i + k * (paramInt3 - 1);
    int i3 = paramInt4 * j + m * (paramInt4 - 1);
    int i4 = n + paramInt1 * (i + k);
    int i5 = i1 + paramInt2 * (j + m);
    paramRect.set(i4, i5, i4 + i2, i5 + i3);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void clearDragOutlines()
  {
    int i = this.mDragOutlineCurrent;
    this.mDragOutlineAnims[i].animateOut();
    int[] arrayOfInt = this.mDragCell;
    this.mDragCell[1] = -1;
    arrayOfInt[0] = -1;
  }
  
  public void clearFolderLeaveBehind()
  {
    this.mFolderLeaveBehindCell[0] = -1;
    this.mFolderLeaveBehindCell[1] = -1;
    invalidate();
  }
  
  int[] createArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, View paramView, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt7)
  {
    int[] arrayOfInt = findNearestArea(paramInt1, paramInt2, paramInt5, paramInt6, paramArrayOfInt1);
    if (paramArrayOfInt2 == null) {
      paramArrayOfInt2 = new int[2];
    }
    ItemConfiguration localItemConfiguration2;
    ItemConfiguration localItemConfiguration3;
    label187:
    int i;
    boolean bool2;
    if (((paramInt7 == 1) || (paramInt7 == 2) || (paramInt7 == 3)) && (this.mPreviousReorderDirection[0] != -100))
    {
      this.mDirectionVector[0] = this.mPreviousReorderDirection[0];
      this.mDirectionVector[1] = this.mPreviousReorderDirection[1];
      if ((paramInt7 == 1) || (paramInt7 == 2))
      {
        this.mPreviousReorderDirection[0] = -100;
        this.mPreviousReorderDirection[1] = -100;
      }
      ItemConfiguration localItemConfiguration1 = simpleSwap(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, this.mDirectionVector, paramView, true, new ItemConfiguration(null));
      localItemConfiguration2 = findConfigurationNoShuffle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramView, new ItemConfiguration(null));
      if ((!localItemConfiguration1.isSolution) || (localItemConfiguration1.area() < localItemConfiguration2.area())) {
        break label379;
      }
      localItemConfiguration3 = localItemConfiguration1;
      i = 1;
      setUseTempCoords(true);
      if (localItemConfiguration3 == null) {
        break label421;
      }
      arrayOfInt[0] = localItemConfiguration3.dragViewX;
      arrayOfInt[1] = localItemConfiguration3.dragViewY;
      paramArrayOfInt2[0] = localItemConfiguration3.dragViewSpanX;
      paramArrayOfInt2[1] = localItemConfiguration3.dragViewSpanY;
      if ((paramInt7 == 0) || (paramInt7 == 1) || (paramInt7 == 2))
      {
        copySolutionToTempState(localItemConfiguration3, paramView);
        setItemPlacementDirty(true);
        if (paramInt7 != 1) {
          break label401;
        }
        bool2 = true;
        label275:
        animateItemsToSolution(localItemConfiguration3, paramView, bool2);
        if ((paramInt7 != 1) && (paramInt7 != 2)) {
          break label407;
        }
        commitTempPlacement();
        completeAndClearReorderHintAnimations();
        setItemPlacementDirty(false);
      }
    }
    for (;;)
    {
      if ((paramInt7 == 1) || (i == 0)) {
        setUseTempCoords(false);
      }
      this.mShortcutsAndWidgets.requestLayout();
      return arrayOfInt;
      getDirectionVectorForDrop(paramInt1, paramInt2, paramInt5, paramInt6, paramView, this.mDirectionVector);
      this.mPreviousReorderDirection[0] = this.mDirectionVector[0];
      this.mPreviousReorderDirection[1] = this.mDirectionVector[1];
      break;
      label379:
      boolean bool1 = localItemConfiguration2.isSolution;
      localItemConfiguration3 = null;
      if (!bool1) {
        break label187;
      }
      localItemConfiguration3 = localItemConfiguration2;
      break label187;
      label401:
      bool2 = false;
      break label275;
      label407:
      beginOrAdjustHintAnimations(localItemConfiguration3, paramView, 150);
      continue;
      label421:
      paramArrayOfInt2[1] = -1;
      paramArrayOfInt2[0] = -1;
      arrayOfInt[1] = -1;
      arrayOfInt[0] = -1;
      i = 0;
    }
  }
  
  boolean createAreaForResize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, int[] paramArrayOfInt, boolean paramBoolean)
  {
    int[] arrayOfInt = new int[2];
    regionToCenterPoint(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt);
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    ItemConfiguration localItemConfiguration1 = new ItemConfiguration(null);
    ItemConfiguration localItemConfiguration2 = simpleSwap(i, j, paramInt3, paramInt4, paramInt3, paramInt4, paramArrayOfInt, paramView, true, localItemConfiguration1);
    setUseTempCoords(true);
    if ((localItemConfiguration2 != null) && (localItemConfiguration2.isSolution))
    {
      copySolutionToTempState(localItemConfiguration2, paramView);
      setItemPlacementDirty(true);
      animateItemsToSolution(localItemConfiguration2, paramView, paramBoolean);
      if (!paramBoolean) {
        break label134;
      }
      commitTempPlacement();
      completeAndClearReorderHintAnimations();
      setItemPlacementDirty(false);
    }
    for (;;)
    {
      this.mShortcutsAndWidgets.requestLayout();
      return localItemConfiguration2.isSolution;
      label134:
      beginOrAdjustHintAnimations(localItemConfiguration2, paramView, 150);
    }
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (this.mForegroundAlpha > 0)
    {
      this.mOverScrollForegroundDrawable.setBounds(this.mForegroundRect);
      this.mOverScrollForegroundDrawable.draw(paramCanvas);
    }
  }
  
  public void enableHardwareLayer(boolean paramBoolean)
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = this.mShortcutsAndWidgets;
    if (paramBoolean) {}
    for (int i = 2;; i = 0)
    {
      localShortcutAndWidgetContainer.setLayerType(i, sPaint);
      return;
    }
  }
  
  boolean findCellForSpan(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    return findCellForSpanThatIntersectsIgnoring(paramArrayOfInt, paramInt1, paramInt2, -1, -1, null, this.mOccupied);
  }
  
  boolean findCellForSpanThatIntersects(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return findCellForSpanThatIntersectsIgnoring(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, null, this.mOccupied);
  }
  
  boolean findCellForSpanThatIntersectsIgnoring(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, boolean[][] paramArrayOfBoolean)
  {
    markCellsAsUnoccupiedForView(paramView, paramArrayOfBoolean);
    boolean bool = false;
    for (;;)
    {
      int i = 0;
      if (paramInt3 >= 0) {
        i = Math.max(0, paramInt3 - (paramInt1 - 1));
      }
      int j = this.mCountX - (paramInt1 - 1);
      int i7;
      int k;
      int m;
      int i5;
      if (paramInt3 >= 0)
      {
        int i6 = paramInt3 + (paramInt1 - 1);
        if (paramInt1 == 1)
        {
          i7 = 1;
          j = Math.min(j, i7 + i6);
        }
      }
      else
      {
        k = 0;
        if (paramInt4 >= 0) {
          k = Math.max(0, paramInt4 - (paramInt2 - 1));
        }
        m = this.mCountY - (paramInt2 - 1);
        if (paramInt4 >= 0)
        {
          int i4 = paramInt4 + (paramInt2 - 1);
          if (paramInt2 != 1) {
            break label217;
          }
          i5 = 1;
          label125:
          m = Math.min(m, i5 + i4);
        }
      }
      for (int n = k;; n++)
      {
        if ((n >= m) || (bool)) {
          break label258;
        }
        int i1 = i;
        label157:
        if (i1 < j)
        {
          label217:
          label229:
          for (int i2 = 0;; i2++)
          {
            if (i2 >= paramInt1) {
              break label235;
            }
            for (int i3 = 0;; i3++)
            {
              if (i3 >= paramInt2) {
                break label229;
              }
              if (paramArrayOfBoolean[(i1 + i2)][(n + i3)] != 0)
              {
                i1 = 1 + (i1 + i2);
                break label157;
                i7 = 0;
                break;
                i5 = 0;
                break label125;
              }
            }
          }
          label235:
          if (paramArrayOfInt != null)
          {
            paramArrayOfInt[0] = i1;
            paramArrayOfInt[1] = n;
          }
          bool = true;
        }
      }
      label258:
      if ((paramInt3 == -1) && (paramInt4 == -1))
      {
        markCellsAsOccupiedForView(paramView, paramArrayOfBoolean);
        return bool;
      }
      paramInt3 = -1;
      paramInt4 = -1;
    }
  }
  
  ItemConfiguration findConfigurationNoShuffle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, View paramView, ItemConfiguration paramItemConfiguration)
  {
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = new int[2];
    findNearestVacantArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, null, arrayOfInt1, arrayOfInt2);
    if ((arrayOfInt1[0] >= 0) && (arrayOfInt1[1] >= 0))
    {
      copyCurrentStateToSolution(paramItemConfiguration, false);
      paramItemConfiguration.dragViewX = arrayOfInt1[0];
      paramItemConfiguration.dragViewY = arrayOfInt1[1];
      paramItemConfiguration.dragViewSpanX = arrayOfInt2[0];
      paramItemConfiguration.dragViewSpanY = arrayOfInt2[1];
      paramItemConfiguration.isSolution = true;
      return paramItemConfiguration;
    }
    paramItemConfiguration.isSolution = false;
    return paramItemConfiguration;
  }
  
  int[] findNearestArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, View paramView, boolean paramBoolean, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[][] paramArrayOfBoolean)
  {
    lazyInitTempRectStack();
    markCellsAsUnoccupiedForView(paramView, paramArrayOfBoolean);
    int i = (int)(paramInt1 - (this.mCellWidth + this.mWidthGap) * (paramInt5 - 1) / 2.0F);
    int j = (int)(paramInt2 - (this.mCellHeight + this.mHeightGap) * (paramInt6 - 1) / 2.0F);
    if (paramArrayOfInt1 != null) {}
    double d1;
    Rect localRect1;
    Stack localStack;
    int k;
    int m;
    for (int[] arrayOfInt1 = paramArrayOfInt1;; arrayOfInt1 = new int[2])
    {
      d1 = 1.7976931348623157E+308D;
      localRect1 = new Rect(-1, -1, -1, -1);
      localStack = new Stack();
      k = this.mCountX;
      m = this.mCountY;
      if ((paramInt3 > 0) && (paramInt4 > 0) && (paramInt5 > 0) && (paramInt6 > 0) && (paramInt5 >= paramInt3) && (paramInt6 >= paramInt4)) {
        break;
      }
      return arrayOfInt1;
    }
    for (int n = 0;; n++)
    {
      int i1 = m - (paramInt4 - 1);
      if (n >= i1) {
        break;
      }
      int i2 = 0;
      int i3 = k - (paramInt3 - 1);
      if (i2 < i3)
      {
        int i4 = -1;
        int i5 = -1;
        if (paramBoolean)
        {
          label245:
          for (int i9 = 0;; i9++)
          {
            if (i9 >= paramInt3) {
              break label251;
            }
            for (int i17 = 0;; i17++)
            {
              if (i17 >= paramInt4) {
                break label245;
              }
              if (paramArrayOfBoolean[(i2 + i9)][(n + i17)] != 0)
              {
                i2++;
                break;
              }
            }
          }
          label251:
          i5 = paramInt3;
          i4 = paramInt4;
          int i10 = 1;
          int i11;
          int i12;
          if (i5 >= paramInt5)
          {
            i11 = 1;
            if (i4 < paramInt6) {
              break label355;
            }
            i12 = 1;
          }
          label396:
          for (;;)
          {
            label281:
            if ((i11 == 0) || (i12 == 0))
            {
              if ((i10 != 0) && (i11 == 0))
              {
                int i16 = 0;
                for (;;)
                {
                  if (i16 < i4)
                  {
                    if ((i2 + i5 > k - 1) || (paramArrayOfBoolean[(i2 + i5)][(n + i16)] != 0)) {
                      i11 = 1;
                    }
                    i16++;
                    continue;
                    i11 = 0;
                    break;
                    label355:
                    i12 = 0;
                    break label281;
                  }
                }
                if (i11 == 0) {
                  i5++;
                }
              }
              label484:
              label490:
              label494:
              for (;;)
              {
                label369:
                int i14;
                label379:
                int i15;
                if (i5 >= paramInt5)
                {
                  i14 = 1;
                  i11 |= i14;
                  if (i4 < paramInt6) {
                    break label484;
                  }
                  i15 = 1;
                  i12 |= i15;
                  if (i10 != 0) {
                    break label490;
                  }
                }
                for (i10 = 1;; i10 = 0)
                {
                  break;
                  if (i12 != 0) {
                    break label494;
                  }
                  for (int i13 = 0; i13 < i5; i13++) {
                    if ((n + i4 > m - 1) || (paramArrayOfBoolean[(i2 + i13)][(n + i4)] != 0)) {
                      i12 = 1;
                    }
                  }
                  if (i12 != 0) {
                    break label369;
                  }
                  i4++;
                  break label369;
                  i14 = 0;
                  break label379;
                  i15 = 0;
                  break label396;
                }
              }
            }
          }
          if (i5 < paramInt5) {
            break label718;
          }
        }
        label718:
        for (;;)
        {
          if (i4 >= paramInt6) {}
          int[] arrayOfInt2 = this.mTmpXY;
          cellToCenterPoint(i2, n, arrayOfInt2);
          Rect localRect2 = (Rect)this.mTempRectStack.pop();
          int i6 = i2 + i5;
          int i7 = n + i4;
          localRect2.set(i2, n, i6, i7);
          Iterator localIterator = localStack.iterator();
          do
          {
            boolean bool = localIterator.hasNext();
            i8 = 0;
            if (!bool) {
              break;
            }
          } while (!((Rect)localIterator.next()).contains(localRect2));
          int i8 = 1;
          localStack.push(localRect2);
          double d2 = Math.sqrt(Math.pow(arrayOfInt2[0] - i, 2.0D) + Math.pow(arrayOfInt2[1] - j, 2.0D));
          if (((d2 > d1) || (i8 != 0)) && (!localRect2.contains(localRect1))) {
            break;
          }
          d1 = d2;
          arrayOfInt1[0] = i2;
          arrayOfInt1[1] = n;
          if (paramArrayOfInt2 != null)
          {
            paramArrayOfInt2[0] = i5;
            paramArrayOfInt2[1] = i4;
          }
          localRect1.set(localRect2);
          break;
        }
      }
    }
    markCellsAsOccupiedForView(paramView, paramArrayOfBoolean);
    if (d1 == 1.7976931348623157E+308D)
    {
      arrayOfInt1[0] = -1;
      arrayOfInt1[1] = -1;
    }
    recycleTempRects(localStack);
    return arrayOfInt1;
  }
  
  int[] findNearestArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, boolean paramBoolean, int[] paramArrayOfInt)
  {
    return findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt3, paramInt4, paramView, paramBoolean, paramArrayOfInt, null, this.mOccupied);
  }
  
  int[] findNearestArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    return findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, null, false, paramArrayOfInt);
  }
  
  int[] findNearestVacantArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, View paramView, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    return findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramView, true, paramArrayOfInt1, paramArrayOfInt2, this.mOccupied);
  }
  
  int[] findNearestVacantArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    return findNearestVacantArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, null, paramArrayOfInt1, paramArrayOfInt2);
  }
  
  int[] findNearestVacantArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, int[] paramArrayOfInt)
  {
    return findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, paramView, true, paramArrayOfInt);
  }
  
  int[] findNearestVacantArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    return findNearestVacantArea(paramInt1, paramInt2, paramInt3, paramInt4, null, paramArrayOfInt);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public float getBackgroundAlpha()
  {
    return this.mBackgroundAlpha;
  }
  
  int getCellHeight()
  {
    return this.mCellHeight;
  }
  
  int getCellWidth()
  {
    return this.mCellWidth;
  }
  
  public View getChildAt(int paramInt1, int paramInt2)
  {
    return this.mShortcutsAndWidgets.getChildAt(paramInt1, paramInt2);
  }
  
  public float getChildrenScale()
  {
    if (this.mIsHotseat) {
      return this.mHotseatScale;
    }
    return 1.0F;
  }
  
  int getCountX()
  {
    return this.mCountX;
  }
  
  int getCountY()
  {
    return this.mCountY;
  }
  
  public int getDesiredHeight()
  {
    return getPaddingTop() + getPaddingBottom() + this.mCountY * this.mCellHeight + Math.max(-1 + this.mCountY, 0) * this.mHeightGap;
  }
  
  public int getDesiredWidth()
  {
    return getPaddingLeft() + getPaddingRight() + this.mCountX * this.mCellWidth + Math.max(-1 + this.mCountX, 0) * this.mWidthGap;
  }
  
  public float getDistanceFromCell(float paramFloat1, float paramFloat2, int[] paramArrayOfInt)
  {
    cellToCenterPoint(paramArrayOfInt[0], paramArrayOfInt[1], this.mTmpPoint);
    return (float)Math.sqrt(Math.pow(paramFloat1 - this.mTmpPoint[0], 2.0D) + Math.pow(paramFloat2 - this.mTmpPoint[1], 2.0D));
  }
  
  int getHeightGap()
  {
    return this.mHeightGap;
  }
  
  boolean getIsDragOverlapping()
  {
    return this.mIsDragOverlapping;
  }
  
  public ShortcutAndWidgetContainer getShortcutsAndWidgets()
  {
    if (getChildCount() > 0) {
      return (ShortcutAndWidgetContainer)getChildAt(0);
    }
    return null;
  }
  
  public CellInfo getTag()
  {
    return (CellInfo)super.getTag();
  }
  
  public boolean getVacantCell(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    return findVacantCell(paramArrayOfInt, paramInt1, paramInt2, this.mCountX, this.mCountY, this.mOccupied);
  }
  
  int getWidthGap()
  {
    return this.mWidthGap;
  }
  
  public void hideFolderAccept(FolderIcon.FolderRingAnimator paramFolderRingAnimator)
  {
    if (this.mFolderOuterRings.contains(paramFolderRingAnimator)) {
      this.mFolderOuterRings.remove(paramFolderRingAnimator);
    }
    invalidate();
  }
  
  public boolean isDropPending()
  {
    return this.mDropPending;
  }
  
  boolean isItemPlacementDirty()
  {
    return this.mItemPlacementDirty;
  }
  
  boolean isNearestDropLocationOccupied(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, int[] paramArrayOfInt)
  {
    int[] arrayOfInt = findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    getViewsIntersectingRegion(arrayOfInt[0], arrayOfInt[1], paramInt3, paramInt4, paramView, null, this.mIntersectingViews);
    return !this.mIntersectingViews.isEmpty();
  }
  
  public boolean isOccupied(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < this.mCountX) && (paramInt2 < this.mCountY)) {
      return this.mOccupied[paramInt1][paramInt2];
    }
    throw new RuntimeException("Position exceeds the bound of this CellLayout");
  }
  
  public boolean lastDownOnOccupiedCell()
  {
    return this.mLastDownOnOccupiedCell;
  }
  
  public void markCellsAsOccupiedForView(View paramView)
  {
    markCellsAsOccupiedForView(paramView, this.mOccupied);
  }
  
  public void markCellsAsOccupiedForView(View paramView, boolean[][] paramArrayOfBoolean)
  {
    if ((paramView == null) || (paramView.getParent() != this.mShortcutsAndWidgets)) {
      return;
    }
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    markCellsForView(localLayoutParams.cellX, localLayoutParams.cellY, localLayoutParams.cellHSpan, localLayoutParams.cellVSpan, paramArrayOfBoolean, true);
  }
  
  public void markCellsAsUnoccupiedForView(View paramView)
  {
    markCellsAsUnoccupiedForView(paramView, this.mOccupied);
  }
  
  public void markCellsAsUnoccupiedForView(View paramView, boolean[][] paramArrayOfBoolean)
  {
    if ((paramView == null) || (paramView.getParent() != this.mShortcutsAndWidgets)) {
      return;
    }
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    markCellsForView(localLayoutParams.cellX, localLayoutParams.cellY, localLayoutParams.cellHSpan, localLayoutParams.cellVSpan, paramArrayOfBoolean, false);
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if ((getParent() instanceof Workspace))
    {
      Workspace localWorkspace = (Workspace)getParent();
      this.mCellInfo.screenId = localWorkspace.getIdForScreen(this);
    }
  }
  
  void onDragEnter()
  {
    this.mDragEnforcer.onDragEnter();
    this.mDragging = true;
  }
  
  void onDragExit()
  {
    this.mDragEnforcer.onDragExit();
    if (this.mDragging) {
      this.mDragging = false;
    }
    int[] arrayOfInt = this.mDragCell;
    this.mDragCell[1] = -1;
    arrayOfInt[0] = -1;
    this.mDragOutlineAnims[this.mDragOutlineCurrent].animateOut();
    this.mDragOutlineCurrent = ((1 + this.mDragOutlineCurrent) % this.mDragOutlineAnims.length);
    revertTempState();
    setIsDragOverlapping(false);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (this.mBackgroundAlpha > 0.0F) {
      if (!this.mUseActiveGlowBackground) {
        break label158;
      }
    }
    label158:
    for (Drawable localDrawable4 = this.mActiveGlowBackground;; localDrawable4 = this.mNormalBackground)
    {
      localDrawable4.setAlpha((int)(255.0F * (this.mBackgroundAlpha * this.mBackgroundAlphaMultiplier)));
      localDrawable4.setBounds(this.mBackgroundRect);
      localDrawable4.draw(paramCanvas);
      Paint localPaint = this.mDragOutlinePaint;
      for (int i = 0; i < this.mDragOutlines.length; i++)
      {
        float f = this.mDragOutlineAlphas[i];
        if (f > 0.0F)
        {
          Rect localRect = this.mDragOutlines[i];
          this.mTempRect.set(localRect);
          Utilities.scaleRectAboutCenter(this.mTempRect, getChildrenScale());
          Bitmap localBitmap2 = (Bitmap)this.mDragOutlineAnims[i].getTag();
          localPaint.setAlpha((int)(0.5F + f));
          paramCanvas.drawBitmap(localBitmap2, null, this.mTempRect, localPaint);
        }
      }
    }
    if (this.mPressedOrFocusedIcon != null)
    {
      int i7 = this.mPressedOrFocusedIcon.getPressedOrFocusedBackgroundPadding();
      Bitmap localBitmap1 = this.mPressedOrFocusedIcon.getPressedOrFocusedBackground();
      if (localBitmap1 != null)
      {
        int i8 = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - this.mCountX * this.mCellWidth;
        int i9 = getPaddingLeft() + (int)Math.ceil(i8 / 2.0F);
        int i10 = getPaddingTop();
        paramCanvas.drawBitmap(localBitmap1, i9 + this.mPressedOrFocusedIcon.getLeft() - i7, i10 + this.mPressedOrFocusedIcon.getTop() - i7, null);
      }
    }
    int j = FolderIcon.FolderRingAnimator.sPreviewSize;
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    for (int k = 0; k < this.mFolderOuterRings.size(); k++)
    {
      FolderIcon.FolderRingAnimator localFolderRingAnimator = (FolderIcon.FolderRingAnimator)this.mFolderOuterRings.get(k);
      cellToPoint(localFolderRingAnimator.mCellX, localFolderRingAnimator.mCellY, this.mTempLocation);
      View localView2 = getChildAt(localFolderRingAnimator.mCellX, localFolderRingAnimator.mCellY);
      if (localView2 != null)
      {
        int i3 = this.mTempLocation[0] + this.mCellWidth / 2;
        int i4 = this.mTempLocation[1] + j / 2 + localView2.getPaddingTop() + localDeviceProfile.folderBackgroundOffset;
        Drawable localDrawable2 = FolderIcon.FolderRingAnimator.sSharedOuterRingDrawable;
        int i5 = (int)(localFolderRingAnimator.getOuterRingSize() * getChildrenScale());
        paramCanvas.save();
        paramCanvas.translate(i3 - i5 / 2, i4 - i5 / 2);
        localDrawable2.setBounds(0, 0, i5, i5);
        localDrawable2.draw(paramCanvas);
        paramCanvas.restore();
        Drawable localDrawable3 = FolderIcon.FolderRingAnimator.sSharedInnerRingDrawable;
        int i6 = (int)(localFolderRingAnimator.getInnerRingSize() * getChildrenScale());
        paramCanvas.save();
        paramCanvas.translate(i3 - i6 / 2, i4 - i6 / 2);
        localDrawable3.setBounds(0, 0, i6, i6);
        localDrawable3.draw(paramCanvas);
        paramCanvas.restore();
      }
    }
    if ((this.mFolderLeaveBehindCell[0] >= 0) && (this.mFolderLeaveBehindCell[1] >= 0))
    {
      Drawable localDrawable1 = FolderIcon.sSharedFolderLeaveBehind;
      int m = localDrawable1.getIntrinsicWidth();
      int n = localDrawable1.getIntrinsicHeight();
      cellToPoint(this.mFolderLeaveBehindCell[0], this.mFolderLeaveBehindCell[1], this.mTempLocation);
      View localView1 = getChildAt(this.mFolderLeaveBehindCell[0], this.mFolderLeaveBehindCell[1]);
      if (localView1 != null)
      {
        int i1 = this.mTempLocation[0] + this.mCellWidth / 2;
        int i2 = this.mTempLocation[1] + j / 2 + localView1.getPaddingTop() + localDeviceProfile.folderBackgroundOffset;
        paramCanvas.save();
        paramCanvas.translate(i1 - m / 2, i2 - m / 2);
        localDrawable1.setBounds(0, 0, m, n);
        localDrawable1.draw(paramCanvas);
        paramCanvas.restore();
      }
    }
  }
  
  void onDropChild(View paramView)
  {
    if (paramView != null)
    {
      ((LayoutParams)paramView.getLayoutParams()).dropped = true;
      paramView.requestLayout();
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (i == 0) {
      clearTagCellInfo();
    }
    if ((this.mInterceptTouchListener != null) && (this.mInterceptTouchListener.onTouch(this, paramMotionEvent))) {
      return true;
    }
    if (i == 0) {
      setTagToCellInfoForPoint((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
    }
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - this.mCountX * this.mCellWidth;
    int j = getPaddingLeft() + (int)Math.ceil(i / 2.0F);
    int k = getPaddingTop();
    int m = getChildCount();
    for (int n = 0; n < m; n++) {
      getChildAt(n).layout(j, k, j + paramInt3 - paramInt1, k + paramInt4 - paramInt2);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = k - (getPaddingLeft() + getPaddingRight());
    int i1 = m - (getPaddingTop() + getPaddingBottom());
    if ((this.mFixedCellWidth < 0) || (this.mFixedCellHeight < 0))
    {
      int i2 = localDeviceProfile.calculateCellWidth(n, this.mCountX);
      int i3 = localDeviceProfile.calculateCellHeight(i1, this.mCountY);
      if ((i2 != this.mCellWidth) || (i3 != this.mCellHeight))
      {
        this.mCellWidth = i2;
        this.mCellHeight = i3;
        this.mShortcutsAndWidgets.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mCountX, this.mCountY);
      }
    }
    int i4 = n;
    int i5 = i1;
    int i11;
    label271:
    int i13;
    if ((this.mFixedWidth > 0) && (this.mFixedHeight > 0))
    {
      i4 = this.mFixedWidth;
      i5 = this.mFixedHeight;
      int i6 = -1 + this.mCountX;
      int i7 = -1 + this.mCountY;
      if ((this.mOriginalWidthGap >= 0) && (this.mOriginalHeightGap >= 0)) {
        break label464;
      }
      int i8 = n - this.mCountX * this.mCellWidth;
      int i9 = i1 - this.mCountY * this.mCellHeight;
      int i10 = this.mMaxGap;
      if (i6 <= 0) {
        break label452;
      }
      i11 = i8 / i6;
      this.mWidthGap = Math.min(i10, i11);
      int i12 = this.mMaxGap;
      if (i7 <= 0) {
        break label458;
      }
      i13 = i9 / i7;
      label300:
      this.mHeightGap = Math.min(i12, i13);
      this.mShortcutsAndWidgets.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mCountX, this.mCountY);
    }
    int i15;
    int i16;
    for (;;)
    {
      int i14 = getChildCount();
      i15 = 0;
      i16 = 0;
      for (int i17 = 0; i17 < i14; i17++)
      {
        View localView = getChildAt(i17);
        localView.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(i5, 1073741824));
        int i18 = localView.getMeasuredWidth();
        i15 = Math.max(i15, i18);
        int i19 = localView.getMeasuredHeight();
        i16 = Math.max(i16, i19);
      }
      if ((i != 0) && (j != 0)) {
        break;
      }
      throw new RuntimeException("CellLayout cannot have UNSPECIFIED dimensions");
      label452:
      i11 = 0;
      break label271;
      label458:
      i13 = 0;
      break label300;
      label464:
      this.mWidthGap = this.mOriginalWidthGap;
      this.mHeightGap = this.mOriginalHeightGap;
    }
    if ((this.mFixedWidth > 0) && (this.mFixedHeight > 0))
    {
      setMeasuredDimension(i15, i16);
      return;
    }
    setMeasuredDimension(k, m);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    Rect localRect = new Rect();
    this.mNormalBackground.getPadding(localRect);
    this.mBackgroundRect.set(-localRect.left, -localRect.top, paramInt1 + localRect.right, paramInt2 + localRect.bottom);
    this.mForegroundRect.set(this.mForegroundPadding, this.mForegroundPadding, paramInt1 - this.mForegroundPadding, paramInt2 - this.mForegroundPadding);
  }
  
  void pointToCellExact(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    paramArrayOfInt[0] = ((paramInt1 - i) / (this.mCellWidth + this.mWidthGap));
    paramArrayOfInt[1] = ((paramInt2 - j) / (this.mCellHeight + this.mHeightGap));
    int k = this.mCountX;
    int m = this.mCountY;
    if (paramArrayOfInt[0] < 0) {
      paramArrayOfInt[0] = 0;
    }
    if (paramArrayOfInt[0] >= k) {
      paramArrayOfInt[0] = (k - 1);
    }
    if (paramArrayOfInt[1] < 0) {
      paramArrayOfInt[1] = 0;
    }
    if (paramArrayOfInt[1] >= m) {
      paramArrayOfInt[1] = (m - 1);
    }
  }
  
  public void prepareChildForDrag(View paramView)
  {
    markCellsAsUnoccupiedForView(paramView);
  }
  
  void regionToCenterPoint(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    paramArrayOfInt[0] = (i + paramInt1 * (this.mCellWidth + this.mWidthGap) + (paramInt3 * this.mCellWidth + (paramInt3 - 1) * this.mWidthGap) / 2);
    paramArrayOfInt[1] = (j + paramInt2 * (this.mCellHeight + this.mHeightGap) + (paramInt4 * this.mCellHeight + (paramInt4 - 1) * this.mHeightGap) / 2);
  }
  
  void regionToRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = i + paramInt1 * (this.mCellWidth + this.mWidthGap);
    int m = j + paramInt2 * (this.mCellHeight + this.mHeightGap);
    paramRect.set(k, m, k + (paramInt3 * this.mCellWidth + (paramInt3 - 1) * this.mWidthGap), m + (paramInt4 * this.mCellHeight + (paramInt4 - 1) * this.mHeightGap));
  }
  
  public void removeAllViews()
  {
    clearOccupiedCells();
    this.mShortcutsAndWidgets.removeAllViews();
  }
  
  public void removeAllViewsInLayout()
  {
    if (this.mShortcutsAndWidgets.getChildCount() > 0)
    {
      clearOccupiedCells();
      this.mShortcutsAndWidgets.removeAllViewsInLayout();
    }
  }
  
  public void removeView(View paramView)
  {
    markCellsAsUnoccupiedForView(paramView);
    this.mShortcutsAndWidgets.removeView(paramView);
  }
  
  public void removeViewAt(int paramInt)
  {
    markCellsAsUnoccupiedForView(this.mShortcutsAndWidgets.getChildAt(paramInt));
    this.mShortcutsAndWidgets.removeViewAt(paramInt);
  }
  
  public void removeViewInLayout(View paramView)
  {
    markCellsAsUnoccupiedForView(paramView);
    this.mShortcutsAndWidgets.removeViewInLayout(paramView);
  }
  
  public void removeViews(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
      markCellsAsUnoccupiedForView(this.mShortcutsAndWidgets.getChildAt(i));
    }
    this.mShortcutsAndWidgets.removeViews(paramInt1, paramInt2);
  }
  
  public void removeViewsInLayout(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
      markCellsAsUnoccupiedForView(this.mShortcutsAndWidgets.getChildAt(i));
    }
    this.mShortcutsAndWidgets.removeViewsInLayout(paramInt1, paramInt2);
  }
  
  protected void resetOverscrollTransforms()
  {
    if (this.mScrollingTransformsDirty)
    {
      setOverscrollTransformsDirty(false);
      setTranslationX(0.0F);
      setRotationY(0.0F);
      setOverScrollAmount(0.0F, false);
      setPivotX(getMeasuredWidth() / 2);
      setPivotY(getMeasuredHeight() / 2);
    }
  }
  
  public void restoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchRestoreInstanceState(paramSparseArray);
  }
  
  void revertTempState()
  {
    if (isItemPlacementDirty())
    {
      int i = this.mShortcutsAndWidgets.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = this.mShortcutsAndWidgets.getChildAt(j);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if ((localLayoutParams.tmpCellX != localLayoutParams.cellX) || (localLayoutParams.tmpCellY != localLayoutParams.cellY))
        {
          localLayoutParams.tmpCellX = localLayoutParams.cellX;
          localLayoutParams.tmpCellY = localLayoutParams.cellY;
          animateChildToPosition(localView, localLayoutParams.cellX, localLayoutParams.cellY, 150, 0, false, false);
        }
      }
      completeAndClearReorderHintAnimations();
      setItemPlacementDirty(false);
    }
  }
  
  public void setBackgroundAlpha(float paramFloat)
  {
    if (this.mBackgroundAlpha != paramFloat)
    {
      this.mBackgroundAlpha = paramFloat;
      invalidate();
    }
  }
  
  public void setBackgroundAlphaMultiplier(float paramFloat)
  {
    if (this.mBackgroundAlphaMultiplier != paramFloat)
    {
      this.mBackgroundAlphaMultiplier = paramFloat;
      invalidate();
    }
  }
  
  public void setCellDimensions(int paramInt1, int paramInt2)
  {
    this.mCellWidth = paramInt1;
    this.mFixedCellWidth = paramInt1;
    this.mCellHeight = paramInt2;
    this.mFixedCellHeight = paramInt2;
    this.mShortcutsAndWidgets.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mCountX, this.mCountY);
  }
  
  protected void setChildrenDrawingCacheEnabled(boolean paramBoolean)
  {
    this.mShortcutsAndWidgets.setChildrenDrawingCacheEnabled(paramBoolean);
  }
  
  protected void setChildrenDrawnWithCacheEnabled(boolean paramBoolean)
  {
    this.mShortcutsAndWidgets.setChildrenDrawnWithCacheEnabled(paramBoolean);
  }
  
  public void setDropPending(boolean paramBoolean)
  {
    this.mDropPending = paramBoolean;
  }
  
  public void setFixedSize(int paramInt1, int paramInt2)
  {
    this.mFixedWidth = paramInt1;
    this.mFixedHeight = paramInt2;
  }
  
  public void setFolderLeaveBehindCell(int paramInt1, int paramInt2)
  {
    this.mFolderLeaveBehindCell[0] = paramInt1;
    this.mFolderLeaveBehindCell[1] = paramInt2;
    invalidate();
  }
  
  public void setGridSize(int paramInt1, int paramInt2)
  {
    this.mCountX = paramInt1;
    this.mCountY = paramInt2;
    int[] arrayOfInt1 = { this.mCountX, this.mCountY };
    this.mOccupied = ((boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt1));
    int[] arrayOfInt2 = { this.mCountX, this.mCountY };
    this.mTmpOccupied = ((boolean[][])Array.newInstance(Boolean.TYPE, arrayOfInt2));
    this.mTempRectStack.clear();
    this.mShortcutsAndWidgets.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mCountX, this.mCountY);
    requestLayout();
  }
  
  public void setInvertIfRtl(boolean paramBoolean)
  {
    this.mShortcutsAndWidgets.setInvertIfRtl(paramBoolean);
  }
  
  void setIsDragOverlapping(boolean paramBoolean)
  {
    if (this.mIsDragOverlapping != paramBoolean)
    {
      this.mIsDragOverlapping = paramBoolean;
      setUseActiveGlowBackground(this.mIsDragOverlapping);
      invalidate();
    }
  }
  
  public void setIsHotseat(boolean paramBoolean)
  {
    this.mIsHotseat = paramBoolean;
    this.mShortcutsAndWidgets.setIsHotseat(paramBoolean);
  }
  
  void setItemPlacementDirty(boolean paramBoolean)
  {
    this.mItemPlacementDirty = paramBoolean;
  }
  
  public void setOnInterceptTouchListener(View.OnTouchListener paramOnTouchListener)
  {
    this.mInterceptTouchListener = paramOnTouchListener;
  }
  
  void setOverScrollAmount(float paramFloat, boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mOverScrollForegroundDrawable != this.mOverScrollLeft)) {}
    for (this.mOverScrollForegroundDrawable = this.mOverScrollLeft;; this.mOverScrollForegroundDrawable = this.mOverScrollRight) {
      do
      {
        this.mForegroundAlpha = Math.round(255.0F * (paramFloat * this.FOREGROUND_ALPHA_DAMPER));
        this.mOverScrollForegroundDrawable.setAlpha(this.mForegroundAlpha);
        invalidate();
        return;
      } while ((paramBoolean) || (this.mOverScrollForegroundDrawable == this.mOverScrollRight));
    }
  }
  
  protected void setOverscrollTransformsDirty(boolean paramBoolean)
  {
    this.mScrollingTransformsDirty = paramBoolean;
  }
  
  void setPressedOrFocusedIcon(BubbleTextView paramBubbleTextView)
  {
    BubbleTextView localBubbleTextView = this.mPressedOrFocusedIcon;
    this.mPressedOrFocusedIcon = paramBubbleTextView;
    if (localBubbleTextView != null) {
      invalidateBubbleTextView(localBubbleTextView);
    }
    if (this.mPressedOrFocusedIcon != null) {
      invalidateBubbleTextView(this.mPressedOrFocusedIcon);
    }
  }
  
  public void setShortcutAndWidgetAlpha(float paramFloat)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).setAlpha(paramFloat);
    }
  }
  
  public void setTagToCellInfoForPoint(int paramInt1, int paramInt2)
  {
    CellInfo localCellInfo = this.mCellInfo;
    Rect localRect = this.mRect;
    int i = paramInt1 + getScrollX();
    int j = paramInt2 + getScrollY();
    for (int k = -1 + this.mShortcutsAndWidgets.getChildCount();; k--)
    {
      boolean bool = false;
      if (k >= 0)
      {
        View localView = this.mShortcutsAndWidgets.getChildAt(k);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (((localView.getVisibility() == 0) || (localView.getAnimation() != null)) && (localLayoutParams.isLockedToGrid))
        {
          localView.getHitRect(localRect);
          float f = localView.getScaleX();
          localRect = new Rect(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
          localRect.offset(getPaddingLeft(), getPaddingTop());
          localRect.inset((int)(localRect.width() * (1.0F - f) / 2.0F), (int)(localRect.height() * (1.0F - f) / 2.0F));
          if (localRect.contains(i, j))
          {
            localCellInfo.cell = localView;
            localCellInfo.cellX = localLayoutParams.cellX;
            localCellInfo.cellY = localLayoutParams.cellY;
            localCellInfo.spanX = localLayoutParams.cellHSpan;
            localCellInfo.spanY = localLayoutParams.cellVSpan;
            bool = true;
          }
        }
      }
      else
      {
        this.mLastDownOnOccupiedCell = bool;
        if (!bool)
        {
          int[] arrayOfInt = this.mTmpXY;
          pointToCellExact(i, j, arrayOfInt);
          localCellInfo.cell = null;
          localCellInfo.cellX = arrayOfInt[0];
          localCellInfo.cellY = arrayOfInt[1];
          localCellInfo.spanX = 1;
          localCellInfo.spanY = 1;
        }
        setTag(localCellInfo);
        return;
      }
    }
  }
  
  void setUseActiveGlowBackground(boolean paramBoolean)
  {
    this.mUseActiveGlowBackground = paramBoolean;
  }
  
  public void setUseTempCoords(boolean paramBoolean)
  {
    int i = this.mShortcutsAndWidgets.getChildCount();
    for (int j = 0; j < i; j++) {
      ((LayoutParams)this.mShortcutsAndWidgets.getChildAt(j).getLayoutParams()).useTmpCoords = paramBoolean;
    }
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  public void showFolderAccept(FolderIcon.FolderRingAnimator paramFolderRingAnimator)
  {
    this.mFolderOuterRings.add(paramFolderRingAnimator);
  }
  
  ItemConfiguration simpleSwap(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt, View paramView, boolean paramBoolean, ItemConfiguration paramItemConfiguration)
  {
    copyCurrentStateToSolution(paramItemConfiguration, false);
    copyOccupiedArray(this.mTmpOccupied);
    int[] arrayOfInt = findNearestArea(paramInt1, paramInt2, paramInt5, paramInt6, new int[2]);
    if (!rearrangementExists(arrayOfInt[0], arrayOfInt[1], paramInt5, paramInt6, paramArrayOfInt, paramView, paramItemConfiguration))
    {
      if ((paramInt5 > paramInt3) && ((paramInt4 == paramInt6) || (paramBoolean))) {
        return simpleSwap(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5 - 1, paramInt6, paramArrayOfInt, paramView, false, paramItemConfiguration);
      }
      if (paramInt6 > paramInt4) {
        return simpleSwap(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6 - 1, paramArrayOfInt, paramView, true, paramItemConfiguration);
      }
      paramItemConfiguration.isSolution = false;
      return paramItemConfiguration;
    }
    paramItemConfiguration.isSolution = true;
    paramItemConfiguration.dragViewX = arrayOfInt[0];
    paramItemConfiguration.dragViewY = arrayOfInt[1];
    paramItemConfiguration.dragViewSpanX = paramInt5;
    paramItemConfiguration.dragViewSpanY = paramInt6;
    return paramItemConfiguration;
  }
  
  void visualizeDropLocation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, Point paramPoint, Rect paramRect)
  {
    int i = this.mDragCell[0];
    int j = this.mDragCell[1];
    if ((paramBitmap == null) && (paramView == null)) {}
    while ((paramInt3 == i) && (paramInt4 == j)) {
      return;
    }
    this.mDragCell[0] = paramInt3;
    this.mDragCell[1] = paramInt4;
    int[] arrayOfInt = this.mTmpPoint;
    cellToPoint(paramInt3, paramInt4, arrayOfInt);
    int k = arrayOfInt[0];
    int m = arrayOfInt[1];
    int i1;
    int n;
    if ((paramView != null) && (paramPoint == null))
    {
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
      int i6 = k + localMarginLayoutParams.leftMargin;
      i1 = m + localMarginLayoutParams.topMargin + (paramView.getHeight() - paramBitmap.getHeight()) / 2;
      n = i6 + (paramInt5 * this.mCellWidth + (paramInt5 - 1) * this.mWidthGap - paramBitmap.getWidth()) / 2;
    }
    for (;;)
    {
      int i2 = this.mDragOutlineCurrent;
      this.mDragOutlineAnims[i2].animateOut();
      this.mDragOutlineCurrent = ((i2 + 1) % this.mDragOutlines.length);
      Rect localRect = this.mDragOutlines[this.mDragOutlineCurrent];
      int i3 = n + paramBitmap.getWidth();
      int i4 = i1 + paramBitmap.getHeight();
      localRect.set(n, i1, i3, i4);
      if (paramBoolean) {
        cellToRect(paramInt3, paramInt4, paramInt5, paramInt6, localRect);
      }
      this.mDragOutlineAnims[this.mDragOutlineCurrent].setTag(paramBitmap);
      this.mDragOutlineAnims[this.mDragOutlineCurrent].animateIn();
      return;
      if ((paramPoint != null) && (paramRect != null))
      {
        n = k + (paramPoint.x + (paramInt5 * this.mCellWidth + (paramInt5 - 1) * this.mWidthGap - paramRect.width()) / 2);
        int i5 = getShortcutsAndWidgets().getCellContentHeight();
        i1 = m + ((int)Math.max(0.0F, (this.mCellHeight - i5) / 2.0F) + paramPoint.y);
      }
      else
      {
        n = k + (paramInt5 * this.mCellWidth + (paramInt5 - 1) * this.mWidthGap - paramBitmap.getWidth()) / 2;
        i1 = m + (paramInt6 * this.mCellHeight + (paramInt6 - 1) * this.mHeightGap - paramBitmap.getHeight()) / 2;
      }
    }
  }
  
  private class CellAndSpan
  {
    int spanX;
    int spanY;
    int x;
    int y;
    
    public CellAndSpan() {}
    
    public CellAndSpan(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.x = paramInt1;
      this.y = paramInt2;
      this.spanX = paramInt3;
      this.spanY = paramInt4;
    }
    
    public void copy(CellAndSpan paramCellAndSpan)
    {
      paramCellAndSpan.x = this.x;
      paramCellAndSpan.y = this.y;
      paramCellAndSpan.spanX = this.spanX;
      paramCellAndSpan.spanY = this.spanY;
    }
    
    public String toString()
    {
      return "(" + this.x + ", " + this.y + ": " + this.spanX + ", " + this.spanY + ")";
    }
  }
  
  static final class CellInfo
  {
    View cell;
    int cellX = -1;
    int cellY = -1;
    long screenId;
    int spanX;
    int spanY;
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Cell[view=");
      if (this.cell == null) {}
      for (Object localObject = "null";; localObject = this.cell.getClass()) {
        return localObject + ", x=" + this.cellX + ", y=" + this.cellY + "]";
      }
    }
  }
  
  private class ItemConfiguration
  {
    int dragViewSpanX;
    int dragViewSpanY;
    int dragViewX;
    int dragViewY;
    boolean isSolution = false;
    HashMap<View, CellLayout.CellAndSpan> map = new HashMap();
    private HashMap<View, CellLayout.CellAndSpan> savedMap = new HashMap();
    ArrayList<View> sortedViews = new ArrayList();
    
    private ItemConfiguration() {}
    
    void add(View paramView, CellLayout.CellAndSpan paramCellAndSpan)
    {
      this.map.put(paramView, paramCellAndSpan);
      this.savedMap.put(paramView, new CellLayout.CellAndSpan(CellLayout.this));
      this.sortedViews.add(paramView);
    }
    
    int area()
    {
      return this.dragViewSpanX * this.dragViewSpanY;
    }
    
    void restore()
    {
      Iterator localIterator = this.savedMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        View localView = (View)localIterator.next();
        ((CellLayout.CellAndSpan)this.savedMap.get(localView)).copy((CellLayout.CellAndSpan)this.map.get(localView));
      }
    }
    
    void save()
    {
      Iterator localIterator = this.map.keySet().iterator();
      while (localIterator.hasNext())
      {
        View localView = (View)localIterator.next();
        ((CellLayout.CellAndSpan)this.map.get(localView)).copy((CellLayout.CellAndSpan)this.savedMap.get(localView));
      }
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    public boolean canReorder = true;
    @ViewDebug.ExportedProperty
    public int cellHSpan;
    @ViewDebug.ExportedProperty
    public int cellVSpan;
    @ViewDebug.ExportedProperty
    public int cellX;
    @ViewDebug.ExportedProperty
    public int cellY;
    boolean dropped;
    public boolean isFullscreen = false;
    public boolean isLockedToGrid = true;
    public int tmpCellX;
    public int tmpCellY;
    public boolean useTmpCoords;
    @ViewDebug.ExportedProperty
    int x;
    @ViewDebug.ExportedProperty
    int y;
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(-1);
      this.cellX = paramInt1;
      this.cellY = paramInt2;
      this.cellHSpan = paramInt3;
      this.cellVSpan = paramInt4;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      this.cellHSpan = 1;
      this.cellVSpan = 1;
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
      this.cellHSpan = 1;
      this.cellVSpan = 1;
    }
    
    public int getHeight()
    {
      return this.height;
    }
    
    public int getWidth()
    {
      return this.width;
    }
    
    public int getX()
    {
      return this.x;
    }
    
    public int getY()
    {
      return this.y;
    }
    
    public void setHeight(int paramInt)
    {
      this.height = paramInt;
    }
    
    public void setWidth(int paramInt)
    {
      this.width = paramInt;
    }
    
    public void setX(int paramInt)
    {
      this.x = paramInt;
    }
    
    public void setY(int paramInt)
    {
      this.y = paramInt;
    }
    
    public void setup(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
    {
      int i;
      int j;
      int k;
      if (this.isLockedToGrid)
      {
        i = this.cellHSpan;
        j = this.cellVSpan;
        if (!this.useTmpCoords) {
          break label145;
        }
        k = this.tmpCellX;
        if (!this.useTmpCoords) {
          break label154;
        }
      }
      label145:
      label154:
      for (int m = this.tmpCellY;; m = this.cellY)
      {
        if (paramBoolean) {
          k = paramInt5 - k - this.cellHSpan;
        }
        this.width = (i * paramInt1 + paramInt3 * (i - 1) - this.leftMargin - this.rightMargin);
        this.height = (j * paramInt2 + paramInt4 * (j - 1) - this.topMargin - this.bottomMargin);
        this.x = (k * (paramInt1 + paramInt3) + this.leftMargin);
        this.y = (m * (paramInt2 + paramInt4) + this.topMargin);
        return;
        k = this.cellX;
        break;
      }
    }
    
    public String toString()
    {
      return "(" + this.cellX + ", " + this.cellY + ")";
    }
  }
  
  class ReorderHintAnimation
  {
    Animator a;
    View child;
    float finalDeltaX;
    float finalDeltaY;
    float finalScale;
    float initDeltaX;
    float initDeltaY;
    float initScale;
    
    public ReorderHintAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      CellLayout.this.regionToCenterPoint(paramInt1, paramInt2, paramInt5, paramInt6, CellLayout.this.mTmpPoint);
      int i = CellLayout.this.mTmpPoint[0];
      int j = CellLayout.this.mTmpPoint[1];
      CellLayout.this.regionToCenterPoint(paramInt3, paramInt4, paramInt5, paramInt6, CellLayout.this.mTmpPoint);
      int k = CellLayout.this.mTmpPoint[0];
      int m = CellLayout.this.mTmpPoint[1];
      int n = k - i;
      int i1 = m - j;
      this.finalDeltaX = 0.0F;
      this.finalDeltaY = 0.0F;
      if ((n == i1) && (n == 0)) {}
      for (;;)
      {
        this.initDeltaX = paramView.getTranslationX();
        this.initDeltaY = paramView.getTranslationY();
        this.finalScale = (CellLayout.this.getChildrenScale() - 4.0F / paramView.getWidth());
        this.initScale = paramView.getScaleX();
        this.child = paramView;
        return;
        if (i1 == 0)
        {
          this.finalDeltaX = (-Math.signum(n) * CellLayout.this.mReorderHintAnimationMagnitude);
        }
        else if (n == 0)
        {
          this.finalDeltaY = (-Math.signum(i1) * CellLayout.this.mReorderHintAnimationMagnitude);
        }
        else
        {
          double d = Math.atan(i1 / n);
          this.finalDeltaX = ((int)(-Math.signum(n) * Math.abs(Math.cos(d) * CellLayout.this.mReorderHintAnimationMagnitude)));
          this.finalDeltaY = ((int)(-Math.signum(i1) * Math.abs(Math.sin(d) * CellLayout.this.mReorderHintAnimationMagnitude)));
        }
      }
    }
    
    private void cancel()
    {
      if (this.a != null) {
        this.a.cancel();
      }
    }
    
    private void completeAnimationImmediately()
    {
      if (this.a != null) {
        this.a.cancel();
      }
      AnimatorSet localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
      this.a = localAnimatorSet;
      Animator[] arrayOfAnimator = new Animator[4];
      View localView1 = this.child;
      float[] arrayOfFloat1 = new float[1];
      arrayOfFloat1[0] = CellLayout.this.getChildrenScale();
      arrayOfAnimator[0] = LauncherAnimUtils.ofFloat(localView1, "scaleX", arrayOfFloat1);
      View localView2 = this.child;
      float[] arrayOfFloat2 = new float[1];
      arrayOfFloat2[0] = CellLayout.this.getChildrenScale();
      arrayOfAnimator[1] = LauncherAnimUtils.ofFloat(localView2, "scaleY", arrayOfFloat2);
      arrayOfAnimator[2] = LauncherAnimUtils.ofFloat(this.child, "translationX", new float[] { 0.0F });
      arrayOfAnimator[3] = LauncherAnimUtils.ofFloat(this.child, "translationY", new float[] { 0.0F });
      localAnimatorSet.playTogether(arrayOfAnimator);
      localAnimatorSet.setDuration(150L);
      localAnimatorSet.setInterpolator(new DecelerateInterpolator(1.5F));
      localAnimatorSet.start();
    }
    
    void animate()
    {
      if (CellLayout.this.mShakeAnimators.containsKey(this.child))
      {
        ((ReorderHintAnimation)CellLayout.this.mShakeAnimators.get(this.child)).cancel();
        CellLayout.this.mShakeAnimators.remove(this.child);
        if ((this.finalDeltaX == 0.0F) && (this.finalDeltaY == 0.0F)) {
          completeAnimationImmediately();
        }
      }
      while ((this.finalDeltaX == 0.0F) && (this.finalDeltaY == 0.0F)) {
        return;
      }
      ValueAnimator localValueAnimator = LauncherAnimUtils.ofFloat(this.child, new float[] { 0.0F, 1.0F });
      this.a = localValueAnimator;
      localValueAnimator.setRepeatMode(2);
      localValueAnimator.setRepeatCount(-1);
      localValueAnimator.setDuration(300L);
      localValueAnimator.setStartDelay((int)(60.0D * Math.random()));
      localValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          float f1 = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          float f2 = f1 * CellLayout.ReorderHintAnimation.this.finalDeltaX + (1.0F - f1) * CellLayout.ReorderHintAnimation.this.initDeltaX;
          float f3 = f1 * CellLayout.ReorderHintAnimation.this.finalDeltaY + (1.0F - f1) * CellLayout.ReorderHintAnimation.this.initDeltaY;
          CellLayout.ReorderHintAnimation.this.child.setTranslationX(f2);
          CellLayout.ReorderHintAnimation.this.child.setTranslationY(f3);
          float f4 = f1 * CellLayout.ReorderHintAnimation.this.finalScale + (1.0F - f1) * CellLayout.ReorderHintAnimation.this.initScale;
          CellLayout.ReorderHintAnimation.this.child.setScaleX(f4);
          CellLayout.ReorderHintAnimation.this.child.setScaleY(f4);
        }
      });
      localValueAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationRepeat(Animator paramAnonymousAnimator)
        {
          CellLayout.ReorderHintAnimation.this.initDeltaX = 0.0F;
          CellLayout.ReorderHintAnimation.this.initDeltaY = 0.0F;
          CellLayout.ReorderHintAnimation.this.initScale = CellLayout.this.getChildrenScale();
        }
      });
      CellLayout.this.mShakeAnimators.put(this.child, this);
      localValueAnimator.start();
    }
  }
  
  private class ViewCluster
  {
    int[] bottomEdge = new int[CellLayout.this.mCountX];
    boolean bottomEdgeDirty;
    Rect boundingRect = new Rect();
    boolean boundingRectDirty;
    PositionComparator comparator = new PositionComparator();
    CellLayout.ItemConfiguration config;
    int[] leftEdge = new int[CellLayout.this.mCountY];
    boolean leftEdgeDirty;
    int[] rightEdge = new int[CellLayout.this.mCountY];
    boolean rightEdgeDirty;
    int[] topEdge = new int[CellLayout.this.mCountX];
    boolean topEdgeDirty;
    ArrayList<View> views;
    
    public ViewCluster(CellLayout.ItemConfiguration paramItemConfiguration)
    {
      this.views = ((ArrayList)paramItemConfiguration.clone());
      Object localObject;
      this.config = localObject;
      resetEdges();
    }
    
    public void addView(View paramView)
    {
      this.views.add(paramView);
      resetEdges();
    }
    
    void computeEdge(int paramInt, int[] paramArrayOfInt)
    {
      int i = this.views.size();
      int j = 0;
      if (j < i)
      {
        CellLayout.CellAndSpan localCellAndSpan = (CellLayout.CellAndSpan)this.config.map.get(this.views.get(j));
        switch (paramInt)
        {
        }
        for (;;)
        {
          j++;
          break;
          int i4 = localCellAndSpan.x;
          for (int i5 = localCellAndSpan.y; i5 < localCellAndSpan.y + localCellAndSpan.spanY; i5++) {
            if ((i4 < paramArrayOfInt[i5]) || (paramArrayOfInt[i5] < 0)) {
              paramArrayOfInt[i5] = i4;
            }
          }
          continue;
          int i2 = localCellAndSpan.x + localCellAndSpan.spanX;
          for (int i3 = localCellAndSpan.y; i3 < localCellAndSpan.y + localCellAndSpan.spanY; i3++) {
            if (i2 > paramArrayOfInt[i3]) {
              paramArrayOfInt[i3] = i2;
            }
          }
          continue;
          int n = localCellAndSpan.y;
          for (int i1 = localCellAndSpan.x; i1 < localCellAndSpan.x + localCellAndSpan.spanX; i1++) {
            if ((n < paramArrayOfInt[i1]) || (paramArrayOfInt[i1] < 0)) {
              paramArrayOfInt[i1] = n;
            }
          }
          continue;
          int k = localCellAndSpan.y + localCellAndSpan.spanY;
          for (int m = localCellAndSpan.x; m < localCellAndSpan.x + localCellAndSpan.spanX; m++) {
            if (k > paramArrayOfInt[m]) {
              paramArrayOfInt[m] = k;
            }
          }
        }
      }
    }
    
    public int[] getBottomEdge()
    {
      if (this.bottomEdgeDirty) {
        computeEdge(3, this.bottomEdge);
      }
      return this.bottomEdge;
    }
    
    public Rect getBoundingRect()
    {
      if (this.boundingRectDirty)
      {
        int i = 1;
        Iterator localIterator = this.views.iterator();
        while (localIterator.hasNext())
        {
          View localView = (View)localIterator.next();
          CellLayout.CellAndSpan localCellAndSpan = (CellLayout.CellAndSpan)this.config.map.get(localView);
          if (i != 0)
          {
            this.boundingRect.set(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.x + localCellAndSpan.spanX, localCellAndSpan.y + localCellAndSpan.spanY);
            i = 0;
          }
          else
          {
            this.boundingRect.union(localCellAndSpan.x, localCellAndSpan.y, localCellAndSpan.x + localCellAndSpan.spanX, localCellAndSpan.y + localCellAndSpan.spanY);
          }
        }
      }
      return this.boundingRect;
    }
    
    public int[] getEdge(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return getBottomEdge();
      case 0: 
        return getLeftEdge();
      case 2: 
        return getRightEdge();
      }
      return getTopEdge();
    }
    
    public int[] getLeftEdge()
    {
      if (this.leftEdgeDirty) {
        computeEdge(0, this.leftEdge);
      }
      return this.leftEdge;
    }
    
    public int[] getRightEdge()
    {
      if (this.rightEdgeDirty) {
        computeEdge(2, this.rightEdge);
      }
      return this.rightEdge;
    }
    
    public int[] getTopEdge()
    {
      if (this.topEdgeDirty) {
        computeEdge(1, this.topEdge);
      }
      return this.topEdge;
    }
    
    boolean isViewTouchingEdge(View paramView, int paramInt)
    {
      boolean bool = true;
      CellLayout.CellAndSpan localCellAndSpan = (CellLayout.CellAndSpan)this.config.map.get(paramView);
      int[] arrayOfInt = getEdge(paramInt);
      switch (paramInt)
      {
      }
      label58:
      label238:
      for (;;)
      {
        bool = false;
        return bool;
        for (int m = localCellAndSpan.y; m < localCellAndSpan.y + localCellAndSpan.spanY; m++) {
          if (arrayOfInt[m] == localCellAndSpan.x + localCellAndSpan.spanX) {
            break label58;
          }
        }
        for (int k = localCellAndSpan.y; k < localCellAndSpan.y + localCellAndSpan.spanY; k++) {
          if (arrayOfInt[k] == localCellAndSpan.x) {
            break label58;
          }
        }
        for (int j = localCellAndSpan.x; j < localCellAndSpan.x + localCellAndSpan.spanX; j++) {
          if (arrayOfInt[j] == localCellAndSpan.y + localCellAndSpan.spanY) {
            break label58;
          }
        }
        for (int i = localCellAndSpan.x;; i++)
        {
          if (i >= localCellAndSpan.x + localCellAndSpan.spanX) {
            break label238;
          }
          if (arrayOfInt[i] == localCellAndSpan.y) {
            break;
          }
        }
      }
    }
    
    void resetEdges()
    {
      for (int i = 0; i < CellLayout.this.mCountX; i++)
      {
        this.topEdge[i] = -1;
        this.bottomEdge[i] = -1;
      }
      for (int j = 0; j < CellLayout.this.mCountY; j++)
      {
        this.leftEdge[j] = -1;
        this.rightEdge[j] = -1;
      }
      this.leftEdgeDirty = true;
      this.rightEdgeDirty = true;
      this.bottomEdgeDirty = true;
      this.topEdgeDirty = true;
      this.boundingRectDirty = true;
    }
    
    void shift(int paramInt1, int paramInt2)
    {
      Iterator localIterator = this.views.iterator();
      while (localIterator.hasNext())
      {
        View localView = (View)localIterator.next();
        CellLayout.CellAndSpan localCellAndSpan = (CellLayout.CellAndSpan)this.config.map.get(localView);
        switch (paramInt1)
        {
        default: 
          localCellAndSpan.y = (paramInt2 + localCellAndSpan.y);
          break;
        case 0: 
          localCellAndSpan.x -= paramInt2;
          break;
        case 2: 
          localCellAndSpan.x = (paramInt2 + localCellAndSpan.x);
          break;
        case 1: 
          localCellAndSpan.y -= paramInt2;
        }
      }
      resetEdges();
    }
    
    public void sortConfigurationForEdgePush(int paramInt)
    {
      this.comparator.whichEdge = paramInt;
      Collections.sort(this.config.sortedViews, this.comparator);
    }
    
    class PositionComparator
      implements Comparator<View>
    {
      int whichEdge = 0;
      
      PositionComparator() {}
      
      public int compare(View paramView1, View paramView2)
      {
        CellLayout.CellAndSpan localCellAndSpan1 = (CellLayout.CellAndSpan)CellLayout.ViewCluster.this.config.map.get(paramView1);
        CellLayout.CellAndSpan localCellAndSpan2 = (CellLayout.CellAndSpan)CellLayout.ViewCluster.this.config.map.get(paramView2);
        switch (this.whichEdge)
        {
        default: 
          return localCellAndSpan1.y - localCellAndSpan2.y;
        case 0: 
          return localCellAndSpan2.x + localCellAndSpan2.spanX - (localCellAndSpan1.x + localCellAndSpan1.spanX);
        case 2: 
          return localCellAndSpan1.x - localCellAndSpan2.x;
        }
        return localCellAndSpan2.y + localCellAndSpan2.spanY - (localCellAndSpan1.y + localCellAndSpan1.spanY);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.CellLayout
 * JD-Core Version:    0.7.0.1
 */