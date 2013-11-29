package com.android.ex.photo.loaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract interface PhotoBitmapLoaderInterface
{
  public abstract void forceLoad();
  
  public abstract void setPhotoUri(String paramString);
  
  public static class BitmapResult
  {
    public Bitmap bitmap;
    public Drawable drawable;
    public int status;
    
    public Drawable getDrawable(Resources paramResources)
    {
      if (paramResources == null) {
        throw new IllegalArgumentException("resources can not be null!");
      }
      if (this.drawable != null) {
        return this.drawable;
      }
      return new BitmapDrawable(paramResources, this.bitmap);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.loaders.PhotoBitmapLoaderInterface
 * JD-Core Version:    0.7.0.1
 */