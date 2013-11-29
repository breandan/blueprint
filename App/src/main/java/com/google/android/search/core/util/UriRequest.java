package com.google.android.search.core.util;

import android.net.Uri;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public final class UriRequest
{
  private final Map<String, String> mHeaders;
  private final Map<String, String> mUniqueParams;
  private final Uri mUri;
  
  public UriRequest(Uri paramUri)
  {
    this(paramUri, null, null);
  }
  
  public UriRequest(Uri paramUri, Map<String, String> paramMap)
  {
    this(paramUri, null, paramMap);
  }
  
  public UriRequest(Uri paramUri, Map<String, String> paramMap1, Map<String, String> paramMap2)
  {
    this.mUri = paramUri;
    if (paramMap1 == null) {}
    for (Map localMap = null;; localMap = Collections.unmodifiableMap(paramMap1))
    {
      this.mUniqueParams = localMap;
      if (paramMap2 == null) {
        paramMap2 = Collections.emptyMap();
      }
      this.mHeaders = Collections.unmodifiableMap(paramMap2);
      return;
    }
  }
  
  public HttpHelper.GetRequest asGetRequest()
  {
    return new HttpHelper.GetRequest(this.mUri.toString(), this.mHeaders);
  }
  
  public boolean equals(@Nullable Object paramObject)
  {
    boolean bool1 = paramObject instanceof UriRequest;
    boolean bool2 = false;
    if (bool1)
    {
      UriRequest localUriRequest = (UriRequest)paramObject;
      boolean bool3 = Objects.equal(this.mUri, localUriRequest.mUri);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = Objects.equal(this.mHeaders, localUriRequest.mHeaders);
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = Objects.equal(this.mUniqueParams, localUriRequest.mUniqueParams);
          bool2 = false;
          if (bool5) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  public Map<String, String> getHeaders()
  {
    return this.mHeaders;
  }
  
  public Map<String, String> getHeadersCopy()
  {
    return Maps.newHashMap(this.mHeaders);
  }
  
  public Map<String, String> getUniqueParams()
  {
    if (this.mUniqueParams != null) {
      return this.mUniqueParams;
    }
    return SearchUrlHelper.getAllQueryParameters(this.mUri);
  }
  
  public Uri getUri()
  {
    return this.mUri;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = this.mUri;
    arrayOfObject[1] = this.mHeaders;
    arrayOfObject[2] = this.mUniqueParams;
    return Objects.hashCode(arrayOfObject);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.mUri.toString());
    localStringBuilder.append(" Headers[");
    Iterator localIterator = this.mHeaders.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localStringBuilder.append((String)localEntry.getKey());
      localStringBuilder.append(":");
      localStringBuilder.append((String)localEntry.getValue());
      localStringBuilder.append("; ");
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.UriRequest
 * JD-Core Version:    0.7.0.1
 */