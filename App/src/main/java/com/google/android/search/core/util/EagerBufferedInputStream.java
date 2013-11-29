package com.google.android.search.core.util;

import android.util.Log;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EagerBufferedInputStream
  extends InputStream
  implements ChunkProducer.ChunkConsumer
{
  private final AtomicInteger mInSerialNum = new AtomicInteger();
  @Nonnull
  private final BufferTaskListener mListener;
  private final LinkedBlockingDeque<ChunkProducer.Chunk> mNewChunks = new LinkedBlockingDeque();
  private final AtomicInteger mOutSerialNum = new AtomicInteger();
  private int mReadBytes = 0;
  private final Deque<ChunkProducer.Chunk> mReadChunks = new LinkedList();
  private InputStream mReadStream = null;
  private final AtomicInteger mState = new AtomicInteger(0);
  private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();
  private int mUnreadBytes = 0;
  private final LinkedList<ChunkProducer.Chunk> mUnreadChunks = new LinkedList();
  
  private EagerBufferedInputStream(BufferTaskListener paramBufferTaskListener)
  {
    this.mListener = ((BufferTaskListener)Preconditions.checkNotNull(paramBufferTaskListener));
  }
  
  private ChunkProducer.Chunk blockUntilNewChunkOrStateChange()
    throws InterruptedException
  {
    return (ChunkProducer.Chunk)this.mNewChunks.takeFirst();
  }
  
  private void clearNow()
  {
    this.mUnreadChunks.clear();
    this.mUnreadChunks.add(SentinelChunk.SINGLETON);
    this.mReadChunks.clear();
    this.mUnreadBytes = 0;
    this.mReadBytes = 0;
    this.mReadStream = null;
  }
  
  private void endOfStream() {}
  
  private void maybeCheckConsecutiveSerialNum(ChunkProducer.Chunk paramChunk, AtomicInteger paramAtomicInteger) {}
  
  public static EagerBufferedInputStream newStream(ChunkProducer paramChunkProducer, BufferTaskListener paramBufferTaskListener)
  {
    Preconditions.checkNotNull(paramChunkProducer);
    EagerBufferedInputStream localEagerBufferedInputStream = new EagerBufferedInputStream(paramBufferTaskListener);
    paramChunkProducer.start(localEagerBufferedInputStream);
    return localEagerBufferedInputStream;
  }
  
  private InputStream nextReadStream()
    throws IOException
  {
    for (;;)
    {
      ChunkProducer.Chunk localChunk;
      try
      {
        updateQueues(null);
        if (this.mUnreadChunks.isEmpty()) {
          updateQueues(blockUntilNewChunkOrStateChange());
        }
        localChunk = (ChunkProducer.Chunk)this.mUnreadChunks.poll();
        maybeCheckConsecutiveSerialNum(localChunk, this.mOutSerialNum);
        if ((localChunk instanceof SentinelChunk))
        {
          this.mUnreadChunks.addFirst(localChunk);
          return null;
        }
        if ((localChunk instanceof ExceptionChunk))
        {
          clearNow();
          throw ((ExceptionChunk)localChunk).getException();
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        Thread.currentThread().interrupt();
        return null;
      }
      if ((localChunk instanceof ChunkProducer.DataChunk))
      {
        this.mReadChunks.addLast(localChunk);
        this.mReadBytes += localChunk.getDataLength();
        this.mUnreadBytes -= localChunk.getDataLength();
        return ((ChunkProducer.DataChunk)localChunk).getInputStream();
      }
      if (!(localChunk instanceof SignalChunk)) {
        Log.e("Search.EagerBufferedInputStream", "Unknown chunk in stream.");
      }
    }
  }
  
  private static String queueToString(Queue<ChunkProducer.Chunk> paramQueue)
  {
    StringBuilder localStringBuilder = new StringBuilder("" + paramQueue.size() + " [");
    Iterator localIterator = paramQueue.iterator();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((ChunkProducer.Chunk)localIterator.next()).toShortString());
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  private int readOrSkip(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.mThreadCheck.check();
    updateQueues(null);
    if (paramInt2 <= 0) {
      return 0;
    }
    int i = 0;
    for (;;)
    {
      if (i < paramInt2)
      {
        if (this.mReadStream == null)
        {
          this.mReadStream = nextReadStream();
          if (this.mReadStream != null) {}
        }
      }
      else
      {
        if (i != 0) {
          break label128;
        }
        endOfStream();
        if (paramArrayOfByte == null) {
          break;
        }
        return -1;
      }
      if (paramArrayOfByte == null) {}
      for (int j = (int)this.mReadStream.skip(paramInt2 - i);; j = this.mReadStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i))
      {
        if (j > 0) {
          break label118;
        }
        this.mReadStream = null;
        break;
      }
      label118:
      i += j;
    }
    label128:
    return i;
  }
  
  private void removeSignalsAndSentinelsFromUnreadChunks()
  {
    Iterator localIterator = this.mUnreadChunks.iterator();
    while (localIterator.hasNext())
    {
      ChunkProducer.Chunk localChunk = (ChunkProducer.Chunk)localIterator.next();
      if (((localChunk instanceof SentinelChunk)) || ((localChunk instanceof SignalChunk))) {
        localIterator.remove();
      }
    }
  }
  
  private void setState(int paramInt)
  {
    this.mThreadCheck.reset();
    this.mState.set(paramInt);
    this.mNewChunks.add(SignalChunk.SINGLETON);
  }
  
  private static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 0: 
      return "normal";
    case 1: 
      return "reset";
    }
    return "closed";
  }
  
  private void updateQueues(@Nullable ChunkProducer.Chunk paramChunk)
  {
    int i = 0;
    ChunkProducer.Chunk localChunk;
    if (paramChunk != null) {
      localChunk = paramChunk;
    }
    while (localChunk != null)
    {
      this.mUnreadBytes += localChunk.getDataLength();
      this.mUnreadChunks.addLast(localChunk);
      if ((localChunk instanceof SentinelChunk)) {
        i = 1;
      }
      localChunk = (ChunkProducer.Chunk)this.mNewChunks.poll();
      continue;
      localChunk = (ChunkProducer.Chunk)this.mNewChunks.poll();
      i = 0;
    }
    if (i != 0) {
      this.mNewChunks.addFirst(SentinelChunk.SINGLETON);
    }
    switch (this.mState.get())
    {
    default: 
      throw new AssertionError("Unknown state");
    case 1: 
      removeSignalsAndSentinelsFromUnreadChunks();
      this.mState.compareAndSet(1, 0);
      if (!this.mReadChunks.isEmpty())
      {
        this.mUnreadChunks.addAll(0, this.mReadChunks);
        this.mReadChunks.clear();
        this.mUnreadBytes += this.mReadBytes;
        this.mReadBytes = 0;
      }
      this.mReadStream = null;
      this.mOutSerialNum.set(0);
    case 0: 
      return;
    }
    removeSignalsAndSentinelsFromUnreadChunks();
    this.mReadChunks.addAll(this.mUnreadChunks);
    this.mUnreadChunks.clear();
    this.mReadBytes += this.mUnreadBytes;
    this.mUnreadBytes = 0;
    this.mReadStream = null;
    this.mUnreadChunks.addLast(SentinelChunk.SINGLETON);
  }
  
  public int available()
  {
    this.mThreadCheck.check();
    return this.mUnreadBytes;
  }
  
  public void close()
  {
    setState(2);
  }
  
  public final void mark(int paramInt)
  {
    this.mThreadCheck.check();
    throw new UnsupportedOperationException("mark() not supported by EagerBufferedInputStream");
  }
  
  public final boolean markSupported()
  {
    this.mThreadCheck.check();
    return false;
  }
  
  public void onChunk(ChunkProducer.DataChunk paramDataChunk)
  {
    maybeCheckConsecutiveSerialNum(paramDataChunk, this.mInSerialNum);
    this.mNewChunks.add(Preconditions.checkNotNull(paramDataChunk));
  }
  
  public void onComplete()
  {
    this.mNewChunks.add(SentinelChunk.SINGLETON);
    this.mListener.onComplete();
  }
  
  public void onFailed(Exception paramException)
  {
    if ((paramException instanceof IOException)) {}
    for (IOException localIOException = (IOException)paramException;; localIOException = new IOException(paramException))
    {
      this.mNewChunks.add(new ExceptionChunk(localIOException));
      this.mNewChunks.add(SentinelChunk.SINGLETON);
      this.mListener.onFailed();
      return;
    }
  }
  
  public final int read()
    throws IOException
  {
    this.mThreadCheck.check();
    updateQueues(null);
    for (;;)
    {
      int i;
      if (this.mReadStream == null)
      {
        this.mReadStream = nextReadStream();
        if (this.mReadStream == null)
        {
          endOfStream();
          i = -1;
        }
      }
      do
      {
        return i;
        i = this.mReadStream.read();
      } while (i >= 0);
      this.mReadStream = null;
    }
  }
  
  public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.mThreadCheck.check();
    if (paramArrayOfByte == null) {
      throw new NullPointerException("Read into null buffer");
    }
    return readOrSkip(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void reset()
  {
    setState(1);
  }
  
  public final long skip(long paramLong)
    throws IOException
  {
    this.mThreadCheck.check();
    if (paramLong <= 0L) {
      return 0L;
    }
    int i = (int)paramLong;
    if (i < 0) {
      throw new IllegalArgumentException("byteCount too large: int overflow");
    }
    return readOrSkip(null, 0, i);
  }
  
  public String toString()
  {
    boolean bool = this.mUnreadChunks.peekFirst() instanceof SentinelChunk;
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("EagerBufferedInputStream{");
    StringBuilder localStringBuilder2 = new StringBuilder().append("source ");
    if (bool) {}
    for (String str = "complete";; str = "incomplete")
    {
      localStringBuilder1.append(str + ", ");
      localStringBuilder1.append("state: " + stateToString(this.mState.get()) + ", ");
      localStringBuilder1.append("new chunks " + queueToString(this.mNewChunks) + ", ");
      localStringBuilder1.append("unread chunks " + queueToString(this.mUnreadChunks) + ", ");
      localStringBuilder1.append("read chunks " + queueToString(this.mReadChunks) + ", ");
      localStringBuilder1.append(this.mUnreadBytes + " unread bytes, ");
      localStringBuilder1.append(this.mReadBytes + " read bytes");
      localStringBuilder1.append("}");
      return localStringBuilder1.toString();
    }
  }
  
  public static abstract interface BufferTaskListener
  {
    public abstract void onComplete();
    
    public abstract void onFailed();
  }
  
  static final class ExceptionChunk
    extends ChunkProducer.Chunk
  {
    @Nonnull
    private final IOException mException;
    
    public ExceptionChunk(@Nonnull IOException paramIOException)
    {
      Preconditions.checkNotNull(paramIOException);
      this.mException = paramIOException;
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof ExceptionChunk)) {
        return ((ExceptionChunk)paramObject).mException.equals(this.mException);
      }
      return false;
    }
    
    @Nonnull
    IOException getException()
    {
      return this.mException;
    }
    
    String toShortString()
    {
      return "E";
    }
  }
  
  static final class SentinelChunk
    extends ChunkProducer.Chunk
  {
    public static final SentinelChunk SINGLETON = new SentinelChunk();
    
    public boolean equals(Object paramObject)
    {
      return paramObject instanceof SentinelChunk;
    }
    
    String toShortString()
    {
      return "X";
    }
  }
  
  static final class SignalChunk
    extends ChunkProducer.Chunk
  {
    public static final SignalChunk SINGLETON = new SignalChunk();
    
    public boolean equals(Object paramObject)
    {
      return paramObject instanceof SignalChunk;
    }
    
    String toShortString()
    {
      return "-";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.EagerBufferedInputStream
 * JD-Core Version:    0.7.0.1
 */