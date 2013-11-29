package com.android.gallery3d.glrenderer;

import android.util.Log;
import com.android.gallery3d.common.Utils;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class BasicTexture
  implements Texture
{
  private static WeakHashMap<BasicTexture, Object> sAllTextures = new WeakHashMap();
  private static ThreadLocal sInFinalizer = new ThreadLocal();
  protected GLCanvas mCanvasRef = null;
  private boolean mHasBorder;
  protected int mHeight = -1;
  protected int mId = -1;
  protected int mState;
  protected int mTextureHeight;
  protected int mTextureWidth;
  protected int mWidth = -1;
  
  protected BasicTexture()
  {
    this(null, 0, 0);
  }
  
  protected BasicTexture(GLCanvas paramGLCanvas, int paramInt1, int paramInt2)
  {
    setAssociatedCanvas(paramGLCanvas);
    this.mId = paramInt1;
    this.mState = paramInt2;
    synchronized (sAllTextures)
    {
      sAllTextures.put(this, null);
      return;
    }
  }
  
  private void freeResource()
  {
    GLCanvas localGLCanvas = this.mCanvasRef;
    if ((localGLCanvas != null) && (this.mId != -1))
    {
      localGLCanvas.unloadTexture(this);
      this.mId = -1;
    }
    this.mState = 0;
    setAssociatedCanvas(null);
  }
  
  public static void invalidateAllTextures()
  {
    synchronized (sAllTextures)
    {
      Iterator localIterator = sAllTextures.keySet().iterator();
      if (localIterator.hasNext())
      {
        BasicTexture localBasicTexture = (BasicTexture)localIterator.next();
        localBasicTexture.mState = 0;
        localBasicTexture.setAssociatedCanvas(null);
      }
    }
  }
  
  public void draw(GLCanvas paramGLCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramGLCanvas.drawTexture(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void finalize()
  {
    sInFinalizer.set(BasicTexture.class);
    recycle();
    sInFinalizer.set(null);
  }
  
  public int getHeight()
  {
    return this.mHeight;
  }
  
  public int getId()
  {
    return this.mId;
  }
  
  protected abstract int getTarget();
  
  public int getTextureHeight()
  {
    return this.mTextureHeight;
  }
  
  public int getTextureWidth()
  {
    return this.mTextureWidth;
  }
  
  public int getWidth()
  {
    return this.mWidth;
  }
  
  public boolean hasBorder()
  {
    return this.mHasBorder;
  }
  
  public boolean isFlippedVertically()
  {
    return false;
  }
  
  public boolean isLoaded()
  {
    return this.mState == 1;
  }
  
  protected abstract boolean onBind(GLCanvas paramGLCanvas);
  
  public void recycle()
  {
    freeResource();
  }
  
  protected void setAssociatedCanvas(GLCanvas paramGLCanvas)
  {
    this.mCanvasRef = paramGLCanvas;
  }
  
  protected void setBorder(boolean paramBoolean)
  {
    this.mHasBorder = paramBoolean;
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    int i;
    if (paramInt1 > 0)
    {
      i = Utils.nextPowerOf2(paramInt1);
      this.mTextureWidth = i;
      if (paramInt2 <= 0) {
        break label114;
      }
    }
    label114:
    for (int j = Utils.nextPowerOf2(paramInt2);; j = 0)
    {
      this.mTextureHeight = j;
      if ((this.mTextureWidth > 4096) || (this.mTextureHeight > 4096))
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(this.mTextureWidth);
        arrayOfObject[1] = Integer.valueOf(this.mTextureHeight);
        Log.w("BasicTexture", String.format("texture is too large: %d x %d", arrayOfObject), new Exception());
      }
      return;
      i = 0;
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.BasicTexture
 * JD-Core Version:    0.7.0.1
 */