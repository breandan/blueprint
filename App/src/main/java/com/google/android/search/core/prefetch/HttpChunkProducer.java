package com.google.android.search.core.prefetch;

import android.net.TrafficStats;
import com.google.android.search.core.util.InputStreamChunkProducer;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HttpChunkProducer
  extends InputStreamChunkProducer
{
  @Nonnull
  private final HttpURLConnection mConnection;
  
  public HttpChunkProducer(@Nonnull HttpURLConnection paramHttpURLConnection, @Nonnull ExecutorService paramExecutorService, int paramInt, @Nonnull InputSupplier<InputStream> paramInputSupplier)
  {
    super(paramInputSupplier, paramExecutorService, paramInt);
    this.mConnection = ((HttpURLConnection)Preconditions.checkNotNull(paramHttpURLConnection));
  }
  
  protected void closeSource(@Nullable InputStream paramInputStream, boolean paramBoolean)
  {
    if (paramInputStream != null) {
      Closeables.closeQuietly(paramInputStream);
    }
    TrafficStats.clearThreadStatsTag();
    this.mConnection.disconnect();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.HttpChunkProducer
 * JD-Core Version:    0.7.0.1
 */