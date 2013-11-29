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
import android.widget.LinearLayout;

public class HolographicLinearLayout
  extends LinearLayout
{
  private final HolographicViewHelper mHolographicHelper;
  private boolean mHotwordOn;
  private ImageView mImageView;
  private int mImageViewId;
  private boolean mIsFocused;
  private boolean mIsPressed;
  
  public HolographicLinearLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public HolographicLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public HolographicLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.HolographicLinearLayout, paramInt, 0);
    this.mImageViewId = localTypedArray.getResourceId(0, -1);
    this.mHotwordOn = localTypedArray.getBoolean(1, false);
    localTypedArray.recycle();
    setWillNotDraw(false);
    this.mHolographicHelper = new HolographicViewHelper(paramContext);
    setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (HolographicLinearLayout.this.isPressed() != HolographicLinearLayout.this.mIsPressed)
        {
          HolographicLinearLayout.access$002(HolographicLinearLayout.this, HolographicLinearLayout.this.isPressed());
          HolographicLinearLayout.this.refreshDrawableState();
        }
        return false;
      }
    });
    setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (HolographicLinearLayout.this.isFocused() != HolographicLinearLayout.this.mIsFocused)
        {
          HolographicLinearLayout.access$102(HolographicLinearLayout.this, HolographicLinearLayout.this.isFocused());
          HolographicLinearLayout.this.refreshDrawableState();
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
    if (this.mImageView != null)
    {
      this.mHolographicHelper.generatePressedFocusedStates(this.mImageView);
      Drawable localDrawable = this.mImageView.getDrawable();
      if ((localDrawable instanceof StateListDrawable))
      {
        StateListDrawable localStateListDrawable = (StateListDrawable)localDrawable;
        localStateListDrawable.setState(getDrawableState());
        localStateListDrawable.invalidateSelf();
      }
    }
  }
  
  void invalidatePressedFocusedStates()
  {
    this.mHolographicHelper.invalidatePressedFocusedStates(this.mImageView);
    invalidate();
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
    if (this.mImageView == null) {
      this.mImageView = ((ImageView)findViewById(this.mImageViewId));
    }
    this.mHolographicHelper.generatePressedFocusedStates(this.mImageView);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HolographicLinearLayout
 * JD-Core Version:    0.7.0.1
 */