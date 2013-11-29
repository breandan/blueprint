package com.google.android.search.core.sdch;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;

public class VcDiffDecoderStream
  extends InputStream
{
  private boolean mClosed;
  private int mDecoderHandle;
  private final InputStream mDelegate;
  private final byte[] mDictionary;
  private boolean mInited;
  private final int mLength;
  private final int mOffset;
  private final byte[] mScratch;
  
  static
  {
    System.loadLibrary("vcdecoder_jni");
  }
  
  VcDiffDecoderStream(InputStream paramInputStream, byte[] paramArrayOfByte)
  {
    this(paramInputStream, paramArrayOfByte, 0, paramArrayOfByte.length, 4096);
  }
  
  public VcDiffDecoderStream(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mDelegate = paramInputStream;
    this.mDictionary = paramArrayOfByte;
    this.mOffset = paramInt1;
    this.mLength = paramInt2;
    this.mScratch = new byte[paramInt3];
    this.mDecoderHandle = -1;
  }
  
  private int decodeInternal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    int j;
    try
    {
      i = ByteStreams.read(this.mDelegate, this.mScratch, 0, this.mScratch.length);
      j = nativeDecode(this.mDecoderHandle, paramArrayOfByte, paramInt1, paramInt2, this.mScratch, i);
      if (j < 0)
      {
        close();
        throw new IOException("Error decoding stream, error code: " + j);
      }
    }
    catch (IOException localIOException)
    {
      close();
      throw localIOException;
    }
    if ((j == 0) && (i == 0))
    {
      close();
      j = -1;
    }
    return j;
  }
  
  private void maybeCleanupNativeHandle()
  {
    if (this.mDecoderHandle != -1)
    {
      nativeCleanup(this.mDecoderHandle);
      this.mDecoderHandle = -1;
    }
  }
  
  private void maybeInit(byte[] paramArrayOfByte)
    throws IOException
  {
    if (!this.mInited)
    {
      this.mInited = true;
      this.mDecoderHandle = nativeStart(paramArrayOfByte, this.mOffset, this.mLength);
      if (this.mDecoderHandle == 0) {
        throw new IOException("Could not initialize streaming decoder");
      }
    }
  }
  
  private native void nativeCleanup(int paramInt);
  
  private native int nativeDecode(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int paramInt4);
  
  private native int nativeStart(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public void close()
    throws IOException
  {
    maybeCleanupNativeHandle();
    this.mDelegate.close();
    super.close();
    this.mClosed = true;
  }
  
  public int read()
    throws IOException
  {
    throw new UnsupportedOperationException("Single byte reads not supported.");
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 == 0) {
      return 0;
    }
    if (this.mClosed) {
      return -1;
    }
    maybeInit(this.mDictionary);
    int i = this.mDecoderHandle;
    boolean bool = false;
    if (i != -1) {
      bool = true;
    }
    Preconditions.checkState(bool);
    int j;
    do
    {
      j = decodeInternal(paramArrayOfByte, paramInt1, paramInt2);
    } while (j == 0);
    return j;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.VcDiffDecoderStream
 * JD-Core Version:    0.7.0.1
 */