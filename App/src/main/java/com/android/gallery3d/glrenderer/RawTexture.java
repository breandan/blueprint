package com.android.gallery3d.glrenderer;

import android.util.Log;

public class RawTexture
  extends BasicTexture
{
  private boolean mIsFlipped;
  private final boolean mOpaque;
  
  protected int getTarget()
  {
    return 3553;
  }
  
  public boolean isFlippedVertically()
  {
    return this.mIsFlipped;
  }
  
  public boolean isOpaque()
  {
    return this.mOpaque;
  }
  
  protected boolean onBind(GLCanvas paramGLCanvas)
  {
    if (isLoaded()) {
      return true;
    }
    Log.w("RawTexture", "lost the content due to context change");
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.RawTexture
 * JD-Core Version:    0.7.0.1
 */