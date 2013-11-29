package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

public class SearchDropTargetBar
  extends FrameLayout
  implements DragController.DragListener
{
  private static final AccelerateInterpolator sAccelerateInterpolator = new AccelerateInterpolator();
  private int mBarHeight;
  private boolean mDeferOnDragEnd = false;
  private ButtonDropTarget mDeleteDropTarget;
  private View mDropTargetBar;
  private ObjectAnimator mDropTargetBarAnim;
  private boolean mEnableDropDownDropTargets;
  private ButtonDropTarget mInfoDropTarget;
  private boolean mIsSearchBarHidden;
  private Drawable mPreviousBackground;
  private View mQSBSearchBar;
  private ObjectAnimator mQSBSearchBarAnim;
  
  public SearchDropTargetBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SearchDropTargetBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void prepareStartAnimation(View paramView)
  {
    paramView.setLayerType(2, null);
  }
  
  private void setupAnimation(ObjectAnimator paramObjectAnimator, final View paramView)
  {
    paramObjectAnimator.setInterpolator(sAccelerateInterpolator);
    paramObjectAnimator.setDuration(200L);
    paramObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        paramView.setLayerType(0, null);
      }
    });
  }
  
  public void deferOnDragEnd()
  {
    this.mDeferOnDragEnd = true;
  }
  
  public void finishAnimations()
  {
    prepareStartAnimation(this.mDropTargetBar);
    this.mDropTargetBarAnim.reverse();
    prepareStartAnimation(this.mQSBSearchBar);
    this.mQSBSearchBarAnim.reverse();
  }
  
  public Rect getSearchBarBounds()
  {
    if (this.mQSBSearchBar != null)
    {
      int[] arrayOfInt = new int[2];
      this.mQSBSearchBar.getLocationOnScreen(arrayOfInt);
      Rect localRect = new Rect();
      localRect.left = arrayOfInt[0];
      localRect.top = arrayOfInt[1];
      localRect.right = (arrayOfInt[0] + this.mQSBSearchBar.getWidth());
      localRect.bottom = (arrayOfInt[1] + this.mQSBSearchBar.getHeight());
      return localRect;
    }
    return null;
  }
  
  public int getTransitionInDuration()
  {
    return 200;
  }
  
  public void hideSearchBar(boolean paramBoolean)
  {
    if ((this.mQSBSearchBarAnim.isRunning()) && (!paramBoolean)) {}
    for (int i = 1; (this.mIsSearchBarHidden) && (i == 0); i = 0) {
      return;
    }
    if (paramBoolean)
    {
      prepareStartAnimation(this.mQSBSearchBar);
      this.mQSBSearchBarAnim.start();
    }
    for (;;)
    {
      this.mIsSearchBarHidden = true;
      return;
      this.mQSBSearchBarAnim.cancel();
      if (this.mEnableDropDownDropTargets) {
        this.mQSBSearchBar.setTranslationY(-this.mBarHeight);
      } else {
        this.mQSBSearchBar.setAlpha(0.0F);
      }
    }
  }
  
  public void onDragEnd()
  {
    if (!this.mDeferOnDragEnd)
    {
      prepareStartAnimation(this.mDropTargetBar);
      this.mDropTargetBarAnim.reverse();
      if (!this.mIsSearchBarHidden)
      {
        prepareStartAnimation(this.mQSBSearchBar);
        this.mQSBSearchBarAnim.reverse();
      }
      return;
    }
    this.mDeferOnDragEnd = false;
  }
  
  public void onDragStart(DragSource paramDragSource, Object paramObject, int paramInt)
  {
    prepareStartAnimation(this.mDropTargetBar);
    this.mDropTargetBarAnim.start();
    if (!this.mIsSearchBarHidden)
    {
      prepareStartAnimation(this.mQSBSearchBar);
      this.mQSBSearchBarAnim.start();
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mDropTargetBar = findViewById(2131296904);
    this.mInfoDropTarget = ((ButtonDropTarget)this.mDropTargetBar.findViewById(2131296518));
    this.mDeleteDropTarget = ((ButtonDropTarget)this.mDropTargetBar.findViewById(2131296517));
    this.mInfoDropTarget.setSearchDropTargetBar(this);
    this.mDeleteDropTarget.setSearchDropTargetBar(this);
    this.mEnableDropDownDropTargets = getResources().getBoolean(2131755012);
    View localView;
    float[] arrayOfFloat;
    if (this.mEnableDropDownDropTargets)
    {
      this.mBarHeight = LauncherAppState.getInstance().getDynamicGrid().getDeviceProfile().searchBarSpaceHeightPx;
      this.mDropTargetBar.setTranslationY(-this.mBarHeight);
      localView = this.mDropTargetBar;
      arrayOfFloat = new float[2];
      arrayOfFloat[0] = (-this.mBarHeight);
      arrayOfFloat[1] = 0.0F;
    }
    for (this.mDropTargetBarAnim = ObjectAnimator.ofFloat(localView, "translationY", arrayOfFloat);; this.mDropTargetBarAnim = ObjectAnimator.ofFloat(this.mDropTargetBar, "alpha", new float[] { 0.0F, 1.0F }))
    {
      setupAnimation(this.mDropTargetBarAnim, this.mDropTargetBar);
      return;
      this.mDropTargetBar.setAlpha(0.0F);
    }
  }
  
  public void onSearchPackagesChanged(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mQSBSearchBar != null)
    {
      Drawable localDrawable = this.mQSBSearchBar.getBackground();
      if ((localDrawable == null) || (paramBoolean1) || (paramBoolean2)) {
        break label41;
      }
      this.mPreviousBackground = localDrawable;
      this.mQSBSearchBar.setBackgroundResource(0);
    }
    label41:
    while ((this.mPreviousBackground == null) || ((!paramBoolean1) && (!paramBoolean2))) {
      return;
    }
    this.mQSBSearchBar.setBackground(this.mPreviousBackground);
  }
  
  public void setup(Launcher paramLauncher, DragController paramDragController)
  {
    paramDragController.addDragListener(this);
    paramDragController.addDragListener(this.mInfoDropTarget);
    paramDragController.addDragListener(this.mDeleteDropTarget);
    paramDragController.addDropTarget(this.mInfoDropTarget);
    paramDragController.addDropTarget(this.mDeleteDropTarget);
    paramDragController.setFlingToDeleteDropTarget(this.mDeleteDropTarget);
    this.mInfoDropTarget.setLauncher(paramLauncher);
    this.mDeleteDropTarget.setLauncher(paramLauncher);
    this.mQSBSearchBar = paramLauncher.getQsbBar();
    View localView;
    float[] arrayOfFloat;
    if (this.mEnableDropDownDropTargets)
    {
      localView = this.mQSBSearchBar;
      arrayOfFloat = new float[2];
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = (-this.mBarHeight);
    }
    for (this.mQSBSearchBarAnim = ObjectAnimator.ofFloat(localView, "translationY", arrayOfFloat);; this.mQSBSearchBarAnim = ObjectAnimator.ofFloat(this.mQSBSearchBar, "alpha", new float[] { 1.0F, 0.0F }))
    {
      setupAnimation(this.mQSBSearchBarAnim, this.mQSBSearchBar);
      return;
    }
  }
  
  public void showSearchBar(boolean paramBoolean)
  {
    if ((this.mQSBSearchBarAnim.isRunning()) && (!paramBoolean)) {}
    for (int i = 1; (!this.mIsSearchBarHidden) && (i == 0); i = 0) {
      return;
    }
    if (paramBoolean)
    {
      prepareStartAnimation(this.mQSBSearchBar);
      this.mQSBSearchBarAnim.reverse();
    }
    for (;;)
    {
      this.mIsSearchBarHidden = false;
      return;
      this.mQSBSearchBarAnim.cancel();
      if (this.mEnableDropDownDropTargets) {
        this.mQSBSearchBar.setTranslationY(0.0F);
      } else {
        this.mQSBSearchBar.setAlpha(1.0F);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.SearchDropTargetBar
 * JD-Core Version:    0.7.0.1
 */