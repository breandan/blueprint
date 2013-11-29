package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class HttpsURLConnectionImpl
  extends HttpsURLConnection
{
  private final HttpUrlConnectionDelegate delegate;
  
  public HttpsURLConnectionImpl(URL paramURL, OkHttpClient paramOkHttpClient)
  {
    super(paramURL);
    this.delegate = new HttpUrlConnectionDelegate(paramURL, paramOkHttpClient, null);
  }
  
  private SSLSocket getSslSocket()
  {
    if ((this.delegate.httpEngine == null) || (this.delegate.httpEngine.sentRequestMillis == -1L)) {
      throw new IllegalStateException("Connection has not yet been established");
    }
    if ((this.delegate.httpEngine instanceof HttpsEngine)) {
      return ((HttpsEngine)this.delegate.httpEngine).getSslSocket();
    }
    return null;
  }
  
  public void addRequestProperty(String paramString1, String paramString2)
  {
    this.delegate.addRequestProperty(paramString1, paramString2);
  }
  
  public void connect()
    throws IOException
  {
    this.connected = true;
    this.delegate.connect();
  }
  
  public void disconnect()
  {
    this.delegate.disconnect();
  }
  
  public boolean getAllowUserInteraction()
  {
    return this.delegate.getAllowUserInteraction();
  }
  
  public String getCipherSuite()
  {
    SecureCacheResponse localSecureCacheResponse = this.delegate.getSecureCacheResponse();
    if (localSecureCacheResponse != null) {
      return localSecureCacheResponse.getCipherSuite();
    }
    SSLSocket localSSLSocket = getSslSocket();
    if (localSSLSocket != null) {
      return localSSLSocket.getSession().getCipherSuite();
    }
    return null;
  }
  
  public int getConnectTimeout()
  {
    return this.delegate.getConnectTimeout();
  }
  
  public Object getContent()
    throws IOException
  {
    return this.delegate.getContent();
  }
  
  public Object getContent(Class[] paramArrayOfClass)
    throws IOException
  {
    return this.delegate.getContent(paramArrayOfClass);
  }
  
  public String getContentEncoding()
  {
    return this.delegate.getContentEncoding();
  }
  
  public int getContentLength()
  {
    return this.delegate.getContentLength();
  }
  
  public String getContentType()
  {
    return this.delegate.getContentType();
  }
  
  public long getDate()
  {
    return this.delegate.getDate();
  }
  
  public boolean getDefaultUseCaches()
  {
    return this.delegate.getDefaultUseCaches();
  }
  
  public boolean getDoInput()
  {
    return this.delegate.getDoInput();
  }
  
  public boolean getDoOutput()
  {
    return this.delegate.getDoOutput();
  }
  
  public InputStream getErrorStream()
  {
    return this.delegate.getErrorStream();
  }
  
  public long getExpiration()
  {
    return this.delegate.getExpiration();
  }
  
  public String getHeaderField(int paramInt)
  {
    return this.delegate.getHeaderField(paramInt);
  }
  
  public String getHeaderField(String paramString)
  {
    return this.delegate.getHeaderField(paramString);
  }
  
  public long getHeaderFieldDate(String paramString, long paramLong)
  {
    return this.delegate.getHeaderFieldDate(paramString, paramLong);
  }
  
  public int getHeaderFieldInt(String paramString, int paramInt)
  {
    return this.delegate.getHeaderFieldInt(paramString, paramInt);
  }
  
  public String getHeaderFieldKey(int paramInt)
  {
    return this.delegate.getHeaderFieldKey(paramInt);
  }
  
  public Map<String, List<String>> getHeaderFields()
  {
    return this.delegate.getHeaderFields();
  }
  
  public HostnameVerifier getHostnameVerifier()
  {
    return this.delegate.client.getHostnameVerifier();
  }
  
  public HttpEngine getHttpEngine()
  {
    return this.delegate.getHttpEngine();
  }
  
  public long getIfModifiedSince()
  {
    return this.delegate.getIfModifiedSince();
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    return this.delegate.getInputStream();
  }
  
  public boolean getInstanceFollowRedirects()
  {
    return this.delegate.getInstanceFollowRedirects();
  }
  
  public long getLastModified()
  {
    return this.delegate.getLastModified();
  }
  
  public Certificate[] getLocalCertificates()
  {
    SecureCacheResponse localSecureCacheResponse = this.delegate.getSecureCacheResponse();
    Certificate[] arrayOfCertificate;
    if (localSecureCacheResponse != null)
    {
      List localList = localSecureCacheResponse.getLocalCertificateChain();
      arrayOfCertificate = null;
      if (localList != null) {
        arrayOfCertificate = (Certificate[])localList.toArray(new Certificate[localList.size()]);
      }
    }
    SSLSocket localSSLSocket;
    do
    {
      return arrayOfCertificate;
      localSSLSocket = getSslSocket();
      arrayOfCertificate = null;
    } while (localSSLSocket == null);
    return localSSLSocket.getSession().getLocalCertificates();
  }
  
  public Principal getLocalPrincipal()
  {
    SecureCacheResponse localSecureCacheResponse = this.delegate.getSecureCacheResponse();
    if (localSecureCacheResponse != null) {
      return localSecureCacheResponse.getLocalPrincipal();
    }
    SSLSocket localSSLSocket = getSslSocket();
    if (localSSLSocket != null) {
      return localSSLSocket.getSession().getLocalPrincipal();
    }
    return null;
  }
  
  public OutputStream getOutputStream()
    throws IOException
  {
    return this.delegate.getOutputStream();
  }
  
  public Principal getPeerPrincipal()
    throws SSLPeerUnverifiedException
  {
    SecureCacheResponse localSecureCacheResponse = this.delegate.getSecureCacheResponse();
    if (localSecureCacheResponse != null) {
      return localSecureCacheResponse.getPeerPrincipal();
    }
    SSLSocket localSSLSocket = getSslSocket();
    if (localSSLSocket != null) {
      return localSSLSocket.getSession().getPeerPrincipal();
    }
    return null;
  }
  
  public Permission getPermission()
    throws IOException
  {
    return this.delegate.getPermission();
  }
  
  public int getReadTimeout()
  {
    return this.delegate.getReadTimeout();
  }
  
  public String getRequestMethod()
  {
    return this.delegate.getRequestMethod();
  }
  
  public Map<String, List<String>> getRequestProperties()
  {
    return this.delegate.getRequestProperties();
  }
  
  public String getRequestProperty(String paramString)
  {
    return this.delegate.getRequestProperty(paramString);
  }
  
  public int getResponseCode()
    throws IOException
  {
    return this.delegate.getResponseCode();
  }
  
  public String getResponseMessage()
    throws IOException
  {
    return this.delegate.getResponseMessage();
  }
  
  public SSLSocketFactory getSSLSocketFactory()
  {
    return this.delegate.client.getSslSocketFactory();
  }
  
  public Certificate[] getServerCertificates()
    throws SSLPeerUnverifiedException
  {
    SecureCacheResponse localSecureCacheResponse = this.delegate.getSecureCacheResponse();
    Certificate[] arrayOfCertificate;
    if (localSecureCacheResponse != null)
    {
      List localList = localSecureCacheResponse.getServerCertificateChain();
      arrayOfCertificate = null;
      if (localList != null) {
        arrayOfCertificate = (Certificate[])localList.toArray(new Certificate[localList.size()]);
      }
    }
    SSLSocket localSSLSocket;
    do
    {
      return arrayOfCertificate;
      localSSLSocket = getSslSocket();
      arrayOfCertificate = null;
    } while (localSSLSocket == null);
    return localSSLSocket.getSession().getPeerCertificates();
  }
  
  public URL getURL()
  {
    return this.delegate.getURL();
  }
  
  public boolean getUseCaches()
  {
    return this.delegate.getUseCaches();
  }
  
  public void setAllowUserInteraction(boolean paramBoolean)
  {
    this.delegate.setAllowUserInteraction(paramBoolean);
  }
  
  public void setChunkedStreamingMode(int paramInt)
  {
    this.delegate.setChunkedStreamingMode(paramInt);
  }
  
  public void setConnectTimeout(int paramInt)
  {
    this.delegate.setConnectTimeout(paramInt);
  }
  
  public void setDefaultUseCaches(boolean paramBoolean)
  {
    this.delegate.setDefaultUseCaches(paramBoolean);
  }
  
  public void setDoInput(boolean paramBoolean)
  {
    this.delegate.setDoInput(paramBoolean);
  }
  
  public void setDoOutput(boolean paramBoolean)
  {
    this.delegate.setDoOutput(paramBoolean);
  }
  
  public void setFixedLengthStreamingMode(int paramInt)
  {
    this.delegate.setFixedLengthStreamingMode(paramInt);
  }
  
  public void setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
  {
    this.delegate.client.setHostnameVerifier(paramHostnameVerifier);
  }
  
  public void setIfModifiedSince(long paramLong)
  {
    this.delegate.setIfModifiedSince(paramLong);
  }
  
  public void setInstanceFollowRedirects(boolean paramBoolean)
  {
    this.delegate.setInstanceFollowRedirects(paramBoolean);
  }
  
  public void setReadTimeout(int paramInt)
  {
    this.delegate.setReadTimeout(paramInt);
  }
  
  public void setRequestMethod(String paramString)
    throws ProtocolException
  {
    this.delegate.setRequestMethod(paramString);
  }
  
  public void setRequestProperty(String paramString1, String paramString2)
  {
    this.delegate.setRequestProperty(paramString1, paramString2);
  }
  
  public void setSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory)
  {
    this.delegate.client.setSslSocketFactory(paramSSLSocketFactory);
  }
  
  public void setUseCaches(boolean paramBoolean)
  {
    this.delegate.setUseCaches(paramBoolean);
  }
  
  public String toString()
  {
    return this.delegate.toString();
  }
  
  public boolean usingProxy()
  {
    return this.delegate.usingProxy();
  }
  
  private final class HttpUrlConnectionDelegate
    extends HttpURLConnectionImpl
  {
    private HttpUrlConnectionDelegate(URL paramURL, OkHttpClient paramOkHttpClient)
    {
      super(paramOkHttpClient);
    }
    
    public HttpURLConnection getHttpConnectionToCache()
    {
      return HttpsURLConnectionImpl.this;
    }
    
    public SecureCacheResponse getSecureCacheResponse()
    {
      if ((this.httpEngine instanceof HttpsEngine)) {
        return (SecureCacheResponse)this.httpEngine.getCacheResponse();
      }
      return null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.http.HttpsURLConnectionImpl
 * JD-Core Version:    0.7.0.1
 */