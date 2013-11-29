package com.android.launcher3;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Workspace
  extends SmoothPagedView
  implements View.OnTouchListener, ViewGroup.OnHierarchyChangeListener, DragController.DragListener, DragScroller, DragSource, DropTarget, Insettable, LauncherTransitionable
{
  static Rect mLandscapeCellLayoutMetrics = null;
  static Rect mPortraitCellLayoutMetrics = null;
  private static boolean sAccessibilityEnabled;
  private boolean mAddToExistingFolderOnDrop = false;
  boolean mAnimatingViewIntoPlace = false;
  private Drawable mBackground;
  private float mBackgroundAlpha = 0.0F;
  private ValueAnimator mBackgroundFadeInAnimation;
  private ValueAnimator mBackgroundFadeOutAnimation;
  private final Runnable mBindPages = new Runnable()
  {
    public void run()
    {
      Workspace.this.mLauncher.getModel().bindRemainingSynchronousPages();
    }
  };
  private int mCameraDistance;
  boolean mChildrenLayersEnabled = true;
  private float mChildrenOutlineAlpha = 0.0F;
  private ObjectAnimator mChildrenOutlineFadeInAnimation;
  private ObjectAnimator mChildrenOutlineFadeOutAnimation;
  private boolean mCreateUserFolderOnDrop = false;
  private float mCurrentScale;
  Launcher.CustomContentCallbacks mCustomContentCallbacks;
  private String mCustomContentDescription = "";
  private long mCustomContentShowTime = -1L;
  boolean mCustomContentShowing;
  private int mDefaultPage;
  private boolean mDeferDropAfterUninstall;
  private Runnable mDeferredAction;
  private Runnable mDelayedResizeRunnable;
  private Runnable mDelayedSnapToPageRunnable;
  private Point mDisplaySize = new Point();
  private DragController mDragController;
  private DropTarget.DragEnforcer mDragEnforcer;
  private FolderIcon.FolderRingAnimator mDragFolderRingAnimator = null;
  private CellLayout.CellInfo mDragInfo;
  private int mDragMode = 0;
  private Bitmap mDragOutline = null;
  private FolderIcon mDragOverFolderIcon = null;
  private int mDragOverX = -1;
  private int mDragOverY = -1;
  private CellLayout mDragOverlappingLayout = null;
  private ShortcutAndWidgetContainer mDragSourceInternal;
  private CellLayout mDragTargetLayout = null;
  private float[] mDragViewVisualCenter = new float[2];
  boolean mDrawBackground = true;
  private CellLayout mDropToLayout = null;
  private final Alarm mFolderCreationAlarm = new Alarm();
  private IconCache mIconCache;
  private boolean mInScrollArea = false;
  boolean mIsDragOccuring = false;
  private boolean mIsSwitchingState = false;
  private int mLastChildCount = -1;
  private float mLastCustomContentScrollProgress = -1.0F;
  private float mLastOverscrollPivotX;
  private int mLastReorderX = -1;
  private int mLastReorderY = -1;
  private Launcher mLauncher;
  private LayoutTransition mLayoutTransition;
  private float mMaxDistanceForFolderCreation;
  private float[] mNewAlphas;
  private float[] mNewBackgroundAlphas;
  private float mNewScale;
  private float[] mOldAlphas;
  private float[] mOldBackgroundAlphas;
  private int mOriginalDefaultPage;
  private HolographicOutlineHelper mOutlineHelper;
  private boolean mOverscrollTransformsSet;
  private int mOverviewModePageOffset;
  private float mOverviewModeShrinkFactor;
  private Runnable mRemoveEmptyScreenRunnable;
  private final Alarm mReorderAlarm = new Alarm();
  private final ArrayList<Integer> mRestoredPages = new ArrayList();
  private float mSavedRotationY;
  private int mSavedScrollX;
  private SparseArray<Parcelable> mSavedStates;
  private float mSavedTranslationX;
  private ArrayList<Long> mScreenOrder = new ArrayList();
  private SpringLoadedDragController mSpringLoadedDragController;
  private float mSpringLoadedShrinkFactor;
  private State mState = State.NORMAL;
  private boolean mStripScreensOnPageStopMoving = false;
  private int[] mTargetCell = new int[2];
  private int[] mTempCell = new int[2];
  private float[] mTempCellLayoutCenterCoordinates = new float[2];
  private int[] mTempEstimate = new int[2];
  private Matrix mTempInverseMatrix = new Matrix();
  private int[] mTempPt = new int[2];
  private final Rect mTempRect = new Rect();
  private int[] mTempVisiblePagesRange = new int[2];
  private final int[] mTempXY = new int[2];
  private long mTouchDownTime = -1L;
  private float mTransitionProgress;
  private boolean mUninstallSuccessful;
  private final WallpaperManager mWallpaperManager;
  WallpaperOffsetInterpolator mWallpaperOffset;
  private IBinder mWindowToken;
  private boolean mWorkspaceFadeInAdjacentScreens;
  private HashMap<Long, CellLayout> mWorkspaceScreens = new HashMap();
  private float mXDown;
  private float mYDown;
  private final ZoomInInterpolator mZoomInInterpolator = new ZoomInInterpolator();
  
  public Workspace(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Workspace(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mContentIsRefreshable = false;
    this.mOutlineHelper = HolographicOutlineHelper.obtain(paramContext);
    this.mDragEnforcer = new DropTarget.DragEnforcer(paramContext);
    setDataIsReady();
    this.mLauncher = ((Launcher)paramContext);
    Resources localResources = getResources();
    this.mWorkspaceFadeInAdjacentScreens = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().shouldFadeAdjacentWorkspaceScreens();
    this.mFadeInAdjacentScreens = false;
    this.mWallpaperManager = WallpaperManager.getInstance(paramContext);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Workspace, paramInt, 0);
    this.mSpringLoadedShrinkFactor = (localResources.getInteger(2131427337) / 100.0F);
    this.mOverviewModeShrinkFactor = (localResources.getInteger(2131427338) / 100.0F);
    this.mOverviewModePageOffset = localResources.getDimensionPixelSize(2131689528);
    this.mCameraDistance = localResources.getInteger(2131427359);
    int i = localTypedArray.getInt(0, 1);
    this.mDefaultPage = i;
    this.mOriginalDefaultPage = i;
    localTypedArray.recycle();
    setOnHierarchyChangeListener(this);
    setHapticFeedbackEnabled(false);
    initWorkspace();
    setMotionEventSplittingEnabled(true);
    setImportantForAccessibility(1);
  }
  
  private void animateBackgroundGradient(float paramFloat, boolean paramBoolean)
  {
    if (this.mBackground == null) {}
    float f;
    do
    {
      return;
      if (this.mBackgroundFadeInAnimation != null)
      {
        this.mBackgroundFadeInAnimation.cancel();
        this.mBackgroundFadeInAnimation = null;
      }
      if (this.mBackgroundFadeOutAnimation != null)
      {
        this.mBackgroundFadeOutAnimation.cancel();
        this.mBackgroundFadeOutAnimation = null;
      }
      f = getBackgroundAlpha();
    } while (paramFloat == f);
    if (paramBoolean)
    {
      this.mBackgroundFadeOutAnimation = LauncherAnimUtils.ofFloat(this, new float[] { f, paramFloat });
      this.mBackgroundFadeOutAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          Workspace.this.setBackgroundAlpha(((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
        }
      });
      this.mBackgroundFadeOutAnimation.setInterpolator(new DecelerateInterpolator(1.5F));
      this.mBackgroundFadeOutAnimation.setDuration(350L);
      this.mBackgroundFadeOutAnimation.start();
      return;
    }
    setBackgroundAlpha(paramFloat);
  }
  
  private void cleanupAddToFolder()
  {
    if (this.mDragOverFolderIcon != null)
    {
      this.mDragOverFolderIcon.onDragExit(null);
      this.mDragOverFolderIcon = null;
    }
  }
  
  private void cleanupFolderCreation()
  {
    if (this.mDragFolderRingAnimator != null)
    {
      this.mDragFolderRingAnimator.animateToNaturalState();
      this.mDragFolderRingAnimator = null;
    }
    this.mFolderCreationAlarm.setOnAlarmListener(null);
    this.mFolderCreationAlarm.cancelAlarm();
  }
  
  private void cleanupReorder(boolean paramBoolean)
  {
    if (paramBoolean) {
      this.mReorderAlarm.cancelAlarm();
    }
    this.mLastReorderX = -1;
    this.mLastReorderY = -1;
  }
  
  private void convertFinalScreenToEmptyScreenIfNecessary()
  {
    if ((hasExtraEmptyScreen()) || (this.mScreenOrder.size() == 0)) {}
    long l;
    CellLayout localCellLayout;
    do
    {
      do
      {
        return;
        l = ((Long)this.mScreenOrder.get(-1 + this.mScreenOrder.size())).longValue();
      } while (l == -301L);
      localCellLayout = (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(l));
    } while ((localCellLayout.getShortcutsAndWidgets().getChildCount() != 0) || (localCellLayout.isDropPending()));
    this.mWorkspaceScreens.remove(Long.valueOf(l));
    this.mScreenOrder.remove(Long.valueOf(l));
    this.mWorkspaceScreens.put(Long.valueOf(-201L), localCellLayout);
    this.mScreenOrder.add(Long.valueOf(-201L));
  }
  
  private Bitmap createDragOutline(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    int i = getResources().getColor(2131230769);
    Bitmap localBitmap = Bitmap.createBitmap(paramInt2, paramInt3, Bitmap.Config.ARGB_8888);
    paramCanvas.setBitmap(localBitmap);
    Rect localRect1 = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    float f = Math.min((paramInt2 - paramInt1) / paramBitmap.getWidth(), (paramInt3 - paramInt1) / paramBitmap.getHeight());
    int j = (int)(f * paramBitmap.getWidth());
    int k = (int)(f * paramBitmap.getHeight());
    Rect localRect2 = new Rect(0, 0, j, k);
    localRect2.offset((paramInt2 - j) / 2, (paramInt3 - k) / 2);
    paramCanvas.drawBitmap(paramBitmap, localRect1, localRect2, null);
    this.mOutlineHelper.applyMediumExpensiveOutlineWithBlur(localBitmap, paramCanvas, i, i, paramBoolean);
    paramCanvas.setBitmap(null);
    return localBitmap;
  }
  
  private Bitmap createDragOutline(View paramView, Canvas paramCanvas, int paramInt)
  {
    int i = getResources().getColor(2131230769);
    Bitmap localBitmap = Bitmap.createBitmap(paramInt + paramView.getWidth(), paramInt + paramView.getHeight(), Bitmap.Config.ARGB_8888);
    paramCanvas.setBitmap(localBitmap);
    drawDragView(paramView, paramCanvas, paramInt, true);
    this.mOutlineHelper.applyMediumExpensiveOutlineWithBlur(localBitmap, paramCanvas, i, i);
    paramCanvas.setBitmap(null);
    return localBitmap;
  }
  
  private void drawDragView(View paramView, Canvas paramCanvas, int paramInt, boolean paramBoolean)
  {
    Rect localRect = this.mTempRect;
    paramView.getDrawingRect(localRect);
    paramCanvas.save();
    if (((paramView instanceof TextView)) && (paramBoolean))
    {
      Drawable localDrawable = ((TextView)paramView).getCompoundDrawables()[1];
      localRect.set(0, 0, paramInt + localDrawable.getIntrinsicWidth(), paramInt + localDrawable.getIntrinsicHeight());
      paramCanvas.translate(paramInt / 2, paramInt / 2);
      localDrawable.draw(paramCanvas);
      paramCanvas.restore();
      return;
    }
    int i;
    if ((paramView instanceof FolderIcon))
    {
      boolean bool2 = ((FolderIcon)paramView).getTextVisible();
      i = 0;
      if (bool2)
      {
        ((FolderIcon)paramView).setTextVisible(false);
        i = 1;
      }
    }
    for (;;)
    {
      paramCanvas.translate(-paramView.getScrollX() + paramInt / 2, -paramView.getScrollY() + paramInt / 2);
      paramCanvas.clipRect(localRect, Region.Op.REPLACE);
      paramView.draw(paramCanvas);
      if (i == 0) {
        break;
      }
      ((FolderIcon)paramView).setTextVisible(true);
      break;
      if ((paramView instanceof BubbleTextView))
      {
        BubbleTextView localBubbleTextView = (BubbleTextView)paramView;
        localRect.bottom = (-3 + localBubbleTextView.getExtendedPaddingTop() + localBubbleTextView.getLayout().getLineTop(0));
        i = 0;
      }
      else
      {
        boolean bool1 = paramView instanceof TextView;
        i = 0;
        if (bool1)
        {
          TextView localTextView = (TextView)paramView;
          localRect.bottom = (localTextView.getExtendedPaddingTop() - localTextView.getCompoundDrawablePadding() + localTextView.getLayout().getLineTop(0));
          i = 0;
        }
      }
    }
  }
  
  private void enableHwLayersOnVisiblePages()
  {
    if (this.mChildrenLayersEnabled)
    {
      int i = getChildCount();
      getVisiblePages(this.mTempVisiblePagesRange);
      int j = this.mTempVisiblePagesRange[0];
      int k = this.mTempVisiblePagesRange[1];
      int m;
      label70:
      CellLayout localCellLayout2;
      if (j == k)
      {
        if (k < i - 1) {
          k++;
        }
      }
      else
      {
        CellLayout localCellLayout1 = (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-301L));
        m = 0;
        if (m >= i) {
          return;
        }
        localCellLayout2 = (CellLayout)getPageAt(m);
        if ((localCellLayout2 == localCellLayout1) || (j > m) || (m > k) || (!shouldDrawChild(localCellLayout2))) {
          break label141;
        }
      }
      label141:
      for (boolean bool = true;; bool = false)
      {
        localCellLayout2.enableHardwareLayer(bool);
        m++;
        break label70;
        if (j <= 0) {
          break;
        }
        j--;
        break;
      }
    }
  }
  
  private void enableOverviewMode(boolean paramBoolean1, int paramInt, boolean paramBoolean2)
  {
    State localState = State.OVERVIEW;
    if (!paramBoolean1) {
      localState = State.NORMAL;
    }
    Animator localAnimator = getChangeStateAnimation(localState, paramBoolean2, 0, paramInt);
    if (localAnimator != null)
    {
      onTransitionPrepare();
      localAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          Workspace.this.onTransitionEnd();
        }
      });
      localAnimator.start();
    }
  }
  
  private void fadeAndRemoveEmptyScreen(int paramInt1, int paramInt2, final Runnable paramRunnable, final boolean paramBoolean)
  {
    PropertyValuesHolder localPropertyValuesHolder1 = PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0F });
    PropertyValuesHolder localPropertyValuesHolder2 = PropertyValuesHolder.ofFloat("backgroundAlpha", new float[] { 0.0F });
    final CellLayout localCellLayout = (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-201L));
    this.mRemoveEmptyScreenRunnable = new Runnable()
    {
      public void run()
      {
        if (Workspace.this.hasExtraEmptyScreen())
        {
          Workspace.this.mWorkspaceScreens.remove(Long.valueOf(-201L));
          Workspace.this.mScreenOrder.remove(Long.valueOf(-201L));
          Workspace.this.removeView(localCellLayout);
          if (paramBoolean) {
            Workspace.this.stripEmptyScreens();
          }
        }
      }
    };
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(localCellLayout, new PropertyValuesHolder[] { localPropertyValuesHolder1, localPropertyValuesHolder2 });
    localObjectAnimator.setDuration(paramInt2);
    localObjectAnimator.setStartDelay(paramInt1);
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (Workspace.this.mRemoveEmptyScreenRunnable != null) {
          Workspace.this.mRemoveEmptyScreenRunnable.run();
        }
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
    });
    localObjectAnimator.start();
  }
  
  private CellLayout findMatchingPageForDragOver(DragView paramDragView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = getChildCount();
    Object localObject = null;
    float f1 = 3.4028235E+38F;
    int j = 0;
    if (j < i)
    {
      if (((Long)this.mScreenOrder.get(j)).longValue() == -301L) {}
      for (;;)
      {
        j++;
        break;
        CellLayout localCellLayout = (CellLayout)getChildAt(j);
        float[] arrayOfFloat1 = { paramFloat1, paramFloat2 };
        localCellLayout.getMatrix().invert(this.mTempInverseMatrix);
        mapPointFromSelfToChild(localCellLayout, arrayOfFloat1, this.mTempInverseMatrix);
        if ((arrayOfFloat1[0] >= 0.0F) && (arrayOfFloat1[0] <= localCellLayout.getWidth()) && (arrayOfFloat1[1] >= 0.0F) && (arrayOfFloat1[1] <= localCellLayout.getHeight())) {
          return localCellLayout;
        }
        if (!paramBoolean)
        {
          float[] arrayOfFloat2 = this.mTempCellLayoutCenterCoordinates;
          arrayOfFloat2[0] = (localCellLayout.getWidth() / 2);
          arrayOfFloat2[1] = (localCellLayout.getHeight() / 2);
          mapPointFromChildToSelf(localCellLayout, arrayOfFloat2);
          arrayOfFloat1[0] = paramFloat1;
          arrayOfFloat1[1] = paramFloat2;
          float f2 = squaredDistance(arrayOfFloat1, arrayOfFloat2);
          if (f2 < f1)
          {
            f1 = f2;
            localObject = localCellLayout;
          }
        }
      }
    }
    return localObject;
  }
  
  private int[] findNearestArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, CellLayout paramCellLayout, int[] paramArrayOfInt)
  {
    return paramCellLayout.findNearestArea(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
  }
  
  static Rect getCellLayoutMetrics(Launcher paramLauncher, int paramInt)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    paramLauncher.getResources();
    Display localDisplay = paramLauncher.getWindowManager().getDefaultDisplay();
    Point localPoint1 = new Point();
    Point localPoint2 = new Point();
    localDisplay.getCurrentSizeRange(localPoint1, localPoint2);
    int i = (int)localDeviceProfile.numColumns;
    int j = (int)localDeviceProfile.numRows;
    int k = localPoint2.y;
    int m = localPoint1.y;
    if (paramInt == 0)
    {
      if (mLandscapeCellLayoutMetrics == null)
      {
        Rect localRect2 = localDeviceProfile.getWorkspacePadding(0);
        int i2 = k - localRect2.left - localRect2.right;
        int i3 = m - localRect2.top - localRect2.bottom;
        mLandscapeCellLayoutMetrics = new Rect();
        mLandscapeCellLayoutMetrics.set(localDeviceProfile.calculateCellWidth(i2, i), localDeviceProfile.calculateCellHeight(i3, j), 0, 0);
      }
      return mLandscapeCellLayoutMetrics;
    }
    if (paramInt == 1)
    {
      if (mPortraitCellLayoutMetrics == null)
      {
        Rect localRect1 = localDeviceProfile.getWorkspacePadding(1);
        int n = m - localRect1.left - localRect1.right;
        int i1 = k - localRect1.top - localRect1.bottom;
        mPortraitCellLayoutMetrics = new Rect();
        mPortraitCellLayoutMetrics.set(localDeviceProfile.calculateCellWidth(n, i), localDeviceProfile.calculateCellHeight(i1, j), 0, 0);
      }
      return mPortraitCellLayoutMetrics;
    }
    return null;
  }
  
  private float[] getDragViewVisualCenter(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null) {}
    for (float[] arrayOfFloat = new float[2];; arrayOfFloat = paramArrayOfFloat)
    {
      int i = paramInt1 + getResources().getDimensionPixelSize(2131689541);
      int j = paramInt2 + getResources().getDimensionPixelSize(2131689542);
      int k = i - paramInt3;
      int m = j - paramInt4;
      arrayOfFloat[0] = (k + paramDragView.getDragRegion().width() / 2);
      arrayOfFloat[1] = (m + paramDragView.getDragRegion().height() / 2);
      return arrayOfFloat;
    }
  }
  
  private void getFinalPositionForDropAnimation(int[] paramArrayOfInt1, float[] paramArrayOfFloat, DragView paramDragView, CellLayout paramCellLayout, ItemInfo paramItemInfo, int[] paramArrayOfInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = paramItemInfo.spanX;
    int j = paramItemInfo.spanY;
    Rect localRect = estimateItemPosition(paramCellLayout, paramItemInfo, paramArrayOfInt2[0], paramArrayOfInt2[1], i, j);
    paramArrayOfInt1[0] = localRect.left;
    paramArrayOfInt1[1] = localRect.top;
    setFinalTransitionTransform(paramCellLayout);
    float f1 = this.mLauncher.getDragLayer().getDescendantCoordRelativeToSelf(paramCellLayout, paramArrayOfInt1, true);
    resetTransitionTransform(paramCellLayout);
    float f2;
    if (paramBoolean2) {
      f2 = 1.0F * localRect.width() / paramDragView.getMeasuredWidth();
    }
    for (float f3 = 1.0F * localRect.height() / paramDragView.getMeasuredHeight();; f3 = 1.0F)
    {
      paramArrayOfInt1[0] = ((int)(paramArrayOfInt1[0] - (paramDragView.getMeasuredWidth() - f1 * localRect.width()) / 2.0F));
      paramArrayOfInt1[1] = ((int)(paramArrayOfInt1[1] - (paramDragView.getMeasuredHeight() - f1 * localRect.height()) / 2.0F));
      paramArrayOfFloat[0] = (f2 * f1);
      paramArrayOfFloat[1] = (f3 * f1);
      return;
      f2 = 1.0F;
    }
  }
  
  private void initAnimationArrays()
  {
    int i = getChildCount();
    if (this.mLastChildCount == i) {
      return;
    }
    this.mOldBackgroundAlphas = new float[i];
    this.mOldAlphas = new float[i];
    this.mNewBackgroundAlphas = new float[i];
    this.mNewAlphas = new float[i];
  }
  
  private boolean isDragWidget(DropTarget.DragObject paramDragObject)
  {
    return ((paramDragObject.dragInfo instanceof LauncherAppWidgetInfo)) || ((paramDragObject.dragInfo instanceof PendingAddWidgetInfo));
  }
  
  private boolean isExternalDragWidget(DropTarget.DragObject paramDragObject)
  {
    return (paramDragObject.dragSource != this) && (isDragWidget(paramDragObject));
  }
  
  private void manageFolderFeedback(ItemInfo paramItemInfo, CellLayout paramCellLayout, int[] paramArrayOfInt, float paramFloat, View paramView)
  {
    boolean bool1 = willCreateUserFolder(paramItemInfo, paramCellLayout, paramArrayOfInt, paramFloat, false);
    if ((this.mDragMode == 0) && (bool1) && (!this.mFolderCreationAlarm.alarmPending()))
    {
      this.mFolderCreationAlarm.setOnAlarmListener(new FolderCreationAlarmListener(paramCellLayout, paramArrayOfInt[0], paramArrayOfInt[1]));
      this.mFolderCreationAlarm.setAlarm(0L);
    }
    do
    {
      return;
      boolean bool2 = willAddToExistingUserFolder(paramItemInfo, paramCellLayout, paramArrayOfInt, paramFloat);
      if ((bool2) && (this.mDragMode == 0))
      {
        this.mDragOverFolderIcon = ((FolderIcon)paramView);
        this.mDragOverFolderIcon.onDragEnter(paramItemInfo);
        if (paramCellLayout != null) {
          paramCellLayout.clearDragOutlines();
        }
        setDragMode(2);
        return;
      }
      if ((this.mDragMode == 2) && (!bool2)) {
        setDragMode(0);
      }
    } while ((this.mDragMode != 1) || (bool1));
    setDragMode(0);
  }
  
  private void moveToScreen(int paramInt, boolean paramBoolean)
  {
    if (!isSmall())
    {
      if (!paramBoolean) {
        break label32;
      }
      snapToPage(paramInt);
    }
    for (;;)
    {
      View localView = getChildAt(paramInt);
      if (localView != null) {
        localView.requestFocus();
      }
      return;
      label32:
      setCurrentPage(paramInt);
    }
  }
  
  private void onDropExternal(int[] paramArrayOfInt, Object paramObject, CellLayout paramCellLayout, boolean paramBoolean, DropTarget.DragObject paramDragObject)
  {
    Runnable local13 = new Runnable()
    {
      public void run()
      {
        Workspace.this.removeExtraEmptyScreen(false, new Runnable()
        {
          public void run()
          {
            Workspace.this.mLauncher.exitSpringLoadedDragModeDelayed(true, 300, null);
          }
        });
      }
    };
    Object localObject1 = (ItemInfo)paramObject;
    int i = ((ItemInfo)localObject1).spanX;
    int j = ((ItemInfo)localObject1).spanY;
    if (this.mDragInfo != null)
    {
      i = this.mDragInfo.spanX;
      j = this.mDragInfo.spanY;
    }
    final long l1;
    long l2;
    AppWidgetHostView localAppWidgetHostView;
    if (this.mLauncher.isHotseatLayout(paramCellLayout))
    {
      l1 = -101L;
      l2 = getIdForScreen(paramCellLayout);
      if ((!this.mLauncher.isHotseatLayout(paramCellLayout)) && (l2 != getScreenIdForPageIndex(this.mCurrentPage)) && (this.mState != State.SPRING_LOADED)) {
        snapToScreenId(l2, null);
      }
      if (!(localObject1 instanceof PendingAddItemInfo)) {
        break label552;
      }
      final PendingAddItemInfo localPendingAddItemInfo = (PendingAddItemInfo)paramObject;
      int i4 = 1;
      if (localPendingAddItemInfo.itemType == 1)
      {
        this.mTargetCell = findNearestArea(paramArrayOfInt[0], paramArrayOfInt[1], i, j, paramCellLayout, this.mTargetCell);
        float f2 = paramCellLayout.getDistanceFromCell(this.mDragViewVisualCenter[0], this.mDragViewVisualCenter[1], this.mTargetCell);
        if ((willCreateUserFolder((ItemInfo)paramDragObject.dragInfo, paramCellLayout, this.mTargetCell, f2, true)) || (willAddToExistingUserFolder((ItemInfo)paramDragObject.dragInfo, paramCellLayout, this.mTargetCell, f2))) {
          i4 = 0;
        }
      }
      final ItemInfo localItemInfo = (ItemInfo)paramDragObject.dragInfo;
      int i5 = 0;
      if (i4 != 0)
      {
        int i8 = localItemInfo.spanX;
        int i9 = localItemInfo.spanY;
        if ((localItemInfo.minSpanX > 0) && (localItemInfo.minSpanY > 0))
        {
          i8 = localItemInfo.minSpanX;
          i9 = localItemInfo.minSpanY;
        }
        int[] arrayOfInt2 = new int[2];
        this.mTargetCell = paramCellLayout.createArea((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], i8, i9, ((ItemInfo)localObject1).spanX, ((ItemInfo)localObject1).spanY, null, this.mTargetCell, arrayOfInt2, 2);
        if (arrayOfInt2[0] == localItemInfo.spanX)
        {
          int i10 = arrayOfInt2[1];
          int i11 = localItemInfo.spanY;
          i5 = 0;
          if (i10 == i11) {}
        }
        else
        {
          i5 = 1;
        }
        localItemInfo.spanX = arrayOfInt2[0];
        localItemInfo.spanY = arrayOfInt2[1];
      }
      Runnable local14 = new Runnable()
      {
        public void run()
        {
          switch (localPendingAddItemInfo.itemType)
          {
          case 2: 
          case 3: 
          default: 
            throw new IllegalStateException("Unknown item type: " + localPendingAddItemInfo.itemType);
          case 4: 
            int[] arrayOfInt = new int[2];
            arrayOfInt[0] = localItemInfo.spanX;
            arrayOfInt[1] = localItemInfo.spanY;
            Workspace.this.mLauncher.addAppWidgetFromDrop((PendingAddWidgetInfo)localPendingAddItemInfo, l1, this.val$screenId, Workspace.this.mTargetCell, arrayOfInt, null);
            return;
          }
          Workspace.this.mLauncher.processShortcutFromDrop(localPendingAddItemInfo.componentName, l1, this.val$screenId, Workspace.this.mTargetCell, null);
        }
      };
      if (localPendingAddItemInfo.itemType != 4) {
        break label546;
      }
      localAppWidgetHostView = ((PendingAddWidgetInfo)localPendingAddItemInfo).boundWidget;
      label443:
      if (((localAppWidgetHostView instanceof AppWidgetHostView)) && (i5 != 0)) {
        AppWidgetResizeFrame.updateWidgetSizeRanges((AppWidgetHostView)localAppWidgetHostView, this.mLauncher, localItemInfo.spanX, localItemInfo.spanY);
      }
      int i6 = localPendingAddItemInfo.itemType;
      int i7 = 0;
      if (i6 == 4)
      {
        ComponentName localComponentName = ((PendingAddWidgetInfo)localPendingAddItemInfo).info.configure;
        i7 = 0;
        if (localComponentName != null) {
          i7 = 1;
        }
      }
      animateWidgetDrop((ItemInfo)localObject1, paramCellLayout, paramDragObject.dragView, local14, i7, localAppWidgetHostView, true);
    }
    label546:
    label552:
    Object localObject2;
    label672:
    float f1;
    int[] arrayOfInt1;
    DragView localDragView;
    Runnable localRunnable;
    do
    {
      return;
      l1 = -100L;
      break;
      localAppWidgetHostView = null;
      break label443;
      switch (((ItemInfo)localObject1).itemType)
      {
      default: 
        throw new IllegalStateException("Unknown item type: " + ((ItemInfo)localObject1).itemType);
      case 0: 
      case 1: 
        if ((((ItemInfo)localObject1).container == -1L) && ((localObject1 instanceof AppInfo)))
        {
          ShortcutInfo localShortcutInfo = new ShortcutInfo((AppInfo)localObject1);
          localObject1 = localShortcutInfo;
        }
        localObject2 = this.mLauncher.createShortcut(2130968595, paramCellLayout, (ShortcutInfo)localObject1);
        if (paramArrayOfInt == null) {
          break label788;
        }
        this.mTargetCell = findNearestArea(paramArrayOfInt[0], paramArrayOfInt[1], i, j, paramCellLayout, this.mTargetCell);
        f1 = paramCellLayout.getDistanceFromCell(this.mDragViewVisualCenter[0], this.mDragViewVisualCenter[1], this.mTargetCell);
        paramDragObject.postAnimationRunnable = local13;
        arrayOfInt1 = this.mTargetCell;
        localDragView = paramDragObject.dragView;
        localRunnable = paramDragObject.postAnimationRunnable;
      }
    } while ((createUserFolderIfNecessary((View)localObject2, l1, paramCellLayout, arrayOfInt1, f1, true, localDragView, localRunnable)) || (addToExistingFolderIfNecessary((View)localObject2, paramCellLayout, this.mTargetCell, f1, paramDragObject, true)));
    label788:
    if (paramArrayOfInt != null) {
      this.mTargetCell = paramCellLayout.createArea((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], 1, 1, 1, 1, null, this.mTargetCell, null, 2);
    }
    for (;;)
    {
      int k = this.mTargetCell[0];
      int m = this.mTargetCell[1];
      int n = ((ItemInfo)localObject1).spanX;
      int i1 = ((ItemInfo)localObject1).spanY;
      addInScreen((View)localObject2, l1, l2, k, m, n, i1, paramBoolean);
      paramCellLayout.onDropChild((View)localObject2);
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)((View)localObject2).getLayoutParams();
      paramCellLayout.getShortcutsAndWidgets().measureChild((View)localObject2);
      Launcher localLauncher = this.mLauncher;
      int i2 = localLayoutParams.cellX;
      int i3 = localLayoutParams.cellY;
      LauncherModel.addOrMoveItemInDatabase(localLauncher, (ItemInfo)localObject1, l1, l2, i2, i3);
      if (paramDragObject.dragView == null) {
        break;
      }
      setFinalTransitionTransform(paramCellLayout);
      this.mLauncher.getDragLayer().animateViewIntoPosition(paramDragObject.dragView, (View)localObject2, local13, this);
      resetTransitionTransform(paramCellLayout);
      return;
      localObject2 = FolderIcon.fromXml(2130968696, this.mLauncher, paramCellLayout, (FolderInfo)localObject1, this.mIconCache);
      break label672;
      paramCellLayout.findCellForSpan(this.mTargetCell, 1, 1);
    }
  }
  
  private void onResetScrollArea()
  {
    setCurrentDragOverlappingLayout(null);
    this.mInScrollArea = false;
  }
  
  private void onTransitionEnd()
  {
    this.mIsSwitchingState = false;
    updateChildrenLayersEnabled(false);
    if (!this.mWorkspaceFadeInAdjacentScreens) {
      for (int i = 0; i < getChildCount(); i++) {
        ((CellLayout)getChildAt(i)).setShortcutAndWidgetAlpha(1.0F);
      }
    }
    showCustomContentIfNecessary();
  }
  
  private void onTransitionPrepare()
  {
    this.mIsSwitchingState = true;
    invalidate();
    updateChildrenLayersEnabled(false);
    hideCustomContentIfNecessary();
  }
  
  private void setChildrenBackgroundAlphaMultipliers(float paramFloat)
  {
    for (int i = 0; i < getChildCount(); i++) {
      ((CellLayout)getChildAt(i)).setBackgroundAlphaMultiplier(paramFloat);
    }
  }
  
  private void setState(State paramState)
  {
    this.mState = paramState;
    updateInteractionForState();
    updateAccessibilityFlags();
  }
  
  private void setupLayoutTransition()
  {
    this.mLayoutTransition = new LayoutTransition();
    this.mLayoutTransition.enableTransitionType(3);
    this.mLayoutTransition.enableTransitionType(1);
    this.mLayoutTransition.disableTransitionType(2);
    this.mLayoutTransition.disableTransitionType(0);
    setLayoutTransition(this.mLayoutTransition);
  }
  
  private static float squaredDistance(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f1 = paramArrayOfFloat1[0] - paramArrayOfFloat2[0];
    float f2 = paramArrayOfFloat2[1] - paramArrayOfFloat2[1];
    return f1 * f1 + f2 * f2;
  }
  
  private void updateAccessibilityFlags()
  {
    if (this.mState == State.NORMAL) {}
    for (int i = 1;; i = 4)
    {
      setImportantForAccessibility(i);
      return;
    }
  }
  
  private void updateChildrenLayersEnabled(boolean paramBoolean)
  {
    boolean bool = true;
    int i;
    if ((this.mState == State.SMALL) || (this.mState == State.OVERVIEW) || (this.mIsSwitchingState))
    {
      i = bool;
      if ((!paramBoolean) && (i == 0) && (!this.mAnimatingViewIntoPlace) && (!isPageMoving())) {
        break label83;
      }
      label53:
      if (bool != this.mChildrenLayersEnabled)
      {
        this.mChildrenLayersEnabled = bool;
        if (!this.mChildrenLayersEnabled) {
          break label88;
        }
        enableHwLayersOnVisiblePages();
      }
    }
    for (;;)
    {
      return;
      i = 0;
      break;
      label83:
      bool = false;
      break label53;
      label88:
      for (int j = 0; j < getPageCount(); j++) {
        ((CellLayout)getChildAt(j)).enableHardwareLayer(false);
      }
    }
  }
  
  private void updatePageAlphaValues(int paramInt)
  {
    if ((this.mOverScrollX < 0) || (this.mOverScrollX > this.mMaxScrollX)) {}
    for (int i = 1; (this.mWorkspaceFadeInAdjacentScreens) && (this.mState == State.NORMAL) && (!this.mIsSwitchingState) && (i == 0); i = 0) {
      for (int j = 0; j < getChildCount(); j++)
      {
        CellLayout localCellLayout = (CellLayout)getChildAt(j);
        if (localCellLayout != null)
        {
          float f = 1.0F - Math.abs(getScrollProgress(paramInt, localCellLayout, j));
          localCellLayout.getShortcutsAndWidgets().setAlpha(f);
        }
      }
    }
  }
  
  private void updateShortcut(HashMap<ComponentName, AppInfo> paramHashMap, ItemInfo paramItemInfo, View paramView)
  {
    if (paramItemInfo.getIntent().getComponent() != null)
    {
      AppInfo localAppInfo = (AppInfo)paramHashMap.get(paramItemInfo.getIntent().getComponent());
      if ((localAppInfo != null) && (LauncherModel.isShortcutInfoUpdateable(paramItemInfo)))
      {
        ShortcutInfo localShortcutInfo = (ShortcutInfo)paramItemInfo;
        BubbleTextView localBubbleTextView = (BubbleTextView)paramView;
        localShortcutInfo.updateIcon(this.mIconCache);
        localShortcutInfo.title = localAppInfo.title.toString();
        localBubbleTextView.applyFromShortcutInfo(localShortcutInfo, this.mIconCache);
      }
    }
  }
  
  private void updateStateForCustomContent(int paramInt)
  {
    boolean bool = hasCustomContent();
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f4;
    if (bool)
    {
      int i = this.mScreenOrder.indexOf(Long.valueOf(-301L));
      int j = getScrollX() - getScrollForPage(i) - getLayoutTransitionOffsetForPage(i);
      float f3 = getScrollForPage(i + 1) - getScrollForPage(i);
      f4 = f3 - j;
      float f5 = (f3 - j) / f3;
      if (isLayoutRtl())
      {
        f2 = Math.min(0.0F, f4);
        f1 = Math.max(0.0F, f5);
      }
    }
    else
    {
      if (Float.compare(f1, this.mLastCustomContentScrollProgress) != 0) {
        break label131;
      }
    }
    label131:
    do
    {
      return;
      f2 = Math.max(0.0F, f4);
      break;
      CellLayout localCellLayout = (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-301L));
      if ((f1 > 0.0F) && (localCellLayout.getVisibility() != 0) && (!isSmall())) {
        localCellLayout.setVisibility(0);
      }
      this.mLastCustomContentScrollProgress = f1;
      setBackgroundAlpha(0.8F * f1);
      if (this.mLauncher.getHotseat() != null) {
        this.mLauncher.getHotseat().setTranslationX(f2);
      }
      if (getPageIndicator() != null) {
        getPageIndicator().setTranslationX(f2);
      }
    } while (this.mCustomContentCallbacks == null);
    this.mCustomContentCallbacks.onScrollProgressChanged(f1);
  }
  
  public boolean acceptDrop(DropTarget.DragObject paramDragObject)
  {
    CellLayout localCellLayout = this.mDropToLayout;
    if (paramDragObject.dragSource != this)
    {
      if (localCellLayout == null) {
        return false;
      }
      if (!transitionStateShouldAllowDrop()) {
        return false;
      }
      this.mDragViewVisualCenter = getDragViewVisualCenter(paramDragObject.x, paramDragObject.y, paramDragObject.xOffset, paramDragObject.yOffset, paramDragObject.dragView, this.mDragViewVisualCenter);
      CellLayout.CellInfo localCellInfo;
      int i;
      if (this.mLauncher.isHotseatLayout(localCellLayout))
      {
        mapPointFromSelfToHotseatLayout(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
        if (this.mDragInfo == null) {
          break label245;
        }
        localCellInfo = this.mDragInfo;
        i = localCellInfo.spanX;
      }
      int k;
      int m;
      float f;
      label245:
      ItemInfo localItemInfo;
      for (int j = localCellInfo.spanY;; j = localItemInfo.spanY)
      {
        k = i;
        m = j;
        if ((paramDragObject.dragInfo instanceof PendingAddWidgetInfo))
        {
          k = ((PendingAddWidgetInfo)paramDragObject.dragInfo).minSpanX;
          m = ((PendingAddWidgetInfo)paramDragObject.dragInfo).minSpanY;
        }
        this.mTargetCell = findNearestArea((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], k, m, localCellLayout, this.mTargetCell);
        f = localCellLayout.getDistanceFromCell(this.mDragViewVisualCenter[0], this.mDragViewVisualCenter[1], this.mTargetCell);
        if (!willCreateUserFolder((ItemInfo)paramDragObject.dragInfo, localCellLayout, this.mTargetCell, f, true)) {
          break label271;
        }
        return true;
        mapPointFromSelfToChild(localCellLayout, this.mDragViewVisualCenter, null);
        break;
        localItemInfo = (ItemInfo)paramDragObject.dragInfo;
        i = localItemInfo.spanX;
      }
      label271:
      if (willAddToExistingUserFolder((ItemInfo)paramDragObject.dragInfo, localCellLayout, this.mTargetCell, f)) {
        return true;
      }
      int[] arrayOfInt1 = new int[2];
      int n = (int)this.mDragViewVisualCenter[0];
      int i1 = (int)this.mDragViewVisualCenter[1];
      int[] arrayOfInt2 = this.mTargetCell;
      this.mTargetCell = localCellLayout.createArea(n, i1, k, m, i, j, null, arrayOfInt2, arrayOfInt1, 3);
      int i2;
      if ((this.mTargetCell[0] >= 0) && (this.mTargetCell[1] >= 0)) {
        i2 = 1;
      }
      while (i2 == 0)
      {
        boolean bool = this.mLauncher.isHotseatLayout(localCellLayout);
        if ((this.mTargetCell != null) && (bool))
        {
          Hotseat localHotseat = this.mLauncher.getHotseat();
          if (localHotseat.isAllAppsButtonRank(localHotseat.getOrderInHotseat(this.mTargetCell[0], this.mTargetCell[1])))
          {
            return false;
            i2 = 0;
            continue;
          }
        }
        this.mLauncher.showOutOfSpaceMessage(bool);
        return false;
      }
    }
    if (getIdForScreen(localCellLayout) == -201L) {
      commitExtraEmptyScreen();
    }
    return true;
  }
  
  void addApplicationShortcut(ShortcutInfo paramShortcutInfo, CellLayout paramCellLayout, long paramLong1, long paramLong2, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4)
  {
    View localView = this.mLauncher.createShortcut(2130968595, paramCellLayout, paramShortcutInfo);
    int[] arrayOfInt = new int[2];
    paramCellLayout.findCellForSpanThatIntersects(arrayOfInt, 1, 1, paramInt3, paramInt4);
    addInScreen(localView, paramLong1, paramLong2, arrayOfInt[0], arrayOfInt[1], 1, 1, paramBoolean);
    LauncherModel.addOrMoveItemInDatabase(this.mLauncher, paramShortcutInfo, paramLong1, paramLong2, arrayOfInt[0], arrayOfInt[1]);
  }
  
  public boolean addExtraEmptyScreen()
  {
    if (!this.mWorkspaceScreens.containsKey(Long.valueOf(-201L)))
    {
      insertNewWorkspaceScreen(-201L);
      return true;
    }
    return false;
  }
  
  public void addExtraEmptyScreenOnDrag()
  {
    this.mRemoveEmptyScreenRunnable = null;
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = this.mDragSourceInternal;
    int i = 0;
    int j = 0;
    if (localShortcutAndWidgetContainer != null)
    {
      int k = this.mDragSourceInternal.getChildCount();
      j = 0;
      if (k == 1) {
        j = 1;
      }
      int m = indexOfChild((CellLayout)this.mDragSourceInternal.getParent());
      int n = -1 + getChildCount();
      i = 0;
      if (m == n) {
        i = 1;
      }
    }
    if ((j != 0) && (i != 0)) {}
    while (this.mWorkspaceScreens.containsKey(Long.valueOf(-201L))) {
      return;
    }
    insertNewWorkspaceScreen(-201L);
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    if (!this.mLauncher.isAllAppsVisible())
    {
      Folder localFolder = getOpenFolder();
      if (localFolder != null) {
        localFolder.addFocusables(paramArrayList, paramInt1);
      }
    }
    else
    {
      return;
    }
    super.addFocusables(paramArrayList, paramInt1, paramInt2);
  }
  
  void addInScreen(View paramView, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    addInScreen(paramView, paramLong1, paramLong2, paramInt1, paramInt2, paramInt3, paramInt4, false, false);
  }
  
  void addInScreen(View paramView, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    addInScreen(paramView, paramLong1, paramLong2, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean, false);
  }
  
  void addInScreen(View paramView, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramLong1 == -100L) && (getScreenWithId(paramLong2) == null))
    {
      Log.e("Launcher.Workspace", "Skipping child, screenId " + paramLong2 + " not found");
      new Throwable().printStackTrace();
      return;
    }
    if (paramLong2 == -201L) {
      throw new RuntimeException("Screen id should not be EXTRA_EMPTY_SCREEN_ID");
    }
    CellLayout localCellLayout;
    label157:
    ViewGroup.LayoutParams localLayoutParams;
    CellLayout.LayoutParams localLayoutParams1;
    label193:
    int i;
    boolean bool;
    if (paramLong1 == -101L)
    {
      localCellLayout = this.mLauncher.getHotseat().getLayout();
      paramView.setOnKeyListener(null);
      if ((paramView instanceof FolderIcon)) {
        ((FolderIcon)paramView).setTextVisible(false);
      }
      if (paramBoolean2)
      {
        paramInt1 = this.mLauncher.getHotseat().getCellXFromOrder((int)paramLong2);
        paramInt2 = this.mLauncher.getHotseat().getCellYFromOrder((int)paramLong2);
        localLayoutParams = paramView.getLayoutParams();
        if ((localLayoutParams != null) && ((localLayoutParams instanceof CellLayout.LayoutParams))) {
          break label407;
        }
        localLayoutParams1 = new CellLayout.LayoutParams(paramInt1, paramInt2, paramInt3, paramInt4);
        if ((paramInt3 < 0) && (paramInt4 < 0)) {
          localLayoutParams1.isLockedToGrid = false;
        }
        i = LauncherModel.getCellLayoutChildId(paramLong1, paramLong2, paramInt1, paramInt2, paramInt3, paramInt4);
        if ((paramView instanceof Folder)) {
          break label445;
        }
        bool = true;
        label235:
        if (!paramBoolean1) {
          break label451;
        }
      }
    }
    label407:
    label445:
    label451:
    for (int j = 0;; j = -1)
    {
      if (!localCellLayout.addViewToCellLayout(paramView, j, i, localLayoutParams1, bool)) {
        Launcher.addDumpLog("Launcher.Workspace", "Failed to add to item at (" + localLayoutParams1.cellX + "," + localLayoutParams1.cellY + ") to CellLayout", true);
      }
      if (!(paramView instanceof Folder))
      {
        paramView.setHapticFeedbackEnabled(false);
        paramView.setOnLongClickListener(this.mLongClickListener);
      }
      if (!(paramView instanceof DropTarget)) {
        break;
      }
      this.mDragController.addDropTarget((DropTarget)paramView);
      return;
      paramLong2 = this.mLauncher.getHotseat().getOrderInHotseat(paramInt1, paramInt2);
      break label157;
      if ((paramView instanceof FolderIcon)) {
        ((FolderIcon)paramView).setTextVisible(true);
      }
      localCellLayout = getScreenWithId(paramLong2);
      paramView.setOnKeyListener(new IconKeyEventListener());
      break label157;
      localLayoutParams1 = (CellLayout.LayoutParams)localLayoutParams;
      localLayoutParams1.cellX = paramInt1;
      localLayoutParams1.cellY = paramInt2;
      localLayoutParams1.cellHSpan = paramInt3;
      localLayoutParams1.cellVSpan = paramInt4;
      break label193;
      bool = false;
      break label235;
    }
  }
  
  void addInScreenFromBind(View paramView, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    addInScreen(paramView, paramLong1, paramLong2, paramInt1, paramInt2, paramInt3, paramInt4, false, true);
  }
  
  public void addToCustomContentPage(View paramView, Launcher.CustomContentCallbacks paramCustomContentCallbacks, String paramString)
  {
    if (getPageIndexForScreenId(-301L) < 0) {
      throw new RuntimeException("Expected custom content screen to exist");
    }
    CellLayout localCellLayout = getScreenWithId(-301L);
    CellLayout.LayoutParams localLayoutParams = new CellLayout.LayoutParams(0, 0, localCellLayout.getCountX(), localCellLayout.getCountY());
    localLayoutParams.canReorder = false;
    localLayoutParams.isFullscreen = true;
    if ((paramView instanceof Insettable)) {
      ((Insettable)paramView).setInsets(this.mInsets);
    }
    localCellLayout.removeAllViews();
    localCellLayout.addViewToCellLayout(paramView, 0, 0, localLayoutParams, true);
    this.mCustomContentDescription = paramString;
    this.mCustomContentCallbacks = paramCustomContentCallbacks;
  }
  
  boolean addToExistingFolderIfNecessary(View paramView, CellLayout paramCellLayout, int[] paramArrayOfInt, float paramFloat, DropTarget.DragObject paramDragObject, boolean paramBoolean)
  {
    if (paramFloat > this.mMaxDistanceForFolderCreation) {}
    FolderIcon localFolderIcon;
    do
    {
      View localView;
      do
      {
        do
        {
          return false;
          localView = paramCellLayout.getChildAt(paramArrayOfInt[0], paramArrayOfInt[1]);
        } while (!this.mAddToExistingFolderOnDrop);
        this.mAddToExistingFolderOnDrop = false;
      } while (!(localView instanceof FolderIcon));
      localFolderIcon = (FolderIcon)localView;
    } while (!localFolderIcon.acceptDrop(paramDragObject.dragInfo));
    localFolderIcon.onDrop(paramDragObject);
    if (!paramBoolean) {
      getParentCellLayoutForView(this.mDragInfo.cell).removeView(this.mDragInfo.cell);
    }
    return true;
  }
  
  public void animateWidgetDrop(ItemInfo paramItemInfo, CellLayout paramCellLayout, DragView paramDragView, final Runnable paramRunnable, int paramInt, final View paramView, boolean paramBoolean)
  {
    Rect localRect = new Rect();
    this.mLauncher.getDragLayer().getViewRectRelativeToSelf(paramDragView, localRect);
    int[] arrayOfInt = new int[2];
    float[] arrayOfFloat = new float[2];
    boolean bool;
    int i;
    if (!(paramItemInfo instanceof PendingAddShortcutInfo))
    {
      bool = true;
      getFinalPositionForDropAnimation(arrayOfInt, arrayOfFloat, paramDragView, paramCellLayout, paramItemInfo, this.mTargetCell, paramBoolean, bool);
      i = -200 + this.mLauncher.getResources().getInteger(2131427354);
      if (((paramView instanceof AppWidgetHostView)) && (paramBoolean))
      {
        Log.d("Launcher.Workspace", "6557954 Animate widget drop, final view is appWidgetHostView");
        this.mLauncher.getDragLayer().removeView(paramView);
      }
      if (((paramInt != 2) && (!paramBoolean)) || (paramView == null)) {
        break label201;
      }
      paramDragView.setCrossFadeBitmap(createWidgetBitmap(paramItemInfo, paramView));
      paramDragView.crossFade((int)(0.8F * i));
    }
    DragLayer localDragLayer;
    for (;;)
    {
      localDragLayer = this.mLauncher.getDragLayer();
      if (paramInt != 4) {
        break label242;
      }
      this.mLauncher.getDragLayer().animateViewIntoPosition(paramDragView, arrayOfInt, 0.0F, 0.1F, 0.1F, 0, paramRunnable, i);
      return;
      bool = false;
      break;
      label201:
      if ((paramItemInfo.itemType == 4) && (paramBoolean))
      {
        float f = Math.min(arrayOfFloat[0], arrayOfFloat[1]);
        arrayOfFloat[1] = f;
        arrayOfFloat[0] = f;
      }
    }
    label242:
    if (paramInt == 1) {}
    for (int j = 2;; j = 0)
    {
      Runnable local15 = new Runnable()
      {
        public void run()
        {
          if (paramView != null) {
            paramView.setVisibility(0);
          }
          if (paramRunnable != null) {
            paramRunnable.run();
          }
        }
      };
      localDragLayer.animateViewIntoPosition(paramDragView, localRect.left, localRect.top, arrayOfInt[0], arrayOfInt[1], 1.0F, 1.0F, 1.0F, arrayOfFloat[0], arrayOfFloat[1], local15, j, i, this);
      return;
    }
  }
  
  public void beginDragShared(View paramView, DragSource paramDragSource)
  {
    Bitmap localBitmap = createDragBitmap(paramView, new Canvas(), 2);
    int i = localBitmap.getWidth();
    int j = localBitmap.getHeight();
    float f = this.mLauncher.getDragLayer().getLocationInDragLayer(paramView, this.mTempXY);
    int k = Math.round(this.mTempXY[0] - (i - f * paramView.getWidth()) / 2.0F);
    int m = Math.round(this.mTempXY[1] - (j - f * j) / 2.0F - 1.0F);
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    Point localPoint;
    Rect localRect;
    if (((paramView instanceof BubbleTextView)) || ((paramView instanceof PagedViewIcon)))
    {
      int n = localDeviceProfile.iconSizePx;
      int i1 = paramView.getPaddingTop();
      int i2 = (i - n) / 2;
      int i3 = i2 + n;
      int i4 = i1 + n;
      m += i1;
      localPoint = new Point(-1, 1);
      localRect = new Rect(i2, i1, i3, i4);
    }
    for (;;)
    {
      if ((paramView instanceof BubbleTextView)) {
        ((BubbleTextView)paramView).clearPressedOrFocusedBackground();
      }
      this.mDragController.startDrag(localBitmap, k, m, paramDragSource, paramView.getTag(), DragController.DRAG_ACTION_MOVE, localPoint, localRect, f);
      if ((paramView.getParent() instanceof ShortcutAndWidgetContainer)) {
        this.mDragSourceInternal = ((ShortcutAndWidgetContainer)paramView.getParent());
      }
      localBitmap.recycle();
      return;
      boolean bool = paramView instanceof FolderIcon;
      localPoint = null;
      localRect = null;
      if (bool)
      {
        int i5 = localDeviceProfile.folderIconSizePx;
        localRect = new Rect(0, paramView.getPaddingTop(), paramView.getWidth(), i5);
        localPoint = null;
      }
    }
  }
  
  public void buildPageHardwareLayers()
  {
    updateChildrenLayersEnabled(true);
    if (getWindowToken() != null)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        ((CellLayout)getChildAt(j)).buildHardwareLayer();
      }
    }
    updateChildrenLayersEnabled(false);
  }
  
  void clearChildrenCache()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(j);
      localCellLayout.setChildrenDrawnWithCacheEnabled(false);
      if (!isHardwareAccelerated()) {
        localCellLayout.setChildrenDrawingCacheEnabled(false);
      }
    }
  }
  
  void clearDropTargets()
  {
    Iterator localIterator = getAllShortcutAndWidgetContainers().iterator();
    while (localIterator.hasNext())
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)localIterator.next();
      int i = localShortcutAndWidgetContainer.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = localShortcutAndWidgetContainer.getChildAt(j);
        if ((localView instanceof DropTarget)) {
          this.mDragController.removeDropTarget((DropTarget)localView);
        }
      }
    }
  }
  
  public long commitExtraEmptyScreen()
  {
    int i = getPageIndexForScreenId(-201L);
    CellLayout localCellLayout = (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-201L));
    this.mWorkspaceScreens.remove(Long.valueOf(-201L));
    this.mScreenOrder.remove(Long.valueOf(-201L));
    long l = LauncherAppState.getLauncherProvider().generateNewScreenId();
    this.mWorkspaceScreens.put(Long.valueOf(l), localCellLayout);
    this.mScreenOrder.add(Long.valueOf(l));
    if (getPageIndicator() != null) {
      getPageIndicator().updateMarker(i, getPageIndicatorMarker(i));
    }
    this.mLauncher.getModel().updateWorkspaceScreenOrder(this.mLauncher, this.mScreenOrder);
    return l;
  }
  
  public void computeScroll()
  {
    super.computeScroll();
    this.mWallpaperOffset.syncWithScroll();
  }
  
  public void createCustomContentPage()
  {
    CellLayout localCellLayout = (CellLayout)this.mLauncher.getLayoutInflater().inflate(2130968935, null);
    this.mWorkspaceScreens.put(Long.valueOf(-301L), localCellLayout);
    this.mScreenOrder.add(0, Long.valueOf(-301L));
    localCellLayout.setPadding(0, 0, 0, 0);
    addFullScreenPage(localCellLayout);
    this.mDefaultPage = (1 + this.mOriginalDefaultPage);
    this.mLauncher.updateCustomContentHintVisibility();
    if (this.mRestorePage != -1001)
    {
      this.mRestorePage = (1 + this.mRestorePage);
      return;
    }
    setCurrentPage(1 + getCurrentPage());
  }
  
  public Bitmap createDragBitmap(View paramView, Canvas paramCanvas, int paramInt)
  {
    Drawable localDrawable;
    if ((paramView instanceof TextView)) {
      localDrawable = ((TextView)paramView).getCompoundDrawables()[1];
    }
    for (Bitmap localBitmap = Bitmap.createBitmap(paramInt + localDrawable.getIntrinsicWidth(), paramInt + localDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);; localBitmap = Bitmap.createBitmap(paramInt + paramView.getWidth(), paramInt + paramView.getHeight(), Bitmap.Config.ARGB_8888))
    {
      paramCanvas.setBitmap(localBitmap);
      drawDragView(paramView, paramCanvas, paramInt, true);
      paramCanvas.setBitmap(null);
      return localBitmap;
    }
  }
  
  boolean createUserFolderIfNecessary(View paramView, long paramLong, CellLayout paramCellLayout, int[] paramArrayOfInt, float paramFloat, boolean paramBoolean, DragView paramDragView, Runnable paramRunnable)
  {
    if (paramFloat > this.mMaxDistanceForFolderCreation) {
      return false;
    }
    View localView = paramCellLayout.getChildAt(paramArrayOfInt[0], paramArrayOfInt[1]);
    CellLayout.CellInfo localCellInfo = this.mDragInfo;
    int i = 0;
    if (localCellInfo != null)
    {
      CellLayout localCellLayout = getParentCellLayoutForView(this.mDragInfo.cell);
      if ((this.mDragInfo.cellX != paramArrayOfInt[0]) || (this.mDragInfo.cellY != paramArrayOfInt[1]) || (localCellLayout != paramCellLayout)) {
        break label111;
      }
    }
    label111:
    for (i = 1; (localView == null) || (i != 0) || (!this.mCreateUserFolderOnDrop); i = 0) {
      return false;
    }
    this.mCreateUserFolderOnDrop = false;
    long l;
    ShortcutInfo localShortcutInfo1;
    ShortcutInfo localShortcutInfo2;
    FolderIcon localFolderIcon;
    int j;
    if (paramArrayOfInt == null)
    {
      l = this.mDragInfo.screenId;
      boolean bool1 = localView.getTag() instanceof ShortcutInfo;
      boolean bool2 = paramView.getTag() instanceof ShortcutInfo;
      if ((!bool1) || (!bool2)) {
        break label356;
      }
      localShortcutInfo1 = (ShortcutInfo)paramView.getTag();
      localShortcutInfo2 = (ShortcutInfo)localView.getTag();
      if (!paramBoolean) {
        getParentCellLayoutForView(this.mDragInfo.cell).removeView(this.mDragInfo.cell);
      }
      Rect localRect = new Rect();
      float f = this.mLauncher.getDragLayer().getDescendantRectRelativeToSelf(localView, localRect);
      paramCellLayout.removeView(localView);
      localFolderIcon = this.mLauncher.addFolder(paramCellLayout, paramLong, l, paramArrayOfInt[0], paramArrayOfInt[1]);
      localShortcutInfo2.cellX = -1;
      localShortcutInfo2.cellY = -1;
      localShortcutInfo1.cellX = -1;
      localShortcutInfo1.cellY = -1;
      if (paramDragView == null) {
        break label333;
      }
      j = 1;
      label296:
      if (j == 0) {
        break label339;
      }
      localFolderIcon.performCreateAnimation(localShortcutInfo2, localView, localShortcutInfo1, paramDragView, localRect, f, paramRunnable);
    }
    for (;;)
    {
      return true;
      l = getIdForScreen(paramCellLayout);
      break;
      label333:
      j = 0;
      break label296;
      label339:
      localFolderIcon.addItem(localShortcutInfo2);
      localFolderIcon.addItem(localShortcutInfo1);
    }
    label356:
    return false;
  }
  
  public Bitmap createWidgetBitmap(ItemInfo paramItemInfo, View paramView)
  {
    int[] arrayOfInt = this.mLauncher.getWorkspace().estimateItemSize(paramItemInfo.spanX, paramItemInfo.spanY, paramItemInfo, false);
    int i = paramView.getVisibility();
    paramView.setVisibility(0);
    int j = View.MeasureSpec.makeMeasureSpec(arrayOfInt[0], 1073741824);
    int k = View.MeasureSpec.makeMeasureSpec(arrayOfInt[1], 1073741824);
    Bitmap localBitmap = Bitmap.createBitmap(arrayOfInt[0], arrayOfInt[1], Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    paramView.measure(j, k);
    paramView.layout(0, 0, arrayOfInt[0], arrayOfInt[1]);
    paramView.draw(localCanvas);
    localCanvas.setBitmap(null);
    paramView.setVisibility(i);
    return localBitmap;
  }
  
  public void deferCompleteDropAfterUninstallActivity()
  {
    this.mDeferDropAfterUninstall = true;
  }
  
  protected void determineScrollingStart(MotionEvent paramMotionEvent)
  {
    if (!isFinishedSwitchingState()) {}
    label192:
    label198:
    label214:
    for (;;)
    {
      return;
      float f1 = paramMotionEvent.getX() - this.mXDown;
      float f2 = Math.abs(f1);
      float f3 = Math.abs(paramMotionEvent.getY() - this.mYDown);
      if (Float.compare(f2, 0.0F) != 0)
      {
        float f4 = (float)Math.atan(f3 / f2);
        if ((f2 > this.mTouchSlop) || (f3 > this.mTouchSlop)) {
          cancelCurrentPageLongPress();
        }
        int i;
        int j;
        if (this.mTouchDownTime - this.mCustomContentShowTime > 200L)
        {
          i = 1;
          if (!isLayoutRtl()) {
            break label198;
          }
          if (f1 >= 0.0F) {
            break label192;
          }
          j = 1;
        }
        for (;;)
        {
          if (((j != 0) && (getScreenIdForPageIndex(getCurrentPage()) == -301L) && (i != 0)) || (f4 > 1.047198F)) {
            break label214;
          }
          if (f4 <= 0.5235988F) {
            break label216;
          }
          super.determineScrollingStart(paramMotionEvent, 1.0F + 4.0F * (float)Math.sqrt((f4 - 0.5235988F) / 0.5235988F));
          return;
          i = 0;
          break;
          j = 0;
          continue;
          if (f1 > 0.0F) {
            j = 1;
          } else {
            j = 0;
          }
        }
      }
    }
    label216:
    super.determineScrollingStart(paramMotionEvent);
  }
  
  void disableLayoutTransitions()
  {
    setLayoutTransition(null);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    this.mSavedStates = paramSparseArray;
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    if ((isSmall()) || (!isFinishedSwitchingState())) {
      return false;
    }
    return super.dispatchUnhandledMove(paramView, paramInt);
  }
  
  void enableChildrenCache(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2)
    {
      int n = paramInt1;
      paramInt1 = paramInt2;
      paramInt2 = n;
    }
    int i = getChildCount();
    int j = Math.max(paramInt1, 0);
    int k = Math.min(paramInt2, i - 1);
    for (int m = j; m <= k; m++)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(m);
      localCellLayout.setChildrenDrawnWithCacheEnabled(true);
      localCellLayout.setChildrenDrawingCacheEnabled(true);
    }
  }
  
  void enableLayoutTransitions()
  {
    setLayoutTransition(this.mLayoutTransition);
  }
  
  public boolean enterOverviewMode()
  {
    if (this.mTouchState != 0) {
      return false;
    }
    enableOverviewMode(true, -1, true);
    return true;
  }
  
  public Rect estimateItemPosition(CellLayout paramCellLayout, ItemInfo paramItemInfo, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Rect localRect = new Rect();
    paramCellLayout.cellToRect(paramInt1, paramInt2, paramInt3, paramInt4, localRect);
    return localRect;
  }
  
  public int[] estimateItemSize(int paramInt1, int paramInt2, ItemInfo paramItemInfo, boolean paramBoolean)
  {
    int[] arrayOfInt = new int[2];
    if (getChildCount() > 0)
    {
      Rect localRect = estimateItemPosition((CellLayout)getChildAt(numCustomPages()), paramItemInfo, 0, 0, paramInt1, paramInt2);
      arrayOfInt[0] = localRect.width();
      arrayOfInt[1] = localRect.height();
      if (paramBoolean)
      {
        arrayOfInt[0] = ((int)(arrayOfInt[0] * this.mSpringLoadedShrinkFactor));
        arrayOfInt[1] = ((int)(arrayOfInt[1] * this.mSpringLoadedShrinkFactor));
      }
      return arrayOfInt;
    }
    arrayOfInt[0] = 2147483647;
    arrayOfInt[1] = 2147483647;
    return arrayOfInt;
  }
  
  public void exitOverviewMode(int paramInt, boolean paramBoolean)
  {
    enableOverviewMode(false, paramInt, paramBoolean);
  }
  
  public void exitOverviewMode(boolean paramBoolean)
  {
    exitOverviewMode(-1, paramBoolean);
  }
  
  public void exitWidgetResizeMode()
  {
    this.mLauncher.getDragLayer().clearAllResizeFrames();
  }
  
  ArrayList<ShortcutAndWidgetContainer> getAllShortcutAndWidgetContainers()
  {
    ArrayList localArrayList = new ArrayList();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      localArrayList.add(((CellLayout)getChildAt(j)).getShortcutsAndWidgets());
    }
    if (this.mLauncher.getHotseat() != null) {
      localArrayList.add(this.mLauncher.getHotseat().getLayout().getShortcutsAndWidgets());
    }
    return localArrayList;
  }
  
  public float getBackgroundAlpha()
  {
    return this.mBackgroundAlpha;
  }
  
  Animator getChangeStateAnimation(State paramState, boolean paramBoolean)
  {
    return getChangeStateAnimation(paramState, paramBoolean, 0, -1);
  }
  
  Animator getChangeStateAnimation(State paramState, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    if (this.mState == paramState) {
      return null;
    }
    initAnimationArrays();
    AnimatorSet localAnimatorSet;
    int i;
    label40:
    label48:
    int j;
    label59:
    int k;
    label70:
    int m;
    label85:
    int n;
    label95:
    int i1;
    label105:
    int i2;
    label115:
    float f1;
    label128:
    float f2;
    label141:
    float f3;
    label149:
    float f4;
    label157:
    float f5;
    label169:
    int i3;
    label182:
    int i4;
    label195:
    int i5;
    label208:
    int i6;
    label221:
    label237:
    int i7;
    label257:
    label284:
    int i8;
    label287:
    CellLayout localCellLayout2;
    int i14;
    label327:
    float f7;
    float f8;
    if (paramBoolean)
    {
      localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
      State localState = this.mState;
      if (localState != State.NORMAL) {
        break label437;
      }
      i = 1;
      if (localState != State.SPRING_LOADED) {
        break label443;
      }
      if (localState != State.SMALL) {
        break label446;
      }
      j = 1;
      if (localState != State.OVERVIEW) {
        break label452;
      }
      k = 1;
      setState(paramState);
      if (paramState != State.NORMAL) {
        break label458;
      }
      m = 1;
      if (paramState != State.SPRING_LOADED) {
        break label464;
      }
      n = 1;
      if (paramState != State.SMALL) {
        break label470;
      }
      i1 = 1;
      if (paramState != State.OVERVIEW) {
        break label476;
      }
      i2 = 1;
      if ((n == 0) && (i2 == 0)) {
        break label482;
      }
      f1 = 1.0F;
      if ((i2 == 0) && (i1 == 0)) {
        break label488;
      }
      f2 = 0.0F;
      if (i2 == 0) {
        break label494;
      }
      f3 = 1.0F;
      if (m != 0) {
        break label500;
      }
      f4 = 0.0F;
      if (i2 == 0) {
        break label506;
      }
      f5 = getOverviewModeTranslationY();
      if ((i == 0) || (i1 == 0)) {
        break label512;
      }
      i3 = 1;
      if ((j == 0) || (m == 0)) {
        break label518;
      }
      i4 = 1;
      if ((i == 0) || (i2 == 0)) {
        break label524;
      }
      i5 = 1;
      if ((k == 0) || (m == 0)) {
        break label530;
      }
      i6 = 1;
      this.mNewScale = 1.0F;
      if (k == 0) {
        break label536;
      }
      disableFreeScroll(paramInt2);
      if (paramState != State.NORMAL)
      {
        if (n == 0) {
          break label548;
        }
        this.mNewScale = this.mSpringLoadedShrinkFactor;
        if (i3 != 0) {
          updateChildrenLayersEnabled(false);
        }
      }
      if (i3 == 0) {
        break label584;
      }
      i7 = getResources().getInteger(2131427335);
      i8 = 0;
      int i9 = getChildCount();
      if (i8 >= i9) {
        break label704;
      }
      localCellLayout2 = (CellLayout)getChildAt(i8);
      int i13 = getNextPage();
      if (i8 != i13) {
        break label624;
      }
      i14 = 1;
      f7 = localCellLayout2.getShortcutsAndWidgets().getAlpha();
      if (i1 == 0) {
        break label630;
      }
      f8 = 0.0F;
      label345:
      if ((!this.mIsSwitchingState) && ((i3 != 0) || (i4 != 0)))
      {
        if ((i4 == 0) || (i14 == 0)) {
          break label673;
        }
        f7 = 0.0F;
        label375:
        localCellLayout2.setShortcutAndWidgetAlpha(f7);
      }
      this.mOldAlphas[i8] = f7;
      this.mNewAlphas[i8] = f8;
      if (!paramBoolean) {
        break label687;
      }
      this.mOldBackgroundAlphas[i8] = localCellLayout2.getBackgroundAlpha();
      this.mNewBackgroundAlphas[i8] = f1;
    }
    for (;;)
    {
      i8++;
      break label287;
      localAnimatorSet = null;
      break;
      label437:
      i = 0;
      break label40;
      label443:
      break label48;
      label446:
      j = 0;
      break label59;
      label452:
      k = 0;
      break label70;
      label458:
      m = 0;
      break label85;
      label464:
      n = 0;
      break label95;
      label470:
      i1 = 0;
      break label105;
      label476:
      i2 = 0;
      break label115;
      label482:
      f1 = 0.0F;
      break label128;
      label488:
      f2 = 1.0F;
      break label141;
      label494:
      f3 = 0.0F;
      break label149;
      label500:
      f4 = 1.0F;
      break label157;
      label506:
      f5 = 0.0F;
      break label169;
      label512:
      i3 = 0;
      break label182;
      label518:
      i4 = 0;
      break label195;
      label524:
      i5 = 0;
      break label208;
      label530:
      i6 = 0;
      break label221;
      label536:
      if (i2 == 0) {
        break label237;
      }
      enableFreeScroll();
      break label237;
      label548:
      if (i2 != 0)
      {
        this.mNewScale = this.mOverviewModeShrinkFactor;
        break label257;
      }
      if (i1 == 0) {
        break label257;
      }
      this.mNewScale = (this.mOverviewModeShrinkFactor - 0.3F);
      break label257;
      label584:
      if ((i5 != 0) || (i6 != 0))
      {
        i7 = getResources().getInteger(2131427336);
        break label284;
      }
      i7 = getResources().getInteger(2131427344);
      break label284;
      label624:
      i14 = 0;
      break label327;
      label630:
      if ((m != 0) && (this.mWorkspaceFadeInAdjacentScreens))
      {
        int i15 = getNextPage();
        if (i8 == i15) {}
        for (f8 = 1.0F;; f8 = 0.0F) {
          break;
        }
      }
      f8 = 1.0F;
      break label345;
      label673:
      if (i14 != 0) {
        break label375;
      }
      f8 = 0.0F;
      f7 = 0.0F;
      break label375;
      label687:
      localCellLayout2.setBackgroundAlpha(f1);
      localCellLayout2.setShortcutAndWidgetAlpha(f8);
    }
    label704:
    View localView1 = this.mLauncher.getQsbBar();
    View localView2 = this.mLauncher.getOverviewPanel();
    Hotseat localHotseat = this.mLauncher.getHotseat();
    ObjectAnimator localObjectAnimator4;
    if (paramBoolean)
    {
      localAnimatorSet.setDuration(i7);
      LauncherViewPropertyAnimator localLauncherViewPropertyAnimator1 = new LauncherViewPropertyAnimator(this);
      localLauncherViewPropertyAnimator1.scaleX(this.mNewScale).scaleY(this.mNewScale).translationY(f5).setInterpolator(this.mZoomInInterpolator);
      localAnimatorSet.play(localLauncherViewPropertyAnimator1);
      int i10 = 0;
      int i11 = getChildCount();
      if (i10 < i11)
      {
        final int i12 = i10;
        final CellLayout localCellLayout1 = (CellLayout)getChildAt(i12);
        float f6 = localCellLayout1.getShortcutsAndWidgets().getAlpha();
        if ((this.mOldAlphas[i12] == 0.0F) && (this.mNewAlphas[i12] == 0.0F))
        {
          localCellLayout1.setBackgroundAlpha(this.mNewBackgroundAlphas[i12]);
          localCellLayout1.setShortcutAndWidgetAlpha(this.mNewAlphas[i12]);
        }
        for (;;)
        {
          i10++;
          break;
          if ((this.mOldAlphas[i12] != this.mNewAlphas[i12]) || (f6 != this.mNewAlphas[i12]))
          {
            LauncherViewPropertyAnimator localLauncherViewPropertyAnimator2 = new LauncherViewPropertyAnimator(localCellLayout1.getShortcutsAndWidgets());
            localLauncherViewPropertyAnimator2.alpha(this.mNewAlphas[i12]).setInterpolator(this.mZoomInInterpolator);
            localAnimatorSet.play(localLauncherViewPropertyAnimator2);
          }
          if ((this.mOldBackgroundAlphas[i12] != 0.0F) || (this.mNewBackgroundAlphas[i12] != 0.0F))
          {
            ValueAnimator localValueAnimator = LauncherAnimUtils.ofFloat(localCellLayout1, new float[] { 0.0F, 1.0F });
            localValueAnimator.setInterpolator(this.mZoomInInterpolator);
            LauncherAnimatorUpdateListener local9 = new LauncherAnimatorUpdateListener()
            {
              public void onAnimationUpdate(float paramAnonymousFloat1, float paramAnonymousFloat2)
              {
                localCellLayout1.setBackgroundAlpha(paramAnonymousFloat1 * Workspace.this.mOldBackgroundAlphas[i12] + paramAnonymousFloat2 * Workspace.this.mNewBackgroundAlphas[i12]);
              }
            };
            localValueAnimator.addUpdateListener(local9);
            localAnimatorSet.play(localValueAnimator);
          }
        }
      }
      PageIndicator localPageIndicator = getPageIndicator();
      ObjectAnimator localObjectAnimator1 = null;
      if (localPageIndicator != null) {
        localObjectAnimator1 = ObjectAnimator.ofFloat(getPageIndicator(), "alpha", new float[] { f2 });
      }
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(localHotseat, "alpha", new float[] { f2 });
      ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(localView1, "alpha", new float[] { f4 });
      localObjectAnimator4 = ObjectAnimator.ofFloat(localView2, "alpha", new float[] { f3 });
      AlphaUpdateListener localAlphaUpdateListener1 = new AlphaUpdateListener(localView2);
      localObjectAnimator4.addListener(localAlphaUpdateListener1);
      AlphaUpdateListener localAlphaUpdateListener2 = new AlphaUpdateListener(localHotseat);
      localObjectAnimator2.addListener(localAlphaUpdateListener2);
      AlphaUpdateListener localAlphaUpdateListener3 = new AlphaUpdateListener(localView1);
      localObjectAnimator3.addListener(localAlphaUpdateListener3);
      if (i5 != 0)
      {
        localObjectAnimator2.setInterpolator(new DecelerateInterpolator(2.0F));
        if (getPageIndicator() != null)
        {
          AlphaUpdateListener localAlphaUpdateListener4 = new AlphaUpdateListener(getPageIndicator());
          localObjectAnimator1.addListener(localAlphaUpdateListener4);
        }
        localAnimatorSet.play(localObjectAnimator4);
        localAnimatorSet.play(localObjectAnimator2);
        localAnimatorSet.play(localObjectAnimator3);
        localAnimatorSet.play(localObjectAnimator1);
        localAnimatorSet.setStartDelay(paramInt1);
      }
    }
    for (;;)
    {
      this.mLauncher.updateVoiceButtonProxyVisible(false);
      if (n == 0) {
        break label1411;
      }
      animateBackgroundGradient(getResources().getInteger(2131427334) / 100.0F, false);
      return localAnimatorSet;
      if (i6 == 0) {
        break;
      }
      localObjectAnimator4.setInterpolator(new DecelerateInterpolator(2.0F));
      break;
      localView2.setAlpha(f3);
      AlphaUpdateListener.updateVisibility(localView2);
      localHotseat.setAlpha(f2);
      AlphaUpdateListener.updateVisibility(localHotseat);
      if (getPageIndicator() != null)
      {
        getPageIndicator().setAlpha(f2);
        AlphaUpdateListener.updateVisibility(getPageIndicator());
      }
      localView1.setAlpha(f4);
      AlphaUpdateListener.updateVisibility(localView1);
      updateCustomContentVisibility();
      setScaleX(this.mNewScale);
      setScaleY(this.mNewScale);
      setTranslationY(f5);
    }
    label1411:
    if (i2 != 0)
    {
      animateBackgroundGradient(getResources().getInteger(2131427334) / 100.0F, true);
      return localAnimatorSet;
    }
    animateBackgroundGradient(0.0F, paramBoolean);
    return localAnimatorSet;
  }
  
  public float getChildrenOutlineAlpha()
  {
    return this.mChildrenOutlineAlpha;
  }
  
  public View getContent()
  {
    return this;
  }
  
  public CellLayout getCurrentDropLayout()
  {
    return (CellLayout)getChildAt(getNextPage());
  }
  
  protected String getCurrentPageDescription()
  {
    if (this.mNextPage != -1) {}
    int j;
    for (int i = this.mNextPage;; i = this.mCurrentPage)
    {
      j = numCustomPages();
      if ((!hasCustomContent()) || (getNextPage() != 0)) {
        break;
      }
      return this.mCustomContentDescription;
    }
    String str = getContext().getString(2131361932);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(i + 1 - j);
    arrayOfObject[1] = Integer.valueOf(getChildCount() - j);
    return String.format(str, arrayOfObject);
  }
  
  protected Launcher.CustomContentCallbacks getCustomContentCallbacks()
  {
    return this.mCustomContentCallbacks;
  }
  
  public int getDescendantFocusability()
  {
    if (isSmall()) {
      return 393216;
    }
    return super.getDescendantFocusability();
  }
  
  public Folder getFolderForTag(Object paramObject)
  {
    Iterator localIterator = getAllShortcutAndWidgetContainers().iterator();
    while (localIterator.hasNext())
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)localIterator.next();
      int i = localShortcutAndWidgetContainer.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = localShortcutAndWidgetContainer.getChildAt(j);
        if ((localView instanceof Folder))
        {
          Folder localFolder = (Folder)localView;
          if ((localFolder.getInfo() == paramObject) && (localFolder.getInfo().opened)) {
            return localFolder;
          }
        }
      }
    }
    return null;
  }
  
  public void getHitRectRelativeToDragLayer(Rect paramRect)
  {
    this.mLauncher.getDragLayer().getDescendantRectRelativeToSelf(this, paramRect);
  }
  
  public long getIdForScreen(CellLayout paramCellLayout)
  {
    Iterator localIterator = this.mWorkspaceScreens.keySet().iterator();
    while (localIterator.hasNext())
    {
      long l = ((Long)localIterator.next()).longValue();
      if (this.mWorkspaceScreens.get(Long.valueOf(l)) == paramCellLayout) {
        return l;
      }
    }
    return -1L;
  }
  
  Folder getOpenFolder()
  {
    DragLayer localDragLayer = this.mLauncher.getDragLayer();
    int i = localDragLayer.getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = localDragLayer.getChildAt(j);
      if ((localView instanceof Folder))
      {
        Folder localFolder = (Folder)localView;
        if (localFolder.getInfo().opened) {
          return localFolder;
        }
      }
    }
    return null;
  }
  
  protected void getOverviewModePages(int[] paramArrayOfInt)
  {
    int i = numCustomPages();
    int j = -1 + getChildCount();
    paramArrayOfInt[0] = Math.max(0, Math.min(i, -1 + getChildCount()));
    paramArrayOfInt[1] = Math.max(0, j);
  }
  
  int getOverviewModeTranslationY()
  {
    int i = getNormalChildHeight();
    int j = (getViewportHeight() - (int)(this.mOverviewModeShrinkFactor * i)) / 2;
    return this.mOverviewModePageOffset - j + this.mInsets.top;
  }
  
  public int getPageIndexForScreenId(long paramLong)
  {
    return indexOfChild((View)this.mWorkspaceScreens.get(Long.valueOf(paramLong)));
  }
  
  protected View.OnClickListener getPageIndicatorClickListener()
  {
    if (!((AccessibilityManager)getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
      return null;
    }
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Workspace.this.enterOverviewMode();
      }
    };
  }
  
  protected String getPageIndicatorDescription()
  {
    String str = getResources().getString(2131361959);
    return getCurrentPageDescription() + ", " + str;
  }
  
  protected PageIndicator.PageMarkerResources getPageIndicatorMarker(int paramInt)
  {
    if ((getScreenIdForPageIndex(paramInt) == -201L) && (this.mScreenOrder.size() - numCustomPages() > 1)) {
      return new PageIndicator.PageMarkerResources(2130837817, 2130837816);
    }
    return super.getPageIndicatorMarker(paramInt);
  }
  
  CellLayout getParentCellLayoutForView(View paramView)
  {
    Iterator localIterator = getWorkspaceAndHotseatCellLayouts().iterator();
    while (localIterator.hasNext())
    {
      CellLayout localCellLayout = (CellLayout)localIterator.next();
      if (localCellLayout.getShortcutsAndWidgets().indexOfChild(paramView) > -1) {
        return localCellLayout;
      }
    }
    return null;
  }
  
  public int getRestorePage()
  {
    return getNextPage() - numCustomPages();
  }
  
  public long getScreenIdForPageIndex(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < this.mScreenOrder.size())) {
      return ((Long)this.mScreenOrder.get(paramInt)).longValue();
    }
    return -1L;
  }
  
  ArrayList<Long> getScreenOrder()
  {
    return this.mScreenOrder;
  }
  
  public CellLayout getScreenWithId(long paramLong)
  {
    return (CellLayout)this.mWorkspaceScreens.get(Long.valueOf(paramLong));
  }
  
  protected int getScrollMode()
  {
    return 1;
  }
  
  ArrayList<ComponentName> getUniqueComponents(boolean paramBoolean, ArrayList<ComponentName> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    getUniqueIntents(this.mLauncher.getHotseat().getLayout(), localArrayList, paramArrayList, false);
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getUniqueIntents((CellLayout)getChildAt(j), localArrayList, paramArrayList, false);
    }
    return localArrayList;
  }
  
  void getUniqueIntents(CellLayout paramCellLayout, ArrayList<ComponentName> paramArrayList1, ArrayList<ComponentName> paramArrayList2, boolean paramBoolean)
  {
    int i = paramCellLayout.getShortcutsAndWidgets().getChildCount();
    ArrayList localArrayList1 = new ArrayList();
    for (int j = 0; j < i; j++) {
      localArrayList1.add(paramCellLayout.getShortcutsAndWidgets().getChildAt(j));
    }
    int k = 0;
    while (k < i)
    {
      View localView = (View)localArrayList1.get(k);
      ItemInfo localItemInfo = (ItemInfo)localView.getTag();
      ShortcutInfo localShortcutInfo2;
      ComponentName localComponentName2;
      label154:
      FolderIcon localFolderIcon;
      int m;
      label182:
      ShortcutInfo localShortcutInfo1;
      ComponentName localComponentName1;
      if ((localItemInfo instanceof ShortcutInfo))
      {
        localShortcutInfo2 = (ShortcutInfo)localItemInfo;
        localComponentName2 = localShortcutInfo2.intent.getComponent();
        Uri localUri2 = localShortcutInfo2.intent.getData();
        if ((localUri2 != null) && (!localUri2.equals(Uri.EMPTY)))
        {
          k++;
          continue;
        }
        if (!paramArrayList1.contains(localComponentName2)) {
          paramArrayList1.add(localComponentName2);
        }
      }
      else
      {
        if (!(localView instanceof FolderIcon)) {
          break label303;
        }
        localFolderIcon = (FolderIcon)localView;
        ArrayList localArrayList2 = localFolderIcon.getFolder().getItemsInReadingOrder();
        m = 0;
        if (m < localArrayList2.size()) {
          if ((((View)localArrayList2.get(m)).getTag() instanceof ShortcutInfo))
          {
            localShortcutInfo1 = (ShortcutInfo)((View)localArrayList2.get(m)).getTag();
            localComponentName1 = localShortcutInfo1.intent.getComponent();
            Uri localUri1 = localShortcutInfo1.intent.getData();
            if ((localUri1 == null) || (localUri1.equals(Uri.EMPTY))) {
              break label305;
            }
          }
        }
      }
      for (;;)
      {
        m++;
        break label182;
        break;
        if (paramBoolean)
        {
          paramCellLayout.removeViewInLayout(localView);
          LauncherModel.deleteItemFromDatabase(this.mLauncher, localShortcutInfo2);
        }
        if (paramArrayList2 == null) {
          break label154;
        }
        paramArrayList2.add(localComponentName2);
        break label154;
        label303:
        break;
        label305:
        if (!paramArrayList1.contains(localComponentName1))
        {
          paramArrayList1.add(localComponentName1);
        }
        else
        {
          if (paramBoolean)
          {
            localFolderIcon.getFolderInfo().remove(localShortcutInfo1);
            LauncherModel.deleteItemFromDatabase(this.mLauncher, localShortcutInfo1);
          }
          if (paramArrayList2 != null) {
            paramArrayList2.add(localComponentName1);
          }
        }
      }
    }
  }
  
  public View getViewForTag(Object paramObject)
  {
    Iterator localIterator = getAllShortcutAndWidgetContainers().iterator();
    while (localIterator.hasNext())
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)localIterator.next();
      int i = localShortcutAndWidgetContainer.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = localShortcutAndWidgetContainer.getChildAt(j);
        if (localView.getTag() == paramObject) {
          return localView;
        }
      }
    }
    return null;
  }
  
  ArrayList<CellLayout> getWorkspaceAndHotseatCellLayouts()
  {
    ArrayList localArrayList = new ArrayList();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      localArrayList.add((CellLayout)getChildAt(j));
    }
    if (this.mLauncher.getHotseat() != null) {
      localArrayList.add(this.mLauncher.getHotseat().getLayout());
    }
    return localArrayList;
  }
  
  public boolean hasCustomContent()
  {
    return (this.mScreenOrder.size() > 0) && (((Long)this.mScreenOrder.get(0)).longValue() == -301L);
  }
  
  public boolean hasExtraEmptyScreen()
  {
    int i = getChildCount() - numCustomPages();
    return (this.mWorkspaceScreens.containsKey(Long.valueOf(-201L))) && (i > 1);
  }
  
  void hideCustomContentIfNecessary()
  {
    if (this.mState != State.NORMAL) {}
    for (int i = 1;; i = 0)
    {
      if ((i != 0) && (hasCustomContent())) {
        ((CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-301L))).setVisibility(4);
      }
      return;
    }
  }
  
  void hideOutlines()
  {
    if ((!isSmall()) && (!this.mIsSwitchingState))
    {
      if (this.mChildrenOutlineFadeInAnimation != null) {
        this.mChildrenOutlineFadeInAnimation.cancel();
      }
      if (this.mChildrenOutlineFadeOutAnimation != null) {
        this.mChildrenOutlineFadeOutAnimation.cancel();
      }
      this.mChildrenOutlineFadeOutAnimation = LauncherAnimUtils.ofFloat(this, "childrenOutlineAlpha", new float[] { 0.0F });
      this.mChildrenOutlineFadeOutAnimation.setDuration(375L);
      this.mChildrenOutlineFadeOutAnimation.setStartDelay(0L);
      this.mChildrenOutlineFadeOutAnimation.start();
    }
  }
  
  protected void initWorkspace()
  {
    getContext();
    this.mCurrentPage = this.mDefaultPage;
    Launcher.setScreen(this.mCurrentPage);
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    DeviceProfile localDeviceProfile = localLauncherAppState.getDynamicGrid().getDeviceProfile();
    this.mIconCache = localLauncherAppState.getIconCache();
    setWillNotDraw(false);
    setClipChildren(false);
    setClipToPadding(false);
    setChildrenDrawnWithCacheEnabled(true);
    setMinScale(this.mOverviewModeShrinkFactor - 0.2F);
    setupLayoutTransition();
    Resources localResources = getResources();
    try
    {
      this.mBackground = localResources.getDrawable(2130837507);
      label94:
      this.mWallpaperOffset = new WallpaperOffsetInterpolator();
      this.mLauncher.getWindowManager().getDefaultDisplay().getSize(this.mDisplaySize);
      this.mMaxDistanceForFolderCreation = (0.55F * localDeviceProfile.iconSizePx);
      this.mFlingThresholdVelocity = ((int)(500.0F * this.mDensity));
      return;
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      break label94;
    }
  }
  
  public long insertNewWorkspaceScreen(long paramLong)
  {
    return insertNewWorkspaceScreen(paramLong, getChildCount());
  }
  
  public long insertNewWorkspaceScreen(long paramLong, int paramInt)
  {
    if (this.mWorkspaceScreens.containsKey(Long.valueOf(paramLong))) {
      throw new RuntimeException("Screen id " + paramLong + " already exists!");
    }
    CellLayout localCellLayout = (CellLayout)this.mLauncher.getLayoutInflater().inflate(2130968935, null);
    localCellLayout.setOnLongClickListener(this.mLongClickListener);
    localCellLayout.setOnClickListener(this.mLauncher);
    localCellLayout.setSoundEffectsEnabled(false);
    this.mWorkspaceScreens.put(Long.valueOf(paramLong), localCellLayout);
    this.mScreenOrder.add(paramInt, Long.valueOf(paramLong));
    addView(localCellLayout, paramInt);
    return paramLong;
  }
  
  public long insertNewWorkspaceScreenBeforeEmptyScreen(long paramLong)
  {
    int i = this.mScreenOrder.indexOf(Long.valueOf(-201L));
    if (i < 0) {
      i = this.mScreenOrder.size();
    }
    return insertNewWorkspaceScreen(paramLong, i);
  }
  
  public boolean isDropEnabled()
  {
    return true;
  }
  
  public boolean isFinishedSwitchingState()
  {
    return (!this.mIsSwitchingState) || (this.mTransitionProgress > 0.5F);
  }
  
  public boolean isInOverviewMode()
  {
    return this.mState == State.OVERVIEW;
  }
  
  public boolean isOnOrMovingToCustomContent()
  {
    return (hasCustomContent()) && (getNextPage() == 0);
  }
  
  boolean isPointInSelfOverHotseat(int paramInt1, int paramInt2, Rect paramRect)
  {
    if (paramRect == null) {
      new Rect();
    }
    this.mTempPt[0] = paramInt1;
    this.mTempPt[1] = paramInt2;
    this.mLauncher.getDragLayer().getDescendantCoordRelativeToSelf(this, this.mTempPt, true);
    return LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().getHotseatRect().contains(this.mTempPt[0], this.mTempPt[1]);
  }
  
  public boolean isSmall()
  {
    return (this.mState == State.SMALL) || (this.mState == State.SPRING_LOADED) || (this.mState == State.OVERVIEW);
  }
  
  public boolean isSwitchingState()
  {
    return this.mIsSwitchingState;
  }
  
  boolean isTouchActive()
  {
    return this.mTouchState != 0;
  }
  
  void mapPointFromChildToSelf(View paramView, float[] paramArrayOfFloat)
  {
    paramArrayOfFloat[0] += paramView.getLeft();
    paramArrayOfFloat[1] += paramView.getTop();
  }
  
  void mapPointFromSelfToChild(View paramView, float[] paramArrayOfFloat, Matrix paramMatrix)
  {
    paramArrayOfFloat[0] -= paramView.getLeft();
    paramArrayOfFloat[1] -= paramView.getTop();
  }
  
  void mapPointFromSelfToHotseatLayout(Hotseat paramHotseat, float[] paramArrayOfFloat)
  {
    this.mTempPt[0] = ((int)paramArrayOfFloat[0]);
    this.mTempPt[1] = ((int)paramArrayOfFloat[1]);
    this.mLauncher.getDragLayer().getDescendantCoordRelativeToSelf(this, this.mTempPt, true);
    this.mLauncher.getDragLayer().mapCoordInSelfToDescendent(paramHotseat.getLayout(), this.mTempPt);
    paramArrayOfFloat[0] = this.mTempPt[0];
    paramArrayOfFloat[1] = this.mTempPt[1];
  }
  
  void moveToCustomContentScreen(boolean paramBoolean)
  {
    int i;
    if (hasCustomContent())
    {
      i = getPageIndexForScreenId(-301L);
      if (!paramBoolean) {
        break label40;
      }
      snapToPage(i);
    }
    for (;;)
    {
      View localView = getChildAt(i);
      if (localView != null) {
        localView.requestFocus();
      }
      return;
      label40:
      setCurrentPage(i);
    }
  }
  
  void moveToDefaultScreen(boolean paramBoolean)
  {
    moveToScreen(this.mDefaultPage, paramBoolean);
  }
  
  protected void notifyPageSwitchListener()
  {
    super.notifyPageSwitchListener();
    Launcher.setScreen(this.mCurrentPage);
    if ((hasCustomContent()) && (getNextPage() == 0) && (!this.mCustomContentShowing))
    {
      this.mCustomContentShowing = true;
      if (this.mCustomContentCallbacks != null)
      {
        this.mCustomContentCallbacks.onShow();
        this.mCustomContentShowTime = System.currentTimeMillis();
        this.mLauncher.updateVoiceButtonProxyVisible(false);
      }
    }
    for (;;)
    {
      if (getPageIndicator() != null) {
        getPageIndicator().setContentDescription(getPageIndicatorDescription());
      }
      return;
      if ((hasCustomContent()) && (getNextPage() != 0) && (this.mCustomContentShowing))
      {
        this.mCustomContentShowing = false;
        if (this.mCustomContentCallbacks != null)
        {
          this.mCustomContentCallbacks.onHide();
          this.mLauncher.resetQSBScroll();
          this.mLauncher.updateVoiceButtonProxyVisible(false);
        }
      }
    }
  }
  
  public int numCustomPages()
  {
    if (hasCustomContent()) {
      return 1;
    }
    return 0;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mWindowToken = getWindowToken();
    computeScroll();
    this.mDragController.setWindowToken(this.mWindowToken);
  }
  
  public void onChildViewAdded(View paramView1, View paramView2)
  {
    if (!(paramView2 instanceof CellLayout)) {
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    }
    CellLayout localCellLayout = (CellLayout)paramView2;
    localCellLayout.setOnInterceptTouchListener(this);
    localCellLayout.setClickable(true);
    localCellLayout.setImportantForAccessibility(2);
    super.onChildViewAdded(paramView1, paramView2);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mWindowToken = null;
  }
  
  public void onDragEnd()
  {
    this.mIsDragOccuring = false;
    updateChildrenLayersEnabled(false);
    this.mLauncher.unlockScreenOrientation(false);
    InstallShortcutReceiver.disableAndFlushInstallQueue(getContext());
    UninstallShortcutReceiver.disableAndFlushUninstallQueue(getContext());
    this.mDragSourceInternal = null;
    this.mLauncher.onInteractionEnd();
  }
  
  public void onDragEnter(DropTarget.DragObject paramDragObject)
  {
    this.mDragEnforcer.onDragEnter();
    this.mCreateUserFolderOnDrop = false;
    this.mAddToExistingFolderOnDrop = false;
    this.mDropToLayout = null;
    CellLayout localCellLayout = getCurrentDropLayout();
    setCurrentDropLayout(localCellLayout);
    setCurrentDragOverlappingLayout(localCellLayout);
    if (LauncherAppState.getInstance().isScreenLarge()) {
      showOutlines();
    }
  }
  
  public void onDragExit(DropTarget.DragObject paramDragObject)
  {
    this.mDragEnforcer.onDragExit();
    if (this.mInScrollArea) {
      if (isPageMoving())
      {
        this.mDropToLayout = ((CellLayout)getPageAt(getNextPage()));
        if (this.mDragMode != 1) {
          break label104;
        }
        this.mCreateUserFolderOnDrop = true;
      }
    }
    for (;;)
    {
      onResetScrollArea();
      setCurrentDropLayout(null);
      setCurrentDragOverlappingLayout(null);
      this.mSpringLoadedDragController.cancel();
      if (!this.mIsPageMoving) {
        hideOutlines();
      }
      return;
      this.mDropToLayout = this.mDragOverlappingLayout;
      break;
      this.mDropToLayout = this.mDragTargetLayout;
      break;
      label104:
      if (this.mDragMode == 2) {
        this.mAddToExistingFolderOnDrop = true;
      }
    }
  }
  
  public void onDragOver(DropTarget.DragObject paramDragObject)
  {
    if ((this.mInScrollArea) || (this.mIsSwitchingState) || (this.mState == State.SMALL)) {}
    label270:
    label306:
    label872:
    for (;;)
    {
      return;
      Rect localRect = new Rect();
      ItemInfo localItemInfo1 = (ItemInfo)paramDragObject.dragInfo;
      if ((localItemInfo1.spanX < 0) || (localItemInfo1.spanY < 0)) {
        throw new RuntimeException("Improper spans found");
      }
      this.mDragViewVisualCenter = getDragViewVisualCenter(paramDragObject.x, paramDragObject.y, paramDragObject.xOffset, paramDragObject.yOffset, paramDragObject.dragView, this.mDragViewVisualCenter);
      View localView1;
      label108:
      int n;
      label246:
      int i;
      int j;
      int k;
      int m;
      boolean bool1;
      if (this.mDragInfo == null)
      {
        localView1 = null;
        if (!isSmall()) {
          break label655;
        }
        Hotseat localHotseat2 = this.mLauncher.getHotseat();
        CellLayout localCellLayout3 = null;
        if (localHotseat2 != null)
        {
          boolean bool4 = isExternalDragWidget(paramDragObject);
          localCellLayout3 = null;
          if (!bool4)
          {
            boolean bool5 = isPointInSelfOverHotseat(paramDragObject.x, paramDragObject.y, localRect);
            localCellLayout3 = null;
            if (bool5) {
              localCellLayout3 = this.mLauncher.getHotseat().getLayout();
            }
          }
        }
        if (localCellLayout3 == null) {
          localCellLayout3 = findMatchingPageForDragOver(paramDragObject.dragView, paramDragObject.x, paramDragObject.y, false);
        }
        CellLayout localCellLayout4 = this.mDragTargetLayout;
        if (localCellLayout3 != localCellLayout4)
        {
          setCurrentDropLayout(localCellLayout3);
          setCurrentDragOverlappingLayout(localCellLayout3);
          if (this.mState != State.SPRING_LOADED) {
            break label635;
          }
          n = 1;
          if (n != 0)
          {
            if (!this.mLauncher.isHotseatLayout(localCellLayout3)) {
              break label641;
            }
            this.mSpringLoadedDragController.cancel();
          }
        }
        if (this.mDragTargetLayout == null) {
          break label759;
        }
        if (!this.mLauncher.isHotseatLayout(this.mDragTargetLayout)) {
          break label761;
        }
        mapPointFromSelfToHotseatLayout(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
        ItemInfo localItemInfo2 = (ItemInfo)paramDragObject.dragInfo;
        i = localItemInfo1.spanX;
        j = localItemInfo1.spanY;
        if ((localItemInfo1.minSpanX > 0) && (localItemInfo1.minSpanY > 0))
        {
          i = localItemInfo1.minSpanX;
          j = localItemInfo1.minSpanY;
        }
        this.mTargetCell = findNearestArea((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], i, j, this.mDragTargetLayout, this.mTargetCell);
        k = this.mTargetCell[0];
        m = this.mTargetCell[1];
        setCurrentDropOverCell(this.mTargetCell[0], this.mTargetCell[1]);
        float f = this.mDragTargetLayout.getDistanceFromCell(this.mDragViewVisualCenter[0], this.mDragViewVisualCenter[1], this.mTargetCell);
        View localView2 = this.mDragTargetLayout.getChildAt(this.mTargetCell[0], this.mTargetCell[1]);
        manageFolderFeedback(localItemInfo2, this.mDragTargetLayout, this.mTargetCell, f, localView2);
        bool1 = this.mDragTargetLayout.isNearestDropLocationOccupied((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], localItemInfo1.spanX, localItemInfo1.spanY, localView1, this.mTargetCell);
        if (bool1) {
          break label777;
        }
        this.mDragTargetLayout.visualizeDropLocation(localView1, this.mDragOutline, (int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], this.mTargetCell[0], this.mTargetCell[1], localItemInfo1.spanX, localItemInfo1.spanY, false, paramDragObject.dragView.getDragVisualizeOffset(), paramDragObject.dragView.getDragRegion());
      }
      for (;;)
      {
        if (((this.mDragMode != 1) && (this.mDragMode != 2) && (bool1)) || (this.mDragTargetLayout == null)) {
          break label872;
        }
        this.mDragTargetLayout.revertTempState();
        return;
        localView1 = this.mDragInfo.cell;
        break label108;
        label635:
        n = 0;
        break label246;
        this.mSpringLoadedDragController.setAlarm(this.mDragTargetLayout);
        break label270;
        Hotseat localHotseat1 = this.mLauncher.getHotseat();
        CellLayout localCellLayout1 = null;
        if (localHotseat1 != null)
        {
          boolean bool2 = isDragWidget(paramDragObject);
          localCellLayout1 = null;
          if (!bool2)
          {
            boolean bool3 = isPointInSelfOverHotseat(paramDragObject.x, paramDragObject.y, localRect);
            localCellLayout1 = null;
            if (bool3) {
              localCellLayout1 = this.mLauncher.getHotseat().getLayout();
            }
          }
        }
        if (localCellLayout1 == null) {
          localCellLayout1 = getCurrentDropLayout();
        }
        CellLayout localCellLayout2 = this.mDragTargetLayout;
        if (localCellLayout1 == localCellLayout2) {
          break label270;
        }
        setCurrentDropLayout(localCellLayout1);
        setCurrentDragOverlappingLayout(localCellLayout1);
        break label270;
        label759:
        break;
        label761:
        mapPointFromSelfToChild(this.mDragTargetLayout, this.mDragViewVisualCenter, null);
        break label306;
        if (((this.mDragMode == 0) || (this.mDragMode == 3)) && (!this.mReorderAlarm.alarmPending()) && ((this.mLastReorderX != k) || (this.mLastReorderY != m)))
        {
          ReorderAlarmListener localReorderAlarmListener = new ReorderAlarmListener(this.mDragViewVisualCenter, i, j, localItemInfo1.spanX, localItemInfo1.spanY, paramDragObject.dragView, localView1);
          this.mReorderAlarm.setOnAlarmListener(localReorderAlarmListener);
          this.mReorderAlarm.setAlarm(250L);
        }
      }
    }
  }
  
  public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt)
  {
    this.mIsDragOccuring = true;
    updateChildrenLayersEnabled(false);
    this.mLauncher.lockScreenOrientation();
    this.mLauncher.onInteractionBegin();
    setChildrenBackgroundAlphaMultipliers(1.0F);
    InstallShortcutReceiver.enableInstallQueue();
    UninstallShortcutReceiver.enableUninstallQueue();
    post(new Runnable()
    {
      public void run()
      {
        if (Workspace.this.mIsDragOccuring) {
          Workspace.this.addExtraEmptyScreenOnDrag();
        }
      }
    });
  }
  
  public void onDragStartedWithItem(View paramView)
  {
    this.mDragOutline = createDragOutline(paramView, new Canvas(), 2);
  }
  
  public void onDragStartedWithItem(PendingAddItemInfo paramPendingAddItemInfo, Bitmap paramBitmap, boolean paramBoolean)
  {
    Canvas localCanvas = new Canvas();
    int[] arrayOfInt = estimateItemSize(paramPendingAddItemInfo.spanX, paramPendingAddItemInfo.spanY, paramPendingAddItemInfo, false);
    this.mDragOutline = createDragOutline(paramBitmap, localCanvas, 2, arrayOfInt[0], arrayOfInt[1], paramBoolean);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if ((this.mBackground != null) && (this.mBackgroundAlpha > 0.0F) && (this.mDrawBackground))
    {
      int i = (int)(255.0F * this.mBackgroundAlpha);
      this.mBackground.setAlpha(i);
      this.mBackground.setBounds(getScrollX(), 0, getScrollX() + getMeasuredWidth(), getMeasuredHeight());
      this.mBackground.draw(paramCanvas);
    }
    super.onDraw(paramCanvas);
    post(this.mBindPages);
  }
  
  public void onDrop(DropTarget.DragObject paramDragObject)
  {
    this.mDragViewVisualCenter = getDragViewVisualCenter(paramDragObject.x, paramDragObject.y, paramDragObject.xOffset, paramDragObject.yOffset, paramDragObject.dragView, this.mDragViewVisualCenter);
    final CellLayout localCellLayout1 = this.mDropToLayout;
    int i;
    if (localCellLayout1 != null)
    {
      if (this.mLauncher.isHotseatLayout(localCellLayout1)) {
        mapPointFromSelfToHotseatLayout(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
      }
    }
    else
    {
      i = -1;
      if (paramDragObject.dragSource == this) {
        break label131;
      }
      arrayOfInt3 = new int[2];
      arrayOfInt3[0] = ((int)this.mDragViewVisualCenter[0]);
      arrayOfInt3[1] = ((int)this.mDragViewVisualCenter[1]);
      onDropExternal(arrayOfInt3, paramDragObject.dragInfo, localCellLayout1, false, paramDragObject);
    }
    label131:
    while (this.mDragInfo == null)
    {
      int[] arrayOfInt3;
      return;
      mapPointFromSelfToChild(localCellLayout1, this.mDragViewVisualCenter, null);
      break;
    }
    View localView = this.mDragInfo.cell;
    int j = 0;
    Runnable local111 = null;
    label207:
    label225:
    label241:
    label374:
    label380:
    int i7;
    if (localCellLayout1 != null)
    {
      boolean bool1 = paramDragObject.cancelled;
      j = 0;
      local111 = null;
      if (!bool1)
      {
        int n;
        boolean bool2;
        long l1;
        long l2;
        int i1;
        if (getParentCellLayoutForView(localView) != localCellLayout1)
        {
          n = 1;
          bool2 = this.mLauncher.isHotseatLayout(localCellLayout1);
          if (!bool2) {
            break label356;
          }
          l1 = -101L;
          if (this.mTargetCell[0] >= 0) {
            break label364;
          }
          l2 = this.mDragInfo.screenId;
          if (this.mDragInfo == null) {
            break label374;
          }
          i1 = this.mDragInfo.spanX;
          if (this.mDragInfo == null) {
            break label380;
          }
        }
        float f;
        for (int i2 = this.mDragInfo.spanY;; i2 = 1)
        {
          this.mTargetCell = findNearestArea((int)this.mDragViewVisualCenter[0], (int)this.mDragViewVisualCenter[1], i1, i2, localCellLayout1, this.mTargetCell);
          f = localCellLayout1.getDistanceFromCell(this.mDragViewVisualCenter[0], this.mDragViewVisualCenter[1], this.mTargetCell);
          if ((this.mInScrollArea) || (!createUserFolderIfNecessary(localView, l1, localCellLayout1, this.mTargetCell, f, false, paramDragObject.dragView, null))) {
            break label386;
          }
          removeExtraEmptyScreen(true, null, 0, true);
          return;
          n = 0;
          break;
          label356:
          l1 = -100L;
          break label207;
          label364:
          l2 = getIdForScreen(localCellLayout1);
          break label225;
          i1 = 1;
          break label241;
        }
        label386:
        if (addToExistingFolderIfNecessary(localView, localCellLayout1, this.mTargetCell, f, paramDragObject, false))
        {
          removeExtraEmptyScreen(true, null, 0, true);
          return;
        }
        ItemInfo localItemInfo2 = (ItemInfo)paramDragObject.dragInfo;
        int i3 = localItemInfo2.spanX;
        int i4 = localItemInfo2.spanY;
        if ((localItemInfo2.minSpanX > 0) && (localItemInfo2.minSpanY > 0))
        {
          i3 = localItemInfo2.minSpanX;
          i4 = localItemInfo2.minSpanY;
        }
        int[] arrayOfInt1 = new int[2];
        int i5 = (int)this.mDragViewVisualCenter[0];
        int i6 = (int)this.mDragViewVisualCenter[1];
        int[] arrayOfInt2 = this.mTargetCell;
        this.mTargetCell = localCellLayout1.createArea(i5, i6, i3, i4, i1, i2, localView, arrayOfInt2, arrayOfInt1, 1);
        if ((this.mTargetCell[0] < 0) || (this.mTargetCell[1] < 0)) {
          break label1143;
        }
        i7 = 1;
        j = 0;
        if (i7 != 0)
        {
          boolean bool5 = localView instanceof AppWidgetHostView;
          j = 0;
          if (bool5) {
            if (arrayOfInt1[0] == localItemInfo2.spanX)
            {
              int i23 = arrayOfInt1[1];
              int i24 = localItemInfo2.spanY;
              j = 0;
              if (i23 == i24) {}
            }
            else
            {
              j = 1;
              localItemInfo2.spanX = arrayOfInt1[0];
              localItemInfo2.spanY = arrayOfInt1[1];
              AppWidgetResizeFrame.updateWidgetSizeRanges((AppWidgetHostView)localView, this.mLauncher, arrayOfInt1[0], arrayOfInt1[1]);
            }
          }
        }
        if ((getScreenIdForPageIndex(this.mCurrentPage) != l2) && (!bool2))
        {
          i = getPageIndexForScreenId(l2);
          snapToPage(i);
        }
        if (i7 == 0) {
          break label1149;
        }
        final ItemInfo localItemInfo3 = (ItemInfo)localView.getTag();
        if (n != 0)
        {
          getParentCellLayoutForView(localView).removeView(localView);
          int i19 = this.mTargetCell[0];
          int i20 = this.mTargetCell[1];
          int i21 = localItemInfo3.spanX;
          int i22 = localItemInfo3.spanY;
          addInScreen(localView, l1, l2, i19, i20, i21, i22);
        }
        CellLayout.LayoutParams localLayoutParams2 = (CellLayout.LayoutParams)localView.getLayoutParams();
        int i8 = this.mTargetCell[0];
        localLayoutParams2.tmpCellX = i8;
        localLayoutParams2.cellX = i8;
        int i9 = this.mTargetCell[1];
        localLayoutParams2.tmpCellY = i9;
        localLayoutParams2.cellY = i9;
        localLayoutParams2.cellHSpan = localItemInfo2.spanX;
        localLayoutParams2.cellVSpan = localItemInfo2.spanY;
        localLayoutParams2.isLockedToGrid = true;
        long l3 = this.mDragInfo.screenId;
        int i10 = this.mTargetCell[0];
        int i11 = this.mTargetCell[1];
        int i12 = this.mDragInfo.spanX;
        int i13 = this.mDragInfo.spanY;
        localView.setId(LauncherModel.getCellLayoutChildId(l1, l3, i10, i11, i12, i13));
        boolean bool3 = l1 < -101L;
        local111 = null;
        if (bool3)
        {
          boolean bool4 = localView instanceof LauncherAppWidgetHostView;
          local111 = null;
          if (bool4)
          {
            final LauncherAppWidgetHostView localLauncherAppWidgetHostView = (LauncherAppWidgetHostView)localView;
            AppWidgetProviderInfo localAppWidgetProviderInfo = localLauncherAppWidgetHostView.getAppWidgetInfo();
            local111 = null;
            if (localAppWidgetProviderInfo != null)
            {
              int i18 = localAppWidgetProviderInfo.resizeMode;
              local111 = null;
              if (i18 != 0)
              {
                final Runnable local10 = new Runnable()
                {
                  public void run()
                  {
                    Workspace.this.mLauncher.getDragLayer().addResizeFrame(localItemInfo3, localLauncherAppWidgetHostView, localCellLayout1);
                  }
                };
                local111 = new Runnable()
                {
                  public void run()
                  {
                    if (!Workspace.this.isPageMoving())
                    {
                      local10.run();
                      return;
                    }
                    Workspace.access$1002(Workspace.this, local10);
                  }
                };
              }
            }
          }
        }
        Launcher localLauncher = this.mLauncher;
        int i14 = localLayoutParams2.cellX;
        int i15 = localLayoutParams2.cellY;
        int i16 = localItemInfo2.spanX;
        int i17 = localItemInfo2.spanY;
        LauncherModel.modifyItemInDatabase(localLauncher, localItemInfo3, l1, l2, i14, i15, i16, i17);
      }
    }
    label1043:
    CellLayout localCellLayout2 = (CellLayout)localView.getParent().getParent();
    final Runnable local112 = local111;
    Runnable local12 = new Runnable()
    {
      public void run()
      {
        Workspace.this.mAnimatingViewIntoPlace = false;
        Workspace.this.updateChildrenLayersEnabled(false);
        if (local112 != null) {
          local112.run();
        }
        Workspace.this.removeExtraEmptyScreen(true, null, 0, true);
      }
    };
    this.mAnimatingViewIntoPlace = true;
    int m;
    if (paramDragObject.dragView.hasDrawn())
    {
      ItemInfo localItemInfo1 = (ItemInfo)localView.getTag();
      if (localItemInfo1.itemType == 4) {
        if (j != 0)
        {
          m = 2;
          label1116:
          animateWidgetDrop(localItemInfo1, localCellLayout2, paramDragObject.dragView, local12, m, localView, false);
        }
      }
    }
    for (;;)
    {
      localCellLayout2.onDropChild(localView);
      return;
      label1143:
      i7 = 0;
      break;
      label1149:
      CellLayout.LayoutParams localLayoutParams1 = (CellLayout.LayoutParams)localView.getLayoutParams();
      this.mTargetCell[0] = localLayoutParams1.cellX;
      this.mTargetCell[1] = localLayoutParams1.cellY;
      ((CellLayout)localView.getParent().getParent()).markCellsAsOccupiedForView(localView);
      local111 = null;
      break label1043;
      m = 0;
      break label1116;
      if (i < 0) {}
      for (int k = -1;; k = 300)
      {
        this.mLauncher.getDragLayer().animateViewIntoPosition(paramDragObject.dragView, localView, k, local12, this);
        break;
      }
      paramDragObject.deferDragViewCleanupPostAnimation = false;
      localView.setVisibility(0);
    }
  }
  
  public void onDropCompleted(final View paramView, final DropTarget.DragObject paramDragObject, final boolean paramBoolean1, final boolean paramBoolean2)
  {
    if (this.mDeferDropAfterUninstall)
    {
      this.mDeferredAction = new Runnable()
      {
        public void run()
        {
          Workspace.this.onDropCompleted(paramView, paramDragObject, paramBoolean1, paramBoolean2);
          Workspace.access$2002(Workspace.this, null);
        }
      };
      return;
    }
    int i;
    if (this.mDeferredAction != null)
    {
      i = 1;
      if ((!paramBoolean2) || ((i != 0) && (!this.mUninstallSuccessful))) {
        break label189;
      }
      if ((paramView != this) && (this.mDragInfo != null))
      {
        localCellLayout2 = getParentCellLayoutForView(this.mDragInfo.cell);
        if (localCellLayout2 != null) {
          localCellLayout2.removeView(this.mDragInfo.cell);
        }
        if ((this.mDragInfo.cell instanceof DropTarget)) {
          this.mDragController.removeDropTarget((DropTarget)this.mDragInfo.cell);
        }
        removeExtraEmptyScreen(true, null, 0, true);
      }
    }
    label189:
    while (this.mDragInfo == null)
    {
      CellLayout localCellLayout2;
      if (((paramDragObject.cancelled) || ((i != 0) && (!this.mUninstallSuccessful))) && (this.mDragInfo.cell != null)) {
        this.mDragInfo.cell.setVisibility(0);
      }
      this.mDragOutline = null;
      this.mDragInfo = null;
      return;
      i = 0;
      break;
    }
    if (this.mLauncher.isHotseatLayout(paramView)) {}
    for (CellLayout localCellLayout1 = this.mLauncher.getHotseat().getLayout();; localCellLayout1 = getScreenWithId(this.mDragInfo.screenId))
    {
      localCellLayout1.onDropChild(this.mDragInfo.cell);
      break;
    }
  }
  
  protected void onEndReordering()
  {
    super.onEndReordering();
    hideOutlines();
    this.mScreenOrder.clear();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(j);
      this.mScreenOrder.add(Long.valueOf(getIdForScreen(localCellLayout)));
    }
    this.mLauncher.getModel().updateWorkspaceScreenOrder(this.mLauncher, this.mScreenOrder);
    enableLayoutTransitions();
  }
  
  public boolean onEnterScrollArea(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 1;
    int j;
    if (!LauncherAppState.isScreenLandscape(getContext()))
    {
      j = i;
      if ((this.mLauncher.getHotseat() == null) || (j == 0)) {
        break label71;
      }
      Rect localRect = new Rect();
      this.mLauncher.getHotseat().getHitRect(localRect);
      if (!localRect.contains(paramInt1, paramInt2)) {
        break label71;
      }
    }
    label71:
    int m;
    do
    {
      return false;
      j = 0;
      break;
      boolean bool1 = isSmall();
      bool2 = false;
      if (bool1) {
        break label201;
      }
      boolean bool3 = this.mIsSwitchingState;
      bool2 = false;
      if (bool3) {
        break label201;
      }
      Folder localFolder = getOpenFolder();
      bool2 = false;
      if (localFolder != null) {
        break label201;
      }
      this.mInScrollArea = i;
      int k = getNextPage();
      if (paramInt3 == 0) {
        i = -1;
      }
      m = k + i;
      setCurrentDropLayout(null);
      bool2 = false;
      if (m < 0) {
        break label201;
      }
      int n = getChildCount();
      bool2 = false;
      if (m >= n) {
        break label201;
      }
    } while (getScreenIdForPageIndex(m) == -301L);
    setCurrentDragOverlappingLayout((CellLayout)getChildAt(m));
    invalidate();
    boolean bool2 = true;
    label201:
    return bool2;
  }
  
  public boolean onExitScrollArea()
  {
    boolean bool1 = this.mInScrollArea;
    boolean bool2 = false;
    if (bool1)
    {
      invalidate();
      CellLayout localCellLayout = getCurrentDropLayout();
      setCurrentDropLayout(localCellLayout);
      setCurrentDragOverlappingLayout(localCellLayout);
      bool2 = true;
      this.mInScrollArea = false;
    }
    return bool2;
  }
  
  public void onFlingToDelete(DropTarget.DragObject paramDragObject, int paramInt1, int paramInt2, PointF paramPointF) {}
  
  public void onFlingToDeleteCompleted() {}
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (0xFF & paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return super.onInterceptTouchEvent(paramMotionEvent);
      this.mXDown = paramMotionEvent.getX();
      this.mYDown = paramMotionEvent.getY();
      this.mTouchDownTime = System.currentTimeMillis();
      continue;
      if ((this.mTouchState == 0) && (!((CellLayout)getChildAt(this.mCurrentPage)).lastDownOnOccupiedCell())) {
        onWallpaperTap(paramMotionEvent);
      }
    }
  }
  
  public void onLauncherTransitionEnd(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    onTransitionEnd();
  }
  
  public void onLauncherTransitionPrepare(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    onTransitionPrepare();
  }
  
  public void onLauncherTransitionStart(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2) {}
  
  public void onLauncherTransitionStep(Launcher paramLauncher, float paramFloat)
  {
    this.mTransitionProgress = paramFloat;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((this.mFirstLayout) && (this.mCurrentPage >= 0) && (this.mCurrentPage < getChildCount()))
    {
      this.mWallpaperOffset.syncWithScroll();
      this.mWallpaperOffset.jumpToFinal();
    }
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onPageBeginMoving()
  {
    super.onPageBeginMoving();
    if (isHardwareAccelerated()) {
      updateChildrenLayersEnabled(false);
    }
    for (;;)
    {
      if (LauncherAppState.getInstance().isScreenLarge()) {
        showOutlines();
      }
      if (this.mWorkspaceFadeInAdjacentScreens) {
        break;
      }
      for (int i = 0; i < getChildCount(); i++) {
        ((CellLayout)getPageAt(i)).setShortcutAndWidgetAlpha(1.0F);
      }
      if (this.mNextPage != -1) {
        enableChildrenCache(this.mCurrentPage, this.mNextPage);
      } else {
        enableChildrenCache(-1 + this.mCurrentPage, 1 + this.mCurrentPage);
      }
    }
  }
  
  protected void onPageEndMoving()
  {
    super.onPageEndMoving();
    if (isHardwareAccelerated())
    {
      updateChildrenLayersEnabled(false);
      if (!this.mDragController.isDragging()) {
        break label106;
      }
      if (isSmall()) {
        this.mDragController.forceTouchMove();
      }
    }
    for (;;)
    {
      if (this.mDelayedResizeRunnable != null)
      {
        this.mDelayedResizeRunnable.run();
        this.mDelayedResizeRunnable = null;
      }
      if (this.mDelayedSnapToPageRunnable != null)
      {
        this.mDelayedSnapToPageRunnable.run();
        this.mDelayedSnapToPageRunnable = null;
      }
      if (this.mStripScreensOnPageStopMoving)
      {
        stripEmptyScreens();
        this.mStripScreensOnPageStopMoving = false;
      }
      return;
      clearChildrenCache();
      break;
      label106:
      if (LauncherAppState.getInstance().isScreenLarge()) {
        hideOutlines();
      }
    }
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    if (!this.mLauncher.isAllAppsVisible())
    {
      Folder localFolder = getOpenFolder();
      if (localFolder != null) {
        return localFolder.requestFocus(paramInt, paramRect);
      }
      return super.onRequestFocusInDescendants(paramInt, paramRect);
    }
    return false;
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    super.onRestoreInstanceState(paramParcelable);
    Launcher.setScreen(this.mCurrentPage);
  }
  
  protected void onResume()
  {
    if (getPageIndicator() != null)
    {
      View.OnClickListener localOnClickListener = getPageIndicatorClickListener();
      if (localOnClickListener != null) {
        getPageIndicator().setOnClickListener(localOnClickListener);
      }
    }
    sAccessibilityEnabled = ((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled();
  }
  
  protected void onStartReordering()
  {
    super.onStartReordering();
    showOutlines();
    disableLayoutTransitions();
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return (isSmall()) || (!isFinishedSwitchingState()) || ((!isSmall()) && (indexOfChild(paramView) != this.mCurrentPage));
  }
  
  public void onUninstallActivityReturned(boolean paramBoolean)
  {
    this.mDeferDropAfterUninstall = false;
    this.mUninstallSuccessful = paramBoolean;
    if (this.mDeferredAction != null) {
      this.mDeferredAction.run();
    }
  }
  
  protected void onWallpaperTap(MotionEvent paramMotionEvent)
  {
    int[] arrayOfInt = this.mTempCell;
    getLocationOnScreen(arrayOfInt);
    int i = paramMotionEvent.getActionIndex();
    arrayOfInt[0] += (int)paramMotionEvent.getX(i);
    arrayOfInt[1] += (int)paramMotionEvent.getY(i);
    WallpaperManager localWallpaperManager = this.mWallpaperManager;
    IBinder localIBinder = getWindowToken();
    if (paramMotionEvent.getAction() == 1) {}
    for (String str = "android.wallpaper.tap";; str = "android.wallpaper.secondaryTap")
    {
      localWallpaperManager.sendWallpaperCommand(localIBinder, str, arrayOfInt[0], arrayOfInt[1], 0, null);
      return;
    }
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    this.mLauncher.onWindowVisibilityChanged(paramInt);
  }
  
  protected void overScroll(float paramFloat)
  {
    acceleratedOverScroll(paramFloat);
  }
  
  protected void reinflateWidgetsIfNecessary()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(j);
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = localCellLayout.getShortcutsAndWidgets();
      int k = localShortcutAndWidgetContainer.getChildCount();
      for (int m = 0; m < k; m++)
      {
        View localView = localShortcutAndWidgetContainer.getChildAt(m);
        if ((localView.getTag() instanceof LauncherAppWidgetInfo))
        {
          LauncherAppWidgetInfo localLauncherAppWidgetInfo = (LauncherAppWidgetInfo)localView.getTag();
          LauncherAppWidgetHostView localLauncherAppWidgetHostView = (LauncherAppWidgetHostView)localLauncherAppWidgetInfo.hostView;
          if ((localLauncherAppWidgetHostView != null) && (localLauncherAppWidgetHostView.orientationChangedSincedInflation()))
          {
            this.mLauncher.removeAppWidget(localLauncherAppWidgetInfo);
            localCellLayout.removeView(localLauncherAppWidgetHostView);
            this.mLauncher.bindAppWidget(localLauncherAppWidgetInfo);
          }
        }
      }
    }
  }
  
  public void removeAllWorkspaceScreens()
  {
    disableLayoutTransitions();
    if (hasCustomContent()) {
      removeCustomContentPage();
    }
    removeAllViews();
    this.mScreenOrder.clear();
    this.mWorkspaceScreens.clear();
    enableLayoutTransitions();
  }
  
  public void removeCustomContentPage()
  {
    CellLayout localCellLayout = getScreenWithId(-301L);
    if (localCellLayout == null) {
      throw new RuntimeException("Expected custom content screen to exist");
    }
    this.mWorkspaceScreens.remove(Long.valueOf(-301L));
    this.mScreenOrder.remove(Long.valueOf(-301L));
    removeView(localCellLayout);
    if (this.mCustomContentCallbacks != null)
    {
      this.mCustomContentCallbacks.onScrollProgressChanged(0.0F);
      this.mCustomContentCallbacks.onHide();
    }
    this.mCustomContentCallbacks = null;
    this.mDefaultPage = (-1 + this.mOriginalDefaultPage);
    this.mLauncher.updateCustomContentHintVisibility();
    if (this.mRestorePage != -1001)
    {
      this.mRestorePage = (-1 + this.mRestorePage);
      return;
    }
    setCurrentPage(-1 + getCurrentPage());
  }
  
  public void removeExtraEmptyScreen(boolean paramBoolean, Runnable paramRunnable)
  {
    removeExtraEmptyScreen(paramBoolean, paramRunnable, 0, false);
  }
  
  public void removeExtraEmptyScreen(final boolean paramBoolean1, final Runnable paramRunnable, int paramInt, final boolean paramBoolean2)
  {
    if (paramInt > 0) {
      postDelayed(new Runnable()
      {
        public void run()
        {
          Workspace.this.removeExtraEmptyScreen(paramBoolean1, paramRunnable, 0, paramBoolean2);
        }
      }, paramInt);
    }
    do
    {
      return;
      convertFinalScreenToEmptyScreenIfNecessary();
      if (hasExtraEmptyScreen())
      {
        int i = this.mScreenOrder.indexOf(Long.valueOf(-201L));
        if (getNextPage() == i)
        {
          snapToPage(-1 + getNextPage(), 400);
          fadeAndRemoveEmptyScreen(400, 150, paramRunnable, paramBoolean2);
          return;
        }
        fadeAndRemoveEmptyScreen(0, 150, paramRunnable, paramBoolean2);
        return;
      }
    } while (paramRunnable == null);
    paramRunnable.run();
  }
  
  void removeItemsByApplicationInfo(ArrayList<AppInfo> paramArrayList)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext()) {
      localHashSet.add(((AppInfo)localIterator.next()).componentName);
    }
    removeItemsByComponentName(localHashSet);
  }
  
  void removeItemsByComponentName(final HashSet<ComponentName> paramHashSet)
  {
    Iterator localIterator1 = getWorkspaceAndHotseatCellLayouts().iterator();
    while (localIterator1.hasNext())
    {
      CellLayout localCellLayout = (CellLayout)localIterator1.next();
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = localCellLayout.getShortcutsAndWidgets();
      final HashMap localHashMap1 = new HashMap();
      for (int i = 0; i < localShortcutAndWidgetContainer.getChildCount(); i++)
      {
        View localView2 = localShortcutAndWidgetContainer.getChildAt(i);
        localHashMap1.put((ItemInfo)localView2.getTag(), localView2);
      }
      final ArrayList localArrayList = new ArrayList();
      final HashMap localHashMap2 = new HashMap();
      LauncherModel.ItemInfoFilter local18 = new LauncherModel.ItemInfoFilter()
      {
        public boolean filterItem(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2, ComponentName paramAnonymousComponentName)
        {
          if ((paramAnonymousItemInfo1 instanceof FolderInfo))
          {
            if (paramHashSet.contains(paramAnonymousComponentName))
            {
              FolderInfo localFolderInfo = (FolderInfo)paramAnonymousItemInfo1;
              ArrayList localArrayList;
              if (localHashMap2.containsKey(localFolderInfo)) {
                localArrayList = (ArrayList)localHashMap2.get(localFolderInfo);
              }
              for (;;)
              {
                localArrayList.add((ShortcutInfo)paramAnonymousItemInfo2);
                return true;
                localArrayList = new ArrayList();
                localHashMap2.put(localFolderInfo, localArrayList);
              }
            }
          }
          else if (paramHashSet.contains(paramAnonymousComponentName))
          {
            localArrayList.add(localHashMap1.get(paramAnonymousItemInfo2));
            return true;
          }
          return false;
        }
      };
      LauncherModel.filterItemInfos(localHashMap1.keySet(), local18);
      Iterator localIterator2 = localHashMap2.keySet().iterator();
      while (localIterator2.hasNext())
      {
        FolderInfo localFolderInfo = (FolderInfo)localIterator2.next();
        Iterator localIterator4 = ((ArrayList)localHashMap2.get(localFolderInfo)).iterator();
        while (localIterator4.hasNext()) {
          localFolderInfo.remove((ShortcutInfo)localIterator4.next());
        }
      }
      Iterator localIterator3 = localArrayList.iterator();
      while (localIterator3.hasNext())
      {
        View localView1 = (View)localIterator3.next();
        localCellLayout.removeViewInLayout(localView1);
        if ((localView1 instanceof DropTarget)) {
          this.mDragController.removeDropTarget((DropTarget)localView1);
        }
      }
      if (localArrayList.size() > 0)
      {
        localShortcutAndWidgetContainer.requestLayout();
        localShortcutAndWidgetContainer.invalidate();
      }
    }
    stripEmptyScreens();
  }
  
  void removeItemsByPackageName(ArrayList<String> paramArrayList)
  {
    final HashSet localHashSet1 = new HashSet();
    localHashSet1.addAll(paramArrayList);
    HashSet localHashSet2 = new HashSet();
    final HashSet localHashSet3 = new HashSet();
    Iterator localIterator = getWorkspaceAndHotseatCellLayouts().iterator();
    while (localIterator.hasNext())
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = ((CellLayout)localIterator.next()).getShortcutsAndWidgets();
      int i = localShortcutAndWidgetContainer.getChildCount();
      for (int j = 0; j < i; j++) {
        localHashSet2.add((ItemInfo)localShortcutAndWidgetContainer.getChildAt(j).getTag());
      }
    }
    LauncherModel.filterItemInfos(localHashSet2, new LauncherModel.ItemInfoFilter()
    {
      public boolean filterItem(ItemInfo paramAnonymousItemInfo1, ItemInfo paramAnonymousItemInfo2, ComponentName paramAnonymousComponentName)
      {
        if (localHashSet1.contains(paramAnonymousComponentName.getPackageName()))
        {
          localHashSet3.add(paramAnonymousComponentName);
          return true;
        }
        return false;
      }
    });
    removeItemsByComponentName(localHashSet3);
  }
  
  public void resetFinalScrollForPageChange(int paramInt)
  {
    if (paramInt >= 0)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(paramInt);
      setScrollX(this.mSavedScrollX);
      localCellLayout.setTranslationX(this.mSavedTranslationX);
      localCellLayout.setRotationY(this.mSavedRotationY);
    }
  }
  
  public void resetTransitionTransform(CellLayout paramCellLayout)
  {
    if (isSwitchingState())
    {
      setScaleX(this.mCurrentScale);
      setScaleY(this.mCurrentScale);
    }
  }
  
  public void restoreInstanceStateForChild(int paramInt)
  {
    if (this.mSavedStates != null)
    {
      this.mRestoredPages.add(Integer.valueOf(paramInt));
      ((CellLayout)getChildAt(paramInt)).restoreInstanceState(this.mSavedStates);
    }
  }
  
  public void restoreInstanceStateForRemainingPages()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      if (!this.mRestoredPages.contains(Integer.valueOf(j))) {
        restoreInstanceStateForChild(j);
      }
    }
    this.mRestoredPages.clear();
    this.mSavedStates = null;
  }
  
  protected void screenScrolled(int paramInt)
  {
    boolean bool1 = isLayoutRtl();
    super.screenScrolled(paramInt);
    updatePageAlphaValues(paramInt);
    updateStateForCustomContent(paramInt);
    enableHwLayersOnVisiblePages();
    int i;
    if (((this.mOverScrollX < 0) && ((!hasCustomContent()) || (isLayoutRtl()))) || ((this.mOverScrollX > this.mMaxScrollX) && ((!hasCustomContent()) || (!isLayoutRtl()))))
    {
      i = 1;
      if (i == 0) {
        break label277;
      }
      j = -1 + getChildCount();
      if (this.mOverScrollX >= 0) {
        break label256;
      }
      bool2 = true;
      if (((bool1) || (!bool2)) && ((!bool1) || (bool2))) {
        break label262;
      }
      k = 0;
      if (!bool2) {
        break label269;
      }
      f1 = 0.75F;
      localCellLayout = (CellLayout)getChildAt(k);
      f2 = getScrollProgress(paramInt, localCellLayout, k);
      localCellLayout.setOverScrollAmount(Math.abs(f2), bool2);
      localCellLayout.setRotationY(-24.0F * f2);
      if ((!this.mOverscrollTransformsSet) || (Float.compare(this.mLastOverscrollPivotX, f1) != 0))
      {
        this.mOverscrollTransformsSet = true;
        this.mLastOverscrollPivotX = f1;
        localCellLayout.setCameraDistance(this.mDensity * this.mCameraDistance);
        localCellLayout.setPivotX(f1 * localCellLayout.getMeasuredWidth());
        localCellLayout.setPivotY(0.5F * localCellLayout.getMeasuredHeight());
        localCellLayout.setOverscrollTransformsDirty(true);
      }
    }
    label256:
    label262:
    label269:
    label277:
    while (!this.mOverscrollTransformsSet) {
      for (;;)
      {
        int j;
        CellLayout localCellLayout;
        float f2;
        return;
        i = 0;
        break;
        boolean bool2 = false;
        continue;
        int k = j;
        continue;
        float f1 = 0.25F;
      }
    }
    this.mOverscrollTransformsSet = false;
    ((CellLayout)getChildAt(0)).resetOverscrollTransforms();
    ((CellLayout)getChildAt(-1 + getChildCount())).resetOverscrollTransforms();
  }
  
  public void scrollLeft()
  {
    if ((!isSmall()) && (!this.mIsSwitchingState)) {
      super.scrollLeft();
    }
    Folder localFolder = getOpenFolder();
    if (localFolder != null) {
      localFolder.completeDragExit();
    }
  }
  
  public void scrollRight()
  {
    if ((!isSmall()) && (!this.mIsSwitchingState)) {
      super.scrollRight();
    }
    Folder localFolder = getOpenFolder();
    if (localFolder != null) {
      localFolder.completeDragExit();
    }
  }
  
  public void setBackgroundAlpha(float paramFloat)
  {
    if (paramFloat != this.mBackgroundAlpha)
    {
      this.mBackgroundAlpha = paramFloat;
      invalidate();
    }
  }
  
  public void setChildrenOutlineAlpha(float paramFloat)
  {
    this.mChildrenOutlineAlpha = paramFloat;
    for (int i = 0; i < getChildCount(); i++) {
      ((CellLayout)getChildAt(i)).setBackgroundAlpha(paramFloat);
    }
  }
  
  void setCurrentDragOverlappingLayout(CellLayout paramCellLayout)
  {
    if (this.mDragOverlappingLayout != null) {
      this.mDragOverlappingLayout.setIsDragOverlapping(false);
    }
    this.mDragOverlappingLayout = paramCellLayout;
    if (this.mDragOverlappingLayout != null) {
      this.mDragOverlappingLayout.setIsDragOverlapping(true);
    }
    invalidate();
  }
  
  void setCurrentDropLayout(CellLayout paramCellLayout)
  {
    if (this.mDragTargetLayout != null)
    {
      this.mDragTargetLayout.revertTempState();
      this.mDragTargetLayout.onDragExit();
    }
    this.mDragTargetLayout = paramCellLayout;
    if (this.mDragTargetLayout != null) {
      this.mDragTargetLayout.onDragEnter();
    }
    cleanupReorder(true);
    cleanupFolderCreation();
    setCurrentDropOverCell(-1, -1);
  }
  
  void setCurrentDropOverCell(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != this.mDragOverX) || (paramInt2 != this.mDragOverY))
    {
      this.mDragOverX = paramInt1;
      this.mDragOverY = paramInt2;
      setDragMode(0);
    }
  }
  
  void setDragMode(int paramInt)
  {
    if (paramInt != this.mDragMode)
    {
      if (paramInt != 0) {
        break label31;
      }
      cleanupAddToFolder();
      cleanupReorder(false);
      cleanupFolderCreation();
    }
    for (;;)
    {
      this.mDragMode = paramInt;
      return;
      label31:
      if (paramInt == 2)
      {
        cleanupReorder(true);
        cleanupFolderCreation();
      }
      else if (paramInt == 1)
      {
        cleanupAddToFolder();
        cleanupReorder(true);
      }
      else if (paramInt == 3)
      {
        cleanupAddToFolder();
        cleanupFolderCreation();
      }
    }
  }
  
  public void setFinalScrollForPageChange(int paramInt)
  {
    CellLayout localCellLayout = (CellLayout)getChildAt(paramInt);
    if (localCellLayout != null)
    {
      this.mSavedScrollX = getScrollX();
      this.mSavedTranslationX = localCellLayout.getTranslationX();
      this.mSavedRotationY = localCellLayout.getRotationY();
      setScrollX(getScrollForPage(paramInt));
      localCellLayout.setTranslationX(0.0F);
      localCellLayout.setRotationY(0.0F);
    }
  }
  
  public void setFinalTransitionTransform(CellLayout paramCellLayout)
  {
    if (isSwitchingState())
    {
      this.mCurrentScale = getScaleX();
      setScaleX(this.mNewScale);
      setScaleY(this.mNewScale);
    }
  }
  
  public void setInsets(Rect paramRect)
  {
    this.mInsets.set(paramRect);
  }
  
  protected void setWallpaperDimension()
  {
    String str = WallpaperCropActivity.getSharedPreferencesKey();
    SharedPreferences localSharedPreferences = this.mLauncher.getSharedPreferences(str, 4);
    WallpaperPickerActivity.suggestWallpaperDimension(this.mLauncher.getResources(), localSharedPreferences, this.mLauncher.getWindowManager(), this.mWallpaperManager);
  }
  
  void setup(DragController paramDragController)
  {
    this.mSpringLoadedDragController = new SpringLoadedDragController(this.mLauncher);
    this.mDragController = paramDragController;
    updateChildrenLayersEnabled(false);
    setWallpaperDimension();
  }
  
  protected boolean shouldDrawChild(View paramView)
  {
    CellLayout localCellLayout = (CellLayout)paramView;
    return (super.shouldDrawChild(paramView)) && ((this.mIsSwitchingState) || (localCellLayout.getShortcutsAndWidgets().getAlpha() > 0.0F) || (localCellLayout.getBackgroundAlpha() > 0.0F));
  }
  
  boolean shouldVoiceButtonProxyBeVisible()
  {
    if (isOnOrMovingToCustomContent()) {}
    while (this.mState != State.NORMAL) {
      return false;
    }
    return true;
  }
  
  void showCustomContentIfNecessary()
  {
    if (this.mState == State.NORMAL) {}
    for (int i = 1;; i = 0)
    {
      if ((i != 0) && (hasCustomContent())) {
        ((CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-301L))).setVisibility(0);
      }
      return;
    }
  }
  
  void showOutlines()
  {
    if ((!isSmall()) && (!this.mIsSwitchingState))
    {
      if (this.mChildrenOutlineFadeOutAnimation != null) {
        this.mChildrenOutlineFadeOutAnimation.cancel();
      }
      if (this.mChildrenOutlineFadeInAnimation != null) {
        this.mChildrenOutlineFadeInAnimation.cancel();
      }
      this.mChildrenOutlineFadeInAnimation = LauncherAnimUtils.ofFloat(this, "childrenOutlineAlpha", new float[] { 1.0F });
      this.mChildrenOutlineFadeInAnimation.setDuration(100L);
      this.mChildrenOutlineFadeInAnimation.start();
    }
  }
  
  public void showOutlinesTemporarily()
  {
    if ((!this.mIsPageMoving) && (!isTouchActive())) {
      snapToPage(this.mCurrentPage);
    }
  }
  
  protected void snapToPage(int paramInt1, int paramInt2, Runnable paramRunnable)
  {
    if (this.mDelayedSnapToPageRunnable != null) {
      this.mDelayedSnapToPageRunnable.run();
    }
    this.mDelayedSnapToPageRunnable = paramRunnable;
    snapToPage(paramInt1, paramInt2);
  }
  
  protected void snapToPage(int paramInt, Runnable paramRunnable)
  {
    snapToPage(paramInt, 950, paramRunnable);
  }
  
  protected void snapToScreenId(long paramLong, Runnable paramRunnable)
  {
    snapToPage(getPageIndexForScreenId(paramLong), paramRunnable);
  }
  
  void startDrag(CellLayout.CellInfo paramCellInfo)
  {
    View localView = paramCellInfo.cell;
    if (!localView.isInTouchMode()) {
      return;
    }
    this.mDragInfo = paramCellInfo;
    localView.setVisibility(4);
    ((CellLayout)localView.getParent().getParent()).prepareChildForDrag(localView);
    localView.clearFocus();
    localView.setPressed(false);
    this.mDragOutline = createDragOutline(localView, new Canvas(), 2);
    beginDragShared(localView, this);
  }
  
  public void stripEmptyScreens()
  {
    if (isPageMoving()) {
      this.mStripScreensOnPageStopMoving = true;
    }
    int i;
    int k;
    do
    {
      return;
      i = getNextPage();
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator1 = this.mWorkspaceScreens.keySet().iterator();
      while (localIterator1.hasNext())
      {
        Long localLong2 = (Long)localIterator1.next();
        CellLayout localCellLayout2 = (CellLayout)this.mWorkspaceScreens.get(localLong2);
        if ((localLong2.longValue() >= 0L) && (localCellLayout2.getShortcutsAndWidgets().getChildCount() == 0)) {
          localArrayList.add(localLong2);
        }
      }
      int j = 1 + numCustomPages();
      k = 0;
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        Long localLong1 = (Long)localIterator2.next();
        CellLayout localCellLayout1 = (CellLayout)this.mWorkspaceScreens.get(localLong1);
        this.mWorkspaceScreens.remove(localLong1);
        this.mScreenOrder.remove(localLong1);
        if (getChildCount() > j)
        {
          if (indexOfChild(localCellLayout1) < i) {
            k++;
          }
          removeView(localCellLayout1);
        }
        else
        {
          this.mRemoveEmptyScreenRunnable = null;
          this.mWorkspaceScreens.put(Long.valueOf(-201L), localCellLayout1);
          this.mScreenOrder.add(Long.valueOf(-201L));
        }
      }
      if (!localArrayList.isEmpty()) {
        this.mLauncher.getModel().updateWorkspaceScreenOrder(this.mLauncher, this.mScreenOrder);
      }
    } while (k < 0);
    setCurrentPage(i - k);
  }
  
  public boolean supportsFlingToDelete()
  {
    return true;
  }
  
  public void syncPageItems(int paramInt, boolean paramBoolean) {}
  
  public void syncPages() {}
  
  public boolean transitionStateShouldAllowDrop()
  {
    return ((!isSwitchingState()) || (this.mTransitionProgress > 0.5F)) && (this.mState != State.SMALL);
  }
  
  void updateCustomContentVisibility()
  {
    if (this.mState == State.NORMAL) {}
    for (int i = 0;; i = 4)
    {
      if (hasCustomContent()) {
        ((CellLayout)this.mWorkspaceScreens.get(Long.valueOf(-301L))).setVisibility(i);
      }
      return;
    }
  }
  
  public void updateInteractionForState()
  {
    if (this.mState != State.NORMAL)
    {
      this.mLauncher.onInteractionBegin();
      return;
    }
    this.mLauncher.onInteractionEnd();
  }
  
  void updateItemLocationsInDatabase(CellLayout paramCellLayout)
  {
    int i = paramCellLayout.getShortcutsAndWidgets().getChildCount();
    long l = getIdForScreen(paramCellLayout);
    int j = -100;
    if (this.mLauncher.isHotseatLayout(paramCellLayout))
    {
      l = -1L;
      j = -101;
    }
    for (int k = 0; k < i; k++)
    {
      ItemInfo localItemInfo = (ItemInfo)paramCellLayout.getShortcutsAndWidgets().getChildAt(k).getTag();
      if ((localItemInfo != null) && (localItemInfo.requiresDbUpdate))
      {
        localItemInfo.requiresDbUpdate = false;
        LauncherModel.modifyItemInDatabase(this.mLauncher, localItemInfo, j, l, localItemInfo.cellX, localItemInfo.cellY, localItemInfo.spanX, localItemInfo.spanY);
      }
    }
  }
  
  void updateShortcuts(ArrayList<AppInfo> paramArrayList)
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
    {
      AppInfo localAppInfo = (AppInfo)localIterator1.next();
      localHashMap.put(localAppInfo.componentName, localAppInfo);
    }
    Iterator localIterator2 = getAllShortcutAndWidgetContainers().iterator();
    if (localIterator2.hasNext())
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)localIterator2.next();
      new HashMap();
      int i = 0;
      label90:
      View localView1;
      ItemInfo localItemInfo;
      if (i < localShortcutAndWidgetContainer.getChildCount())
      {
        localView1 = localShortcutAndWidgetContainer.getChildAt(i);
        localItemInfo = (ItemInfo)localView1.getTag();
        if ((!(localItemInfo instanceof FolderInfo)) || (!(localView1 instanceof FolderIcon))) {
          break label206;
        }
        FolderIcon localFolderIcon = (FolderIcon)localView1;
        Iterator localIterator3 = localFolderIcon.getFolder().getItemsInReadingOrder().iterator();
        while (localIterator3.hasNext())
        {
          View localView2 = (View)localIterator3.next();
          updateShortcut(localHashMap, (ItemInfo)localView2.getTag(), localView2);
        }
        localFolderIcon.invalidate();
      }
      for (;;)
      {
        i++;
        break label90;
        break;
        label206:
        if ((localItemInfo instanceof ShortcutInfo)) {
          updateShortcut(localHashMap, localItemInfo, localView1);
        }
      }
    }
  }
  
  boolean willAddToExistingUserFolder(Object paramObject, CellLayout paramCellLayout, int[] paramArrayOfInt, float paramFloat)
  {
    if (paramFloat > this.mMaxDistanceForFolderCreation) {}
    View localView;
    do
    {
      CellLayout.LayoutParams localLayoutParams;
      do
      {
        return false;
        localView = paramCellLayout.getChildAt(paramArrayOfInt[0], paramArrayOfInt[1]);
        if (localView == null) {
          break;
        }
        localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
      } while ((localLayoutParams.useTmpCoords) && ((localLayoutParams.tmpCellX != localLayoutParams.cellX) || (localLayoutParams.tmpCellY != localLayoutParams.tmpCellY)));
    } while ((!(localView instanceof FolderIcon)) || (!((FolderIcon)localView).acceptDrop(paramObject)));
    return true;
  }
  
  boolean willCreateUserFolder(ItemInfo paramItemInfo, CellLayout paramCellLayout, int[] paramArrayOfInt, float paramFloat, boolean paramBoolean)
  {
    int i = 1;
    if (paramFloat > this.mMaxDistanceForFolderCreation) {}
    View localView;
    CellLayout.LayoutParams localLayoutParams;
    do
    {
      return false;
      localView = paramCellLayout.getChildAt(paramArrayOfInt[0], paramArrayOfInt[i]);
      if (localView == null) {
        break;
      }
      localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
    } while ((localLayoutParams.useTmpCoords) && ((localLayoutParams.tmpCellX != localLayoutParams.cellX) || (localLayoutParams.tmpCellY != localLayoutParams.tmpCellY)));
    CellLayout.CellInfo localCellInfo = this.mDragInfo;
    int k = 0;
    if (localCellInfo != null)
    {
      if (localView == this.mDragInfo.cell) {
        k = i;
      }
    }
    else
    {
      label107:
      if ((localView == null) || (k != 0) || ((paramBoolean) && (!this.mCreateUserFolderOnDrop))) {
        break label176;
      }
      boolean bool = localView.getTag() instanceof ShortcutInfo;
      if ((paramItemInfo.itemType != 0) && (paramItemInfo.itemType != i)) {
        break label178;
      }
      int m = i;
      label159:
      if ((!bool) || (m == 0)) {
        break label184;
      }
    }
    for (;;)
    {
      return i;
      k = 0;
      break label107;
      label176:
      break;
      label178:
      int n = 0;
      break label159;
      label184:
      int j = 0;
    }
  }
  
  static class AlphaUpdateListener
    implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener
  {
    View view;
    
    public AlphaUpdateListener(View paramView)
    {
      this.view = paramView;
    }
    
    public static void updateVisibility(View paramView)
    {
      int i;
      if (Workspace.sAccessibilityEnabled)
      {
        i = 8;
        if ((paramView.getAlpha() >= 0.01F) || (paramView.getVisibility() == i)) {
          break label38;
        }
        paramView.setVisibility(i);
      }
      label38:
      while ((paramView.getAlpha() <= 0.01F) || (paramView.getVisibility() == 0))
      {
        return;
        i = 4;
        break;
      }
      paramView.setVisibility(0);
    }
    
    public void onAnimationCancel(Animator paramAnimator) {}
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      updateVisibility(this.view);
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator)
    {
      this.view.setVisibility(0);
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      updateVisibility(this.view);
    }
  }
  
  class FolderCreationAlarmListener
    implements OnAlarmListener
  {
    int cellX;
    int cellY;
    CellLayout layout;
    
    public FolderCreationAlarmListener(CellLayout paramCellLayout, int paramInt1, int paramInt2)
    {
      this.layout = paramCellLayout;
      this.cellX = paramInt1;
      this.cellY = paramInt2;
    }
    
    public void onAlarm(Alarm paramAlarm)
    {
      if (Workspace.this.mDragFolderRingAnimator != null) {
        Workspace.this.mDragFolderRingAnimator.animateToNaturalState();
      }
      Workspace.access$1202(Workspace.this, new FolderIcon.FolderRingAnimator(Workspace.this.mLauncher, null));
      Workspace.this.mDragFolderRingAnimator.setCell(this.cellX, this.cellY);
      Workspace.this.mDragFolderRingAnimator.setCellLayout(this.layout);
      Workspace.this.mDragFolderRingAnimator.animateToAcceptState();
      this.layout.showFolderAccept(Workspace.this.mDragFolderRingAnimator);
      this.layout.clearDragOutlines();
      Workspace.this.setDragMode(1);
    }
  }
  
  static class InverseZInterpolator
    implements TimeInterpolator
  {
    private Workspace.ZInterpolator zInterpolator;
    
    public InverseZInterpolator(float paramFloat)
    {
      this.zInterpolator = new Workspace.ZInterpolator(paramFloat);
    }
    
    public float getInterpolation(float paramFloat)
    {
      return 1.0F - this.zInterpolator.getInterpolation(1.0F - paramFloat);
    }
  }
  
  class ReorderAlarmListener
    implements OnAlarmListener
  {
    View child;
    DragView dragView;
    float[] dragViewCenter;
    int minSpanX;
    int minSpanY;
    int spanX;
    int spanY;
    
    public ReorderAlarmListener(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, View paramView)
    {
      this.dragViewCenter = paramArrayOfFloat;
      this.minSpanX = paramInt1;
      this.minSpanY = paramInt2;
      this.spanX = paramInt3;
      this.spanY = paramInt4;
      this.child = paramView;
      this.dragView = paramDragView;
    }
    
    public void onAlarm(Alarm paramAlarm)
    {
      int[] arrayOfInt = new int[2];
      Workspace.access$1302(Workspace.this, Workspace.this.findNearestArea((int)Workspace.this.mDragViewVisualCenter[0], (int)Workspace.this.mDragViewVisualCenter[1], this.minSpanX, this.minSpanY, Workspace.this.mDragTargetLayout, Workspace.this.mTargetCell));
      Workspace.access$1702(Workspace.this, Workspace.this.mTargetCell[0]);
      Workspace.access$1802(Workspace.this, Workspace.this.mTargetCell[1]);
      Workspace.access$1302(Workspace.this, Workspace.this.mDragTargetLayout.createArea((int)Workspace.this.mDragViewVisualCenter[0], (int)Workspace.this.mDragViewVisualCenter[1], this.minSpanX, this.minSpanY, this.spanX, this.spanY, this.child, Workspace.this.mTargetCell, arrayOfInt, 0));
      if ((Workspace.this.mTargetCell[0] < 0) || (Workspace.this.mTargetCell[1] < 0))
      {
        Workspace.this.mDragTargetLayout.revertTempState();
        if ((arrayOfInt[0] == this.spanX) && (arrayOfInt[1] == this.spanY)) {
          break label312;
        }
      }
      label312:
      for (boolean bool = true;; bool = false)
      {
        Workspace.this.mDragTargetLayout.visualizeDropLocation(this.child, Workspace.this.mDragOutline, (int)Workspace.this.mDragViewVisualCenter[0], (int)Workspace.this.mDragViewVisualCenter[1], Workspace.this.mTargetCell[0], Workspace.this.mTargetCell[1], arrayOfInt[0], arrayOfInt[1], bool, this.dragView.getDragVisualizeOffset(), this.dragView.getDragRegion());
        return;
        Workspace.this.setDragMode(3);
        break;
      }
    }
  }
  
  static enum State
  {
    static
    {
      SMALL = new State("SMALL", 2);
      OVERVIEW = new State("OVERVIEW", 3);
      State[] arrayOfState = new State[4];
      arrayOfState[0] = NORMAL;
      arrayOfState[1] = SPRING_LOADED;
      arrayOfState[2] = SMALL;
      arrayOfState[3] = OVERVIEW;
      $VALUES = arrayOfState;
    }
    
    private State() {}
  }
  
  class WallpaperOffsetInterpolator
    implements Choreographer.FrameCallback
  {
    private final int ANIMATION_DURATION = 250;
    private final int MIN_PARALLAX_PAGE_SPAN = 3;
    boolean mAnimating;
    float mAnimationStartOffset;
    long mAnimationStartTime;
    Choreographer mChoreographer = Choreographer.getInstance();
    float mCurrentOffset = 0.5F;
    float mFinalOffset = 0.0F;
    Interpolator mInterpolator = new DecelerateInterpolator(1.5F);
    int mNumScreens;
    boolean mWaitingForUpdate;
    
    public WallpaperOffsetInterpolator() {}
    
    private void animateToFinal()
    {
      this.mAnimating = true;
      this.mAnimationStartOffset = this.mCurrentOffset;
      this.mAnimationStartTime = System.currentTimeMillis();
    }
    
    private int getNumScreensExcludingEmptyAndCustom()
    {
      return Workspace.this.getChildCount() - numEmptyScreensToIgnore() - Workspace.this.numCustomPages();
    }
    
    private int numEmptyScreensToIgnore()
    {
      if ((Workspace.this.getChildCount() - Workspace.this.numCustomPages() >= 3) && (Workspace.this.hasExtraEmptyScreen())) {
        return 1;
      }
      return 0;
    }
    
    private void scheduleUpdate()
    {
      if (!this.mWaitingForUpdate)
      {
        this.mChoreographer.postFrameCallback(this);
        this.mWaitingForUpdate = true;
      }
    }
    
    private void setWallpaperOffsetSteps()
    {
      Workspace.this.mWallpaperManager.setWallpaperOffsetSteps(1.0F / (-1 + Workspace.this.getChildCount()), 1.0F);
    }
    
    private void updateOffset(boolean paramBoolean)
    {
      if ((this.mWaitingForUpdate) || (paramBoolean))
      {
        this.mWaitingForUpdate = false;
        if ((!computeScrollOffset()) || (Workspace.this.mWindowToken == null)) {}
      }
      try
      {
        Workspace.this.mWallpaperManager.setWallpaperOffsets(Workspace.this.mWindowToken, Workspace.this.mWallpaperOffset.getCurrX(), 0.5F);
        setWallpaperOffsetSteps();
        return;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e("Launcher.Workspace", "Error updating wallpaper offset: " + localIllegalArgumentException);
      }
    }
    
    private float wallpaperOffsetForCurrentScroll()
    {
      if (Workspace.this.getChildCount() <= 1) {}
      int m;
      int n;
      do
      {
        return 0.0F;
        int i = numEmptyScreensToIgnore();
        int j = Workspace.this.numCustomPages();
        int k = -1 + Workspace.this.getChildCount() - i;
        if (Workspace.this.isLayoutRtl())
        {
          int i4 = j;
          j = k;
          k = i4;
        }
        m = Workspace.this.getScrollForPage(j);
        n = Workspace.this.getScrollForPage(k) - m;
      } while (n == 0);
      float f = Math.max(0.0F, Math.min(1.0F, (Workspace.this.getScrollX() - m - Workspace.this.getLayoutTransitionOffsetForPage(0)) / n));
      int i1 = getNumScreensExcludingEmptyAndCustom();
      int i2 = Math.max(3, i1 - 1);
      boolean bool = Workspace.this.isLayoutRtl();
      int i3 = 0;
      if (bool) {
        i3 = 1 + (i2 - i1);
      }
      return f * (-1 + (i3 + i1)) / i2;
    }
    
    public boolean computeScrollOffset()
    {
      float f1 = this.mCurrentOffset;
      boolean bool;
      if (this.mAnimating)
      {
        long l = System.currentTimeMillis() - this.mAnimationStartTime;
        float f2 = (float)l / 250.0F;
        float f3 = this.mInterpolator.getInterpolation(f2);
        this.mCurrentOffset = (this.mAnimationStartOffset + f3 * (this.mFinalOffset - this.mAnimationStartOffset));
        if (l < 250L)
        {
          bool = true;
          this.mAnimating = bool;
        }
      }
      for (;;)
      {
        if (Math.abs(this.mCurrentOffset - this.mFinalOffset) > 1.0E-007F) {
          scheduleUpdate();
        }
        if (Math.abs(f1 - this.mCurrentOffset) <= 1.0E-007F) {
          break label135;
        }
        return true;
        bool = false;
        break;
        this.mCurrentOffset = this.mFinalOffset;
      }
      label135:
      return false;
    }
    
    public void doFrame(long paramLong)
    {
      updateOffset(false);
    }
    
    public float getCurrX()
    {
      return this.mCurrentOffset;
    }
    
    public void jumpToFinal()
    {
      this.mCurrentOffset = this.mFinalOffset;
    }
    
    public void setFinalX(float paramFloat)
    {
      scheduleUpdate();
      this.mFinalOffset = Math.max(0.0F, Math.min(paramFloat, 1.0F));
      if (getNumScreensExcludingEmptyAndCustom() != this.mNumScreens)
      {
        if (this.mNumScreens > 0) {
          animateToFinal();
        }
        this.mNumScreens = getNumScreensExcludingEmptyAndCustom();
      }
    }
    
    public void syncWithScroll()
    {
      float f = wallpaperOffsetForCurrentScroll();
      Workspace.this.mWallpaperOffset.setFinalX(f);
      updateOffset(true);
    }
  }
  
  static class ZInterpolator
    implements TimeInterpolator
  {
    private float focalLength;
    
    public ZInterpolator(float paramFloat)
    {
      this.focalLength = paramFloat;
    }
    
    public float getInterpolation(float paramFloat)
    {
      return (1.0F - this.focalLength / (paramFloat + this.focalLength)) / (1.0F - this.focalLength / (1.0F + this.focalLength));
    }
  }
  
  static class ZoomInInterpolator
    implements TimeInterpolator
  {
    private final DecelerateInterpolator decelerate = new DecelerateInterpolator(3.0F);
    private final Workspace.InverseZInterpolator inverseZInterpolator = new Workspace.InverseZInterpolator(0.35F);
    
    public float getInterpolation(float paramFloat)
    {
      return this.decelerate.getInterpolation(this.inverseZInterpolator.getInterpolation(paramFloat));
    }
  }
  
  static class ZoomOutInterpolator
    implements TimeInterpolator
  {
    private final DecelerateInterpolator decelerate = new DecelerateInterpolator(0.75F);
    private final Workspace.ZInterpolator zInterpolator = new Workspace.ZInterpolator(0.13F);
    
    public float getInterpolation(float paramFloat)
    {
      return this.decelerate.getInterpolation(this.zInterpolator.getInterpolation(paramFloat));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Workspace
 * JD-Core Version:    0.7.0.1
 */