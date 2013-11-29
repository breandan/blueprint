package com.android.launcher3;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.android.photos.views.TiledImageRenderer.TileSource;
import com.android.photos.views.TiledImageView;
import com.android.photos.views.TiledImageView.ImageRendererWrapper;

public class CropView
  extends TiledImageView
  implements ScaleGestureDetector.OnScaleGestureListener
{
  private float mCenterX;
  private float mCenterY;
  private float mFirstX;
  private float mFirstY;
  Matrix mInverseRotateMatrix = new Matrix();
  private float mLastX;
  private float mLastY;
  private float mMinScale;
  Matrix mRotateMatrix = new Matrix();
  private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(paramContext, this);
  private float[] mTempAdjustment = { 0.0F, 0.0F };
  private float[] mTempCoef = { 0.0F, 0.0F };
  private RectF mTempEdges = new RectF();
  private float[] mTempImageDims = { 0.0F, 0.0F };
  private float[] mTempPoint = { 0.0F, 0.0F };
  private float[] mTempRendererCenter = { 0.0F, 0.0F };
  TouchCallback mTouchCallback;
  private long mTouchDownTime;
  private boolean mTouchEnabled = true;
  
  public CropView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CropView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void getEdgesHelper(RectF paramRectF)
  {
    float f1 = getWidth();
    float f2 = getHeight();
    float[] arrayOfFloat1 = getImageDims();
    float f3 = arrayOfFloat1[0];
    float f4 = arrayOfFloat1[1];
    float f5 = this.mRenderer.source.getImageWidth() / 2.0F;
    float f6 = this.mRenderer.source.getImageHeight() / 2.0F;
    float[] arrayOfFloat2 = this.mTempRendererCenter;
    arrayOfFloat2[0] = (this.mCenterX - f5);
    arrayOfFloat2[1] = (this.mCenterY - f6);
    this.mRotateMatrix.mapPoints(arrayOfFloat2);
    arrayOfFloat2[0] += f3 / 2.0F;
    arrayOfFloat2[1] += f4 / 2.0F;
    float f7 = this.mRenderer.scale;
    float f8 = f7 * (f1 / 2.0F - arrayOfFloat2[0] + (f3 - f1) / 2.0F) + f1 / 2.0F;
    float f9 = f7 * (f2 / 2.0F - arrayOfFloat2[1] + (f4 - f2) / 2.0F) + f2 / 2.0F;
    float f10 = f8 - f7 * (f3 / 2.0F);
    float f11 = f8 + f7 * (f3 / 2.0F);
    float f12 = f9 - f7 * (f4 / 2.0F);
    float f13 = f9 + f7 * (f4 / 2.0F);
    paramRectF.left = f10;
    paramRectF.right = f11;
    paramRectF.top = f12;
    paramRectF.bottom = f13;
  }
  
  private float[] getImageDims()
  {
    float f1 = this.mRenderer.source.getImageWidth();
    float f2 = this.mRenderer.source.getImageHeight();
    float[] arrayOfFloat = this.mTempImageDims;
    arrayOfFloat[0] = f1;
    arrayOfFloat[1] = f2;
    this.mRotateMatrix.mapPoints(arrayOfFloat);
    arrayOfFloat[0] = Math.abs(arrayOfFloat[0]);
    arrayOfFloat[1] = Math.abs(arrayOfFloat[1]);
    return arrayOfFloat;
  }
  
  private void updateCenter()
  {
    this.mRenderer.centerX = Math.round(this.mCenterX);
    this.mRenderer.centerY = Math.round(this.mCenterY);
  }
  
  private void updateMinScale(int paramInt1, int paramInt2, TiledImageRenderer.TileSource paramTileSource, boolean paramBoolean)
  {
    Object localObject1 = this.mLock;
    if (paramBoolean) {}
    try
    {
      this.mRenderer.scale = 1.0F;
      if (paramTileSource != null)
      {
        float[] arrayOfFloat = getImageDims();
        float f1 = arrayOfFloat[0];
        float f2 = arrayOfFloat[1];
        this.mMinScale = Math.max(paramInt1 / f1, paramInt2 / f2);
        this.mRenderer.scale = Math.max(this.mMinScale, this.mRenderer.scale);
      }
      return;
    }
    finally {}
  }
  
  public RectF getCrop()
  {
    RectF localRectF = this.mTempEdges;
    getEdgesHelper(localRectF);
    float f1 = this.mRenderer.scale;
    float f2 = -localRectF.left / f1;
    float f3 = -localRectF.top / f1;
    return new RectF(f2, f3, f2 + getWidth() / f1, f3 + getHeight() / f1);
  }
  
  public int getImageRotation()
  {
    return this.mRenderer.rotation;
  }
  
  public Point getSourceDimensions()
  {
    return new Point(this.mRenderer.source.getImageWidth(), this.mRenderer.source.getImageHeight());
  }
  
  public void moveToLeft()
  {
    if ((getWidth() == 0) || (getHeight() == 0)) {
      getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
      {
        public void onGlobalLayout()
        {
          CropView.this.moveToLeft();
          CropView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      });
    }
    RectF localRectF = this.mTempEdges;
    getEdgesHelper(localRectF);
    float f = this.mRenderer.scale;
    this.mCenterX = ((float)(this.mCenterX + Math.ceil(localRectF.left / f)));
    updateCenter();
  }
  
  public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
  {
    TiledImageView.ImageRendererWrapper localImageRendererWrapper = this.mRenderer;
    localImageRendererWrapper.scale *= paramScaleGestureDetector.getScaleFactor();
    this.mRenderer.scale = Math.max(this.mMinScale, this.mRenderer.scale);
    invalidate();
    return true;
  }
  
  public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
  {
    return true;
  }
  
  public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateMinScale(paramInt1, paramInt2, this.mRenderer.source, false);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    int j;
    int k;
    label23:
    float f1;
    float f2;
    int m;
    int n;
    if (i == 6)
    {
      j = 1;
      if (j == 0) {
        break label63;
      }
      k = paramMotionEvent.getActionIndex();
      f1 = 0.0F;
      f2 = 0.0F;
      m = paramMotionEvent.getPointerCount();
      n = 0;
      label38:
      if (n >= m) {
        break label94;
      }
      if (k != n) {
        break label69;
      }
    }
    for (;;)
    {
      n++;
      break label38;
      j = 0;
      break;
      label63:
      k = -1;
      break label23;
      label69:
      f1 += paramMotionEvent.getX(n);
      f2 += paramMotionEvent.getY(n);
    }
    label94:
    int i1;
    float f3;
    float f4;
    if (j != 0)
    {
      i1 = m - 1;
      f3 = f1 / i1;
      f4 = f2 / i1;
      if (i != 0) {
        break label175;
      }
      this.mFirstX = f3;
      this.mFirstY = f4;
      this.mTouchDownTime = System.currentTimeMillis();
      if (this.mTouchCallback != null) {
        this.mTouchCallback.onTouchDown();
      }
    }
    for (;;)
    {
      if (this.mTouchEnabled) {
        break label292;
      }
      return true;
      i1 = m;
      break;
      label175:
      if (i == 1)
      {
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
        float f5 = (this.mFirstX - f3) * (this.mFirstX - f3) + (this.mFirstY - f4) * (this.mFirstY - f4);
        float f6 = localViewConfiguration.getScaledTouchSlop() * localViewConfiguration.getScaledTouchSlop();
        long l = System.currentTimeMillis();
        if (this.mTouchCallback != null)
        {
          if ((f5 < f6) && (l < this.mTouchDownTime + ViewConfiguration.getTapTimeout())) {
            this.mTouchCallback.onTap();
          }
          this.mTouchCallback.onTouchUp();
        }
      }
    }
    for (;;)
    {
      label292:
      RectF localRectF;
      float f7;
      float[] arrayOfFloat3;
      int i2;
      synchronized (this.mLock)
      {
        this.mScaleGestureDetector.onTouchEvent(paramMotionEvent);
        switch (i)
        {
        default: 
          if (this.mRenderer.source == null) {
            break label693;
          }
          localRectF = this.mTempEdges;
          getEdgesHelper(localRectF);
          f7 = this.mRenderer.scale;
          float[] arrayOfFloat2 = this.mTempCoef;
          arrayOfFloat2[0] = 1.0F;
          arrayOfFloat2[1] = 1.0F;
          this.mRotateMatrix.mapPoints(arrayOfFloat2);
          arrayOfFloat3 = this.mTempAdjustment;
          this.mTempAdjustment[0] = 0.0F;
          this.mTempAdjustment[1] = 0.0F;
          if (localRectF.left > 0.0F)
          {
            arrayOfFloat3[0] = (localRectF.left / f7);
            if (localRectF.top <= 0.0F) {
              break label619;
            }
            arrayOfFloat3[1] = FloatMath.ceil(localRectF.top / f7);
            break label710;
            if (i2 > 1) {
              break label654;
            }
            if (arrayOfFloat2[i2] <= 0.0F) {
              break label716;
            }
            arrayOfFloat3[i2] = FloatMath.ceil(arrayOfFloat3[i2]);
          }
          break;
        case 2: 
          float[] arrayOfFloat1 = this.mTempPoint;
          arrayOfFloat1[0] = ((this.mLastX - f3) / this.mRenderer.scale);
          arrayOfFloat1[1] = ((this.mLastY - f4) / this.mRenderer.scale);
          this.mInverseRotateMatrix.mapPoints(arrayOfFloat1);
          this.mCenterX += arrayOfFloat1[0];
          this.mCenterY += arrayOfFloat1[1];
          updateCenter();
          invalidate();
        }
      }
      if (localRectF.right < getWidth())
      {
        arrayOfFloat3[0] = ((localRectF.right - getWidth()) / f7);
        continue;
        label619:
        if (localRectF.bottom < getHeight())
        {
          arrayOfFloat3[1] = ((localRectF.bottom - getHeight()) / f7);
          break label710;
          this.mInverseRotateMatrix.mapPoints(arrayOfFloat3);
          this.mCenterX += arrayOfFloat3[0];
          this.mCenterY += arrayOfFloat3[1];
          updateCenter();
          this.mLastX = f3;
          this.mLastY = f4;
          return true;
        }
        label654:
        label693:
        label710:
        i2 = 0;
        continue;
        label716:
        i2++;
      }
    }
  }
  
  public void setScale(float paramFloat)
  {
    synchronized (this.mLock)
    {
      this.mRenderer.scale = paramFloat;
      return;
    }
  }
  
  public void setTileSource(TiledImageRenderer.TileSource paramTileSource, Runnable paramRunnable)
  {
    super.setTileSource(paramTileSource, paramRunnable);
    this.mCenterX = this.mRenderer.centerX;
    this.mCenterY = this.mRenderer.centerY;
    this.mRotateMatrix.reset();
    this.mRotateMatrix.setRotate(this.mRenderer.rotation);
    this.mInverseRotateMatrix.reset();
    this.mInverseRotateMatrix.setRotate(-this.mRenderer.rotation);
    updateMinScale(getWidth(), getHeight(), paramTileSource, true);
  }
  
  public void setTouchCallback(TouchCallback paramTouchCallback)
  {
    this.mTouchCallback = paramTouchCallback;
  }
  
  public void setTouchEnabled(boolean paramBoolean)
  {
    this.mTouchEnabled = paramBoolean;
  }
  
  public static abstract interface TouchCallback
  {
    public abstract void onTap();
    
    public abstract void onTouchDown();
    
    public abstract void onTouchUp();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.CropView
 * JD-Core Version:    0.7.0.1
 */