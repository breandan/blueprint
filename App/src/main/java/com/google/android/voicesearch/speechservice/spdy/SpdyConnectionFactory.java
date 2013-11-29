package com.google.android.voicesearch.speechservice.spdy;

import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpResponseFetcher;
import com.google.android.speech.network.ConnectionFactory;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.HttpServerInfo;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.Nonnull;

public class SpdyConnectionFactory
  implements ConnectionFactory
{
  private final HttpHelper mHttpHelper;
  
  public SpdyConnectionFactory(HttpHelper paramHttpHelper)
  {
    this.mHttpHelper = paramHttpHelper;
  }
  
  public HttpURLConnection openHttpConnection(GstaticConfiguration.HttpServerInfo paramHttpServerInfo)
    throws IOException
  {
    return openHttpConnection(paramHttpServerInfo, new URL(paramHttpServerInfo.getUrl()));
  }
  
  public HttpURLConnection openHttpConnection(GstaticConfiguration.HttpServerInfo paramHttpServerInfo, URL paramURL)
    throws IOException
  {
    if (!"https".equals(paramURL.getProtocol())) {
      throw new UnsupportedOperationException("SpdyConnectionFactory only supports HTTPS connections");
    }
    HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(paramURL.toString());
    localGetRequest.setUseSpdy(true);
    localGetRequest.setUseCaches(false);
    HttpURLConnection localHttpURLConnection = (HttpURLConnection)this.mHttpHelper.get(localGetRequest, 14, new DeferredResponse());
    if (paramHttpServerInfo.hasChunkSize())
    {
      localHttpURLConnection.setChunkedStreamingMode(paramHttpServerInfo.getChunkSize());
      return localHttpURLConnection;
    }
    localHttpURLConnection.setChunkedStreamingMode(1024);
    return localHttpURLConnection;
  }
  
  public static class DeferredResponse
    extends HttpResponseFetcher<HttpURLConnection>
  {
    public HttpURLConnection fetchResponse(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
      throws IOException
    {
      return paramHttpURLConnection;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.speechservice.spdy.SpdyConnectionFactory
 * JD-Core Version:    0.7.0.1
 */