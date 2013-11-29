package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class InfoDropTarget
  extends ButtonDropTarget
{
  private TransitionDrawable mDrawable;
  private ColorStateList mOriginalTextColor;
  
  public InfoDropTarget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public InfoDropTarget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private boolean isFromAllApps(DragSource paramDragSource)
  {
    return paramDragSource instanceof AppsCustomizePagedView;
  }
  
  public boolean acceptDrop(DropTarget.DragObject paramDragObject)
  {
    ComponentName localComponentName;
    if ((paramDragObject.dragInfo instanceof AppInfo)) {
      localComponentName = ((AppInfo)paramDragObject.dragInfo).componentName;
    }
    for (;;)
    {
      if (localComponentName != null) {
        this.mLauncher.startApplicationDetailsActivity(localComponentName);
      }
      paramDragObject.deferDragViewCleanupPostAnimation = false;
      return false;
      if ((paramDragObject.dragInfo instanceof ShortcutInfo))
      {
        localComponentName = ((ShortcutInfo)paramDragObject.dragInfo).intent.getComponent();
      }
      else
      {
        boolean bool = paramDragObject.dragInfo instanceof PendingAddItemInfo;
        localComponentName = null;
        if (bool) {
          localComponentName = ((PendingAddItemInfo)paramDragObject.dragInfo).componentName;
        }
      }
    }
  }
  
  public void onDragEnd()
  {
    super.onDragEnd();
    this.mActive = false;
  }
  
  public void onDragEnter(DropTarget.DragObject paramDragObject)
  {
    super.onDragEnter(paramDragObject);
    this.mDrawable.startTransition(this.mTransitionDuration);
    setTextColor(this.mHoverColor);
  }
  
  public void onDragExit(DropTarget.DragObject paramDragObject)
  {
    super.onDragExit(paramDragObject);
    if (!paramDragObject.dragComplete)
    {
      this.mDrawable.resetTransition();
      setTextColor(this.mOriginalTextColor);
    }
  }
  
  public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt)
  {
    boolean bool = true;
    if (!isFromAllApps(paramDragSource)) {
      bool = false;
    }
    this.mActive = bool;
    this.mDrawable.resetTransition();
    setTextColor(this.mOriginalTextColor);
    ViewGroup localViewGroup = (ViewGroup)getParent();
    if (bool) {}
    for (int i = 0;; i = 8)
    {
      localViewGroup.setVisibility(i);
      return;
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mOriginalTextColor = getTextColors();
    this.mHoverColor = getResources().getColor(2131230759);
    this.mDrawable = ((TransitionDrawable)getCurrentDrawable());
    if (this.mDrawable != null) {
      this.mDrawable.setCrossFadeEnabled(true);
    }
    if ((getResources().getConfiguration().orientation == 2) && (!LauncherAppState.getInstance().isScreenLarge())) {
      setText("");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.InfoDropTarget
 * JD-Core Version:    0.7.0.1
 */