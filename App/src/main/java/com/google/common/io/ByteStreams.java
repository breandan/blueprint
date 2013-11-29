package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams
{
  public static long copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4096];
    int i;
    for (long l = 0L;; l += i)
    {
      i = paramInputStream.read(arrayOfByte);
      if (i == -1) {
        return l;
      }
      paramOutputStream.write(arrayOfByte, 0, i);
    }
  }
  
  public static int read(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 0) {
      throw new IndexOutOfBoundsException("len is negative");
    }
    int i = 0;
    for (;;)
    {
      int j;
      if (i < paramInt2)
      {
        j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (j != -1) {}
      }
      else
      {
        return i;
      }
      i += j;
    }
  }
  
  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    readFully(paramInputStream, paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (read(paramInputStream, paramArrayOfByte, paramInt1, paramInt2) != paramInt2) {
      throw new EOFException();
    }
  }
  
  public static byte[] toByteArray(InputSupplier<? extends InputStream> paramInputSupplier)
    throws IOException
  {
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      byte[] arrayOfByte = toByteArray(localInputStream);
      Closeables.close(localInputStream, false);
      return arrayOfByte;
    }
    finally
    {
      Closeables.close(localInputStream, true);
    }
  }
  
  public static byte[] toByteArray(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    copy(paramInputStream, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }
  
  public static void write(byte[] paramArrayOfByte, OutputSupplier<? extends OutputStream> paramOutputSupplier)
    throws IOException
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    OutputStream localOutputStream = (OutputStream)paramOutputSupplier.getOutput();
    try
    {
      localOutputStream.write(paramArrayOfByte);
      Closeables.close(localOutputStream, false);
      return;
    }
    finally
    {
      Closeables.close(localOutputStream, true);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.io.ByteStreams
 * JD-Core Version:    0.7.0.1
 */