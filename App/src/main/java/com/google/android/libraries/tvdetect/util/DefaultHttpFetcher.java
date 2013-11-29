package com.google.android.libraries.tvdetect.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class DefaultHttpFetcher
  implements HttpFetcher
{
  private static final Pattern HTTP_HEADER_KEY_VALUE_REGEXP = Pattern.compile("^(.+?):[ ]?(.+)$");
  private static final DefaultHttpFetcher INSTANCE = new DefaultHttpFetcher();
  private static DefaultHttpClient defaultClient;
  
  public static DefaultHttpFetcher create()
  {
    return INSTANCE;
  }
  
  private static DefaultHttpClient getThreadSafeClient()
  {
    try
    {
      if (defaultClient == null)
      {
        SSLSocketFactory localSSLSocketFactory = SSLSocketFactory.getSocketFactory();
        localSSLSocketFactory.setHostnameVerifier(new TolerantHostnameVerifier());
        defaultClient = new DefaultHttpClient();
        ClientConnectionManager localClientConnectionManager = defaultClient.getConnectionManager();
        localClientConnectionManager.getSchemeRegistry().register(new Scheme("https", localSSLSocketFactory, 443));
        localClientConnectionManager.getSchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        HttpParams localHttpParams = defaultClient.getParams();
        localHttpParams.setParameter("http.conn-manager.max-total", Integer.valueOf(30));
        HttpConnectionParams.setConnectionTimeout(localHttpParams, 3000);
        HttpConnectionParams.setSoTimeout(localHttpParams, 5000);
        defaultClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localHttpParams, localClientConnectionManager.getSchemeRegistry()), localHttpParams);
      }
      DefaultHttpClient localDefaultHttpClient = defaultClient;
      return localDefaultHttpClient;
    }
    finally {}
  }
  
  public static Map<String, String> parseHttpHeaders(String paramString)
  {
    localHashMap = new HashMap();
    Scanner localScanner = new Scanner(paramString);
    try
    {
      while (localScanner.hasNextLine())
      {
        String str = localScanner.nextLine();
        Matcher localMatcher = HTTP_HEADER_KEY_VALUE_REGEXP.matcher(str);
        if (localMatcher.matches()) {
          localHashMap.put(localMatcher.group(1).toUpperCase(Locale.US), localMatcher.group(2));
        }
      }
      return localHashMap;
    }
    finally
    {
      localScanner.close();
    }
  }
  
  public byte[] fetchUrl(String paramString)
    throws HttpFetcherException
  {
    DefaultHttpClient localDefaultHttpClient = getThreadSafeClient();
    HttpGet localHttpGet = new HttpGet(paramString);
    HttpResponse localHttpResponse;
    try
    {
      localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
      if (localHttpResponse.getStatusLine().getStatusCode() != 200) {
        throw new HttpFetcherException("HTTP status not OK");
      }
    }
    catch (IOException localIOException1)
    {
      throw new HttpFetcherException("Could not fetch URL");
    }
    HttpEntity localHttpEntity = localHttpResponse.getEntity();
    if (localHttpEntity == null) {
      throw new HttpFetcherException("HTTP response has no entity");
    }
    try
    {
      byte[] arrayOfByte = StreamUtil.streamToByteArray(localHttpEntity.getContent());
      return arrayOfByte;
    }
    catch (IOException localIOException2)
    {
      throw new HttpFetcherException("Could not convert HTTP content to bytes", localIOException2);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.DefaultHttpFetcher
 * JD-Core Version:    0.7.0.1
 */