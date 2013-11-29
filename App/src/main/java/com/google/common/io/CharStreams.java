package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public final class CharStreams
{
  public static long copy(Readable paramReadable, Appendable paramAppendable)
    throws IOException
  {
    CharBuffer localCharBuffer = CharBuffer.allocate(2048);
    int i;
    for (long l = 0L;; l += i)
    {
      i = paramReadable.read(localCharBuffer);
      if (i == -1) {
        return l;
      }
      localCharBuffer.flip();
      paramAppendable.append(localCharBuffer, 0, i);
    }
  }
  
  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> paramOutputSupplier, final Charset paramCharset)
  {
    Preconditions.checkNotNull(paramOutputSupplier);
    Preconditions.checkNotNull(paramCharset);
    new OutputSupplier()
    {
      public OutputStreamWriter getOutput()
        throws IOException
      {
        return new OutputStreamWriter((OutputStream)this.val$out.getOutput(), paramCharset);
      }
    };
  }
  
  public static <W extends Appendable,  extends Closeable> void write(CharSequence paramCharSequence, OutputSupplier<W> paramOutputSupplier)
    throws IOException
  {
    Preconditions.checkNotNull(paramCharSequence);
    Appendable localAppendable = (Appendable)paramOutputSupplier.getOutput();
    try
    {
      localAppendable.append(paramCharSequence);
      Closeables.close((Closeable)localAppendable, false);
      return;
    }
    finally
    {
      Closeables.close((Closeable)localAppendable, true);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.io.CharStreams
 * JD-Core Version:    0.7.0.1
 */