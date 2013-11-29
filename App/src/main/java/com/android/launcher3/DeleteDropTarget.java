package com.android.launcher3;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DeleteDropTarget
  extends ButtonDropTarget
{
  private static int DELETE_ANIMATION_DURATION = 285;
  private static int FLING_DELETE_ANIMATION_DURATION = 350;
  private static float FLING_TO_DELETE_FRICTION = 0.035F;
  private static int MODE_FLING_DELETE_ALONG_VECTOR = 1;
  private static int MODE_FLING_DELETE_TO_TRASH = 0;
  private TransitionDrawable mCurrentDrawable;
  private final int mFlingDeleteMode = MODE_FLING_DELETE_ALONG_VECTOR;
  private ColorStateList mOriginalTextColor;
  private TransitionDrawable mRemoveDrawable;
  private TransitionDrawable mUninstallDrawable;
  private boolean mWaitingForUninstall = false;
  
  public DeleteDropTarget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DeleteDropTarget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void animateToTrashAndCompleteDrop(final DropTarget.DragObject paramDragObject)
  {
    DragLayer localDragLayer = this.mLauncher.getDragLayer();
    Rect localRect1 = new Rect();
    localDragLayer.getViewRectRelativeToSelf(paramDragObject.dragView, localRect1);
    Rect localRect2 = getIconRect(paramDragObject.dragView.getMeasuredWidth(), paramDragObject.dragView.getMeasuredHeight(), this.mCurrentDrawable.getIntrinsicWidth(), this.mCurrentDrawable.getIntrinsicHeight());
    float f = localRect2.width() / localRect1.width();
    this.mSearchDropTargetBar.deferOnDragEnd();
    deferCompleteDropIfUninstalling(paramDragObject);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        DeleteDropTarget.this.completeDrop(paramDragObject);
        DeleteDropTarget.this.mSearchDropTargetBar.onDragEnd();
        DeleteDropTarget.this.mLauncher.exitSpringLoadedDragMode();
      }
    };
    localDragLayer.animateView(paramDragObject.dragView, localRect1, localRect2, f, 1.0F, 1.0F, 0.1F, 0.1F, DELETE_ANIMATION_DURATION, new DecelerateInterpolator(2.0F), new LinearInterpolator(), local1, 0, null);
  }
  
  private void completeDrop(DropTarget.DragObject paramDragObject)
  {
    ItemInfo localItemInfo = (ItemInfo)paramDragObject.dragInfo;
    boolean bool = this.mWaitingForUninstall;
    this.mWaitingForUninstall = false;
    if (isAllAppsApplication(paramDragObject.dragSource, localItemInfo))
    {
      AppInfo localAppInfo = (AppInfo)localItemInfo;
      this.mLauncher.startApplicationUninstallActivity(localAppInfo.componentName, localAppInfo.flags);
      if ((bool) && (!this.mWaitingForUninstall))
      {
        if (!(paramDragObject.dragSource instanceof Folder)) {
          break label322;
        }
        ((Folder)paramDragObject.dragSource).onUninstallActivityReturned(false);
      }
    }
    label322:
    while (!(paramDragObject.dragSource instanceof Workspace))
    {
      return;
      if (isUninstallFromWorkspace(paramDragObject))
      {
        ShortcutInfo localShortcutInfo = (ShortcutInfo)localItemInfo;
        if ((localShortcutInfo.intent == null) || (localShortcutInfo.intent.getComponent() == null)) {
          break;
        }
        final ComponentName localComponentName = localShortcutInfo.intent.getComponent();
        final DragSource localDragSource = paramDragObject.dragSource;
        int i = AppInfo.initFlags(ShortcutInfo.getPackageInfo(getContext(), localComponentName.getPackageName()));
        this.mWaitingForUninstall = this.mLauncher.startApplicationUninstallActivity(localComponentName, i);
        if (!this.mWaitingForUninstall) {
          break;
        }
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            DeleteDropTarget.access$102(DeleteDropTarget.this, false);
            String str = localComponentName.getPackageName();
            int i = AllAppsList.findActivitiesForPackage(DeleteDropTarget.this.getContext(), str).size();
            boolean bool = false;
            if (i == 0) {
              bool = true;
            }
            if ((localDragSource instanceof Folder)) {
              ((Folder)localDragSource).onUninstallActivityReturned(bool);
            }
            while (!(localDragSource instanceof Workspace)) {
              return;
            }
            ((Workspace)localDragSource).onUninstallActivityReturned(bool);
          }
        };
        this.mLauncher.addOnResumeCallback(local2);
        break;
      }
      if (isWorkspaceOrFolderApplication(paramDragObject))
      {
        LauncherModel.deleteItemFromDatabase(this.mLauncher, localItemInfo);
        break;
      }
      if (isWorkspaceFolder(paramDragObject))
      {
        FolderInfo localFolderInfo = (FolderInfo)localItemInfo;
        this.mLauncher.removeFolder(localFolderInfo);
        LauncherModel.deleteFolderContentsFromDatabase(this.mLauncher, localFolderInfo);
        break;
      }
      if (!isWorkspaceOrFolderWidget(paramDragObject)) {
        break;
      }
      this.mLauncher.removeAppWidget((LauncherAppWidgetInfo)localItemInfo);
      LauncherModel.deleteItemFromDatabase(this.mLauncher, localItemInfo);
      final LauncherAppWidgetInfo localLauncherAppWidgetInfo = (LauncherAppWidgetInfo)localItemInfo;
      final LauncherAppWidgetHost localLauncherAppWidgetHost = this.mLauncher.getAppWidgetHost();
      if (localLauncherAppWidgetHost == null) {
        break;
      }
      new Thread("deleteAppWidgetId")
      {
        public void run()
        {
          localLauncherAppWidgetHost.deleteAppWidgetId(localLauncherAppWidgetInfo.appWidgetId);
        }
      }.start();
      break;
    }
    ((Workspace)paramDragObject.dragSource).onUninstallActivityReturned(false);
  }
  
  private ValueAnimator.AnimatorUpdateListener createFlingAlongVectorAnimatorListener(DragLayer paramDragLayer, DropTarget.DragObject paramDragObject, PointF paramPointF, long paramLong, int paramInt, ViewConfiguration paramViewConfiguration)
  {
    Rect localRect = new Rect();
    paramDragLayer.getViewRectRelativeToSelf(paramDragObject.dragView, localRect);
    return new FlingAlongVectorAnimatorUpdateListener(paramDragLayer, paramPointF, localRect, paramLong, FLING_TO_DELETE_FRICTION);
  }
  
  private ValueAnimator.AnimatorUpdateListener createFlingToTrashAnimatorListener(final DragLayer paramDragLayer, DropTarget.DragObject paramDragObject, PointF paramPointF, ViewConfiguration paramViewConfiguration)
  {
    Rect localRect1 = getIconRect(paramDragObject.dragView.getMeasuredWidth(), paramDragObject.dragView.getMeasuredHeight(), this.mCurrentDrawable.getIntrinsicWidth(), this.mCurrentDrawable.getIntrinsicHeight());
    Rect localRect2 = new Rect();
    paramDragLayer.getViewRectRelativeToSelf(paramDragObject.dragView, localRect2);
    int i = (int)(Math.min(1.0F, Math.abs(paramPointF.length()) / (paramViewConfiguration.getScaledMaximumFlingVelocity() / 2.0F)) * -localRect2.top);
    int j = (int)(i / (paramPointF.y / paramPointF.x));
    final float f1 = i + localRect2.top;
    final float f2 = j + localRect2.left;
    final float f3 = localRect2.left;
    final float f4 = localRect2.top;
    final float f5 = localRect1.left;
    final float f6 = localRect1.top;
    new ValueAnimator.AnimatorUpdateListener()
    {
      public float getInterpolation(float paramAnonymousFloat)
      {
        return paramAnonymousFloat * (paramAnonymousFloat * (paramAnonymousFloat * (paramAnonymousFloat * (paramAnonymousFloat * (paramAnonymousFloat * (paramAnonymousFloat * paramAnonymousFloat))))));
      }
    }
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        DragView localDragView = (DragView)paramDragLayer.getAnimatedView();
        float f1 = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        float f2 = this.val$scaleAlphaInterpolator.getInterpolation(f1);
        float f3 = localDragView.getInitialScale();
        float f4 = localDragView.getScaleX();
        float f5 = (1.0F - f4) * localDragView.getMeasuredWidth() / 2.0F;
        float f6 = (1.0F - f4) * localDragView.getMeasuredHeight() / 2.0F;
        float f7 = (1.0F - f1) * (1.0F - f1) * (f3 - f5) + f1 * (2.0F * (1.0F - f1)) * (f2 - f5) + f1 * f1 * f5;
        float f8 = (1.0F - f1) * (1.0F - f1) * (f4 - f6) + f1 * (2.0F * (1.0F - f1)) * (f1 - f5) + f1 * f1 * f6;
        localDragView.setTranslationX(f7);
        localDragView.setTranslationY(f8);
        localDragView.setScaleX(f3 * (1.0F - f2));
        localDragView.setScaleY(f3 * (1.0F - f2));
        localDragView.setAlpha(0.5F + (1.0F - 0.5F) * (1.0F - f2));
      }
    };
  }
  
  private void deferCompleteDropIfUninstalling(DropTarget.DragObject paramDragObject)
  {
    this.mWaitingForUninstall = false;
    if (isUninstallFromWorkspace(paramDragObject))
    {
      if (!(paramDragObject.dragSource instanceof Folder)) {
        break label39;
      }
      ((Folder)paramDragObject.dragSource).deferCompleteDropAfterUninstallActivity();
    }
    for (;;)
    {
      this.mWaitingForUninstall = true;
      return;
      label39:
      if ((paramDragObject.dragSource instanceof Workspace)) {
        ((Workspace)paramDragObject.dragSource).deferCompleteDropAfterUninstallActivity();
      }
    }
  }
  
  private boolean isAllAppsApplication(DragSource paramDragSource, Object paramObject)
  {
    return ((paramDragSource instanceof AppsCustomizePagedView)) && ((paramObject instanceof AppInfo));
  }
  
  private boolean isAllAppsWidget(DragSource paramDragSource, Object paramObject)
  {
    if (((paramDragSource instanceof AppsCustomizePagedView)) && ((paramObject instanceof PendingAddItemInfo))) {}
    switch (((PendingAddItemInfo)paramObject).itemType)
    {
    case 2: 
    case 3: 
    default: 
      return false;
    }
    return true;
  }
  
  private boolean isDragSourceWorkspaceOrFolder(DropTarget.DragObject paramDragObject)
  {
    return ((paramDragObject.dragSource instanceof Workspace)) || ((paramDragObject.dragSource instanceof Folder));
  }
  
  private boolean isUninstallFromWorkspace(DropTarget.DragObject paramDragObject)
  {
    if ((AppsCustomizePagedView.DISABLE_ALL_APPS) && (isWorkspaceOrFolderApplication(paramDragObject)))
    {
      ShortcutInfo localShortcutInfo = (ShortcutInfo)paramDragObject.dragInfo;
      if ((localShortcutInfo.intent != null) && (localShortcutInfo.intent.getComponent() != null))
      {
        Set localSet = localShortcutInfo.intent.getCategories();
        boolean bool1 = false;
        if (localSet != null)
        {
          Iterator localIterator = localSet.iterator();
          do
          {
            boolean bool2 = localIterator.hasNext();
            bool1 = false;
            if (!bool2) {
              break;
            }
          } while (!((String)localIterator.next()).equals("android.intent.category.LAUNCHER"));
          bool1 = true;
        }
        return bool1;
      }
    }
    return false;
  }
  
  private boolean isWorkspaceFolder(DropTarget.DragObject paramDragObject)
  {
    return ((paramDragObject.dragSource instanceof Workspace)) && ((paramDragObject.dragInfo instanceof FolderInfo));
  }
  
  private boolean isWorkspaceOrFolderApplication(DropTarget.DragObject paramDragObject)
  {
    return (isDragSourceWorkspaceOrFolder(paramDragObject)) && ((paramDragObject.dragInfo instanceof ShortcutInfo));
  }
  
  private boolean isWorkspaceOrFolderWidget(DropTarget.DragObject paramDragObject)
  {
    return (isDragSourceWorkspaceOrFolder(paramDragObject)) && ((paramDragObject.dragInfo instanceof LauncherAppWidgetInfo));
  }
  
  private void resetHoverColor()
  {
    this.mCurrentDrawable.resetTransition();
    setTextColor(this.mOriginalTextColor);
  }
  
  private void setHoverColor()
  {
    this.mCurrentDrawable.startTransition(this.mTransitionDuration);
    setTextColor(this.mHoverColor);
  }
  
  public static boolean willAcceptDrop(Object paramObject)
  {
    if ((paramObject instanceof ItemInfo))
    {
      ItemInfo localItemInfo = (ItemInfo)paramObject;
      if ((localItemInfo.itemType == 4) || (localItemInfo.itemType == 1)) {}
      do
      {
        do
        {
          do
          {
            return true;
          } while ((!AppsCustomizePagedView.DISABLE_ALL_APPS) && (localItemInfo.itemType == 2));
          if ((AppsCustomizePagedView.DISABLE_ALL_APPS) || (localItemInfo.itemType != 0) || (!(localItemInfo instanceof AppInfo))) {
            break;
          }
        } while ((0x1 & ((AppInfo)paramObject).flags) != 0);
        return false;
        if ((localItemInfo.itemType != 0) || (!(localItemInfo instanceof ShortcutInfo))) {
          break;
        }
      } while ((!AppsCustomizePagedView.DISABLE_ALL_APPS) || ((0x1 & ((ShortcutInfo)paramObject).flags) != 0));
      return false;
    }
    return false;
  }
  
  public boolean acceptDrop(DropTarget.DragObject paramDragObject)
  {
    return willAcceptDrop(paramDragObject.dragInfo);
  }
  
  public void onDragEnd()
  {
    super.onDragEnd();
    this.mActive = false;
  }
  
  public void onDragEnter(DropTarget.DragObject paramDragObject)
  {
    super.onDragEnter(paramDragObject);
    setHoverColor();
  }
  
  public void onDragExit(DropTarget.DragObject paramDragObject)
  {
    super.onDragExit(paramDragObject);
    if (!paramDragObject.dragComplete)
    {
      resetHoverColor();
      return;
    }
    paramDragObject.dragView.setColor(this.mHoverColor);
  }
  
  public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt)
  {
    boolean bool = true;
    int i;
    label56:
    int j;
    if ((!AppsCustomizePagedView.DISABLE_ALL_APPS) && (isAllAppsApplication(paramDragSource, paramObject)))
    {
      i = 1;
      if ((!willAcceptDrop(paramObject)) || (isAllAppsWidget(paramDragSource, paramObject))) {
        bool = false;
      }
      if (i == 0) {
        break label136;
      }
      setCompoundDrawablesRelativeWithIntrinsicBounds(this.mUninstallDrawable, null, null, null);
      this.mCurrentDrawable = ((TransitionDrawable)getCurrentDrawable());
      this.mActive = bool;
      resetHoverColor();
      ViewGroup localViewGroup = (ViewGroup)getParent();
      j = 0;
      if (!bool) {
        break label150;
      }
      label94:
      localViewGroup.setVisibility(j);
      if (getText().length() > 0) {
        if (i == 0) {
          break label157;
        }
      }
    }
    label136:
    label150:
    label157:
    for (int k = 2131361904;; k = 2131361903)
    {
      setText(k);
      return;
      i = 0;
      break;
      setCompoundDrawablesRelativeWithIntrinsicBounds(this.mRemoveDrawable, null, null, null);
      break label56;
      j = 8;
      break label94;
    }
  }
  
  public void onDrop(DropTarget.DragObject paramDragObject)
  {
    animateToTrashAndCompleteDrop(paramDragObject);
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mOriginalTextColor = getTextColors();
    Resources localResources = getResources();
    this.mHoverColor = localResources.getColor(2131230758);
    this.mUninstallDrawable = ((TransitionDrawable)localResources.getDrawable(2130838101));
    this.mRemoveDrawable = ((TransitionDrawable)localResources.getDrawable(2130838033));
    this.mRemoveDrawable.setCrossFadeEnabled(true);
    this.mUninstallDrawable.setCrossFadeEnabled(true);
    this.mCurrentDrawable = ((TransitionDrawable)getCurrentDrawable());
    if ((getResources().getConfiguration().orientation == 2) && (!LauncherAppState.getInstance().isScreenLarge())) {
      setText("");
    }
  }
  
  public void onFlingToDelete(final DropTarget.DragObject paramDragObject, int paramInt1, int paramInt2, PointF paramPointF)
  {
    final boolean bool = paramDragObject.dragSource instanceof AppsCustomizePagedView;
    paramDragObject.dragView.setColor(0);
    paramDragObject.dragView.updateInitialScaleToCurrentScale();
    if (bool) {
      resetHoverColor();
    }
    if (this.mFlingDeleteMode == MODE_FLING_DELETE_TO_TRASH)
    {
      this.mSearchDropTargetBar.deferOnDragEnd();
      this.mSearchDropTargetBar.finishAnimations();
    }
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(this.mLauncher);
    DragLayer localDragLayer = this.mLauncher.getDragLayer();
    int i = FLING_DELETE_ANIMATION_DURATION;
    final long l = AnimationUtils.currentAnimationTimeMillis();
    TimeInterpolator local6 = new TimeInterpolator()
    {
      private int mCount = -1;
      private float mOffset = 0.0F;
      
      public float getInterpolation(float paramAnonymousFloat)
      {
        if (this.mCount < 0) {
          this.mCount = (1 + this.mCount);
        }
        for (;;)
        {
          return Math.min(1.0F, paramAnonymousFloat + this.mOffset);
          if (this.mCount == 0)
          {
            this.mOffset = Math.min(0.5F, (float)(AnimationUtils.currentAnimationTimeMillis() - l) / this.val$duration);
            this.mCount = (1 + this.mCount);
          }
        }
      }
    };
    ValueAnimator.AnimatorUpdateListener localAnimatorUpdateListener;
    if (this.mFlingDeleteMode == MODE_FLING_DELETE_TO_TRASH) {
      localAnimatorUpdateListener = createFlingToTrashAnimatorListener(localDragLayer, paramDragObject, paramPointF, localViewConfiguration);
    }
    for (;;)
    {
      deferCompleteDropIfUninstalling(paramDragObject);
      Runnable local7 = new Runnable()
      {
        public void run()
        {
          if (!bool)
          {
            DeleteDropTarget.this.mLauncher.exitSpringLoadedDragMode();
            DeleteDropTarget.this.completeDrop(paramDragObject);
          }
          DeleteDropTarget.this.mLauncher.getDragController().onDeferredEndFling(paramDragObject);
        }
      };
      localDragLayer.animateView(paramDragObject.dragView, localAnimatorUpdateListener, i, local6, local7, 0, null);
      return;
      int j = this.mFlingDeleteMode;
      int k = MODE_FLING_DELETE_ALONG_VECTOR;
      localAnimatorUpdateListener = null;
      if (j == k) {
        localAnimatorUpdateListener = createFlingAlongVectorAnimatorListener(localDragLayer, paramDragObject, paramPointF, l, i, localViewConfiguration);
      }
    }
  }
  
  private static class FlingAlongVectorAnimatorUpdateListener
    implements ValueAnimator.AnimatorUpdateListener
  {
    private final TimeInterpolator mAlphaInterpolator = new DecelerateInterpolator(0.75F);
    private DragLayer mDragLayer;
    private float mFriction;
    private Rect mFrom;
    private boolean mHasOffsetForScale;
    private long mPrevTime;
    private PointF mVelocity;
    
    public FlingAlongVectorAnimatorUpdateListener(DragLayer paramDragLayer, PointF paramPointF, Rect paramRect, long paramLong, float paramFloat)
    {
      this.mDragLayer = paramDragLayer;
      this.mVelocity = paramPointF;
      this.mFrom = paramRect;
      this.mPrevTime = paramLong;
      this.mFriction = (1.0F - paramFloat * paramDragLayer.getResources().getDisplayMetrics().density);
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      DragView localDragView = (DragView)this.mDragLayer.getAnimatedView();
      float f1 = ((Float)paramValueAnimator.getAnimatedValue()).floatValue();
      long l = AnimationUtils.currentAnimationTimeMillis();
      if (!this.mHasOffsetForScale)
      {
        this.mHasOffsetForScale = true;
        float f2 = localDragView.getScaleX();
        float f3 = (f2 - 1.0F) * localDragView.getMeasuredWidth() / 2.0F;
        float f4 = (f2 - 1.0F) * localDragView.getMeasuredHeight() / 2.0F;
        Rect localRect3 = this.mFrom;
        localRect3.left = ((int)(f3 + localRect3.left));
        Rect localRect4 = this.mFrom;
        localRect4.top = ((int)(f4 + localRect4.top));
      }
      Rect localRect1 = this.mFrom;
      localRect1.left = ((int)(localRect1.left + this.mVelocity.x * (float)(l - this.mPrevTime) / 1000.0F));
      Rect localRect2 = this.mFrom;
      localRect2.top = ((int)(localRect2.top + this.mVelocity.y * (float)(l - this.mPrevTime) / 1000.0F));
      localDragView.setTranslationX(this.mFrom.left);
      localDragView.setTranslationY(this.mFrom.top);
      localDragView.setAlpha(1.0F - this.mAlphaInterpolator.getInterpolation(f1));
      PointF localPointF1 = this.mVelocity;
      localPointF1.x *= this.mFriction;
      PointF localPointF2 = this.mVelocity;
      localPointF2.y *= this.mFriction;
      this.mPrevTime = l;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DeleteDropTarget
 * JD-Core Version:    0.7.0.1
 */