package com.android.gallery3d.exif;

import android.util.Log;
import android.util.SparseIntArray;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.TreeMap;

class ExifParser
{
  private static final short TAG_EXIF_IFD;
  private static final short TAG_GPS_IFD;
  private static final short TAG_INTEROPERABILITY_IFD;
  private static final short TAG_JPEG_INTERCHANGE_FORMAT;
  private static final short TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
  private static final short TAG_STRIP_BYTE_COUNTS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS);
  private static final short TAG_STRIP_OFFSETS;
  private static final Charset US_ASCII = Charset.forName("US-ASCII");
  private int mApp1End;
  private boolean mContainExifData = false;
  private final TreeMap<Integer, Object> mCorrespondingEvent = new TreeMap();
  private byte[] mDataAboveIfd0;
  private int mIfd0Position;
  private int mIfdStartOffset = 0;
  private int mIfdType;
  private ImageEvent mImageEvent;
  private final ExifInterface mInterface;
  private ExifTag mJpegSizeTag;
  private boolean mNeedToParseOffsetsInCurrentIfd;
  private int mNumOfTagInIfd = 0;
  private int mOffsetToApp1EndFromSOF = 0;
  private final int mOptions;
  private ExifTag mStripSizeTag;
  private ExifTag mTag;
  private int mTiffStartPosition;
  private final CountedDataInputStream mTiffStream;
  
  static
  {
    TAG_EXIF_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD);
    TAG_GPS_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD);
    TAG_INTEROPERABILITY_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD);
    TAG_JPEG_INTERCHANGE_FORMAT = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
    TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
    TAG_STRIP_OFFSETS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS);
  }
  
  private ExifParser(InputStream paramInputStream, int paramInt, ExifInterface paramExifInterface)
    throws IOException, ExifInvalidFormatException
  {
    if (paramInputStream == null) {
      throw new IOException("Null argument inputStream to ExifParser");
    }
    this.mInterface = paramExifInterface;
    this.mContainExifData = seekTiffData(paramInputStream);
    this.mTiffStream = new CountedDataInputStream(paramInputStream);
    this.mOptions = paramInt;
    if (!this.mContainExifData) {}
    long l;
    do
    {
      do
      {
        return;
        parseTiffHeader();
        l = this.mTiffStream.readUnsignedInt();
        if (l > 2147483647L) {
          throw new ExifInvalidFormatException("Invalid offset " + l);
        }
        this.mIfd0Position = ((int)l);
        this.mIfdType = 0;
      } while ((!isIfdRequested(0)) && (!needToParseOffsetsInCurrentIfd()));
      registerIfd(0, l);
    } while (l == 8L);
    this.mDataAboveIfd0 = new byte[-8 + (int)l];
    read(this.mDataAboveIfd0);
  }
  
  private boolean checkAllowed(int paramInt1, int paramInt2)
  {
    int i = this.mInterface.getTagInfo().get(paramInt2);
    if (i == 0) {
      return false;
    }
    return ExifInterface.isIfdAllowed(i, paramInt1);
  }
  
  private void checkOffsetOrImageTag(ExifTag paramExifTag)
  {
    if (paramExifTag.getComponentCount() == 0) {}
    int i;
    int j;
    label238:
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
                  return;
                  i = paramExifTag.getTagId();
                  j = paramExifTag.getIfd();
                  if ((i != TAG_EXIF_IFD) || (!checkAllowed(j, ExifInterface.TAG_EXIF_IFD))) {
                    break;
                  }
                } while ((!isIfdRequested(2)) && (!isIfdRequested(3)));
                registerIfd(2, paramExifTag.getValueAt(0));
                return;
                if ((i != TAG_GPS_IFD) || (!checkAllowed(j, ExifInterface.TAG_GPS_IFD))) {
                  break;
                }
              } while (!isIfdRequested(4));
              registerIfd(4, paramExifTag.getValueAt(0));
              return;
              if ((i != TAG_INTEROPERABILITY_IFD) || (!checkAllowed(j, ExifInterface.TAG_INTEROPERABILITY_IFD))) {
                break;
              }
            } while (!isIfdRequested(3));
            registerIfd(3, paramExifTag.getValueAt(0));
            return;
            if ((i != TAG_JPEG_INTERCHANGE_FORMAT) || (!checkAllowed(j, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT))) {
              break;
            }
          } while (!isThumbnailRequested());
          registerCompressedImage(paramExifTag.getValueAt(0));
          return;
          if ((i != TAG_JPEG_INTERCHANGE_FORMAT_LENGTH) || (!checkAllowed(j, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH))) {
            break;
          }
        } while (!isThumbnailRequested());
        this.mJpegSizeTag = paramExifTag;
        return;
        if ((i != TAG_STRIP_OFFSETS) || (!checkAllowed(j, ExifInterface.TAG_STRIP_OFFSETS))) {
          break;
        }
      } while (!isThumbnailRequested());
      if (paramExifTag.hasValue())
      {
        int k = 0;
        if (k < paramExifTag.getComponentCount())
        {
          if (paramExifTag.getDataType() != 3) {
            break label273;
          }
          registerUncompressedStrip(k, paramExifTag.getValueAt(k));
        }
        for (;;)
        {
          k++;
          break label238;
          break;
          registerUncompressedStrip(k, paramExifTag.getValueAt(k));
        }
      }
      this.mCorrespondingEvent.put(Integer.valueOf(paramExifTag.getOffset()), new ExifTagEvent(paramExifTag, false));
      return;
    } while ((i != TAG_STRIP_BYTE_COUNTS) || (!checkAllowed(j, ExifInterface.TAG_STRIP_BYTE_COUNTS)) || (!isThumbnailRequested()) || (!paramExifTag.hasValue()));
    label273:
    this.mStripSizeTag = paramExifTag;
  }
  
  private boolean isIfdRequested(int paramInt)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default: 
      bool = false;
    }
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
              return bool;
            } while ((0x1 & this.mOptions) != 0);
            return false;
          } while ((0x2 & this.mOptions) != 0);
          return false;
        } while ((0x4 & this.mOptions) != 0);
        return false;
      } while ((0x8 & this.mOptions) != 0);
      return false;
    } while ((0x10 & this.mOptions) != 0);
    return false;
  }
  
  private boolean isThumbnailRequested()
  {
    return (0x20 & this.mOptions) != 0;
  }
  
  private boolean needToParseOffsetsInCurrentIfd()
  {
    switch (this.mIfdType)
    {
    default: 
    case 0: 
      do
      {
        return false;
      } while ((!isIfdRequested(2)) && (!isIfdRequested(4)) && (!isIfdRequested(3)) && (!isIfdRequested(1)));
      return true;
    case 1: 
      return isThumbnailRequested();
    }
    return isIfdRequested(3);
  }
  
  protected static ExifParser parse(InputStream paramInputStream, ExifInterface paramExifInterface)
    throws IOException, ExifInvalidFormatException
  {
    return new ExifParser(paramInputStream, 63, paramExifInterface);
  }
  
  private void parseTiffHeader()
    throws IOException, ExifInvalidFormatException
  {
    int i = this.mTiffStream.readShort();
    if (18761 == i) {
      this.mTiffStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    while (this.mTiffStream.readShort() != 42)
    {
      throw new ExifInvalidFormatException("Invalid TIFF header");
      if (19789 == i) {
        this.mTiffStream.setByteOrder(ByteOrder.BIG_ENDIAN);
      } else {
        throw new ExifInvalidFormatException("Invalid TIFF header");
      }
    }
  }
  
  private ExifTag readTag()
    throws IOException, ExifInvalidFormatException
  {
    short s1 = this.mTiffStream.readShort();
    short s2 = this.mTiffStream.readShort();
    long l1 = this.mTiffStream.readUnsignedInt();
    if (l1 > 2147483647L) {
      throw new ExifInvalidFormatException("Number of component is larger then Integer.MAX_VALUE");
    }
    if (!ExifTag.isValidType(s2))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Short.valueOf(s1);
      arrayOfObject[1] = Short.valueOf(s2);
      Log.w("ExifParser", String.format("Tag %04x: Invalid data type %d", arrayOfObject));
      this.mTiffStream.skip(4L);
      return null;
    }
    int i = (int)l1;
    int j = this.mIfdType;
    if ((int)l1 != 0) {}
    ExifTag localExifTag;
    int k;
    long l2;
    for (boolean bool1 = true;; bool1 = false)
    {
      localExifTag = new ExifTag(s1, s2, i, j, bool1);
      k = localExifTag.getDataSize();
      if (k <= 4) {
        break label246;
      }
      l2 = this.mTiffStream.readUnsignedInt();
      if (l2 <= 2147483647L) {
        break;
      }
      throw new ExifInvalidFormatException("offset is larger then Integer.MAX_VALUE");
    }
    if ((l2 < this.mIfd0Position) && (s2 == 7))
    {
      byte[] arrayOfByte = new byte[(int)l1];
      System.arraycopy(this.mDataAboveIfd0, -8 + (int)l2, arrayOfByte, 0, (int)l1);
      localExifTag.setValue(arrayOfByte);
      return localExifTag;
    }
    localExifTag.setOffset((int)l2);
    return localExifTag;
    label246:
    boolean bool2 = localExifTag.hasDefinedCount();
    localExifTag.setHasDefinedCount(false);
    readFullTagValue(localExifTag);
    localExifTag.setHasDefinedCount(bool2);
    this.mTiffStream.skip(4 - k);
    localExifTag.setOffset(-4 + this.mTiffStream.getReadByteCount());
    return localExifTag;
  }
  
  private void registerCompressedImage(long paramLong)
  {
    this.mCorrespondingEvent.put(Integer.valueOf((int)paramLong), new ImageEvent(3));
  }
  
  private void registerIfd(int paramInt, long paramLong)
  {
    this.mCorrespondingEvent.put(Integer.valueOf((int)paramLong), new IfdEvent(paramInt, isIfdRequested(paramInt)));
  }
  
  private void registerUncompressedStrip(int paramInt, long paramLong)
  {
    this.mCorrespondingEvent.put(Integer.valueOf((int)paramLong), new ImageEvent(4, paramInt));
  }
  
  private boolean seekTiffData(InputStream paramInputStream)
    throws IOException, ExifInvalidFormatException
  {
    CountedDataInputStream localCountedDataInputStream = new CountedDataInputStream(paramInputStream);
    if (localCountedDataInputStream.readShort() != -40) {
      throw new ExifInvalidFormatException("Invalid JPEG format");
    }
    for (short s = localCountedDataInputStream.readShort();; s = localCountedDataInputStream.readShort())
    {
      boolean bool1 = false;
      int i;
      if (s != -39)
      {
        boolean bool2 = JpegHeader.isSofMarker(s);
        bool1 = false;
        if (!bool2)
        {
          i = localCountedDataInputStream.readUnsignedShort();
          if ((s != -31) || (i < 8)) {
            break label137;
          }
          int j = localCountedDataInputStream.readInt();
          int k = localCountedDataInputStream.readShort();
          i -= 6;
          if ((j != 1165519206) || (k != 0)) {
            break label137;
          }
          this.mTiffStartPosition = localCountedDataInputStream.getReadByteCount();
          this.mApp1End = i;
          this.mOffsetToApp1EndFromSOF = (this.mTiffStartPosition + this.mApp1End);
          bool1 = true;
        }
      }
      return bool1;
      label137:
      if ((i < 2) || (i - 2 != localCountedDataInputStream.skip(i - 2)))
      {
        Log.w("ExifParser", "Invalid JPEG format.");
        return false;
      }
    }
  }
  
  private void skipTo(int paramInt)
    throws IOException
  {
    this.mTiffStream.skipTo(paramInt);
    while ((!this.mCorrespondingEvent.isEmpty()) && (((Integer)this.mCorrespondingEvent.firstKey()).intValue() < paramInt)) {
      this.mCorrespondingEvent.pollFirstEntry();
    }
  }
  
  protected ByteOrder getByteOrder()
  {
    return this.mTiffStream.getByteOrder();
  }
  
  protected int getCompressedImageSize()
  {
    if (this.mJpegSizeTag == null) {
      return 0;
    }
    return (int)this.mJpegSizeTag.getValueAt(0);
  }
  
  protected int getCurrentIfd()
  {
    return this.mIfdType;
  }
  
  protected int getStripIndex()
  {
    return this.mImageEvent.stripIndex;
  }
  
  protected int getStripSize()
  {
    if (this.mStripSizeTag == null) {
      return 0;
    }
    return (int)this.mStripSizeTag.getValueAt(0);
  }
  
  protected ExifTag getTag()
  {
    return this.mTag;
  }
  
  protected int next()
    throws IOException, ExifInvalidFormatException
  {
    int i = 1;
    if (!this.mContainExifData) {
      i = 5;
    }
    int j;
    int k;
    do
    {
      return i;
      j = this.mTiffStream.getReadByteCount();
      k = 2 + this.mIfdStartOffset + 12 * this.mNumOfTagInIfd;
      if (j >= k) {
        break;
      }
      this.mTag = readTag();
      if (this.mTag == null) {
        return next();
      }
    } while (!this.mNeedToParseOffsetsInCurrentIfd);
    checkOffsetOrImageTag(this.mTag);
    return i;
    if (j == k)
    {
      if (this.mIfdType != 0) {
        break label271;
      }
      long l2 = readUnsignedLong();
      if (((isIfdRequested(i)) || (isThumbnailRequested())) && (l2 != 0L)) {
        registerIfd(i, l2);
      }
    }
    while (this.mCorrespondingEvent.size() != 0)
    {
      Map.Entry localEntry = this.mCorrespondingEvent.pollFirstEntry();
      Object localObject = localEntry.getValue();
      try
      {
        skipTo(((Integer)localEntry.getKey()).intValue());
        if (!(localObject instanceof IfdEvent)) {
          break label482;
        }
        this.mIfdType = ((IfdEvent)localObject).ifd;
        this.mNumOfTagInIfd = this.mTiffStream.readUnsignedShort();
        this.mIfdStartOffset = ((Integer)localEntry.getKey()).intValue();
        if (2 + (12 * this.mNumOfTagInIfd + this.mIfdStartOffset) <= this.mApp1End) {
          break label454;
        }
        Log.w("ExifParser", "Invalid size of IFD " + this.mIfdType);
        return 5;
      }
      catch (IOException localIOException)
      {
        label271:
        int m;
        long l1;
        Log.w("ExifParser", "Failed to skip to data at: " + localEntry.getKey() + " for " + localObject.getClass().getName() + ", the file may be broken.");
      }
      m = 4;
      if (this.mCorrespondingEvent.size() > 0) {
        m = ((Integer)this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
      }
      if (m < 4)
      {
        Log.w("ExifParser", "Invalid size of link to next IFD: " + m);
      }
      else
      {
        l1 = readUnsignedLong();
        if (l1 != 0L)
        {
          Log.w("ExifParser", "Invalid link to next IFD: " + l1);
          continue;
          continue;
          label454:
          this.mNeedToParseOffsetsInCurrentIfd = needToParseOffsetsInCurrentIfd();
          if (((IfdEvent)localObject).isRequested) {
            return 0;
          }
          skipRemainingTagsInCurrentIfd();
          continue;
          label482:
          if ((localObject instanceof ImageEvent))
          {
            this.mImageEvent = ((ImageEvent)localObject);
            return this.mImageEvent.type;
          }
          ExifTagEvent localExifTagEvent = (ExifTagEvent)localObject;
          this.mTag = localExifTagEvent.tag;
          if (this.mTag.getDataType() != 7)
          {
            readFullTagValue(this.mTag);
            checkOffsetOrImageTag(this.mTag);
          }
          if (localExifTagEvent.isRequested) {
            return 2;
          }
        }
      }
    }
    return 5;
  }
  
  protected int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return this.mTiffStream.read(paramArrayOfByte);
  }
  
  protected void readFullTagValue(ExifTag paramExifTag)
    throws IOException
  {
    int i = paramExifTag.getDataType();
    Object localObject;
    if ((i == 2) || (i == 7) || (i == 1))
    {
      int j = paramExifTag.getComponentCount();
      if ((this.mCorrespondingEvent.size() > 0) && (((Integer)this.mCorrespondingEvent.firstEntry().getKey()).intValue() < j + this.mTiffStream.getReadByteCount()))
      {
        localObject = this.mCorrespondingEvent.firstEntry().getValue();
        if (!(localObject instanceof ImageEvent)) {
          break label221;
        }
        Log.w("ExifParser", "Thumbnail overlaps value for tag: \n" + paramExifTag.toString());
        Map.Entry localEntry = this.mCorrespondingEvent.pollFirstEntry();
        Log.w("ExifParser", "Invalid thumbnail offset: " + localEntry.getKey());
      }
    }
    switch (paramExifTag.getDataType())
    {
    case 6: 
    case 8: 
    default: 
      return;
      if ((localObject instanceof IfdEvent)) {
        Log.w("ExifParser", "Ifd " + ((IfdEvent)localObject).ifd + " overlaps value for tag: \n" + paramExifTag.toString());
      }
      for (;;)
      {
        int i8 = ((Integer)this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
        Log.w("ExifParser", "Invalid size of tag: \n" + paramExifTag.toString() + " setting count to: " + i8);
        paramExifTag.forceSetComponentCount(i8);
        break;
        if ((localObject instanceof ExifTagEvent)) {
          Log.w("ExifParser", "Tag value for tag: \n" + ((ExifTagEvent)localObject).tag.toString() + " overlaps value for tag: \n" + paramExifTag.toString());
        }
      }
    case 1: 
    case 7: 
      byte[] arrayOfByte = new byte[paramExifTag.getComponentCount()];
      read(arrayOfByte);
      paramExifTag.setValue(arrayOfByte);
      return;
    case 2: 
      paramExifTag.setValue(readString(paramExifTag.getComponentCount()));
      return;
    case 4: 
      long[] arrayOfLong = new long[paramExifTag.getComponentCount()];
      int i6 = 0;
      int i7 = arrayOfLong.length;
      while (i6 < i7)
      {
        arrayOfLong[i6] = readUnsignedLong();
        i6++;
      }
      paramExifTag.setValue(arrayOfLong);
      return;
    case 5: 
      Rational[] arrayOfRational2 = new Rational[paramExifTag.getComponentCount()];
      int i4 = 0;
      int i5 = arrayOfRational2.length;
      while (i4 < i5)
      {
        arrayOfRational2[i4] = readUnsignedRational();
        i4++;
      }
      paramExifTag.setValue(arrayOfRational2);
      return;
    case 3: 
      int[] arrayOfInt2 = new int[paramExifTag.getComponentCount()];
      int i2 = 0;
      int i3 = arrayOfInt2.length;
      while (i2 < i3)
      {
        arrayOfInt2[i2] = readUnsignedShort();
        i2++;
      }
      paramExifTag.setValue(arrayOfInt2);
      return;
    case 9: 
      label221:
      int[] arrayOfInt1 = new int[paramExifTag.getComponentCount()];
      int n = 0;
      int i1 = arrayOfInt1.length;
      while (n < i1)
      {
        arrayOfInt1[n] = readLong();
        n++;
      }
      paramExifTag.setValue(arrayOfInt1);
      return;
    }
    Rational[] arrayOfRational1 = new Rational[paramExifTag.getComponentCount()];
    int k = 0;
    int m = arrayOfRational1.length;
    while (k < m)
    {
      arrayOfRational1[k] = readRational();
      k++;
    }
    paramExifTag.setValue(arrayOfRational1);
  }
  
  protected int readLong()
    throws IOException
  {
    return this.mTiffStream.readInt();
  }
  
  protected Rational readRational()
    throws IOException
  {
    int i = readLong();
    int j = readLong();
    return new Rational(i, j);
  }
  
  protected String readString(int paramInt)
    throws IOException
  {
    return readString(paramInt, US_ASCII);
  }
  
  protected String readString(int paramInt, Charset paramCharset)
    throws IOException
  {
    if (paramInt > 0) {
      return this.mTiffStream.readString(paramInt, paramCharset);
    }
    return "";
  }
  
  protected long readUnsignedLong()
    throws IOException
  {
    return 0xFFFFFFFF & readLong();
  }
  
  protected Rational readUnsignedRational()
    throws IOException
  {
    return new Rational(readUnsignedLong(), readUnsignedLong());
  }
  
  protected int readUnsignedShort()
    throws IOException
  {
    return 0xFFFF & this.mTiffStream.readShort();
  }
  
  protected void registerForTagValue(ExifTag paramExifTag)
  {
    if (paramExifTag.getOffset() >= this.mTiffStream.getReadByteCount()) {
      this.mCorrespondingEvent.put(Integer.valueOf(paramExifTag.getOffset()), new ExifTagEvent(paramExifTag, true));
    }
  }
  
  protected void skipRemainingTagsInCurrentIfd()
    throws IOException, ExifInvalidFormatException
  {
    int i = 2 + this.mIfdStartOffset + 12 * this.mNumOfTagInIfd;
    int j = this.mTiffStream.getReadByteCount();
    if (j > i) {}
    long l;
    do
    {
      return;
      if (this.mNeedToParseOffsetsInCurrentIfd) {
        while (j < i)
        {
          this.mTag = readTag();
          j += 12;
          if (this.mTag != null) {
            checkOffsetOrImageTag(this.mTag);
          }
        }
      }
      skipTo(i);
      l = readUnsignedLong();
    } while ((this.mIfdType != 0) || ((!isIfdRequested(1)) && (!isThumbnailRequested())) || (l <= 0L));
    registerIfd(1, l);
  }
  
  private static class ExifTagEvent
  {
    boolean isRequested;
    ExifTag tag;
    
    ExifTagEvent(ExifTag paramExifTag, boolean paramBoolean)
    {
      this.tag = paramExifTag;
      this.isRequested = paramBoolean;
    }
  }
  
  private static class IfdEvent
  {
    int ifd;
    boolean isRequested;
    
    IfdEvent(int paramInt, boolean paramBoolean)
    {
      this.ifd = paramInt;
      this.isRequested = paramBoolean;
    }
  }
  
  private static class ImageEvent
  {
    int stripIndex;
    int type;
    
    ImageEvent(int paramInt)
    {
      this.stripIndex = 0;
      this.type = paramInt;
    }
    
    ImageEvent(int paramInt1, int paramInt2)
    {
      this.type = paramInt1;
      this.stripIndex = paramInt2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.ExifParser
 * JD-Core Version:    0.7.0.1
 */