package com.android.launcher3;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews;

public class LauncherAppWidgetHostView
  extends AppWidgetHostView
  implements DragLayer.TouchCompleteListener
{
  private Context mContext;
  private DragLayer mDragLayer;
  private LayoutInflater mInflater;
  private CheckLongPressHelper mLongPressHelper;
  private int mPreviousOrientation;
  
  public LauncherAppWidgetHostView(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.mLongPressHelper = new CheckLongPressHelper(this);
    this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.mDragLayer = ((Launcher)paramContext).getDragLayer();
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    this.mLongPressHelper.cancelLongPress();
  }
  
  public int getDescendantFocusability()
  {
    return 393216;
  }
  
  protected View getErrorView()
  {
    return this.mInflater.inflate(2130968600, this, false);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0) {
      this.mLongPressHelper.cancelLongPress();
    }
    if (this.mLongPressHelper.hasPerformedLongPress())
    {
      this.mLongPressHelper.cancelLongPress();
      return true;
    }
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return false;
      this.mLongPressHelper.postCheckForLongPress();
      this.mDragLayer.setTouchCompleteListener(this);
      continue;
      this.mLongPressHelper.cancelLongPress();
    }
  }
  
  public void onTouchComplete()
  {
    if (!this.mLongPressHelper.hasPerformedLongPress()) {
      this.mLongPressHelper.cancelLongPress();
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return false;
      this.mLongPressHelper.cancelLongPress();
    }
  }
  
  public boolean orientationChangedSincedInflation()
  {
    int i = this.mContext.getResources().getConfiguration().orientation;
    return this.mPreviousOrientation != i;
  }
  
  public void updateAppWidget(RemoteViews paramRemoteViews)
  {
    this.mPreviousOrientation = this.mContext.getResources().getConfiguration().orientation;
    super.updateAppWidget(paramRemoteViews);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherAppWidgetHostView
 * JD-Core Version:    0.7.0.1
 */