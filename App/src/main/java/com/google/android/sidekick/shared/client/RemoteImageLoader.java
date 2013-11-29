package com.google.android.sidekick.shared.client;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;

public class RemoteImageLoader
  implements UriLoader<Drawable>
{
  private static final String TAG = Tag.getTag(RemoteImageLoader.class);
  private final boolean mLoadFromCache;
  private final Executor mLoaderThread;
  private final NowRemoteClient mNowRemoteClient;
  private boolean mPaused = false;
  private final Object mPendingLock = new Object();
  private final Map<Uri, RemoteNowOrLater> mPendingMap = Maps.newHashMap();
  private final Resources mResources;
  private final Executor mUiThread;
  
  public RemoteImageLoader(Executor paramExecutor1, Executor paramExecutor2, Resources paramResources, NowRemoteClient paramNowRemoteClient, boolean paramBoolean)
  {
    this.mUiThread = paramExecutor1;
    this.mLoaderThread = paramExecutor2;
    this.mResources = paramResources;
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mLoadFromCache = paramBoolean;
  }
  
  private void loadImage(Uri paramUri)
  {
    if (this.mPaused) {
      return;
    }
    if (!this.mLoadFromCache) {
      this.mNowRemoteClient.prefetchImage(paramUri);
    }
    new ImageLoaderTask(this.mUiThread, this.mLoaderThread, paramUri).execute(new Void[0]);
  }
  
  private void onImageLoaded(Uri paramUri, Drawable paramDrawable)
  {
    synchronized (this.mPendingLock)
    {
      RemoteNowOrLater localRemoteNowOrLater = (RemoteNowOrLater)this.mPendingMap.get(paramUri);
      if (localRemoteNowOrLater != null)
      {
        localRemoteNowOrLater.notifyConsumers(paramDrawable);
        this.mPendingMap.remove(paramUri);
      }
      return;
    }
  }
  
  public void clearCache() {}
  
  public CancellableNowOrLater<? extends Drawable> load(Uri paramUri)
  {
    synchronized (this.mPendingLock)
    {
      RemoteNowOrLater localRemoteNowOrLater = (RemoteNowOrLater)this.mPendingMap.get(paramUri);
      if (localRemoteNowOrLater == null)
      {
        localRemoteNowOrLater = new RemoteNowOrLater();
        this.mPendingMap.put(paramUri, localRemoteNowOrLater);
        loadImage(paramUri);
      }
      return localRemoteNowOrLater;
    }
  }
  
  public void pause()
  {
    this.mPaused = true;
  }
  
  public void resume()
  {
    this.mPaused = false;
    synchronized (this.mPendingLock)
    {
      Iterator localIterator = this.mPendingMap.entrySet().iterator();
      while (localIterator.hasNext()) {
        if (((RemoteNowOrLater)((Map.Entry)localIterator.next()).getValue()).isCancelled()) {
          localIterator.remove();
        }
      }
    }
    if (this.mNowRemoteClient.isConnected()) {
      retry();
    }
  }
  
  public void retry()
  {
    synchronized (this.mPendingLock)
    {
      Iterator localIterator = this.mPendingMap.keySet().iterator();
      if (localIterator.hasNext()) {
        loadImage((Uri)localIterator.next());
      }
    }
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return true;
  }
  
  class ImageLoaderTask
    extends ExecutorAsyncTask<Void, Drawable>
  {
    private final Uri mUri;
    
    ImageLoaderTask(Executor paramExecutor1, Executor paramExecutor2, Uri paramUri)
    {
      super(paramExecutor2);
      this.mUri = paramUri;
    }
    
    protected Drawable doInBackground(Void... paramVarArgs)
    {
      Bitmap localBitmap = RemoteImageLoader.this.mNowRemoteClient.blockingGetImage(this.mUri, RemoteImageLoader.this.mLoadFromCache);
      if (localBitmap == null) {
        return null;
      }
      return new BitmapDrawable(RemoteImageLoader.this.mResources, localBitmap);
    }
    
    protected void onPostExecute(Drawable paramDrawable)
    {
      RemoteImageLoader.this.onImageLoaded(this.mUri, paramDrawable);
    }
  }
  
  class RemoteNowOrLater
    implements CancellableNowOrLater<Drawable>
  {
    private List<Consumer<? super Drawable>> mConsumers = Lists.newArrayListWithCapacity(1);
    private final Object mLock = new Object();
    
    RemoteNowOrLater() {}
    
    public void cancelGetLater(Consumer<? super Drawable> paramConsumer)
    {
      synchronized (this.mLock)
      {
        this.mConsumers.remove(paramConsumer);
        return;
      }
    }
    
    public void getLater(Consumer<? super Drawable> paramConsumer)
    {
      synchronized (this.mLock)
      {
        this.mConsumers.add(paramConsumer);
        return;
      }
    }
    
    public Drawable getNow()
    {
      throw new UnsupportedOperationException("remote bitmaps are never available now");
    }
    
    public boolean haveNow()
    {
      return false;
    }
    
    public boolean isCancelled()
    {
      synchronized (this.mLock)
      {
        boolean bool = this.mConsumers.isEmpty();
        return bool;
      }
    }
    
    public void notifyConsumers(Drawable paramDrawable)
    {
      synchronized (this.mLock)
      {
        Iterator localIterator = this.mConsumers.iterator();
        if (localIterator.hasNext()) {
          ((Consumer)localIterator.next()).consume(paramDrawable);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.RemoteImageLoader
 * JD-Core Version:    0.7.0.1
 */