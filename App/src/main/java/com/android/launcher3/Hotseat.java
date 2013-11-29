package com.android.launcher3;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Hotseat
  extends FrameLayout
{
  private int mAllAppsButtonRank;
  private CellLayout mContent;
  private boolean mIsLandscape;
  private Launcher mLauncher;
  private boolean mTransposeLayoutWithOrientation;
  
  public Hotseat(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Hotseat(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Hotseat(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mTransposeLayoutWithOrientation = paramContext.getResources().getBoolean(2131755015);
    if (paramContext.getResources().getConfiguration().orientation == 2) {}
    for (boolean bool = true;; bool = false)
    {
      this.mIsLandscape = bool;
      return;
    }
  }
  
  private boolean hasVerticalHotseat()
  {
    return (this.mIsLandscape) && (this.mTransposeLayoutWithOrientation);
  }
  
  Rect getCellCoordinates(int paramInt1, int paramInt2)
  {
    Rect localRect = new Rect();
    this.mContent.cellToRect(paramInt1, paramInt2, 1, 1, localRect);
    int[] arrayOfInt = new int[2];
    Utilities.getDescendantCoordRelativeToParent(this, this.mLauncher.getDragLayer(), arrayOfInt, false);
    localRect.offset(arrayOfInt[0], arrayOfInt[1]);
    int i = this.mContent.getShortcutsAndWidgets().getCellContentWidth();
    int j = this.mContent.getShortcutsAndWidgets().getCellContentHeight();
    localRect.offset((int)Math.max(0.0F, (localRect.width() - i) / 2.0F), (int)Math.max(0.0F, (localRect.height() - j) / 2.0F));
    return localRect;
  }
  
  int getCellXFromOrder(int paramInt)
  {
    if (hasVerticalHotseat()) {
      paramInt = 0;
    }
    return paramInt;
  }
  
  int getCellYFromOrder(int paramInt)
  {
    if (hasVerticalHotseat()) {
      return this.mContent.getCountY() - (paramInt + 1);
    }
    return 0;
  }
  
  CellLayout getLayout()
  {
    return this.mContent;
  }
  
  int getOrderInHotseat(int paramInt1, int paramInt2)
  {
    if (hasVerticalHotseat()) {
      paramInt1 = -1 + (this.mContent.getCountY() - paramInt2);
    }
    return paramInt1;
  }
  
  public boolean isAllAppsButtonRank(int paramInt)
  {
    if (AppsCustomizePagedView.DISABLE_ALL_APPS) {}
    while (paramInt != this.mAllAppsButtonRank) {
      return false;
    }
    return true;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    this.mAllAppsButtonRank = localDeviceProfile.hotseatAllAppsRank;
    this.mContent = ((CellLayout)findViewById(2131296698));
    if ((localDeviceProfile.isLandscape) && (!localDeviceProfile.isLargeTablet())) {
      this.mContent.setGridSize(1, (int)localDeviceProfile.numHotseatIcons);
    }
    for (;;)
    {
      this.mContent.setIsHotseat(true);
      resetLayout();
      return;
      this.mContent.setGridSize((int)localDeviceProfile.numHotseatIcons, 1);
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.mLauncher.getWorkspace().isSmall();
  }
  
  void resetLayout()
  {
    this.mContent.removeAllViewsInLayout();
    if (!AppsCustomizePagedView.DISABLE_ALL_APPS)
    {
      Context localContext = getContext();
      TextView localTextView = (TextView)LayoutInflater.from(localContext).inflate(2130968593, this.mContent, false);
      Drawable localDrawable = localContext.getResources().getDrawable(2130837505);
      Utilities.resizeIconDrawable(localDrawable);
      localTextView.setCompoundDrawables(null, localDrawable, null, null);
      localTextView.setContentDescription(localContext.getString(2131361899));
      if (this.mLauncher != null) {
        localTextView.setOnTouchListener(this.mLauncher.getHapticFeedbackTouchListener());
      }
      localTextView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (Hotseat.this.mLauncher != null) {
            Hotseat.this.mLauncher.onClickAllAppsButton(paramAnonymousView);
          }
        }
      });
      CellLayout.LayoutParams localLayoutParams = new CellLayout.LayoutParams(getCellXFromOrder(this.mAllAppsButtonRank), getCellYFromOrder(this.mAllAppsButtonRank), 1, 1);
      localLayoutParams.canReorder = false;
      this.mContent.addViewToCellLayout(localTextView, -1, 0, localLayoutParams, true);
    }
  }
  
  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mContent.setOnLongClickListener(paramOnLongClickListener);
  }
  
  public void setup(Launcher paramLauncher)
  {
    this.mLauncher = paramLauncher;
    setOnKeyListener(new HotseatIconKeyEventListener());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Hotseat
 * JD-Core Version:    0.7.0.1
 */