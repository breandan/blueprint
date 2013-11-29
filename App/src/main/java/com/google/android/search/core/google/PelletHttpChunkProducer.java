package com.google.android.search.core.google;

import com.google.android.search.core.prefetch.HttpChunkProducer;
import com.google.android.search.core.util.InputStreamChunkProducer.SizeExceededException;
import com.google.android.search.core.util.InputStreamChunkProducer.ZeroSizeException;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;

public class PelletHttpChunkProducer
  extends HttpChunkProducer
{
  private final PelletChunkHelper mHelper;
  
  public PelletHttpChunkProducer(@Nonnull HttpURLConnection paramHttpURLConnection, @Nonnull ExecutorService paramExecutorService, int paramInt, @Nonnull String paramString, @Nonnull InputSupplier<InputStream> paramInputSupplier, @Nonnull PelletDemultiplexer.ExtrasConsumer paramExtrasConsumer)
  {
    super(paramHttpURLConnection, paramExecutorService, paramInt, paramInputSupplier);
    this.mHelper = new PelletChunkHelper(this, paramInt, paramExtrasConsumer, paramHttpURLConnection.getURL().toString(), paramString);
  }
  
  protected void bufferAllData(InputStream paramInputStream)
    throws IOException, InterruptedException, InputStreamChunkProducer.SizeExceededException, InputStreamChunkProducer.ZeroSizeException
  {
    this.mHelper.bufferAllData(paramInputStream, getChunkConsumerProxy());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PelletHttpChunkProducer
 * JD-Core Version:    0.7.0.1
 */