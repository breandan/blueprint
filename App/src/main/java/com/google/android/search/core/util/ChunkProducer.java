package com.google.android.search.core.util;

import com.google.android.shared.util.Consumer;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public abstract class ChunkProducer
  implements Closeable
{
  private volatile Future<?> mBufferTask;
  private ChunkConsumer mConsumer;
  @Nonnull
  private final ExecutorService mExecutor;
  protected final int mMaxResponseBytes;
  private final AtomicInteger mState = new AtomicInteger(1);
  
  protected ChunkProducer(@Nonnull ExecutorService paramExecutorService, int paramInt)
  {
    this.mExecutor = ((ExecutorService)Preconditions.checkNotNull(paramExecutorService));
    this.mMaxResponseBytes = paramInt;
  }
  
  protected void cancelAndInterruptBufferTask()
  {
    setFailed(new IOException("Stream cancelled"));
    Future localFuture = this.mBufferTask;
    if (localFuture != null) {
      localFuture.cancel(true);
    }
  }
  
  protected void consumerOnChunk(DataChunk paramDataChunk)
    throws IOException
  {
    int i = this.mState.get();
    if (i != 1) {
      throw new IOException("Cannot process chunk because state is " + i);
    }
    this.mConsumer.onChunk(paramDataChunk);
  }
  
  protected Consumer<DataChunk> getChunkConsumerProxy()
  {
    new Consumer()
    {
      public boolean consume(ChunkProducer.DataChunk paramAnonymousDataChunk)
      {
        try
        {
          ChunkProducer.this.consumerOnChunk(paramAnonymousDataChunk);
          return true;
        }
        catch (IOException localIOException)
        {
          for (;;)
          {
            ChunkProducer.this.setFailed(localIOException);
          }
        }
      }
    };
  }
  
  protected abstract void runBufferTask();
  
  protected final boolean setComplete()
  {
    if (this.mState.compareAndSet(1, 2))
    {
      this.mConsumer.onComplete();
      return true;
    }
    return false;
  }
  
  protected void setConsumer(@Nonnull ChunkConsumer paramChunkConsumer)
  {
    if (this.mConsumer == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mConsumer = ((ChunkConsumer)Preconditions.checkNotNull(paramChunkConsumer));
      return;
    }
  }
  
  protected final boolean setFailed(Exception paramException)
  {
    if (this.mState.compareAndSet(1, 3))
    {
      this.mConsumer.onFailed(paramException);
      return true;
    }
    return false;
  }
  
  public void start(@Nonnull ChunkConsumer paramChunkConsumer)
  {
    setConsumer(paramChunkConsumer);
    this.mBufferTask = this.mExecutor.submit(new Runnable()
    {
      public void run()
      {
        ChunkProducer.this.runBufferTask();
      }
    });
  }
  
  public static abstract class Chunk
  {
    public int getDataLength()
    {
      return 0;
    }
    
    abstract String toShortString();
  }
  
  public static abstract interface ChunkConsumer
  {
    public abstract void onChunk(ChunkProducer.DataChunk paramDataChunk);
    
    public abstract void onComplete();
    
    public abstract void onFailed(Exception paramException);
  }
  
  public static class DataChunk
    extends ChunkProducer.Chunk
  {
    @Nonnull
    private final byte[] mData;
    private final int mLength;
    private final int mSerialNum;
    
    public DataChunk()
    {
      this(new byte[0], -1, 0);
    }
    
    public DataChunk(@Nonnull String paramString, @Nonnull Charset paramCharset, int paramInt)
    {
      this(paramString.getBytes((Charset)Preconditions.checkNotNull(paramCharset)), -1, paramInt);
    }
    
    public DataChunk(@Nonnull byte[] paramArrayOfByte, int paramInt)
    {
      this(paramArrayOfByte, -1, paramInt);
    }
    
    public DataChunk(@Nonnull byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      boolean bool2;
      boolean bool3;
      if (paramInt1 >= -1)
      {
        bool2 = bool1;
        Preconditions.checkArgument(bool2);
        if (paramInt1 > paramArrayOfByte.length) {
          break label73;
        }
        bool3 = bool1;
        label31:
        Preconditions.checkArgument(bool3);
        if (paramInt2 < 0) {
          break label79;
        }
      }
      for (;;)
      {
        Preconditions.checkArgument(bool1);
        this.mData = ((byte[])Preconditions.checkNotNull(paramArrayOfByte));
        this.mLength = paramInt1;
        this.mSerialNum = paramInt2;
        return;
        bool2 = false;
        break;
        label73:
        bool3 = false;
        break label31;
        label79:
        bool1 = false;
      }
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof DataChunk)) {
        return Arrays.equals(this.mData, ((DataChunk)paramObject).mData);
      }
      return false;
    }
    
    public int getDataLength()
    {
      return this.mData.length;
    }
    
    @Nonnull
    public InputStream getInputStream()
    {
      if (this.mLength == -1) {
        return new ByteArrayInputStream(this.mData);
      }
      return new ByteArrayInputStream(this.mData, 0, this.mLength);
    }
    
    String toShortString()
    {
      return "D";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.ChunkProducer
 * JD-Core Version:    0.7.0.1
 */