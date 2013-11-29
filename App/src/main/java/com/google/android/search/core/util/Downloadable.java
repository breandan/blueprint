package com.google.android.search.core.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.UriLoader;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public abstract class Downloadable
{
  private final Executor mBgExecutor;
  private final UriLoader<byte[]> mLoader;
  private final String mTag;
  
  public Downloadable(String paramString, UriLoader<byte[]> paramUriLoader, Executor paramExecutor)
  {
    this.mTag = paramString;
    this.mLoader = paramUriLoader;
    this.mBgExecutor = paramExecutor;
  }
  
  private boolean isCacheUpToDate()
  {
    return TextUtils.equals(getLatestUrl(), getCachedDataUrl());
  }
  
  protected abstract byte[] getCachedData();
  
  protected abstract String getCachedDataUrl();
  
  protected abstract byte[] getExternalData();
  
  protected abstract String getLatestUrl();
  
  public void initializeFromCached()
  {
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          if (Downloadable.this.useCache()) {}
          byte[] arrayOfByte;
          for (Object localObject = Downloadable.this.getCachedData();; localObject = arrayOfByte)
          {
            Downloadable.this.onDataLoaded((byte[])localObject);
            return;
            arrayOfByte = Downloadable.this.getExternalData();
          }
          return;
        }
        catch (Exception localException)
        {
          Downloadable.this.onLoadException(null, localException);
        }
      }
    });
  }
  
  public void maybeUpdateCache()
  {
    if (!isCacheUpToDate())
    {
      if (useCache())
      {
        final String str = getLatestUrl();
        this.mBgExecutor.execute(new Runnable()
        {
          public void run()
          {
            try
            {
              boolean bool = TextUtils.isEmpty(str);
              byte[] arrayOfByte = null;
              if (!bool) {
                arrayOfByte = (byte[])Downloadable.this.mLoader.load(Uri.parse(str)).getNow();
              }
              if ((TextUtils.isEmpty(str)) || ((arrayOfByte != null) && (arrayOfByte.length > 0)))
              {
                Downloadable.this.onDataLoaded(arrayOfByte);
                Downloadable.this.saveCached(arrayOfByte, str);
              }
              return;
            }
            catch (IllegalStateException localIllegalStateException)
            {
              Downloadable.this.onDownloadException(str, localIllegalStateException);
              return;
            }
            catch (Exception localException)
            {
              Downloadable.this.onLoadException(str, localException);
            }
          }
        });
      }
    }
    else {
      return;
    }
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        Downloadable.this.saveCached(null, null);
      }
    });
  }
  
  protected abstract void onDataLoaded(@Nullable byte[] paramArrayOfByte)
    throws Exception;
  
  protected void onDownloadException(String paramString, IllegalStateException paramIllegalStateException)
  {
    Log.w(this.mTag, "Invalid data at URL: " + paramString, paramIllegalStateException);
  }
  
  protected void onLoadException(@Nullable String paramString, Exception paramException)
  {
    if (paramString != null)
    {
      Log.w(this.mTag, "Unable to parse data from " + paramString + ":", paramException);
      return;
    }
    Log.w(this.mTag, "Unable to parse data from local storage:", paramException);
  }
  
  protected abstract void saveCached(@Nullable byte[] paramArrayOfByte, @Nullable String paramString);
  
  protected abstract boolean useCache();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.Downloadable
 * JD-Core Version:    0.7.0.1
 */