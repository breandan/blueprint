package com.google.android.search.core.util;

import com.google.android.shared.util.Util;
import com.google.common.base.Objects;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public abstract interface HttpHelper
{
  public static final Charset DEFAULT_CHARSET = Util.UTF_8;
  
  public abstract <T> T get(GetRequest paramGetRequest, int paramInt, HttpResponseFetcher<T> paramHttpResponseFetcher)
    throws IOException;
  
  public abstract String get(GetRequest paramGetRequest, int paramInt)
    throws IOException, HttpHelper.HttpException;
  
  public abstract boolean haveNetworkConnection();
  
  public abstract String post(PostRequest paramPostRequest, int paramInt)
    throws IOException, HttpHelper.HttpException;
  
  public abstract byte[] rawGet(GetRequest paramGetRequest, int paramInt)
    throws IOException, HttpHelper.HttpException;
  
  public abstract ByteArrayWithHeadersResponse rawGetWithHeaders(GetRequest paramGetRequest, int paramInt)
    throws IOException, HttpHelper.HttpException;
  
  public abstract byte[] rawPost(PostRequest paramPostRequest, int paramInt)
    throws IOException, HttpHelper.HttpException;
  
  public abstract void scheduleCacheFlush();
  
  public static class GetRequest
  {
    private boolean mFollowRedirects = true;
    private Map<String, String> mHeaders;
    private int mMaxStaleSecs;
    private boolean mRewriteUrl = true;
    private String mUrl;
    private boolean mUseCaches = true;
    private boolean mUsePellets;
    private boolean mUseSpdy;
    
    public GetRequest() {}
    
    public GetRequest(String paramString)
    {
      this.mUrl = paramString;
    }
    
    public GetRequest(String paramString, Map<String, String> paramMap)
    {
      this(paramString);
      this.mHeaders = paramMap;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof GetRequest;
      boolean bool2 = false;
      if (bool1)
      {
        GetRequest localGetRequest = (GetRequest)paramObject;
        boolean bool3 = Objects.equal(this.mUrl, localGetRequest.mUrl);
        bool2 = false;
        if (bool3)
        {
          boolean bool4 = Objects.equal(this.mHeaders, localGetRequest.mHeaders);
          bool2 = false;
          if (bool4) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    
    public boolean getFollowRedirects()
    {
      return this.mFollowRedirects;
    }
    
    public Map<String, String> getHeaders()
    {
      return this.mHeaders;
    }
    
    public int getMaxStaleSecs()
    {
      return this.mMaxStaleSecs;
    }
    
    public boolean getRewriteUrl()
    {
      return this.mRewriteUrl;
    }
    
    public String getUrl()
    {
      return this.mUrl;
    }
    
    public boolean getUseCaches()
    {
      return this.mUseCaches;
    }
    
    public boolean getUsePellets()
    {
      return this.mUsePellets;
    }
    
    public boolean getUseSpdy()
    {
      return this.mUseSpdy;
    }
    
    public void setFollowRedirects(boolean paramBoolean)
    {
      this.mFollowRedirects = paramBoolean;
    }
    
    public void setHeader(String paramString1, String paramString2)
    {
      if (this.mHeaders == null) {
        this.mHeaders = new HashMap();
      }
      this.mHeaders.put(paramString1, paramString2);
    }
    
    public void setMaxStaleSecs(int paramInt)
    {
      this.mMaxStaleSecs = paramInt;
    }
    
    public void setRewriteUrl(boolean paramBoolean)
    {
      this.mRewriteUrl = paramBoolean;
    }
    
    public void setUseCaches(boolean paramBoolean)
    {
      this.mUseCaches = paramBoolean;
    }
    
    public void setUsePellets(boolean paramBoolean)
    {
      this.mUsePellets = paramBoolean;
    }
    
    public void setUseSpdy(boolean paramBoolean)
    {
      this.mUseSpdy = paramBoolean;
    }
    
    public String toString()
    {
      return "GetRequest{" + this.mUrl + "}";
    }
  }
  
  public static class HttpException
    extends IOException
  {
    private final int mStatusCode;
    
    public HttpException(int paramInt, String paramString)
    {
      super();
      this.mStatusCode = paramInt;
    }
    
    public int getStatusCode()
    {
      return this.mStatusCode;
    }
  }
  
  public static class HttpRedirectException
    extends HttpHelper.HttpException
  {
    private final String mLocation;
    
    public HttpRedirectException(int paramInt, String paramString1, String paramString2)
    {
      super(paramString1);
      this.mLocation = paramString2;
    }
    
    public String getRedirectLocation()
    {
      return this.mLocation;
    }
  }
  
  public static class PostRequest
    extends HttpHelper.GetRequest
  {
    private byte[] mContent;
    
    public PostRequest(String paramString)
    {
      super();
    }
    
    public PostRequest(String paramString, Map<String, String> paramMap)
    {
      super(paramMap);
    }
    
    public byte[] getContent()
    {
      return this.mContent;
    }
    
    public void setContent(String paramString)
    {
      if (paramString == null) {}
      for (byte[] arrayOfByte = null;; arrayOfByte = paramString.getBytes(HttpHelper.DEFAULT_CHARSET))
      {
        this.mContent = arrayOfByte;
        return;
      }
    }
    
    public void setContent(byte[] paramArrayOfByte)
    {
      this.mContent = paramArrayOfByte;
    }
  }
  
  public static abstract interface UrlRewriter
  {
    public abstract String rewrite(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.HttpHelper
 * JD-Core Version:    0.7.0.1
 */