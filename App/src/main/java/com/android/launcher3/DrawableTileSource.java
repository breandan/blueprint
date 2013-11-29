package com.android.launcher3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.android.gallery3d.glrenderer.BasicTexture;
import com.android.gallery3d.glrenderer.BitmapTexture;
import com.android.photos.views.TiledImageRenderer;
import com.android.photos.views.TiledImageRenderer.TileSource;

public class DrawableTileSource
  implements TiledImageRenderer.TileSource
{
  private Drawable mDrawable;
  private BitmapTexture mPreview;
  private int mPreviewSize;
  private int mTileSize;
  
  public DrawableTileSource(Context paramContext, Drawable paramDrawable, int paramInt)
  {
    this.mTileSize = TiledImageRenderer.suggestedTileSize(paramContext);
    this.mDrawable = paramDrawable;
    this.mPreviewSize = Math.min(paramInt, 1024);
  }
  
  public int getImageHeight()
  {
    return this.mDrawable.getIntrinsicHeight();
  }
  
  public int getImageWidth()
  {
    return this.mDrawable.getIntrinsicWidth();
  }
  
  public BasicTexture getPreview()
  {
    if (this.mPreviewSize == 0) {
      return null;
    }
    if (this.mPreview == null)
    {
      float f1 = getImageWidth();
      for (float f2 = getImageHeight(); (f1 > 1024.0F) || (f2 > 1024.0F); f2 /= 2.0F) {
        f1 /= 2.0F;
      }
      Bitmap localBitmap = Bitmap.createBitmap((int)f1, (int)f2, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      this.mDrawable.setBounds(new Rect(0, 0, (int)f1, (int)f2));
      this.mDrawable.draw(localCanvas);
      localCanvas.setBitmap(null);
      this.mPreview = new BitmapTexture(localBitmap);
    }
    return this.mPreview;
  }
  
  public int getRotation()
  {
    return 0;
  }
  
  public Bitmap getTile(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap)
  {
    int i = getTileSize();
    if (paramBitmap == null) {
      paramBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    }
    Canvas localCanvas = new Canvas(paramBitmap);
    Rect localRect = new Rect(0, 0, getImageWidth(), getImageHeight());
    localRect.offset(-paramInt2, -paramInt3);
    this.mDrawable.setBounds(localRect);
    this.mDrawable.draw(localCanvas);
    localCanvas.setBitmap(null);
    return paramBitmap;
  }
  
  public int getTileSize()
  {
    return this.mTileSize;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DrawableTileSource
 * JD-Core Version:    0.7.0.1
 */