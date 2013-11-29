package com.google.android.search.shared.imageloader;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.google.android.shared.util.SynchronousLoader;
import com.google.android.shared.util.Util;
import com.google.common.io.Closeables;
import java.io.FileInputStream;

public class ContentProviderImageLoader
  extends SynchronousLoader<Drawable>
{
  private final Context mContext;
  
  public ContentProviderImageLoader(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public void clearCache() {}
  
  public Drawable loadNow(Uri paramUri)
  {
    localContentProviderClient = null;
    localAssetFileDescriptor = null;
    localFileInputStream = null;
    label71:
    do
    {
      do
      {
        do
        {
          try
          {
            localContentProviderClient = this.mContext.getContentResolver().acquireUnstableContentProviderClient(paramUri);
            if (localContentProviderClient != null) {
              break label71;
            }
            Log.w("Search.ContentImageLoader", "Couldn't acquire content provider for " + paramUri);
            Closeables.closeQuietly(null);
            Util.closeQuietly(null);
            localObject2 = null;
            if (localContentProviderClient != null) {
              localContentProviderClient.release();
            }
          }
          catch (Throwable localThrowable)
          {
            do
            {
              Drawable localDrawable;
              Log.w("Search.ContentImageLoader", "Failed to load " + paramUri + ": " + localThrowable);
              Closeables.closeQuietly(localFileInputStream);
              Util.closeQuietly(localAssetFileDescriptor);
              Object localObject2 = null;
            } while (localContentProviderClient == null);
            localContentProviderClient.release();
            return null;
          }
          finally
          {
            Closeables.closeQuietly(localFileInputStream);
            Util.closeQuietly(localAssetFileDescriptor);
            if (localContentProviderClient == null) {
              break label293;
            }
            localContentProviderClient.release();
          }
          return localObject2;
          localAssetFileDescriptor = localContentProviderClient.openAssetFile(paramUri, "r");
          if (localAssetFileDescriptor != null) {
            break;
          }
          Log.w("Search.ContentImageLoader", "openAssetFile() failed for " + paramUri);
          Closeables.closeQuietly(null);
          Util.closeQuietly(localAssetFileDescriptor);
          localObject2 = null;
        } while (localContentProviderClient == null);
        localContentProviderClient.release();
        return null;
        localFileInputStream = localAssetFileDescriptor.createInputStream();
        if (localFileInputStream != null) {
          break;
        }
        Log.w("Search.ContentImageLoader", "Failed to create input stream");
        Closeables.closeQuietly(localFileInputStream);
        Util.closeQuietly(localAssetFileDescriptor);
        localObject2 = null;
      } while (localContentProviderClient == null);
      localContentProviderClient.release();
      return null;
      localDrawable = Drawable.createFromResourceStream(this.mContext.getResources(), null, localFileInputStream, null);
      localObject2 = localDrawable;
      Closeables.closeQuietly(localFileInputStream);
      Util.closeQuietly(localAssetFileDescriptor);
    } while (localContentProviderClient == null);
    localContentProviderClient.release();
    return localObject2;
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return "content".equals(paramUri.getScheme());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.imageloader.ContentProviderImageLoader
 * JD-Core Version:    0.7.0.1
 */