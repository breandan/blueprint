package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;

public class HolographicViewHelper
{
  private int mHighlightColor;
  private int mHotwordColor;
  private boolean mStatesUpdated;
  private final Canvas mTempCanvas = new Canvas();
  
  public HolographicViewHelper(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    this.mHighlightColor = localResources.getColor(17170450);
    this.mHotwordColor = localResources.getColor(17170452);
  }
  
  private Bitmap createImageWithOverlay(ImageView paramImageView, Canvas paramCanvas, int paramInt)
  {
    Drawable localDrawable = paramImageView.getDrawable();
    Bitmap localBitmap = Bitmap.createBitmap(localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    paramCanvas.setBitmap(localBitmap);
    paramCanvas.save();
    localDrawable.draw(paramCanvas);
    paramCanvas.restore();
    paramCanvas.drawColor(paramInt, PorterDuff.Mode.SRC_IN);
    paramCanvas.setBitmap(null);
    return localBitmap;
  }
  
  private Bitmap createOriginalImage(ImageView paramImageView, Canvas paramCanvas)
  {
    Drawable localDrawable = paramImageView.getDrawable();
    Bitmap localBitmap = Bitmap.createBitmap(localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    paramCanvas.setBitmap(localBitmap);
    paramCanvas.save();
    localDrawable.draw(paramCanvas);
    paramCanvas.restore();
    paramCanvas.setBitmap(null);
    return localBitmap;
  }
  
  void generatePressedFocusedStates(ImageView paramImageView)
  {
    if ((!this.mStatesUpdated) && (paramImageView != null))
    {
      this.mStatesUpdated = true;
      Bitmap localBitmap1 = createOriginalImage(paramImageView, this.mTempCanvas);
      Bitmap localBitmap2 = createImageWithOverlay(paramImageView, this.mTempCanvas, this.mHighlightColor);
      Bitmap localBitmap3 = createImageWithOverlay(paramImageView, this.mTempCanvas, this.mHotwordColor);
      FastBitmapDrawable localFastBitmapDrawable1 = new FastBitmapDrawable(localBitmap1);
      FastBitmapDrawable localFastBitmapDrawable2 = new FastBitmapDrawable(localBitmap2);
      FastBitmapDrawable localFastBitmapDrawable3 = new FastBitmapDrawable(localBitmap3);
      StateListDrawable localStateListDrawable = new StateListDrawable();
      localStateListDrawable.addState(new int[] { 16842919 }, localFastBitmapDrawable2);
      localStateListDrawable.addState(new int[] { 16842908 }, localFastBitmapDrawable2);
      localStateListDrawable.addState(new int[] { 2130771999 }, localFastBitmapDrawable3);
      localStateListDrawable.addState(new int[0], localFastBitmapDrawable1);
      paramImageView.setImageDrawable(localStateListDrawable);
    }
  }
  
  void invalidatePressedFocusedStates(ImageView paramImageView)
  {
    this.mStatesUpdated = false;
    if (paramImageView != null) {
      paramImageView.invalidate();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.HolographicViewHelper
 * JD-Core Version:    0.7.0.1
 */