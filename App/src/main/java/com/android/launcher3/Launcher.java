package com.android.launcher3;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Advanceable;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Launcher
  extends Activity
  implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, LauncherModel.Callbacks
{
  private static int NEW_APPS_ANIMATION_DELAY;
  private static int NEW_APPS_ANIMATION_INACTIVE_TIMEOUT_SECONDS;
  private static int NEW_APPS_PAGE_MOVE_DELAY;
  private static ArrayList<ComponentName> mIntentsOnWorkspaceFromUpgradePath;
  private static Drawable.ConstantState[] sAppMarketIcon;
  static DateFormat sDateFormat;
  static Date sDateStamp;
  static final ArrayList<String> sDumpLogs;
  private static HashMap<Long, FolderInfo> sFolders;
  public static boolean sForceEnableRotation = isPropertyEnabled("launcher_force_rotate");
  private static Drawable.ConstantState[] sGlobalSearchIcon;
  private static LocaleConfiguration sLocaleConfiguration;
  private static final Object sLock = new Object();
  private static boolean sPausedFromUserAction;
  private static ArrayList<PendingAddArguments> sPendingAddList;
  static long sRunStart;
  private static int sScreen = 2;
  private static Drawable.ConstantState[] sVoiceSearchIcon;
  private final int ADVANCE_MSG = 1;
  private final int mAdvanceInterval = 20000;
  private final int mAdvanceStagger = 250;
  private View mAllAppsButton;
  private Intent mAppMarketIntent = null;
  private LauncherAppWidgetHost mAppWidgetHost;
  private AppWidgetManager mAppWidgetManager;
  private AppsCustomizePagedView mAppsCustomizeContent;
  private AppsCustomizeTabHost mAppsCustomizeTabHost;
  private boolean mAttached = false;
  private boolean mAutoAdvanceRunning = false;
  private long mAutoAdvanceSentTime;
  private long mAutoAdvanceTimeLeft = -1L;
  private ArrayList<Runnable> mBindOnResumeCallbacks = new ArrayList();
  private Runnable mBindPackagesUpdatedRunnable = new Runnable()
  {
    public void run()
    {
      Launcher.this.bindPackagesUpdated(Launcher.this.mWidgetsAndShortcuts);
      Launcher.access$2902(Launcher.this, null);
    }
  };
  private Runnable mBuildLayersRunnable = new Runnable()
  {
    public void run()
    {
      if (Launcher.this.mWorkspace != null) {
        Launcher.this.mWorkspace.buildPageHardwareLayers();
      }
    }
  };
  private final BroadcastReceiver mCloseSystemDialogsReceiver = new CloseSystemDialogsIntentReceiver(null);
  private SpannableStringBuilder mDefaultKeySsb = null;
  private DragController mDragController;
  private DragLayer mDragLayer;
  private Bitmap mFolderIconBitmap;
  private Canvas mFolderIconCanvas;
  private ImageView mFolderIconImageView;
  private FolderInfo mFolderInfo;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (paramAnonymousMessage.what == 1)
      {
        int i = 0;
        Iterator localIterator = Launcher.this.mWidgetsToAdvance.keySet().iterator();
        while (localIterator.hasNext())
        {
          View localView1 = (View)localIterator.next();
          final View localView2 = localView1.findViewById(((AppWidgetProviderInfo)Launcher.this.mWidgetsToAdvance.get(localView1)).autoAdvanceViewId);
          int j = i * 250;
          if ((localView2 instanceof Advanceable)) {
            postDelayed(new Runnable()
            {
              public void run()
              {
                ((Advanceable)localView2).advance();
              }
            }, j);
          }
          i++;
        }
        Launcher.this.sendAdvanceMessage(20000L);
      }
    }
  };
  private View.OnTouchListener mHapticFeedbackTouchListener;
  private boolean mHasFocus = false;
  private HideFromAccessibilityHelper mHideFromAccessibilityHelper = new HideFromAccessibilityHelper();
  private Hotseat mHotseat;
  private IconCache mIconCache;
  private LayoutInflater mInflater;
  private View mLauncherView;
  private LauncherModel mModel;
  private ArrayList<Runnable> mOnResumeCallbacks = new ArrayList();
  private boolean mOnResumeNeedsLoad;
  private State mOnResumeState = State.NONE;
  private View mOverviewPanel;
  private boolean mPaused = true;
  private ItemInfo mPendingAddInfo = new ItemInfo();
  private int mPendingAddWidgetId = -1;
  private AppWidgetProviderInfo mPendingAddWidgetInfo;
  private View mQsbBar;
  QSBScroller mQsbScroller = new QSBScroller()
  {
    int scrollY = 0;
  };
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      String str = paramAnonymousIntent.getAction();
      if ("android.intent.action.SCREEN_OFF".equals(str))
      {
        Launcher.access$1302(Launcher.this, false);
        Launcher.this.mDragLayer.clearAllResizeFrames();
        Launcher.this.updateRunning();
        if ((Launcher.this.mAppsCustomizeTabHost != null) && (Launcher.this.mPendingAddInfo.container == -1L)) {
          Launcher.this.showWorkspace(false);
        }
      }
      while (!"android.intent.action.USER_PRESENT".equals(str)) {
        return;
      }
      Launcher.access$1302(Launcher.this, true);
      Launcher.this.updateRunning();
    }
  };
  private Rect mRectForFolderAnimation = new Rect();
  private final int mRestoreScreenOrientationDelay = 500;
  private boolean mRestoring;
  private Bundle mSavedInstanceState;
  private Bundle mSavedState;
  private SearchDropTargetBar mSearchDropTargetBar;
  private SharedPreferences mSharedPrefs;
  private State mState = State.WORKSPACE;
  private AnimatorSet mStateAnimation;
  private Stats mStats;
  private final ArrayList<Integer> mSynchronouslyBoundPages = new ArrayList();
  private int[] mTmpAddItemCellCoordinates = new int[2];
  private boolean mUserPresent = true;
  private boolean mVisible = false;
  private boolean mWaitingForResult;
  private BubbleTextView mWaitingForResume;
  private View mWeightWatcher;
  private final ContentObserver mWidgetObserver = new AppWidgetResetObserver();
  private ArrayList<Object> mWidgetsAndShortcuts;
  private HashMap<View, AppWidgetProviderInfo> mWidgetsToAdvance = new HashMap();
  private Workspace mWorkspace;
  private Drawable mWorkspaceBackgroundDrawable;
  private boolean mWorkspaceLoading = true;
  
  static
  {
    NEW_APPS_PAGE_MOVE_DELAY = 500;
    NEW_APPS_ANIMATION_INACTIVE_TIMEOUT_SECONDS = 5;
    NEW_APPS_ANIMATION_DELAY = 500;
    sPausedFromUserAction = false;
    sLocaleConfiguration = null;
    sFolders = new HashMap();
    sGlobalSearchIcon = new Drawable.ConstantState[2];
    sVoiceSearchIcon = new Drawable.ConstantState[2];
    sAppMarketIcon = new Drawable.ConstantState[2];
    sDumpLogs = new ArrayList();
    sDateStamp = new Date();
    sDateFormat = DateFormat.getDateTimeInstance(3, 3);
    sRunStart = System.currentTimeMillis();
    mIntentsOnWorkspaceFromUpgradePath = null;
    sPendingAddList = new ArrayList();
  }
  
  private boolean acceptFilter()
  {
    return !((InputMethodManager)getSystemService("input_method")).isFullscreenMode();
  }
  
  public static void addDumpLog(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean) {
      Log.d(paramString1, paramString2);
    }
  }
  
  private boolean canRunNewAppsAnimation()
  {
    return System.currentTimeMillis() - this.mDragController.getLastGestureUpTime() > 1000 * NEW_APPS_ANIMATION_INACTIVE_TIMEOUT_SECONDS;
  }
  
  private void checkForLocaleChange()
  {
    if (sLocaleConfiguration == null) {
      new AsyncTask()
      {
        protected Launcher.LocaleConfiguration doInBackground(Void... paramAnonymousVarArgs)
        {
          Launcher.LocaleConfiguration localLocaleConfiguration = new Launcher.LocaleConfiguration(null);
          Launcher.readConfiguration(Launcher.this, localLocaleConfiguration);
          return localLocaleConfiguration;
        }
        
        protected void onPostExecute(Launcher.LocaleConfiguration paramAnonymousLocaleConfiguration)
        {
          Launcher.access$402(paramAnonymousLocaleConfiguration);
          Launcher.this.checkForLocaleChange();
        }
      }.execute(new Void[0]);
    }
    String str2;
    int j;
    int m;
    int n;
    do
    {
      return;
      Configuration localConfiguration = getResources().getConfiguration();
      String str1 = sLocaleConfiguration.locale;
      str2 = localConfiguration.locale.toString();
      int i = sLocaleConfiguration.mcc;
      j = localConfiguration.mcc;
      int k = sLocaleConfiguration.mnc;
      m = localConfiguration.mnc;
      if ((str2.equals(str1)) && (j == i))
      {
        n = 0;
        if (m == k) {}
      }
      else
      {
        n = 1;
      }
    } while (n == 0);
    sLocaleConfiguration.locale = str2;
    sLocaleConfiguration.mcc = j;
    sLocaleConfiguration.mnc = m;
    this.mIconCache.flush();
    new Thread("WriteLocaleConfiguration")
    {
      public void run()
      {
        Launcher.writeConfiguration(Launcher.this, this.val$localeConfiguration);
      }
    }.start();
  }
  
  private void clearTypedText()
  {
    this.mDefaultKeySsb.clear();
    this.mDefaultKeySsb.clearSpans();
    Selection.setSelection(this.mDefaultKeySsb, 0);
  }
  
  private boolean completeAdd(PendingAddArguments paramPendingAddArguments)
  {
    int i = paramPendingAddArguments.requestCode;
    boolean bool = false;
    switch (i)
    {
    }
    for (;;)
    {
      resetAddInfo();
      return bool;
      completeAddApplication(paramPendingAddArguments.intent, paramPendingAddArguments.container, paramPendingAddArguments.screenId, paramPendingAddArguments.cellX, paramPendingAddArguments.cellY);
      bool = false;
      continue;
      processShortcut(paramPendingAddArguments.intent);
      bool = false;
      continue;
      completeAddShortcut(paramPendingAddArguments.intent, paramPendingAddArguments.container, paramPendingAddArguments.screenId, paramPendingAddArguments.cellX, paramPendingAddArguments.cellY);
      bool = true;
      continue;
      completeAddAppWidget(paramPendingAddArguments.intent.getIntExtra("appWidgetId", -1), paramPendingAddArguments.container, paramPendingAddArguments.screenId, null, null);
      bool = true;
    }
  }
  
  private void completeAddAppWidget(final int paramInt, long paramLong1, long paramLong2, AppWidgetHostView paramAppWidgetHostView, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    if (paramAppWidgetProviderInfo == null) {
      paramAppWidgetProviderInfo = this.mAppWidgetManager.getAppWidgetInfo(paramInt);
    }
    CellLayout localCellLayout = getCellLayout(paramLong1, paramLong2);
    int[] arrayOfInt1 = getMinSpanForWidget(this, paramAppWidgetProviderInfo);
    int[] arrayOfInt2 = getSpanForWidget(this, paramAppWidgetProviderInfo);
    int[] arrayOfInt3 = this.mTmpAddItemCellCoordinates;
    int[] arrayOfInt4 = this.mPendingAddInfo.dropPos;
    int[] arrayOfInt5 = new int[2];
    boolean bool;
    if ((this.mPendingAddInfo.cellX >= 0) && (this.mPendingAddInfo.cellY >= 0))
    {
      arrayOfInt3[0] = this.mPendingAddInfo.cellX;
      arrayOfInt3[1] = this.mPendingAddInfo.cellY;
      arrayOfInt2[0] = this.mPendingAddInfo.spanX;
      arrayOfInt2[1] = this.mPendingAddInfo.spanY;
      bool = true;
    }
    while (!bool)
    {
      if (paramInt != -1) {
        new Thread("deleteAppWidgetId")
        {
          public void run()
          {
            Launcher.this.mAppWidgetHost.deleteAppWidgetId(paramInt);
          }
        }.start();
      }
      showOutOfSpaceMessage(isHotseatLayout(localCellLayout));
      return;
      if (arrayOfInt4 != null)
      {
        int[] arrayOfInt6 = localCellLayout.findNearestVacantArea(arrayOfInt4[0], arrayOfInt4[1], arrayOfInt1[0], arrayOfInt1[1], arrayOfInt2[0], arrayOfInt2[1], arrayOfInt3, arrayOfInt5);
        arrayOfInt2[0] = arrayOfInt5[0];
        arrayOfInt2[1] = arrayOfInt5[1];
        if (arrayOfInt6 != null) {}
        for (bool = true;; bool = false) {
          break;
        }
      }
      bool = localCellLayout.findCellForSpan(arrayOfInt3, arrayOfInt1[0], arrayOfInt1[1]);
    }
    LauncherAppWidgetInfo localLauncherAppWidgetInfo = new LauncherAppWidgetInfo(paramInt, paramAppWidgetProviderInfo.provider);
    localLauncherAppWidgetInfo.spanX = arrayOfInt2[0];
    localLauncherAppWidgetInfo.spanY = arrayOfInt2[1];
    localLauncherAppWidgetInfo.minSpanX = this.mPendingAddInfo.minSpanX;
    localLauncherAppWidgetInfo.minSpanY = this.mPendingAddInfo.minSpanY;
    LauncherModel.addItemToDatabase(this, localLauncherAppWidgetInfo, paramLong1, paramLong2, arrayOfInt3[0], arrayOfInt3[1], false);
    if (!this.mRestoring)
    {
      if (paramAppWidgetHostView != null) {
        break label448;
      }
      localLauncherAppWidgetInfo.hostView = this.mAppWidgetHost.createView(this, paramInt, paramAppWidgetProviderInfo);
      localLauncherAppWidgetInfo.hostView.setAppWidget(paramInt, paramAppWidgetProviderInfo);
    }
    for (;;)
    {
      localLauncherAppWidgetInfo.hostView.setTag(localLauncherAppWidgetInfo);
      localLauncherAppWidgetInfo.hostView.setVisibility(0);
      localLauncherAppWidgetInfo.notifyWidgetSizeChanged(this);
      this.mWorkspace.addInScreen(localLauncherAppWidgetInfo.hostView, paramLong1, paramLong2, arrayOfInt3[0], arrayOfInt3[1], localLauncherAppWidgetInfo.spanX, localLauncherAppWidgetInfo.spanY, isWorkspaceLocked());
      addWidgetToAutoAdvanceIfNeeded(localLauncherAppWidgetInfo.hostView, paramAppWidgetProviderInfo);
      resetAddInfo();
      return;
      label448:
      localLauncherAppWidgetInfo.hostView = paramAppWidgetHostView;
    }
  }
  
  private void completeAddShortcut(Intent paramIntent, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt1 = this.mTmpAddItemCellCoordinates;
    int[] arrayOfInt2 = this.mPendingAddInfo.dropPos;
    CellLayout localCellLayout = getCellLayout(paramLong1, paramLong2);
    ShortcutInfo localShortcutInfo = this.mModel.infoFromShortcutIntent(this, paramIntent, null);
    if (localShortcutInfo == null) {}
    View localView;
    do
    {
      boolean bool;
      DropTarget.DragObject localDragObject;
      do
      {
        do
        {
          return;
          localView = createShortcut(localShortcutInfo);
          if ((paramInt1 < 0) || (paramInt2 < 0)) {
            break;
          }
          arrayOfInt1[0] = paramInt1;
          arrayOfInt1[1] = paramInt2;
          bool = true;
        } while (this.mWorkspace.createUserFolderIfNecessary(localView, paramLong1, localCellLayout, arrayOfInt1, 0.0F, true, null, null));
        localDragObject = new DropTarget.DragObject();
        localDragObject.dragInfo = localShortcutInfo;
      } while (this.mWorkspace.addToExistingFolderIfNecessary(localView, localCellLayout, arrayOfInt1, 0.0F, localDragObject, true));
      while (!bool)
      {
        showOutOfSpaceMessage(isHotseatLayout(localCellLayout));
        return;
        if (arrayOfInt2 != null)
        {
          if (localCellLayout.findNearestVacantArea(arrayOfInt2[0], arrayOfInt2[1], 1, 1, arrayOfInt1) != null) {}
          for (bool = true;; bool = false) {
            break;
          }
        }
        bool = localCellLayout.findCellForSpan(arrayOfInt1, 1, 1);
      }
      LauncherModel.addItemToDatabase(this, localShortcutInfo, paramLong1, paramLong2, arrayOfInt1[0], arrayOfInt1[1], false);
    } while (this.mRestoring);
    this.mWorkspace.addInScreen(localView, paramLong1, paramLong2, arrayOfInt1[0], arrayOfInt1[1], 1, 1, isWorkspaceLocked());
  }
  
  private void completeTwoStageWidgetDrop(final int paramInt1, final int paramInt2)
  {
    CellLayout localCellLayout = this.mWorkspace.getScreenWithId(this.mPendingAddInfo.screenId);
    int i;
    AppWidgetHostView localAppWidgetHostView1;
    Runnable local7;
    if (paramInt1 == -1)
    {
      i = 3;
      final AppWidgetHostView localAppWidgetHostView2 = this.mAppWidgetHost.createView(this, paramInt2, this.mPendingAddWidgetInfo);
      localAppWidgetHostView1 = localAppWidgetHostView2;
      local7 = new Runnable()
      {
        public void run()
        {
          Launcher.this.completeAddAppWidget(paramInt2, Launcher.this.mPendingAddInfo.container, Launcher.this.mPendingAddInfo.screenId, localAppWidgetHostView2, null);
          Launcher localLauncher = Launcher.this;
          if (paramInt1 != 0) {}
          for (boolean bool = true;; bool = false)
          {
            localLauncher.exitSpringLoadedDragModeDelayed(bool, 300, null);
            return;
          }
        }
      };
      if (this.mDragLayer.getAnimatedView() == null) {
        break label129;
      }
      this.mWorkspace.animateWidgetDrop(this.mPendingAddInfo, localCellLayout, (DragView)this.mDragLayer.getAnimatedView(), local7, i, localAppWidgetHostView1, true);
    }
    label129:
    while (local7 == null)
    {
      return;
      local7 = null;
      i = 0;
      localAppWidgetHostView1 = null;
      if (paramInt1 != 0) {
        break;
      }
      this.mAppWidgetHost.deleteAppWidgetId(paramInt2);
      i = 4;
      local7 = null;
      localAppWidgetHostView1 = null;
      break;
    }
    local7.run();
  }
  
  private void copyFolderIconToImage(FolderIcon paramFolderIcon)
  {
    int i = paramFolderIcon.getMeasuredWidth();
    int j = paramFolderIcon.getMeasuredHeight();
    if (this.mFolderIconImageView == null) {
      this.mFolderIconImageView = new ImageView(this);
    }
    if ((this.mFolderIconBitmap == null) || (this.mFolderIconBitmap.getWidth() != i) || (this.mFolderIconBitmap.getHeight() != j))
    {
      this.mFolderIconBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      this.mFolderIconCanvas = new Canvas(this.mFolderIconBitmap);
    }
    if ((this.mFolderIconImageView.getLayoutParams() instanceof DragLayer.LayoutParams)) {}
    for (DragLayer.LayoutParams localLayoutParams = (DragLayer.LayoutParams)this.mFolderIconImageView.getLayoutParams();; localLayoutParams = new DragLayer.LayoutParams(i, j))
    {
      float f = this.mDragLayer.getDescendantRectRelativeToSelf(paramFolderIcon, this.mRectForFolderAnimation);
      localLayoutParams.customPosition = true;
      localLayoutParams.x = this.mRectForFolderAnimation.left;
      localLayoutParams.y = this.mRectForFolderAnimation.top;
      localLayoutParams.width = ((int)(f * i));
      localLayoutParams.height = ((int)(f * j));
      this.mFolderIconCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      paramFolderIcon.draw(this.mFolderIconCanvas);
      this.mFolderIconImageView.setImageBitmap(this.mFolderIconBitmap);
      if (paramFolderIcon.getFolder() != null)
      {
        this.mFolderIconImageView.setPivotX(paramFolderIcon.getFolder().getPivotXForIconAnimation());
        this.mFolderIconImageView.setPivotY(paramFolderIcon.getFolder().getPivotYForIconAnimation());
      }
      if (this.mDragLayer.indexOfChild(this.mFolderIconImageView) != -1) {
        this.mDragLayer.removeView(this.mFolderIconImageView);
      }
      this.mDragLayer.addView(this.mFolderIconImageView, localLayoutParams);
      if (paramFolderIcon.getFolder() != null) {
        paramFolderIcon.getFolder().bringToFront();
      }
      return;
    }
  }
  
  private ValueAnimator createNewAppBounceAnimation(View paramView, int paramInt)
  {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder = new PropertyValuesHolder[3];
    arrayOfPropertyValuesHolder[0] = PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0F });
    arrayOfPropertyValuesHolder[1] = PropertyValuesHolder.ofFloat("scaleX", new float[] { 1.0F });
    arrayOfPropertyValuesHolder[2] = PropertyValuesHolder.ofFloat("scaleY", new float[] { 1.0F });
    ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofPropertyValuesHolder(paramView, arrayOfPropertyValuesHolder);
    localObjectAnimator.setDuration(450L);
    localObjectAnimator.setStartDelay(paramInt * 85);
    localObjectAnimator.setInterpolator(new SmoothPagedView.OvershootInterpolator());
    return localObjectAnimator;
  }
  
  private void dismissCling(final Cling paramCling, final Runnable paramRunnable, final String paramString, int paramInt, boolean paramBoolean)
  {
    Runnable local38;
    if ((paramCling != null) && (paramCling.getVisibility() != 8))
    {
      local38 = new Runnable()
      {
        public void run()
        {
          paramCling.cleanup();
          new Thread("dismissClingThread")
          {
            public void run()
            {
              SharedPreferences.Editor localEditor = Launcher.this.mSharedPrefs.edit();
              localEditor.putBoolean(Launcher.38.this.val$flag, true);
              localEditor.commit();
            }
          }.start();
          if (paramRunnable != null) {
            paramRunnable.run();
          }
        }
      };
      if (paramInt > 0) {
        break label66;
      }
      local38.run();
    }
    for (;;)
    {
      this.mHideFromAccessibilityHelper.restoreImportantForAccessibility(this.mDragLayer);
      if (paramBoolean) {
        paramCling.setSystemUiVisibility(0xFFFFFFFE & paramCling.getSystemUiVisibility());
      }
      return;
      label66:
      paramCling.hide(paramInt, local38);
    }
  }
  
  private void dispatchOnLauncherTransitionEnd(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramView instanceof LauncherTransitionable)) {
      ((LauncherTransitionable)paramView).onLauncherTransitionEnd(this, paramBoolean1, paramBoolean2);
    }
    dispatchOnLauncherTransitionStep(paramView, 1.0F);
  }
  
  private void dispatchOnLauncherTransitionPrepare(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramView instanceof LauncherTransitionable)) {
      ((LauncherTransitionable)paramView).onLauncherTransitionPrepare(this, paramBoolean1, paramBoolean2);
    }
  }
  
  private void dispatchOnLauncherTransitionStart(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramView instanceof LauncherTransitionable)) {
      ((LauncherTransitionable)paramView).onLauncherTransitionStart(this, paramBoolean1, paramBoolean2);
    }
    dispatchOnLauncherTransitionStep(paramView, 0.0F);
  }
  
  private void dispatchOnLauncherTransitionStep(View paramView, float paramFloat)
  {
    if ((paramView instanceof LauncherTransitionable)) {
      ((LauncherTransitionable)paramView).onLauncherTransitionStep(this, paramFloat);
    }
  }
  
  private int getCurrentOrientationIndexForGlobalIcons()
  {
    switch (getResources().getConfiguration().orientation)
    {
    default: 
      return 0;
    }
    return 1;
  }
  
  private Drawable getExternalPackageToolbarIcon(ComponentName paramComponentName, String paramString)
  {
    try
    {
      PackageManager localPackageManager = getPackageManager();
      Bundle localBundle = localPackageManager.getActivityInfo(paramComponentName, 128).metaData;
      if (localBundle != null)
      {
        int i = localBundle.getInt(paramString);
        if (i != 0)
        {
          Drawable localDrawable = localPackageManager.getResourcesForActivity(paramComponentName).getDrawable(i);
          return localDrawable;
        }
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.w("Launcher", "Failed to load toolbar icon; " + paramComponentName.flattenToShortString() + " not found", localNameNotFoundException);
      return null;
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      for (;;)
      {
        Log.w("Launcher", "Failed to load toolbar icon from " + paramComponentName.flattenToShortString(), localNotFoundException);
      }
    }
  }
  
  static int[] getMinSpanForWidget(Context paramContext, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    return getSpanForWidget(paramContext, paramAppWidgetProviderInfo.provider, paramAppWidgetProviderInfo.minResizeWidth, paramAppWidgetProviderInfo.minResizeHeight);
  }
  
  static int[] getSpanForWidget(Context paramContext, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    return getSpanForWidget(paramContext, paramAppWidgetProviderInfo.provider, paramAppWidgetProviderInfo.minWidth, paramAppWidgetProviderInfo.minHeight);
  }
  
  static int[] getSpanForWidget(Context paramContext, ComponentName paramComponentName, int paramInt1, int paramInt2)
  {
    Rect localRect = AppWidgetHostView.getDefaultPaddingForWidget(paramContext, paramComponentName, null);
    return CellLayout.rectToCell(paramInt1 + localRect.left + localRect.right, paramInt2 + localRect.top + localRect.bottom, null);
  }
  
  private String getTypedText()
  {
    return this.mDefaultKeySsb.toString();
  }
  
  private void growAndFadeOutFolderIcon(FolderIcon paramFolderIcon)
  {
    if (paramFolderIcon == null) {
      return;
    }
    PropertyValuesHolder localPropertyValuesHolder1 = PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0F });
    PropertyValuesHolder localPropertyValuesHolder2 = PropertyValuesHolder.ofFloat("scaleX", new float[] { 1.5F });
    PropertyValuesHolder localPropertyValuesHolder3 = PropertyValuesHolder.ofFloat("scaleY", new float[] { 1.5F });
    if (((FolderInfo)paramFolderIcon.getTag()).container == -101L)
    {
      CellLayout localCellLayout = (CellLayout)paramFolderIcon.getParent().getParent();
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)paramFolderIcon.getLayoutParams();
      localCellLayout.setFolderLeaveBehindCell(localLayoutParams.cellX, localLayoutParams.cellY);
    }
    copyFolderIconToImage(paramFolderIcon);
    paramFolderIcon.setVisibility(4);
    ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofPropertyValuesHolder(this.mFolderIconImageView, new PropertyValuesHolder[] { localPropertyValuesHolder1, localPropertyValuesHolder2, localPropertyValuesHolder3 });
    localObjectAnimator.setDuration(getResources().getInteger(2131427355));
    localObjectAnimator.start();
  }
  
  private void handleFolderClick(FolderIcon paramFolderIcon)
  {
    FolderInfo localFolderInfo = paramFolderIcon.getFolderInfo();
    Folder localFolder = this.mWorkspace.getFolderForTag(localFolderInfo);
    if ((localFolderInfo.opened) && (localFolder == null))
    {
      Log.d("Launcher", "Folder info marked as open, but associated folder is not open. Screen: " + localFolderInfo.screenId + " (" + localFolderInfo.cellX + ", " + localFolderInfo.cellY + ")");
      localFolderInfo.opened = false;
    }
    if ((!localFolderInfo.opened) && (!paramFolderIcon.getFolder().isDestroyed()))
    {
      closeFolder();
      openFolder(paramFolderIcon);
    }
    int i;
    do
    {
      do
      {
        return;
      } while (localFolder == null);
      i = this.mWorkspace.getPageForView(localFolder);
      closeFolder(localFolder);
    } while (i == this.mWorkspace.getCurrentPage());
    closeFolder();
    openFolder(paramFolderIcon);
  }
  
  private void hideAppsCustomizeHelper(Workspace.State paramState, final boolean paramBoolean1, boolean paramBoolean2, final Runnable paramRunnable)
  {
    if (this.mStateAnimation != null)
    {
      this.mStateAnimation.setDuration(0L);
      this.mStateAnimation.cancel();
      this.mStateAnimation = null;
    }
    Resources localResources = getResources();
    int i = localResources.getInteger(2131427340);
    int j = localResources.getInteger(2131427343);
    float f = localResources.getInteger(2131427341);
    final AppsCustomizeTabHost localAppsCustomizeTabHost = this.mAppsCustomizeTabHost;
    final Workspace localWorkspace = this.mWorkspace;
    Animator localAnimator;
    if (paramState == Workspace.State.NORMAL)
    {
      int k = localResources.getInteger(2131427345);
      localAnimator = this.mWorkspace.getChangeStateAnimation(paramState, paramBoolean1, k, -1);
    }
    for (;;)
    {
      setPivotsForZoom(localAppsCustomizeTabHost, f);
      showHotseat(paramBoolean1);
      if (!paramBoolean1) {
        break;
      }
      LauncherViewPropertyAnimator localLauncherViewPropertyAnimator = new LauncherViewPropertyAnimator(localAppsCustomizeTabHost);
      localLauncherViewPropertyAnimator.scaleX(f).scaleY(f).setDuration(i).setInterpolator(new Workspace.ZoomInInterpolator());
      ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofFloat(localAppsCustomizeTabHost, "alpha", new float[] { 1.0F, 0.0F }).setDuration(j);
      localObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          float f = 1.0F - ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          Launcher.this.dispatchOnLauncherTransitionStep(localAppsCustomizeTabHost, f);
          Launcher.this.dispatchOnLauncherTransitionStep(localWorkspace, f);
        }
      });
      this.mStateAnimation = LauncherAnimUtils.createAnimatorSet();
      dispatchOnLauncherTransitionPrepare(localAppsCustomizeTabHost, paramBoolean1, true);
      dispatchOnLauncherTransitionPrepare(localWorkspace, paramBoolean1, true);
      this.mAppsCustomizeContent.stopScrolling();
      this.mStateAnimation.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          localAppsCustomizeTabHost.setVisibility(8);
          Launcher.this.dispatchOnLauncherTransitionEnd(localAppsCustomizeTabHost, paramBoolean1, true);
          Launcher.this.dispatchOnLauncherTransitionEnd(localWorkspace, paramBoolean1, true);
          if (paramRunnable != null) {
            paramRunnable.run();
          }
          Launcher.this.mAppsCustomizeContent.updateCurrentPageScroll();
        }
      });
      this.mStateAnimation.playTogether(new Animator[] { localLauncherViewPropertyAnimator, localObjectAnimator });
      if (localAnimator != null) {
        this.mStateAnimation.play(localAnimator);
      }
      dispatchOnLauncherTransitionStart(localAppsCustomizeTabHost, paramBoolean1, true);
      dispatchOnLauncherTransitionStart(localWorkspace, paramBoolean1, true);
      LauncherAnimUtils.startAnimationAfterNextDraw(this.mStateAnimation, localWorkspace);
      return;
      if (paramState != Workspace.State.SPRING_LOADED)
      {
        Workspace.State localState = Workspace.State.OVERVIEW;
        localAnimator = null;
        if (paramState != localState) {}
      }
      else
      {
        localAnimator = this.mWorkspace.getChangeStateAnimation(paramState, paramBoolean1);
      }
    }
    localAppsCustomizeTabHost.setVisibility(8);
    dispatchOnLauncherTransitionPrepare(localAppsCustomizeTabHost, paramBoolean1, true);
    dispatchOnLauncherTransitionStart(localAppsCustomizeTabHost, paramBoolean1, true);
    dispatchOnLauncherTransitionEnd(localAppsCustomizeTabHost, paramBoolean1, true);
    dispatchOnLauncherTransitionPrepare(localWorkspace, paramBoolean1, true);
    dispatchOnLauncherTransitionStart(localWorkspace, paramBoolean1, true);
    dispatchOnLauncherTransitionEnd(localWorkspace, paramBoolean1, true);
  }
  
  private Cling initCling(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    Cling localCling = (Cling)findViewById(paramInt1);
    View localView = null;
    if (paramInt2 > 0) {
      localView = findViewById(2131296750);
    }
    if (localCling != null)
    {
      localCling.init(this, localView);
      localCling.show(paramBoolean1, 250);
      if (paramBoolean2) {
        localCling.setSystemUiVisibility(0x1 | localCling.getSystemUiVisibility());
      }
    }
    return localCling;
  }
  
  private static State intToState(int paramInt)
  {
    State localState = State.WORKSPACE;
    State[] arrayOfState = State.values();
    for (int i = 0;; i++) {
      if (i < arrayOfState.length)
      {
        if (arrayOfState[i].ordinal() == paramInt) {
          localState = arrayOfState[i];
        }
      }
      else {
        return localState;
      }
    }
  }
  
  private void invalidatePressedFocusedStates(View paramView1, View paramView2)
  {
    if ((paramView1 instanceof HolographicLinearLayout)) {
      ((HolographicLinearLayout)paramView1).invalidatePressedFocusedStates();
    }
    while (!(paramView2 instanceof HolographicImageView)) {
      return;
    }
    ((HolographicImageView)paramView2).invalidatePressedFocusedStates();
  }
  
  private boolean isClingsEnabled()
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    if (localDeviceProfile.isTablet()) {}
    while ((localDeviceProfile.isLandscape) || (ActivityManager.isRunningInTestHarness()) || (((AccessibilityManager)getSystemService("accessibility")).isTouchExplorationEnabled())) {
      return false;
    }
    return true;
  }
  
  private static boolean isPropertyEnabled(String paramString)
  {
    return Log.isLoggable(paramString, 2);
  }
  
  private int mapConfigurationOriActivityInfoOri(int paramInt)
  {
    Display localDisplay = getWindowManager().getDefaultDisplay();
    int i = 2;
    switch (localDisplay.getRotation())
    {
    default: 
    case 0: 
    case 2: 
      for (;;)
      {
        int[] arrayOfInt = { 1, 0, 9, 8 };
        int j = 0;
        if (i == 2) {
          j = 1;
        }
        return arrayOfInt[((j + localDisplay.getRotation()) % 4)];
        i = paramInt;
      }
    }
    if (paramInt == 2) {}
    for (i = 1;; i = 2) {
      break;
    }
  }
  
  private void onAppWidgetReset()
  {
    if (this.mAppWidgetHost != null) {
      this.mAppWidgetHost.startListening();
    }
  }
  
  /* Error */
  private static void readConfiguration(Context paramContext, LocaleConfiguration paramLocaleConfiguration)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new 1377	java/io/DataInputStream
    //   5: dup
    //   6: aload_0
    //   7: ldc_w 1379
    //   10: invokevirtual 1385	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   13: invokespecial 1388	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   16: astore_3
    //   17: aload_1
    //   18: aload_3
    //   19: invokevirtual 1391	java/io/DataInputStream:readUTF	()Ljava/lang/String;
    //   22: putfield 500	com/android/launcher3/Launcher$LocaleConfiguration:locale	Ljava/lang/String;
    //   25: aload_1
    //   26: aload_3
    //   27: invokevirtual 1394	java/io/DataInputStream:readInt	()I
    //   30: putfield 514	com/android/launcher3/Launcher$LocaleConfiguration:mcc	I
    //   33: aload_1
    //   34: aload_3
    //   35: invokevirtual 1394	java/io/DataInputStream:readInt	()I
    //   38: putfield 518	com/android/launcher3/Launcher$LocaleConfiguration:mnc	I
    //   41: aload_3
    //   42: ifnull +78 -> 120
    //   45: aload_3
    //   46: invokevirtual 1397	java/io/DataInputStream:close	()V
    //   49: return
    //   50: astore 10
    //   52: return
    //   53: astore 12
    //   55: aload_2
    //   56: ifnull -7 -> 49
    //   59: aload_2
    //   60: invokevirtual 1397	java/io/DataInputStream:close	()V
    //   63: return
    //   64: astore 5
    //   66: return
    //   67: astore 11
    //   69: aload_2
    //   70: ifnull -21 -> 49
    //   73: aload_2
    //   74: invokevirtual 1397	java/io/DataInputStream:close	()V
    //   77: return
    //   78: astore 7
    //   80: return
    //   81: astore 8
    //   83: aload_2
    //   84: ifnull +7 -> 91
    //   87: aload_2
    //   88: invokevirtual 1397	java/io/DataInputStream:close	()V
    //   91: aload 8
    //   93: athrow
    //   94: astore 9
    //   96: goto -5 -> 91
    //   99: astore 8
    //   101: aload_3
    //   102: astore_2
    //   103: goto -20 -> 83
    //   106: astore 6
    //   108: aload_3
    //   109: astore_2
    //   110: goto -41 -> 69
    //   113: astore 4
    //   115: aload_3
    //   116: astore_2
    //   117: goto -62 -> 55
    //   120: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	paramContext	Context
    //   0	121	1	paramLocaleConfiguration	LocaleConfiguration
    //   1	116	2	localObject1	Object
    //   16	100	3	localDataInputStream	java.io.DataInputStream
    //   113	1	4	localFileNotFoundException1	java.io.FileNotFoundException
    //   64	1	5	localIOException1	java.io.IOException
    //   106	1	6	localIOException2	java.io.IOException
    //   78	1	7	localIOException3	java.io.IOException
    //   81	11	8	localObject2	Object
    //   99	1	8	localObject3	Object
    //   94	1	9	localIOException4	java.io.IOException
    //   50	1	10	localIOException5	java.io.IOException
    //   67	1	11	localIOException6	java.io.IOException
    //   53	1	12	localFileNotFoundException2	java.io.FileNotFoundException
    // Exception table:
    //   from	to	target	type
    //   45	49	50	java/io/IOException
    //   2	17	53	java/io/FileNotFoundException
    //   59	63	64	java/io/IOException
    //   2	17	67	java/io/IOException
    //   73	77	78	java/io/IOException
    //   2	17	81	finally
    //   87	91	94	java/io/IOException
    //   17	41	99	finally
    //   17	41	106	java/io/IOException
    //   17	41	113	java/io/FileNotFoundException
  }
  
  private void registerContentObservers()
  {
    getContentResolver().registerContentObserver(LauncherProvider.CONTENT_APPWIDGET_RESET_URI, true, this.mWidgetObserver);
  }
  
  private void removeCling(int paramInt)
  {
    final View localView = findViewById(paramInt);
    if (localView != null)
    {
      final ViewGroup localViewGroup = (ViewGroup)localView.getParent();
      localViewGroup.post(new Runnable()
      {
        public void run()
        {
          localViewGroup.removeView(localView);
        }
      });
      this.mHideFromAccessibilityHelper.restoreImportantForAccessibility(this.mDragLayer);
    }
  }
  
  private void resetAddInfo()
  {
    this.mPendingAddInfo.container = -1L;
    this.mPendingAddInfo.screenId = -1L;
    ItemInfo localItemInfo1 = this.mPendingAddInfo;
    this.mPendingAddInfo.cellY = -1;
    localItemInfo1.cellX = -1;
    ItemInfo localItemInfo2 = this.mPendingAddInfo;
    this.mPendingAddInfo.spanY = -1;
    localItemInfo2.spanX = -1;
    ItemInfo localItemInfo3 = this.mPendingAddInfo;
    this.mPendingAddInfo.minSpanY = -1;
    localItemInfo3.minSpanX = -1;
    this.mPendingAddInfo.dropPos = null;
  }
  
  private void restoreState(Bundle paramBundle)
  {
    if (paramBundle == null) {}
    do
    {
      return;
      if (intToState(paramBundle.getInt("launcher.state", State.WORKSPACE.ordinal())) == State.APPS_CUSTOMIZE) {
        this.mOnResumeState = State.APPS_CUSTOMIZE;
      }
      int i = paramBundle.getInt("launcher.current_screen", -1001);
      if (i != -1001) {
        this.mWorkspace.setRestorePage(i);
      }
      long l1 = paramBundle.getLong("launcher.add_container", -1L);
      long l2 = paramBundle.getLong("launcher.add_screen", -1L);
      if ((l1 != -1L) && (l2 > -1L))
      {
        this.mPendingAddInfo.container = l1;
        this.mPendingAddInfo.screenId = l2;
        this.mPendingAddInfo.cellX = paramBundle.getInt("launcher.add_cell_x");
        this.mPendingAddInfo.cellY = paramBundle.getInt("launcher.add_cell_y");
        this.mPendingAddInfo.spanX = paramBundle.getInt("launcher.add_span_x");
        this.mPendingAddInfo.spanY = paramBundle.getInt("launcher.add_span_y");
        this.mPendingAddWidgetInfo = ((AppWidgetProviderInfo)paramBundle.getParcelable("launcher.add_widget_info"));
        this.mPendingAddWidgetId = paramBundle.getInt("launcher.add_widget_id");
        this.mWaitingForResult = true;
        this.mRestoring = true;
      }
      if (paramBundle.getBoolean("launcher.rename_folder", false))
      {
        long l3 = paramBundle.getLong("launcher.rename_folder_id");
        this.mFolderInfo = this.mModel.getFolderById(this, sFolders, l3);
        this.mRestoring = true;
      }
    } while (this.mAppsCustomizeTabHost == null);
    String str = paramBundle.getString("apps_customize_currentTab");
    if (str != null)
    {
      this.mAppsCustomizeTabHost.setContentTypeImmediate(this.mAppsCustomizeTabHost.getContentTypeForTabTag(str));
      this.mAppsCustomizeContent.loadAssociatedPages(this.mAppsCustomizeContent.getCurrentPage());
    }
    int j = paramBundle.getInt("apps_customize_currentIndex");
    this.mAppsCustomizeContent.restorePageForIndex(j);
  }
  
  private void sendAdvanceMessage(long paramLong)
  {
    this.mHandler.removeMessages(1);
    Message localMessage = this.mHandler.obtainMessage(1);
    this.mHandler.sendMessageDelayed(localMessage, paramLong);
    this.mAutoAdvanceSentTime = System.currentTimeMillis();
  }
  
  private void setCustomContentHintVisibility(Cling paramCling, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    final TextView localTextView = (TextView)paramCling.findViewById(2131296594);
    if (localTextView != null)
    {
      if ((!paramBoolean1) || (paramString.isEmpty())) {
        break label77;
      }
      localTextView.setText(paramString);
      localTextView.setVisibility(0);
      if (paramBoolean2)
      {
        localTextView.setAlpha(0.0F);
        localTextView.animate().alpha(1.0F).setDuration(250L).start();
      }
    }
    else
    {
      return;
    }
    localTextView.setAlpha(1.0F);
    return;
    label77:
    if (paramBoolean2)
    {
      localTextView.animate().alpha(0.0F).setDuration(250L).setListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          localTextView.setVisibility(8);
        }
      }).start();
      return;
    }
    localTextView.setAlpha(0.0F);
    localTextView.setVisibility(8);
  }
  
  private void setPivotsForZoom(View paramView, float paramFloat)
  {
    paramView.setPivotX(paramView.getWidth() / 2.0F);
    paramView.setPivotY(paramView.getHeight() / 2.0F);
  }
  
  static void setScreen(int paramInt)
  {
    synchronized (sLock)
    {
      sScreen = paramInt;
      return;
    }
  }
  
  private void setWorkspaceBackground(boolean paramBoolean)
  {
    View localView = this.mLauncherView;
    if (paramBoolean) {}
    for (Drawable localDrawable = this.mWorkspaceBackgroundDrawable;; localDrawable = null)
    {
      localView.setBackground(localDrawable);
      return;
    }
  }
  
  private void setupViews()
  {
    DragController localDragController = this.mDragController;
    this.mLauncherView = findViewById(2131296744);
    this.mDragLayer = ((DragLayer)findViewById(2131296745));
    this.mWorkspace = ((Workspace)this.mDragLayer.findViewById(2131296746));
    this.mLauncherView.setSystemUiVisibility(1536);
    this.mWorkspaceBackgroundDrawable = getResources().getDrawable(2130838131);
    this.mDragLayer.setup(this, localDragController);
    this.mHotseat = ((Hotseat)findViewById(2131296747));
    if (this.mHotseat != null)
    {
      this.mHotseat.setup(this);
      this.mHotseat.setOnLongClickListener(this);
    }
    this.mOverviewPanel = findViewById(2131296749);
    View localView1 = findViewById(2131296827);
    localView1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Launcher.this.showAllApps(true, AppsCustomizePagedView.ContentType.Widgets, true);
      }
    });
    localView1.setOnTouchListener(getHapticFeedbackTouchListener());
    View localView2 = findViewById(2131296826);
    localView2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Launcher.this.startWallpaper();
      }
    });
    localView2.setOnTouchListener(getHapticFeedbackTouchListener());
    View localView3 = findViewById(2131296828);
    localView3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Launcher.this.startSettings();
      }
    });
    localView3.setOnTouchListener(getHapticFeedbackTouchListener());
    this.mOverviewPanel.setAlpha(0.0F);
    this.mWorkspace.setHapticFeedbackEnabled(false);
    this.mWorkspace.setOnLongClickListener(this);
    this.mWorkspace.setup(localDragController);
    localDragController.addDragListener(this.mWorkspace);
    this.mSearchDropTargetBar = ((SearchDropTargetBar)this.mDragLayer.findViewById(2131296748));
    this.mAppsCustomizeTabHost = ((AppsCustomizeTabHost)findViewById(2131296754));
    this.mAppsCustomizeContent = ((AppsCustomizePagedView)this.mAppsCustomizeTabHost.findViewById(2131296342));
    this.mAppsCustomizeContent.setup(this, localDragController);
    localDragController.setDragScoller(this.mWorkspace);
    localDragController.setScrollView(this.mDragLayer);
    localDragController.setMoveTarget(this.mWorkspace);
    localDragController.addDropTarget(this.mWorkspace);
    if (this.mSearchDropTargetBar != null) {
      this.mSearchDropTargetBar.setup(this, localDragController);
    }
    View localView4;
    if (getResources().getBoolean(2131755016))
    {
      Log.v("Launcher", "adding WeightWatcher");
      this.mWeightWatcher = new WeightWatcher(this);
      this.mWeightWatcher.setAlpha(0.5F);
      ((FrameLayout)this.mLauncherView).addView(this.mWeightWatcher, new FrameLayout.LayoutParams(-1, -2, 80));
      boolean bool = shouldShowWeightWatcher();
      localView4 = this.mWeightWatcher;
      if (!bool) {
        break label459;
      }
    }
    label459:
    for (int i = 0;; i = 8)
    {
      localView4.setVisibility(i);
      return;
    }
  }
  
  private boolean shouldShowWeightWatcher()
  {
    return getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0).getBoolean("debug.show_mem", false);
  }
  
  private void showAppsCustomizeHelper(boolean paramBoolean1, boolean paramBoolean2)
  {
    showAppsCustomizeHelper(paramBoolean1, paramBoolean2, this.mAppsCustomizeContent.getContentType());
  }
  
  private void showAppsCustomizeHelper(final boolean paramBoolean1, boolean paramBoolean2, AppsCustomizePagedView.ContentType paramContentType)
  {
    if (this.mStateAnimation != null)
    {
      this.mStateAnimation.setDuration(0L);
      this.mStateAnimation.cancel();
      this.mStateAnimation = null;
    }
    Resources localResources = getResources();
    int i = localResources.getInteger(2131427339);
    int j = localResources.getInteger(2131427342);
    final float f = localResources.getInteger(2131427341);
    final Workspace localWorkspace = this.mWorkspace;
    final AppsCustomizeTabHost localAppsCustomizeTabHost = this.mAppsCustomizeTabHost;
    int k = localResources.getInteger(2131427346);
    setPivotsForZoom(localAppsCustomizeTabHost, f);
    Animator localAnimator = this.mWorkspace.getChangeStateAnimation(Workspace.State.SMALL, paramBoolean1);
    if (!AppsCustomizePagedView.DISABLE_ALL_APPS) {
      this.mAppsCustomizeTabHost.setContentTypeImmediate(paramContentType);
    }
    if (paramBoolean1)
    {
      localAppsCustomizeTabHost.setScaleX(f);
      localAppsCustomizeTabHost.setScaleY(f);
      LauncherViewPropertyAnimator localLauncherViewPropertyAnimator = new LauncherViewPropertyAnimator(localAppsCustomizeTabHost);
      localLauncherViewPropertyAnimator.scaleX(1.0F).scaleY(1.0F).setDuration(i).setInterpolator(new Workspace.ZoomOutInterpolator());
      localAppsCustomizeTabHost.setVisibility(0);
      localAppsCustomizeTabHost.setAlpha(0.0F);
      ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofFloat(localAppsCustomizeTabHost, "alpha", new float[] { 0.0F, 1.0F }).setDuration(j);
      localObjectAnimator.setInterpolator(new DecelerateInterpolator(1.5F));
      localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          if (paramAnonymousValueAnimator == null) {
            throw new RuntimeException("animation is null");
          }
          float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
          Launcher.this.dispatchOnLauncherTransitionStep(localWorkspace, f);
          Launcher.this.dispatchOnLauncherTransitionStep(localAppsCustomizeTabHost, f);
        }
      });
      this.mStateAnimation = LauncherAnimUtils.createAnimatorSet();
      this.mStateAnimation.play(localLauncherViewPropertyAnimator).after(k);
      this.mStateAnimation.play(localObjectAnimator).after(k);
      this.mStateAnimation.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          Launcher.this.dispatchOnLauncherTransitionEnd(localWorkspace, paramBoolean1, false);
          Launcher.this.dispatchOnLauncherTransitionEnd(localAppsCustomizeTabHost, paramBoolean1, false);
          if (Launcher.this.mSearchDropTargetBar != null) {
            Launcher.this.mSearchDropTargetBar.hideSearchBar(false);
          }
        }
        
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          localAppsCustomizeTabHost.setTranslationX(0.0F);
          localAppsCustomizeTabHost.setTranslationY(0.0F);
          localAppsCustomizeTabHost.setVisibility(0);
          localAppsCustomizeTabHost.bringToFront();
        }
      });
      if (localAnimator != null) {
        this.mStateAnimation.play(localAnimator);
      }
      dispatchOnLauncherTransitionPrepare(localWorkspace, paramBoolean1, false);
      dispatchOnLauncherTransitionPrepare(localAppsCustomizeTabHost, paramBoolean1, false);
      int m;
      if ((localAppsCustomizeTabHost.getContent().getMeasuredWidth() != 0) && (this.mWorkspace.getMeasuredWidth() != 0))
      {
        int n = localAppsCustomizeTabHost.getMeasuredWidth();
        m = 0;
        if (n != 0) {}
      }
      else
      {
        m = 1;
      }
      final Runnable local21 = new Runnable()
      {
        public void run()
        {
          if (Launcher.this.mStateAnimation != this.val$stateAnimation) {
            return;
          }
          Launcher.this.setPivotsForZoom(localAppsCustomizeTabHost, f);
          Launcher.this.dispatchOnLauncherTransitionStart(localWorkspace, paramBoolean1, false);
          Launcher.this.dispatchOnLauncherTransitionStart(localAppsCustomizeTabHost, paramBoolean1, false);
          LauncherAnimUtils.startAnimationAfterNextDraw(Launcher.this.mStateAnimation, localAppsCustomizeTabHost);
        }
      };
      if (m != 0)
      {
        localAppsCustomizeTabHost.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            local21.run();
            localAppsCustomizeTabHost.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        });
        return;
      }
      local21.run();
      return;
    }
    localAppsCustomizeTabHost.setTranslationX(0.0F);
    localAppsCustomizeTabHost.setTranslationY(0.0F);
    localAppsCustomizeTabHost.setScaleX(1.0F);
    localAppsCustomizeTabHost.setScaleY(1.0F);
    localAppsCustomizeTabHost.setVisibility(0);
    localAppsCustomizeTabHost.bringToFront();
    if ((!paramBoolean2) && (!LauncherAppState.getInstance().isScreenLarge()) && (this.mSearchDropTargetBar != null)) {
      this.mSearchDropTargetBar.hideSearchBar(false);
    }
    dispatchOnLauncherTransitionPrepare(localWorkspace, paramBoolean1, false);
    dispatchOnLauncherTransitionStart(localWorkspace, paramBoolean1, false);
    dispatchOnLauncherTransitionEnd(localWorkspace, paramBoolean1, false);
    dispatchOnLauncherTransitionPrepare(localAppsCustomizeTabHost, paramBoolean1, false);
    dispatchOnLauncherTransitionStart(localAppsCustomizeTabHost, paramBoolean1, false);
    dispatchOnLauncherTransitionEnd(localAppsCustomizeTabHost, paramBoolean1, false);
  }
  
  private void shrinkAndFadeInFolderIcon(final FolderIcon paramFolderIcon)
  {
    if (paramFolderIcon == null) {
      return;
    }
    PropertyValuesHolder localPropertyValuesHolder1 = PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0F });
    PropertyValuesHolder localPropertyValuesHolder2 = PropertyValuesHolder.ofFloat("scaleX", new float[] { 1.0F });
    PropertyValuesHolder localPropertyValuesHolder3 = PropertyValuesHolder.ofFloat("scaleY", new float[] { 1.0F });
    final CellLayout localCellLayout = (CellLayout)paramFolderIcon.getParent().getParent();
    this.mDragLayer.removeView(this.mFolderIconImageView);
    copyFolderIconToImage(paramFolderIcon);
    ObjectAnimator localObjectAnimator = LauncherAnimUtils.ofPropertyValuesHolder(this.mFolderIconImageView, new PropertyValuesHolder[] { localPropertyValuesHolder1, localPropertyValuesHolder2, localPropertyValuesHolder3 });
    localObjectAnimator.setDuration(getResources().getInteger(2131427355));
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (localCellLayout != null)
        {
          localCellLayout.clearFolderLeaveBehind();
          Launcher.this.mDragLayer.removeView(Launcher.this.mFolderIconImageView);
          paramFolderIcon.setVisibility(0);
        }
      }
    });
    localObjectAnimator.start();
  }
  
  private boolean skipCustomClingIfNoAccounts()
  {
    AccountManager localAccountManager;
    if (((Cling)findViewById(2131296752)).getDrawIdentifier().equals("workspace_custom"))
    {
      localAccountManager = AccountManager.get(this);
      if (localAccountManager != null) {
        break label33;
      }
    }
    label33:
    while (localAccountManager.getAccountsByType("com.google").length != 0) {
      return false;
    }
    return true;
  }
  
  private void startGlobalSearch(String paramString, boolean paramBoolean, Bundle paramBundle, Rect paramRect)
  {
    ComponentName localComponentName = ((SearchManager)getSystemService("search")).getGlobalSearchActivity();
    if (localComponentName == null)
    {
      Log.w("Launcher", "No global search activity found.");
      return;
    }
    Intent localIntent = new Intent("android.search.action.GLOBAL_SEARCH");
    localIntent.addFlags(268435456);
    localIntent.setComponent(localComponentName);
    if (paramBundle == null) {}
    for (Bundle localBundle = new Bundle();; localBundle = new Bundle(paramBundle))
    {
      if (!localBundle.containsKey("source")) {
        localBundle.putString("source", getPackageName());
      }
      localIntent.putExtra("app_data", localBundle);
      if (!TextUtils.isEmpty(paramString)) {
        localIntent.putExtra("query", paramString);
      }
      if (paramBoolean) {
        localIntent.putExtra("select_query", paramBoolean);
      }
      localIntent.setSourceBounds(paramRect);
      try
      {
        startActivity(localIntent);
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Log.e("Launcher", "Global search activity not found: " + localComponentName);
        return;
      }
    }
  }
  
  private void toggleShowWeightWatcher()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0);
    boolean bool;
    View localView;
    int i;
    if (!localSharedPreferences.getBoolean("debug.show_mem", true))
    {
      bool = true;
      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
      localEditor.putBoolean("debug.show_mem", bool);
      localEditor.commit();
      if (this.mWeightWatcher != null)
      {
        localView = this.mWeightWatcher;
        i = 0;
        if (!bool) {
          break label82;
        }
      }
    }
    for (;;)
    {
      localView.setVisibility(i);
      return;
      bool = false;
      break;
      label82:
      i = 8;
    }
  }
  
  private void updateButtonWithDrawable(int paramInt, Drawable.ConstantState paramConstantState)
  {
    ((ImageView)findViewById(paramInt)).setImageDrawable(paramConstantState.newDrawable(getResources()));
  }
  
  private Drawable.ConstantState updateButtonWithIconFromExternalActivity(int paramInt1, ComponentName paramComponentName, int paramInt2, String paramString)
  {
    ImageView localImageView = (ImageView)findViewById(paramInt1);
    Drawable localDrawable = getExternalPackageToolbarIcon(paramComponentName, paramString);
    if (localImageView != null)
    {
      if (localDrawable != null) {
        break label46;
      }
      localImageView.setImageResource(paramInt2);
    }
    while (localDrawable != null)
    {
      return localDrawable.getConstantState();
      label46:
      localImageView.setImageDrawable(localDrawable);
    }
    return null;
  }
  
  private void updateGlobalIcons()
  {
    int i = getCurrentOrientationIndexForGlobalIcons();
    boolean bool1;
    boolean bool2;
    if ((sGlobalSearchIcon[i] != null) && (sVoiceSearchIcon[i] != null))
    {
      Drawable.ConstantState localConstantState = sAppMarketIcon[i];
      bool1 = false;
      bool2 = false;
      if (localConstantState != null) {}
    }
    else
    {
      bool1 = updateGlobalSearchIcon();
      bool2 = updateVoiceSearchIcon(bool1);
    }
    if (sGlobalSearchIcon[i] != null)
    {
      updateGlobalSearchIcon(sGlobalSearchIcon[i]);
      bool1 = true;
    }
    if (sVoiceSearchIcon[i] != null)
    {
      updateVoiceSearchIcon(sVoiceSearchIcon[i]);
      bool2 = true;
    }
    if (this.mSearchDropTargetBar != null) {
      this.mSearchDropTargetBar.onSearchPackagesChanged(bool1, bool2);
    }
  }
  
  private void updateRunning()
  {
    long l = 20000L;
    boolean bool;
    if ((this.mVisible) && (this.mUserPresent) && (!this.mWidgetsToAdvance.isEmpty()))
    {
      bool = true;
      if (bool != this.mAutoAdvanceRunning)
      {
        this.mAutoAdvanceRunning = bool;
        if (!bool) {
          break label77;
        }
        if (this.mAutoAdvanceTimeLeft != -1L) {
          break label69;
        }
      }
    }
    for (;;)
    {
      sendAdvanceMessage(l);
      return;
      bool = false;
      break;
      label69:
      l = this.mAutoAdvanceTimeLeft;
    }
    label77:
    if (!this.mWidgetsToAdvance.isEmpty()) {
      this.mAutoAdvanceTimeLeft = Math.max(0L, l - (System.currentTimeMillis() - this.mAutoAdvanceSentTime));
    }
    this.mHandler.removeMessages(1);
    this.mHandler.removeMessages(0);
  }
  
  private boolean waitUntilResume(Runnable paramRunnable)
  {
    return waitUntilResume(paramRunnable, false);
  }
  
  private boolean waitUntilResume(Runnable paramRunnable, boolean paramBoolean)
  {
    if (this.mPaused)
    {
      Log.i("Launcher", "Deferring update until onResume");
      while ((paramBoolean) && (this.mBindOnResumeCallbacks.remove(paramRunnable))) {}
      this.mBindOnResumeCallbacks.add(paramRunnable);
      return true;
    }
    return false;
  }
  
  /* Error */
  private static void writeConfiguration(Context paramContext, LocaleConfiguration paramLocaleConfiguration)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new 1978	java/io/DataOutputStream
    //   5: dup
    //   6: aload_0
    //   7: ldc_w 1379
    //   10: iconst_0
    //   11: invokevirtual 1982	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
    //   14: invokespecial 1985	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   17: astore_3
    //   18: aload_3
    //   19: aload_1
    //   20: getfield 500	com/android/launcher3/Launcher$LocaleConfiguration:locale	Ljava/lang/String;
    //   23: invokevirtual 1988	java/io/DataOutputStream:writeUTF	(Ljava/lang/String;)V
    //   26: aload_3
    //   27: aload_1
    //   28: getfield 514	com/android/launcher3/Launcher$LocaleConfiguration:mcc	I
    //   31: invokevirtual 1991	java/io/DataOutputStream:writeInt	(I)V
    //   34: aload_3
    //   35: aload_1
    //   36: getfield 518	com/android/launcher3/Launcher$LocaleConfiguration:mnc	I
    //   39: invokevirtual 1991	java/io/DataOutputStream:writeInt	(I)V
    //   42: aload_3
    //   43: invokevirtual 1992	java/io/DataOutputStream:flush	()V
    //   46: aload_3
    //   47: ifnull +89 -> 136
    //   50: aload_3
    //   51: invokevirtual 1993	java/io/DataOutputStream:close	()V
    //   54: return
    //   55: astore 11
    //   57: return
    //   58: astore 13
    //   60: aload_2
    //   61: ifnull -7 -> 54
    //   64: aload_2
    //   65: invokevirtual 1993	java/io/DataOutputStream:close	()V
    //   68: return
    //   69: astore 5
    //   71: return
    //   72: astore 12
    //   74: aload_0
    //   75: ldc_w 1379
    //   78: invokevirtual 1997	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   81: invokevirtual 2002	java/io/File:delete	()Z
    //   84: pop
    //   85: aload_2
    //   86: ifnull -32 -> 54
    //   89: aload_2
    //   90: invokevirtual 1993	java/io/DataOutputStream:close	()V
    //   93: return
    //   94: astore 10
    //   96: return
    //   97: astore 7
    //   99: aload_2
    //   100: ifnull +7 -> 107
    //   103: aload_2
    //   104: invokevirtual 1993	java/io/DataOutputStream:close	()V
    //   107: aload 7
    //   109: athrow
    //   110: astore 8
    //   112: goto -5 -> 107
    //   115: astore 7
    //   117: aload_3
    //   118: astore_2
    //   119: goto -20 -> 99
    //   122: astore 6
    //   124: aload_3
    //   125: astore_2
    //   126: goto -52 -> 74
    //   129: astore 4
    //   131: aload_3
    //   132: astore_2
    //   133: goto -73 -> 60
    //   136: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	paramContext	Context
    //   0	137	1	paramLocaleConfiguration	LocaleConfiguration
    //   1	132	2	localObject1	Object
    //   17	115	3	localDataOutputStream	java.io.DataOutputStream
    //   129	1	4	localFileNotFoundException1	java.io.FileNotFoundException
    //   69	1	5	localIOException1	java.io.IOException
    //   122	1	6	localIOException2	java.io.IOException
    //   97	11	7	localObject2	Object
    //   115	1	7	localObject3	Object
    //   110	1	8	localIOException3	java.io.IOException
    //   94	1	10	localIOException4	java.io.IOException
    //   55	1	11	localIOException5	java.io.IOException
    //   72	1	12	localIOException6	java.io.IOException
    //   58	1	13	localFileNotFoundException2	java.io.FileNotFoundException
    // Exception table:
    //   from	to	target	type
    //   50	54	55	java/io/IOException
    //   2	18	58	java/io/FileNotFoundException
    //   64	68	69	java/io/IOException
    //   2	18	72	java/io/IOException
    //   89	93	94	java/io/IOException
    //   2	18	97	finally
    //   74	85	97	finally
    //   103	107	110	java/io/IOException
    //   18	46	115	finally
    //   18	46	122	java/io/IOException
    //   18	46	129	java/io/FileNotFoundException
  }
  
  void addAppWidgetFromDrop(PendingAddWidgetInfo paramPendingAddWidgetInfo, long paramLong1, long paramLong2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    resetAddInfo();
    ItemInfo localItemInfo1 = this.mPendingAddInfo;
    paramPendingAddWidgetInfo.container = paramLong1;
    localItemInfo1.container = paramLong1;
    ItemInfo localItemInfo2 = this.mPendingAddInfo;
    paramPendingAddWidgetInfo.screenId = paramLong2;
    localItemInfo2.screenId = paramLong2;
    this.mPendingAddInfo.dropPos = paramArrayOfInt3;
    this.mPendingAddInfo.minSpanX = paramPendingAddWidgetInfo.minSpanX;
    this.mPendingAddInfo.minSpanY = paramPendingAddWidgetInfo.minSpanY;
    if (paramArrayOfInt1 != null)
    {
      this.mPendingAddInfo.cellX = paramArrayOfInt1[0];
      this.mPendingAddInfo.cellY = paramArrayOfInt1[1];
    }
    if (paramArrayOfInt2 != null)
    {
      this.mPendingAddInfo.spanX = paramArrayOfInt2[0];
      this.mPendingAddInfo.spanY = paramArrayOfInt2[1];
    }
    AppWidgetHostView localAppWidgetHostView = paramPendingAddWidgetInfo.boundWidget;
    if (localAppWidgetHostView != null)
    {
      addAppWidgetImpl(localAppWidgetHostView.getAppWidgetId(), paramPendingAddWidgetInfo, localAppWidgetHostView, paramPendingAddWidgetInfo.info);
      return;
    }
    int i = getAppWidgetHost().allocateAppWidgetId();
    Bundle localBundle = paramPendingAddWidgetInfo.bindOptions;
    if (localBundle != null) {}
    for (boolean bool = this.mAppWidgetManager.bindAppWidgetIdIfAllowed(i, paramPendingAddWidgetInfo.componentName, localBundle); bool; bool = this.mAppWidgetManager.bindAppWidgetIdIfAllowed(i, paramPendingAddWidgetInfo.componentName))
    {
      addAppWidgetImpl(i, paramPendingAddWidgetInfo, null, paramPendingAddWidgetInfo.info);
      return;
    }
    this.mPendingAddWidgetInfo = paramPendingAddWidgetInfo.info;
    Intent localIntent = new Intent("android.appwidget.action.APPWIDGET_BIND");
    localIntent.putExtra("appWidgetId", i);
    localIntent.putExtra("appWidgetProvider", paramPendingAddWidgetInfo.componentName);
    startActivityForResult(localIntent, 11);
  }
  
  void addAppWidgetImpl(int paramInt, ItemInfo paramItemInfo, AppWidgetHostView paramAppWidgetHostView, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    addAppWidgetImpl(paramInt, paramItemInfo, paramAppWidgetHostView, paramAppWidgetProviderInfo, 0);
  }
  
  void addAppWidgetImpl(int paramInt1, ItemInfo paramItemInfo, AppWidgetHostView paramAppWidgetHostView, AppWidgetProviderInfo paramAppWidgetProviderInfo, int paramInt2)
  {
    if (paramAppWidgetProviderInfo.configure != null)
    {
      this.mPendingAddWidgetInfo = paramAppWidgetProviderInfo;
      this.mPendingAddWidgetId = paramInt1;
      Intent localIntent = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
      localIntent.setComponent(paramAppWidgetProviderInfo.configure);
      localIntent.putExtra("appWidgetId", paramInt1);
      Utilities.startActivityForResultSafely(this, localIntent, 5);
      return;
    }
    Runnable local16 = new Runnable()
    {
      public void run()
      {
        Launcher.this.exitSpringLoadedDragModeDelayed(true, 300, null);
      }
    };
    completeAddAppWidget(paramInt1, paramItemInfo.container, paramItemInfo.screenId, paramAppWidgetHostView, paramAppWidgetProviderInfo);
    this.mWorkspace.removeExtraEmptyScreen(true, local16, paramInt2, false);
  }
  
  protected void addCustomContentToLeft() {}
  
  FolderIcon addFolder(CellLayout paramCellLayout, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    FolderInfo localFolderInfo = new FolderInfo();
    localFolderInfo.title = getText(2131361862);
    LauncherModel.addItemToDatabase(this, localFolderInfo, paramLong1, paramLong2, paramInt1, paramInt2, false);
    sFolders.put(Long.valueOf(localFolderInfo.id), localFolderInfo);
    FolderIcon localFolderIcon = FolderIcon.fromXml(2130968696, this, paramCellLayout, localFolderInfo, this.mIconCache);
    this.mWorkspace.addInScreen(localFolderIcon, paramLong1, paramLong2, paramInt1, paramInt2, 1, 1, isWorkspaceLocked());
    this.mWorkspace.getParentCellLayoutForView(localFolderIcon).getShortcutsAndWidgets().measureChild(localFolderIcon);
    return localFolderIcon;
  }
  
  public void addOnResumeCallback(Runnable paramRunnable)
  {
    this.mOnResumeCallbacks.add(paramRunnable);
  }
  
  public QSBScroller addToCustomContentPage(View paramView, CustomContentCallbacks paramCustomContentCallbacks, String paramString)
  {
    this.mWorkspace.addToCustomContentPage(paramView, paramCustomContentCallbacks, paramString);
    return this.mQsbScroller;
  }
  
  void addWidgetToAutoAdvanceIfNeeded(View paramView, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    if ((paramAppWidgetProviderInfo == null) || (paramAppWidgetProviderInfo.autoAdvanceViewId == -1)) {}
    View localView;
    do
    {
      return;
      localView = paramView.findViewById(paramAppWidgetProviderInfo.autoAdvanceViewId);
    } while (!(localView instanceof Advanceable));
    this.mWidgetsToAdvance.put(paramView, paramAppWidgetProviderInfo);
    ((Advanceable)localView).fyiWillBeAdvancedByHostKThx();
    updateRunning();
  }
  
  public void bindAddScreens(ArrayList<Long> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      this.mWorkspace.insertNewWorkspaceScreenBeforeEmptyScreen(((Long)paramArrayList.get(j)).longValue());
    }
  }
  
  public void bindAllApplications(ArrayList<AppInfo> paramArrayList)
  {
    if (AppsCustomizePagedView.DISABLE_ALL_APPS) {
      if (mIntentsOnWorkspaceFromUpgradePath != null) {
        mIntentsOnWorkspaceFromUpgradePath = null;
      }
    }
    while (this.mAppsCustomizeContent == null) {
      return;
    }
    this.mAppsCustomizeContent.setApps(paramArrayList);
    this.mAppsCustomizeContent.onPackagesUpdated(LauncherModel.getSortedWidgetsAndShortcuts(this));
  }
  
  public void bindAppWidget(final LauncherAppWidgetInfo paramLauncherAppWidgetInfo)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindAppWidget(paramLauncherAppWidgetInfo);
      }
    })) {
      return;
    }
    Workspace localWorkspace = this.mWorkspace;
    int i = paramLauncherAppWidgetInfo.appWidgetId;
    AppWidgetProviderInfo localAppWidgetProviderInfo = this.mAppWidgetManager.getAppWidgetInfo(i);
    paramLauncherAppWidgetInfo.hostView = this.mAppWidgetHost.createView(this, i, localAppWidgetProviderInfo);
    paramLauncherAppWidgetInfo.hostView.setTag(paramLauncherAppWidgetInfo);
    paramLauncherAppWidgetInfo.onBindAppWidget(this);
    localWorkspace.addInScreen(paramLauncherAppWidgetInfo.hostView, paramLauncherAppWidgetInfo.container, paramLauncherAppWidgetInfo.screenId, paramLauncherAppWidgetInfo.cellX, paramLauncherAppWidgetInfo.cellY, paramLauncherAppWidgetInfo.spanX, paramLauncherAppWidgetInfo.spanY, false);
    addWidgetToAutoAdvanceIfNeeded(paramLauncherAppWidgetInfo.hostView, localAppWidgetProviderInfo);
    localWorkspace.requestLayout();
  }
  
  public void bindAppsAdded(final ArrayList<Long> paramArrayList, final ArrayList<ItemInfo> paramArrayList1, final ArrayList<ItemInfo> paramArrayList2, final ArrayList<AppInfo> paramArrayList3)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindAppsAdded(paramArrayList, paramArrayList1, paramArrayList2, paramArrayList3);
      }
    })) {}
    do
    {
      return;
      bindAddScreens(paramArrayList);
      if (!paramArrayList1.isEmpty()) {
        bindItems(paramArrayList1, 0, paramArrayList1.size(), false);
      }
      if (!paramArrayList2.isEmpty()) {
        bindItems(paramArrayList2, 0, paramArrayList2.size(), true);
      }
      this.mWorkspace.removeExtraEmptyScreen(false, null);
    } while ((AppsCustomizePagedView.DISABLE_ALL_APPS) || (paramArrayList3 == null) || (this.mAppsCustomizeContent == null));
    this.mAppsCustomizeContent.addApps(paramArrayList3);
  }
  
  public void bindAppsUpdated(final ArrayList<AppInfo> paramArrayList)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindAppsUpdated(paramArrayList);
      }
    })) {}
    do
    {
      return;
      if (this.mWorkspace != null) {
        this.mWorkspace.updateShortcuts(paramArrayList);
      }
    } while ((AppsCustomizePagedView.DISABLE_ALL_APPS) || (this.mAppsCustomizeContent == null));
    this.mAppsCustomizeContent.updateApps(paramArrayList);
  }
  
  public void bindComponentsRemoved(final ArrayList<String> paramArrayList, final ArrayList<AppInfo> paramArrayList1)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindComponentsRemoved(paramArrayList, paramArrayList1);
      }
    })) {}
    do
    {
      return;
      if (!paramArrayList.isEmpty()) {
        this.mWorkspace.removeItemsByPackageName(paramArrayList);
      }
      if (!paramArrayList1.isEmpty()) {
        this.mWorkspace.removeItemsByApplicationInfo(paramArrayList1);
      }
      this.mDragController.onAppsRemoved(paramArrayList, paramArrayList1);
    } while ((AppsCustomizePagedView.DISABLE_ALL_APPS) || (this.mAppsCustomizeContent == null));
    this.mAppsCustomizeContent.removeApps(paramArrayList1);
  }
  
  public void bindFolders(final HashMap<Long, FolderInfo> paramHashMap)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindFolders(paramHashMap);
      }
    })) {
      return;
    }
    sFolders.clear();
    sFolders.putAll(paramHashMap);
  }
  
  public void bindItems(final ArrayList<ItemInfo> paramArrayList, final int paramInt1, final int paramInt2, final boolean paramBoolean)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.bindItems(paramArrayList, paramInt1, paramInt2, paramBoolean);
      }
    })) {
      return;
    }
    final AnimatorSet localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
    final ArrayList localArrayList = new ArrayList();
    int i;
    Workspace localWorkspace;
    long l1;
    int j;
    label64:
    ItemInfo localItemInfo;
    if ((paramBoolean) && (canRunNewAppsAnimation()))
    {
      i = 1;
      localWorkspace = this.mWorkspace;
      l1 = -1L;
      j = paramInt1;
      if (j >= paramInt2) {
        break label363;
      }
      localItemInfo = (ItemInfo)paramArrayList.get(j);
      if ((localItemInfo.container != -101L) || (this.mHotseat != null)) {
        break label112;
      }
    }
    for (;;)
    {
      j++;
      break label64;
      i = 0;
      break;
      label112:
      switch (localItemInfo.itemType)
      {
      default: 
        throw new RuntimeException("Invalid Item Type");
      case 0: 
      case 1: 
        View localView = createShortcut((ShortcutInfo)localItemInfo);
        if (localItemInfo.container == -100L)
        {
          CellLayout localCellLayout = this.mWorkspace.getScreenWithId(localItemInfo.screenId);
          if ((localCellLayout != null) && (localCellLayout.isOccupied(localItemInfo.cellX, localItemInfo.cellY))) {
            throw new RuntimeException("OCCUPIED");
          }
        }
        localWorkspace.addInScreenFromBind(localView, localItemInfo.container, localItemInfo.screenId, localItemInfo.cellX, localItemInfo.cellY, 1, 1);
        if (i != 0)
        {
          localView.setAlpha(0.0F);
          localView.setScaleX(0.0F);
          localView.setScaleY(0.0F);
          localArrayList.add(createNewAppBounceAnimation(localView, j));
          l1 = localItemInfo.screenId;
        }
        break;
      case 2: 
        localWorkspace.addInScreenFromBind(FolderIcon.fromXml(2130968696, this, (ViewGroup)localWorkspace.getChildAt(localWorkspace.getCurrentPage()), (FolderInfo)localItemInfo, this.mIconCache), localItemInfo.container, localItemInfo.screenId, localItemInfo.cellX, localItemInfo.cellY, 1, 1);
      }
    }
    label363:
    final Runnable local28;
    if ((i != 0) && (l1 > -1L))
    {
      long l2 = this.mWorkspace.getScreenIdForPageIndex(this.mWorkspace.getNextPage());
      final int k = this.mWorkspace.getPageIndexForScreenId(l1);
      local28 = new Runnable()
      {
        public void run()
        {
          localAnimatorSet.playTogether(localArrayList);
          localAnimatorSet.start();
        }
      };
      if (l1 == l2) {
        break label456;
      }
      this.mWorkspace.postDelayed(new Runnable()
      {
        public void run()
        {
          Launcher.this.mWorkspace.snapToPage(k);
          Launcher.this.mWorkspace.postDelayed(local28, Launcher.NEW_APPS_ANIMATION_DELAY);
        }
      }, NEW_APPS_PAGE_MOVE_DELAY);
    }
    for (;;)
    {
      localWorkspace.requestLayout();
      return;
      label456:
      this.mWorkspace.postDelayed(local28, NEW_APPS_ANIMATION_DELAY);
    }
  }
  
  public void bindPackagesUpdated(ArrayList<Object> paramArrayList)
  {
    if (waitUntilResume(this.mBindPackagesUpdatedRunnable, true)) {
      this.mWidgetsAndShortcuts = paramArrayList;
    }
    while ((AppsCustomizePagedView.DISABLE_ALL_APPS) || (this.mAppsCustomizeContent == null)) {
      return;
    }
    this.mAppsCustomizeContent.onPackagesUpdated(paramArrayList);
  }
  
  public void bindScreens(ArrayList<Long> paramArrayList)
  {
    bindAddScreens(paramArrayList);
    if (paramArrayList.size() == 0) {
      this.mWorkspace.addExtraEmptyScreen();
    }
    if ((!this.mWorkspace.hasCustomContent()) && (hasCustomContentToLeft())) {
      this.mWorkspace.createCustomContentPage();
    }
  }
  
  public void bindSearchablesChanged()
  {
    boolean bool1 = updateGlobalSearchIcon();
    boolean bool2 = updateVoiceSearchIcon(bool1);
    if (this.mSearchDropTargetBar != null) {
      this.mSearchDropTargetBar.onSearchPackagesChanged(bool1, bool2);
    }
  }
  
  public void closeFolder()
  {
    Folder localFolder = this.mWorkspace.getOpenFolder();
    if (localFolder != null)
    {
      if (localFolder.isEditingName()) {
        localFolder.dismissEditingName();
      }
      closeFolder(localFolder);
      dismissFolderCling(null);
    }
  }
  
  void closeFolder(Folder paramFolder)
  {
    paramFolder.getInfo().opened = false;
    if ((ViewGroup)paramFolder.getParent().getParent() != null) {
      shrinkAndFadeInFolderIcon((FolderIcon)this.mWorkspace.getViewForTag(paramFolder.mInfo));
    }
    paramFolder.animateClosed();
    getDragLayer().sendAccessibilityEvent(32);
  }
  
  public void closeSystemDialogs()
  {
    getWindow().closeAllPanels();
    this.mWaitingForResult = false;
  }
  
  void completeAddApplication(Intent paramIntent, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = this.mTmpAddItemCellCoordinates;
    CellLayout localCellLayout = getCellLayout(paramLong1, paramLong2);
    if ((paramInt1 >= 0) && (paramInt2 >= 0))
    {
      arrayOfInt[0] = paramInt1;
      arrayOfInt[1] = paramInt2;
    }
    while (localCellLayout.findCellForSpan(arrayOfInt, 1, 1))
    {
      ShortcutInfo localShortcutInfo = this.mModel.getShortcutInfo(getPackageManager(), paramIntent, this);
      if (localShortcutInfo == null) {
        break;
      }
      localShortcutInfo.setActivity(this, paramIntent.getComponent(), 270532608);
      localShortcutInfo.container = -1L;
      this.mWorkspace.addApplicationShortcut(localShortcutInfo, localCellLayout, paramLong1, paramLong2, arrayOfInt[0], arrayOfInt[1], isWorkspaceLocked(), paramInt1, paramInt2);
      return;
    }
    showOutOfSpaceMessage(isHotseatLayout(localCellLayout));
    return;
    Log.e("Launcher", "Couldn't find ActivityInfo for selected application: " + paramIntent);
  }
  
  View createShortcut(int paramInt, ViewGroup paramViewGroup, ShortcutInfo paramShortcutInfo)
  {
    BubbleTextView localBubbleTextView = (BubbleTextView)this.mInflater.inflate(paramInt, paramViewGroup, false);
    localBubbleTextView.applyFromShortcutInfo(paramShortcutInfo, this.mIconCache);
    localBubbleTextView.setOnClickListener(this);
    return localBubbleTextView;
  }
  
  View createShortcut(ShortcutInfo paramShortcutInfo)
  {
    return createShortcut(2130968595, (ViewGroup)this.mWorkspace.getChildAt(this.mWorkspace.getCurrentPage()), paramShortcutInfo);
  }
  
  public void disableVoiceButtonProxy(boolean paramBoolean)
  {
    updateVoiceButtonProxyVisible(paramBoolean);
  }
  
  public void dismissFirstRunCling(View paramView)
  {
    dismissCling((Cling)findViewById(2131296751), new Runnable()
    {
      public void run()
      {
        Launcher.this.showFirstRunWorkspaceCling();
      }
    }, "cling_gel.first_run.dismissed", 200, false);
    this.mSearchDropTargetBar.hideSearchBar(true);
  }
  
  public void dismissFolderCling(View paramView)
  {
    dismissCling((Cling)findViewById(2131296753), null, "cling_gel.folder.dismissed", 200, true);
  }
  
  public void dismissWorkspaceCling(View paramView)
  {
    Cling localCling = (Cling)findViewById(2131296752);
    Runnable local42 = null;
    if (paramView == null) {
      local42 = new Runnable()
      {
        public void run()
        {
          Launcher.this.mWorkspace.enterOverviewMode();
        }
      };
    }
    dismissCling(localCling, local42, "cling_gel.workspace.dismissed", 200, true);
    this.mSearchDropTargetBar.showSearchBar(true);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = 1;
    if (paramKeyEvent.getAction() == 0) {
      switch (paramKeyEvent.getKeyCode())
      {
      }
    }
    for (;;)
    {
      i = super.dispatchKeyEvent(paramKeyEvent);
      return i;
      if (isPropertyEnabled("launcher_dump_state"))
      {
        dumpState();
        return i;
        if (paramKeyEvent.getAction() == i) {
          switch (paramKeyEvent.getKeyCode())
          {
          }
        }
      }
    }
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    boolean bool = super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    List localList = paramAccessibilityEvent.getText();
    localList.clear();
    if (this.mState == State.APPS_CUSTOMIZE)
    {
      localList.add(this.mAppsCustomizeTabHost.getCurrentTabView().getContentDescription());
      return bool;
    }
    localList.add(getString(2131361900));
    return bool;
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    synchronized (sDumpLogs)
    {
      paramPrintWriter.println(" ");
      paramPrintWriter.println("Debug logs: ");
      for (int i = 0; i < sDumpLogs.size(); i++) {
        paramPrintWriter.println("  " + (String)sDumpLogs.get(i));
      }
      return;
    }
  }
  
  public void dumpLogsToLocalData() {}
  
  public void dumpState()
  {
    Log.d("Launcher", "BEGIN launcher3 dump state for launcher " + this);
    Log.d("Launcher", "mSavedState=" + this.mSavedState);
    Log.d("Launcher", "mWorkspaceLoading=" + this.mWorkspaceLoading);
    Log.d("Launcher", "mRestoring=" + this.mRestoring);
    Log.d("Launcher", "mWaitingForResult=" + this.mWaitingForResult);
    Log.d("Launcher", "mSavedInstanceState=" + this.mSavedInstanceState);
    Log.d("Launcher", "sFolders.size=" + sFolders.size());
    this.mModel.dumpState();
    if (this.mAppsCustomizeContent != null) {
      this.mAppsCustomizeContent.dumpState();
    }
    Log.d("Launcher", "END launcher3 dump state");
  }
  
  void enterSpringLoadedDragMode()
  {
    if (isAllAppsVisible())
    {
      hideAppsCustomizeHelper(Workspace.State.SPRING_LOADED, true, true, null);
      this.mState = State.APPS_CUSTOMIZE_SPRING_LOADED;
    }
  }
  
  void exitSpringLoadedDragMode()
  {
    if (this.mState == State.APPS_CUSTOMIZE_SPRING_LOADED)
    {
      showAppsCustomizeHelper(true, true);
      this.mState = State.APPS_CUSTOMIZE;
    }
  }
  
  void exitSpringLoadedDragModeDelayed(final boolean paramBoolean, int paramInt, final Runnable paramRunnable)
  {
    if (this.mState != State.APPS_CUSTOMIZE_SPRING_LOADED) {
      return;
    }
    this.mHandler.postDelayed(new Runnable()
    {
      public void run()
      {
        if (paramBoolean)
        {
          Launcher.this.mAppsCustomizeTabHost.setVisibility(8);
          Launcher.this.showWorkspace(true, paramRunnable);
          return;
        }
        Launcher.this.exitSpringLoadedDragMode();
      }
    }, paramInt);
  }
  
  public void finishBindingItems(final boolean paramBoolean)
  {
    if (waitUntilResume(new Runnable()
    {
      public void run()
      {
        Launcher.this.finishBindingItems(paramBoolean);
      }
    })) {
      return;
    }
    if (this.mSavedState != null)
    {
      if (!this.mWorkspace.hasFocus()) {
        this.mWorkspace.getChildAt(this.mWorkspace.getCurrentPage()).requestFocus();
      }
      this.mSavedState = null;
    }
    this.mWorkspace.restoreInstanceStateForRemainingPages();
    for (int i = 0; i < sPendingAddList.size(); i++) {
      completeAdd((PendingAddArguments)sPendingAddList.get(i));
    }
    sPendingAddList.clear();
    this.mWorkspaceLoading = false;
    if (paramBoolean)
    {
      this.mWorkspace.getUniqueComponents(true, null);
      mIntentsOnWorkspaceFromUpgradePath = this.mWorkspace.getUniqueComponents(true, null);
    }
    this.mWorkspace.post(new Runnable()
    {
      public void run()
      {
        Launcher.this.onFinishBindingItems();
      }
    });
  }
  
  public LauncherAppWidgetHost getAppWidgetHost()
  {
    return this.mAppWidgetHost;
  }
  
  CellLayout getCellLayout(long paramLong1, long paramLong2)
  {
    if (paramLong1 == -101L)
    {
      if (this.mHotseat != null) {
        return this.mHotseat.getLayout();
      }
      return null;
    }
    return this.mWorkspace.getScreenWithId(paramLong2);
  }
  
  public int getCurrentWorkspaceScreen()
  {
    if (this.mWorkspace != null) {
      return this.mWorkspace.getCurrentPage();
    }
    return 2;
  }
  
  public DragController getDragController()
  {
    return this.mDragController;
  }
  
  public DragLayer getDragLayer()
  {
    return this.mDragLayer;
  }
  
  protected String getFirstRunClingSearchBarHint()
  {
    return "";
  }
  
  protected String getFirstRunCustomContentHint()
  {
    return "";
  }
  
  protected String getFirstRunFocusedHotseatAppBubbleDescription()
  {
    return "";
  }
  
  protected String getFirstRunFocusedHotseatAppBubbleTitle()
  {
    return "";
  }
  
  protected ComponentName getFirstRunFocusedHotseatAppComponentName()
  {
    return null;
  }
  
  protected int getFirstRunFocusedHotseatAppDrawableId()
  {
    return -1;
  }
  
  protected int getFirstRunFocusedHotseatAppRank()
  {
    return -1;
  }
  
  public View.OnTouchListener getHapticFeedbackTouchListener()
  {
    if (this.mHapticFeedbackTouchListener == null) {
      this.mHapticFeedbackTouchListener = new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          if ((0xFF & paramAnonymousMotionEvent.getAction()) == 0) {
            paramAnonymousView.performHapticFeedback(1);
          }
          return false;
        }
      };
    }
    return this.mHapticFeedbackTouchListener;
  }
  
  Hotseat getHotseat()
  {
    return this.mHotseat;
  }
  
  public LauncherModel getModel()
  {
    return this.mModel;
  }
  
  View getOverviewPanel()
  {
    return this.mOverviewPanel;
  }
  
  public View getQsbBar()
  {
    if (this.mQsbBar == null)
    {
      this.mQsbBar = this.mInflater.inflate(2130968815, this.mSearchDropTargetBar, false);
      this.mSearchDropTargetBar.addView(this.mQsbBar);
    }
    return this.mQsbBar;
  }
  
  SearchDropTargetBar getSearchBar()
  {
    return this.mSearchDropTargetBar;
  }
  
  public Stats getStats()
  {
    return this.mStats;
  }
  
  protected ComponentName getWallpaperPickerComponent()
  {
    return new ComponentName(getPackageName(), WallpaperPickerActivity.class.getName());
  }
  
  Workspace getWorkspace()
  {
    return this.mWorkspace;
  }
  
  protected boolean hasCustomContentToLeft()
  {
    return false;
  }
  
  protected void invalidateHasCustomContentToLeft()
  {
    if ((this.mWorkspace == null) || (this.mWorkspace.getScreenOrder().isEmpty())) {}
    do
    {
      return;
      if ((!this.mWorkspace.hasCustomContent()) && (hasCustomContentToLeft()))
      {
        this.mWorkspace.createCustomContentPage();
        addCustomContentToLeft();
        return;
      }
    } while ((!this.mWorkspace.hasCustomContent()) || (hasCustomContentToLeft()));
    this.mWorkspace.removeCustomContentPage();
  }
  
  public boolean isAllAppsButtonRank(int paramInt)
  {
    if (this.mHotseat != null) {
      return this.mHotseat.isAllAppsButtonRank(paramInt);
    }
    return false;
  }
  
  public boolean isAllAppsVisible()
  {
    return (this.mState == State.APPS_CUSTOMIZE) || (this.mOnResumeState == State.APPS_CUSTOMIZE);
  }
  
  boolean isDraggingEnabled()
  {
    return !this.mModel.isLoadingWorkspace();
  }
  
  public boolean isFolderClingVisible()
  {
    Cling localCling = (Cling)findViewById(2131296753);
    boolean bool = false;
    if (localCling != null)
    {
      int i = localCling.getVisibility();
      bool = false;
      if (i == 0) {
        bool = true;
      }
    }
    return bool;
  }
  
  boolean isHotseatLayout(View paramView)
  {
    return (this.mHotseat != null) && (paramView != null) && ((paramView instanceof CellLayout)) && (paramView == this.mHotseat.getLayout());
  }
  
  public boolean isRotationEnabled()
  {
    return (sForceEnableRotation) || (getResources().getBoolean(2131755011));
  }
  
  public boolean isWorkspaceLocked()
  {
    return (this.mWorkspaceLoading) || (this.mWaitingForResult);
  }
  
  void lockAllApps() {}
  
  public void lockScreenOrientation()
  {
    if (isRotationEnabled()) {
      setRequestedOrientation(mapConfigurationOriActivityInfoOri(getResources().getConfiguration().orientation));
    }
  }
  
  protected void moveToCustomContentScreen(boolean paramBoolean)
  {
    closeFolder();
    this.mWorkspace.moveToCustomContentScreen(paramBoolean);
  }
  
  protected void onActivityResult(int paramInt1, final int paramInt2, Intent paramIntent)
  {
    this.mWaitingForResult = false;
    int i = this.mPendingAddWidgetId;
    this.mPendingAddWidgetId = -1;
    Runnable local4 = new Runnable()
    {
      public void run()
      {
        Launcher localLauncher = Launcher.this;
        if (paramInt2 != 0) {}
        for (boolean bool = true;; bool = false)
        {
          localLauncher.exitSpringLoadedDragModeDelayed(bool, 300, null);
          return;
        }
      }
    };
    int n;
    if (paramInt1 == 11) {
      if (paramIntent != null)
      {
        n = paramIntent.getIntExtra("appWidgetId", -1);
        if (paramInt2 != 0) {
          break label79;
        }
        completeTwoStageWidgetDrop(0, n);
        this.mWorkspace.removeExtraEmptyScreen(true, local4, 500, false);
      }
    }
    label79:
    do
    {
      do
      {
        return;
        n = -1;
        break;
      } while (paramInt2 != -1);
      addAppWidgetImpl(n, this.mPendingAddInfo, null, this.mPendingAddWidgetInfo, 500);
      return;
      if (paramInt1 != 10) {
        break label133;
      }
    } while ((paramInt2 != -1) || (!this.mWorkspace.isInOverviewMode()));
    this.mWorkspace.exitOverviewMode(false);
    return;
    label133:
    int j;
    int k;
    label166:
    final int m;
    if ((paramInt1 == 9) || (paramInt1 == 5))
    {
      j = 1;
      if (j == 0) {
        break label285;
      }
      if (paramIntent == null) {
        break label232;
      }
      k = paramIntent.getIntExtra("appWidgetId", -1);
      if (k >= 0) {
        break label238;
      }
      m = i;
      label175:
      if ((m >= 0) && (paramInt2 != 0)) {
        break label245;
      }
      Log.e("Launcher", "Error: appWidgetId (EXTRA_APPWIDGET_ID) was not returned from the \\widget configuration activity.");
      completeTwoStageWidgetDrop(0, m);
    }
    label232:
    label238:
    label245:
    final CellLayout localCellLayout;
    for (Object localObject = new Runnable()
        {
          public void run()
          {
            Launcher.this.exitSpringLoadedDragModeDelayed(false, 0, null);
          }
        };; localObject = new Runnable()
        {
          public void run()
          {
            Launcher.this.completeTwoStageWidgetDrop(paramInt2, m);
            localCellLayout.setDropPending(false);
          }
        })
    {
      this.mWorkspace.removeExtraEmptyScreen(true, (Runnable)localObject, 500, false);
      return;
      j = 0;
      break;
      k = -1;
      break label166;
      m = k;
      break label175;
      localCellLayout = this.mWorkspace.getScreenWithId(this.mPendingAddInfo.screenId);
      localCellLayout.setDropPending(true);
    }
    label285:
    PendingAddArguments localPendingAddArguments;
    if ((paramInt2 == -1) && (this.mPendingAddInfo.container != -1L))
    {
      localPendingAddArguments = new PendingAddArguments(null);
      localPendingAddArguments.requestCode = paramInt1;
      localPendingAddArguments.intent = paramIntent;
      localPendingAddArguments.container = this.mPendingAddInfo.container;
      localPendingAddArguments.screenId = this.mPendingAddInfo.screenId;
      localPendingAddArguments.cellX = this.mPendingAddInfo.cellX;
      localPendingAddArguments.cellY = this.mPendingAddInfo.cellY;
      if (isWorkspaceLocked())
      {
        sPendingAddList.add(localPendingAddArguments);
        this.mWorkspace.removeExtraEmptyScreen(true, local4, 500, false);
      }
    }
    for (;;)
    {
      this.mDragLayer.clearAnimatedView();
      return;
      completeAdd(localPendingAddArguments);
      break;
      if (paramInt2 == 0) {
        this.mWorkspace.removeExtraEmptyScreen(true, local4, 500, false);
      }
    }
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
    localIntentFilter.addAction("android.intent.action.USER_PRESENT");
    registerReceiver(this.mReceiver, localIntentFilter);
    FirstFrameAnimatorHelper.initializeDrawListener(getWindow().getDecorView());
    this.mAttached = true;
    this.mVisible = true;
  }
  
  public void onBackPressed()
  {
    if (isAllAppsVisible())
    {
      if (this.mAppsCustomizeContent.getContentType() == AppsCustomizePagedView.ContentType.Applications)
      {
        showWorkspace(true);
        return;
      }
      showOverviewMode(true);
      return;
    }
    if (this.mWorkspace.isInOverviewMode())
    {
      this.mWorkspace.exitOverviewMode(true);
      return;
    }
    if (this.mWorkspace.getOpenFolder() != null)
    {
      Folder localFolder = this.mWorkspace.getOpenFolder();
      if (localFolder.isEditingName())
      {
        localFolder.dismissEditingName();
        return;
      }
      closeFolder();
      return;
    }
    this.mWorkspace.exitWidgetResizeMode();
    this.mWorkspace.showOutlinesTemporarily();
  }
  
  public void onClick(View paramView)
  {
    if (paramView.getWindowToken() == null) {}
    label7:
    do
    {
      do
      {
        Object localObject;
        boolean bool;
        do
        {
          do
          {
            break label7;
            break label7;
            do
            {
              return;
            } while (!this.mWorkspace.isFinishedSwitchingState());
            if (!(paramView instanceof Workspace)) {
              break;
            }
          } while (!this.mWorkspace.isInOverviewMode());
          this.mWorkspace.exitOverviewMode(true);
          return;
          if (((paramView instanceof CellLayout)) && (this.mWorkspace.isInOverviewMode())) {
            this.mWorkspace.exitOverviewMode(this.mWorkspace.indexOfChild(paramView), true);
          }
          localObject = paramView.getTag();
          if (!(localObject instanceof ShortcutInfo)) {
            break;
          }
          ShortcutInfo localShortcutInfo = (ShortcutInfo)localObject;
          Intent localIntent = localShortcutInfo.intent;
          if (localIntent.getComponent() != null)
          {
            String str = localIntent.getComponent().getClassName();
            if (str.equals(WidgetAdder.class.getName()))
            {
              showAllApps(true, AppsCustomizePagedView.ContentType.Widgets, true);
              return;
            }
            if (str.equals(MemoryDumpActivity.class.getName()))
            {
              MemoryDumpActivity.startDump(this);
              return;
            }
            if (str.equals(ToggleWeightWatcher.class.getName()))
            {
              toggleShowWeightWatcher();
              return;
            }
          }
          int[] arrayOfInt = new int[2];
          paramView.getLocationOnScreen(arrayOfInt);
          localIntent.setSourceBounds(new Rect(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight()));
          bool = startActivitySafely(paramView, localIntent, localObject);
          this.mStats.recordLaunch(localIntent, localShortcutInfo);
        } while ((!bool) || (!(paramView instanceof BubbleTextView)));
        this.mWaitingForResume = ((BubbleTextView)paramView);
        this.mWaitingForResume.setStayPressed(true);
        return;
        if (!(localObject instanceof FolderInfo)) {
          break;
        }
      } while (!(paramView instanceof FolderIcon));
      handleFolderClick((FolderIcon)paramView);
      return;
    } while (paramView != this.mAllAppsButton);
    if (isAllAppsVisible())
    {
      showWorkspace(true);
      return;
    }
    onClickAllAppsButton(paramView);
  }
  
  public void onClickAllAppsButton(View paramView)
  {
    showAllApps(true, AppsCustomizePagedView.ContentType.Applications, true);
  }
  
  public void onClickAppMarketButton(View paramView) {}
  
  public void onClickSearchButton(View paramView)
  {
    paramView.performHapticFeedback(1);
    onSearchRequested();
  }
  
  public void onClickVoiceButton(View paramView)
  {
    paramView.performHapticFeedback(1);
    startVoice();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    LauncherAppState.setApplicationContext(getApplicationContext());
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    Point localPoint1 = new Point();
    Point localPoint2 = new Point();
    Point localPoint3 = new Point();
    Display localDisplay = getWindowManager().getDefaultDisplay();
    localDisplay.getCurrentSizeRange(localPoint1, localPoint2);
    localDisplay.getRealSize(localPoint3);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localDisplay.getMetrics(localDisplayMetrics);
    DeviceProfile localDeviceProfile = localLauncherAppState.initDynamicGrid(this, Math.min(localPoint1.x, localPoint1.y), Math.min(localPoint2.x, localPoint2.y), localPoint3.x, localPoint3.y, localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
    this.mSharedPrefs = getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0);
    this.mModel = localLauncherAppState.setLauncher(this);
    this.mIconCache = localLauncherAppState.getIconCache();
    this.mIconCache.flushInvalidIcons(localDeviceProfile);
    this.mDragController = new DragController(this);
    this.mInflater = getLayoutInflater();
    this.mStats = new Stats(this);
    this.mAppWidgetManager = AppWidgetManager.getInstance(this);
    this.mAppWidgetHost = new LauncherAppWidgetHost(this, 1024);
    this.mAppWidgetHost.startListening();
    this.mPaused = false;
    checkForLocaleChange();
    setContentView(2130968734);
    setupViews();
    localDeviceProfile.layout(this);
    registerContentObservers();
    lockAllApps();
    this.mSavedState = paramBundle;
    restoreState(this.mSavedState);
    if (!this.mRestoring)
    {
      if (!sPausedFromUserAction) {
        break label360;
      }
      this.mModel.startLoader(true, -1);
    }
    for (;;)
    {
      this.mDefaultKeySsb = new SpannableStringBuilder();
      Selection.setSelection(this.mDefaultKeySsb, 0);
      IntentFilter localIntentFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
      registerReceiver(this.mCloseSystemDialogsReceiver, localIntentFilter);
      updateGlobalIcons();
      unlockScreenOrientation(true);
      showFirstRunCling();
      return;
      label360:
      this.mModel.startLoader(true, this.mWorkspace.getCurrentPage());
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    this.mHandler.removeMessages(1);
    this.mHandler.removeMessages(0);
    this.mWorkspace.removeCallbacks(this.mBuildLayersRunnable);
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    this.mModel.stopLoader();
    localLauncherAppState.setLauncher(null);
    try
    {
      this.mAppWidgetHost.stopListening();
      this.mAppWidgetHost = null;
      this.mWidgetsToAdvance.clear();
      TextKeyListener.getInstance().release();
      if (this.mModel != null) {
        this.mModel.unbindItemInfosAndClearQueuedBindRunnables();
      }
      getContentResolver().unregisterContentObserver(this.mWidgetObserver);
      unregisterReceiver(this.mCloseSystemDialogsReceiver);
      this.mDragLayer.clearAllResizeFrames();
      ((ViewGroup)this.mWorkspace.getParent()).removeAllViews();
      this.mWorkspace.removeAllViews();
      this.mWorkspace = null;
      this.mDragController = null;
      LauncherAnimUtils.onDestroyActivity();
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        Log.w("Launcher", "problem while stopping AppWidgetHost during Launcher destruction", localNullPointerException);
      }
    }
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mVisible = false;
    if (this.mAttached)
    {
      unregisterReceiver(this.mReceiver);
      this.mAttached = false;
    }
    updateRunning();
  }
  
  protected void onFinishBindingItems()
  {
    if ((this.mWorkspace != null) && (hasCustomContentToLeft()) && (this.mWorkspace.hasCustomContent())) {
      addCustomContentToLeft();
    }
  }
  
  protected void onInteractionBegin() {}
  
  protected void onInteractionEnd() {}
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getUnicodeChar();
    boolean bool = super.onKeyDown(paramInt, paramKeyEvent);
    int j;
    if ((i > 0) && (!Character.isWhitespace(i)))
    {
      j = 1;
      if ((bool) || (!acceptFilter()) || (j == 0) || (!TextKeyListener.getInstance().onKeyDown(this.mWorkspace, this.mDefaultKeySsb, paramInt, paramKeyEvent)) || (this.mDefaultKeySsb == null) || (this.mDefaultKeySsb.length() <= 0)) {
        break label95;
      }
      bool = onSearchRequested();
    }
    label95:
    while ((paramInt != 82) || (!paramKeyEvent.isLongPress()))
    {
      return bool;
      j = 0;
      break;
    }
    return true;
  }
  
  public boolean onLongClick(View paramView)
  {
    if (!isDraggingEnabled()) {}
    do
    {
      do
      {
        return false;
      } while ((isWorkspaceLocked()) || (this.mState != State.WORKSPACE));
      if ((!(paramView instanceof Workspace)) || (this.mWorkspace.isInOverviewMode())) {
        break;
      }
    } while (!this.mWorkspace.enterOverviewMode());
    this.mWorkspace.performHapticFeedback(0, 1);
    return true;
    if (!(paramView instanceof CellLayout)) {
      paramView = (View)paramView.getParent().getParent();
    }
    resetAddInfo();
    CellLayout.CellInfo localCellInfo = (CellLayout.CellInfo)paramView.getTag();
    if (localCellInfo == null) {
      return true;
    }
    View localView = localCellInfo.cell;
    int i;
    if ((isHotseatLayout(paramView)) || (this.mWorkspace.allowLongPress()))
    {
      i = 1;
      if ((i != 0) && (!this.mDragController.isDragging()))
      {
        if (localView != null) {
          break label196;
        }
        this.mWorkspace.performHapticFeedback(0, 1);
        if (!this.mWorkspace.isInOverviewMode()) {
          break label185;
        }
        this.mWorkspace.startReordering(paramView);
      }
    }
    for (;;)
    {
      return true;
      i = 0;
      break;
      label185:
      this.mWorkspace.enterOverviewMode();
      continue;
      label196:
      if (!(localView instanceof Folder)) {
        this.mWorkspace.startDrag(localCellInfo);
      }
    }
  }
  
  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    if ("android.intent.action.MAIN".equals(paramIntent.getAction()))
    {
      closeSystemDialogs();
      if ((!this.mHasFocus) || ((0x400000 & paramIntent.getFlags()) == 4194304)) {
        break label53;
      }
    }
    label53:
    for (int i = 1; this.mWorkspace == null; i = 0) {
      return;
    }
    Folder localFolder = this.mWorkspace.getOpenFolder();
    this.mWorkspace.exitWidgetResizeMode();
    if ((i != 0) && (this.mState == State.WORKSPACE) && (!this.mWorkspace.isTouchActive()) && (localFolder == null)) {
      this.mWorkspace.moveToDefaultScreen(true);
    }
    closeFolder();
    exitSpringLoadedDragMode();
    if (i != 0) {
      showWorkspace(true);
    }
    for (;;)
    {
      View localView = getWindow().peekDecorView();
      if ((localView != null) && (localView.getWindowToken() != null)) {
        ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(localView.getWindowToken(), 0);
      }
      if (this.mAppsCustomizeTabHost == null) {
        break;
      }
      this.mAppsCustomizeTabHost.reset();
      return;
      this.mOnResumeState = State.WORKSPACE;
    }
  }
  
  public void onPageBoundSynchronously(int paramInt)
  {
    this.mSynchronouslyBoundPages.add(Integer.valueOf(paramInt));
  }
  
  protected void onPause()
  {
    InstallShortcutReceiver.enableInstallQueue();
    super.onPause();
    this.mPaused = true;
    this.mDragController.cancelDrag();
    this.mDragController.resetLastGestureUpTime();
    if (this.mWorkspace.getCustomContentCallbacks() != null) {
      this.mWorkspace.getCustomContentCallbacks().onHide();
    }
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    super.onPrepareOptionsMenu(paramMenu);
    if (!this.mWorkspace.isInOverviewMode()) {
      this.mWorkspace.enterOverviewMode();
    }
    return false;
  }
  
  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    Iterator localIterator = this.mSynchronouslyBoundPages.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      this.mWorkspace.restoreInstanceStateForChild(i);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    if (this.mOnResumeState == State.WORKSPACE)
    {
      showWorkspace(false);
      this.mOnResumeState = State.NONE;
      if (this.mState != State.WORKSPACE) {
        break label172;
      }
    }
    label172:
    for (boolean bool = true;; bool = false)
    {
      setWorkspaceBackground(bool);
      this.mPaused = false;
      sPausedFromUserAction = false;
      if ((this.mRestoring) || (this.mOnResumeNeedsLoad))
      {
        this.mWorkspaceLoading = true;
        this.mModel.startLoader(true, -1);
        this.mRestoring = false;
        this.mOnResumeNeedsLoad = false;
      }
      if (this.mBindOnResumeCallbacks.size() <= 0) {
        break label199;
      }
      if (this.mAppsCustomizeContent != null) {
        this.mAppsCustomizeContent.setBulkBind(true);
      }
      for (int j = 0; j < this.mBindOnResumeCallbacks.size(); j++) {
        ((Runnable)this.mBindOnResumeCallbacks.get(j)).run();
      }
      if (this.mOnResumeState != State.APPS_CUSTOMIZE) {
        break;
      }
      showAllApps(false, AppsCustomizePagedView.ContentType.Applications, false);
      break;
    }
    if (this.mAppsCustomizeContent != null) {
      this.mAppsCustomizeContent.setBulkBind(false);
    }
    this.mBindOnResumeCallbacks.clear();
    label199:
    if (this.mOnResumeCallbacks.size() > 0)
    {
      for (int i = 0; i < this.mOnResumeCallbacks.size(); i++) {
        ((Runnable)this.mOnResumeCallbacks.get(i)).run();
      }
      this.mOnResumeCallbacks.clear();
    }
    if (this.mWaitingForResume != null) {
      this.mWaitingForResume.setStayPressed(false);
    }
    if (this.mAppsCustomizeContent != null) {
      this.mAppsCustomizeContent.resetDrawableState();
    }
    getWorkspace().reinflateWidgetsIfNecessary();
    InstallShortcutReceiver.disableAndFlushInstallQueue(this);
    updateVoiceButtonProxyVisible(false);
    updateGlobalIcons();
    if ((this.mWorkspace.getCustomContentCallbacks() != null) && (this.mWorkspace.isOnOrMovingToCustomContent())) {
      this.mWorkspace.getCustomContentCallbacks().onShow();
    }
    this.mWorkspace.updateInteractionForState();
    this.mWorkspace.onResume();
  }
  
  public Object onRetainNonConfigurationInstance()
  {
    this.mModel.stopLoader();
    if (this.mAppsCustomizeContent != null) {
      this.mAppsCustomizeContent.surrender();
    }
    return Boolean.TRUE;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    if (this.mWorkspace.getChildCount() > 0) {
      paramBundle.putInt("launcher.current_screen", this.mWorkspace.getRestorePage());
    }
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("launcher.state", this.mState.ordinal());
    closeFolder();
    if ((this.mPendingAddInfo.container != -1L) && (this.mPendingAddInfo.screenId > -1L) && (this.mWaitingForResult))
    {
      paramBundle.putLong("launcher.add_container", this.mPendingAddInfo.container);
      paramBundle.putLong("launcher.add_screen", this.mPendingAddInfo.screenId);
      paramBundle.putInt("launcher.add_cell_x", this.mPendingAddInfo.cellX);
      paramBundle.putInt("launcher.add_cell_y", this.mPendingAddInfo.cellY);
      paramBundle.putInt("launcher.add_span_x", this.mPendingAddInfo.spanX);
      paramBundle.putInt("launcher.add_span_y", this.mPendingAddInfo.spanY);
      paramBundle.putParcelable("launcher.add_widget_info", this.mPendingAddWidgetInfo);
      paramBundle.putInt("launcher.add_widget_id", this.mPendingAddWidgetId);
    }
    if ((this.mFolderInfo != null) && (this.mWaitingForResult))
    {
      paramBundle.putBoolean("launcher.rename_folder", true);
      paramBundle.putLong("launcher.rename_folder_id", this.mFolderInfo.id);
    }
    if (this.mAppsCustomizeTabHost != null)
    {
      String str = this.mAppsCustomizeTabHost.getCurrentTabTag();
      if (str != null) {
        paramBundle.putString("apps_customize_currentTab", str);
      }
      paramBundle.putInt("apps_customize_currentIndex", this.mAppsCustomizeContent.getSaveInstanceStateIndex());
    }
  }
  
  public boolean onSearchRequested()
  {
    startSearch(null, false, null, true);
    return true;
  }
  
  protected void onStart()
  {
    super.onStart();
    FirstFrameAnimatorHelper.setIsVisible(true);
  }
  
  protected void onStop()
  {
    super.onStop();
    FirstFrameAnimatorHelper.setIsVisible(false);
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public void onTrimMemory(int paramInt)
  {
    super.onTrimMemory(paramInt);
    if (paramInt >= 60) {
      this.mAppsCustomizeTabHost.onTrimMemory();
    }
  }
  
  protected void onUserLeaveHint()
  {
    super.onUserLeaveHint();
    sPausedFromUserAction = true;
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    this.mHasFocus = paramBoolean;
  }
  
  public void onWindowVisibilityChanged(int paramInt)
  {
    if (paramInt == 0) {}
    for (boolean bool = true;; bool = false)
    {
      this.mVisible = bool;
      updateRunning();
      if (this.mVisible)
      {
        this.mAppsCustomizeTabHost.onWindowVisible();
        if (!this.mWorkspaceLoading) {
          this.mWorkspace.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener()
          {
            private boolean mStarted = false;
            
            public void onDraw()
            {
              if (this.mStarted) {
                return;
              }
              this.mStarted = true;
              Launcher.this.mWorkspace.postDelayed(Launcher.this.mBuildLayersRunnable, 500L);
              Launcher.this.mWorkspace.post(new Runnable()
              {
                public void run()
                {
                  if ((Launcher.this.mWorkspace != null) && (Launcher.this.mWorkspace.getViewTreeObserver() != null)) {
                    Launcher.this.mWorkspace.getViewTreeObserver().removeOnDrawListener(jdField_this);
                  }
                }
              });
            }
          });
        }
        clearTypedText();
      }
      return;
    }
  }
  
  public void onWorkspaceShown(boolean paramBoolean) {}
  
  public void openFolder(FolderIcon paramFolderIcon)
  {
    Folder localFolder = paramFolderIcon.getFolder();
    localFolder.mInfo.opened = true;
    if (localFolder.getParent() == null)
    {
      this.mDragLayer.addView(localFolder);
      this.mDragController.addDropTarget(localFolder);
    }
    for (;;)
    {
      localFolder.animateOpen();
      growAndFadeOutFolderIcon(paramFolderIcon);
      localFolder.sendAccessibilityEvent(32);
      getDragLayer().sendAccessibilityEvent(2048);
      return;
      Log.w("Launcher", "Opening folder (" + localFolder + ") which already has a parent (" + localFolder.getParent() + ").");
    }
  }
  
  void processShortcut(Intent paramIntent)
  {
    String str1 = getResources().getString(2131361887);
    String str2 = paramIntent.getStringExtra("android.intent.extra.shortcut.NAME");
    if ((str1 != null) && (str1.equals(str2)))
    {
      Intent localIntent1 = new Intent("android.intent.action.MAIN", null);
      localIntent1.addCategory("android.intent.category.LAUNCHER");
      Intent localIntent2 = new Intent("android.intent.action.PICK_ACTIVITY");
      localIntent2.putExtra("android.intent.extra.INTENT", localIntent1);
      localIntent2.putExtra("android.intent.extra.TITLE", getText(2131361898));
      Utilities.startActivityForResultSafely(this, localIntent2, 6);
      return;
    }
    Utilities.startActivityForResultSafely(this, paramIntent, 1);
  }
  
  void processShortcutFromDrop(ComponentName paramComponentName, long paramLong1, long paramLong2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    resetAddInfo();
    this.mPendingAddInfo.container = paramLong1;
    this.mPendingAddInfo.screenId = paramLong2;
    this.mPendingAddInfo.dropPos = paramArrayOfInt2;
    if (paramArrayOfInt1 != null)
    {
      this.mPendingAddInfo.cellX = paramArrayOfInt1[0];
      this.mPendingAddInfo.cellY = paramArrayOfInt1[1];
    }
    Intent localIntent = new Intent("android.intent.action.CREATE_SHORTCUT");
    localIntent.setComponent(paramComponentName);
    processShortcut(localIntent);
  }
  
  public void removeAppWidget(LauncherAppWidgetInfo paramLauncherAppWidgetInfo)
  {
    removeWidgetToAutoAdvance(paramLauncherAppWidgetInfo.hostView);
    paramLauncherAppWidgetInfo.hostView = null;
  }
  
  void removeFolder(FolderInfo paramFolderInfo)
  {
    sFolders.remove(Long.valueOf(paramFolderInfo.id));
  }
  
  void removeWidgetToAutoAdvance(View paramView)
  {
    if (this.mWidgetsToAdvance.containsKey(paramView))
    {
      this.mWidgetsToAdvance.remove(paramView);
      updateRunning();
    }
  }
  
  public void resetQSBScroll()
  {
    this.mSearchDropTargetBar.animate().translationY(0.0F).start();
    getQsbBar().animate().translationY(0.0F).start();
  }
  
  public boolean setLoadOnResume()
  {
    if (this.mPaused)
    {
      Log.i("Launcher", "setLoadOnResume");
      this.mOnResumeNeedsLoad = true;
      return true;
    }
    return false;
  }
  
  void showAllApps(boolean paramBoolean1, AppsCustomizePagedView.ContentType paramContentType, boolean paramBoolean2)
  {
    if (this.mState != State.WORKSPACE) {
      return;
    }
    if (paramBoolean2) {
      this.mAppsCustomizeTabHost.reset();
    }
    showAppsCustomizeHelper(paramBoolean1, false, paramContentType);
    this.mAppsCustomizeTabHost.requestFocus();
    this.mState = State.APPS_CUSTOMIZE;
    this.mUserPresent = false;
    updateRunning();
    closeFolder();
    getWindow().getDecorView().sendAccessibilityEvent(32);
  }
  
  public void showFirstRunCling()
  {
    if ((isClingsEnabled()) && (!this.mSharedPrefs.getBoolean("cling_gel.first_run.dismissed", false)) && (!skipCustomClingIfNoAccounts()))
    {
      Cling localCling = (Cling)findViewById(2131296751);
      if (localCling != null)
      {
        String str1 = getFirstRunClingSearchBarHint();
        String str2 = getFirstRunCustomContentHint();
        if (!str1.isEmpty())
        {
          TextView localTextView = (TextView)localCling.findViewById(2131296593);
          localTextView.setText(str1);
          localTextView.setVisibility(0);
        }
        setCustomContentHintVisibility(localCling, str2, true, false);
      }
      initCling(2131296751, 0, false, true);
      return;
    }
    removeCling(2131296751);
  }
  
  public Cling showFirstRunFoldersCling()
  {
    if ((isClingsEnabled()) && (!this.mSharedPrefs.getBoolean("cling_gel.folder.dismissed", false))) {
      return initCling(2131296753, 2131296750, true, true);
    }
    removeCling(2131296753);
    return null;
  }
  
  public void showFirstRunWorkspaceCling()
  {
    if ((isClingsEnabled()) && (!this.mSharedPrefs.getBoolean("cling_gel.workspace.dismissed", false)))
    {
      initCling(2131296752, 0, false, true).setFocusedHotseatApp(getFirstRunFocusedHotseatAppDrawableId(), getFirstRunFocusedHotseatAppRank(), getFirstRunFocusedHotseatAppComponentName(), getFirstRunFocusedHotseatAppBubbleTitle(), getFirstRunFocusedHotseatAppBubbleDescription());
      return;
    }
    removeCling(2131296752);
  }
  
  void showHotseat(boolean paramBoolean)
  {
    if (!LauncherAppState.getInstance().isScreenLarge())
    {
      if (!paramBoolean) {
        break label62;
      }
      if (this.mHotseat.getAlpha() != 1.0F)
      {
        SearchDropTargetBar localSearchDropTargetBar = this.mSearchDropTargetBar;
        int i = 0;
        if (localSearchDropTargetBar != null) {
          i = this.mSearchDropTargetBar.getTransitionInDuration();
        }
        this.mHotseat.animate().alpha(1.0F).setDuration(i);
      }
    }
    return;
    label62:
    this.mHotseat.setAlpha(1.0F);
  }
  
  void showOutOfSpaceMessage(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 2131361892;; i = 2131361891)
    {
      Toast.makeText(this, getString(i), 0).show();
      return;
    }
  }
  
  void showOverviewMode(boolean paramBoolean)
  {
    this.mWorkspace.setVisibility(0);
    hideAppsCustomizeHelper(Workspace.State.OVERVIEW, paramBoolean, false, null);
    this.mState = State.WORKSPACE;
    onWorkspaceShown(paramBoolean);
  }
  
  protected void showWorkspace(boolean paramBoolean)
  {
    showWorkspace(paramBoolean, null);
  }
  
  void showWorkspace(boolean paramBoolean, Runnable paramRunnable)
  {
    if (this.mWorkspace.isInOverviewMode()) {
      this.mWorkspace.exitOverviewMode(paramBoolean);
    }
    if (this.mState != State.WORKSPACE) {
      if (this.mState == State.WORKSPACE) {
        break label144;
      }
    }
    label144:
    for (int i = 1;; i = 0)
    {
      this.mWorkspace.setVisibility(0);
      hideAppsCustomizeHelper(Workspace.State.NORMAL, paramBoolean, false, paramRunnable);
      if (this.mSearchDropTargetBar != null)
      {
        SearchDropTargetBar localSearchDropTargetBar = this.mSearchDropTargetBar;
        boolean bool = false;
        if (paramBoolean)
        {
          bool = false;
          if (i != 0) {
            bool = true;
          }
        }
        localSearchDropTargetBar.showSearchBar(bool);
      }
      if (this.mAllAppsButton != null) {
        this.mAllAppsButton.requestFocus();
      }
      this.mState = State.WORKSPACE;
      this.mUserPresent = true;
      updateRunning();
      getWindow().getDecorView().sendAccessibilityEvent(32);
      onWorkspaceShown(paramBoolean);
      return;
    }
  }
  
  boolean startActivity(View paramView, Intent paramIntent, Object paramObject)
  {
    paramIntent.addFlags(268435456);
    if (paramView != null) {}
    for (;;)
    {
      try
      {
        if (paramIntent.hasExtra("com.android.launcher3.intent.extra.shortcut.INGORE_LAUNCH_ANIMATION")) {
          break label141;
        }
        i = 1;
        if (i != 0) {
          startActivity(paramIntent, ActivityOptions.makeScaleUpAnimation(paramView, 0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight()).toBundle());
        } else {
          startActivity(paramIntent);
        }
      }
      catch (SecurityException localSecurityException)
      {
        Toast.makeText(this, 2131361872, 0).show();
        Log.e("Launcher", "Launcher does not have the permission to launch " + paramIntent + ". Make sure to create a MAIN intent-filter for the corresponding activity " + "or use the exported attribute for this activity. " + "tag=" + paramObject + " intent=" + paramIntent, localSecurityException);
        return false;
      }
      return true;
      label141:
      int i = 0;
    }
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if (paramInt >= 0) {
      this.mWaitingForResult = true;
    }
    super.startActivityForResult(paramIntent, paramInt);
  }
  
  boolean startActivitySafely(View paramView, Intent paramIntent, Object paramObject)
  {
    try
    {
      boolean bool = startActivity(paramView, paramIntent, paramObject);
      return bool;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Toast.makeText(this, 2131361872, 0).show();
      Log.e("Launcher", "Unable to launch. tag=" + paramObject + " intent=" + paramIntent, localActivityNotFoundException);
    }
    return false;
  }
  
  void startApplicationDetailsActivity(ComponentName paramComponentName)
  {
    Intent localIntent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", paramComponentName.getPackageName(), null));
    localIntent.setFlags(276824064);
    startActivitySafely(null, localIntent, "startApplicationDetailsActivity");
  }
  
  boolean startApplicationUninstallActivity(ComponentName paramComponentName, int paramInt)
  {
    if ((paramInt & 0x1) == 0)
    {
      Toast.makeText(this, 2131361927, 0).show();
      return false;
    }
    Intent localIntent = new Intent("android.intent.action.DELETE", Uri.fromParts("package", paramComponentName.getPackageName(), paramComponentName.getClassName()));
    localIntent.setFlags(276824064);
    startActivity(localIntent);
    return true;
  }
  
  public void startBinding()
  {
    this.mBindOnResumeCallbacks.clear();
    this.mWorkspace.clearDropTargets();
    this.mWorkspace.removeAllWorkspaceScreens();
    this.mWidgetsToAdvance.clear();
    if (this.mHotseat != null) {
      this.mHotseat.resetLayout();
    }
  }
  
  public void startSearch(String paramString, boolean paramBoolean, Bundle paramBundle, Rect paramRect)
  {
    startGlobalSearch(paramString, paramBoolean, paramBundle, paramRect);
  }
  
  public void startSearch(String paramString, boolean paramBoolean1, Bundle paramBundle, boolean paramBoolean2)
  {
    showWorkspace(true);
    if (paramString == null) {
      paramString = getTypedText();
    }
    if (paramBundle == null)
    {
      paramBundle = new Bundle();
      paramBundle.putString("source", "launcher-search");
    }
    Rect localRect = new Rect();
    if (this.mSearchDropTargetBar != null) {
      localRect = this.mSearchDropTargetBar.getSearchBarBounds();
    }
    startSearch(paramString, paramBoolean1, paramBundle, localRect);
  }
  
  protected void startSettings() {}
  
  public void startVoice()
  {
    try
    {
      ComponentName localComponentName = ((SearchManager)getSystemService("search")).getGlobalSearchActivity();
      Intent localIntent2 = new Intent("android.speech.action.WEB_SEARCH");
      localIntent2.setFlags(268435456);
      if (localComponentName != null) {
        localIntent2.setPackage(localComponentName.getPackageName());
      }
      startActivity(null, localIntent2, "onClickVoiceButton");
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Intent localIntent1 = new Intent("android.speech.action.WEB_SEARCH");
      localIntent1.setFlags(268435456);
      startActivitySafely(null, localIntent1, "onClickVoiceButton");
    }
  }
  
  protected void startWallpaper()
  {
    Intent localIntent = new Intent("android.intent.action.SET_WALLPAPER");
    localIntent.setComponent(getWallpaperPickerComponent());
    startActivityForResult(localIntent, 10);
  }
  
  public void unlockScreenOrientation(boolean paramBoolean)
  {
    if (isRotationEnabled())
    {
      if (paramBoolean) {
        setRequestedOrientation(-1);
      }
    }
    else {
      return;
    }
    this.mHandler.postDelayed(new Runnable()
    {
      public void run()
      {
        Launcher.this.setRequestedOrientation(-1);
      }
    }, 500L);
  }
  
  public void updateCustomContentHintVisibility()
  {
    Cling localCling = (Cling)findViewById(2131296751);
    String str = getFirstRunCustomContentHint();
    if (this.mWorkspace.hasCustomContent()) {
      if (localCling != null) {
        setCustomContentHintVisibility(localCling, str, true, true);
      }
    }
    while (localCling == null) {
      return;
    }
    setCustomContentHintVisibility(localCling, str, false, true);
  }
  
  protected void updateGlobalSearchIcon(Drawable.ConstantState paramConstantState)
  {
    View localView = findViewById(2131296956);
    ImageView localImageView = (ImageView)findViewById(2131296955);
    updateButtonWithDrawable(2131296955, paramConstantState);
    invalidatePressedFocusedStates(localView, localImageView);
  }
  
  protected boolean updateGlobalSearchIcon()
  {
    View localView1 = findViewById(2131296956);
    ImageView localImageView = (ImageView)findViewById(2131296955);
    View localView2 = findViewById(2131296957);
    View localView3 = findViewById(2131296757);
    ComponentName localComponentName = ((SearchManager)getSystemService("search")).getGlobalSearchActivity();
    if (localComponentName != null)
    {
      int i = getCurrentOrientationIndexForGlobalIcons();
      sGlobalSearchIcon[i] = updateButtonWithIconFromExternalActivity(2131296955, localComponentName, 2130837769, "com.android.launcher.toolbar_search_icon");
      if (sGlobalSearchIcon[i] == null) {
        sGlobalSearchIcon[i] = updateButtonWithIconFromExternalActivity(2131296955, localComponentName, 2130837769, "com.android.launcher.toolbar_icon");
      }
      if (localView1 != null) {
        localView1.setVisibility(0);
      }
      localImageView.setVisibility(0);
      invalidatePressedFocusedStates(localView1, localImageView);
      return true;
    }
    if (localView1 != null) {
      localView1.setVisibility(8);
    }
    if (localView2 != null) {
      localView2.setVisibility(8);
    }
    if (localImageView != null) {
      localImageView.setVisibility(8);
    }
    if (localView3 != null) {
      localView3.setVisibility(8);
    }
    updateVoiceButtonProxyVisible(false);
    return false;
  }
  
  public void updateVoiceButtonProxyVisible(boolean paramBoolean)
  {
    View localView = findViewById(2131296756);
    int i;
    int j;
    if (localView != null)
    {
      if ((paramBoolean) || (!this.mWorkspace.shouldVoiceButtonProxyBeVisible())) {
        break label46;
      }
      i = 1;
      j = 0;
      if (i == 0) {
        break label51;
      }
    }
    for (;;)
    {
      localView.setVisibility(j);
      localView.bringToFront();
      return;
      label46:
      i = 0;
      break;
      label51:
      j = 8;
    }
  }
  
  protected void updateVoiceSearchIcon(Drawable.ConstantState paramConstantState)
  {
    View localView1 = findViewById(2131296957);
    View localView2 = findViewById(2131296757);
    updateButtonWithDrawable(2131296757, paramConstantState);
    invalidatePressedFocusedStates(localView1, localView2);
  }
  
  protected boolean updateVoiceSearchIcon(boolean paramBoolean)
  {
    View localView1 = findViewById(2131296957);
    View localView2 = findViewById(2131296757);
    ComponentName localComponentName1 = ((SearchManager)getSystemService("search")).getGlobalSearchActivity();
    ComponentName localComponentName2 = null;
    if (localComponentName1 != null)
    {
      Intent localIntent = new Intent("android.speech.action.WEB_SEARCH");
      localIntent.setPackage(localComponentName1.getPackageName());
      localComponentName2 = localIntent.resolveActivity(getPackageManager());
    }
    if (localComponentName2 == null) {
      localComponentName2 = new Intent("android.speech.action.WEB_SEARCH").resolveActivity(getPackageManager());
    }
    if ((paramBoolean) && (localComponentName2 != null))
    {
      int i = getCurrentOrientationIndexForGlobalIcons();
      sVoiceSearchIcon[i] = updateButtonWithIconFromExternalActivity(2131296757, localComponentName2, 2130837771, "com.android.launcher.toolbar_voice_search_icon");
      if (sVoiceSearchIcon[i] == null) {
        sVoiceSearchIcon[i] = updateButtonWithIconFromExternalActivity(2131296757, localComponentName2, 2130837771, "com.android.launcher.toolbar_icon");
      }
      if (localView1 != null) {
        localView1.setVisibility(0);
      }
      localView2.setVisibility(0);
      updateVoiceButtonProxyVisible(false);
      invalidatePressedFocusedStates(localView1, localView2);
      return true;
    }
    if (localView1 != null) {
      localView1.setVisibility(8);
    }
    if (localView2 != null) {
      localView2.setVisibility(8);
    }
    updateVoiceButtonProxyVisible(false);
    return false;
  }
  
  private class AppWidgetResetObserver
    extends ContentObserver
  {
    public AppWidgetResetObserver()
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      Launcher.this.onAppWidgetReset();
    }
  }
  
  private class CloseSystemDialogsIntentReceiver
    extends BroadcastReceiver
  {
    private CloseSystemDialogsIntentReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Launcher.this.closeSystemDialogs();
    }
  }
  
  public static abstract interface CustomContentCallbacks
  {
    public abstract void onHide();
    
    public abstract void onScrollProgressChanged(float paramFloat);
    
    public abstract void onShow();
  }
  
  private static class LocaleConfiguration
  {
    public String locale;
    public int mcc = -1;
    public int mnc = -1;
  }
  
  private static class PendingAddArguments
  {
    int cellX;
    int cellY;
    long container;
    Intent intent;
    int requestCode;
    long screenId;
  }
  
  public static abstract interface QSBScroller {}
  
  private static enum State
  {
    static
    {
      APPS_CUSTOMIZE = new State("APPS_CUSTOMIZE", 2);
      APPS_CUSTOMIZE_SPRING_LOADED = new State("APPS_CUSTOMIZE_SPRING_LOADED", 3);
      State[] arrayOfState = new State[4];
      arrayOfState[0] = NONE;
      arrayOfState[1] = WORKSPACE;
      arrayOfState[2] = APPS_CUSTOMIZE;
      arrayOfState[3] = APPS_CUSTOMIZE_SPRING_LOADED;
      $VALUES = arrayOfState;
    }
    
    private State() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Launcher
 * JD-Core Version:    0.7.0.1
 */