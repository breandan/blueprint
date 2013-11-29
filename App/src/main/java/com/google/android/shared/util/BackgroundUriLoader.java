package com.google.android.shared.util;

import android.net.Uri;
import java.util.concurrent.Executor;

public class BackgroundUriLoader<A>
  implements UriLoader<A>
{
  private final SynchronousLoader<A> mDelegate;
  private final Executor mExecutor;
  
  public BackgroundUriLoader(Executor paramExecutor, SynchronousLoader<A> paramSynchronousLoader)
  {
    this.mExecutor = paramExecutor;
    this.mDelegate = paramSynchronousLoader;
  }
  
  public void clearCache()
  {
    this.mDelegate.clearCache();
  }
  
  public CancellableNowOrLater<? extends A> load(final Uri paramUri)
  {
    final CachedConsumer localCachedConsumer = new CachedConsumer();
    this.mExecutor.execute(new Runnable()
    {
      public void run()
      {
        localCachedConsumer.consume(BackgroundUriLoader.this.mDelegate.loadNow(paramUri));
      }
    });
    return localCachedConsumer;
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return this.mDelegate.supportsUri(paramUri);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.BackgroundUriLoader
 * JD-Core Version:    0.7.0.1
 */