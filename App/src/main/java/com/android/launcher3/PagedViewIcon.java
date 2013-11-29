package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class PagedViewIcon
  extends TextView
{
  private Bitmap mIcon;
  private boolean mLockDrawableState = false;
  private PressedCallback mPressedCallback;
  
  public PagedViewIcon(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagedViewIcon(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PagedViewIcon(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void applyFromApplicationInfo(AppInfo paramAppInfo, boolean paramBoolean, PressedCallback paramPressedCallback)
  {
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    this.mIcon = paramAppInfo.iconBitmap;
    this.mPressedCallback = paramPressedCallback;
    Drawable localDrawable = Utilities.createIconDrawable(this.mIcon);
    localDrawable.setBounds(0, 0, localDeviceProfile.allAppsIconSizePx, localDeviceProfile.allAppsIconSizePx);
    setCompoundDrawables(null, localDrawable, null, null);
    setCompoundDrawablePadding(localDeviceProfile.iconDrawablePaddingPx);
    setText(paramAppInfo.title);
    setTag(paramAppInfo);
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (getCurrentTextColor() == getResources().getColor(17170445))
    {
      getPaint().clearShadowLayer();
      super.draw(paramCanvas);
      return;
    }
    getPaint().setShadowLayer(4.0F, 0.0F, 2.0F, -587202560);
    super.draw(paramCanvas);
    paramCanvas.save(2);
    paramCanvas.clipRect(getScrollX(), getScrollY() + getExtendedPaddingTop(), getScrollX() + getWidth(), getScrollY() + getHeight(), Region.Op.INTERSECT);
    getPaint().setShadowLayer(1.75F, 0.0F, 0.0F, -872415232);
    super.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (isPressed())
    {
      setAlpha(0.4F);
      if (this.mPressedCallback != null) {
        this.mPressedCallback.iconPressed(this);
      }
    }
    while (this.mLockDrawableState) {
      return;
    }
    setAlpha(1.0F);
  }
  
  public void lockDrawableState()
  {
    this.mLockDrawableState = true;
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    setTextSize(0, LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().allAppsIconTextSizePx);
  }
  
  public void resetDrawableState()
  {
    this.mLockDrawableState = false;
    post(new Runnable()
    {
      public void run()
      {
        PagedViewIcon.this.refreshDrawableState();
      }
    });
  }
  
  public static abstract interface PressedCallback
  {
    public abstract void iconPressed(PagedViewIcon paramPagedViewIcon);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PagedViewIcon
 * JD-Core Version:    0.7.0.1
 */