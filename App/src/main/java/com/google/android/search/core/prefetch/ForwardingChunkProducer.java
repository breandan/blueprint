package com.google.android.search.core.prefetch;

import com.google.android.search.core.util.ChunkProducer;
import com.google.android.search.core.util.ChunkProducer.ChunkConsumer;
import com.google.android.search.core.util.ChunkProducer.DataChunk;
import com.google.android.shared.util.ExecutorServiceAdapter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public class ForwardingChunkProducer
  extends ChunkProducer
{
  private static final ExecutorService UNUSABLE_EXECUTOR = new ExecutorServiceAdapter();
  private final Charset mCharset;
  private final AtomicInteger mDataChunkSerialNum = new AtomicInteger();
  
  public ForwardingChunkProducer(Charset paramCharset, int paramInt)
  {
    super(UNUSABLE_EXECUTOR, paramInt);
    this.mCharset = paramCharset;
  }
  
  public void close() {}
  
  public void markComplete()
  {
    setComplete();
  }
  
  public void offerChunk(String paramString)
    throws IOException
  {
    int i = this.mDataChunkSerialNum.getAndIncrement();
    consumerOnChunk(new ChunkProducer.DataChunk(paramString, this.mCharset, i));
  }
  
  public void reportError(Exception paramException)
  {
    if ((paramException instanceof IOException)) {}
    for (IOException localIOException = (IOException)paramException;; localIOException = new IOException(paramException))
    {
      setFailed(localIOException);
      return;
    }
  }
  
  protected void runBufferTask() {}
  
  public void start(@Nonnull ChunkProducer.ChunkConsumer paramChunkConsumer)
  {
    setConsumer(paramChunkConsumer);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.ForwardingChunkProducer
 * JD-Core Version:    0.7.0.1
 */