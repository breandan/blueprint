package com.android.launcher3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class HolographicImageView
  extends ImageView
{
  private final HolographicViewHelper mHolographicHelper;
  private boolean mHotwordOn;
  private boolean mIsFocused;
  private boolean mIsPressed;
  
  public HolographicImageView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public HolographicImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public HolographicImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mHolographicHelper = new HolographicViewHelper(paramContext);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.HolographicLinearLayout, paramInt, 0);
    this.mHotwordOn = localTypedArray.getBoolean(1, false);
    localTypedArray.recycle();
    setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (HolographicImageView.this.isPressed() != HolographicImageView.this.mIsPressed)
        {
          HolographicImageView.access$002(HolographicImageView.this, HolographicImageView.this.isPressed());
          HolographicImageView.this.refreshDrawableState();
        }
        return false;
      }
    });
    setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (HolographicImageView.this.isFocused() != HolographicImageView.this.mIsFocused)
        {
          HolographicImageView.access$102(HolographicImageView.this, HolographicImageView.this.isFocused());
          HolographicImageView.this.refreshDrawableState();
        }
      }
    });
  }
  
  private boolean isHotwordOn()
  {
    return this.mHotwordOn;
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    this.mHolographicHelper.generatePressedFocusedStates(this);
    Drawable localDrawable = getDrawable();
    if ((localDrawable instanceof StateListDrawable))
    {
      StateListDrawable localStateListDrawable = (StateListDrawable)localDrawable;
      localStateListDrawable.setState(getDrawableState());
      localStateListDrawable.invalidateSelf();
    }
  }
  
  void invalidatePressedFocusedStates()
  {
    this.mHolographicHelper.invalidatePressedFocusedStates(this);
  }
  
  public int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isHotwordOn()) {
      mergeDrawableStates(arrayOfInt, new int[] { 2130771999 });
    }
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    this.mHolographicHelper.generatePressedFocusedStates(this);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HolographicImageView
 * JD-Core Version:    0.7.0.1
 */