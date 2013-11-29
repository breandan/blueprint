package com.android.gallery3d.glrenderer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;
import java.util.HashMap;
import junit.framework.Assert;

public abstract class UploadedTexture
  extends BasicTexture
{
  private static BorderKey sBorderKey = new BorderKey(null);
  private static HashMap<BorderKey, Bitmap> sBorderLines = new HashMap();
  private static int sUploadedCount;
  protected Bitmap mBitmap;
  private int mBorder;
  private boolean mContentValid = true;
  private boolean mIsUploading = false;
  private boolean mOpaque = true;
  private boolean mThrottled = false;
  
  protected UploadedTexture()
  {
    this(false);
  }
  
  protected UploadedTexture(boolean paramBoolean)
  {
    super(null, 0, 0);
    if (paramBoolean)
    {
      setBorder(true);
      this.mBorder = 1;
    }
  }
  
  private void freeBitmap()
  {
    if (this.mBitmap != null) {}
    for (boolean bool = true;; bool = false)
    {
      Assert.assertTrue(bool);
      onFreeBitmap(this.mBitmap);
      this.mBitmap = null;
      return;
    }
  }
  
  private Bitmap getBitmap()
  {
    if (this.mBitmap == null)
    {
      this.mBitmap = onGetBitmap();
      int i = this.mBitmap.getWidth() + 2 * this.mBorder;
      int j = this.mBitmap.getHeight() + 2 * this.mBorder;
      if (this.mWidth == -1) {
        setSize(i, j);
      }
    }
    return this.mBitmap;
  }
  
  private static Bitmap getBorderLine(boolean paramBoolean, Bitmap.Config paramConfig, int paramInt)
  {
    BorderKey localBorderKey = sBorderKey;
    localBorderKey.vertical = paramBoolean;
    localBorderKey.config = paramConfig;
    localBorderKey.length = paramInt;
    Bitmap localBitmap = (Bitmap)sBorderLines.get(localBorderKey);
    if (localBitmap == null) {
      if (!paramBoolean) {
        break label64;
      }
    }
    label64:
    for (localBitmap = Bitmap.createBitmap(1, paramInt, paramConfig);; localBitmap = Bitmap.createBitmap(paramInt, 1, paramConfig))
    {
      sBorderLines.put(localBorderKey.clone(), localBitmap);
      return localBitmap;
    }
  }
  
  private void uploadToCanvas(GLCanvas paramGLCanvas)
  {
    Bitmap localBitmap1 = getBitmap();
    if (localBitmap1 != null) {
      try
      {
        int i = localBitmap1.getWidth();
        int j = localBitmap1.getHeight();
        (i + 2 * this.mBorder);
        (j + 2 * this.mBorder);
        int k = getTextureWidth();
        int m = getTextureHeight();
        boolean bool;
        if ((i <= k) && (j <= m))
        {
          bool = true;
          Assert.assertTrue(bool);
          this.mId = paramGLCanvas.getGLId().generateTexture();
          paramGLCanvas.setTextureParameters(this);
          if ((i != k) || (j != m)) {
            break label145;
          }
          paramGLCanvas.initializeTexture(this, localBitmap1);
        }
        for (;;)
        {
          freeBitmap();
          setAssociatedCanvas(paramGLCanvas);
          this.mState = 1;
          this.mContentValid = true;
          return;
          bool = false;
          break;
          label145:
          int n = GLUtils.getInternalFormat(localBitmap1);
          int i1 = GLUtils.getType(localBitmap1);
          Bitmap.Config localConfig = localBitmap1.getConfig();
          paramGLCanvas.initializeTextureSize(this, n, i1);
          paramGLCanvas.texSubImage2D(this, this.mBorder, this.mBorder, localBitmap1, n, i1);
          if (this.mBorder > 0)
          {
            paramGLCanvas.texSubImage2D(this, 0, 0, getBorderLine(true, localConfig, m), n, i1);
            paramGLCanvas.texSubImage2D(this, 0, 0, getBorderLine(false, localConfig, k), n, i1);
          }
          if (i + this.mBorder < k)
          {
            Bitmap localBitmap3 = getBorderLine(true, localConfig, m);
            paramGLCanvas.texSubImage2D(this, i + this.mBorder, 0, localBitmap3, n, i1);
          }
          if (j + this.mBorder < m)
          {
            Bitmap localBitmap2 = getBorderLine(false, localConfig, k);
            paramGLCanvas.texSubImage2D(this, 0, j + this.mBorder, localBitmap2, n, i1);
          }
        }
        this.mState = -1;
      }
      finally
      {
        freeBitmap();
      }
    }
    throw new RuntimeException("Texture load fail, no bitmap");
  }
  
  public int getHeight()
  {
    if (this.mWidth == -1) {
      getBitmap();
    }
    return this.mHeight;
  }
  
  protected int getTarget()
  {
    return 3553;
  }
  
  public int getWidth()
  {
    if (this.mWidth == -1) {
      getBitmap();
    }
    return this.mWidth;
  }
  
  protected void invalidateContent()
  {
    if (this.mBitmap != null) {
      freeBitmap();
    }
    this.mContentValid = false;
    this.mWidth = -1;
    this.mHeight = -1;
  }
  
  public boolean isContentValid()
  {
    return (isLoaded()) && (this.mContentValid);
  }
  
  public boolean isOpaque()
  {
    return this.mOpaque;
  }
  
  protected boolean onBind(GLCanvas paramGLCanvas)
  {
    updateContent(paramGLCanvas);
    return isContentValid();
  }
  
  protected abstract void onFreeBitmap(Bitmap paramBitmap);
  
  protected abstract Bitmap onGetBitmap();
  
  public void recycle()
  {
    super.recycle();
    if (this.mBitmap != null) {
      freeBitmap();
    }
  }
  
  public void updateContent(GLCanvas paramGLCanvas)
  {
    if (!isLoaded()) {
      if (this.mThrottled)
      {
        k = 1 + sUploadedCount;
        sUploadedCount = k;
        if (k <= 100) {}
      }
    }
    while (this.mContentValid)
    {
      int k;
      return;
      uploadToCanvas(paramGLCanvas);
      return;
    }
    Bitmap localBitmap = getBitmap();
    int i = GLUtils.getInternalFormat(localBitmap);
    int j = GLUtils.getType(localBitmap);
    paramGLCanvas.texSubImage2D(this, this.mBorder, this.mBorder, localBitmap, i, j);
    freeBitmap();
    this.mContentValid = true;
  }
  
  private static class BorderKey
    implements Cloneable
  {
    public Bitmap.Config config;
    public int length;
    public boolean vertical;
    
    public BorderKey clone()
    {
      try
      {
        BorderKey localBorderKey = (BorderKey)super.clone();
        return localBorderKey;
      }
      catch (CloneNotSupportedException localCloneNotSupportedException)
      {
        throw new AssertionError(localCloneNotSupportedException);
      }
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof BorderKey)) {}
      BorderKey localBorderKey;
      do
      {
        return false;
        localBorderKey = (BorderKey)paramObject;
      } while ((this.vertical != localBorderKey.vertical) || (this.config != localBorderKey.config) || (this.length != localBorderKey.length));
      return true;
    }
    
    public int hashCode()
    {
      int i = this.config.hashCode() ^ this.length;
      if (this.vertical) {
        return i;
      }
      return -i;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.glrenderer.UploadedTexture
 * JD-Core Version:    0.7.0.1
 */