package com.google.android.search.core;

import android.content.ContentResolver;
import com.google.android.gsf.Gservices;

public class GserviceWrapper
{
  private final ContentResolver mContentResolver;
  
  public GserviceWrapper(ContentResolver paramContentResolver)
  {
    this.mContentResolver = paramContentResolver;
  }
  
  public String getString(String paramString)
  {
    return Gservices.getString(this.mContentResolver, paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GserviceWrapper
 * JD-Core Version:    0.7.0.1
 */