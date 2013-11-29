package com.google.android.libraries.tvdetect.util;

import java.io.IOException;

public class HttpFetcherException
  extends Exception
{
  public HttpFetcherException(String paramString)
  {
    super(paramString);
  }
  
  public HttpFetcherException(String paramString, IOException paramIOException)
  {
    super(paramString, paramIOException);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.HttpFetcherException
 * JD-Core Version:    0.7.0.1
 */