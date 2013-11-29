package com.android.launcher3;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PagedViewWidget
  extends LinearLayout
{
  private static boolean sDeletePreviewsWhenDetachedFromWindow = true;
  private static boolean sRecyclePreviewsWhenDetachedFromWindow = true;
  static PagedViewWidget sShortpressTarget = null;
  private String mDimensionsFormatString;
  private Object mInfo;
  boolean mIsAppWidget;
  private final Rect mOriginalImagePadding = new Rect();
  CheckForShortPress mPendingCheckForShortPress = null;
  ShortPressListener mShortPressListener = null;
  boolean mShortPressTriggered = false;
  private WidgetPreviewLoader mWidgetPreviewLoader;
  
  public PagedViewWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagedViewWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PagedViewWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mDimensionsFormatString = paramContext.getResources().getString(2131361878);
    setWillNotDraw(false);
    setClipToPadding(false);
  }
  
  private void checkForShortPress()
  {
    if (sShortpressTarget != null) {
      return;
    }
    if (this.mPendingCheckForShortPress == null) {
      this.mPendingCheckForShortPress = new CheckForShortPress();
    }
    postDelayed(this.mPendingCheckForShortPress, 120L);
  }
  
  private void cleanUpShortPress()
  {
    removeShortPressCallback();
    if (this.mShortPressTriggered)
    {
      if (this.mShortPressListener != null) {
        this.mShortPressListener.cleanUpShortPress(this);
      }
      this.mShortPressTriggered = false;
    }
  }
  
  private void removeShortPressCallback()
  {
    if (this.mPendingCheckForShortPress != null) {
      removeCallbacks(this.mPendingCheckForShortPress);
    }
  }
  
  static void resetShortPressTarget()
  {
    sShortpressTarget = null;
  }
  
  public static void setDeletePreviewsWhenDetachedFromWindow(boolean paramBoolean)
  {
    sDeletePreviewsWhenDetachedFromWindow = paramBoolean;
  }
  
  public static void setRecyclePreviewsWhenDetachedFromWindow(boolean paramBoolean)
  {
    sRecyclePreviewsWhenDetachedFromWindow = paramBoolean;
  }
  
  public void applyFromAppWidgetProviderInfo(AppWidgetProviderInfo paramAppWidgetProviderInfo, int paramInt, int[] paramArrayOfInt, WidgetPreviewLoader paramWidgetPreviewLoader)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    this.mIsAppWidget = true;
    this.mInfo = paramAppWidgetProviderInfo;
    ImageView localImageView = (ImageView)findViewById(2131296346);
    if (paramInt > -1) {
      localImageView.setMaxWidth(paramInt);
    }
    ((TextView)findViewById(2131296347)).setText(paramAppWidgetProviderInfo.label);
    TextView localTextView = (TextView)findViewById(2131296348);
    if (localTextView != null)
    {
      int i = Math.min(paramArrayOfInt[0], (int)localDeviceProfile.numColumns);
      int j = Math.min(paramArrayOfInt[1], (int)localDeviceProfile.numRows);
      String str = this.mDimensionsFormatString;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(j);
      localTextView.setText(String.format(str, arrayOfObject));
    }
    this.mWidgetPreviewLoader = paramWidgetPreviewLoader;
  }
  
  public void applyFromResolveInfo(PackageManager paramPackageManager, ResolveInfo paramResolveInfo, WidgetPreviewLoader paramWidgetPreviewLoader)
  {
    this.mIsAppWidget = false;
    this.mInfo = paramResolveInfo;
    CharSequence localCharSequence = paramResolveInfo.loadLabel(paramPackageManager);
    ((TextView)findViewById(2131296347)).setText(localCharSequence);
    TextView localTextView = (TextView)findViewById(2131296348);
    if (localTextView != null)
    {
      String str = this.mDimensionsFormatString;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(1);
      arrayOfObject[1] = Integer.valueOf(1);
      localTextView.setText(String.format(str, arrayOfObject));
    }
    this.mWidgetPreviewLoader = paramWidgetPreviewLoader;
  }
  
  void applyPreview(FastBitmapDrawable paramFastBitmapDrawable, int paramInt)
  {
    PagedViewWidgetImageView localPagedViewWidgetImageView = (PagedViewWidgetImageView)findViewById(2131296346);
    if (paramFastBitmapDrawable != null)
    {
      localPagedViewWidgetImageView.mAllowRequestLayout = false;
      localPagedViewWidgetImageView.setImageDrawable(paramFastBitmapDrawable);
      if (this.mIsAppWidget) {
        localPagedViewWidgetImageView.setPadding((getPreviewSize()[0] - paramFastBitmapDrawable.getIntrinsicWidth()) / 2 + this.mOriginalImagePadding.left, this.mOriginalImagePadding.top, this.mOriginalImagePadding.right, this.mOriginalImagePadding.bottom);
      }
      localPagedViewWidgetImageView.setAlpha(1.0F);
      localPagedViewWidgetImageView.mAllowRequestLayout = true;
    }
  }
  
  public int[] getPreviewSize()
  {
    ImageView localImageView = (ImageView)findViewById(2131296346);
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = (localImageView.getWidth() - this.mOriginalImagePadding.left - this.mOriginalImagePadding.right);
    arrayOfInt[1] = (localImageView.getHeight() - this.mOriginalImagePadding.top);
    return arrayOfInt;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (sDeletePreviewsWhenDetachedFromWindow)
    {
      ImageView localImageView = (ImageView)findViewById(2131296346);
      if (localImageView != null)
      {
        FastBitmapDrawable localFastBitmapDrawable = (FastBitmapDrawable)localImageView.getDrawable();
        if ((sRecyclePreviewsWhenDetachedFromWindow) && (this.mInfo != null) && (localFastBitmapDrawable != null) && (localFastBitmapDrawable.getBitmap() != null)) {
          this.mWidgetPreviewLoader.recycleBitmap(this.mInfo, localFastBitmapDrawable.getBitmap());
        }
        localImageView.setImageDrawable(null);
      }
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    ImageView localImageView = (ImageView)findViewById(2131296346);
    this.mOriginalImagePadding.left = localImageView.getPaddingLeft();
    this.mOriginalImagePadding.top = localImageView.getPaddingTop();
    this.mOriginalImagePadding.right = localImageView.getPaddingRight();
    this.mOriginalImagePadding.bottom = localImageView.getPaddingBottom();
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    TextView localTextView1 = (TextView)findViewById(2131296347);
    if (localTextView1 != null) {
      localTextView1.setTextSize(0, localDeviceProfile.iconTextSizePx);
    }
    TextView localTextView2 = (TextView)findViewById(2131296348);
    if (localTextView2 != null) {
      localTextView2.setTextSize(0, localDeviceProfile.iconTextSizePx);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return true;
      cleanUpShortPress();
      continue;
      checkForShortPress();
      continue;
      cleanUpShortPress();
    }
  }
  
  void setShortPressListener(ShortPressListener paramShortPressListener)
  {
    this.mShortPressListener = paramShortPressListener;
  }
  
  class CheckForShortPress
    implements Runnable
  {
    CheckForShortPress() {}
    
    public void run()
    {
      if (PagedViewWidget.sShortpressTarget != null) {
        return;
      }
      if (PagedViewWidget.this.mShortPressListener != null)
      {
        PagedViewWidget.this.mShortPressListener.onShortPress(PagedViewWidget.this);
        PagedViewWidget.sShortpressTarget = PagedViewWidget.this;
      }
      PagedViewWidget.this.mShortPressTriggered = true;
    }
  }
  
  static abstract interface ShortPressListener
  {
    public abstract void cleanUpShortPress(View paramView);
    
    public abstract void onShortPress(View paramView);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewWidget
 * JD-Core Version:    0.7.0.1
 */