package com.google.android.search.core.google;

import com.google.android.search.core.util.InputStreamChunkProducer;
import com.google.android.search.core.util.InputStreamChunkProducer.SizeExceededException;
import com.google.android.search.core.util.InputStreamChunkProducer.ZeroSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;

public class PelletChunkProducer
  extends InputStreamChunkProducer
{
  private final PelletChunkHelper mHelper;
  
  public PelletChunkProducer(@Nonnull InputStream paramInputStream, @Nonnull ExecutorService paramExecutorService, int paramInt, @Nonnull PelletDemultiplexer.ExtrasConsumer paramExtrasConsumer, @Nonnull String paramString1, @Nonnull String paramString2)
  {
    super(paramInputStream, paramExecutorService, paramInt);
    this.mHelper = new PelletChunkHelper(this, paramInt, paramExtrasConsumer, paramString1, paramString2);
  }
  
  protected void bufferAllData(InputStream paramInputStream)
    throws IOException, InterruptedException, InputStreamChunkProducer.SizeExceededException, InputStreamChunkProducer.ZeroSizeException
  {
    this.mHelper.bufferAllData(paramInputStream, getChunkConsumerProxy());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PelletChunkProducer
 * JD-Core Version:    0.7.0.1
 */