package com.google.android.search.shared.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.shared.util.SynchronousLoader;

public class ResizingImageLoader
  extends SynchronousLoader<Drawable>
{
  private final int mMaxHeight;
  private final int mMaxWidth;
  private final SynchronousLoader<Drawable> mWrapped;
  
  public ResizingImageLoader(int paramInt1, int paramInt2, SynchronousLoader<Drawable> paramSynchronousLoader)
  {
    this.mMaxWidth = paramInt1;
    this.mMaxHeight = paramInt2;
    this.mWrapped = paramSynchronousLoader;
  }
  
  private Drawable resize(Drawable paramDrawable)
  {
    if (paramDrawable == null) {
      paramDrawable = null;
    }
    Bitmap localBitmap1;
    do
    {
      do
      {
        return paramDrawable;
      } while (!(paramDrawable instanceof BitmapDrawable));
      localBitmap1 = ((BitmapDrawable)paramDrawable).getBitmap();
    } while ((localBitmap1 == null) || ((localBitmap1.getWidth() <= this.mMaxWidth) && (localBitmap1.getHeight() <= this.mMaxHeight)));
    float f = Math.min(this.mMaxWidth / localBitmap1.getWidth(), this.mMaxHeight / localBitmap1.getHeight());
    Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, Math.round(f * localBitmap1.getWidth()), Math.round(f * localBitmap1.getHeight()), false);
    localBitmap2.setDensity(0);
    return new BitmapDrawable(null, localBitmap2);
  }
  
  public void clearCache() {}
  
  public Drawable loadNow(Uri paramUri)
  {
    return resize((Drawable)this.mWrapped.loadNow(paramUri));
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return this.mWrapped.supportsUri(paramUri);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.imageloader.ResizingImageLoader
 * JD-Core Version:    0.7.0.1
 */