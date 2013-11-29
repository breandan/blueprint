package com.google.android.search.core.util;

import android.net.Uri;
import android.util.Log;
import com.google.android.shared.util.SynchronousLoader;
import com.google.common.base.Preconditions;
import java.io.IOException;

public class NetworkBytesLoader
  extends SynchronousLoader<byte[]>
{
  private final HttpHelper mHttpHelper;
  private final int mTrafficTag;
  
  public NetworkBytesLoader(HttpHelper paramHttpHelper, int paramInt)
  {
    this.mHttpHelper = paramHttpHelper;
    this.mTrafficTag = paramInt;
  }
  
  public void clearCache() {}
  
  public byte[] loadNow(Uri paramUri)
  {
    if ((paramUri.getScheme().equals("http")) || (paramUri.getScheme().equals("https"))) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      try
      {
        byte[] arrayOfByte = this.mHttpHelper.rawGet(new HttpHelper.GetRequest(paramUri.toString()), this.mTrafficTag);
        return arrayOfByte;
      }
      catch (HttpHelper.HttpException localHttpException)
      {
        Log.e("Search.NetworkLoader", "Failed to load " + paramUri + ": " + localHttpException);
        return null;
      }
      catch (IOException localIOException)
      {
        Log.e("Search.NetworkLoader", "Failed to load " + paramUri + ": " + localIOException);
      }
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
 * Qualified Name:     com.google.android.search.core.util.NetworkBytesLoader
 * JD-Core Version:    0.7.0.1
 */