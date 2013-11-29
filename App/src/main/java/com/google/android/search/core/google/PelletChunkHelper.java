package com.google.android.search.core.google;

import com.google.android.search.core.util.ChunkProducer.DataChunk;
import com.google.android.search.core.util.InputStreamChunkProducer;
import com.google.android.search.core.util.InputStreamChunkProducer.SizeExceededException;
import com.google.android.search.core.util.InputStreamChunkProducer.ZeroSizeException;
import com.google.android.shared.util.Consumer;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;

class PelletChunkHelper
{
  @Nonnull
  private final String mBaseUri;
  @Nonnull
  private final PelletDemultiplexer.ExtrasConsumer mExtrasConsumer;
  private final int mMaxResponseBytes;
  @Nonnull
  private final InputStreamChunkProducer mParent;
  @Nonnull
  private final String mSuggestionPelletPath;
  
  PelletChunkHelper(@Nonnull InputStreamChunkProducer paramInputStreamChunkProducer, int paramInt, @Nonnull PelletDemultiplexer.ExtrasConsumer paramExtrasConsumer, @Nonnull String paramString1, @Nonnull String paramString2)
  {
    this.mParent = ((InputStreamChunkProducer)Preconditions.checkNotNull(paramInputStreamChunkProducer));
    this.mMaxResponseBytes = paramInt;
    this.mExtrasConsumer = ((PelletDemultiplexer.ExtrasConsumer)Preconditions.checkNotNull(paramExtrasConsumer));
    this.mBaseUri = ((String)Preconditions.checkNotNull(paramString1));
    this.mSuggestionPelletPath = paramString2;
  }
  
  void bufferAllData(InputStream paramInputStream, Consumer<ChunkProducer.DataChunk> paramConsumer)
    throws IOException, InterruptedException, InputStreamChunkProducer.SizeExceededException, InputStreamChunkProducer.ZeroSizeException
  {
    int i = 0;
    PelletParser localPelletParser = new PelletParser(paramInputStream);
    PelletDemultiplexer localPelletDemultiplexer = new PelletDemultiplexer(paramConsumer, this.mExtrasConsumer, this.mBaseUri, this.mSuggestionPelletPath);
    label90:
    label96:
    do
    {
      PelletParser.Pellet localPellet;
      try
      {
        this.mParent.throwIOExceptionIfStopped(null);
        localPelletDemultiplexer.onEndOfResponse();
      }
      finally
      {
        try
        {
          localPellet = localPelletParser.read();
          if (localPellet != null) {
            break label96;
          }
          if (i != 0) {
            break label90;
          }
          throw new InputStreamChunkProducer.ZeroSizeException();
        }
        catch (IllegalStateException localIllegalStateException)
        {
          this.mParent.throwIOExceptionIfStopped(localIllegalStateException);
          throw localIllegalStateException;
        }
        localObject = finally;
        localPelletDemultiplexer.onEndOfResponse();
      }
      return;
      i += localPellet.mData.length;
      localPelletDemultiplexer.consume(localPellet);
      if (i > this.mMaxResponseBytes) {
        throw new InputStreamChunkProducer.SizeExceededException();
      }
    } while (!Thread.currentThread().isInterrupted());
    throw new InterruptedException();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PelletChunkHelper
 * JD-Core Version:    0.7.0.1
 */