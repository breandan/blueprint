package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.Dispatcher;
import com.squareup.okhttp.internal.http.HttpAuthenticator;
import com.squareup.okhttp.internal.http.HttpURLConnectionImpl;
import com.squareup.okhttp.internal.http.HttpsURLConnectionImpl;
import com.squareup.okhttp.internal.http.OkResponseCacheAdapter;
import com.squareup.okhttp.internal.tls.OkHostnameVerifier;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.ResponseCache;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public final class OkHttpClient
  implements URLStreamHandlerFactory
{
  private static final List<String> DEFAULT_TRANSPORTS = Util.immutableList(Arrays.asList(new String[] { "spdy/3", "http/1.1" }));
  private OkAuthenticator authenticator;
  private int connectTimeout;
  private ConnectionPool connectionPool;
  private CookieHandler cookieHandler;
  private final Dispatcher dispatcher;
  private boolean followProtocolRedirects = true;
  private HostnameVerifier hostnameVerifier;
  private Proxy proxy;
  private ProxySelector proxySelector;
  private int readTimeout;
  private ResponseCache responseCache;
  private final RouteDatabase routeDatabase;
  private SSLSocketFactory sslSocketFactory;
  private List<String> transports;
  
  public OkHttpClient()
  {
    this.routeDatabase = new RouteDatabase();
    this.dispatcher = new Dispatcher();
  }
  
  private OkHttpClient(OkHttpClient paramOkHttpClient)
  {
    this.routeDatabase = paramOkHttpClient.routeDatabase;
    this.dispatcher = paramOkHttpClient.dispatcher;
  }
  
  private OkHttpClient copyWithDefaults()
  {
    OkHttpClient localOkHttpClient = new OkHttpClient(this);
    localOkHttpClient.proxy = this.proxy;
    ProxySelector localProxySelector;
    CookieHandler localCookieHandler;
    label46:
    ResponseCache localResponseCache;
    label64:
    SSLSocketFactory localSSLSocketFactory;
    label83:
    Object localObject;
    label102:
    OkAuthenticator localOkAuthenticator;
    label121:
    ConnectionPool localConnectionPool;
    if (this.proxySelector != null)
    {
      localProxySelector = this.proxySelector;
      localOkHttpClient.proxySelector = localProxySelector;
      if (this.cookieHandler == null) {
        break label198;
      }
      localCookieHandler = this.cookieHandler;
      localOkHttpClient.cookieHandler = localCookieHandler;
      if (this.responseCache == null) {
        break label205;
      }
      localResponseCache = this.responseCache;
      localOkHttpClient.responseCache = localResponseCache;
      if (this.sslSocketFactory == null) {
        break label213;
      }
      localSSLSocketFactory = this.sslSocketFactory;
      localOkHttpClient.sslSocketFactory = localSSLSocketFactory;
      if (this.hostnameVerifier == null) {
        break label221;
      }
      localObject = this.hostnameVerifier;
      localOkHttpClient.hostnameVerifier = ((HostnameVerifier)localObject);
      if (this.authenticator == null) {
        break label229;
      }
      localOkAuthenticator = this.authenticator;
      localOkHttpClient.authenticator = localOkAuthenticator;
      if (this.connectionPool == null) {
        break label237;
      }
      localConnectionPool = this.connectionPool;
      label140:
      localOkHttpClient.connectionPool = localConnectionPool;
      localOkHttpClient.followProtocolRedirects = this.followProtocolRedirects;
      if (this.transports == null) {
        break label245;
      }
    }
    label198:
    label205:
    label213:
    label221:
    label229:
    label237:
    label245:
    for (List localList = this.transports;; localList = DEFAULT_TRANSPORTS)
    {
      localOkHttpClient.transports = localList;
      localOkHttpClient.connectTimeout = this.connectTimeout;
      localOkHttpClient.readTimeout = this.readTimeout;
      return localOkHttpClient;
      localProxySelector = ProxySelector.getDefault();
      break;
      localCookieHandler = CookieHandler.getDefault();
      break label46;
      localResponseCache = ResponseCache.getDefault();
      break label64;
      localSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
      break label83;
      localObject = OkHostnameVerifier.INSTANCE;
      break label102;
      localOkAuthenticator = HttpAuthenticator.SYSTEM_DEFAULT;
      break label121;
      localConnectionPool = ConnectionPool.getDefault();
      break label140;
    }
  }
  
  public URLStreamHandler createURLStreamHandler(final String paramString)
  {
    if ((!paramString.equals("http")) && (!paramString.equals("https"))) {
      return null;
    }
    new URLStreamHandler()
    {
      protected int getDefaultPort()
      {
        if (paramString.equals("http")) {
          return 80;
        }
        if (paramString.equals("https")) {
          return 443;
        }
        throw new AssertionError();
      }
      
      protected URLConnection openConnection(URL paramAnonymousURL)
      {
        return OkHttpClient.this.open(paramAnonymousURL);
      }
      
      protected URLConnection openConnection(URL paramAnonymousURL, Proxy paramAnonymousProxy)
      {
        return OkHttpClient.this.open(paramAnonymousURL, paramAnonymousProxy);
      }
    };
  }
  
  public OkAuthenticator getAuthenticator()
  {
    return this.authenticator;
  }
  
  public int getConnectTimeout()
  {
    return this.connectTimeout;
  }
  
  public ConnectionPool getConnectionPool()
  {
    return this.connectionPool;
  }
  
  public CookieHandler getCookieHandler()
  {
    return this.cookieHandler;
  }
  
  public boolean getFollowProtocolRedirects()
  {
    return this.followProtocolRedirects;
  }
  
  public HostnameVerifier getHostnameVerifier()
  {
    return this.hostnameVerifier;
  }
  
  public OkResponseCache getOkResponseCache()
  {
    if ((this.responseCache instanceof HttpResponseCache)) {
      return ((HttpResponseCache)this.responseCache).okResponseCache;
    }
    if (this.responseCache != null) {
      return new OkResponseCacheAdapter(this.responseCache);
    }
    return null;
  }
  
  public Proxy getProxy()
  {
    return this.proxy;
  }
  
  public ProxySelector getProxySelector()
  {
    return this.proxySelector;
  }
  
  public int getReadTimeout()
  {
    return this.readTimeout;
  }
  
  public ResponseCache getResponseCache()
  {
    return this.responseCache;
  }
  
  public RouteDatabase getRoutesDatabase()
  {
    return this.routeDatabase;
  }
  
  public SSLSocketFactory getSslSocketFactory()
  {
    return this.sslSocketFactory;
  }
  
  public List<String> getTransports()
  {
    return this.transports;
  }
  
  public HttpURLConnection open(URL paramURL)
  {
    return open(paramURL, this.proxy);
  }
  
  HttpURLConnection open(URL paramURL, Proxy paramProxy)
  {
    String str = paramURL.getProtocol();
    OkHttpClient localOkHttpClient = copyWithDefaults();
    localOkHttpClient.proxy = paramProxy;
    if (str.equals("http")) {
      return new HttpURLConnectionImpl(paramURL, localOkHttpClient);
    }
    if (str.equals("https")) {
      return new HttpsURLConnectionImpl(paramURL, localOkHttpClient);
    }
    throw new IllegalArgumentException("Unexpected protocol: " + str);
  }
  
  public void setConnectTimeout(long paramLong, TimeUnit paramTimeUnit)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("timeout < 0");
    }
    if (paramTimeUnit == null) {
      throw new IllegalArgumentException("unit == null");
    }
    long l = paramTimeUnit.toMillis(paramLong);
    if (l > 2147483647L) {
      throw new IllegalArgumentException("Timeout too large.");
    }
    this.connectTimeout = ((int)l);
  }
  
  public OkHttpClient setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
  {
    this.hostnameVerifier = paramHostnameVerifier;
    return this;
  }
  
  public void setReadTimeout(long paramLong, TimeUnit paramTimeUnit)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("timeout < 0");
    }
    if (paramTimeUnit == null) {
      throw new IllegalArgumentException("unit == null");
    }
    long l = paramTimeUnit.toMillis(paramLong);
    if (l > 2147483647L) {
      throw new IllegalArgumentException("Timeout too large.");
    }
    this.readTimeout = ((int)l);
  }
  
  public OkHttpClient setResponseCache(ResponseCache paramResponseCache)
  {
    this.responseCache = paramResponseCache;
    return this;
  }
  
  public OkHttpClient setSslSocketFactory(SSLSocketFactory paramSSLSocketFactory)
  {
    this.sslSocketFactory = paramSSLSocketFactory;
    return this;
  }
  
  public OkHttpClient setTransports(List<String> paramList)
  {
    List localList = Util.immutableList(paramList);
    if (!localList.contains("http/1.1")) {
      throw new IllegalArgumentException("transports doesn't contain http/1.1: " + localList);
    }
    if (localList.contains(null)) {
      throw new IllegalArgumentException("transports must not contain null");
    }
    if (localList.contains("")) {
      throw new IllegalArgumentException("transports contains an empty string");
    }
    this.transports = localList;
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.OkHttpClient
 * JD-Core Version:    0.7.0.1
 */