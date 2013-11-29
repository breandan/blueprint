package com.google.android.search.shared.ui;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import java.util.Arrays;

public class ClipOrFadeLayerDrawable
  extends LayerDrawable
{
  private int mAlpha = 255;
  private Path mClipPath;
  private float mFade;
  private boolean mSupportPressedState = true;
  
  public ClipOrFadeLayerDrawable(Drawable paramDrawable1, Drawable paramDrawable2)
  {
    super(new Drawable[] { paramDrawable1, paramDrawable2 });
  }
  
  private int[] removeElement(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i] == paramInt)
      {
        int[] arrayOfInt = Arrays.copyOf(paramArrayOfInt, -1 + paramArrayOfInt.length);
        if (i < -1 + paramArrayOfInt.length) {
          arrayOfInt[i] = paramArrayOfInt[(-1 + paramArrayOfInt.length)];
        }
        return arrayOfInt;
      }
    }
    return paramArrayOfInt;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (this.mClipPath != null)
    {
      getDrawable(0).setAlpha(this.mAlpha);
      getDrawable(1).setAlpha(this.mAlpha);
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath, Region.Op.DIFFERENCE);
      getDrawable(0).draw(paramCanvas);
      paramCanvas.restore();
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath);
      getDrawable(1).draw(paramCanvas);
      paramCanvas.restore();
      return;
    }
    int i = (int)(0.5D + this.mAlpha * this.mFade);
    getDrawable(0).setAlpha(this.mAlpha - i);
    getDrawable(1).setAlpha(i);
    super.draw(paramCanvas);
  }
  
  public float getAlphaFade()
  {
    return this.mFade;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    if (!this.mSupportPressedState) {
      paramArrayOfInt = removeElement(paramArrayOfInt, 16842919);
    }
    return super.onStateChange(paramArrayOfInt);
  }
  
  public void setAlpha(int paramInt)
  {
    this.mAlpha = paramInt;
  }
  
  public void setAlphaFade(float paramFloat)
  {
    this.mFade = paramFloat;
    invalidateSelf();
  }
  
  public void setClipPath(Path paramPath)
  {
    this.mClipPath = paramPath;
    invalidateSelf();
  }
  
  public void setSupportPressedState(boolean paramBoolean)
  {
    this.mSupportPressedState = paramBoolean;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.ClipOrFadeLayerDrawable
 * JD-Core Version:    0.7.0.1
 */