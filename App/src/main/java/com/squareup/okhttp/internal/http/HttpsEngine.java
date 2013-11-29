package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.TunnelRequest;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.CacheResponse;
import java.net.SecureCacheResponse;
import java.net.URL;
import javax.net.ssl.SSLSocket;

public final class HttpsEngine
  extends HttpEngine
{
  private SSLSocket sslSocket;
  
  public HttpsEngine(OkHttpClient paramOkHttpClient, Policy paramPolicy, String paramString, RawHeaders paramRawHeaders, Connection paramConnection, RetryableOutputStream paramRetryableOutputStream)
    throws IOException
  {
    super(paramOkHttpClient, paramPolicy, paramString, paramRawHeaders, paramConnection, paramRetryableOutputStream);
    if (paramConnection != null) {}
    for (SSLSocket localSSLSocket = (SSLSocket)paramConnection.getSocket();; localSSLSocket = null)
    {
      this.sslSocket = localSSLSocket;
      return;
    }
  }
  
  protected boolean acceptCacheResponseType(CacheResponse paramCacheResponse)
  {
    return paramCacheResponse instanceof SecureCacheResponse;
  }
  
  protected void connected(Connection paramConnection)
  {
    this.sslSocket = ((SSLSocket)paramConnection.getSocket());
  }
  
  public SSLSocket getSslSocket()
  {
    return this.sslSocket;
  }
  
  protected TunnelRequest getTunnelConfig()
  {
    String str = this.requestHeaders.getUserAgent();
    if (str == null) {
      str = getDefaultUserAgent();
    }
    URL localURL = this.policy.getURL();
    return new TunnelRequest(localURL.getHost(), Util.getEffectivePort(localURL), str, this.requestHeaders.getProxyAuthorization());
  }
  
  protected boolean includeAuthorityInRequestLine()
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.HttpsEngine
 * JD-Core Version:    0.7.0.1
 */