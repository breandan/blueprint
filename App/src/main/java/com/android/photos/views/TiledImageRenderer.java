package com.android.photos.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LongSparseArray;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SynchronizedPool;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.glrenderer.BasicTexture;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.UploadedTexture;

public class TiledImageRenderer
{
  private static Pools.Pool<Bitmap> sTilePool = new Pools.SynchronizedPool(64);
  private final Rect[] mActiveRange;
  private final LongSparseArray<Tile> mActiveTiles = new LongSparseArray();
  private boolean mBackgroundTileUploaded;
  protected int mCenterX;
  protected int mCenterY;
  private final TileQueue mDecodeQueue = new TileQueue(null);
  protected int mImageHeight = -1;
  protected int mImageWidth = -1;
  private boolean mLayoutTiles;
  private int mLevel = 0;
  protected int mLevelCount;
  private TileSource mModel;
  private int mOffsetX;
  private int mOffsetY;
  private View mParent;
  private BasicTexture mPreview;
  private final Object mQueueLock = new Object();
  private final TileQueue mRecycledQueue = new TileQueue(null);
  private boolean mRenderComplete;
  protected int mRotation;
  protected float mScale;
  private final RectF mSourceRect = new RectF();
  private final RectF mTargetRect = new RectF();
  private TileDecoder mTileDecoder;
  private final Rect mTileRange = new Rect();
  private int mTileSize;
  private final TileQueue mUploadQueue = new TileQueue(null);
  private int mUploadQuota;
  private int mViewHeight;
  private int mViewWidth;
  
  public TiledImageRenderer(View paramView)
  {
    Rect[] arrayOfRect = new Rect[2];
    arrayOfRect[0] = new Rect();
    arrayOfRect[1] = new Rect();
    this.mActiveRange = arrayOfRect;
    this.mParent = paramView;
    this.mTileDecoder = new TileDecoder(null);
    this.mTileDecoder.start();
  }
  
  private void activateTile(int paramInt1, int paramInt2, int paramInt3)
  {
    long l = makeTileKey(paramInt1, paramInt2, paramInt3);
    Tile localTile1 = (Tile)this.mActiveTiles.get(l);
    if (localTile1 != null)
    {
      if (localTile1.mTileState == 2) {
        localTile1.mTileState = 1;
      }
      return;
    }
    Tile localTile2 = obtainTile(paramInt1, paramInt2, paramInt3);
    this.mActiveTiles.put(l, localTile2);
  }
  
  private void calculateLevelCount()
  {
    if (this.mPreview != null)
    {
      this.mLevelCount = Math.max(0, Utils.ceilLog2(this.mImageWidth / this.mPreview.getWidth()));
      return;
    }
    int i = 1;
    int j = Math.max(this.mImageWidth, this.mImageHeight);
    int k = this.mTileSize;
    while (k < j)
    {
      k <<= 1;
      i++;
    }
    this.mLevelCount = i;
  }
  
  private void decodeTile(Tile paramTile)
  {
    boolean bool;
    synchronized (this.mQueueLock)
    {
      if (paramTile.mTileState != 2) {
        return;
      }
      paramTile.mTileState = 4;
      bool = paramTile.decode();
      synchronized (this.mQueueLock)
      {
        if (paramTile.mTileState == 32)
        {
          paramTile.mTileState = 64;
          if (paramTile.mDecodedTile != null)
          {
            sTilePool.release(paramTile.mDecodedTile);
            paramTile.mDecodedTile = null;
          }
          this.mRecycledQueue.push(paramTile);
          return;
        }
      }
    }
    if (bool) {}
    for (int i = 8;; i = 16)
    {
      paramTile.mTileState = i;
      if (!bool) {
        return;
      }
      this.mUploadQueue.push(paramTile);
      invalidate();
      return;
    }
  }
  
  private void drawTile(GLCanvas paramGLCanvas, int paramInt1, int paramInt2, int paramInt3, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    RectF localRectF1 = this.mSourceRect;
    RectF localRectF2 = this.mTargetRect;
    localRectF2.set(paramFloat1, paramFloat2, paramFloat1 + paramFloat3, paramFloat2 + paramFloat3);
    localRectF1.set(0.0F, 0.0F, this.mTileSize, this.mTileSize);
    Tile localTile = getTile(paramInt1, paramInt2, paramInt3);
    if (localTile != null) {
      if (!localTile.isContentValid())
      {
        if (localTile.mTileState != 8) {
          break label127;
        }
        if (this.mUploadQuota > 0)
        {
          this.mUploadQuota = (-1 + this.mUploadQuota);
          localTile.updateContent(paramGLCanvas);
        }
      }
      else
      {
        if (!drawTile(localTile, paramGLCanvas, localRectF1, localRectF2)) {
          break label151;
        }
      }
    }
    label127:
    while (this.mPreview == null)
    {
      return;
      this.mRenderComplete = false;
      break;
      if (localTile.mTileState == 16) {
        break;
      }
      this.mRenderComplete = false;
      queueForDecode(localTile);
      break;
    }
    label151:
    int i = this.mTileSize << paramInt3;
    float f1 = this.mPreview.getWidth() / this.mImageWidth;
    float f2 = this.mPreview.getHeight() / this.mImageHeight;
    localRectF1.set(f1 * paramInt1, f2 * paramInt2, f1 * (paramInt1 + i), f2 * (paramInt2 + i));
    paramGLCanvas.drawTexture(this.mPreview, localRectF1, localRectF2);
  }
  
  private boolean drawTile(Tile paramTile, GLCanvas paramGLCanvas, RectF paramRectF1, RectF paramRectF2)
  {
    if (paramTile.isContentValid())
    {
      paramGLCanvas.drawTexture(paramTile, paramRectF1, paramRectF2);
      return true;
    }
    Tile localTile = paramTile.getParentTile();
    if (localTile == null) {
      return false;
    }
    if (paramTile.mX == localTile.mX)
    {
      paramRectF1.left /= 2.0F;
      paramRectF1.right /= 2.0F;
      label64:
      if (paramTile.mY != localTile.mY) {
        break label137;
      }
      paramRectF1.top /= 2.0F;
    }
    for (paramRectF1.bottom /= 2.0F;; paramRectF1.bottom = ((this.mTileSize + paramRectF1.bottom) / 2.0F))
    {
      paramTile = localTile;
      break;
      paramRectF1.left = ((this.mTileSize + paramRectF1.left) / 2.0F);
      paramRectF1.right = ((this.mTileSize + paramRectF1.right) / 2.0F);
      break label64;
      label137:
      paramRectF1.top = ((this.mTileSize + paramRectF1.top) / 2.0F);
    }
  }
  
  private void getRange(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, float paramFloat, int paramInt4)
  {
    double d1 = Math.toRadians(-paramInt4);
    double d2 = this.mViewWidth;
    double d3 = this.mViewHeight;
    double d4 = Math.cos(d1);
    double d5 = Math.sin(d1);
    int i = (int)Math.ceil(Math.max(Math.abs(d4 * d2 - d5 * d3), Math.abs(d4 * d2 + d5 * d3)));
    int j = (int)Math.ceil(Math.max(Math.abs(d5 * d2 + d4 * d3), Math.abs(d5 * d2 - d4 * d3)));
    int k = (int)Math.floor(paramInt1 - i / (2.0F * paramFloat));
    int m = (int)Math.floor(paramInt2 - j / (2.0F * paramFloat));
    int n = (int)Math.ceil(k + i / paramFloat);
    int i1 = (int)Math.ceil(m + j / paramFloat);
    int i2 = this.mTileSize << paramInt3;
    paramRect.set(Math.max(0, i2 * (k / i2)), Math.max(0, i2 * (m / i2)), Math.min(this.mImageWidth, n), Math.min(this.mImageHeight, i1));
  }
  
  private void getRange(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    getRange(paramRect, paramInt1, paramInt2, paramInt3, 1.0F / (1 << paramInt3 + 1), paramInt4);
  }
  
  private Tile getTile(int paramInt1, int paramInt2, int paramInt3)
  {
    return (Tile)this.mActiveTiles.get(makeTileKey(paramInt1, paramInt2, paramInt3));
  }
  
  private void invalidate()
  {
    this.mParent.postInvalidate();
  }
  
  private void invalidateTiles()
  {
    synchronized (this.mQueueLock)
    {
      this.mDecodeQueue.clean();
      this.mUploadQueue.clean();
      int i = this.mActiveTiles.size();
      for (int j = 0; j < i; j++) {
        recycleTile((Tile)this.mActiveTiles.valueAt(j));
      }
      this.mActiveTiles.clear();
      return;
    }
  }
  
  private static boolean isHighResolution(Context paramContext)
  {
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
    return (localDisplayMetrics.heightPixels > 2048) || (localDisplayMetrics.widthPixels > 2048);
  }
  
  private void layoutTiles()
  {
    if ((this.mViewWidth == 0) || (this.mViewHeight == 0) || (!this.mLayoutTiles)) {}
    int j;
    int k;
    Rect[] arrayOfRect;
    do
    {
      return;
      this.mLayoutTiles = false;
      this.mLevel = Utils.clamp(Utils.floorLog2(1.0F / this.mScale), 0, this.mLevelCount);
      int i;
      if (this.mLevel != this.mLevelCount)
      {
        Rect localRect2 = this.mTileRange;
        getRange(localRect2, this.mCenterX, this.mCenterY, this.mLevel, this.mScale, this.mRotation);
        this.mOffsetX = Math.round(this.mViewWidth / 2.0F + (localRect2.left - this.mCenterX) * this.mScale);
        this.mOffsetY = Math.round(this.mViewHeight / 2.0F + (localRect2.top - this.mCenterY) * this.mScale);
        if (this.mScale * (1 << this.mLevel) > 0.75F) {
          i = -1 + this.mLevel;
        }
      }
      for (;;)
      {
        j = Math.max(0, Math.min(i, -2 + this.mLevelCount));
        k = Math.min(j + 2, this.mLevelCount);
        arrayOfRect = this.mActiveRange;
        for (int m = j; m < k; m++) {
          getRange(arrayOfRect[(m - j)], this.mCenterX, this.mCenterY, m, this.mRotation);
        }
        i = this.mLevel;
        continue;
        i = -2 + this.mLevel;
        this.mOffsetX = Math.round(this.mViewWidth / 2.0F - this.mCenterX * this.mScale);
        this.mOffsetY = Math.round(this.mViewHeight / 2.0F - this.mCenterY * this.mScale);
      }
    } while (this.mRotation % 90 != 0);
    for (;;)
    {
      int i1;
      int i3;
      int i4;
      int i5;
      synchronized (this.mQueueLock)
      {
        this.mDecodeQueue.clean();
        this.mUploadQueue.clean();
        this.mBackgroundTileUploaded = false;
        int n = this.mActiveTiles.size();
        i1 = 0;
        if (i1 < n)
        {
          Tile localTile = (Tile)this.mActiveTiles.valueAt(i1);
          int i2 = localTile.mTileLevel;
          if ((i2 >= j) && (i2 < k) && (arrayOfRect[(i2 - j)].contains(localTile.mX, localTile.mY))) {
            break label579;
          }
          this.mActiveTiles.removeAt(i1);
          i1--;
          n--;
          recycleTile(localTile);
          break label579;
        }
        i3 = j;
        if (i3 >= k) {
          break label574;
        }
        i4 = this.mTileSize << i3;
        Rect localRect1 = arrayOfRect[(i3 - j)];
        i5 = localRect1.top;
        int i6 = localRect1.bottom;
        if (i5 >= i6) {
          break label568;
        }
        int i7 = localRect1.left;
        int i8 = localRect1.right;
        if (i7 < i8)
        {
          activateTile(i7, i5, i3);
          i7 += i4;
        }
      }
      i5 += i4;
      continue;
      label568:
      i3++;
      continue;
      label574:
      invalidate();
      return;
      label579:
      i1++;
    }
  }
  
  private static long makeTileKey(int paramInt1, int paramInt2, int paramInt3)
  {
    return (paramInt1 << 16 | paramInt2) << 16 | paramInt3;
  }
  
  private Tile obtainTile(int paramInt1, int paramInt2, int paramInt3)
  {
    synchronized (this.mQueueLock)
    {
      Tile localTile1 = this.mRecycledQueue.pop();
      if (localTile1 != null)
      {
        localTile1.mTileState = 1;
        localTile1.update(paramInt1, paramInt2, paramInt3);
        return localTile1;
      }
      Tile localTile2 = new Tile(paramInt1, paramInt2, paramInt3);
      return localTile2;
    }
  }
  
  private void queueForDecode(Tile paramTile)
  {
    synchronized (this.mQueueLock)
    {
      if (paramTile.mTileState == 1)
      {
        paramTile.mTileState = 2;
        if (this.mDecodeQueue.push(paramTile)) {
          this.mQueueLock.notifyAll();
        }
      }
      return;
    }
  }
  
  private void recycleTile(Tile paramTile)
  {
    synchronized (this.mQueueLock)
    {
      if (paramTile.mTileState == 4)
      {
        paramTile.mTileState = 32;
        return;
      }
      paramTile.mTileState = 64;
      if (paramTile.mDecodedTile != null)
      {
        sTilePool.release(paramTile.mDecodedTile);
        paramTile.mDecodedTile = null;
      }
      this.mRecycledQueue.push(paramTile);
      return;
    }
  }
  
  public static int suggestedTileSize(Context paramContext)
  {
    if (isHighResolution(paramContext)) {
      return 512;
    }
    return 256;
  }
  
  private void uploadBackgroundTiles(GLCanvas paramGLCanvas)
  {
    this.mBackgroundTileUploaded = true;
    int i = this.mActiveTiles.size();
    for (int j = 0; j < i; j++)
    {
      Tile localTile = (Tile)this.mActiveTiles.valueAt(j);
      if (!localTile.isContentValid()) {
        queueForDecode(localTile);
      }
    }
  }
  
  private void uploadTiles(GLCanvas paramGLCanvas)
  {
    int i = 1;
    Tile localTile = null;
    for (;;)
    {
      if (i > 0) {}
      synchronized (this.mQueueLock)
      {
        localTile = this.mUploadQueue.pop();
        if (localTile == null)
        {
          if (localTile != null) {
            invalidate();
          }
          return;
        }
      }
      if (!localTile.isContentValid()) {
        if (localTile.mTileState == 8)
        {
          localTile.updateContent(paramGLCanvas);
          i--;
        }
        else
        {
          Log.w("TiledImageRenderer", "Tile in upload queue has invalid state: " + localTile.mTileState);
        }
      }
    }
  }
  
  public boolean draw(GLCanvas paramGLCanvas)
  {
    layoutTiles();
    uploadTiles(paramGLCanvas);
    this.mUploadQuota = 1;
    this.mRenderComplete = true;
    int i = this.mLevel;
    int j = this.mRotation;
    int k = 0;
    if (j != 0) {
      k = 0x0 | 0x2;
    }
    if (k != 0)
    {
      paramGLCanvas.save(k);
      if (j != 0)
      {
        int i4 = this.mViewWidth / 2;
        int i5 = this.mViewHeight / 2;
        paramGLCanvas.translate(i4, i5);
        paramGLCanvas.rotate(j, 0.0F, 0.0F, 1.0F);
        paramGLCanvas.translate(-i4, -i5);
      }
    }
    for (;;)
    {
      int m;
      int n;
      int i1;
      try
      {
        if (i != this.mLevelCount)
        {
          m = this.mTileSize << i;
          float f1 = m * this.mScale;
          Rect localRect = this.mTileRange;
          n = localRect.top;
          i1 = 0;
          if (n < localRect.bottom)
          {
            float f2 = this.mOffsetY + f1 * i1;
            int i2 = localRect.left;
            int i3 = 0;
            if (i2 >= localRect.right) {
              break label356;
            }
            drawTile(paramGLCanvas, i2, n, i, this.mOffsetX + f1 * i3, f2, f1);
            i2 += m;
            i3++;
            continue;
          }
        }
        else if (this.mPreview != null)
        {
          this.mPreview.draw(paramGLCanvas, this.mOffsetX, this.mOffsetY, Math.round(this.mImageWidth * this.mScale), Math.round(this.mImageHeight * this.mScale));
        }
        if (k != 0) {
          paramGLCanvas.restore();
        }
        if (this.mRenderComplete)
        {
          if (!this.mBackgroundTileUploaded) {
            uploadBackgroundTiles(paramGLCanvas);
          }
          if ((!this.mRenderComplete) && (this.mPreview == null)) {
            break label354;
          }
          return true;
        }
      }
      finally
      {
        if (k != 0) {
          paramGLCanvas.restore();
        }
      }
      invalidate();
      continue;
      label354:
      return false;
      label356:
      n += m;
      i1++;
    }
  }
  
  public void freeTextures()
  {
    this.mLayoutTiles = true;
    this.mTileDecoder.finishAndWait();
    synchronized (this.mQueueLock)
    {
      this.mUploadQueue.clean();
      this.mDecodeQueue.clean();
      for (Tile localTile = this.mRecycledQueue.pop(); localTile != null; localTile = this.mRecycledQueue.pop()) {
        localTile.recycle();
      }
      int i = this.mActiveTiles.size();
      int j = 0;
      if (j < i)
      {
        ((Tile)this.mActiveTiles.valueAt(j)).recycle();
        j++;
      }
    }
    this.mActiveTiles.clear();
    this.mTileRange.set(0, 0, 0, 0);
    while (sTilePool.acquire() != null) {}
  }
  
  public void notifyModelInvalidated()
  {
    invalidateTiles();
    if (this.mModel == null)
    {
      this.mImageWidth = 0;
      this.mImageHeight = 0;
      this.mLevelCount = 0;
      this.mPreview = null;
    }
    for (;;)
    {
      this.mLayoutTiles = true;
      return;
      this.mImageWidth = this.mModel.getImageWidth();
      this.mImageHeight = this.mModel.getImageHeight();
      this.mPreview = this.mModel.getPreview();
      this.mTileSize = this.mModel.getTileSize();
      calculateLevelCount();
    }
  }
  
  public void setModel(TileSource paramTileSource, int paramInt)
  {
    if (this.mModel != paramTileSource)
    {
      this.mModel = paramTileSource;
      notifyModelInvalidated();
    }
    if (this.mRotation != paramInt)
    {
      this.mRotation = paramInt;
      this.mLayoutTiles = true;
    }
  }
  
  public void setPosition(int paramInt1, int paramInt2, float paramFloat)
  {
    if ((this.mCenterX == paramInt1) && (this.mCenterY == paramInt2) && (this.mScale == paramFloat)) {
      return;
    }
    this.mCenterX = paramInt1;
    this.mCenterY = paramInt2;
    this.mScale = paramFloat;
    this.mLayoutTiles = true;
  }
  
  public void setViewSize(int paramInt1, int paramInt2)
  {
    this.mViewWidth = paramInt1;
    this.mViewHeight = paramInt2;
  }
  
  private class Tile
    extends UploadedTexture
  {
    public Bitmap mDecodedTile;
    public Tile mNext;
    public int mTileLevel;
    public volatile int mTileState = 1;
    public int mX;
    public int mY;
    
    public Tile(int paramInt1, int paramInt2, int paramInt3)
    {
      this.mX = paramInt1;
      this.mY = paramInt2;
      this.mTileLevel = paramInt3;
    }
    
    boolean decode()
    {
      try
      {
        Bitmap localBitmap = (Bitmap)TiledImageRenderer.sTilePool.acquire();
        if ((localBitmap != null) && (localBitmap.getWidth() != TiledImageRenderer.this.mTileSize)) {
          localBitmap = null;
        }
        this.mDecodedTile = TiledImageRenderer.this.mModel.getTile(this.mTileLevel, this.mX, this.mY, localBitmap);
      }
      catch (Throwable localThrowable)
      {
        for (;;)
        {
          Log.w("TiledImageRenderer", "fail to decode tile", localThrowable);
        }
      }
      return this.mDecodedTile != null;
    }
    
    public Tile getParentTile()
    {
      if (1 + this.mTileLevel == TiledImageRenderer.this.mLevelCount) {
        return null;
      }
      int i = TiledImageRenderer.this.mTileSize << 1 + this.mTileLevel;
      int j = i * (this.mX / i);
      int k = i * (this.mY / i);
      return TiledImageRenderer.this.getTile(j, k, 1 + this.mTileLevel);
    }
    
    public int getTextureHeight()
    {
      return TiledImageRenderer.this.mTileSize;
    }
    
    public int getTextureWidth()
    {
      return TiledImageRenderer.this.mTileSize;
    }
    
    protected void onFreeBitmap(Bitmap paramBitmap)
    {
      TiledImageRenderer.sTilePool.release(paramBitmap);
    }
    
    protected Bitmap onGetBitmap()
    {
      if (this.mTileState == 8) {}
      for (boolean bool = true;; bool = false)
      {
        Utils.assertTrue(bool);
        int i = TiledImageRenderer.this.mImageWidth - this.mX >> this.mTileLevel;
        int j = TiledImageRenderer.this.mImageHeight - this.mY >> this.mTileLevel;
        setSize(Math.min(TiledImageRenderer.this.mTileSize, i), Math.min(TiledImageRenderer.this.mTileSize, j));
        Bitmap localBitmap = this.mDecodedTile;
        this.mDecodedTile = null;
        this.mTileState = 1;
        return localBitmap;
      }
    }
    
    public String toString()
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Integer.valueOf(this.mX / TiledImageRenderer.this.mTileSize);
      arrayOfObject[1] = Integer.valueOf(this.mY / TiledImageRenderer.this.mTileSize);
      arrayOfObject[2] = Integer.valueOf(TiledImageRenderer.this.mLevel);
      arrayOfObject[3] = Integer.valueOf(TiledImageRenderer.this.mLevelCount);
      return String.format("tile(%s, %s, %s / %s)", arrayOfObject);
    }
    
    public void update(int paramInt1, int paramInt2, int paramInt3)
    {
      this.mX = paramInt1;
      this.mY = paramInt2;
      this.mTileLevel = paramInt3;
      invalidateContent();
    }
  }
  
  private class TileDecoder
    extends Thread
  {
    private TileDecoder() {}
    
    private TiledImageRenderer.Tile waitForTile()
      throws InterruptedException
    {
      synchronized (TiledImageRenderer.this.mQueueLock)
      {
        TiledImageRenderer.Tile localTile = TiledImageRenderer.this.mDecodeQueue.pop();
        if (localTile != null) {
          return localTile;
        }
        TiledImageRenderer.this.mQueueLock.wait();
      }
    }
    
    public void finishAndWait()
    {
      interrupt();
      try
      {
        join();
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.w("TiledImageRenderer", "Interrupted while waiting for TileDecoder thread to finish!");
      }
    }
    
    public void run()
    {
      try
      {
        while (!isInterrupted())
        {
          TiledImageRenderer.Tile localTile = waitForTile();
          TiledImageRenderer.this.decodeTile(localTile);
        }
        return;
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  private static class TileQueue
  {
    private TiledImageRenderer.Tile mHead;
    
    private boolean contains(TiledImageRenderer.Tile paramTile)
    {
      for (TiledImageRenderer.Tile localTile = this.mHead; localTile != null; localTile = localTile.mNext) {
        if (localTile == paramTile) {
          return true;
        }
      }
      return false;
    }
    
    public void clean()
    {
      this.mHead = null;
    }
    
    public TiledImageRenderer.Tile pop()
    {
      TiledImageRenderer.Tile localTile = this.mHead;
      if (localTile != null) {
        this.mHead = localTile.mNext;
      }
      return localTile;
    }
    
    public boolean push(TiledImageRenderer.Tile paramTile)
    {
      if (contains(paramTile))
      {
        Log.w("TiledImageRenderer", "Attempting to add a tile already in the queue!");
        return false;
      }
      TiledImageRenderer.Tile localTile = this.mHead;
      boolean bool = false;
      if (localTile == null) {
        bool = true;
      }
      paramTile.mNext = this.mHead;
      this.mHead = paramTile;
      return bool;
    }
  }
  
  public static abstract interface TileSource
  {
    public abstract int getImageHeight();
    
    public abstract int getImageWidth();
    
    public abstract BasicTexture getPreview();
    
    public abstract int getRotation();
    
    public abstract Bitmap getTile(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap);
    
    public abstract int getTileSize();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.views.TiledImageRenderer
 * JD-Core Version:    0.7.0.1
 */