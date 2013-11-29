package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.spdy.SpdyStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.URI;
import java.net.URL;

public final class SpdyTransport
  implements Transport
{
  private final HttpEngine httpEngine;
  private final SpdyConnection spdyConnection;
  private SpdyStream stream;
  
  public SpdyTransport(HttpEngine paramHttpEngine, SpdyConnection paramSpdyConnection)
  {
    this.httpEngine = paramHttpEngine;
    this.spdyConnection = paramSpdyConnection;
  }
  
  public OutputStream createRequestBody()
    throws IOException
  {
    writeRequestHeaders();
    return this.stream.getOutputStream();
  }
  
  public void flushRequest()
    throws IOException
  {
    this.stream.getOutputStream().close();
  }
  
  public InputStream getTransferStream(CacheRequest paramCacheRequest)
    throws IOException
  {
    return new UnknownLengthHttpInputStream(this.stream.getInputStream(), paramCacheRequest, this.httpEngine);
  }
  
  public boolean makeReusable(boolean paramBoolean, OutputStream paramOutputStream, InputStream paramInputStream)
  {
    if (paramBoolean)
    {
      if (this.stream != null) {
        this.stream.closeLater(5);
      }
    }
    else {
      return true;
    }
    return false;
  }
  
  public ResponseHeaders readResponseHeaders()
    throws IOException
  {
    RawHeaders localRawHeaders = RawHeaders.fromNameValueBlock(this.stream.getResponseHeaders());
    this.httpEngine.receiveHeaders(localRawHeaders);
    ResponseHeaders localResponseHeaders = new ResponseHeaders(this.httpEngine.uri, localRawHeaders);
    localResponseHeaders.setTransport("spdy/3");
    return localResponseHeaders;
  }
  
  public void writeRequestBody(RetryableOutputStream paramRetryableOutputStream)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  public void writeRequestHeaders()
    throws IOException
  {
    if (this.stream != null) {
      return;
    }
    this.httpEngine.writingRequestHeaders();
    RawHeaders localRawHeaders = this.httpEngine.requestHeaders.getHeaders();
    if (this.httpEngine.connection.getHttpMinorVersion() == 1) {}
    for (String str = "HTTP/1.1";; str = "HTTP/1.0")
    {
      URL localURL = this.httpEngine.policy.getURL();
      localRawHeaders.addSpdyRequestHeaders(this.httpEngine.method, HttpEngine.requestPath(localURL), str, HttpEngine.getOriginAddress(localURL), this.httpEngine.uri.getScheme());
      boolean bool = this.httpEngine.hasRequestBody();
      this.stream = this.spdyConnection.newStream(localRawHeaders.toNameValueBlock(), bool, true);
      this.stream.setReadTimeout(this.httpEngine.client.getReadTimeout());
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.SpdyTransport
 * JD-Core Version:    0.7.0.1
 */