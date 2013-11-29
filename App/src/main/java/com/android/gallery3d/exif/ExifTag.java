package com.android.gallery3d.exif;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ExifTag
{
  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
  private static final int[] TYPE_TO_SIZE_MAP;
  private static Charset US_ASCII = Charset.forName("US-ASCII");
  private int mComponentCountActual;
  private final short mDataType;
  private boolean mHasDefinedDefaultComponentCount;
  private int mIfd;
  private int mOffset;
  private final short mTagId;
  private Object mValue;
  
  static
  {
    TYPE_TO_SIZE_MAP = new int[11];
    TYPE_TO_SIZE_MAP[1] = 1;
    TYPE_TO_SIZE_MAP[2] = 1;
    TYPE_TO_SIZE_MAP[3] = 2;
    TYPE_TO_SIZE_MAP[4] = 4;
    TYPE_TO_SIZE_MAP[5] = 8;
    TYPE_TO_SIZE_MAP[7] = 1;
    TYPE_TO_SIZE_MAP[9] = 4;
    TYPE_TO_SIZE_MAP[10] = 8;
  }
  
  ExifTag(short paramShort1, short paramShort2, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.mTagId = paramShort1;
    this.mDataType = paramShort2;
    this.mComponentCountActual = paramInt1;
    this.mHasDefinedDefaultComponentCount = paramBoolean;
    this.mIfd = paramInt2;
    this.mValue = null;
  }
  
  private boolean checkBadComponentCount(int paramInt)
  {
    return (this.mHasDefinedDefaultComponentCount) && (this.mComponentCountActual != paramInt);
  }
  
  private boolean checkOverflowForRational(Rational[] paramArrayOfRational)
  {
    int i = paramArrayOfRational.length;
    for (int j = 0; j < i; j++)
    {
      Rational localRational = paramArrayOfRational[j];
      if ((localRational.getNumerator() < -2147483648L) || (localRational.getDenominator() < -2147483648L) || (localRational.getNumerator() > 2147483647L) || (localRational.getDenominator() > 2147483647L)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean checkOverflowForUnsignedLong(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfInt[j] < 0) {
        return true;
      }
    }
    return false;
  }
  
  private boolean checkOverflowForUnsignedLong(long[] paramArrayOfLong)
  {
    int i = paramArrayOfLong.length;
    for (int j = 0; j < i; j++)
    {
      long l = paramArrayOfLong[j];
      if ((l < 0L) || (l > 4294967295L)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean checkOverflowForUnsignedRational(Rational[] paramArrayOfRational)
  {
    int i = paramArrayOfRational.length;
    for (int j = 0; j < i; j++)
    {
      Rational localRational = paramArrayOfRational[j];
      if ((localRational.getNumerator() < 0L) || (localRational.getDenominator() < 0L) || (localRational.getNumerator() > 4294967295L) || (localRational.getDenominator() > 4294967295L)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean checkOverflowForUnsignedShort(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfInt[j];
      if ((k > 65535) || (k < 0)) {
        return true;
      }
    }
    return false;
  }
  
  private static String convertTypeToString(short paramShort)
  {
    switch (paramShort)
    {
    case 6: 
    case 8: 
    default: 
      return "";
    case 1: 
      return "UNSIGNED_BYTE";
    case 2: 
      return "ASCII";
    case 3: 
      return "UNSIGNED_SHORT";
    case 4: 
      return "UNSIGNED_LONG";
    case 5: 
      return "UNSIGNED_RATIONAL";
    case 7: 
      return "UNDEFINED";
    case 9: 
      return "LONG";
    }
    return "RATIONAL";
  }
  
  public static int getElementSize(short paramShort)
  {
    return TYPE_TO_SIZE_MAP[paramShort];
  }
  
  public static boolean isValidIfd(int paramInt)
  {
    return (paramInt == 0) || (paramInt == 1) || (paramInt == 2) || (paramInt == 3) || (paramInt == 4);
  }
  
  public static boolean isValidType(short paramShort)
  {
    return (paramShort == 1) || (paramShort == 2) || (paramShort == 3) || (paramShort == 4) || (paramShort == 5) || (paramShort == 7) || (paramShort == 9) || (paramShort == 10);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {}
    ExifTag localExifTag;
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                do
                {
                  return false;
                } while (!(paramObject instanceof ExifTag));
                localExifTag = (ExifTag)paramObject;
              } while ((localExifTag.mTagId != this.mTagId) || (localExifTag.mComponentCountActual != this.mComponentCountActual) || (localExifTag.mDataType != this.mDataType));
              if (this.mValue == null) {
                break;
              }
            } while (localExifTag.mValue == null);
            if (!(this.mValue instanceof long[])) {
              break;
            }
          } while (!(localExifTag.mValue instanceof long[]));
          return Arrays.equals((long[])this.mValue, (long[])localExifTag.mValue);
          if (!(this.mValue instanceof Rational[])) {
            break;
          }
        } while (!(localExifTag.mValue instanceof Rational[]));
        return Arrays.equals((Rational[])this.mValue, (Rational[])localExifTag.mValue);
        if (!(this.mValue instanceof byte[])) {
          break;
        }
      } while (!(localExifTag.mValue instanceof byte[]));
      return Arrays.equals((byte[])this.mValue, (byte[])localExifTag.mValue);
      return this.mValue.equals(localExifTag.mValue);
    } while (localExifTag.mValue != null);
    return true;
  }
  
  public String forceGetValueAsString()
  {
    if (this.mValue == null) {
      return "";
    }
    if ((this.mValue instanceof byte[]))
    {
      if (this.mDataType == 2) {
        return new String((byte[])this.mValue, US_ASCII);
      }
      return Arrays.toString((byte[])this.mValue);
    }
    if ((this.mValue instanceof long[]))
    {
      if (((long[])this.mValue).length == 1) {
        return String.valueOf(((long[])(long[])this.mValue)[0]);
      }
      return Arrays.toString((long[])this.mValue);
    }
    if ((this.mValue instanceof Object[]))
    {
      if (((Object[])this.mValue).length == 1)
      {
        Object localObject = ((Object[])(Object[])this.mValue)[0];
        if (localObject == null) {
          return "";
        }
        return localObject.toString();
      }
      return Arrays.toString((Object[])this.mValue);
    }
    return this.mValue.toString();
  }
  
  protected void forceSetComponentCount(int paramInt)
  {
    this.mComponentCountActual = paramInt;
  }
  
  public int getComponentCount()
  {
    return this.mComponentCountActual;
  }
  
  public int getDataSize()
  {
    return getComponentCount() * getElementSize(getDataType());
  }
  
  public short getDataType()
  {
    return this.mDataType;
  }
  
  public int getIfd()
  {
    return this.mIfd;
  }
  
  protected int getOffset()
  {
    return this.mOffset;
  }
  
  public short getTagId()
  {
    return this.mTagId;
  }
  
  public int[] getValueAsInts()
  {
    Object localObject = this.mValue;
    int[] arrayOfInt = null;
    if (localObject == null) {}
    for (;;)
    {
      return arrayOfInt;
      boolean bool = this.mValue instanceof long[];
      arrayOfInt = null;
      if (bool)
      {
        long[] arrayOfLong = (long[])this.mValue;
        arrayOfInt = new int[arrayOfLong.length];
        for (int i = 0; i < arrayOfLong.length; i++) {
          arrayOfInt[i] = ((int)arrayOfLong[i]);
        }
      }
    }
  }
  
  protected long getValueAt(int paramInt)
  {
    if ((this.mValue instanceof long[])) {
      return ((long[])(long[])this.mValue)[paramInt];
    }
    if ((this.mValue instanceof byte[])) {
      return ((byte[])(byte[])this.mValue)[paramInt];
    }
    throw new IllegalArgumentException("Cannot get integer value from " + convertTypeToString(this.mDataType));
  }
  
  protected boolean hasDefinedCount()
  {
    return this.mHasDefinedDefaultComponentCount;
  }
  
  public boolean hasValue()
  {
    return this.mValue != null;
  }
  
  protected void setHasDefinedCount(boolean paramBoolean)
  {
    this.mHasDefinedDefaultComponentCount = paramBoolean;
  }
  
  protected void setIfd(int paramInt)
  {
    this.mIfd = paramInt;
  }
  
  protected void setOffset(int paramInt)
  {
    this.mOffset = paramInt;
  }
  
  public boolean setValue(String paramString)
  {
    if ((this.mDataType != 2) && (this.mDataType != 7)) {
      return false;
    }
    byte[] arrayOfByte1 = paramString.getBytes(US_ASCII);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1.length > 0) {
      if ((arrayOfByte1[(-1 + arrayOfByte1.length)] == 0) || (this.mDataType == 7)) {
        arrayOfByte2 = arrayOfByte1;
      }
    }
    for (;;)
    {
      int i = arrayOfByte2.length;
      if (checkBadComponentCount(i)) {
        break;
      }
      this.mComponentCountActual = i;
      this.mValue = arrayOfByte2;
      return true;
      arrayOfByte2 = Arrays.copyOf(arrayOfByte1, 1 + arrayOfByte1.length);
      continue;
      if ((this.mDataType == 2) && (this.mComponentCountActual == 1)) {
        arrayOfByte2 = new byte[] { 0 };
      }
    }
  }
  
  public boolean setValue(byte[] paramArrayOfByte)
  {
    return setValue(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public boolean setValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (checkBadComponentCount(paramInt2)) {}
    while ((this.mDataType != 1) && (this.mDataType != 7)) {
      return false;
    }
    this.mValue = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, this.mValue, 0, paramInt2);
    this.mComponentCountActual = paramInt2;
    return true;
  }
  
  public boolean setValue(int[] paramArrayOfInt)
  {
    if (checkBadComponentCount(paramArrayOfInt.length)) {}
    while (((this.mDataType != 3) && (this.mDataType != 9) && (this.mDataType != 4)) || ((this.mDataType == 3) && (checkOverflowForUnsignedShort(paramArrayOfInt))) || ((this.mDataType == 4) && (checkOverflowForUnsignedLong(paramArrayOfInt)))) {
      return false;
    }
    long[] arrayOfLong = new long[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfLong[i] = paramArrayOfInt[i];
    }
    this.mValue = arrayOfLong;
    this.mComponentCountActual = paramArrayOfInt.length;
    return true;
  }
  
  public boolean setValue(long[] paramArrayOfLong)
  {
    if ((checkBadComponentCount(paramArrayOfLong.length)) || (this.mDataType != 4)) {}
    while (checkOverflowForUnsignedLong(paramArrayOfLong)) {
      return false;
    }
    this.mValue = paramArrayOfLong;
    this.mComponentCountActual = paramArrayOfLong.length;
    return true;
  }
  
  public boolean setValue(Rational[] paramArrayOfRational)
  {
    if (checkBadComponentCount(paramArrayOfRational.length)) {}
    while (((this.mDataType != 5) && (this.mDataType != 10)) || ((this.mDataType == 5) && (checkOverflowForUnsignedRational(paramArrayOfRational))) || ((this.mDataType == 10) && (checkOverflowForRational(paramArrayOfRational)))) {
      return false;
    }
    this.mValue = paramArrayOfRational;
    this.mComponentCountActual = paramArrayOfRational.length;
    return true;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Short.valueOf(this.mTagId);
    return String.format("tag id: %04X\n", arrayOfObject) + "ifd id: " + this.mIfd + "\ntype: " + convertTypeToString(this.mDataType) + "\ncount: " + this.mComponentCountActual + "\noffset: " + this.mOffset + "\nvalue: " + forceGetValueAsString() + "\n";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.ExifTag
 * JD-Core Version:    0.7.0.1
 */