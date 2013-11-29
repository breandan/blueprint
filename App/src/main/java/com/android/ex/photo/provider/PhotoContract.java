package com.android.ex.photo.provider;

public final class PhotoContract
{
  public static abstract interface PhotoQuery
  {
    public static final String[] PROJECTION = { "uri", "_display_name", "contentUri", "thumbnailUri", "contentType" };
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.provider.PhotoContract
 * JD-Core Version:    0.7.0.1
 */