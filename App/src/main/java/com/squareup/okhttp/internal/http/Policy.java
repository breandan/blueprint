package com.squareup.okhttp.internal.http;

import java.net.HttpURLConnection;
import java.net.URL;

public abstract interface Policy
{
  public abstract int getChunkLength();
  
  public abstract long getFixedContentLength();
  
  public abstract HttpURLConnection getHttpConnectionToCache();
  
  public abstract long getIfModifiedSince();
  
  public abstract URL getURL();
  
  public abstract boolean getUseCaches();
  
  public abstract boolean usingProxy();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.Policy
 * JD-Core Version:    0.7.0.1
 */