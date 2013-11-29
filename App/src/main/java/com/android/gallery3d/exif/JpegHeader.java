package com.android.gallery3d.exif;

class JpegHeader
{
  public static final boolean isSofMarker(short paramShort)
  {
    return (paramShort >= -64) && (paramShort <= -49) && (paramShort != -60) && (paramShort != -56) && (paramShort != -52);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.JpegHeader
 * JD-Core Version:    0.7.0.1
 */