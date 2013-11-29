package com.google.android.sidekick.main;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.google.android.sidekick.main.inject.StaticMapCache;
import com.google.android.sidekick.shared.util.StaticMapKey;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class InProcessStaticMapLoader
  extends StaticMapLoader
{
  private final StaticMapCache mMapCache;
  
  public InProcessStaticMapLoader(Resources paramResources, Executor paramExecutor1, Executor paramExecutor2, StaticMapCache paramStaticMapCache)
  {
    super(paramResources, paramExecutor1, paramExecutor2);
    this.mMapCache = paramStaticMapCache;
  }
  
  protected Bitmap blockingLoadMapBitmap(@Nullable StaticMapKey paramStaticMapKey)
  {
    if (paramStaticMapKey == null) {
      return this.mMapCache.getSampleMap();
    }
    return this.mMapCache.get(paramStaticMapKey.getLocation(), paramStaticMapKey.getFrequentPlaceEntry(), paramStaticMapKey.isShowRoute());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.InProcessStaticMapLoader
 * JD-Core Version:    0.7.0.1
 */