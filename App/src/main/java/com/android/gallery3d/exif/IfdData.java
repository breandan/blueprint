package com.android.gallery3d.exif;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class IfdData
{
  private static final int[] sIfds = { 0, 1, 2, 3, 4 };
  private final Map<Short, ExifTag> mExifTags = new HashMap();
  private final int mIfdId;
  private int mOffsetToNextIfd = 0;
  
  IfdData(int paramInt)
  {
    this.mIfdId = paramInt;
  }
  
  protected static int[] getIfds()
  {
    return sIfds;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof IfdData))
    {
      IfdData localIfdData = (IfdData)paramObject;
      if ((localIfdData.getId() == this.mIfdId) && (localIfdData.getTagCount() == getTagCount()))
      {
        ExifTag[] arrayOfExifTag = localIfdData.getAllTags();
        int i = arrayOfExifTag.length;
        int j = 0;
        label59:
        ExifTag localExifTag;
        if (j < i)
        {
          localExifTag = arrayOfExifTag[j];
          if (!ExifInterface.isOffsetTag(localExifTag.getTagId())) {
            break label89;
          }
        }
        label89:
        while (localExifTag.equals((ExifTag)this.mExifTags.get(Short.valueOf(localExifTag.getTagId()))))
        {
          j++;
          break label59;
          break;
        }
        return false;
      }
    }
    return false;
  }
  
  protected ExifTag[] getAllTags()
  {
    return (ExifTag[])this.mExifTags.values().toArray(new ExifTag[this.mExifTags.size()]);
  }
  
  protected int getId()
  {
    return this.mIfdId;
  }
  
  protected ExifTag getTag(short paramShort)
  {
    return (ExifTag)this.mExifTags.get(Short.valueOf(paramShort));
  }
  
  protected int getTagCount()
  {
    return this.mExifTags.size();
  }
  
  protected ExifTag setTag(ExifTag paramExifTag)
  {
    paramExifTag.setIfd(this.mIfdId);
    return (ExifTag)this.mExifTags.put(Short.valueOf(paramExifTag.getTagId()), paramExifTag);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.IfdData
 * JD-Core Version:    0.7.0.1
 */