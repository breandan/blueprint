package com.android.gallery3d.glrenderer;

import android.graphics.Bitmap;
import android.graphics.RectF;

public abstract interface GLCanvas
{
  public abstract void drawTexture(BasicTexture paramBasicTexture, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void drawTexture(BasicTexture paramBasicTexture, RectF paramRectF1, RectF paramRectF2);
  
  public abstract GLId getGLId();
  
  public abstract void initializeTexture(BasicTexture paramBasicTexture, Bitmap paramBitmap);
  
  public abstract void initializeTextureSize(BasicTexture paramBasicTexture, int paramInt1, int paramInt2);
  
  public abstract void restore();
  
  public abstract void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public abstract void save(int paramInt);
  
  public abstract void setTextureParameters(BasicTexture paramBasicTexture);
  
  public abstract void texSubImage2D(BasicTexture paramBasicTexture, int paramInt1, int paramInt2, Bitmap paramBitmap, int paramInt3, int paramInt4);
  
  public abstract void translate(float paramFloat1, float paramFloat2);
  
  public abstract boolean unloadTexture(BasicTexture paramBasicTexture);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.GLCanvas
 * JD-Core Version:    0.7.0.1
 */