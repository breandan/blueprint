package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketPermission;
import java.net.URL;
import java.security.Permission;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLHandshakeException;

public class HttpURLConnectionImpl
  extends HttpURLConnection
  implements Policy
{
  final OkHttpClient client;
  private long fixedContentLength = -1L;
  protected HttpEngine httpEngine;
  protected IOException httpEngineFailure;
  private final RawHeaders rawRequestHeaders = new RawHeaders();
  private int redirectionCount;
  
  public HttpURLConnectionImpl(URL paramURL, OkHttpClient paramOkHttpClient)
  {
    super(paramURL);
    this.client = paramOkHttpClient;
  }
  
  private boolean execute(boolean paramBoolean)
    throws IOException
  {
    try
    {
      this.httpEngine.sendRequest();
      if (paramBoolean) {
        this.httpEngine.readResponse();
      }
      return true;
    }
    catch (IOException localIOException)
    {
      if (handleFailure(localIOException)) {
        return false;
      }
      throw localIOException;
    }
  }
  
  private HttpEngine getResponse()
    throws IOException
  {
    initHttpEngine();
    if (this.httpEngine.hasResponse()) {
      return this.httpEngine;
    }
    for (;;)
    {
      if (execute(true))
      {
        Retry localRetry = processResponseHeaders();
        if (localRetry == Retry.NONE)
        {
          this.httpEngine.automaticallyReleaseConnectionToPool();
          return this.httpEngine;
        }
        String str = this.method;
        OutputStream localOutputStream = this.httpEngine.getRequestBody();
        int i = getResponseCode();
        if ((i == 300) || (i == 301) || (i == 302) || (i == 303))
        {
          str = "GET";
          localOutputStream = null;
        }
        if ((localOutputStream != null) && (!(localOutputStream instanceof RetryableOutputStream))) {
          throw new HttpRetryException("Cannot retry streamed HTTP body", this.httpEngine.getResponseCode());
        }
        if (localRetry == Retry.DIFFERENT_CONNECTION) {
          this.httpEngine.automaticallyReleaseConnectionToPool();
        }
        this.httpEngine.release(false);
        this.httpEngine = newHttpEngine(str, this.rawRequestHeaders, this.httpEngine.getConnection(), (RetryableOutputStream)localOutputStream);
      }
    }
  }
  
  private boolean handleFailure(IOException paramIOException)
    throws IOException
  {
    RouteSelector localRouteSelector = this.httpEngine.routeSelector;
    if ((localRouteSelector != null) && (this.httpEngine.connection != null)) {
      localRouteSelector.connectFailed(this.httpEngine.connection, paramIOException);
    }
    OutputStream localOutputStream = this.httpEngine.getRequestBody();
    if ((localOutputStream == null) || ((localOutputStream instanceof RetryableOutputStream))) {}
    for (int i = 1; ((localRouteSelector == null) && (this.httpEngine.connection == null)) || ((localRouteSelector != null) && (!localRouteSelector.hasNext())) || (!isRecoverable(paramIOException)) || (i == 0); i = 0)
    {
      this.httpEngineFailure = paramIOException;
      return false;
    }
    this.httpEngine.release(true);
    RetryableOutputStream localRetryableOutputStream = (RetryableOutputStream)localOutputStream;
    this.httpEngine = newHttpEngine(this.method, this.rawRequestHeaders, null, localRetryableOutputStream);
    this.httpEngine.routeSelector = localRouteSelector;
    return true;
  }
  
  private void initHttpEngine()
    throws IOException
  {
    if (this.httpEngineFailure != null) {
      throw this.httpEngineFailure;
    }
    if (this.httpEngine != null) {
      return;
    }
    this.connected = true;
    do
    {
      try
      {
        if (this.doOutput)
        {
          if (this.method.equals("GET")) {
            this.method = "POST";
          }
        }
        else
        {
          this.httpEngine = newHttpEngine(this.method, this.rawRequestHeaders, null, null);
          return;
        }
      }
      catch (IOException localIOException)
      {
        this.httpEngineFailure = localIOException;
        throw localIOException;
      }
    } while ((this.method.equals("POST")) || (this.method.equals("PUT")));
    throw new ProtocolException(this.method + " does not support writing");
  }
  
  private boolean isRecoverable(IOException paramIOException)
  {
    if (((paramIOException instanceof SSLHandshakeException)) && ((paramIOException.getCause() instanceof CertificateException))) {}
    for (int i = 1;; i = 0)
    {
      boolean bool = paramIOException instanceof ProtocolException;
      if ((i != 0) || (bool)) {
        break;
      }
      return true;
    }
    return false;
  }
  
  private HttpEngine newHttpEngine(String paramString, RawHeaders paramRawHeaders, Connection paramConnection, RetryableOutputStream paramRetryableOutputStream)
    throws IOException
  {
    if (this.url.getProtocol().equals("http")) {
      return new HttpEngine(this.client, this, paramString, paramRawHeaders, paramConnection, paramRetryableOutputStream);
    }
    if (this.url.getProtocol().equals("https")) {
      return new HttpsEngine(this.client, this, paramString, paramRawHeaders, paramConnection, paramRetryableOutputStream);
    }
    throw new AssertionError();
  }
  
  private Retry processResponseHeaders()
    throws IOException
  {
    if (this.httpEngine.connection != null) {}
    int i;
    for (Proxy localProxy = this.httpEngine.connection.getRoute().getProxy();; localProxy = this.client.getProxy())
    {
      i = getResponseCode();
      switch (i)
      {
      default: 
        return Retry.NONE;
      }
    }
    if (localProxy.type() != Proxy.Type.HTTP) {
      throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
    }
    if (HttpAuthenticator.processAuthHeader(this.client.getAuthenticator(), getResponseCode(), this.httpEngine.getResponseHeaders().getHeaders(), this.rawRequestHeaders, localProxy, this.url)) {
      return Retry.SAME_CONNECTION;
    }
    return Retry.NONE;
    if (!getInstanceFollowRedirects()) {
      return Retry.NONE;
    }
    int j = 1 + this.redirectionCount;
    this.redirectionCount = j;
    if (j > 20) {
      throw new ProtocolException("Too many redirects: " + this.redirectionCount);
    }
    if ((i == 307) && (!this.method.equals("GET")) && (!this.method.equals("HEAD"))) {
      return Retry.NONE;
    }
    String str = getHeaderField("Location");
    if (str == null) {
      return Retry.NONE;
    }
    URL localURL = this.url;
    this.url = new URL(localURL, str);
    if ((!this.url.getProtocol().equals("https")) && (!this.url.getProtocol().equals("http"))) {
      return Retry.NONE;
    }
    boolean bool1 = localURL.getProtocol().equals(this.url.getProtocol());
    if ((!bool1) && (!this.client.getFollowProtocolRedirects())) {
      return Retry.NONE;
    }
    boolean bool2 = localURL.getHost().equals(this.url.getHost());
    if (Util.getEffectivePort(localURL) == Util.getEffectivePort(this.url)) {}
    for (int k = 1; (bool2) && (k != 0) && (bool1); k = 0) {
      return Retry.SAME_CONNECTION;
    }
    return Retry.DIFFERENT_CONNECTION;
  }
  
  private void setTransports(String paramString, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramBoolean) {
      localArrayList.addAll(this.client.getTransports());
    }
    String[] arrayOfString = paramString.split(",", -1);
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(arrayOfString[j]);
    }
    this.client.setTransports(localArrayList);
  }
  
  public final void addRequestProperty(String paramString1, String paramString2)
  {
    if (this.connected) {
      throw new IllegalStateException("Cannot add request property after connection is made");
    }
    if (paramString1 == null) {
      throw new NullPointerException("field == null");
    }
    if (paramString2 == null)
    {
      Platform.get().logW("Ignoring header " + paramString1 + " because its value was null.");
      return;
    }
    if ("X-Android-Transports".equals(paramString1))
    {
      setTransports(paramString2, true);
      return;
    }
    this.rawRequestHeaders.add(paramString1, paramString2);
  }
  
  public final void connect()
    throws IOException
  {
    initHttpEngine();
    while (!execute(false)) {}
  }
  
  public final void disconnect()
  {
    if (this.httpEngine != null)
    {
      if (this.httpEngine.hasResponse()) {
        Util.closeQuietly(this.httpEngine.getResponseBody());
      }
      this.httpEngine.release(true);
    }
  }
  
  public final int getChunkLength()
  {
    return this.chunkLength;
  }
  
  public int getConnectTimeout()
  {
    return this.client.getConnectTimeout();
  }
  
  public final InputStream getErrorStream()
  {
    try
    {
      HttpEngine localHttpEngine = getResponse();
      boolean bool = localHttpEngine.hasResponseBody();
      Object localObject = null;
      if (bool)
      {
        int i = localHttpEngine.getResponseCode();
        localObject = null;
        if (i >= 400)
        {
          InputStream localInputStream = localHttpEngine.getResponseBody();
          localObject = localInputStream;
        }
      }
      return localObject;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  public final long getFixedContentLength()
  {
    return this.fixedContentLength;
  }
  
  public final String getHeaderField(int paramInt)
  {
    try
    {
      String str = getResponse().getResponseHeaders().getHeaders().getValue(paramInt);
      return str;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  public final String getHeaderField(String paramString)
  {
    try
    {
      RawHeaders localRawHeaders = getResponse().getResponseHeaders().getHeaders();
      if (paramString == null) {
        return localRawHeaders.getStatusLine();
      }
      String str = localRawHeaders.get(paramString);
      return str;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  public final String getHeaderFieldKey(int paramInt)
  {
    try
    {
      String str = getResponse().getResponseHeaders().getHeaders().getFieldName(paramInt);
      return str;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  public final Map<String, List<String>> getHeaderFields()
  {
    try
    {
      Map localMap = getResponse().getResponseHeaders().getHeaders().toMultimap(true);
      return localMap;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  public HttpURLConnection getHttpConnectionToCache()
  {
    return this;
  }
  
  public HttpEngine getHttpEngine()
  {
    return this.httpEngine;
  }
  
  public final InputStream getInputStream()
    throws IOException
  {
    if (!this.doInput) {
      throw new ProtocolException("This protocol does not support input");
    }
    HttpEngine localHttpEngine = getResponse();
    if (getResponseCode() >= 400) {
      throw new FileNotFoundException(this.url.toString());
    }
    InputStream localInputStream = localHttpEngine.getResponseBody();
    if (localInputStream == null) {
      throw new ProtocolException("No response body exists; responseCode=" + getResponseCode());
    }
    return localInputStream;
  }
  
  public final OutputStream getOutputStream()
    throws IOException
  {
    connect();
    OutputStream localOutputStream = this.httpEngine.getRequestBody();
    if (localOutputStream == null) {
      throw new ProtocolException("method does not support a request body: " + this.method);
    }
    if (this.httpEngine.hasResponse()) {
      throw new ProtocolException("cannot write request body after response has been read");
    }
    return localOutputStream;
  }
  
  public final Permission getPermission()
    throws IOException
  {
    String str = getURL().getHost();
    int i = Util.getEffectivePort(getURL());
    if (usingProxy())
    {
      InetSocketAddress localInetSocketAddress = (InetSocketAddress)this.client.getProxy().address();
      str = localInetSocketAddress.getHostName();
      i = localInetSocketAddress.getPort();
    }
    return new SocketPermission(str + ":" + i, "connect, resolve");
  }
  
  public int getReadTimeout()
  {
    return this.client.getReadTimeout();
  }
  
  public final Map<String, List<String>> getRequestProperties()
  {
    if (this.connected) {
      throw new IllegalStateException("Cannot access request header fields after connection is set");
    }
    return this.rawRequestHeaders.toMultimap(false);
  }
  
  public final String getRequestProperty(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return this.rawRequestHeaders.get(paramString);
  }
  
  public final int getResponseCode()
    throws IOException
  {
    return getResponse().getResponseCode();
  }
  
  public String getResponseMessage()
    throws IOException
  {
    return getResponse().getResponseHeaders().getHeaders().getResponseMessage();
  }
  
  public void setConnectTimeout(int paramInt)
  {
    this.client.setConnectTimeout(paramInt, TimeUnit.MILLISECONDS);
  }
  
  public void setFixedLengthStreamingMode(int paramInt)
  {
    setFixedLengthStreamingMode(paramInt);
  }
  
  public void setFixedLengthStreamingMode(long paramLong)
  {
    if (this.connected) {
      throw new IllegalStateException("Already connected");
    }
    if (this.chunkLength > 0) {
      throw new IllegalStateException("Already in chunked mode");
    }
    if (paramLong < 0L) {
      throw new IllegalArgumentException("contentLength < 0");
    }
    this.fixedContentLength = paramLong;
    this.fixedContentLength = ((int)Math.min(paramLong, 2147483647L));
  }
  
  public void setReadTimeout(int paramInt)
  {
    this.client.setReadTimeout(paramInt, TimeUnit.MILLISECONDS);
  }
  
  public final void setRequestProperty(String paramString1, String paramString2)
  {
    if (this.connected) {
      throw new IllegalStateException("Cannot set request property after connection is made");
    }
    if (paramString1 == null) {
      throw new NullPointerException("field == null");
    }
    if (paramString2 == null)
    {
      Platform.get().logW("Ignoring header " + paramString1 + " because its value was null.");
      return;
    }
    if ("X-Android-Transports".equals(paramString1))
    {
      setTransports(paramString2, false);
      return;
    }
    this.rawRequestHeaders.set(paramString1, paramString2);
  }
  
  public final boolean usingProxy()
  {
    Proxy localProxy = this.client.getProxy();
    return (localProxy != null) && (localProxy.type() != Proxy.Type.DIRECT);
  }
  
  static enum Retry
  {
    static
    {
      DIFFERENT_CONNECTION = new Retry("DIFFERENT_CONNECTION", 2);
      Retry[] arrayOfRetry = new Retry[3];
      arrayOfRetry[0] = NONE;
      arrayOfRetry[1] = SAME_CONNECTION;
      arrayOfRetry[2] = DIFFERENT_CONNECTION;
      $VALUES = arrayOfRetry;
    }
    
    private Retry() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.HttpURLConnectionImpl
 * JD-Core Version:    0.7.0.1
 */