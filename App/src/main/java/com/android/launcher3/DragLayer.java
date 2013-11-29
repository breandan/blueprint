package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DragLayer
  extends FrameLayout
  implements ViewGroup.OnHierarchyChangeListener
{
  private View mAnchorView = null;
  private int mAnchorViewInitialScrollX = 0;
  private TimeInterpolator mCubicEaseOutInterpolator = new DecelerateInterpolator(1.5F);
  private AppWidgetResizeFrame mCurrentResizeFrame;
  private DragController mDragController;
  private ValueAnimator mDropAnim = null;
  private DragView mDropView = null;
  private ValueAnimator mFadeOutAnim = null;
  private Rect mHitRect = new Rect();
  private boolean mHoverPointClosesFolder = false;
  private boolean mInScrollArea;
  private final Rect mInsets = new Rect();
  private Launcher mLauncher;
  private Drawable mLeftHoverDrawable;
  private int mQsbIndex = -1;
  private final ArrayList<AppWidgetResizeFrame> mResizeFrames = new ArrayList();
  private Drawable mRightHoverDrawable;
  private int[] mTmpXY = new int[2];
  private TouchCompleteListener mTouchCompleteListener;
  private int mWorkspaceIndex = -1;
  private int mXDown;
  private int mYDown;
  
  public DragLayer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setMotionEventSplittingEnabled(false);
    setChildrenDrawingOrderEnabled(true);
    setOnHierarchyChangeListener(this);
    this.mLeftHoverDrawable = getResources().getDrawable(2130838010);
    this.mRightHoverDrawable = getResources().getDrawable(2130838011);
  }
  
  private void fadeOutDragView()
  {
    this.mFadeOutAnim = new ValueAnimator();
    this.mFadeOutAnim.setDuration(150L);
    this.mFadeOutAnim.setFloatValues(new float[] { 0.0F, 1.0F });
    this.mFadeOutAnim.removeAllUpdateListeners();
    this.mFadeOutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        float f = 1.0F - ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        DragLayer.this.mDropView.setAlpha(f);
      }
    });
    this.mFadeOutAnim.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (DragLayer.this.mDropView != null) {
          DragLayer.this.mDragController.onDeferredEndDrag(DragLayer.this.mDropView);
        }
        DragLayer.access$202(DragLayer.this, null);
        DragLayer.this.invalidate();
      }
    });
    this.mFadeOutAnim.start();
  }
  
  private boolean handleTouchDown(MotionEvent paramMotionEvent, boolean paramBoolean)
  {
    Rect localRect = new Rect();
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    Iterator localIterator = this.mResizeFrames.iterator();
    while (localIterator.hasNext())
    {
      AppWidgetResizeFrame localAppWidgetResizeFrame = (AppWidgetResizeFrame)localIterator.next();
      localAppWidgetResizeFrame.getHitRect(localRect);
      if ((localRect.contains(i, j)) && (localAppWidgetResizeFrame.beginResizeIfPointInRegion(i - localAppWidgetResizeFrame.getLeft(), j - localAppWidgetResizeFrame.getTop())))
      {
        this.mCurrentResizeFrame = localAppWidgetResizeFrame;
        this.mXDown = i;
        this.mYDown = j;
        requestDisallowInterceptTouchEvent(true);
        return true;
      }
    }
    Folder localFolder = this.mLauncher.getWorkspace().getOpenFolder();
    if ((localFolder != null) && (!this.mLauncher.isFolderClingVisible()) && (paramBoolean))
    {
      if ((localFolder.isEditingName()) && (!isEventOverFolderTextRegion(localFolder, paramMotionEvent)))
      {
        localFolder.dismissEditingName();
        return true;
      }
      getDescendantRectRelativeToSelf(localFolder, localRect);
      if (!isEventOverFolder(localFolder, paramMotionEvent))
      {
        this.mLauncher.closeFolder();
        return true;
      }
    }
    return false;
  }
  
  private boolean isEventOverFolder(Folder paramFolder, MotionEvent paramMotionEvent)
  {
    getDescendantRectRelativeToSelf(paramFolder, this.mHitRect);
    return this.mHitRect.contains((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
  }
  
  private boolean isEventOverFolderTextRegion(Folder paramFolder, MotionEvent paramMotionEvent)
  {
    getDescendantRectRelativeToSelf(paramFolder.getEditTextRegion(), this.mHitRect);
    return this.mHitRect.contains((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
  }
  
  private boolean isLayoutRtl()
  {
    return getLayoutDirection() == 1;
  }
  
  private void sendTapOutsideFolderAccessibilityEvent(boolean paramBoolean)
  {
    AccessibilityManager localAccessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
    if (localAccessibilityManager.isEnabled()) {
      if (!paramBoolean) {
        break label68;
      }
    }
    label68:
    for (int i = 2131361948;; i = 2131361947)
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(8);
      onInitializeAccessibilityEvent(localAccessibilityEvent);
      localAccessibilityEvent.getText().add(getContext().getString(i));
      localAccessibilityManager.sendAccessibilityEvent(localAccessibilityEvent);
      return;
    }
  }
  
  private void updateChildIndices()
  {
    if (this.mLauncher != null)
    {
      this.mWorkspaceIndex = indexOfChild(this.mLauncher.getWorkspace());
      this.mQsbIndex = indexOfChild(this.mLauncher.getSearchBar());
    }
  }
  
  public void addChildrenForAccessibility(ArrayList<View> paramArrayList)
  {
    Folder localFolder = this.mLauncher.getWorkspace().getOpenFolder();
    if (localFolder != null)
    {
      paramArrayList.add(localFolder);
      return;
    }
    super.addChildrenForAccessibility(paramArrayList);
  }
  
  public void addResizeFrame(ItemInfo paramItemInfo, LauncherAppWidgetHostView paramLauncherAppWidgetHostView, CellLayout paramCellLayout)
  {
    AppWidgetResizeFrame localAppWidgetResizeFrame = new AppWidgetResizeFrame(getContext(), paramLauncherAppWidgetHostView, paramCellLayout, this);
    LayoutParams localLayoutParams = new LayoutParams(-1, -1);
    localLayoutParams.customPosition = true;
    addView(localAppWidgetResizeFrame, localLayoutParams);
    this.mResizeFrames.add(localAppWidgetResizeFrame);
    localAppWidgetResizeFrame.snapToWidget(false);
  }
  
  public void animateView(DragView paramDragView, ValueAnimator.AnimatorUpdateListener paramAnimatorUpdateListener, int paramInt1, TimeInterpolator paramTimeInterpolator, final Runnable paramRunnable, final int paramInt2, View paramView)
  {
    if (this.mDropAnim != null) {
      this.mDropAnim.cancel();
    }
    if (this.mFadeOutAnim != null) {
      this.mFadeOutAnim.cancel();
    }
    this.mDropView = paramDragView;
    this.mDropView.cancelAnimation();
    this.mDropView.resetLayoutParams();
    if (paramView != null) {
      this.mAnchorViewInitialScrollX = paramView.getScrollX();
    }
    this.mAnchorView = paramView;
    this.mDropAnim = new ValueAnimator();
    this.mDropAnim.setInterpolator(paramTimeInterpolator);
    this.mDropAnim.setDuration(paramInt1);
    this.mDropAnim.setFloatValues(new float[] { 0.0F, 1.0F });
    this.mDropAnim.addUpdateListener(paramAnimatorUpdateListener);
    this.mDropAnim.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (paramRunnable != null) {
          paramRunnable.run();
        }
        switch (paramInt2)
        {
        default: 
          return;
        case 0: 
          DragLayer.this.clearAnimatedView();
          return;
        }
        DragLayer.this.fadeOutDragView();
      }
    });
    this.mDropAnim.start();
  }
  
  public void animateView(final DragView paramDragView, final Rect paramRect1, final Rect paramRect2, final float paramFloat1, final float paramFloat2, final float paramFloat3, final float paramFloat4, final float paramFloat5, int paramInt1, final Interpolator paramInterpolator1, final Interpolator paramInterpolator2, Runnable paramRunnable, int paramInt2, View paramView)
  {
    float f1 = (float)Math.sqrt(Math.pow(paramRect2.left - paramRect1.left, 2.0D) + Math.pow(paramRect2.top - paramRect1.top, 2.0D));
    Resources localResources = getResources();
    float f2 = localResources.getInteger(2131427356);
    if (paramInt1 < 0)
    {
      int i = localResources.getInteger(2131427354);
      if (f1 < f2) {
        i = (int)(i * this.mCubicEaseOutInterpolator.getInterpolation(f1 / f2));
      }
      int j = localResources.getInteger(2131427353);
      paramInt1 = Math.max(i, j);
    }
    TimeInterpolator localTimeInterpolator;
    if (paramInterpolator2 != null)
    {
      localTimeInterpolator = null;
      if (paramInterpolator1 != null) {}
    }
    else
    {
      localTimeInterpolator = this.mCubicEaseOutInterpolator;
    }
    final float f3 = paramDragView.getAlpha();
    animateView(paramDragView, new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        float f1 = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        int i = paramDragView.getMeasuredWidth();
        int j = paramDragView.getMeasuredHeight();
        float f2;
        float f3;
        label48:
        float f6;
        float f7;
        float f8;
        int k;
        int m;
        if (paramInterpolator2 == null)
        {
          f2 = f1;
          if (paramInterpolator1 != null) {
            break label332;
          }
          f3 = f1;
          float f4 = paramFloat2 * this.val$dropViewScale;
          float f5 = paramFloat3 * this.val$dropViewScale;
          f6 = f1 * paramFloat4 + f4 * (1.0F - f1);
          f7 = f1 * paramFloat5 + f5 * (1.0F - f1);
          f8 = f2 * paramFloat1 + f3 * (1.0F - f2);
          float f9 = paramRect1.left + (f4 - 1.0F) * i / 2.0F;
          float f10 = paramRect1.top + (f5 - 1.0F) * j / 2.0F;
          k = (int)(f9 + Math.round(f3 * (paramRect2.left - f9)));
          m = (int)(f10 + Math.round(f3 * (paramRect2.top - f10)));
          if (DragLayer.this.mAnchorView != null) {
            break label347;
          }
        }
        label332:
        label347:
        for (int n = 0;; n = (int)(DragLayer.this.mAnchorView.getScaleX() * (DragLayer.this.mAnchorViewInitialScrollX - DragLayer.this.mAnchorView.getScrollX())))
        {
          int i1 = n + (k - DragLayer.this.mDropView.getScrollX());
          int i2 = m - DragLayer.this.mDropView.getScrollY();
          DragLayer.this.mDropView.setTranslationX(i1);
          DragLayer.this.mDropView.setTranslationY(i2);
          DragLayer.this.mDropView.setScaleX(f6);
          DragLayer.this.mDropView.setScaleY(f7);
          DragLayer.this.mDropView.setAlpha(f8);
          return;
          f2 = paramInterpolator2.getInterpolation(f1);
          break;
          f3 = paramInterpolator1.getInterpolation(f1);
          break label48;
        }
      }
    }, paramInt1, localTimeInterpolator, paramRunnable, paramInt2, paramView);
  }
  
  public void animateViewIntoPosition(DragView paramDragView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, Runnable paramRunnable, int paramInt5, int paramInt6, View paramView)
  {
    animateView(paramDragView, new Rect(paramInt1, paramInt2, paramInt1 + paramDragView.getMeasuredWidth(), paramInt2 + paramDragView.getMeasuredHeight()), new Rect(paramInt3, paramInt4, paramInt3 + paramDragView.getMeasuredWidth(), paramInt4 + paramDragView.getMeasuredHeight()), paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramInt6, null, null, paramRunnable, paramInt5, paramView);
  }
  
  public void animateViewIntoPosition(DragView paramDragView, View paramView)
  {
    animateViewIntoPosition(paramDragView, paramView, null, null);
  }
  
  public void animateViewIntoPosition(DragView paramDragView, final View paramView1, int paramInt, final Runnable paramRunnable, View paramView2)
  {
    ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)paramView1.getParent();
    CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)paramView1.getLayoutParams();
    localShortcutAndWidgetContainer.measureChild(paramView1);
    Rect localRect = new Rect();
    getViewRectRelativeToSelf(paramDragView, localRect);
    int[] arrayOfInt = new int[2];
    float f1 = paramView1.getScaleX();
    arrayOfInt[0] = (localLayoutParams.x + (int)(paramView1.getMeasuredWidth() * (1.0F - f1) / 2.0F));
    arrayOfInt[1] = (localLayoutParams.y + (int)(paramView1.getMeasuredHeight() * (1.0F - f1) / 2.0F));
    float f2 = f1 * getDescendantCoordRelativeToSelf((View)paramView1.getParent(), arrayOfInt);
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    int k;
    int m;
    if ((paramView1 instanceof TextView))
    {
      k = (int)(j + Math.round(f2 * ((TextView)paramView1).getPaddingTop()) - paramDragView.getMeasuredHeight() * (1.0F - f2) / 2.0F);
      m = i - (paramDragView.getMeasuredWidth() - Math.round(f2 * paramView1.getMeasuredWidth())) / 2;
    }
    for (;;)
    {
      int n = localRect.left;
      int i1 = localRect.top;
      paramView1.setVisibility(4);
      animateViewIntoPosition(paramDragView, n, i1, m, k, 1.0F, 1.0F, 1.0F, f2, f2, new Runnable()
      {
        public void run()
        {
          paramView1.setVisibility(0);
          if (paramRunnable != null) {
            paramRunnable.run();
          }
        }
      }, 0, paramInt, paramView2);
      return;
      if ((paramView1 instanceof FolderIcon))
      {
        k = (int)((int)(j + Math.round(f2 * (paramView1.getPaddingTop() - paramDragView.getDragRegionTop())) - 2.0F * f2 / 2.0F) - (1.0F - f2) * paramDragView.getMeasuredHeight() / 2.0F);
        m = i - (paramDragView.getMeasuredWidth() - Math.round(f2 * paramView1.getMeasuredWidth())) / 2;
      }
      else
      {
        k = j - Math.round(f2 * (paramDragView.getHeight() - paramView1.getMeasuredHeight())) / 2;
        m = i - Math.round(f2 * (paramDragView.getMeasuredWidth() - paramView1.getMeasuredWidth())) / 2;
      }
    }
  }
  
  public void animateViewIntoPosition(DragView paramDragView, View paramView1, Runnable paramRunnable, View paramView2)
  {
    animateViewIntoPosition(paramDragView, paramView1, -1, paramRunnable, paramView2);
  }
  
  public void animateViewIntoPosition(DragView paramDragView, int[] paramArrayOfInt, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, Runnable paramRunnable, int paramInt2)
  {
    Rect localRect = new Rect();
    getViewRectRelativeToSelf(paramDragView, localRect);
    animateViewIntoPosition(paramDragView, localRect.left, localRect.top, paramArrayOfInt[0], paramArrayOfInt[1], paramFloat1, 1.0F, 1.0F, paramFloat2, paramFloat3, paramRunnable, paramInt1, paramInt2, null);
  }
  
  public void clearAllResizeFrames()
  {
    if (this.mResizeFrames.size() > 0)
    {
      Iterator localIterator = this.mResizeFrames.iterator();
      while (localIterator.hasNext())
      {
        AppWidgetResizeFrame localAppWidgetResizeFrame = (AppWidgetResizeFrame)localIterator.next();
        localAppWidgetResizeFrame.commitResize();
        removeView(localAppWidgetResizeFrame);
      }
      this.mResizeFrames.clear();
    }
  }
  
  public void clearAnimatedView()
  {
    if (this.mDropAnim != null) {
      this.mDropAnim.cancel();
    }
    if (this.mDropView != null) {
      this.mDragController.onDeferredEndDrag(this.mDropView);
    }
    this.mDropView = null;
    invalidate();
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    int i;
    Rect localRect;
    int j;
    int k;
    int m;
    label100:
    CellLayout localCellLayout2;
    if ((this.mInScrollArea) && (!LauncherAppState.getInstance().isScreenLarge()))
    {
      Workspace localWorkspace = this.mLauncher.getWorkspace();
      i = getMeasuredWidth();
      localRect = new Rect();
      getDescendantRectRelativeToSelf(localWorkspace.getChildAt(0), localRect);
      j = localWorkspace.getNextPage();
      boolean bool = isLayoutRtl();
      if (!bool) {
        break label158;
      }
      k = j + 1;
      CellLayout localCellLayout1 = (CellLayout)localWorkspace.getChildAt(k);
      if (!bool) {
        break label167;
      }
      m = j - 1;
      localCellLayout2 = (CellLayout)localWorkspace.getChildAt(m);
      if ((localCellLayout1 == null) || (!localCellLayout1.getIsDragOverlapping())) {
        break label176;
      }
      this.mLeftHoverDrawable.setBounds(0, localRect.top, this.mLeftHoverDrawable.getIntrinsicWidth(), localRect.bottom);
      this.mLeftHoverDrawable.draw(paramCanvas);
    }
    label158:
    label167:
    label176:
    while ((localCellLayout2 == null) || (!localCellLayout2.getIsDragOverlapping()))
    {
      return;
      k = j - 1;
      break;
      m = j + 1;
      break label100;
    }
    this.mRightHoverDrawable.setBounds(i - this.mRightHoverDrawable.getIntrinsicWidth(), localRect.top, i, localRect.bottom);
    this.mRightHoverDrawable.draw(paramCanvas);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return (this.mDragController.dispatchKeyEvent(paramKeyEvent)) || (super.dispatchKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    return this.mDragController.dispatchUnhandledMove(paramView, paramInt);
  }
  
  protected boolean fitSystemWindows(Rect paramRect)
  {
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = getChildAt(j);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localView.getLayoutParams();
      if ((localView instanceof Insettable)) {
        ((Insettable)localView).setInsets(paramRect);
      }
      for (;;)
      {
        localView.setLayoutParams(localLayoutParams);
        j++;
        break;
        localLayoutParams.topMargin += paramRect.top - this.mInsets.top;
        localLayoutParams.leftMargin += paramRect.left - this.mInsets.left;
        localLayoutParams.rightMargin += paramRect.right - this.mInsets.right;
        localLayoutParams.bottomMargin += paramRect.bottom - this.mInsets.bottom;
      }
    }
    this.mInsets.set(paramRect);
    return true;
  }
  
  public View getAnimatedView()
  {
    return this.mDropView;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    return paramInt2;
  }
  
  public float getDescendantCoordRelativeToSelf(View paramView, int[] paramArrayOfInt)
  {
    return getDescendantCoordRelativeToSelf(paramView, paramArrayOfInt, false);
  }
  
  public float getDescendantCoordRelativeToSelf(View paramView, int[] paramArrayOfInt, boolean paramBoolean)
  {
    return Utilities.getDescendantCoordRelativeToParent(paramView, this, paramArrayOfInt, paramBoolean);
  }
  
  public float getDescendantRectRelativeToSelf(View paramView, Rect paramRect)
  {
    this.mTmpXY[0] = 0;
    this.mTmpXY[1] = 0;
    float f = getDescendantCoordRelativeToSelf(paramView, this.mTmpXY);
    paramRect.set(this.mTmpXY[0], this.mTmpXY[1], (int)(this.mTmpXY[0] + f * paramView.getMeasuredWidth()), (int)(this.mTmpXY[1] + f * paramView.getMeasuredHeight()));
    return f;
  }
  
  public float getLocationInDragLayer(View paramView, int[] paramArrayOfInt)
  {
    paramArrayOfInt[0] = 0;
    paramArrayOfInt[1] = 0;
    return getDescendantCoordRelativeToSelf(paramView, paramArrayOfInt);
  }
  
  public void getViewRectRelativeToSelf(View paramView, Rect paramRect)
  {
    int[] arrayOfInt = new int[2];
    getLocationInWindow(arrayOfInt);
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    paramView.getLocationInWindow(arrayOfInt);
    int k = arrayOfInt[0];
    int m = arrayOfInt[1];
    int n = k - i;
    int i1 = m - j;
    paramRect.set(n, i1, n + paramView.getMeasuredWidth(), i1 + paramView.getMeasuredHeight());
  }
  
  public float mapCoordInSelfToDescendent(View paramView, int[] paramArrayOfInt)
  {
    return Utilities.mapCoordInSelfToDescendent(paramView, this, paramArrayOfInt);
  }
  
  public void onChildViewAdded(View paramView1, View paramView2)
  {
    updateChildIndices();
  }
  
  public void onChildViewRemoved(View paramView1, View paramView2)
  {
    updateChildIndices();
  }
  
  void onEnterScrollArea(int paramInt)
  {
    this.mInScrollArea = true;
    invalidate();
  }
  
  void onExitScrollArea()
  {
    this.mInScrollArea = false;
    invalidate();
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean onInterceptHoverEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mLauncher == null) || (this.mLauncher.getWorkspace() == null)) {}
    Folder localFolder;
    do
    {
      return false;
      localFolder = this.mLauncher.getWorkspace().getOpenFolder();
    } while ((localFolder == null) || (!((AccessibilityManager)getContext().getSystemService("accessibility")).isTouchExplorationEnabled()));
    switch (paramMotionEvent.getAction())
    {
    case 8: 
    default: 
      return false;
    }
    boolean bool2;
    for (;;)
    {
      bool2 = isEventOverFolder(localFolder, paramMotionEvent);
      if ((bool2) || (this.mHoverPointClosesFolder)) {
        break label161;
      }
      sendTapOutsideFolderAccessibilityEvent(localFolder.isEditingName());
      this.mHoverPointClosesFolder = true;
      return true;
      boolean bool1 = isEventOverFolder(localFolder, paramMotionEvent);
      if (!bool1)
      {
        sendTapOutsideFolderAccessibilityEvent(localFolder.isEditingName());
        this.mHoverPointClosesFolder = true;
        return true;
      }
      if (!bool1) {
        break;
      }
      this.mHoverPointClosesFolder = false;
    }
    return true;
    label161:
    if (bool2)
    {
      this.mHoverPointClosesFolder = false;
      return false;
    }
    return true;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (i == 0)
    {
      if (handleTouchDown(paramMotionEvent, true)) {
        return true;
      }
    }
    else if ((i == 1) || (i == 3))
    {
      if (this.mTouchCompleteListener != null) {
        this.mTouchCompleteListener.onTouchComplete();
      }
      this.mTouchCompleteListener = null;
    }
    clearAllResizeFrames();
    return this.mDragController.onInterceptTouchEvent(paramMotionEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localView.getLayoutParams();
      if ((localLayoutParams instanceof LayoutParams))
      {
        LayoutParams localLayoutParams1 = (LayoutParams)localLayoutParams;
        if (localLayoutParams1.customPosition) {
          localView.layout(localLayoutParams1.x, localLayoutParams1.y, localLayoutParams1.x + localLayoutParams1.width, localLayoutParams1.y + localLayoutParams1.height);
        }
      }
    }
  }
  
  public boolean onRequestSendAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    Folder localFolder = this.mLauncher.getWorkspace().getOpenFolder();
    if (localFolder != null)
    {
      if (paramView == localFolder) {
        return super.onRequestSendAccessibilityEvent(paramView, paramAccessibilityEvent);
      }
      return false;
    }
    return super.onRequestSendAccessibilityEvent(paramView, paramAccessibilityEvent);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    int j = (int)paramMotionEvent.getX();
    int k = (int)paramMotionEvent.getY();
    if (i == 0) {
      if (!handleTouchDown(paramMotionEvent, false)) {
        break label64;
      }
    }
    for (;;)
    {
      return true;
      if ((i == 1) || (i == 3))
      {
        if (this.mTouchCompleteListener != null) {
          this.mTouchCompleteListener.onTouchComplete();
        }
        this.mTouchCompleteListener = null;
      }
      label64:
      AppWidgetResizeFrame localAppWidgetResizeFrame = this.mCurrentResizeFrame;
      int m = 0;
      if (localAppWidgetResizeFrame != null)
      {
        m = 1;
        switch (i)
        {
        }
      }
      while (m == 0)
      {
        return this.mDragController.onTouchEvent(paramMotionEvent);
        this.mCurrentResizeFrame.visualizeResizeForDelta(j - this.mXDown, k - this.mYDown);
        continue;
        this.mCurrentResizeFrame.visualizeResizeForDelta(j - this.mXDown, k - this.mYDown);
        this.mCurrentResizeFrame.onTouchUp();
        this.mCurrentResizeFrame = null;
      }
    }
  }
  
  public void setTouchCompleteListener(TouchCompleteListener paramTouchCompleteListener)
  {
    this.mTouchCompleteListener = paramTouchCompleteListener;
  }
  
  public void setup(Launcher paramLauncher, DragController paramDragController)
  {
    this.mLauncher = paramLauncher;
    this.mDragController = paramDragController;
  }
  
  public static class LayoutParams
    extends FrameLayout.LayoutParams
  {
    public boolean customPosition = false;
    public int x;
    public int y;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
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
  }
  
  public static abstract interface TouchCompleteListener
  {
    public abstract void onTouchComplete();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DragLayer
 * JD-Core Version:    0.7.0.1
 */