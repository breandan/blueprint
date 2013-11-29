package com.android.gallery3d.util;

public class IntArray
{
  private int[] mData = new int[8];
  private int mSize = 0;
  
  public void add(int paramInt)
  {
    if (this.mData.length == this.mSize)
    {
      int[] arrayOfInt2 = new int[this.mSize + this.mSize];
      System.arraycopy(this.mData, 0, arrayOfInt2, 0, this.mSize);
      this.mData = arrayOfInt2;
    }
    int[] arrayOfInt1 = this.mData;
    int i = this.mSize;
    this.mSize = (i + 1);
    arrayOfInt1[i] = paramInt;
  }
  
  public int removeLast()
  {
    this.mSize = (-1 + this.mSize);
    return this.mData[this.mSize];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.util.IntArray
 * JD-Core Version:    0.7.0.1
 */