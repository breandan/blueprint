package com.google.android.search.shared.ui;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.google.android.googlequicksearchbox.R.styleable;

public class CrossfadingWebImageView
  extends RoundedCornerWebImageView
{
  private DrawableUpdater mDrawableUpdater;
  private final boolean mFadeIfLoadedFromCache;
  private final int mTransitionTime;
  private final int pressedHighlightColor;
  
  public CrossfadingWebImageView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CrossfadingWebImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CrossfadingWebImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CrossfadingWebImageView, paramInt, 0);
    this.mTransitionTime = localTypedArray.getInt(0, 300);
    this.mFadeIfLoadedFromCache = localTypedArray.getBoolean(1, false);
    this.pressedHighlightColor = localTypedArray.getColor(2, -1);
    localTypedArray.recycle();
  }
  
  protected void drawableStateChanged()
  {
    if (this.pressedHighlightColor == -1) {
      return;
    }
    int[] arrayOfInt = getDrawableState();
    int i = arrayOfInt.length;
    for (int j = 0;; j++)
    {
      int k = 0;
      if (j < i)
      {
        int m = arrayOfInt[j];
        if ((m == 16842919) || (m == 16842908)) {
          k = 1;
        }
      }
      else
      {
        if (k == 0) {
          break;
        }
        setColorFilter(this.pressedHighlightColor);
        return;
      }
    }
    setColorFilter(null);
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    getContext();
    if (this.mDrawableUpdater != null)
    {
      removeCallbacks(this.mDrawableUpdater);
      this.mDrawableUpdater = null;
    }
    if ((isLoadedFromCache()) && (!this.mFadeIfLoadedFromCache))
    {
      super.setImageDrawable(paramDrawable);
      return;
    }
    for (ViewParent localViewParent = getParent();; localViewParent = localViewParent.getParent())
    {
      int i = 0;
      if (localViewParent != null)
      {
        if ((localViewParent instanceof ViewGroup))
        {
          LayoutTransition localLayoutTransition = ((ViewGroup)localViewParent).getLayoutTransition();
          if ((localLayoutTransition != null) && (localLayoutTransition.isRunning())) {
            i = 1;
          }
        }
      }
      else
      {
        if (i == 0) {
          break;
        }
        super.setImageDrawable(paramDrawable);
        return;
      }
    }
    if ((paramDrawable == null) && (getDrawable() == null))
    {
      super.setImageDrawable(paramDrawable);
      return;
    }
    Object localObject = getDrawable();
    if (localObject == null) {
      localObject = new ColorDrawable(0);
    }
    for (;;)
    {
      TransitionDrawable localTransitionDrawable = new TransitionDrawable(new Drawable[] { localObject, paramDrawable });
      localTransitionDrawable.setCrossFadeEnabled(true);
      super.setImageDrawable(localTransitionDrawable);
      localTransitionDrawable.startTransition(this.mTransitionTime);
      this.mDrawableUpdater = new DrawableUpdater(paramDrawable, null);
      postDelayed(this.mDrawableUpdater, this.mTransitionTime);
      return;
      if (paramDrawable == null) {
        paramDrawable = new ColorDrawable(0);
      }
    }
  }
  
  private class DrawableUpdater
    implements Runnable
  {
    private final Drawable mPendingDrawable;
    
    private DrawableUpdater(Drawable paramDrawable)
    {
      this.mPendingDrawable = paramDrawable;
    }
    
    public void run()
    {
      CrossfadingWebImageView.this.setImageDrawable(this.mPendingDrawable);
      CrossfadingWebImageView.access$202(CrossfadingWebImageView.this, null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.CrossfadingWebImageView
 * JD-Core Version:    0.7.0.1
 */