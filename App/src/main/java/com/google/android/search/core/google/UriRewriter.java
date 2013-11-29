package com.google.android.search.core.google;

import android.content.ContentResolver;
import android.content.Context;
import com.google.android.common.http.UrlRules;
import com.google.android.common.http.UrlRules.Rule;
import com.google.android.search.core.util.HttpHelper.UrlRewriter;

public class UriRewriter
  implements HttpHelper.UrlRewriter
{
  private final ContentResolver mResolver;
  
  public UriRewriter(Context paramContext)
  {
    this.mResolver = paramContext.getContentResolver();
  }
  
  public String rewrite(String paramString)
  {
    return UrlRules.getRules(this.mResolver).matchRule(paramString).apply(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.UriRewriter
 * JD-Core Version:    0.7.0.1
 */