package com.google.android.search.gel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.android.search.shared.ui.PathClippingView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

public class SearchOverlayClipAnimation
  implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener
{
  @Nullable
  private View mAnchorView;
  private final ValueAnimator mAnimator;
  private float mCircleRadius;
  private int mCircleStartRadius;
  private int mCircleX;
  private int mCircleY;
  private final ViewGroup mContainer;
  private final Rect mRect = new Rect();
  private final List<PathClipingViewInfo> mViewInfos = Lists.newArrayList();
  
  public SearchOverlayClipAnimation(ViewGroup paramViewGroup)
  {
    this.mContainer = paramViewGroup;
    this.mAnimator = new ValueAnimator();
    this.mAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    this.mAnimator.addUpdateListener(this);
    this.mAnimator.addListener(this);
    this.mAnimator.setDuration(350L);
    this.mCircleStartRadius = paramViewGroup.getContext().getResources().getDimensionPixelSize(2131689628);
  }
  
  private int findViewInfo(PathClippingView paramPathClippingView)
  {
    for (int i = 0; i < this.mViewInfos.size(); i++) {
      if (((PathClipingViewInfo)this.mViewInfos.get(i)).mClippingView == paramPathClippingView) {
        return i;
      }
    }
    return -1;
  }
  
  private void updateCircleCenterTo(View paramView)
  {
    this.mRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
    this.mContainer.offsetDescendantRectToMyCoords(paramView, this.mRect);
    this.mCircleX = this.mRect.centerX();
    this.mCircleY = this.mRect.centerY();
  }
  
  public void addView(PathClippingView paramPathClippingView, View paramView)
  {
    if (!this.mAnimator.isRunning()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if (findViewInfo(paramPathClippingView) == -1) {
        this.mViewInfos.add(new PathClipingViewInfo(paramPathClippingView, paramView, null));
      }
      return;
    }
  }
  
  public void onAnimationCancel(Animator paramAnimator) {}
  
  public void onAnimationEnd(Animator paramAnimator)
  {
    for (int i = 0; i < this.mViewInfos.size(); i++) {
      ((PathClipingViewInfo)this.mViewInfos.get(i)).mClippingView.setClipPath(null);
    }
  }
  
  public void onAnimationRepeat(Animator paramAnimator) {}
  
  public void onAnimationStart(Animator paramAnimator)
  {
    for (int i = 0; i < this.mViewInfos.size(); i++)
    {
      this.mRect.left = 0;
      this.mRect.top = 0;
      this.mContainer.offsetRectIntoDescendantCoords(((PathClipingViewInfo)this.mViewInfos.get(i)).mView, this.mRect);
      ((PathClipingViewInfo)this.mViewInfos.get(i)).mOffset.set(this.mRect.left, this.mRect.top);
    }
  }
  
  public void onAnimationUpdate(ValueAnimator paramValueAnimator)
  {
    if (this.mAnchorView != null) {
      updateCircleCenterTo(this.mAnchorView);
    }
    this.mCircleRadius = ((Float)paramValueAnimator.getAnimatedValue()).floatValue();
    for (int i = 0; i < this.mViewInfos.size(); i++)
    {
      PathClipingViewInfo localPathClipingViewInfo = (PathClipingViewInfo)this.mViewInfos.get(i);
      localPathClipingViewInfo.mPath.reset();
      localPathClipingViewInfo.mPath.addCircle(this.mCircleX + localPathClipingViewInfo.mOffset.x, this.mCircleY + localPathClipingViewInfo.mOffset.y, this.mCircleRadius, Path.Direction.CW);
      localPathClipingViewInfo.mClippingView.setClipPath(localPathClipingViewInfo.mPath);
    }
  }
  
  public void removeView(PathClippingView paramPathClippingView)
  {
    if (!this.mAnimator.isRunning()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      int i = findViewInfo(paramPathClippingView);
      if (i > -1) {
        this.mViewInfos.remove(i);
      }
      return;
    }
  }
  
  public void setupCenter(int paramInt1, int paramInt2)
  {
    if (!this.mAnimator.isRunning()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mCircleX = paramInt1;
      this.mCircleY = paramInt2;
      return;
    }
  }
  
  public void start(boolean paramBoolean, int paramInt1, int paramInt2, View paramView)
  {
    boolean bool;
    float f;
    if (!this.mAnimator.isRunning())
    {
      bool = true;
      Preconditions.checkState(bool);
      f = QuantumPaperUtils.getFillRadius(paramInt1, paramInt2, this.mCircleX, this.mCircleY);
      this.mAnchorView = paramView;
      if (!paramBoolean) {
        break label90;
      }
      ValueAnimator localValueAnimator = this.mAnimator;
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = this.mCircleStartRadius;
      arrayOfFloat[1] = f;
      localValueAnimator.setFloatValues(arrayOfFloat);
    }
    for (;;)
    {
      this.mAnimator.start();
      return;
      bool = false;
      break;
      label90:
      this.mAnimator.setFloatValues(new float[] { f, 1.0F });
    }
  }
  
  private static class PathClipingViewInfo
  {
    private final PathClippingView mClippingView;
    private final Point mOffset;
    private final Path mPath;
    private final View mView;
    
    private PathClipingViewInfo(PathClippingView paramPathClippingView, View paramView)
    {
      this.mClippingView = paramPathClippingView;
      this.mView = paramView;
      this.mOffset = new Point();
      this.mPath = new Path();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.SearchOverlayClipAnimation
 * JD-Core Version:    0.7.0.1
 */