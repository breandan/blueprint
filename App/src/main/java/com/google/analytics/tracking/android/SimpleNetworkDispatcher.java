package com.google.analytics.tracking.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

class SimpleNetworkDispatcher
  implements Dispatcher
{
  private final Context ctx;
  private final HttpClientFactory httpClientFactory;
  private final String userAgent;
  
  SimpleNetworkDispatcher(AnalyticsStore paramAnalyticsStore, HttpClientFactory paramHttpClientFactory, Context paramContext)
  {
    this(paramHttpClientFactory, paramContext);
  }
  
  SimpleNetworkDispatcher(HttpClientFactory paramHttpClientFactory, Context paramContext)
  {
    this.ctx = paramContext.getApplicationContext();
    this.userAgent = createUserAgentString("GoogleAnalytics", "2.0", Build.VERSION.RELEASE, Utils.getLanguage(Locale.getDefault()), Build.MODEL, Build.ID);
    this.httpClientFactory = paramHttpClientFactory;
  }
  
  private HttpEntityEnclosingRequest buildRequest(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      Log.w("Empty hit, discarding.");
      return null;
    }
    String str = paramString2 + "?" + paramString1;
    BasicHttpEntityEnclosingRequest localBasicHttpEntityEnclosingRequest;
    if (str.length() < 2036) {
      localBasicHttpEntityEnclosingRequest = new BasicHttpEntityEnclosingRequest("GET", str);
    }
    for (;;)
    {
      localBasicHttpEntityEnclosingRequest.addHeader("User-Agent", this.userAgent);
      return localBasicHttpEntityEnclosingRequest;
      localBasicHttpEntityEnclosingRequest = new BasicHttpEntityEnclosingRequest("POST", paramString2);
      try
      {
        localBasicHttpEntityEnclosingRequest.setEntity(new StringEntity(paramString1));
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Log.w("Encoding error, discarding hit");
      }
    }
    return null;
  }
  
  private URL getUrl(Hit paramHit)
  {
    if (TextUtils.isEmpty(paramHit.getHitUrl())) {
      return null;
    }
    try
    {
      URL localURL1 = new URL(paramHit.getHitUrl());
      return localURL1;
    }
    catch (MalformedURLException localMalformedURLException1)
    {
      try
      {
        URL localURL2 = new URL("http://www.google-analytics.com/collect");
        return localURL2;
      }
      catch (MalformedURLException localMalformedURLException2) {}
    }
    return null;
  }
  
  private void logDebugInformation(boolean paramBoolean, HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
  {
    StringBuffer localStringBuffer;
    if (paramBoolean)
    {
      localStringBuffer = new StringBuffer();
      Header[] arrayOfHeader = paramHttpEntityEnclosingRequest.getAllHeaders();
      int i = arrayOfHeader.length;
      for (int j = 0; j < i; j++) {
        localStringBuffer.append(arrayOfHeader[j].toString()).append("\n");
      }
      localStringBuffer.append(paramHttpEntityEnclosingRequest.getRequestLine().toString()).append("\n");
      if (paramHttpEntityEnclosingRequest.getEntity() == null) {}
    }
    try
    {
      InputStream localInputStream = paramHttpEntityEnclosingRequest.getEntity().getContent();
      if (localInputStream != null)
      {
        int k = localInputStream.available();
        if (k > 0)
        {
          byte[] arrayOfByte = new byte[k];
          localInputStream.read(arrayOfByte);
          localStringBuffer.append("POST:\n");
          localStringBuffer.append(new String(arrayOfByte)).append("\n");
        }
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.w("Error Writing hit to log...");
      }
    }
    Log.i(localStringBuffer.toString());
  }
  
  String createUserAgentString(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", new Object[] { paramString1, paramString2, paramString3, paramString4, paramString5, paramString6 });
  }
  
  public int dispatchHits(List<Hit> paramList)
  {
    int i = 0;
    int j = Math.min(paramList.size(), 40);
    int k = 0;
    HttpClient localHttpClient;
    HttpHost localHttpHost;
    HttpEntityEnclosingRequest localHttpEntityEnclosingRequest;
    if (k < j)
    {
      localHttpClient = this.httpClientFactory.newInstance();
      Hit localHit = (Hit)paramList.get(k);
      URL localURL = getUrl(localHit);
      if (localURL == null)
      {
        if (Log.isDebugEnabled()) {
          Log.w("No destination: discarding hit: " + localHit.getHitParams());
        }
        for (;;)
        {
          i++;
          k++;
          break;
          Log.w("No destination: discarding hit.");
        }
      }
      localHttpHost = new HttpHost(localURL.getHost(), localURL.getPort(), localURL.getProtocol());
      String str1 = localURL.getPath();
      if (TextUtils.isEmpty(localHit.getHitParams())) {}
      for (String str2 = "";; str2 = HitBuilder.postProcessHit(localHit, System.currentTimeMillis()))
      {
        localHttpEntityEnclosingRequest = buildRequest(str2, str1);
        if (localHttpEntityEnclosingRequest != null) {
          break label192;
        }
        i++;
        break;
      }
      label192:
      localHttpEntityEnclosingRequest.addHeader("Host", localHttpHost.toHostString());
      logDebugInformation(Log.isDebugEnabled(), localHttpEntityEnclosingRequest);
      if (str2.length() > 8192) {
        Log.w("Hit too long (> 8192 bytes)--not sent");
      }
    }
    for (;;)
    {
      i++;
      break;
      try
      {
        HttpResponse localHttpResponse = localHttpClient.execute(localHttpHost, localHttpEntityEnclosingRequest);
        if (localHttpResponse.getStatusLine().getStatusCode() != 200)
        {
          Log.w("Bad response: " + localHttpResponse.getStatusLine().getStatusCode());
          return i;
        }
      }
      catch (ClientProtocolException localClientProtocolException)
      {
        Log.w("ClientProtocolException sending hit; discarding hit...");
      }
      catch (IOException localIOException)
      {
        Log.w("Exception sending hit: " + localIOException.getClass().getSimpleName());
        Log.w(localIOException.getMessage());
      }
    }
    return i;
  }
  
  public boolean okToDispatch()
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)this.ctx.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()))
    {
      Log.vDebug("...no network connectivity");
      return false;
    }
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.SimpleNetworkDispatcher
 * JD-Core Version:    0.7.0.1
 */