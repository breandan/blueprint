package com.google.android.libraries.tvdetect.util;

public abstract interface HttpFetcher
{
  public abstract byte[] fetchUrl(String paramString)
    throws HttpFetcherException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.HttpFetcher
 * JD-Core Version:    0.7.0.1
 */