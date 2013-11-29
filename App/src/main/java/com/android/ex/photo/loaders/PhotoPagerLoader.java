package com.android.ex.photo.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.content.CursorLoader;
import com.android.ex.photo.provider.PhotoContract.PhotoQuery;

public class PhotoPagerLoader
  extends CursorLoader
{
  private final Uri mPhotosUri;
  private final String[] mProjection;
  
  public PhotoPagerLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString)
  {
    super(paramContext);
    this.mPhotosUri = paramUri;
    if (paramArrayOfString != null) {}
    for (;;)
    {
      this.mProjection = paramArrayOfString;
      return;
      paramArrayOfString = PhotoContract.PhotoQuery.PROJECTION;
    }
  }
  
  public Cursor loadInBackground()
  {
    setUri(this.mPhotosUri.buildUpon().appendQueryParameter("contentType", "image/").build());
    setProjection(this.mProjection);
    return super.loadInBackground();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.loaders.PhotoPagerLoader
 * JD-Core Version:    0.7.0.1
 */