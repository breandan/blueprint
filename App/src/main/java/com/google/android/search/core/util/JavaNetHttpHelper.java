package com.google.android.search.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.io.ByteStreams;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

public class JavaNetHttpHelper
  implements HttpHelper
{
  private static boolean sInitialized;
  private final ExecutorService mBgExecutor;
  private final SearchConfig mConfig;
  private final ConnectivityManager mConnectivityManager;
  private final Context mContext;
  private final Runnable mFlushTask = new Runnable()
  {
    public void run()
    {
      HttpResponseCache localHttpResponseCache = (HttpResponseCache)JavaNetHttpHelper.this.mSpdyClient.getResponseCache();
      if (localHttpResponseCache != null) {}
      try
      {
        localHttpResponseCache.flush();
        return;
      }
      catch (IOException localIOException)
      {
        Log.e("Search.JavaNetHttpHelper", "Error flushing cache.", localIOException);
      }
    }
  };
  private final OkHttpClient mNonSpdyClient;
  private final HttpHelper.UrlRewriter mRewriter;
  private final OkHttpClient mSpdyClient;
  private final Supplier<String> mUserAgent;
  
  public JavaNetHttpHelper(SearchConfig paramSearchConfig, HttpHelper.UrlRewriter paramUrlRewriter, Supplier<String> paramSupplier, ExecutorService paramExecutorService, Context paramContext)
  {
    this.mConfig = paramSearchConfig;
    this.mUserAgent = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    this.mRewriter = paramUrlRewriter;
    this.mBgExecutor = paramExecutorService;
    this.mContext = paramContext;
    this.mConnectivityManager = ((ConnectivityManager)Preconditions.checkNotNull(this.mContext.getSystemService("connectivity")));
    this.mSpdyClient = new OkHttpClient();
    this.mNonSpdyClient = new OkHttpClient();
  }
  
  public static Charset charsetOrDefault(String paramString)
  {
    if (paramString == null) {
      return DEFAULT_CHARSET;
    }
    try
    {
      if (!Charset.isSupported(paramString))
      {
        Log.w("Search.JavaNetHttpHelper", "Unsupported charset: " + paramString);
        Charset localCharset = DEFAULT_CHARSET;
        return localCharset;
      }
    }
    catch (IllegalCharsetNameException localIllegalCharsetNameException)
    {
      Log.w("Search.JavaNetHttpHelper", "Illegal charset name: " + paramString);
      return DEFAULT_CHARSET;
    }
    return Charset.forName(paramString);
  }
  
  private HttpURLConnection createConnection(String paramString, Map<String, String> paramMap, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException
  {
    if ((paramBoolean1) || (paramString.startsWith("https://"))) {
      maybeInitCacheAndSocketFactory(this.mConfig, this.mContext, this.mSpdyClient, this.mNonSpdyClient);
    }
    if (paramBoolean3) {
      paramString = this.mRewriter.rewrite(paramString);
    }
    HttpURLConnection localHttpURLConnection = createConnectionInternal(new URL(paramString), paramBoolean2);
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localHttpURLConnection.addRequestProperty((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    localHttpURLConnection.addRequestProperty("User-Agent", (String)this.mUserAgent.get());
    return localHttpURLConnection;
  }
  
  private static Charset extractCharset(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getContentType();
    if (str == null) {
      return DEFAULT_CHARSET;
    }
    int i = str.indexOf("charset=");
    if (i < 0) {
      return DEFAULT_CHARSET;
    }
    return charsetOrDefault(str.substring(i + "charset=".length()).trim());
  }
  
  private static void maybeInitCacheAndSocketFactory(SearchConfig paramSearchConfig, Context paramContext, OkHttpClient paramOkHttpClient1, OkHttpClient paramOkHttpClient2)
  {
    try
    {
      boolean bool = sInitialized;
      if (!bool) {}
      try
      {
        HttpResponseCache localHttpResponseCache1 = new HttpResponseCache(new File(paramContext.getCacheDir(), "http"), paramSearchConfig.getHttpCacheSize());
        localHttpResponseCache2 = localHttpResponseCache1;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Log.e("Search.JavaNetHttpHelper", "Failed to install HTTP cache", localIOException);
          HttpResponseCache localHttpResponseCache2 = null;
        }
      }
      if (localHttpResponseCache2 != null)
      {
        paramOkHttpClient1.setResponseCache(localHttpResponseCache2);
        paramOkHttpClient2.setResponseCache(localHttpResponseCache2);
      }
      paramOkHttpClient1.setSslSocketFactory(new VelvetSslSocketFactory(paramSearchConfig.getHttpConnectTimeout(), paramContext, paramSearchConfig, true));
      paramOkHttpClient2.setSslSocketFactory(new VelvetSslSocketFactory(paramSearchConfig.getHttpConnectTimeout(), paramContext, paramSearchConfig, false));
      sInitialized = true;
      return;
    }
    finally {}
  }
  
  public static MimeTypeAndCharSet parseContentTypeHeader(String paramString)
  {
    String str1 = "";
    Object localObject = "";
    try
    {
      BasicHeader localBasicHeader = new BasicHeader("Content-Type", paramString);
      if (localBasicHeader.getElements().length > 0)
      {
        HeaderElement localHeaderElement = localBasicHeader.getElements()[0];
        str1 = localHeaderElement.getName();
        NameValuePair localNameValuePair = localHeaderElement.getParameterByName("charset");
        if (localNameValuePair != null)
        {
          String str2 = localNameValuePair.getValue();
          localObject = str2;
        }
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Log.w("Search.JavaNetHttpHelper", "Missing content type header");
      }
    }
    return new MimeTypeAndCharSet(str1, (String)localObject);
  }
  
  @Nullable
  private <T> T post(String paramString, Map<String, String> paramMap, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean1, HttpResponseFetcher<T> paramHttpResponseFetcher, boolean paramBoolean2)
    throws IOException
  {
    boolean bool = true;
    if (!haveNetworkConnection()) {
      return null;
    }
    if (paramMap == null) {
      paramMap = new HashMap();
    }
    int i;
    HttpURLConnection localHttpURLConnection;
    if (paramArrayOfByte == null)
    {
      i = 0;
      paramMap.put("Content-Length", Integer.toString(i));
      localHttpURLConnection = createConnection(paramString, paramMap, bool, paramBoolean1, paramBoolean2);
      if (paramArrayOfByte == null) {
        break label97;
      }
    }
    for (;;)
    {
      localHttpURLConnection.setDoOutput(bool);
      localHttpURLConnection.setRequestMethod("POST");
      return paramHttpResponseFetcher.fetchResponse(localHttpURLConnection, paramArrayOfByte, paramInt);
      i = paramArrayOfByte.length;
      break;
      label97:
      bool = false;
    }
  }
  
  private static byte[] toByteArray(InputStream paramInputStream, int paramInt)
  {
    Object localObject1 = null;
    if (paramInt < 0) {}
    for (;;)
    {
      try
      {
        byte[] arrayOfByte = ByteStreams.toByteArray(paramInputStream);
        localObject1 = arrayOfByte;
        if (paramInputStream == null) {}
      }
      finally
      {
        if (paramInputStream != null) {
          paramInputStream.close();
        }
      }
      try
      {
        paramInputStream.close();
        return localObject1;
      }
      catch (IOException localIOException)
      {
        Log.e("Search.JavaNetHttpHelper", "Failed to read byte response from InputStream.");
      }
      localObject1 = new byte[paramInt];
      ByteStreams.readFully(paramInputStream, (byte[])localObject1);
    }
    return localObject1;
  }
  
  HttpURLConnection createConnectionInternal(URL paramURL, boolean paramBoolean)
    throws IOException
  {
    int i = this.mConfig.getHttpConnectTimeout();
    int j = this.mConfig.getHttpReadTimeout();
    if ((paramBoolean) && (this.mConfig.isSpdyEnabled()) && ("https".equals(paramURL.getProtocol()))) {}
    for (OkHttpClient localOkHttpClient = this.mSpdyClient;; localOkHttpClient = this.mNonSpdyClient)
    {
      HttpURLConnection localHttpURLConnection = localOkHttpClient.open(paramURL);
      if (i != 0) {
        localHttpURLConnection.setConnectTimeout(i);
      }
      if (j != 0) {
        localHttpURLConnection.setReadTimeout(j);
      }
      return localHttpURLConnection;
    }
  }
  
  public <T> T get(HttpHelper.GetRequest paramGetRequest, int paramInt, HttpResponseFetcher<T> paramHttpResponseFetcher)
    throws IOException
  {
    HttpURLConnection localHttpURLConnection = createConnection(paramGetRequest.getUrl(), paramGetRequest.getHeaders(), paramGetRequest.getUseCaches(), paramGetRequest.getUseSpdy(), paramGetRequest.getRewriteUrl());
    localHttpURLConnection.setRequestMethod("GET");
    localHttpURLConnection.setInstanceFollowRedirects(paramGetRequest.getFollowRedirects());
    localHttpURLConnection.setUseCaches(paramGetRequest.getUseCaches());
    if ((!haveNetworkConnection()) && (paramGetRequest.getUseCaches()))
    {
      localHttpURLConnection.addRequestProperty("Cache-Control", "only-if-cached");
      int i = paramGetRequest.getMaxStaleSecs();
      if (i <= 0) {
        break label126;
      }
      localHttpURLConnection.addRequestProperty("Cache-Control", "max-stale=" + i);
    }
    for (;;)
    {
      return paramHttpResponseFetcher.fetchResponse(localHttpURLConnection, null, paramInt);
      label126:
      localHttpURLConnection.addRequestProperty("Cache-Control", "max-stale");
    }
  }
  
  public String get(HttpHelper.GetRequest paramGetRequest, int paramInt)
    throws IOException
  {
    return (String)get(paramGetRequest, paramInt, new StringFetcher(null));
  }
  
  public boolean haveNetworkConnection()
  {
    NetworkInfo localNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
    return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
  }
  
  @Nullable
  public String post(HttpHelper.PostRequest paramPostRequest, int paramInt)
    throws IOException
  {
    return (String)post(paramPostRequest.getUrl(), paramPostRequest.getHeaders(), paramPostRequest.getContent(), paramInt, paramPostRequest.getUseSpdy(), new StringFetcher(null), paramPostRequest.getRewriteUrl());
  }
  
  public byte[] rawGet(HttpHelper.GetRequest paramGetRequest, int paramInt)
    throws IOException
  {
    return (byte[])get(paramGetRequest, paramInt, new ByteArrayFetcher(null));
  }
  
  public ByteArrayWithHeadersResponse rawGetWithHeaders(HttpHelper.GetRequest paramGetRequest, int paramInt)
    throws IOException
  {
    return (ByteArrayWithHeadersResponse)get(paramGetRequest, paramInt, new ByteArrayWithHeadersFetcher(null));
  }
  
  @Nullable
  public byte[] rawPost(HttpHelper.PostRequest paramPostRequest, int paramInt)
    throws IOException
  {
    return (byte[])post(paramPostRequest.getUrl(), paramPostRequest.getHeaders(), paramPostRequest.getContent(), paramInt, paramPostRequest.getUseSpdy(), new ByteArrayFetcher(null), paramPostRequest.getRewriteUrl());
  }
  
  public void scheduleCacheFlush()
  {
    this.mBgExecutor.execute(this.mFlushTask);
  }
  
  private static class ByteArrayFetcher
    extends HttpResponseFetcher<byte[]>
  {
    public byte[] fetchResponse(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
      throws IOException
    {
      try
      {
        TrafficStats.setThreadStatsTag(paramInt);
        sendRequest(paramHttpURLConnection, paramArrayOfByte);
        int i = paramHttpURLConnection.getContentLength();
        byte[] arrayOfByte = JavaNetHttpHelper.toByteArray(paramHttpURLConnection.getInputStream(), i);
        return arrayOfByte;
      }
      finally
      {
        TrafficStats.clearThreadStatsTag();
        paramHttpURLConnection.disconnect();
      }
    }
  }
  
  private static class ByteArrayWithHeadersFetcher
    extends HttpResponseFetcher<ByteArrayWithHeadersResponse>
  {
    public ByteArrayWithHeadersResponse fetchResponse(HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
      throws IOException
    {
      try
      {
        TrafficStats.setThreadStatsTag(paramInt);
        sendRequest(paramHttpURLConnection, paramArrayOfByte);
        int i = paramHttpURLConnection.getContentLength();
        ByteArrayWithHeadersResponse localByteArrayWithHeadersResponse = new ByteArrayWithHeadersResponse(JavaNetHttpHelper.toByteArray(paramHttpURLConnection.getInputStream(), i), paramHttpURLConnection.getHeaderFields());
        return localByteArrayWithHeadersResponse;
      }
      finally
      {
        TrafficStats.clearThreadStatsTag();
        paramHttpURLConnection.disconnect();
      }
    }
  }
  
  public static final class MimeTypeAndCharSet
  {
    @Nonnull
    public final String mCharset;
    @Nonnull
    public final String mMimeType;
    
    public MimeTypeAndCharSet(String paramString1, String paramString2)
    {
      this.mMimeType = ((String)Preconditions.checkNotNull(paramString1));
      this.mCharset = ((String)Preconditions.checkNotNull(paramString2));
    }
    
    public String toString()
    {
      return "{mimeType=" + this.mMimeType + ", charset=" + this.mCharset + "}";
    }
  }
  
  private static class StringFetcher
    extends HttpResponseFetcher<String>
  {
    /* Error */
    public String fetchResponse(@Nonnull HttpURLConnection paramHttpURLConnection, byte[] paramArrayOfByte, int paramInt)
      throws IOException
    {
      // Byte code:
      //   0: iload_3
      //   1: invokestatic 25	android/net/TrafficStats:setThreadStatsTag	(I)V
      //   4: aload_0
      //   5: aload_1
      //   6: aload_2
      //   7: invokevirtual 29	com/google/android/search/core/util/JavaNetHttpHelper$StringFetcher:sendRequest	(Ljava/net/HttpURLConnection;[B)V
      //   10: aload_1
      //   11: invokestatic 35	com/google/android/search/core/util/JavaNetHttpHelper:access$400	(Ljava/net/HttpURLConnection;)Ljava/nio/charset/Charset;
      //   14: astore 5
      //   16: new 37	java/io/ByteArrayOutputStream
      //   19: dup
      //   20: sipush 512
      //   23: sipush 8192
      //   26: iconst_2
      //   27: aload_1
      //   28: invokevirtual 43	java/net/HttpURLConnection:getContentLength	()I
      //   31: imul
      //   32: invokestatic 49	java/lang/Math:min	(II)I
      //   35: invokestatic 52	java/lang/Math:max	(II)I
      //   38: invokespecial 54	java/io/ByteArrayOutputStream:<init>	(I)V
      //   41: astore 6
      //   43: aconst_null
      //   44: astore 7
      //   46: aload_1
      //   47: invokevirtual 58	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
      //   50: astore 7
      //   52: aload 7
      //   54: aload 6
      //   56: invokestatic 64	com/google/common/io/ByteStreams:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)J
      //   59: pop2
      //   60: aload 7
      //   62: ifnull +8 -> 70
      //   65: aload 7
      //   67: invokevirtual 69	java/io/InputStream:close	()V
      //   70: invokestatic 72	android/net/TrafficStats:clearThreadStatsTag	()V
      //   73: aload_1
      //   74: invokevirtual 75	java/net/HttpURLConnection:disconnect	()V
      //   77: aload 6
      //   79: aload 5
      //   81: invokevirtual 81	java/nio/charset/Charset:name	()Ljava/lang/String;
      //   84: invokevirtual 85	java/io/ByteArrayOutputStream:toString	(Ljava/lang/String;)Ljava/lang/String;
      //   87: areturn
      //   88: astore 8
      //   90: aload 7
      //   92: ifnull +8 -> 100
      //   95: aload 7
      //   97: invokevirtual 69	java/io/InputStream:close	()V
      //   100: aload 8
      //   102: athrow
      //   103: astore 4
      //   105: invokestatic 72	android/net/TrafficStats:clearThreadStatsTag	()V
      //   108: aload_1
      //   109: invokevirtual 75	java/net/HttpURLConnection:disconnect	()V
      //   112: aload 4
      //   114: athrow
      //   115: astore 4
      //   117: goto -12 -> 105
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	120	0	this	StringFetcher
      //   0	120	1	paramHttpURLConnection	HttpURLConnection
      //   0	120	2	paramArrayOfByte	byte[]
      //   0	120	3	paramInt	int
      //   103	10	4	localObject1	Object
      //   115	1	4	localObject2	Object
      //   14	66	5	localCharset	Charset
      //   41	37	6	localByteArrayOutputStream	java.io.ByteArrayOutputStream
      //   44	52	7	localInputStream	InputStream
      //   88	13	8	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   46	60	88	finally
      //   65	70	103	finally
      //   95	100	103	finally
      //   100	103	103	finally
      //   0	43	115	finally
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.JavaNetHttpHelper
 * JD-Core Version:    0.7.0.1
 */