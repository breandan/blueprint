package com.google.android.speech.network;

import com.google.wireless.voicesearch.proto.GstaticConfiguration.HttpServerInfo;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract interface ConnectionFactory
{
  public abstract HttpURLConnection openHttpConnection(GstaticConfiguration.HttpServerInfo paramHttpServerInfo)
    throws IOException;
  
  public abstract HttpURLConnection openHttpConnection(GstaticConfiguration.HttpServerInfo paramHttpServerInfo, URL paramURL)
    throws IOException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.ConnectionFactory
 * JD-Core Version:    0.7.0.1
 */