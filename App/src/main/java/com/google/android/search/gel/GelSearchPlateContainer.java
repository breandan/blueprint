package com.google.android.search.gel;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.android.search.shared.ui.PathClippingView;
import com.google.android.shared.util.LayoutUtils;

public class GelSearchPlateContainer
  extends FrameLayout
  implements PathClippingView
{
  private Path mClipPath;
  private ValueAnimator mDoodleAlphaAnimator;
  private float mDoodleAlphaMultiplier = 1.0F;
  private ValueAnimator mDownPressAnimator;
  private boolean mDrawTouchFeedback;
  private final Rect mHoloBounds;
  private int mMaxTouchFeedbackRadius;
  private int mMinTouchFeedbackRadius;
  private final Drawable mMinusOnePlate;
  private int mMode;
  private float mProximityToNow;
  private View mRecognizer;
  private TouchDelegate mRecognizerViewTouchDelegate;
  private int mTouchFeedbackInset;
  private Paint mTouchFeedbackPaint;
  private int mTouchFeedbackRadius;
  private float mTouchX;
  private float mTouchY;
  private final Drawable mTransparentPlate;
  private final Rect mWhiteBounds;
  private final Drawable mWhitePlate;
  
  public GelSearchPlateContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mWhitePlate = paramContext.getResources().getDrawable(2130838037);
    this.mMinusOnePlate = paramContext.getResources().getDrawable(2130838037);
    this.mTransparentPlate = paramContext.getResources().getDrawable(2130838040);
    this.mHoloBounds = new Rect();
    this.mWhiteBounds = new Rect();
    Rect localRect = new Rect();
    this.mWhitePlate.getPadding(localRect);
    setPadding(localRect.left, localRect.top, localRect.right, localRect.bottom);
    setWillNotDraw(false);
    this.mTouchFeedbackPaint = new Paint();
    this.mTouchFeedbackPaint.setColor(getResources().getColor(2131230898));
    this.mTouchFeedbackPaint.setAlpha(153);
    this.mTouchFeedbackPaint.setFlags(1);
    this.mTouchFeedbackRadius = getResources().getDimensionPixelSize(2131689628);
    this.mTouchFeedbackInset = getResources().getDimensionPixelSize(2131689576);
    this.mDownPressAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    this.mDownPressAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    this.mDownPressAnimator.setDuration(100L);
    this.mMinTouchFeedbackRadius = getResources().getDimensionPixelSize(2131689629);
    this.mMaxTouchFeedbackRadius = getResources().getDimensionPixelSize(2131689628);
    this.mDownPressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        float f = ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
        GelSearchPlateContainer.this.mTouchFeedbackPaint.setAlpha((int)(153.0F * f));
        GelSearchPlateContainer.access$102(GelSearchPlateContainer.this, GelSearchPlateContainer.this.mMinTouchFeedbackRadius + (int)(f * (GelSearchPlateContainer.this.mMaxTouchFeedbackRadius - GelSearchPlateContainer.this.mMinTouchFeedbackRadius)));
        GelSearchPlateContainer.this.invalidate();
      }
    });
    this.mDoodleAlphaAnimator = ValueAnimator.ofFloat(new float[] { 0.94F, 1.0F });
    this.mDoodleAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        GelSearchPlateContainer.access$402(GelSearchPlateContainer.this, ((Float)paramAnonymousValueAnimator.getAnimatedValue()).floatValue());
        GelSearchPlateContainer.this.invalidate();
      }
    });
  }
  
  private void drawLauncherPlates(Canvas paramCanvas)
  {
    this.mMinusOnePlate.setBounds(this.mHoloBounds);
    this.mMinusOnePlate.setAlpha((int)(0.5D + 255.0F * (this.mProximityToNow * this.mDoodleAlphaMultiplier)));
    this.mMinusOnePlate.draw(paramCanvas);
    this.mTransparentPlate.setBounds(this.mHoloBounds);
    this.mTransparentPlate.setAlpha((int)(0.5D + 255.0F * (1.0F - this.mProximityToNow)));
    this.mTransparentPlate.draw(paramCanvas);
  }
  
  private void updateTouchDelegateEnabled(int paramInt)
  {
    if (paramInt == 11) {}
    for (TouchDelegate localTouchDelegate = this.mRecognizerViewTouchDelegate;; localTouchDelegate = null)
    {
      setTouchDelegate(localTouchDelegate);
      return;
    }
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    this.mHoloBounds.bottom = 0;
    super.onConfigurationChanged(paramConfiguration);
  }
  
  public void onDoodleChanged(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (float f = 0.94F; this.mProximityToNow != 0.0F; f = 1.0F)
    {
      this.mDoodleAlphaAnimator.cancel();
      ValueAnimator localValueAnimator = this.mDoodleAlphaAnimator;
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = this.mDoodleAlphaMultiplier;
      arrayOfFloat[1] = f;
      localValueAnimator.setFloatValues(arrayOfFloat);
      this.mDoodleAlphaAnimator.start();
      return;
    }
    this.mDoodleAlphaMultiplier = f;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    this.mHoloBounds.right = paramCanvas.getWidth();
    this.mWhiteBounds.set(0, 0, paramCanvas.getWidth(), paramCanvas.getHeight());
    this.mWhitePlate.setBounds(this.mWhiteBounds);
    if (this.mClipPath != null)
    {
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath, Region.Op.DIFFERENCE);
      drawLauncherPlates(paramCanvas);
      paramCanvas.restore();
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath);
      this.mWhitePlate.draw(paramCanvas);
      paramCanvas.restore();
    }
    do
    {
      return;
      if (this.mMode != 11)
      {
        this.mWhitePlate.draw(paramCanvas);
        return;
      }
      drawLauncherPlates(paramCanvas);
    } while ((!this.mDrawTouchFeedback) && (!this.mDownPressAnimator.isRunning()));
    this.mHoloBounds.inset(-this.mTouchFeedbackInset, -this.mTouchFeedbackInset);
    paramCanvas.clipRect(this.mHoloBounds);
    paramCanvas.drawCircle(this.mTouchX, this.mTouchY, this.mTouchFeedbackRadius, this.mTouchFeedbackPaint);
    this.mHoloBounds.inset(this.mTouchFeedbackInset, this.mTouchFeedbackInset);
  }
  
  protected void onFinishInflate()
  {
    View localView = findViewById(2131296970);
    this.mRecognizer = findViewById(2131296528);
    TouchFeedbackListener localTouchFeedbackListener = new TouchFeedbackListener(null);
    localView.setOnTouchListener(localTouchFeedbackListener);
    this.mRecognizer.setOnTouchListener(localTouchFeedbackListener);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    Rect localRect;
    if ((this.mMode == 11) && (this.mRecognizerViewTouchDelegate == null))
    {
      localRect = new Rect(0, 0, this.mRecognizer.getWidth(), this.mRecognizer.getHeight());
      offsetDescendantRectToMyCoords(this.mRecognizer, localRect);
      localRect.top = paramInt2;
      localRect.bottom = paramInt4;
      if (!LayoutUtils.isLayoutRtl(this)) {
        break label114;
      }
      localRect.left = paramInt1;
    }
    for (;;)
    {
      this.mRecognizerViewTouchDelegate = new TouchDelegate(localRect, this.mRecognizer);
      updateTouchDelegateEnabled(this.mMode);
      return;
      label114:
      localRect.right = paramInt3;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = this.mHoloBounds.bottom;
    if (((i == 0) || ((i != 0) && (getMeasuredHeight() != i))) && (this.mMode == 11)) {
      this.mHoloBounds.bottom = getMeasuredHeight();
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    return true;
  }
  
  public void setClipPath(Path paramPath)
  {
    this.mClipPath = paramPath;
    invalidate();
  }
  
  public void setMode(int paramInt)
  {
    if (this.mMode == paramInt) {
      return;
    }
    updateTouchDelegateEnabled(paramInt);
    if (paramInt != 11) {
      this.mDrawTouchFeedback = false;
    }
    this.mMode = paramInt;
  }
  
  public void setProximityToNow(float paramFloat)
  {
    this.mProximityToNow = paramFloat;
    invalidate();
  }
  
  private class TouchFeedbackListener
    implements View.OnTouchListener
  {
    private TouchFeedbackListener() {}
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      if (GelSearchPlateContainer.this.mMode != 11)
      {
        GelSearchPlateContainer.access$702(GelSearchPlateContainer.this, false);
        return paramView.getVisibility() != 0;
      }
      Rect localRect = new Rect();
      int i = paramMotionEvent.getActionMasked();
      boolean bool;
      int j;
      label151:
      int k;
      if ((i == 0) || (i == 2)) {
        if ((i == 0) || (paramView.isPressed()))
        {
          bool = true;
          if ((bool) && (!GelSearchPlateContainer.this.mDrawTouchFeedback)) {
            GelSearchPlateContainer.this.mDownPressAnimator.start();
          }
          if ((!bool) && (GelSearchPlateContainer.this.mDrawTouchFeedback)) {
            GelSearchPlateContainer.this.mDownPressAnimator.reverse();
          }
          GelSearchPlateContainer.access$702(GelSearchPlateContainer.this, bool);
          if (paramView != GelSearchPlateContainer.this.mRecognizer) {
            break label232;
          }
          j = paramView.getWidth() / 2;
          localRect.left = j;
          if (paramView != GelSearchPlateContainer.this.mRecognizer) {
            break label242;
          }
          k = paramView.getHeight() / 2;
          label176:
          localRect.top = k;
          GelSearchPlateContainer.this.offsetDescendantRectToMyCoords(paramView, localRect);
          GelSearchPlateContainer.access$1002(GelSearchPlateContainer.this, localRect.left);
          GelSearchPlateContainer.access$1102(GelSearchPlateContainer.this, localRect.top);
        }
      }
      for (;;)
      {
        GelSearchPlateContainer.this.invalidate();
        return false;
        bool = false;
        break;
        label232:
        j = (int)paramMotionEvent.getX();
        break label151;
        label242:
        k = (int)paramMotionEvent.getY();
        break label176;
        if ((i == 3) || (i == 1)) {
          GelSearchPlateContainer.access$702(GelSearchPlateContainer.this, false);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.GelSearchPlateContainer
 * JD-Core Version:    0.7.0.1
 */