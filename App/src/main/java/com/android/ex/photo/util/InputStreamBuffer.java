package com.android.ex.photo.util;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class InputStreamBuffer
{
  private boolean mAutoAdvance;
  private byte[] mBuffer;
  private int mFilled = 0;
  private InputStream mInputStream;
  private int mOffset = 0;
  
  public InputStreamBuffer(InputStream paramInputStream, int paramInt, boolean paramBoolean)
  {
    this.mInputStream = paramInputStream;
    if (paramInt <= 0)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      throw new IllegalArgumentException(String.format("Buffer size %d must be positive.", arrayOfObject));
    }
    this.mBuffer = new byte[leastPowerOf2(paramInt)];
    this.mAutoAdvance = paramBoolean;
  }
  
  private boolean fill(int paramInt)
  {
    boolean bool = true;
    Trace.beginSection("fill");
    if (paramInt < this.mOffset)
    {
      Trace.endSection();
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = Integer.valueOf(paramInt);
      arrayOfObject3[bool] = Integer.valueOf(this.mOffset);
      throw new IllegalStateException(String.format("Index %d is before buffer %d", arrayOfObject3));
    }
    int i = paramInt - this.mOffset;
    if (this.mInputStream == null)
    {
      Trace.endSection();
      return false;
    }
    int j = i + 1;
    int k;
    if (j > this.mBuffer.length)
    {
      if (this.mAutoAdvance)
      {
        advanceTo(paramInt);
        i = paramInt - this.mOffset;
      }
    }
    else {
      k = -1;
    }
    try
    {
      int m = this.mInputStream.read(this.mBuffer, this.mFilled, this.mBuffer.length - this.mFilled);
      k = m;
    }
    catch (IOException localIOException)
    {
      label145:
      label162:
      Object[] arrayOfObject1;
      label287:
      break label145;
    }
    if (k != -1)
    {
      this.mFilled = (k + this.mFilled);
      if (Log.isLoggable("InputStreamBuffer", 3))
      {
        arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Integer.valueOf(i);
        arrayOfObject1[bool] = this;
        Log.d("InputStreamBuffer", String.format("fill %d      buffer: %s", arrayOfObject1));
      }
      Trace.endSection();
      if (i >= this.mFilled) {
        break label287;
      }
    }
    for (;;)
    {
      return bool;
      int n = leastPowerOf2(j);
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(this.mBuffer.length);
      arrayOfObject2[bool] = Integer.valueOf(n);
      Log.w("InputStreamBuffer", String.format("Increasing buffer length from %d to %d. Bad buffer size chosen, or advanceTo() not called.", arrayOfObject2));
      this.mBuffer = Arrays.copyOf(this.mBuffer, n);
      break;
      this.mInputStream = null;
      break label162;
      bool = false;
    }
  }
  
  private static int leastPowerOf2(int paramInt)
  {
    int i = paramInt - 1;
    int j = i | i >> 1;
    int k = j | j >> 2;
    int m = k | k >> 4;
    int n = m | m >> 8;
    return 1 + (n | n >> 16);
  }
  
  private void shiftToBeginning(int paramInt)
  {
    if (paramInt >= this.mBuffer.length)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      arrayOfObject[1] = Integer.valueOf(this.mBuffer.length);
      throw new IndexOutOfBoundsException(String.format("Index %d out of bounds. Length %d", arrayOfObject));
    }
    for (int i = 0; i + paramInt < this.mFilled; i++) {
      this.mBuffer[i] = this.mBuffer[(i + paramInt)];
    }
  }
  
  public void advanceTo(int paramInt)
    throws IllegalStateException, IndexOutOfBoundsException
  {
    Trace.beginSection("advance to");
    int i = paramInt - this.mOffset;
    if (i <= 0)
    {
      Trace.endSection();
      return;
    }
    if (i < this.mFilled)
    {
      shiftToBeginning(i);
      this.mOffset = paramInt;
      this.mFilled -= i;
    }
    for (;;)
    {
      if (Log.isLoggable("InputStreamBuffer", 3))
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(i);
        arrayOfObject[1] = this;
        Log.d("InputStreamBuffer", String.format("advanceTo %d buffer: %s", arrayOfObject));
      }
      Trace.endSection();
      return;
      label107:
      int m;
      if (this.mInputStream != null)
      {
        int j = i - this.mFilled;
        int k = 0;
        m = 0;
        if (j > 0) {}
        for (;;)
        {
          try
          {
            l = this.mInputStream.skip(j);
            if (l > 0L) {
              continue;
            }
            k++;
            if (k < 5) {
              break label107;
            }
            m = 1;
          }
          catch (IOException localIOException)
          {
            long l;
            m = 1;
            continue;
          }
          if (m != 0) {
            this.mInputStream = null;
          }
          this.mOffset = (paramInt - j);
          this.mFilled = 0;
          break;
          j = (int)(j - l);
        }
      }
      this.mOffset = paramInt;
      this.mFilled = 0;
    }
  }
  
  public byte get(int paramInt)
    throws IllegalStateException, IndexOutOfBoundsException
  {
    Trace.beginSection("get");
    if (has(paramInt))
    {
      int i = paramInt - this.mOffset;
      Trace.endSection();
      return this.mBuffer[i];
    }
    Trace.endSection();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    throw new IndexOutOfBoundsException(String.format("Index %d beyond length.", arrayOfObject));
  }
  
  public boolean has(int paramInt)
    throws IllegalStateException, IndexOutOfBoundsException
  {
    Trace.beginSection("has");
    if (paramInt < this.mOffset)
    {
      Trace.endSection();
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      arrayOfObject[1] = Integer.valueOf(this.mOffset);
      throw new IllegalStateException(String.format("Index %d is before buffer %d", arrayOfObject));
    }
    int i = paramInt - this.mOffset;
    if ((i >= this.mFilled) || (i >= this.mBuffer.length))
    {
      Trace.endSection();
      return fill(paramInt);
    }
    Trace.endSection();
    return true;
  }
  
  public String toString()
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(this.mOffset);
    arrayOfObject[1] = Integer.valueOf(this.mBuffer.length);
    arrayOfObject[2] = Integer.valueOf(this.mFilled);
    return String.format("+%d+%d [%d]", arrayOfObject);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.InputStreamBuffer
 * JD-Core Version:    0.7.0.1
 */