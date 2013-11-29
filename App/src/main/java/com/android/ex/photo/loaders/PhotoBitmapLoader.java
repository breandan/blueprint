package com.android.ex.photo.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.util.ImageUtils;

public class PhotoBitmapLoader
  extends AsyncTaskLoader<PhotoBitmapLoaderInterface.BitmapResult>
  implements PhotoBitmapLoaderInterface
{
  private Bitmap mBitmap;
  private String mPhotoUri;
  
  public PhotoBitmapLoader(Context paramContext, String paramString)
  {
    super(paramContext);
    this.mPhotoUri = paramString;
  }
  
  public void deliverResult(PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult)
  {
    Bitmap localBitmap1;
    if (paramBitmapResult != null)
    {
      localBitmap1 = paramBitmapResult.bitmap;
      if (!isReset()) {
        break label31;
      }
      if (localBitmap1 != null) {
        onReleaseResources(localBitmap1);
      }
    }
    label31:
    Bitmap localBitmap2;
    do
    {
      return;
      localBitmap1 = null;
      break;
      localBitmap2 = this.mBitmap;
      this.mBitmap = localBitmap1;
      if (isStarted()) {
        super.deliverResult(paramBitmapResult);
      }
    } while ((localBitmap2 == null) || (localBitmap2 == localBitmap1) || (localBitmap2.isRecycled()));
    onReleaseResources(localBitmap2);
  }
  
  public PhotoBitmapLoaderInterface.BitmapResult loadInBackground()
  {
    PhotoBitmapLoaderInterface.BitmapResult localBitmapResult = new PhotoBitmapLoaderInterface.BitmapResult();
    Context localContext = getContext();
    ContentResolver localContentResolver;
    if ((localContext != null) && (this.mPhotoUri != null)) {
      localContentResolver = localContext.getContentResolver();
    }
    try
    {
      localBitmapResult = ImageUtils.createLocalBitmap(localContentResolver, Uri.parse(this.mPhotoUri), PhotoViewFragment.sPhotoSize.intValue());
      if (localBitmapResult.bitmap != null) {
        localBitmapResult.bitmap.setDensity(160);
      }
      return localBitmapResult;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      localBitmapResult.status = 1;
    }
    return localBitmapResult;
  }
  
  public void onCanceled(PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult)
  {
    super.onCanceled(paramBitmapResult);
    if (paramBitmapResult != null) {
      onReleaseResources(paramBitmapResult.bitmap);
    }
  }
  
  protected void onReleaseResources(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
      paramBitmap.recycle();
    }
  }
  
  protected void onReset()
  {
    super.onReset();
    onStopLoading();
    if (this.mBitmap != null)
    {
      onReleaseResources(this.mBitmap);
      this.mBitmap = null;
    }
  }
  
  protected void onStartLoading()
  {
    if (this.mBitmap != null)
    {
      PhotoBitmapLoaderInterface.BitmapResult localBitmapResult = new PhotoBitmapLoaderInterface.BitmapResult();
      localBitmapResult.status = 0;
      localBitmapResult.bitmap = this.mBitmap;
      deliverResult(localBitmapResult);
    }
    if ((takeContentChanged()) || (this.mBitmap == null)) {
      forceLoad();
    }
  }
  
  protected void onStopLoading()
  {
    cancelLoad();
  }
  
  public void setPhotoUri(String paramString)
  {
    this.mPhotoUri = paramString;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.loaders.PhotoBitmapLoader
 * JD-Core Version:    0.7.0.1
 */