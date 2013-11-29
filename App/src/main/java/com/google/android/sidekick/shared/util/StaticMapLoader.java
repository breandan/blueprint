package com.google.android.sidekick.shared.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public abstract class StaticMapLoader
{
  private static final String TAG = Tag.getTag(StaticMapLoader.class);
  private final Executor mLoaderThread;
  private final Object mLock = new Object();
  private boolean mPaused;
  private final Map<StaticMapKey, List<MapConsumer>> mPendingConsumers = Maps.newHashMap();
  private final Resources mResources;
  private final List<MapConsumer> mSampleMapConsumers = Lists.newArrayListWithCapacity(1);
  private final Executor mUiThread;
  
  public StaticMapLoader(Resources paramResources, Executor paramExecutor1, Executor paramExecutor2)
  {
    this.mResources = paramResources;
    this.mUiThread = paramExecutor1;
    this.mLoaderThread = paramExecutor2;
  }
  
  private void loadMapForKey(@Nullable StaticMapKey paramStaticMapKey, ImageView paramImageView)
  {
    localObject1 = this.mLock;
    if (paramStaticMapKey != null) {}
    for (;;)
    {
      try
      {
        Object localObject3 = (List)this.mPendingConsumers.get(paramStaticMapKey);
        if (localObject3 == null)
        {
          localObject3 = Lists.newArrayListWithCapacity(1);
          this.mPendingConsumers.put(paramStaticMapKey, localObject3);
        }
        ((List)localObject3).add(new MapConsumer(paramImageView));
        if (!this.mPaused) {
          new MapLoaderTask(this.mUiThread, this.mLoaderThread, paramStaticMapKey).execute(new Void[0]);
        }
        return;
      }
      finally {}
      this.mSampleMapConsumers.add(new MapConsumer(paramImageView));
    }
  }
  
  private void pruneConsumerList(List<MapConsumer> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      if (!((MapConsumer)localIterator.next()).isValid()) {
        localIterator.remove();
      }
    }
  }
  
  protected Drawable blockingLoadMap(@Nullable StaticMapKey paramStaticMapKey)
  {
    ExtraPreconditions.checkNotMainThread();
    Bitmap localBitmap = blockingLoadMapBitmap(paramStaticMapKey);
    if (localBitmap == null) {
      return null;
    }
    return new BitmapDrawable(this.mResources, localBitmap);
  }
  
  protected abstract Bitmap blockingLoadMapBitmap(@Nullable StaticMapKey paramStaticMapKey);
  
  public void loadMap(@Nullable Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean, ImageView paramImageView)
  {
    loadMapForKey(new StaticMapKey(paramLocation, paramFrequentPlaceEntry, paramBoolean), paramImageView);
  }
  
  public void loadSampleMap(ImageView paramImageView)
  {
    loadMapForKey(null, paramImageView);
  }
  
  void onMapLoaded(StaticMapKey paramStaticMapKey, Drawable paramDrawable)
  {
    Object localObject1 = this.mLock;
    if (paramStaticMapKey == null) {}
    for (;;)
    {
      try
      {
        List localList = this.mSampleMapConsumers;
        if (localList == null) {
          break label99;
        }
        Iterator localIterator = localList.iterator();
        if (!localIterator.hasNext()) {
          break;
        }
        ((MapConsumer)localIterator.next()).consume(paramDrawable);
        continue;
        localList = (List)this.mPendingConsumers.get(paramStaticMapKey);
      }
      finally {}
    }
    if (paramStaticMapKey == null) {
      this.mSampleMapConsumers.clear();
    }
    for (;;)
    {
      label99:
      return;
      this.mPendingConsumers.remove(paramStaticMapKey);
    }
  }
  
  public void pause()
  {
    this.mPaused = true;
  }
  
  public void resume()
  {
    this.mPaused = false;
    if (shouldRetryOnResume()) {
      retry();
    }
  }
  
  public void retry()
  {
    synchronized (this.mLock)
    {
      if (!this.mSampleMapConsumers.isEmpty()) {
        pruneConsumerList(this.mSampleMapConsumers);
      }
      if (!this.mPendingConsumers.isEmpty())
      {
        Iterator localIterator2 = this.mPendingConsumers.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          List localList = (List)((Map.Entry)localIterator2.next()).getValue();
          pruneConsumerList(localList);
          if (localList.isEmpty()) {
            localIterator2.remove();
          }
        }
      }
    }
    if (!this.mSampleMapConsumers.isEmpty()) {
      new MapLoaderTask(this.mUiThread, this.mLoaderThread, null).execute(new Void[0]);
    }
    Iterator localIterator1 = this.mPendingConsumers.keySet().iterator();
    while (localIterator1.hasNext())
    {
      StaticMapKey localStaticMapKey = (StaticMapKey)localIterator1.next();
      new MapLoaderTask(this.mUiThread, this.mLoaderThread, localStaticMapKey).execute(new Void[0]);
    }
  }
  
  protected boolean shouldRetryOnResume()
  {
    return true;
  }
  
  static class MapConsumer
    implements Consumer<Drawable>
  {
    private final WeakReference<ImageView> mImageViewRef;
    
    public MapConsumer(ImageView paramImageView)
    {
      this.mImageViewRef = new WeakReference(paramImageView);
    }
    
    public boolean consume(Drawable paramDrawable)
    {
      ImageView localImageView = (ImageView)this.mImageViewRef.get();
      if (localImageView != null) {
        localImageView.setImageDrawable(paramDrawable);
      }
      return true;
    }
    
    boolean isValid()
    {
      ImageView localImageView = (ImageView)this.mImageViewRef.get();
      if (localImageView == null) {}
      while (localImageView.getWindowToken() == null) {
        return false;
      }
      return true;
    }
  }
  
  class MapLoaderTask
    extends ExecutorAsyncTask<Void, Drawable>
  {
    private final StaticMapKey mKey;
    
    MapLoaderTask(Executor paramExecutor1, Executor paramExecutor2, @Nullable StaticMapKey paramStaticMapKey)
    {
      super(paramExecutor2);
      this.mKey = paramStaticMapKey;
    }
    
    protected Drawable doInBackground(Void... paramVarArgs)
    {
      return StaticMapLoader.this.blockingLoadMap(this.mKey);
    }
    
    protected void onPostExecute(Drawable paramDrawable)
    {
      StaticMapLoader.this.onMapLoaded(this.mKey, paramDrawable);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.StaticMapLoader
 * JD-Core Version:    0.7.0.1
 */