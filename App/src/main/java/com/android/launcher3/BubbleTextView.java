package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class BubbleTextView
  extends TextView
{
  private Drawable mBackground;
  private boolean mBackgroundSizeChanged;
  private boolean mDidInvalidateForPressedState;
  private int mFocusedGlowColor;
  private int mFocusedOutlineColor;
  private boolean mIsTextVisible;
  private CheckLongPressHelper mLongPressHelper;
  private HolographicOutlineHelper mOutlineHelper;
  private int mPressedGlowColor;
  private Bitmap mPressedOrFocusedBackground;
  private int mPressedOutlineColor;
  private int mPrevAlpha = -1;
  private boolean mShadowsEnabled = true;
  private boolean mStayPressed;
  private final Canvas mTempCanvas = new Canvas();
  private final Rect mTempRect = new Rect();
  private int mTextColor;
  
  public BubbleTextView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public BubbleTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public BubbleTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private Bitmap createGlowingOutline(Canvas paramCanvas, int paramInt1, int paramInt2)
  {
    int i = this.mOutlineHelper.mMaxOuterBlurRadius;
    Bitmap localBitmap = Bitmap.createBitmap(i + getWidth(), i + getHeight(), Bitmap.Config.ARGB_8888);
    paramCanvas.setBitmap(localBitmap);
    drawWithPadding(paramCanvas, i);
    this.mOutlineHelper.applyExtraThickExpensiveOutlineWithBlur(localBitmap, paramCanvas, paramInt2, paramInt1);
    paramCanvas.setBitmap(null);
    return localBitmap;
  }
  
  private void drawWithPadding(Canvas paramCanvas, int paramInt)
  {
    Rect localRect = this.mTempRect;
    getDrawingRect(localRect);
    localRect.bottom = (-3 + getExtendedPaddingTop() + getLayout().getLineTop(0));
    paramCanvas.save();
    paramCanvas.scale(getScaleX(), getScaleY(), (paramInt + getWidth()) / 2, (paramInt + getHeight()) / 2);
    paramCanvas.translate(-getScrollX() + paramInt / 2, -getScrollY() + paramInt / 2);
    paramCanvas.clipRect(localRect, Region.Op.REPLACE);
    draw(paramCanvas);
    paramCanvas.restore();
  }
  
  private void init()
  {
    this.mLongPressHelper = new CheckLongPressHelper(this);
    this.mBackground = getBackground();
    this.mOutlineHelper = HolographicOutlineHelper.obtain(getContext());
    int i = getContext().getResources().getColor(2131230769);
    this.mPressedGlowColor = i;
    this.mPressedOutlineColor = i;
    this.mFocusedGlowColor = i;
    this.mFocusedOutlineColor = i;
    setShadowLayer(4.0F, 0.0F, 2.0F, -587202560);
  }
  
  public void applyFromShortcutInfo(ShortcutInfo paramShortcutInfo, IconCache paramIconCache)
  {
    Bitmap localBitmap = paramShortcutInfo.getIcon(paramIconCache);
    DeviceProfile localDeviceProfile = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile();
    setCompoundDrawables(null, Utilities.createIconDrawable(localBitmap), null, null);
    setCompoundDrawablePadding(localDeviceProfile.iconDrawablePaddingPx);
    setText(paramShortcutInfo.title);
    setTag(paramShortcutInfo);
  }
  
  public void cancelLongPress()
  {
    super.cancelLongPress();
    this.mLongPressHelper.cancelLongPress();
  }
  
  void clearPressedOrFocusedBackground()
  {
    this.mPressedOrFocusedBackground = null;
    setCellLayoutPressedOrFocusedIcon();
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (!this.mShadowsEnabled)
    {
      super.draw(paramCanvas);
      return;
    }
    Drawable localDrawable = this.mBackground;
    int i;
    int j;
    if (localDrawable != null)
    {
      i = getScrollX();
      j = getScrollY();
      if (this.mBackgroundSizeChanged)
      {
        localDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
        this.mBackgroundSizeChanged = false;
      }
      if ((i | j) != 0) {
        break label113;
      }
      localDrawable.draw(paramCanvas);
    }
    while (getCurrentTextColor() == getResources().getColor(17170445))
    {
      getPaint().clearShadowLayer();
      super.draw(paramCanvas);
      return;
      label113:
      paramCanvas.translate(i, j);
      localDrawable.draw(paramCanvas);
      paramCanvas.translate(-i, -j);
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
    if (isPressed()) {
      if (!this.mDidInvalidateForPressedState) {
        setCellLayoutPressedOrFocusedIcon();
      }
    }
    label149:
    label152:
    for (;;)
    {
      Drawable localDrawable = this.mBackground;
      if ((localDrawable != null) && (localDrawable.isStateful())) {
        localDrawable.setState(getDrawableState());
      }
      super.drawableStateChanged();
      return;
      int i;
      if (this.mPressedOrFocusedBackground == null)
      {
        i = 1;
        label57:
        if (!this.mStayPressed) {
          this.mPressedOrFocusedBackground = null;
        }
        if (isFocused())
        {
          if (getLayout() != null) {
            break label126;
          }
          this.mPressedOrFocusedBackground = null;
          label88:
          this.mStayPressed = false;
          setCellLayoutPressedOrFocusedIcon();
        }
        if (this.mPressedOrFocusedBackground != null) {
          break label149;
        }
      }
      for (int j = 1;; j = 0)
      {
        if ((i != 0) || (j == 0)) {
          break label152;
        }
        setCellLayoutPressedOrFocusedIcon();
        break;
        i = 0;
        break label57;
        label126:
        this.mPressedOrFocusedBackground = createGlowingOutline(this.mTempCanvas, this.mFocusedGlowColor, this.mFocusedOutlineColor);
        break label88;
      }
    }
  }
  
  Bitmap getPressedOrFocusedBackground()
  {
    return this.mPressedOrFocusedBackground;
  }
  
  int getPressedOrFocusedBackgroundPadding()
  {
    return this.mOutlineHelper.mMaxOuterBlurRadius / 2;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (this.mBackground != null) {
      this.mBackground.setCallback(this);
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mBackground != null) {
      this.mBackground.setCallback(null);
    }
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    setTextSize(0, LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().iconTextSizePx);
    setTextColor(getResources().getColor(2131230764));
  }
  
  protected boolean onSetAlpha(int paramInt)
  {
    if (this.mPrevAlpha != paramInt)
    {
      this.mPrevAlpha = paramInt;
      super.onSetAlpha(paramInt);
    }
    return true;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    case 2: 
    default: 
      return bool;
    case 0: 
      if (this.mPressedOrFocusedBackground == null) {
        this.mPressedOrFocusedBackground = createGlowingOutline(this.mTempCanvas, this.mPressedGlowColor, this.mPressedOutlineColor);
      }
      if (isPressed())
      {
        this.mDidInvalidateForPressedState = true;
        setCellLayoutPressedOrFocusedIcon();
      }
      for (;;)
      {
        this.mLongPressHelper.postCheckForLongPress();
        return bool;
        this.mDidInvalidateForPressedState = false;
      }
    }
    if (!isPressed()) {
      this.mPressedOrFocusedBackground = null;
    }
    this.mLongPressHelper.cancelLongPress();
    return bool;
  }
  
  void setCellLayoutPressedOrFocusedIcon()
  {
    CellLayout localCellLayout;
    if ((getParent() instanceof ShortcutAndWidgetContainer))
    {
      ShortcutAndWidgetContainer localShortcutAndWidgetContainer = (ShortcutAndWidgetContainer)getParent();
      if (localShortcutAndWidgetContainer != null)
      {
        localCellLayout = (CellLayout)localShortcutAndWidgetContainer.getParent();
        if (this.mPressedOrFocusedBackground == null) {
          break label43;
        }
      }
    }
    for (;;)
    {
      localCellLayout.setPressedOrFocusedIcon(this);
      return;
      label43:
      this = null;
    }
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((getLeft() != paramInt1) || (getRight() != paramInt3) || (getTop() != paramInt2) || (getBottom() != paramInt4)) {
      this.mBackgroundSizeChanged = true;
    }
    return super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setGlowColor(int paramInt)
  {
    this.mPressedGlowColor = paramInt;
    this.mPressedOutlineColor = paramInt;
    this.mFocusedGlowColor = paramInt;
    this.mFocusedOutlineColor = paramInt;
  }
  
  public void setShadowsEnabled(boolean paramBoolean)
  {
    this.mShadowsEnabled = paramBoolean;
    getPaint().clearShadowLayer();
    invalidate();
  }
  
  void setStayPressed(boolean paramBoolean)
  {
    this.mStayPressed = paramBoolean;
    if (!paramBoolean) {
      this.mPressedOrFocusedBackground = null;
    }
    setCellLayoutPressedOrFocusedIcon();
  }
  
  public void setTag(Object paramObject)
  {
    if (paramObject != null) {
      LauncherModel.checkItemInfo((ItemInfo)paramObject);
    }
    super.setTag(paramObject);
  }
  
  public void setTextColor(int paramInt)
  {
    this.mTextColor = paramInt;
    super.setTextColor(paramInt);
  }
  
  public void setTextVisibility(boolean paramBoolean)
  {
    Resources localResources = getResources();
    if (paramBoolean) {
      super.setTextColor(this.mTextColor);
    }
    for (;;)
    {
      this.mIsTextVisible = paramBoolean;
      return;
      super.setTextColor(localResources.getColor(17170445));
    }
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    return (paramDrawable == this.mBackground) || (super.verifyDrawable(paramDrawable));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.BubbleTextView
 * JD-Core Version:    0.7.0.1
 */