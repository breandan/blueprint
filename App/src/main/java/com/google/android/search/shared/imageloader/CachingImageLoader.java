package com.google.android.search.shared.imageloader;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.util.LruCache;
import com.google.android.shared.util.CachedLater;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Now;
import com.google.android.shared.util.NowOrLater;
import com.google.android.shared.util.NowOrLaterWrapper;
import com.google.android.shared.util.UriLoader;
import com.google.android.shared.util.Util;

public class CachingImageLoader
  implements UriLoader<Drawable>
{
  private final IconCache mIconCache;
  private final UriLoader<Drawable> mWrapped;
  
  public CachingImageLoader(UriLoader<Drawable> paramUriLoader)
  {
    this.mWrapped = paramUriLoader;
    this.mIconCache = new IconCache(50);
  }
  
  public void clearCache()
  {
    this.mIconCache.evictAll();
  }
  
  public CancellableNowOrLater<Drawable> load(Uri paramUri)
  {
    if ((Util.isEmpty(paramUri)) || ("0".equals(paramUri.toString()))) {
      return Now.returnNull();
    }
    return ((Entry)this.mIconCache.get(paramUri)).getDrawable();
  }
  
  public boolean supportsUri(Uri paramUri)
  {
    return this.mWrapped.supportsUri(paramUri);
  }
  
  private class Entry
    extends CachedLater<Drawable.ConstantState>
    implements Consumer<Drawable>
  {
    private final CancellableNowOrLater<Drawable> mDrawable;
    private final Uri mDrawableId;
    
    public Entry(Uri paramUri)
    {
      this.mDrawableId = paramUri;
      this.mDrawable = new NowOrLaterWrapper(this)
      {
        public Drawable get(Drawable.ConstantState paramAnonymousConstantState)
        {
          if (paramAnonymousConstantState == null) {
            return null;
          }
          return paramAnonymousConstantState.newDrawable();
        }
      };
    }
    
    public boolean consume(Drawable paramDrawable)
    {
      if (paramDrawable != null) {
        store(paramDrawable.getConstantState());
      }
      for (;;)
      {
        return true;
        storeNothing();
      }
    }
    
    protected void create()
    {
      CachingImageLoader.this.mWrapped.load(this.mDrawableId).getLater(this);
    }
    
    public CancellableNowOrLater<Drawable> getDrawable()
    {
      prefetch();
      return this.mDrawable;
    }
  }
  
  private class IconCache
    extends LruCache<Uri, CachingImageLoader.Entry>
  {
    public IconCache(int paramInt)
    {
      super();
    }
    
    protected CachingImageLoader.Entry create(Uri paramUri)
    {
      return new CachingImageLoader.Entry(CachingImageLoader.this, paramUri);
    }
    
    protected void entryRemoved(boolean paramBoolean, Uri paramUri, CachingImageLoader.Entry paramEntry1, CachingImageLoader.Entry paramEntry2) {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.imageloader.CachingImageLoader
 * JD-Core Version:    0.7.0.1
 */