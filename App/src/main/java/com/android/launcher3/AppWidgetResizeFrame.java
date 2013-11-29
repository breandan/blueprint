package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class AppWidgetResizeFrame
  extends FrameLayout
{
  private static Rect mTmpRect = new Rect();
  final int BACKGROUND_PADDING = 24;
  final float DIMMED_HANDLE_ALPHA = 0.0F;
  final float RESIZE_THRESHOLD = 0.66F;
  final int SNAP_DURATION = 150;
  private int mBackgroundPadding;
  private int mBaselineHeight;
  private int mBaselineWidth;
  private int mBaselineX;
  private int mBaselineY;
  private boolean mBottomBorderActive;
  private ImageView mBottomHandle;
  private int mBottomTouchRegionAdjustment = 0;
  private CellLayout mCellLayout;
  private int mDeltaX;
  private int mDeltaXAddOn;
  private int mDeltaY;
  private int mDeltaYAddOn;
  int[] mDirectionVector = new int[2];
  private DragLayer mDragLayer;
  int[] mLastDirectionVector = new int[2];
  private Launcher mLauncher;
  private boolean mLeftBorderActive;
  private ImageView mLeftHandle;
  private int mMinHSpan;
  private int mMinVSpan;
  private int mResizeMode;
  private boolean mRightBorderActive;
  private ImageView mRightHandle;
  private int mRunningHInc;
  private int mRunningVInc;
  int[] mTmpPt = new int[2];
  private boolean mTopBorderActive;
  private ImageView mTopHandle;
  private int mTopTouchRegionAdjustment = 0;
  private int mTouchTargetWidth;
  private int mWidgetPaddingBottom;
  private int mWidgetPaddingLeft;
  private int mWidgetPaddingRight;
  private int mWidgetPaddingTop;
  private LauncherAppWidgetHostView mWidgetView;
  
  public AppWidgetResizeFrame(Context paramContext, LauncherAppWidgetHostView paramLauncherAppWidgetHostView, CellLayout paramCellLayout, DragLayer paramDragLayer)
  {
    super(paramContext);
    this.mLauncher = ((Launcher)paramContext);
    this.mCellLayout = paramCellLayout;
    this.mWidgetView = paramLauncherAppWidgetHostView;
    this.mResizeMode = paramLauncherAppWidgetHostView.getAppWidgetInfo().resizeMode;
    this.mDragLayer = paramDragLayer;
    AppWidgetProviderInfo localAppWidgetProviderInfo = paramLauncherAppWidgetHostView.getAppWidgetInfo();
    int[] arrayOfInt = Launcher.getMinSpanForWidget(this.mLauncher, localAppWidgetProviderInfo);
    this.mMinHSpan = arrayOfInt[0];
    this.mMinVSpan = arrayOfInt[1];
    setBackgroundResource(2130838124);
    setPadding(0, 0, 0, 0);
    this.mLeftHandle = new ImageView(paramContext);
    this.mLeftHandle.setImageResource(2130838126);
    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-2, -2, 19);
    addView(this.mLeftHandle, localLayoutParams1);
    this.mRightHandle = new ImageView(paramContext);
    this.mRightHandle.setImageResource(2130838127);
    FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-2, -2, 21);
    addView(this.mRightHandle, localLayoutParams2);
    this.mTopHandle = new ImageView(paramContext);
    this.mTopHandle.setImageResource(2130838128);
    FrameLayout.LayoutParams localLayoutParams3 = new FrameLayout.LayoutParams(-2, -2, 49);
    addView(this.mTopHandle, localLayoutParams3);
    this.mBottomHandle = new ImageView(paramContext);
    this.mBottomHandle.setImageResource(2130838125);
    FrameLayout.LayoutParams localLayoutParams4 = new FrameLayout.LayoutParams(-2, -2, 81);
    addView(this.mBottomHandle, localLayoutParams4);
    Rect localRect = AppWidgetHostView.getDefaultPaddingForWidget(paramContext, paramLauncherAppWidgetHostView.getAppWidgetInfo().provider, null);
    this.mWidgetPaddingLeft = localRect.left;
    this.mWidgetPaddingTop = localRect.top;
    this.mWidgetPaddingRight = localRect.right;
    this.mWidgetPaddingBottom = localRect.bottom;
    if (this.mResizeMode == 1)
    {
      this.mTopHandle.setVisibility(8);
      this.mBottomHandle.setVisibility(8);
    }
    for (;;)
    {
      this.mBackgroundPadding = ((int)Math.ceil(24.0F * this.mLauncher.getResources().getDisplayMetrics().density));
      this.mTouchTargetWidth = (2 * this.mBackgroundPadding);
      this.mCellLayout.markCellsAsUnoccupiedForView(this.mWidgetView);
      return;
      if (this.mResizeMode == 2)
      {
        this.mLeftHandle.setVisibility(8);
        this.mRightHandle.setVisibility(8);
      }
    }
  }
  
  static Rect getWidgetSizeRanges(Launcher paramLauncher, int paramInt1, int paramInt2, Rect paramRect)
  {
    if (paramRect == null) {
      paramRect = new Rect();
    }
    Rect localRect1 = Workspace.getCellLayoutMetrics(paramLauncher, 0);
    Rect localRect2 = Workspace.getCellLayoutMetrics(paramLauncher, 1);
    float f = paramLauncher.getResources().getDisplayMetrics().density;
    int i = localRect1.left;
    int j = localRect1.top;
    int k = localRect1.right;
    int m = localRect1.bottom;
    int n = (int)((paramInt1 * i + k * (paramInt1 - 1)) / f);
    int i1 = (int)((paramInt2 * j + m * (paramInt2 - 1)) / f);
    int i2 = localRect2.left;
    int i3 = localRect2.top;
    int i4 = localRect2.right;
    int i5 = localRect2.bottom;
    int i6 = (int)((paramInt1 * i2 + i4 * (paramInt1 - 1)) / f);
    int i7 = (int)((paramInt2 * i3 + i5 * (paramInt2 - 1)) / f);
    paramRect.set(i6, i1, n, i7);
    return paramRect;
  }
  
  private void resizeWidgetIfNeeded(boolean paramBoolean)
  {
    int i = this.mCellLayout.getCellWidth() + this.mCellLayout.getWidthGap();
    int j = this.mCellLayout.getCellHeight() + this.mCellLayout.getHeightGap();
    int k = this.mDeltaX + this.mDeltaXAddOn;
    int m = this.mDeltaY + this.mDeltaYAddOn;
    float f1 = 1.0F * k / i - this.mRunningHInc;
    float f2 = 1.0F * m / j - this.mRunningVInc;
    int n = this.mCellLayout.getCountX();
    int i1 = this.mCellLayout.getCountY();
    boolean bool1 = Math.abs(f1) < 0.66F;
    int i2 = 0;
    if (bool1) {
      i2 = Math.round(f1);
    }
    boolean bool2 = Math.abs(f2) < 0.66F;
    int i3 = 0;
    if (bool2) {
      i3 = Math.round(f2);
    }
    if ((!paramBoolean) && (i2 == 0) && (i3 == 0)) {}
    CellLayout.LayoutParams localLayoutParams;
    int i4;
    int i5;
    int i6;
    int i7;
    label225:
    int i8;
    int i9;
    label293:
    int i11;
    int i12;
    label361:
    int i15;
    label424:
    int i14;
    label479:
    do
    {
      return;
      localLayoutParams = (CellLayout.LayoutParams)this.mWidgetView.getLayoutParams();
      i4 = localLayoutParams.cellHSpan;
      i5 = localLayoutParams.cellVSpan;
      if (!localLayoutParams.useTmpCoords) {
        break;
      }
      i6 = localLayoutParams.tmpCellX;
      if (!localLayoutParams.useTmpCoords) {
        break label641;
      }
      i7 = localLayoutParams.tmpCellY;
      if (!this.mLeftBorderActive) {
        break label651;
      }
      int i18 = Math.max(-i6, i2);
      i8 = Math.min(localLayoutParams.cellHSpan - this.mMinHSpan, i18);
      int i19 = Math.min(i6, i2 * -1);
      i2 = Math.max(-(localLayoutParams.cellHSpan - this.mMinHSpan), i19);
      i9 = -i2;
      if (!this.mTopBorderActive) {
        break label711;
      }
      int i16 = Math.max(-i7, i3);
      i11 = Math.min(localLayoutParams.cellVSpan - this.mMinVSpan, i16);
      int i17 = Math.min(i7, i3 * -1);
      i3 = Math.max(-(localLayoutParams.cellVSpan - this.mMinVSpan), i17);
      i12 = -i3;
      this.mDirectionVector[0] = 0;
      this.mDirectionVector[1] = 0;
      if ((this.mLeftBorderActive) || (this.mRightBorderActive))
      {
        i4 += i2;
        i6 += i8;
        if (i9 != 0)
        {
          int[] arrayOfInt2 = this.mDirectionVector;
          if (!this.mLeftBorderActive) {
            break label771;
          }
          i15 = -1;
          arrayOfInt2[0] = i15;
        }
      }
      if ((this.mTopBorderActive) || (this.mBottomBorderActive))
      {
        i5 += i3;
        i7 += i11;
        if (i12 != 0)
        {
          int[] arrayOfInt1 = this.mDirectionVector;
          if (!this.mTopBorderActive) {
            break label777;
          }
          i14 = -1;
          arrayOfInt1[1] = i14;
        }
      }
    } while ((!paramBoolean) && (i12 == 0) && (i9 == 0));
    if (paramBoolean)
    {
      this.mDirectionVector[0] = this.mLastDirectionVector[0];
      this.mDirectionVector[1] = this.mLastDirectionVector[1];
    }
    for (;;)
    {
      if (this.mCellLayout.createAreaForResize(i6, i7, i4, i5, this.mWidgetView, this.mDirectionVector, paramBoolean))
      {
        localLayoutParams.tmpCellX = i6;
        localLayoutParams.tmpCellY = i7;
        localLayoutParams.cellHSpan = i4;
        localLayoutParams.cellVSpan = i5;
        this.mRunningVInc = (i12 + this.mRunningVInc);
        this.mRunningHInc = (i9 + this.mRunningHInc);
        if (!paramBoolean) {
          updateWidgetSizeRanges(this.mWidgetView, this.mLauncher, i4, i5);
        }
      }
      this.mWidgetView.requestLayout();
      return;
      i6 = localLayoutParams.cellX;
      break;
      label641:
      i7 = localLayoutParams.cellY;
      break label225;
      label651:
      boolean bool3 = this.mRightBorderActive;
      i8 = 0;
      i9 = 0;
      if (!bool3) {
        break label293;
      }
      int i10 = Math.min(n - (i6 + i4), i2);
      i2 = Math.max(-(localLayoutParams.cellHSpan - this.mMinHSpan), i10);
      i9 = i2;
      i8 = 0;
      break label293;
      label711:
      boolean bool4 = this.mBottomBorderActive;
      i11 = 0;
      i12 = 0;
      if (!bool4) {
        break label361;
      }
      int i13 = Math.min(i1 - (i7 + i5), i3);
      i3 = Math.max(-(localLayoutParams.cellVSpan - this.mMinVSpan), i13);
      i12 = i3;
      i11 = 0;
      break label361;
      label771:
      i15 = 1;
      break label424;
      label777:
      i14 = 1;
      break label479;
      this.mLastDirectionVector[0] = this.mDirectionVector[0];
      this.mLastDirectionVector[1] = this.mDirectionVector[1];
    }
  }
  
  static void updateWidgetSizeRanges(AppWidgetHostView paramAppWidgetHostView, Launcher paramLauncher, int paramInt1, int paramInt2)
  {
    getWidgetSizeRanges(paramLauncher, paramInt1, paramInt2, mTmpRect);
    paramAppWidgetHostView.updateAppWidgetSize(null, mTmpRect.left, mTmpRect.top, mTmpRect.right, mTmpRect.bottom);
  }
  
  private void visualizeResizeForDelta(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    updateDeltas(paramInt1, paramInt2);
    DragLayer.LayoutParams localLayoutParams = (DragLayer.LayoutParams)getLayoutParams();
    if (this.mLeftBorderActive)
    {
      localLayoutParams.x = (this.mBaselineX + this.mDeltaX);
      localLayoutParams.width = (this.mBaselineWidth - this.mDeltaX);
      if (!this.mTopBorderActive) {
        break label119;
      }
      localLayoutParams.y = (this.mBaselineY + this.mDeltaY);
      localLayoutParams.height = (this.mBaselineHeight - this.mDeltaY);
    }
    for (;;)
    {
      resizeWidgetIfNeeded(paramBoolean);
      requestLayout();
      return;
      if (!this.mRightBorderActive) {
        break;
      }
      localLayoutParams.width = (this.mBaselineWidth + this.mDeltaX);
      break;
      label119:
      if (this.mBottomBorderActive) {
        localLayoutParams.height = (this.mBaselineHeight + this.mDeltaY);
      }
    }
  }
  
  public boolean beginResizeIfPointInRegion(int paramInt1, int paramInt2)
  {
    float f1 = 1.0F;
    int i;
    int j;
    label26:
    boolean bool1;
    label42:
    boolean bool2;
    label69:
    boolean bool3;
    label96:
    boolean bool4;
    label128:
    boolean bool5;
    label165:
    float f2;
    label218:
    float f3;
    label241:
    float f4;
    label264:
    ImageView localImageView4;
    if ((0x1 & this.mResizeMode) != 0)
    {
      i = 1;
      if ((0x2 & this.mResizeMode) == 0) {
        break label299;
      }
      j = 1;
      if ((paramInt1 >= this.mTouchTargetWidth) || (i == 0)) {
        break label305;
      }
      bool1 = true;
      this.mLeftBorderActive = bool1;
      if ((paramInt1 <= getWidth() - this.mTouchTargetWidth) || (i == 0)) {
        break label311;
      }
      bool2 = true;
      this.mRightBorderActive = bool2;
      if ((paramInt2 >= this.mTouchTargetWidth + this.mTopTouchRegionAdjustment) || (j == 0)) {
        break label317;
      }
      bool3 = true;
      this.mTopBorderActive = bool3;
      if ((paramInt2 <= getHeight() - this.mTouchTargetWidth + this.mBottomTouchRegionAdjustment) || (j == 0)) {
        break label323;
      }
      bool4 = true;
      this.mBottomBorderActive = bool4;
      if ((!this.mLeftBorderActive) && (!this.mRightBorderActive) && (!this.mTopBorderActive) && (!this.mBottomBorderActive)) {
        break label329;
      }
      bool5 = true;
      this.mBaselineWidth = getMeasuredWidth();
      this.mBaselineHeight = getMeasuredHeight();
      this.mBaselineX = getLeft();
      this.mBaselineY = getTop();
      if (bool5)
      {
        ImageView localImageView1 = this.mLeftHandle;
        if (!this.mLeftBorderActive) {
          break label335;
        }
        f2 = f1;
        localImageView1.setAlpha(f2);
        ImageView localImageView2 = this.mRightHandle;
        if (!this.mRightBorderActive) {
          break label341;
        }
        f3 = f1;
        localImageView2.setAlpha(f3);
        ImageView localImageView3 = this.mTopHandle;
        if (!this.mTopBorderActive) {
          break label347;
        }
        f4 = f1;
        localImageView3.setAlpha(f4);
        localImageView4 = this.mBottomHandle;
        if (!this.mBottomBorderActive) {
          break label353;
        }
      }
    }
    for (;;)
    {
      localImageView4.setAlpha(f1);
      return bool5;
      i = 0;
      break;
      label299:
      j = 0;
      break label26;
      label305:
      bool1 = false;
      break label42;
      label311:
      bool2 = false;
      break label69;
      label317:
      bool3 = false;
      break label96;
      label323:
      bool4 = false;
      break label128;
      label329:
      bool5 = false;
      break label165;
      label335:
      f2 = 0.0F;
      break label218;
      label341:
      f3 = 0.0F;
      break label241;
      label347:
      f4 = 0.0F;
      break label264;
      label353:
      f1 = 0.0F;
    }
  }
  
  public void commitResize()
  {
    resizeWidgetIfNeeded(true);
    requestLayout();
  }
  
  public void onTouchUp()
  {
    int i = this.mCellLayout.getCellWidth() + this.mCellLayout.getWidthGap();
    int j = this.mCellLayout.getCellHeight() + this.mCellLayout.getHeightGap();
    this.mDeltaXAddOn = (i * this.mRunningHInc);
    this.mDeltaYAddOn = (j * this.mRunningVInc);
    this.mDeltaX = 0;
    this.mDeltaY = 0;
    post(new Runnable()
    {
      public void run()
      {
        AppWidgetResizeFrame.this.snapToWidget(true);
      }
    });
  }
  
  public void snapToWidget(boolean paramBoolean)
  {
    DragLayer.LayoutParams localLayoutParams = (DragLayer.LayoutParams)getLayoutParams();
    int i = this.mWidgetView.getWidth() + 2 * this.mBackgroundPadding - this.mWidgetPaddingLeft - this.mWidgetPaddingRight;
    int j = this.mWidgetView.getHeight() + 2 * this.mBackgroundPadding - this.mWidgetPaddingTop - this.mWidgetPaddingBottom;
    this.mTmpPt[0] = this.mWidgetView.getLeft();
    this.mTmpPt[1] = this.mWidgetView.getTop();
    this.mDragLayer.getDescendantCoordRelativeToSelf(this.mCellLayout.getShortcutsAndWidgets(), this.mTmpPt);
    int k = this.mTmpPt[0] - this.mBackgroundPadding + this.mWidgetPaddingLeft;
    int m = this.mTmpPt[1] - this.mBackgroundPadding + this.mWidgetPaddingTop;
    if (m < 0)
    {
      this.mTopTouchRegionAdjustment = (-m);
      if (m + j <= this.mDragLayer.getHeight()) {
        break label257;
      }
    }
    label257:
    for (this.mBottomTouchRegionAdjustment = (-(m + j - this.mDragLayer.getHeight()));; this.mBottomTouchRegionAdjustment = 0)
    {
      if (paramBoolean) {
        break label265;
      }
      localLayoutParams.width = i;
      localLayoutParams.height = j;
      localLayoutParams.x = k;
      localLayoutParams.y = m;
      this.mLeftHandle.setAlpha(1.0F);
      this.mRightHandle.setAlpha(1.0F);
      this.mTopHandle.setAlpha(1.0F);
      this.mBottomHandle.setAlpha(1.0F);
      requestLayout();
      return;
      this.mTopTouchRegionAdjustment = 0;
      break;
    }
    label265:
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[0] = localLayoutParams.width;
    arrayOfInt1[1] = i;
    PropertyValuesHolder localPropertyValuesHolder1 = PropertyValuesHolder.ofInt("width", arrayOfInt1);
    int[] arrayOfInt2 = new int[2];
    arrayOfInt2[0] = localLayoutParams.height;
    arrayOfInt2[1] = j;
    PropertyValuesHolder localPropertyValuesHolder2 = PropertyValuesHolder.ofInt("height", arrayOfInt2);
    int[] arrayOfInt3 = new int[2];
    arrayOfInt3[0] = localLayoutParams.x;
    arrayOfInt3[1] = k;
    PropertyValuesHolder localPropertyValuesHolder3 = PropertyValuesHolder.ofInt("x", arrayOfInt3);
    int[] arrayOfInt4 = new int[2];
    arrayOfInt4[0] = localLayoutParams.y;
    arrayOfInt4[1] = m;
    ObjectAnimator localObjectAnimator1 = LauncherAnimUtils.ofPropertyValuesHolder(localLayoutParams, this, new PropertyValuesHolder[] { localPropertyValuesHolder1, localPropertyValuesHolder2, localPropertyValuesHolder3, PropertyValuesHolder.ofInt("y", arrayOfInt4) });
    ObjectAnimator localObjectAnimator2 = LauncherAnimUtils.ofFloat(this.mLeftHandle, "alpha", new float[] { 1.0F });
    ObjectAnimator localObjectAnimator3 = LauncherAnimUtils.ofFloat(this.mRightHandle, "alpha", new float[] { 1.0F });
    ObjectAnimator localObjectAnimator4 = LauncherAnimUtils.ofFloat(this.mTopHandle, "alpha", new float[] { 1.0F });
    ObjectAnimator localObjectAnimator5 = LauncherAnimUtils.ofFloat(this.mBottomHandle, "alpha", new float[] { 1.0F });
    ValueAnimator.AnimatorUpdateListener local2 = new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        AppWidgetResizeFrame.this.requestLayout();
      }
    };
    localObjectAnimator1.addUpdateListener(local2);
    AnimatorSet localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
    if (this.mResizeMode == 2) {
      localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator4, localObjectAnimator5 });
    }
    for (;;)
    {
      localAnimatorSet.setDuration(150L);
      localAnimatorSet.start();
      return;
      if (this.mResizeMode == 1) {
        localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2, localObjectAnimator3 });
      } else {
        localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2, localObjectAnimator3, localObjectAnimator4, localObjectAnimator5 });
      }
    }
  }
  
  public void updateDeltas(int paramInt1, int paramInt2)
  {
    if (this.mLeftBorderActive)
    {
      this.mDeltaX = Math.max(-this.mBaselineX, paramInt1);
      this.mDeltaX = Math.min(this.mBaselineWidth - 2 * this.mTouchTargetWidth, this.mDeltaX);
    }
    do
    {
      while (this.mTopBorderActive)
      {
        this.mDeltaY = Math.max(-this.mBaselineY, paramInt2);
        this.mDeltaY = Math.min(this.mBaselineHeight - 2 * this.mTouchTargetWidth, this.mDeltaY);
        return;
        if (this.mRightBorderActive)
        {
          this.mDeltaX = Math.min(this.mDragLayer.getWidth() - (this.mBaselineX + this.mBaselineWidth), paramInt1);
          this.mDeltaX = Math.max(-this.mBaselineWidth + 2 * this.mTouchTargetWidth, this.mDeltaX);
        }
      }
    } while (!this.mBottomBorderActive);
    this.mDeltaY = Math.min(this.mDragLayer.getHeight() - (this.mBaselineY + this.mBaselineHeight), paramInt2);
    this.mDeltaY = Math.max(-this.mBaselineHeight + 2 * this.mTouchTargetWidth, this.mDeltaY);
  }
  
  public void visualizeResizeForDelta(int paramInt1, int paramInt2)
  {
    visualizeResizeForDelta(paramInt1, paramInt2, false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppWidgetResizeFrame
 * JD-Core Version:    0.7.0.1
 */