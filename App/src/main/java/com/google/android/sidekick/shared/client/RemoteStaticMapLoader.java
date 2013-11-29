package com.google.android.sidekick.shared.client;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.google.android.sidekick.shared.util.StaticMapKey;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class RemoteStaticMapLoader
  extends StaticMapLoader
{
  private final NowRemoteClient mRemoteClient;
  
  public RemoteStaticMapLoader(Resources paramResources, Executor paramExecutor1, Executor paramExecutor2, NowRemoteClient paramNowRemoteClient)
  {
    super(paramResources, paramExecutor1, paramExecutor2);
    this.mRemoteClient = paramNowRemoteClient;
  }
  
  protected Bitmap blockingLoadMapBitmap(@Nullable StaticMapKey paramStaticMapKey)
  {
    if (paramStaticMapKey == null) {
      return this.mRemoteClient.getSampleMap();
    }
    return this.mRemoteClient.getStaticMap(paramStaticMapKey.getLocation(), paramStaticMapKey.getFrequentPlaceEntry(), paramStaticMapKey.isShowRoute());
  }
  
  protected boolean shouldRetryOnResume()
  {
    return this.mRemoteClient.isConnected();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.RemoteStaticMapLoader
 * JD-Core Version:    0.7.0.1
 */