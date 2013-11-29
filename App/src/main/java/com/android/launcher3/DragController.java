package com.android.launcher3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import java.util.ArrayList;
import java.util.Iterator;

public class DragController
{
  public static int DRAG_ACTION_COPY = 1;
  public static int DRAG_ACTION_MOVE = 0;
  private final int[] mCoordinatesTemp = new int[2];
  private int mDistanceSinceScroll = 0;
  private Rect mDragLayerRect = new Rect();
  private DropTarget.DragObject mDragObject;
  private DragScroller mDragScroller;
  private boolean mDragging;
  private ArrayList<DropTarget> mDropTargets = new ArrayList();
  private DropTarget mFlingToDeleteDropTarget;
  protected int mFlingToDeleteThresholdVelocity;
  private Handler mHandler;
  private InputMethodManager mInputMethodManager;
  private DropTarget mLastDropTarget;
  private int[] mLastTouch = new int[2];
  private long mLastTouchUpTime = -1L;
  private Launcher mLauncher;
  private ArrayList<DragListener> mListeners = new ArrayList();
  private int mMotionDownX;
  private int mMotionDownY;
  private View mMoveTarget;
  private Rect mRectTemp = new Rect();
  private ScrollRunnable mScrollRunnable = new ScrollRunnable();
  private int mScrollState = 0;
  private View mScrollView;
  private int mScrollZone;
  private int[] mTmpPoint = new int[2];
  private VelocityTracker mVelocityTracker;
  private IBinder mWindowToken;
  
  public DragController(Launcher paramLauncher)
  {
    Resources localResources = paramLauncher.getResources();
    this.mLauncher = paramLauncher;
    this.mHandler = new Handler();
    this.mScrollZone = localResources.getDimensionPixelSize(2131689540);
    this.mVelocityTracker = VelocityTracker.obtain();
    this.mFlingToDeleteThresholdVelocity = ((int)(localResources.getDisplayMetrics().density * localResources.getInteger(2131427333)));
  }
  
  private void acquireVelocityTrackerAndAddMovement(MotionEvent paramMotionEvent)
  {
    if (this.mVelocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
  }
  
  private void checkScrollState(int paramInt1, int paramInt2)
  {
    int i = ViewConfiguration.get(this.mLauncher).getScaledWindowTouchSlop();
    int j;
    DragLayer localDragLayer;
    int k;
    label45:
    int m;
    label53:
    int n;
    if (this.mDistanceSinceScroll < i)
    {
      j = 900;
      localDragLayer = this.mLauncher.getDragLayer();
      if (localDragLayer.getLayoutDirection() != 1) {
        break label137;
      }
      k = 1;
      if (k == 0) {
        break label143;
      }
      m = 1;
      n = 0;
      if (k == 0) {
        break label149;
      }
      label61:
      if (paramInt1 >= this.mScrollZone) {
        break label155;
      }
      if (this.mScrollState == 0)
      {
        this.mScrollState = 1;
        if (this.mDragScroller.onEnterScrollArea(paramInt1, paramInt2, m))
        {
          localDragLayer.onEnterScrollArea(m);
          this.mScrollRunnable.setDirection(m);
          this.mHandler.postDelayed(this.mScrollRunnable, j);
        }
      }
    }
    label137:
    label143:
    label149:
    label155:
    do
    {
      do
      {
        return;
        j = 500;
        break;
        k = 0;
        break label45;
        m = 0;
        break label53;
        n = 1;
        break label61;
        if (paramInt1 <= this.mScrollView.getWidth() - this.mScrollZone) {
          break label231;
        }
      } while (this.mScrollState != 0);
      this.mScrollState = 1;
    } while (!this.mDragScroller.onEnterScrollArea(paramInt1, paramInt2, n));
    localDragLayer.onEnterScrollArea(n);
    this.mScrollRunnable.setDirection(n);
    this.mHandler.postDelayed(this.mScrollRunnable, j);
    return;
    label231:
    clearScrollRunnable();
  }
  
  private void checkTouchMove(DropTarget paramDropTarget)
  {
    if (paramDropTarget != null)
    {
      if (this.mLastDropTarget != paramDropTarget)
      {
        if (this.mLastDropTarget != null) {
          this.mLastDropTarget.onDragExit(this.mDragObject);
        }
        paramDropTarget.onDragEnter(this.mDragObject);
      }
      paramDropTarget.onDragOver(this.mDragObject);
    }
    for (;;)
    {
      this.mLastDropTarget = paramDropTarget;
      return;
      if (this.mLastDropTarget != null) {
        this.mLastDropTarget.onDragExit(this.mDragObject);
      }
    }
  }
  
  private void clearScrollRunnable()
  {
    this.mHandler.removeCallbacks(this.mScrollRunnable);
    if (this.mScrollState == 1)
    {
      this.mScrollState = 0;
      this.mScrollRunnable.setDirection(1);
      this.mDragScroller.onExitScrollArea();
      this.mLauncher.getDragLayer().onExitScrollArea();
    }
  }
  
  private void drop(float paramFloat1, float paramFloat2)
  {
    int[] arrayOfInt = this.mCoordinatesTemp;
    DropTarget localDropTarget = findDropTarget((int)paramFloat1, (int)paramFloat2, arrayOfInt);
    this.mDragObject.x = arrayOfInt[0];
    this.mDragObject.y = arrayOfInt[1];
    boolean bool1 = false;
    if (localDropTarget != null)
    {
      this.mDragObject.dragComplete = true;
      localDropTarget.onDragExit(this.mDragObject);
      boolean bool2 = localDropTarget.acceptDrop(this.mDragObject);
      bool1 = false;
      if (bool2)
      {
        localDropTarget.onDrop(this.mDragObject);
        bool1 = true;
      }
    }
    this.mDragObject.dragSource.onDropCompleted((View)localDropTarget, this.mDragObject, false, bool1);
  }
  
  private void dropOnFlingToDeleteTarget(float paramFloat1, float paramFloat2, PointF paramPointF)
  {
    int[] arrayOfInt = this.mCoordinatesTemp;
    this.mDragObject.x = arrayOfInt[0];
    this.mDragObject.y = arrayOfInt[1];
    if ((this.mLastDropTarget != null) && (this.mFlingToDeleteDropTarget != this.mLastDropTarget)) {
      this.mLastDropTarget.onDragExit(this.mDragObject);
    }
    this.mFlingToDeleteDropTarget.onDragEnter(this.mDragObject);
    this.mDragObject.dragComplete = true;
    this.mFlingToDeleteDropTarget.onDragExit(this.mDragObject);
    boolean bool1 = this.mFlingToDeleteDropTarget.acceptDrop(this.mDragObject);
    boolean bool2 = false;
    if (bool1)
    {
      this.mFlingToDeleteDropTarget.onFlingToDelete(this.mDragObject, this.mDragObject.x, this.mDragObject.y, paramPointF);
      bool2 = true;
    }
    this.mDragObject.dragSource.onDropCompleted((View)this.mFlingToDeleteDropTarget, this.mDragObject, true, bool2);
  }
  
  private void endDrag()
  {
    if (this.mDragging)
    {
      this.mDragging = false;
      clearScrollRunnable();
      DragView localDragView = this.mDragObject.dragView;
      boolean bool = false;
      if (localDragView != null)
      {
        bool = this.mDragObject.deferDragViewCleanupPostAnimation;
        if (!bool) {
          this.mDragObject.dragView.remove();
        }
        this.mDragObject.dragView = null;
      }
      if (!bool)
      {
        Iterator localIterator = this.mListeners.iterator();
        while (localIterator.hasNext()) {
          ((DragListener)localIterator.next()).onDragEnd();
        }
      }
    }
    releaseVelocityTracker();
  }
  
  private DropTarget findDropTarget(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    Rect localRect = this.mRectTemp;
    ArrayList localArrayList = this.mDropTargets;
    int i = -1 + localArrayList.size();
    if (i >= 0)
    {
      DropTarget localDropTarget = (DropTarget)localArrayList.get(i);
      if (!localDropTarget.isDropEnabled()) {}
      do
      {
        i--;
        break;
        localDropTarget.getHitRectRelativeToDragLayer(localRect);
        this.mDragObject.x = paramInt1;
        this.mDragObject.y = paramInt2;
      } while (!localRect.contains(paramInt1, paramInt2));
      paramArrayOfInt[0] = paramInt1;
      paramArrayOfInt[1] = paramInt2;
      this.mLauncher.getDragLayer().mapCoordInSelfToDescendent((View)localDropTarget, paramArrayOfInt);
      return localDropTarget;
    }
    return null;
  }
  
  private int[] getClampedDragLayerPos(float paramFloat1, float paramFloat2)
  {
    this.mLauncher.getDragLayer().getLocalVisibleRect(this.mDragLayerRect);
    this.mTmpPoint[0] = ((int)Math.max(this.mDragLayerRect.left, Math.min(paramFloat1, -1 + this.mDragLayerRect.right)));
    this.mTmpPoint[1] = ((int)Math.max(this.mDragLayerRect.top, Math.min(paramFloat2, -1 + this.mDragLayerRect.bottom)));
    return this.mTmpPoint;
  }
  
  private void handleMoveEvent(int paramInt1, int paramInt2)
  {
    this.mDragObject.dragView.move(paramInt1, paramInt2);
    int[] arrayOfInt = this.mCoordinatesTemp;
    DropTarget localDropTarget = findDropTarget(paramInt1, paramInt2, arrayOfInt);
    this.mDragObject.x = arrayOfInt[0];
    this.mDragObject.y = arrayOfInt[1];
    checkTouchMove(localDropTarget);
    this.mDistanceSinceScroll = ((int)(this.mDistanceSinceScroll + Math.sqrt(Math.pow(this.mLastTouch[0] - paramInt1, 2.0D) + Math.pow(this.mLastTouch[1] - paramInt2, 2.0D))));
    this.mLastTouch[0] = paramInt1;
    this.mLastTouch[1] = paramInt2;
    checkScrollState(paramInt1, paramInt2);
  }
  
  private PointF isFlingingToDelete(DragSource paramDragSource)
  {
    PointF localPointF1;
    if (this.mFlingToDeleteDropTarget == null) {
      localPointF1 = null;
    }
    PointF localPointF2;
    do
    {
      return localPointF1;
      if (!paramDragSource.supportsFlingToDelete()) {
        return null;
      }
      ViewConfiguration localViewConfiguration = ViewConfiguration.get(this.mLauncher);
      this.mVelocityTracker.computeCurrentVelocity(1000, localViewConfiguration.getScaledMaximumFlingVelocity());
      if (this.mVelocityTracker.getYVelocity() >= this.mFlingToDeleteThresholdVelocity) {
        break;
      }
      localPointF1 = new PointF(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
      localPointF2 = new PointF(0.0F, -1.0F);
    } while ((float)Math.acos((localPointF1.x * localPointF2.x + localPointF1.y * localPointF2.y) / (localPointF1.length() * localPointF2.length())) <= Math.toRadians(35.0D));
    return null;
  }
  
  private void releaseVelocityTracker()
  {
    if (this.mVelocityTracker != null)
    {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
  }
  
  public void addDragListener(DragListener paramDragListener)
  {
    this.mListeners.add(paramDragListener);
  }
  
  public void addDropTarget(DropTarget paramDropTarget)
  {
    this.mDropTargets.add(paramDropTarget);
  }
  
  public void cancelDrag()
  {
    if (this.mDragging)
    {
      if (this.mLastDropTarget != null) {
        this.mLastDropTarget.onDragExit(this.mDragObject);
      }
      this.mDragObject.deferDragViewCleanupPostAnimation = false;
      this.mDragObject.cancelled = true;
      this.mDragObject.dragComplete = true;
      this.mDragObject.dragSource.onDropCompleted(null, this.mDragObject, false, false);
    }
    endDrag();
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return this.mDragging;
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    return (this.mMoveTarget != null) && (this.mMoveTarget.dispatchUnhandledMove(paramView, paramInt));
  }
  
  public void forceTouchMove()
  {
    int[] arrayOfInt = this.mCoordinatesTemp;
    DropTarget localDropTarget = findDropTarget(this.mLastTouch[0], this.mLastTouch[1], arrayOfInt);
    this.mDragObject.x = arrayOfInt[0];
    this.mDragObject.y = arrayOfInt[1];
    checkTouchMove(localDropTarget);
  }
  
  long getLastGestureUpTime()
  {
    if (this.mDragging) {
      return System.currentTimeMillis();
    }
    return this.mLastTouchUpTime;
  }
  
  public boolean isDragging()
  {
    return this.mDragging;
  }
  
  public void onAppsRemoved(ArrayList<String> paramArrayList, ArrayList<AppInfo> paramArrayList1)
  {
    ShortcutInfo localShortcutInfo;
    Iterator localIterator;
    if (this.mDragObject != null)
    {
      Object localObject = this.mDragObject.dragInfo;
      if ((localObject instanceof ShortcutInfo))
      {
        localShortcutInfo = (ShortcutInfo)localObject;
        localIterator = paramArrayList1.iterator();
      }
    }
    for (;;)
    {
      if (localIterator.hasNext())
      {
        AppInfo localAppInfo = (AppInfo)localIterator.next();
        if ((localShortcutInfo == null) || (localShortcutInfo.intent == null)) {
          continue;
        }
        ComponentName localComponentName = localShortcutInfo.intent.getComponent();
        if ((!localComponentName.equals(localAppInfo.componentName)) && (!paramArrayList.contains(localComponentName.getPackageName()))) {
          break label117;
        }
      }
      label117:
      for (int i = 1; i != 0; i = 0)
      {
        cancelDrag();
        return;
      }
    }
  }
  
  void onDeferredEndDrag(DragView paramDragView)
  {
    paramDragView.remove();
    if (this.mDragObject.deferDragViewCleanupPostAnimation)
    {
      Iterator localIterator = this.mListeners.iterator();
      while (localIterator.hasNext()) {
        ((DragListener)localIterator.next()).onDragEnd();
      }
    }
  }
  
  void onDeferredEndFling(DropTarget.DragObject paramDragObject)
  {
    paramDragObject.dragSource.onFlingToDeleteCompleted();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    int[] arrayOfInt = getClampedDragLayerPos(paramMotionEvent.getX(), paramMotionEvent.getY());
    int j = arrayOfInt[0];
    int k = arrayOfInt[1];
    switch (i)
    {
    }
    for (;;)
    {
      return this.mDragging;
      this.mMotionDownX = j;
      this.mMotionDownY = k;
      this.mLastDropTarget = null;
      continue;
      this.mLastTouchUpTime = System.currentTimeMillis();
      if (this.mDragging)
      {
        PointF localPointF = isFlingingToDelete(this.mDragObject.dragSource);
        if (!DeleteDropTarget.willAcceptDrop(this.mDragObject.dragInfo)) {
          localPointF = null;
        }
        if (localPointF == null) {
          break label156;
        }
        dropOnFlingToDeleteTarget(j, k, localPointF);
      }
      for (;;)
      {
        endDrag();
        break;
        label156:
        drop(j, k);
      }
      cancelDrag();
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.mDragging) {
      return false;
    }
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    int[] arrayOfInt = getClampedDragLayerPos(paramMotionEvent.getX(), paramMotionEvent.getY());
    int j = arrayOfInt[0];
    int k = arrayOfInt[1];
    switch (i)
    {
    }
    for (;;)
    {
      return true;
      this.mMotionDownX = j;
      this.mMotionDownY = k;
      if ((j < this.mScrollZone) || (j > this.mScrollView.getWidth() - this.mScrollZone))
      {
        this.mScrollState = 1;
        this.mHandler.postDelayed(this.mScrollRunnable, 500L);
      }
      for (;;)
      {
        handleMoveEvent(j, k);
        break;
        this.mScrollState = 0;
      }
      handleMoveEvent(j, k);
      continue;
      handleMoveEvent(j, k);
      this.mHandler.removeCallbacks(this.mScrollRunnable);
      if (this.mDragging)
      {
        PointF localPointF = isFlingingToDelete(this.mDragObject.dragSource);
        if (!DeleteDropTarget.willAcceptDrop(this.mDragObject.dragInfo)) {
          localPointF = null;
        }
        if (localPointF == null) {
          break label241;
        }
        dropOnFlingToDeleteTarget(j, k, localPointF);
      }
      for (;;)
      {
        endDrag();
        break;
        label241:
        drop(j, k);
      }
      this.mHandler.removeCallbacks(this.mScrollRunnable);
      cancelDrag();
    }
  }
  
  public void removeDropTarget(DropTarget paramDropTarget)
  {
    this.mDropTargets.remove(paramDropTarget);
  }
  
  void resetLastGestureUpTime()
  {
    this.mLastTouchUpTime = -1L;
  }
  
  public void setDragScoller(DragScroller paramDragScroller)
  {
    this.mDragScroller = paramDragScroller;
  }
  
  public void setFlingToDeleteDropTarget(DropTarget paramDropTarget)
  {
    this.mFlingToDeleteDropTarget = paramDropTarget;
  }
  
  void setMoveTarget(View paramView)
  {
    this.mMoveTarget = paramView;
  }
  
  public void setScrollView(View paramView)
  {
    this.mScrollView = paramView;
  }
  
  public void setWindowToken(IBinder paramIBinder)
  {
    this.mWindowToken = paramIBinder;
  }
  
  public void startDrag(Bitmap paramBitmap, int paramInt1, int paramInt2, DragSource paramDragSource, Object paramObject, int paramInt3, Point paramPoint, Rect paramRect, float paramFloat)
  {
    if (this.mInputMethodManager == null) {
      this.mInputMethodManager = ((InputMethodManager)this.mLauncher.getSystemService("input_method"));
    }
    this.mInputMethodManager.hideSoftInputFromWindow(this.mWindowToken, 0);
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((DragListener)localIterator.next()).onDragStart(paramDragSource, paramObject, paramInt3);
    }
    int i = this.mMotionDownX - paramInt1;
    int j = this.mMotionDownY - paramInt2;
    int k;
    if (paramRect == null)
    {
      k = 0;
      if (paramRect != null) {
        break label315;
      }
    }
    label315:
    for (int m = 0;; m = paramRect.top)
    {
      this.mDragging = true;
      this.mDragObject = new DropTarget.DragObject();
      this.mDragObject.dragComplete = false;
      this.mDragObject.xOffset = (this.mMotionDownX - (paramInt1 + k));
      this.mDragObject.yOffset = (this.mMotionDownY - (paramInt2 + m));
      this.mDragObject.dragSource = paramDragSource;
      this.mDragObject.dragInfo = paramObject;
      DropTarget.DragObject localDragObject = this.mDragObject;
      DragView localDragView = new DragView(this.mLauncher, paramBitmap, i, j, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), paramFloat);
      localDragObject.dragView = localDragView;
      if (paramPoint != null) {
        localDragView.setDragVisualizeOffset(new Point(paramPoint));
      }
      if (paramRect != null) {
        localDragView.setDragRegion(new Rect(paramRect));
      }
      this.mLauncher.getDragLayer().performHapticFeedback(0);
      localDragView.show(this.mMotionDownX, this.mMotionDownY);
      handleMoveEvent(this.mMotionDownX, this.mMotionDownY);
      return;
      k = paramRect.left;
      break;
    }
  }
  
  public void startDrag(View paramView, Bitmap paramBitmap, DragSource paramDragSource, Object paramObject, int paramInt, Point paramPoint, float paramFloat)
  {
    int[] arrayOfInt = this.mCoordinatesTemp;
    this.mLauncher.getDragLayer().getLocationInDragLayer(paramView, arrayOfInt);
    int i;
    if (paramPoint != null)
    {
      i = paramPoint.x;
      if (paramPoint == null) {
        break label139;
      }
    }
    label139:
    for (int j = paramPoint.y;; j = 0)
    {
      startDrag(paramBitmap, i + (arrayOfInt[0] + paramView.getPaddingLeft()) + (int)((paramFloat * paramBitmap.getWidth() - paramBitmap.getWidth()) / 2.0F), j + (arrayOfInt[1] + paramView.getPaddingTop()) + (int)((paramFloat * paramBitmap.getHeight() - paramBitmap.getHeight()) / 2.0F), paramDragSource, paramObject, paramInt, null, null, paramFloat);
      if (paramInt == DRAG_ACTION_MOVE) {
        paramView.setVisibility(8);
      }
      return;
      i = 0;
      break;
    }
  }
  
  static abstract interface DragListener
  {
    public abstract void onDragEnd();
    
    public abstract void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt);
  }
  
  private class ScrollRunnable
    implements Runnable
  {
    private int mDirection;
    
    ScrollRunnable() {}
    
    public void run()
    {
      if (DragController.this.mDragScroller != null)
      {
        if (this.mDirection != 0) {
          break label109;
        }
        DragController.this.mDragScroller.scrollLeft();
      }
      for (;;)
      {
        DragController.access$102(DragController.this, 0);
        DragController.access$202(DragController.this, 0);
        DragController.this.mDragScroller.onExitScrollArea();
        DragController.this.mLauncher.getDragLayer().onExitScrollArea();
        if (DragController.this.isDragging()) {
          DragController.this.checkScrollState(DragController.this.mLastTouch[0], DragController.this.mLastTouch[1]);
        }
        return;
        label109:
        DragController.this.mDragScroller.scrollRight();
      }
    }
    
    void setDirection(int paramInt)
    {
      this.mDirection = paramInt;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DragController
 * JD-Core Version:    0.7.0.1
 */