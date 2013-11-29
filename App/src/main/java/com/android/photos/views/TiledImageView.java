package com.android.photos.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.gallery3d.glrenderer.BasicTexture;
import com.android.gallery3d.glrenderer.GLES20Canvas;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TiledImageView
  extends FrameLayout
{
  private static final boolean IS_SUPPORTED;
  private static final boolean USE_CHOREOGRAPHER;
  private Choreographer.FrameCallback mFrameCallback;
  private Runnable mFreeTextures = new Runnable()
  {
    public void run()
    {
      TiledImageView.this.mRenderer.image.freeTextures();
    }
  };
  private GLSurfaceView mGLSurfaceView;
  private boolean mInvalPending = false;
  protected Object mLock = new Object();
  protected ImageRendererWrapper mRenderer;
  private RectF mTempRectF = new RectF();
  private float[] mValues = new float[9];
  
  static
  {
    boolean bool1 = true;
    boolean bool2;
    if (Build.VERSION.SDK_INT >= 16)
    {
      bool2 = bool1;
      IS_SUPPORTED = bool2;
      if (Build.VERSION.SDK_INT < 16) {
        break label34;
      }
    }
    for (;;)
    {
      USE_CHOREOGRAPHER = bool1;
      return;
      bool2 = false;
      break;
      label34:
      bool1 = false;
    }
  }
  
  public TiledImageView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TiledImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (!IS_SUPPORTED) {
      return;
    }
    this.mRenderer = new ImageRendererWrapper();
    this.mRenderer.image = new TiledImageRenderer(this);
    this.mGLSurfaceView = new GLSurfaceView(paramContext);
    this.mGLSurfaceView.setEGLContextClientVersion(2);
    this.mGLSurfaceView.setRenderer(new TileRenderer(null));
    this.mGLSurfaceView.setRenderMode(0);
    addView(this.mGLSurfaceView, new FrameLayout.LayoutParams(-1, -1));
  }
  
  @TargetApi(16)
  private void invalOnVsync()
  {
    if (!this.mInvalPending)
    {
      this.mInvalPending = true;
      if (this.mFrameCallback == null) {
        this.mFrameCallback = new Choreographer.FrameCallback()
        {
          public void doFrame(long paramAnonymousLong)
          {
            TiledImageView.access$102(TiledImageView.this, false);
            TiledImageView.this.mGLSurfaceView.requestRender();
          }
        };
      }
      Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
    }
  }
  
  private void updateScaleIfNecessaryLocked(ImageRendererWrapper paramImageRendererWrapper)
  {
    if ((paramImageRendererWrapper == null) || (paramImageRendererWrapper.source == null) || (paramImageRendererWrapper.scale > 0.0F) || (getWidth() == 0)) {
      return;
    }
    paramImageRendererWrapper.scale = Math.min(getWidth() / paramImageRendererWrapper.source.getImageWidth(), getHeight() / paramImageRendererWrapper.source.getImageHeight());
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if (!IS_SUPPORTED) {
      return;
    }
    super.dispatchDraw(paramCanvas);
  }
  
  public void invalidate()
  {
    if (!IS_SUPPORTED) {
      return;
    }
    if (USE_CHOREOGRAPHER)
    {
      invalOnVsync();
      return;
    }
    this.mGLSurfaceView.requestRender();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!IS_SUPPORTED) {
      return;
    }
    synchronized (this.mLock)
    {
      updateScaleIfNecessaryLocked(this.mRenderer);
      return;
    }
  }
  
  public void setTileSource(TiledImageRenderer.TileSource paramTileSource, Runnable paramRunnable)
  {
    if (!IS_SUPPORTED) {
      return;
    }
    synchronized (this.mLock)
    {
      this.mRenderer.source = paramTileSource;
      this.mRenderer.isReadyCallback = paramRunnable;
      ImageRendererWrapper localImageRendererWrapper1 = this.mRenderer;
      if (paramTileSource != null) {}
      for (int i = paramTileSource.getImageWidth() / 2;; i = 0)
      {
        localImageRendererWrapper1.centerX = i;
        ImageRendererWrapper localImageRendererWrapper2 = this.mRenderer;
        if (paramTileSource == null) {
          break;
        }
        j = paramTileSource.getImageHeight() / 2;
        localImageRendererWrapper2.centerY = j;
        ImageRendererWrapper localImageRendererWrapper3 = this.mRenderer;
        int k = 0;
        if (paramTileSource != null) {
          k = paramTileSource.getRotation();
        }
        localImageRendererWrapper3.rotation = k;
        this.mRenderer.scale = 0.0F;
        updateScaleIfNecessaryLocked(this.mRenderer);
        invalidate();
        return;
      }
      int j = 0;
    }
  }
  
  @SuppressLint({"NewApi"})
  public void setTranslationX(float paramFloat)
  {
    if (!IS_SUPPORTED) {
      return;
    }
    super.setTranslationX(paramFloat);
  }
  
  protected static class ImageRendererWrapper
  {
    public int centerX;
    public int centerY;
    TiledImageRenderer image;
    Runnable isReadyCallback;
    public int rotation;
    public float scale;
    public TiledImageRenderer.TileSource source;
  }
  
  private class TileRenderer
    implements GLSurfaceView.Renderer
  {
    private GLES20Canvas mCanvas;
    
    private TileRenderer() {}
    
    public void onDrawFrame(GL10 paramGL10)
    {
      this.mCanvas.clearBuffer();
      Runnable localRunnable;
      synchronized (TiledImageView.this.mLock)
      {
        localRunnable = TiledImageView.this.mRenderer.isReadyCallback;
        TiledImageView.this.mRenderer.image.setModel(TiledImageView.this.mRenderer.source, TiledImageView.this.mRenderer.rotation);
        TiledImageView.this.mRenderer.image.setPosition(TiledImageView.this.mRenderer.centerX, TiledImageView.this.mRenderer.centerY, TiledImageView.this.mRenderer.scale);
        if ((!TiledImageView.this.mRenderer.image.draw(this.mCanvas)) || (localRunnable == null)) {}
      }
      synchronized (TiledImageView.this.mLock)
      {
        if (TiledImageView.this.mRenderer.isReadyCallback == localRunnable) {
          TiledImageView.this.mRenderer.isReadyCallback = null;
        }
        if (localRunnable != null) {
          TiledImageView.this.post(localRunnable);
        }
        return;
        localObject2 = finally;
        throw localObject2;
      }
    }
    
    public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2)
    {
      this.mCanvas.setSize(paramInt1, paramInt2);
      TiledImageView.this.mRenderer.image.setViewSize(paramInt1, paramInt2);
    }
    
    public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
    {
      this.mCanvas = new GLES20Canvas();
      BasicTexture.invalidateAllTextures();
      TiledImageView.this.mRenderer.image.setModel(TiledImageView.this.mRenderer.source, TiledImageView.this.mRenderer.rotation);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.views.TiledImageView
 * JD-Core Version:    0.7.0.1
 */