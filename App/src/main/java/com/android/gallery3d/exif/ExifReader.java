package com.android.gallery3d.exif;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

class ExifReader
{
  private final ExifInterface mInterface;
  
  ExifReader(ExifInterface paramExifInterface)
  {
    this.mInterface = paramExifInterface;
  }
  
  protected ExifData read(InputStream paramInputStream)
    throws ExifInvalidFormatException, IOException
  {
    ExifParser localExifParser = ExifParser.parse(paramInputStream, this.mInterface);
    ExifData localExifData = new ExifData(localExifParser.getByteOrder());
    int i = localExifParser.next();
    if (i != 5)
    {
      switch (i)
      {
      }
      for (;;)
      {
        i = localExifParser.next();
        break;
        localExifData.addIfdData(new IfdData(localExifParser.getCurrentIfd()));
        continue;
        ExifTag localExifTag2 = localExifParser.getTag();
        if (!localExifTag2.hasValue())
        {
          localExifParser.registerForTagValue(localExifTag2);
        }
        else
        {
          localExifData.getIfdData(localExifTag2.getIfd()).setTag(localExifTag2);
          continue;
          ExifTag localExifTag1 = localExifParser.getTag();
          if (localExifTag1.getDataType() == 7) {
            localExifParser.readFullTagValue(localExifTag1);
          }
          localExifData.getIfdData(localExifTag1.getIfd()).setTag(localExifTag1);
          continue;
          byte[] arrayOfByte2 = new byte[localExifParser.getCompressedImageSize()];
          if (arrayOfByte2.length == localExifParser.read(arrayOfByte2))
          {
            localExifData.setCompressedThumbnail(arrayOfByte2);
          }
          else
          {
            Log.w("ExifReader", "Failed to read the compressed thumbnail");
            continue;
            byte[] arrayOfByte1 = new byte[localExifParser.getStripSize()];
            if (arrayOfByte1.length == localExifParser.read(arrayOfByte1)) {
              localExifData.setStripBytes(localExifParser.getStripIndex(), arrayOfByte1);
            } else {
              Log.w("ExifReader", "Failed to read the strip bytes");
            }
          }
        }
      }
    }
    return localExifData;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.ExifReader
 * JD-Core Version:    0.7.0.1
 */