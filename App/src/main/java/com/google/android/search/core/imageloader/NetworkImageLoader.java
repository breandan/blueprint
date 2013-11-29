package com.google.android.search.core.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.shared.util.SynchronousLoader;
import java.io.IOException;
import junit.framework.Assert;

public class NetworkImageLoader
  extends SynchronousLoader<Drawable>
{
  static final int MAX_BITMAP_SIZE = 2048;
  private final HttpHelper mHttpHelper;
  private final Resources mResources;
  
  public NetworkImageLoader(HttpHelper paramHttpHelper, Resources paramResources)
  {
    this.mHttpHelper = paramHttpHelper;
    this.mResources = paramResources;
  }
  
  private int calculateScale(int paramInt1, int paramInt2)
  {
    int i = 1;
    if ((paramInt2 > 2048) || (paramInt1 > 2048)) {
      i = Math.max(Math.round(paramInt2 / 2048.0F), Math.round(paramInt1 / 2048.0F));
    }
    return i;
  }
  
  public void clearCache() {}
  
  public Drawable loadNow(Uri paramUri)
  {
    boolean bool1;
    if (!paramUri.getScheme().equals("http"))
    {
      boolean bool2 = paramUri.getScheme().equals("https");
      bool1 = false;
      if (!bool2) {}
    }
    else
    {
      bool1 = true;
    }
    Assert.assertTrue(bool1);
    try
    {
      HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(paramUri.toString());
      localGetRequest.setMaxStaleSecs(86400);
      byte[] arrayOfByte = this.mHttpHelper.rawGet(localGetRequest, 7);
      if (arrayOfByte == null) {
        return null;
      }
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      localOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, localOptions);
      if ((localOptions.outWidth > 0) && (localOptions.outHeight > 0)) {
        localOptions.inSampleSize = calculateScale(localOptions.outWidth, localOptions.outHeight);
      }
      localOptions.inJustDecodeBounds = false;
      Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, localOptions);
      if ((localBitmap != null) && ((localBitmap.getWidth() > 2048) || (localBitmap.getHeight() > 2048)))
      {
        int i = calculateScale(localBitmap.getWidth(), localBitmap.getHeight());
        if (i > 1) {
          localBitmap = Bitmap.createScaledBitmap(localBitmap, localBitmap.getWidth() / i, localBitmap.getHeight() / i, false);
        }
      }
      if (localBitmap == null)
      {
        Log.e("Search.NetworkImageLoader", "Failed to decode " + paramUri);
        return null;
      }
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(this.mResources, localBitmap);
      return localBitmapDrawable;
    }
    catch (HttpHelper.HttpException localHttpException)
    {
      Log.e("Search.NetworkImageLoader", "[1] Failed to load " + paramUri, localHttpException);
      return null;
    }
    catch (IOException localIOException)
    {
      Log.e("Search.NetworkImageLoader", "[2] Failed to load " + paramUri, localIOException);
    }
    return null;
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    String str = paramUri.getScheme();
    return ("http".equals(str)) || ("https".equals(str));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.imageloader.NetworkImageLoader
 * JD-Core Version:    0.7.0.1
 */