package com.google.android.search.core.prefetch;

import android.util.Log;
import android.webkit.WebResourceResponse;
import com.google.android.search.core.util.JavaNetHttpHelper;
import com.google.android.search.core.util.JavaNetHttpHelper.MimeTypeAndCharSet;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;

public class WebPage
{
  public static final Charset HTML_CHARSET = Util.UTF_8;
  public static final Charset JSON_CHARSET = Util.UTF_8;
  @Nonnull
  public final InputStream mContentStream;
  private final AtomicBoolean mContentStreamUsed = new AtomicBoolean(false);
  @Nonnull
  public final JavaNetHttpHelper.MimeTypeAndCharSet mContentType;
  @Nonnull
  public final Map<String, List<String>> mHeaders;
  
  public WebPage(@Nonnull Map<String, List<String>> paramMap, @Nonnull InputStream paramInputStream)
  {
    this.mHeaders = ((Map)Preconditions.checkNotNull(paramMap));
    this.mContentStream = ((InputStream)Preconditions.checkNotNull(paramInputStream));
    this.mContentType = contentTypeAfterStrippingJson(paramMap);
  }
  
  @Nonnull
  static JavaNetHttpHelper.MimeTypeAndCharSet contentTypeAfterStrippingJson(Map<String, List<String>> paramMap)
  {
    JavaNetHttpHelper.MimeTypeAndCharSet localMimeTypeAndCharSet = getContentType(paramMap);
    String str1 = localMimeTypeAndCharSet.mMimeType;
    Object localObject = localMimeTypeAndCharSet.mCharset;
    for (;;)
    {
      try
      {
        if (!"application/json".equals(str1)) {
          continue;
        }
        if (!JSON_CHARSET.equals(Charset.forName((String)localObject)))
        {
          Log.e("Velvet.WebPage", "Expected charset " + JSON_CHARSET + " but received " + (String)localObject);
          return getDefaultContentType();
        }
        String str3 = HTML_CHARSET.name();
        localObject = str3;
      }
      catch (IllegalCharsetNameException localIllegalCharsetNameException)
      {
        Log.e("Velvet.WebPage", "Unknown charset", localIllegalCharsetNameException);
        return getDefaultContentType();
        Log.w("Velvet.WebPage", "Unexpected response MIME type: " + str1);
        String str2 = HTML_CHARSET.name();
        localObject = str2;
        continue;
      }
      catch (UnsupportedCharsetException localUnsupportedCharsetException)
      {
        Log.e("Velvet.WebPage", "Unsupported charset", localUnsupportedCharsetException);
      }
      return new JavaNetHttpHelper.MimeTypeAndCharSet("text/html", (String)localObject);
      if (!"text/html".equals(str1)) {
        continue;
      }
      Charset.forName((String)localObject);
    }
    return getDefaultContentType();
  }
  
  static JavaNetHttpHelper.MimeTypeAndCharSet getContentType(Map<String, List<String>> paramMap)
  {
    List localList = (List)paramMap.get("Content-Type");
    if ((localList != null) && (!localList.isEmpty())) {
      return JavaNetHttpHelper.parseContentTypeHeader((String)localList.get(0));
    }
    return getDefaultContentType();
  }
  
  private static JavaNetHttpHelper.MimeTypeAndCharSet getDefaultContentType()
  {
    return new JavaNetHttpHelper.MimeTypeAndCharSet("text/html", "UTF-8");
  }
  
  public Map<String, List<String>> getHeaders()
  {
    return this.mHeaders;
  }
  
  public String toString()
  {
    return "WebPage{" + this.mContentType + "}";
  }
  
  public WebResourceResponse toWebResourceResponse()
  {
    if (!this.mContentStreamUsed.compareAndSet(false, true)) {}
    try
    {
      this.mContentStream.reset();
      return new WebResourceResponse(this.mContentType.mMimeType, this.mContentType.mCharset, this.mContentStream);
    }
    catch (IOException localIOException)
    {
      Log.e("Velvet.WebPage", "Could not reset input stream", localIOException);
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.WebPage
 * JD-Core Version:    0.7.0.1
 */