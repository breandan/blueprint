package com.android.gallery3d.exif;

public class Rational
{
  private final long mDenominator;
  private final long mNumerator;
  
  public Rational(long paramLong1, long paramLong2)
  {
    this.mNumerator = paramLong1;
    this.mDenominator = paramLong2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == null) {}
    do
    {
      return false;
      if (this == paramObject) {
        return bool;
      }
    } while (!(paramObject instanceof Rational));
    Rational localRational = (Rational)paramObject;
    if ((this.mNumerator == localRational.mNumerator) && (this.mDenominator == localRational.mDenominator)) {}
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  public long getDenominator()
  {
    return this.mDenominator;
  }
  
  public long getNumerator()
  {
    return this.mNumerator;
  }
  
  public String toString()
  {
    return this.mNumerator + "/" + this.mDenominator;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.Rational
 * JD-Core Version:    0.7.0.1
 */