package com.android.gallery3d.exif;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

class ExifData
{
  private static final byte[] USER_COMMENT_ASCII = { 65, 83, 67, 73, 73, 0, 0, 0 };
  private static final byte[] USER_COMMENT_JIS = { 74, 73, 83, 0, 0, 0, 0, 0 };
  private static final byte[] USER_COMMENT_UNICODE = { 85, 78, 73, 67, 79, 68, 69, 0 };
  private final ByteOrder mByteOrder;
  private final IfdData[] mIfdDatas = new IfdData[5];
  private ArrayList<byte[]> mStripBytes = new ArrayList();
  private byte[] mThumbnail;
  
  ExifData(ByteOrder paramByteOrder)
  {
    this.mByteOrder = paramByteOrder;
  }
  
  protected void addIfdData(IfdData paramIfdData)
  {
    this.mIfdDatas[paramIfdData.getId()] = paramIfdData;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof ExifData))
    {
      ExifData localExifData = (ExifData)paramObject;
      if ((localExifData.mByteOrder != this.mByteOrder) || (localExifData.mStripBytes.size() != this.mStripBytes.size()) || (!Arrays.equals(localExifData.mThumbnail, this.mThumbnail))) {
        return false;
      }
      for (int i = 0; i < this.mStripBytes.size(); i++) {
        if (!Arrays.equals((byte[])localExifData.mStripBytes.get(i), (byte[])this.mStripBytes.get(i))) {
          return false;
        }
      }
      for (int j = 0; j < 5; j++)
      {
        IfdData localIfdData1 = localExifData.getIfdData(j);
        IfdData localIfdData2 = getIfdData(j);
        if ((localIfdData1 != localIfdData2) && (localIfdData1 != null) && (!localIfdData1.equals(localIfdData2))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  protected IfdData getIfdData(int paramInt)
  {
    if (ExifTag.isValidIfd(paramInt)) {
      return this.mIfdDatas[paramInt];
    }
    return null;
  }
  
  protected ExifTag getTag(short paramShort, int paramInt)
  {
    IfdData localIfdData = this.mIfdDatas[paramInt];
    if (localIfdData == null) {
      return null;
    }
    return localIfdData.getTag(paramShort);
  }
  
  protected void setCompressedThumbnail(byte[] paramArrayOfByte)
  {
    this.mThumbnail = paramArrayOfByte;
  }
  
  protected void setStripBytes(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramInt < this.mStripBytes.size())
    {
      this.mStripBytes.set(paramInt, paramArrayOfByte);
      return;
    }
    for (int i = this.mStripBytes.size(); i < paramInt; i++) {
      this.mStripBytes.add(null);
    }
    this.mStripBytes.add(paramArrayOfByte);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.ExifData
 * JD-Core Version:    0.7.0.1
 */