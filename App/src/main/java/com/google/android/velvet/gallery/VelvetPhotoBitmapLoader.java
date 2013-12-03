package com.google.android.velvet.gallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.UriLoader;
import com.google.android.velvet.VelvetServices;

public class VelvetPhotoBitmapLoader
        extends Loader<PhotoBitmapLoaderInterface.BitmapResult>
        implements PhotoBitmapLoaderInterface {
    private Drawable mDrawable;
    private final UriLoader<Drawable> mNetworkImageLoader;
    private final NetworkImageLoaderConsumer mNetworkImageLoaderConsumer;
    private Uri mPhotoUri;

    VelvetPhotoBitmapLoader(Context paramContext, UriLoader<Drawable> paramUriLoader, String paramString) {
        super(paramContext);
        this.mNetworkImageLoader = paramUriLoader;
        this.mNetworkImageLoaderConsumer = new NetworkImageLoaderConsumer(null);
        Uri localUri = null;
        if (paramString == null) {
        }
        for (; ; ) {
            this.mPhotoUri = localUri;
            return;
            localUri = Uri.parse(paramString);
        }
    }

    public VelvetPhotoBitmapLoader(Context paramContext, String paramString) {
        this(paramContext, VelvetServices.get().getNonCachingImageLoader(), paramString);
    }

    public void deliverResult(PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult) {
        if (isReset()) {
        }
        do {
            return;
            this.mDrawable = paramBitmapResult.drawable;
        } while (!isStarted());
        super.deliverResult(paramBitmapResult);
    }

    protected void onReset() {
        super.onReset();
        onStopLoading();
        this.mDrawable = null;
    }

    public void onStartLoading() {
        if (this.mDrawable != null) {
            PhotoBitmapLoaderInterface.BitmapResult localBitmapResult = new PhotoBitmapLoaderInterface.BitmapResult();
            localBitmapResult.status = 0;
            localBitmapResult.drawable = this.mDrawable;
            deliverResult(localBitmapResult);
            return;
        }
        if (this.mPhotoUri != null) {
            this.mNetworkImageLoader.load(this.mPhotoUri).getLater(this.mNetworkImageLoaderConsumer);
            return;
        }
        Log.e("VelvetPhotoBitmapLoader", "Attempted to load a null URI");
    }

    public void setPhotoUri(String paramString) {
        if (paramString == null) {
        }
        for (Uri localUri = null; ; localUri = Uri.parse(paramString)) {
            this.mPhotoUri = localUri;
            return;
        }
    }

    private class NetworkImageLoaderConsumer
            implements Consumer<Drawable> {
        private NetworkImageLoaderConsumer() {
        }

        public boolean consume(Drawable paramDrawable) {
            PhotoBitmapLoaderInterface.BitmapResult localBitmapResult = new PhotoBitmapLoaderInterface.BitmapResult();
            localBitmapResult.drawable = paramDrawable;
            if (paramDrawable == null) {
                localBitmapResult.status = 1;
            }
            VelvetPhotoBitmapLoader.this.deliverResult(localBitmapResult);
            return true;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.VelvetPhotoBitmapLoader

 * JD-Core Version:    0.7.0.1

 */