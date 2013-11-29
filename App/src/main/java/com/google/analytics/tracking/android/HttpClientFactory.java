package com.google.analytics.tracking.android;

import org.apache.http.client.HttpClient;

abstract interface HttpClientFactory
{
  public abstract HttpClient newInstance();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.HttpClientFactory
 * JD-Core Version:    0.7.0.1
 */