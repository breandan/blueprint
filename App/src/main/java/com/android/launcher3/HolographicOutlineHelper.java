package com.android.launcher3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class HolographicOutlineHelper
{
  static HolographicOutlineHelper INSTANCE;
  private final Paint mBlurPaint = new Paint();
  private final Paint mErasePaint = new Paint();
  private BlurMaskFilter mExtraThickInnerBlurMaskFilter;
  private BlurMaskFilter mExtraThickOuterBlurMaskFilter;
  private final Paint mHolographicPaint = new Paint();
  public int mMaxOuterBlurRadius;
  private BlurMaskFilter mMediumInnerBlurMaskFilter;
  private BlurMaskFilter mMediumOuterBlurMaskFilter;
  public int mMinOuterBlurRadius;
  private BlurMaskFilter mThickInnerBlurMaskFilter;
  private BlurMaskFilter mThickOuterBlurMaskFilter;
  private BlurMaskFilter mThinOuterBlurMaskFilter;
  
  private HolographicOutlineHelper(Context paramContext)
  {
    float f = LauncherAppState.getInstance().getScreenDensity();
    this.mMinOuterBlurRadius = ((int)(f * 1.0F));
    this.mMaxOuterBlurRadius = ((int)(f * 12.0F));
    this.mExtraThickOuterBlurMaskFilter = new BlurMaskFilter(12.0F * f, BlurMaskFilter.Blur.OUTER);
    this.mThickOuterBlurMaskFilter = new BlurMaskFilter(f * 6.0F, BlurMaskFilter.Blur.OUTER);
    this.mMediumOuterBlurMaskFilter = new BlurMaskFilter(f * 2.0F, BlurMaskFilter.Blur.OUTER);
    this.mThinOuterBlurMaskFilter = new BlurMaskFilter(f * 1.0F, BlurMaskFilter.Blur.OUTER);
    this.mExtraThickInnerBlurMaskFilter = new BlurMaskFilter(f * 6.0F, BlurMaskFilter.Blur.NORMAL);
    this.mThickInnerBlurMaskFilter = new BlurMaskFilter(4.0F * f, BlurMaskFilter.Blur.NORMAL);
    this.mMediumInnerBlurMaskFilter = new BlurMaskFilter(f * 2.0F, BlurMaskFilter.Blur.NORMAL);
    this.mHolographicPaint.setFilterBitmap(true);
    this.mHolographicPaint.setAntiAlias(true);
    this.mBlurPaint.setFilterBitmap(true);
    this.mBlurPaint.setAntiAlias(true);
    this.mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    this.mErasePaint.setFilterBitmap(true);
    this.mErasePaint.setAntiAlias(true);
  }
  
  public static HolographicOutlineHelper obtain(Context paramContext)
  {
    if (INSTANCE == null) {
      INSTANCE = new HolographicOutlineHelper(paramContext);
    }
    return INSTANCE;
  }
  
  void applyExpensiveOutlineWithBlur(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3)
  {
    applyExpensiveOutlineWithBlur(paramBitmap, paramCanvas, paramInt1, paramInt2, true, paramInt3);
  }
  
  void applyExpensiveOutlineWithBlur(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    if (paramBoolean)
    {
      int[] arrayOfInt4 = new int[paramBitmap.getWidth() * paramBitmap.getHeight()];
      paramBitmap.getPixels(arrayOfInt4, 0, paramBitmap.getWidth(), 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
      for (int i = 0; i < arrayOfInt4.length; i++) {
        if (arrayOfInt4[i] >>> 24 < 188) {
          arrayOfInt4[i] = 0;
        }
      }
      paramBitmap.setPixels(arrayOfInt4, 0, paramBitmap.getWidth(), 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    }
    Bitmap localBitmap1 = paramBitmap.extractAlpha();
    BlurMaskFilter localBlurMaskFilter1;
    label148:
    int[] arrayOfInt1;
    Bitmap localBitmap2;
    switch (paramInt3)
    {
    default: 
      throw new RuntimeException("Invalid blur thickness");
    case 2: 
      localBlurMaskFilter1 = this.mExtraThickOuterBlurMaskFilter;
      this.mBlurPaint.setMaskFilter(localBlurMaskFilter1);
      arrayOfInt1 = new int[2];
      localBitmap2 = localBitmap1.extractAlpha(this.mBlurPaint, arrayOfInt1);
      if (paramInt3 == 2) {
        this.mBlurPaint.setMaskFilter(this.mMediumOuterBlurMaskFilter);
      }
      break;
    }
    int[] arrayOfInt2;
    Bitmap localBitmap3;
    for (;;)
    {
      arrayOfInt2 = new int[2];
      localBitmap3 = localBitmap1.extractAlpha(this.mBlurPaint, arrayOfInt2);
      paramCanvas.setBitmap(localBitmap1);
      paramCanvas.drawColor(-16777216, PorterDuff.Mode.SRC_OUT);
      switch (paramInt3)
      {
      default: 
        throw new RuntimeException("Invalid blur thickness");
        localBlurMaskFilter1 = this.mThickOuterBlurMaskFilter;
        break label148;
        localBlurMaskFilter1 = this.mMediumOuterBlurMaskFilter;
        break label148;
        this.mBlurPaint.setMaskFilter(this.mThinOuterBlurMaskFilter);
      }
    }
    BlurMaskFilter localBlurMaskFilter2 = this.mExtraThickInnerBlurMaskFilter;
    for (;;)
    {
      this.mBlurPaint.setMaskFilter(localBlurMaskFilter2);
      int[] arrayOfInt3 = new int[2];
      Bitmap localBitmap4 = localBitmap1.extractAlpha(this.mBlurPaint, arrayOfInt3);
      paramCanvas.setBitmap(localBitmap4);
      paramCanvas.drawBitmap(localBitmap1, -arrayOfInt3[0], -arrayOfInt3[1], this.mErasePaint);
      paramCanvas.drawRect(0.0F, 0.0F, -arrayOfInt3[0], localBitmap4.getHeight(), this.mErasePaint);
      paramCanvas.drawRect(0.0F, 0.0F, localBitmap4.getWidth(), -arrayOfInt3[1], this.mErasePaint);
      paramCanvas.setBitmap(paramBitmap);
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      this.mHolographicPaint.setColor(paramInt1);
      paramCanvas.drawBitmap(localBitmap4, arrayOfInt3[0], arrayOfInt3[1], this.mHolographicPaint);
      paramCanvas.drawBitmap(localBitmap2, arrayOfInt1[0], arrayOfInt1[1], this.mHolographicPaint);
      this.mHolographicPaint.setColor(paramInt2);
      paramCanvas.drawBitmap(localBitmap3, arrayOfInt2[0], arrayOfInt2[1], this.mHolographicPaint);
      paramCanvas.setBitmap(null);
      localBitmap3.recycle();
      localBitmap2.recycle();
      localBitmap4.recycle();
      localBitmap1.recycle();
      return;
      localBlurMaskFilter2 = this.mThickInnerBlurMaskFilter;
      continue;
      localBlurMaskFilter2 = this.mMediumInnerBlurMaskFilter;
    }
  }
  
  void applyExtraThickExpensiveOutlineWithBlur(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2)
  {
    applyExpensiveOutlineWithBlur(paramBitmap, paramCanvas, paramInt1, paramInt2, 2);
  }
  
  void applyMediumExpensiveOutlineWithBlur(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2)
  {
    applyExpensiveOutlineWithBlur(paramBitmap, paramCanvas, paramInt1, paramInt2, 1);
  }
  
  void applyMediumExpensiveOutlineWithBlur(Bitmap paramBitmap, Canvas paramCanvas, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    applyExpensiveOutlineWithBlur(paramBitmap, paramCanvas, paramInt1, paramInt2, paramBoolean, 1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HolographicOutlineHelper
 * JD-Core Version:    0.7.0.1
 */