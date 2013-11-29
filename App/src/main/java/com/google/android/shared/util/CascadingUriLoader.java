package com.google.android.shared.util;

import android.net.Uri;
import android.util.Log;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;

public class CascadingUriLoader<T>
  implements UriLoader<T>
{
  private final ImmutableList<UriLoader<T>> mLoaders;
  
  public CascadingUriLoader(ImmutableList<UriLoader<T>> paramImmutableList)
  {
    this.mLoaders = paramImmutableList;
  }
  
  public static <T> CascadingUriLoader<T> create(ImmutableList<UriLoader<T>> paramImmutableList)
  {
    return new CascadingUriLoader(paramImmutableList);
  }
  
  public void clearCache()
  {
    Iterator localIterator = this.mLoaders.iterator();
    while (localIterator.hasNext()) {
      ((UriLoader)localIterator.next()).clearCache();
    }
  }
  
  public CancellableNowOrLater<? extends T> load(Uri paramUri)
  {
    if (Util.isEmpty(paramUri)) {
      return Now.returnNull();
    }
    Iterator localIterator = this.mLoaders.iterator();
    while (localIterator.hasNext())
    {
      UriLoader localUriLoader = (UriLoader)localIterator.next();
      if (localUriLoader.supportsUri(paramUri)) {
        return localUriLoader.load(paramUri);
      }
    }
    Log.e("ImageLoader", "No loader can load " + paramUri);
    return Now.returnNull();
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.CascadingUriLoader
 * JD-Core Version:    0.7.0.1
 */