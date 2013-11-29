package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public final class Files
{
  public static InputSupplier<FileInputStream> newInputStreamSupplier(File paramFile)
  {
    Preconditions.checkNotNull(paramFile);
    new InputSupplier()
    {
      public FileInputStream getInput()
        throws IOException
      {
        return new FileInputStream(this.val$file);
      }
    };
  }
  
  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File paramFile)
  {
    return newOutputStreamSupplier(paramFile, false);
  }
  
  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File paramFile, final boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramFile);
    new OutputSupplier()
    {
      public FileOutputStream getOutput()
        throws IOException
      {
        return new FileOutputStream(this.val$file, paramBoolean);
      }
    };
  }
  
  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File paramFile, Charset paramCharset, boolean paramBoolean)
  {
    return CharStreams.newWriterSupplier(newOutputStreamSupplier(paramFile, paramBoolean), paramCharset);
  }
  
  public static byte[] toByteArray(File paramFile)
    throws IOException
  {
    if (paramFile.length() <= 2147483647L) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      if (paramFile.length() != 0L) {
        break;
      }
      return ByteStreams.toByteArray(newInputStreamSupplier(paramFile));
    }
    byte[] arrayOfByte = new byte[(int)paramFile.length()];
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    try
    {
      ByteStreams.readFully(localFileInputStream, arrayOfByte);
      Closeables.close(localFileInputStream, false);
      return arrayOfByte;
    }
    finally
    {
      Closeables.close(localFileInputStream, true);
    }
  }
  
  public static void write(CharSequence paramCharSequence, File paramFile, Charset paramCharset)
    throws IOException
  {
    write(paramCharSequence, paramFile, paramCharset, false);
  }
  
  private static void write(CharSequence paramCharSequence, File paramFile, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    CharStreams.write(paramCharSequence, newWriterSupplier(paramFile, paramCharset, paramBoolean));
  }
  
  public static void write(byte[] paramArrayOfByte, File paramFile)
    throws IOException
  {
    ByteStreams.write(paramArrayOfByte, newOutputStreamSupplier(paramFile));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.io.Files
 * JD-Core Version:    0.7.0.1
 */