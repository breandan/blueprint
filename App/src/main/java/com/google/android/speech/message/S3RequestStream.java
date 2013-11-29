package com.google.android.speech.message;

import com.google.android.speech.utils.HexUtils;
import com.google.common.base.Preconditions;
import com.google.speech.s3.S3.S3Request;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class S3RequestStream
  implements Closeable
{
  private static final byte[] S3_STREAM_PREFIX = { 0, 0 };
  private final boolean mChunked;
  private final String mHeader;
  private boolean mHeaderWritten;
  private final OutputStream mOut;
  private final ByteBuffer mScratch = ByteBuffer.wrap(new byte[1024]);
  
  public S3RequestStream(OutputStream paramOutputStream, String paramString, boolean paramBoolean)
  {
    this.mOut = paramOutputStream;
    this.mHeader = paramString;
    this.mChunked = paramBoolean;
  }
  
  private void internalWrite(S3.S3Request paramS3Request)
    throws IOException
  {
    maybeChunkAndSendBytes(paramS3Request.toByteArray());
  }
  
  /* Error */
  private void maybeChunkAndSendBytes(byte[] paramArrayOfByte)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: arraylength
    //   2: istore_2
    //   3: aload_0
    //   4: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   7: iload_2
    //   8: invokevirtual 58	java/nio/ByteBuffer:putInt	(I)Ljava/nio/ByteBuffer;
    //   11: pop
    //   12: aload_0
    //   13: getfield 40	com/google/android/speech/message/S3RequestStream:mChunked	Z
    //   16: ifne +50 -> 66
    //   19: aload_0
    //   20: getfield 36	com/google/android/speech/message/S3RequestStream:mOut	Ljava/io/OutputStream;
    //   23: aload_0
    //   24: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   27: invokevirtual 61	java/nio/ByteBuffer:array	()[B
    //   30: iconst_0
    //   31: aload_0
    //   32: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   35: invokevirtual 65	java/nio/ByteBuffer:position	()I
    //   38: invokevirtual 71	java/io/OutputStream:write	([BII)V
    //   41: aload_0
    //   42: getfield 36	com/google/android/speech/message/S3RequestStream:mOut	Ljava/io/OutputStream;
    //   45: aload_1
    //   46: invokevirtual 73	java/io/OutputStream:write	([B)V
    //   49: aload_0
    //   50: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   53: invokevirtual 77	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   56: pop
    //   57: aload_0
    //   58: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   61: invokevirtual 77	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   64: pop
    //   65: return
    //   66: iload_2
    //   67: ifle -10 -> 57
    //   70: aload_0
    //   71: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   74: invokevirtual 80	java/nio/ByteBuffer:remaining	()I
    //   77: iload_2
    //   78: invokestatic 86	java/lang/Math:min	(II)I
    //   81: istore 6
    //   83: aload_0
    //   84: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   87: aload_1
    //   88: aload_1
    //   89: arraylength
    //   90: iload_2
    //   91: isub
    //   92: iload 6
    //   94: invokevirtual 90	java/nio/ByteBuffer:put	([BII)Ljava/nio/ByteBuffer;
    //   97: pop
    //   98: iload_2
    //   99: iload 6
    //   101: isub
    //   102: istore_2
    //   103: aload_0
    //   104: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   107: invokevirtual 80	java/nio/ByteBuffer:remaining	()I
    //   110: ifeq +7 -> 117
    //   113: iload_2
    //   114: ifne -48 -> 66
    //   117: aload_0
    //   118: getfield 36	com/google/android/speech/message/S3RequestStream:mOut	Ljava/io/OutputStream;
    //   121: aload_0
    //   122: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   125: invokevirtual 61	java/nio/ByteBuffer:array	()[B
    //   128: iconst_0
    //   129: aload_0
    //   130: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   133: invokevirtual 65	java/nio/ByteBuffer:position	()I
    //   136: invokevirtual 71	java/io/OutputStream:write	([BII)V
    //   139: aload_0
    //   140: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   143: invokevirtual 77	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   146: pop
    //   147: goto -81 -> 66
    //   150: astore 4
    //   152: aload_0
    //   153: getfield 34	com/google/android/speech/message/S3RequestStream:mScratch	Ljava/nio/ByteBuffer;
    //   156: invokevirtual 77	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   159: pop
    //   160: aload 4
    //   162: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	163	0	this	S3RequestStream
    //   0	163	1	paramArrayOfByte	byte[]
    //   2	112	2	i	int
    //   150	11	4	localObject	Object
    //   81	21	6	j	int
    // Exception table:
    //   from	to	target	type
    //   12	57	150	finally
    //   70	98	150	finally
    //   103	113	150	finally
    //   117	147	150	finally
  }
  
  public void close()
    throws IOException
  {
    this.mOut.close();
  }
  
  public void flush()
    throws IOException
  {
    this.mOut.flush();
  }
  
  public void write(S3.S3Request paramS3Request)
    throws IOException
  {
    Preconditions.checkState(this.mHeaderWritten);
    if (this.mScratch.position() == 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      internalWrite(paramS3Request);
      return;
    }
  }
  
  public void writeHeader(S3.S3Request paramS3Request)
    throws IOException
  {
    if (!this.mHeaderWritten) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkState(bool1);
      int i = this.mScratch.position();
      boolean bool2 = false;
      if (i == 0) {
        bool2 = true;
      }
      Preconditions.checkState(bool2);
      this.mScratch.put(S3_STREAM_PREFIX);
      this.mScratch.put(HexUtils.hexToBytes(this.mHeader.replace("_", "")));
      internalWrite(paramS3Request);
      this.mHeaderWritten = true;
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.message.S3RequestStream
 * JD-Core Version:    0.7.0.1
 */