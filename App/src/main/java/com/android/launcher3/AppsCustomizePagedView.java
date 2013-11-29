package com.android.launcher3;

import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AppsCustomizePagedView
  extends PagedViewWithDraggableItems
  implements View.OnClickListener, View.OnKeyListener, DragSource, LauncherTransitionable, PagedViewIcon.PressedCallback, PagedViewWidget.ShortPressListener
{
  private static float CAMERA_DISTANCE = 6500.0F;
  public static boolean DISABLE_ALL_APPS = false;
  private static float TRANSITION_MAX_ROTATION;
  private static float TRANSITION_PIVOT;
  private static float TRANSITION_SCALE_FACTOR = 0.74F;
  private Rect mAllAppsPadding = new Rect();
  private AccelerateInterpolator mAlphaInterpolator = new AccelerateInterpolator(0.9F);
  private ArrayList<AppInfo> mApps;
  private Runnable mBindWidgetRunnable = null;
  CanvasCache mCachedAppWidgetPreviewCanvas = new CanvasCache();
  RectCache mCachedAppWidgetPreviewDestRect = new RectCache();
  PaintCache mCachedAppWidgetPreviewPaint = new PaintCache();
  RectCache mCachedAppWidgetPreviewSrcRect = new RectCache();
  BitmapCache mCachedShortcutPreviewBitmap = new BitmapCache();
  CanvasCache mCachedShortcutPreviewCanvas = new CanvasCache();
  PaintCache mCachedShortcutPreviewPaint = new PaintCache();
  private Canvas mCanvas;
  private int mClingFocusedX;
  private int mClingFocusedY;
  private int mContentHeight;
  private ContentType mContentType = ContentType.Applications;
  private int mContentWidth;
  PendingAddWidgetInfo mCreateWidgetInfo = null;
  private ArrayList<Runnable> mDeferredPrepareLoadWidgetPreviewsTasks = new ArrayList();
  private ArrayList<AsyncTaskPageData> mDeferredSyncWidgetPageItems = new ArrayList();
  private DragController mDragController;
  private boolean mDraggingWidget = false;
  private boolean mHasShownAllAppsCling;
  private IconCache mIconCache;
  private boolean mInBulkBind;
  private boolean mInTransition;
  private Runnable mInflateWidgetRunnable = null;
  private Launcher mLauncher;
  private final LayoutInflater mLayoutInflater;
  private DecelerateInterpolator mLeftScreenAlphaInterpolator = new DecelerateInterpolator(4.0F);
  private boolean mNeedToUpdatePageCountsAndInvalidateData;
  private int mNumAppsPages;
  private int mNumWidgetPages;
  private final PackageManager mPackageManager;
  private PagedViewIcon mPressedIcon;
  ArrayList<AppsCustomizeAsyncTask> mRunningTasks;
  private int mSaveInstanceStateItemIndex = -1;
  private Rect mTmpRect = new Rect();
  int mWidgetCleanupState = -1;
  private int mWidgetCountX;
  private int mWidgetCountY;
  private int mWidgetHeightGap;
  private Toast mWidgetInstructionToast;
  int mWidgetLoadingId = -1;
  WidgetPreviewLoader mWidgetPreviewLoader;
  private PagedViewCellLayout mWidgetSpacingLayout;
  private int mWidgetWidthGap;
  private ArrayList<Object> mWidgets;
  Workspace.ZInterpolator mZInterpolator = new Workspace.ZInterpolator(0.5F);
  
  static
  {
    TRANSITION_PIVOT = 0.65F;
    TRANSITION_MAX_ROTATION = 22.0F;
  }
  
  public AppsCustomizePagedView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    this.mPackageManager = paramContext.getPackageManager();
    this.mApps = new ArrayList();
    this.mWidgets = new ArrayList();
    this.mIconCache = LauncherAppState.getInstance().getIconCache();
    this.mCanvas = new Canvas();
    this.mRunningTasks = new ArrayList();
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AppsCustomizePagedView, 0, 0);
    int i = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().edgeMarginPx;
    this.mWidgetHeightGap = i;
    this.mWidgetWidthGap = i;
    this.mWidgetCountX = localTypedArray.getInt(4, 2);
    this.mWidgetCountY = localTypedArray.getInt(5, 2);
    this.mClingFocusedX = localTypedArray.getInt(6, 0);
    this.mClingFocusedY = localTypedArray.getInt(7, 0);
    localTypedArray.recycle();
    this.mWidgetSpacingLayout = new PagedViewCellLayout(getContext());
    this.mFadeInAdjacentScreens = false;
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
  }
  
  private void addAppsWithoutInvalidate(ArrayList<AppInfo> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      AppInfo localAppInfo = (AppInfo)paramArrayList.get(j);
      int k = Collections.binarySearch(this.mApps, localAppInfo, LauncherModel.getAppNameComparator());
      if (k < 0) {
        this.mApps.add(-(k + 1), localAppInfo);
      }
    }
  }
  
  private void beginDraggingApplication(View paramView)
  {
    this.mLauncher.getWorkspace().onDragStartedWithItem(paramView);
    this.mLauncher.getWorkspace().beginDragShared(paramView, this);
  }
  
  private boolean beginDraggingWidget(View paramView)
  {
    this.mDraggingWidget = true;
    ImageView localImageView = (ImageView)paramView.findViewById(2131296346);
    Object localObject = (PendingAddItemInfo)paramView.getTag();
    if (localImageView.getDrawable() == null)
    {
      this.mDraggingWidget = false;
      return false;
    }
    float f = 1.0F;
    Bitmap localBitmap1;
    Point localPoint;
    if ((localObject instanceof PendingAddWidgetInfo))
    {
      if (this.mCreateWidgetInfo == null) {
        return false;
      }
      PendingAddWidgetInfo localPendingAddWidgetInfo = this.mCreateWidgetInfo;
      localObject = localPendingAddWidgetInfo;
      int j = ((PendingAddItemInfo)localObject).spanX;
      int k = ((PendingAddItemInfo)localObject).spanY;
      int[] arrayOfInt1 = this.mLauncher.getWorkspace().estimateItemSize(j, k, localPendingAddWidgetInfo, true);
      FastBitmapDrawable localFastBitmapDrawable = (FastBitmapDrawable)localImageView.getDrawable();
      int m = Math.min((int)(1.25F * localFastBitmapDrawable.getIntrinsicWidth()), arrayOfInt1[0]);
      int n = Math.min((int)(1.25F * localFastBitmapDrawable.getIntrinsicHeight()), arrayOfInt1[1]);
      int[] arrayOfInt2 = new int[1];
      localBitmap1 = this.mWidgetPreviewLoader.generateWidgetPreview(localPendingAddWidgetInfo.componentName, localPendingAddWidgetInfo.previewImage, localPendingAddWidgetInfo.icon, j, k, m, n, null, arrayOfInt2);
      int i1 = Math.min(arrayOfInt2[0], this.mWidgetPreviewLoader.maxWidthForWidgetPreview(j));
      f = i1 / localBitmap1.getWidth();
      int i2 = localFastBitmapDrawable.getIntrinsicWidth();
      localPoint = null;
      if (i1 < i2)
      {
        int i3 = (localFastBitmapDrawable.getIntrinsicWidth() - i1) / 2;
        localPoint = new Point(i3, 0);
      }
      if (((localObject instanceof PendingAddWidgetInfo)) && (((PendingAddWidgetInfo)localObject).previewImage == 0)) {
        break label464;
      }
    }
    label464:
    for (boolean bool = true;; bool = false)
    {
      Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, localBitmap1.getWidth(), localBitmap1.getHeight(), false);
      this.mLauncher.lockScreenOrientation();
      this.mLauncher.getWorkspace().onDragStartedWithItem((PendingAddItemInfo)localObject, localBitmap2, bool);
      DragController localDragController = this.mDragController;
      int i = DragController.DRAG_ACTION_COPY;
      localDragController.startDrag(localImageView, localBitmap1, this, localObject, i, localPoint, f);
      localBitmap2.recycle();
      localBitmap1.recycle();
      return true;
      PendingAddShortcutInfo localPendingAddShortcutInfo = (PendingAddShortcutInfo)paramView.getTag();
      Drawable localDrawable = this.mIconCache.getFullResIcon(localPendingAddShortcutInfo.shortcutActivityInfo);
      localBitmap1 = Bitmap.createBitmap(localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      this.mCanvas.setBitmap(localBitmap1);
      this.mCanvas.save();
      WidgetPreviewLoader.renderDrawableToBitmap(localDrawable, localBitmap1, 0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.mCanvas.restore();
      this.mCanvas.setBitmap(null);
      ((PendingAddItemInfo)localObject).spanY = 1;
      ((PendingAddItemInfo)localObject).spanX = 1;
      localPoint = null;
      break;
    }
  }
  
  private void cancelAllTasks()
  {
    Iterator localIterator = this.mRunningTasks.iterator();
    while (localIterator.hasNext())
    {
      AppsCustomizeAsyncTask localAppsCustomizeAsyncTask = (AppsCustomizeAsyncTask)localIterator.next();
      localAppsCustomizeAsyncTask.cancel(false);
      localIterator.remove();
      this.mDirtyPageContent.set(localAppsCustomizeAsyncTask.page, Boolean.valueOf(true));
      View localView = getPageAt(localAppsCustomizeAsyncTask.page);
      if ((localView instanceof PagedViewGridLayout)) {
        ((PagedViewGridLayout)localView).removeAllViewsOnPage();
      }
    }
    this.mDeferredSyncWidgetPageItems.clear();
    this.mDeferredPrepareLoadWidgetPreviewsTasks.clear();
  }
  
  private void cleanupWidgetPreloading(boolean paramBoolean)
  {
    PendingAddWidgetInfo localPendingAddWidgetInfo;
    if (!paramBoolean)
    {
      localPendingAddWidgetInfo = this.mCreateWidgetInfo;
      this.mCreateWidgetInfo = null;
      if (this.mWidgetCleanupState != 0) {
        break label58;
      }
      removeCallbacks(this.mBindWidgetRunnable);
      removeCallbacks(this.mInflateWidgetRunnable);
    }
    for (;;)
    {
      this.mWidgetCleanupState = -1;
      this.mWidgetLoadingId = -1;
      this.mCreateWidgetInfo = null;
      PagedViewWidget.resetShortPressTarget();
      return;
      label58:
      if (this.mWidgetCleanupState == 1)
      {
        if (this.mWidgetLoadingId != -1) {
          this.mLauncher.getAppWidgetHost().deleteAppWidgetId(this.mWidgetLoadingId);
        }
        removeCallbacks(this.mInflateWidgetRunnable);
      }
      else if (this.mWidgetCleanupState == 2)
      {
        if (this.mWidgetLoadingId != -1) {
          this.mLauncher.getAppWidgetHost().deleteAppWidgetId(this.mWidgetLoadingId);
        }
        AppWidgetHostView localAppWidgetHostView = localPendingAddWidgetInfo.boundWidget;
        this.mLauncher.getDragLayer().removeView(localAppWidgetHostView);
      }
    }
  }
  
  private void dumpAppWidgetProviderInfoList(String paramString1, String paramString2, ArrayList<Object> paramArrayList)
  {
    Log.d(paramString1, paramString2 + " size=" + paramArrayList.size());
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if ((localObject instanceof AppWidgetProviderInfo))
      {
        AppWidgetProviderInfo localAppWidgetProviderInfo = (AppWidgetProviderInfo)localObject;
        Log.d(paramString1, "   label=\"" + localAppWidgetProviderInfo.label + "\" previewImage=" + localAppWidgetProviderInfo.previewImage + " resizeMode=" + localAppWidgetProviderInfo.resizeMode + " configure=" + localAppWidgetProviderInfo.configure + " initialLayout=" + localAppWidgetProviderInfo.initialLayout + " minWidth=" + localAppWidgetProviderInfo.minWidth + " minHeight=" + localAppWidgetProviderInfo.minHeight);
      }
      else if ((localObject instanceof ResolveInfo))
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localObject;
        Log.d(paramString1, "   label=\"" + localResolveInfo.loadLabel(this.mPackageManager) + "\" icon=" + localResolveInfo.icon);
      }
    }
  }
  
  private void enableHwLayersOnVisiblePages()
  {
    int i = getChildCount();
    getVisiblePages(this.mTempVisiblePagesRange);
    int j = this.mTempVisiblePagesRange[0];
    int k = this.mTempVisiblePagesRange[1];
    int m = -1;
    if (j == k) {
      if (k < i - 1)
      {
        k++;
        m = k;
      }
    }
    for (;;)
    {
      for (int n = 0; n < i; n++)
      {
        View localView2 = getPageAt(n);
        if ((j > n) || (n > k) || ((n != m) && (!shouldDrawChild(localView2)))) {
          localView2.setLayerType(0, null);
        }
      }
      if (j > 0)
      {
        j--;
        m = j;
        continue;
        m = j + 1;
      }
    }
    for (int i1 = 0; i1 < i; i1++)
    {
      View localView1 = getPageAt(i1);
      if ((j <= i1) && (i1 <= k) && ((i1 == m) || (shouldDrawChild(localView1))) && (localView1.getLayerType() != 2)) {
        localView1.setLayerType(2, null);
      }
    }
  }
  
  private void endDragging(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBoolean1) || (!paramBoolean2) || ((paramView != this.mLauncher.getWorkspace()) && (!(paramView instanceof DeleteDropTarget))))
    {
      this.mLauncher.getWorkspace().removeExtraEmptyScreen(true, new Runnable()
      {
        public void run()
        {
          AppsCustomizePagedView.this.mLauncher.exitSpringLoadedDragMode();
          AppsCustomizePagedView.this.mLauncher.unlockScreenOrientation(false);
        }
      });
      return;
    }
    this.mLauncher.unlockScreenOrientation(false);
  }
  
  private int findAppByComponent(List<AppInfo> paramList, AppInfo paramAppInfo)
  {
    ComponentName localComponentName = paramAppInfo.intent.getComponent();
    int i = paramList.size();
    for (int j = 0; j < i; j++) {
      if (((AppInfo)paramList.get(j)).intent.getComponent().equals(localComponentName)) {
        return j;
      }
    }
    return -1;
  }
  
  private int getMiddleComponentIndexOnCurrentPage()
  {
    int i = -1;
    int j;
    if (getPageCount() > 0)
    {
      j = getCurrentPage();
      if (this.mContentType != ContentType.Applications) {
        break label72;
      }
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = ((AppsCustomizeCellLayout)getPageAt(j)).getShortcutsAndWidgets();
      int i1 = this.mCellCountX * this.mCellCountY;
      int i2 = localShortcutAndWidgetContainer.getChildCount();
      if (i2 > 0) {
        i = j * i1 + i2 / 2;
      }
    }
    label72:
    int k;
    int m;
    int n;
    do
    {
      return i;
      if (this.mContentType != ContentType.Widgets) {
        break;
      }
      k = this.mApps.size();
      PagedViewGridLayout localPagedViewGridLayout = (PagedViewGridLayout)getPageAt(j);
      m = this.mWidgetCountX * this.mWidgetCountY;
      n = localPagedViewGridLayout.getChildCount();
    } while (n <= 0);
    return k + j * m + n / 2;
    throw new RuntimeException("Invalid ContentType");
  }
  
  private int getSleepForPage(int paramInt)
  {
    return Math.max(0, 200 * getWidgetPageLoadPriority(paramInt));
  }
  
  private AppsCustomizeTabHost getTabHost()
  {
    return (AppsCustomizeTabHost)this.mLauncher.findViewById(2131296754);
  }
  
  private int getThreadPriorityForPage(int paramInt)
  {
    int i = getWidgetPageLoadPriority(paramInt);
    if (i <= 0) {
      return 1;
    }
    if (i <= 1) {
      return 19;
    }
    return 19;
  }
  
  private int getWidgetPageLoadPriority(int paramInt)
  {
    int i = this.mCurrentPage;
    if (this.mNextPage > -1) {
      i = this.mNextPage;
    }
    Iterator localIterator = this.mRunningTasks.iterator();
    for (int j = 2147483647; localIterator.hasNext(); j = Math.abs(((AppsCustomizeAsyncTask)localIterator.next()).page - i)) {}
    int k = Math.abs(paramInt - i);
    return k - Math.min(k, j);
  }
  
  private void invalidateOnDataChange()
  {
    if (!isDataReady())
    {
      requestLayout();
      return;
    }
    cancelAllTasks();
    invalidatePageData();
  }
  
  private void loadWidgetPreviewsInBackground(AppsCustomizeAsyncTask paramAppsCustomizeAsyncTask, AsyncTaskPageData paramAsyncTaskPageData)
  {
    if (paramAppsCustomizeAsyncTask != null) {
      paramAppsCustomizeAsyncTask.syncThreadPriority();
    }
    ArrayList localArrayList1 = paramAsyncTaskPageData.items;
    ArrayList localArrayList2 = paramAsyncTaskPageData.generatedImages;
    int i = localArrayList1.size();
    for (int j = 0;; j++)
    {
      if (j < i)
      {
        if (paramAppsCustomizeAsyncTask == null) {
          break label51;
        }
        if (!paramAppsCustomizeAsyncTask.isCancelled()) {}
      }
      else
      {
        return;
      }
      paramAppsCustomizeAsyncTask.syncThreadPriority();
      label51:
      localArrayList2.add(this.mWidgetPreviewLoader.getPreview(localArrayList1.get(j)));
    }
  }
  
  private void onSyncWidgetPageItems(AsyncTaskPageData paramAsyncTaskPageData, boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.mInTransition))
    {
      this.mDeferredSyncWidgetPageItems.add(paramAsyncTaskPageData);
      return;
    }
    for (;;)
    {
      int j;
      try
      {
        PagedViewGridLayout localPagedViewGridLayout = (PagedViewGridLayout)getPageAt(paramAsyncTaskPageData.page);
        int i = paramAsyncTaskPageData.items.size();
        j = 0;
        if (j < i)
        {
          PagedViewWidget localPagedViewWidget = (PagedViewWidget)localPagedViewGridLayout.getChildAt(j);
          if (localPagedViewWidget == null) {
            break label165;
          }
          localPagedViewWidget.applyPreview(new FastBitmapDrawable((Bitmap)paramAsyncTaskPageData.generatedImages.get(j)), j);
          break label165;
        }
        enableHwLayersOnVisiblePages();
        Iterator localIterator = this.mRunningTasks.iterator();
        if (localIterator.hasNext())
        {
          AppsCustomizeAsyncTask localAppsCustomizeAsyncTask = (AppsCustomizeAsyncTask)localIterator.next();
          localAppsCustomizeAsyncTask.setThreadPriority(getThreadPriorityForPage(localAppsCustomizeAsyncTask.page));
          continue;
        }
      }
      finally
      {
        paramAsyncTaskPageData.cleanup(false);
      }
      return;
      label165:
      j++;
    }
  }
  
  private void preloadWidget(final PendingAddWidgetInfo paramPendingAddWidgetInfo)
  {
    final AppWidgetProviderInfo localAppWidgetProviderInfo = paramPendingAddWidgetInfo.info;
    final Bundle localBundle = getDefaultOptionsForWidget(this.mLauncher, paramPendingAddWidgetInfo);
    if (localAppWidgetProviderInfo.configure != null)
    {
      paramPendingAddWidgetInfo.bindOptions = localBundle;
      return;
    }
    this.mWidgetCleanupState = 0;
    this.mBindWidgetRunnable = new Runnable()
    {
      public void run()
      {
        AppsCustomizePagedView.this.mWidgetLoadingId = AppsCustomizePagedView.this.mLauncher.getAppWidgetHost().allocateAppWidgetId();
        if (localBundle == null) {
          if (AppWidgetManager.getInstance(AppsCustomizePagedView.this.mLauncher).bindAppWidgetIdIfAllowed(AppsCustomizePagedView.this.mWidgetLoadingId, paramPendingAddWidgetInfo.componentName)) {
            AppsCustomizePagedView.this.mWidgetCleanupState = 1;
          }
        }
        while (!AppWidgetManager.getInstance(AppsCustomizePagedView.this.mLauncher).bindAppWidgetIdIfAllowed(AppsCustomizePagedView.this.mWidgetLoadingId, paramPendingAddWidgetInfo.componentName, localBundle)) {
          return;
        }
        AppsCustomizePagedView.this.mWidgetCleanupState = 1;
      }
    };
    post(this.mBindWidgetRunnable);
    this.mInflateWidgetRunnable = new Runnable()
    {
      public void run()
      {
        if (AppsCustomizePagedView.this.mWidgetCleanupState != 1) {
          return;
        }
        AppWidgetHostView localAppWidgetHostView = AppsCustomizePagedView.this.mLauncher.getAppWidgetHost().createView(AppsCustomizePagedView.this.getContext(), AppsCustomizePagedView.this.mWidgetLoadingId, localAppWidgetProviderInfo);
        paramPendingAddWidgetInfo.boundWidget = localAppWidgetHostView;
        AppsCustomizePagedView.this.mWidgetCleanupState = 2;
        localAppWidgetHostView.setVisibility(4);
        int[] arrayOfInt = AppsCustomizePagedView.this.mLauncher.getWorkspace().estimateItemSize(paramPendingAddWidgetInfo.spanX, paramPendingAddWidgetInfo.spanY, paramPendingAddWidgetInfo, false);
        DragLayer.LayoutParams localLayoutParams = new DragLayer.LayoutParams(arrayOfInt[0], arrayOfInt[1]);
        localLayoutParams.y = 0;
        localLayoutParams.x = 0;
        localLayoutParams.customPosition = true;
        localAppWidgetHostView.setLayoutParams(localLayoutParams);
        AppsCustomizePagedView.this.mLauncher.getDragLayer().addView(localAppWidgetHostView);
      }
    };
    post(this.mInflateWidgetRunnable);
  }
  
  private void prepareLoadWidgetPreviewsTask(int paramInt1, ArrayList<Object> paramArrayList, int paramInt2, int paramInt3, int paramInt4)
  {
    Iterator localIterator = this.mRunningTasks.iterator();
    while (localIterator.hasNext())
    {
      AppsCustomizeAsyncTask localAppsCustomizeAsyncTask2 = (AppsCustomizeAsyncTask)localIterator.next();
      int i = localAppsCustomizeAsyncTask2.page;
      if ((i < getAssociatedLowerPageBound(this.mCurrentPage)) || (i > getAssociatedUpperPageBound(this.mCurrentPage)))
      {
        localAppsCustomizeAsyncTask2.cancel(false);
        localIterator.remove();
      }
      else
      {
        localAppsCustomizeAsyncTask2.setThreadPriority(getThreadPriorityForPage(i));
      }
    }
    AsyncTaskPageData localAsyncTaskPageData = new AsyncTaskPageData(paramInt1, paramArrayList, paramInt2, paramInt3, new AsyncTaskCallback()new AsyncTaskCallback
    {
      /* Error */
      public void run(AppsCustomizeAsyncTask paramAnonymousAppsCustomizeAsyncTask, AsyncTaskPageData paramAnonymousAsyncTaskPageData)
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 21	com/android/launcher3/AppsCustomizePagedView$6:val$sleepMs	I
        //   4: i2l
        //   5: invokestatic 34	java/lang/Thread:sleep	(J)V
        //   8: aload_0
        //   9: getfield 19	com/android/launcher3/AppsCustomizePagedView$6:this$0	Lcom/android/launcher3/AppsCustomizePagedView;
        //   12: aload_1
        //   13: aload_2
        //   14: invokestatic 38	com/android/launcher3/AppsCustomizePagedView:access$100	(Lcom/android/launcher3/AppsCustomizePagedView;Lcom/android/launcher3/AppsCustomizeAsyncTask;Lcom/android/launcher3/AsyncTaskPageData;)V
        //   17: aload_1
        //   18: invokevirtual 44	com/android/launcher3/AppsCustomizeAsyncTask:isCancelled	()Z
        //   21: ifeq +8 -> 29
        //   24: aload_2
        //   25: iconst_1
        //   26: invokevirtual 50	com/android/launcher3/AsyncTaskPageData:cleanup	(Z)V
        //   29: return
        //   30: astore 4
        //   32: aload_1
        //   33: invokevirtual 44	com/android/launcher3/AppsCustomizeAsyncTask:isCancelled	()Z
        //   36: ifeq +8 -> 44
        //   39: aload_2
        //   40: iconst_1
        //   41: invokevirtual 50	com/android/launcher3/AsyncTaskPageData:cleanup	(Z)V
        //   44: aload 4
        //   46: athrow
        //   47: astore_3
        //   48: goto -40 -> 8
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	51	0	this	6
        //   0	51	1	paramAnonymousAppsCustomizeAsyncTask	AppsCustomizeAsyncTask
        //   0	51	2	paramAnonymousAsyncTaskPageData	AsyncTaskPageData
        //   47	1	3	localException	java.lang.Exception
        //   30	15	4	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   0	8	30	finally
        //   8	17	30	finally
        //   0	8	47	java/lang/Exception
      }
    }, new AsyncTaskCallback()
    {
      public void run(AppsCustomizeAsyncTask paramAnonymousAppsCustomizeAsyncTask, AsyncTaskPageData paramAnonymousAsyncTaskPageData)
      {
        AppsCustomizePagedView.this.mRunningTasks.remove(paramAnonymousAppsCustomizeAsyncTask);
        if (paramAnonymousAppsCustomizeAsyncTask.isCancelled()) {
          return;
        }
        AppsCustomizePagedView.this.onSyncWidgetPageItems(paramAnonymousAsyncTaskPageData, false);
      }
    }, this.mWidgetPreviewLoader);
    AppsCustomizeAsyncTask localAppsCustomizeAsyncTask1 = new AppsCustomizeAsyncTask(paramInt1, AsyncTaskPageData.Type.LoadWidgetPreviewData);
    localAppsCustomizeAsyncTask1.setThreadPriority(getThreadPriorityForPage(paramInt1));
    localAppsCustomizeAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new AsyncTaskPageData[] { localAsyncTaskPageData });
    this.mRunningTasks.add(localAppsCustomizeAsyncTask1);
  }
  
  private void removeAppsWithoutInvalidate(ArrayList<AppInfo> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      AppInfo localAppInfo = (AppInfo)paramArrayList.get(j);
      int k = findAppByComponent(this.mApps, localAppInfo);
      if (k > -1) {
        this.mApps.remove(k);
      }
    }
  }
  
  private void setVisibilityOnChildren(ViewGroup paramViewGroup, int paramInt)
  {
    int i = paramViewGroup.getChildCount();
    for (int j = 0; j < i; j++) {
      paramViewGroup.getChildAt(j).setVisibility(paramInt);
    }
  }
  
  private void setupPage(AppsCustomizeCellLayout paramAppsCustomizeCellLayout)
  {
    paramAppsCustomizeCellLayout.setGridSize(this.mCellCountX, this.mCellCountY);
    setVisibilityOnChildren(paramAppsCustomizeCellLayout, 8);
    int i = View.MeasureSpec.makeMeasureSpec(this.mContentWidth, -2147483648);
    int j = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, -2147483648);
    paramAppsCustomizeCellLayout.setMinimumWidth(getPageContentWidth());
    paramAppsCustomizeCellLayout.measure(i, j);
    paramAppsCustomizeCellLayout.setPadding(this.mAllAppsPadding.left, this.mAllAppsPadding.top, this.mAllAppsPadding.right, this.mAllAppsPadding.bottom);
    setVisibilityOnChildren(paramAppsCustomizeCellLayout, 0);
  }
  
  private void setupPage(PagedViewGridLayout paramPagedViewGridLayout)
  {
    int i = View.MeasureSpec.makeMeasureSpec(this.mContentWidth, -2147483648);
    int j = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, -2147483648);
    paramPagedViewGridLayout.setMinimumWidth(getPageContentWidth());
    paramPagedViewGridLayout.measure(i, j);
  }
  
  private void updatePageCounts()
  {
    this.mNumWidgetPages = ((int)Math.ceil(this.mWidgets.size() / (this.mWidgetCountX * this.mWidgetCountY)));
    this.mNumAppsPages = ((int)Math.ceil(this.mApps.size() / (this.mCellCountX * this.mCellCountY)));
  }
  
  private void updatePageCountsAndInvalidateData()
  {
    if (this.mInBulkBind)
    {
      this.mNeedToUpdatePageCountsAndInvalidateData = true;
      return;
    }
    updatePageCounts();
    invalidateOnDataChange();
    this.mNeedToUpdatePageCountsAndInvalidateData = false;
  }
  
  public void addApps(ArrayList<AppInfo> paramArrayList)
  {
    if (!DISABLE_ALL_APPS)
    {
      addAppsWithoutInvalidate(paramArrayList);
      updatePageCountsAndInvalidateData();
    }
  }
  
  protected boolean beginDragging(View paramView)
  {
    if (!super.beginDragging(paramView)) {
      return false;
    }
    if ((paramView instanceof PagedViewIcon)) {
      beginDraggingApplication(paramView);
    }
    while ((!(paramView instanceof PagedViewWidget)) || (beginDraggingWidget(paramView)))
    {
      postDelayed(new Runnable()
      {
        public void run()
        {
          if (AppsCustomizePagedView.this.mLauncher.getDragController().isDragging())
          {
            AppsCustomizePagedView.this.resetDrawableState();
            AppsCustomizePagedView.this.mLauncher.enterSpringLoadedDragMode();
          }
        }
      }, 150L);
      return true;
    }
    return false;
  }
  
  public void cleanUpShortPress(View paramView)
  {
    if (!this.mDraggingWidget) {
      cleanupWidgetPreloading(false);
    }
  }
  
  public void clearAllWidgetPages()
  {
    cancelAllTasks();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getPageAt(j);
      if ((localView instanceof PagedViewGridLayout))
      {
        ((PagedViewGridLayout)localView).removeAllViewsOnPage();
        this.mDirtyPageContent.set(j, Boolean.valueOf(true));
      }
    }
  }
  
  protected void determineDraggingStart(MotionEvent paramMotionEvent) {}
  
  public void dumpState()
  {
    AppInfo.dumpApplicationInfoList("AppsCustomizePagedView", "mApps", this.mApps);
    dumpAppWidgetProviderInfoList("AppsCustomizePagedView", "mWidgets", this.mWidgets);
  }
  
  protected int getAssociatedLowerPageBound(int paramInt)
  {
    int i = getChildCount();
    int j = Math.min(i, 5);
    return Math.max(Math.min(paramInt - 2, i - j), 0);
  }
  
  protected int getAssociatedUpperPageBound(int paramInt)
  {
    int i = getChildCount();
    int j = Math.min(i, 5);
    return Math.min(Math.max(paramInt + 2, j - 1), i - 1);
  }
  
  public View getContent()
  {
    if (getChildCount() > 0) {
      return getChildAt(0);
    }
    return null;
  }
  
  public ContentType getContentType()
  {
    return this.mContentType;
  }
  
  protected String getCurrentPageDescription()
  {
    int i;
    int j;
    if (this.mNextPage != -1)
    {
      i = this.mNextPage;
      if (this.mContentType != ContentType.Applications) {
        break label82;
      }
      j = 2131361933;
    }
    for (int k = this.mNumAppsPages;; k = this.mNumWidgetPages)
    {
      String str = getContext().getString(j);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i + 1);
      arrayOfObject[1] = Integer.valueOf(k);
      return String.format(str, arrayOfObject);
      i = this.mCurrentPage;
      break;
      label82:
      if (this.mContentType != ContentType.Widgets) {
        break label104;
      }
      j = 2131361934;
    }
    label104:
    throw new RuntimeException("Invalid ContentType");
  }
  
  Bundle getDefaultOptionsForWidget(Launcher paramLauncher, PendingAddWidgetInfo paramPendingAddWidgetInfo)
  {
    int i = Build.VERSION.SDK_INT;
    Bundle localBundle = null;
    if (i >= 17)
    {
      AppWidgetResizeFrame.getWidgetSizeRanges(this.mLauncher, paramPendingAddWidgetInfo.spanX, paramPendingAddWidgetInfo.spanY, this.mTmpRect);
      Rect localRect = AppWidgetHostView.getDefaultPaddingForWidget(this.mLauncher, paramPendingAddWidgetInfo.componentName, null);
      float f = getResources().getDisplayMetrics().density;
      int j = (int)((localRect.left + localRect.right) / f);
      int k = (int)((localRect.top + localRect.bottom) / f);
      localBundle = new Bundle();
      localBundle.putInt("appWidgetMinWidth", this.mTmpRect.left - j);
      localBundle.putInt("appWidgetMinHeight", this.mTmpRect.top - k);
      localBundle.putInt("appWidgetMaxWidth", this.mTmpRect.right - j);
      localBundle.putInt("appWidgetMaxHeight", this.mTmpRect.bottom - k);
    }
    return localBundle;
  }
  
  View getPageAt(int paramInt)
  {
    return getChildAt(indexToPage(paramInt));
  }
  
  public int getPageContentWidth()
  {
    return this.mContentWidth;
  }
  
  int getPageForComponent(int paramInt)
  {
    if (paramInt < 0) {
      return 0;
    }
    if (paramInt < this.mApps.size()) {
      return paramInt / (this.mCellCountX * this.mCellCountY);
    }
    int i = this.mWidgetCountX * this.mWidgetCountY;
    return (paramInt - this.mApps.size()) / i;
  }
  
  int getSaveInstanceStateIndex()
  {
    if (this.mSaveInstanceStateItemIndex == -1) {
      this.mSaveInstanceStateItemIndex = getMiddleComponentIndexOnCurrentPage();
    }
    return this.mSaveInstanceStateItemIndex;
  }
  
  public void iconPressed(PagedViewIcon paramPagedViewIcon)
  {
    if (this.mPressedIcon != null) {
      this.mPressedIcon.resetDrawableState();
    }
    this.mPressedIcon = paramPagedViewIcon;
  }
  
  protected int indexToPage(int paramInt)
  {
    return -1 + (getChildCount() - paramInt);
  }
  
  protected void init()
  {
    super.init();
    this.mCenterPagesVertically = false;
    setDragSlopeThreshold(getContext().getResources().getInteger(2131427349) / 100.0F);
  }
  
  public void onClick(View paramView)
  {
    if ((!this.mLauncher.isAllAppsVisible()) || (this.mLauncher.getWorkspace().isSwitchingState())) {}
    do
    {
      return;
      if ((paramView instanceof PagedViewIcon))
      {
        AppInfo localAppInfo = (AppInfo)paramView.getTag();
        if (this.mPressedIcon != null) {
          this.mPressedIcon.lockDrawableState();
        }
        this.mLauncher.startActivitySafely(paramView, localAppInfo.intent, localAppInfo);
        this.mLauncher.getStats().recordLaunch(localAppInfo.intent);
        return;
      }
    } while (!(paramView instanceof PagedViewWidget));
    if (this.mWidgetInstructionToast != null) {
      this.mWidgetInstructionToast.cancel();
    }
    this.mWidgetInstructionToast = Toast.makeText(getContext(), 2131361876, 0);
    this.mWidgetInstructionToast.show();
    float f = getResources().getDimensionPixelSize(2131689542);
    ImageView localImageView = (ImageView)paramView.findViewById(2131296346);
    AnimatorSet localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
    ObjectAnimator localObjectAnimator1 = LauncherAnimUtils.ofFloat(localImageView, "translationY", new float[] { f });
    localObjectAnimator1.setDuration(125L);
    ObjectAnimator localObjectAnimator2 = LauncherAnimUtils.ofFloat(localImageView, "translationY", new float[] { 0.0F });
    localObjectAnimator2.setDuration(100L);
    localAnimatorSet.play(localObjectAnimator1).before(localObjectAnimator2);
    localAnimatorSet.setInterpolator(new AccelerateInterpolator());
    localAnimatorSet.start();
  }
  
  protected void onDataReady(int paramInt1, int paramInt2)
  {
    if (this.mWidgetPreviewLoader == null) {
      this.mWidgetPreviewLoader = new WidgetPreviewLoader(this.mLauncher);
    }
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    this.mWidgetSpacingLayout.setPadding(this.mPageLayoutPaddingLeft, this.mPageLayoutPaddingTop, this.mPageLayoutPaddingRight, this.mPageLayoutPaddingBottom);
    this.mCellCountX = localDeviceProfile.allAppsNumCols;
    this.mCellCountY = localDeviceProfile.allAppsNumRows;
    updatePageCounts();
    this.mContentWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    this.mContentHeight = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    int i = View.MeasureSpec.makeMeasureSpec(this.mContentWidth, -2147483648);
    int j = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, -2147483648);
    this.mWidgetSpacingLayout.measure(i, j);
    boolean bool = getTabHost().isTransitioning();
    invalidatePageData(Math.max(0, getPageForComponent(this.mSaveInstanceStateItemIndex)), bool);
    if (!bool) {
      post(new Runnable()
      {
        public void run()
        {
          AppsCustomizePagedView.this.showAllAppsCling();
        }
      });
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    cancelAllTasks();
  }
  
  public void onDropCompleted(View paramView, DropTarget.DragObject paramDragObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      return;
    }
    endDragging(paramView, false, paramBoolean2);
    if (!paramBoolean2)
    {
      boolean bool = paramView instanceof Workspace;
      i = 0;
      if (bool)
      {
        int j = this.mLauncher.getCurrentWorkspaceScreen();
        CellLayout localCellLayout = (CellLayout)((Workspace)paramView).getChildAt(j);
        ItemInfo localItemInfo = (ItemInfo)paramDragObject.dragInfo;
        i = 0;
        if (localCellLayout != null)
        {
          localCellLayout.calculateSpans(localItemInfo);
          if (localCellLayout.findCellForSpan(null, localItemInfo.spanX, localItemInfo.spanY)) {
            break label131;
          }
        }
      }
    }
    label131:
    for (int i = 1;; i = 0)
    {
      if (i != 0) {
        this.mLauncher.showOutOfSpaceMessage(false);
      }
      paramDragObject.deferDragViewCleanupPostAnimation = false;
      cleanupWidgetPreloading(paramBoolean2);
      this.mDraggingWidget = false;
      return;
    }
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    setPadding(localDeviceProfile.edgeMarginPx, 2 * localDeviceProfile.edgeMarginPx, localDeviceProfile.edgeMarginPx, 2 * localDeviceProfile.edgeMarginPx);
  }
  
  public void onFlingToDeleteCompleted()
  {
    endDragging(null, true, true);
    cleanupWidgetPreloading(false);
    this.mDraggingWidget = false;
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    return FocusHelper.handleAppsCustomizeKeyEvent(paramView, paramInt, paramKeyEvent);
  }
  
  public void onLauncherTransitionEnd(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mInTransition = false;
    Iterator localIterator1 = this.mDeferredSyncWidgetPageItems.iterator();
    while (localIterator1.hasNext()) {
      onSyncWidgetPageItems((AsyncTaskPageData)localIterator1.next(), false);
    }
    this.mDeferredSyncWidgetPageItems.clear();
    Iterator localIterator2 = this.mDeferredPrepareLoadWidgetPreviewsTasks.iterator();
    while (localIterator2.hasNext()) {
      ((Runnable)localIterator2.next()).run();
    }
    this.mDeferredPrepareLoadWidgetPreviewsTasks.clear();
    boolean bool = false;
    if (!paramBoolean2) {
      bool = true;
    }
    this.mForceDrawAllChildrenNextFrame = bool;
  }
  
  public void onLauncherTransitionPrepare(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mInTransition = true;
    if (paramBoolean2) {
      cancelAllTasks();
    }
  }
  
  public void onLauncherTransitionStart(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2) {}
  
  public void onLauncherTransitionStep(Launcher paramLauncher, float paramFloat) {}
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    if ((!isDataReady()) && ((DISABLE_ALL_APPS) || (!this.mApps.isEmpty())) && (!this.mWidgets.isEmpty()))
    {
      setDataIsReady();
      setMeasuredDimension(i, j);
      onDataReady(i, j);
    }
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void onPackagesUpdated(ArrayList<Object> paramArrayList)
  {
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    DeviceProfile localDeviceProfile = localLauncherAppState.getDynamicGrid().getDeviceProfile();
    this.mWidgets.clear();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if ((localObject instanceof AppWidgetProviderInfo))
      {
        AppWidgetProviderInfo localAppWidgetProviderInfo = (AppWidgetProviderInfo)localObject;
        if (localLauncherAppState.shouldShowAppOrWidgetProvider(localAppWidgetProviderInfo.provider))
        {
          localAppWidgetProviderInfo.label = localAppWidgetProviderInfo.label.trim();
          if ((localAppWidgetProviderInfo.minWidth > 0) && (localAppWidgetProviderInfo.minHeight > 0))
          {
            int[] arrayOfInt1 = Launcher.getSpanForWidget(this.mLauncher, localAppWidgetProviderInfo);
            int[] arrayOfInt2 = Launcher.getMinSpanForWidget(this.mLauncher, localAppWidgetProviderInfo);
            int i = Math.min(arrayOfInt1[0], arrayOfInt2[0]);
            int j = Math.min(arrayOfInt1[1], arrayOfInt2[1]);
            if ((i <= (int)localDeviceProfile.numColumns) && (j <= (int)localDeviceProfile.numRows)) {
              this.mWidgets.add(localAppWidgetProviderInfo);
            } else {
              Log.e("AppsCustomizePagedView", "Widget " + localAppWidgetProviderInfo.provider + " can not fit on this device (" + localAppWidgetProviderInfo.minWidth + ", " + localAppWidgetProviderInfo.minHeight + ")");
            }
          }
          else
          {
            Log.e("AppsCustomizePagedView", "Widget " + localAppWidgetProviderInfo.provider + " has invalid dimensions (" + localAppWidgetProviderInfo.minWidth + ", " + localAppWidgetProviderInfo.minHeight + ")");
          }
        }
      }
      else
      {
        this.mWidgets.add(localObject);
      }
    }
    updatePageCountsAndInvalidateData();
  }
  
  protected void onPageEndMoving()
  {
    super.onPageEndMoving();
    this.mForceDrawAllChildrenNextFrame = true;
    this.mSaveInstanceStateItemIndex = -1;
  }
  
  public void onShortPress(View paramView)
  {
    if (this.mCreateWidgetInfo != null) {
      cleanupWidgetPreloading(false);
    }
    this.mCreateWidgetInfo = new PendingAddWidgetInfo((PendingAddWidgetInfo)paramView.getTag());
    preloadWidget(this.mCreateWidgetInfo);
  }
  
  protected void overScroll(float paramFloat)
  {
    acceleratedOverScroll(paramFloat);
  }
  
  public void removeApps(ArrayList<AppInfo> paramArrayList)
  {
    if (!DISABLE_ALL_APPS)
    {
      removeAppsWithoutInvalidate(paramArrayList);
      updatePageCountsAndInvalidateData();
    }
  }
  
  public void reset()
  {
    this.mSaveInstanceStateItemIndex = -1;
    AppsCustomizeTabHost localAppsCustomizeTabHost = getTabHost();
    String str = localAppsCustomizeTabHost.getCurrentTabTag();
    if ((str != null) && (!str.equals(localAppsCustomizeTabHost.getTabTagForContentType(ContentType.Applications)))) {
      localAppsCustomizeTabHost.setCurrentTabFromContent(ContentType.Applications);
    }
    if (this.mCurrentPage != 0) {
      invalidatePageData(0);
    }
  }
  
  public void resetDrawableState()
  {
    if (this.mPressedIcon != null)
    {
      this.mPressedIcon.resetDrawableState();
      this.mPressedIcon = null;
    }
  }
  
  void restorePageForIndex(int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    this.mSaveInstanceStateItemIndex = paramInt;
  }
  
  protected void screenScrolled(int paramInt)
  {
    boolean bool = isLayoutRtl();
    super.screenScrolled(paramInt);
    int i = 0;
    if (i < getChildCount())
    {
      View localView = getPageAt(i);
      float f1;
      float f3;
      float f4;
      float f5;
      label87:
      float f6;
      float f7;
      label127:
      int j;
      int k;
      float f8;
      label165:
      int m;
      label179:
      int n;
      if (localView != null)
      {
        f1 = getScrollProgress(paramInt, localView, i);
        float f2 = Math.max(0.0F, f1);
        f3 = Math.min(0.0F, f1);
        if (!bool) {
          break label281;
        }
        f4 = f2 * localView.getMeasuredWidth();
        f5 = this.mZInterpolator.getInterpolation(Math.abs(f2));
        f6 = 1.0F - f5 + f5 * TRANSITION_SCALE_FACTOR;
        if ((!bool) || (f1 <= 0.0F)) {
          break label309;
        }
        f7 = this.mAlphaInterpolator.getInterpolation(1.0F - Math.abs(f2));
        localView.setCameraDistance(this.mDensity * CAMERA_DISTANCE);
        j = localView.getMeasuredWidth();
        k = localView.getMeasuredHeight();
        if (!bool) {
          break label355;
        }
        f8 = 1.0F - TRANSITION_PIVOT;
        if (!bool) {
          break label369;
        }
        if (f1 <= 0.0F) {
          break label363;
        }
        m = 1;
        if (!bool) {
          break label394;
        }
        if (f1 >= 0.0F) {
          break label388;
        }
        n = 1;
        label193:
        if ((i != 0) || (m == 0)) {
          break label413;
        }
        localView.setPivotX(f8 * j);
        localView.setRotationY(f1 * -TRANSITION_MAX_ROTATION);
        f6 = 1.0F;
        f7 = 1.0F;
        f4 = 0.0F;
        label234:
        localView.setTranslationX(f4);
        localView.setScaleX(f6);
        localView.setScaleY(f6);
        localView.setAlpha(f7);
        if (f7 != 0.0F) {
          break label494;
        }
        localView.setVisibility(4);
      }
      for (;;)
      {
        i++;
        break;
        label281:
        f4 = f3 * localView.getMeasuredWidth();
        f5 = this.mZInterpolator.getInterpolation(Math.abs(f3));
        break label87;
        label309:
        if ((!bool) && (f1 < 0.0F))
        {
          f7 = this.mAlphaInterpolator.getInterpolation(1.0F - Math.abs(f1));
          break label127;
        }
        f7 = this.mLeftScreenAlphaInterpolator.getInterpolation(1.0F - f1);
        break label127;
        label355:
        f8 = TRANSITION_PIVOT;
        break label165;
        label363:
        m = 0;
        break label179;
        label369:
        if (f1 < 0.0F)
        {
          m = 1;
          break label179;
        }
        m = 0;
        break label179;
        label388:
        n = 0;
        break label193;
        label394:
        if (f1 > 0.0F)
        {
          n = 1;
          break label193;
        }
        n = 0;
        break label193;
        label413:
        if ((i == -1 + getChildCount()) && (n != 0))
        {
          localView.setPivotX((1.0F - f8) * j);
          localView.setRotationY(f1 * -TRANSITION_MAX_ROTATION);
          f6 = 1.0F;
          f7 = 1.0F;
          f4 = 0.0F;
          break label234;
        }
        localView.setPivotY(k / 2.0F);
        localView.setPivotX(j / 2.0F);
        localView.setRotationY(0.0F);
        break label234;
        label494:
        if (localView.getVisibility() != 0) {
          localView.setVisibility(0);
        }
      }
    }
    enableHwLayersOnVisiblePages();
  }
  
  void setAllAppsPadding(Rect paramRect)
  {
    this.mAllAppsPadding.set(paramRect);
  }
  
  public void setApps(ArrayList<AppInfo> paramArrayList)
  {
    if (!DISABLE_ALL_APPS)
    {
      this.mApps = paramArrayList;
      Collections.sort(this.mApps, LauncherModel.getAppNameComparator());
      updatePageCountsAndInvalidateData();
    }
  }
  
  public void setBulkBind(boolean paramBoolean)
  {
    if (paramBoolean) {
      this.mInBulkBind = true;
    }
    do
    {
      return;
      this.mInBulkBind = false;
    } while (!this.mNeedToUpdatePageCountsAndInvalidateData);
    updatePageCountsAndInvalidateData();
  }
  
  public void setContentType(ContentType paramContentType)
  {
    if ((this.mContentType != paramContentType) || (paramContentType == ContentType.Widgets)) {
      if (this.mContentType == paramContentType) {
        break label37;
      }
    }
    label37:
    for (int i = 0;; i = getCurrentPage())
    {
      this.mContentType = paramContentType;
      invalidatePageData(i, true);
      return;
    }
  }
  
  void setWidgetsPageIndicatorPadding(int paramInt)
  {
    this.mPageLayoutPaddingBottom = paramInt;
  }
  
  public void setup(Launcher paramLauncher, DragController paramDragController)
  {
    this.mLauncher = paramLauncher;
    this.mDragController = paramDragController;
  }
  
  void showAllAppsCling()
  {
    if ((!this.mHasShownAllAppsCling) && (isDataReady()))
    {
      this.mHasShownAllAppsCling = true;
      int[] arrayOfInt1 = new int[2];
      int[] arrayOfInt2 = this.mWidgetSpacingLayout.estimateCellPosition(this.mClingFocusedX, this.mClingFocusedY);
      this.mLauncher.getDragLayer().getLocationInDragLayer(this, arrayOfInt1);
      arrayOfInt2[0] += (getMeasuredWidth() - this.mWidgetSpacingLayout.getMeasuredWidth()) / 2 + arrayOfInt1[0];
      arrayOfInt2[1] += arrayOfInt1[1] - this.mLauncher.getDragLayer().getPaddingTop();
    }
  }
  
  protected void snapToPage(int paramInt1, int paramInt2, int paramInt3)
  {
    super.snapToPage(paramInt1, paramInt2, paramInt3);
    Iterator localIterator = this.mRunningTasks.iterator();
    while (localIterator.hasNext())
    {
      AppsCustomizeAsyncTask localAppsCustomizeAsyncTask = (AppsCustomizeAsyncTask)localIterator.next();
      int i = localAppsCustomizeAsyncTask.page;
      if (((this.mNextPage > this.mCurrentPage) && (i >= this.mCurrentPage)) || ((this.mNextPage < this.mCurrentPage) && (i <= this.mCurrentPage))) {
        localAppsCustomizeAsyncTask.setThreadPriority(getThreadPriorityForPage(i));
      } else {
        localAppsCustomizeAsyncTask.setThreadPriority(19);
      }
    }
  }
  
  public boolean supportsFlingToDelete()
  {
    return true;
  }
  
  public void surrender()
  {
    cancelAllTasks();
  }
  
  public void syncAppsPageItems(int paramInt, boolean paramBoolean)
  {
    boolean bool = isLayoutRtl();
    int i = this.mCellCountX * this.mCellCountY;
    int j = paramInt * i;
    int k = Math.min(j + i, this.mApps.size());
    AppsCustomizeCellLayout localAppsCustomizeCellLayout = (AppsCustomizeCellLayout)getPageAt(paramInt);
    localAppsCustomizeCellLayout.removeAllViewsOnPage();
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    for (int m = j; m < k; m++)
    {
      AppInfo localAppInfo = (AppInfo)this.mApps.get(m);
      PagedViewIcon localPagedViewIcon = (PagedViewIcon)this.mLayoutInflater.inflate(2130968596, localAppsCustomizeCellLayout, false);
      localPagedViewIcon.applyFromApplicationInfo(localAppInfo, true, this);
      localPagedViewIcon.setOnClickListener(this);
      localPagedViewIcon.setOnLongClickListener(this);
      localPagedViewIcon.setOnTouchListener(this);
      localPagedViewIcon.setOnKeyListener(this);
      int n = m - j;
      int i1 = n % this.mCellCountX;
      int i2 = n / this.mCellCountX;
      if (bool) {
        i1 = -1 + (this.mCellCountX - i1);
      }
      localAppsCustomizeCellLayout.addViewToCellLayout(localPagedViewIcon, -1, m, new CellLayout.LayoutParams(i1, i2, 1, 1), false);
      localArrayList1.add(localAppInfo);
      localArrayList2.add(localAppInfo.iconBitmap);
    }
    enableHwLayersOnVisiblePages();
  }
  
  public void syncPageItems(int paramInt, boolean paramBoolean)
  {
    if (this.mContentType == ContentType.Widgets)
    {
      syncWidgetPageItems(paramInt, paramBoolean);
      return;
    }
    syncAppsPageItems(paramInt, paramBoolean);
  }
  
  public void syncPages()
  {
    disablePagedViewAnimations();
    removeAllViews();
    cancelAllTasks();
    Context localContext = getContext();
    if (this.mContentType == ContentType.Applications) {
      for (int j = 0; j < this.mNumAppsPages; j++)
      {
        AppsCustomizeCellLayout localAppsCustomizeCellLayout = new AppsCustomizeCellLayout(localContext);
        setupPage(localAppsCustomizeCellLayout);
        addView(localAppsCustomizeCellLayout, new PagedView.LayoutParams(-1, -1));
      }
    }
    if (this.mContentType == ContentType.Widgets) {
      for (int i = 0; i < this.mNumWidgetPages; i++)
      {
        PagedViewGridLayout localPagedViewGridLayout = new PagedViewGridLayout(localContext, this.mWidgetCountX, this.mWidgetCountY);
        setupPage(localPagedViewGridLayout);
        addView(localPagedViewGridLayout, new PagedView.LayoutParams(-1, -1));
      }
    }
    throw new RuntimeException("Invalid ContentType");
    enablePagedViewAnimations();
  }
  
  public void syncWidgetPageItems(final int paramInt, final boolean paramBoolean)
  {
    int i = this.mWidgetCountX * this.mWidgetCountY;
    final ArrayList localArrayList = new ArrayList();
    final int j = (this.mContentWidth - this.mPageLayoutPaddingLeft - this.mPageLayoutPaddingRight - (-1 + this.mWidgetCountX) * this.mWidgetWidthGap) / this.mWidgetCountX;
    final int k = (this.mContentHeight - this.mPageLayoutPaddingTop - this.mPageLayoutPaddingBottom - (-1 + this.mWidgetCountY) * this.mWidgetHeightGap) / this.mWidgetCountY;
    int m = paramInt * i;
    for (int n = m; n < Math.min(m + i, this.mWidgets.size()); n++) {
      localArrayList.add(this.mWidgets.get(n));
    }
    final PagedViewGridLayout localPagedViewGridLayout = (PagedViewGridLayout)getPageAt(paramInt);
    localPagedViewGridLayout.setColumnCount(localPagedViewGridLayout.getCellCountX());
    int i1 = 0;
    if (i1 < localArrayList.size())
    {
      Object localObject = localArrayList.get(i1);
      PagedViewWidget localPagedViewWidget = (PagedViewWidget)this.mLayoutInflater.inflate(2130968599, localPagedViewGridLayout, false);
      if ((localObject instanceof AppWidgetProviderInfo))
      {
        AppWidgetProviderInfo localAppWidgetProviderInfo = (AppWidgetProviderInfo)localObject;
        PendingAddWidgetInfo localPendingAddWidgetInfo = new PendingAddWidgetInfo(localAppWidgetProviderInfo, null, null);
        int[] arrayOfInt1 = Launcher.getSpanForWidget(this.mLauncher, localAppWidgetProviderInfo);
        localPendingAddWidgetInfo.spanX = arrayOfInt1[0];
        localPendingAddWidgetInfo.spanY = arrayOfInt1[1];
        int[] arrayOfInt2 = Launcher.getMinSpanForWidget(this.mLauncher, localAppWidgetProviderInfo);
        localPendingAddWidgetInfo.minSpanX = arrayOfInt2[0];
        localPendingAddWidgetInfo.minSpanY = arrayOfInt2[1];
        localPagedViewWidget.applyFromAppWidgetProviderInfo(localAppWidgetProviderInfo, -1, arrayOfInt1, this.mWidgetPreviewLoader);
        localPagedViewWidget.setTag(localPendingAddWidgetInfo);
        localPagedViewWidget.setShortPressListener(this);
      }
      for (;;)
      {
        localPagedViewWidget.setOnClickListener(this);
        localPagedViewWidget.setOnLongClickListener(this);
        localPagedViewWidget.setOnTouchListener(this);
        localPagedViewWidget.setOnKeyListener(this);
        int i2 = i1 % this.mWidgetCountX;
        int i3 = i1 / this.mWidgetCountX;
        GridLayout.LayoutParams localLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(i3, GridLayout.START), GridLayout.spec(i2, GridLayout.TOP));
        localLayoutParams.width = j;
        localLayoutParams.height = k;
        localLayoutParams.setGravity(8388659);
        if (i2 > 0) {
          localLayoutParams.leftMargin = this.mWidgetWidthGap;
        }
        if (i3 > 0) {
          localLayoutParams.topMargin = this.mWidgetHeightGap;
        }
        localPagedViewGridLayout.addView(localPagedViewWidget, localLayoutParams);
        i1++;
        break;
        if ((localObject instanceof ResolveInfo))
        {
          ResolveInfo localResolveInfo = (ResolveInfo)localObject;
          PendingAddShortcutInfo localPendingAddShortcutInfo = new PendingAddShortcutInfo(localResolveInfo.activityInfo);
          localPendingAddShortcutInfo.itemType = 1;
          localPendingAddShortcutInfo.componentName = new ComponentName(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name);
          localPagedViewWidget.applyFromResolveInfo(this.mPackageManager, localResolveInfo, this.mWidgetPreviewLoader);
          localPagedViewWidget.setTag(localPendingAddShortcutInfo);
        }
      }
    }
    localPagedViewGridLayout.setOnLayoutListener(new Runnable()
    {
      public void run()
      {
        int i = j;
        int j = k;
        if (localPagedViewGridLayout.getChildCount() > 0)
        {
          int[] arrayOfInt = ((PagedViewWidget)localPagedViewGridLayout.getChildAt(0)).getPreviewSize();
          i = arrayOfInt[0];
          j = arrayOfInt[1];
        }
        AppsCustomizePagedView.this.mWidgetPreviewLoader.setPreviewSize(i, j, AppsCustomizePagedView.this.mWidgetSpacingLayout);
        if (paramBoolean)
        {
          AsyncTaskPageData localAsyncTaskPageData = new AsyncTaskPageData(paramInt, localArrayList, i, j, null, null, AppsCustomizePagedView.this.mWidgetPreviewLoader);
          AppsCustomizePagedView.this.loadWidgetPreviewsInBackground(null, localAsyncTaskPageData);
          AppsCustomizePagedView.this.onSyncWidgetPageItems(localAsyncTaskPageData, paramBoolean);
        }
        for (;;)
        {
          localPagedViewGridLayout.setOnLayoutListener(null);
          return;
          if (AppsCustomizePagedView.this.mInTransition)
          {
            AppsCustomizePagedView.this.mDeferredPrepareLoadWidgetPreviewsTasks.add(this);
          }
          else
          {
            AppsCustomizePagedView localAppsCustomizePagedView = AppsCustomizePagedView.this;
            int k = paramInt;
            ArrayList localArrayList = localArrayList;
            int m = AppsCustomizePagedView.this.mWidgetCountX;
            localAppsCustomizePagedView.prepareLoadWidgetPreviewsTask(k, localArrayList, i, j, m);
          }
        }
      }
    });
  }
  
  public void updateApps(ArrayList<AppInfo> paramArrayList)
  {
    if (!DISABLE_ALL_APPS)
    {
      removeAppsWithoutInvalidate(paramArrayList);
      addAppsWithoutInvalidate(paramArrayList);
      updatePageCountsAndInvalidateData();
    }
  }
  
  public static enum ContentType
  {
    static
    {
      ContentType[] arrayOfContentType = new ContentType[2];
      arrayOfContentType[0] = Applications;
      arrayOfContentType[1] = Widgets;
      $VALUES = arrayOfContentType;
    }
    
    private ContentType() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppsCustomizePagedView
 * JD-Core Version:    0.7.0.1
 */