package com.google.android.speech.audio;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class AudioEncoderInputStream
  extends InputStream
{
  private final int mChannels;
  private MediaCodec mCodec;
  private ByteBuffer[] mCodecInputBuffers;
  private ByteBuffer[] mCodecOutputBuffers;
  private final int mCodecType;
  private int mCurrentOutputBufferIndex = -1;
  private final ByteBuffer mDataIn;
  private boolean mEof;
  private final String mMimeType;
  private final ByteBuffer mPendingHeader;
  private final int mReadSize;
  private final int mSampleRate;
  private final InputStream mStream;
  private int mTotalRead;
  
  public AudioEncoderInputStream(InputStream paramInputStream, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mReadSize = paramInt2;
    this.mStream = paramInputStream;
    this.mSampleRate = paramInt1;
    this.mChannels = paramInt4;
    this.mMimeType = paramString;
    MediaFormat localMediaFormat;
    if ("audio/mp4a-latm".equals(paramString))
    {
      if (this.mSampleRate == 11025)
      {
        int m = i;
        Preconditions.checkState(m);
        if (this.mChannels != i) {
          break label192;
        }
      }
      for (;;)
      {
        Preconditions.checkState(i);
        this.mCodecType = 0;
        this.mPendingHeader = ByteBuffer.wrap(new byte[7]);
        this.mDataIn = ByteBuffer.wrap(new byte[this.mReadSize]);
        this.mDataIn.position(this.mReadSize);
        localMediaFormat = new MediaFormat();
        localMediaFormat.setString("mime", paramString);
        localMediaFormat.setInteger("sample-rate", this.mSampleRate);
        localMediaFormat.setInteger("bitrate", paramInt3);
        localMediaFormat.setInteger("channel-count", this.mChannels);
        if (!isAac()) {
          break label298;
        }
        try
        {
          startCodecByName("OMX.google.aac.encoder", localMediaFormat);
          return;
        }
        catch (Exception localException)
        {
          int n;
          label192:
          int j;
          boolean bool1;
          startCodecByMimeType(paramString, localMediaFormat);
          return;
        }
        n = 0;
        break;
        j = 0;
      }
    }
    if ("audio/amr-wb".equals(paramString))
    {
      if (this.mSampleRate == 16000) {}
      for (bool1 = j;; bool1 = false)
      {
        Preconditions.checkState(bool1);
        int k = this.mChannels;
        boolean bool2 = false;
        if (k == j) {
          bool2 = j;
        }
        Preconditions.checkState(bool2);
        this.mCodecType = j;
        this.mPendingHeader = ByteBuffer.wrap("#!AMR-WB\n".getBytes());
        break;
      }
    }
    throw new IllegalArgumentException("Unsupported audio codec");
    label298:
    startCodecByMimeType(paramString, localMediaFormat);
  }
  
  private void encodeStream()
    throws IOException
  {
    boolean bool1 = true;
    if (this.mCurrentOutputBufferIndex > -1)
    {
      this.mCodec.releaseOutputBuffer(this.mCurrentOutputBufferIndex, false);
      this.mCurrentOutputBufferIndex = -1;
    }
    int i = this.mCodec.dequeueInputBuffer(10000L);
    if (i != -1) {
      onInputBufferReady(this.mCodec, i);
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int j = this.mCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
    boolean bool2;
    boolean bool3;
    if (j == -2)
    {
      MediaFormat localMediaFormat = this.mCodec.getOutputFormat();
      if (this.mSampleRate == localMediaFormat.getInteger("sample-rate"))
      {
        bool2 = bool1;
        Preconditions.checkState(bool2);
        if (this.mChannels != localMediaFormat.getInteger("channel-count")) {
          break label187;
        }
        bool3 = bool1;
        label128:
        Preconditions.checkState(bool3);
        Preconditions.checkState(this.mMimeType.equals(localMediaFormat.getString("mime")));
        j = this.mCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
      }
    }
    else
    {
      if (j == -2) {
        break label193;
      }
      label170:
      Preconditions.checkState(bool1);
      if (j != -1) {
        break label198;
      }
    }
    label187:
    label193:
    label198:
    do
    {
      return;
      bool2 = false;
      break;
      bool3 = false;
      break label128;
      bool1 = false;
      break label170;
      if (j == -3)
      {
        this.mCodecOutputBuffers = this.mCodec.getOutputBuffers();
        return;
      }
    } while (j == -1);
    onOutputBufferReady(this.mCodec, j, localBufferInfo.offset, localBufferInfo.size, localBufferInfo.presentationTimeUs, localBufferInfo.flags);
  }
  
  private boolean isAac()
  {
    return this.mCodecType == 0;
  }
  
  private void onInputBufferReady(MediaCodec paramMediaCodec, int paramInt)
    throws IOException
  {
    ByteBuffer localByteBuffer = this.mCodecInputBuffers[paramInt];
    localByteBuffer.clear();
    localByteBuffer.position(0);
    Preconditions.checkState(localByteBuffer.hasRemaining());
    for (;;)
    {
      int i;
      if ((localByteBuffer.position() < this.mReadSize) && (localByteBuffer.hasRemaining()) && (!this.mEof))
      {
        if (this.mDataIn.hasRemaining())
        {
          int j = Math.min(this.mDataIn.remaining(), Math.min(localByteBuffer.remaining(), this.mReadSize - localByteBuffer.position()));
          localByteBuffer.put(this.mDataIn.array(), this.mDataIn.position(), j);
          this.mDataIn.position(j + this.mDataIn.position());
          continue;
        }
        i = this.mStream.read(this.mDataIn.array());
        if (i != -1) {}
      }
      else
      {
        if (localByteBuffer.position() <= 0) {
          break;
        }
        paramMediaCodec.queueInputBuffer(paramInt, 0, localByteBuffer.position(), 0L, 0);
        return;
      }
      this.mDataIn.position(0);
      this.mDataIn.limit(i);
      this.mTotalRead = (i + this.mTotalRead);
    }
    this.mEof = true;
    paramMediaCodec.queueInputBuffer(paramInt, 0, 0, 0L, 4);
  }
  
  private void onOutputBufferReady(MediaCodec paramMediaCodec, int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4)
  {
    this.mCurrentOutputBufferIndex = paramInt1;
    if (isAac())
    {
      this.mPendingHeader.clear();
      setAdtsHeaderBytes(paramInt3, this.mPendingHeader);
      this.mPendingHeader.flip();
    }
    ByteBuffer localByteBuffer = this.mCodecOutputBuffers[paramInt1];
    localByteBuffer.clear();
    localByteBuffer.position(paramInt2);
    localByteBuffer.limit(paramInt2 + paramInt3);
  }
  
  private void release()
  {
    if (this.mCodec != null) {
      this.mCodec.release();
    }
    this.mCodec = null;
  }
  
  private void setAdtsHeaderBytes(int paramInt, ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer.remaining() >= 7) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      long l = writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(writeBits(0L, 12, 4095), 1, 0), 2, 0), 1, 1), 2, 0), 4, 10), 1, 0), 3, 1), 1, 0), 1, 0), 1, 0), 1, 0), 13, paramInt + 7), 11, 2047), 2, 0);
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 48));
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 40));
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 32));
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 24));
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 16));
      paramByteBuffer.put((byte)(int)(0xFF & l >>> 8));
      paramByteBuffer.put((byte)(int)l);
      return;
    }
  }
  
  private void startAndConfigureCodec(MediaCodec paramMediaCodec, MediaFormat paramMediaFormat)
  {
    try
    {
      this.mCodec = paramMediaCodec;
      this.mCodec.configure(paramMediaFormat, null, null, 1);
      this.mCodec.start();
      this.mCodecInputBuffers = this.mCodec.getInputBuffers();
      this.mCodecOutputBuffers = this.mCodec.getOutputBuffers();
      if (this.mCodec == null) {
        throw new IllegalArgumentException("Could not create codec");
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        this.mCodec = null;
        this.mCodecInputBuffers = null;
        this.mCodecOutputBuffers = null;
      }
    }
  }
  
  private void startCodecByMimeType(String paramString, MediaFormat paramMediaFormat)
  {
    startAndConfigureCodec(MediaCodec.createEncoderByType(paramString), paramMediaFormat);
  }
  
  private void startCodecByName(String paramString, MediaFormat paramMediaFormat)
  {
    startAndConfigureCodec(MediaCodec.createByCodecName(paramString), paramMediaFormat);
  }
  
  private void stop()
  {
    if (this.mCodec != null) {
      this.mCodec.stop();
    }
  }
  
  private static long writeBits(long paramLong, int paramInt1, int paramInt2)
  {
    long l = -1L >>> 64 - paramInt1;
    return paramLong << paramInt1 | l & paramInt2;
  }
  
  public void close()
  {
    Closeables.closeQuietly(this.mStream);
    stop();
    release();
  }
  
  public void finalize()
    throws Throwable
  {
    if (this.mCodec != null)
    {
      close();
      throw new IllegalStateException("no one closed");
    }
  }
  
  public int getTotalRead()
  {
    return this.mTotalRead;
  }
  
  public int read()
  {
    throw new UnsupportedOperationException("Single-byte read not supported");
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if ((this.mEof) && (!this.mDataIn.hasRemaining())) {
      i = 1;
    }
    while ((i == 0) && ((this.mCurrentOutputBufferIndex == -1) || (!this.mCodecOutputBuffers[this.mCurrentOutputBufferIndex].hasRemaining())))
    {
      encodeStream();
      continue;
      i = 0;
    }
    if (this.mEof) {
      return -1;
    }
    boolean bool = this.mPendingHeader.hasRemaining();
    int j = 0;
    if (bool)
    {
      j = Math.min(paramInt2, this.mPendingHeader.remaining());
      this.mPendingHeader.get(paramArrayOfByte, paramInt1, j);
      paramInt1 += j;
      paramInt2 -= j;
    }
    ByteBuffer localByteBuffer = this.mCodecOutputBuffers[this.mCurrentOutputBufferIndex];
    Preconditions.checkState(localByteBuffer.hasRemaining());
    int k = Math.min(paramInt2, localByteBuffer.remaining());
    localByteBuffer.get(paramArrayOfByte, paramInt1, k);
    return k + j;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.audio.AudioEncoderInputStream
 * JD-Core Version:    0.7.0.1
 */